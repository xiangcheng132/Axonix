<template>
  <div class="app-container">
    <el-button type="primary" @click="handleAdd">添加用户</el-button>
    <el-table :data="users" border>
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
import UserAPI from '@/api/user_api'

export default {
  data() {
    return {
      users: []
    }
  },
  created() {
    this.fetchUsers()
  },
  methods: {
    async fetchUsers() {
      try {
        const response = await UserAPI.getUsers()
        this.users = response.data.map(user => ({
          ...user,
          preferences: typeof user.preferences === 'string' ? JSON.parse(user.preferences) : (user.preferences || { language: '无' })
        }))
        console.log('用户数据：', this.users)
      } catch (error) {
        console.error('获取用户列表失败', error)
      }
    },
    handleAdd() {
      this.$router.push('/add-user')
    },
    handleEdit(user) {
      this.$router.push({ path: '/edit-user', query: { id: user.id } })
    },
    async handleDelete(id) {
      try {
        await UserAPI.deleteUser(id)
        this.fetchUsers()
      } catch (error) {
        console.error('删除用户失败，存在与用户关联的记录，请先删除该用户的所有记录！', error)
      }
    },
    formatDate(date) {
      return date ? new Date(date).toLocaleString() : '无'
    },
    formatRole(role) {
      return role === 'admin' ? '管理员' : role === 'user' ? '用户' : role;
    }
  }
}
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