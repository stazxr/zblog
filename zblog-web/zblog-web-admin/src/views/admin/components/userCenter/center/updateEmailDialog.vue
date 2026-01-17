<template>
  <div>
    <el-dialog
      title="修改邮箱"
      :visible.sync="dialogVisible"
      :fullscreen="isMobile"
      :close-on-click-modal="false"
      :close-on-press-escape="true"
      :before-close="handleClose"
      append-to-body
      width="500px"
    >
      <el-form
        ref="updateEmailForm"
        :model="formData"
        :rules="formRules"
        label-width="80px"
      >
        <el-form-item label="新邮箱" prop="email">
          <el-input v-model="formData.email" auto-complete="off">
            <el-button slot="append" :loading="codeLoading" :disabled="isCodeDisabled" @click="sendCode">
              {{ buttonName }}
            </el-button>
          </el-input>
        </el-form-item>
        <el-form-item label="验证码" prop="code">
          <el-input v-model="formData.code" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="cancel">取 消</el-button>
        <el-button type="primary" :loading="submitLoading" @click="submit">提 交</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { validEmail } from '@/utils/validate'
import { mapGetters } from 'vuex'
export default {
  data() {
    const validMail = (rule, value, callback) => {
      if (value === '' || value === null) {
        callback(new Error('新邮箱不能为空'))
      } else if (value === this.user.email) {
        callback(new Error('新邮箱不能与旧邮箱相同'))
      } else if (validEmail(value)) {
        callback()
      } else {
        callback(new Error('邮箱格式错误'))
      }
    }
    return {
      dialogVisible: false,
      submitLoading: false,
      codeLoading: false,
      buttonName: '获取验证码',
      isCodeDisabled: false,
      codeTime: 60,
      formData: { email: '', code: '', uuid: '' },
      formRules: {
        email: [
          { required: true, validator: validMail, trigger: 'blur' }
        ],
        code: [
          { required: true, message: '验证码不能为空', trigger: 'blur' }
        ]
      }
    }
  },
  computed: {
    isMobile() {
      return this.$store.state.app.device === 'mobile'
    },
    ...mapGetters([
      'user'
    ])
  },
  methods: {
    sendCode() {
      if (this.formData.email && this.formData.email !== this.user.email) {
        const _this = this
        this.codeLoading = true
        this.buttonName = '验证码发送中'
        this.$mapi.communal.sendEmailCode({ email: this.formData.email }).then(res => {
          this.formData.uuid = res.data
          this.$message.success('发送成功，验证码有效期5分钟')
          this.codeLoading = false
          this.isCodeDisabled = true
          this.buttonName = this.codeTime-- + '秒后重新发送'
          this.timer = window.setInterval(function() {
            _this.buttonName = _this.codeTime + '秒后重新发送'
            --_this.codeTime
            if (_this.codeTime < 0) {
              _this.buttonName = '重新发送'
              _this.codeTime = 60
              _this.isCodeDisabled = false
              window.clearInterval(_this.timer)
            }
          }, 1000)
        }).catch(() => {
          this.codeTime = 60
          this.buttonName = '获取验证码'
          this.isCodeDisabled = false
          this.codeLoading = false
        })
      }
    },
    submit() {
      this.$refs.updateEmailForm.validate((valid) => {
        if (valid) {
          this.submitLoading = true
          this.$mapi.userCenter.updateUserEmail(this.formData).then(() => {
            this.$message.success('邮箱修改成功')
            this.$store.dispatch('RefreshUser').then(() => {
              this.doClose()
            })
          }).finally(_ => {
            this.submitLoading = false
          })
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
    doClose() {
      this.codeTime = 60
      this.buttonName = '获取验证码'
      this.isCodeDisabled = false
      this.codeLoading = false

      this.dialogVisible = false
      this.submitLoading = false
      this.$refs.updateEmailForm.resetFields()
      this.formData = { email: '', code: '', uuid: '' }
      window.clearInterval(this.timer)
    }
  }
}
</script>

<style scoped>

</style>
