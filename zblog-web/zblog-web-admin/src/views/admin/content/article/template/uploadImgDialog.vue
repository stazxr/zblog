<template>
  <div>
    <el-drawer title="封面上传" :visible.sync="dialogVisible" destroy-on-close :before-close="handleClose" size="60%">
      <div class="demo-drawer__content">
        <div style="margin: 10px 50px;">
          <el-upload
            ref="upload"
            name="file"
            class="avatar-uploader"
            list-type="picture-card"
            :action="$store.state.api.fileUploadApi"
            :limit="isReplace ? 1 : limit"
            :show-file-list="true"
            :with-credentials="withCredentials"
            :before-upload="beforeUpload"
            :before-remove="beforeRemove"
            :on-exceed="handleExceed"
            :on-preview="handlePictureCardPreview"
            :on-remove="handleRemove"
            :on-progress="handleProgress"
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

    <el-dialog title="预览" :visible.sync="previewDialogVisible">
      <img width="100%" :src="previewDialogImageUrl" alt="">
    </el-dialog>
  </div>
</template>

<script>
import * as imageConversion from 'image-conversion'
export default {
  name: 'UploadArticleImgDialog',
  props: {
    dialogVisible: {
      type: Boolean,
      default: false
    },
    // 最大文件上传数
    limit: {
      type: Number,
      default: 1
    },
    // 是否携带 Cookie
    withCredentials: {
      type: Boolean,
      default: true
    },
    // 是否启用图片压缩
    compress: {
      type: Boolean,
      default: true
    },
    // 原始图片最大大小，单位：MB
    maxFileSize: {
      type: Number,
      default: 20
    },
    // 图片压缩目标大小，单位：KB
    limitSize: {
      type: Number,
      default: 200
    },
    // 允许上传的图片MIME类型
    accept: {
      type: Array,
      default() {
        return [
          'image/jpeg',
          'image/png',
          'image/webp'
        ]
      }
    }
  },
  data() {
    return {
      imageUrl: '',
      fileList: [],
      isReplace: false,
      replaceIndex: null,
      uploadLoading: false,
      previewDialogVisible: false,
      previewDialogImageUrl: ''
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
      this.uploadLoading = false
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
      this.$message.warning(`当前限制选择 ${this.limit} 个文件，本次选择了 ${files.length} 个文件，共选择了 ${files.length + fileList.length} 个文件`)
    },
    handleRemove(file) {
      if (file && file.status === 'success' && file.response && file.response.code === 200) {
        const data = file.response.data
        if (data && Array.isArray(data)) {
          for (let i = 0; i < data.length; i++) {
            this.$mapi.file.deleteFile({ fileId: data[i].fileId })
          }
        }
      }
    },
    beforeUpload(file) {
      if (!this.accept.includes(file.type)) {
        this.$message.warning('图片格式不支持')
        return false
      }

      if (file.size > this.maxFileSize * 1024 * 1024) {
        this.$message.warning(`图片不能超过${this.maxFileSize}MB`)
        return false
      }

      if (!this.compress || file.size / 1024 <= this.limitSize) {
        return file
      }

      return imageConversion.compressAccurately(file, this.limitSize).then(blob => {
        return new File([blob], file.name, { type: file.type })
      })
    },
    beforeRemove(file) {
      // return this.$confirm(`确定移除 ${file.name} ？`)
    },
    // 上传进度
    handleProgress() {
      this.uploadLoading = true
    },
    handleSuccess(response, file) {
      this.uploadLoading = false
      if (response.code === '000000000') {
        const data = response.data && Array.isArray(response.data) && response.data.length > 0 ? response.data[0] : null
        if (!data) {
          this.$message.error('上传返回数据异常')
          return
        }

        this.$message.success(response.message || '上传成功')
        this.fileList.push(data)
      } else {
        this.$message.error(response.message || '上传失败')
      }
    },
    handleError(err) {
      try {
        this.uploadLoading = false
        this.$message.error(JSON.parse(err.message.toString()).message)
      } catch {
        this.$message.error('上传失败')
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
