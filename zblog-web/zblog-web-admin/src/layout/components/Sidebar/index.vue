<template>
  <div :class="{'has-logo' : showLogo}">
    <logo v-if="showLogo" :collapse="isCollapse" />
    <el-scrollbar wrap-class="scrollbar-wrapper">
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
    sidebarRouters() {
      return this.$store.state.router.sidebarRouters
    },
    activeMenu() {
      const route = this.$route
      const { meta, path } = route
      // if set path, the sidebar will highlight the path you set
      if (meta.activeMenu) {
        return meta.activeMenu
      }
      return path
    },
    variables() {
      return variables
    },
    sidebar() {
      return this.$store.state.app.sidebar
    },
    showLogo() {
      return this.$store.state.settings.sidebarLogo
    },
    isCollapse() {
      return !this.sidebar.opened
    },
    backgroundColor() {
      return variables['menuBg']
    },
    textColor() {
      return variables['menuText']
    },
    activeTextColor() {
      return variables['menuActiveText']
    }
  }
}
</script>
