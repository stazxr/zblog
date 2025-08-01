<template>
  <div :class="classObj" class="app-wrapper">
    <!-- 手机端侧边栏的关闭功能: 背景层, 表示侧边栏在手机端是否处于开启状态。只有当侧边栏展开时，才会显示这个背景层。 -->
    <div v-if="isMobileSidebarOpen" class="drawer-bg" @click="handleClickOutside" />
    <!-- 侧边栏: 显示菜单列表 -->
    <sidebar class="sidebar-container" />
    <div :class="{ hasTagsView: needTagsView }" class="main-container">
      <div :class="{ 'fixed-header': fixedHeader }">
        <navbar />
        <tags-view v-if="needTagsView" />
      </div>
      <app-main />
      <right-panel v-if="showSettings">
        <settings />
      </right-panel>
    </div>
    <!-- 防止刷新后主题丢失 -->
    <Theme v-show="false" ref="theme" />
  </div>
</template>

<script>
import RightPanel from '@/components/RightPanel'
import ResizeMixin from './mixin/ResizeHandler'
import Theme from '@/components/ThemePicker'
import { Sidebar, AppMain, Navbar, Settings, TagsView } from './components'
import { mapState } from 'vuex'
import Cookies from 'js-cookie'
import { clearTimer } from '@/utils/timerUtils'

export default {
  name: 'Layout',
  components: {
    Sidebar,
    AppMain,
    Navbar,
    RightPanel,
    Settings,
    TagsView,
    Theme
  },
  mixins: [ResizeMixin],
  data() {
    return {
      // 用户信息刷新定时器
      interval: null
    }
  },
  computed: {
    ...mapState({
      // 侧边栏设置
      sidebar: state => state.app.sidebar,
      // 设备类型
      device: state => state.app.device,
      // 是否显示设置面板
      showSettings: state => state.settings.showSettings,
      // 是否显示标签栏
      needTagsView: state => state.settings.tagsView,
      // 是否固定头部
      fixedHeader: state => state.settings.fixedHeader
    }),
    // 动态 CSS
    classObj() {
      return {
        hideSidebar: !this.sidebar.opened,
        openSidebar: this.sidebar.opened,
        withoutAnimation: this.sidebar.withoutAnimation,
        mobile: this.device === 'mobile'
      }
    },
    // 是否是移动端且侧边栏为展开状态
    isMobileSidebarOpen() {
      return this.device === 'mobile' && this.sidebar.opened
    }
  },
  mounted() {
    if (Cookies.get('theme')) {
      this.$refs.theme.theme = Cookies.get('theme')
      this.$store.dispatch('settings/ChangeSetting', {
        key: 'theme',
        value: Cookies.get('theme')
      })
    }
  },
  created() {
    // 每60秒刷新一次用户信息（后台会同步刷新访问令牌）
    this.interval = setInterval(() => {
      this.$store.dispatch('RefreshUser')
    }, 60000)
  },
  destroyed() {
    // 调用清除定时器的工具函数
    this.interval = clearTimer(this.interval)
  },
  methods: {
    handleClickOutside() {
      this.$store.dispatch('app/CloseSideBar', { withoutAnimation: false })
    }
  }
}
</script>

<style lang="scss" scoped>
  @import "~@/assets/styles/mixin.scss";
  @import "~@/assets/styles/variables.scss";

  .app-wrapper {
    @include clearfix;
    position: relative;
    height: 100%;
    width: 100%;

    &.mobile.openSidebar {
      position: fixed;
      top: 0;
    }
  }

  .drawer-bg {
    background: #000;
    opacity: 0.3;
    width: 100%;
    height: 100%;
    position: absolute;
    top: 0;
    left: 0; // 确保 背景层 从 页面的左边缘 开始，完全覆盖在页面的左侧区域
    z-index: 999; // 确保背景层位于侧边栏上方
  }

  .fixed-header {
    position: fixed;
    top: 0;
    right: 0;
    z-index: 9;
    width: calc(100% - #{$sideBarWidth});
    transition: width 0.28s;
    padding: 0;
  }

  .hideSidebar .fixed-header {
    width: calc(100% - 54px)
  }

  .mobile .fixed-header {
    width: 100%;
  }
</style>
