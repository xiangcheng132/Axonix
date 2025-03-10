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

    <div class="stat-cards">
      <el-row :gutter="20">
        <el-col :xs="24" :sm="12" :md="8" :lg="6" v-for="stat in statsList" :key="stat.type">
          <el-card class="stat-card" @click.native="handleStatClick(stat.type)">
            <div class="stat-content">
              <div class="stat-title">{{ stat.title }}</div>
              <div class="stat-value">{{ stat.value }}</div>
            </div>
          </el-card>
        </el-col>
      </el-row>
    </div>
  </div>
</template>

<script>
import { mapGetters } from 'vuex'
import axios from 'axios';


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
    },

    statsList() {
      return [
        {
          type: 'user',
          title: '用户数量',
          value: this.stats.userCount
        },
        {
          type: 'post',
          title: '论坛帖子',
          value: this.stats.postCount
        },
        {
          type: 'category',
          title: '论坛分类',
          value: this.stats.categoryCount
        },
        {
          type: 'log',
          title: '操作记录',
          value: this.stats.logCount
        },
        {
          type: 'api',
          title: '第三方api日志',
          value: this.stats.apiCount
        },
        {
          type: 'pay',
          title: '支付日志',
          value: this.stats.payCount
        },
        {
          type: 'facility',
          title: '设施数量',
          value: this.stats.facilityCount
        },
        {
          type: 'emergencyrequest',
          title: '紧急求助数量',
          value: this.stats.emergencyrequestCount
        },
      ]
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
      try {
        const [userRes, postRes, categoryRes, logRes, apiRes, payRes, facilityRes, emergencyrequestRes] = await Promise.all([
          axios.get('http://localhost:8080/users/count'),
          axios.get('http://localhost:8080/forum-posts/count'),
          axios.get('http://localhost:8080/forum-categories/count'),
          axios.get('http://localhost:8080/operationLogs/count'),
          axios.get('http://localhost:8080/third-party-api-logs/count'),
          axios.get('http://localhost:8080/payments/count'),
          axios.get('http://localhost:8080/facilities/count'),
          axios.get('http://localhost:8080/emergency-requests/count'),
        ])

        this.stats = {
          userCount: userRes.data,
          postCount: postRes.data,
          categoryCount: categoryRes.data,
          logCount: logRes.data,
          apiCount: apiRes.data,
          payCount: payRes.data,
          facilityCount: facilityRes.data,
          emergencyrequestCount: emergencyrequestRes.data
        }
      } catch (error) {
        console.error('获取统计数据失败:', error)
      }
    },
    handleStatClick(type) {
      const routes = {
        user: '/User/index',
        post: '/Forum/ForumPost',
        category: '/Forum/ForumCategory',
        log: '/OperationLog/index',
        api: '/ThirdPartyApiLog/index',
        pay: '/payment/index',
        facility: '/Facility/index',
        emergencyrequest: '/EmergencyRequest/index'
      }
      this.$router.push(routes[type])
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
