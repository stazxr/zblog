import qs from 'qs'
import api from '../../custom-axios'

const themeApi = '/api/themes'

export default {
  // 分页查询主题列表
  pageThemeList: params => {
    return api.httpRequest().get(`${themeApi}/pageList`, params)
  },
  // 查询主题详情
  queryThemeDetail: params => {
    return api.httpRequest().get(`${themeApi}/queryThemeDetail`, params)
  },
  // 新增主题
  addTheme: params => {
    return api.httpRequest().post(`${themeApi}/addTheme`, params)
  },
  // 编辑主题
  editTheme: params => {
    return api.httpRequest().post(`${themeApi}/editTheme`, params)
  },
  // 删除主题
  deleteTheme: params => {
    return api.httpRequest().post(`${themeApi}/deleteTheme`, qs.stringify(params), {
      headers: { 'Content-Type': 'application/x-www-form-urlencoded;charset=UTF-8' }
    })
  }
}
