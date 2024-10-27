import qs from 'qs'
import api from '../custom-axios'

const albumApi = '/api/albums'

export default {
  // 分页查询相册列表
  pageList: params => {
    return api.httpRequest().get(`${albumApi}/pageAlbumList`, params)
  },
  // 查询相册详情
  queryAlbumDetail: params => {
    return api.httpRequest().get(`${albumApi}/queryAlbumDetail`, params)
  },
  // 新增或编辑相册
  addOrEditAlbum: params => {
    return api.httpRequest().post(`${albumApi}/addOrEditAlbum`, params)
  },
  // 删除相册
  deleteAlbum: params => {
    return api.httpRequest().post(`${albumApi}/deleteAlbum`, qs.stringify(params), {
      headers: { 'Content-Type': 'application/x-www-form-urlencoded;charset=UTF-8' }
    })
  },
  // 分页查询照片列表
  pagePhotoList: params => {
    return api.httpRequest().get(`${albumApi}/pagePhotoList`, params)
  },
  // 上传相册照片
  saveAlbumPhoto: params => {
    return api.httpRequest().post(`${albumApi}/saveAlbumPhoto`, params)
  },
  // 移动相册照片
  moveAlbumPhoto: params => {
    return api.httpRequest().post(`${albumApi}/moveAlbumPhoto`, params)
  },
  // 删除相册照片
  deleteAlbumPhoto: params => {
    return api.httpRequest().post(`${albumApi}/deleteAlbumPhoto`, params)
  },
  // 查询用户相册列表
  queryUserAlbumList: params => {
    return api.httpRequest().get(`${albumApi}/queryUserAlbumList`, params)
  },
  // 分页查询回收站照片列表
  pageDeletePhotoList: params => {
    return api.httpRequest().get(`${albumApi}/pageDeletePhotoList`, params)
  },
  // 永久删除相册照片
  deleteAlbumPhotoForever: params => {
    return api.httpRequest().post(`${albumApi}/deleteAlbumPhotoForever`, params)
  },
  // 恢复相册照片
  recoverAlbumPhoto: params => {
    return api.httpRequest().post(`${albumApi}/recoverAlbumPhoto`, params)
  }
}
