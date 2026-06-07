import api from '.././custom-axios'

const authApi = '/api/auth'

export default {
  // 获取当前登录用户信息
  loginId: params => {
    return api.httpRequest().get(`${authApi}/loginId`, params)
  },
  // 续签
  refresh: params => {
    return api.httpRequest().post(`${authApi}/refresh`, params)
  }
}
