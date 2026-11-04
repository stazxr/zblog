<template>
  <div class="muses-markdown-editor" :class="{ 'is-fullscreen': fullscreen }">
    <div ref="editorContainer">
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
        :default-fullscreen="false"
        @upload-image="handleUploadImage"
        @fullscreen-change="handleFullscreenChange"
      />
    </div>

    <!-- 视频选择器 -->
    <input ref="videoInput" class="video-input" type="file" accept="video/*" @change="handleUploadVideo">

    <!-- 附件选择器 -->
    <input ref="attachmentInput" class="attachment-input" type="file" @change="handleUploadAttachment">

    <div v-if="fullscreen" class="fullscreen-mask" />
  </div>
</template>

<script>
import CodeBlockEnhancer from '@/frame/muses/plugins/code-block-enhancer'
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
    // 图片上传方法
    uploadImage: {
      type: Function,
      default: null
    },
    // 视频上传方法
    uploadVideo: {
      type: Function,
      default: null
    },
    // 附件上传方法
    uploadAttachment: {
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
      default: 'h quote | bold underline italic strikethrough inlinecode | ul ol todo-list align indent | tip emoji link image video attachment table code hr | undo redo clear'
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
      fullscreen: false, // 是否全屏
      fullscreenHeight: 0, // 全屏高度
      videoEditor: null, // 当前等待插入视频的编辑器实例
      attachmentEditor: null, // 当前等待插入附件的编辑器实例
      codeBlockEnhancer: null
    }
  },
  computed: {
    // 编辑器最终高度
    editorHeight() {
      if (!this.fullscreen) {
        return this.formatHeight(this.height)
      }

      return `${this.fullscreenHeight}px`
    },
    // 最终工具栏配置
    mergedToolbar() {
      const defaultToolbar = {
        underline: {
          title: '下划线',
          icon: 'iconfont icon-underline',
          action: editor => {
            this.insertUnderline(editor)
          }
        },
        inlinecode: {
          title: '行内代码',
          icon: 'iconfont icon-inlinecode',
          action: editor => {
            this.insertInlineCode(editor)
          }
        },
        align: {
          title: '对齐',
          icon: 'iconfont icon-align',
          menus: [
            {
              name: 'left',
              text: '左对齐',
              action: editor => {
                this.insertLeftAlign(editor)
              }
            },
            {
              name: 'right',
              text: '右对齐',
              action: editor => {
                this.insertRightAlign(editor)
              }
            },
            {
              name: 'center',
              text: '居中',
              action: editor => {
                this.insertCenterAlign(editor)
              }
            }
          ]
        },
        indent: {
          title: '增加缩进',
          icon: 'iconfont icon-indent',
          action: editor => {
            this.insertIndent(editor)
          }
        },
        video: {
          title: '插入视频',
          icon: 'iconfont icon-video',
          action: editor => {
            this.insertVideo(editor)
          }
        },
        attachment: {
          title: '插入附件',
          icon: 'iconfont icon-attachment',
          action: editor => {
            this.insertAttach(editor)
          }
        }
      }

      return Object.assign({}, defaultToolbar, this.toolbar)
    }
  },
  watch: {
    // 同步外部内容
    value(value) {
      if (value !== this.innerValue) {
        this.innerValue = value
      }
    },
    // 向外同步 Markdown
    innerValue(value) {
      this.$emit('input', value)
      this.$emit('change', value)
    }
  },
  mounted() {
    this.bindFullscreenEvent()
    this.$nextTick(() => {
      this.codeBlockEnhancer = new CodeBlockEnhancer(
        this.$refs.editorContainer
      )
      this.codeBlockEnhancer.init()
    })
  },
  beforeDestroy() {
    this.unbindFullscreenEvent()
    this.exitFullscreen()
    if (this.codeBlockEnhancer) {
      this.codeBlockEnhancer.destroy()
      this.codeBlockEnhancer = null
    }
  },
  methods: {
    // 格式化编辑器高度
    formatHeight(height) {
      if (typeof height === 'number') {
        return `${height}px`
      }

      if (/^\d+$/.test(height)) {
        return `${height}px`
      }

      return height
    },
    // 下划线
    insertUnderline(editor) {
      editor.insert((selected) => {
        const content = selected || '请输入文本'
        return {
          text: `<u>${content}</u>`,
          selected: content
        }
      })
    },
    // 行内代码
    insertInlineCode(editor) {
      editor.insert((selected) => {
        const content = selected || '请输入文本'
        return {
          text: `\`${content}\``,
          selected: content
        }
      })
    },
    // 向左对齐
    insertLeftAlign(editor) {
      editor.insert((selected) => {
        const content = selected || '请输入文本'
        return {
          text: `::: align-left\n${content}\n:::`,
          selected: content
        }
      })
    },
    // 向右对齐
    insertRightAlign(editor) {
      editor.insert((selected) => {
        const content = selected || '请输入文本'
        return {
          text: `::: align-right\n${content}\n:::`,
          selected: content
        }
      })
    },
    // 居中对齐
    insertCenterAlign(editor) {
      editor.insert((selected) => {
        const content = selected || '请输入文本'
        return {
          text: `::: align-center\n${content}\n:::`,
          selected: content
        }
      })
    },
    // 增加缩进
    insertIndent(editor) {
      editor.insert((selected) => {
        const content = selected || '请输入文本'
        return {
          text: `::: indent\n${content}\n:::\n`,
          selected: content
        }
      })
    },
    // 插入视频
    insertVideo(editor) {
      if (!this.uploadVideo) {
        this.$emit('upload-video')
        return
      }

      this.videoEditor = editor
      const input = this.$refs.videoInput
      if (!input) {
        return
      }

      input.value = ''
      input.click()
    },
    // 插入附件
    insertAttach(editor) {
      if (!this.uploadAttachment) {
        this.$emit('upload-attachment')
        return
      }

      this.attachmentEditor = editor
      const input = this.$refs.attachmentInput
      if (!input) {
        return
      }

      input.value = ''
      input.click()
    },

    // 图片上传
    async handleUploadImage(event, insertImage, files) {
      const type = 'image'
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
        this.$emit('upload-start', { type: type, file })
        const result = await this.uploadImage(file)
        const url = this.resolveUploadUrl(result)
        if (!url) {
          this.$emit('upload-error', { type: type, file, error: new Error('图片上传成功，但没有获取到图片地址') })
          return
        }

        const filename = this.resolveUploadFilename(file, result)
        insertImage({ url, desc: filename })
        this.$emit('upload-success', { type: type, file, result })
      } catch (error) {
        this.$emit('upload-error', { type: type, file, error })
      }
    },
    // 视频上传
    async handleUploadVideo(event) {
      const type = 'video'
      const file = event.target.files && event.target.files[0]
      if (!file) {
        return
      }

      const editor = this.videoEditor
      if (!editor) {
        this.$emit('upload-error', { type: type, file, error: new Error('未获取到 Markdown 编辑器实例') })
        return
      }

      try {
        this.$emit('upload-start', { type: type, file })
        const result = await this.uploadVideo(file)
        const url = this.resolveUploadUrl(result)
        if (!url) {
          this.$emit('upload-error', { type: type, file, error: new Error('视频上传成功，但没有获取到视频地址') })
          return
        }

        await this.doInsertVideo(editor, url)
        this.$emit('upload-success', { type: type, file, result })
      } catch (error) {
        this.$emit('upload-error', { type: type, file, error })
      } finally {
        event.target.value = ''
        this.videoEditor = null
      }
    },
    // 附件上传
    async handleUploadAttachment(event) {
      const type = 'attachment'
      const file = event.target.files && event.target.files[0]
      if (!file) {
        return
      }

      const editor = this.attachmentEditor
      if (!editor) {
        this.$emit('upload-error', { type: type, file, error: new Error('未获取到 Markdown 编辑器实例') })
        return
      }

      try {
        this.$emit('upload-start', { type: type, file })
        const result = await this.uploadAttachment(file)
        const url = this.resolveUploadUrl(result)
        if (!url) {
          this.$emit('upload-error', { type: type, file, error: new Error('附件上传成功，但没有获取到附件地址') })
          return
        }

        const filename = this.resolveUploadFilename(file, result)
        await this.doInsertAttachment(editor, filename, url)
        this.$emit('upload-success', { type: type, file, result })
      } catch (error) {
        this.$emit('upload-error', { type: type, file, error })
      } finally {
        event.target.value = ''
        this.attachmentEditor = null
      }
    },
    // 插入视频
    async doInsertVideo(editor, url) {
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
    // 插入附件
    async doInsertAttachment(editor, filename, url) {
      const safeUrl = this.escapeHtmlAttribute(url)
      const safeFilename = this.escapeHtmlAttribute(filename)
      editor.insert(() => {
        return {
          text: [
            '',
            `[下载附件：${safeFilename}](${safeUrl})`,
            ''
          ].join('\n'),
          selected: ''
        }
      })
    },
    // 获取文件列表
    getFiles(event) {
      if (!event || !event.target) {
        return []
      }

      return Array.prototype.slice.call(event.target.files || [])
    },
    // 解析上传文件地址
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
    // 解析上传文件名称
    resolveUploadFilename(file, result) {
      if (result && result.name) {
        return result.name
      }
      if (result && result.data && result.data.name) {
        return result.data.name
      }
      return file.name.replace(/([\\[\]])/g, '\\$1')
    },
    // 转义 HTML 属性
    escapeHtmlAttribute(value) {
      return String(value)
        .replace(/&/g, '&amp;')
        .replace(/"/g, '&quot;')
        .replace(/</g, '&lt;')
        .replace(/>/g, '&gt;')
    },

    // 切换全屏
    handleFullscreenChange() {
      if (this.fullscreen) {
        this.exitFullscreen()
      } else {
        this.enterFullscreen()
      }
      this.focus()
    },
    // 进入全屏
    enterFullscreen() {
      this.fullscreen = true
      this.updateFullscreenHeight()
      document.body.classList.add('muses-markdown-editor-fullscreen')
    },
    // 退出全屏
    exitFullscreen() {
      this.fullscreen = false
      document.body.classList.remove('muses-markdown-editor-fullscreen')
    },

    // 绑定全屏事件
    bindFullscreenEvent() {
      window.addEventListener('resize', this.handleWindowResize)
    },
    // 解绑全屏事件
    unbindFullscreenEvent() {
      window.removeEventListener('resize', this.handleWindowResize)
    },
    // 窗口大小变化
    handleWindowResize() {
      if (this.fullscreen) {
        this.updateFullscreenHeight()
      }
    },
    // 更新全屏高度
    updateFullscreenHeight() {
      this.fullscreenHeight = window.innerHeight
    },

    // 聚焦编辑器
    focus() {
      const editor = this.$refs.editor
      if (editor && editor.$refs && editor.$refs.editor) {
        const target = editor.$refs.editor
        if (target.focus) {
          target.focus()
        }
      }
    },

    // 获取 Markdown 内容
    getValue() {
      return this.innerValue
    },
    // 设置 Markdown 内容
    setValue(value) {
      this.innerValue = value || ''
    },
    // 获取编辑器实例
    getEditor() {
      return this.$refs.editor
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
   * 隐藏视频选择器
   */
  .video-input {
    display: none;
  }

  /**
   * 隐藏附件选择器
   */
  .attachment-input {
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

// 给编辑器和预览区指定相同的字体
.muses-markdown-editor textarea, .muses-markdown-editor .vuepress-markdown-body {
  font-family: menlo,Ubuntu Mono,consolas,Courier New,Microsoft Yahei,Hiragino Sans GB,WenQuanYi Micro Hei,sans-serif !important;
}

/**
 * Markdown 代码块样式
 *
 * 保留 v-md-editor 原有的行号布局，
 * 只优化代码块滚动条和显示效果。
 */

/* 代码块容器 */
.muses-markdown-editor {
  .v-md-editor {
    pre {
      white-space: pre !important;
      overflow-x: auto !important;
      overflow-y: hidden;

      /* Firefox */
      scrollbar-width: thin;
      scrollbar-color: transparent transparent;
    }

    /* Chrome / Edge 滚动条 */
    pre::-webkit-scrollbar {
      height: 6px;
    }

    pre::-webkit-scrollbar-track {
      background: transparent;
    }

    pre::-webkit-scrollbar-thumb {
      background: transparent;
      border-radius: 10px;
    }

    /* 悬停显示滚动条 */
    pre:hover {
      scrollbar-color: #b8b8b8 transparent;
    }

    pre:hover::-webkit-scrollbar-thumb {
      background: #b8b8b8;
    }

    pre::-webkit-scrollbar-thumb:hover {
      background: #888;
    }

    /* 保留代码原始格式 */
    pre code {
      white-space: pre !important;
    }
  }
}
</style>
