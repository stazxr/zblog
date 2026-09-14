<template>
  <div class="muses-markdown-editor" :class="{ 'is-fullscreen': fullscreen }">
    <v-md-editor
      ref="editor"
      v-model="innerValue"
      :height="editorHeight"
      :disabled-menus="disabledMenus"
      :left-toolbar="leftToolbar"
      :right-toolbar="rightToolbar"
      :toolbar="toolbar"
      :mode="mode"
      :include-level="includeLevel"
      @upload-image="handleUploadImage"
      @upload-video="handleUploadVideo"
    />

    <div v-if="fullscreen" class="fullscreen-mask" />
  </div>
</template>

<script>
export default {
  name: 'MusesMarkdownEditor',

  props: {
    /**
     * Markdown 内容
     */
    value: {
      type: String,
      default: ''
    },

    /**
     * 编辑器高度
     */
    height: {
      type: [String, Number],
      default: 600
    },

    /**
     * 编辑器模式
     *
     * edit      编辑模式
     * editable  编辑 + 预览
     */
    mode: {
      type: String,
      default: 'editable'
    },

    /**
     * 是否禁用编辑器
     */
    disabled: {
      type: Boolean,
      default: false
    },

    /**
     * 图片上传方法
     *
     * 接收 File
     * 返回 Promise
     *
     * 返回值可以是：
     * {
     *   url: 'xxx'
     * }
     *
     * 或：
     * 'xxx'
     */
    uploadImage: {
      type: Function,
      default: null
    },

    /**
     * 视频上传方法
     *
     * 接收 File
     * 返回 Promise
     */
    uploadVideo: {
      type: Function,
      default: null
    },

    /**
     * 禁用工具栏菜单
     */
    disabledMenus: {
      type: Array,
      default: () => []
    },

    /**
     * 左侧工具栏
     */
    leftToolbar: {
      type: String,
      default: 'undo redo clear | h bold italic strikethrough quote | ul ol table hr | link image video code | emoji'
    },

    /**
     * 右侧工具栏
     */
    rightToolbar: {
      type: String,
      default: 'preview toc sync-scroll fullscreen'
    },

    /**
     * 工具栏配置
     */
    toolbar: {
      type: Object,
      default: () => ({})
    },

    /**
     * 标题级别
     */
    includeLevel: {
      type: Array,
      default: () => [1, 2, 3, 4, 5, 6]
    }
  },

  data() {
    return {
      innerValue: this.value,
      fullscreen: false,
      fullscreenHeight: 0
    }
  },

  computed: {
    /**
     * 编辑器最终高度
     */
    editorHeight() {
      if (!this.fullscreen) {
        return this.formatHeight(this.height)
      }

      return `${this.fullscreenHeight}px`
    }
  },

  watch: {
    /**
     * 同步外部内容
     */
    value(value) {
      if (value !== this.innerValue) {
        this.innerValue = value
      }
    },

    /**
     * 向外同步 Markdown
     */
    innerValue(value) {
      this.$emit('input', value)
      this.$emit('change', value)
    }
  },

  mounted() {
    this.bindFullscreenEvent()
  },

  beforeDestroy() {
    this.unbindFullscreenEvent()
    this.exitFullscreen()
  },

  methods: {
    /**
     * 格式化编辑器高度
     */
    formatHeight(height) {
      if (typeof height === 'number') {
        return `${height}px`
      }

      if (/^\d+$/.test(height)) {
        return `${height}px`
      }

      return height
    },

    /**
     * 图片上传
     *
     * @param event 文件选择事件
     * @param insert 插入方法
     * @param files 文件列表
     */
    async handleUploadImage(event, insert, files) {
      const fileList = files || this.getFiles(event)

      if (!fileList || !fileList.length) {
        return
      }

      if (!this.uploadImage) {
        this.$emit('upload-image', fileList)
        return
      }

      for (let i = 0; i < fileList.length; i++) {
        const file = fileList[i]

        try {
          this.$emit('upload-start', {
            type: 'image',
            file
          })

          const result = await this.uploadImage(file)
          const url = this.resolveUploadUrl(result)

          if (!url) {
            throw new Error('图片上传成功，但没有获取到图片地址')
          }

          insert(url, file.name)

          this.$emit('upload-success', {
            type: 'image',
            file,
            result
          })
        } catch (error) {
          this.$emit('upload-error', {
            type: 'image',
            file,
            error
          })
        }
      }
    },

    /**
     * 视频上传
     */
    async handleUploadVideo(event, insert, files) {
      const fileList = files || this.getFiles(event)

      if (!fileList || !fileList.length) {
        return
      }

      if (!this.uploadVideo) {
        this.$emit('upload-video', fileList)
        return
      }

      for (let i = 0; i < fileList.length; i++) {
        const file = fileList[i]

        try {
          this.$emit('upload-start', {
            type: 'video',
            file
          })

          const result = await this.uploadVideo(file)
          const url = this.resolveUploadUrl(result)

          if (!url) {
            throw new Error('视频上传成功，但没有获取到视频地址')
          }

          insert(url, file.name)

          this.$emit('upload-success', {
            type: 'video',
            file,
            result
          })
        } catch (error) {
          this.$emit('upload-error', {
            type: 'video',
            file,
            error
          })
        }
      }
    },

    /**
     * 获取文件列表
     */
    getFiles(event) {
      if (!event || !event.target) {
        return []
      }

      return Array.prototype.slice.call(event.target.files || [])
    },

    /**
     * 解析上传结果
     *
     * 支持：
     *
     * 1. 'https://xxx.com/a.png'
     *
     * 2. { url: 'https://xxx.com/a.png' }
     *
     * 3. { data: { url: 'https://xxx.com/a.png' } }
     *
     * 4. { data: 'https://xxx.com/a.png' }
     */
    resolveUploadUrl(result) {
      if (!result) {
        return ''
      }

      if (typeof result === 'string') {
        return result
      }

      if (result.url) {
        return result.url
      }

      if (result.data) {
        if (typeof result.data === 'string') {
          return result.data
        }

        if (result.data.url) {
          return result.data.url
        }
      }

      return ''
    },

    /**
     * 绑定全屏事件
     */
    bindFullscreenEvent() {
      window.addEventListener('resize', this.handleWindowResize)
    },

    /**
     * 解绑全屏事件
     */
    unbindFullscreenEvent() {
      window.removeEventListener('resize', this.handleWindowResize)
    },

    /**
     * 窗口大小变化
     */
    handleWindowResize() {
      if (this.fullscreen) {
        this.updateFullscreenHeight()
      }
    },

    /**
     * 更新全屏高度
     */
    updateFullscreenHeight() {
      this.fullscreenHeight = window.innerHeight
    },

    /**
     * 进入全屏
     */
    enterFullscreen() {
      this.fullscreen = true
      this.updateFullscreenHeight()

      document.body.classList.add('muses-markdown-editor-fullscreen')
    },

    /**
     * 退出全屏
     */
    exitFullscreen() {
      this.fullscreen = false
      document.body.classList.remove('muses-markdown-editor-fullscreen')
    },

    /**
     * 切换全屏
     */
    toggleFullscreen() {
      if (this.fullscreen) {
        this.exitFullscreen()
      } else {
        this.enterFullscreen()
      }
    },

    /**
     * 获取 Markdown 内容
     */
    getValue() {
      return this.innerValue
    },

    /**
     * 设置 Markdown 内容
     */
    setValue(value) {
      this.innerValue = value || ''
    },

    /**
     * 获取编辑器实例
     */
    getEditor() {
      return this.$refs.editor
    },

    /**
     * 聚焦编辑器
     */
    focus() {
      const editor = this.$refs.editor

      if (editor && editor.$refs && editor.$refs.editor) {
        const target = editor.$refs.editor

        if (target.focus) {
          target.focus()
        }
      }
    }
  }
}
</script>

<style lang="scss">
.muses-markdown-editor {
  position: relative;
  width: 100%;

  .v-md-editor {
    width: 100%;
    border: 1px solid #dcdfe6;
    border-radius: 4px;
    box-shadow: none;
    overflow: hidden;
  }

  .v-md-editor:hover {
    border-color: #c0c4cc;
  }

  .v-md-editor:focus-within {
    border-color: #409eff;
  }

  &.is-fullscreen {
    position: fixed;
    top: 0;
    right: 0;
    bottom: 0;
    left: 0;
    z-index: 3000;
    width: 100vw;
    height: 100vh;
    background: #fff;

    .v-md-editor {
      position: relative;
      z-index: 3001;
      width: 100%;
      height: 100% !important;
      border: 0;
      border-radius: 0;
    }
  }
}

.muses-markdown-editor-fullscreen {
  overflow: hidden !important;
}
</style>
