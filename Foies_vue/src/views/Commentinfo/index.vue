<template>
    <div class="app-container">
      <!-- 搜索表单 -->
      <el-form :inline="true" :model="searchForm" class="search-form">
        <el-form-item label="内容">
          <el-input v-model="searchForm.content" placeholder="请输入评论内容" clearable />
        </el-form-item>
        <el-form-item label="用户ID">
          <el-input v-model="searchForm.userId" placeholder="请输入用户ID" clearable />
        </el-form-item>
        <el-form-item label="帖子ID">
          <el-input v-model="searchForm.postId" placeholder="请输入帖子ID" clearable />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="fetchComments">查询</el-button>
          <el-button @click="resetSearch">重置</el-button>
        </el-form-item>
      </el-form>
  
      <!-- 操作按钮和记录数显示 -->
      <el-row class="action-row" type="flex" justify="space-between" align="middle">
        <el-col :span="8">
          <el-button type="danger" @click="confirmBatchDelete" :disabled="selectedComments.length === 0">批量删除</el-button>
        </el-col>
        <el-col :span="8" class="record-count" style="text-align: right;">
          <span>当前共有 {{ totalRecords }} 条记录</span>
        </el-col>
      </el-row>
  
      <!-- 评论表格 -->
      <el-table :data="comments" border @selection-change="handleSelectionChange">
        <el-table-column type="selection" width="50" />
        <el-table-column prop="id" label="ID" />
        <el-table-column prop="userId" label="用户ID" />
        <el-table-column prop="postId" label="帖子ID" />
        <el-table-column prop="likes" label="点赞数" />
        <el-table-column prop="dislikes" label="点踩数" />
        <el-table-column prop="createdAt" label="创建时间">
          <template slot-scope="scope">{{ formatDate(scope.row.createdAt) }}</template>
        </el-table-column>
        <el-table-column prop="content" label="评论内容">
          <template slot-scope="scope">{{ scope.row.content }}</template>
        </el-table-column>
        <el-table-column prop="status" label="审核状态" width="120">
          <template slot-scope="scope">
            <el-select v-model="scope.row.status" @change="updateStatus(scope.row)" size="small">
              <el-option
                v-for="(label, value) in statusMap"
                :key="value"
                :label="label"
                :value="value"
              />
            </el-select>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="160" fixed="right">
          <template slot-scope="scope">
            <el-button size="mini" type="danger" @click="handleDelete(scope.row.id)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
  
      <!-- 批量删除确认框 -->
      <el-dialog :visible.sync="dialogVisible" title="确认删除" width="400px">
        <span>确定要删除选中的评论吗？</span>
        <span slot="footer" class="dialog-footer">
          <el-button @click="dialogVisible = false">取消</el-button>
          <el-button type="primary" @click="handleBatchDelete">确定</el-button>
        </span>
      </el-dialog>
    </div>
  </template>
  
  <script>
  import CommentAPI from '@/api/commentinfo_api';
  
  export default {
    data() {
      return {
        comments: [], // 存储评论列表
        totalRecords: 0, // 添加记录数
        selectedComments: [], // 选中的评论ID
        searchForm: {
          content: '', // 搜索表单内容
          userId: '', // 用户ID
          postId: ''  // 帖子ID
        },
        dialogVisible: false, // 控制删除确认框
        statusMap: {
          0: '审核中',
          1: '驳回',
          2: '异常',
          3: '已发布',
          4: '私密'
        }
      };
    },
    created() {
      this.fetchComments(); // 页面加载时获取评论列表
    },
    methods: {
      // 获取评论列表
      async fetchComments() {
        const example = {
          oredCriteria: [{ criteria: [] }]
        };
  
        if (this.searchForm.content) {
          example.oredCriteria[0].criteria.push({
            condition: "content LIKE",
            value: `%${this.searchForm.content}%`,
            singleValue: true
          });
        }
  
        if (this.searchForm.userId) {
          example.oredCriteria[0].criteria.push({
            condition: "userId =",
            value: this.searchForm.userId,
            singleValue: true
          });
        }
  
        if (this.searchForm.postId) {
          example.oredCriteria[0].criteria.push({
            condition: "postId =",
            value: this.searchForm.postId,
            singleValue: true
          });
        }
  
        try {
          const response = await CommentAPI.getCommentsByExampleWithBlobs(example);
          this.comments = response.data;
  
          const countResponse = await CommentAPI.countComments(example);
          this.totalRecords = countResponse.data; // 获取总记录数
        } catch (error) {
          console.error('获取评论列表失败', error);
        }
      },
  
      // 重置搜索
      resetSearch() {
        this.searchForm = { content: '', userId: '', postId: '' };
        this.fetchComments();
      },
  
      // 选择评论时的回调
      handleSelectionChange(selection) {
        this.selectedComments = selection.map(comment => comment.id);
      },
  
      // 删除评论
      async handleDelete(id) {
        try {
          await CommentAPI.deleteComment(id);
          this.$message.success('删除成功');
          this.fetchComments();
        } catch (error) {
          this.$message.error('删除失败');
          console.error(error);
        }
      },
  
      // 批量删除
      confirmBatchDelete() {
        if (this.selectedComments.length === 0) return;
        this.dialogVisible = true; // 显示确认删除框
      },
  
      // 批量删除评论
      async handleBatchDelete() {
        try {
          await Promise.all(this.selectedComments.map(id => CommentAPI.deleteComment(id)));
          this.fetchComments();
          this.selectedComments = [];
          this.dialogVisible = false; // 关闭确认框
          this.$message.success('批量删除成功');
        } catch (error) {
          console.error('批量删除失败', error);
        }
      },
  
      // 更新评论状态
      async updateStatus(comment) {
        try {
          await CommentAPI.updateComment(comment);
          this.$message.success('状态更新成功');
        } catch (error) {
          this.$message.error('更新状态失败');
          console.error(error);
        }
      },
  
      // 格式化时间
      formatDate(date) {
        return date ? new Date(date).toLocaleString() : "无";
      },
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
    margin-right: 5px;
  }
  
  .search-form {
    margin-bottom: 0px;
  }
  
  .record-count {
    margin-top: 20px;
    font-size: 14px;
    color: #606266;
  }
  
  .action-row {
    margin-bottom: 20px;
  }
  </style>
  