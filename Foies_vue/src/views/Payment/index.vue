<template>
  <div class="app-container">
    <!-- 搜索表单 -->
    <el-form :inline="true" class="demo-form-inline">
      <el-form-item label="用户ID">
        <el-input v-model="filters.userId" placeholder="输入用户ID"></el-input>
      </el-form-item>
      <el-form-item label="交易ID">
        <el-input v-model="filters.transactionId" placeholder="输入交易ID"></el-input>
      </el-form-item>
      <el-form-item label="状态">
        <el-select v-model="filters.status" placeholder="选择状态">
          <el-option label="待处理" value="pending"></el-option>
          <el-option label="已完成" value="completed"></el-option>
          <el-option label="失败" value="failed"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="fetchPayments">查询</el-button>
        <el-button @click="resetSearch">重置</el-button>
      </el-form-item>
    </el-form>

    <!-- 操作按钮 -->
    <el-button type="primary" @click="handleAdd">添加支付日志</el-button>
    <el-button type="danger" @click="confirmBatchDelete" :disabled="selectedPayments.length === 0">批量删除</el-button>

    <!-- 支付日志列表 -->
    <el-table :data="payments" border @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" />
      <el-table-column prop="id" label="ID" width="80" />
      <el-table-column prop="userId" label="用户ID" />
      <el-table-column prop="transactionId" label="交易ID" />
      <el-table-column prop="amount" label="金额" />
      <el-table-column prop="status" label="状态">
        <template #default="scope">
          <el-tag :type="getStatusTag(scope.row.status)">
            {{ formatStatus(scope.row.status) }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="createdtime" label="创建时间">
        <template #default="scope">
          {{ formatDate(scope.row.createdtime) }}
        </template>
      </el-table-column>

      <el-table-column label="操作" width="200">
        <template #default="scope">
          <el-button size="mini" @click="handleView(scope.row)">查看</el-button>
          <el-button size="mini" type="danger" @click="handleDelete(scope.row.id)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>
  </div>
</template>

<script>
import PaymentApi from '@/api/Payment_api';

export default {
data() {
  return {
    payments: [],
    selectedPayments: [],
    filters: {
      userId: '',
      transactionId: '',
      status: ''
    }
  };
},
created() {
  this.fetchPayments();
},
methods: {
  async fetchPayments() {
    const example = {
      oredCriteria: [
        {
          criteria: []
        }
      ]
    };

    if (this.filters.userId) {
      example.oredCriteria[0].criteria.push({
        condition: "user_id LIKE",
        value: `%${this.filters.userId}%`,
        singleValue: true
      });
    }

    if (this.filters.transactionId) {
      example.oredCriteria[0].criteria.push({
        condition: "transaction_id LIKE",
        value: `%${this.filters.transactionId}%`,
        singleValue: true
      });
    }

    if (this.filters.status) {
      example.oredCriteria[0].criteria.push({
        condition: "status =",
        value: this.filters.status,
        singleValue: true
      });
    }

    try {
      const response = await PaymentApi.getPayments(example);
      this.payments = response;
    } catch (error) {
      console.error('获取支付日志失败', error);
    }
  },

  resetSearch() {
    this.filters.userId = '';
    this.filters.transactionId = '';
    this.filters.status = '';
    this.fetchPayments();
  },

  handleSelectionChange(selection) {
    this.selectedPayments = selection.map(payment => payment.id);
  },

  handleAdd() {
    this.$router.push('/add-payment-log');
  },

  handleView(log) {
      this.$router.push({ path: '/payment-log-detail/:id', query: { id: log.id } });
    },

    async handleDelete(id) {
      try {
        await PaymentApi.deletePayment(id);
        this.fetchPayments();
        this.$message.success('操作日志删除成功');
      } catch (error) {
        console.error('删除操作日志失败', error);
      }
    },

  confirmBatchDelete() {
    if (this.selectedPayments.length === 0) return;
    this.$confirm('确定要删除选中的支付日志吗？', '警告', {
      type: 'warning'
    }).then(() => {
      this.handleBatchDelete();
    }).catch(() => {});
  },

  async handleBatchDelete() {
    try {
      await Promise.all(this.selectedPayments.map(id => PaymentApi.deletePayment(id)));
      this.fetchPayments();
      this.selectedPayments = [];
      this.$message.success('批量删除成功');
    } catch (error) {
      console.error('批量删除失败', error);
    }
  },

  formatStatus(status) {
    const statusMap = {
      pending: '待处理',
      completed: '已完成',
      failed: '失败'
    };
    return statusMap[status] || '未知';
  },

  getStatusTag(status) {
    const tagMap = {
      pending: 'warning',
      completed: 'success',
      failed: 'danger'
    };
    return tagMap[status] || 'info';
  },

  formatDate(date) {
    if (!date) return '无';
    const d = new Date(date);
    const year = d.getFullYear();
    const month = String(d.getMonth() + 1).padStart(2, '0');
    const day = String(d.getDate()).padStart(2, '0');
    const hours = String(d.getHours()).padStart(2, '0');
    const minutes = String(d.getMinutes()).padStart(2, '0');
    const seconds = String(d.getSeconds()).padStart(2, '0');
    return `${year}-${month}-${day} ${hours}:${minutes}:${seconds}`;
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
