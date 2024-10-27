import Vue from 'vue'
import VueRouter from 'vue-router'

Vue.use(VueRouter)

const routes = [
  {
    path: '/',
    name: 'Home',
    component: resolve => require(['../views/home/index'], resolve)
  },
  {
    path: '/articles/:articleId',
    component: resolve => require(['../views/article/article'], resolve)
  },
  {
    path: '/article404',
    component: resolve => require(['../views/article/articleNotFound'], resolve)
  },
  {
    path: '/archives',
    component: resolve => require(['../views/archive/index'], resolve),
    meta: {
      title: '归档'
    }
  },
  {
    path: '/tags',
    component: resolve => require(['../views/tag/index'], resolve),
    meta: {
      title: '标签'
    }
  },
  {
    path: '/tags/:tagId',
    component: resolve => require(['../views/article/articleList'], resolve)
  },
  {
    path: '/categories',
    component: resolve => require(['../views/category/index'], resolve),
    meta: {
      title: '分类'
    }
  },
  {
    path: '/categories/:categoryId',
    component: resolve => require(['../views/article/articleList'], resolve)
  },
  {
    path: '/albums',
    component: resolve => require(['../views/album/index'], resolve),
    meta: {
      title: '相册列表'
    }
  },
  {
    path: '/albums/:albumId',
    component: resolve => require(['../views/album/photo'], resolve),
    meta: {
      title: '相册'
    }
  },
  {
    path: '/talks',
    component: resolve => require(['../views/talk/index'], resolve),
    meta: {
      title: '说说列表'
    }
  },
  {
    path: '/talks/:talkId',
    component: resolve => require(['../views/talk/detail'], resolve),
    meta: {
      title: '说说'
    }
  },
  {
    path: '/columns',
    component: resolve => require(['../views/column/index'], resolve),
    meta: {
      title: '专栏列表'
    }
  },
  {
    path: '/columns/:columnId',
    component: resolve => require(['../views/column/detail'], resolve),
    meta: {
      title: '专栏'
    }
  },
  {
    path: '/links',
    component: resolve => require(['../views/link/index'], resolve),
    meta: {
      title: '友链列表'
    }
  },
  {
    path: '/message',
    component: resolve => require(['../views/message/index'], resolve),
    meta: {
      title: '弹幕'
    }
  },
  {
    path: '/statistics',
    component: resolve => require(['../views/statistics/index'], resolve),
    meta: {
      title: '统计'
    }
  },
  {
    path: '/versions',
    component: resolve => require(['../views/versions/index'], resolve),
    meta: {
      title: '版本'
    }
  },
  {
    path: '/user',
    component: resolve => require(['../views/user/index'], resolve),
    meta: {
      title: '个人中心'
    }
  },
  {
    path: '/404',
    component: (resolve) => require(['../views/features/404'], resolve),
    hidden: true
  },
  {
    path: '/oauth/login',
    component: resolve => require(['../components/OauthLogin.vue'], resolve)
  },
  {
    path: '*',
    redirect: '/404'
  }
]

const router = new VueRouter({
  mode: 'history',
  base: process.env.BASE_URL,
  routes,
  scrollBehavior(to, from, savedPosition) {
    return { x: 0, y: 0 }
  }
})

export default router
