<template>
  <v-app-bar app :class="navClass" hide-on-scroll flat height="60">
    <!-- 手机端导航栏 -->
    <div class="d-md-none nav-mobile-container">
      <div class="" style="font-size:18px;font-weight:bold;">
        <router-link to="/">
          {{ websiteConfig['websiteAuthor'] }}
        </router-link>
      </div>
      <div style="margin-left:auto">
        <a @click="openSearch"><i class="iconfont icon-sousuo" style="font-weight: bold;" /></a>
        <a style="margin-left:10px;font-size:20px" @click="openDrawer">
          <i class="iconfont icon-daohang" style="font-weight: bold;" />
        </a>
      </div>
    </div>

    <!-- 电脑端导航栏 -->
    <div class="d-md-block d-none nav-container">
      <div class="float-left blog-title">
        <router-link to="/">
          {{ websiteConfig['websiteAuthor'] }}
        </router-link>
      </div>
      <div class="float-right nav-title">
        <div class="menus-item">
          <a class="menu-btn" @click="openSearch">
            🔍 搜索
          </a>
        </div>
        <div class="menus-item">
          <router-link class="menu-btn" to="/">
            🏡 <span>首页</span>
          </router-link>
        </div>
        <div class="menus-item">
          <a class="menu-btn">
            📕 文章
            <i class="iconfont icon-xiangxia expand" />
          </a>
          <ul class="menus-submenu">
            <li>
              <router-link to="/archives">
                📚 归档
              </router-link>
            </li>
            <li>
              <router-link to="/categories">
                📖 分类
              </router-link>
            </li>
            <li>
              <router-link to="/columns">
                📃 专栏
              </router-link>
            </li>
            <li>
              <router-link to="/tags">
                🔖 标签
              </router-link>
            </li>
          </ul>
        </div>
        <div class="menus-item">
          <a class="menu-btn">
            🌈 生活
            <i class="iconfont icon-xiangxia expand" />
          </a>
          <ul class="menus-submenu">
            <li>
              <router-link to="/albums">
                💽 相册
              </router-link>
            </li>
            <li>
              <router-link to="/talks">
                💬 说说
              </router-link>
            </li>
          </ul>
        </div>
        <div class="menus-item">
          <a class="menu-btn">
            💖 社交
            <i class="iconfont icon-xiangxia expand" />
          </a>
          <ul class="menus-submenu">
            <router-link to="/links">
              📌 友链
            </router-link>
            <router-link to="/message">
              💌 弹幕
            </router-link>
          </ul>
        </div>
        <div class="menus-item">
          <a class="menu-btn">
            🌍 网站
            <i class="iconfont icon-xiangxia expand" />
          </a>
          <ul class="menus-submenu">
            <li>
              <a v-if="$store.state.websiteConfig['readMeLink'] && $store.state.websiteConfig['readMeLink'] !== ''" :href="$store.state.websiteConfig['readMeLink']" target="_blank">
                🧐 关于
              </a>
              <a v-if="$store.state.websiteConfig['websiteAdminLink'] && $store.state.websiteConfig['websiteAdminLink'] !== ''" :href="$store.state.websiteConfig['websiteAdminLink']" target="_blank">
                🐱 后台
              </a>
              <router-link to="/statistics">
                📊 统计
              </router-link>
              <router-link to="/versions">
                👣 版本
              </router-link>
            </li>
          </ul>
        </div>
        <div class="menus-item">
          <a v-if="$store.state.user.id == null || $store.state.user.id === ''" class="menu-btn" @click="openLogin">
            🔒 登录
          </a>
          <template v-else>
            <img v-if="$store.state.user.avatar !== ''" class="user-avatar" :src="$store.state.user.avatar" height="30" width="30" alt="">
            <img v-else class="user-avatar" :src="$store.state.otherConfig['touristAvatar']" height="30" width="30" alt="">
            <ul class="menus-submenu">
              <li>
                <router-link to="/user">
                  <span v-if="$store.state.user.gender === 1">🧑 </span>
                  <span v-else-if="$store.state.user.gender === 2">👧 </span>
                  <span v-else>🤷 </span>
                  个人中心
                </router-link>
              </li>
              <li>
                <a @click="logout">
                  🔓 退出
                </a>
              </li>
            </ul>
          </template>
        </div>
      </div>
    </div>
  </v-app-bar>
</template>

<script>
export default {
  name: 'TopNavBar',
  data() {
    return {
      navClass: 'nav'
    }
  },
  computed: {
    websiteConfig() {
      return this.$store.state.websiteConfig
    },
    avatar() {
      return this.$store.state.avatar
    }
  },
  mounted() {
    window.addEventListener('scroll', this.scroll)
  },
  methods: {
    scroll() {
      const that = this
      that.scrollTop = window.pageYOffset || document.documentElement.scrollTop || document.body.scrollTop
      if (that.scrollTop > 60) {
        that.navClass = 'nav-fixed'
      } else {
        that.navClass = 'nav'
      }
    },
    openSearch() {
      this.$store.state.searchFlag = true
    },
    openDrawer() {
      this.$store.state.drawerFlag = true
    },
    openLogin() {
      this.$store.state.loginFlag = true
    },
    logout() {
      this.$mapi.other.logout({ userId: this.$store.state.user.id }).then(_ => {
        if (this.$route.path === '/user') {
          this.$router.push('/')
        }

        this.$store.commit('logout')
        this.$toast({ type: 'success', message: '注销成功' })
      })
    }
  }
}
</script>

<style scoped>
i {
  margin-right: 4px;
}
ul {
  list-style: none;
}

/* nav */
.nav {
  background: var(--nav-bg-color) !important;
}
.nav a {
  color: var(--nav-font-color) !important;
}
.nav .menu-btn {
  text-shadow: 0.05rem 0.05rem 0.1rem rgba(0, 0, 0, 0.3);
}
.nav .menu-btn:hover {
  color: var(--blue-color) !important;
}
.nav .blog-title a {
  text-shadow: 0.1rem 0.1rem 0.2rem rgba(0, 0, 0, 0.15);
}
.theme--light.nav .menus-submenu {
  box-shadow: var(--light-menus-submenu-box-shadow) !important;
  background-color: var(--light-menus-submenu-bg-color) !important;
}
.theme--light.nav .menus-submenu a {
  color: var(--light-menus-submenu-font-color) !important;
}
.theme--dark.nav .menus-submenu {
  background: var(--dark-menus-submenu-bg-color) !important;
}
.theme--dark.nav .menus-submenu a {
  color: var(--dark-menus-submenu-font-color) !important;
}

/* nav-fixed */
.nav-fixed .menus-item a, .nav-fixed .blog-title a {
  text-shadow: none;
}
.nav-fixed .menu-btn:hover {
  color: var(--blue-color) !important;
}
.theme--light.nav-fixed {
  box-shadow: var(--light-nav-fixed-box-shadow) !important;
  background: var(--light-nav-fixed-bg-color) !important;
}
.theme--light.nav-fixed a {
  color: var(--light-nav-fixed-font-color) !important;
}
.theme--dark.nav-fixed {
  background: var(--dark-nav-fixed-bg-color) !important;
}
.theme--dark.nav-fixed a {
  color: var(--dark-nav-fixed-font-color) !important;
}
.theme--light.nav-fixed .menus-submenu {
  box-shadow: var(--light-menus-submenu-box-shadow) !important;
  background-color: var(--light-menus-submenu-bg-color) !important;
}
.theme--light.nav-fixed .menus-submenu a {
  color: var(--light-menus-submenu-font-color) !important;
}
.theme--dark.nav-fixed .menus-submenu {
  background: var(--dark-menus-submenu-bg-color) !important;
}
.theme--dark.nav-fixed .menus-submenu a {
  color: var(--dark-menus-submenu-font-color) !important;
}

/* mobile nav */
.nav-mobile-container {
  width: 100%;
  display: flex;
  align-items: center;
}

/* pc nav */
.nav-container {
  font-size: 16px;
  width: 100%;
  height: 100%;
}
.blog-title, .nav-title {
  display: flex;
  align-items: center;
  height: 100%;
}
.blog-title a {
  font-size: 20px;
  font-weight: bold;
}
.menus-item {
  position: relative;
  display: inline-block;
  margin: 0 0.5rem;
}
.menus-item a {
  transition: all 0.2s;
}
.menu-btn {
  line-height: 60px;
}
.menu-btn:hover:after {
  width: 100%;
}
.menus-item a:after {
  position: absolute;
  display: block;
  bottom: 0;
  left: 0;
  z-index: -1;
  width: 0;
  height: 6px;
  background-color: var(--light-blue-color);
  content: "";
  transition: all 0.3s ease-in-out;
}
.menus-item:hover .menus-submenu {
  display: block;
}
.menus-submenu {
  position: absolute;
  display: none;
  right: 0;
  width: max-content;
  margin-top: 0;
  animation: submenu 0.3s 0.1s ease both;
}
.menus-submenu:before {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 20px;
  content: "";
}
.menus-submenu a {
  line-height: 2;
  text-shadow: none;
  display: block;
  padding: 6px 14px;
}
.menus-submenu a:hover {
  background: var(--blue-color);
}
@keyframes submenu {
  0% {
    opacity: 0;
    filter: alpha(opacity=0);
    transform: translateY(10px);
  }
  100% {
    opacity: 1;
    filter: none;
    transform: translateY(0);
  }
}

/* 用户头像 */
.user-avatar {
  cursor: var(--globalPointer);
  height: 40px;
  width: 40px;
  margin: auto;
  border-radius: 15%;
}
</style>
