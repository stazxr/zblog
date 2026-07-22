import router from './routers'
import store from '@/store'
import Config from '@/settings'
import { Message } from 'element-ui'
import NProgress from 'nprogress'
import 'nprogress/nprogress.css'
import { filterAsyncRouter } from '@/utils/router'

NProgress.configure({
  showSpinner: false
})

// 白名单
const whiteList = new Set([
  '/login', // 登录
  '/forceUpdatePass' // 修改密码（登录前）
])

function validateRoutes(routes = []) {
  routes.forEach(route => {
    if (!route.path) {
      throw new Error('路由缺少path: ' + JSON.stringify(route))
    }

    if (route.children && route.children.length) {
      validateRoutes(route.children)
    }
  })
}

router.beforeEach(async(to, from, next) => {
  NProgress.start()

  const isLoginRoute = to.path === '/login'
  const isWhiteRoute = whiteList.has(to.path)

  try {
    // 白名单直接放行
    if (isWhiteRoute) {
      if (store.getters.user && isLoginRoute) {
        next('/')
      } else {
        next()
      }
      return
    }

    // 未初始化用户信息
    if (!store.getters.user) {
      // 重新加载用户信息
      await store.dispatch('RefreshUser')
    }

    if (!store.getters.routeLoaded) {
      try {
        const menus = store.getters.menus
        const sidebarRoutes = filterAsyncRouter(JSON.parse(JSON.stringify(menus)))
        const rewriteRoutes = filterAsyncRouter(JSON.parse(JSON.stringify(menus)), false, true)
        validateRoutes(rewriteRoutes)
        rewriteRoutes.push({ path: '*', redirect: '/404', hidden: true })
        store.commit('SET_ROUTERS', rewriteRoutes)
        store.commit('SET_SIDEBAR_ROUTERS', sidebarRoutes)
        store.commit('SET_ROUTE_LOADED', true)
        router.addRoutes(rewriteRoutes)
        next({ ...to, replace: true })
      } catch (e) {
        console.error('Dynamic Route Error:', e)
        Message.error('动态路由加载失败，请检查路由配置')
        await store.dispatch('Logout')
        next('/login')
        NProgress.done()
        return
      }
    }

    next()
  } catch (e) {
    console.error('Route Error:', e)
    await store.dispatch('Logout')
    next(`/login?redirect=${encodeURIComponent(to.fullPath)}`)
  }
})

router.afterEach((to, from) => {
  to.meta.title && (document.title = to.meta.title + ' - ' + Config.title)
  NProgress.done()
})

router.onError((error) => {
  console.error('Router Error:', error)

  const chunkFailed = /Loading chunk .* failed/i.test(error.message)
  const networkError = /Network Error/i.test(error.message)
  const timeoutError = /timeout/i.test(error.message)

  if (chunkFailed) {
    if (sessionStorage.getItem('reloaded')) {
      showError('系统更新失败，请清除缓存后重试', 0, true)
      NProgress.done()
      return
    }
    sessionStorage.setItem('reloaded', '1')
    showError('页面资源加载失败，正在为您刷新...', 2000, false)
    setTimeout(() => {
      window.location.reload()
    }, 800)
    return
  }

  if (networkError) {
    showError('网络异常，请检查网络连接后重试')
    NProgress.done()
    return
  }

  if (timeoutError) {
    showError('页面加载超时，请稍后重试')
    NProgress.done()
    return
  }

  showError('页面加载失败，请稍后重试')
  NProgress.done()
})

function showError(msg, duration = 3000, showClose = true) {
  Message({
    message: msg,
    type: 'error',
    duration,
    showClose: showClose
  })
}
