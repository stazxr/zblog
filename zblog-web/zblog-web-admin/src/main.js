import Vue from 'vue'
import Cookies from 'js-cookie'
import 'normalize.css/normalize.css'
import ElementUI from 'element-ui'
import mavonEditor from 'mavon-editor'
import 'mavon-editor/dist/css/index.css'
import perm from '@/directive/perm'
import checkPerm from '@/directive/checkPerm'
import preventReClick from '@/directive/preventReClick'
import Highlight from '@/directive/highlight'
import './assets/styles/element-variables.scss'
import './assets/styles/index.scss'
import App from './App'
import store from '@/store'
import router from '@/router/routers'
import '@/assets/icons'
import '@/assets/iconfont/iconfont.css'
import '@/router/index'
import 'echarts-gl'
import api from '@/api/http-index'
import muses from '@/frame/muses'
import config from '@/utils/config'
import 'default-passive-events'
import $ from 'jquery'

import { Boot } from '@wangeditor/editor'
import attachmentModule from '@wangeditor/plugin-upload-attachment'
import formulaModule from '@wangeditor/plugin-formula'
import markdownModule from '@wangeditor/plugin-md'

// wangeditor 附件上传注册
Boot.registerModule(attachmentModule)

// wangeditor 公式注册
Boot.registerModule(formulaModule)

// wangeditor markdown 注册
Boot.registerModule(markdownModule)

// 加载插件
Vue.use(perm)
Vue.use(checkPerm)
Vue.use(preventReClick)
Vue.use(Highlight)
Vue.use(mavonEditor)
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

new Vue({
  el: '#app',
  router,
  store,
  render: h => h(App)
})
