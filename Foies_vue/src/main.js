import Vue from 'vue'

import 'normalize.css/normalize.css' // A modern alternative to CSS resets

import ElementUI from 'element-ui'
import 'element-ui/lib/theme-chalk/index.css'
import locale from 'element-ui/lib/locale/lang/en' // lang i18n

import '@/styles/index.scss' // global css

import App from './App'
import store from './store'
import router from './router'

import '@/icons' // icon
import '@/permission' // permission control

import * as echarts from 'echarts'

import userApi from '@/api/user_api' 
import vipApi from '@/api/uservip_api'
import helpinfoApi from '@/api/helpinfo_api'
import ailogApi from '@/api/ai_log_api'
import navlogApi from '@/api/nav_log_api'
import postinfoApi from '@/api/postinfo_api'
import commentinfoApi from './api/commentinfo_api'
import notificationsApi from '@/api/notifications_api'
import sosinfoApi from '@/api/sosinfo_api'
import feedbackApi from './api/feedback_api'

Vue.prototype.$api = {
  user: userApi,
  vip: vipApi,
  helpinfo: helpinfoApi,
  ailog: ailogApi,
  navlog: navlogApi,
  postinfo: postinfoApi,
  commentinfo: commentinfoApi,
  notifications: notificationsApi,
  sosinfo: sosinfoApi,
  feedback: feedbackApi
}
Vue.prototype.$echarts = echarts

if (process.env.NODE_ENV === 'production') {
  const { mockXHR } = require('../mock')
  mockXHR()
}

// set ElementUI lang to EN
Vue.use(ElementUI, { locale })
// 如果想要中文版 element-ui，按如下方式声明
// Vue.use(ElementUI)

Vue.config.productionTip = false

new Vue({
  el: '#app',
  router,
  store,
  render: h => h(App)
})
