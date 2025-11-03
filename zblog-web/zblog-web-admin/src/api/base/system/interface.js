import api from '../../custom-axios'

const interfaceApi = '/api/interfaces'

export default {
  // 分页查询接口列表
  pageInterfaceList: params => {
    return api.httpRequest().get(`${interfaceApi}/pageList`, params)
  }
}
