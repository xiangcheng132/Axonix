import axios from 'axios';

const API_URL = 'http://localhost:8080/api/ai-log';

export default {
  getAiLogs(example) {
    return axios.post(`${API_URL}/select/by-example`, example);
  },
  countAiLogs(example = {}) {
    return axios.post(`${API_URL}/count`, example);
  }
};
