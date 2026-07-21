import api from '../../custom-axios'

const visitorApi = '/api/visitor'

export default {
  // 分页查询访客列表
  pageVisitorList: params => {
    return api.httpRequest().get(`${visitorApi}/pageList`, params)
  },
  // 查询访客详情
  queryVisitorDetail: params => {
    return api.httpRequest().get(`${visitorApi}/queryVisitorDetail`, params)
  }
}
