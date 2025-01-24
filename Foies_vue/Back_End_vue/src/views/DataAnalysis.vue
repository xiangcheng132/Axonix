<template>
  <div class="data-analysis">
    <!-- 返回主页按钮 -->
    <button @click="goToDashboard" class="back-button">返回主页</button>

    <h1>数据分析</h1>
    <p>这里展示数据分析的内容，包括图表、分析结果等。</p>

    <!-- 示例内容：图表展示 -->
    <div class="chart-container">
      <p>图表区域（可以用图表库展示数据）</p>
      <!-- 例如，你可以使用 ECharts、Chart.js 等库来展示图表 -->
    </div>

    <!-- 其他内容：可以添加筛选选项、分析结果表格等 -->
  </div>
</template>

<script>
export default {
  name: 'DataAnalysis',
  data() {
    return {
      chartData: [], // 示例数据
    };
  },
  methods: {
    // 跳转到 AdminDashboard 页面
    goToDashboard() {
      this.$router.push({ name: 'AdminDashboard' });
    },

    // 模拟获取数据
    fetchData() {
      this.chartData = [12, 19, 3, 5, 2, 3]; // 示例数据
    },
  },
  mounted() {
    this.fetchData(); // 组件加载时获取数据
  },
};
</script>

<style scoped>
/* 数据分析页面样式 */
.data-analysis {
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

.chart-container {
  margin-top: 20px;
  padding: 20px;
  background-color: white;
  border-radius: 8px;
  box-shadow: 0 2px 5px rgba(0, 0, 0, 0.1);
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
