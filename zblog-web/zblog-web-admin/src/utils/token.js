import Cookies from 'js-cookie'

const tokenKey = 'token'

// 过期时间（单位：天）
const tokenExpireDays = 1

/**
 * 设置token
 */
export function setToken(token) {
  removeToken()

  Cookies.set(tokenKey, token, {
    expires: tokenExpireDays,
    path: '/',
    secure: process.env.NODE_ENV === 'production',
    sameSite: 'Lax'
  })
}

/**
 * 获取token
 */
export function getToken() {
  return Cookies.get(tokenKey) || ''
}

/**
 * 删除token
 */
export function removeToken() {
  Cookies.remove(tokenKey, {
    path: '/'
  })
}
