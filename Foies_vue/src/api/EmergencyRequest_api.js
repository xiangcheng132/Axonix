import axios from 'axios';

const API_URL = 'http://localhost:8080/emergency-requests';

export default {
  getEmergencyRequestCount(example) {
    return axios.get(`${API_URL}/count`, { params: example });
  },
  getEmergencyRequests(example) {
    return axios.post(`${API_URL}/list`, example);
  },
  getEmergencyRequestById(id) {
    return axios.get(`${API_URL}/${id}`);
  },
  addEmergencyRequest(request) {
    return axios.post(`${API_URL}/`, request);
  },
  addEmergencyRequestSelective(request) {
    return axios.post(`${API_URL}/selective`, request);
  },
  updateEmergencyRequest(id, request) {
    return axios.put(`${API_URL}/${id}`, request);
  },
  updateEmergencyRequestSelective(id, request) {
    return axios.patch(`${API_URL}/${id}/selective`, request);
  },
  deleteEmergencyRequest(id) {
    return axios.delete(`${API_URL}/${id}`);
  }
};
