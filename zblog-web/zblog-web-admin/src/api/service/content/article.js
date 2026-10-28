import qs from 'qs'
import api from '../../custom-axios'

const articleApi = '/api/articles'

export default {
  // 分页查询我的文章列表
  pageMyArticleList: params => {
    return api.httpRequest().get(`${articleApi}/pageMyList`, params)
  },
  // 查询我的文章数量统计信息
  queryMyArticleCountInfo: params => {
    return api.httpRequest().get(`${articleApi}/queryMyArticleCountInfo`, params)
  },
  // 查询我的文章详情
  queryMyArticleDetail: params => {
    return api.httpRequest().get(`${articleApi}/queryMyArticleDetail`, params)
  },
  // 初始化文章
  initArticle: params => {
    return api.httpRequest().post(`${articleApi}/initArticle`, params)
  },
  // 保存文章
  saveArticle: params => {
    return api.httpRequest().post(`${articleApi}/saveArticle`, params)
  },
  // 发布文章
  publishArticle: params => {
    return api.httpRequest().post(`${articleApi}/publishArticle`, params)
  },
  // 定时发布文章
  autoPublishArticle: params => {
    return api.httpRequest().post(`${articleApi}/autoPublishArticle`, params)
  },
  // 删除文章
  deleteArticle: params => {
    return api.httpRequest().post(`${articleApi}/deleteArticle`, qs.stringify(params), {
      headers: { 'Content-Type': 'application/x-www-form-urlencoded;charset=UTF-8' }
    })
  },
  deleteArticle2: params => {
    return api.httpRequest().post(`${articleApi}/deleteArticle2`, qs.stringify(params), {
      headers: { 'Content-Type': 'application/x-www-form-urlencoded;charset=UTF-8' }
    })
  },
  deleteArticle3: params => {
    return api.httpRequest().post(`${articleApi}/deleteArticle3`, params)
  },
  // 还原文章（移至草稿箱）
  resetArticle: params => {
    return api.httpRequest().post(`${articleApi}/resetArticle`, qs.stringify(params), {
      headers: { 'Content-Type': 'application/x-www-form-urlencoded;charset=UTF-8' }
    })
  },
  // 文章内容自动保存
  autoSaveArticleContent: params => {
    return api.httpRequest().post(`${articleApi}/autoSaveArticleContent`, params)
  },
  // 分页查询文章内容自动保存列表
  queryAutoSaveArticleContentByPage: params => {
    return api.httpRequest().get(`${articleApi}/queryAutoSaveArticleContentByPage`, params)
  },
  // 查询文章内容自动保存详情
  queryAutoSaveArticleContentById: params => {
    return api.httpRequest().get(`${articleApi}/queryAutoSaveArticleContentById`, params)
  },
  // 查询最新的文章草稿
  queryLastArticleDraft: params => {
    return api.httpRequest().get(`${articleApi}/queryLastArticleDraft`, params)
  },
  // 分页查询文章操作日志列表
  pageArticleOperationLogList: params => {
    return api.httpRequest().get(`${articleApi}/pageArticleOperationLogList`, params)
  },
  // 分页查询文章发布版本列表
  pageArticleVersionList: params => {
    return api.httpRequest().get(`${articleApi}/pageArticleVersionList`, params)
  },
  // 微信公众号推送文章
  publishWeChat: params => {
    return api.httpRequest().post(`${articleApi}/publishWeChat`, params)
  }
}
