<template>
  <div class="app-container">
    <!-- 搜索表单 -->
    <el-form :inline="true" class="demo-form-inline">
      <el-form-item label="用户ID">
        <el-input v-model="filters.userId" placeholder="输入用户ID"></el-input>
      </el-form-item>
      <el-form-item label="标题关键词">
        <el-input v-model="filters.titleKeyword" placeholder="输入标题关键词"></el-input>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="fetchForumPosts">查询</el-button>
        <el-button @click="resetSearch">重置</el-button>
      </el-form-item>
    </el-form>

    <!-- 添加帖子按钮 -->
    <el-button type="primary" @click="handleAdd">添加帖子</el-button>

    <!-- 批量删除按钮 -->
    <el-button type="danger" @click="confirmBatchDelete" :disabled="selectedPosts.length === 0">批量删除</el-button>

    <el-table :data="forumPosts" border @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" />
      <el-table-column prop="id" label="ID" width="80" />
      <el-table-column prop="userId" label="用户ID" />
      <el-table-column prop="title" label="标题" />
      <el-table-column prop="commentCount" label="评论数" />
      <el-table-column prop="createdtime" label="创建时间">
        <template slot-scope="scope">
          {{ formatDate(scope.row.createdtime) }}
        </template>
      </el-table-column>
      <el-table-column prop="updatedtime" label="最后一次修改时间">
        <template slot-scope="scope">
          {{ formatDate(scope.row.updatedtime) }}
        </template>
      </el-table-column>
      <el-table-column label="操作" width="200">
        <template slot-scope="scope">
          <el-button size="mini" @click="handleView(scope.row)">编辑</el-button>
          <el-button size="mini" type="danger" @click="handleDelete(scope.row.id)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>
  </div>
</template>

<script>
import ForumPostApi from '@/api/ForumPost_api';
import ForumCommentApi from '@/api/ForumComment_api';

export default {
  data() {
    return {
      forumPosts: [],
      selectedPosts: [], // 存储选中的帖子记录ID
      filters: {
        userId: '',
        titleKeyword: '',
        minCommentCount: 0
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

      if (this.filters.userId) {
        example.oredCriteria[0].criteria.push({
          condition: "user_id LIKE",
          value: `%${this.filters.userId}%`,
          singleValue: true
        });
      }

      if (this.filters.titleKeyword) {
        example.oredCriteria[0].criteria.push({
          condition: "title LIKE",
          value: `%${this.filters.titleKeyword}%`,
          singleValue: true
        });
      }


      try {
        const response = await ForumPostApi.getPosts(example);
        this.forumPosts = await Promise.all(
          response.data.map(async (post) => {
            const commentCount = await ForumCommentApi.getCommentCount({ postId: post.id });
            return { ...post, commentCount: commentCount.data };
          })
        );
      } catch (error) {
        console.error('获取帖子失败', error);
      }
    },

    handleAdd() {
      this.$router.push('/add-forum-post');
    },

    handleView(post) {
      this.$router.push({ name: 'ForumPostDetail', params: { id: post.id } });
    },

    async handleDelete(id) {
      try {
        await ForumPostApi.deletePost(id);
        this.fetchForumPosts();
        this.$message.success('帖子删除成功');
      } catch (error) {
        console.error('删除帖子失败', error);
      }
    },

    handleSelectionChange(selection) {
      this.selectedPosts = selection.map(item => item.id);
    },

    confirmBatchDelete() {
      if (this.selectedPosts.length === 0) return;

      this.$confirm('确定要删除选中的帖子记录吗？', '警告', {
        type: 'warning'
      }).then(() => {
        this.handleBatchDelete();
      }).catch(() => { });
    },

    async handleBatchDelete() {
      try {
        await Promise.all(this.selectedPosts.map(id => ForumPostApi.deletePost(id)));
        this.fetchForumPosts();
        this.selectedPosts = [];
        this.$message.success('批量删除成功');
      } catch (error) {
        console.error('批量删除失败', error);
      }
    },

    formatDate(date) {
      return date ? new Date(date).toLocaleString() : '无';
    },

    resetSearch() {
      this.filters.userId = '';
      this.filters.titleKeyword = '';
      this.filters.minCommentCount = 0;
      this.fetchForumPosts();
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