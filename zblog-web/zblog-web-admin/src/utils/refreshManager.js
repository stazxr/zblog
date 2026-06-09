import { Message } from 'element-ui'
import authApi from '@/api/base/auth'

// 是否正在续签
let isRefreshing = false

// 等待续签完成的请求队列
let requestQueue = []

/**
 * 处理续签请求
 *
 * @param config 原始请求配置
 * @param requestExecutor 请求执行器（一般为 instance(config)）
 * @param onLoginExpired 登录失效回调
 * @param errorMessage 错误提示
 * @returns {Promise<unknown>}
 */
export function refreshRequest(config, requestExecutor, onLoginExpired, errorMessage) {
  if (config._retry) {
    onLoginExpired()
    return Promise.reject(new Error('refresh retry failed'))
  }

  config._retry = true

  return new Promise((resolve, reject) => {
    requestQueue.push({
      resolve: () => resolve(requestExecutor(config)),
      reject: () => reject(new Error('refresh failed'))
    })

    if (!isRefreshing) {
      doRefresh(onLoginExpired, errorMessage)
    }
  })
}

/**
 * 执行续签
 *
 * @param onLoginExpired 登录失效回调
 * @param errorMessage 错误提示
 */
function doRefresh(onLoginExpired, errorMessage) {
  isRefreshing = true

  const startTime = Date.now()

  authApi.refresh()
    .then(res => {
      const success = res.data === true
      const cost = Date.now() - startTime

      if (success) {
        console.info(
          `[AUTH][${new Date().toLocaleString()}][REFRESH] 续签成功，耗时=${cost}ms，等待重试请求数=${requestQueue.length}`
        )
      } else {
        Message.error(errorMessage || '登录已过期，请重新登录')
        console.warn(
          `[AUTH][${new Date().toLocaleString()}][REFRESH] 续签失败，耗时=${cost}ms，等待重试请求数=${requestQueue.length}`
        )
      }

      processQueue(success)

      if (!success) {
        onLoginExpired()
      }
    })
    .catch(error => {
      const cost = Date.now() - startTime

      console.error(
        `[AUTH][${new Date().toLocaleString()}][REFRESH] 续签异常，耗时=${cost}ms，等待重试请求数=${requestQueue.length}`,
        error
      )

      processQueue(false)
      Message.error(errorMessage || '登录已过期，请重新登录')
      onLoginExpired()
    })
    .finally(() => {
      isRefreshing = false
    })
}

/**
 * 处理等待队列
 *
 * @param success 是否续签成功
 */
function processQueue(success) {
  requestQueue.forEach(item => {
    success ? item.resolve() : item.reject()
  })

  requestQueue = []
}
