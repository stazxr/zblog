import Vue from 'vue'
import Cookies from 'js-cookie'
import 'normalize.css/normalize.css'
import ElementUI from 'element-ui'
import perm from '@/directive/perm'
import checkPerm from '@/directive/checkPerm'
import preventReClick from '@/directive/preventReClick'
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

// highlightjs
import hljs from 'highlight.js'
import Highlight from '@/directive/highlight'

// codemirror 编辑器的相关资源
import Codemirror from 'codemirror'
import 'codemirror/mode/markdown/markdown'
import 'codemirror/mode/javascript/javascript'
import 'codemirror/mode/css/css'
import 'codemirror/mode/htmlmixed/htmlmixed'
import 'codemirror/mode/vue/vue'
import 'codemirror/addon/edit/closebrackets'
import 'codemirror/addon/edit/closetag'
import 'codemirror/addon/edit/matchbrackets'
import 'codemirror/addon/display/placeholder'
import 'codemirror/addon/selection/active-line'
import 'codemirror/addon/scroll/simplescrollbars'
import 'codemirror/addon/scroll/simplescrollbars.css'
import 'codemirror/lib/codemirror.css'

// v-md-editor 编辑器配置
import VMdEditor from '@kangc/v-md-editor/lib/codemirror-editor'
import '@kangc/v-md-editor/lib/style/codemirror-editor.css'
import githubTheme from '@kangc/v-md-editor/lib/theme/github.js'
import '@kangc/v-md-editor/lib/theme/style/github.css'
VMdEditor.Codemirror = Codemirror
VMdEditor.use(githubTheme, {
  Hljs: hljs
})
Vue.use(VMdEditor)

// 加载插件
Vue.use(perm)
Vue.use(checkPerm)
Vue.use(preventReClick)
Vue.use(Highlight)
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
