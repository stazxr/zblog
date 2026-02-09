import api from '../../custom-axios'

const cacheApi = '/api/cache'

export default {
  // 模糊查询缓存池数据
  scan: params => {
    return api.httpRequest().get(`${cacheApi}/scan`, params)
  },
  // 查看缓存详情
  value: params => {
    return api.httpRequest().get(`${cacheApi}/value`, params)
  },
  // 删除缓存
  delete: params => {
    return api.httpRequest().get(`${cacheApi}/delete`, params)
  }
}
