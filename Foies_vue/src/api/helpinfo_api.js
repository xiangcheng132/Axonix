import axios from 'axios';

const apiUrl = 'http://localhost:8080/api/help-request';

export default {
  // 获取求助记录列表
  async getHelpRequests(data) {
    return axios.post(`${apiUrl}/select/by-example`, data);
  },

  // 添加求助记录
  async addHelpRequest(data) {
    return axios.post(`${apiUrl}/insert/selective`, data);
  },

  // 更新求助记录
  async updateHelpRequest(data) {
    return axios.put(`${apiUrl}/update/selective`, data);
  },

  // 删除求助记录
  async deleteHelpRequest(id) {
    return axios.delete(`${apiUrl}/delete/${id}`);
  },

  // 批量删除求助记录
  async deleteHelpRequests(data) {
    return axios.post(`${apiUrl}/delete/by-example`, data);
  },
  
  // 获取求助记录详情
  async getHelpRequestDetail(id) {
    return axios.get(`${apiUrl}/select/${id}`);
  },

  // 统计求助记录数量
  countHelpRequests(example = {}) {
    return axios.post(`${apiUrl}/count`, example);
  }
};
