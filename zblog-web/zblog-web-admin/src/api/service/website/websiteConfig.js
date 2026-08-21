import api from '@/api/custom-axios'

const websiteConfigApi = '/api/websiteConfig'

export default {
  // 查询网站配置详情
  queryWebsiteConfigDetail: params => {
    return api.httpRequest().get(`${websiteConfigApi}/detail`, params)
  },
  // 编辑网站配置
  editWebsiteConfig: params => {
    return api.httpRequest().post(`${websiteConfigApi}/edit`, params)
  }
}
