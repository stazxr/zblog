import api from './custom-axios'

const sequenceApi = '/api/sequence'

export default {
  // 生成唯一序列
  getId: params => {
    return api.httpRequest().get(`${sequenceApi}/getId`, params)
  },
  // 生成唯一序列列表
  getIds: params => {
    return api.httpRequest().get(`${sequenceApi}/getIds`, params)
  }
}
