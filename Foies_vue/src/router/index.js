import Vue from 'vue'
import Router from 'vue-router'

Vue.use(Router)

import Layout from '@/layout'

export const constantRoutes = [
  {
    //登录路由
    path: '/login',
    component: () => import('@/views/login/index'),
    hidden: true
  },

  {
    //页面未找到
    path: '/404',
    component: () => import('@/views/404'),
    hidden: true
  },

  {
    path: '/',
    component: Layout,
    redirect: '/dashboard',
    children: [{
      path: 'dashboard',
      name: 'Dashboard',
      component: () => import('@/views/dashboard/index'),
      meta: { title: '首页', icon: 'dashboard' }
    }]
  },

  {
    //管理员信息管理
    path: '/Admininfo',
    component: Layout,
    children: [
      {
        path: 'index',
        name: 'Admininfo',
        component: () => import('@/views/Admininfo/index'),
        meta: { title: '管理员信息', icon: 'user' }
      }
    ]
  },

  {
    path: '/User',
    component: Layout,
    redirect: '/User',
    name: 'User',
    meta: { title: '用户管理', icon: 'user' },
    children: [
      {
        //用户信息管理
        path: 'Userinfo',
        name: 'Userinfo',
        component: () => import('@/views/Userinfo/index'),
        meta: { title: '用户信息', icon: 'user' }
      },
      {
        //用户VIP管理
        path: 'Uservip',
        name: 'Uservip',
        component: () => import('@/views/Uservip/index'),
        meta: { title: '用户VIP', icon: 'user' }
      },
      {
        //用户紧急联系人管理
        path: 'Usercontact',
        name: 'Usercontact',
        component: () => import('@/views/Usercontact/index'),
        meta: { title: '用户紧急联系人', icon: 'user' }
      },
    ]
  },

  {
    //求助管理
    path: '/Helpinfo',
    component: Layout,
    children: [
      {
        path: 'index',
        name: 'Helpinfo',
        component: () => import('@/views/Helpinfo/index'),
        meta: { title: '求助记录', icon: 'form' }
      }
    ]
  },

  {
    path: '/Logs',
    component: Layout,
    redirect: '/Logs',
    name: 'Logs',
    meta: { title: '系统日志', icon: 'form' },
    children: [
      {
        //AI助理日志
        path: 'AIlogs',
        name: 'AIlogs',
        component: () => import('@/views/AIlogs/index'),
        meta: { title: 'AI助理日志', icon: 'form' }
      },
      {
        //导航日志
        path: 'NAVlogs',
        name: 'NAVlogs',
        component: () => import('@/views/NAVlogs/index'),
        meta: { title: '导航日志', icon: 'form' }
      },
    ]
  },

  {
    path: '/Forum',
    component: Layout,
    redirect: '/Forum',
    name: 'Forum',
    meta: { title: '论坛管理', icon: 'form' },
    children: [
      {
        //论坛帖子管理
        path: 'Postinfo',
        name: 'Postinfo',
        component: () => import('@/views/Postinfo/index'),
        meta: { title: '论坛帖子', icon: 'form' }
      },
      {
        //论坛评论管理
        path: 'Commentinfo',
        name: 'Commentinfo',
        component: () => import('@/views/Commentinfo/index'),
        meta: { title: '论坛评论', icon: 'form' }
      },
    ]
  },

  {
    path: '/Notifications & Feedback',
    component: Layout,
    redirect: '/Notifications & Feedback',
    name: 'Notifications & Feedback',
    meta: { title: '通知与反馈', icon: 'form' },
    children: [
      {
        //通知管理
        path: 'Notifications',
        name: 'Notifications',
        component: () => import('@/views/Notifications/index'),
        meta: { title: '通知', icon: 'form' }
      },
      {
        //紧急SOS通知信息
        path: 'SOSinfo',
        name: 'SOSinfo',
        component: () => import('@/views/SOSinfo/index'),
        meta: { title: '紧急SOS通知', icon: 'form' }
      },
      {
        //反馈管理
        path: 'Feedback',
        name: 'Feedback',
        component: () => import('@/views/Feedback/index'),
        meta: { title: '反馈', icon: 'form' }
      },
    ]
  },


  { path: '*', redirect: '/404', hidden: true }
]

const createRouter = () => new Router({
  scrollBehavior: () => ({ y: 0 }),
  routes: constantRoutes
})

const router = createRouter()

export function resetRouter() {
  const newRouter = createRouter()
  router.matcher = newRouter.matcher
}

export default router
