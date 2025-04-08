<template>
  <div class="app-container">
    <!-- 搜索表单 -->
    <el-form :inline="true" :model="searchForm" class="search-form">
      <el-form-item label="帖子标题">
        <el-input v-model="searchForm.title" placeholder="请输入帖子标题" clearable />
      </el-form-item>
      <el-form-item label="用户ID">
        <el-input v-model="searchForm.userId" placeholder="请输入用户ID" clearable />
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="fetchForumPosts">查询</el-button>
        <el-button @click="resetSearch">重置</el-button>
      </el-form-item>
    </el-form>

    <!-- 操作按钮和记录数显示 -->
    <el-row class="action-row" type="flex" justify="space-between" align="middle">
      <el-col :span="8">
        <el-button type="danger" @click="confirmBatchDelete" :disabled="selectedPosts.length === 0">批量删除</el-button>
      </el-col>
      <el-col :span="8" class="record-count" style="text-align: right;">
        <span>当前共有 {{ totalRecords }} 条记录</span>
      </el-col>
    </el-row>

    <!-- 帖子表格 -->
    <el-table :data="posts" border @selection-change="handleSelectionChange":empty-text="'没有数据'">
      <el-table-column type="selection" width="55" />
      <el-table-column prop="id" label="ID" width="80" />
      <el-table-column prop="title" label="标题" width="200" />
      <el-table-column prop="userId" label="用户ID" width="100" />
      <el-table-column prop="likes" label="点赞数" width="100" />
      <el-table-column prop="dislikes" label="点踩数" width="100" />
      <el-table-column prop="createdAt" label="创建时间" width="180">
        <template slot-scope="scope">
          {{ formatDate(scope.row.createdAt) }}
        </template>
      </el-table-column>
      <el-table-column prop="updatedAt" label="更新时间" width="180">
        <template slot-scope="scope">
          {{ formatDate(scope.row.updatedAt) }}
        </template>
      </el-table-column>
      <el-table-column prop="status" label="审核状态" width="120">
        <template slot-scope="scope">
          <el-select v-model="scope.row.status" @change="updatePostStatus(scope.row)" size="small">
            <el-option label="审核中" :value="0" />
            <el-option label="驳回" :value="1" />
            <el-option label="异常" :value="2" />
            <el-option label="已发布" :value="3" />
            <el-option label="私密" :value="4" />
          </el-select>
        </template>
      </el-table-column>
      <el-table-column label="操作" width="160" fixed="right">
        <template slot-scope="scope">
          <el-button size="mini" @click="handleView(scope.row)">查看</el-button>
          <el-button size="mini" type="danger" @click="handleDelete(scope.row.id)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <!-- 查看帖子弹出框 -->
    <el-dialog :visible.sync="dialogVisible" title="帖子详情" width="800px">
      <div>
        <el-form :model="currentPost" label-width="100px">
          <el-form-item label="帖子标题">
            <el-input v-model="currentPost.title" readonly />
          </el-form-item>
          <el-form-item label="用户ID">
            <el-input v-model="currentPost.userId" readonly />
          </el-form-item>
          <el-form-item label="点赞数">
            <el-input v-model="currentPost.likes" readonly />
          </el-form-item>
          <el-form-item label="点踩数">
            <el-input v-model="currentPost.dislikes" readonly />
          </el-form-item>
          <el-form-item label="内容">
            <el-input type="textarea" v-model="currentPost.content" rows="6" readonly />
          </el-form-item>
        </el-form>
      </div>
      <span slot="footer" class="dialog-footer">
        <el-button @click="closeDialog">关闭</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
import PostinfoAPI from '@/api/postinfo_api';

export default {
  data() {
    return {
      posts: [],
      selectedPosts: [],
      totalRecords: 0,
      searchForm: {
        title: '',
        userId: ''
      },
      dialogVisible: false,
      currentPost: {
        id: null,
        title: '',
        userId: '',
        likes: 0,
        dislikes: 0,
        createdAt: '',
        updatedAt: '',
        content: ''
      }
    };
  },

  created() {
    this.fetchForumPosts();
  },

  methods: {
    async fetchForumPosts() {
      const example = {
        oredCriteria: [{ criteria: [] }]
      };

      if (this.searchForm.title) {
        example.oredCriteria[0].criteria.push({
          condition: "title LIKE",
          value: `%${this.searchForm.title}%`,
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

      try {
        const response = await PostinfoAPI.getForumPostsWithBlobs(example);
        this.posts = response.data;

        const countResponse = await PostinfoAPI.countForumPosts(example);
        this.totalRecords = countResponse.data;
      } catch (error) {
        console.error('获取帖子列表失败', error);
      }
    },

    resetSearch() {
      this.searchForm = { title: '', userId: '' };
      this.fetchForumPosts();
    },

    handleSelectionChange(selection) {
      this.selectedPosts = selection.map(post => post.id);
    },

    formatDate(date) {
      const d = new Date(date);
      return `${d.getFullYear()}/${d.getMonth() + 1}/${d.getDate()} ${d.getHours()}:${d.getMinutes()}:${d.getSeconds()}`;
    },

    async updatePostStatus(post) {
      try {
        await PostinfoAPI.updateForumPost(post);
        this.$message.success('帖子状态更新成功');
        post.updatedAt = new Date();
      } catch (error) {
        console.error('更新帖子状态失败', error);
        this.$message.error('更新帖子状态失败');
      }
    },

    async handleDelete(postId) {
      this.$confirm('确定要删除该帖子吗？', '警告', { 
        type: 'warning',
        cancelButtonText: '取消',
        confirmButtonText: '确定' })
        .then(async () => {
          try {
            await PostinfoAPI.deleteForumPost(postId);
            this.$message.success('删除成功');
            this.fetchForumPosts();
          } catch (error) {
            console.error('删除失败', error);
            this.$message.error('删除失败');
          }
        })
        .catch(() => { });
    },

    async confirmBatchDelete() {
      if (this.selectedPosts.length === 0) {
        this.$message.warning('请选择要删除的帖子');
        return;
      }

      this.$confirm('确定要删除选中的帖子吗？', '警告', {
        type: 'warning',
        cancelButtonText: '取消',  // 设置取消按钮文本
        confirmButtonText: '确定'   // 设置确定按钮文本
      })
        .then(async () => {
          try {
            await Promise.all(this.selectedPosts.map(id => PostinfoAPI.deleteForumPost(id)));
            this.fetchForumPosts();
            this.selectedPosts = [];
            this.$message.success('批量删除成功');
          } catch (error) {
            console.error('批量删除失败', error);
            this.$message.error('批量删除失败');
          }
        })
        .catch(() => { });
    },

    async handleView(post) {
      try {
        const response = await PostinfoAPI.getForumPostDetail(post.id);
        this.currentPost = response.data;
        this.dialogVisible = true;
      } catch (error) {
        console.error('获取帖子详情失败', error);
        this.$message.error('获取帖子详情失败');
      }
    },

    closeDialog() {
      this.dialogVisible = false;
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

.dialog-footer {
  text-align: right;
}
</style>