<template>
  <div class="app-container">
    <div class="components-container">
      <div class="password-wrapper">
        <el-card class="password-card" shadow="never">
          <!-- 标题 -->
          <div class="password-header">
            <span class="title">{{ title }}</span>
          </div>

          <!-- 表单 -->
          <el-form
            ref="updatePassForm"
            :model="formData"
            :rules="formRules"
            label-width="80px"
            size="medium"
            class="password-form"
          >
            <el-form-item label="原密码" prop="oldPass">
              <el-input v-model="formData.oldPass" type="password" show-password placeholder="请输入原密码" />
            </el-form-item>
            <el-form-item label="新密码" prop="newPass">
              <el-input v-model="formData.newPass" type="password" show-password maxlength="20" placeholder="请输入新密码" />
              <!-- 密码强度 -->
              <div v-if="formData.newPass" class="password-strength">
                <span class="strength-label">强度</span>
                <div class="strength-bar">
                  <div class="strength-bar-inner" :class="strengthClass" :style="{ width: strengthPercent + '%' }" />
                </div>
                <span :class="['strength-text', strengthClass]"> {{ strengthText }} </span>
              </div>
            </el-form-item>
            <el-form-item label="确认密码" prop="confirmPass">
              <el-input v-model="formData.confirmPass" type="password" show-password maxlength="20" placeholder="请再次输入新密码" />
            </el-form-item>
          </el-form>
          <!-- 说明 -->
          <div class="password-desc"> {{ passwordRemark }} </div>
          <!-- 底部按钮 -->
          <div class="password-footer">
            <el-button @click="cancel">取 消</el-button>
            <el-button type="primary" :loading="submitLoading" @click="submit">完 成</el-button>
          </div>
        </el-card>
      </div>
    </div>
  </div>
</template>

<script>
import { encrypt } from '@/utils/rsaEncrypt'
export default {
  data() {
    const confirmPass = (rule, value, callback) => {
      if (value) {
        if (this.formData.newPass !== value) {
          callback(new Error('两次输入的新密码不一致'))
        }
        callback()
      } else {
        callback(new Error('请再次输入新密码'))
      }
    }
    const validatePassword = (rule, value, callback) => {
      if (value) {
        if (value.length < 6 || value.length > 20) {
          callback(new Error('密码长度必须为 6-20 位'))
        }
        if (value.includes(this.formData.username)) {
          callback(new Error('密码不能包含用户名'))
        }
        let typeCount = 0
        if (/\d/.test(value)) typeCount++
        if (/[a-z]/.test(value)) typeCount++
        if (/[A-Z]/.test(value)) typeCount++
        if (/[^\da-zA-Z]/.test(value)) typeCount++
        if (typeCount < 2) {
          callback(new Error('需要包含大写字母/小写字母/数字/特殊字符中的至少两种'))
        }
        callback()
      } else {
        callback(new Error('请输入新密码'))
      }
    }
    return {
      type: null,
      submitLoading: false,
      passwordRemark: '密码设置要求: 密码不能包含用户名，密码长度必须大于等于6位且小于等于20位，需要包含大写字母，小写字母，数字，特殊字符中的至少两种',
      formData: { username: '', oldPass: '', newPass: '', confirmPass: '' },
      formRules: {
        oldPass: [
          { required: true, message: '请输入原密码', trigger: 'blur' }
        ],
        newPass: [
          { required: true, validator: validatePassword, trigger: 'change' }
        ],
        confirmPass: [
          { required: true, validator: confirmPass, trigger: 'blur' }
        ]
      }
    }
  },
  computed: {
    title() {
      if (this.type === '1') {
        return '重置密码'
      } else if (this.type === '2') {
        return '密码已过期'
      } else {
        return '设置密码'
      }
    },
    strengthScore() {
      const pwd = this.formData.newPass || ''
      let score = 0
      if (pwd.length >= 6 && pwd.length <= 20 && !pwd.includes(this.formData.username)) {
        score++
        if (/\d/.test(pwd)) score++
        if (/[a-z]/.test(pwd)) score++
        if (/[A-Z]/.test(pwd)) score++
        if (/[^\da-zA-Z]/.test(pwd)) score++
      }
      return score
    },
    strengthText() {
      if (this.strengthScore <= 2) return '弱'
      if (this.strengthScore <= 4) return '中'
      return '强'
    },
    strengthClass() {
      if (this.strengthScore <= 2) return 'weak'
      if (this.strengthScore <= 4) return 'medium'
      return 'strong'
    },
    strengthPercent() {
      if (this.strengthScore <= 2) return 33
      if (this.strengthScore <= 4) return 66
      return 100
    }
  },
  created() {
    const data = sessionStorage.getItem('force_update_pwd_user')
    if (data) {
      const { username, type } = JSON.parse(data)
      this.formData.username = username
      this.type = type
    } else {
      // 防止非法访问
      this.$router.replace('/login')
    }
  },
  destroyed() {
    sessionStorage.removeItem('force_update_pwd_user')
  },
  methods: {
    cancel() {
      this.resetForm()
      this.$router.push('/login')
    },
    resetForm() {
      this.type = null
      this.$refs.updatePassForm.resetFields()
      this.formData = { username: null, oldPass: '', newPass: '', confirmPass: '' }
    },
    submit() {
      this.$refs.updatePassForm.validate((valid) => {
        if (valid) {
          this.submitLoading = true
          const payload = encrypt(JSON.stringify({
            username: this.formData.username,
            oldPass: this.formData.oldPass,
            newPass: this.formData.newPass,
            confirmPass: this.formData.confirmPass
          }))
          this.$mapi.userCenter.forceUpdateUserPass({ _f: payload }).then(() => {
            // 跳转登录页面
            this.resetForm()
            this.$message.success('密码修改成功，请重新登录')
            setTimeout(() => {
              this.$router.replace('/login')
            }, 1000)
          }).finally(() => {
            this.submitLoading = false
          })
        }
      })
    }
  }
}
</script>

<style scoped>
.password-wrapper {
  display: flex;
  justify-content: center;
  margin-top: 40px;
}
.password-card {
  width: 100%;
  max-width: 480px;
  border-radius: 6px;
}
/* 标题 */
.password-header {
  text-align: center;
  font-size: 18px;
  font-weight: 600;
  padding-bottom: 20px;
}
/* 表单 */
.password-form {
  margin-top: 10px;
}
/* 说明 */
.password-desc {
  font-size: 13px;
  color: #909399;
  margin: 16px 0;
}
/* 底部按钮 */
.password-footer {
  display: flex;
  justify-content: flex-end;
  gap: 12px;
  margin-top: 24px;
}
/* 密码强度校验 */
.password-strength {
  display: flex;
  align-items: center;
  margin-top: 6px;
  font-size: 12px;
}
.strength-label {
  color: #909399;
  margin-right: 6px;
}
.strength-bar {
  flex: 1;
  height: 6px;
  background: #ebeef5;
  border-radius: 3px;
  margin: 0 6px;
  overflow: hidden;
}
.strength-bar-inner {
  height: 100%;
  transition: width 0.25s ease;
}
.strength-bar-inner.weak {
  background-color: #f56c6c;
}
.strength-bar-inner.medium {
  background-color: #e6a23c;
}
.strength-bar-inner.strong {
  background-color: #67c23a;
}
.strength-text {
  min-width: 24px;
  font-weight: 500;
}
.strength-text.weak {
  color: #f56c6c;
}
.strength-text.medium {
  color: #e6a23c;
}
.strength-text.strong {
  color: #67c23a;
}
/* 手机端 */
@media (max-width: 768px) {
  .password-wrapper {
    margin-top: 16px;
  }
  .password-footer {
    flex-direction: column;
  }
  .password-footer .el-button {
    width: 100%;
    margin-left: 0;
  }
}
</style>
