<template>
  <div>
    <el-dialog append-to-body :close-on-click-modal="false" :before-close="handleClose" :visible.sync="dialogVisible" :title="dialogTitle" width="520px">
      <el-form ref="addForm" :inline="true" :model="addForm" :rules="addRules" size="small" label-width="80px">
        <el-form-item label="接口名称" prop="name">
          <el-input v-model="addForm.name" style="width: 380px" maxlength="50" show-word-limit />
        </el-form-item>
        <el-form-item label="接口地址" prop="value">
          <el-input v-model="addForm.value" style="width: 380px" maxlength="200" show-word-limit />
        </el-form-item>
        <el-form-item label="接口排序" prop="sort">
          <el-input-number v-model.number="addForm.sort" :min="0" :max="99999" step-strictly controls-position="right" style="width: 380px" />
        </el-form-item>
        <el-form-item label="接口描述">
          <el-input v-model="addForm.desc" type="textarea" maxlength="100" show-word-limit style="width: 380px" />
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
    },
    data: {
      type: Object,
      default: null
    },
    dictKey: {
      type: String,
      default: ''
    }
  },
  data() {
    return {
      submitLoading: false,
      addForm: {
        id: '',
        name: '',
        key: '',
        value: '',
        desc: '',
        sort: '99999'
      },
      addRules: {
        name: [
          { required: true, message: '请输入接口名称', trigger: 'blur' }
        ],
        value: [
          { required: true, message: '请输入接口地址', trigger: 'blur' }
        ],
        sort: [
          { required: true, message: '请输入接口排序', trigger: 'blur' }
        ]
      }
    }
  },
  watch: {
    data(val) {
      if (val != null) {
        this.addForm.id = val['id']
        this.addForm.name = val['name']
        this.addForm.value = val['value']
        this.addForm.desc = val['desc']
        this.addForm.sort = val['sort']
      }
    }
  },
  methods: {
    doClose(result = false) {
      this.addForm = {
        id: '',
        name: '',
        key: '',
        value: '',
        desc: '',
        sort: '99999'
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
          this.addForm.key = this.dictKey
          if (this.addForm.id == null || this.addForm.id === '') {
            // add
            this.$mapi.router.addBlackOrWhiteRouter(this.addForm).then(res => {
              this.$message.success(res.message)
              this.doClose(true)
            }).finally(_ => {
              this.submitLoading = false
            })
          } else {
            // edit
            this.$mapi.router.editBlackOrWhiteRouter(this.addForm).then(res => {
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
::v-deep .el-input-number .el-input__inner {
  text-align: left;
}
</style>
