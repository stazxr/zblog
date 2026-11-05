import MusesMarkdownEditor from './muses-markdown-editor.vue'
import MusesMarkdownPreview from './muses-markdown-preview.vue'

// Markdown 编辑器
MusesMarkdownEditor.install = Vue => Vue.component(MusesMarkdownEditor.name, MusesMarkdownEditor)

// Markdown 预览组件
MusesMarkdownPreview.install = Vue => Vue.component(MusesMarkdownPreview.name, MusesMarkdownPreview)

export {
  MusesMarkdownEditor,
  MusesMarkdownPreview
}

