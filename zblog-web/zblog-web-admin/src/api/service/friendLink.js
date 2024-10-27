import api from '@/api/custom-axios'
import qs from 'qs'

const friendLinkApi = '/api/friendLinks'

export default {
  // 分页查询友链列表
  pageList: params => {
    return api.httpRequest().get(`${friendLinkApi}/pageList`, params)
  },
  // 查询友链详情
  queryFriendLinkDetail: params => {
    return api.httpRequest().get(`${friendLinkApi}/queryFriendLinkDetail`, params)
  },
  // 新增友链
  addFriendLink: params => {
    return api.httpRequest().post(`${friendLinkApi}/addFriendLink`, params)
  },
  // 编辑友链
  editFriendLink: params => {
    return api.httpRequest().post(`${friendLinkApi}/editFriendLink`, params)
  },
  // 删除友链
  deleteFriendLink: params => {
    return api.httpRequest().post(`${friendLinkApi}/deleteFriendLink`, qs.stringify(params), {
      headers: { 'Content-Type': 'application/x-www-form-urlencoded;charset=UTF-8' }
    })
  }
}
