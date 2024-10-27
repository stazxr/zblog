import qs from 'qs'
import api from '../custom-axios'

const dictApi = '/api/dict'

export default {
  // 分页查询字典列表
  pageDictList: params => {
    return api.httpRequest().get(`${dictApi}/pageList`, params)
  },
  // 查询字典子列表
  queryChildList: params => {
    return api.httpRequest().get(`${dictApi}/queryChildList`, params)
  },
  // 查询字典详情
  queryDictDetail: params => {
    return api.httpRequest().get(`${dictApi}/queryDictDetail`, params)
  },
  // 新增字典
  addDict: params => {
    return api.httpRequest().post(`${dictApi}/addDict`, params)
  },
  // 编辑字典
  editDict: params => {
    return api.httpRequest().post(`${dictApi}/editDict`, params)
  },
  // 删除字典
  deleteDict: params => {
    return api.httpRequest().post(`${dictApi}/deleteDict`, qs.stringify(params), {
      headers: { 'Content-Type': 'application/x-www-form-urlencoded;charset=UTF-8' }
    })
  }
}
