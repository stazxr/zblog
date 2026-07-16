/**
 * 默认配置
 *
 * 所有默认参数统一放在这里，后续 BarrageEngine、TrackScheduler 都从这里读取。
 *
 * @author SunTao
 */
const Constants = {
  /**
   * 最大同时显示弹幕数量
   */
  MAX_SHOWING: 100,

  /**
   * 最大等待队列
   */
  MAX_QUEUE: 500,

  /**
   * 默认轨道高度(px)
   */
  TRACK_HEIGHT: 80,

  /**
   * 默认轨道数量
   *
   * 如果没有指定，会根据容器高度自动计算。
   */
  TRACK_COUNT: 0,

  /**
   * 顶部预留距离
   */
  TOP_SPACE: 60,

  /**
   * 底部预留距离
   */
  BOTTOM_SPACE: 50,

  /**
   * 默认安全距离(px)
   *
   * 两条弹幕尾部至少相隔这么远，防止视觉重叠。
   */
  SAFE_DISTANCE: 80,

  /**
   * 弹幕离场缓冲距离
   */
  EXIT_BUFFER: 30,

  /**
   * 默认最小速度(px/s)
   */
  MIN_SPEED: 80,

  /**
   * 默认最大速度(px/s)
   */
  MAX_SPEED: 120,

  /**
   * 默认头像
   */
  DEFAULT_AVATAR: require('../assets/default-avatar.png'),

  /**
   * 默认昵称
   */
  DEFAULT_NICKNAME: '游客',

  /**
   * 默认字体颜色
   */
  DEFAULT_COLOR: '#ffffff',

  /**
   * 点赞按钮是否默认显示
   */
  SHOW_LIKE: true,

  /**
   * 是否显示昵称
   */
  SHOW_NICKNAME: true,

  /**
   * 是否显示头像
   */
  SHOW_AVATAR: true,

  /**
   * requestAnimationFrame 调度间隔
   *
   * 理论上浏览器约16ms一帧，
   * 这里留作以后扩展。
   */
  FRAME_INTERVAL: 16
}

export default Constants
