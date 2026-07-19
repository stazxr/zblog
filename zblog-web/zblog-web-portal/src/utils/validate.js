/**
 * 是否 Json 字符串
 *
 * @param str
 * @returns {boolean}
 */
export function isJson(str) {
  try {
    JSON.parse(str)
    return true
  } catch (e) {
    return false
  }
}

/**
 * 是否是外部链接
 *
 * @param path
 * @returns {boolean}
 */
export function isExternal(path) {
  return /^(https?:|http?:|mailto:|tel:)/.test(path)
}
