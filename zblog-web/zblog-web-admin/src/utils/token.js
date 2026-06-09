import Cookies from 'js-cookie'

const tokenKey = 'token'

/**
 * 获取token
 */
export function getToken() {
  return Cookies.get(tokenKey) || ''
}
