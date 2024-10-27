// 前端防多次点击，重复提交 use: v-preventReClick || v-preventReClick = '1000'
export default function(Vue) {
  Vue.directive('preventReClick', {
    inserted(el, binding) {
      el.addEventListener('click', () => {
        if (!el['disabled']) {
          el['disabled'] = true
          setTimeout(() => {
            el['disabled'] = false
          }, binding.value || 3000)
        }
      })
    }
  })
}
