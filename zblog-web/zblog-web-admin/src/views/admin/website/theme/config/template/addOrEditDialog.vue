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
          <muses-image-upload
            v-model="formData.pageCover"
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
    handleUploadSuccess(file) {
      this.formData.pageCoverId = file.fileId
      this.formData.pageCover = file.fileAccessUrL
    },
    handleUploadRemove() {
      this.formData.pageCoverId = null
      this.formData.pageCover = null
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
      this.submitLoading = false
      this.$emit('addOrEditDone', result)
    }
  }
}
</script>

<style lang="scss" scoped>

</style>
