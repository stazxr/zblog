import qs from 'qs'
import api from '../custom-axios'

const logApi = '/api/logs'

export default {
  // 分页查询操作日志列表
  queryOperateLogsByPage: params => {
    return api.httpRequest().get(`${logApi}/queryOperateLogsByPage`, params)
  },
  // 分页查询接口日志列表
  queryApiLogsByPage: params => {
    return api.httpRequest().get(`${logApi}/queryApiLogsByPage`, params)
  },
  // 查询用户操作日志记录
  queryUserLog: params => {
    return api.httpRequest().get(`${logApi}/queryUserLog`, params)
  },
  // 导出操作日志
  exportOperateLog: params => {
    return api.httpRequest().get(`${logApi}/exportOperateLog`, params, { responseType: 'blob' })
  },
  // 导出接口日志
  exportAllLog: params => {
    return api.httpRequest().get(`${logApi}/exportAllLog`, params, { responseType: 'blob' })
  },
  // 删除日志列表
  deleteLog: params => {
    return api.httpRequest().post(`${logApi}/deleteLog`, qs.stringify(params), {
      headers: { 'Content-Type': 'application/x-www-form-urlencoded;charset=UTF-8' }
    })
  },
  // 查询日志堆栈详情
  queryLogErrorDetail: params => {
    return api.httpRequest().get(`${logApi}/queryLogErrorDetail`, params)
  }
}
