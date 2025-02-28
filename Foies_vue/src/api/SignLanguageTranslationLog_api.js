import axios from 'axios';

// 定义 API 的基础 URL
const API_URL = 'http://localhost:8080/sign-language-translation-logs';

export default {
  /**
   * 获取日志记录数量
   * @param {Object} example - 查询条件对象
   * @returns {Promise} - 返回包含日志数量的 Promise 对象
   */
  getLogCount(example) {
    return axios.get(`${API_URL}/count`, example)
      .then(response => response.data)
      .catch(error => {
        console.error('Error getting log count:', error);
        throw error;
      });
  },

  /**
   * 获取日志列表
   * @param {Object} example - 查询条件对象
   * @returns {Promise} - 返回包含日志列表的 Promise 对象
   */
  getLogs(example) {
    return axios.get(`${API_URL}/list`, example)
      .then(response => response.data)
      .catch(error => {
        console.error('Error getting logs:', error);
        throw error;
      });
  },

  /**
   * 根据 ID 获取日志详情
   * @param {Number} id - 日志记录的 ID
   * @returns {Promise} - 返回包含日志详情的 Promise 对象
   */
  getLogById(id) {
    return axios.get(`${API_URL}/${id}`)
      .then(response => response.data)
      .catch(error => {
        console.error('Error getting log by ID:', error);
        throw error;
      });
  },

  /**
   * 添加日志记录
   * @param {Object} log - 日志记录对象
   * @returns {Promise} - 返回添加操作的结果
   */
  addLog(log) {
    return axios.post(`${API_URL}/insert`, log)
      .then(response => response.data)
      .catch(error => {
        console.error('Error adding log:', error);
        throw error;
      });
  },

  /**
   * 选择性添加日志记录
   * @param {Object} log - 日志记录对象
   * @returns {Promise} - 返回选择性添加操作的结果
   */
  addLogSelective(log) {
    return axios.post(`${API_URL}/selective`, log)
      .then(response => response.data)
      .catch(error => {
        console.error('Error adding selective log:', error);
        throw error;
      });
  },

  /**
   * 更新日志记录（根据 ID）
   * @param {Number} id - 日志记录的 ID
   * @param {Object} log - 日志记录对象
   * @returns {Promise} - 返回更新操作的结果
   */
  updateLog(id, log) {
    return axios.put(`${API_URL}/${id}`, log)
      .then(response => response.data)
      .catch(error => {
        console.error('Error updating log:', error);
        throw error;
      });
  },

  /**
   * 更新日志记录（包含 BLOB 字段）
   * @param {Number} id - 日志记录的 ID
   * @param {Object} log - 日志记录对象
   * @returns {Promise} - 返回更新操作的结果
   */
  updateLogWithBlobs(id, log) {
    return axios.put(`${API_URL}/${id}/blobs`, log)
      .then(response => response.data)
      .catch(error => {
        console.error('Error updating log with blobs:', error);
        throw error;
      });
  },

  /**
   * 选择性更新日志记录（根据 ID）
   * @param {Number} id - 日志记录的 ID
   * @param {Object} log - 日志记录对象
   * @returns {Promise} - 返回选择性更新操作的结果
   */
  updateLogSelective(id, log) {
    return axios.patch(`${API_URL}/${id}/selective`, log)
      .then(response => response.data)
      .catch(error => {
        console.error('Error updating log selectively:', error);
        throw error;
      });
  },

  /**
   * 删除日志记录（根据 ID）
   * @param {Number} id - 日志记录的 ID
   * @returns {Promise} - 返回删除操作的结果
   */
  deleteLog(id) {
    return axios.delete(`${API_URL}/${id}`)
      .then(response => response.data)
      .catch(error => {
        console.error('Error deleting log:', error);
        throw error;
      });
  }
};