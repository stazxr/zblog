<template>
  <div :class="{ 'has-logo': showLogo }">
    <!-- logo -->
    <logo v-if="showLogo" :collapse="isCollapse" />

    <!-- 菜单栏 -->
    <el-scrollbar wrap-class="scrollbar-wrapper"> <!-- 使用 el-scrollbar 包裹 el-menu，提供自定义滚动条 -->
      <el-menu
        :default-active="activeMenu"
        :collapse="isCollapse"
        :background-color="backgroundColor"
        :text-color="textColor"
        :active-text-color="activeTextColor"
        :collapse-transition="false"
        unique-opened
        mode="vertical"
      >
        <!-- 遍历 sidebarRouters，动态生成菜单项 -->
        <sidebar-item v-for="route in sidebarRouters" :key="route.path" :item="route" :base-path="route.path" />
      </el-menu>
    </el-scrollbar>
  </div>
</template>

<script>
import Logo from './Logo'
import SidebarItem from './SidebarItem'
import variables from '@/assets/styles/variables.scss'

export default {
  components: { SidebarItem, Logo },
  computed: {
    // 从 Vuex 中获取侧边栏的菜单列表
    sidebarRouters() {
      return this.$store.state.router.sidebarRouters
    },
    // 默认激活菜单项
    activeMenu() {
      const route = this.$route
      const { meta, path } = route
      // if set path, the sidebar will highlight the path you set
      if (meta.activeMenu) {
        return meta.activeMenu
      }
      return path
    },
    // 侧边栏设置
    sidebar() {
      return this.$store.state.app.sidebar
    },
    // 是否显示侧边栏的 Logo
    showLogo() {
      return this.$store.state.settings.sidebarLogo
    },
    // 侧边栏是否折叠
    isCollapse() {
      return !this.sidebar.opened
    },
    // 获取菜单背景颜色
    backgroundColor() {
      return variables['menuBg']
    },
    // 获取菜单文字颜色
    textColor() {
      return variables['menuText']
    },
    // 获取激活菜单项文字颜色
    activeTextColor() {
      return variables['menuActiveText']
    }
  }
}
</script>
