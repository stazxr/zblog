import store from '@/store'

export default function(Vue) {
  Vue.directive('perm', {
    inserted(el, binding) {
      const { value } = binding
      let hasPermission = false
      if (value && typeof (value) === 'string' && value.length > 0) {
        const userPerms = store.getters && store.getters.perms
        hasPermission = userPerms.includes(value)
      } else if (value && value instanceof Array && value.length > 0) {
        const userPerms = store.getters && store.getters.perms
        const needPerms = value

        // 必须有所有的权限才可以
        hasPermission = true
        for (let i = 0; i < needPerms.length; i++) {
          const needPerm = needPerms[i]
          if (!userPerms.includes(needPerm)) {
            hasPermission = false
            break
          }
        }

        // 有一个权限就可以
        // hasPermission = userPerms.some(permCode => {
        //   return needPerms.includes(permCode)
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
