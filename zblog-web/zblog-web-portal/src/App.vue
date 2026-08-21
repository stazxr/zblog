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
      return this.$vuetify.breakpoint.smAndDown
    }
  },
  created() {
    // 获取当前登录用户信息
    this.getUserInfo()
    this.interval = window.setInterval(() => {
      this.getUserInfo()
    }, 60000)
    // 记录访客信息
    this.recordVisitor()
    // 初始化 Socket
    this.initWebSocket()
  },
  beforeDestroy() {
    const stomp = this.$store.state.ws.stomp
    if (stomp) {
      stomp.disconnect()
      console.log('websocket断开连接')
      this.$store.commit('SET_WS', null)
    }

    if (this.interval) {
      console.log('clear interval')
      clearInterval(this.interval)
    }
  },
  methods: {
    getUserInfo() {
      this.$mapi.portal.webLoginId().then(res => {
        this.$store.commit('setUserInfo', res.data)
      })
    },
    recordVisitor() {
      this.$mapi.portal.recordVisitor()
    },
    async initWebSocket() {
      try {
        await this.$ws.connect()
      } catch (e) {
        console.error('websocket init failed', e)
      }
    }
  }
}
</script>
