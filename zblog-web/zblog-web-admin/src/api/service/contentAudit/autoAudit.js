import api from '../../custom-axios'

const autoAuditApi = '/api/autoAudits'

export default {
  // 分页查询自动审核记录列表
  pageAutoAuditRecordList: params => {
    return api.httpRequest().get(`${autoAuditApi}/pageList`, params)
  },
  // 查询自动审核记录详情
  queryAutoAuditRecordDetail: params => {
    return api.httpRequest().get(`${autoAuditApi}/queryAutoAuditRecordDetail`, params)
  }
}
