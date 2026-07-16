/**
 * DOM测量工具，用于测量一条弹幕真实宽度。
 *
 * @author SunTao
 */
import Constants from './Constants'

export default class Measure {
  /**
   * 创建测量器
   */
  constructor() {
    /**
     * 隐藏测量DOM
     */
    this.element = null
    this.init()
  }

  /**
   * 初始化
   */
  init() {
    this.element = document.createElement('div')

    this.element.style.position = 'fixed'
    this.element.style.left = '-99999px'
    this.element.style.top = '-99999px'

    this.element.style.display = 'flex'
    this.element.style.alignItems = 'center'

    this.element.style.height = '36px'
    this.element.style.padding = '4px 12px 4px 5px'

    this.element.style.whiteSpace = 'nowrap'
    this.element.style.fontSize = '14px'

    this.element.style.visibility = 'hidden'

    document.body.appendChild(this.element)
  }

  /**
   * 测量弹幕宽度
   *
   * @param barrage BarrageItem
   * @return width(px)
   */
  estimate(barrage) {
    if (!barrage) {
      return 0
    }

    this.element.innerHTML = ''

    if (Constants.SHOW_AVATAR) {
      const avatar = document.createElement('img')
      avatar.src = barrage.avatar || Constants.DEFAULT_AVATAR
      avatar.style.width = '28px'
      avatar.style.height = '28px'
      avatar.style.borderRadius = '50%'
      avatar.style.marginRight = '8px'
      this.element.appendChild(avatar)
    }

    if (Constants.SHOW_NICKNAME) {
      const nickname = document.createElement('span')
      nickname.innerText = barrage.nickname || Constants.DEFAULT_NICKNAME
      nickname.style.marginRight = '8px'
      nickname.style.color = '#ffd04b'
      this.element.appendChild(nickname)
    }

    const content = document.createElement('span')
    content.innerText = barrage.content || ''
    content.style.color = barrage.color || Constants.DEFAULT_COLOR
    this.element.appendChild(content)
    if (Constants.SHOW_LIKE) {
      const like = document.createElement('span')
      like.innerText = ' 👍 ' + (barrage.likeCount || 0)
      like.style.marginLeft = '12px'
      this.element.appendChild(like)
    }

    return this.element.offsetWidth
  }

  /**
   * 销毁
   */
  destroy() {
    if (!this.element) {
      return
    }

    document.body.removeChild(this.element)
    this.element = null
  }
}
