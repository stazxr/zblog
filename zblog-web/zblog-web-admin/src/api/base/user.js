import qs from 'qs'
import api from '../custom-axios'

const userApi = '/api/users'

export default {
  // 修改头像
  updateUserHeadImg: params => {
    return api.httpRequest().post(`${userApi}/updateUserHeadImg`, params)
  },
  // 修改个人基础信息
  updateUserBaseInfo: params => {
    return api.httpRequest().post(`${userApi}/updateUserBaseInfo`, params)
  },
  // 修改用户密码
  updateUserPass: params => {
    return api.httpRequest().post(`${userApi}/updateUserPass`, params)
  },
  // 强制修改用户密码
  forceUpdateUserPass: params => {
    return api.httpRequest().post(`${userApi}/forceUpdatePass`, params)
  },
  // 修改用户邮箱
  updateUserEmail: params => {
    return api.httpRequest().post(`${userApi}/updateUserEmail`, params)
  },
  // 分页查询用户列表（公共方法）
  pageListOfCommon: params => {
    return api.httpRequest().get(`${userApi}/pageListOfCommon`, params)
  },
  // 分页查询用户列表
  pageList: params => {
    return api.httpRequest().get(`${userApi}/pageList`, params)
  },
  // 查询用户详情
  queryUserDetail: params => {
    return api.httpRequest().get(`${userApi}/queryUserDetail`, params)
  },
  // 更新用户状态
  updateUserStatus: params => {
    return api.httpRequest().post(`${userApi}/updateUserStatus`, params)
  },
  // 新增用户
  addUser: params => {
    return api.httpRequest().post(`${userApi}/addUser`, params)
  },
  // 编辑用户
  editUser: params => {
    return api.httpRequest().post(`${userApi}/editUser`, params)
  },
  // 删除用户
  deleteUser: params => {
    return api.httpRequest().post(`${userApi}/deleteUser`, qs.stringify(params), {
      headers: { 'Content-Type': 'application/x-www-form-urlencoded;charset=UTF-8' }
    })
  }
}
