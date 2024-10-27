<template>
  <v-app>
    <!-- 导航栏 -->
    <TopNavBar />
    <!-- 内容 -->
    <v-main>
      <router-view :key="$route.fullPath" />
    </v-main>
    <!-- 页脚 -->
    <Footer />
    <!-- 返回顶部 -->
    <BackTop />
    <!-- 侧边导航栏 -->
    <SideNavBar />
    <!-- 搜索模态框 -->
    <SearchModel />
    <!-- 登录模态框 -->
    <LoginModel />
    <!-- 注册模态框 -->
    <RegisterModel />
    <!-- 忘记密码模态框 -->
    <ForgetModel />
    <!-- 音乐播放器 -->
    <Player v-if="otherConfig['isMusicPlayer'] === 1 && !isMobile" />
    <!-- 聊天机器人：因为后台API突然无法使用，暂时关闭此功能 -->
    <Robot v-if="false" />
  </v-app>
</template>

<script>
import TopNavBar from './components/layout/TopNavBar'
import SideNavBar from './components/layout/SideNavBar'
import Footer from './components/layout/Footer'
import BackTop from './components/BackTop'
import SearchModel from './components/model/SearchModel'
import LoginModel from './components/model/LoginModel'
import RegisterModel from './components/model/RegisterModel'
import ForgetModel from './components/model/ForgetModel'
import Player from './components/zw-player/Player'
import Robot from './components/Robot'
export default {
  name: 'App',
  components: {
    TopNavBar, SideNavBar, Footer, BackTop, SearchModel, LoginModel, RegisterModel, ForgetModel, Player, Robot
  },
  data() {
    return {
      interval: null
    }
  },
  computed: {
    websiteConfig() {
      return this.$store.state.websiteConfig
    },
    otherConfig() {
      return this.$store.state.otherConfig
    },
    pageLoading() {
      return this.$store.state.pageLoading
    },
    isMobile() {
      return navigator.userAgent.match(
        /(phone|pad|pod|iPhone|iPod|ios|iPad|Android|Mobile|BlackBerry|IEMobile|MQQBrowser|JUC|Fennec|wOSBrowser|BrowserNG|WebOS|Symbian|Windows Phone)/i
      )
    }
  },
  created() {
    // 获取博客信息
    this.getBlogInfo()
    // 上传访客信息
    this.recordVisitor()

    // 每60秒刷新一次用户信息
    this.checkUserLoginStatus()
    this.interval = window.setInterval(() => {
      this.checkUserLoginStatus()
    }, 60000)
  },
  destroyed() {
    console.log('clear interval')
    clearInterval(this.interval)
  },
  methods: {
    getBlogInfo() {
      this.$mapi.portal.queryBlogInfo().then(res => {
        this.$store.commit('setBlogInfo', res.data)
        document.title = res.data['webInfo'] ? res.data['webInfo']['websiteName'] : 'Z-BLOG'
      })
    },
    recordVisitor() {
      this.$mapi.portal.recordVisitor()
    },
    checkUserLoginStatus() {
      console.log('开始检查用户的登录状态...')
      this.$mapi.other.checkUserLoginStatus().then(res => {
        if (res.code === 200 && res.data != null) {
          // 查询用户信息
          console.log('用户已登录，加载用户信息...')
          const userId = res.data.userId
          this.$mapi.portal.queryUserDetail({ userId: userId }).then(({ code, data, message }) => {
            if (code === 200) {
              this.$store.commit('login', data)
              this.userToken = data.userToken
            } else {
              this.$toast({ type: 'error', message: message })
            }
          }).catch(_ => {
            this.$toast({ type: 'error', message: '用户信息加载失败' })
          })
        } else {
          console.log('用户未登录...')
          this.$store.commit('logout')
        }
      })
    }
  }
}
</script>
