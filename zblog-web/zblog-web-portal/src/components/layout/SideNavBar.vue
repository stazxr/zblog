<template>
  <v-navigation-drawer v-model="drawer" app width="250" disable-resize-watcher right overlay-opacity="0.8">
    <!-- 博主介绍 -->
    <div class="blogger-info">
      <v-avatar size="110" style="margin-bottom:0.5rem">
        <img :src="websiteConfig['websiteAvatar']" alt="">
      </v-avatar>
    </div>
    <!-- 博客信息 -->
    <div class="blog-info-wrapper">
      <div class="blog-info-data">
        <router-link to="/archives">
          <div style="font-size: 0.875rem;">文章</div>
          <div style="font-size: 1.125rem;">
            {{ countInfo.articleCount || 0 }}
          </div>
        </router-link>
      </div>
      <div class="blog-info-data">
        <router-link to="/categories">
          <div style="font-size: 0.875rem;">分类</div>
          <div style="font-size: 1.125rem;">
            {{ countInfo.categoryCount || 0 }}
          </div>
        </router-link>
      </div>
      <div class="blog-info-data">
        <router-link to="/columns">
          <div style="font-size: 0.875rem;">专栏</div>
          <div style="font-size: 1.125rem;">
            {{ countInfo.columnCount || 0 }}
          </div>
        </router-link>
      </div>
      <div class="blog-info-data">
        <router-link to="/tags">
          <div style="font-size: 0.875rem;">标签</div>
          <div style="font-size: 1.125rem;">
            {{ countInfo.tagCount || 0 }}
          </div>
        </router-link>
      </div>
    </div>
    <hr>
    <!-- 页面导航 -->
    <div class="menu-container">
      <div class="menus-item">
        <router-link to="/">
          <span>🏡</span> 首页
        </router-link>
      </div>
      <div class="menus-item">
        <router-link to="/archives">
          <span>📚</span> 归档
        </router-link>
      </div>
      <div class="menus-item">
        <router-link to="/categories">
          <span>📖</span> 分类
        </router-link>
      </div>
      <div class="menus-item">
        <router-link to="/columns">
          <span>📃</span> 专栏
        </router-link>
      </div>
      <div class="menus-item">
        <router-link to="/tags">
          <span>🔖</span> 标签
        </router-link>
      </div>
      <div class="menus-item">
        <router-link to="/albums">
          <span>💽</span> 相册
        </router-link>
      </div>
      <div class="menus-item">
        <router-link to="/talks">
          <span>💬</span> 说说
        </router-link>
      </div>
      <div class="menus-item">
        <router-link to="/links">
          <span>📌</span> 友链
        </router-link>
      </div>
      <div class="menus-item">
        <router-link to="/barrageMessage">
          <span>💌</span> 弹幕
        </router-link>
      </div>
      <div class="menus-item">
        <a v-if="$store.state.websiteConfig['readMeLink'] && $store.state.websiteConfig['readMeLink'] !== ''" :href="$store.state.websiteConfig['readMeLink']" target="_blank">
          <span>🧐</span> 关于
        </a>
      </div>
      <div class="menus-item">
        <router-link to="/statistics">
          <span>📊</span> 统计
        </router-link>
      </div>
      <div class="menus-item">
        <router-link to="/versions">
          <span>👣</span> 版本
        </router-link>
      </div>
      <div v-if="$store.state.user.id == null || $store.state.user.id === ''" class="menus-item">
        <a @click="openLogin">
          <span>🔒</span> 登录
        </a>
      </div>
      <div v-else>
        <div class="menus-item">
          <router-link to="/user">
            <span v-if="$store.state.user.gender === 1">🧑 </span>
            <span v-else-if="$store.state.user.gender === 2">👧 </span>
            <span v-else>🤷 </span>
            个人中心
          </router-link>
        </div>
        <div class="menus-item">
          <a @click="logout">
            <span>🔓</span> 退出
          </a>
        </div>
      </div>
    </div>
  </v-navigation-drawer>
</template>

<script>
export default {
  name: 'SideNavBar',
  computed: {
    websiteConfig() {
      return this.$store.state.websiteConfig
    },
    countInfo() {
      return this.$store.state.countInfo
    },
    drawer: {
      set(value) {
        this.$store.state.drawerFlag = value
      },
      get() {
        return this.$store.state.drawerFlag
      }
    }
  },
  methods: {
    openLogin() {
      this.$store.state.loginFlag = true
    },
    logout() {
      this.$mapi.other.logout({ userId: this.$store.state.user.id }).then(_ => {
        if (this.$route.path === '/user') {
          this.$router.go(-1)
        }

        this.$store.commit('logout')
        this.$toast({ type: 'success', message: '注销成功' })
      })
    }
  }
}
</script>

<style scoped>
hr {
  border: 2px dashed #d2ebfd;
  margin: 20px 0;
}

.blogger-info {
  padding: 26px 30px 0;
  text-align: center;
}
.blog-info-wrapper {
  display: flex;
  align-items: center;
  padding: 12px 10px 0;
}
.blog-info-data {
  flex: 1;
  line-height: 2;
  text-align: center;
}
.menu-container {
  padding: 0 10px 40px;
  animation: 0.8s ease 0s 1 normal none running sidebarItem;
}
.menus-item a {
  padding: 6px 30px;
  display: block;
  line-height: 2;
}
.menus-item span {
  margin-right: 2rem;
}
@keyframes sidebarItem {
  0% {
    transform: translateX(200px);
  }
  100% {
    transform: translateX(0);
  }
}
</style>
