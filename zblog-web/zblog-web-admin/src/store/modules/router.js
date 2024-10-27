import { defaultRouterMap } from '@/router/routers'

const permission = {
  state: {
    routers: defaultRouterMap,
    addRouters: [],
    sidebarRouters: []
  },
  mutations: {
    SET_ROUTERS: (state, routers) => {
      state.addRouters = routers
      state.routers = defaultRouterMap.concat(routers)
    },
    SET_SIDEBAR_ROUTERS: (state, routers) => {
      state.sidebarRouters = defaultRouterMap.concat(routers)
    }
  },
  actions: {
    GenerateRoutes({ commit }, asyncRouter) {
      commit('SET_ROUTERS', asyncRouter)
    },
    SetSidebarRouters({ commit }, sidebarRouter) {
      commit('SET_SIDEBAR_ROUTERS', sidebarRouter)
    }
  }
}

export default permission
