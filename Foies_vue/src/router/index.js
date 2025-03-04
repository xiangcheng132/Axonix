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
    //添加用户
    path: '/add-user',
    component: () => import('@/views/User/AddUser.vue')
  },

  {
    //添加第三方api接口日志
    path: '/add-log',
    component: () => import('@/views/ThirdPartyApiLog/AddThirdPartyApiLog.vue')
  },

  {
    //添加通知
    path: '/add-notification',
    component: () => import('@/views/Notification/AddNotification.vue')
  },

  {
    //添加支付日志
    path: '/add-payment-log',
    component: () => import('@/views/Payment/AddPaymentLog.vue')
  },

  {
    //查看修改用户
    path: '/edit-user',
    component: () => import('@/views/User/EditUser.vue')
  },

  {
    //添加操作记录日志
    path: '/add-operation-log',
    component: () => import('@/views/OperationLog/AddOperationLog.vue')
  },

  {
    //添加手语翻译记录
    path: '/add-SignLanguageTranslationlog',
    component: () => import('@/views/SignLanguageTranslationLog/AddSignLanguageTranslationLog.vue')
  },


  {
    //添加紧急求助记录
    path: '/add-emergency-request',
    component: () => import('@/views/EmergencyRequest/AddEmergencyRequest.vue')
  },

  {
    //添加紧急联系人
    path: '/add-emergency-contact',
    component: () => import('@/views/EmergencyContact/AddEmergencyContact.vue')
  },

  {
    //查看修改支付日志
    path: '/payment-log-detail/:id',
    name: 'PaymentLogDetail',
    component: () => import('@/views/Payment/LogDetail.vue')
  },

  {
    //查看修改第三方api接口日志
    path: '/log-detail',
    name: 'LogDetail',
    component: () => import('@/views/ThirdPartyApiLog/LogDetail.vue'),
    props: route => ({ id: route.query.id })
  },

  {
    //查看修改通知
    path: '/edit-notification',
    component: () => import('@/views/Notification/EditNotification.vue'),
    props: route => ({ id: route.query.id })
  },

  {
    //查看修改手语翻译记录
    path: '/edit-SignLanguageTranslationLog',
    name: 'LogDetail',
    component: () => import('@/views/SignLanguageTranslationLog/EditSignLanguageTranslationLog.vue'),
    props: route => ({ id: route.query.id })
  },

  {
    //查看修改操作记录日志
    path: '/edit-operation-log',
    component: () => import('@/views/OperationLog/EditOperationLog.vue'),
    props: route => ({ id: route.query.id })
  },

  {
    //查看修改消息
    path: '/edit-message',
    component: () => import('@/views/Message/EditMessage.vue'),
    props: route => ({ id: route.query.id })
  },

  {
    //查看修改紧急求助记录
    path: '/edit-emergency-request',
    component: () => import('@/views/EmergencyRequest/EditEmergencyRequest.vue'),
    props: route => ({ id: route.query.id })
  },
  {
    //查看修改紧急求助记录
    path: '/edit-emergency-contact',
    component: () => import('@/views/EmergencyContact/EditEmergencyContact.vue'),
    props: route => ({ id: route.query.id })
  },

  {
    //添加位置信息
    path: '/add-location-share',
    component: () => import('@/views/LocationShare/AddLocationShare.vue'),
    props: route => ({ id: route.query.id }) 
  },

  {
    //查看修改位置信息
    path: '/edit-location-share',
    component: () => import('@/views/LocationShare/EditLocationShare.vue'),
    props: route => ({ id: route.query.id }) 
  },

  {
    //添加好友关系信息
    path: '/add-friend-relationship',
    component: () => import('@/views/FriendRelationship/AddFriendRelationship.vue'),
    props: route => ({ id: route.query.id }) 
  },

  {
    //添加帖子
    path: '/add-forum-post',
    component: () => import('@/views/ForumPost/AddForumPost.vue'),
    props: route => ({ id: route.query.id }) 
  },
  
  {
    //查看修改好友关系信息
    path: '/edit-friend-relationship',
    component: () => import('@/views/FriendRelationship/EditFriendRelationship.vue'),
    props: route => ({ id: route.query.id }) 
  },

  {
    //添加设施信息
    path: '/add-facility',
    component: () => import('@/views/Facility/AddFacility.vue'),
    props: route => ({ id: route.query.id }) 
  },

  {
    //查看修改设施信息
    path: '/edit-facility',
    component: () => import('@/views/Facility/EditFacility.vue'),
    props: route => ({ id: route.query.id }) 
  },

  {
<<<<<<< Updated upstream
=======
<<<<<<< HEAD
    //查看修改帖子和评论
    path: '/edit-forum-post/:id',
    name:'ForumPostDetail',
    component: () => import('@/views/ForumPost/EditForumPost.vue'),
=======
>>>>>>> Stashed changes
    //添加论坛分类信息
    path: '/add-forum-category',
    component: () => import('@/views/ForumCategory/AddForumCategory.vue'),
    props: route => ({ id: route.query.id }) 
  },

  {
    //查看修改论坛分类信息
    path: '/edit-forum-category',
    component: () => import('@/views/ForumCategory/EditForumCategory.vue'),
<<<<<<< Updated upstream
=======
>>>>>>> 45b62b24d81aa698056348e1c54b41b13934d2a3
>>>>>>> Stashed changes
    props: route => ({ id: route.query.id }) 
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
        meta: { title: '论坛帖子与评论管理', icon: 'table' }
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
