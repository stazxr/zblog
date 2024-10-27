<template>
  <div style="display: inline-block">
    <el-dialog :visible.sync="dialog" :close-on-click-modal="false" :before-close="cancel" :title="title" append-to-body width="500px" @close="cancel">
      <el-form ref="form" :model="form" :rules="rules" size="small" label-width="88px">
        <el-form-item label="旧密码" prop="oldPass">
          <el-input v-model="form.oldPass" type="password" auto-complete="on" style="width: 370px;" />
        </el-form-item>
        <el-form-item label="新密码" prop="newPass">
          <el-input v-model="form.newPass" type="password" auto-complete="on" style="width: 370px;" />
        </el-form-item>
        <el-form-item label="确认密码" prop="confirmPass">
          <el-input v-model="form.confirmPass" type="password" auto-complete="on" style="width: 370px;" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="text" @click="cancel">取消</el-button>
        <el-button :loading="loading" type="primary" @click="doSubmit">确认</el-button>
      </div>
      <div style="margin-left: 5px;">
        <el-alert :title="passwordRemark" type="info" effect="dark" :closable="false" />
      </div>
    </el-dialog>
  </div>
</template>

<script>
export default {
  data() {
    const confirmPass = (rule, value, callback) => {
      if (value) {
        if (this.form.newPass !== value) {
          callback(new Error('两次输入的密码不一致'))
        } else {
          callback()
        }
      } else {
        callback(new Error('请再次输入密码'))
      }
    }
    return {
      title: '修改密码',
      loading: false,
      dialog: false,
      passwordRemark: '密码设置要求: 密码不能包含用户名，密码长度必须大于等于6位且小于等于20位，需要包含大写字母，小写字母，数字，特殊字符中的至少三种',
      form: { oldPass: '', newPass: '', confirmPass: '' },
      rules: {
        oldPass: [
          { required: true, message: '请输入旧密码', trigger: 'blur' }
        ],
        newPass: [
          { required: true, message: '请输入新密码', trigger: 'blur' },
          { min: 6, max: 20, message: '长度在 6 到 20 个字符', trigger: 'blur' }
        ],
        confirmPass: [
          { required: true, validator: confirmPass, trigger: 'blur' }
        ]
      }
    }
  },
  methods: {
    cancel() {
      this.resetForm()
    },
    doSubmit() {
      this.$refs['form'].validate((valid) => {
        if (valid) {
          this.loading = true
          this.$mapi.user.updateUserPass(this.form).then(() => {
            this.resetForm()
            this.$message.success('密码修改成功，请重新登录')

            // 跳转登录页面
            setTimeout(() => {
              this.$store.dispatch('Logout').then(() => {
                location.reload()
              })
            }, 1500)
          }).finally(() => {
            this.loading = false
          })
        }
      })
    },
    resetForm() {
      this.dialog = false
      this.$refs['form'].resetFields()
      this.form = { oldPass: '', newPass: '', confirmPass: '' }
    }
  }
}
</script>

<style scoped>

</style>
