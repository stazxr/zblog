import Vue from 'vue'
import componentLoading from './PageLoading'

const ComLoading = Vue.extend(componentLoading)
const instance = new ComLoading({
  el: document.createElement('div')
})

instance.show = false
const loading = {
  show() {
    instance.show = true
    document.body.appendChild(instance.$el)
  },
  hide() {
    instance.show = false
  }
}

export default {
  install() {
    if (!Vue.$loading) {
      Vue.$loading = loading
    }
    Vue.mixin({
      created() {
        this.$loading = Vue.$loading
      }
    })
  }
}
