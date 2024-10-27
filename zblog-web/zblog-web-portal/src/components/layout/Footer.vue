<template>
  <v-footer v-if="!isMessage" app padless absolute>
    <div class="footer-wrap">
      <div class="footer-title">{{ websiteConfig['websiteIntro'] }}</div>
      <div>
        ©{{ websiteConfig['websiteCreateTime'] | year }} - {{ new Date().getFullYear() }} By {{ websiteConfig['websiteAuthor'] }}
        <a href="https://beian.miit.gov.cn/" target="_blank">{{ websiteConfig['websiteRecordNo'] }}</a>
      </div>
    </div>
  </v-footer>
</template>

<script>
export default {
  computed: {
    isMessage() {
      return this.$route.path === '/message'
    },
    websiteConfig() {
      return this.$store.state.websiteConfig
    }
  }
}
</script>

<style scoped>
.footer-wrap {
  width: 100%;
  line-height: 2;
  position: relative;
  padding: 5px 0;
  color: #eee;
  font-size: 12px;
  text-align: center;
  background: linear-gradient(-45deg, #ee7752, #ce3e75, #23a6d5, #23d5ab);
  background-size: 400% 400%;
  animation: Gradient 10s ease infinite;
}
.footer-wrap .footer-title {
  font-size: 16px;
}
.footer-wrap a {
  color: #eee !important;
}
@keyframes Gradient {
  0% {
    background-position: 0 50%;
  }
  50% {
    background-position: 100% 50%;
  }
  100% {
    background-position: 0 50%;
  }
}
</style>
