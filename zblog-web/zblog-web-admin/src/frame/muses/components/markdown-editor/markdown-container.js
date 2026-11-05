/**
 * Markdown 容器扩展
 */

import './markdown-container.css'
import markdownItContainer from 'markdown-it-container'

/**
 * 注册段落缩进容器
 *
 * @param md markdown-it 实例
 */
export function createIndentPlugin(md) {
  md.use(markdownItContainer, 'indent', {
    render(tokens, idx) {
      const token = tokens[idx]
      if (token.nesting === 1) {
        return '<div class="markdown-indent">\n'
      }
      return '</div>\n'
    }
  })
}
