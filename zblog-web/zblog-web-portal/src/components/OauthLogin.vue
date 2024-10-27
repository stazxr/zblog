<template>
  <div class="oauth-background">
    <div id="preloader_1">
      <span />
      <span />
      <span />
      <span />
      <span />
    </div>
  </div>
</template>

<script>
export default {
  data() {
    return {
      urlParam: {}
    }
  },
  mounted() {
    this.login()
  },
  methods: {
    setReqHashParam() {
      const param = this.$route.hash
      if (param.indexOf('#') !== -1) {
        const paramAry = param.substring(1).split('&')
        for (let i = 0; i < paramAry.length; i++) {
          if (paramAry[i] !== '' && paramAry[i].indexOf('=') !== -1) {
            const tmpAry = paramAry[i].split('=')
            if (tmpAry.length === 2 && tmpAry[0] !== '') {
              this.$set(this.urlParam, tmpAry[0], tmpAry[1])
            }
          }
        }
      } else {
        this.urlParam = {}
      }
    },
    login() {
      // 关闭登录框
      this.$store.state.loginFlag = false

      // 登录方式
      this.setReqHashParam()
      const authType = this.$route.query['authType']
      if (authType === 'qq') {
        // QQ 登录
        const param = {
          accessToken: this.urlParam['access_token'],
          expires_in: this.urlParam['expires_in']
        }

        this.$mapi.portal.loginQq(param).then(({ code, data, message }) => {
          if (code === 200) {
            this.$store.commit('login', data)
            this.userToken = data.userToken
            this.$toast({ type: 'success', message: '登录成功' })
          } else {
            this.$toast({ type: 'error', message: message })
          }
        }).catch(_ => {
          this.$toast({ type: 'error', message: '登录失败' })
        })
      }

      // 跳转回原页面
      const loginUrl = this.$store.state.loginUrl
      if (loginUrl != null && loginUrl !== '') {
        this.$router.push({ path: loginUrl })
      } else {
        this.$router.push({ path: '/' })
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
