import qs from 'qs'
import api from './custom-axios'

const commonApi = '/api/common'

export default {
  // 获取系统公钥
  querySystemPublicKey: params => {
    return api.httpRequest().get(`${commonApi}/querySystemPublicKey`, params)
  },
  // 发送邮箱验证码
  sendEmailCode: params => {
    return api.httpRequest().post(`${commonApi}/sendEmailCode`, qs.stringify(params), {
      headers: { 'Content-Type': 'application/x-www-form-urlencoded;charset=UTF-8' }
    })
  },
  // 获取当前登录人信息
  loginId: params => {
    return api.httpRequest().get(`/api/auth/loginId`, params)
  },
  // 根据字典KEY查询配置信息列表
  queryConfListByDictKey: params => {
    return api.httpRequest().get(`/api/dict/queryConfListByDictKey`, params)
  },
  // 根据字典KEY查询配置信息
  queryDictValueByDictKey: params => {
    return api.httpRequest().get(`/api/dict/queryDictValueByDictKey`, params)
  },
  // 生成一个全局ID
  getId: params => {
    return api.httpRequest().get(`/api/id/getId`, params)
  }
}
