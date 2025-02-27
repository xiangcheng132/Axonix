import axios from 'axios';

const API_URL = 'http://localhost:8080/payments';

export default {
  // 获取支付日志数量
  getPaymentCount(example) {
    return axios.get(`${API_URL}/count`, { params: example });
  },

  // 获取支付日志列表
  getPayments(example) {
    return axios.get(`${API_URL}/list`, { params: example });
  },

  // 根据 ID 获取支付日志详情
  getPaymentById(id) {
    return axios.get(`${API_URL}/${id}`);
  },

  // 添加支付日志
  addPayment(payment) {
    return axios.post(`${API_URL}/insert`, payment);
  },

  // 选择性添加支付日志
  addPaymentSelective(payment) {
    return axios.post(`${API_URL}/selective`, payment);
  },

  // 更新支付日志（根据 ID）
  updatePayment(id, payment) {
    return axios.put(`${API_URL}/${id}`, payment);
  },

  // 选择性更新支付日志（根据 ID）
  updatePaymentSelective(id, payment) {
    return axios.patch(`${API_URL}/${id}/selective`, payment);
  },

  // 删除支付日志（根据 ID）
  deletePayment(id) {
    return axios.delete(`${API_URL}/${id}`);
  }
};
