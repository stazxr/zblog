/**
 * Muses Markdown Editor 初始化
 *
 * 负责：
 * 1. 初始化 @kangc/v-md-editor
 * 2. 配置 VuePress Markdown 主题
 * 3. 注册 Markdown 编辑器扩展插件
 * 4. 初始化独立的 Markdown 预览组件
 *
 * @author Sun Tao
 * @date 2026-10-30
 */
import VMdEditor from '@kangc/v-md-editor'

// 主题
import vuepressTheme from '@kangc/v-md-editor/lib/theme/vuepress.js'
import '@kangc/v-md-editor/lib/theme/style/vuepress.css'

// import githubTheme from '@kangc/v-md-editor/lib/theme/github.js'
// import '@kangc/v-md-editor/lib/theme/style/github.css'

// 预览
import VMdPreview from '@kangc/v-md-editor/lib/preview'
import '@kangc/v-md-editor/lib/style/preview.css'

// 编辑器
import '@kangc/v-md-editor/lib/style/base-editor.css'

// Prism
import Prism from 'prismjs'

import 'prismjs/components/prism-markup'
import 'prismjs/components/prism-css'
import 'prismjs/components/prism-clike'
import 'prismjs/components/prism-docker'
import 'prismjs/components/prism-javascript'
import 'prismjs/components/prism-typescript'
import 'prismjs/components/prism-java'
import 'prismjs/components/prism-json'
import 'prismjs/components/prism-bash'
import 'prismjs/components/prism-sql'
import 'prismjs/components/prism-yaml'
import 'prismjs/components/prism-scss'
import 'prismjs/components/prism-xml-doc'

// Tip
import createTipPlugin from '@kangc/v-md-editor/lib/plugins/tip/index'
import '@kangc/v-md-editor/lib/plugins/tip/tip.css'

// To do List
import createTodoListPlugin from '@kangc/v-md-editor/lib/plugins/todo-list/index'
import '@kangc/v-md-editor/lib/plugins/todo-list/todo-list.css'

// Emoji
import createEmojiPlugin from '@kangc/v-md-editor/lib/plugins/emoji/index'
import '@kangc/v-md-editor/lib/plugins/emoji/emoji.css'

// Line Number
import createLineNumberPlugin from '@kangc/v-md-editor/lib/plugins/line-number/index'

// Highlight Lines
import createHighlightLinesPlugin from '@kangc/v-md-editor/lib/plugins/highlight-lines/index'
import '@kangc/v-md-editor/lib/plugins/highlight-lines/highlight-lines.css'

// Copy Code
import createCopyCodePlugin from '@kangc/v-md-editor/lib/plugins/copy-code/index'
import '@kangc/v-md-editor/lib/plugins/copy-code/copy-code.css'

// Align
import createAlignPlugin from '@kangc/v-md-editor/lib/plugins/align'

let installed = false

/**
 * 初始化 Muses Markdown Editor 和 Markdown Preview
 *
 * @param {Object} Vue Vue 构造函数
 * @return {Object} Markdown 编辑器实例
 */
export default function installMarkdownEditor(Vue) {
  if (installed) {
    return VMdEditor
  }

  // 编辑器主题
  VMdEditor.use(vuepressTheme, {
    Prism,
    extend(md) {
      md.set({
        quotes: '""\'\''
      })
    }
  })

  // 编辑器扩展插件
  VMdEditor.use(createTipPlugin())
  VMdEditor.use(createTodoListPlugin({ color: '#3eaf7c' }))
  VMdEditor.use(createEmojiPlugin())
  VMdEditor.use(createLineNumberPlugin())
  VMdEditor.use(createHighlightLinesPlugin())
  VMdEditor.use(createCopyCodePlugin())
  VMdEditor.use(createAlignPlugin())

  // 配置独立预览组件的 VuePress 主题
  VMdPreview.use(vuepressTheme, {
    Prism,
    extend(md) {
      md.set({
        quotes: '""\'\''
      })
    }
  })

  VMdPreview.use(createTipPlugin())
  VMdPreview.use(createTodoListPlugin({ color: '#3eaf7c' }))
  VMdPreview.use(createEmojiPlugin())
  VMdPreview.use(createLineNumberPlugin())
  VMdPreview.use(createHighlightLinesPlugin())
  VMdPreview.use(createCopyCodePlugin())
  VMdPreview.use(createAlignPlugin())

  // 注册 Vue 插件
  Vue.use(VMdEditor)
  Vue.use(VMdPreview)

  installed = true

  return VMdEditor
}
