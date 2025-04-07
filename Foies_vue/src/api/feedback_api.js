import axios from 'axios';

const API_URL = 'http://localhost:8080/api/feedback';

export default {
  // 统计数量
  countFeedbacks(example = {}) {
    return axios.post(`${API_URL}/count`, example);
  },

  // 条件删除
  deleteFeedbacksByExample(example) {
    return axios.delete(`${API_URL}/delete/by-example`, { data: example });
  },

  // 主键删除
  deleteFeedback(id) {
    return axios.delete(`${API_URL}/delete/${id}`);
  },

  // 完整插入（包含BLOB字段）
  addFeedback(feedback) {
    return axios.post(`${API_URL}/insert`, feedback);
  },

  // 选择性插入（忽略空字段）
  addFeedbackSelective(feedback) {
    return axios.post(`${API_URL}/insert/selective`, feedback);
  },

  // 条件查询（包含BLOB字段）
  getFeedbacksByExampleWithBlobs(example) {
    return axios.post(`${API_URL}/select/by-example-with-blobs`, example);
  },

  // 条件查询（不包含BLOB字段）
  getFeedbacksByExample(example) {
    return axios.post(`${API_URL}/select/by-example`, example);
  },

  // 主键查询
  getFeedbackById(id) {
    return axios.get(`${API_URL}/select/${id}`);
  },

  // 条件选择性更新（忽略空字段）
  updateFeedbackByExampleSelective(feedback, example) {
    return axios.put(`${API_URL}/update/by-example/selective`, feedback, { params: { example } });
  },

  // 条件完整更新（包含BLOB字段）
  updateFeedbackByExampleWithBlobs(feedback, example) {
    return axios.put(`${API_URL}/update/by-example/with-blobs`, feedback, { params: { example } });
  },

  // 条件完整更新（不包含BLOB字段）
  updateFeedbackByExample(feedback, example) {
    return axios.put(`${API_URL}/update/by-example`, feedback, { params: { example } });
  },

  // 主键选择性更新（忽略空字段）
  updateFeedbackSelective(feedback) {
    return axios.put(`${API_URL}/update/selective`, feedback);
  },

  // 主键完整更新（包含BLOB字段）
  updateFeedbackWithBlobs(feedback) {
    return axios.put(`${API_URL}/update/with-blobs`, feedback);
  },

  // 主键完整更新（不包含BLOB字段）
  updateFeedback(feedback) {
    return axios.put(`${API_URL}/update`, feedback);
  }
};
