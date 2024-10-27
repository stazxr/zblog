<template>
  <div>
    <el-dialog append-to-body :close-on-click-modal="false" :before-close="handleClose" :visible.sync="dialogVisible" :title="dialogTitle" width="620px">
      <el-form ref="addForm" :inline="true" :model="addForm" :rules="addRules" size="small" label-width="100px">
        <el-form-item label="字典名称" prop="name">
          <el-input v-model="addForm.name" style="width: 178px" maxlength="50" show-word-limit />
        </el-form-item>
        <el-form-item label="字典排序" prop="sort">
          <el-input-number v-model.number="addForm.sort" :min="0" :max="99999" step-strictly controls-position="right" style="width: 178px" />
        </el-form-item>
        <el-form-item v-if="addForm.type === 2" label="字典KEY" prop="key">
          <el-input v-model="addForm.key" style="width: 178px" maxlength="50" show-word-limit />
        </el-form-item>
        <el-form-item v-if="addForm.type === 2" label="字典状态" prop="enabled">
          <el-select v-model="addForm.enabled" placeholder="字典状态" style="width: 178px">
            <el-option v-for="item in enabledEnums" :key="item.value" :label="item.name" :value="item.value" />
          </el-select>
        </el-form-item>
        <el-form-item v-if="addForm.type === 2" label="字典VALUE" prop="value">
          <el-input v-model="addForm.value" type="textarea" :autosize="{ minRows: 4, maxRows: 4 }" style="width: 470px" />
        </el-form-item>
        <el-form-item label="使用说明">
          <el-input v-model="addForm.desc" type="textarea" resize="none" :autosize="{ minRows: 4, maxRows: 4 }" maxlength="100" show-word-limit style="width: 470px" />
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
    dataId: {
      type: String,
      default: ''
    }
  },
  data() {
    return {
      submitLoading: false,
      enabledEnums: [
        { name: '启用', value: true },
        { name: '禁用', value: false }
      ],
      addForm: {
        id: '',
        pid: '',
        name: '',
        type: '',
        key: '',
        value: '',
        desc: '',
        sort: 99999,
        enabled: true
      },
      addRules: {
        name: [
          { required: true, message: '请输入字典名称', trigger: 'blur' }
        ],
        sort: [
          { required: true, message: '请输入字典排序', trigger: 'blur' }
        ],
        key: [
          { required: true, message: '请输入字典KEY', trigger: 'blur' }
        ],
        enabled: [
          { required: true, message: '请选择字典状态', trigger: 'blur' }
        ]
      }
    }
  },
  watch: {
    dataId(id) {
      this.addForm.id = id
    }
  },
  methods: {
    initData(type, pid) {
      this.addForm.type = type
      this.addForm.pid = pid
      this.$nextTick(() => {
        this.getDictDetail()
      })
    },
    getDictDetail() {
      if (this.addForm.id != null && this.addForm.id !== '') {
        this.$mapi.dict.queryDictDetail({ dictId: this.addForm.id }).then(res => {
          const { data } = res
          Object.keys(this.addForm).forEach(key => {
            this.addForm[key] = data[key]
          })
        }).catch(_ => {
          setTimeout(() => { this.doClose() }, 500)
        })
      }
    },
    doClose(result = false) {
      this.addForm = {
        id: '',
        pid: '',
        name: '',
        type: '',
        key: '',
        value: '',
        desc: '',
        sort: 99999,
        enabled: true
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
            this.$mapi.dict.addDict(this.addForm).then(res => {
              this.$message.success(res.message)
              this.doClose(true)
            }).finally(_ => {
              this.submitLoading = false
            })
          } else {
            // edit
            this.$mapi.dict.editDict(this.addForm).then(res => {
              this.$message.success(res.message)
              this.doClose(true)
            }).finally(_ => {
              this.submitLoading = false
            })
          }
        } else {
          console.log('error submit!!')
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
