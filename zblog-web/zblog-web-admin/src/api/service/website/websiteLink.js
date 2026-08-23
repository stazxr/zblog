import api from '@/api/custom-axios'
import qs from 'qs'

const websiteLinkApi = '/api/websiteLinks'

export default {
  // 分页查询网站链接列表
  pageWebsiteLinkList: params => {
    return api.httpRequest().get(`${websiteLinkApi}/pageList`, params)
  },
  // 查询网站链接详情
  queryWebsiteLinkDetail: params => {
    return api.httpRequest().get(`${websiteLinkApi}/queryWebsiteLinkDetail`, params)
  },
  // 新增网站链接
  addWebsiteLink: params => {
    return api.httpRequest().post(`${websiteLinkApi}/addWebsiteLink`, params)
  },
  // 编辑网站链接
  editWebsiteLink: params => {
    return api.httpRequest().post(`${websiteLinkApi}/editWebsiteLink`, params)
  },
  // 删除网站链接
  deleteWebsiteLink: params => {
    return api.httpRequest().post(`${websiteLinkApi}/deleteWebsiteLink`, qs.stringify(params), {
      headers: { 'Content-Type': 'application/x-www-form-urlencoded;charset=UTF-8' }
    })
  }
}
