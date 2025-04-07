import axios from 'axios';

const apiUrl = 'http://localhost:8080/api/forum-post';

export default {
  // 获取帖子列表（基础字段）
  async getForumPosts(data) {
    return axios.post(`${apiUrl}/select/by-example`, data);
  },

  // 获取帖子列表（带BLOB内容）
  async getForumPostsWithBlobs(data) {
    return axios.post(`${apiUrl}/select/by-example-with-blobs`, data);
  },

  // 添加帖子（选择性插入）
  async addForumPost(data) {
    return axios.post(`${apiUrl}/insert/selective`, data);
  },

  // 添加帖子（完整插入）
  async insertForumPost(data) {
    return axios.post(`${apiUrl}/insert`, data);
  },

  // 更新帖子（选择性更新）
  async updateForumPostSelective(data) {
    return axios.put(`${apiUrl}/update/selective`, data);
  },

  // 更新帖子（完整更新）
  async updateForumPost(data) {
    return axios.put(`${apiUrl}/update`, data);
  },

  // 更新帖子（带BLOB内容的完整更新）
  async updateForumPostWithBlobs(data) {
    return axios.put(`${apiUrl}/update/with-blobs`, data);
  },

  // 删除帖子（按主键）
  async deleteForumPost(id) {
    return axios.delete(`${apiUrl}/delete/${id}`);
  },

  // 批量删除帖子
  async deleteForumPosts(data) {
    return axios.post(`${apiUrl}/delete/by-example`, data);
  },

  // 获取帖子详情（按主键）
  async getForumPostDetail(id) {
    return axios.get(`${apiUrl}/select/${id}`);
  },

  // 统计帖子数量
  countForumPosts(example = {}) {
    return axios.post(`${apiUrl}/count`, example);
  }
};
