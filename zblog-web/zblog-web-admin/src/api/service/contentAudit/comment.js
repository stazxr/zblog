import api from '../../custom-axios'
import qs from 'qs'

const commentApi = '/api/comments'

export default {
  // 分页查询评论列表
  pageCommentList: params => {
    return api.httpRequest().get(`${commentApi}/pageList`, params)
  },
  // 查询评论详情
  queryCommentDetail: params => {
    return api.httpRequest().get(`${commentApi}/queryCommentDetail`, params)
  },
  // 审核评论
  auditComment: params => {
    return api.httpRequest().post(`${commentApi}/auditComment`, params)
  },
  // 删除评论
  deleteComment: params => {
    return api.httpRequest().post(`${commentApi}/deleteComment`, qs.stringify(params), {
      headers: { 'Content-Type': 'application/x-www-form-urlencoded;charset=UTF-8' }
    })
  }
}
