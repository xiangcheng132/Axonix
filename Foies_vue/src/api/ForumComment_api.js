import axios from 'axios';

const API_URL = 'http://localhost:8080/forum-comments';

export default {
  getCommentCount(example) {
    return axios.get(`${API_URL}/count`, { params: example });
  },

  getComments(queryParams) {
    return axios.get(`${API_URL}/list${queryParams}`);
  },

  getCommentById(id) {
    return axios.get(`${API_URL}/${id}`);
  },

  addComment(comment) {
    return axios.post(`${API_URL}/`, comment);
  },

  addCommentSelective(comment) {
    return axios.post(`${API_URL}/selective`, comment);
  },

  updateComment(id, comment) {
    return axios.put(`${API_URL}/${id}`, comment);
  },

  updateCommentSelective(id, comment) {
    return axios.patch(`${API_URL}/${id}/selective`, comment);
  },

  deleteComment(id) {
    return axios.delete(`${API_URL}/${id}`);
  }
};
