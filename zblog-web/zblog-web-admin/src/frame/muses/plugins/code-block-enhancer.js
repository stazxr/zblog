/**
 * Markdown 代码块增强工具
 *
 * @author Sun Tao
 * @since 2026-11-05
 */

import './code-block-enhancer.scss'

/**
 * 语言名称映射
 */
const LANGUAGE_MAP = {
  js: 'JavaScript',
  javascript: 'JavaScript',
  ts: 'TypeScript',
  typescript: 'TypeScript',
  java: 'Java',
  html: 'HTML',
  xml: 'XML',
  css: 'CSS',
  scss: 'SCSS',
  less: 'Less',
  json: 'JSON',
  sql: 'SQL',
  bash: 'Bash',
  shell: 'Shell',
  sh: 'Shell',
  python: 'Python',
  py: 'Python',
  go: 'Go',
  c: 'C',
  cpp: 'C++',
  vue: 'Vue',
  jsx: 'JSX',
  tsx: 'TSX',
  yaml: 'YAML',
  yml: 'YAML',
  markdown: 'Markdown',
  md: 'Markdown',
  text: 'Plain Text'
}

/**
 * 代码块增强器
 */
class CodeBlockEnhancer {
  constructor(container) {
    this.container = container
    this.observer = null
    this.handleClick = this.handleClick.bind(this)
  }

  /**
   * 初始化
   */
  init() {
    if (!this.container) {
      return
    }

    this.enhance()

    this.observer = new MutationObserver(() => {
      this.enhance()
    })

    this.observer.observe(this.container, {
      childList: true,
      subtree: true
    })

    this.container.addEventListener('click', this.handleClick)
  }

  /**
   * 增强代码块
   */
  enhance() {
    const blocks = this.container.querySelectorAll(
      '.v-md-pre-wrapper'
    )

    blocks.forEach(codeWrapper => {
      // 已经增强过的代码块不再处理
      if (codeWrapper.closest('.z-code-block')) {
        return
      }

      this.createToolbar(codeWrapper)
    })
  }

  /**
   * 获取代码块语言
   */
  getLanguage(codeWrapper) {
    const className = codeWrapper.className || ''
    const pre = codeWrapper.querySelector('pre')
    const preClass = pre ? pre.className : ''

    const match = (
      className + ' ' + preClass
    ).match(/(?:v-md-pre-wrapper-|v-md-prism-)([a-zA-Z0-9+#.-]+)/)

    if (!match) {
      return 'TEXT'
    }

    const language = match[1].toLowerCase()
    return LANGUAGE_MAP[language] || language.toUpperCase()
  }

  /**
   * 创建代码块工具栏
   */
  createToolbar(codeWrapper) {
    const parent = codeWrapper.parentNode

    if (!parent) {
      return
    }

    // 创建外层容器
    const wrapper = document.createElement('div')
    wrapper.className = 'z-code-block'

    // 创建顶部工具栏
    const toolbar = document.createElement('div')
    toolbar.className = 'z-code-toolbar'

    // 语言标识
    const languageElement = document.createElement('span')
    languageElement.className = 'z-code-language'
    const icon = document.createElement('i')
    icon.className = 'iconfont icon-inlinecode z-code-language-icon'
    languageElement.appendChild(icon)
    languageElement.appendChild(document.createTextNode(this.getLanguage(codeWrapper)))

    // 操作区域
    const actions = document.createElement('div')
    actions.className = 'z-code-actions'

    // 复制按钮
    const copyButton = document.createElement('button')
    copyButton.type = 'button'
    copyButton.className = 'z-code-action'
    copyButton.dataset.action = 'copy'
    copyButton.textContent = '复制'

    // 自动换行按钮
    const wrapButton = document.createElement('button')
    wrapButton.type = 'button'
    wrapButton.className = 'z-code-action'
    wrapButton.dataset.action = 'wrap'
    wrapButton.textContent = '自动换行'

    actions.appendChild(wrapButton)
    actions.appendChild(copyButton)

    toolbar.appendChild(languageElement)
    toolbar.appendChild(actions)

    // 保留 v-md-pre-wrapper 原有 DOM 结构
    parent.insertBefore(wrapper, codeWrapper)
    wrapper.appendChild(toolbar)
    wrapper.appendChild(codeWrapper)
  }

  /**
   * 工具栏事件代理
   */
  handleClick(event) {
    const button = event.target.closest('.z-code-action')

    if (!button || !this.container.contains(button)) {
      return
    }

    const wrapper = button.closest('.z-code-block')
    const pre = wrapper && wrapper.querySelector('pre')

    if (!pre) {
      return
    }

    const action = button.dataset.action

    if (action === 'copy') {
      this.copyCode(pre, button)
    } else if (action === 'wrap') {
      this.toggleWrap(pre, button)
    }
  }

  /**
   * 复制代码
   */
  async copyCode(pre, button) {
    const code = pre.querySelector('code')
    const text = code ? code.textContent : pre.textContent

    try {
      if (navigator.clipboard && window.isSecureContext) {
        await navigator.clipboard.writeText(text)
      } else {
        const textarea = document.createElement('textarea')
        textarea.value = text
        textarea.style.position = 'fixed'
        textarea.style.opacity = '0'

        document.body.appendChild(textarea)
        textarea.select()

        const success = document.execCommand('copy')
        document.body.removeChild(textarea)

        if (!success) {
          throw new Error('复制失败')
        }
      }

      button.textContent = '已复制'

      setTimeout(() => {
        if (button.isConnected) {
          button.textContent = '复制'
        }
      }, 1500)
    } catch (error) {
      button.textContent = '复制失败'
    }
  }

  /**
   * 切换自动换行
   */
  toggleWrap(pre, button) {
    const enabled = pre.classList.toggle('z-code-wrap')

    button.classList.toggle('active', enabled)
    button.setAttribute('aria-pressed', String(enabled))
  }

  /**
   * 销毁
   */
  destroy() {
    if (this.observer) {
      this.observer.disconnect()
      this.observer = null
    }

    if (this.container) {
      this.container.removeEventListener('click', this.handleClick)
    }
  }
}

export default CodeBlockEnhancer
