import api from '../custom-axios'

const webSettingApi = '/api/webSetting'

export default {
  // 查询网站配置信息
  queryWebInfo: params => {
    return api.httpRequest().get(`${webSettingApi}/queryWebInfo`, params)
  },
  // 修改网站配置信息
  updateWebInfo: params => {
    return api.httpRequest().post(`${webSettingApi}/updateWebInfo`, params)
  },
  // 查询网站社交信息
  querySocialInfo: params => {
    return api.httpRequest().get(`${webSettingApi}/querySocialInfo`, params)
  },
  // 修改网站社交信息
  updateSocialInfo: params => {
    return api.httpRequest().post(`${webSettingApi}/updateSocialInfo`, params)
  },
  // 查询网站其他信息
  queryOtherInfo: params => {
    return api.httpRequest().get(`${webSettingApi}/queryOtherInfo`, params)
  },
  // 修改网站其他信息
  updateOtherInfo: params => {
    return api.httpRequest().post(`${webSettingApi}/updateOtherInfo`, params)
  }
}
