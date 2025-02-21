<template>
  <div class="user-management">
    <h1>用户管理</h1>
    <p>这里展示用户管理的内容</p>

    <!-- 返回主页按钮 -->
    <button @click="goToDashboard" class="back-button">返回主页</button>
  </div>
</template>

<script>
export default {
  name: 'UserManagement',
  methods: {
    // 返回主页
    goToDashboard() {
      this.$router.push({ name: 'AdminDashboard' });
    }
  }
};
</script>

<style scoped>
.user-management {
  padding: 20px;
}

button.back-button {
  background-color: #007bff;
  color: white;
  padding: 10px 20px;
  border: none;
  border-radius: 4px;
  margin-top: 20px;
  cursor: pointer;
  font-size: 1rem;
  transition: background-color 0.3s ease;
}

button.back-button:hover {
  background-color: #0056b3;
}
</style>
