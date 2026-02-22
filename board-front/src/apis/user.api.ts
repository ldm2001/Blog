import axios from 'axios';

import { PatchNicknameRequestDto, PatchProfileImageRequestDto } from './request/user';
import { ResponseDto } from './response';
import {
  GetSignInUserResponseDto,
  GetUserResponseDto,
  PatchNicknameResponseDto,
  PatchProfileImageResponseDto,
} from './response/user';
import { API_DOMAIN, authorization } from './common';

const GET_SIGN_IN_USER_URL = () => `${API_DOMAIN}/user`;
const GET_USER_URL = (userEmail: string) => `${API_DOMAIN}/user/${userEmail}`;
const PATCH_NICKNAME_URL = () => `${API_DOMAIN}/user/nickname`;
const PATCH_PROFILE_IMAGE_URL = () => `${API_DOMAIN}/user/profile-image`;

export const getSignInUserRequest = async (accessToken: string) => {
  const result = await axios.get(GET_SIGN_IN_USER_URL(), authorization(accessToken))
    .then(response => {
      const responseBody: GetSignInUserResponseDto = response.data;
      return responseBody;
    })
    .catch(error => {
      if (!error.response) return null;
      const responseBody: ResponseDto = error.response.data;
      return responseBody;
    });
  return result;
};

export const getUserRequest = async (userEmail: string) => {
  const result = await axios.get(GET_USER_URL(userEmail))
    .then(response => {
      const responseBody: GetUserResponseDto = response.data;
      return responseBody;
    })
    .catch(error => {
      if (!error.response) return null;
      const responseBody: ResponseDto = error.response.data;
      return responseBody;
    });
  return result;
};

export const patchNicknameRequest = async (requestBody: PatchNicknameRequestDto, accessToken: string) => {
  const result = await axios.patch(PATCH_NICKNAME_URL(), requestBody, authorization(accessToken))
    .then(response => {
      const responseBody: PatchNicknameResponseDto = response.data;
      return responseBody;
    })
    .catch(error => {
      if (!error.response) return null;
      const responseBody: ResponseDto = error.response.data;
      return responseBody;
    });
  return result;
};

export const patchProfileImageRequest = async (requestBody: PatchProfileImageRequestDto, accessToken: string) => {
  const result = await axios.patch(PATCH_PROFILE_IMAGE_URL(), requestBody, authorization(accessToken))
    .then(response => {
      const responseBody: PatchProfileImageResponseDto = response.data;
      return responseBody;
    })
    .catch(error => {
      if (!error.response) return null;
      const responseBody: ResponseDto = error.response.data;
      return responseBody;
    });
  return result;
};
