<template>
  <v-snackbar v-model="show" top :timeout="3000" :color="color">
    <i :class="icon" style="margin-right: 10px;" />{{ message }}
  </v-snackbar>
</template>

<script>
export default {
  data: function() {
    return {
      show: false,
      message: '',
      type: 'info',
      color: '#49b1f5',
      icon: 'iconfont icon-info'
    }
  },
  watch: {
    type(value) {
      switch (value) {
        case 'info':
          this.color = '#49B1F5'
          this.icon = 'iconfont icon-info'
          break
        case 'help':
          this.color = '#72797C'
          this.icon = 'iconfont icon-help'
          break
        case 'success':
          this.color = '#52C41A'
          this.icon = 'iconfont icon-success'
          break
        case 'warning':
          this.color = '#F57C00'
          this.icon = 'iconfont icon-warning'
          break
        case 'error':
          this.color = '#F56C6C'
          this.icon = 'iconfont icon-error'
          break
        default:
          this.color = '#49b1f5'
          this.icon = 'iconfont icon-success'
      }
    }
  }
}
</script>
