import api from '../../custom-axios'

const hostApi = '/api/host'

export default {
  queryHostData: params => {
    return api.httpRequest().get(`${hostApi}/data`, params)
  }
}
