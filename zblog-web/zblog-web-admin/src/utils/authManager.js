import store from '@/store'
import router from '@/router/routers'

export function clearStoreLoginInfo() {
  store.commit('SET_USER', null)
  store.commit('RESET_ROUTERS')
}

export function gotoLogin() {
  clearStoreLoginInfo()

  router.replace({
    path: '/login',
    query: {
      redirect: router.currentRoute.fullPath
    }
  })
}
