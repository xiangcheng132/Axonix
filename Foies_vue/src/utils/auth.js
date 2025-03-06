import Cookies from 'js-cookie'

const TokenKey = 'vue_admin_template_token'

// 获取 token
export function getToken() {
  return localStorage.getItem('token') || sessionStorage.getItem('token');  // 你可以根据需要使用 localStorage 或 sessionStorage
}


// 设置 token
export function setToken(token) {
  localStorage.setItem('token', token);  // 设置 token 到 localStorage 中
  // 或者 sessionStorage
  // sessionStorage.setItem('token', token); 
}

// 移除 token
export function removeToken() {
  localStorage.removeItem('token');  // 移除 token
  // 或者 sessionStorage
  // sessionStorage.removeItem('token');
}

