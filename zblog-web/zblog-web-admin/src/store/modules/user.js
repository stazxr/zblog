import communal from '@/api/communal'
import { setToken, removeToken } from '@/utils/token'

const user = {
  state: {
    user: null,
    loadMenus: false
  },
  mutations: {
    SET_USER: (state, user) => {
      state.user = user
    },
    SET_LOAD_MENUS: (state, loadMenus) => {
      state.loadMenus = loadMenus
    }
  },
  actions: {
    // 登录
    Login({ commit }, loginParam) {
      return new Promise((resolve, reject) => {
        communal.login(loginParam).then(res => {
          const { access_token, change_pwd } = res.data
          if (!change_pwd) {
            // 查询用户信息
            setToken(access_token)
            commit('SET_LOAD_MENUS', true)
            communal.loginId().then(res => {
              commit('SET_USER', res.data)
              resolve(change_pwd)
            })
          } else {
            resolve(change_pwd)
          }
        }).catch(error => {
          reject(error)
        })
      })
    },
    GetUserInfo({ commit }) {
      return new Promise((resolve, reject) => {
        communal.checkUserLoginStatus().then(res => {
          if (res.code === 200 && res.data != null) {
            const userToken = res.data['accessToken']
            setToken(userToken)
          }

          communal.loginId().then(res => {
            commit('SET_USER', res.data)
            resolve(res)
          }).catch(error => {
            reject(error)
          })
        }).catch(error => {
          reject(error)
        })
      })
    },
    Logout({ commit }) {
      return new Promise((resolve, reject) => {
        communal.logout().then(_ => {
          logout(commit)
          resolve()
        }).catch(error => {
          logout(commit)
          reject(error)
        })
      })
    }
  }
}

export const logout = (commit) => {
  commit('SET_USER', null)
  removeToken()
}

export default user
