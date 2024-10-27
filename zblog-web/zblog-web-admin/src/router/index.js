import router from './routers'
import store from '@/store'
import Config from '@/settings'
import { Message } from 'element-ui'
import NProgress from 'nprogress'
import 'nprogress/nprogress.css'
import { getToken } from '@/utils/token'
import { filterAsyncRouter } from '@/utils/router'
import perm from '@/api/base/perm'

NProgress.configure({ showSpinner: false })

// no redirect whitelist
const whiteList = ['/login', '/forceUpdatePass']

router.beforeEach((to, from, next) => {
  NProgress.start()
  if (getToken()) {
    // 已登录
    if (to.path === '/login') {
      // 在已登录情况下，访问登录页面跳转到后台管理首页
      next({ path: '/' })
      NProgress.done()
    } else {
      // 在已登录情况下，访问其他页面
      if (store.getters.user === null) {
        store.dispatch('GetUserInfo').then(() => {
          loadMenus(next, to)
        }).catch(() => {
          store.dispatch('Logout').then(() => {
            location.reload()
          })
        })
      } else {
        if (store.getters.loadMenus) {
          loadMenus(next, to)
        } else {
          next()
        }
      }
    }
  } else {
    // 未登录
    if (whiteList.indexOf(to.path) !== -1) {
      // 白名单直接访问
      next()
    } else {
      // 重定向到登录页面
      next(`/login?redirect=${to.fullPath}`)
      NProgress.done()
    }
  }
})

router.afterEach((to, from) => {
  to.meta.title && (document.title = to.meta.title + ' - ' + Config.title)
  NProgress.done()
})

router.onError(error => {
  NProgress.done()
  Message.error(error.message || '系统发生未知错误')
})

export const loadMenus = (next, to) => {
  store.commit('SET_LOAD_MENUS', false)
  perm.buildUserMenus().then(res => {
    const data = res.data
    const sdata = JSON.parse(JSON.stringify(data))
    const rdata = JSON.parse(JSON.stringify(data))
    const sidebarRoutes = filterAsyncRouter(sdata)
    const rewriteRoutes = filterAsyncRouter(rdata, false, true)
    rewriteRoutes.push({ path: '*', redirect: '/404', hidden: true })

    store.dispatch('GenerateRoutes', rewriteRoutes).then(() => {
      router.addRoutes(rewriteRoutes)
      next({ ...to, replace: true })
    })

    store.dispatch('SetSidebarRouters', sidebarRoutes)
  }).catch(e => {
    console.log('Load Menus Error', e)
  }).finally(_ => {
    NProgress.done()
  })
}
