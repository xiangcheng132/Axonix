import axios from 'axios';

const API_URL = 'http://localhost:8080/api/forum-comment';

export default {
  getCommentsByExampleWithBlobs(example) {
    return axios.post(`${API_URL}/select/by-example-with-blobs`, example);
  },

  getCommentsByExample(example) {
    return axios.post(`${API_URL}/select/by-example`, example);
  },

  getCommentById(id) {
    return axios.get(`${API_URL}/select/${id}`);
  },

  addComment(comment) {
    return axios.post(`${API_URL}/insert`, comment);
  },

  updateCommentWithBlobs(comment) {
    return axios.put(`${API_URL}/update/with-blobs`, comment);
  },

  updateComment(comment) {
    return axios.put(`${API_URL}/update`, comment);
  },

  deleteComment(id) {
    return axios.delete(`${API_URL}/delete/${id}`);
  },

  deleteCommentsByExample(example) {
    return axios.delete(`${API_URL}/delete/by-example`, { data: example });
  },

  countComments(example = {}) {
    return axios.post(`${API_URL}/count`, example);
  }
};
