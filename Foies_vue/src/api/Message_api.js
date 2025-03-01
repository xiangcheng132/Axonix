import axios from 'axios';

const API_URL = 'http://localhost:8080/messages';

export default {
  getMessageCount(example) {
    return axios.get(`${API_URL}/count`, { params: example });
  },
  getMessages(example) {
    return axios.get(`${API_URL}/list`, { params: example });
  },
  getMessageById(id) {
    return axios.get(`${API_URL}/${id}`);
  },
  addMessage(message) {
    return axios.post(`${API_URL}/`, message);
  },
  addMessageSelective(message) {
    return axios.post(`${API_URL}/selective`, message);
  },
  updateMessage(id, message) {
    return axios.put(`${API_URL}/${id}`, message);
  },
  updateMessageSelective(id, message) {
    return axios.patch(`${API_URL}/${id}/selective`, message);
  },
  deleteMessage(id) {
    return axios.delete(`${API_URL}/${id}`);
  }
};