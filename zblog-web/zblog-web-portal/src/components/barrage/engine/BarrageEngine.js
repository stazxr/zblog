import TrackScheduler from './TrackScheduler'
import BarrageItem from './BarrageItem'
import Measure from './Measure'
import Constants from './Constants'

/**
 * 弹幕核心引擎
 *
 * 负责：
 *
 * 1. 接收弹幕
 * 2. 管理队列
 * 3. 分配轨道
 * 4. 控制生命周期
 *
 * Vue组件不要参与调度
 *
 * 所有逻辑集中这里
 */
export default class BarrageEngine {
  constructor(options = {}) {
    /**
     * Vue中的数组引用
     *
     * 最终显示的数据
     *
     * 注意：
     *
     * 这里不能重新赋值
     *
     * 否则Vue响应式会断开
     */
    this.showingList = options.showingList || []

    /**
     * 等待进入屏幕的弹幕
     */
    this.queue = []

    /**
     * 最大同时显示数量
     */
    this.maxShowing = Constants.MAX_SHOWING

    /**
     * 最大队列数量
     */
    this.maxSize = Constants.MAX_QUEUE

    /**
     * 轨道调度器
     */
    this.scheduler = new TrackScheduler({
      containerWidth: options.containerWidth,
      containerHeight: options.containerHeight
    })

    /**
     * 测量工具
     */
    this.measure = new Measure()

    /**
     * 弹幕实例
     */
    this.instances = new Map()

    /**
     * Vue回调
     *
     * 用于通知组件更新
     */
    this.options = options

    /**
     * 是否运行
     */
    this.running = false

    /**
     * requestAnimationFrame
     */
    this.frameId = null
  }

  /**
   * 添加单条弹幕
   *
   * 外部调用：
   *
   * engine.add(data)
   */
  add(data) {
    if (!data) {
      return
    }

    /**
     * 队列数量限制
     */
    if (this.queue.length >= this.maxSize) {
      return
    }

    this.queue.push(new BarrageItem(data))
    this.start()
  }

  /**
   * 批量添加
   *
   * 页面初始化历史弹幕使用
   *
   * engine.addAll(list)
   */
  addAll(list) {
    if (!Array.isArray(list) || list.length === 0) {
      return
    }

    /**
     * 队列数量限制
     */
    if (this.queue.length >= this.maxSize) {
      return
    }

    list.forEach(item => {
      if (!item) {
        return
      }

      this.queue.push(new BarrageItem(item))
    })

    this.start()
  }

  /**
   * 启动调度
   */
  start() {
    if (this.running) {
      return
    }

    this.running = true
    this.loop()
  }

  /**
   * 主循环
   *
   * 每帧检查：有没有弹幕可以进入
   */
  loop = () => {
    if (!this.running) {
      return
    }

    this.dispatch()
    if (this.queue.length === 0 && this.showingList.length === 0) {
      this.running = false
      return
    }

    this.frameId = requestAnimationFrame(this.loop)
  }

  /**
   * 调度弹幕进入屏幕
   */
  dispatch() {
    /**
     * 没有等待弹幕
     */
    if (this.queue.length === 0) {
      return
    }

    /**
     * 屏幕数量限制
     */
    if (this.showingList.length >= this.maxShowing) {
      return
    }

    /**
     * 获取第一条等待弹幕
     */
    const barrage = this.queue[0]

    /**
     * 查找轨道
     */
    const track = this.scheduler.findTrack(barrage)

    /**
     * 当前没有轨道
     *
     * 等下一帧
     */
    if (!track) {
      return
    }

    /**
     * 绑定轨道
     */
    this.scheduler.bind(track, barrage)

    /**
     * 从等待队列删除
     */
    this.queue.shift()

    /**
     * 准备进入Vue
     */
    this.prepare(barrage)
  }

  /**
   * 准备显示
   *
   * 这里负责：
   *
   * 1. 测量宽度
   * 2. 计算动画时间
   * 3. 放入Vue数组
   */
  prepare(item) {
    item.prepare(this.measure, this.scheduler.containerWidth)
    const view = item.toJSON()
    this.instances.set(view.id, item)
    this.showingList.push(view)
    if (this.options.onAdd) {
      this.options.onAdd(view)
    }
  }

  /**
   * 删除弹幕
   *
   * 动画结束调用
   */
  remove(viewId) {
    const index = this.showingList.findIndex(item => item.id === viewId)
    if (index > -1) {
      this.showingList.splice(index, 1)
    }

    /**
     * 释放轨道
     */
    const item = this.instances.get(viewId)
    if (item) {
      this.scheduler.release(item)
      this.instances.delete(viewId)
    }
  }

  /**
   * 鼠标暂停
   */
  pause(view) {
    const item = this.instances.get(view.id)
    if (!item) {
      return
    }

    item.pause()
    view.hover = true
  }

  /**
   * 鼠标恢复
   */
  resume(view) {
    const item = this.instances.get(view.id)
    if (!item) {
      return
    }

    item.resume()
    view.hover = false
  }

  /**
   * 清空
   */
  clear() {
    this.queue = []
    this.showingList.splice(0, this.showingList.length)
    this.instances.clear()
    this.scheduler.init()
  }

  /**
   * 销毁
   */
  destroy() {
    this.running = false
    if (this.frameId) {
      cancelAnimationFrame(this.frameId)
    }

    this.clear()
  }
}
