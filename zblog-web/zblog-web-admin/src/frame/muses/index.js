import MusesSearchForm from './components/search-form/index.js'
import MusesSearchFormItem from './components/search-form-item/index.js'
import MusesPagination from './components/pagination/index.js'
import MusesEmpty from './components/empty/index.js'
import MusesImageUpload from './components/image-upload/index.js'
import MusesImageCropUpload from './components/image-crop-upload/index.js'
import MusesMarkdownEditor from './components/markdown-editor/index.js'

import installMarkdownEditor from './plugins/markdown-editor'

const components = [
  MusesSearchForm,
  MusesSearchFormItem,
  MusesPagination,
  MusesEmpty,
  MusesImageUpload,
  MusesImageCropUpload,
  MusesMarkdownEditor
]

const install = function(Vue) {
  // 初始化 Markdown 编辑器
  installMarkdownEditor(Vue)

  // 注册 Muses 组件
  components.forEach(component => {
    Vue.component(component.name, component)
  })
}

// 浏览器环境下支持直接通过 <script> 引入
if (typeof window !== 'undefined' && window.Vue) {
  install(window.Vue)
}

export default {
  install,
  MusesSearchForm,
  MusesSearchFormItem,
  MusesPagination,
  MusesEmpty,
  MusesImageUpload,
  MusesImageCropUpload,
  MusesMarkdownEditor
}
