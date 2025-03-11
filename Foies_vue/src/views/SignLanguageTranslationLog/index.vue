<template>
  <div class="app-container">
    <!-- 搜索表单 -->
    <el-form :inline="true" class="demo-form-inline">
      <el-form-item label="用户ID">
        <el-input v-model="filters.userId" placeholder="输入用户ID"></el-input>
      </el-form-item>
      <el-form-item label="视频URL">
        <el-input v-model="filters.originalVideoUrl" placeholder="输入视频URL"></el-input>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="fetchLogs">查询</el-button>
        <el-button @click="resetSearch">重置</el-button>
      </el-form-item>
    </el-form>

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
        <template #default="scope">
          {{ formatDate(scope.row.createdtime) }}
        </template>
      </el-table-column>
      <el-table-column label="操作" width="200">
        <template #default="scope">
          <el-button size="mini" @click="handleEdit(scope.row)">编辑</el-button>
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
      logs: [],
      selectedLogs: [],
      filters: {
        userId: '',
        originalVideoUrl: '',
        translatedText: ''
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

      if (this.filters.userId) {
        filters.oredCriteria[0].criteria.push({
          condition: "user_id LIKE",
          value: `%${this.filters.userId}%`,
          singleValue: true
        });
      }
      if (this.filters.originalVideoUrl) {
        filters.oredCriteria[0].criteria.push({
          condition: "original_video_url LIKE",
          value: `%${this.filters.originalVideoUrl}%`,
          singleValue: true
        });
      }

      try {
        const response = await SignLanguageTranslationLogApi.getLogs(filters);
        this.logs = response;
      } catch (error) {
        console.error('获取日志列表失败', error);
      }
    },

    resetSearch() {
      this.filters.userId = '';
      this.filters.originalVideoUrl = '';
      this.filters.translatedText = '';
      this.fetchLogs();
    },

    handleSelectionChange(selection) {
      this.selectedLogs = selection.map(log => log.id);
    },

    confirmBatchDelete() {
      if (this.selectedLogs.length === 0) return;
      this.$confirm('确定要删除选中的日志吗？', '警告', {
        type: 'warning'
      }).then(() => {
        this.handleBatchDelete();
      }).catch(() => {});
    },

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

    formatDate(date) {
      return date ? new Date(date).toLocaleString() : '无';
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
