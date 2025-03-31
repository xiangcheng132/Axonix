import axios from 'axios';

const API_URL = 'http://localhost:8080/api/users';

export default {
  getUsers(example) {
    return axios.post(`${API_URL}/list`, example)
      .then(response => {
        // 确保返回的是数组
        if (!Array.isArray(response.data)) {
          return { data: [] };
        }
        return response;
      })
      .catch(error => {
        console.error('API Error:', error);
        throw error;
      });
  },
  getUserById(id) {
    return axios.get(`${API_URL}/detail/${id}`);
  },
  addUser(user) {
    return axios.post(`${API_URL}/register`, user);
  },
  updateUser(user) {
    return axios.put(`${API_URL}/update`, user);
  },
  deleteUser(id) {
    return axios.delete(`${API_URL}/delete/${id}`);
  },
  countUsers(example = {}) {
    return axios.post(`${API_URL}/count`, example);
  }  
};
