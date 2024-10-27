import api from './custom-axios'

export default {
  // 获取面板
  getHomePanelDataCount: params => {
    return api.httpRequest().get(`/api/home/getHomePanelDataCount`, params)
  },
  // 根据图标类型，获取图标的详细数据
  getHomePanelDetailDataByType: params => {
    return api.httpRequest().get(`/api/home/getHomePanelDetailDataByType`, params)
  },
  // 获取首页面板的访客地域数据
  getHomePanelVisitorAreaCount: params => {
    return api.httpRequest().get(`/api/home/getHomePanelVisitorAreaCount`, params)
  }
}
