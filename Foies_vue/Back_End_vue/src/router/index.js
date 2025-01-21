import { createRouter, createWebHistory } from 'vue-router';
import AdminDashboard from '../views/AdminDashboard.vue'; // 导入 管理后端 主页
import UserManagement from '../views/UserManagement.vue'; // 导入 用户管理 页面
import DataAnalysis from '../views/DataAnalysis.vue'; // 导入 数据处理与分析 页面
import ServiceIntegration from '../views/ServiceIntegration.vue'; // 导入 服务接口与集成 页面
import DataPrivacy from '../views/DataPrivacy.vue'; // 导入 数据隐私与加密 页面
import AdaptivePrecisionEnhancement from '../views/AdaptivePrecisionEnhancement.vue'; // 导入 自适应精度增强 页面

const routes = [
  {
    path: '/',
    name: 'AdminDashboard',
    component: AdminDashboard // 默认展示 管理后端 主页
  },
  {
    path: '/user-management',
    name: 'UserManagement',
    component: UserManagement // 点击链接时跳转到 用户管理 页面
  },
  {
    path: '/data-analysis',
    name: 'DataAnalysis',
    component: DataAnalysis // 点击链接时跳转到 数据处理与分析 页面
  },
  {
    path: '/service-integration',
    name: 'ServiceIntegration',
    component: ServiceIntegration // 点击链接时跳转到 服务接口与集成 页面
  },
  {
    path: '/data-privacy',
    name: 'DataPrivacy',
    component: DataPrivacy // 点击链接时跳转到 数据隐私与加密 页面
  },
  {
    path: '/adaptive-precision-enhancement',
    name: 'AdaptivePrecisionEnhancement',
    component: AdaptivePrecisionEnhancement // 点击链接时跳转到 数据隐私与加密 页面
  },
  // 其他路由配置
];

const router = createRouter({
  history: createWebHistory(process.env.BASE_URL),
  routes
});

export default router;
