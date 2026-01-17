import qs from 'qs'
import api from './custom-axios'

export default {
  // 获取系统公钥
  querySystemPublicKey: params => {
    return api.httpRequest().get(`/api/common/querySystemPublicKey`, params)
  },
  // 发送邮箱验证码
  sendEmailCode: params => {
    return api.httpRequest().post(`/api/common/sendEmailCode`, qs.stringify(params), {
      headers: { 'Content-Type': 'application/x-www-form-urlencoded;charset=UTF-8' }
    })
  },

  // 登录
  login: params => {
    return api.httpRequest().post(`/api/login`, params)
  },
  // 登出
  logout: params => {
    return api.httpRequest().post(`/api/logout`, params)
  },
  // 获取登录验证码
  loginCode: params => {
    return api.httpRequest().get(`/api/auth/loginCode`, params)
  },
  // 检查用户的登录状态
  checkUserLoginStatus: params => {
    return api.httpRequest().post(`/api/auth/checkUserLoginStatus`, params)
  },
  // 获取当前登录人信息
  loginId: params => {
    return api.httpRequest().get(`/api/auth/loginId`, params)
  },
  // 生成一个全局ID
  getId: params => {
    return api.httpRequest().get(`/api/id/getId`, params)
  },
  // 查询权限树列表（公共）
  queryPublicPermTree: params => {
    return api.httpRequest().get(`/api/perms/queryPublicPermTree`, params)
  },
  // 根据字典KEY查询配置信息列表
  queryConfListByDictKey: params => {
    return api.httpRequest().get(`/api/dict/queryConfListByDictKey`, params)
  },
  // 根据字典KEY查询配置信息
  queryDictValueByDictKey: params => {
    return api.httpRequest().get(`/api/dict/queryDictValueByDictKey`, params)
  }
}
