<template>
  <div :class="{ 'show': show }" class="header-search">
    <svg-icon class-name="search-icon" icon-class="search" @click.stop="click" />
    <el-select
      ref="headerSearchSelect"
      v-model="search"
      :remote-method="querySearch"
      filterable
      default-first-option
      remote
      placeholder="Search"
      class="header-search-select"
      @change="change"
    >
      <el-option v-for="item in options" :key="item.path" :value="item" :label="item.title.join(' > ')" />
    </el-select>
  </div>
</template>

<script>
import Fuse from 'fuse.js' // 导入 fuzzy-search，提供模糊搜索功能
import path from 'path'

export default {
  name: 'HeaderSearch',
  data() {
    return {
      search: '', // 当前输入的搜索关键词
      options: [], // 搜索结果选项
      searchPool: [], // 所有可搜索的路由数据
      show: false, // 控制搜索框是否显示
      fuse: undefined // Fuse.js 实例，用于处理模糊搜索
    }
  },
  computed: {
    routes() {
      // 从 Vuex store 获取路由数据
      return this.$store.state.router.routers
    }
  },
  watch: {
    routes() {
      // 路由变化时，重新生成路由列表
      this.searchPool = this.generateRoutes(this.routes)
    },
    searchPool(list) {
      // 搜索池数据变化时，重新初始化 Fuse 搜索引擎
      this.initFuse(list)
    },
    show(value) {
      // 控制点击空白区域时关闭搜索框
      if (value) {
        document.body.addEventListener('click', this.close)
      } else {
        document.body.removeEventListener('click', this.close)
      }
    }
  },
  mounted() {
    // 组件挂载后初始化搜索池
    this.searchPool = this.generateRoutes(this.routes)
  },
  methods: {
    click() {
      this.show = !this.show
      if (this.show) {
        this.$refs.headerSearchSelect && this.$refs.headerSearchSelect.focus()
      }
    },
    // 关闭搜索框并清空搜索结果
    close() {
      this.$refs.headerSearchSelect && this.$refs.headerSearchSelect.blur()
      this.options = []
      this.show = false
    },
    // 处理选择项，如果是外部链接则在新窗口打开
    change(val) {
      if (this.isHttp(val.path)) {
        window.open(val.path, '_blank')
      } else {
        this.$router.push(val.path)
      }
      this.search = ''
      this.options = []
      this.$nextTick(() => {
        this.show = false
      })
    },
    // 初始化 Fuse.js 搜索引擎
    initFuse(list) {
      this.fuse = new Fuse(list, {
        shouldSort: true,
        threshold: 0.4,
        location: 0,
        distance: 100,
        maxPatternLength: 32,
        minMatchCharLength: 1,
        keys: [
          { name: 'title', weight: 0.7 }, // 优先匹配标题
          { name: 'path', weight: 0.3 } // 次要匹配路径
        ]
      })
    },
    // 递归生成所有可搜索的路由项，包括父子路由
    generateRoutes(routes, basePath = '/', prefixTitle = []) {
      let res = []

      for (const router of routes) {
        // skip hidden router
        if (router.hidden) { continue }

        const data = {
          path: this.isHttp(router.path) ? router.path : path.resolve(basePath, router.path),
          title: [...prefixTitle]
        }

        if (router.meta && router.meta.title) {
          data.title = [...data.title, router.meta.title]
          if (router.redirect !== 'noredirect') {
            res.push(data)
          }
        }

        // recursive child routes
        if (router.children) {
          const tempRoutes = this.generateRoutes(router.children, data.path, data.title)
          if (tempRoutes.length >= 1) {
            res = [...res, ...tempRoutes]
          }
        }
      }

      return res
    },
    // 处理搜索输入框中的关键词，进行模糊搜索
    querySearch(query) {
      if (query !== '') {
        this.options = this.fuse.search(query)
      } else {
        this.options = []
      }
    },
    isHttp(url) {
      return url.indexOf('http://') !== -1 || url.indexOf('https://') !== -1
    }
  }
}
</script>

<style lang="scss" scoped>
.header-search {
  font-size: 0 !important;

  .search-icon {
    cursor: pointer;
    font-size: 18px;
    vertical-align: middle;
  }

  .header-search-select {
    font-size: 18px;
    transition: width 0.2s;
    width: 0;
    overflow: hidden;
    background: transparent;
    border-radius: 0;
    display: inline-block;
    vertical-align: middle;

   ::v-deep .el-input__inner {
      border-radius: 0;
      border: 0;
      padding-left: 0;
      padding-right: 0;
      box-shadow: none !important;
      border-bottom: 1px solid #d9d9d9;
      vertical-align: middle;
    }
  }

  &.show {
    .header-search-select {
      width: 210px;
      margin-left: 10px;
    }
  }
}
</style>
