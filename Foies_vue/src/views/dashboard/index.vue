<template>
  <div class="dashboard-container">
    <el-card class="admin-card" :style="cardStyle">
      <div class="admin-info">
        <svg-icon icon-class="user" class="user-icon" />
        <div class="admin-details">
          <h2 class="admin-title">管理员</h2>
          <div class="admin-name">{{ name }}</div>
        </div>
        <div class="greeting">
          <span class="greeting-text">{{ greetingText }}</span>
          <span class="current-time">{{ currentTime }}</span>
        </div>
      </div>
    </el-card>
  </div>
</template>

<script>
import { mapGetters } from 'vuex'

export default {
  name: 'Dashboard',
  data() {
    return {
      currentTime: '',
      timer: null,
      stats: {
        userCount: 0,
        postCount: 0,
        categoryCount: 0,
        logCount: 0
      }
    }
  },
  computed: {
    ...mapGetters([
      'name'
    ]),
    greetingText() {
      const hour = new Date().getHours();
      if (hour >= 6 && hour < 12) {
        return '早上好，现在是';
      } else if (hour >= 12 && hour < 14) {
        return '中午好，现在是';
      } else if (hour >= 14 && hour < 18) {
        return '下午好，现在是';
      } else {
        return '晚上好，现在是';
      }
    },

    cardStyle() {
      const hour = new Date().getHours();
      let backgroundColor = '#409EFF'; // 默认蓝色
      if (hour >= 6 && hour < 12) {
        backgroundColor = '#67C23A'; // 早上绿色
      } else if (hour >= 12 && hour < 14) {
        backgroundColor = '#E6A23C'; // 中午橙色
      } else if (hour >= 14 && hour < 18) {
        backgroundColor = '#409EFF'; // 下午蓝色
      } else {
        backgroundColor = '#909399'; // 晚上灰色
      }
      return { backgroundColor };
    }
  },
  methods: {
    updateTime() {
      const now = new Date()
      this.currentTime = now.toLocaleTimeString('zh-CN', {
        hour: '2-digit',
        minute: '2-digit',
        second: '2-digit'
      })
    },
    async fetchStats() {
      // 在这里你可以请求后台接口来获取实际的统计数据
      // 例如：
      // const response = await fetch('/api/stats');
      // this.stats = await response.json();
    }
  },
  mounted() {
    this.updateTime()
    this.timer = setInterval(this.updateTime, 1000)
    this.fetchStats()
  },
  beforeDestroy() {
    clearInterval(this.timer)
  }
}
</script>

<style lang="scss" scoped>
.dashboard-container {
  padding: 20px;

  .admin-card {
    margin-bottom: 20px;
    transition: background-color 0.5s ease;

    .admin-info {
      display: flex;
      align-items: center;
      padding: 15px;
      color: #fff;

      .user-icon {
        width: 50px;
        height: 50px;
        margin-right: 20px;
        color: #fff;
      }

      .admin-details {
        flex: 1;

        .admin-title {
          font-size: 16px;
          margin-bottom: 5px;
        }

        .admin-name {
          font-size: 24px;
          font-weight: 500;
        }
      }

      .greeting {
        margin-left: 40px;
        text-align: right;

        .greeting-text {
          font-size: 18px;
          margin-right: 8px;
        }

        .current-time {
          font-size: 20px;
          font-weight: 500;
        }
      }
    }
  }

  .stat-cards {
    .stat-card {
      margin-bottom: 20px;
      min-height: 150px;
      cursor: pointer;
      transition: transform 0.2s ease;

      &:hover {
        transform: translateY(-5px);
      }

      .stat-content {
        text-align: center;
        padding: 30px 0;

        .stat-title {
          font-size: 16px;
          color: #909399;
          margin-bottom: 10px;
        }

        .stat-value {
          font-size: 24px;
          color: #303133;
          font-weight: 500;
        }
      }
    }
  }
}
</style>
