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
    <el-table :data="comments" border @selection-change="handleSelectionChange" :empty-text="'没有数据'">
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
            <el-option v-for="(label, value) in statusMap" :key="value" :label="label" :value="value" />
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
      comments: [],
      totalRecords: 0,
      selectedComments: [],
      searchForm: {
        content: '',
        userId: '',
        postId: ''
      },
      dialogVisible: false,
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
    this.fetchComments();
  },
  methods: {
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
          condition: "user_id =",
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
        this.totalRecords = countResponse.data;
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
      this.$confirm('确定要删除这条评论吗？', '提示', {
        type: 'warning',
        cancelButtonText: '取消',
        confirmButtonText: '确定',
        beforeClose: (action, instance, done) => {
          if (action === 'confirm') {
            instance.confirmButtonLoading = true;
            CommentAPI.deleteComment(id)
              .then(() => {
                this.$message.success('删除成功');
                this.fetchComments();
                done();
              })
              .catch(error => {
                this.$message.error('删除失败');
                console.error(error);
              })
              .finally(() => {
                instance.confirmButtonLoading = false;
              });
          } else {
            done();
          }
        }
      }).catch(() => {
        this.$message.info('已取消删除');
      });
    },

    async confirmBatchDelete() {
      if (this.selectedComments.length === 0) {
        this.$message.warning('请选择要删除的评论');
        return;
      }

      this.$confirm('确定要删除选中的评论吗？', '警告', {
        type: 'warning',
        cancelButtonText: '取消',
        confirmButtonText: '确定'
      })
        .then(async () => {
          try {
            await Promise.all(this.selectedComments.map(id => CommentAPI.deleteComment(id)));
            this.fetchComments();
            this.selectedComments = [];
            this.$message.success('批量删除成功');
          } catch (error) {
            console.error('批量删除失败', error);
            this.$message.error('批量删除失败');
          }
        })
        .catch(() => { });
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
