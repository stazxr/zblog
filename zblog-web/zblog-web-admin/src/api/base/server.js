import api from '../custom-axios'

const serverApi = '/api/server'

export default {
  queryServerData: params => {
    return api.httpRequest().get(`${serverApi}/data`, params)
  }
}
