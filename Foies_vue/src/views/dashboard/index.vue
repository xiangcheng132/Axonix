<template>
  <div class="dashboard-container">
    <el-row :gutter="20">
      <!-- 管理员信息卡片 -->
      <el-col :xs="24" :sm="24" :md="16" :lg="16">
        <el-card class="admin-card" :style="cardStyle">
          <div class="admin-profile">
            <div class="avatar-wrapper">
              <svg-icon icon-class="user" class="user-icon" />
              <div class="online-indicator"></div>
            </div>
            <div class="admin-meta">
              <div class="title-group">
                <span class="admin-badge">系统管理员</span>
                <h1 class="admin-name">{{ name }}</h1>
              </div>
              <div class="time-group">
                <div class="greeting">
                  <span class="greeting-icon">👋</span>
                  <span class="greeting-text">{{ greetingText }}</span>
                </div>
                <div class="current-time">
                  <span class="time-icon">🕒</span>
                  <span class="time-value">{{ currentTime }}</span>
                </div>
              </div>
            </div>
          </div>
        </el-card>
        <!-- 通知信息卡片 -->
        <!-- 修改后的通知信息卡片 -->
        <el-card class="notifications-card" shadow="hover" style="margin-top: 20px;">
          <h3>通知信息</h3>
          <div class="notifications-table-wrapper">
            <el-table 
              :data="notifications" 
              border 
              style="width: 100%"
              max-height="300">  <!-- 新增max-height属性 -->
              <el-table-column 
                prop="title" 
                label="通知标题" 
                align="center">
                <template #default="scope">
                  <el-link 
                    type="primary" 
                    @click="showNotificationDetails(scope.row)">
                    {{ scope.row.title }}
                  </el-link>
                </template>
              </el-table-column>
            </el-table>
          </div>
        </el-card>
      </el-col>

      <!-- 统计卡片组 -->
      <el-col :xs="24" :sm="24" :md="8" :lg="8" class="stat-cards">
        <el-card class="stat-card" shadow="hover">
          <div class="stat-content">
            <div class="stat-title"></div>
            <el-table :data="statTableData" border style="width: 100%">
              <el-table-column prop="label" label="统计项" align="center"></el-table-column>
              <el-table-column prop="value" label="数量" align="center"></el-table-column>
            </el-table>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 修改后的图表展示区域 -->
    <el-row :gutter="20">
      <el-col :span="14">
        <el-card shadow="hover">
          <div id="regionChart" style="height: 400px;"></div>
        </el-card>
      </el-col>
      <el-col :span="10">
        <el-card shadow="hover">
          <div id="disabilityChart" style="height: 400px;"></div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 通知详情弹窗 -->
    <el-dialog 
      title="通知详情" 
      :visible.sync="notificationDialogVisible" 
      width="50%">
      <p>{{ selectedNotification.content }}</p>
      <span slot="footer" class="dialog-footer">
        <el-button @click="notificationDialogVisible = false">关闭</el-button>
      </span>
    </el-dialog>
  </div>
</template>


<script>
import { mapGetters } from 'vuex'
import * as echarts from 'echarts'

export default {
  name: 'Dashboard',
  data() {
    return {
      currentTime: '',
      timer: null,
      stats: {
        totalUsers: 0,
        vipUsers: 0,
        helpinfo: 0,
        ailog: 0,
        navlog: 0,
        postinfo: 0,
        commentinfo: 0,
        notifications: 0,
        sosinfo: 0,
        feedback: 0
      },
      regionChart: null,
      disabilityChart: null,
      notifications: [], // 新增通知数据
      notificationDialogVisible: false, // 控制弹窗显示
      selectedNotification: {}, // 当前选中的通知
    }
  },
  computed: {
    ...mapGetters(['name']),
    greetingText() {
      const hour = new Date().getHours()
      if (hour >= 6 && hour < 12) return '早上好，现在是'
      if (hour >= 12 && hour < 14) return '中午好，现在是'
      if (hour >= 14 && hour < 18) return '下午好，现在是'
      return '晚上好，现在是'
    },
    cardStyle() {
      const hour = new Date().getHours()
      return {
        backgroundColor: this.getBackgroundColor(hour)
      }
    },
    statTableData() {
      return [
        { label: '总用户数', value: this.stats.totalUsers },
        { label: 'VIP 用户', value: this.stats.vipUsers },
        { label: '帮助请求', value: this.stats.helpinfo },
        { label: 'AI 日志', value: this.stats.ailog },
        { label: '导航日志', value: this.stats.navlog },
        { label: '论坛帖子', value: this.stats.postinfo },
        { label: '评论数', value: this.stats.commentinfo },
        { label: '通知数', value: this.stats.notifications },
        { label: 'SOS 通知', value: this.stats.sosinfo },
        { label: '反馈数', value: this.stats.feedback }
      ]
    }
  },
  methods: {
    getBackgroundColor(hour) {
      if (hour >= 6 && hour < 12) return '#67C23A'
      if (hour >= 12 && hour < 14) return '#E6A23C'
      if (hour >= 14 && hour < 18) return '#409EFF'
      return '#909399'
    },
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
        const totalRes = await this.$api.user.countUsers()
        this.stats.totalUsers = totalRes.data

        const vipRes = await this.$api.vip.countVips({})
        this.stats.vipUsers = vipRes.data

        const helpRes = await this.$api.helpinfo.countHelpRequests({})
        this.stats.helpinfo = helpRes.data

        const ailogRes = await this.$api.ailog.countAiLogs({})
        this.stats.ailog = ailogRes.data

        const navlogRes = await this.$api.navlog.countNavLogs({})
        this.stats.navlog = navlogRes.data

        const postinfoRes = await this.$api.postinfo.countForumPosts({})
        this.stats.postinfo = postinfoRes.data

        const commentinfoRes = await this.$api.commentinfo.countComments({})
        this.stats.commentinfo = commentinfoRes.data

        const notificationsRes = await this.$api.notifications.countNotifications({})
        this.stats.notifications = notificationsRes.data

        const sosinfoRes = await this.$api.sosinfo.countSosNotifications({})
        this.stats.sosinfo = sosinfoRes.data

        const feedbackRes = await this.$api.feedback.countFeedbacks({})
        this.stats.feedback = feedbackRes.data

        this.initCharts()
      } catch (error) {
        console.error('数据获取失败:', error)
        this.$message.error('数据加载失败，请稍后重试')
      }
    },
    async fetchNotifications() {
      try {
        const example = {};
        const response = await this.$api.notifications.getNotificationsByExampleWithBLOBs(example);
        this.notifications = response.data;
      } catch (error) {
        console.error('获取通知信息失败:', error);
        this.$message.error('通知信息加载失败，请稍后重试');
      }
    },
    showNotificationDetails(notification) {
      this.selectedNotification = notification;
      this.notificationDialogVisible = true;
    },
    initCharts() {
      this.$nextTick(() => {
        this.initRegionChart()
        this.initDisabilityChart()
      })
    },
    async initRegionChart() {
      try {
        const res = await this.$api.user.getUsers({
          fields: ['province']
        })

        const rawData = Array.isArray(res.data) ? res.data : []
        const provinceData = this.processProvinceData(rawData)
        const chartData = Object.entries(provinceData).map(([name, value]) => ({
          name,
          value
        }))

        this.regionChart = echarts.init(document.getElementById('regionChart'))
        const option = this.getRegionOption(chartData)
        this.regionChart.setOption(option)

        window.addEventListener('resize', () => this.regionChart.resize())
      } catch (error) {
        console.error('地区分布图表加载失败:', error)
        this.$message.error('地区数据加载失败')
      }
    },
    async initDisabilityChart() {
      try {
        const res = await this.$api.user.getUsers({
          fields: ['disability_type']
        })

        const rawData = Array.isArray(res.data) ? res.data : []
        const disabilityData = this.processDisabilityData(rawData)
        const chartData = Object.entries(disabilityData).map(([name, value]) => ({
          name,
          value
        }))

        this.disabilityChart = echarts.init(document.getElementById('disabilityChart'))
        const option = this.getDisabilityOption(chartData)
        this.disabilityChart.setOption(option)

        window.addEventListener('resize', () => this.disabilityChart.resize())
      } catch (error) {
        console.error('残障类型图表加载失败:', error)
        this.$message.error('残障数据加载失败')
      }
    },
    processProvinceData(users) {
      return users.reduce((acc, user) => {
        const province = user?.province?.trim() || '未知地区'
        acc[province] = (acc[province] || 0) + 1
        return acc
      }, {})
    },
    processDisabilityData(users) {
      const typeMap = {
        0: '无障碍',
        1: '视障',
        2: '听障',
        3: '其他障碍'
      }

      return users.reduce((acc, user) => {
        const type = user.disabilityType ?? 3 
        const label = typeMap[type] || '其他障碍'
        acc[label] = (acc[label] || 0) + 1
        return acc
      }, {})
    },
    getRegionOption(data) {
      return {
        title: {
          text: '用户地区分布',
          subtext: `共统计 ${data.reduce((sum, item) => sum + item.value, 0)} 位用户`,
          left: 'center',
          top: '5%',
          textStyle: {
            color: '#666',
            fontSize: 16
          }
        },
        tooltip: {
          trigger: 'item',
          formatter: '{a} <br/>{b} : {c} ({d}%)'
        },
        legend: {
          type: 'scroll',
          orient: 'vertical',
          left: 'left',
          top: '15%',
          bottom: '15%',
          itemGap: 8,
          itemWidth: 14,
          itemHeight: 14,
          textStyle: {
            color: '#666',
            fontSize: 12
          }
        },
        series: [{
          name: '地区分布',
          type: 'pie',
          radius: ['35%', '65%'],
          center: ['55%', '55%'],
          data: data.sort((a, b) => b.value - a.value),
          emphasis: {
            itemStyle: {
              shadowBlur: 10,
              shadowOffsetX: 0,
              shadowColor: 'rgba(0, 0, 0, 0.5)'
            }
          },
          itemStyle: {
            borderRadius: 6,
            borderColor: '#fff',
            borderWidth: 2
          },
          label: {
            formatter: '{b|{b}} \n {per|{d}%}',
            position: 'outer',
            alignTo: 'edge',
            margin: 100,
            rich: {
              b: { fontSize: 12, lineHeight: 20 },
              per: { color: '#999', fontSize: 10 }
            }
          },
          color: [
            '#5470C6', '#91CC75', '#FAC858', '#EE6666',
            '#73C0DE', '#3BA272', '#FC8452', '#9A60B4'
          ]
        }]
      }
    },
    getDisabilityOption(data) {
      return {
        title: {
          text: '残障类型分布',
          subtext: `总用户数: ${data.reduce((sum, item) => sum + item.value, 0)}`,
          left: 'center',
          textStyle: {
            color: '#666',
            fontSize: 16
          }
        },
        tooltip: {
          trigger: 'item',
          formatter: '{a}<br/>{b}: {c} ({d}%)'
        },
        legend: {
          orient: 'vertical',
          left: 'left',
          top: 'middle',
          textStyle: {
            color: '#666'
          }
        },
        series: [{
          name: '残障类型',
          type: 'pie',
          radius: '55%',
          center: ['50%', '60%'],
          data: data.sort((a, b) => b.value - a.value),
          emphasis: {
            itemStyle: {
              shadowBlur: 10,
              shadowOffsetX: 0,
              shadowColor: 'rgba(0, 0, 0, 0.5)'
            }
          },
          itemStyle: {
            borderRadius: 6,
            borderColor: '#fff',
            borderWidth: 2
          },
          label: {
            formatter: '{b|{b}}\n{per|{d}%}',
            rich: {
              b: { fontSize: 12, lineHeight: 20 },
              per: { color: '#999', fontSize: 10 }
            }
          },
          color: [
            '#67C23A', // 无障碍
            '#409EFF', // 视障
            '#E6A23C', // 听障
            '#F56C6C'  // 其他障碍
          ]
        }]
      }
    }
  },
  mounted() {
    this.updateTime()
    this.timer = setInterval(this.updateTime, 1000)
    this.fetchStats()
    this.fetchNotifications();
  },
  beforeDestroy() {
    clearInterval(this.timer)
    if (this.regionChart) this.regionChart.dispose()
    if (this.disabilityChart) this.disabilityChart.dispose()
  }
}
</script>

<style lang="scss" scoped>
.dashboard-container {
  display: flex;
  flex-direction: column;
  gap: 20px;

  .admin-card {
    height: 200px;
  }
  .notifications-card {
    height: 425px;
  }

  .notifications-card {
    display: flex;
    flex-direction: column;

    h3 {
      font-size: 18px;
      font-weight: bold;
      margin-bottom: 16px;
    }

    .notifications-table-wrapper {
      flex: 1;
      overflow-y: auto;
    }
  }

  .stat-cards {
    display: flex;
    flex-direction: column;
    justify-content: space-between;
    height: 100%;
  }
}

@media (max-width: 768px) {
  .dashboard-container .admin-card,
  .dashboard-container .notifications-card,
  .dashboard-container .stat-card {
    height: auto;
  }
}

.dashboard-container {
  padding: 20px;

  .admin-card {
    margin-bottom: 30px;
    border-radius: 16px;
    overflow: hidden;
    transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
    box-shadow: 0 8px 24px rgba(0, 0, 0, 0.12);
    background: linear-gradient(135deg, rgba(255,255,255,0.15) 0%, rgba(255,255,255,0.05) 100%);
    backdrop-filter: blur(8px);
    border: 1px solid rgba(255,255,255,0.2);

    .admin-profile {
      display: grid;
      grid-template-columns: auto 1fr;
      gap: 24px;
      padding: 24px;
      align-items: center;

      .avatar-wrapper {
        position: relative;
        .user-icon {
          width: 80px;
          height: 80px;
          color: rgba(255,255,255,0.9);
          filter: drop-shadow(0 4px 12px rgba(0,0,0,0.1));
        }
        .online-indicator {
          position: absolute;
          right: 0;
          bottom: 0;
          width: 16px;
          height: 16px;
          background: #67C23A;
          border: 2px solid #fff;
          border-radius: 50%;
          box-shadow: 0 2px 4px rgba(0,0,0,0.1);
        }
      }

      .admin-meta {
        .title-group {
          margin-bottom: 16px;
          .admin-badge {
            display: inline-block;
            background: rgba(255,255,255,0.15);
            color: rgba(255,255,255,0.9);
            padding: 4px 12px;
            border-radius: 20px;
            font-size: 12px;
            font-weight: 600;
            letter-spacing: 0.5px;
            backdrop-filter: blur(4px);
            margin-bottom: 8px;
          }
          .admin-name {
            font-size: 32px;
            font-weight: 700;
            color: rgba(255,255,255,0.95);
            letter-spacing: -0.5px;
            margin: 0;
            line-height: 1.2;
          }
        }

        .time-group {
          display: grid;
          gap: 8px;
          .greeting, .current-time {
            display: flex;
            align-items: center;
            gap: 8px;
            font-size: 16px;
            color: rgba(255,255,255,0.85);
            .greeting-icon, .time-icon {
              font-size: 20px;
            }
            .time-value {
              font-family: 'Courier New', monospace;
              font-weight: 600;
              letter-spacing: 1px;
            }
          }
        }
      }
    }

    &:hover {
      transform: translateY(-4px);
      box-shadow: 0 12px 32px rgba(0, 0, 0, 0.2);
    }
  }

  .stat-cards {
    display: flex;
    flex-direction: column;
    justify-content: flex-end; // 使统计卡片组与管理员信息卡片底部对齐
    height: 100%; // 确保统计卡片组占满可用高度

    .stat-card {
      flex: 1; // 使统计卡片高度更小
      margin-bottom: 0; // 去掉多余的间距
    }
  }

  .stat-cards {
    height: 100%; // 使统计卡片组高度与管理员信息卡片一致
    display: flex;
    flex-direction: column;
    justify-content: space-between; // 确保内容均匀分布

    .stat-card {
      flex-grow: 1; // 使统计卡片填充可用空间
      margin-bottom: 0; // 去掉多余的间距
    }
  }

  .stat-cards {
    margin-top: 20px;

    .stat-card {
      margin-bottom: 20px;
      transition: transform 0.3s ease, box-shadow 0.3s ease;
      background: linear-gradient(135deg, #f5f7fa, #ffffff);
      border-radius: 12px;
      box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);

      &:hover {
        transform: translateY(-5px);
        box-shadow: 0 8px 20px rgba(0, 0, 0, 0.15);
      }

      .stat-content {
        padding: 20px;
        text-align: center;

        .stat-title {
          font-size: 18px;
          font-weight: bold;
          color: #303133;
          margin-bottom: 16px;
          border-bottom: 1px solid #ebeef5;
          padding-bottom: 8px;
        }

        .stat-list {
          list-style: none;
          padding: 0;
          margin: 0;

          li {
            display: flex;
            justify-content: space-between;
            align-items: center;
            padding: 8px 0;
            border-bottom: 1px dashed #ebeef5;

            &:last-child {
              border-bottom: none;
            }

            .stat-label {
              font-size: 14px;
              color: #606266;
              font-weight: 500;
            }

            .stat-value {
              font-size: 16px;
              color: #409EFF;
              font-weight: bold;
            }
          }
        }

        el-table {
          margin-top: 16px;
          font-size: 14px;

          .el-table__header-wrapper {
            background-color: #f5f7fa;
          }

          .el-table__row {
            &:hover {
              background-color: #f0f9ff;
            }
          }
        }
      }
    }
  }

  .stat-list {
    list-style: none;
    padding: 0;
    margin: 0;
    font-size: 16px;
    color: #303133;

    li {
      margin-bottom: 8px;
      display: flex;
      justify-content: space-between;

      span:first-child {
        font-weight: bold;
        color: #606266;
      }
    }
  }

  #regionChart,
  #disabilityChart {
    height: 400px;
    width: 100%;
    background: rgba(255,255,255,0.9);
    border-radius: 8px;
    box-shadow: 0 2px 12px rgba(0,0,0,0.1);
  }

  .notifications-card {
    padding: 20px;
    border-radius: 12px;
    background: #fff;
    box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);

    h3 {
      font-size: 18px;
      font-weight: bold;
      margin-bottom: 16px;
    }
  }
}

@media (max-width: 768px) {
  .el-col {
    width: 100% !important;
    margin-bottom: 20px;
    
    #regionChart,
    #disabilityChart {
      height: 300px;
    }
  }
}
</style>
