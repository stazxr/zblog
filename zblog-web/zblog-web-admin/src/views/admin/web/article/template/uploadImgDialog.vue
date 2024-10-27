<template>
  <div>
    <el-drawer title="图片上传" :visible.sync="dialogVisible" destroy-on-close :before-close="handleClose" size="60%">
      <div class="demo-drawer__content">
        <div style="margin: 10px 50px;">
          <el-upload
            ref="upload"
            name="file"
            class="avatar-uploader"
            list-type="picture-card"
            :action="$store.state.api.fileUploadApi"
            :headers="headers"
            :limit="isReplace ? 1 : maxUploadSize"
            :show-file-list="true"
            :before-upload="beforeUpload"
            :before-remove="beforeRemove"
            :on-exceed="handleExceed"
            :on-preview="handlePictureCardPreview"
            :on-remove="handleRemove"
            :on-error="handleError"
            :on-success="handleSuccess"
          >
            <img v-if="imageUrl" :src="imageUrl" alt="" class="avatar">
            <i v-else class="el-icon-plus avatar-uploader-icon" />
          </el-upload>
        </div>
        <div class="demo-drawer__footer">
          <el-col :span="6" :offset="18">
            <el-button @click="cancel">取 消</el-button>
            <el-button type="primary" @click="submit">确定</el-button>
          </el-col>
        </div>
      </div>
    </el-drawer>

    <el-dialog :visible.sync="previewDialogVisible">
      <img width="100%" :src="previewDialogImageUrl" alt="">
    </el-dialog>
  </div>
</template>

<script>
import * as imageConversion from 'image-conversion'
import { getToken } from '@/utils/token'
export default {
  name: 'UploadArticleImgDialog',
  props: {
    dialogVisible: {
      type: Boolean,
      default: false
    },
    maxUploadSize: {
      type: Number,
      default: 1
    }
  },
  data() {
    return {
      imageUrl: '',
      fileList: [],
      isReplace: false,
      replaceIndex: null,
      previewDialogVisible: false,
      previewDialogImageUrl: '',
      headers: {
        Authorization: ''
      }
    }
  },
  methods: {
    initData(isReplace, index) {
      if (isReplace) {
        // 替换只允许上传一张图
        this.isReplace = true
        this.replaceIndex = index
      } else {
        this.isReplace = false
        this.replaceIndex = null
      }
    },
    doClose(submitFlag) {
      if (submitFlag) {
        this.$emit('uploadImgDone', this.fileList, this.replaceIndex)
      } else {
        this.$emit('uploadImgDone')
      }

      this.imageUrl = ''
      this.fileList = []
      this.isReplace = false
      this.replaceIndex = null
    },
    handleClose() {
      this.$confirm('确认关闭？').then(_ => {
        this.doClose(false)
      }).catch(_ => {})
    },
    cancel() {
      this.handleClose()
    },
    handleExceed(files, fileList) {
      this.$message.warning(`当前限制选择 ${this.maxUploadSize} 个文件，本次选择了 ${files.length} 个文件，共选择了 ${files.length + fileList.length} 个文件`)
    },
    handleRemove(file) {
      if (file && file.status === 'success' && file.response && file.response.code === 200) {
        const data = file.response.data
        if (data && Array.isArray(data)) {
          for (let i = 0; i < data.length; i++) {
            this.$mapi.file.deleteFile({ fileId: data[i].id })
          }
        }
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
      if (file.name.indexOf('.') !== -1) {
        const imgType = file.name.substring(file.name.lastIndexOf('.') + 1).toLowerCase()
        if (imgType !== 'jpg' && imgType !== 'jpeg' && imgType !== 'png' && imgType !== 'webp') {
          this.$message.warning('上传文件只能是 jpg, jpeg, png, webp 格式!')
          return false
        }
      } else {
        this.$message.warning('上传文件只能是 jpg, jpeg, png, webp 格式!')
        return false
      }

      // 压缩图片
      this.headers.Authorization = getToken()
      return new Promise(resolve => {
        if (file.size / 1024 < this.$config.UPLOAD_SIZE) {
          resolve(file)
        }

        imageConversion.compressAccurately(file, this.$config.UPLOAD_SIZE).then(res => {
          resolve(res)
        })
      })
    },
    beforeRemove(file) {
      // return this.$confirm(`确定移除 ${file.name} ？`)
    },
    handleSuccess(response, file) {
      if (response.code === 200) {
        // success
        if (response.data && Array.isArray(response.data)) {
          for (let i = 0; i < response.data.length; i++) {
            this.fileList.push(response.data[i])
          }
        }

        this.$message.success(response.message || '上传成功')
      } else {
        // error
        this.$refs.upload.handleError(response, file)
      }
    },
    handlePictureCardPreview(file) {
      this.previewDialogImageUrl = file.url
      this.previewDialogVisible = true
    },
    submit() {
      this.doClose(true)
    }
  }
}
</script>

<style scoped>
.demo-drawer__content {
  display: flex;
  flex-direction: column;
  height: 100%;
}
.demo-drawer__footer {
  margin-top: 10px;
  display: flex;
}
.demo-drawer__footer button {
  flex: 1;
  margin: 5px 20px;
}

.avatar-uploader .el-upload {
  border: 1px dashed #d9d9d9;
  border-radius: 6px;
  cursor: pointer;
  position: relative;
  overflow: hidden;
}
.avatar-uploader .el-upload:hover {
  border-color: #409EFF;
}
.avatar-uploader-icon {
  font-size: 28px;
  color: #8c939d;
  width: 160px;
  height: 90px;
  line-height: 160px;
  text-align: center;
}
.avatar {
  width: 160px;
  height: 90px;
  display: block;
}
::v-deep .el-upload--picture-card {
  width: 160px;
  height: 90px;
  position: relative;
}
::v-deep .el-upload-list--picture-card .el-upload-list__item {
  width: 160px;
  height: 90px;
}

::v-deep .avatar-uploader-icon {
  position: absolute;
  top: 50%;
  left: 50%;
  -webkit-transform: translate(-50%, -50%);
  transform: translate(-50%, -50%);
  line-height: 85px;
}
</style>
