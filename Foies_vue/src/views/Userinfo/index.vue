<template>
  <div class="app-container">
    <!-- 搜索表单 -->
    <el-form :inline="true" :model="searchForm" class="search-form">
      <el-form-item label="用户名">
        <el-input v-model="searchForm.username" placeholder="请输入用户名" clearable />
      </el-form-item>
      <el-form-item label="电话">
        <el-input v-model="searchForm.phone" placeholder="请输入电话" clearable />
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="fetchUsers">查询</el-button>
        <el-button @click="resetSearch">重置</el-button>
      </el-form-item>
    </el-form>

    <!-- 操作按钮 -->
    <el-button type="primary" @click="handleAdd">添加用户</el-button>
    <el-button type="danger" @click="confirmBatchDelete" :disabled="selectedUsers.length === 0">批量删除</el-button>

    <!-- 用户表格 -->
    <el-table :data="users" border @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" />
      <el-table-column prop="id" label="ID" width="80" />
      <el-table-column prop="username" label="用户名" width="120" />
      <el-table-column prop="avatar" label="头像" width="80">
        <template slot-scope="scope">
          <el-avatar :src="scope.row.avatar" size="small" />
        </template>
      </el-table-column>
      <el-table-column prop="gender" label="性别" width="80">
        <template slot-scope="scope">{{ formatGender(scope.row.gender) }}</template>
      </el-table-column>
      <el-table-column prop="age" label="年龄" width="80" />
      <el-table-column prop="province" label="省份" width="100" />
      <el-table-column prop="city" label="城市" width="100" />
      <el-table-column prop="county" label="县" width="100" />
      <el-table-column prop="address" label="详细地址" width="180" show-overflow-tooltip />
      <el-table-column prop="email" label="邮箱" width="180" show-overflow-tooltip />
      <el-table-column prop="phone" label="电话" width="140" />
      <el-table-column prop="disabilityType" label="残疾类型" width="120">
        <template slot-scope="scope">{{ formatDisability(scope.row.disabilityType) }}</template>
      </el-table-column>
      <el-table-column prop="isVip" label="VIP" width="80">
        <template slot-scope="scope">{{ scope.row.isVip ? "是" : "否" }}</template>
      </el-table-column>
      <el-table-column prop="lastLoginTime" label="最后登录时间" width="180">
        <template slot-scope="scope">{{ formatDate(scope.row.lastLoginTime) }}</template>
      </el-table-column>
      <el-table-column prop="createdAt" label="创建时间" width="180">
        <template slot-scope="scope">{{ formatDate(scope.row.createdAt) }}</template>
      </el-table-column>
      <el-table-column prop="updatedAt" label="更新时间" width="180">
        <template slot-scope="scope">{{ formatDate(scope.row.updatedAt) }}</template>
      </el-table-column>
      <el-table-column label="操作" width="160" fixed="right">
        <template slot-scope="scope">
          <el-button size="mini" @click="handleEdit(scope.row)">编辑</el-button>
          <el-button size="mini" type="danger" @click="handleDelete(scope.row.id)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <!-- 添加/编辑用户弹出框 -->
    <el-dialog :visible.sync="dialogVisible" title="用户信息" width="600px">
      <el-form ref="userForm" :model="user" :rules="rules" label-width="100px">
        <el-form-item label="用户名" prop="username">
          <el-input v-model="user.username" />
        </el-form-item>

        <el-form-item label="密码" prop="password">
          <el-input v-model="user.password" type="password" />
        </el-form-item>

        <el-form-item label="邮箱" prop="email">
          <el-input v-model="user.email" />
        </el-form-item>

        <el-form-item label="电话" prop="phone">
          <el-input v-model="user.phone" />
        </el-form-item>

        <el-form-item label="性别" prop="gender">
          <el-select v-model="user.gender">
            <el-option label="男" :value="1" />
            <el-option label="女" :value="2" />
            <el-option label="未知" :value="0" />
          </el-select>
        </el-form-item>

        <el-form-item label="年龄" prop="age">
          <el-input-number v-model="user.age" :min="0" :max="120" />
        </el-form-item>

        <el-form-item label="省份" prop="province">
          <el-input v-model="user.province" />
        </el-form-item>

        <el-form-item label="城市" prop="city">
          <el-input v-model="user.city" />
        </el-form-item>

        <el-form-item label="县" prop="county">
          <el-input v-model="user.county" />
        </el-form-item>

        <el-form-item label="详细地址" prop="address">
          <el-input v-model="user.address" />
        </el-form-item>

        <el-form-item label="残疾类型" prop="disabilityType">
          <el-select v-model="user.disabilityType">
            <el-option label="无" :value="0" />
            <el-option label="视障" :value="1" />
            <el-option label="听障" :value="2" />
            <el-option label="其他障碍" :value="3" />
          </el-select>
        </el-form-item>

        <el-form-item label="是否 VIP" prop="isVip">
          <el-switch v-model="user.isVip" :active-value="1" :inactive-value="0" />
        </el-form-item>

        <el-form-item>
          <el-button type="primary" @click="submitForm">保存</el-button>
          <el-button @click="closeDialog">取消</el-button>
        </el-form-item>
      </el-form>
    </el-dialog>
  </div>
</template>

<script>
import UserAPI from '@/api/user_api';

export default {
  data() {
    return {
      users: [],
      selectedUsers: [],
      searchForm: {
        username: '',
        phone: ''
      },
      dialogVisible: false,  // 控制弹出框显示
      user: {
        id: null,
        username: '',
        password: '',
        email: '',
        phone: '',
        gender: 0,
        age: 0,
        province: '',
        city: '',
        county: '',
        address: '',
        disabilityType: 0,
        isVip: 0,
        createdAt: '',
        updatedAt: '',
      },
      rules: {
        username: [{ required: true, message: '请输入用户名', trigger: 'blur' }],
        password: [{ required: true, message: '请输入密码', trigger: 'blur' }],
        email: [{ required: true, type: 'email', message: '请输入有效邮箱', trigger: 'blur' }],
        phone: [{ required: true, message: '请输入电话', trigger: 'blur' }]
      }
    };
  },
  created() {
    this.fetchUsers();
  },
  methods: {
    async fetchUsers() {
  const example = {
    oredCriteria: [{ criteria: [] }]
  };

  if (this.searchForm.username) {
    example.oredCriteria[0].criteria.push({
      condition: "username LIKE",
      value: `%${this.searchForm.username}%`,
      singleValue: true
    });
  }

  if (this.searchForm.phone) {
    example.oredCriteria[0].criteria.push({
      condition: "phone LIKE",
      value: `%${this.searchForm.phone}%`,
      singleValue: true
    });
  }

  try {
    const response = await UserAPI.getUsers(example);
    console.log("Response data type:", Array.isArray(response.data)); // 检查是否为数组
    console.log("Response data:", response.data); // 打印返回的数据
    this.users = response.data;
  } catch (error) {
    console.error('获取用户列表失败', error);
  }
},

    resetSearch() {
      this.searchForm = { username: '', phone: '' };
      this.fetchUsers();
    },

    handleSelectionChange(selection) {
      this.selectedUsers = selection.map(user => user.id);
    },

    handleAdd() {
      const now = new Date().toISOString(); // 获取当前时间
      this.user = { // 重置用户数据以便添加新用户
        id: null,
        username: '',
        password: '',
        email: '',
        phone: '',
        gender: 0,
        age: 0,
        province: '',
        city: '',
        county: '',
        address: '',
        disabilityType: 0,
        isVip: 0,
        createdAt: now, // 自动写入当前时间为创建时间
        updatedAt: ''
      };
      this.dialogVisible = true;
    },


    handleEdit(user) {
      this.user = { ...user };  // 填充要编辑的用户数据
      this.dialogVisible = true;
    },

    async submitForm() {
      this.$refs.userForm.validate(async (valid) => {
        if (valid) {
          const now = new Date().toISOString();
          this.user.updatedAt = now;

          try {
            if (this.user.id) {
              await UserAPI.updateUser(this.user);  // 更新现有用户
            } else {
              await UserAPI.addUser(this.user);  // 添加新用户
            }
            this.$message.success('用户信息保存成功');
            this.dialogVisible = false;
            this.fetchUsers();
          } catch (error) {
            this.$message.error('保存用户信息失败');
            console.error(error);
          }
        }
      });
    },

    closeDialog() {
      this.dialogVisible = false;
    },

    confirmBatchDelete() {
      if (this.selectedUsers.length === 0) return;
      this.$confirm('确定要删除选中的用户吗？', '警告', { type: 'warning' })
        .then(() => this.handleBatchDelete())
        .catch(() => { });
    },

    async handleBatchDelete() {
      try {
        await Promise.all(this.selectedUsers.map(id => UserAPI.deleteUser(id)));
        this.fetchUsers();
        this.selectedUsers = [];
        this.$message.success('批量删除成功');
      } catch (error) {
        console.error('批量删除失败', error);
      }
    },

    formatGender(gender) {
      return gender === 1 ? "男" : gender === 2 ? "女" : "未知";
    },

    formatDisability(type) {
      const types = { 0: "无", 1: "视障", 2: "听障", 3: "其他障碍" };
      return types[type] || "未知";
    },

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
  margin-top: 20px;
}

.el-button {
  margin-right: 5px;
}

.search-form {
  margin-bottom: 0px;
}

.add-user-container {
  max-width: 600px;
  margin: 0 auto;
  padding: 20px;
  background: #fff;
  border-radius: 8px;
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
}
</style>
