/**
 * 格式化相对时间
 *
 * @param {String|Date|Number} time 时间
 * @returns {String}
 */
export function formatRelativeTime(time) {
  if (!time) {
    return ''
  }

  const date = parseDate(time)
  if (!date || isNaN(date.getTime())) {
    return time
  }

  const now = new Date()
  const diff = now.getTime() - date.getTime()

  // 未来时间
  if (diff < 0) {
    return formatDate(date)
  }

  const seconds = Math.floor(diff / 1000)
  const minutes = Math.floor(seconds / 60)
  const hours = Math.floor(minutes / 60)
  const days = Math.floor(hours / 24)

  if (seconds < 60) {
    return '刚刚'
  }

  if (minutes < 60) {
    return `${minutes}分钟前`
  }

  if (hours < 24) {
    return `${hours}小时前`
  }

  if (days < 2) {
    return '昨天'
  }

  if (days < 7) {
    return `${days}天前`
  }

  return formatDate(date)
}

/**
 * 解析时间
 */
function parseDate(time) {
  if (time instanceof Date) {
    return time
  }

  // 时间戳
  if (typeof time === 'number') {
    return new Date(time)
  }

  // 处理 MySQL 时间格式
  if (typeof time === 'string') {
    const value = time.trim()

    // 2026-09-08 14:32:10
    if (/^\d{4}-\d{2}-\d{2} \d{2}:\d{2}:\d{2}$/.test(value)) {
      return new Date(value.replace(/-/g, '/'))
    }

    return new Date(value)
  }

  return null
}

/**
 * 显示具体日期
 */
function formatDate(date) {
  const year = date.getFullYear()
  const month = String(date.getMonth() + 1).padStart(2, '0')
  const day = String(date.getDate()).padStart(2, '0')

  return `${year}-${month}-${day}`
}
