import axios from 'axios';

const API_URL = 'http://localhost:8080/operationLogs';

export default {
  getOperationLogCount(example) {
    return axios.get(`${API_URL}/count`, { params: example });
  },
  getOperationLogs(example) {
    return axios.get(`${API_URL}/list`, { params: example });
  },
  getOperationLogById(id) {
    return axios.get(`${API_URL}/${id}`);
  },
  addOperationLog(log) {
    return axios.post(`${API_URL}/`, log);
  },
  addOperationLogSelective(log) {
    return axios.post(`${API_URL}/selective`, log);
  },
  updateOperationLog(id, log) {
    return axios.put(`${API_URL}/${id}`, log);
  },
  updateOperationLogSelective(id, log) {
    return axios.patch(`${API_URL}/${id}/selective`, log);
  },
  deleteOperationLog(id) {
    return axios.delete(`${API_URL}/${id}`);
  }
};
