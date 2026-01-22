<template>
  <el-form ref="musesSearchForm" class="search-form" v-bind="$attrs" v-on="$listeners">
    <slot />
  </el-form>
</template>

<script>
import ResizeObserver from 'resize-observer-polyfill'
export default {
  name: 'MusesSearchForm',
  inheritAttrs: false,
  props: {
    // 表单项的宽度（不包含 label-width）
    itemWidth: {
      type: Number,
      default: 100
    },
    // 表单项之间的间距
    itemPadding: {
      type: Number,
      default: 10
    },
    // 计算单行个数时，需要预留的宽度，一般为父组件的 (padding-left + padding-right)
    offset: {
      type: Number,
      default: 0
    }
  },
  data() {
    return {
      // 表单项节点列表
      vDom: [],
      // 默认表单域标签的宽度，当 label-width 值配置为 auto 时生效
      defaultLabelWidth: '80px',
      // 表单的真实宽度
      realFormWidth: 0,
      // 单行显示的表单项的长度
      showNodeCount: 0,
      // 是否包含按钮操作的表单项，包含则计算单行可显示表单项个数时减一
      hasBtn: false,
      expandInfo: {
        // 是否显示所有表单项
        isShowAll: false,
        // 表单项节点个数
        nodeLength: 0,
        // 单行能显示的表单项数目
        showNodeCount: 0,
        // 表单域宽度
        itemWidth: 0,
        // 表单域间隔
        paddingRight: 0
      }
    }
  },
  // 父组件使用 provide 提供数据，子组件使用 inject 来注入父组件提供的数据或方法
  provide() {
    return {
      expandInfo: this.expandInfo,
      handleClick: this.handleClick
    }
  },
  watch: {
    realFormWidth: {
      handler() {
        // 当表单长度变化时，控制表单项是否显示
        const offsetCount = this.hasBtn ? 1 : 0
        let lastDom = null
        this.vDom.forEach((dom, index) => {
          dom.componentInstance.isShowBtn = false
          dom.componentInstance.isShow = !!(dom.componentInstance.$props['btn'] || (index + 1) <= this.showNodeCount - offsetCount)
          if (dom.componentInstance.isShow) {
            lastDom = dom
          }
        })

        // 未隐藏的最后一个表单项显示按钮 TODO BUGS 当没有配置 btn 搜索表单不显示
        lastDom.componentInstance.isShowBtn = true
      }
    }
  },
  mounted() {
    // 表单项解析
    const tmpDom = []
    this.hasBtn = false
    this.$slots.default.forEach(slot => {
      if (slot.tag !== undefined && slot.data) {
        if (slot.componentInstance.$props['btn']) {
          this.hasBtn = true
        }
        tmpDom.push(slot)
      }
    })
    this.vDom = tmpDom
    this.expandInfo.nodeLength = this.vDom.length
    this.watchForm()
  },
  methods: {
    watchForm() {
      // 创建 ResizeObserver 实例
      const ro = new ResizeObserver(entries => {
        if (entries != null && entries.length > 0 && entries[0] != null) {
          // 计算表单的宽度
          const { width } = entries[0].contentRect
          this.realFormWidth = width
          // console.log('查询表单长度', this.realFormWidth)
          // 表单域标签的宽度
          const labelWidth = this.$attrs['label-width'] ? this.$attrs['label-width'] : 0
          const realLabelWidth = parseInt(labelWidth === 'auto' ? this.defaultLabelWidth : labelWidth)
          // console.log('表单域标签的宽度', realLabelWidth)
          // console.log('表单域宽度（不包含标签）', this.itemWidth)
          // console.log('表单域间隔的宽度', this.itemPadding)

          // 表单域的宽度（包含标签）
          const realItemWidth = realLabelWidth + this.itemWidth + this.itemPadding
          // console.log('真实表单域的宽度（包含标签）', realItemWidth)
          // 计算可显示表单数
          this.showNodeCount = Math.floor((this.realFormWidth - this.offset) / realItemWidth)
          // console.log('可以显示的表单域个数', this.showNodeCount)

          this.expandInfo.showNodeCount = this.showNodeCount
          this.expandInfo.itemWidth = realItemWidth
          this.expandInfo.paddingRight = this.itemPadding
          // console.log('扩展信息', this.expandInfo)
        }
      })

      // 监听 musesSearchForm 表单的尺寸变化
      ro.observe(this.$refs.musesSearchForm.$el)
    },
    handleClick() {
      this.expandInfo.isShowAll = !this.expandInfo.isShowAll
      if (this.expandInfo.isShowAll) {
        this.vDom.forEach((dom, index) => {
          dom.componentInstance.isShowBtn = index === this.vDom.length - 1
        })
      } else {
        let lastDom = null
        this.vDom.forEach((dom) => {
          dom.componentInstance.isShowBtn = false
          if (dom.componentInstance.isShow) {
            lastDom = dom
          }
        })
        lastDom.componentInstance.isShowBtn = true
      }
    }
  }
}
</script>

<style lang="scss" scoped>
.search-form {
  display: flex;
  flex-wrap: wrap;

  ::v-deep(.el-form-item--mini.el-form-item),
  ::v-deep(.el-form-item--small.el-form-item) {
    margin-bottom: 10px; // 自定义间距
  }
}
</style>
