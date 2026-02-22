import axios from 'axios';

import { PatchBoardRequestDto, PostBoardRequestDto, PostCommentRequestDto } from './request/board';
import { ResponseDto } from './response';
import {
  DeleteBoardResponseDto,
  GetBoardResponseDto,
  GetCommentListResponseDto,
  GetFavoriteListResponseDto,
  GetLatestBoardListResponseDto,
  GetSearchBoardListResponseDto,
  GetTop3BoardListResponseDto,
  IncreaseViewCountResponseDto,
  PatchBoardResponseDto,
  PostBoardResponseDto,
  PostCommentResponseDto,
  PutFavoriteResponseDto,
} from './response/board';
import { GetUserBoardListResponseDto } from './response/user';
import { API_DOMAIN, authorization } from './common';

const GET_BOARD_URL = (boardNumber: number | string) => `${API_DOMAIN}/board/${boardNumber}`;
const GET_LATEST_BOARD_LIST_URL = () => `${API_DOMAIN}/board/latest-list`;
const GET_TOP_3_BOARD_LIST_URL = () => `${API_DOMAIN}/board/top-3?_t=${Date.now()}`;
const INCREASE_VIEW_COUNT_URL = (boardNumber: number | string) => `${API_DOMAIN}/board/${boardNumber}/increase-view-count`;
const GET_FAVORITE_LIST_URL = (boardNumber: number | string) => `${API_DOMAIN}/board/${boardNumber}/favorite-list`;
const GET_COMMENT_LIST_URL = (boardNumber: number | string) => `${API_DOMAIN}/board/${boardNumber}/comment-list`;
const POST_BOARD_URL = () => `${API_DOMAIN}/board`;
const POST_COMMENT_URL = (boardNumber: number | string) => `${API_DOMAIN}/board/${boardNumber}/comment`;
const PATCH_BOARD_URL = (boardNumber: number | string) => `${API_DOMAIN}/board/${boardNumber}`;
const PUT_FAVORITE_URL = (boardNumber: number | string) => `${API_DOMAIN}/board/${boardNumber}/favorite`;
const DELETE_BOARD_URL = (boardNumber: number | string) => `${API_DOMAIN}/board/${boardNumber}`;
const GET_BOARD_SEARCH_LIST_URL = (searchWord: string, page: number, preSearchWord?: string) =>
  `${API_DOMAIN}/board/search-list/${searchWord}${preSearchWord ? `/${preSearchWord}` : ''}?page=${page}`;
const GET_USER_BOARD_LIST_URL = (userEmail: string, page: number) =>
  `${API_DOMAIN}/board/user-board-list/${userEmail}?page=${page}`;

export const getBoardRequest = async (boardNumber: number | string) => {
  const result = await axios.get(GET_BOARD_URL(boardNumber))
    .then(response => {
      const responseBody: GetBoardResponseDto = response.data;
      return responseBody;
    })
    .catch(error => {
      if (!error.response) return null;
      const responseBody: ResponseDto = error.response.data;
      return responseBody;
    });
  return result;
};

export const getLatestBoardListRequest = async () => {
  const result = await axios.get(GET_LATEST_BOARD_LIST_URL())
    .then(response => {
      const responseBody: GetLatestBoardListResponseDto = response.data;
      return responseBody;
    })
    .catch(error => {
      if (!error.response) return null;
      const responseBody: ResponseDto = error.response.data;
      return responseBody;
    });
  return result;
};

export const getTop3BoardListRequest = async () => {
  const result = await axios.get(GET_TOP_3_BOARD_LIST_URL())
    .then(response => {
      const responseBody: GetTop3BoardListResponseDto = response.data;
      return responseBody;
    })
    .catch(error => {
      if (!error.response) return null;
      const responseBody: ResponseDto = error.response.data;
      return responseBody;
    });
  return result;
};

export const increaseViewCountRequest = async (boardNumber: number | string) => {
  const result = await axios.get(INCREASE_VIEW_COUNT_URL(boardNumber))
    .then(response => {
      const responseBody: IncreaseViewCountResponseDto = response.data;
      return responseBody;
    })
    .catch(error => {
      if (!error.response) return null;
      const responseBody: ResponseDto = error.response.data;
      return responseBody;
    });
  return result;
};

export const getFavoriteListRequest = async (boardNumber: number | string) => {
  const result = await axios.get(GET_FAVORITE_LIST_URL(boardNumber))
    .then(response => {
      const responseBody: GetFavoriteListResponseDto = response.data;
      return responseBody;
    })
    .catch(error => {
      if (!error.response) return null;
      const responseBody: ResponseDto = error.response.data;
      return responseBody;
    });
  return result;
};

export const getCommentListRequest = async (boardNumber: number | string) => {
  const result = await axios.get(GET_COMMENT_LIST_URL(boardNumber))
    .then(response => {
      const responseBody: GetCommentListResponseDto = response.data;
      return responseBody;
    })
    .catch(error => {
      if (!error.response) return null;
      const responseBody: ResponseDto = error.response.data;
      return responseBody;
    });
  return result;
};

export const postBoardRequest = async (requestBody: PostBoardRequestDto, accessToken: string) => {
  const result = await axios.post(POST_BOARD_URL(), requestBody, authorization(accessToken))
    .then(response => {
      const responseBody: PostBoardResponseDto = response.data;
      return responseBody;
    })
    .catch(error => {
      if (!error.response) return null;
      const responseBody: ResponseDto = error.response.data;
      return responseBody;
    });
  return result;
};

export const postCommentRequest = async (boardNumber: number | string, requestBody: PostCommentRequestDto, accessToken: string) => {
  const result = await axios.post(POST_COMMENT_URL(boardNumber), requestBody, authorization(accessToken))
    .then(response => {
      const responseBody: PostCommentResponseDto = response.data;
      return responseBody;
    })
    .catch(error => {
      if (!error.response) return null;
      const responseBody: ResponseDto = error.response.data;
      return responseBody;
    });
  return result;
};

export const patchBoardRequest = async (boardNumber: number | string, requestBody: PatchBoardRequestDto, accessToken: string) => {
  const result = await axios.patch(PATCH_BOARD_URL(boardNumber), requestBody, authorization(accessToken))
    .then(response => {
      const responseBody: PatchBoardResponseDto = response.data;
      return responseBody;
    })
    .catch(error => {
      if (!error.response) return null;
      const responseBody: ResponseDto = error.response.data;
      return responseBody;
    });
  return result;
};

export const putFavoriteRequest = async (boardNumber: number | string, accessToken: string) => {
  const result = await axios.put(PUT_FAVORITE_URL(boardNumber), {}, authorization(accessToken))
    .then(response => {
      const responseBody: PutFavoriteResponseDto = response.data;
      return responseBody;
    })
    .catch(error => {
      if (!error.response) return null;
      const responseBody: ResponseDto = error.response.data;
      return responseBody;
    });
  return result;
};

export const deleteBoardRequest = async (boardNumber: number | string, accessToken: string) => {
  const result = await axios.delete(DELETE_BOARD_URL(boardNumber), authorization(accessToken))
    .then(response => {
      const responseBody: DeleteBoardResponseDto = response.data;
      return responseBody;
    })
    .catch(error => {
      if (!error.response) return null;
      const responseBody: ResponseDto = error.response.data;
      return responseBody;
    });
  return result;
};

export const getBoardSearchListRequest = async (searchWord: string, page: number, preSearchWord?: string) => {
  const result = await axios.get(GET_BOARD_SEARCH_LIST_URL(searchWord, page, preSearchWord))
    .then(response => {
      const responseBody: GetSearchBoardListResponseDto = response.data;
      return responseBody;
    })
    .catch(error => {
      if (!error.response) return null;
      const responseBody: ResponseDto = error.response.data;
      return responseBody;
    });
  return result;
};

export const getUserBoardListRequest = async (userEmail: string, page: number) => {
  const result = await axios.get(GET_USER_BOARD_LIST_URL(userEmail, page))
    .then(response => {
      const responseBody: GetUserBoardListResponseDto = response.data;
      return responseBody;
    })
    .catch(error => {
      if (!error.response) return null;
      const responseBody: ResponseDto = error.response.data;
      return responseBody;
    });
  return result;
};
