import Hljs from 'highlight.js'
// import 'highlight.js/styles/vs.css'
// import 'highlight.js/styles/vs2015.css'
import 'highlight.js/styles/xcode.css'

const Highlight = {}

// 自定义代码高亮插件
Highlight.install = function(Vue) {
  // 自定义指令 v-highlight
  Vue.directive('highlight', {
    // 被绑定元素插入父节点时调用
    inserted: function(el) {
      Hljs.configure({
        ignoreUnescapedHTML: true
      })
      const blocks = el.querySelectorAll('pre code')
      blocks.forEach((el) => {
        Hljs.highlightElement(el)
      })
    },
    // 指令所在组件的 VNode 及其子 VNode 全部更新后调用
    componentUpdated: function(el) {
      Hljs.configure({
        ignoreUnescapedHTML: true
      })
      const blocks = el.querySelectorAll('pre code')
      blocks.forEach((el) => {
        Hljs.highlightElement(el)
      })
    }
  })
}

export default Highlight
