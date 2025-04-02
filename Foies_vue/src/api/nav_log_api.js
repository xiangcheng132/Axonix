import axios from 'axios';

const API_URL = 'http://localhost:8080/api/traffic-logs';

export default {
  getNavLogs(example) {
    return axios.post(`${API_URL}/select/by-example`, example);
  },
  countNavLogs(example = {}) {
    return axios.post(`${API_URL}/count`, example);
  }
};
