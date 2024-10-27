<template>
  <div>
    <el-dialog append-to-body :close-on-click-modal="false" :before-close="handleClose" :visible.sync="dialogVisible" title="文件上传" width="520px">
      <el-form ref="addForm" size="small">
        <el-form-item>
          <el-upload
            ref="upload"
            name="file"
            :action="$store.state.api.testUploadFile + '?type=' + type"
            :headers="headers"
            :auto-upload="false"
            :limit="maxUploadSize"
            :on-exceed="handleExceed"
            :on-change="handleChange"
            :on-remove="handleRemove"
            :on-error="handleError"
            :on-success="handleSuccess"
            :on-preview="handlePreview"
            :before-upload="beforeUpload"
            :before-remove="beforeRemove"
            :file-list="fileList"
          >
            <div class="custom-upload" style="width: 480px;"><i class="el-icon-upload" /> 添加文件</div>
            <div slot="tip" class="el-upload__tip">
              可上传任意格式文件（文件大小小于等于10M），功能仅作演示用，每日零点清理手动上传的文件。
            </div>
          </el-upload>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="text" @click="cancel">取 消</el-button>
        <el-button :loading="submitLoading" type="primary" @click="submit">确 认</el-button>
      </div>
    </el-dialog>

    <!-- preview -->
    <el-dialog :visible.sync="previewDialogVisible">
      <img width="100%" :src="previewImageUrl" alt="">
    </el-dialog>
  </div>
</template>

<script>
import { getToken } from '@/utils/token'
export default {
  props: {
    dialogVisible: {
      type: Boolean,
      default: false
    }
  },
  data() {
    return {
      type: null,
      maxUploadSize: 1,
      submitLoading: false,
      previewDialogVisible: false,
      previewImageUrl: '',
      headers: {
        Authorization: ''
      },
      fileList: []
    }
  },
  methods: {
    initData(type) {
      this.$nextTick(() => {
        this.type = type
      })
    },
    doClose(result = false) {
      this.submitLoading = false
      this.fileList = []
      this.$refs.upload.clearFiles()
      this.$emit('uploadDone', result)
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
    submit() {
      this.headers.Authorization = getToken()
      this.$refs.upload.submit()
    },
    handleExceed(files, fileList) {
      this.$message.warning(`当前限制选择 ${this.maxUploadSize} 个文件，本次选择了 ${files.length} 个文件，共选择了 ${files.length + fileList.length} 个文件`)
    },
    handleChange(file, fileList) {
      // this.fileList = fileList.slice(-3)
    },
    handleRemove(file, fileList) {
      if (file && file.status === 'success' && file.response && file.response.code === 200) {
        const data = file.response.data
        if (data && Array.isArray(data)) {
          const ids = []
          for (let i = 0; i < data.length; i++) {
            ids.push(data[i].id)
          }

          this.$mapi.file.testDeleteFile(ids)
        }
      }
    },
    handleError(err, file, fileList) {
      try {
        this.submitLoading = false
        this.$message.error(JSON.parse(err.message.toString()).message)
      } catch {
        this.$message.error('系统发生未知错误')
      }
    },
    handleSuccess(response, file, fileList) {
      this.submitLoading = false
      if (response.code === 200) {
        // success
        this.$message.success(response.message || '上传成功')
        this.doClose(true)
      } else {
        // error
        this.$refs.upload.handleError(response, file)
      }
    },
    handlePreview(file) {
      if (file && file.url) {
        this.previewImageUrl = file.url
        this.previewDialogVisible = true
      }
    },
    beforeUpload(file) {
      this.submitLoading = true

      let isLt2M = true
      isLt2M = file.size / 1024 / 1024 < 10
      if (!isLt2M) {
        this.submitLoading = false
        this.$message.error('上传文件大小不能超过 10MB!')
      }
      return isLt2M
    },
    beforeRemove(file, fileList) {
      return this.$confirm(`确定移除 ${file.name} ？`)
    }
  }
}
</script>

<style rel="stylesheet/scss" lang="scss" scoped>

</style>
