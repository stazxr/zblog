/**
 * Muses Markdown Editor 初始化
 *
 * 负责：
 * 1. 初始化 @kangc/v-md-editor
 * 2. 配置 VuePress Markdown 主题
 *
 * @author Sun Tao
 * @date 2026-09-14
 */

import VMdEditor from '@kangc/v-md-editor'
import vuepressTheme from '@kangc/v-md-editor/lib/theme/vuepress.js'

import '@kangc/v-md-editor/lib/style/base-editor.css'
import '@kangc/v-md-editor/lib/theme/style/vuepress.css'

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

  /**
   * VuePress Markdown 主题
   *
   * v-md-editor 1.7.12 自带：
   * - Markdown
   * - 代码高亮
   * - KaTeX
   * - Mermaid
   * - 表格
   * - 任务列表等
   */
  VMdEditor.use(vuepressTheme)

  /**
   * 注册到当前 Vue 实例
   */
  Vue.use(VMdEditor)

  installed = true

  return VMdEditor
}
