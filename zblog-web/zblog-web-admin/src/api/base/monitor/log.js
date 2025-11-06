import api from '../../custom-axios'

const logApi = '/api/logs'

export default {
  // 分页查询接口日志列表
  pageInterfaceLogList: params => {
    return api.httpRequest().get(`${logApi}/pageInterfaceLogList`, params)
  },
  // 分页查询操作日志列表
  pageOperationLogList: params => {
    return api.httpRequest().get(`${logApi}/pageOperationLogList`, params)
  },
  // 导出接口日志
  exportInterfaceLog: params => {
    return api.httpRequest().get(`${logApi}/exportInterfaceLog`, params, { responseType: 'blob' })
  },
  // 导出操作日志
  exportOperationLog: params => {
    return api.httpRequest().get(`${logApi}/exportOperationLog`, params, { responseType: 'blob' })
  },
  // 查询接口日志异常堆栈
  queryInterfaceLogExpDetail: params => {
    return api.httpRequest().get(`${logApi}/queryInterfaceLogExpDetail`, params)
  },
  // 查询操作日志异常堆栈
  queryOperationLogExpDetail: params => {
    return api.httpRequest().get(`${logApi}/queryOperationLogExpDetail`, params)
  },
  // 删除接口日志
  deleteInterfaceLog: params => {
    return api.httpRequest().post(`${logApi}/deleteInterfaceLog`, params)
  },
  // 删除操作日志
  deleteOperationLog: params => {
    return api.httpRequest().post(`${logApi}/deleteOperationLog`, params)
  }
}
