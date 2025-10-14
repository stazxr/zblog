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
        <el-form-item label="角色名称" prop="roleName">
          <el-input v-model="formData.roleName" :style="isMobile ? '' : 'width: 380px;'" maxlength="25" show-word-limit />
        </el-form-item>
        <el-form-item label="角色编码" prop="roleCode">
          <el-input v-model="formData.roleCode" :style="isMobile ? '' : 'width: 380px;'" maxlength="25" show-word-limit />
        </el-form-item>
        <el-form-item label="角色状态" prop="enabled">
          <el-select v-model="formData.enabled" :style="isMobile ? '' : 'width: 380px;'">
            <el-option v-for="item in roleEnabled" :key="item.value" :label="item.name" :value="item.value" />
          </el-select>
        </el-form-item>
        <el-form-item label="角色描述">
          <el-input v-model="formData.roleDesc" :style="isMobile ? '' : 'width: 380px;'" type="textarea" maxlength="100" show-word-limit />
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
      roleEnabled: [
        { name: '启用', value: true },
        { name: '禁用', value: false }
      ],
      formData: {
        id: null,
        roleName: null,
        roleCode: null,
        roleDesc: null,
        enabled: true
      },
      formRules: {
        roleName: [
          { required: true, message: '请输入角色名称', trigger: 'blur' }
        ],
        roleCode: [
          { required: true, message: '请输入角色编码', trigger: 'blur' }
        ],
        enabled: [
          { required: true, message: '请选择角色状态', trigger: 'change' }
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
    initData(roleId) {
      if (roleId != null && roleId !== '') {
        this.$nextTick(() => {
          this.getRoleDetail(roleId)
        })
      }
    },
    getRoleDetail(roleId) {
      this.$mapi.role.queryRoleDetail({ roleId }).then(res => {
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
            this.$mapi.role.addRole(this.formData).then(res => {
              this.$message.success(res.message)
              this.doClose(true)
            }).finally(_ => {
              this.submitLoading = false
            })
          } else {
            // edit
            this.$mapi.role.editRole(this.formData).then(res => {
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
        roleName: null,
        roleCode: null,
        roleDesc: null,
        enabled: true
      }
      this.$refs.addOrEditForm.resetFields()
      this.submitLoading = false
      this.$emit('addOrEditDone', result)
    }
  }
}
</script>

<style scoped>

</style>
