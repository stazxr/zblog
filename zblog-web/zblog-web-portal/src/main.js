import Vue from 'vue'
import App from './App.vue'
// 路由组件
import router from './router/routers'
// Vuex
import store from './store'
// Vuetify
import vuetify from './plugins/vuetify'
// Api
import api from './api/http-index'
// WS
import ws from '@/utils/websocket'
// 消息提醒
import Toast from './components/toast/index'
// 确认框
import Confirm from './components/confirm/index'
// 动画
import animated from 'animate.css'
import './assets/styles/animation.css'
// 样式
import './assets/styles/index.css'
import './assets/styles/variable.css'
// 图标
import './assets/iconfont/iconfont.js'
import './assets/iconfont/iconfont.css'
// 图片预览
import VueImageSwipe from 'vue-image-swipe'
import 'vue-image-swipe/dist/vue-image-swipe.css'
// 无限加载
import InfiniteLoading from 'vue-infinite-loading'
// 弹幕墙
import { vueBaberrage } from 'vue-baberrage'
// 日期工具
import dayjs from 'dayjs'
// 代码高亮
import Highlight from './directive/highlight'
import loading from './components/loading/index'

// 安装插件
Vue.use(animated)
Vue.use(InfiniteLoading)
Vue.use(vueBaberrage)
Vue.use(Toast)
Vue.use(Confirm)
Vue.use(VueImageSwipe)
Vue.use(Highlight)
Vue.use(loading)

// 全局挂载
Vue.prototype['$mapi'] = api
Vue.prototype['$ws'] = ws

// 关闭提示
Vue.config.productionTip = false

// 过滤器
Vue.filter('year', function(value) {
  return dayjs(value).format('YYYY')
})

Vue.filter('hour', function(value) {
  return dayjs(value).format('HH:mm:ss')
})

new Vue({
  el: '#app',
  router,
  store,
  vuetify,
  render: h => h(App)
})
