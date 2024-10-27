import qs from 'qs'
import api from '../custom-axios'

const articleApi = '/api/articles'

export default {
  // 分页查询用户文章列表
  pageArticleList: params => {
    return api.httpRequest().get(`${articleApi}/pageList`, params)
  },
  // 分页查询用户文章列表
  pageAuditArticleList: params => {
    return api.httpRequest().get(`${articleApi}/pagePublicList`, params)
  },
  // 查询文章详情
  queryArticleDetail: params => {
    return api.httpRequest().get(`${articleApi}/queryArticleDetail`, params)
  },
  // 新增文章
  addArticle: params => {
    return api.httpRequest().post(`${articleApi}/addArticle`, params)
  },
  // 编辑文章
  editArticle: params => {
    return api.httpRequest().post(`${articleApi}/editArticle`, params)
  },
  // 保存草稿
  saveArticleDraft: params => {
    return api.httpRequest().post(`${articleApi}/saveArticleDraft`, params)
  },
  // 文章内容自动保存
  autoSaveArticleContent: params => {
    return api.httpRequest().post(`${articleApi}/autoSaveArticleContent`, params)
  },
  // 文章自动发布
  publishArticleByTiming: params => {
    return api.httpRequest().post(`${articleApi}/publishArticleByTiming`, params)
  },
  // 查询最新的文章草稿
  queryLastArticleDraft: params => {
    return api.httpRequest().get(`${articleApi}/queryLastArticleDraft`, params)
  },
  // 分页查询文章内容自动保存列表
  queryAutoSaveArticleContentByPage: params => {
    return api.httpRequest().get(`${articleApi}/queryAutoSaveArticleContentByPage`, params)
  },
  // 查询文章内容自动保存信息
  queryAutoSaveArticleContentById: params => {
    return api.httpRequest().get(`${articleApi}/queryAutoSaveArticleContentById`, params)
  },
  // 删除文章：移动至回收站
  moveToRecycleBin: params => {
    return api.httpRequest().post(`${articleApi}/moveToRecycleBin`, qs.stringify(params), {
      headers: { 'Content-Type': 'application/x-www-form-urlencoded;charset=UTF-8' }
    })
  },
  // 将文章回收至草稿箱
  recycleToDraftBox: params => {
    return api.httpRequest().post(`${articleApi}/recycleToDraftBox`, qs.stringify(params), {
      headers: { 'Content-Type': 'application/x-www-form-urlencoded;charset=UTF-8' }
    })
  },
  // 彻底删除文章
  deleteArticle: params => {
    return api.httpRequest().post(`${articleApi}/deleteArticle`, qs.stringify(params), {
      headers: { 'Content-Type': 'application/x-www-form-urlencoded;charset=UTF-8' }
    })
  },
  // 删除文章：批量移动至回收站
  batchMoveToRecycleBin: params => {
    return api.httpRequest().post(`${articleApi}/batchMoveToRecycleBin`, params)
  },
  // 清空回收站
  clearRecycleBin: params => {
    return api.httpRequest().post(`${articleApi}/clearRecycleBin`, params)
  },
  // 文章审核
  auditArticle: params => {
    return api.httpRequest().post(`${articleApi}/auditArticle`, params)
  },
  // 查询分类列表
  queryCategoryTree: params => {
    return api.httpRequest().get(`${articleApi}/queryCategoryTree`, params)
  },
  // 查询标签列表
  queryTagList: params => {
    return api.httpRequest().get(`${articleApi}/queryTagList`, params)
  },
  // 查询文章默认封面
  queryArticleDefaultImg: params => {
    return api.httpRequest().get(`${articleApi}/queryArticleDefaultImg`, params)
  }
}
