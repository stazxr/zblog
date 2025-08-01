import api from './../custom-axios'

const menuApi = '/api/menu'

export default {
  // 查询用户菜单列表（树）
  queryUserMenuTree: params => {
    return api.httpRequest().get(`${menuApi}/queryUserMenuTree`, params)
  }
}
