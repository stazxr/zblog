// 遍历后台传来的路由字符串，转换为组件对象
import Layout from '@/layout'
import ParentView from '@/components/ParentView'

export function filterAsyncRouter(routers, parent = null, useFilter = false) {
  return routers.map(router => {
    const _router = { ...router } // 避免污染原对象

    /** 预处理 children（仅在 useFilter 时） */
    if (useFilter && Array.isArray(_router.children)) {
      _router.children = filterChildren(_router.children)
    }

    /** 设置 component */
    if (_router.component) {
      switch (_router.component) {
        case 'Layout':
          _router.component = Layout
          break
        case 'ParentView':
          _router.component = ParentView
          break
        default:
          _router.component = loadView(_router.component)
      }
    }

    /** 递归处理 children */
    if (Array.isArray(_router.children) && _router.children.length > 0) {
      _router.children = filterAsyncRouter(_router.children, _router, useFilter)
    } else {
      delete _router.children
      delete _router.redirect
    }

    return _router
  })
}

function filterChildren(childrenMap, parent = null) {
  const result = []
  childrenMap.forEach(child => {
    const item = { ...child }

    // 父级 path 合并
    if (parent && parent.path && item.path) {
      item.path = `${parent.path}/${item.path}`
    }

    // 如果是 ParentView，则它的 children 挂到上层
    if (item.component === 'ParentView' && Array.isArray(item.children)) {
      item.children.forEach(grand => {
        const grandCopy = { ...grand }
        grandCopy.path = `${item.path}/${grandCopy.path}`
        if (grandCopy.children) {
          result.push(...filterChildren(grandCopy.children, grandCopy))
        } else {
          result.push(grandCopy)
        }
      })
    } else {
      result.push(item)
    }
  })

  // 返回结果
  return result
}

/** 动态加载 view */
function loadView(view) {
  return resolve => require([`@/views/${view}`], resolve)
}
