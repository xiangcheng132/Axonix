import axios from 'axios';

const API_URL = 'http://localhost:8080/forum-posts';

export default {
  getPostCount(example) {
    return axios.get(`${API_URL}/count`, { params: example });
  },

  getPosts(example) {
    return axios.post(`${API_URL}/list`, example);
  },

  getPostById(id) {
    return axios.get(`${API_URL}/${id}`);
  },

  addPost(post) {
    return axios.post(`${API_URL}/`, post);
  },

  addPostSelective(post) {
    return axios.post(`${API_URL}/selective`, post);
  },

  updatePost(id, post) {
    return axios.put(`${API_URL}/${id}`, post);
  },

  updatePostSelective(id, post) {
    return axios.patch(`${API_URL}/${id}/selective`, post);
  },

  deletePost(id) {
    return axios.delete(`${API_URL}/${id}`);
  }
};
