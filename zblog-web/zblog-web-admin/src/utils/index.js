/**
 * 防抖函数
 *
 * @param func 需要防抖的函数
 * @param wait 防抖的时间间隔（毫秒）
 * @param immediate 如果为 true，则在第一次触发时立即执行函数，否则等待 wait 毫秒后再执行
 * @returns {function(...[*]): *}
 */
export function debounce(func, wait, immediate = false) {
  let timeout, args, context, timestamp, result

  const later = function() {
    // 据上一次触发时间间隔
    const last = +new Date() - timestamp

    // 上次被包装函数被调用时间间隔 last 小于设定时间间隔 wait
    if (last < wait && last > 0) {
      timeout = setTimeout(later, wait - last)
    } else {
      timeout = null
      // 如果设定为immediate===true，因为开始边界已经调用过了此处无需调用
      if (!immediate) {
        result = func.apply(context, args)
        if (!timeout) context = args = null
      }
    }
  }

  return function(...args) {
    context = this
    timestamp = +new Date()
    const callNow = immediate && !timeout
    // 如果延时不存在，重新设定延时
    if (!timeout) timeout = setTimeout(later, wait)
    if (callNow) {
      result = func.apply(context, args)
      context = args = null
    }

    return result
  }
}

/**
 * 判断元素是否具有某个 CSS 类
 *
 * @param ele 目标 DOM 元素
 * @param cls 要判断的 CSS 类名
 * @returns {boolean}
 */
export function hasClass(ele, cls) {
  return !!ele.className.match(new RegExp('\\b' + cls + '\\b'))
}

/**
 * 向元素添加指定的 CSS 类
 *
 * @param ele 目标 DOM 元素
 * @param cls 要添加的 CSS 类名
 */
export function addClass(ele, cls) {
  if (!hasClass(ele, cls)) ele.className += ' ' + cls
}

/**
 * 从元素中移除指定的 CSS 类
 *
 * @param ele 目标 DOM 元素
 * @param cls 要移除的 CSS 类名
 */
export function removeClass(ele, cls) {
  if (hasClass(ele, cls)) {
    const reg = new RegExp('(\\s|^)' + cls + '(\\s|$)')
    ele.className = ele.className.replace(reg, ' ')
  }
}

/**
 * 通过创建 Blob 对象并生成 URL，触发文件下载
 *
 * @param obj 要下载的数据，可以是文本、JSON 等
 * @param filename 下载的文件名
 */
export function downloadFile(obj, filename) {
  const url = window.URL.createObjectURL(new Blob([obj]))
  downloadFileByUrl(url, filename)
}

/**
 * 通过传入文件 URL 和文件名，触发浏览器下载文件
 *
 * @param url 文件的 URL 地址
 * @param filename 下载时的文件名
 */
export function downloadFileByUrl(url, filename) {
  const link = document.createElement('a')
  link.style.display = 'none'
  link.href = url
  link.setAttribute('download', filename)
  document.body.appendChild(link)
  link.click()
  document.body.removeChild(link)
}

/**
 * 格式化日期中的月份，保证两位数
 *
 * @param date JavaScript 的 Date 对象
 * @returns {string}
 */
export function formatMonth(date) {
  return ('0' + (date.getMonth() + 1)).slice(-2)
}

/**
 * 格式化日期中的天数，保证两位数
 *
 * @param date JavaScript 的 Date 对象
 * @returns {string}
 */
export function formatDay(date) {
  return ('0' + date.getDate()).slice(-2)
}
