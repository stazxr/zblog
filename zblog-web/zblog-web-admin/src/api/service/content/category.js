import qs from 'qs'
import api from '../../custom-axios'

const categoryApi = '/api/categories'

export default {
  // 查询分类树列表
  queryCategoryTree: params => {
    return api.httpRequest().get(`${categoryApi}/queryCategoryTree`, params)
  },
  // 查询一级类别列表
  queryFirstCategoryList: params => {
    return api.httpRequest().get(`${categoryApi}/queryFirstCategoryList`, params)
  },
  // 查询分类详情
  queryCategoryDetail: params => {
    return api.httpRequest().get(`${categoryApi}/queryCategoryDetail`, params)
  },
  // 新增类别
  addCategory: params => {
    return api.httpRequest().post(`${categoryApi}/addCategory`, params)
  },
  // 编辑类别
  editCategory: params => {
    return api.httpRequest().post(`${categoryApi}/editCategory`, params)
  },
  // 删除类别
  deleteCategory: params => {
    return api.httpRequest().post(`${categoryApi}/deleteCategory`, qs.stringify(params), {
      headers: { 'Content-Type': 'application/x-www-form-urlencoded;charset=UTF-8' }
    })
  }
}
