import axios from 'axios';

const API_URL = 'http://localhost:8080/api/admin';

export default {
  getAdmins(example) {
    return axios.post(`${API_URL}/select/by-example`, example);
  },
  getAdminById(id) {
    return axios.get(`${API_URL}/select/${id}`);
  },
  addAdmin(admin) {
    return axios.post(`${API_URL}/insert`, admin);
  },
  updateAdmin(admin) {
    return axios.put(`${API_URL}/update`, admin);
  },
  deleteAdmin(id) {
    return axios.delete(`${API_URL}/delete/${id}`);
  },
  countAdmins(example = {}) {
    return axios.post(`${API_URL}/count`, example);
  }
};
