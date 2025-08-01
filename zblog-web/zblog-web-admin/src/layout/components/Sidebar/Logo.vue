<template>
  <div class="sidebar-logo-container" :class="{ 'collapse': collapse }">
    <transition name="sidebarLogoFade">
      <!-- 当侧边栏折叠时显示 logo -->
      <router-link v-if="collapse" key="collapse" class="sidebar-logo-link" to="/">
        <img v-if="logoImg" :src="logoImg" class="sidebar-logo" alt="">
        <h1 v-else class="sidebar-title">{{ title }} </h1>
      </router-link>
      <!-- 当侧边栏展开时显示 logo 和标题 -->
      <router-link v-else key="expand" class="sidebar-logo-link" to="/">
        <img v-if="logoImg" :src="logoImg" class="sidebar-logo" alt="">
        <h1 class="sidebar-title">{{ title }} </h1>
      </router-link>
    </transition>
  </div>
</template>

<script>
import LogoImg from '@/assets/images/sidebar-logo.png'
export default {
  name: 'SidebarLogo',
  props: {
    // 控制侧边栏的展开和折叠状态
    collapse: {
      type: Boolean,
      required: true
    }
  },
  data() {
    return {
      logoImg: LogoImg // 存储 logo 图片路径
    }
  },
  computed: {
    title() {
      // 从 Vuex 中获取侧边栏标题，若没有设置则返回默认值 'Z-BLOG'
      return this.$store.state.settings.title || 'Z-BLOG'
    }
  }
}
</script>

<style lang="scss" scoped>
// 定义淡入效果，过渡时持续 1.5 秒
.sidebarLogoFade-enter-active {
  transition: opacity 0.5s ease; /* 缩短动画时间，提高流畅性 */
}

// 定义淡出效果，透明度为 0
.sidebarLogoFade-enter, .sidebarLogoFade-leave-to {
  opacity: 0;
}

// 容器的基本样式
.sidebar-logo-container {
  position: relative;
  width: 100%;
  height: 50px; // 和 .has-logo 样式保持一致
  line-height: 50px;
  text-align: center;
  overflow: hidden;

  // 侧边栏 logo 和标题的容器样式
  & .sidebar-logo-link {
    height: 100%;
    width: 100%;

    // logo 图标的样式
    & .sidebar-logo {
      width: 32px;
      height: 32px;
      vertical-align: middle;
      margin-right: 6px;
    }

    // 标题的样式
    & .sidebar-title {
      display: inline-block;
      color: #fff;
      font-weight: 600;
      font-size: 14px;
      font-family: Avenir, Helvetica Neue, Arial, Helvetica, sans-serif;
      vertical-align: middle;
      margin: 0;
      line-height: 50px;
    }
  }

  // 当侧边栏折叠时，logo 右侧的 margin 设为 0
  &.collapse {
    .sidebar-logo {
      margin-right: 0;
    }
  }
}
</style>
