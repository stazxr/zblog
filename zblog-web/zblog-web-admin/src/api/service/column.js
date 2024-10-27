import qs from 'qs'
import api from '../custom-axios'

const columnApi = '/api/columns'

export default {
  // 分页查询专栏列表
  pageColumnList: params => {
    return api.httpRequest().get(`${columnApi}/pageColumnList`, params)
  },
  // 查询专栏详情
  queryColumnDetail: params => {
    return api.httpRequest().get(`${columnApi}/queryColumnDetail`, params)
  },
  // 新增专栏
  addColumn: params => {
    return api.httpRequest().post(`${columnApi}/addColumn`, params)
  },
  // 编辑专栏
  editColumn: params => {
    return api.httpRequest().post(`${columnApi}/editColumn`, params)
  },
  // 配置专栏
  configColumn: params => {
    return api.httpRequest().post(`${columnApi}/configColumn`, params)
  },
  // 删除专栏
  deleteColumn: params => {
    return api.httpRequest().post(`${columnApi}/deleteColumn`, qs.stringify(params), {
      headers: { 'Content-Type': 'application/x-www-form-urlencoded;charset=UTF-8' }
    })
  },
  // 查询非专栏对应的文章列表
  queryArticleListNotColumn: params => {
    return api.httpRequest().post(`${columnApi}/queryArticleListNotColumn`, params)
  }
}
