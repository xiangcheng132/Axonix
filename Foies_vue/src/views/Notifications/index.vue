<template>
  <div class="app-container">
    <!-- 搜索表单 -->
    <el-form :inline="true" :model="searchForm" class="search-form">
      <el-form-item label="标题">
        <el-input v-model="searchForm.title" placeholder="请输入通知标题" clearable />
      </el-form-item>
      <el-form-item label="内容">
        <el-input v-model="searchForm.content" placeholder="请输入通知内容" clearable />
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="fetchNotifications">查询</el-button>
        <el-button @click="resetSearch">重置</el-button>
      </el-form-item>
    </el-form>

    <!-- 操作按钮和记录数显示 -->
    <el-row class="action-row" type="flex" justify="space-between" align="middle">
      <el-col :span="8">
        <el-button type="primary" @click="handleAdd">添加通知</el-button>
        <el-button type="danger" @click="confirmBatchDelete"
          :disabled="selectedNotifications.length === 0">批量删除</el-button>
      </el-col>
      <el-col :span="8" class="record-count" style="text-align: right;">
        <span>当前共有 {{ totalRecords }} 条记录</span>
      </el-col>
    </el-row>

    <!-- 通知表格 -->
    <el-table :data="notifications" border @selection-change="handleSelectionChange":empty-text="'没有数据'">
      <el-table-column type="selection" width="50" />
      <el-table-column prop="id" label="ID" />
      <el-table-column prop="title" label="标题" />
      <el-table-column prop="content" label="内容" />
      <el-table-column prop="adminId" label="管理员ID" />
      <el-table-column prop="sendTime" label="发送时间">
        <template slot-scope="scope">{{ formatDate(scope.row.sendTime) }}</template>
      </el-table-column>
      <el-table-column prop="createdAt" label="创建时间">
        <template slot-scope="scope">{{ formatDate(scope.row.createdAt) }}</template>
      </el-table-column>
      <el-table-column label="操作" width="160" fixed="right">
        <template slot-scope="scope">
          <el-button size="mini" @click="handleEdit(scope.row)">编辑</el-button>
          <el-button size="mini" type="danger" @click="handleDelete(scope.row.id)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <!-- 添加/编辑通知弹出框 -->
    <el-dialog :visible.sync="dialogVisible" title="通知信息" width="600px">
      <el-form ref="notificationForm" :model="notification" :rules="rules" label-width="100px">
        <el-form-item label="标题" prop="title">
          <el-input v-model="notification.title" />
        </el-form-item>

        <el-form-item label="内容" prop="content">
          <el-input v-model="notification.content" />
        </el-form-item>

        <el-form-item label="管理员ID" prop="adminId">
          <el-input v-model="notification.adminId" :disabled="true" />
        </el-form-item>

        <el-form-item label="发送时间" prop="sendTime">
          <el-date-picker v-model="notification.sendTime" type="datetime" placeholder="选择发送时间" />
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
import NotificationAPI from '@/api/notifications_api';
import AdminAPI from '@/api/admin_api'; // 导入管理员 API

export default {
  data() {
    return {
      notifications: [],
      totalRecords: 0, // 添加记录数
      selectedNotifications: [],
      searchForm: {
        title: '',
        content: ''
      },
      dialogVisible: false,  // 控制弹出框显示
      notification: {
        id: null,
        adminId: null,  // 管理员ID，默认值为null
        title: '',
        content: '',
        sendTime: '',
        createdAt: '',
      },
      rules: {
        title: [{ required: true, message: '请输入通知标题', trigger: 'blur' }],
        content: [{ required: true, message: '请输入通知内容', trigger: 'blur' }],
        sendTime: [{ required: true, message: '请选择发送时间', trigger: 'change' }],
        adminId: [{ required: true, message: '管理员ID不能为空', trigger: 'blur' }]
      }
    };
  },
  created() {
    this.fetchNotifications();
    this.fetchAdminId();  // 获取管理员ID
  },
  methods: {
    async fetchNotifications() {
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

      if (this.searchForm.content) {
        example.oredCriteria[0].criteria.push({
          condition: "content LIKE",
          value: `%${this.searchForm.content}%`,
          singleValue: true
        });
      }

      try {
        const response = await NotificationAPI.getNotificationsByExampleWithBLOBs(example);
        this.notifications = response.data;

        const countResponse = await NotificationAPI.countNotifications(example);
        this.totalRecords = countResponse.data; // 假设返回的是记录总数
      } catch (error) {
        console.error('获取通知列表失败', error);
      }
    },

    async fetchAdminId() {
      try {
        const response = await AdminAPI.getAdmins({});
        if (response.data && response.data.length > 0) {
          // 假设返回第一个管理员ID，您可以根据实际情况处理
          this.notification.adminId = response.data[0].id;
        }
      } catch (error) {
        console.error('获取管理员信息失败', error);
      }
    },

    resetSearch() {
      this.searchForm = { title: '', content: '' };
      this.fetchNotifications();
    },

    handleSelectionChange(selection) {
      this.selectedNotifications = selection.map(notification => notification.id);
    },

    handleAdd() {
      this.notification = {
        id: null,
        adminId: this.notification.adminId,  // 默认使用获取的管理员ID
        title: '',
        content: '',
        sendTime: '',
        createdAt: '',
      };
      this.dialogVisible = true;
    },

    handleEdit(notification) {
      this.notification = { ...notification };
      this.dialogVisible = true;
    },

    async submitForm() {
      this.$refs.notificationForm.validate(async (valid) => {
        if (valid) {
          try {
            if (this.notification.id) {
              await NotificationAPI.updateNotificationByIdWithBLOBs(this.notification);
            } else {
              await NotificationAPI.addNotificationSelective(this.notification);
            }
            this.$message.success('通知信息保存成功');
            this.dialogVisible = false;
            this.fetchNotifications();
          } catch (error) {
            this.$message.error('保存通知信息失败');
            console.error(error);
          }
        }
      });
    },

    closeDialog() {
      this.dialogVisible = false;
    },

    confirmBatchDelete() {
      if (this.selectedNotifications.length === 0) return;
      this.$confirm('确定要删除选中的通知吗？', '警告', {
        type: 'warning',
        cancelButtonText: '取消',
        confirmButtonText: '确定'
      })
        .then(() => this.handleBatchDelete())
        .catch(() => { });
    },

    async handleBatchDelete() {
      try {
        const example = {
          oredCriteria: [{ criteria: [] }]
        };

        // 可根据需要添加批量删除的筛选条件
        await NotificationAPI.deleteNotificationByExample(example);
        this.fetchNotifications();
        this.selectedNotifications = [];
        this.$message.success('批量删除成功');
      } catch (error) {
        console.error('批量删除失败', error);
      }
    },

    // 删除单个通知
    async handleDelete(id) {
      try {
        await NotificationAPI.deleteNotificationById(id);
        this.fetchNotifications();
        this.$message.success('删除成功');
      } catch (error) {
        console.error('删除失败', error);
        this.$message.error('删除失败');
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

.record-count {
  margin-top: 20px;
  font-size: 14px;
  color: #606266;
}
</style>