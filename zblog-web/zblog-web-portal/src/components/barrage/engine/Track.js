/**
 * 单条弹幕轨道
 *
 * 每一条轨道只负责维护自己，Scheduler 不关心轨道内部实现。
 *
 * 职责：
 *
 * 1、记录当前正在运行的弹幕
 * 2、判断是否允许新的弹幕进入
 * 3、释放轨道
 *
 * @author SunTao
 */
export default class Track {
  /**
   * 创建轨道
   *
   * @param index 轨道编号
   * @param top 顶部位置(px)
   * @param speed 当前轨道速度(px/s)
   * @param safeDistance 安全距离(px)
   */
  constructor(index, top, speed, safeDistance) {
    /** 轨道编号 */
    this.index = index
    /** top位置 */
    this.top = top
    /** 当前轨道速度 */
    this.speed = speed
    /** 最小安全距离 */
    this.safeDistance = safeDistance
    /**
     * 当前轨道最后一条弹幕
     *
     * 注意：
     * 一条轨道只需要记录最后进入的一条即可，因为后面的弹幕永远只需要和最后一条比较。
     */
    this.current = null
  }

  /**
   * 当前轨道是否为空
   */
  isEmpty() {
    return this.current == null
  }

  /**
   * 是否允许新的弹幕进入
   *
   * 判断规则：
   *    当前弹幕尾部距离入口 > 安全距离
   */
  canEnter() {
    if (!this.current) {
      return true
    }

    return this.getTailDistance() >= this.safeDistance
  }

  /**
   * 绑定新的弹幕
   */
  bind(barrage) {
    barrage.setTrack(this)
    this.current = barrage
  }

  /**
   * 释放轨道
   */
  release(barrage) {
    if (this.current && this.current.id === barrage.id) {
      this.current = null
    }
  }

  /**
   * 获取当前尾部距离
   *
   * 方便调试
   */
  getTailDistance() {
    if (!this.current) {
      return Infinity
    }

    return this.current.getTailDistance()
  }

  /**
   * 获取当前弹幕
   */
  getCurrent() {
    return this.current
  }

  /**
   * 重置
   */
  reset() {
    this.current = null
  }
}
