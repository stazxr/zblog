import qs from 'qs'
import api from '../custom-axios'

const roleApi = '/api/roles'

export default {
  // 查询角色列表
  roleList: params => {
    return api.httpRequest().get(`${roleApi}/list`, params)
  },
  // 分页查询角色列表
  pageRoleList: params => {
    return api.httpRequest().get(`${roleApi}/pageList`, params)
  },
  // 查询角色详情
  queryRoleDetail: params => {
    return api.httpRequest().get(`${roleApi}/queryRoleDetail`, params)
  },
  // 新增角色
  addRole: params => {
    return api.httpRequest().post(`${roleApi}/addRole`, params)
  },
  // 编辑角色
  editRole: params => {
    return api.httpRequest().post(`${roleApi}/editRole`, params)
  },
  // 删除角色
  deleteRole: params => {
    return api.httpRequest().post(`${roleApi}/deleteRole`, qs.stringify(params), {
      headers: { 'Content-Type': 'application/x-www-form-urlencoded;charset=UTF-8' }
    })
  },
  // 授权角色
  authRole: params => {
    return api.httpRequest().post(`${roleApi}/authRole`, params)
  },
  // 查询角色对应的权限ID列表
  queryPermIdsByRoleId: params => {
    return api.httpRequest().get(`${roleApi}/queryPermIdsByRoleId`, params)
  },
  // 分页查询角色对应的用户列表
  pageUsersByRoleId: params => {
    return api.httpRequest().get(`${roleApi}/pageUsersByRoleId`, params)
  },
  // 新增用户角色
  batchAddUserRole: params => {
    return api.httpRequest().post(`${roleApi}/batchAddUserRole`, params)
  },
  // 删除用户角色
  batchDeleteUserRole: params => {
    return api.httpRequest().post(`${roleApi}/batchDeleteUserRole`, params)
  }
}
