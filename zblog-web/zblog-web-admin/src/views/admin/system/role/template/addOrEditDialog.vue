<template>
  <div>
    <el-dialog append-to-body :close-on-click-modal="false" :before-close="handleClose" :visible.sync="dialogVisible" :title="dialogTitle" width="520px">
      <el-form ref="addForm" :inline="true" :model="addForm" :rules="addRules" size="small" label-width="80px">
        <el-form-item label="角色名称" prop="roleName">
          <el-input v-model="addForm.roleName" style="width: 380px" maxlength="25" show-word-limit />
        </el-form-item>
        <el-form-item label="角色编码" prop="roleCode">
          <el-input v-model="addForm.roleCode" style="width: 380px" maxlength="25" show-word-limit />
        </el-form-item>
        <el-form-item label="角色状态" prop="enabled">
          <el-select v-model="addForm.enabled" placeholder="角色状态" style="width: 380px">
            <el-option v-for="item in roleEnabled" :key="item.value" :label="item.name" :value="item.value" />
          </el-select>
        </el-form-item>
        <el-form-item label="角色描述">
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
    dataId: {
      type: String,
      default: ''
    }
  },
  data() {
    return {
      submitLoading: false,
      roleEnabled: [
        { name: '启用', value: true },
        { name: '禁用', value: false }
      ],
      addForm: {
        id: '',
        roleName: '',
        roleCode: '',
        desc: '',
        enabled: true
      },
      addRules: {
        roleName: [
          { required: true, message: '请输入角色名称', trigger: 'blur' }
        ],
        roleCode: [
          { required: true, message: '请输入角色编码', trigger: 'blur' }
        ],
        enabled: [
          { required: true, message: '请选择权限状态', trigger: 'blur' }
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
    initData() {
      this.$nextTick(() => {
        this.getRoleDetail()
      })
    },
    getRoleDetail() {
      if (this.addForm.id != null && this.addForm.id !== '') {
        this.$mapi.role.queryRoleDetail({ roleId: this.addForm.id }).then(res => {
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
        roleName: '',
        roleCode: '',
        desc: '',
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
            this.$mapi.role.addRole(this.addForm).then(res => {
              this.$message.success(res.message)
              this.doClose(true)
            }).finally(_ => {
              this.submitLoading = false
            })
          } else {
            // edit
            this.$mapi.role.editRole(this.addForm).then(res => {
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
