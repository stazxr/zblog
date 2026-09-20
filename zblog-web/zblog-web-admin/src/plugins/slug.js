import { pinyin } from 'pinyin-pro'

/**
 * 生成 URL Slug
 *
 * @param {String} value 原始文本
 * @returns {String} Slug
 */
export function generateSlug(value) {
  if (value === null || value === undefined) {
    return ''
  }

  const text = String(value).trim()

  if (!text) {
    return ''
  }

  // 中文转拼音
  const pinyinText = pinyin(text, {
    toneType: 'none',
    type: 'string'
  })

  return pinyinText
    .toLowerCase()
    .replace(/[^a-z0-9]+/g, '-')
    .replace(/^-+|-+$/g, '')
}

/**
 * Vue 插件
 */
export default {
  install(Vue) {
    Vue.prototype.$slug = generateSlug
  }
}
