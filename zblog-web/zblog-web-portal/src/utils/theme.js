/**
 * 获取主题页面随机封面背景
 *
 * @param {Object} pages 页面配置
 * @param {String} pageLabel 页面标识
 * @param {String} defaultCover 默认封面
 * @returns {String}
 */
export function getPageRandomCover(pages, pageLabel, defaultCover = '') {
  if (!pages || typeof pages !== 'object') {
    return buildBackground(defaultCover)
  }

  let pageList = pages[pageLabel]
  if (!Array.isArray(pageList) || pageList.length === 0) {
    pageList = pages['default']
    if (!Array.isArray(pageList) || pageList.length === 0) {
      return buildBackground(defaultCover)
    }
  }

  // 随机取一个
  const index = Math.floor(Math.random() * pageList.length)
  const cover = pageList[index]?.pageCover || defaultCover
  return buildBackground(cover)
}

/**
 * 构建背景样式
 */
function buildBackground(url) {
  if (!url) {
    return ''
  }

  return `background: url(${url}) center center / cover no-repeat`
}
