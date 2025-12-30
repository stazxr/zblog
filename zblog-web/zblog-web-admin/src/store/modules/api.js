// 获取环境变量
const wsUrl = process.env.VUE_APP_WS_API || ''
const baseUrl = process.env.VUE_APP_BASE_API === '/' ? '' : process.env.VUE_APP_BASE_API || ''

// 创建一个函数用于拼接完整的 API 地址
const createApiUrl = (path) => `${baseUrl}${path}`

const api = {
  state: {
    baseApi: baseUrl,
    // 通用文件上传
    fileUploadApi: createApiUrl('/api/file/uploadFile'),
    // 文件上传测试接口
    testUploadFile: createApiUrl('/api/file/testUploadFile'),
    // Druid
    sqlApi: createApiUrl('/druid/index.html'),
    // Swagger
    swaggerApi: createApiUrl('/doc.html'),
    // webSsh
    webSshApi: wsUrl ? `${wsUrl}/webssh` : ''
  }
}

export default api
