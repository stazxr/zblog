import Vue from 'vue'
import App from './App'
// 路由组件
import '@/router/index'
import router from '@/router/routers'
// Vuex
import store from '@/store'
// Api
import api from '@/api/http-index'
import Cookies from 'js-cookie'
import 'normalize.css/normalize.css'
import ElementUI from 'element-ui'
import perm from '@/directive/perm'
import checkPerm from '@/directive/checkPerm'
import preventReClick from '@/directive/preventReClick'
import './assets/styles/element-variables.scss'
import './assets/styles/index.scss'
import '@/assets/icons'
import '@/assets/iconfont/iconfont.css'
import 'echarts-gl'
import muses from '@/frame/muses'
import config from '@/utils/config'
import 'default-passive-events'
import $ from 'jquery'
import JsonViewer from 'vue-json-viewer'
import 'vue-json-viewer/style.css'
import Slug from '@/plugins/slug' // Slug 插件

// highlightjs
// import Highlight from '@/directive/highlight'

// 页面插画
Vue.prototype.$assets = {
  nodata: require('@/assets/gallery/v1/nodata.png')
}

// 加载插件
Vue.use(perm)
Vue.use(checkPerm)
Vue.use(preventReClick)
// Vue.use(Highlight)
Vue.use(JsonViewer)
Vue.use(Slug)
Vue.use(ElementUI, {
  size: Cookies.get('size') || 'small',
  locale: ''
})

// 声明全局变量
Vue.prototype['$config'] = config
Vue.prototype['$mapi'] = api
Vue.prototype['$'] = $

// 全量安装 muses 框架
muses.install(Vue)

// 关闭提示
Vue.config.productionTip = false

// remove reloaded
sessionStorage.removeItem('reloaded')

new Vue({
  el: '#app',
  router,
  store,
  render: h => h(App)
})
