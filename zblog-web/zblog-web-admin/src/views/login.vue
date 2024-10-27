<template>
  <div style="height: 100%">
    <div v-if="pageLoading" class="oauth-background">
      <div id="preloader_1">
        <span />
        <span />
        <span />
        <span />
        <span />
      </div>
    </div>
    <div v-else class="login" :style="'background-image:url('+ Background +');'">
      <el-form ref="loginForm" :model="loginForm" :rules="loginRules" label-position="left" label-width="0px" class="login-form">
        <h3 class="title">
          Z-BLOG 后台管理系统
        </h3>
        <el-form-item prop="username">
          <el-input id="username" ref="username" v-model="loginForm.username" type="text" auto-complete="off" placeholder="请输入用户名">
            <svg-icon slot="prefix" icon-class="username" class="el-input__icon input-icon" />
          </el-input>
        </el-form-item>
        <el-form-item prop="password">
          <el-input id="password" ref="password" v-model="loginForm.password" :type="pwdFlagType" auto-complete="off" placeholder="请输入密码" @keyup.enter.native="handleLogin">
            <svg-icon slot="prefix" icon-class="password" class="el-input__icon input-icon" />
            <svg-icon v-show="loginForm.password !== ''" slot="suffix" :icon-class="pwdFlag ? 'eye-close' : 'eye'" class="el-input__icon input-icon" style="margin-right: 5px;" @click="getPwdFlag()" />
          </el-input>
        </el-form-item>
        <el-form-item prop="code">
          <el-input id="code" ref="code" v-model="loginForm.code" auto-complete="off" placeholder="请输入验证码" style="width: 63%" @keyup.enter.native="handleLogin">
            <svg-icon slot="prefix" icon-class="auth-code" class="el-input__icon input-icon" />
          </el-input>
          <div class="login-code">
            <img :src="codeUrl" alt="" @click="getCode">
          </div>
        </el-form-item>
        <!-- <el-checkbox v-model="loginForm.rememberMe" style="margin:0 0 25px 0;">
          记住我
        </el-checkbox> -->
        <el-form-item style="width:100%;">
          <el-button ref="loginBtn" :loading="loading" size="medium" type="primary" style="width:100%;" @click.native.prevent="handleLogin">
            <span v-if="!loading">登 录</span>
            <span v-else>登 录 中...</span>
          </el-button>
        </el-form-item>
      </el-form>
      <!--  底部  -->
      <div v-if="$store.state.settings.showFooter" id="el-login-footer">
        <span v-html="$store.state.settings.footerTxt" />
        <span> ⋅ </span>
        <a href="https://beian.miit.gov.cn/#/Integrated/index" target="_blank">{{ $store.state.settings.caseNumber }}</a>
      </div>
    </div>
  </div>
</template>

<script>
import qs from 'qs'
import Background from '@/assets/images/background.jpg'
import { encrypt } from '@/utils/rsaEncrypt'
import { setToken } from '@/utils/token'
export default {
  name: 'Login',
  data() {
    return {
      pageLoading: true,
      Background: Background,
      codeUrl: '',
      loginForm: {
        username: '',
        password: '',
        rememberMe: false,
        code: '',
        uuid: '',
        loginType: '1'
      },
      loginRules: {
        username: [{ required: true, trigger: 'blur', message: '用户名不能为空' }],
        password: [{ required: true, trigger: 'blur', message: '密码不能为空' }],
        code: [{ required: true, trigger: 'change', message: '验证码不能为空' }]
      },
      loading: false,
      redirect: undefined,
      // password input type
      pwdFlag: false,
      pwdFlagType: 'password'
    }
  },
  watch: {
    $route: {
      handler: function(route) {
        const data = route.query
        if (data && data.redirect) {
          this.redirect = data.redirect === '/' ? '/' : data.redirect
          delete data.redirect
          if (JSON.stringify(data) !== '{}') {
            this.redirect = this.redirect + '&' + qs.stringify(data, { indices: false })
          }
        } else {
          this.redirect = '/'
        }
      },
      immediate: true
    }
  },
  // 在模板渲染成html前调用，即通常初始化某些属性值，然后再渲染成视图
  created() {
    // 判断用户是否登录
    this.checkUserLoginStatus()

    // 获取验证码
    this.getCode()

    // 登录过期提醒
    this.point()

    // 单点登录
    const data = qs.parse(window.location.search.replace('?', ''))
    if (data['isIframe'] && data.origin) {
      console.log('第一次交互成功：后台连接上了前台，并准备向前台发送一个消息', data)
      window.parent.postMessage('ok', data.origin)
      window.addEventListener('message', function(event) {
        console.log('后台收到前台的消息', event)
        if (event.origin === data.origin) {
          if (event.data != null) {
            console.log('单点登录', event.data)
            setToken(event.data)
          } else {
            console.log('第三次交互成功：后台收到了前台的回应')
          }
        }
      }, false)
    }
  },
  // 在模板渲染成html后调用，通常是初始化页面完成后，再对html的dom节点进行一些需要的操作
  mounted() {
    // 页面加载自动聚焦
    if (!this.pageLoading) {
      if (this.loginForm.username === '') {
        this.$refs.username.focus()
      } else if (this.loginForm.password === '') {
        this.$refs.password.focus()
      } else if (this.loginForm.code === '') {
        this.$refs.code.focus()
      } else {
        this.$refs.loginBtn.focus()
      }
    }
  },
  methods: {
    getPwdFlag() {
      this.pwdFlag = !this.pwdFlag
      this.pwdFlagType = this.pwdFlag ? 'text' : 'password'
    },
    getCode() {
      this.$mapi.communal.loginCode({ _t: new Date().getTime() }).then(res => {
        this.codeUrl = res.data.img
        this.loginForm.code = ''
        this.loginForm.uuid = res.data.uuid
      })
    },
    checkUserLoginStatus() {
      this.pageLoading = true
      console.log('开始检查用户的登录状态...')
      this.$mapi.communal.checkUserLoginStatus().then(res => {
        if (res.code === 200 && res.data != null) {
          // 查询用户信息
          console.log('用户已登录，加载用户信息...')
          const userToken = res.data['accessToken']
          setToken(userToken)
          this.$router.push({ path: this.redirect || '/' })
        } else {
          console.log('用户未登录...')
          this.pageLoading = false
        }
      }).catch(_ => {
        this.pageLoading = false
      })
    },
    handleLogin() {
      this.$refs.loginForm.validate(valid => {
        if (valid) {
          const username = this.loginForm.username
          const loginParam = {
            username: username,
            password: encrypt(this.loginForm.password),
            rememberMe: this.loginForm.rememberMe,
            code: this.loginForm.code,
            uuid: this.loginForm.uuid
          }

          this.loading = true
          this.$store.dispatch('Login', loginParam).then(change_pwd => {
            if (change_pwd) {
              this.$message.error('需要重置您的密码，请修改密码')
              this.$router.push({ path: '/forceUpdatePass', query: { username: username }})
            } else {
              this.$message.success('登录成功')
              this.$router.push({ path: this.redirect || '/' })
            }
          }).catch(e => {
            console.log('Login Error', e)
            if (e.message && e.message === '10009') {
              // 跳转修改密码界面
              this.$router.push({ path: '/forceUpdatePass', query: { username: username }})
            } else {
              this.getCode()
              this.$refs.code.focus()
            }
          }).finally(_ => {
            this.loading = false
          })
        }
      })
    },
    point() {
      const point = window.sessionStorage.getItem('point')
      if (point !== null && point !== undefined) {
        this.$message.warning('当前登录状态已过期，请重新登录')
        window.sessionStorage.removeItem('point')
      }
    }
  }
}
</script>

<style scoped>
.oauth-background {
  position: fixed;
  top: 0;
  bottom: 0;
  left: 0;
  right: 0;
  background: #fff;
  z-index: 1000;
}
#preloader_1 {
  position: relative;
  top: 45vh;
  left: 45vw;
}
#preloader_1 span {
  display: block;
  bottom: 0;
  width: 9px;
  height: 5px;
  background: #9b59b6;
  position: absolute;
  animation: preloader_1 1.5s infinite ease-in-out;
}
#preloader_1 span:nth-child(2) {
  left: 11px;
  animation-delay: 0.2s;
}
#preloader_1 span:nth-child(3) {
  left: 22px;
  animation-delay: 0.4s;
}
#preloader_1 span:nth-child(4) {
  left: 33px;
  animation-delay: 0.6s;
}
#preloader_1 span:nth-child(5) {
  left: 44px;
  animation-delay: 0.8s;
}
@keyframes preloader_1 {
  0% {
    height: 5px;
    transform: translateY(0px);
    background: #9b59b6;
  }
  25% {
    height: 30px;
    transform: translateY(15px);
    background: #3498db;
  }
  50% {
    height: 5px;
    transform: translateY(0px);
    background: #9b59b6;
  }
  100% {
    height: 5px;
    transform: translateY(0px);
    background: #9b59b6;
  }
}
</style>

<style rel="stylesheet/scss" lang="scss" scoped>
  .login {
    display: flex;
    justify-content: center;
    align-items: center;
    height: 100%;
    background-size: cover;
  }
  .title {
    margin: 0 auto 30px auto;
    text-align: center;
    color: #707070;
  }

  .login-form {
    border-radius: 6px;
    width: 385px;
    padding: 25px 25px 5px 25px;
    background-color: rgba(255, 255, 255, 0.9);
    opacity: 0.9;
    .el-input {
      height: 38px;
      input {
        height: 38px;
      }
    }
    .input-icon {
      height: 39px;width: 14px;margin-left: 2px;
    }
  }
  .login-tip {
    font-size: 13px;
    text-align: center;
    color: #bfbfbf;
  }
  .login-code {
    width: 33%;
    display: inline-block;
    height: 38px;
    float: right;
    img {
      cursor: pointer;
      vertical-align:middle
    }
  }
</style>
