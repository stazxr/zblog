import api from '../../custom-axios'
import qs from 'qs'

const sensitiveWordApi = '/api/sensitiveWords'

export default {
  // 分页查询敏感词列表
  pageSensitiveWordList: params => {
    return api.httpRequest().get(`${sensitiveWordApi}/pageList`, params)
  },
  // 新增敏感词
  addSensitiveWord: params => {
    return api.httpRequest().post(`${sensitiveWordApi}/addSensitiveWord`, params)
  },
  // 编辑敏感词
  editSensitiveWord: params => {
    return api.httpRequest().post(`${sensitiveWordApi}/editSensitiveWord`, params)
  },
  // 删除敏感词
  deleteSensitiveWord: params => {
    return api.httpRequest().post(`${sensitiveWordApi}/deleteSensitiveWord`, qs.stringify(params), {
      headers: { 'Content-Type': 'application/x-www-form-urlencoded;charset=UTF-8' }
    })
  }
}
