import { login, logout, getInfo } from '@/api/user'
import { getToken, setToken, removeToken } from '@/utils/auth'
import { resetRouter } from '@/router'

// 获取默认状态
const getDefaultState = () => {
  return {
    token: getToken(),
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
          const token = response.token || response.data?.token;
          if (token) {
            setToken(token);
            commit('SET_TOKEN', token);
            resolve();
          } else {
            reject('No token returned');
          }
        })
        .catch(error => {
          console.error('❌ Login Error:', error);  // 打印详细错误信息
          reject(error.response?.data?.message || '登录失败，请检查用户名或密码');
        });
    });
  },
  
  // 其他 actions...


  

  async getInfo({ commit, state }) {
    try {
      const response = await getInfo(state.token)
      const data = response.data

      if (!data) {
        throw new Error('Verification failed, please login again.')
      }

      // 更新用户信息
      commit('SET_NAME', data.username || 'Unknown')
      commit('SET_AVATAR', data.avatar || '')

      return data
    } catch (error) {
      console.error('❌ GetInfo Failed:', error)
      throw error
    }
  },

  async logout({ commit }) {
    try {
      // 前端 JWT 认证通常不需要后端 `logout` 请求
      removeToken() // 先移除 token
      resetRouter() // 重置路由
      commit('RESET_STATE') // 清除 Vuex 状态
    } catch (error) {
      console.error('❌ Logout Failed:', error)
      throw error
    }
  },

  async resetToken({ commit }) {
    removeToken() // 清除 token
    commit('RESET_STATE')
  }
}

export default {
  namespaced: true,
  state,
  mutations,
  actions
}
