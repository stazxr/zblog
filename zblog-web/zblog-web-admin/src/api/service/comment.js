import api from '../custom-axios'

const commentApi = '/api/comments'

export default {
  // 分页查询评论列表
  pageList: params => {
    return api.httpRequest().get(`${commentApi}/pageList`, params)
  },
  // 删除评论
  deleteComment: params => {
    return api.httpRequest().post(`${commentApi}/deleteComment`, params)
  },
  // 审核评论
  auditComment: params => {
    return api.httpRequest().post(`${commentApi}/auditComment`, params)
  }
}
