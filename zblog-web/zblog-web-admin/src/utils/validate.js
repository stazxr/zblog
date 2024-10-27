/**
 * 手机号正则校验
 *
 * @param phone
 * @returns {boolean}
 */
export function isValidPhone(phone) {
  const reg = /^1([38]\d|4[014-9]|[59][0-35-9]|6[2567]|7[0-8])\d{8}$/
  return reg.test(phone)
}

/**
 * 用户名正则校验
 *
 * @param username
 * @returns {boolean}
 */
export function validUsername(username) {
  const reg = /^[\da-zA-Z]{4,20}$/i
  return reg.test(username)
}

/**
 * 邮箱正则校验
 *
 * @param email
 * @returns {boolean}
 */
export function validEmail(email) {
  const reg = /^(([^<>()\[\]\\.,;:\s@"]+(\.[^<>()\[\]\\.,;:\s@"]+)*)|(".+"))@((\[\d{1,3}\.\d{1,3}\.\d{1,3}\.\d{1,3}])|(([a-zA-Z\-\d]+\.)+[a-zA-Z]{2,}))$/
  return reg.test(email)
}

/**
 * 是否是外部链接
 *
 * @param path
 * @returns {boolean}
 */
export function isExternal(path) {
  return /^(https?:|mailto:|tel:)/.test(path)
}

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
