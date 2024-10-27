import Vue from 'vue'
import VueRouter from 'vue-router'
import Layout from '../layout/index'

Vue.use(VueRouter)

// 默认路由
export const defaultRouterMap = [
  {
    path: '/login',
    meta: { title: '登录', noCache: true },
    name: 'Login',
    component: (resolve) => require(['@/views/login'], resolve),
    hidden: true
  },
  {
    path: '/403',
    component: (resolve) => require(['@/views/features/403'], resolve),
    hidden: true
  },
  {
    path: '/404',
    component: (resolve) => require(['@/views/features/404'], resolve),
    hidden: true
  },
  {
    path: '/redirect',
    component: Layout,
    hidden: true,
    children: [
      {
        path: '/redirect/:path*',
        component: (resolve) => require(['@/views/features/redirect'], resolve)
      }
    ]
  },
  {
    path: '/forceUpdatePass',
    component: (resolve) => require(['@/views/features/forceUpdatePass'], resolve),
    meta: { title: '修改密码', noCache: true },
    hidden: true
  },
  {
    path: '/',
    component: Layout,
    redirect: '/dashboard',
    children: [
      {
        path: 'dashboard',
        component: (resolve) => require(['@/views/admin/home'], resolve),
        name: 'Dashboard',
        meta: { title: '首页', icon: 'home', affix: true, noCache: true }
      }
    ]
  },
  {
    path: '/user',
    component: Layout,
    hidden: true,
    redirect: 'noredirect',
    children: [
      {
        path: 'center',
        component: (resolve) => require(['@/views/admin/components/userCenter'], resolve),
        name: 'UserCenter',
        meta: { title: '个人中心' }
      },
      {
        path: 'search',
        component: (resolve) => require(['@/views/admin/components/userSearch'], resolve),
        name: 'UserSearch',
        meta: { title: '用户查询', hideTag: true }
      }
    ]
  },
  {
    path: '/publish',
    component: Layout,
    hidden: true,
    redirect: 'noredirect',
    children: [
      {
        path: 'blog/success/:articleId',
        component: (resolve) => require(['@/views/admin/web/article/template/publishArticleSuccess'], resolve),
        name: 'PublishSuccess',
        meta: { title: '发布成功' }
      }
    ]
  },
  {
    path: '/xterm',
    meta: { title: 'WEB_SSH', noCache: true },
    name: 'XtermPage',
    component: (resolve) => require(['@/views/admin/mnt/node/xterm'], resolve),
    hidden: true
  }
]

export default new VueRouter({
  mode: 'hash',
  routes: defaultRouterMap,
  scrollBehavior(to, from, savedPosition) {
    if (savedPosition && to.meta.keepAlive) {
      return savedPosition
    } else {
      return { x: 0, y: 0 }
    }
  }
})
