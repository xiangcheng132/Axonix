<template>
  <div class="app-container">
    <!-- 操作按钮 -->
    <el-button type="primary" @click="handleAdd">添加日志</el-button>
    <el-button type="danger" @click="handleBatchDelete" :disabled="selectedLogs.length === 0">批量删除</el-button>

    <!-- 日志表格 -->
    <el-table :data="logs" border @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" />
      <el-table-column prop="id" label="ID" width="80" />
      <el-table-column prop="serviceName" label="服务名称"></el-table-column>
      <el-table-column prop="createdtime" label="请求时间">
        <template slot-scope="scope">
          {{ formatDate(scope.row.createdtime) }}
        </template>
      </el-table-column>
      <el-table-column prop="status" label="状态">
        <template slot-scope="scope">
          <el-tag :type="getStatusTag(scope.row.status)">
            {{ formatStatus(scope.row.status) }}
          </el-tag>
        </template>
      </el-table-column>
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
      selectedLogs: [], // 存储选中的日志
      filters: {
        service_name: '',
        status: '', // 过滤条件
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
        console.log("Fetched logs data:", JSON.stringify(response.data, null, 2));
        this.logs = response.data;
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

    // 监听表格选择变化
    handleSelectionChange(selection) {
      this.selectedLogs = selection.map(log => log.id);
    },

    // 批量删除日志
    async handleBatchDelete() {
      if (this.selectedLogs.length === 0) {
        return;
      }
      try {
        await Promise.all(this.selectedLogs.map(id => ThirdPartyApiLogApi.deleteLog(id)));
        this.fetchLogs();
        this.selectedLogs = [];
      } catch (error) {
        console.error('批量删除失败', error);
      }
    },

    formatDate(date) {
      return date ? new Date(date).toLocaleString() : '无';
    },

    formatStatus(status) {
      const statusMap = {
        success: '成功',
        failure: '失败'
      };
      return statusMap[status] || '未知';
    },

    getStatusTag(status) {
      const tagMap = {
        success: 'success',
        failure: 'danger'
      };
      return tagMap[status] || 'info';
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
