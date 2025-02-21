<template>
  <div class="admin-dashboard">
    <!-- Header Section -->
    <header>
      <h1>后台管理系统</h1>
      <p>欢迎来到管理控制台</p>
    </header>

    <!-- Navigation -->
    <nav>
      <ul>
        <li><router-link to="/user-management">用户管理</router-link></li>
        <li><router-link to="/data-analysis">数据处理与分析</router-link></li>
        <li><router-link to="/service-integration">服务接口与集成</router-link></li>
        <li><router-link to="/data-privacy">数据隐私与加密</router-link></li>
        <li><router-link to="/adaptive-precision-enhancement">自适应精度增强</router-link></li>
      </ul>
    </nav>

    <!-- Main Content -->
    <main>
      <section class="overview">
        <h2>系统概览</h2>
        <p>此区域展示系统的当前状态、重要统计数据等。</p>
      </section>

      <section class="activity">
        <h2>最新活动</h2>
        <p>这里展示系统的最近活动记录、用户行为等。</p>
      </section>
    </main>
  </div>
</template>

<script>
export default {
  name: 'AdminDashboard',
}
</script>

<style scoped>
/* General Layout */
.admin-dashboard {
  padding: 30px;
  font-family: 'Arial', sans-serif;
  background-color: #f4f7fb;
}

/* Header Section */
header {
  text-align: center;
  margin-bottom: 40px;
  background-color: #007bff;
  color: white;
  padding: 20px;
  border-radius: 8px;
}

header h1 {
  font-size: 2.5rem;
  font-weight: bold;
}

header p {
  font-size: 1.2rem;
  margin-top: 10px;
}

/* Navigation */
nav {
  background-color: #fff;
  border-radius: 8px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
  margin-bottom: 30px;
}

nav ul {
  list-style-type: none;
  padding: 0;
  margin: 0;
  display: flex;
  justify-content: space-around;
}

nav li {
  margin: 10px 0;
}

nav a {
  text-decoration: none;
  color: #333;
  font-size: 1.2rem;
  font-weight: bold;
  padding: 12px 20px;
  display: block;
  border-radius: 4px;
  transition: background-color 0.3s ease, color 0.3s ease;
}

nav a:hover {
  background-color: #007bff;
  color: white;
}

/* Main Content */
main {
  display: flex;
  gap: 30px;
  flex-wrap: wrap;
}

section {
  background-color: white;
  padding: 20px;
  border-radius: 8px;
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
  width: 48%;
  box-sizing: border-box;
}

section h2 {
  font-size: 1.8rem;
  margin-bottom: 15px;
  color: #333;
}

section p {
  font-size: 1.1rem;
  color: #666;
  line-height: 1.6;
}

@media (max-width: 768px) {
  /* Stack sections vertically on smaller screens */
  main {
    flex-direction: column;
  }

  section {
    width: 100%;
  }

  header h1 {
    font-size: 2rem;
  }

  header p {
    font-size: 1rem;
  }

  nav a {
    font-size: 1rem;
    padding: 10px 15px;
  }
}
</style>
