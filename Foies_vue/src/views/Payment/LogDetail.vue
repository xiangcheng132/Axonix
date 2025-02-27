<template>
  <div class="log-detail-container">
      <h2>支付日志详情</h2>
      <el-form ref="logForm" :model="log" label-width="120px" :disabled="!isEditMode">
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

          <el-form-item label="创建时间" prop="createdTime">
              <el-date-picker v-model="log.createdTime" type="datetime" format="yyyy-MM-dd HH:mm:ss" value-format="yyyy-MM-ddTHH:mm:ss" />
          </el-form-item>
      </el-form>

      <div class="button-group">
          <el-button v-if="!isEditMode" type="primary" @click="toggleEditMode">编辑</el-button>
          <el-button v-else type="success" @click="saveChanges">保存</el-button>
          <el-button v-if="isEditMode" @click="cancelEditMode">取消</el-button>
          <el-button @click="goBack">返回</el-button>
      </div>
  </div>
</template>

<script>
import PaymentApi from '@/api/Payment_api';

export default {
  data() {
    return {
      log: {},
      isEditMode: false,
      originalLog: null
    };
  },
  created() {
    const id = this.$route.params.id; // 直接从$route.params获取ID
    if (!id) {
      console.error('支付日志ID未定义');
      return;
    }
    this.fetchPaymentDetail(id);
  },
  methods: {
    async fetchPaymentDetail(id) {
      try {
        const response = await PaymentApi.getPaymentById(id);
        this.log = { ...response.data };
        // 确保createdTime是ISO字符串形式
        if (this.log.createdTime) {
          this.log.createdTime = new Date(this.log.createdTime).toISOString().slice(0, 19).replace('T', ' ');
        }
        this.originalLog = { ...response.data };
      } catch (error) {
        console.error('获取支付日志详情失败', error);
      }
    },
    toggleEditMode() {
      this.isEditMode = true;
    },
    cancelEditMode() {
      this.log = { ...this.originalLog };
      // 还原createdTime的格式
      if (this.log.createdTime) {
        this.log.createdTime = new Date(this.log.createdTime).toISOString().slice(0, 19).replace('T', ' ');
      }
      this.isEditMode = false;
    },
    async saveChanges() {
      try {
        let updatedLog = { ...this.log };
        if (updatedLog.createdTime) {
          updatedLog.createdTime = new Date(updatedLog.createdTime).toISOString();
        }

        await PaymentApi.updatePayment(this.$route.params.id, updatedLog); // 使用正确的ID
        this.$message.success('支付日志更新成功');
        this.isEditMode = false;
      } catch (error) {
        this.$message.error('更新支付日志失败，输入的用户ID不存在！');
        console.error(error);
      }
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