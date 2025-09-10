<template>
  <!-- 如果菜单项的显示状态为 true，则加载菜单项 -->
  <div v-if="item.show" class="menu-wrapper">
    <!-- 判断是否只有一个显示的子菜单，并且该菜单不强制显示子菜单，且当前菜单没有 alwaysShow 属性 -->
    <template v-if="hasOneShowingChild(item.children, item) && (!onlyOneChild.children || onlyOneChild.noShowingChildren) && !(item.alwaysShow === true)">
      <!-- 如果 onlyOneChild 存在 meta 信息，则显示该子菜单项 -->
      <app-link v-if="onlyOneChild.meta" :to="resolvePath(onlyOneChild.path)">
        <el-menu-item :index="resolvePath(onlyOneChild.path)" :class="{'submenu-title-noDropdown':!isNest}">
          <!-- 使用 item 组件来显示子菜单的图标和标题 -->
          <item :icon="onlyOneChild.meta.icon || (item.meta && item.meta.icon)" :title="onlyOneChild.meta.title" />
        </el-menu-item>
      </app-link>
    </template>

    <!-- 如果上面的条件不成立，显示一个包含子菜单的菜单项 -->
    <el-submenu v-else ref="subMenu" :index="resolvePath(item.path)" popper-append-to-body>
      <template slot="title">
        <!-- 显示菜单项的标题和图标 -->
        <item v-if="item.meta" :icon="item.meta.icon" :title="item.meta.title" />
      </template>
      <!-- 使用 sidebar-item 递归渲染子菜单 -->
      <sidebar-item
        v-for="child in item.children"
        :key="child.path"
        :is-nest="true"
        :item="child"
        :base-path="resolvePath(child.path)"
        class="nest-menu"
      />
    </el-submenu>
  </div>
</template>

<script>
import path from 'path'
import { isExternal } from '@/utils/validate'
import Item from './Item'
import AppLink from './Link'
import FixIosBug from './FixIosBug' // 导入 mixin，用于修复 iOS 特有的 bug

export default {
  name: 'SidebarItem', // 侧边栏菜单项
  components: { Item, AppLink },
  mixins: [FixIosBug],
  props: {
    item: {
      type: Object,
      required: true
    },
    isNest: {
      type: Boolean,
      default: false
    },
    basePath: {
      type: String,
      default: ''
    }
  },
  data() {
    // 用于存储唯一子菜单项的数据
    this.onlyOneChild = null
    return {}
  },
  methods: {
    /**
     * 检查当前菜单项是否只有一个可见的子菜单，并更新 onlyOneChild。
     * @param {Array} children - 当前菜单项的子菜单数组
     * @param {Object} parent - 当前菜单项本身
     * @returns {Boolean} - 是否只有一个可见的子菜单
     */
    hasOneShowingChild(children = [], parent) {
      const showingChildren = children.filter(item => {
        if (item.show) {
          // 如果子菜单项可见，将其临时设置为 onlyOneChild
          this.onlyOneChild = item
          return true
        } else {
          return false
        }
      })

      // 如果只有一个可见的子菜单，返回 true
      if (showingChildren.length === 1) {
        return true
      }

      // 如果没有可见的子菜单，将父菜单作为唯一子菜单
      if (showingChildren.length === 0) {
        this.onlyOneChild = { ... parent, path: '', noShowingChildren: true }
        return true
      }

      this.onlyOneChild = null
      return false
    },
    /**
     * 解析路由路径，处理外部链接和内部链接。
     * @param {String} routePath - 路由路径
     * @returns {String} - 解析后的完整路径
     */
    resolvePath(routePath) {
      // 如果是外部链接，直接返回路由路径
      if (isExternal(routePath)) {
        return routePath
      }
      // 如果基础路径是外部链接，直接返回基础路径
      if (isExternal(this.basePath)) {
        return this.basePath
      }
      // 否则，将基础路径和路由路径合并为一个完整路径
      return path.resolve(this.basePath, routePath)
    }
  }
}
</script>
