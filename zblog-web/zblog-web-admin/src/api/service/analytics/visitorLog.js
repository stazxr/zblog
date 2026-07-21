import api from '../../custom-axios'

const visitorLogApi = '/api/visitorLog'

export default {
  // 分页查询访客日志列表
  pageVisitorLogList: params => {
    return api.httpRequest().get(`${visitorLogApi}/pageList`, params)
  },
  // 查询访客日志详情
  queryVisitorLogDetail: params => {
    return api.httpRequest().get(`${visitorLogApi}/queryVisitorLogDetail`, params)
  }
}
