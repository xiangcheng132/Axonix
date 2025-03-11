<template>
  <div class="log-detail-container">
    <h2>日志详情</h2>
    <el-form ref="logForm" :model="log" label-width="120px">
      <!-- 服务名称 -->
      <el-form-item label="服务名称" prop="serviceName">
        <el-input v-model="log.serviceName" />
      </el-form-item>

      <!-- 请求时间 -->
      <el-form-item label="请求时间" prop="createdtime">
        <el-date-picker
          v-model="log.createdtime"
          type="datetime"
          format="yyyy-MM-dd HH:mm:ss"
          value-format="yyyy-MM-ddTHH:mm:ss.SSSZ"
          placeholder="选择日期时间"
        />
      </el-form-item>

      <!-- 状态 -->
      <el-form-item label="状态" prop="status">
        <el-select v-model="log.status">
          <el-option label="成功" value="success" />
          <el-option label="失败" value="failure" />
        </el-select>
      </el-form-item>

      <!-- 请求数据 -->
      <el-form-item label="请求数据" prop="requestData">
        <el-input v-model="log.requestData" type="textarea" :rows="4" />
      </el-form-item>

      <!-- 响应数据 -->
      <el-form-item label="响应数据" prop="responseData">
        <el-input v-model="log.responseData" type="textarea" :rows="4" />
      </el-form-item>

      <!-- 按钮组 -->
      <el-form-item>
        <el-button type="primary" @click="submitForm">提交</el-button>
        <el-button @click="resetForm">重置</el-button>
        <el-button @click="goBack">取消</el-button>
      </el-form-item>
    </el-form>
  </div>
</template>

<script>
import ThirdPartyApiLogApi from '@/api/ThirdPartyApiLog_api';

export default {
  data() {
    return {
      log: {
        serviceName: '',
        createdtime: '',
        status: '',
        requestData: '',
        responseData: ''
      },
      originalLog: null
    };
  },
  created() {
    this.fetchLogDetail();
  },
  methods: {
    async fetchLogDetail() {
      const id = this.$route.query.id;
      if (!id) {
        this.$message.error('未提供日志 ID');
        this.$router.push('/ThirdPartyApiLog/index');
        return;
      }
      try {
        const response = await ThirdPartyApiLogApi.getLogById(id);
        this.log = { ...response.data };
        if (this.log.createdtime) {
          this.log.createdtime = new Date(this.log.createdtime).toISOString().slice(0, 19).replace('T', ' ');
        }
        this.originalLog = { ...response.data };
      } catch (error) {
        this.$message.error('获取日志详情失败');
        console.error(error);
      }
    },
    async submitForm() {
      try {
        let updatedLog = { ...this.log };
        if (updatedLog.createdtime) {
          updatedLog.createdtime = new Date(updatedLog.createdtime).toISOString();
        }
        await ThirdPartyApiLogApi.updateLogWithBlobs(this.$route.query.id, updatedLog);
        this.$message.success('日志更新成功');
        this.originalLog = { ...this.log };
        this.$router.push('/ThirdPartyApiLog/index');
      } catch (error) {
        this.$message.error('更新日志失败');
        console.error(error);
      }
    },
    resetForm() {
      this.log = { ...this.originalLog };
      if (this.log.createdtime) {
        this.log.createdtime = new Date(this.log.createdtime).toISOString().slice(0, 19).replace('T', ' ');
      }
    },
    goBack() {
      this.$router.push('/ThirdPartyApiLog/index');
    }
  }
};
</script>

<style scoped>
.log-detail-container {
  max-width: 600px;
  margin: 0 auto;
  padding: 20px;
  background: #fff;
  border-radius: 8px;
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
}
</style>