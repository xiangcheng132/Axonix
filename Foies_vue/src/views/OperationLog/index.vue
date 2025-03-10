<template>
  <div class="app-container">
    <!-- 搜索表单 -->
    <el-form :inline="true" class="demo-form-inline">
      <el-form-item label="管理员ID">
        <el-input v-model="filters.adminId" placeholder="输入管理员ID"></el-input>
      </el-form-item>
      <el-form-item label="操作内容">
        <el-input v-model="filters.action" placeholder="输入操作内容"></el-input>
      </el-form-item>
      <el-form-item label="目标ID">
        <el-input v-model="filters.targetId" placeholder="输入目标ID"></el-input>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="fetchOperationLogs">查询</el-button>
        <el-button @click="resetSearch">重置</el-button>
      </el-form-item>
    </el-form>

    <!-- 操作按钮 -->
    <el-button type="primary" @click="handleAdd">添加日志</el-button>
    <el-button type="danger" @click="confirmBatchDelete" :disabled="selectedLogs.length === 0">批量删除</el-button>

    <!-- 操作日志表格 -->
    <el-table :data="operationLogs" border @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" />
      <el-table-column prop="id" label="ID" width="80" />
      <el-table-column prop="adminId" label="管理员ID" />
      <el-table-column prop="action" label="操作内容" />
      <el-table-column prop="targetId" label="目标ID" />
      <el-table-column prop="timestamp" label="操作时间">
        <template #default="scope">
          {{ formatDate(scope.row.timestamp) }}
        </template>
      </el-table-column>

      <el-table-column label="操作" width="200">
        <template #default="scope">
          <el-button size="mini" @click="handleView(scope.row)">查看</el-button>
          <el-button size="mini" type="danger" @click="handleDelete(scope.row.id)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>
  </div>
</template>

<script>
import OperationLogApi from '@/api/OperationLog_api';

export default {
  data() {
    return {
      operationLogs: [],
      selectedLogs: [],
      filters: {
        adminId: '',
        action: '',
        targetId: ''
      }
    };
  },
  created() {
    this.fetchOperationLogs();
  },
  methods: {
    async fetchOperationLogs() {
      const example = {
        oredCriteria: [{ criteria: [] }]
      };

      if (this.filters.adminId) {
        example.oredCriteria[0].criteria.push({
          condition: "admin_id LIKE",
          value: `%${this.filters.adminId}%`,
          singleValue: true
        });
      }

      if (this.filters.action) {
        example.oredCriteria[0].criteria.push({
          condition: "action LIKE",
          value: `%${this.filters.action}%`,
          singleValue: true
        });
      }

      if (this.filters.targetId) {
        example.oredCriteria[0].criteria.push({
          condition: "target_id LIKE",
          value: `%${this.filters.targetId}%`,
          singleValue: true
        });
      }

      try {
        const response = await OperationLogApi.getOperationLogs(example);
        this.operationLogs = response.data;
      } catch (error) {
        console.error('获取操作日志失败', error);
      }
    },

    resetSearch() {
      this.filters.adminId = '';
      this.filters.action = '';
      this.filters.targetId = '';
      this.fetchOperationLogs();
    },

    handleSelectionChange(selection) {
      this.selectedLogs = selection.map(log => log.id);
    },

    handleAdd() {
      this.$router.push('/add-operation-log');
    },

    handleView(log) {
      this.$router.push({ path: '/edit-operation-log', query: { id: log.id } });
    },

    async handleDelete(id) {
      try {
        await OperationLogApi.deleteOperationLog(id);
        this.fetchOperationLogs();
        this.$message.success('操作日志删除成功');
      } catch (error) {
        console.error('删除操作日志失败', error);
      }
    },

    confirmBatchDelete() {
      if (this.selectedLogs.length === 0) return;
      this.$confirm('确定要删除选中的操作日志吗？', '警告', {
        type: 'warning'
      }).then(() => {
        this.handleBatchDelete();
      }).catch(() => {});
    },

    async handleBatchDelete() {
      try {
        await Promise.all(this.selectedLogs.map(id => OperationLogApi.deleteOperationLog(id)));
        this.fetchOperationLogs();
        this.selectedLogs = [];
        this.$message.success('批量删除成功');
      } catch (error) {
        console.error('批量删除失败', error);
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
