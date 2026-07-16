/**
 * 弹幕轨道调度器
 *
 * @author SunTao
 */
import Track from './Track'
import Constants from './Constants'

export default class TrackScheduler {
  constructor(options = {}) {
    this.containerWidth = options.containerWidth || window.innerWidth
    this.containerHeight = options.containerHeight || window.innerHeight

    this.trackCount = Constants.TRACK_COUNT
    this.trackHeight = Constants.TRACK_HEIGHT
    this.safeDistance = Constants.SAFE_DISTANCE
    this.topSpace = Constants.TOP_SPACE
    this.bottomSpace = Constants.BOTTOM_SPACE
    this.minSpeed = Constants.MIN_SPEED
    this.maxSpeed = Constants.MAX_SPEED

    this.lastTrackIndex = -1
    this.tracks = []
    this.init()
  }

  /**
   * 初始化轨道
   */
  init() {
    console.log('[Barrage] 初始化轨道')
    this.tracks = []
    const count = this.calculateTrackCount()
    console.log('[Barrage] 轨道数量：', count)
    for (let i = 0; i < count; i++) {
      const track = new Track(i, this.topSpace + i * this.trackHeight, this.randomSpeed(), this.safeDistance)
      console.log('[Barrage] 创建轨道:', { index: i, top: track.top, speed: track.speed })
      this.tracks.push(track)
    }
  }

  /**
   * 根据容器高度自动计算轨道数量
   */
  calculateTrackCount() {
    if (this.trackCount && this.trackCount > 0) {
      return this.trackCount
    }

    const height = this.containerHeight - this.topSpace - this.bottomSpace
    return Math.max(1, Math.floor(height / this.trackHeight))
  }

  /**
   * 随机速度
   */
  randomSpeed() {
    return Math.floor(Math.random() * (this.maxSpeed - this.minSpeed) + this.minSpeed)
  }

  /**
   * 获取最优轨道
   *
   * 调度策略：
   *
   * ① 优先寻找空轨道
   *
   * ② 再寻找满足安全距离的轨道
   *
   * ③ 如果没有，则返回 null
   *
   * @param barrage 当前等待进入的弹幕
   * @return Track
   */
  findTrack(barrage) {
    if (!barrage) {
      return null
    }

    const count = this.tracks.length
    if (count === 0) {
      return null
    }

    /**
     * 从上一条轨道的下一条开始寻找
     */
    const start = (this.lastTrackIndex + 1) % count

    /**
     * 第一轮
     *
     * 优先寻找空轨道
     */
    for (let i = 0; i < count; i++) {
      const index = (start + i) % count
      const track = this.tracks[index]
      if (track.isEmpty()) {
        this.lastTrackIndex = index
        return track
      }
    }

    /**
     * 第二轮
     *
     * 寻找可以进入的轨道
     */
    for (let i = 0; i < count; i++) {
      const index = (start + i) % count
      const track = this.tracks[index]
      if (track.canEnter(this.containerWidth)) {
        this.lastTrackIndex = index
        return track
      }
    }

    /**
     * 当前没有可进入轨道
     */
    return null
  }

  /**
   * 绑定轨道
   *
   * @param track 轨道
   * @param barrage 弹幕
   */
  bind(track, barrage) {
    if (!track || !barrage) {
      return
    }

    track.bind(barrage)
  }

  /**
   * 释放轨道
   *
   * @param barrage 已结束弹幕
   */
  release(barrage) {
    if (!barrage) {
      return
    }

    const track = this.tracks[barrage.track.index]

    if (!track) {
      return
    }

    track.release(barrage)
  }

  /**
   * 容器大小变化
   *
   * 浏览器 resize 时调用。
   */
  resize(width, height) {
    this.containerWidth = width
    this.containerHeight = height
    const count = this.calculateTrackCount()
    if (count !== this.tracks.length) {
      this.init()
    }
  }
}
