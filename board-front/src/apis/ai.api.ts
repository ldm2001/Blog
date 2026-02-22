import axios from 'axios';

import { API_DOMAIN } from './common';

const SUGGEST_TITLE_URL = () => `${API_DOMAIN}/ai/suggest-title`;

export const suggestTitleRequest = async (content: string) => {
  const result = await axios.post(SUGGEST_TITLE_URL(), { content })
    .then(response => {
      const responseBody: { suggestedTitles: string[] } = response.data;
      return responseBody;
    })
    .catch(() => null);
  return result;
};
