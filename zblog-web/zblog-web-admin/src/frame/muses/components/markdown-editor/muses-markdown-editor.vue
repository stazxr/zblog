<template>
  <div class="muses-markdown-editor" :class="{ 'is-fullscreen': fullscreen }">
    <v-md-editor
      ref="editor"
      v-model="innerValue"
      :height="editorHeight"
      :disabled="disabled"
      :disabled-menus="disabledMenus"
      :left-toolbar="leftToolbar"
      :right-toolbar="rightToolbar"
      :toolbar="mergedToolbar"
      :mode="mode"
      :include-level="includeLevel"
      @upload-image="handleUploadImage"
    />

    <!-- 视频文件选择器 -->
    <input ref="videoInput" class="video-input" type="file" accept="video/*" @change="handleVideoFileChange">

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
     * @param file 图片文件
     * @return Promise
     */
    uploadImage: {
      type: Function,
      default: null
    },
    /**
     * 视频上传方法
     *
     * @param file 视频文件
     * @return Promise
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
      default: 'undo redo clear | h bold italic strikethrough quote tip | ul ol todo-list table hr | link image video code align | emoji'
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
     *
     * 可以覆盖组件默认的自定义工具栏配置。
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
      /**
       * 是否全屏
       */
      fullscreen: false,
      /**
       * 全屏高度
       */
      fullscreenHeight: 0,
      /**
       * 当前等待插入视频的编辑器实例
       */
      videoEditor: null
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
    },
    /**
     * 最终工具栏配置
     *
     * 保留外部 toolbar 配置，同时提供：
     *
     * video：视频上传
     * align：内容对齐
     */
    mergedToolbar() {
      const defaultToolbar = {
        video: {
          title: '视频',
          icon: 'v-md-icon-tip',
          action: editor => {
            this.openVideoSelector(editor)
          }
        }
      }

      return Object.assign({}, defaultToolbar, this.toolbar)
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
     *
     * @param height 高度
     * @return String ${num}px
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
     * v-md-editor 1.7.12：
     * insertImage({
     *   url,
     *   desc
     * })
     *
     * @param event 文件选择事件
     * @param insertImage 图片插入方法
     * @param files 文件列表
     */
    async handleUploadImage(event, insertImage, files) {
      const fileList = files || this.getFiles(event)
      if (!fileList || !fileList.length) {
        return
      }

      // 当前组件一次只处理一个图片
      const file = fileList[0]
      if (!this.uploadImage) {
        this.$emit('upload-image', file)
        return
      }

      try {
        this.$emit('upload-start', {
          type: 'image',
          file
        })

        const result = await this.uploadImage(file)
        const url = this.resolveUploadUrl(result)

        if (!url) {
          this.$emit('upload-error', {
            type: 'image',
            file,
            error: new Error('图片上传成功，但没有获取到图片地址')
          })
          return
        }

        insertImage({
          url,
          desc: file.name
        })

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
    },
    /**
     * 打开视频选择器
     *
     * @param editor v-md-editor 编辑器实例
     */
    openVideoSelector(editor) {
      if (!this.uploadVideo) {
        this.$emit('upload-video')
        return
      }

      this.videoEditor = editor

      const input = this.$refs.videoInput

      if (!input) {
        return
      }

      // 允许重复选择同一个文件
      input.value = ''

      input.click()
    },
    /**
     * 视频文件选择完成
     *
     * @param event change 事件
     */
    async handleVideoFileChange(event) {
      const files = event.target.files

      if (!files || !files.length) {
        return
      }

      const file = files[0]
      const editor = this.videoEditor

      if (!editor) {
        this.$emit('upload-error', {
          type: 'video',
          file,
          error: new Error('未获取到 Markdown 编辑器实例')
        })
        return
      }

      try {
        this.$emit('upload-start', {
          type: 'video',
          file
        })

        const result = await this.uploadVideo(file)
        const url = this.resolveUploadUrl(result)

        if (!url) {
          this.$emit('upload-error', {
            type: 'video',
            file,
            error: new Error('视频上传成功，但没有获取到视频地址')
          })
          return
        }

        this.insertVideo(editor, url)

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
      } finally {
        event.target.value = ''
        this.videoEditor = null
      }
    },
    /**
     * 插入视频
     *
     * Markdown 没有标准的视频语法，
     * 使用 HTML video 标签。
     *
     * @param editor v-md-editor 编辑器实例
     * @param url 视频地址
     */
    insertVideo(editor, url) {
      const safeUrl = this.escapeHtmlAttribute(url)
      editor.insert(() => {
        return {
          text: [
            '',
            `<video src="${safeUrl}" controls preload="metadata" style="max-width: 100%;"></video>`,
            ''
          ].join('\n'),
          selected: ''
        }
      })
    },
    /**
     * 获取文件列表
     *
     * @param event 文件选择事件
     * @return Array 文件列表
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
     * 2. { url: 'https://xxx.com/a.png' }
     * 3. { data: { url: 'https://xxx.com/a.png' } }
     * 4. { data: 'https://xxx.com/a.png' }
     *
     * @param result 上传结果
     * @return String 文件地址
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
     * 转义 HTML 属性
     *
     * @param value 属性值
     * @return String 转义后的属性值
     */
    escapeHtmlAttribute(value) {
      return String(value)
        .replace(/&/g, '&amp;')
        .replace(/"/g, '&quot;')
        .replace(/</g, '&lt;')
        .replace(/>/g, '&gt;')
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
     *
     * @return Markdown 内容
     */
    getValue() {
      return this.innerValue
    },
    /**
     * 设置 Markdown 内容
     *
     * @param value Markdown 内容
     */
    setValue(value) {
      this.innerValue = value || ''
    },
    /**
     * 获取编辑器实例
     *
     * @return HTMLElement 编辑器实例
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

  /**
   * 隐藏视频文件选择器
   */
  .video-input {
    display: none;
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

/**
 * 全屏时禁止页面滚动
 */
.muses-markdown-editor-fullscreen {
  overflow: hidden !important;
}

/**
 * Markdown 内容对齐
 *
 * 注意：
 * 这些样式需要同时放到博客前台 Markdown 内容容器中，
 * 因为文章最终是在前台页面渲染。
 */
.md-align-left {
  text-align: left;
}

.md-align-center {
  text-align: center;
}

.md-align-right {
  text-align: right;
}

.md-align-justify {
  text-align: justify;
}
</style>
