<template>
  <div>
    <el-dialog append-to-body :close-on-click-modal="false" :before-close="handleClose" :visible.sync="dialogVisible" :title="dialogTitle" width="540px">
      <el-form ref="addForm" :inline="true" :model="addForm" :rules="addRules" size="small" label-width="80px">
        <el-form-item label="标签名称" prop="name">
          <el-input v-model="addForm.name" style="width: 158px" maxlength="50" show-word-limit />
        </el-form-item>
        <el-form-item label="标签状态" prop="enabled">
          <el-select v-model="addForm.enabled" placeholder="标签状态" style="width: 158px">
            <el-option v-for="item in enabledEnums" :key="item.value" :label="item.name" :value="item.value" />
          </el-select>
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
      enabledEnums: [
        { name: '启用', value: true },
        { name: '禁用', value: false }
      ],
      addForm: {
        id: '',
        name: '',
        enabled: true
      },
      addRules: {
        name: [
          { required: true, message: '请输入标签名称', trigger: 'blur' }
        ],
        enabled: [
          { required: true, message: '请选择标签状态', trigger: 'blur' }
        ]
      }
    }
  },
  methods: {
    initData(dataId) {
      this.$nextTick(() => {
        this.getTagDetail(dataId)
      })
    },
    getTagDetail(dataId) {
      if (dataId != null && dataId !== '') {
        this.$mapi.tag.queryTagDetail({ tagId: dataId }).then(res => {
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
        name: '',
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
            this.$mapi.tag.addTag(this.addForm).then(res => {
              this.$message.success(res.message)
              this.doClose(true)
            }).finally(_ => {
              this.submitLoading = false
            })
          } else {
            // edit
            this.$mapi.tag.editTag(this.addForm).then(res => {
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
