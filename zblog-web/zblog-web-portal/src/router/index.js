import router from './routers'
import { recordVisitorLog } from '@/utils/visitor'

router.beforeEach((to, from, next) => {
  to.meta['title'] && (document.title = to.meta['title'])
  next()
})

router.afterEach((to, from) => {
  window.scrollTo({
    top: 0,
    behavior: 'instant'
  })

  // 记录访客日志
  recordVisitorLog()
})
