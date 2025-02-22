<template>
  <div class="data-privacy">
    <!-- 返回主页按钮 -->
    <button @click="goToDashboard" class="back-button">返回主页</button>

    <h1>数据隐私与加密</h1>
    <p>在此页面中，我们展示了数据隐私保护和加密技术的实现，确保用户数据的安全性。</p>

    <!-- 数据加密设置 -->
    <div class="encryption-settings">
      <h2>加密设置</h2>
      <p>此区域展示如何为敏感数据配置加密，包括数据传输和存储加密的选项。</p>
      <button>启用数据加密</button>
      <button>查看加密设置</button>
    </div>

    <!-- 隐私政策 -->
    <div class="privacy-policy">
      <h2>隐私政策</h2>
      <p>请阅读我们的隐私政策，了解我们如何收集、使用和保护您的数据。</p>
      <button>查看隐私政策</button>
    </div>

    <!-- 审计日志 -->
    <div class="audit-logs">
      <h2>审计日志</h2>
      <p>查看最近的加密和隐私保护操作日志。</p>
      <button>查看审计日志</button>
    </div>
  </div>
</template>

<script>
export default {
  name: 'DataPrivacy',
  data() {
    return {
      encryptionEnabled: false,
      auditLogs: [],
    };
  },
  methods: {
    // 跳转到 AdminDashboard 页面
    goToDashboard() {
      this.$router.push({ name: 'AdminDashboard' });
    },

    // 启用加密的操作
    enableEncryption() {
      this.encryptionEnabled = true;
    },

    // 获取审计日志
    fetchAuditLogs() {
      this.auditLogs = [
        { action: '启用数据加密', timestamp: '2023-01-10 10:00:00' },
        { action: '查看隐私政策', timestamp: '2023-01-11 11:30:00' },
      ];
    },
  },
  mounted() {
    this.fetchAuditLogs();
  },
};
</script>

<style scoped>
/* 数据隐私与加密页面样式 */
.data-privacy {
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

/* 返回主页按钮 */
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

button {
  background-color: #007bff;
  color: white;
  padding: 10px 20px;
  border: none;
  border-radius: 4px;
  margin-top: 10px;
  cursor: pointer;
  transition: background-color 0.3s ease;
}

button:hover {
  background-color: #0056b3;
}

/* 区域间的间距和卡片式布局 */
.encryption-settings, .privacy-policy, .audit-logs {
  background-color: white;
  padding: 20px;
  border-radius: 8px;
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
  margin-top: 20px;
}

.encryption-settings p, .privacy-policy p, .audit-logs p {
  font-size: 1rem;
  color: #666;
  line-height: 1.6;
}
</style>
