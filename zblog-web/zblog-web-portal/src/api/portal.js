import qs from 'qs'
import api from './custom-axios'

const portalApi = '/api/portal'

export default {
  // 登录
  webLogin: params => {
    return api.httpRequest().post(`${portalApi}/webLogin`, params)
  },
  // 查询博客前台信息
  queryBlogInfo: params => {
    return api.httpRequest().get(`${portalApi}/queryBlogInfo`, params)
  },
  // 记录访客信息
  recordVisitor: params => {
    return api.httpRequest().post(`${portalApi}/recordVisitor`, params)
  },
  // 获取文章列表
  queryArticleList: params => {
    return api.httpRequest().get(`${portalApi}/queryArticleList`, params)
  },
  // 获取文章详情
  queryArticleDetail: params => {
    return api.httpRequest().get(`${portalApi}/queryArticleDetail`, params)
  },
  // 获取弹幕列表
  queryMessageList: params => {
    return api.httpRequest().get(`${portalApi}/queryMessageList`, params)
  },
  // 发送弹幕
  saveMessage: params => {
    return api.httpRequest().post(`${portalApi}/saveMessage`, params)
  },
  // 获取友链列表
  queryFriendLinkList: params => {
    return api.httpRequest().get(`${portalApi}/queryFriendLinkList`, params)
  },
  // 获取评论列表
  queryCommentList: params => {
    return api.httpRequest().get(`${portalApi}/queryCommentList`, params)
  },
  // 保存评论
  saveComment: params => {
    return api.httpRequest().post(`${portalApi}/saveComment`, params)
  },
  // 点赞评论
  likeComment: params => {
    return api.httpRequest().post(`${portalApi}/likeComment`, params)
  },
  // 回复评论
  replyComment: params => {
    return api.httpRequest().post(`${portalApi}/replyComment`, params)
  },
  // 删除评论
  deleteComment: params => {
    return api.httpRequest().post(`${portalApi}/deleteComment`, params)
  },
  // 获取评论回复列表
  queryCommentReplyList: params => {
    return api.httpRequest().get(`${portalApi}/queryCommentReplyList`, params)
  },
  // 获取轮播的说说列表
  queryBoardTalkList: params => {
    return api.httpRequest().get(`${portalApi}/queryBoardTalkList`, params)
  },
  // 获取标签云数据
  queryBoardTagList: params => {
    return api.httpRequest().get(`${portalApi}/queryBoardTagList`, params)
  },
  // 获取热门文章列表
  queryBoardHotArticleList: params => {
    return api.httpRequest().get(`${portalApi}/queryBoardHotArticleList`, params)
  },
  // 获取分类专栏列表
  queryBoardCategoryList: params => {
    return api.httpRequest().get(`${portalApi}/queryBoardCategoryList`, params)
  },
  // 获取最新评论列表
  queryBoardLastedCommentList: params => {
    return api.httpRequest().get(`${portalApi}/queryBoardLastedCommentList`, params)
  },
  // 获取说说列表
  queryTalkList: params => {
    return api.httpRequest().get(`${portalApi}/queryTalkList`, params)
  },
  // 获取说说详情
  queryTalkById: params => {
    return api.httpRequest().get(`${portalApi}/queryTalkById`, params)
  },
  // 点赞说说
  likeTalk: params => {
    return api.httpRequest().post(`${portalApi}/likeTalk`, params)
  },
  // 获取标签列表
  queryTagList: params => {
    return api.httpRequest().get(`${portalApi}/queryTagList`, params)
  },
  // 获取分类列表
  queryCategoryList: params => {
    return api.httpRequest().get(`${portalApi}/queryCategoryList`, params)
  },
  // 获取文章归档列表
  queryArchiveList: params => {
    return api.httpRequest().get(`${portalApi}/queryArchiveList`, params)
  },
  // 获取分类详情
  queryCategoryById: params => {
    return api.httpRequest().get(`${portalApi}/queryCategoryById`, params)
  },
  // 获取标签详情
  queryTagById: params => {
    return api.httpRequest().get(`${portalApi}/queryTagById`, params)
  },
  // 点赞文章
  likeArticle: params => {
    return api.httpRequest().post(`${portalApi}/likeArticle`, params)
  },
  // 浏览文章
  viewArticle: params => {
    return api.httpRequest().post(`${portalApi}/viewArticle`, qs.stringify(params), {
      headers: { 'Content-Type': 'application/x-www-form-urlencoded;charset=UTF-8' }
    })
  },
  // 根据关键字搜索文章
  searchArticleList: params => {
    return api.httpRequest().get(`${portalApi}/searchArticleList`, params)
  },
  // 查询 Github 贡献日历数据
  queryGithubCalendarData: params => {
    return api.httpRequest().get(`${portalApi}/queryGithubCalendarData`, params)
  },
  // 获取相册列表
  queryAlbumList: params => {
    return api.httpRequest().get(`${portalApi}/queryAlbumList`, params)
  },
  // 获取相册照片列表
  queryAlbumPhotoList: params => {
    return api.httpRequest().get(`${portalApi}/queryAlbumPhotoList`, params)
  },
  // 获取专栏列表
  queryColumnList: params => {
    return api.httpRequest().get(`${portalApi}/queryColumnList`, params)
  },
  // 获取专栏详情
  queryColumnById: params => {
    return api.httpRequest().get(`${portalApi}/queryColumnById`, params)
  },
  // 获取专栏文章列表
  queryColumnArticleList: params => {
    return api.httpRequest().get(`${portalApi}/queryColumnArticleList`, params)
  },
  // 查询前台版本列表
  queryVersionList: params => {
    return api.httpRequest().get(`${portalApi}/queryVersionList`, params)
  },
  // QQ 登录
  loginQq: params => {
    return api.httpRequest().post(`${portalApi}/oauth/login/qq`, params)
  },
  // 查询用户详情
  queryUserDetail: params => {
    return api.httpRequest().get(`${portalApi}/queryUserDetail`, params)
  }
}
