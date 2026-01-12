import qs from 'qs'
import api from '../../custom-axios'

const fileApi = '/api/file'

export default {
  // 分页查询文件列表
  pageFileList: params => {
    return api.httpRequest().get(`${fileApi}/pageFileList`, params)
  },
  // 下载文件
  downloadFile: params => {
    return api.httpRequest().get(`${fileApi}/downloadFile`, params, { responseType: 'blob' })
  },
  // 删除文件
  deleteFile: params => {
    return api.httpRequest().post(`${fileApi}/deleteFile`, qs.stringify(params), {
      headers: { 'Content-Type': 'application/x-www-form-urlencoded;charset=UTF-8' }
    })
  },
  // 测试文件删除
  deleteFileTest: params => {
    return api.httpRequest().post(`${fileApi}/deleteFileTest`, qs.stringify(params), {
      headers: { 'Content-Type': 'application/x-www-form-urlencoded;charset=UTF-8' }
    })
  }
}
