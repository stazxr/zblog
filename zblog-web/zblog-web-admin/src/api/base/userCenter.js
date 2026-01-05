import api from '../custom-axios'

const userCenterApi = '/api/user/center'

export default {
  // 强制修改用户密码
  forceUpdateUserPass: params => {
    return api.httpRequest().post(`${userCenterApi}/forceUpdatePass`, params)
  },
  // 修改用户密码
  updateUserPass: params => {
    return api.httpRequest().post(`${userCenterApi}/updateUserPass`, params)
  },
  // 修改头像
  updateUserHeadImg: params => {
    return api.httpRequest().post(`${userCenterApi}/updateUserHeadImg`, params)
  }
}
