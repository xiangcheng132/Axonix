import axios from 'axios';

const API_URL = 'http://localhost:8080/notifications';

export default {
  getNotificationCount(example) {
    return axios.get(`${API_URL}/count`, { params: example });
  },
  getNotifications(example) {
    return axios.post(`${API_URL}/list`, example);
  },
  getNotificationById(id) {
    return axios.get(`${API_URL}/${id}`);
  },
  addNotification(notification) {
    return axios.post(`${API_URL}/`, notification);
  },
  addNotificationSelective(notification) {
    return axios.post(`${API_URL}/selective`, notification);
  },
  updateNotification(id, notification) {
    return axios.put(`${API_URL}/${id}`, notification);
  },
  updateNotificationSelective(id, notification) {
    return axios.patch(`${API_URL}/${id}/selective`, notification);
  },
  deleteNotification(id) {
    return axios.delete(`${API_URL}/${id}`);
  }
};
