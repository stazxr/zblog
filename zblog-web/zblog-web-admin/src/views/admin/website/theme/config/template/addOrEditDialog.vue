<template>
  <div>
    <el-dialog
      :title="dialogTitle"
      :visible.sync="dialogVisible"
      :fullscreen="isMobile"
      :close-on-click-modal="false"
      :close-on-press-escape="true"
      :before-close="handleClose"
      append-to-body
      width="520px"
    >
      <el-form ref="addOrEditForm" :inline="!isMobile" :model="formData" :rules="formRules" label-width="80px">
        <el-form-item label="页面名称" prop="pageId">
          <el-select v-model="formData.pageId" :style="isMobile ? '' : 'width: 360px;'" placeholder="请选择页面">
            <el-option v-for="item in pageList" :key="item.id" :label="item.pageName + '（' + item.pageLabel + '）'" :value="item.id" />
          </el-select>
        </el-form-item>
        <el-form-item label="页面封面" prop="pageCover">
          <el-upload
            ref="upload"
            name="file"
            class="upload-cover"
            drag
            :action="$store.state.api.fileUploadApi"
            :show-file-list="false"
            :with-credentials="true"
            :before-upload="beforeUpload"
            :on-error="handleError"
            :on-success="handleSuccess"
          >
            <i v-if="formData.pageCover === null" class="el-icon-upload" />
            <div v-if="formData.pageCover === null" class="el-upload__text">
              将文件拖到此处，或<em>点击上传</em>
            </div>
            <img v-else :src="formData.pageCover" width="360px" height="180px" alt="">
          </el-upload>
          <el-button v-if="formData.pageCover !== null" type="danger" @click="removeImg">清除图片</el-button>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="text" @click="cancel">取 消</el-button>
        <el-button :loading="submitLoading" type="primary" @click="submit">确 认</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
export default {
  props: {
    dialogVisible: {
      type: Boolean,
      default: false
    },
    dialogTitle: {
      type: String,
      default: ''
    }
  },
  data() {
    return {
      submitLoading: false,
      showImgUpload: false,
      pageList: [],
      formData: {
        id: null,
        themeId: null,
        pageId: null,
        pageCover: null,
        pageCoverId: null
      },
      formRules: {
        pageId: [
          { required: true, message: '请选择页面', trigger: 'change' }
        ],
        pageCover: [
          { required: true, message: '请上传页面封面', trigger: 'blur' }
        ]
      }
    }
  },
  computed: {
    isMobile() {
      return this.$store.state.app.device === 'mobile'
    }
  },
  methods: {
    initData(themeId, dataId) {
      this.formData.themeId = themeId
      if (dataId != null && dataId !== '') {
        this.$nextTick(() => {
          this.getThemePageDetail(dataId)
        })
      }
      this.loadPageList()
    },
    getThemePageDetail(dataId) {
      if (dataId != null && dataId !== '') {
        this.$mapi.theme.queryThemePageDetail({ themePageId: dataId }).then(res => {
          const { data } = res
          Object.keys(this.formData).forEach(key => {
            this.formData[key] = data[key]
          })
        }).catch(_ => {
          this.doClose()
        })
      }
    },
    loadPageList() {
      this.$mapi.page.pageList().then(res => {
        const { data } = res
        this.pageList = data
      }).catch(_ => {
        this.pageList = []
      })
    },
    beforeUpload(file) {
      // 支持类型：.jpg,.jpeg,.png
      let imgType = ''
      if (file.name.indexOf('.') !== -1) {
        imgType = file.name.substring(file.name.lastIndexOf('.') + 1).toLowerCase()
      }
      if (imgType === '' || (imgType !== 'jpg' && imgType !== 'jpeg' && imgType !== 'png')) {
        this.$message.warning('上传文件只能是 jpg, jpeg, png 格式!')
        return false
      }

      return file
      // 压缩图片
      // return new Promise(resolve => {
      //   if (file.size / 1024 < this.$config.UPLOAD_SIZE) {
      //     resolve(file)
      //   }
      //
      //   imageConversion.compressAccurately(file, this.$config.UPLOAD_SIZE).then(res => {
      //     resolve(res)
      //   })
      // })
    },
    handleError(err) {
      try {
        console.log('err', err)
        this.$message.error(err.message)
      } catch {
        this.$message.error('系统发生未知错误')
      }
    },
    handleSuccess(response, file) {
      if (response.code === '000000000') {
        // success
        if (response.data && Array.isArray(response.data) && response.data.length > 0) {
          this.formData.pageCoverId = response.data[0]['fileId']
          this.formData.pageCover = response.data[0]['fileAccessUrL']
        }

        this.$message.success('上传成功')
      } else {
        // error
        this.$refs.upload.handleError(response, file)
      }
    },
    removeImg() {
      this.formData.pageCoverId = null
      this.formData.pageCover = null
    },
    submit() {
      this.$refs.addOrEditForm.validate((valid) => {
        if (valid) {
          this.submitLoading = true
          if (this.formData.id == null || this.formData.id === '') {
            // add
            this.$mapi.theme.addThemePage(this.formData).then(res => {
              this.$message.success(res.message)
              this.doClose(true)
            }).finally(_ => {
              this.submitLoading = false
            })
          } else {
            // edit
            this.$mapi.theme.editThemePage(this.formData).then(res => {
              this.$message.success(res.message)
              this.doClose(true)
            }).finally(_ => {
              this.submitLoading = false
            })
          }
        }
      })
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
    doClose(result = false) {
      this.formData = {
        id: null,
        themeId: null,
        pageId: null,
        pageCover: null,
        pageCoverId: null
      }
      this.$refs.addOrEditForm.resetFields()
      this.showImgUpload = false
      this.submitLoading = false
      this.$emit('addOrEditDone', result)
    }
  }
}
</script>

<style lang="scss" scoped>

</style>
