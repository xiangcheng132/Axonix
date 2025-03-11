<template>
  <div class="log-detail-container">
      <h2>支付日志详情</h2>
      <el-form ref="logForm" :model="log" label-width="120px">
          <el-form-item label="交易ID" prop="transactionId">
              <el-input v-model="log.transactionId" />
          </el-form-item>

          <el-form-item label="用户ID" prop="userId">
              <el-input v-model="log.userId" />
          </el-form-item>

          <el-form-item label="金额" prop="amount">
              <el-input v-model="log.amount" type="number" />
          </el-form-item>

          <el-form-item label="状态" prop="status">
              <el-select v-model="log.status">
                  <el-option label="待处理" value="pending" />
                  <el-option label="已完成" value="completed" />
                  <el-option label="失败" value="failed" />
              </el-select>
          </el-form-item>
      </el-form>

      <div class="button-group">
          <el-button type="primary" @click="saveChanges">提交</el-button>
          <el-button @click="resetForm">重置</el-button>
          <el-button @click="goBack">取消</el-button>
      </div>
  </div>
</template>

<script>
import PaymentApi from '@/api/Payment_api';

export default {
  data() {
    return {
      log: {},
      originalLog: null
    };
  },
  created() {
    this.fetchPaymentDetail();
  },
  methods: {
    async fetchPaymentDetail() {
      const id = this.$route.params.id;
      if (!id) {
        this.$message.error('未提供支付日志 ID');
        this.$router.push('/Payment/index');
        return;
      }
      try {
        const response = await PaymentApi.getPaymentById(id);
        this.log = { ...response.data };
        this.originalLog = { ...response.data };
      } catch (error) {
        this.$message.error('获取支付日志详情失败');
        console.error(error);
      }
    },
    async saveChanges() {
      try {
        await PaymentApi.updatePayment(this.$route.params.id, this.log);
        this.$message.success('支付日志更新成功');
        this.$router.push('/Payment/index');
      } catch (error) {
        this.$message.error('更新支付日志失败');
        console.error(error);
      }
    },
    resetForm() {
      this.log = { ...this.originalLog };
    },
    goBack() {
      this.$router.push('/Payment/index');
    }
  }
};
</script>

<style scoped>
.log-detail-container {
  max-width: 600px;
  margin: 0 auto;
  padding: 20px;
}
.button-group {
  text-align: right;
  margin-top: 20px;
}
</style>