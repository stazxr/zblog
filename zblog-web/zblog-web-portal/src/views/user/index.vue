<template>
  <div>
    <div class="banner" :style="cover">
      <h1 class="banner-title">个人中心</h1>
    </div>
    <v-card class="blog-container">
      <div>
        <span class="info-title">基本信息</span>
      </div>
      <v-row class="info-wrapper">
        <v-col md="3" cols="12">
          <button id="pick-avatar">
            <v-avatar size="140">
              <img :src="$store.state.user.avatar" alt="">
            </v-avatar>
          </button>
        </v-col>
        <v-col md="7" cols="12">
          <v-text-field v-model="userInfo.nickname" disabled label="昵称" placeholder="昵称" />
          <v-text-field v-model="userInfo.webSite" disabled class="mt-7" label="个人网站" placeholder="个人网站" />
          <v-text-field v-model="userInfo.intro" disabled class="mt-7" label="签名" placeholder="签名" />
          <div class="mt-7 binding">
            <v-text-field v-model="userInfo.email" disabled label="邮箱" placeholder="邮箱" />
          </div>
          <v-btn v-if="websiteAdminLink != null && websiteAdminLink !== ''" outlined class="mt-5">
            <a :href="websiteAdminLink" target="_blank">登录后台管理系统</a>
          </v-btn>
        </v-col>
      </v-row>
    </v-card>
  </div>
</template>

<script>
export default {
  data: function() {
    return {
      userInfo: {
        nickname: this.$store.state.user.nickname,
        intro: this.$store.state.user.intro,
        webSite: this.$store.state.user.webSite,
        email: this.$store.state.user.email
      }
    }
  },
  computed: {
    cover() {
      let cover = ''
      this.$store.state.pageList.forEach(item => {
        if (item['pageLabel'] === 'user') {
          cover = item['pageCover']
        }
      })
      return 'background: url(' + cover + ') center center / cover no-repeat'
    },
    websiteAdminLink() {
      return this.$store.state.websiteConfig['websiteAdminLink']
    }
  },
  methods: {

  }
}
</script>

<style scoped>
.info-title {
  font-size: 1.25rem;
  font-weight: bold;
}
.info-wrapper {
  margin-top: 1rem;
  display: flex;
  align-items: center;
  justify-content: center;
}
#pick-avatar {
  outline: none;
}
.binding {
  display: flex;
  align-items: center;
}
::v-deep [disabled] {
  cursor: var(--globalCursor);
}
</style>
