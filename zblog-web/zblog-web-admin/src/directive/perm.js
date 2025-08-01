import store from '@/store'
import CryptoJS from 'crypto-js'

export default function(Vue) {
  Vue.directive('perm', {
    inserted(el, binding) {
      const { value } = binding
      let hasPermission = false
      if (value && typeof (value) === 'string' && value.length > 0) {
        const md5Value = CryptoJS.MD5(value).toString(CryptoJS.enc.Hex)
        const userPerms = store.getters && store.getters.perms
        hasPermission = userPerms.includes(md5Value)
      } else if (value && value instanceof Array && value.length > 0) {
        const userPerms = store.getters && store.getters.perms
        const needPerms = value

        // 必须有所有的权限才可以
        hasPermission = true
        for (let i = 0; i < needPerms.length; i++) {
          const needPerm = needPerms[i]
          const md5Value = CryptoJS.MD5(needPerm).toString(CryptoJS.enc.Hex)
          if (!userPerms.includes(md5Value)) {
            hasPermission = false
            break
          }
        }

        // 有一个权限就可以
        // hasPermission = userPerms.some(permCode => {
        //   const md5Value = CryptoJS.MD5(permCode).toString(CryptoJS.enc.Hex)
        //   return needPerms.includes(md5Value)
        // })
      } else {
        console.error(`error! use Like v-perm="'permCode'" or v-perm="['permCode1','permCode2']"`)
      }

      // hidden
      if (!hasPermission) {
        el.parentNode && el.parentNode.removeChild(el)
      }
    }
  })
}
