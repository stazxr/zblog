/**
 * Muses Markdown Editor 初始化
 *
 * @author Sun Tao
 * @date 2026-10-30
 */
import VMdEditor, { xss } from '@kangc/v-md-editor'

// 主题
import vuepressTheme from '@kangc/v-md-editor/lib/theme/vuepress.js'
import '@kangc/v-md-editor/lib/theme/style/vuepress.css'
// import githubTheme from '@kangc/v-md-editor/lib/theme/github.js'
// import '@kangc/v-md-editor/lib/theme/style/github.css'

// 预览
// import VMdPreview from '@kangc/v-md-editor/lib/preview'
// import '@kangc/v-md-editor/lib/style/preview.css'
import VMdPreviewHtml from '@kangc/v-md-editor/lib/preview-html'
import '@kangc/v-md-editor/lib/style/preview-html.css'

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
// import createHighlightLinesPlugin from '@kangc/v-md-editor/lib/plugins/highlight-lines/index'
// import '@kangc/v-md-editor/lib/plugins/highlight-lines/highlight-lines.css'

// Copy Code
// import createCopyCodePlugin from '@kangc/v-md-editor/lib/plugins/copy-code/index'
// import '@kangc/v-md-editor/lib/plugins/copy-code/copy-code.css'

// Align
import createAlignPlugin from '@kangc/v-md-editor/lib/plugins/align'

// Markdown Container
import { createIndentPlugin } from '../markdown-container.js'

let installed = false

/**
 * 创建 Markdown 配置
 */
function createMarkdownOptions() {
  return {
    Prism,
    extend(md) {
      // 注册段落缩进容器
      createIndentPlugin(md)

      // 中文引号
      md.set({
        quotes: '""\'\''
      })
    }
  }
}

/**
 * 初始化 Muses Markdown Editor 和 Markdown Preview
 */
export default function installMarkdownEditor(Vue) {
  if (installed) {
    return VMdEditor
  }

  // 编辑器主题
  VMdEditor.use(vuepressTheme, createMarkdownOptions())

  // 编辑器扩展插件
  VMdEditor.use(createTipPlugin())
  VMdEditor.use(createTodoListPlugin({ color: '#3eaf7c' }))
  VMdEditor.use(createEmojiPlugin())
  VMdEditor.use(createLineNumberPlugin())
  // VMdEditor.use(createHighlightLinesPlugin())
  // VMdEditor.use(createCopyCodePlugin())
  VMdEditor.use(createAlignPlugin())

  // 预览组件主题
  // VMdPreview.use(vuepressTheme, createMarkdownOptions())
  // VMdPreview.use(createTipPlugin())
  // VMdPreview.use(createTodoListPlugin({ color: '#3eaf7c' }))
  // VMdPreview.use(createEmojiPlugin())
  // VMdPreview.use(createLineNumberPlugin())
  // VMdPreview.use(createHighlightLinesPlugin())
  // VMdPreview.use(createCopyCodePlugin())
  // VMdPreview.use(createAlignPlugin())

  // 注册 Vue 插件
  Vue.use(VMdEditor)
  Vue.use(VMdPreviewHtml)

  installed = true

  return VMdEditor
}

/**
 * Markdown 转 HTML
 *
 * 使用编辑器已经注册的 Markdown 解析器，确保扩展语法与编辑器保持一致。
 */
export function markdownToHtml(markdown) {
  if (!markdown) {
    return ''
  }

  if (!installed) {
    throw new Error('Muses Markdown Editor 尚未初始化')
  }

  const html = VMdEditor.themeConfig.markdownParser.render(markdown)

  // HTML 安全过滤
  return xss.process(html)
}
