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
          <muses-image-upload
            v-model="formData.previewCover"
            @on-success="handleUploadSuccess"
            @on-remove="handleUploadRemove"
          />
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
    handleUploadSuccess(file) {
      this.formData.previewCoverId = file.fileId
      this.formData.previewCover = file.fileAccessUrL
    },
    handleUploadRemove() {
      this.formData.previewCoverId = null
      this.formData.previewCover = null
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
        themeName: null,
        themeType: null,
        previewCover: null,
        previewCoverId: null,
        description: null
      }
      this.$refs.addOrEditForm.resetFields()
      this.submitLoading = false
      this.$emit('addOrEditDone', result)
    }
  }
}
</script>

<style rel="stylesheet/scss" lang="scss" scoped>
::v-deep .el-input-number .el-input__inner {
  text-align: left;
}
</style>
