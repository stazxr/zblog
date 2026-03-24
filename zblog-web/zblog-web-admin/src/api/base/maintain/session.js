import api from '../../custom-axios'

const sessionApi = '/api/sessions'

export default {
  // 查询在线用户列表
  list: params => {
    return api.httpRequest().get(`${sessionApi}/list`, params)
  },
  // 踢出用户
  delete: params => {
    return api.httpRequest().get(`${sessionApi}/kicout`, params)
  }
}
