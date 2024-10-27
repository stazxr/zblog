import api from '../custom-axios'
import qs from 'qs'

const talkApi = '/api/talks'

export default {
  // 分页查询说说列表
  pageList: params => {
    return api.httpRequest().get(`${talkApi}/pageList`, params)
  },
  // 查询说说详情
  queryTalkDetail: params => {
    return api.httpRequest().get(`${talkApi}/queryTalkDetail`, params)
  },
  // 新增或编辑说说
  addOrEditTalk: params => {
    return api.httpRequest().post(`${talkApi}/addOrEditTalk`, params)
  },
  // 删除说说
  deleteTalk: params => {
    return api.httpRequest().post(`${talkApi}/deleteTalk`, qs.stringify(params), {
      headers: { 'Content-Type': 'application/x-www-form-urlencoded;charset=UTF-8' }
    })
  }
}
