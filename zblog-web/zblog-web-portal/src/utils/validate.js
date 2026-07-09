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
