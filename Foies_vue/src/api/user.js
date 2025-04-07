import axios from 'axios';

export function login(data) {
  return axios.post('http://localhost:8080/api/admin/login', data, {
    headers: { 'Content-Type': 'application/json' }
  })
    .catch(error => {
      console.error("❌ Login Error:", error.response || error); // 打印完整的错误信息
      throw error; // 抛出错误
    });
}

export function getInfo(token) {
  return axios.get('http://localhost:8080/api/admin/getUserInfo', {
    headers: { 'Authorization': `Bearer ${token}` }
  });
}

export function logout(token) {
  return axios.post('http://localhost:8080/api/admin/logout', {}, {
    headers: { 'Authorization': `Bearer ${token}` }
  });
}
