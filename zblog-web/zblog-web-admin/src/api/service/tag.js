import qs from 'qs'
import api from '../custom-axios'

const tagApi = '/api/tags'

export default {
  // 分页查询标签列表
  pageTagList: params => {
    return api.httpRequest().get(`${tagApi}/pageList`, params)
  },
  // 查询标签详情
  queryTagDetail: params => {
    return api.httpRequest().get(`${tagApi}/queryTagDetail`, params)
  },
  // 新增标签
  addTag: params => {
    return api.httpRequest().post(`${tagApi}/addTag`, params)
  },
  // 编辑标签
  editTag: params => {
    return api.httpRequest().post(`${tagApi}/editTag`, params)
  },
  // 删除标签
  deleteTag: params => {
    return api.httpRequest().post(`${tagApi}/deleteTag`, qs.stringify(params), {
      headers: { 'Content-Type': 'application/x-www-form-urlencoded;charset=UTF-8' }
    })
  }
}
