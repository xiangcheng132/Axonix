<template>
    <div class="app-container">
      <!-- 添加好友关系按钮 -->
      <el-button type="primary" @click="handleAdd">添加好友关系</el-button>
  
      <!-- 批量删除按钮 -->
      <el-button type="danger" @click="confirmBatchDelete" :disabled="selectedFriendRelationships.length === 0">批量删除</el-button>
  
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
            <el-button size="mini" @click="handleView(scope.row)">查看</el-button>
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
      selectedFriendRelationships: [] // 存储选中的好友关系记录ID
    };
  },
  created() {
    this.fetchFriendRelationships();
  },
  methods: {
    async fetchFriendRelationships() {
      try {
        const response = await FriendRelationshipApi.getFriendRelationships();
        this.friendRelationships = response.data;
      } catch (error) {
        console.error('获取好友关系失败', error);
      }
    },
    handleAdd() {
      this.$router.push('/add-friend-relationship');
    },
    handleView(friendRelationship) {
      this.$router.push({ path: '/edit-friend-relationship', query: { id: friendRelationship.id } });
    },
    formatStatus(status) {
      const statusMap = {
        pending: '待处理',
        accepted: '已接受',
        blocked: '已拒绝'
      };
      return statusMap[status] || '未知';
    },
    getStatusTag(status) {
      const tagMap = {
        pending: 'warning',
        accepted: 'success',
        blocked: 'danger'
      };
      return tagMap[status] || 'info';
    },
    async handleDelete(id) {
      try {
        await FriendRelationshipApi.deleteFriendRelationship(id);
        this.fetchFriendRelationships();
        this.$message.success('好友关系删除成功');
      } catch (error) {
        console.error('删除好友关系失败', error);
      }
    },

    // 监听表格多选
    handleSelectionChange(selection) {
      this.selectedFriendRelationships = selection.map(item => item.id);
    },

    // 批量删除前的确认操作
    confirmBatchDelete() {
      if (this.selectedFriendRelationships.length === 0) return;

      this.$confirm('确定要删除选中的好友关系记录吗？', '警告', {
        type: 'warning'
      }).then(() => {
        this.handleBatchDelete();
      }).catch(() => {});
    },

    // 处理批量删除
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
