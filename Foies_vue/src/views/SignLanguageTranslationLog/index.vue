<template>
    <div class="app-container">
      <!-- 添加日志按钮 -->
      <el-button type="primary" @click="handleAdd">添加日志</el-button>
  
      <!-- 日志列表表格 -->
      <el-table :data="logs" border>
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
          //console.log("Fetched logs data:", JSON.stringify(response, null, 2));  // 使用JSON.stringify来更好地查看对象结构
          this.logs = response;  // 将返回的日志数据赋值给logs
        } catch (error) {
          console.error('获取日志列表失败', error);
        }
      },
  
      // 处理添加日志
      handleAdd() {
        this.$router.push('/add-SignLanguageTranslationlog');
      },

      // 处理编辑日志
      handleEdit(log) {
      this.$router.push({ path: '/edit-SignLanguageTranslationLog', query: { id: log.id } });
    },
  
      // 处理删除日志
      async handleDelete(id) {
        try {
          await SignLanguageTranslationLogApi.deleteLog(id);
          this.fetchLogs(); // 删除成功后重新获取日志列表
        } catch (error) {
          console.error('删除日志失败', error);
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