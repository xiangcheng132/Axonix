<template>
  <div class="app-container">
    <!-- 搜索表单 -->
    <el-form :inline="true" class="demo-form-inline">
      <el-form-item label="用户ID">
        <el-input v-model="filters.userId" placeholder="输入用户ID"></el-input>
      </el-form-item>
      <el-form-item label="好友用户ID">
        <el-input v-model="filters.friendUserId" placeholder="输入好友用户ID"></el-input>
      </el-form-item>
      <el-form-item label="状态">
        <el-select v-model="filters.status" placeholder="选择状态">
          <el-option label="待处理" value="pending"></el-option>
          <el-option label="已接受" value="accepted"></el-option>
          <el-option label="已拒绝" value="blocked"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="fetchFriendRelationships">查询</el-button>
        <el-button @click="resetSearch">重置</el-button>
      </el-form-item>
    </el-form>

    <!-- 添加好友关系按钮 -->
    <el-button type="primary" @click="handleAdd">添加好友关系</el-button>

    <!-- 批量删除按钮 -->
    <el-button type="danger" @click="confirmBatchDelete" :disabled="selectedFriendRelationships.length === 0">批量删除</el-button>

    <!-- 好友关系表格 -->
    <el-table :data="friendRelationships" border @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" />
      <el-table-column prop="id" label="ID" width="80" />
      <el-table-column prop="userId" label="用户ID" />
      <el-table-column prop="friendUserId" label="好友用户ID" />
      <el-table-column prop="status" label="状态">
        <template slot-scope="scope">
          <el-tag :type="getStatusTag(scope.row.status)">
            {{ formatStatus(scope.row.status) }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="createdtime" label="创建时间">
        <template slot-scope="scope">
          {{ formatDate(scope.row.createdtime) }}
        </template>
      </el-table-column>
      <el-table-column prop="updatedtime" label="更新时间">
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
import FriendRelationshipApi from '@/api/FriendRelationship_api';

export default {
  data() {
    return {
      friendRelationships: [],
      selectedFriendRelationships: [], // 存储选中的好友关系记录ID
      filters: {
        userId: '',
        friendUserId: '',
        status: ''
      }
    };
  },
  created() {
    this.fetchFriendRelationships();
  },
  methods: {
    // 查询好友关系
    async fetchFriendRelationships() {
      const query = {
        oredCriteria: [{ criteria: [] }]
      };

      // 过滤条件
      if (this.filters.userId) {
        query.oredCriteria[0].criteria.push({
          condition: "user_id LIKE",
          value: `%${this.filters.userId}%`,
          singleValue: true
        });
      }

      if (this.filters.friendUserId) {
        query.oredCriteria[0].criteria.push({
          condition: "friend_user_id LIKE",
          value: `%${this.filters.friendUserId}%`,
          singleValue: true
        });
      }

      if (this.filters.status) {
        query.oredCriteria[0].criteria.push({
          condition: "status =",
          value: this.filters.status,
          singleValue: true
        });
      }

      try {
        const response = await FriendRelationshipApi.getFriendRelationships(query);
        this.friendRelationships = response.data;
      } catch (error) {
        console.error('获取好友关系失败', error);
      }
    },

    // 重置搜索
    resetSearch() {
      this.filters.userId = '';
      this.filters.friendUserId = '';
      this.filters.status = '';
      this.fetchFriendRelationships();
    },

    // 处理表格多选
    handleSelectionChange(selection) {
      this.selectedFriendRelationships = selection.map(item => item.id);
    },

    // 添加好友关系
    handleAdd() {
      this.$router.push('/add-friend-relationship');
    },

    // 查看好友关系
    handleView(friendRelationship) {
      this.$router.push({ path: '/edit-friend-relationship', query: { id: friendRelationship.id } });
    },

    // 删除好友关系
    async handleDelete(id) {
      try {
        await FriendRelationshipApi.deleteFriendRelationship(id);
        this.fetchFriendRelationships();
        this.$message.success('好友关系删除成功');
      } catch (error) {
        console.error('删除好友关系失败', error);
      }
    },

    // 批量删除操作
    confirmBatchDelete() {
      if (this.selectedFriendRelationships.length === 0) return;

      this.$confirm('确定要删除选中的好友关系记录吗？', '警告', {
        type: 'warning'
      }).then(() => {
        this.handleBatchDelete();
      }).catch(() => {});
    },

    async handleBatchDelete() {
      try {
        await Promise.all(this.selectedFriendRelationships.map(id => FriendRelationshipApi.deleteFriendRelationship(id)));
        this.fetchFriendRelationships();
        this.selectedFriendRelationships = [];
        this.$message.success('批量删除成功');
      } catch (error) {
        console.error('批量删除失败', error);
      }
    },

    // 格式化状态
    formatStatus(status) {
      const statusMap = {
        pending: '待处理',
        accepted: '已接受',
        blocked: '已拒绝'
      };
      return statusMap[status] || '未知';
    },

    // 获取状态标签类型
    getStatusTag(status) {
      const tagMap = {
        pending: 'warning',
        accepted: 'success',
        blocked: 'danger'
      };
      return tagMap[status] || 'info';
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
