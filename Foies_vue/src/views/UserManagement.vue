<template>
  <div class="user-management">
    <h1>用户管理</h1>

    <!-- 展示错误信息 -->
    <p v-if="errorMessage" class="error-message">{{ errorMessage }}</p>

    <!-- 展示用户信息 -->
    <div v-if="user" class="user-card">
    <h1>{{ user.username }} 的用户信息</h1>
    <p>角色: {{ user.role }}</p>
    <p>邮箱: {{ user.email }}</p>
    <p>电话: {{ user.phone || '未提供' }}</p>
    <p>残障类型: {{ user.disability_type || '无' }}</p>
    <p>偏好语言: {{ (user.preferences && user.preferences.language) || '未指定' }}</p>
    <p>注册时间: {{ new Date(user.createdTime).toLocaleString() }}</p>
    <p>上次更新时间: {{ new Date(user.updatedTime).toLocaleString() }}</p>
  </div>

    <!-- 返回主页按钮 -->
    <button @click="goToDashboard" class="back-button">返回主页</button>

    <!-- 按钮触发获取用户信息 -->
    <button @click="fetchUserData" class="fetch-user-button">获取用户信息</button>
  </div>
  <div>
    
  </div>
</template>

<script>
import axios from 'axios';

export default {
  name: 'UserManagement',
  data() {
    return {
      user: null, // 用来存储获取到的用户信息
      errorMessage: '', // 错误消息
    };
  },
  methods: {
    // 返回主页
    goToDashboard() {
      this.$router.push({ name: 'AdminDashboard' });
    },
    // 获取指定ID的用户信息
    async fetchUserData() {
  try {
    const response = await axios.get('http://localhost:8080/users/1');
    console.log(response.data); // 检查这里输出的内容
    this.user = response.data;
    this.errorMessage = ''; // 清除任何先前的错误消息
  } catch (error) {
    console.error("获取用户信息时出错:", error);
    this.errorMessage = "无法加载用户数据，请检查网络连接或联系管理员。";
  }
}
  }
};
</script>

<style scoped>
/* 现有样式保持不变 */
.fetch-user-button {
  background-color: #28a745;
  color: white;
  padding: 10px 20px;
  border: none;
  border-radius: 4px;
  margin-top: 20px;
  cursor: pointer;
  font-size: 1rem;
  transition: background-color 0.3s ease;
}

.fetch-user-button:hover {
  background-color: #218838;
}

.error-message {
  color: red;
}
</style>