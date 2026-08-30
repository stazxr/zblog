import router from './routers'
import store from './../store'
import { recordVisitorLog } from '@/utils/visitor'

router.beforeEach((to, from, next) => {
  next()
})

router.afterEach((to, from) => {
  const websiteConfig = store.state.websiteConfig
  const websiteTitle = websiteConfig.websiteTitle || ''

  if (to.meta.title) {
    document.title = `${to.meta.title} - ${websiteTitle}`
  } else {
    document.title = `${websiteTitle}`
  }

  if (to.fullPath === from.fullPath) {
    return
  }

  // 记录访客日志
  setTimeout(() => {
    recordVisitorLog()
  }, 0)
})
