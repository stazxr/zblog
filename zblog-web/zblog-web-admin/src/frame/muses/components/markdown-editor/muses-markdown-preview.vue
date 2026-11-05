<template>
  <div ref="previewContainer" class="muses-markdown-preview">
    <v-md-preview-html
      :html="html"
      preview-class="vuepress-markdown-body"
    />
  </div>
</template>

<script>
import CodeBlockEnhancer from './code-block-enhancer.js'
export default {
  name: 'MusesMarkdownPreview',
  props: {
    html: {
      type: String,
      default: ''
    }
  },
  data() {
    return {
      codeBlockEnhancer: null
    }
  },
  mounted() {
    this.initCodeBlockEnhancer()
  },
  beforeDestroy() {
    this.destroyCodeBlockEnhancer()
  },
  methods: {
    /**
     * 初始化代码块增强器
     */
    initCodeBlockEnhancer() {
      if (this.codeBlockEnhancer) {
        return
      }

      this.codeBlockEnhancer = new CodeBlockEnhancer(
        this.$refs.previewContainer
      )

      this.codeBlockEnhancer.init()
    },
    /**
     * 销毁代码块增强器
     */
    destroyCodeBlockEnhancer() {
      if (this.codeBlockEnhancer) {
        this.codeBlockEnhancer.destroy()
        this.codeBlockEnhancer = null
      }
    }
  }
}
</script>

<style lang="scss" scoped>
.muses-markdown-preview {
  width: 100%;
  min-width: 0;
}
</style>
