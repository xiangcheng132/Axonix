import axios from 'axios';

const apiUrl = 'http://localhost:8080/api/vips';

export default {
  // 获取VIP列表
  async getVips(data) {
    return axios.post(`${apiUrl}/select/by-example`, data);
  },

  // 添加VIP
  async addVip(data) {
    return axios.post(`${apiUrl}/insert/selective`, data);
  },

  // 更新VIP
  async updateVip(data) {
    return axios.put(`${apiUrl}/update/selective`, data);
  },

  // 删除VIP
  async deleteVip(id) {
    return axios.delete(`${apiUrl}/delete/${id}`);
  },

  // 批量删除VIP
  async deleteVips(data) {
    return axios.post(`${apiUrl}/delete/by-example`, data);
  },
  
  // 获取VIP详情
  async getVipDetail(id) {
    return axios.get(`${apiUrl}/select/${id}`);
  },
};
