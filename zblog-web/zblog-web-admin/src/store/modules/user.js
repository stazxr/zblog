import login from '@/api/login'
import communal from '@/api/communal'

const user = {
  state: {
    // 登录用户信息
    user: null,
    // 用户权限信息
    perms: [],
    // 用户菜单信息
    menus: []
  },
  mutations: {
    /**
     * 设置用户信息
     */
    SET_USER: (state, data) => {
      if (data == null) {
        state.user = null
        state.perms = []
        state.menus = []
      } else {
        state.user = data.user
        state.perms = data.perms || []
        state.menus = data.menus || []
      }
    }
  },
  actions: {
    // 登录
    async Login({ commit }, loginParam) {
      await login.login(loginParam)
      const res = await communal.loginId()
      commit('SET_USER', res.data)
      const user = res.data.user
      return (user && user.passwordExpireTime == null)
    },
    // 刷新用户信息
    async RefreshUser({ commit }) {
      const res = await communal.loginId()
      commit('SET_USER', res.data)
      return res.data
    },
    // 登出
    async Logout({ commit }) {
      try {
        await login.logout()
      } catch (e) {
        console.warn('logout error:', e)
      } finally {
        commit('SET_USER', null)
        commit('RESET_ROUTERS')
      }
    }
  }
}

export default user
