import api from '../../custom-axios'
import qs from 'qs'

const sessionApi = '/api/sessions'

export default {
  // 查询在线用户列表
  list: params => {
    return api.httpRequest().get(`${sessionApi}/list`, params)
  },
  // 踢出用户
  kickout: params => {
    return api.httpRequest().post(`${sessionApi}/kickout`, qs.stringify(params), {
      headers: { 'Content-Type': 'application/x-www-form-urlencoded;charset=UTF-8' }
    })
  }
}
