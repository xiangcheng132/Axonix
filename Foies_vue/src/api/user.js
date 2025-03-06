import axios from 'axios';

export function login(data) {
  return axios.post('http://localhost:8080/users/login', data, {
    headers: {
      'Content-Type': 'application/json'
    }
  })
  .then(response => {
    console.log("✅ Login Success:", response.data);
    // 确保正确获取 token 并返回
    const token = response.data.token;
    if (token) {
      return { token };  // 返回token对象
    } else {
      throw new Error('No token in response');
    }
  })
  .catch(error => {
    console.error("❌ Login Error:", error);
    console.error("📌 Error Response:", error.response?.data);
    throw error;
  });
}



export function getInfo(token) {
  return axios.get('http://localhost:8080/users/getUserInfo', {  // 假设这个是正确的接口
    headers: { 'Authorization': `Bearer ${token}` }
  });
}


export function logout(token) {
  return axios.post('http://localhost:8080/users/logout', {}, {
    headers: { 'Authorization': `Bearer ${token}` }
  });
}

