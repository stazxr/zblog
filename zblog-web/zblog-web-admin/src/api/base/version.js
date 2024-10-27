import qs from 'qs'
import api from '../custom-axios'

const versionApi = '/api/version'

export default {
  // 分页查询版本列表
  pageVersionList: params => {
    return api.httpRequest().get(`${versionApi}/pageList`, params)
  },
  // 查询版本信息
  queryVersionInfo: params => {
    return api.httpRequest().get(`${versionApi}/queryVersionInfo`, params)
  },
  // 新增版本
  addVersion: params => {
    return api.httpRequest().post(`${versionApi}/addVersion`, params)
  },
  // 编辑版本
  editVersion: params => {
    return api.httpRequest().post(`${versionApi}/editVersion`, params)
  },
  // 删除版本
  deleteVersion: params => {
    return api.httpRequest().post(`${versionApi}/deleteVersion`, qs.stringify(params), {
      headers: { 'Content-Type': 'application/x-www-form-urlencoded;charset=UTF-8' }
    })
  }
}
