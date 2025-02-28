import Vue from 'vue'
import Router from 'vue-router'

Vue.use(Router)

import Layout from '@/layout'

export const constantRoutes = [
  {
    path: '/login',
    component: () => import('@/views/login/index'),
    hidden: true
  },

  {
    path: '/404',
    component: () => import('@/views/404'),
    hidden: true
  },

  {
    path: '/add-user',
    component: () => import('@/views/User/AddUser.vue')
  },

  {
    path: '/log-detail',
    name: 'LogDetail',
    component: () => import('@/views/ThirdPartyApiLog/LogDetail.vue'),
    props: route => ({ id: route.query.id }) 
  },

  {
    path: '/add-log',
    component: () => import('@/views/ThirdPartyApiLog/AddThirdPartyApiLog.vue')
  },

  {
    path: '/add-notification',
    component: () => import('@/views/Notification/AddNotification.vue')
  },

  {
    path: '/add-payment-log',
    component: () => import('@/views/Payment/AddPaymentLog.vue')
  },
  
  {
    path: '/payment-log-detail/:id', 
    name: 'PaymentLogDetail',
    component: () => import('@/views/Payment/LogDetail.vue')
  },

  {
    path: '/edit-user',
    component: () => import('@/views/User/EditUser.vue')
  },

  {
    path: '/edit-notification',
    component: () => import('@/views/Notification/EditNotification.vue'),
    props: route => ({ id: route.query.id }) 
  },

  {
    path: '/edit-SignLanguageTranslationLog',
    name: 'LogDetail',
    component: () => import('@/views/SignLanguageTranslationLog/EditSignLanguageTranslationLog.vue'),
    props: route => ({ id: route.query.id }) 
  },

  {
    path: '/add-SignLanguageTranslationlog',
    component: () => import('@/views/SignLanguageTranslationLog/AddSignLanguageTranslationLog.vue')
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
    path: '/example',
    component: Layout,
    redirect: '/example/table',
    name: 'Example',
    meta: { title: '案例', icon: 'el-icon-s-help' },
    children: [
      {
        path: 'table',
        name: 'Table',
        component: () => import('@/views/table/index'),
        meta: { title: '表格', icon: 'table' }
      },
      {
        path: 'tree',
        name: 'Tree',
        component: () => import('@/views/tree/index'),
        meta: { title: '树状表', icon: 'tree' }
      }
    ]
  },

  {
    path: '/User',
    component: Layout,
    children: [
      {
        path: 'index',
        name: 'User',
        component: () => import('@/views/User/index'),
        meta: { title: '用户管理', icon: 'user' }
      }
    ]
  },

  {
    path: '/Forum',
    component: Layout,
    redirect: '/Forum',
    name: 'Forum',
    meta: { title: '论坛管理', icon: 'el-icon-s-help' },
    children: [
      {
        path: 'ForumPost',
        name: 'ForumPost',
        component: () => import('@/views/ForumPost/index'),
        meta: { title: '论坛帖子管理', icon: 'table' }
      },
      {
        path: 'ForumComment',
        name: 'ForumComment',
        component: () => import('@/views/ForumComment/index'),
        meta: { title: '论坛评论管理', icon: 'table' }
      },
      {
        path: 'ForumCategory',
        name: 'ForumCategory',
        component: () => import('@/views/ForumCategory/index'),
        meta: { title: '论坛分类管理', icon: 'table' }
      }
    ]
  },

  {
    path: '/ThirdPartyApiLog',
    component: Layout,
    children: [
      {
        path: 'index',
        name: 'ThirdPartyApiLog',
        component: () => import('@/views/ThirdPartyApiLog/index'),
        meta: { title: '第三方api管理', icon: 'form' }
      }
    ]
  },

  {
    path: '/SignLanguageTranslationLog',
    component: Layout,
    children: [
      {
        path: 'index',
        name: 'SignLanguageTranslationLog',
        component: () => import('@/views/SignLanguageTranslationLog/index'),
        meta: { title: '手语翻译管理', icon: 'form' }
      }
    ]
  },

  {
    path: '/Payment',
    component: Layout,
    children: [
      {
        path: 'index',
        name: 'Payment',
        component: () => import('@/views/Payment/index'),
        meta: { title: '支付管理', icon: 'form' }
      }
    ]
  },

  {
    path: '/OperationLog',
    component: Layout,
    children: [
      {
        path: 'index',
        name: 'OperationLog',
        component: () => import('@/views/OperationLog/index'),
        meta: { title: '操作记录管理', icon: 'form' }
      }
    ]
  },

  {
    path: '/Notification',
    component: Layout,
    children: [
      {
        path: 'index',
        name: 'Notification',
        component: () => import('@/views/Notification/index'),
        meta: { title: '通知管理', icon: 'form' }
      }
    ]
  },

  {
    path: '/Message',
    component: Layout,
    children: [
      {
        path: 'index',
        name: 'Message',
        component: () => import('@/views/Message/index'),
        meta: { title: '消息管理', icon: 'form' }
      }
    ]
  },

  {
    path: '/LocationShare',
    component: Layout,
    children: [
      {
        path: 'index',
        name: 'LocationShare',
        component: () => import('@/views/LocationShare/index'),
        meta: { title: '位置共享管理', icon: 'form' }
      }
    ]
  },

  {
    path: '/FriendRelationship',
    component: Layout,
    children: [
      {
        path: 'index',
        name: 'FriendRelationship',
        component: () => import('@/views/FriendRelationship/index'),
        meta: { title: '好友关系管理', icon: 'form' }
      }
    ]
  },

  {
    path: '/Facility',
    component: Layout,
    children: [
      {
        path: 'index',
        name: 'Facility',
        component: () => import('@/views/Facility/index'),
        meta: { title: '设施管理', icon: 'form' }
      }
    ]
  },

  {
    path: '/EmergencyRequest',
    component: Layout,
    children: [
      {
        path: 'index',
        name: 'EmergencyRequest',
        component: () => import('@/views/EmergencyRequest/index'),
        meta: { title: '紧急求助管理', icon: 'form' }
      }
    ]
  },

  {
    path: '/EmergencyContact',
    component: Layout,
    children: [
      {
        path: 'index',
        name: 'EmergencyContact',
        component: () => import('@/views/EmergencyContact/index'),
        meta: { title: '紧急联系人管理', icon: 'form' }
      }
    ]
  },

  {
    path: '/form',
    component: Layout,
    children: [
      {
        path: 'index',
        name: 'Form',
        component: () => import('@/views/form/index'),
        meta: { title: '表单', icon: 'form' }
      }
    ]
  },

  // 404 page must be placed at the end !!!
  { path: '*', redirect: '/404', hidden: true }
]

const createRouter = () => new Router({
  // mode: 'history', // require service support
  scrollBehavior: () => ({ y: 0 }),
  routes: constantRoutes
})

const router = createRouter()

// Detail see: https://github.com/vuejs/vue-router/issues/1234#issuecomment-357941465
export function resetRouter() {
  const newRouter = createRouter()
  router.matcher = newRouter.matcher // reset router
}

export default router
