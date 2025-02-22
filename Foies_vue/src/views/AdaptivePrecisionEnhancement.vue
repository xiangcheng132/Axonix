<template>
  <div class="adaptive-precision-enhancement">
    <!-- 返回主页按钮 -->
    <button @click="goToDashboard" class="back-button">返回主页</button>

    <h1>自适应精度增强</h1>
    <p>本页面展示如何动态调整计算精度，以优化性能和准确性。</p>

    <!-- 精度设置 -->
    <div class="precision-settings">
      <h2>精度设置</h2>
      <p>根据不同的计算需求，可以调整精度来平衡性能和准确性。</p>
      <label for="precision">选择精度级别:</label>
      <select v-model="selectedPrecision" id="precision">
        <option value="low">低精度</option>
        <option value="medium">中等精度</option>
        <option value="high">高精度</option>
      </select>
      <button @click="applyPrecisionSettings">应用精度设置</button>
    </div>

    <!-- 优化结果 -->
    <div class="optimization-results">
      <h2>优化结果</h2>
      <p>根据当前设置，以下是优化后的计算结果和性能数据：</p>
      <div v-if="optimizationResults">
        <p><strong>精度级别:</strong> {{ optimizationResults.precision }}</p>
        <p><strong>计算性能:</strong> {{ optimizationResults.performance }}%</p>
        <p><strong>计算时间:</strong> {{ optimizationResults.time }} ms</p>
      </div>
      <div v-else>
        <p>尚未应用精度设置。</p>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: 'AdaptivePrecisionEnhancement',
  data() {
    return {
      selectedPrecision: 'medium', // 默认精度为中等
      optimizationResults: null, // 存储优化结果
    };
  },
  methods: {
    // 跳转到 AdminDashboard 页面
    goToDashboard() {
      this.$router.push({ name: 'AdminDashboard' });
    },

    // 模拟应用精度设置的操作
    applyPrecisionSettings() {
      let performance = 0;
      let time = 0;
      
      switch (this.selectedPrecision) {
        case 'low':
          performance = 80;
          time = 50;
          break;
        case 'medium':
          performance = 90;
          time = 30;
          break;
        case 'high':
          performance = 95;
          time = 20;
          break;
        default:
          performance = 85;
          time = 40;
          break;
      }

      this.optimizationResults = {
        precision: this.selectedPrecision,
        performance: performance,
        time: time,
      };
    }
  }
};
</script>

<style scoped>
/* 自适应精度增强页面样式 */
.adaptive-precision-enhancement {
  padding: 30px;
  background-color: #f9f9f9;
}

h1 {
  font-size: 2.5rem;
  color: #333;
}

p {
  font-size: 1.2rem;
  color: #666;
  margin-top: 10px;
}

h2 {
  font-size: 1.8rem;
  color: #333;
  margin-top: 30px;
}

label {
  font-size: 1.2rem;
  color: #333;
  margin-right: 10px;
}

select {
  font-size: 1.2rem;
  padding: 5px;
  margin: 10px 0;
}

button {
  background-color: #007bff;
  color: white;
  padding: 10px 20px;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  transition: background-color 0.3s ease;
}

button:hover {
  background-color: #0056b3;
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

/* 区域间的间距和卡片式布局 */
.precision-settings, .optimization-results {
  background-color: white;
  padding: 20px;
  border-radius: 8px;
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
  margin-top: 20px;
}

.optimization-results p {
  font-size: 1rem;
  color: #666;
  line-height: 1.6;
}
</style>
