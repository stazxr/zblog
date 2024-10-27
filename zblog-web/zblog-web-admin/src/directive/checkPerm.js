import store from '@/store'

export default {
  install(Vue) {
    Vue.prototype.checkPerm = (value) => {
      if (value && typeof (value) === 'string' && value.length > 0) {
        const userPerms = store.getters && store.getters.perms
        return userPerms.includes(value)
      } else if (value && value instanceof Array && value.length > 0) {
        const userPerms = store.getters && store.getters.perms
        const needPerms = value
        return userPerms.some(permCode => {
          return needPerms.includes(permCode)
        })
      } else {
        return false
      }
    }
  }
}
