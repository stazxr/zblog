<template>
  <div v-if="item.show" class="menu-wrapper">
    <!-- 情况一：只有一个可见子节点 -->
    <template
      v-if="item['alwaysShow'] === false &&
        oneChildInfo.hasOne &&
        (!oneChildInfo.child.children || oneChildInfo.child.noShowingChildren)
      "
    >
      <app-link v-if="oneChildInfo.child.meta" :to="resolvePath(oneChildInfo.child.path)">
        <el-menu-item
          :index="resolvePath(oneChildInfo.child.path)"
          :class="{ 'submenu-title-noDropdown': !isNest }"
        >
          <item
            :icon="oneChildInfo.child.meta.icon || (item.meta && item.meta.icon)"
            :title="oneChildInfo.child.meta.title"
          />
        </el-menu-item>
      </app-link>
    </template>

    <!-- 情况二：多个子菜单 -->
    <el-submenu v-else :index="resolvePath(item.path)" popper-append-to-body>
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
  computed: {
    /**
     * 计算唯一子节点信息
     * 绝不修改 data，从而避免无限循环
     */
    oneChildInfo() {
      const children = this.item.children || []
      const showing = []
      let theChild = null

      for (let i = 0; i < children.length; i++) {
        if (children[i].show) {
          showing.push(children[i])
          theChild = children[i]
        }
      }

      if (showing.length === 1) {
        return { hasOne: true, child: theChild }
      }

      if (showing.length === 0) {
        // 没子节点，则使用父节点
        const parentAsChild = {
          ...this.item,
          path: '',
          noShowingChildren: true
        }
        return { hasOne: true, child: parentAsChild }
      }

      // 多个子节点
      return { hasOne: false, child: null }
    }
  },
  methods: {
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
      return path.resolve(this.basePath || '', routePath || '')
    }
  }
}
</script>
