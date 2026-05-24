const getters = {
  // 登录用户信息
  user: state => state.user.user,
  // 用户权限信息
  perms: state => state.user.perms,
  // 用户菜单信息
  menus: state => state.user.menus,
  // 全部路由
  routers: state => state.router.routers,
  // 动态路由
  addRouters: state => state.router.addRouters,
  // 左侧菜单
  sidebarRouters: state => state.router.sidebarRouters,
  // 路由是否已加载
  routeLoaded: state => state.router.routeLoaded
}
export default getters
