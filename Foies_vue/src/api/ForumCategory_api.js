import axios from 'axios';

const API_URL = 'http://localhost:8080/forum-categories';

export default {
  getForumCategoryCount(example) {
    return axios.get(`${API_URL}/count`, { params: example });
  },
  getForumCategories(example) {
    return axios.post(`${API_URL}/list`, example );
  },
  getForumCategoryById(id) {
    return axios.get(`${API_URL}/${id}`);
  },
  addForumCategory(forumCategory) {
    return axios.post(`${API_URL}/`, forumCategory);
  },
  addForumCategorySelective(forumCategory) {
    return axios.post(`${API_URL}/selective`, forumCategory);
  },
  updateForumCategory(id, forumCategory) {
    return axios.put(`${API_URL}/${id}`, forumCategory);
  },
  updateForumCategorySelective(id, forumCategory) {
    return axios.patch(`${API_URL}/${id}/selective`, forumCategory);
  },
  deleteForumCategory(id) {
    return axios.delete(`${API_URL}/${id}`);
  }
};
