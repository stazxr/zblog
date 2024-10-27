const getters = {
  user: state => state.user.user,
  perms: state => state.user.user == null ? [] : state.user.user['perms'] == null ? [] : state.user.user['perms'],
  loadMenus: state => state.user.loadMenus,
  webSshApi: state => state.api.webSshApi
}
export default getters
