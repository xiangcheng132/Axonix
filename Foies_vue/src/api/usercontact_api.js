import axios from 'axios';

const API_URL = 'http://localhost:8080/api/emergency-contact';

export default {
  countContacts(example) {
    return axios.post(`${API_URL}/count`, example);
  },
  deleteContactById(id) {
    return axios.delete(`${API_URL}/delete/${id}`);
  },
  deleteContactsByExample(example) {
    return axios.delete(`${API_URL}/delete/by-example`, { data: example });
  },
  addContact(contact) {
    return axios.post(`${API_URL}/insert`, contact);
  },
  addContactSelective(contact) {
    return axios.post(`${API_URL}/insert/selective`, contact);
  },
  getContacts(example) {
    return axios.post(`${API_URL}/select/by-example`, example);
  },
  getContactById(id) {
    return axios.get(`${API_URL}/select/${id}`);
  },
  updateContact(contact) {
    return axios.put(`${API_URL}/update`, contact);
  },
  updateContactSelective(contact) {
    return axios.put(`${API_URL}/update/selective`, contact);
  },
  updateContactsByExample(contact, example) {
    return axios.put(`${API_URL}/update/by-example`, { contact, example });
  },
  updateContactsSelective(contact, example) {
    return axios.put(`${API_URL}/update/by-example/selective`, { contact, example });
  }
};