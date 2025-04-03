<template>
    <div class="app-container">
      <!-- 搜索表单 -->
      <el-form :inline="true" :model="searchForm" class="search-form">
        <el-form-item label="用户ID">
          <el-input v-model="searchForm.userId" placeholder="请输入用户ID" clearable />
        </el-form-item>
        <el-form-item label="标题">
          <el-input v-model="searchForm.title" placeholder="请输入标题" clearable />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="fetchSosInfo">查询</el-button>
          <el-button @click="resetSearch">重置</el-button>
        </el-form-item>
      </el-form>
  
      <!-- 记录数显示 -->
      <el-row class="action-row" type="flex" justify="space-between" align="middle">
        <el-col :span="8" class="record-count" style="text-align: left;">
          <span>当前共有 {{ totalRecords }} 条记录</span>
        </el-col>
      </el-row>
  
      <!-- SOS 信息表格 -->
      <el-table :data="sosInfoList" border>
        <el-table-column prop="id" label="SOS ID" />
        <el-table-column prop="userId" label="用户ID" />
        <el-table-column prop="title" label="标题" />
        <el-table-column prop="contactId" label="联系人ID" />
        <el-table-column prop="lng" label="经度" />
        <el-table-column prop="lat" label="纬度" />
        <el-table-column prop="createdAt" label="创建时间">
          <template slot-scope="scope">{{ formatDate(scope.row.createdAt) }}</template>
        </el-table-column>
        <el-table-column prop="sendTime" label="发送时间">
          <template slot-scope="scope">{{ formatDate(scope.row.sendTime) }}</template>
        </el-table-column>
        <el-table-column prop="content" label="内容" />
      </el-table>
    </div>
  </template>
  
  <script>
  import SosNotificationAPI from '@/api/sosinfo_api';
  
  export default {
    data() {
      return {
        sosInfoList: [],   // SOS 信息数据
        totalRecords: 0,   // 总记录数
        searchForm: {      // 搜索表单
          userId: '',
          title: ''
        }
      };
    },
  
    created() {
      this.fetchSosInfo();
    },
  
    methods: {
      // 获取 SOS 信息数据
      async fetchSosInfo() {
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
  
        if (this.searchForm.title) {
          example.oredCriteria[0].criteria.push({
            condition: "title LIKE",
            value: `%${this.searchForm.title}%`,
            singleValue: true
          });
        }
  
        try {
          const response = await SosNotificationAPI.getSosNotificationsByExampleWithBlobs(example);
          this.sosInfoList = response.data;
  
          // 获取记录总数并更新
          const countResponse = await SosNotificationAPI.countSosNotifications(example);
          this.totalRecords = countResponse.data;
        } catch (error) {
          console.error('获取SOS信息列表失败', error);
        }
      },
  
      // 重置搜索表单
      resetSearch() {
        this.searchForm = { userId: '', title: '' };
        this.fetchSosInfo();
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
  