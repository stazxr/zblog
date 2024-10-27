import Vue from 'vue'
import Vuex from 'vuex'
import createPersistedState from 'vuex-persistedstate'

Vue.use(Vuex)

export default new Vuex.Store({
  state: {
    // 网站配置信息
    websiteConfig: {},
    // 社交配置信息
    socialConfig: {},
    // 其他配置信息
    otherConfig: {},
    // 计数信息
    countInfo: {
      // 文章数量
      articleCount: 0,
      // 分类数量
      categoryCount: 0,
      // 专栏数量
      columnCount: 0,
      // 标签数量
      tagCount: 0,
      // 访问总量
      viewsCount: 0,
      // 游客总量
      visitorCount: 0,
      // 用户总量
      userCount: 0,
      // 文章阅读数
      articleViewCount: 0,
      // 评论总量
      commentCount: 0,
      // 弹幕总量
      messageCount: 0
    },
    // 网站版本
    websiteVersion: '',
    // 文章默认封面
    articleDefaultImg: '',
    // 页面信息
    pageList: [],
    // 用户信息
    user: {
      id: null,
      nickname: '',
      avatar: '',
      gender: '',
      intro: '',
      webSite: ''
    },
    // 登录模态框是否显示
    loginFlag: false,
    // 注册模态框是否显示
    registerFlag: false,
    // 忘记密码模态框是否显示
    forgetFlag: false,
    // 侧边栏模态框
    drawerFlag: false,
    // 搜索模态框
    searchFlag: false,
    // 登录链接
    loginUrl: '',
    // 用户点赞列表
    commentLikeSet: [],
    articleLikeSet: [],
    talkLikeSet: []
  },
  mutations: {
    // 设置网站信息
    setBlogInfo(state, blogInfo) {
      state.websiteConfig = blogInfo['webInfo'] || {}
      state.socialConfig = blogInfo['socialInfo'] || {}
      state.otherConfig = blogInfo['otherInfo'] || {}
      state.countInfo.articleCount = blogInfo['articleCount'] || 0
      state.countInfo.categoryCount = blogInfo['categoryCount'] || 0
      state.countInfo.columnCount = blogInfo['columnCount'] || 0
      state.countInfo.tagCount = blogInfo['tagCount'] || 0
      state.countInfo.viewsCount = blogInfo['viewsCount'] || 0
      state.countInfo.visitorCount = blogInfo['visitorCount'] || 0
      state.countInfo.userCount = blogInfo['userCount'] || 0
      state.countInfo.articleViewCount = blogInfo['articleViewCount'] || 0
      state.countInfo.commentCount = blogInfo['commentCount'] || 0
      state.countInfo.messageCount = blogInfo['messageCount'] || 0
      state.websiteVersion = blogInfo['websiteVersion'] || ''
      state.articleDefaultImg = blogInfo['articleDefaultImg'] || ''
      state.pageList = blogInfo['pageList'] || []
    },
    // 登录成功，设置用户信息
    login(state, user) {
      state.user.id = user['id']
      state.user.nickname = user['nickname']
      state.user.avatar = user['headImgUrl']
      state.user.gender = user['gender']
      state.user.webSite = user['website']
      state.user.intro = user['signature']
      state.user.email = user['email']

      state.commentLikeSet = user.commentLikeSet ? user.commentLikeSet : []
      state.talkLikeSet = user.talkLikeSet ? user.talkLikeSet : []
      state.articleLikeSet = user.articleLikeSet ? user.articleLikeSet : []
    },
    // 登出
    logout(state) {
      state.user.id = null
      state.user.nickname = null
      state.user.avatar = null
      state.user.webSite = null
      state.user.intro = null
      state.user.email = null

      state.commentLikeSet = []
      state.talkLikeSet = []
      state.articleLikeSet = []
    },
    // 关闭模态框
    closeModel(state) {
      state.registerFlag = false
      state.loginFlag = false
      state.searchFlag = false
    },
    // 三方登录时，保存链接
    saveLoginUrl(state, url) {
      state.loginUrl = url
    },
    // 点赞评论
    commentLike(state, commentId) {
      const commentLikeSet = state.commentLikeSet
      if (commentLikeSet.indexOf(commentId) !== -1) {
        commentLikeSet.splice(commentLikeSet.indexOf(commentId), 1)
      } else {
        commentLikeSet.push(commentId)
      }
    },
    // 点赞文章
    articleLike(state, articleId) {
      const articleLikeSet = state.articleLikeSet
      if (articleLikeSet.indexOf(articleId) !== -1) {
        articleLikeSet.splice(articleLikeSet.indexOf(articleId), 1)
      } else {
        articleLikeSet.push(articleId)
      }
    },
    // 点赞说说
    talkLike(state, talkId) {
      const talkLikeSet = state.talkLikeSet
      if (talkLikeSet.indexOf(talkId) !== -1) {
        talkLikeSet.splice(talkLikeSet.indexOf(talkId), 1)
      } else {
        talkLikeSet.push(talkId)
      }
    }
  },
  actions: {},
  modules: {},
  plugins: [
    createPersistedState({
      storage: window.sessionStorage
    })
  ]
})
