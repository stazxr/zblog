import api from './custom-axios'
import qs from 'qs'

export default {
  // 发送邮箱验证码
  sendEmailCode: params => {
    return api.httpRequest().post(`/api/email/sendCode`, params)
  },
  // 用户注册
  userRegister: params => {
    return api.httpRequest().post(`/api/users/register`, params)
  },
  // 通过邮箱修改密码
  updateUserPwdByEmail: params => {
    return api.httpRequest().post(`/api/users/updateUserPwdByEmail`, params)
  },
  // 检查用户的登录状态
  checkUserLoginStatus: params => {
    return api.httpRequest().post(`/api/auth/checkUserLoginStatus`, params)
  },
  // 查询用户详情
  logout: params => {
    return api.httpRequest().post(`/api/logout/custom`, qs.stringify(params), {
      headers: { 'Content-Type': 'application/x-www-form-urlencoded;charset=UTF-8' }
    })
  }
}
