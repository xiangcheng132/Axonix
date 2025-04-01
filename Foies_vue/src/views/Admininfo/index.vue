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
          <el-button type="primary" @click="fetchAdmins">查询</el-button>
          <el-button @click="resetSearch">重置</el-button>
        </el-form-item>
      </el-form>
  
      <!-- 操作按钮 -->
      <el-button type="primary" @click="handleAdd">添加管理员</el-button>
      <el-button type="danger" @click="confirmBatchDelete" :disabled="selectedAdmins.length === 0">批量删除</el-button>
  
      <!-- 管理员表格 -->
      <el-table :data="admins" border @selection-change="handleSelectionChange">
        <el-table-column type="selection" width="50"/>
        <el-table-column prop="id" label="ID"/>
        <el-table-column prop="username" label="用户名"/>
        <el-table-column prop="phone" label="电话"/>
        <el-table-column prop="createdAt" label="创建时间">
          <template slot-scope="scope">{{ formatDate(scope.row.createdAt) }}</template>
        </el-table-column>
        <el-table-column prop="updatedAt" label="更新时间">
          <template slot-scope="scope">{{ formatDate(scope.row.updatedAt) }}</template>
        </el-table-column>
        <el-table-column label="操作" width="160" fixed="right">
          <template slot-scope="scope">
            <el-button size="mini" @click="handleEdit(scope.row)">编辑</el-button>
            <el-button size="mini" type="danger" @click="handleDelete(scope.row.id)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
  
      <!-- 添加/编辑管理员弹出框 -->
      <el-dialog :visible.sync="dialogVisible" title="管理员信息" width="600px">
        <el-form ref="adminForm" :model="admin" :rules="rules" label-width="100px">
          <el-form-item label="用户名" prop="username">
            <el-input v-model="admin.username" />
          </el-form-item>
  
          <el-form-item label="密码" prop="password">
            <el-input v-model="admin.password" type="password" />
          </el-form-item>
  
          <el-form-item label="电话" prop="phone">
            <el-input v-model="admin.phone" />
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
  import AdminAPI from '@/api/admin_api';
  
  export default {
    data() {
      return {
        admins: [],
        selectedAdmins: [],
        searchForm: {
          username: '',
          phone: ''
        },
        dialogVisible: false,  // 控制弹出框显示
        admin: {
          id: null,
          username: '',
          password: '',
          phone: '',
          createdAt: '',
          updatedAt: '',
        },
        rules: {
          username: [{ required: true, message: '请输入用户名', trigger: 'blur' }],
          password: [{ required: true, message: '请输入密码', trigger: 'blur' }],
          phone: [{ required: true, message: '请输入电话', trigger: 'blur' }]
        }
      };
    },
    created() {
      this.fetchAdmins();
    },
    methods: {
      async fetchAdmins() {
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
          const response = await AdminAPI.getAdmins(example);
          this.admins = response.data;
        } catch (error) {
          console.error('获取管理员列表失败', error);
        }
      },
  
      resetSearch() {
        this.searchForm = { username: '', phone: '' };
        this.fetchAdmins();
      },
  
      handleSelectionChange(selection) {
        this.selectedAdmins = selection.map(admin => admin.id);
      },
  
      handleAdd() {
        this.admin = {
          id: null,
          username: '',
          password: '',
          phone: '',
          createdAt: '',
          updatedAt: ''
        };
        this.dialogVisible = true;
      },
  
      handleEdit(admin) {
        this.admin = { ...admin };
        this.dialogVisible = true;
      },
  
      async submitForm() {
        this.$refs.adminForm.validate(async (valid) => {
          if (valid) {
            try {
              if (this.admin.id) {
                await AdminAPI.updateAdmin(this.admin);
              } else {
                await AdminAPI.addAdmin(this.admin);
              }
              this.$message.success('管理员信息保存成功');
              this.dialogVisible = false;
              this.fetchAdmins();
            } catch (error) {
              this.$message.error('保存管理员信息失败');
              console.error(error);
            }
          }
        });
      },
  
      closeDialog() {
        this.dialogVisible = false;
      },
  
      confirmBatchDelete() {
        if (this.selectedAdmins.length === 0) return;
        this.$confirm('确定要删除选中的管理员吗？', '警告', { type: 'warning' })
          .then(() => this.handleBatchDelete())
          .catch(() => { });
      },
  
      async handleBatchDelete() {
        try {
          await Promise.all(this.selectedAdmins.map(id => AdminAPI.deleteAdmin(id)));
          this.fetchAdmins();
          this.selectedAdmins = [];
          this.$message.success('批量删除成功');
        } catch (error) {
          console.error('批量删除失败', error);
        }
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
  
  .add-admin-container {
    max-width: 600px;
    margin: 0 auto;
    padding: 20px;
    background: #fff;
    border-radius: 8px;
    box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
  }
  </style>
  