import axios from 'axios';

const API_URL = 'http://localhost:8080/api/notifications';

export default {
  countNotifications(example = {}) {
    return axios.post(`${API_URL}/count`, example);
  },
  deleteNotificationByExample(example) {
    return axios.delete(`${API_URL}/delete/by-example`, { data: example });
  },
  deleteNotificationById(id) {
    return axios.delete(`${API_URL}/delete/${id}`);
  },
  addNotification(notification) {
    return axios.post(`${API_URL}/insert`, notification);
  },
  addNotificationSelective(notification) {
    return axios.post(`${API_URL}/insert/selective`, notification);
  },
  getNotificationsByExampleWithBLOBs(example) {
    return axios.post(`${API_URL}/select/by-example-with-blobs`, example);
  },
  getNotificationsByExample(example) {
    return axios.post(`${API_URL}/select/by-example`, example);
  },
  getNotificationById(id) {
    return axios.get(`${API_URL}/select/${id}`);
  },
  updateNotificationByExampleSelective(notification, example) {
    return axios.put(`${API_URL}/update/by-example/selective`, { notification, example });
  },
  updateNotificationByExampleWithBLOBs(notification, example) {
    return axios.put(`${API_URL}/update/by-example/with-blobs`, { notification, example });
  },
  updateNotificationByExample(notification, example) {
    return axios.put(`${API_URL}/update/by-example`, { notification, example });
  },
  updateNotificationByIdSelective(notification) {
    return axios.put(`${API_URL}/update/selective`, notification);
  },
  updateNotificationByIdWithBLOBs(notification) {
    return axios.put(`${API_URL}/update/with-blobs`, notification);
  },
  updateNotificationById(notification) {
    return axios.put(`${API_URL}/update`, notification);
  }
};
