<template>
  <el-breadcrumb class="app-breadcrumb" separator="/">
    <transition-group name="breadcrumb">
      <el-breadcrumb-item v-for="(item, index) in levelList" :key="item.path">
        <!-- 禁止重定向或非最后一个的面包屑禁止点击 -->
        <span v-if="item.redirect === 'noredirect' || index === levelList.length - 1" class="no-redirect">{{ item.meta.title }}</span>
        <a v-else @click.prevent="handleLink(item)">{{ item.meta.title }}</a>
      </el-breadcrumb-item>
    </transition-group>
  </el-breadcrumb>
</template>

<script>
import pathToRegexp from 'path-to-regexp'

export default {
  data() {
    return {
      // 存储当前的面包屑路径列表
      levelList: null
    }
  },
  watch: {
    $route(route) {
      // 监听路由变化，当路由变化时重新获取面包屑数据
      if (route.path.startsWith('/redirect/')) {
        return // 如果是跳转页面，忽略更新面包屑
      }
      this.getBreadcrumb()
    }
  },
  created() {
    this.getBreadcrumb()
  },
  methods: {
    // 获取与当前路由匹配的面包屑数据
    getBreadcrumb() {
      console.log('this.$route', this.$route)
      let matched = this.$route.matched.filter(item => item.meta && item.meta.title)
      console.log('matched', matched)
      const first = matched[0]

      if (!this.isDashboard(first)) {
        // 如果不是 Dashboard 页面，则在开头加上首页项
        matched = [{ path: '/', meta: { title: '首页' }}].concat(matched)
      }

      // 过滤掉没有 title 或设置了 breadcrumb 为 false 的路由项
      this.levelList = matched.filter(item => item.meta && item.meta.title && item.meta.breadcrumb !== false)
    },
    // 判断是否是 Dashboard 页面
    isDashboard(route) {
      const name = route && route.name
      if (!name) {
        return false
      }
      return name.trim().toLocaleLowerCase() === 'Dashboard'.toLocaleLowerCase()
    },
    // 点击面包屑项时的处理方法
    handleLink(item) {
      console.log('items', item)
      const { redirect, path } = item
      if (redirect) {
        // 如果有 redirect 属性，跳转到指定的路径
        this.$router.push(redirect)
      } else {
        this.$router.push(this.pathCompile(path))
      }
    },
    // 处理路径中的动态参数
    pathCompile(path) {
      const { params } = this.$route
      const toPath = pathToRegexp.compile(path)
      return toPath(params)
    }
  }
}
</script>

<style lang="scss" scoped>
.app-breadcrumb.el-breadcrumb {
  display: inline-block;
  font-size: 14px;
  line-height: 50px; /* 行高，使面包屑居中 */
  margin-left: 8px;
  .no-redirect {
    color: #97a8be; /* 未点击的面包屑项的颜色 */
    cursor: text; /* 改变鼠标为文本光标 */
  }
}
</style>
