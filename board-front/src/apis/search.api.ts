import axios from 'axios';

import { ResponseDto } from './response';
import { GetPopularListResponseDto, GetRelationListResponseDto } from './response/search';
import { API_DOMAIN } from './common';

const GET_POPULAR_LIST_URL = () => `${API_DOMAIN}/search/popular-list`;
const GET_RELATION_LIST_URL = (searchWord: string) => `${API_DOMAIN}/search/${searchWord}/relation-list`;

export const getPopularListRequest = async () => {
  const result = await axios.get(GET_POPULAR_LIST_URL())
    .then(response => {
      const responseBody: GetPopularListResponseDto = response.data;
      return responseBody;
    })
    .catch(error => {
      if (!error.response) return null;
      const responseBody: ResponseDto = error.response.data;
      return responseBody;
    });
  return result;
};

export const getRelationListRequest = async (searchWord: string) => {
  const result = await axios.get(GET_RELATION_LIST_URL(searchWord))
    .then(response => {
      const responseBody: GetRelationListResponseDto = response.data;
      return responseBody;
    })
    .catch(error => {
      if (!error.response) return null;
      const responseBody: ResponseDto = error.response.data;
      return responseBody;
    });
  return result;
};
