import api from '../../custom-axios'

const sensitiveWordApi = '/api/sensitiveWords'

export default {
  // 分页查询敏感词列表
  pageSensitiveWordList: params => {
    return api.httpRequest().get(`${sensitiveWordApi}/pageList`, params)
  }
}
