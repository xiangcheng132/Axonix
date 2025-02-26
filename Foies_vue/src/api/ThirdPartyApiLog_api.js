import axios from 'axios';

const API_URL = 'http://localhost:8080/third-party-api-logs';

export default {
  // 获取日志记录数量
  getLogCount(example) {
    console.log('Sending request to get log count...');
    return axios.get(`${API_URL}/count`, { params: example })
      .then(response => {
        console.log('Received response for log count:', response);
        return response;
      })
      .catch(error => {
        console.error('Error getting log count:', error);
        throw error;
      });
  },

  // 获取日志列表
  getLogs(example) {
    return axios.get(`${API_URL}/list`, { params: example });
  },

  // 根据 ID 获取日志详情
  getLogById(id) {
    return axios.get(`${API_URL}/${id}`);
  },

  // 添加日志记录
  addLog(log) {
    return axios.post(`${API_URL}/insert`, log);
  },

  // 选择性添加日志记录
  addLogSelective(log) {
    return axios.post(`${API_URL}/selective`, log);
  },

  // 更新日志记录（根据 ID）
  updateLog(id, log) {
    return axios.put(`${API_URL}/${id}`, log);
  },

  // 更新日志记录（包含 BLOB 字段）
  updateLogWithBlobs(id, log) {
    return axios.put(`${API_URL}/${id}/blobs`, log);
  },

  // 选择性更新日志记录（根据 ID）
  updateLogSelective(id, log) {
    return axios.patch(`${API_URL}/${id}/selective`, log);
  },

  // 删除日志记录（根据 ID）
  deleteLog(id) {
    return axios.delete(`${API_URL}/${id}`);
  }
};