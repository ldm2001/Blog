import axios from 'axios';
import useLoginUserStore from 'stores/login-user.store';

export const DOMAIN = 'http://localhost:4000';
export const API_DOMAIN = `${DOMAIN}/api/v1`;

// 인증이 필요한 API용 axios 인스턴스
export const authAxios = axios.create({
  baseURL: API_DOMAIN,
  withCredentials: true,
});

// 갱신 중복 방지 플래그
let isRefreshing = false;
let refreshSubscribers: ((token: string) => void)[] = [];

const onRefreshed = (token: string) => {
  refreshSubscribers.forEach(callback => callback(token));
  refreshSubscribers = [];
};

const addRefreshSubscriber = (callback: (token: string) => void) => {
  refreshSubscribers.push(callback);
};

// 요청 interceptor: 메모리의 accessToken을 Authorization 헤더에 자동 주입
authAxios.interceptors.request.use(config => {
  const token = useLoginUserStore.getState().accessToken;
  if (token) {
    config.headers.Authorization = `Bearer ${token}`;
  }
  return config;
});

// 응답 interceptor: 401 시 refresh 시도 후 원래 요청 재시도
authAxios.interceptors.response.use(
  response => response,
  async error => {
    const originalRequest = error.config;

    // refresh 엔드포인트 자체의 401은 재시도하지 않음
    if (originalRequest.url?.includes('/auth/refresh')) {
      return Promise.reject(error);
    }

    if (error.response?.status === 401 && !originalRequest._retry) {
      originalRequest._retry = true;

      if (isRefreshing) {
        // 이미 갱신 중이면 대기열에 추가
        return new Promise(resolve => {
          addRefreshSubscriber((token: string) => {
            originalRequest.headers.Authorization = `Bearer ${token}`;
            resolve(authAxios(originalRequest));
          });
        });
      }

      isRefreshing = true;

      try {
        const response = await axios.post(`${API_DOMAIN}/auth/refresh`, {}, { withCredentials: true });
        const { token } = response.data;

        useLoginUserStore.getState().setAccessToken(token);
        originalRequest.headers.Authorization = `Bearer ${token}`;

        onRefreshed(token);
        return authAxios(originalRequest);
      } catch {
        useLoginUserStore.getState().resetLoginUser();
        return Promise.reject(error);
      } finally {
        isRefreshing = false;
      }
    }

    return Promise.reject(error);
  }
);

// 하위 호환용 (기존 코드에서 아직 참조할 수 있으므로)
export const authorization = (accessToken: string) => {
  return { headers: { Authorization: `Bearer ${accessToken}` } };
};
