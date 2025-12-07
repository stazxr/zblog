import qs from 'qs'
import api from './../../custom-axios'

const permApi = '/api/perms'

export default {
  // 查询权限树列表
  queryPermTree: params => {
    return api.httpRequest().get(`${permApi}/queryPermTree`, params)
  },
  // 查询权限详细信息
  queryPermDetail: params => {
    return api.httpRequest().get(`${permApi}/queryPermDetail`, params)
  },
  // 查询权限编码列表
  queryPermCodes: params => {
    return api.httpRequest().get(`${permApi}/queryPermCodes`, params)
  },
  // 根据权限编码查询资源信息
  queryResourceByPermCode: params => {
    return api.httpRequest().get(`${permApi}/queryResourceByPermCode`, params)
  },
  // 新增权限
  addPerm: params => {
    return api.httpRequest().post(`${permApi}/addPerm`, params)
  },
  // 编辑权限
  editPerm: params => {
    return api.httpRequest().post(`${permApi}/editPerm`, params)
  },
  // 删除权限
  deletePerm: params => {
    return api.httpRequest().post(`${permApi}/deletePerm`, qs.stringify(params), {
      headers: { 'Content-Type': 'application/x-www-form-urlencoded;charset=UTF-8' }
    })
  }
}
