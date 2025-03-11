<template>
  <div class="app-container">
    <el-form :inline="true" class="demo-form-inline">
      <el-form-item label="服务名称">
        <el-input v-model="searchForm.serviceName" placeholder="输入服务名称"></el-input>
      </el-form-item>
      <el-form-item label="状态">
        <el-select v-model="searchForm.status" placeholder="选择状态">
          <el-option label="成功" value="success"></el-option>
          <el-option label="失败" value="failure"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="fetchLogs">查询</el-button>
        <el-button @click="resetSearch">重置</el-button>
      </el-form-item>
    </el-form>

    <!-- 操作按钮 -->
    <el-button type="primary" @click="handleAdd">添加日志</el-button>
    <el-button type="danger" @click="confirmBatchDelete" :disabled="selectedLogs.length === 0">批量删除</el-button>

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
          <el-button size="mini" @click="handleView(scope.row)">编辑</el-button>
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
      selectedLogs: [],
      searchForm: {
        serviceName: '',
        status: ''
      }
    };
  },
  created() {
    this.fetchLogs();
  },
  methods: {
    async fetchLogs() {
      const filters = {
        oredCriteria: [
          {
            criteria: []
          }
        ]
      };

      if (this.searchForm.serviceName) {
        filters.oredCriteria[0].criteria.push({
          condition: "service_name LIKE",
          value: `%${this.searchForm.serviceName}%`,
          singleValue: true
        });
      }

      if (this.searchForm.status) {
        filters.oredCriteria[0].criteria.push({
          condition: "status =",
          value: this.searchForm.status,
          singleValue: true
        });
      }

      try {
        const response = await ThirdPartyApiLogApi.getLogs(filters);
        this.logs = response.data;
      } catch (error) {
        console.error('获取日志列表失败', error);
      }
    },

    resetSearch() {
      this.searchForm.serviceName = '';
      this.searchForm.status = '';
      this.fetchLogs();
    },

    // 监听表格选择变化
    handleSelectionChange(selection) {
      this.selectedLogs = selection.map(log => log.id);
    },

    // 批量删除前的确认操作
    confirmBatchDelete() {
      if (this.selectedLogs.length === 0) return;

      this.$confirm('确定要删除选中的日志吗？', '警告', {
        type: 'warning'
      }).then(() => {
        this.handleBatchDelete();
      }).catch(() => { });
    },

    // 批量删除日志
    async handleBatchDelete() {
      try {
        await Promise.all(this.selectedLogs.map(id => ThirdPartyApiLogApi.deleteLog(id)));
        this.fetchLogs();
        this.selectedLogs = [];
        this.$message.success('批量删除成功');
      } catch (error) {
        console.error('批量删除失败', error);
      }
    },

    formatDate(date) {
      if (!date) return '无';
      const localDate = new Date(date);
      return localDate.toLocaleString('zh-CN', { timeZone: 'Asia/Shanghai' });
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
    },

    // 跳转到添加日志页面
    handleAdd() {
      this.$router.push('/add-log');
    },

    // 跳转到日志详情页面
    handleView(row) {
      this.$router.push({ path: '/log-detail', query: { id: row.id } });
    },

    // 删除日志
    async handleDelete(id) {
      try {
        await ThirdPartyApiLogApi.deleteLog(id);
        this.fetchLogs();
        this.$message.success('删除成功');
      } catch (error) {
        console.error('删除失败', error);
      }
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