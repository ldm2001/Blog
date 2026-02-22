export const DOMAIN = 'http://localhost:4000';
export const API_DOMAIN = `${DOMAIN}/api/v1`;

export const authorization = (accessToken: string) => {
  return { headers: { Authorization: `Bearer ${accessToken}` } };
};
