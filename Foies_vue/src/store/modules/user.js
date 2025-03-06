import { login, logout, getInfo } from '@/api/user'
import { getToken, setToken, removeToken } from '@/utils/auth'
import { resetRouter } from '@/router'

// user.js
const getDefaultState = () => {
  const token = getToken();  // 确保 token 被正确获取
  return {
    token: token,
    name: '',
    avatar: ''
  }
}


const state = getDefaultState()

const mutations = {
  RESET_STATE: (state) => {
    Object.assign(state, getDefaultState())
  },
  SET_TOKEN: (state, token) => {
    state.token = token
  },
  SET_NAME: (state, name) => {
    state.name = name
  },
  SET_AVATAR: (state, avatar) => {
    state.avatar = avatar
  }
}

const actions = {
  login({ commit }, userInfo) {
    const { username, password } = userInfo;
    return new Promise((resolve, reject) => {
      login({ username: username.trim(), password: password })
        .then(response => {
          console.log('✅ Login API Response:', response); // 打印完整的 API 返回
          const { token } = response;
  
          // 确保 token 存在
          if (!token) {
            console.error('❌ Login failed: No token in response');
            return reject('Login failed: Token is missing.');
          }
  
          // 存储 token 到 localStorage 或 sessionStorage
          setToken(token);
  
          // 更新 Vuex 状态
          commit('SET_TOKEN', token);
  
          resolve();  // 登录成功，继续执行后续操作
        })
        .catch(error => {
          console.error('❌ Login Request Failed:', error); // 打印完整的错误信息
          reject(error.response?.data?.message || 'Login request failed');
        });
    });
  },
  
  
  // get user info
  getInfo({ commit, state }) {
    return new Promise((resolve, reject) => {
      getInfo(state.token).then(response => {
        const { data } = response

        if (!data) {
          return reject('Verification failed, please Login again.')
        }

        const { name, avatar } = data

        commit('SET_NAME', name)
        commit('SET_AVATAR', avatar)
        resolve(data)
      }).catch(error => {
        reject(error)
      })
    })
  },

  // user logout
  logout({ commit, state }) {
    return new Promise((resolve, reject) => {
      logout(state.token).then(() => {
        removeToken() // must remove token first
        resetRouter()
        commit('RESET_STATE')
        resolve()
      }).catch(error => {
        reject(error)
      })
    })
  },

  // remove token
  resetToken({ commit }) {
    return new Promise(resolve => {
      removeToken() // must remove token first
      commit('RESET_STATE')
      resolve()
    })
  }
}

export default {
  namespaced: true,
  state,
  mutations,
  actions
}
