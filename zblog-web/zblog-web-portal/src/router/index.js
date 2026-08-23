import router from './routers'
import store from './../store'
import { recordVisitorLog } from '@/utils/visitor'

router.beforeEach((to, from, next) => {
  next()
})

router.afterEach((to, from) => {
  const websiteConfig = store.state.websiteConfig
  const websiteName = websiteConfig.websiteName || ''

  if (to.meta.title) {
    document.title = `${to.meta.title} - ${websiteName}`
  } else {
    document.title = `${websiteName}`
  }

  if (to.fullPath === from.fullPath) {
    return
  }

  // 记录访客日志
  setTimeout(() => {
    recordVisitorLog()
  }, 0)
})
