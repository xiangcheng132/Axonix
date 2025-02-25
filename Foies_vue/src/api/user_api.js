import axios from 'axios';

const API_URL = 'http://localhost:8080/users';

export default {
  getUsers() {
    return axios.post(`${API_URL}/getByExample`, {});
  },
  getUserById(id) {
    return axios.get(`${API_URL}/${id}`);
  },
  addUser(user) {
    return axios.post(`${API_URL}/add`, user);
  },
  updateUser(user) {
    return axios.put(`${API_URL}/updateById`, user);
  },
  deleteUser(id) {
    return axios.delete(`${API_URL}/delete/${id}`);
  }
};