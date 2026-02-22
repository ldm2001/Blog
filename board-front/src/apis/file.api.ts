import axios from 'axios';

import { DOMAIN } from './common';

const FILE_DOMAIN = `${DOMAIN}/file`;
const FILE_UPLOAD_URL = () => `${FILE_DOMAIN}/upload`;
const multipartFormData = { headers: { 'Content-Type': 'multipart/form-data' } };

export const fileUploadRequest = async (data: FormData) => {
  const result = await axios.post(FILE_UPLOAD_URL(), data, multipartFormData)
    .then(response => {
      const responseBody: string = response.data;
      return responseBody;
    })
    .catch(() => null);
  return result;
};
