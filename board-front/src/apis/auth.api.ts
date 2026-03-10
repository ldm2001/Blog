import axios from 'axios';

import { SignInRequestDto, SignUpRequestDto } from './request/auth';
import { ResponseDto } from './response';
import { RefreshTokenResponseDto, SignInResponseDto, SignUpResponseDto } from './response/auth';
import { API_DOMAIN } from './common';

const SIGN_IN_URL = () => `${API_DOMAIN}/auth/sign-in`;
const SIGN_UP_URL = () => `${API_DOMAIN}/auth/sign-up`;
const REFRESH_TOKEN_URL = () => `${API_DOMAIN}/auth/refresh`;
const SIGN_OUT_URL = () => `${API_DOMAIN}/auth/sign-out`;
const AUTH_COOKIE_CONFIG = { withCredentials: true };

export const signInRequest = async (requestBody: SignInRequestDto) => {
  const result = await axios.post(SIGN_IN_URL(), requestBody, AUTH_COOKIE_CONFIG)
    .then(response => {
      const responseBody: SignInResponseDto = response.data;
      return responseBody;
    })
    .catch(error => {
      if (!error.response) return null;
      const responseBody: ResponseDto = error.response.data;
      return responseBody;
    });
  return result;
};

export const signUpRequest = async (requestBody: SignUpRequestDto) => {
  const result = await axios.post(SIGN_UP_URL(), requestBody)
    .then(response => {
      const responseBody: SignUpResponseDto = response.data;
      return responseBody;
    })
    .catch(error => {
      if (!error.response) return null;
      const responseBody: ResponseDto = error.response.data;
      return responseBody;
    });
  return result;
};

export const refreshTokenRequest = async () => {
  const result = await axios.post(REFRESH_TOKEN_URL(), {}, AUTH_COOKIE_CONFIG)
    .then(response => {
      const responseBody: RefreshTokenResponseDto = response.data;
      return responseBody;
    })
    .catch(error => {
      if (!error.response) return null;
      const responseBody: ResponseDto = error.response.data;
      return responseBody;
    });
  return result;
};

export const signOutRequest = async () => {
  const result = await axios.post(SIGN_OUT_URL(), {}, AUTH_COOKIE_CONFIG)
    .then(response => {
      const responseBody: ResponseDto = response.data;
      return responseBody;
    })
    .catch(error => {
      if (!error.response) return null;
      const responseBody: ResponseDto = error.response.data;
      return responseBody;
    });
  return result;
};
