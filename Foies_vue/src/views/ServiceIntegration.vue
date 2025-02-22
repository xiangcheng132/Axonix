<template>
  <div class="service-integration">
    <!-- 返回主页按钮 -->
    <button @click="goToDashboard" class="back-button">返回主页</button>

    <h1>接口服务与集成</h1>
    <p>这里展示与外部服务集成的接口信息，包括API文档、集成状态等。</p>

    <!-- 示例内容：API接口列表 -->
    <div class="api-list">
      <h2>已集成的接口</h2>
      <ul>
        <li v-for="(api, index) in apis" :key="index">
          <strong>{{ api.name }}</strong> - <a :href="api.documentation" target="_blank">查看文档</a>
        </li>
      </ul>
    </div>

    <!-- 示例内容：接口集成状态 -->
    <div class="integration-status">
      <h2>接口集成状态</h2>
      <table>
        <thead>
          <tr>
            <th>接口名称</th>
            <th>状态</th>
            <th>最后更新时间</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="(status, index) in integrationStatus" :key="index">
            <td>{{ status.apiName }}</td>
            <td>{{ status.status }}</td>
            <td>{{ status.lastUpdated }}</td>
          </tr>
        </tbody>
      </table>
    </div>
  </div>
</template>

<script>
export default {
  name: 'ServiceIntegration',
  data() {
    return {
      apis: [
        { name: '用户管理API', documentation: 'https://example.com/docs/user-management' },
        { name: '支付服务API', documentation: 'https://example.com/docs/payment-service' },
        { name: '订单管理API', documentation: 'https://example.com/docs/order-management' }
      ],
      integrationStatus: [
        { apiName: '用户管理API', status: '已集成', lastUpdated: '2023-01-15' },
        { apiName: '支付服务API', status: '集成中', lastUpdated: '2023-01-10' },
        { apiName: '订单管理API', status: '已集成', lastUpdated: '2023-01-12' }
      ]
    };
  },
  methods: {
    // 跳转到 AdminDashboard 页面
    goToDashboard() {
      this.$router.push({ name: 'AdminDashboard' });
    },
    fetchIntegrationData() {
      // 例如，通过axios从后端获取API接口信息和集成状态
    }
  },
  mounted() {
    this.fetchIntegrationData(); // 组件加载时获取数据
  }
};
</script>

<style scoped>
/* 服务集成页面样式 */
.service-integration {
  padding: 20px;
  background-color: #f9f9f9;
}

h1 {
  font-size: 2rem;
  color: #333;
}

p {
  font-size: 1rem;
  color: #666;
}

.api-list {
  margin-top: 20px;
}

.api-list ul {
  list-style-type: none;
  padding: 0;
}

.api-list li {
  margin-bottom: 10px;
}

.api-list a {
  color: #007bff;
  text-decoration: none;
}

.api-list a:hover {
  text-decoration: underline;
}

.integration-status {
  margin-top: 30px;
}

.integration-status table {
  width: 100%;
  border-collapse: collapse;
}

.integration-status th,
.integration-status td {
  padding: 10px;
  border: 1px solid #ddd;
  text-align: left;
}

.integration-status th {
  background-color: #f2f2f2;
}

.integration-status tr:nth-child(even) {
  background-color: #f9f9f9;
}

.integration-status tr:hover {
  background-color: #f1f1f1;
}

/* 返回主页按钮样式 */
.back-button {
  background-color: #6c757d;
  color: white;
  padding: 10px 20px;
  border: none;
  border-radius: 4px;
  margin-bottom: 20px;
  cursor: pointer;
  font-size: 1rem;
  transition: background-color 0.3s ease;
}

.back-button:hover {
  background-color: #5a6268;
}
</style>
