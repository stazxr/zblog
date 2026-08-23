import Vue from 'vue'
import App from './App.vue'
// 路由组件
import '@/router/index'
import router from '@/router/routers'
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
// 头像获取
import { getAvatar } from '@/utils/avatar'
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
Vue.prototype['$getAvatar'] = getAvatar

// 关闭提示
Vue.config.productionTip = false

// 过滤器
Vue.filter('year', function(value) {
  if (!value) return ''
  return dayjs(value).format('YYYY')
})
Vue.filter('hour', function(value) {
  if (!value) return ''
  return dayjs(value).format('HH:mm:ss')
})

/**
 * 应用网站配置
 */
function applyWebsiteConfig(config) {
  if (!config) {
    return
  }

  // 网站标题
  if (config.websiteTitle) {
    document.title = config.websiteTitle
  }

  // SEO
  updateMeta('keywords', config.websiteKeywords)
  updateMeta('description', config.websiteDescription)

  // FAVICON
  updateFavicon(config.websiteFavicon)
}

/**
 * 更新 meta 标签
 */
function updateMeta(name, content) {
  if (!content) {
    return
  }

  let meta = document.querySelector(`meta[name="${name}"]`)
  if (!meta) {
    meta = document.createElement('meta')
    meta.name = name
    document.head.appendChild(meta)
  }

  meta.content = content
}

/**
 * 更新 favicon
 */
function updateFavicon(url) {
  if (!url) {
    return
  }

  let link = document.querySelector('link[rel="icon"]')
  if (!link) {
    link = document.createElement('link')
    link.rel = 'icon'
    document.head.appendChild(link)
  }

  link.href = url
}

/**
 * 启动 Vue
 */
async function bootstrap() {
  try {
    // 加载网站配置
    const config = await store.dispatch('website/init')
    applyWebsiteConfig(config)
  } catch (e) {
    console.error('加载网站配置失败', e)
  }

  new Vue({
    router,
    store,
    vuetify,
    render: h => h(App)
  }).$mount('#app')
}

bootstrap()
