<template>
    <div class="app-container">
      <!-- 搜索表单 -->
      <el-form :inline="true" :model="searchForm" class="search-form">
        <el-form-item label="用户ID">
          <el-input v-model="searchForm.userId" placeholder="请输入用户ID" clearable />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="fetchNavLogs">查询</el-button>
          <el-button @click="resetSearch">重置</el-button>
        </el-form-item>
      </el-form>
  
      <!-- 记录数显示 -->
      <el-row class="action-row" type="flex" justify="space-between" align="middle">
        <el-col :span="8" class="record-count" style="text-align: left;">
          <span>当前共有 {{ totalRecords }} 条记录</span>
        </el-col>
      </el-row>
  
      <!-- 路况日志表格 -->
      <el-table :data="navLogs" border>
        <el-table-column prop="id" label="日志ID" />
        <el-table-column prop="userId" label="用户ID" />
        <el-table-column prop="startTime" label="开始时间">
          <template slot-scope="scope">{{ formatDate(scope.row.startTime) }}</template>
        </el-table-column>
        <el-table-column prop="endTime" label="结束时间">
          <template slot-scope="scope">{{ formatDate(scope.row.endTime) }}</template>
        </el-table-column>
      </el-table>
    </div>
  </template>
  
  <script>
  import NavLogAPI from '@/api/nav_log_api';
  
  export default {
    data() {
      return {
        navLogs: [],        // 路况日志数据
        totalRecords: 0,    // 总记录数
        searchForm: {       // 搜索表单
          userId: ''
        }
      };
    },
  
    created() {
      this.fetchNavLogs();
    },
  
    methods: {
      // 获取路况日志数据
      async fetchNavLogs() {
        const example = {
          oredCriteria: [{ criteria: [] }]
        };
  
        if (this.searchForm.userId) {
          example.oredCriteria[0].criteria.push({
            condition: "user_id =",
            value: this.searchForm.userId,
            singleValue: true
          });
        }
  
        try {
          const response = await NavLogAPI.getNavLogs(example);
          this.navLogs = response.data;
  
          // 获取记录总数并更新
          const countResponse = await NavLogAPI.countNavLogs(example);
          this.totalRecords = countResponse.data;
        } catch (error) {
          console.error('获取路况日志列表失败', error);
        }
      },
  
      // 重置搜索表单
      resetSearch() {
        this.searchForm = { userId: '' };
        this.fetchNavLogs();
      },
  
      // 格式化日期
      formatDate(date) {
        return date ? new Date(date).toLocaleString() : "无";
      }
    }
  };
  </script>
  
  <style scoped>
  .app-container {
    padding: 20px;
  }
  
  .el-table {
    margin-top: 10px;
  }
  
  .el-button {
    margin-right: 5px;
  }
  
  .search-form {
    margin-bottom: 0px;
  }
  
  .record-count {
    font-size: 14px;
    color: #606266;
  }
  
  .action-row {
    margin-bottom: 20px;
  }
  </style>
  