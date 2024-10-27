<template>
  <div style="display: inline-block;">
    <el-dialog :visible.sync="dialog" :close-on-click-modal="false" :before-close="cancel" :title="title" append-to-body width="500px" @close="cancel">
      <el-form ref="form" :model="form" :rules="rules" size="small" label-width="88px">
        <el-form-item label="新邮箱" prop="email">
          <el-input v-model="form.email" auto-complete="on" style="width: 200px;" />
          <el-button :loading="codeLoading" :disabled="isDisabled" size="small" @click="sendCode">{{ buttonName }}</el-button>
        </el-form-item>
        <el-form-item label="验证码" prop="code">
          <el-input v-model="form.code" style="width: 320px;" />
        </el-form-item>
        <el-form-item label="当前密码" prop="pass">
          <el-input v-model="form.pass" type="password" style="width: 320px;" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="text" @click="cancel">取消</el-button>
        <el-button :loading="loading" type="primary" @click="doSubmit">确认</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { validEmail } from '@/utils/validate'
export default {
  props: {
    email: {
      type: String,
      required: true
    }
  },
  data() {
    const validMail = (rule, value, callback) => {
      if (value === '' || value === null) {
        callback(new Error('新邮箱不能为空'))
      } else if (value === this.email) {
        callback(new Error('新邮箱不能与旧邮箱相同'))
      } else if (validEmail(value)) {
        callback()
      } else {
        callback(new Error('邮箱格式错误'))
      }
    }
    return {
      title: '修改邮箱',
      loading: false,
      dialog: false,
      codeLoading: false,
      buttonName: '获取验证码',
      isDisabled: false,
      time: 60,
      form: { pass: '', email: '', code: '', uuid: '' },
      rules: {
        pass: [
          { required: true, message: '当前密码不能为空', trigger: 'blur' }
        ],
        email: [
          { required: true, validator: validMail, trigger: 'blur' }
        ],
        code: [
          { required: true, message: '验证码不能为空', trigger: 'blur' }
        ]
      }
    }
  },
  methods: {
    cancel() {
      this.resetForm()
    },
    sendCode() {
      if (this.form.email && this.form.email !== this.email) {
        const _this = this
        this.codeLoading = true
        this.buttonName = '验证码发送中'
        this.$mapi.communal.sendEmailCode({ email: this.form.email }).then(res => {
          this.form.uuid = res.data
          this.$message.success('发送成功，验证码有效期5分钟')
          this.codeLoading = false
          this.isDisabled = true
          this.buttonName = this.time-- + '秒后重新发送'
          this.timer = window.setInterval(function() {
            _this.buttonName = _this.time + '秒后重新发送'
            --_this.time
            if (_this.time < 0) {
              _this.buttonName = '重新发送'
              _this.time = 60
              _this.isDisabled = false
              window.clearInterval(_this.timer)
            }
          }, 1000)
        }).catch(() => {
          this.isDisabled = false
          this.codeLoading = false
          this.buttonName = '获取验证码'
        })
      }
    },
    doSubmit() {
      this.$refs['form'].validate((valid) => {
        if (valid) {
          this.loading = true
          this.$mapi.user.updateUserEmail(this.form).then(() => {
            this.$store.dispatch('GetUserInfo').then(() => {
              this.$message.success('邮箱修改成功')
              this.resetForm()
              this.loading = false
            })
          }).catch(_ => {
            this.loading = false
          })
        }
      })
    },
    resetBtn() {
      this.buttonName = '获取验证码'
      this.isDisabled = false
      this.codeLoading = false
    },
    resetForm() {
      this.dialog = false
      this.time = 60
      this.$refs['form'].resetFields()
      window.clearInterval(this.timer)
      this.resetBtn()
      this.form = { pass: '', email: '', code: '', uuid: '' }
    }
  }
}
</script>

<style scoped>

</style>
