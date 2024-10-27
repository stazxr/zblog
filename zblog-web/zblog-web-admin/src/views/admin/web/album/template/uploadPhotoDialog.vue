<template>
  <el-dialog :close-on-click-modal="false" :before-close="handleClose" :visible.sync="dialogVisible" width="70%" top="10vh">
    <div slot="title" class="dialog-title-container">
      上传照片
    </div>
    <div class="upload-container">
      <el-upload
        ref="upload"
        name="files"
        multiple
        :action="$store.state.api.fileUploadApi"
        :headers="headers"
        list-type="picture-card"
        :before-upload="beforeUpload"
        :on-success="handleSuccess"
        :on-error="handleError"
        :on-remove="handleRemove"
      >
        <i class="el-icon-plus" />
      </el-upload>
    </div>
    <div slot="footer">
      <div class="upload-footer">
        <div class="upload-count">共上传{{ uploadList.length }}张照片</div>
        <div style="margin-left:auto">
          <el-button @click="cancel">取 消</el-button>
          <el-button type="primary" :disabled="uploadList.length === 0" @click="savePhotos">
            开始上传
          </el-button>
        </div>
      </div>
    </div>
  </el-dialog>
</template>

<script>
import { getToken } from '@/utils/token'
export default {
  name: 'UploadPhotoDialog',
  props: {
    dialogVisible: {
      type: Boolean,
      default: false
    }
  },
  data() {
    return {
      submitLoading: false,
      uploadList: [],
      headers: {
        Authorization: ''
      }
    }
  },
  methods: {
    doClose(result = false) {
      this.uploadList = []
      this.submitLoading = false
      this.$refs.upload.clearFiles()
      this.$emit('uploadPhotoDone', result)
    },
    handleClose() {
      if (!this.submitLoading) {
        this.$confirm('确认关闭？').then(_ => {
          this.doClose()
        }).catch(_ => {})
      }
    },
    cancel() {
      this.handleClose()
    },
    handleRemove(file) {
      if (file && file.status === 'success' && file.response && file.response.code === 200) {
        const data = file.response.data
        if (data && Array.isArray(data)) {
          for (let i = 0; i < data.length; i++) {
            this.$mapi.file.deleteFile({ fileId: data[i].id }).then(_ => {
              this.uploadList.forEach((item, index) => {
                if (item.id === data[i].id) {
                  this.uploadList.splice(index, 1)
                }
              })
            })
          }
        }
      }
    },
    handleSuccess(response, file) {
      if (response.code === 200) {
        // success
        if (response.data && Array.isArray(response.data) && response.data.length > 0) {
          for (let i = 0; i < response.data.length; i++) {
            this.uploadList.push(response.data[i])
          }
        }
      } else {
        // error
        this.$refs.upload.handleError(response, file)
      }
    },
    handleError(err) {
      try {
        this.$message.error(JSON.parse(err.message.toString()).message)
      } catch {
        this.$message.error('系统发生未知错误')
      }
    },
    beforeUpload(file) {
      // 只允许上传图片
      if (file.name.indexOf('.') !== -1) {
        const imgType = file.name.substring(file.name.lastIndexOf('.') + 1).toLowerCase()
        if (imgType !== 'jpg' && imgType !== 'jpeg' && imgType !== 'png') {
          this.$message.warning('上传文件只能是 jpg, jpeg, png 格式!')
          return false
        }
      } else {
        this.$message.warning('上传文件只能是 jpg, jpeg, png 格式!')
        return false
      }

      // 设置TOKEN
      this.headers.Authorization = getToken()
    },
    savePhotos() {
      const param = {
        albumId: this.$route.query.albumId,
        photoList: this.uploadList
      }

      this.submitLoading = true
      this.$mapi.album.saveAlbumPhoto(param).then(_ => {
        this.$message.success('上传成功')
        this.doClose(true)
      }).finally(_ => {
        this.submitLoading = false
      })
    }
  }
}
</script>

<style scoped>
.upload {
  height: 100%;
  display: flex;
  align-items: center;
  justify-content: center;
}
.upload-footer {
  display: flex;
  align-items: center;
}
</style>
