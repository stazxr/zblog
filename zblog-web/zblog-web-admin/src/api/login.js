import api from './custom-axios'

export default {
  // 获取登录验证码
  loginCode: params => {
    return api.httpRequest().get(`/api/loginCode`, params)
  },
  // 用户登录
  login: params => {
    return api.httpRequest().post(`/api/login`, params)
  },
  // 用户登出
  logout: params => {
    return api.httpRequest().post(`/api/logout`, params)
  }
}
