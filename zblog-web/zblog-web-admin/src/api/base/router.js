import qs from 'qs'
import api from '../custom-axios'

const routerApi = '/api/router'

export default {
  // 分页查询路由列表
  pageRouterList: params => {
    return api.httpRequest().get(`${routerApi}/pageList`, params)
  },
  // 分页查询黑白名单列表
  pageBlackOrWhiteList: params => {
    return api.httpRequest().get(`${routerApi}/pageBlackOrWhiteList`, params)
  },
  // 根据权限编码查询路由信息
  queryRouterByCode: params => {
    return api.httpRequest().get(`${routerApi}/queryRouterByCode`, params)
  },
  // 修改接口的日志展示状态
  changeLogShowStatus: params => {
    return api.httpRequest().post(`${routerApi}/updateLogShowStatus`, params)
  },
  // 新增黑白名单
  addBlackOrWhiteRouter: params => {
    return api.httpRequest().post(`${routerApi}/addBlackOrWhiteRouter`, params)
  },
  // 编辑黑白名单
  editBlackOrWhiteRouter: params => {
    return api.httpRequest().post(`${routerApi}/editBlackOrWhiteRouter`, params)
  },
  // 修改黑白名单状态
  changeBlackOrWhiteRouterStatus: params => {
    return api.httpRequest().post(`${routerApi}/changeBlackOrWhiteRouterStatus`, params)
  },
  // 删除黑白名单
  deleteBlackOrWhiteRouter: params => {
    return api.httpRequest().post(`${routerApi}/deleteBlackOrWhiteRouter`, qs.stringify(params), {
      headers: { 'Content-Type': 'application/x-www-form-urlencoded;charset=UTF-8' }
    })
  }
}
