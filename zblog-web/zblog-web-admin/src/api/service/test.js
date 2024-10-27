import api from '../custom-axios'

const testApi = '/api/test'

export default {
  // 测试POI
  excelPoi: params => {
    return api.httpRequest().post(`${testApi}/excelPoi`, params, { responseType: 'blob' })
  }
}
