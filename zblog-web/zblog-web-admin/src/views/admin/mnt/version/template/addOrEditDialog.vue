<template>
  <div>
    <el-dialog append-to-body :close-on-click-modal="false" :before-close="handleClose" :visible.sync="dialogVisible" :title="dialogTitle" width="520px">
      <el-form ref="addForm" :inline="true" :model="addForm" :rules="addRules" size="small" label-width="80px">
        <el-form-item label="版本名称" prop="versionName">
          <el-input v-model="addForm.versionName" maxlength="30" show-word-limit style="width: 380px" />
        </el-form-item>
        <el-form-item label="版本描述" prop="updateContent">
          <el-input v-model="addForm.updateContent" type="textarea" maxlength="1000" show-word-limit style="width: 380px" />
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
      addForm: {
        id: '',
        versionName: '',
        updateContent: ''
      },
      addRules: {
        versionName: [
          { required: true, message: '请输入版本名称', trigger: 'blur' }
        ]
      },
      submitLoading: false
    }
  },
  methods: {
    initData(dataId) {
      this.addForm.id = dataId
      this.getVersionInfo()
    },
    getVersionInfo() {
      if (this.addForm.id != null && this.addForm.id !== '') {
        this.$mapi.version.queryVersionInfo({ versionId: this.addForm.id }).then(res => {
          const { data } = res
          Object.keys(this.addForm).forEach(key => {
            this.addForm[key] = data[key]
          })
        }).catch(_ => {
          this.doClose()
        })
      }
    },
    doClose(result = false) {
      this.addForm = {
        id: '',
        versionName: '',
        updateContent: ''
      }
      this.$refs['addForm'].resetFields()
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
      this.$refs['addForm'].validate((valid) => {
        if (valid) {
          this.submitLoading = true
          if (this.addForm.id == null || this.addForm.id === '') {
            // add
            this.$mapi.version.addVersion(this.addForm).then(res => {
              this.$message.success(res.message)
              this.doClose(true)
            }).finally(_ => {
              this.submitLoading = false
            })
          } else {
            // edit
            this.$mapi.version.editVersion(this.addForm).then(res => {
              this.$message.success(res.message)
              this.doClose(true)
            }).finally(_ => {
              this.submitLoading = false
            })
          }
        } else {
          return false
        }
      })
    }
  }
}
</script>

<style rel="stylesheet/scss" lang="scss" scoped>

</style>
