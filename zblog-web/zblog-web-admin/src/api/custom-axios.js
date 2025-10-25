import axios from 'axios'
import { Message } from 'element-ui'
import JsonBig from 'json-bigint'
import { setToken, getToken, removeToken } from '@/utils/token'
import { isJson } from '@/utils/validate'

const defaultTimeout = 120000

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
      return data
    } catch (e) {
      console.log('transform response error [' + data + ']', e)
      throw e
    }
  }
]

// 是否允许携带凭证
instance.defaults.withCredentials = true

// 自定义响应成功的HTTP状态码
instance.defaults.validateStatus = status => {
  // default: status >= 200 && status < 300
  // 3XX: 临时重定向，永久重定向，缓存
  return /^([23])\d{2}$/.test(String(status))
}

// 设置请求拦截器
instance.interceptors.request.use(config => {
  // set default header
  config.headers.Authorization = getToken()
  // config.headers['Content-Type'] = 'application/json;charset=UTF-8'

  // 根据 User-Agent 判断客户端类型
  const ua = navigator.userAgent.toLowerCase()
  const isMobile = /mobile|android|iphone|ipad|phone/i.test(ua)
  config.headers['X-Client-Type'] = isMobile ? '01' : '02'

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
  if (response.status === 200 && response.data) {
    // refresh Token
    if (response.headers['new-token'] !== undefined && response.headers['new-token'] !== '') {
      setToken(response.headers['new-token'])
      console.log('refresh token success')
    }

    const result = response.data
    return responseHandler(result)
  }
}, error => {
  // 统一异常处理，一般不会出现401|403|404，后端将其作为业务异常处理了
  let errorMsg
  console.log('axios error', error)
  const { response, request } = error
  console.log('response error', response)
  console.log('request error', request)
  if (response) {
    // 请求成功发出且服务器也响应了状态码，但状态代码超出了 2xx 的范围
    switch (response.status) {
      case 400:
        errorMsg = '请求数据格式不正确'
        break
      case 401:
        logout(false)
        break
      case 403:
        errorMsg = '权限不足，请联系管理员添加权限'
        break
      case 404:
        errorMsg = '请求资源不存在'
        break
      case 503:
        errorMsg = '系统繁忙，请稍后再试'
        break
      default:
        errorMsg = '系统发生未知错误'
        break
    }
  } else if (request) {
    // 请求已经成功发起，但没有收到响应
    if (!window.navigator.onLine) {
      // 断网处理
      errorMsg = '没网啦~'
    } else {
      if (error.message && error.message.includes('timeout')) {
        errorMsg = '请求超时'
      } else {
        errorMsg = '服务无响应'
      }
    }
  }

  errorMsg = errorMsg || '系统发生未知错误' // default
  Message.error(errorMsg)
  return Promise.reject(new Error(errorMsg))
})

function logout(expired) {
  if (expired) {
    window.sessionStorage.setItem('point', '401')
  }

  removeToken()
  location.reload()
}

function responseHandler(result) {
  const code = result.code || 200
  if (code === 200) {
    // success, return data
    return result
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
  } else if (code === 403) {
    Message.error(result.message || '权限不足，请联系管理员')
    return Promise.reject(new Error(result.message || '403'))
  } else if (code === 404) {
    Message.error(result.message || '请求资源不存在')
    return Promise.reject(new Error(result.message || '404'))
  } else {
    Message.error(result.message || '系统发生未知错误')
    return Promise.reject(new Error(result.message || '系统发生未知错误'))
  }
}

export const get = (url, params, requestItem) => {
  return new Promise((resolve, reject) => {
    let options = {
      method: 'get',
      url: url,
      // 是与请求一起发送的 URL 参数，必须是一个简单对象或 URLSearchParams 对象
      params: params,
      // 自定义请求头
      headers: {
        'Content-Type': 'application/json;charset=UTF-8'
      },
      // 浏览器将要响应的数据类型，['arraybuffer', 'document', 'json', 'text', 'stream', 'blob'(浏览器专属)]
      responseType: 'json'
    }

    options = Object.assign({}, options, requestItem)
    instance(options).then(response => {
      resolve(response)
    }).catch(err => {
      reject(err)
    })
  })
}

export const post = (url, params, requestItem) => {
  return new Promise((resolve, reject) => {
    let options = {
      method: 'post',
      url: url,
      // 作为请求体被发送的数据，[string, plain object, ArrayBuffer, ArrayBufferView, URLSearchParams]
      // 浏览器专属: FormData, File, Blob; Node 专属: Stream, Buffer
      // 可选语法，Country=China&City=Xian，只有 value 会被发送，key 则不会
      data: params,
      // 自定义请求头
      headers: {
        'Content-Type': 'application/json;charset=UTF-8'
      },
      // 浏览器将要响应的数据类型，['arraybuffer', 'document', 'json', 'text', 'stream', 'blob'(浏览器专属)]
      responseType: 'json'
    }

    options = Object.assign({}, options, requestItem)
    instance(options).then(response => {
      resolve(response)
    }).catch(err => {
      reject(err)
    })
  })
}

export default {
  httpRequest() {
    return {
      get,
      post
    }
  }
}
