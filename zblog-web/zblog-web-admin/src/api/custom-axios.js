import axios from 'axios'
import router from '../router/routers'
import { Message } from 'element-ui'
import JsonBig from 'json-bigint'
import { setToken, getToken, removeToken } from '@/utils/token'
import { isJson } from '@/utils/validate'

const defaultTimeout = 120000

// 错误码
const statusMessageMap = {
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

// 是否允许携带凭证
instance.defaults.withCredentials = false

// 自定义响应成功的HTTP状态码
instance.defaults.validateStatus = status => {
  return status >= 200 && status < 300
}

// 设置请求拦截器
instance.interceptors.request.use(config => {
  // set default header
  const token = getToken()
  if (token) {
    config.headers.Authorization = token
  }
  // config.headers['Content-Type'] = 'application/json;charset=UTF-8'

  // 根据 User-Agent 判断客户端类型
  // const ua = navigator.userAgent.toLowerCase()
  // const isMobile = /mobile|android|iphone|ipad|phone/i.test(ua)
  // config.headers['x-client-type'] = isMobile ? '01' : '02'

  // return config
  return config
}, error => {
  return Promise.reject(error)
})

// 设置响应拦截器
instance.interceptors.response.use(response => {
  // response {data: {}, status: 200, statusText: 'OK', headers: {}, config: {}, request: {}}
  // data formatter: {code: 200, message, '操作成功', data: {}, identifier: 1} => code maybe [200, 401, 403, 404, 500...]
  // data maybe really data, eg: data => 后端直接返回了数据，没有封装为上述格式
  // return Promise.reject(new Error(res.msg || 'Error'))
  console.log('response', response)
  if (response.status === 200 && response.data) {
    // refresh Token
    const newToken = response.headers['x-new-token']
    if (newToken) {
      setToken(newToken)
      console.log('refresh token success')
    }

    const result = response.data
    const responseType = response.config.responseType
    if (responseType === 'json') {
      return responseJsonHandler(result)
    } else {
      return result
    }
  }
}, error => {
  // 统一异常处理，一般不会出现401|403|404，后端将其作为业务异常处理了
  const { response, request } = error
  if (response) {
    console.log('response', response)
    // 请求成功发出且服务器也响应了状态码，但状态代码超出了 2xx 的范围
    const status = response.status
    const errorMessage = response && response.data ? response.data.message : null
    const errorCode = response && response.data ? response.data.errorCode : null
    const _errorMessage = errorMessage || statusMessageMap[status] || `系统发生未知错误`
    Message.error(_errorMessage)
    if (status === 401) {
      logout(false)
    }
    return Promise.reject(new Error(errorCode))
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
  }

  Message.error('系统发生未知错误') // default
  return Promise.reject(new Error())
})

function logout(expired) {
  if (expired) {
    window.sessionStorage.setItem('point', '401')
  }

  removeToken()
  router.replace('/login')
}

function responseJsonHandler(result) {
  const { code, success, message } = result
  if (code === 200) {
    if (success) {
      // 业务成功
      return result
    } else {
      // 业务失败
      Message.error(message || '操作失败')
      return Promise.reject(new Error(message || '操作失败'))
    }
  } else if (code === 401) {
    const identifier = result.identifier || 10001
    if (identifier === 10001) {
      Message.error(result.message || '登录失败')
      return Promise.reject(new Error(result.message || '登录失败'))
    } else if (identifier === 10002) {
      // 密码过期，跳转到修改密码的页面
      Message.error(result.message || '请修改密码')
      return Promise.reject(new Error('' + identifier))
    } else if (identifier === 900002) {
      logout(true)
    } else if (identifier === 900003 || identifier === 900004) {
      Message.error(result.message || '系统异常')
      return Promise.reject(new Error(result.message || '系统异常'))
    } else {
      Message.error(result.message || '请登录')
      logout(false)
    }
  } else if (code === 404) {
    Message.error(result.message || '请求资源不存在')
    return Promise.reject(new Error(result.message || '404'))
  } else {
    Message.error(result.message || '系统发生未知错误')
    return Promise.reject(new Error(result.message || '系统发生未知错误'))
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
  console.log(options, options)
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
