/**
 * Muses Markdown Editor 初始化
 *
 * 负责：
 * 1. 初始化 @kangc/v-md-editor
 * 2. 配置 VuePress Markdown 主题
 * 3. 注册 Markdown 编辑器扩展插件
 *
 * @author Sun Tao
 * @date 2026-09-21
 */

import VMdEditor from '@kangc/v-md-editor'
import vuepressTheme from '@kangc/v-md-editor/lib/theme/vuepress.js'

import '@kangc/v-md-editor/lib/style/base-editor.css'
import '@kangc/v-md-editor/lib/theme/style/vuepress.css'

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

// Emoji
import createEmojiPlugin from '@kangc/v-md-editor/lib/plugins/emoji/index'
import '@kangc/v-md-editor/lib/plugins/emoji/emoji.css'

// To do List
import createTodoListPlugin from '@kangc/v-md-editor/lib/plugins/todo-list/index'
import '@kangc/v-md-editor/lib/plugins/todo-list/todo-list.css'

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
 * 初始化 Muses Markdown Editor
 *
 * @param {Object} Vue Vue 构造函数
 * @return {Object} Markdown 编辑器实例
 */
export default function installMarkdownEditor(Vue) {
  if (installed) {
    return VMdEditor
  }

  // Markdown 主题
  VMdEditor.use(vuepressTheme, {
    Prism
  })

  // 提示块
  VMdEditor.use(createTipPlugin())

  // Emoji
  VMdEditor.use(createEmojiPlugin())

  // To do List
  VMdEditor.use(createTodoListPlugin({ color: '#3eaf7c' }))

  // 代码行号
  VMdEditor.use(createLineNumberPlugin())

  // 代码高亮行
  VMdEditor.use(createHighlightLinesPlugin())

  // 代码复制
  VMdEditor.use(createCopyCodePlugin())

  // 内容对齐
  VMdEditor.use(createAlignPlugin())

  // 注册 Vue 插件
  Vue.use(VMdEditor)

  installed = true

  return VMdEditor
}
