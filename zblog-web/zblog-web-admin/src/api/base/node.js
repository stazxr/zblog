import qs from 'qs'
import api from '../custom-axios'

const nodeApi = '/api/node'

export default {
  // 分页查询节点列表
  pageNodeList: params => {
    return api.httpRequest().get(`${nodeApi}/pageList`, params)
  },
  // 查询节点详情
  queryNodeDetail: params => {
    return api.httpRequest().get(`${nodeApi}/queryNodeDetail`, params)
  },
  // 新增节点
  addNode: params => {
    return api.httpRequest().post(`${nodeApi}/addNode`, params)
  },
  // 编辑节点
  editNode: params => {
    return api.httpRequest().post(`${nodeApi}/editNode`, params)
  },
  // 删除节点
  deleteNode: params => {
    return api.httpRequest().post(`${nodeApi}/deleteNode`, qs.stringify(params), {
      headers: { 'Content-Type': 'application/x-www-form-urlencoded;charset=UTF-8' }
    })
  },
  // 测试 SSH 连通性
  sshTest: params => {
    return api.httpRequest().post(`${nodeApi}/sshTest`, params)
  }
}
