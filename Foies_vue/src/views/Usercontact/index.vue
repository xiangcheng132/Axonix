<template>
  <div class="app-container">
    <el-form :inline="true" :model="searchForm" class="search-form">
      <el-form-item label="用户ID">
        <el-input v-model="searchForm.userId" placeholder="请输入用户ID" clearable />
      </el-form-item>
      <el-form-item label="联系电话">
        <el-input v-model="searchForm.contactPhone" placeholder="请输入联系电话" clearable />
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="fetchContacts">查询</el-button>
        <el-button @click="resetSearch">重置</el-button>
      </el-form-item>
    </el-form>

    <el-button type="primary" @click="handleAdd">添加紧急联系人</el-button>
    <el-button type="danger" @click="confirmBatchDelete" :disabled="selectedContacts.length === 0">批量删除</el-button>

    <el-table :data="contacts" border @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" />
      <el-table-column prop="id" label="ID" width="80" />
      <el-table-column prop="userId" label="用户ID" width="100" />
      <el-table-column prop="contactUserId" label="联系人用户ID" width="120" />
      <el-table-column prop="contactPhone" label="联系电话" width="140" />
      <el-table-column prop="relationship" label="关系" width="120" />
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

    <el-dialog :visible.sync="dialogVisible" title="紧急联系人信息" width="600px">
      <el-form ref="contactForm" :model="contact" label-width="100px">
        <el-form-item label="用户ID">
          <el-input v-model="contact.userId" />
        </el-form-item>
        <el-form-item label="联系人用户ID">
          <el-input v-model="contact.contactUserId" />
        </el-form-item>
        <el-form-item label="联系电话">
          <el-input v-model="contact.contactPhone" />
        </el-form-item>
        <el-form-item label="关系">
          <el-input v-model="contact.relationship" />
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
import UserContactAPI from '@/api/usercontact_api';

export default {
  data() {
    return {
      contacts: [],
      selectedContacts: [],
      searchForm: {
        userId: '',
        contactPhone: ''
      },
      dialogVisible: false,
      contact: {
        id: null,
        userId: '',
        contactUserId: '',
        contactPhone: '',
        relationship: '',
        createdAt: '',
        updatedAt: ''
      }
    };
  },
  created() {
    this.fetchContacts();
  },
  methods: {
    async fetchContacts() {
      try {
        const response = await UserContactAPI.getContacts({ oredCriteria: [{ criteria: [] }] });
        this.contacts = response.data;
      } catch (error) {
        console.error('获取联系人失败', error);
      }
    },
    resetSearch() {
      this.searchForm = { userId: '', contactPhone: '' };
      this.fetchContacts();
    },
    handleSelectionChange(selection) {
      this.selectedContacts = selection.map(contact => contact.id);
    },
    handleAdd() {
      const now = new Date().toISOString();
      this.contact = { 
        id: null, 
        userId: '', 
        contactUserId: '', 
        contactPhone: '', 
        relationship: '',
        createdAt: now, // 自动记录创建时间
        updatedAt: '' 
      };
      this.dialogVisible = true;
    },
    handleEdit(contact) {
      this.contact = { ...contact };
      this.dialogVisible = true;
    },
    async submitForm() {
      try {
        const now = new Date().toISOString();
        this.contact.updatedAt = now; // 自动记录更新时间
        
        if (this.contact.id) {
          await UserContactAPI.updateContact(this.contact);
        } else {
          await UserContactAPI.addContact(this.contact);
        }
        this.$message.success('联系人信息保存成功');
        this.dialogVisible = false;
        this.fetchContacts();
      } catch (error) {
        console.error('保存联系人信息失败', error);
      }
    },
    async handleDelete(id) {
      await UserContactAPI.deleteContactById(id);
      this.fetchContacts();
    },
    confirmBatchDelete() {
      this.$confirm('确定要删除选中的联系人吗？', '警告', { type: 'warning' })
        .then(() => this.handleBatchDelete())
        .catch(() => {});
    },
    async handleBatchDelete() {
      await Promise.all(this.selectedContacts.map(id => UserContactAPI.deleteContactById(id)));
      this.fetchContacts();
    },
    closeDialog() {
      this.dialogVisible = false;
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
  margin-bottom: 20px;
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