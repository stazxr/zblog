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
      width="580px"
    >
      <el-form ref="addOrEditForm" :inline="!isMobile" :model="formData" :rules="formRules" label-width="80px">
        <el-form-item label="版本名称" prop="versionName">
          <el-input v-model="formData.versionName" maxlength="30" show-word-limit :style="isMobile ? '' : 'width: 178px;'" />
        </el-form-item>
        <el-form-item label="权限排序" prop="sort">
          <el-input-number
            v-model.number="formData.sort"
            :min="0"
            :max="99999"
            :style="isMobile ? 'width: 100%;' : 'width: 178px;'"
            step-strictly
            controls-position="right"
          />
        </el-form-item>
        <el-form-item label="修订内容" prop="updateContent">
          <el-input v-model="formData.updateContent" type="textarea" maxlength="2000" show-word-limit :rows="5" :style="isMobile ? '' : 'width: 450px;'" />
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
      formData: {
        id: null,
        versionName: null,
        updateContent: null,
        sort: 99999
      },
      formRules: {
        versionName: [
          { required: true, message: '请输入版本名称', trigger: 'blur' }
        ],
        updateContent: [
          { required: true, message: '请输入修订内容', trigger: 'blur' }
        ],
        sort: [
          { required: true, message: '请输入版本排序', trigger: 'blur' }
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
      if (dataId != null && dataId !== '') {
        this.$nextTick(() => {
          this.getVersionDetail(dataId)
        })
      }
    },
    getVersionDetail(dataId) {
      this.$mapi.version.queryVersionDetail({ versionId: dataId }).then(res => {
        const { data } = res
        Object.keys(this.formData).forEach(key => {
          this.formData[key] = data[key]
        })
      }).catch(_ => {
        this.doClose()
      })
    },
    submit() {
      this.$refs.addOrEditForm.validate((valid) => {
        if (valid) {
          this.submitLoading = true
          if (this.formData.id === null) {
            // add
            this.$mapi.version.addVersion(this.formData).then(res => {
              this.$message.success(res.message)
              this.doClose(true)
            }).finally(_ => {
              this.submitLoading = false
            })
          } else {
            // edit
            this.$mapi.version.editVersion(this.formData).then(res => {
              this.$message.success(res.message)
              this.doClose(true)
            }).finally(_ => {
              this.submitLoading = false
            })
          }
        }
      })
    },
    cancel() {
      this.handleClose()
    },
    handleClose() {
      if (!this.submitLoading) {
        this.$confirm('是否确认关闭弹窗？').then(_ => {
          this.doClose()
        }).catch(_ => {})
      }
    },
    doClose(result = false) {
      this.formData = {
        id: null,
        versionName: null,
        updateContent: null,
        sort: 99999
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
