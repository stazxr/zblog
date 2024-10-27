import qs from 'qs'
import api from '../custom-axios'

const pageApi = '/api/pages'

export default {
  // 查询页面列表
  pageList: params => {
    return api.httpRequest().get(`${pageApi}/queryPageList`, params)
  },
  // 查询页面详情
  queryPageDetail: params => {
    return api.httpRequest().get(`${pageApi}/queryPageDetail`, params)
  },
  // 新增或编辑页面
  addOrEditPage: params => {
    return api.httpRequest().post(`${pageApi}/addOrEditPage`, params)
  },
  // 删除页面
  deletePage: params => {
    return api.httpRequest().post(`${pageApi}/deletePage`, qs.stringify(params), {
      headers: { 'Content-Type': 'application/x-www-form-urlencoded;charset=UTF-8' }
    })
  }
}
