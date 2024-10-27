import qs from 'qs'
import api from '../custom-axios'

const fileApi = '/api/file'

export default {
  // 分页查询文件列表
  pageFileList: params => {
    return api.httpRequest().get(`${fileApi}/queryFileListByPage`, params)
  },
  // 下载文件
  downloadFile: params => {
    return api.httpRequest().get(`${fileApi}/downloadFile`, params, { responseType: 'blob' })
  },
  // 删除文件
  deleteFile: params => {
    return api.httpRequest().post(`${fileApi}/deleteFile`, params)
  },
  // 测试文件删除
  testDeleteFile: params => {
    return api.httpRequest().post(`${fileApi}/testDeleteFile`, params)
  },
  // 获取配置信息
  getConfigInfo: params => {
    return api.httpRequest().get(`${fileApi}/getConfigInfo`, params)
  },
  // 保存配置信息
  setConfigInfo: params => {
    return api.httpRequest().post(`${fileApi}/setConfigInfo`, params)
  },
  // 获取激活的存储类型
  getConfigStorageType: params => {
    return api.httpRequest().get(`${fileApi}/getConfigStorageType`, params)
  },
  // 激活存储配置
  activeStorageConfig: params => {
    return api.httpRequest().post(`${fileApi}/activeStorageConfig`, qs.stringify(params), {
      headers: { 'Content-Type': 'application/x-www-form-urlencoded;charset=UTF-8' }
    })
  }
}
