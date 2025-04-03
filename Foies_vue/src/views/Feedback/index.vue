<template>
    <div class="app-container">
      <!-- 搜索表单 -->
      <el-form :inline="true" :model="searchForm" class="search-form">
        <el-form-item label="用户ID">
          <el-input v-model="searchForm.userId" placeholder="请输入用户ID" clearable />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="fetchFeedbacks">查询</el-button>
          <el-button @click="resetSearch">重置</el-button>
        </el-form-item>
      </el-form>
  
      <!-- 记录数显示 -->
      <el-row class="action-row" type="flex" justify="space-between" align="middle">
        <el-col :span="8" class="record-count">
          <span>当前共有 {{ totalRecords }} 条记录</span>
        </el-col>
      </el-row>
  
      <!-- 反馈表格 -->
      <el-table :data="feedbacks" border>
        <el-table-column prop="id" label="反馈ID" />
        <el-table-column prop="userId" label="用户ID" />
        <el-table-column prop="issue" label="反馈内容" />
        <el-table-column prop="createdAt" label="创建时间">
          <template v-slot="scope">{{ formatDate(scope.row.createdAt) }}</template>
        </el-table-column>
        <el-table-column prop="status" label="状态">
          <template v-slot="scope">
            <el-select v-model="scope.row.status" @change="updateStatus(scope.row)">
              <el-option v-for="item in statusOptions" :key="item.value" :label="item.label" :value="item.value" />
            </el-select>
          </template>
        </el-table-column>
      </el-table>
    </div>
  </template>
  
  <script>
  import FeedbackAPI from '@/api/feedback_api';
  
  export default {
    data() {
      return {
        feedbacks: [],        // 反馈数据
        totalRecords: 0,      // 总记录数
        searchForm: { userId: '' }, // 搜索表单
        statusOptions: [      // 状态选项
          { value: 0, label: '待处理' },
          { value: 1, label: '处理中' },
          { value: 2, label: '已解决' }
        ]
      };
    },
  
    created() {
      this.fetchFeedbacks();
    },
  
    methods: {
      // 获取反馈数据
      async fetchFeedbacks() {
        const example = { oredCriteria: [{ criteria: [] }] };
        if (this.searchForm.userId) {
          example.oredCriteria[0].criteria.push({
            condition: "user_id =",
            value: this.searchForm.userId,
            singleValue: true
          });
        }
        try {
          const response = await FeedbackAPI.getFeedbacksByExampleWithBlobs(example);
          this.feedbacks = response.data;
          const countResponse = await FeedbackAPI.countFeedbacks(example);
          this.totalRecords = countResponse.data;
        } catch (error) {
          console.error('获取反馈列表失败', error);
        }
      },
  
      // 重置搜索
      resetSearch() {
        this.searchForm = { userId: '' };
        this.fetchFeedbacks();
      },
  
      // 修改状态
      async updateStatus(row) {
  try {
    // 准备要更新的反馈记录
    const feedback = { id: row.id, status: row.status };
    
    // 调用 API 进行状态更新
    await FeedbackAPI.updateFeedbackSelective(feedback);
    
    // 更新成功后的反馈信息
    this.$message.success('状态更新成功');
  } catch (error) {
    // 如果发生错误，显示错误信息
    this.$message.error('状态更新失败');
    console.error('状态更新错误', error);
  }
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
  