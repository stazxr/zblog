import qs from 'qs'
import api from '../../custom-axios'

const barrageMessageApi = '/api/barrageMessages'

export default {
  // 分页查询弹幕列表
  pageBarrageMessageList: params => {
    return api.httpRequest().get(`${barrageMessageApi}/pageList`, params)
  },
  // 查询弹幕详情
  queryBarrageMessageDetail: params => {
    return api.httpRequest().get(`${barrageMessageApi}/queryBarrageMessageDetail`, params)
  },
  // 审核弹幕
  auditBarrageMessage: params => {
    return api.httpRequest().post(`${barrageMessageApi}/auditBarrageMessage`, params)
  },
  // 删除弹幕
  deleteBarrageMessage: params => {
    return api.httpRequest().post(`${barrageMessageApi}/deleteBarrageMessage`, qs.stringify(params), {
      headers: { 'Content-Type': 'application/x-www-form-urlencoded;charset=UTF-8' }
    })
  }
}
