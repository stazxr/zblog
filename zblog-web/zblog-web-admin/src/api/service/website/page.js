import qs from 'qs'
import api from '../../custom-axios'

const pageApi = '/api/pages'

export default {
  // 分页查询页面列表
  pagePageList: params => {
    return api.httpRequest().get(`${pageApi}/pageList`, params)
  },
  // 查询页面列表（公共）
  pageList: params => {
    return api.httpRequest().get(`${pageApi}/list`, params)
  },
  // 查询页面详情
  queryPageDetail: params => {
    return api.httpRequest().get(`${pageApi}/queryPageDetail`, params)
  },
  // 新增页面
  addPage: params => {
    return api.httpRequest().post(`${pageApi}/addPage`, params)
  },
  // 编辑页面
  editPage: params => {
    return api.httpRequest().post(`${pageApi}/editPage`, params)
  },
  // 删除页面
  deletePage: params => {
    return api.httpRequest().post(`${pageApi}/deletePage`, qs.stringify(params), {
      headers: { 'Content-Type': 'application/x-www-form-urlencoded;charset=UTF-8' }
    })
  }
}
