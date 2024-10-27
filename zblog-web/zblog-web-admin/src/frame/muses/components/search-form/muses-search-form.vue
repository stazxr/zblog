<template>
  <el-form ref="musesSearchForm" v-bind="$attrs" v-on="$listeners">
    <slot />
  </el-form>
</template>

<script>
import ResizeObserver from 'resize-observer-polyfill'
export default {
  name: 'MusesSearchForm',
  inheritAttrs: false,
  props: {
    itemWidth: {
      type: Number,
      default: 100
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
      realWidth: 0,
      // 单行显示的表单项的长度
      count: 0,
      // 是否包含按钮操作的表单项，包含则计算单行可显示表单项个数时减一
      hasBtn: false,
      expandInfo: {
        // 表单项节点个数
        nodeLength: 0,
        // 单行能显示的表单项数目
        count: 0,
        // 表单项最小长度
        minWidth: 0,
        // 是否显示所有表单项
        isShowAll: false
      }
    }
  },
  provide() {
    return {
      expandInfo: this.expandInfo,
      handleClick: this.handleClick
    }
  },
  watch: {
    realWidth: {
      handler() {
        // 当表单长度变化时，控制表单项是否显示
        const offset = this.hasBtn ? 1 : 0
        let lastDom = null
        this.vDom.forEach((dom, index) => {
          dom.componentInstance.isShowBtn = false
          dom.componentInstance.isShow = !!(dom.componentInstance.$props['btn'] || (index + 1) <= this.count - offset)
          if (dom.componentInstance.isShow) {
            lastDom = dom
          }
        })

        // 未隐藏的最后一个表单项显示按钮
        lastDom.componentInstance.isShowBtn = true
      }
    }
  },
  mounted() {
    // 表单项解析
    const tmpDom = []
    this.expandInfo.hasBtn = false
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
      // // 创建 ResizeObserver 实例
      const ro = new ResizeObserver(entries => {
        if (entries != null && entries.length > 0 && entries[0] != null) {
          // 计算表单的宽度
          const { width } = entries[0].contentRect
          this.realWidth = width
          // 表单域标签的宽度
          const labelWidth = this.$attrs['label-width'] ? this.$attrs['label-width'] : 0
          const realLabelWidth = parseInt(labelWidth === 'auto' ? this.defaultLabelWidth : labelWidth)
          // 表单项的宽度
          const minWidth = realLabelWidth + this.itemWidth
          // 计算可显示表单数
          this.count = Math.floor((this.realWidth - this.offset) / minWidth)

          this.expandInfo.count = this.count
          this.expandInfo.minWidth = minWidth
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
.el-form {
  // 响应式布局
  display: flex;
  flex-wrap: wrap;
}
</style>
