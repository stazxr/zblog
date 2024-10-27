import api from '../custom-axios'

const messageApi = '/api/messages'

export default {
  // 分页查询留言列表
  pageList: params => {
    return api.httpRequest().get(`${messageApi}/pageList`, params)
  },
  // 删除留言
  deleteMessage: params => {
    return api.httpRequest().post(`${messageApi}/deleteMessage`, params)
  },
  // 审核留言
  auditMessage: params => {
    return api.httpRequest().post(`${messageApi}/auditMessage`, params)
  }
}
