<template>
  <div class="app-container">
    <!-- 操作按钮 -->
    <el-button type="primary" @click="handleAdd">添加日志</el-button>
    <el-button type="danger" @click="confirmBatchDelete" :disabled="selectedLogs.length === 0">批量删除</el-button>

    <!-- 日志列表表格 -->
    <el-table :data="logs" border @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" />
      <el-table-column prop="id" label="ID" width="80" />
      <el-table-column prop="userId" label="用户ID"></el-table-column>
      <el-table-column prop="originalVideoUrl" label="原始视频URL"></el-table-column>
      <el-table-column prop="createdtime" label="创建时间">
        <template slot-scope="scope">
          {{ formatDate(scope.row.createdtime) }}
        </template>
      </el-table-column>
      <el-table-column label="操作" width="200">
        <template slot-scope="scope">
          <el-button size="mini" @click="handleEdit(scope.row)">查看</el-button>
          <el-button size="mini" type="danger" @click="handleDelete(scope.row.id)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>
  </div>
</template>

<script>
import SignLanguageTranslationLogApi from '@/api/SignLanguageTranslationLog_api';

export default {
  data() {
    return {
      logs: [], // 存储日志记录的数组
      selectedLogs: [], // 存储选中的日志ID
      filters: {
        userId: '', 
        originalVideoUrl: '',
        createdtime: '',
        translatedText: ''
      }
    };
  },
  created() {
    this.fetchLogs();
  },
  methods: {
    async fetchLogs() {
      try {
        const response = await SignLanguageTranslationLogApi.getLogs(this.filters);
        this.logs = response;
      } catch (error) {
        console.error('获取日志列表失败', error);
      }
    },

    handleAdd() {
      this.$router.push('/add-SignLanguageTranslationlog');
    },

    handleEdit(log) {
      this.$router.push({ path: '/edit-SignLanguageTranslationLog', query: { id: log.id } });
    },

    async handleDelete(id) {
      try {
        await SignLanguageTranslationLogApi.deleteLog(id);
        this.fetchLogs();
      } catch (error) {
        console.error('删除日志失败', error);
      }
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
      }).catch(() => {});
    },

    // 处理批量删除
    async handleBatchDelete() {
      try {
        await Promise.all(this.selectedLogs.map(id => SignLanguageTranslationLogApi.deleteLog(id)));
        this.fetchLogs();
        this.selectedLogs = [];
        this.$message.success('批量删除成功');
      } catch (error) {
        console.error('批量删除失败', error);
      }
    },

    // 格式化日期
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
