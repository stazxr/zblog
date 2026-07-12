import SockJS from 'sockjs-client'
import Stomp from 'stompjs'

class WebSocketManager {
  constructor() {
    this.socket = null
    this.stomp = null
    this.connected = false
    this.subscriptions = new Map()

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
        console.log('WebSocket连接成功')
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
      console.warn('WebSocket未连接')
      return null
    }

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
      subscription.unsubscribe()
      this.subscriptions.delete(destination)
    }
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
