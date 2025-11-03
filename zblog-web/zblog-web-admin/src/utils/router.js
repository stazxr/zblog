// 遍历后台传来的路由字符串，转换为组件对象
import Layout from '@/layout'
import ParentView from '@/components/ParentView'

export function filterAsyncRouter(routers, lastRouter = false, type = false) {
  return routers.filter(router => {
    if (type && router.children) {
      router.children = filterChildren(router.children)
    }

    // set component
    if (router.component) {
      if (router.component === 'Layout') {
        router.component = Layout
      } else if (router.component === 'ParentView') {
        router.component = ParentView
      } else {
        router.component = loadView(router.component)
      }
    }

    if (router.children != null && router.children && router.children.length) {
      router.children = filterAsyncRouter(router.children, router, type)
    } else {
      delete router['children']
      delete router['redirect']
    }
    return true
  })
}

function filterChildren(childrenMap, lastRouter = false) {
  let children = []
  childrenMap.forEach((el, index) => {
    if (el.children && el.children.length) {
      if (el.component === 'ParentView') {
        el.children.forEach(c => {
          c.path = el.path + '/' + c.path
          if (c.children && c.children.length) {
            children = children.concat(filterChildren(c.children, c))
            return
          }
          children.push(c)
        })
        return
      }
    }
    if (lastRouter) {
      el.path = lastRouter.path + '/' + el.path
    }
    children = children.concat(el)
  })
  return children
}

function loadView(view) {
  return (resolve) => require([`@/views/${view}`], resolve)
}
