<template>
  <div class="right-side" :style="isShow">
    <div :class="'right-side-config-hide ' + isOut">
      <span :class="'right-side-icon ' + icon" style="font-size: 14px;" @click="check" />
    </div>
    <div class="setting-container" @click="show">
      <span class="iconfont icon-shezhi" style="font-size: 14px;" />
    </div>
    <i class="right-side-icon iconfont icon-xiangshang4" style="font-size: 14px;" @click="backTop" />
  </div>
</template>

<script>
export default {
  data: function() {
    return {
      isShow: '',
      isOut: 'right-side-out',
      icon: 'iconfont icon-moon_fill'
    }
  },
  mounted() {
    window.addEventListener('scroll', this.scrollToTop)
  },
  destroyed() {
    window.removeEventListener('scroll', this.scrollToTop)
  },
  methods: {
    backTop() {
      window.scrollTo({
        behavior: 'smooth',
        top: 0
      })
    },
    scrollToTop() {
      const that = this
      that.scrollTop = window.pageYOffset || document.documentElement.scrollTop || document.body.scrollTop
      if (that.scrollTop > 20) {
        that.isShow = 'opacity: 1;transform: translateX(-38px);'
      } else {
        that.isShow = ''
      }
    },
    show() {
      this.isOut = this.isOut === 'right-side-out' ? 'right-side-in' : 'right-side-out'
    },
    check() {
      this.icon = this.icon === 'iconfont icon-moon_fill' ? 'iconfont icon-taiyang' : 'iconfont icon-moon_fill'
      this.$vuetify.theme.dark = !this.$vuetify.theme.dark
    }
  }
}
</script>

<style scoped>
.right-side {
  z-index: 4;
  position: fixed;
  right: -38px;
  bottom: 85px;
  transition: all 0.5s;
}
.right-side-config-hide {
  transform: translate(35px, 0);
}
.right-side-out {
  animation: right-side-out 0.3s;
}
.right-side-in {
  transform: translate(0, 0) !important;
  animation: right-side-in 0.3s;
}
.right-side i {
  font-size: 18px;
}
.right-side-icon, .setting-container {
  display: block;
  margin-bottom: 2px;
  width: 30px;
  height: 30px;
  background-color: #49b1f5;
  color: #fff;
  text-align: center;
  font-size: 18px;
  line-height: 30px;
  cursor: pointer;
}
.right-side-icon:hover, .setting-container:hover {
  background-color: #ff7242;
}
.setting-container i {
  display: block;
  animation: turn-around 2s linear infinite;
}

@keyframes right-side-out {
  0% {
    transform: translate(0, 0);
  }
  100% {
    transform: translate(30px, 0);
  }
}
@keyframes right-side-in {
  0% {
    transform: translate(30px, 0);
  }
  100% {
    transform: translate(0, 0);
  }
}
</style>
