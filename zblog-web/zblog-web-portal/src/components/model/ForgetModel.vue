<template>
  <v-dialog v-model="forgetFlag" :fullscreen="isMobile" max-width="460">
    <v-card class="login-container" style="border-radius:4px">
      <v-icon class="float-right iconfont icon-guanbi" @click="forgetFlag = false" />
      <div class="login-wrapper">
        <v-text-field v-model="email" label="邮箱号" placeholder="请输入您的邮箱号" clearable @keyup.enter="forget" />
        <div class="mt-7 send-wrapper">
          <v-text-field v-model="code" maxlength="6" label="验证码" placeholder="请输入6位验证码" @keyup.enter="forget" />
          <v-btn :disabled="flag" text small @click="sendCode">
            {{ codeMsg }}
          </v-btn>
        </div>
        <v-text-field
          v-model="password"
          class="mt-7"
          label="新密码"
          placeholder="请输入您的新密码"
          :append-icon="show ? 'iconfont icon-in_zhengyan_fill' : 'iconfont icon-in_biyan_fill'"
          :type="show ? 'text' : 'password'"
          @keyup.enter="forget"
          @click:append="show = !show"
        />
        <v-btn :disabled="btnDisabled" class="mt-7" block color="green" style="color:#fff" @click="forget">确定</v-btn>
        <div class="mt-10 login-tip">
          已有账号？<span @click="openLogin">登录</span>
        </div>
      </div>
    </v-card>
  </v-dialog>
</template>

<script>
import { encrypt } from '../../utils/rsaEncrypt'
export default {
  data: function() {
    return {
      email: '',
      code: '',
      uuid: '',
      password: '',
      flag: true,
      codeMsg: '发送',
      time: 60,
      show: false,
      btnDisabled: false
    }
  },
  computed: {
    forgetFlag: {
      set(value) {
        this.$store.state.forgetFlag = value
      },
      get() {
        return this.$store.state.forgetFlag
      }
    },
    isMobile() {
      const clientWidth = document.documentElement.clientWidth
      return clientWidth <= 960
    }
  },
  watch: {
    email(value) {
      const reg = /^[A-Za-z\d\u4e00-\u9fa5]+@[a-zA-Z\d_-]+(\.[a-zA-Z\d_-]+)+$/
      this.flag = !reg.test(value)
    }
  },
  methods: {
    openLogin() {
      this.$store.state.forgetFlag = false
      this.$store.state.loginFlag = true
    },
    sendCode() {
      if (this.email) {
        const _this = this
        _this.countDown()
        this.$mapi.other.sendEmailCode({ email: this.email }).then(res => {
          if (res.code === 200) {
            this.uuid = res.data
            _this.$toast({ type: 'success', message: '发送成功，验证码有效期5分钟' })
          } else {
            _this.$toast({ type: 'error', message: res.message })
          }
        }).catch(() => {
          _this.$toast({ type: 'error', message: '发送失败' })
        })
      }
    },
    countDown() {
      this.flag = true
      this.timer = setInterval(() => {
        this.time--
        this.codeMsg = this.time + 's'
        if (this.time <= 0) {
          clearInterval(this.timer)
          this.codeMsg = '发送'
          this.time = 60
          this.flag = false
        }
      }, 1000)
    },
    forget() {
      const emailReg = /^[A-Za-z\d\u4e00-\u9fa5]+@[a-zA-Z\d_-]+(\.[a-zA-Z\d_-]+)+$/
      if (!emailReg.test(this.email)) {
        this.$toast({ type: 'error', message: '邮箱格式不正确' })
        return false
      }

      if (this.code.trim().length !== 6) {
        this.$toast({ type: 'error', message: '请输入6位验证码' })
        return false
      }

      const forgetData = {
        email: this.email,
        password: encrypt(this.password.trim()),
        code: this.code,
        uuid: this.uuid
      }

      this.btnDisabled = true
      this.$mapi.other.updateUserPwdByEmail(forgetData).then(({ code, message }) => {
        if (code === 200) {
          this.$toast({ type: 'success', message: '修改成功' })
          this.openLogin()
        } else {
          this.$toast({ type: 'error', message: message })
        }
      }).catch(_ => {
        this.$toast({ type: 'error', message: '修改失败' })
      }).finally(_ => {
        this.btnDisabled = false
      })
    }
  }
}
</script>
