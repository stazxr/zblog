// 获取环境变量
const baseUrl = process.env.VUE_APP_BASE_API === '/' ? '' : process.env.VUE_APP_BASE_API || ''

// 创建一个函数用于拼接完整的 API 地址
const createApiUrl = (path) => `${baseUrl}${path}`

const api = {
  state: {
    baseApi: baseUrl,
    // Druid
    sqlApi: createApiUrl('/druid/index.html'),
    // 通用文件上传
    fileUploadApi: createApiUrl('/api/file/uploadFile'),
    // 测试文件上传
    uploadFileTest: createApiUrl('/api/file/uploadFileTest'),
    // Swagger
    swaggerApi: createApiUrl('/doc.html')
  }
}

export default api
