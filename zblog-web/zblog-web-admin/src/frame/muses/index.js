import MusesSearchForm from './components/search-form/index.js'
import MusesSearchFormItem from './components/search-form-item/index.js'
import MusesPagination from './components/pagination/index.js'

const components = [
  MusesSearchForm, MusesSearchFormItem,
  MusesPagination
]

const install = function(vue) {
  components.map(component => {
    vue.component(component.name, component)
  })

  if (typeof window !== 'undefined' && window.Vue) {
    install(window.Vue)
  }
}

export default {
  install,
  MusesSearchForm,
  MusesSearchFormItem,
  MusesPagination
}
