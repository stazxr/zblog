import router from './routers'

router.beforeEach((to, from, next) => {
  to.meta['title'] && (document.title = to.meta['title'])
  next()
})

router.afterEach((to, from) => {
  window.scrollTo({
    top: 0,
    behavior: 'instant'
  })
})
