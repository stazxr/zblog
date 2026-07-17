import SockJS from 'sockjs-client'
import Stomp from 'stompjs'

class WebSocketManager {
  constructor() {
    this.socket = null
    this.stomp = null
    this.connected = false
    this.subscriptions = new Map()

    /**
     * 等待连接完成的订阅
     */
    this.pendingSubscriptions = new Map()

    window.addEventListener('beforeunload', () => {
      this.disconnect()
    })
  }

  /**
   * 建立连接
   */
  connect() {
    if (this.connected) {
      return Promise.resolve()
    }

    return new Promise((resolve, reject) => {
      this.socket = new SockJS('/ws')
      this.stomp = Stomp.over(this.socket)
      this.stomp.debug = null
      this.stomp.connect({}, frame => {
        this.connected = true
        console.log('WebSocket连接成功', frame)
        this.flushPendingSubscriptions()
        resolve(frame)
      }, error => {
        console.error('WebSocket连接失败', error)
        this.connected = false
        reject(error)
      })
    })
  }

  /**
   * 订阅消息
   */
  subscribe(destination, callback) {
    if (!this.stomp || !this.connected) {
      console.log('WebSocket未连接，缓存订阅：', destination)
      this.pendingSubscriptions.set(destination, callback)
      return null
    }

    console.log('开启订阅', destination)
    const subscription = this.stomp.subscribe(destination, message => {
      const data = JSON.parse(message.body)
      callback(data)
    })

    this.subscriptions.set(destination, subscription)
    return subscription
  }

  /**
   * 取消订阅
   */
  unsubscribe(destination) {
    const subscription = this.subscriptions.get(destination)
    if (subscription) {
      console.log('关闭订阅', destination)
      subscription.unsubscribe()
      this.subscriptions.delete(destination)
    }
  }

  /**
   * 处理等待订阅
   */
  flushPendingSubscriptions() {
    this.pendingSubscriptions.forEach((callback, destination) => {
      this.subscribe(destination, callback)
    })
    this.pendingSubscriptions.clear()
  }

  /**
   * 断开连接
   */
  disconnect() {
    if (this.stomp && this.connected) {
      this.stomp.disconnect(() => {
        console.log('WebSocket断开')
      })
    }

    this.connected = false
  }
}

export default new WebSocketManager()
