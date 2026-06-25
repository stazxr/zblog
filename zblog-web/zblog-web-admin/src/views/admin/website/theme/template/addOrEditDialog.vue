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
      width="530px"
    >
      <el-form ref="addOrEditForm" :inline="!isMobile" :model="formData" :rules="formRules" label-width="100px">
        <el-form-item label="主题名称" prop="themeName">
          <el-input v-model="formData.themeName" :style="isMobile ? '' : 'width: 360px;'" maxlength="50" show-word-limit />
        </el-form-item>
        <el-form-item label="主题类型" prop="themeType">
          <el-select v-model="formData.themeType" placeholder="主题类型" :disabled="formData.id != null" :style="isMobile ? '' : 'width: 360px;'">
            <el-option v-for="item in themeTypeEnums" :key="item.value" :label="item.name" :value="item.value" />
          </el-select>
        </el-form-item>
        <el-form-item label="主题描述" prop="description">
          <el-input
            v-model="formData.description"
            type="textarea"
            rows="4"
            maxlength="200"
            show-word-limit
            :style="isMobile ? '' : 'width: 360px;'"
          />
        </el-form-item>
        <el-form-item label="主题预览图" prop="previewCover">
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
            <i v-if="formData.previewCover === null" class="el-icon-upload" />
            <div v-if="formData.previewCover === null" class="el-upload__text">
              将文件拖到此处，或<em>点击上传</em>
            </div>
            <img v-else :src="formData.previewCover" width="360px" height="180px" alt="">
          </el-upload>
          <el-button v-if="formData.previewCover !== null" type="danger" @click="removeImg">清除图片</el-button>
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
      themeTypeEnums: [
        { name: '移动端', value: 'MOBILE' },
        { name: 'PC端', value: 'PC' }
      ],
      showImgUpload: false,
      langExt: {
        success: '上传成功!',
        fail: '上传失败!'
      },
      formData: {
        id: null,
        themeName: null,
        themeType: null,
        previewCover: null,
        previewCoverId: null,
        description: null
      },
      formRules: {
        themeName: [
          { required: true, message: '请输入主题名称', trigger: 'blur' }
        ],
        themeType: [
          { required: true, message: '请选择主题类型', trigger: 'change' }
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
    initData(dataId) {
      this.$nextTick(() => {
        if (dataId != null && dataId !== '') {
          this.queryDetail(dataId)
        }
      })
    },
    queryDetail(dataId) {
      this.$mapi.theme.queryThemeDetail({ themeId: dataId }).then(res => {
        const { data } = res
        Object.keys(this.formData).forEach(key => {
          this.formData[key] = data[key]
        })
      }).catch(_ => {
        setTimeout(() => { this.doClose() }, 500)
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
          this.formData.previewCoverId = response.data[0]['fileId']
          this.formData.previewCover = response.data[0]['fileAccessUrL']
        }

        this.$message.success('上传成功')
      } else {
        // error
        this.$refs.upload.handleError(response, file)
      }
    },
    removeImg() {
      this.formData.previewCoverId = null
      this.formData.previewCover = null
    },
    doClose(result = false) {
      this.formData = {
        id: null,
        themeName: null,
        themeType: null,
        previewCover: null,
        previewCoverId: null,
        description: null
      }
      this.$refs.addOrEditForm.resetFields()
      this.showImgUpload = false
      this.submitLoading = false
      this.$emit('addOrEditDone', result)
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
      this.$refs.addOrEditForm.validate((valid) => {
        if (valid) {
          this.submitLoading = true
          if (this.formData.id == null || this.formData.id === '') {
            // add
            this.$mapi.theme.addTheme(this.formData).then(res => {
              this.$message.success(res.message)
              this.doClose(true)
            }).finally(_ => {
              this.submitLoading = false
            })
          } else {
            // edit
            this.$mapi.theme.editTheme(this.formData).then(res => {
              this.$message.success(res.message)
              this.doClose(true)
            }).finally(_ => {
              this.submitLoading = false
            })
          }
        }
      })
    }
  }
}
</script>

<style rel="stylesheet/scss" lang="scss" scoped>
::v-deep .el-input-number .el-input__inner {
  text-align: left;
}
</style>
