import Vue from 'vue'
import { getInfo, login, logout, register } from '@/api/login'
import { ACCESS_TOKEN } from '@/store/mutation-types'
import { welcome } from '@/utils/util'

const user = {
  state: {
    token: '',
    name: '',
    welcome: '',
    avatar: '/avatar.jpg',
    permissions: [],
    info: {},
    lang: 'CN'
  },

  mutations: {
    SET_TOKEN: (state, token) => {
      state.token = token
    },
    SET_NAME: (state, { name, welcome }) => {
      state.name = name
      state.welcome = welcome
    },
    SET_AVATAR: (state, avatar) => {
      state.avatar = avatar
    },
    SET_PERMISSIONS: (state, permissions) => {
      state.permissions = permissions
    },
    SET_INFO: (state, info) => {
      state.info = info
    },
    SET_LANG: (state, lang) => {
      state.lang = lang
    }
  },

  actions: {
    // 登录
    Login ({ commit }, userInfo) {
      return new Promise((resolve, reject) => {
        login(userInfo).then(response => {
          if (response.success) {
            const result = response.result
            // 7天有效期
            Vue.ls.set(ACCESS_TOKEN, result.token, 7 * 24 * 60 * 60 * 1000)
            commit('SET_TOKEN', result.token)
          }
          resolve(response)
        }).catch(error => {
          reject(error)
        })
      })
    },

    // 注册
    Register ({ commit }, userInfo) {
      return new Promise((resolve, reject) => {
        register(userInfo).then(response => {
          const result = response.result
          // 7天有效期
          Vue.ls.set(ACCESS_TOKEN, result.token, 7 * 24 * 60 * 60 * 1000)
          commit('SET_TOKEN', result.token)
          resolve()
        }).catch(error => {
          reject(error)
        })
      })
    },

    // 获取用户信息
    GetInfo ({ commit }) {
      return new Promise((resolve, reject) => {
        getInfo().then(response => {
          const result = response.result
          commit('SET_PERMISSIONS', result.permissions)
          commit('SET_INFO', result)
          commit('SET_NAME', { name: result.name, welcome: welcome() })
          commit('SET_AVATAR', result.avatar)
          resolve(response)
        }).catch(error => {
          reject(error)
        })
      })
    },

    // 登出
    Logout ({ commit, state }) {
      return new Promise((resolve) => {
        logout(state.token).then(() => {
          resolve()
        }).catch(() => {
          resolve()
        }).finally(() => {
          commit('SET_TOKEN', '')
          commit('SET_PERMISSIONS', [])
          Vue.ls.remove(ACCESS_TOKEN)
        })
      })
    },

    SetLang ({ commit }, lang) {
      commit('SET_LANG', lang)
    }
  }
}

export default user
