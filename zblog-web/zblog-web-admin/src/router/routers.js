import Vue from 'vue'
import VueRouter from 'vue-router'

// 组件清单
import Layout from '../layout/index'
import _403 from '@/views/features/403'
import _404 from '@/views/features/404'
import _redirect from '@/views/features/redirect'
import Home from '@/views/admin/home'
import Login from '@/views/login'
import UserCenter from '@/views/admin/components/userCenter'
import UserSearch from '@/views/admin/components/userSearch'
import ForceUpdatePass from '@/views/admin/components/forceUpdatePass'
import PublishSuccess from '@/views/admin/content/article/template/publishSuccess'

Vue.use(VueRouter)

// 默认路由
export const defaultRouterMap = [
  {
    path: '/403',
    component: _403,
    show: false
  },
  {
    path: '/404',
    component: _404,
    show: false
  },
  {
    path: '/redirect',
    component: Layout,
    show: false,
    children: [
      {
        path: '/redirect/:path*',
        component: _redirect
      }
    ]
  },
  {
    path: '/',
    component: Layout,
    redirect: '/dashboard',
    show: true,
    alwaysShow: false,
    children: [
      {
        show: true,
        path: 'dashboard',
        component: Home,
        name: 'Dashboard',
        meta: { title: '首页', icon: 'icon-home', affix: true, cache: false, breadcrumb: true }
      }
    ]
  },
  {
    path: '/login',
    meta: { title: '登录', cache: false },
    name: 'Login',
    component: Login,
    show: false
  },
  {
    path: '/user',
    component: Layout,
    show: false,
    redirect: 'noredirect',
    children: [
      {
        path: 'center',
        component: UserCenter,
        name: 'UserCenter',
        meta: { title: '个人中心' }
      },
      {
        path: 'search',
        component: UserSearch,
        name: 'UserSearch',
        meta: { title: '用户查询', hideTag: true }
      }
    ]
  },
  {
    path: '/forceUpdatePass',
    component: ForceUpdatePass,
    meta: { title: '修改密码', cache: false },
    show: false
  },
  {
    name: 'PublishSuccess',
    path: '/article/publish/success/:articleId',
    component: PublishSuccess,
    meta: { title: '发布完成', hideTag: true, cache: false },
    show: false
  }
]

export default new VueRouter({
  mode: 'hash', // "hash" | "history" | "abstract"
  routes: defaultRouterMap,
  scrollBehavior(to, from, savedPosition) {
    if (savedPosition && to.meta.keepAlive) {
      return savedPosition
    } else {
      return { x: 0, y: 0 }
    }
  }
})
