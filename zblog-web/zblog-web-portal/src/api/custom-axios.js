// import Vue from 'vue'
import axios from 'axios'
import JSONBIG from 'json-bigint'
import { getToken } from '../utils/token'

const defaultTimeout = 120000

// create instance
const instance = axios.create()

// baseURL
// instance.defaults.baseURL = process.env.VUE_APP_BASE_API

// 超时时间
instance.defaults.timeout = defaultTimeout

// 处理超过16位数字精度丢失问题
instance.defaults.transformResponse = [
  function(data) {
    if (data instanceof Blob) {
      return data
    }

    const json = JSONBIG({
      storeAsString: true
    })
    return json.parse(data)
  }
]

// 是否允许携带凭证
instance.defaults.withCredentials = true

// 设置请求拦截器
instance.interceptors.request.use(
  config => {
    // set default header
    config.headers.Authorization = getToken()
    // config.headers['Content-Type'] = 'application/json;charset=UTF-8'
    // config.headers['Content-Type'] = 'application/x-www-form-urlencoded;charset=UTF-8'
    return config
  },
  error => {
    return Promise.reject(error)
  }
)

// 设置响应拦截器
instance.interceptors.response.use(
  response => {
    // response {data: {}, status: 200, statusText: 'OK', headers: {}, config: {}, request: {}}
    // data formatter: {code: 200, message, '操作成功', data: {}, identifier: 1} => code maybe [200, 401, 403, 404, 500...]
    // data maybe really data, eg: data => 后端直接返回了数据，没有封装为上述格式
    // return Promise.reject(new Error(res.msg || 'Error'))
    if (response.status === 200 && response.data) {
      const result = response.data
      return responseHandler(result)
    }
  },
  error => {
    return Promise.reject(error)
  }
)

function responseHandler(result) {
  const code = result.code || 200
  if (code === 200) {
    // success, return data
    return result
  } else {
    return result
    // Vue.prototype.$toast({ type: 'error', message: '系统异常' })
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
    return { get, post }
  }
}
