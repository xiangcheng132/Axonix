<template>
  <div class="app-container">
    <!-- 搜索表单 -->
    <el-form :inline="true" :model="searchForm" class="search-form">
      <el-form-item label="用户名">
        <el-input v-model="searchForm.username" placeholder="请输入用户名" clearable />
      </el-form-item>
      <el-form-item label="邮箱">
        <el-input v-model="searchForm.email" placeholder="请输入邮箱" clearable />
      </el-form-item>
      <el-form-item label="电话">
        <el-input v-model="searchForm.phone" placeholder="请输入电话" clearable />
      </el-form-item>
      <el-form-item label="角色">
        <el-select v-model="searchForm.role" placeholder="请选择角色" clearable>
          <el-option label="管理员" value="admin" />
          <el-option label="用户" value="user" />
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="fetchUsers">查询</el-button>
        <el-button @click="resetSearch">重置</el-button>
      </el-form-item>
    </el-form>

    <el-button type="primary" @click="handleAdd">添加用户</el-button>
    <el-button type="danger" @click="confirmBatchDelete" :disabled="selectedUsers.length === 0">批量删除</el-button>

    <el-table :data="users" border @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" />
      <el-table-column prop="id" label="ID" width="80" />
      <el-table-column prop="username" label="用户名" />
      <el-table-column prop="email" label="邮箱" />
      <el-table-column prop="phone" label="电话" />
      <el-table-column prop="disabilityType" label="残疾类型">
        <template slot-scope="scope">{{ scope.row.disabilityType || "无" }}</template>
      </el-table-column>
      <el-table-column prop="preferences" label="语言偏好">
        <template slot-scope="scope">{{ scope.row.preferences.language || "无" }}</template>
      </el-table-column>
      <el-table-column prop="createdtime" label="创建时间">
        <template slot-scope="scope">{{ formatDate(scope.row.createdtime) }}</template>
      </el-table-column>
      <el-table-column prop="updatedtime" label="最后一次更新时间">
        <template slot-scope="scope">{{ formatDate(scope.row.updatedtime) }}</template>
      </el-table-column>
      <el-table-column prop="role" label="角色" width="100">
        <template slot-scope="scope">{{ formatRole(scope.row.role) }}</template>
      </el-table-column>
      <el-table-column label="操作" width="200">
        <template slot-scope="scope">
          <el-button size="mini" @click="handleEdit(scope.row)">编辑</el-button>
          <el-button size="mini" type="danger" @click="handleDelete(scope.row.id)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>
  </div>
</template>

<script>
import UserAPI from '@/api/user_api';

export default {
  data() {
    return {
      users: [],
      selectedUsers: [], // 存储选中的用户 ID
      searchForm: {  // 存储查询条件
        username: '',
        email: '',
        phone: '',
        role: ''
      }
    };
  },
  created() {
    this.fetchUsers();
  },
  methods: {
    async fetchUsers() {
      try {
        const criteria = [];
        
        // 添加查询条件
        if (this.searchForm.username) {
          criteria.push({ condition: "username LIKE", value: `%${this.searchForm.username}%`, singleValue: true });
        }
        if (this.searchForm.email) {
          criteria.push({ condition: "email LIKE", value: `%${this.searchForm.email}%`, singleValue: true });
        }
        if (this.searchForm.phone) {
          criteria.push({ condition: "phone LIKE", value: `%${this.searchForm.phone}%`, singleValue: true });
        }
        if (this.searchForm.role) {
          criteria.push({ condition: "role =", value: this.searchForm.role, singleValue: true });
        }

        const requestData = {
          orderByClause: "id ASC",
          distinct: false,
          oredCriteria: criteria.length > 0 ? [{ criteria }] : []
        };

        const response = await UserAPI.getUsers(requestData);
        this.users = response.data.map(user => ({
          ...user,
          preferences: typeof user.preferences === 'string' ? JSON.parse(user.preferences) : (user.preferences || { language: '无' })
        }));
      } catch (error) {
        console.error('获取用户列表失败', error);
      }
    },

    handleAdd() {
      this.$router.push('/add-user');
    },

    handleEdit(user) {
      this.$router.push({ path: '/edit-user', query: { id: user.id } });
    },

    async handleDelete(id) {
      try {
        await UserAPI.deleteUser(id);
        this.fetchUsers();
      } catch (error) {
        console.error('删除用户失败，存在与用户关联的记录，请先删除该用户的所有记录！', error);
      }
    },

    confirmBatchDelete() {
      if (this.selectedUsers.length === 0) return;

      this.$confirm('确定要删除选中的用户吗？', '警告', {
        type: 'warning'
      }).then(() => {
        this.handleBatchDelete();
      }).catch(() => {});
    },

    // 批量删除用户
    async handleBatchDelete() {
      try {
        await Promise.all(this.selectedUsers.map(id => UserAPI.deleteUser(id)));
        this.fetchUsers();
        this.selectedUsers = []; // 清空选中的用户
        this.$message.success('批量删除成功');
      } catch (error) {
        console.error('批量删除失败', error);
      }
    },

    // 监听多选框的变化
    handleSelectionChange(selection) {
      this.selectedUsers = selection.map(user => user.id);
    },

    formatDate(date) {
      return date ? new Date(date).toLocaleString() : '无';
    },

    formatRole(role) {
      return role === 'admin' ? '管理员' : role === 'user' ? '用户' : role;
    },

    // 重置查询条件
    resetSearch() {
      this.searchForm = { username: '', email: '', phone: '', role: '' };
      this.fetchUsers();
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

.search-form {
  margin-bottom: 20px;
}
</style>
