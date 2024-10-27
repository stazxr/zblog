/**
 * 存储值
 *
 * @param key
 * @param value
 * @param expires 过期时间
 */
function localSet(key, value, expires) {
  const params = { key, value, expires }
  if (expires) {
    const data = Object.assign(params, { startTime: new Date().getTime() })
    window.localStorage.setItem(key, JSON.stringify(data))
  } else {
    // obj
    if (Object.prototype.toString.call(value) === '[object Object]') {
      value = JSON.stringify(value)
    }

    // array
    if (Object.prototype.toString.call(value) === '[object Array]') {
      value = JSON.stringify(value)
    }

    window.localStorage.setItem(key, value)
  }
}

/**
 * 获取值
 *
 * @param key
 * @returns {null|any}
 */
function localGet(key) {
  const cacheVal = window.localStorage.getItem(key)
  // 先将拿到的试着进行json转为对象的形式
  let item
  try {
    item = JSON.parse(cacheVal)
  } catch (error) {
    item = cacheVal
  }

  if (item && item.startTime) {
    // 如果有startTime的值，说明设置了失效时间
    const date = new Date().getTime()
    if (date - item.startTime > item.expires) {
      // 如果大于就是过期了，如果小于或等于就还没过期
      window.localStorage.removeItem(key)
      return null
    } else {
      return item.value
    }
  } else {
    return item
  }
}

module.exports = {
  localSet, localGet
}
