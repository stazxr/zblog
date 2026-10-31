import api from './custom-axios'

const userCenterApi = '/api/user/center'

export default {
  // 强制修改个人密码
  forceUpdateUserPass: params => {
    return api.httpRequest().post(`${userCenterApi}/forceUpdatePass`, params)
  },
  // 修改个人密码
  updateUserPass: params => {
    return api.httpRequest().post(`${userCenterApi}/updateUserPass`, params)
  },
  // 修改个人头像
  updateUserHeadImg: params => {
    return api.httpRequest().post(`${userCenterApi}/updateUserHeadImg`, params)
  },
  // 修改个人邮箱
  updateUserEmail: params => {
    return api.httpRequest().post(`${userCenterApi}/updateUserEmail`, params)
  },
  // 修改个人信息
  updateUserProfileInfo: params => {
    return api.httpRequest().post(`${userCenterApi}/updateUserProfileInfo`, params)
  },
  // 查询个人微信公众号配置信息
  queryUserWechatAccountInfo: params => {
    return api.httpRequest().get(`${userCenterApi}/queryUserWechatAccountInfo`, params)
  },
  // 修改个人微信公众号配置信息
  updateUserWechatAccountInfo: params => {
    return api.httpRequest().post(`${userCenterApi}/updateUserWechatAccountInfo`, params)
  },
  // 分页查询用户操作日志列表
  pageUserLogList: params => {
    return api.httpRequest().get(`${userCenterApi}/pageUserLogList`, params)
  },
  // 分页查询用户登录日志列表
  pageUserLoginLogList: params => {
    return api.httpRequest().get(`${userCenterApi}/pageUserLoginLogList`, params)
  }
}
