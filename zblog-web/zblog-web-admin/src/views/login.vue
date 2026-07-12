<template>
  <div style="height: 100%">
    <div v-if="pageLoading" class="oauth-background">
      <div id="preloader_1">
        <span /><span /><span /><span /><span />
      </div>
    </div>

    <!-- 背景容器（新增） -->
    <div v-else class="login-wrapper">
      <!-- 背景图 -->
      <div class="bg-image" :style="'background-image:url('+ background +');'" />

      <!-- 渐变遮罩（新增） -->
      <div class="bg-overlay" />

      <!-- 登录 -->
      <div class="login">
        <el-form
          ref="loginForm"
          :model="loginForm"
          :rules="loginFormRules"
          label-position="left"
          label-width="0px"
          class="login-form"
        >
          <h3 class="title">{{ $store.state.settings.title }}</h3>

          <el-form-item prop="username">
            <el-input ref="username" v-model="loginForm.username" placeholder="请输入用户名">
              <svg-icon slot="prefix" icon-class="icon-username" />
            </el-input>
          </el-form-item>

          <el-form-item prop="password">
            <el-input ref="password" v-model="loginForm.password" :type="pwdFlagType" placeholder="请输入密码">
              <svg-icon slot="prefix" icon-class="icon-password" />
              <svg-icon v-show="loginForm.password" slot="suffix" :icon-class="pwdFlag ? 'eye-close' : 'eye'" @click="getPwdFlag" />
            </el-input>
          </el-form-item>

          <el-form-item prop="code">
            <div class="code-wrapper">
              <el-input ref="code" v-model="loginForm.code" placeholder="请输入验证码" @keyup.enter.native="handleLogin">
                <svg-icon slot="prefix" icon-class="icon-authCode" />
              </el-input>
              <img :src="codeUrl" class="code-img" alt="" @click="getCode">
            </div>
          </el-form-item>

          <el-form-item>
            <el-button :loading="loginLoading" type="primary" class="login-btn" @click="handleLogin">
              {{ loginLoading ? '登录中...' : '登录' }}
            </el-button>
          </el-form-item>
        </el-form>

        <!-- footer -->
        <div v-if="$store.state.settings.showFooter" id="el-login-footer">
          <span v-html="$store.state.settings.footerTxt" />
          <span> ⋅ </span>
          <a href="https://beian.miit.gov.cn/#/Integrated/index" target="_blank">
            {{ $store.state.settings.caseNumber }}
          </a>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import qs from 'qs'
import backgroundWebImg from '@/assets/images/login/bg-web.webp'
import backgroundMobileImg from '@/assets/images/login/bg-mobile.webp'
import { encrypt } from '@/utils/rsaEncrypt'
export default {
  name: 'Login',
  data() {
    return {
      pageLoading: false, // 页面加载状态
      backgroundWebImg: backgroundWebImg,
      backgroundMobileImg: backgroundMobileImg,
      publicKey: null, // 公钥
      codeUrl: null, // 验证码
      loginForm: { // 登录表单
        username: null,
        password: null,
        code: null,
        uuid: null
      },
      loginFormRules: { // 登录表单校验规则
        username: [{ required: true, trigger: 'blur', message: '用户名不能为空' }],
        password: [{ required: true, trigger: 'blur', message: '密码不能为空' }],
        code: [{ required: true, trigger: 'change', message: '验证码不能为空' }]
      },
      loginLoading: false, // 登录按钮加载状态
      redirect: undefined, // 重定向页面
      pwdFlag: false, // 是否显示密码
      pwdFlagType: 'password' // 密码输入框类型
    }
  },
  computed: {
    background() {
      return this.$store.state.app.device === 'mobile' ? backgroundMobileImg : backgroundWebImg
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
  created() {
    // 获取验证码
    this.getCode()

    // 登录过期提醒
    this.point()
  },
  mounted() {
    this.loadPublicKey()
  },
  methods: {
    loadPublicKey() {
      this.$mapi.communal.querySystemPublicKey().then(res => {
        this.publicKey = res.data
      }).catch(_ => {
        this.publicKey = null
      })
    },
    getPwdFlag() {
      this.pwdFlag = !this.pwdFlag
      this.pwdFlagType = this.pwdFlag ? 'text' : 'password'
    },
    getCode() {
      this.$mapi.login.loginCode({ _t: new Date().getTime() }).then(res => {
        this.codeUrl = res.data.img
        this.loginForm.code = null
        this.loginForm.uuid = res.data.uuid
      })
    },
    handleLogin() {
      if (this.loginLoading) return
      this.$refs.loginForm.validate(valid => {
        if (valid) {
          const username = this.loginForm.username
          const payload = encrypt(this.publicKey, JSON.stringify({
            username: username,
            password: this.loginForm.password,
            code: this.loginForm.code,
            uuid: this.loginForm.uuid
          }))

          this.loginLoading = true
          this.$store.dispatch('Login', { _l: payload }).then(change_pwd => {
            if (change_pwd) {
              this.$message.error('首次登录需要重置密码，请修改密码')
              sessionStorage.setItem('force_update_pwd_user', JSON.stringify({
                username,
                type: '1'
              }))
              this.$router.push({ path: '/forceUpdatePass' })
            } else {
              this.$message.success('登录成功')
              window.location.href = this.redirect || '/'
            }
          }).catch(e => {
            if (e.message && e.message === 'EAUTHN004') {
              // 跳转修改密码界面
              sessionStorage.setItem('force_update_pwd_user', JSON.stringify({
                username,
                type: '2'
              }))
              this.$router.push({ path: '/forceUpdatePass' })
            } else {
              this.getCode()
              this.$refs.code.focus()
            }
          }).finally(_ => {
            this.loginLoading = false
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
      this.pageLoading = false
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

<style lang="scss" scoped>
/* 背景容器 */
.login-wrapper {
  position: relative;
  height: 100%;
}

/* 背景图 */
.bg-image {
  position: fixed;
  inset: 0;
  background-size: cover;
  background-position: center;
  z-index: 0;
}

/* ⭐ 渐变遮罩（高级感核心） */
.bg-overlay {
  position: fixed;
  inset: 0;
  z-index: 1;
  backdrop-filter: blur(2px);
  background:
    linear-gradient(
        rgba(255,255,255,0.15),
        rgba(255,255,255,0.05)
    ),
    radial-gradient(circle at center,
      rgba(255,255,255,0.15),
      rgba(0,0,0,0.3)
    );
}

/* 登录区域 */
.login {
  position: relative;
  z-index: 2;
  display: flex;
  justify-content: center;
  align-items: center;
  height: 100%;
}

/* 登录卡片 */
.login-form {
  width: 385px;
  padding: 25px;
  border-radius: 12px;
  backdrop-filter: blur(15px);
  background: linear-gradient(
      rgba(255,255,255,0.6),
      rgba(255,255,255,0.3)
  );
  border: 1px solid rgba(255,255,255,0.4);
  box-shadow: 0 12px 40px rgba(0,0,0,0.2);
}

.login-form ::v-deep .el-input__inner {
  background-color: rgba(255,255,255,0.7);
}

.login-form ::v-deep .input-icon {
  opacity: 0.7;
}

/* 标题 */
.title {
  text-align: center;
  margin-bottom: 25px;
  color: #333;
}

/* ⭐ 验证码一行布局 */
.code-wrapper {
  display: flex;
  align-items: center;
}

.code-wrapper .el-input {
  flex: 1;
}

.code-img {
  width: 110px;
  height: 31px;
  margin-left: 10px;
  cursor: pointer;
  border-radius: 4px;
  transition: 0.2s;
  opacity: 0.7;
}

.code-img:hover {
  transform: scale(1.05);
}

/* 按钮 */
.login-btn {
  width: 100%;
  opacity: 0.7;
}

/* 移动端适配 */
@media (max-width: 768px) {
  .login-form {
    width: 90%;
  }
}

</style>
