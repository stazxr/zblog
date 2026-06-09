import axios from 'axios'
import { Message } from 'element-ui'
import JsonBig from 'json-bigint'
import { isJson } from '@/utils/validate'
import { refreshRequest } from '@/utils/refreshManager'
import { gotoLogin } from '@/utils/authManager'

const defaultTimeout = 120000

// 错误码
const defaultStatusMessageMap = {
  400: '请求参数错误，请检查输入内容',
  401: '登录状态已失效，请重新登录',
  403: '没有访问权限，请联系管理员',
  404: '请求的资源不存在',
  429: '请求过于频繁，请稍后再试',
  500: '服务器内部错误，请联系管理员',
  502: '网关异常，请稍后再试',
  503: '系统繁忙，请稍后再试',
  504: '服务器响应超时，请稍后重试'
}

// create instance
const instance = axios.create()

// baseURL
instance.defaults.baseURL = process.env.VUE_APP_BASE_API

// 超时时间
instance.defaults.timeout = defaultTimeout

// 是否允许携带凭证
instance.defaults.withCredentials = true

// 处理超过 16 位数字精度丢失问题
instance.defaults.transformResponse = [
  function(data) {
    try {
      if (isJson(data)) {
        return JsonBig.parse(data, {
          // 指定在解析过程中是否严格检查 JSON 格式的语法
          strict: false,
          // 指定解析后的值是否应该存储为字符串而不是 BigNumber 类型
          storeAsString: true,
          // 指定是否始终将所有数字解析为 BigNumber 类型，默认情况下，只有超出 JavaScript 数字范围的数字才会解析为 BigNumber 类型
          alwaysParseAsBig: false,
          // 指定是否使用本地的 BigInt 类型而不是 bignumber.js 库来表示大整数
          useNativeBigInt: false,
          // 指定在解析 JSON 时如何处理原型污染（prototype pollution）问题
          // 默认为 "error"，表示在遇到原型污染时抛出错误；可以设置为 "remove"，表示移除原型污染属性；或者设置为 "ignore"，表示忽略原型污染问题
          protoAction: 'error',
          // 指定在解析 JSON 时如何处理 JavaScript 构造函数的问题
          // 默认为 "error"，表示在遇到 JavaScript 构造函数时抛出错误；可以设置为 "remove"，表示移除构造函数；或者设置为 "ignore"，表示忽略构造函数问题
          constructorAction: 'error'
        })
      }
    } catch (e) {
      console.log('transform response error [' + data + ']', e)
    }
    return data
  }
]

// 自定义响应成功的HTTP状态码
instance.defaults.validateStatus = status => {
  return status >= 200 && status < 300
}

// 设置请求拦截器
instance.interceptors.request.use(config => {
  // 根据 User-Agent 判断客户端类型
  const ua = navigator.userAgent.toLowerCase()
  const isMobile = /mobile|android|iphone|ipad|phone/i.test(ua)
  config.headers['x-client-type'] = isMobile ? '01' : '02'

  // return config
  return config
}, error => {
  return Promise.reject(error)
})

// 设置响应拦截器
instance.interceptors.response.use(response => {
  if (response.status === 200 && response.data) {
    const result = response.data
    const responseType = response.config.responseType
    if (responseType === 'json') {
      if (result.code === '000000000') {
        return result
      } else {
        Message.error(result.message || '操作失败')
        return Promise.reject(new Error(result.message || '操作失败'))
      }
    } else {
      return result
    }
  }
}, error => {
  console.log('error', error)
  const { response, request } = error
  if (response) {
    // 请求成功发出且服务器也响应了状态码，但状态代码超出了 2xx 的范围
    const status = response.status
    const result = response.data
    const code = result ? result.code : null
    const type = result ? result.type : null
    const errorMessage = result ? result.message : null
    const _errorMessage = errorMessage || defaultStatusMessageMap[status] || `系统发生未知错误`
    if (status === 401) {
      return handle401(code, type, _errorMessage, response, error)
    }

    Message.error(_errorMessage)
    return Promise.reject(error)
  } else if (request) {
    // 请求已经成功发起，但没有收到响应
    if (!window.navigator.onLine) {
      // 断网处理
      Message.error('网络异常，请检查网络链接')
    } else {
      if (error.message && error.message.includes('timeout')) {
        Message.error('请求超时，请稍后再试')
      } else {
        Message.error('服务无响应')
      }
    }
    return Promise.reject(new Error())
  } else {
    const defaultMessage = '服务异常，请稍后再试'
    Message.error(defaultMessage)
    return Promise.reject(new Error(defaultMessage))
  }
})

function handle401(code, type, message, response, error) {
  switch (type) {
    case 'ST000001':
      Message.error(message)
      return Promise.reject(new Error(code))

    case 'ST000002':
      Message.error(message)
      gotoLogin()
      return Promise.reject(error)

    case 'ST000003':
      return refreshRequest(response.config, config => instance(config), gotoLogin, message)

    default:
      Message.error(message)
      return Promise.reject(error)
  }
}

export const get = (url, params, requestItem = {}) => {
  const options = {
    method: 'get',
    url,
    // 是与请求一起发送的 URL 参数，必须是一个简单对象或 URLSearchParams 对象
    params,
    // 自定义请求头
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    },
    // 浏览器将要响应的数据类型，['arraybuffer', 'document', 'json', 'text', 'stream', 'blob'(浏览器专属)]
    responseType: 'json',
    ...requestItem
  }
  return instance(options)
}

export const post = (url, data, requestItem = {}) => {
  const options = {
    method: 'post',
    url,
    // 作为请求体被发送的数据，[string, plain object, ArrayBuffer, ArrayBufferView, URLSearchParams]
    // 浏览器专属: FormData, File, Blob; Node 专属: Stream, Buffer
    // 可选语法，Country=China&City=Xian，只有 value 会被发送，key 则不会
    data,
    // 自定义请求头
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    },
    // 浏览器将要响应的数据类型，['arraybuffer', 'document', 'json', 'text', 'stream', 'blob'(浏览器专属)]
    responseType: 'json',
    ...requestItem
  }
  return instance(options)
}

export default {
  httpRequest() {
    return {
      get,
      post
    }
  }
}
