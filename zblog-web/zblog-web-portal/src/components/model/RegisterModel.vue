<template>
  <v-dialog v-model="registerFlag" :fullscreen="isMobile" max-width="460">
    <v-card class="register-container" style="border-radius:4px">
      <v-icon class="float-right iconfont icon-guanbi" @click="registerFlag = false" />
      <div class="login-wrapper">
        <!-- 用户名 -->
        <v-text-field v-model="username" label="用户名" placeholder="请输入您的用户名，用于登录" clearable @keyup.enter="register" />
        <!-- 邮箱 -->
        <v-text-field v-model="email" label="邮箱号" placeholder="请输入您的邮箱号，用于登录、接收通知" clearable @keyup.enter="register" />
        <!-- 验证码 -->
        <div class="mt-7 send-wrapper">
          <v-text-field v-model="code" maxlength="6" label="验证码" placeholder="请输入6位验证码" @keyup.enter="register" />
          <v-btn text small :disabled="flag" @click="sendCode">
            {{ codeMsg }}
          </v-btn>
        </div>
        <!-- 密码 -->
        <v-text-field
          v-model="password"
          class="mt-7"
          label="密码"
          placeholder="请输入您的密码"
          :append-icon="show ? 'iconfont icon-in_zhengyan_fill' : 'iconfont icon-in_biyan_fill'"
          :type="show ? 'text' : 'password'"
          @keyup.enter="register"
          @click:append="show = !show"
        />
        <!-- 注册按钮 -->
        <v-btn :disabled="btnDisabled || username === ''" class="mt-7" block color="red" style="color:#fff" @click="register">注册</v-btn>
        <!-- 登录 -->
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
      username: '',
      password: '',
      email: '',
      uuid: '',
      code: '',
      flag: true,
      codeMsg: '发送',
      time: 60,
      show: false,
      btnDisabled: false
    }
  },
  computed: {
    registerFlag: {
      set(value) {
        this.$store.state.registerFlag = value
      },
      get() {
        return this.$store.state.registerFlag
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
      this.$store.state.registerFlag = false
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
    register() {
      const usernameReg = /^[\dA-Za-z]{4,20}$/
      if (!usernameReg.test(this.username)) {
        this.$toast({ type: 'error', message: '用户名只允许包含数字和字母, 且长度范围要求为[4, 20]' })
        return false
      }

      const emailReg = /^[A-Za-z\d\u4e00-\u9fa5]+@[a-zA-Z\d_-]+(\.[a-zA-Z\d_-]+)+$/
      if (!emailReg.test(this.email)) {
        this.$toast({ type: 'error', message: '邮箱格式不正确' })
        return false
      }

      if (this.code.trim().length !== 6) {
        this.$toast({ type: 'error', message: '请输入6位验证码' })
        return false
      }

      const registerData = {
        username: this.username,
        password: this.password,
        email: this.email,
        code: this.code,
        uuid: this.uuid
      }

      this.btnDisabled = true
      this.$mapi.other.userRegister(registerData).then(({ code, message }) => {
        if (code === 200) {
          // 注册成功，自动跳转登录
          this.username = ''
          this.password = ''
          this.email = ''
          this.code = ''
          this.uuid = ''
          this.$toast({ type: 'success', message: '注册成功，即将自动登录...' })
          const param = {
            username: this.username,
            password: encrypt(this.password.trim())
          }
          this.$mapi.portal.webLogin(param).then(({ code, data, message }) => {
            if (code === 200) {
              this.$store.commit('login', data)
              this.$store.commit('closeModel')
              this.$toast({ type: 'success', message: '自动登录成功' })
            } else {
              this.$toast({ type: 'error', message: message })
            }
          }).catch(_ => {
            this.$toast({ type: 'error', message: '自动登录失败，请尝试手动登录' })
          })
        } else {
          this.$toast({ type: 'error', message: message })
        }
      }).catch(_ => {
        this.$toast({ type: 'error', message: '注册失败' })
      }).finally(_ => {
        this.btnDisabled = false
      })
    }
  }
}
</script>
