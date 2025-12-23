import Vue from 'vue'
import VueRouter from 'vue-router'
import Layout from '../layout/index'
// import ParentView from '../components/ParentView/index'

Vue.use(VueRouter)

// 默认路由
export const defaultRouterMap = [
  {
    path: '/login',
    meta: { title: '登录', cache: false },
    name: 'Login',
    component: (resolve) => require(['@/views/login'], resolve),
    show: false
  },
  {
    path: '/403',
    component: (resolve) => require(['@/views/features/403'], resolve),
    show: false
  },
  {
    path: '/404',
    component: (resolve) => require(['@/views/features/404'], resolve),
    show: false
  },
  {
    path: '/redirect',
    component: Layout,
    show: false,
    children: [
      {
        path: '/redirect/:path*',
        component: (resolve) => require(['@/views/features/redirect'], resolve)
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
        component: (resolve) => require(['@/views/admin/home'], resolve),
        name: 'Dashboard',
        meta: { title: '首页', icon: 'icon-home', affix: true, cache: false, breadcrumb: true }
      }
    ]
  },
  {
    path: '/user',
    component: Layout,
    show: false,
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
  }

  /*
  {
    path: '/forceUpdatePass',
    component: (resolve) => require(['@/views/features/forceUpdatePass'], resolve),
    meta: { title: '修改密码', cache: false },
    show: false
  },
  {
    path: '/publish',
    component: Layout,
    show: false,
    redirect: 'noredirect',
    children: [
      {
        path: 'blog/success/:articleId',
        component: (resolve) => require(['@/views/admin/web/article/template/publishArticleSuccess'], resolve),
        name: 'PublishSuccess',
        meta: { title: '发布成功' }
      }
    ]
  },*/
  /*
   {
    path: '/xterm',
    meta: { title: 'WEB_SSH', cache: false },
    name: 'XtermPage',
    component: (resolve) => require(['@/views/admin/mnt/node/xterm'], resolve),
    show: false
  }*/
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
