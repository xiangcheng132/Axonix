<template>
  <div class="app-container">
    <!-- 搜索表单 -->
    <el-form :inline="true" class="demo-form-inline">
      <el-form-item label="用户ID">
        <el-input v-model="filters.userId" placeholder="输入用户ID"></el-input>
      </el-form-item>
      <el-form-item label="标题">
        <el-input v-model="filters.title" placeholder="输入标题"></el-input>
      </el-form-item>
      <el-form-item label="类型">
        <el-input v-model="filters.type" placeholder="输入类型"></el-input>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="fetchNotifications">查询</el-button>
        <el-button @click="resetSearch">重置</el-button>
      </el-form-item>
    </el-form>

    <!-- 操作按钮 -->
    <el-button type="primary" @click="handleAdd">添加通知</el-button>
    <el-button type="danger" @click="confirmBatchDelete" :disabled="selectedNotifications.length === 0">批量删除</el-button>

    <!-- 通知列表 -->
    <el-table :data="notifications" border @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" />
      <el-table-column prop="id" label="ID" width="80" />
      <el-table-column prop="userId" label="用户ID" />
      <el-table-column prop="title" label="标题" />
      <el-table-column prop="type" label="类型" />
      <el-table-column prop="readStatus" label="已读状态">
        <template slot-scope="scope">
          <el-tag :type="getReadStatusTag(scope.row.readStatus)">
            {{ scope.row.readStatus === 0 ? '未读' : '已读' }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="createdtime" label="创建时间">
        <template slot-scope="scope">
          {{ formatDate(scope.row.createdtime) }}
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
import NotificationApi from '@/api/Notification_api';

export default {
  data() {
    return {
      notifications: [],
      selectedNotifications: [], // 存储选中的通知 ID 列表
      filters: {
        userId: '',
        title: '',
        type: ''
      }
    };
  },
  created() {
    this.fetchNotifications();
  },
  methods: {
    async fetchNotifications() {
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

  if (this.filters.title) {
    example.oredCriteria[0].criteria.push({
      condition: "title LIKE",
      value: `%${this.filters.title}%`,
      singleValue: true
    });
  }

  if (this.filters.type) {
    example.oredCriteria[0].criteria.push({
      condition: "type LIKE",
      value: `%${this.filters.type}%`,
      singleValue: true
    });
  }

  console.log('查询条件:', example); // 打印查询条件

  try {
    const response = await NotificationApi.getNotifications(example);
    console.log('服务器响应:', response.data); // 打印服务器返回的数据
    this.notifications = response.data;
  } catch (error) {
    console.error('获取通知失败', error);
  }
},

    resetSearch() {
      this.filters.userId = '';
      this.filters.title = '';
      this.filters.type = '';
      this.fetchNotifications();
    },

    handleAdd() {
      this.$router.push('/add-notification');
    },

    handleView(notification) {
      this.$router.push({ path: '/edit-notification', query: { id: notification.id } });
    },

    async handleDelete(id) {
      try {
        await NotificationApi.deleteNotification(id);
        this.fetchNotifications();
        this.$message.success('通知删除成功');
      } catch (error) {
        console.error('删除通知失败', error);
      }
    },

    handleSelectionChange(selection) {
      this.selectedNotifications = selection.map(notification => notification.id);
    },

    confirmBatchDelete() {
      if (this.selectedNotifications.length === 0) return;

      this.$confirm('确定要删除选中的通知吗？', '警告', {
        type: 'warning'
      }).then(() => {
        this.handleBatchDelete();
      }).catch(() => {});
    },

    async handleBatchDelete() {
      try {
        await Promise.all(this.selectedNotifications.map(id => NotificationApi.deleteNotification(id)));
        this.fetchNotifications();
        this.selectedNotifications = [];
        this.$message.success('批量删除成功');
      } catch (error) {
        console.error('批量删除失败', error);
      }
    },

    formatDate(date) {
      return date ? new Date(date).toLocaleString() : '无';
    },

    getReadStatusTag(readStatus) {
      return readStatus === 0 ? 'danger' : 'success';
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