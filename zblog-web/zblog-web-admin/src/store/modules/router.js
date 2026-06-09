import { defaultRouterMap } from '@/router/routers'

const router = {
  state: {
    // 全部路由
    routers: defaultRouterMap,
    // 动态路由
    addRouters: [],
    // 左侧菜单路由
    sidebarRouters: [],
    // 是否已加载路由
    routeLoaded: false
  },
  mutations: {
    /**
     * 设置动态路由
     */
    SET_ROUTERS: (state, routers) => {
      state.addRouters = routers || []
      state.routers = [
        ...defaultRouterMap,
        ...state.addRouters
      ]
    },
    /**
     * 设置侧边栏路由
     */
    SET_SIDEBAR_ROUTERS: (state, routers) => {
      state.sidebarRouters = [
        ...defaultRouterMap,
        ...(routers || [])
      ]
    },
    SET_ROUTE_LOADED: (state, loaded) => {
      state.routeLoaded = loaded
    },
    /**
     * 重置路由状态
     */
    RESET_ROUTERS: (state) => {
      state.routers = defaultRouterMap
      state.addRouters = []
      state.sidebarRouters = []
      state.routeLoaded = false
    }
  }
}

export default router
