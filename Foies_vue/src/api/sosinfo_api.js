import axios from 'axios';

const API_URL = 'http://localhost:8080/api/sos-notifications';

export default {
  getSosNotificationsByExampleWithBlobs(example) {
    return axios.post(`${API_URL}/select/by-example-with-blobs`, example);
  },

  getSosNotificationsByExample(example) {
    return axios.post(`${API_URL}/select/by-example`, example);
  },

  getSosNotificationById(id) {
    return axios.get(`${API_URL}/select/${id}`);
  },

  addSosNotification(notification) {
    return axios.post(`${API_URL}/insert`, notification);
  },

  updateSosNotificationWithBlobs(notification) {
    return axios.put(`${API_URL}/update/with-blobs`, notification);
  },

  updateSosNotification(notification) {
    return axios.put(`${API_URL}/update`, notification);
  },

  deleteSosNotification(id) {
    return axios.delete(`${API_URL}/delete/${id}`);
  },

  deleteSosNotificationsByExample(example) {
    return axios.delete(`${API_URL}/delete/by-example`, { data: example });
  },

  countSosNotifications(example = {}) {
    return axios.post(`${API_URL}/count`, example);
  }
};
