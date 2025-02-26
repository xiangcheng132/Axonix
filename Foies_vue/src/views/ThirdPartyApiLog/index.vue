<template>
  <div class="app-container">
    <el-button type="primary" @click="handleAdd">添加日志</el-button>
    <el-table :data="logs" border>
      <el-table-column prop="id" label="ID" width="80" />
      <el-table-column prop="serviceName" label="服务名称"></el-table-column>
      <el-table-column prop="createdtime" label="请求时间">
        <template slot-scope="scope">
          {{ formatDate(scope.row.createdtime) }}
        </template>
      </el-table-column>
      <el-table-column prop="status" label="状态" />
      <el-table-column label="操作" width="200">
        <template slot-scope="scope">
          <el-button size="mini" @click="handleView(scope.row)">查看</el-button>
          <el-button size="mini" type="danger" @click="handleDelete(scope.row.id)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>
  </div>
</template>

<script>
import ThirdPartyApiLogApi from '@/api/ThirdPartyApiLog_api';

export default {
  data() {
    return {
      logs: [],
      filters: {
        service_name: '',
        status: '', // 可以根据需要添加过滤条件
      }
    };
  },
  created() {
    this.fetchLogs();
  },
  methods: {
    async fetchLogs() {
      try {
        const response = await ThirdPartyApiLogApi.getLogs(this.filters);
        console.log("Fetched logs data:", JSON.stringify(response.data, null, 2));  // 使用JSON.stringify来更好地查看对象结构
        this.logs = response.data;  // 将返回的日志数据赋值给logs
      } catch (error) {
        console.error('获取日志列表失败', error);
      }
    },

    handleAdd() {
      this.$router.push('/add-log');
    },

    handleView(log) {
      this.$router.push({ path: '/log-detail', query: { id: log.id } });
    },

    async handleDelete(id) {
      try {
        await ThirdPartyApiLogApi.deleteLog(id);
        this.fetchLogs();
      } catch (error) {
        console.error('删除日志失败', error);
      }
    },

    formatDate(date) {
      return date ? new Date(date).toLocaleString() : '无';
    }
  }
};
</script>

<style scoped>
.app-container {
  padding: 20px;
}

.el-table {
  margin-top: 20px;
}

.el-button {
  margin-right: 10px;
}
</style>