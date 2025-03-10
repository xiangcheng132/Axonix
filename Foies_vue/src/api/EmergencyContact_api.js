import axios from 'axios';

const API_URL = 'http://localhost:8080/emergency-contacts';

export default {
  getEmergencyContactCount(example) {
    return axios.get(`${API_URL}/count`, { params: example });
  },
  getEmergencyContacts(example) {
    return axios.post(`${API_URL}/list`, example );
  },
  getEmergencyContactById(id) {
    return axios.get(`${API_URL}/${id}`);
  },
  addEmergencyContact(contact) {
    return axios.post(`${API_URL}/`, contact);
  },
  addEmergencyContactSelective(contact) {
    return axios.post(`${API_URL}/selective`, contact);
  },
  updateEmergencyContact(id, contact) {
    return axios.put(`${API_URL}/${id}`, contact);
  },
  updateEmergencyContactSelective(id, contact) {
    return axios.patch(`${API_URL}/${id}/selective`, contact);
  },
  deleteEmergencyContact(id) {
    return axios.delete(`${API_URL}/${id}`);
  }
};
