/**
 * 单条弹幕对象
 *
 * 说明：
 *
 * 一条弹幕从进入队列开始，
 * 一直到动画结束，
 * 全部由 BarrageItem 管理自己的状态。
 *
 * @author SunTao
 */
import Constants from './Constants'

export default class BarrageItem {
  /**
   * 创建弹幕
   *
   * @param data 后端返回数据
   */
  constructor(data = {}) {
    /**
     * 原始数据
     */
    this.raw = data

    /**
     * ===========================
     * 后端数据
     * ===========================
     */

    this.id = data.id

    this.avatar = data.avatar

    this.nickname = data.nickname

    this.content = data.content

    this.likeCount = data.likeCount || 0

    this.color = data.color || '#ffffff'

    /**
     * ===========================
     * 运行状态
     * ===========================
     */

    /**
     * 所属轨道
     */
    this.track = null

    /**
     * 宽度(px)
     */
    this.width = 0

    /**
     * 总移动距离(px)
     */
    this.distance = 0

    /**
     * 动画时长(s)
     */
    this.duration = 0

    /**
     * 开始时间
     */
    this.startTime = 0

    /**
     * 暂停开始时间
     */
    this.pauseStartTime = null

    /**
     * 是否暂停
     */
    this.hover = false

    /**
     * 是否开始
     */
    this.running = false
  }

  /**
   * 初始化弹幕
   */
  prepare(measure, containerWidth) {
    const width = measure.estimate(this)
    this.setWidth(width)
    this.start(containerWidth)
  }

  /**
   * 设置真实宽度
   */
  setWidth(width) {
    this.width = width
  }

  /**
   * 设置轨道
   */
  setTrack(track) {
    this.track = track
  }

  /**
   * 开始动画
   */
  start(containerWidth) {
    if (!this.track) {
      console.error('弹幕未绑定轨道')
      return
    }

    this.running = true
    this.startTime = Date.now()
    this.distance = containerWidth + this.width + Constants.EXIT_BUFFER
    this.duration = this.distance / this.track.speed
  }

  getTailDistance() {
    return this.getMovedDistance() - this.width
  }

  /**
   * 已移动距离(px)
   */
  getMovedDistance() {
    return this.getRunTime() * this.track.speed
  }

  /**
   * 已运行时间(s)
   */
  getRunTime() {
    if (!this.running) {
      return 0
    }

    return (Date.now() - this.startTime) / 1000
  }

  /**
   * 剩余距离(px)
   */
  getRemainDistance() {
    return Math.max(0, this.distance - this.getMovedDistance())
  }

  /**
   * 剩余时间(s)
   */
  getRemainTime() {
    if (this.track.speed <= 0) {
      return 0
    }

    return this.getRemainDistance() / this.track.speed
  }

  /**
   * 当前头部位置
   *
   * 相对于容器左侧
   */
  getHead(containerWidth) {
    return containerWidth - this.getMovedDistance()
  }

  /**
   * 当前尾部位置
   */
  getTail(containerWidth) {
    return this.getHead(containerWidth) + this.width
  }

  /**
   * 是否已经结束
   */
  isFinished() {
    return this.getRemainDistance() <= 0
  }

  /**
   * 暂停
   */
  pause() {
    if (!this.running) {
      return
    }

    if (this.hover) {
      return
    }

    this.hover = true
    this.pauseStartTime = Date.now()
  }

  /**
   * 恢复
   */
  resume() {
    if (!this.hover) {
      return
    }

    this.hover = false
    const pauseDuration = Date.now() - this.pauseStartTime
    this.pauseStartTime = null
    this.startTime += pauseDuration
  }

  /**
   * 转成Vue对象
   */
  toJSON() {
    return {
      id: this.id,
      avatar: this.avatar,
      nickname: this.nickname,
      content: this.content,
      likeCount: this.likeCount,
      color: this.color,

      track: this.track.index,
      top: this.track.top,
      speed: this.track.speed,

      width: this.width,
      duration: this.duration,
      distance: this.distance,
      hover: this.hover,
      running: this.running
    }
  }
}
