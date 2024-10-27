// token key
const tokenKey = 'usrToken'

// refresh token key
const refTokenKey = 'usrRefToken'

// set token pair
export function setTokenPair(tokenPair) {
  const { atk, rtk } = tokenPair
  setToken(atk)
  setRefToken(rtk)
}

// set token
export function setToken(token) {
  removeToken()
  window.localStorage.setItem(tokenKey, token)
}

// set refresh token
export function setRefToken(refToken) {
  removeRefToken()
  window.localStorage.setItem(refTokenKey, refToken)
}

// get token
export function getToken() {
  return window.localStorage.getItem(tokenKey) || ''
}

// get refresh token
export function getRefToken() {
  return window.localStorage.getItem(refTokenKey) || ''
}

// remove token pair
export function removeTokenPair() {
  removeToken()
  removeRefToken()
}

// remove token
export function removeToken() {
  window.localStorage.removeItem(tokenKey)
}

// remove refresh token
export function removeRefToken() {
  window.localStorage.removeItem(refTokenKey)
}
