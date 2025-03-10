<template>
  <div class="app-container">
    <!-- 搜索表单 -->
    <el-form :inline="true" class="demo-form-inline">
      <el-form-item label="发送者ID">
        <el-input v-model="filters.senderId" placeholder="输入发送者ID"></el-input>
      </el-form-item>
      <el-form-item label="接收者ID">
        <el-input v-model="filters.receiverId" placeholder="输入接收者ID"></el-input>
      </el-form-item>
      <el-form-item label="消息类型">
        <el-input v-model="filters.type" placeholder="输入消息类型"></el-input>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="fetchMessages">查询</el-button>
        <el-button @click="resetSearch">重置</el-button>
      </el-form-item>
    </el-form>

    <!-- 批量删除按钮 -->
    <el-button type="danger" @click="confirmBatchDelete" :disabled="selectedMessages.length === 0">批量删除</el-button>

    <el-table :data="messages" border @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" />
      <el-table-column prop="id" label="ID" width="80" />
      <el-table-column prop="senderId" label="发送者ID" />
      <el-table-column prop="receiverId" label="接收者ID" />
      <el-table-column prop="type" label="消息类型">
        <template slot-scope="scope">
          {{ formatMessageType(scope.row.type) }}
        </template>
      </el-table-column>
      <el-table-column prop="content" label="消息内容" />
      <el-table-column prop="createdtime" label="创建时间">
        <template slot-scope="scope">
          {{ formatDate(scope.row.createdtime) }}
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
import MessageApi from '@/api/Message_api';

export default {
  data() {
    return {
      messages: [],
      selectedMessages: [], // 存储选中的消息 ID 列表
      filters: {
        senderId: '',
        receiverId: '',
        type: ''
      }
    };
  },
  created() {
    this.fetchMessages();
  },
  methods: {
    formatMessageType(type) {
      const typeMap = {
        text: '文字',
        voice: '语音',
        video: '视频'
      };
      return typeMap[type] || '未知类型';
    },

    async fetchMessages() {
      const example = {
        oredCriteria: [{ criteria: [] }]
      };

      if (this.filters.senderId) {
        example.oredCriteria[0].criteria.push({
          condition: "sender_id LIKE",
          value: `%${this.filters.senderId}%`,
          singleValue: true
        });
      }

      if (this.filters.receiverId) {
        example.oredCriteria[0].criteria.push({
          condition: "receiver_id LIKE",
          value: `%${this.filters.receiverId}%`,
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

      try {
        const response = await MessageApi.getMessages(example);
        this.messages = response.data;
      } catch (error) {
        console.error('获取消息失败', error);
      }
    },

    handleView(message) {
      this.$router.push({ path: '/edit-message', query: { id: message.id } });
    },

    async handleDelete(id) {
      try {
        await MessageApi.deleteMessage(id);
        this.fetchMessages();
        this.$message.success('消息删除成功');
      } catch (error) {
        console.error('删除消息失败', error);
      }
    },

    handleSelectionChange(selection) {
      this.selectedMessages = selection.map(message => message.id);
    },

    confirmBatchDelete() {
      if (this.selectedMessages.length === 0) return;

      this.$confirm('确定要删除选中的消息吗？', '警告', {
        type: 'warning'
      }).then(() => {
        this.handleBatchDelete();
      }).catch(() => {});
    },

    async handleBatchDelete() {
      try {
        await Promise.all(this.selectedMessages.map(id => MessageApi.deleteMessage(id)));
        this.fetchMessages();
        this.selectedMessages = [];
        this.$message.success('批量删除成功');
      } catch (error) {
        console.error('批量删除失败', error);
      }
    },

    formatDate(date) {
      return date ? new Date(date).toLocaleString() : '无';
    },

    resetSearch() {
      this.filters.senderId = '';
      this.filters.receiverId = '';
      this.filters.type = '';
      this.fetchMessages();
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