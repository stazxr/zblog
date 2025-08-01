import store from '@/store'
import CryptoJS from 'crypto-js'

export default {
  install(Vue) {
    Vue.prototype.checkPerm = (value) => {
      if (value && typeof (value) === 'string' && value.length > 0) {
        const md5Value = CryptoJS.MD5(value).toString(CryptoJS.enc.Hex)
        const userPerms = store.getters && store.getters.perms
        return userPerms.includes(md5Value)
      } else if (value && value instanceof Array && value.length > 0) {
        const userPerms = store.getters && store.getters.perms
        const needPerms = value
        for (let i = 0; i < needPerms.length; i++) {
          const needPerm = needPerms[i]
          const md5Value = CryptoJS.MD5(needPerm).toString(CryptoJS.enc.Hex)
          if (userPerms.includes(md5Value)) {
            return true
          }
        }

        return false
      } else {
        return false
      }
    }
  }
}
