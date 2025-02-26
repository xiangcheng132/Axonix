<template>
    <div class="log-detail-container">
      <h2>日志详情</h2>
      <el-form label-width="120px" disabled>
        <!-- 服务名称 -->
        <el-form-item label="服务名称">
          <el-input v-model="log.serviceName" />
        </el-form-item>
  
        <!-- 请求时间 -->
        <el-form-item label="请求时间">
          <el-date-picker
            v-model="log.createdtime"
            type="datetime"
            format="yyyy-MM-dd HH:mm:ss"
            value-format="yyyy-MM-dd HH:mm:ss"
            placeholder="选择日期时间"
          />
        </el-form-item>
  
        <!-- 状态 -->
        <el-form-item label="状态">
          <el-select v-model="log.status">
            <el-option label="成功" value="success" />
            <el-option label="失败" value="failure" />
          </el-select>
        </el-form-item>
  
        <!-- 请求数据 -->
        <el-form-item label="请求数据">
          <el-input v-model="log.requestData" type="textarea" :rows="4" />
        </el-form-item>
  
        <!-- 响应数据 -->
        <el-form-item label="响应数据">
          <el-input v-model="log.responseData" type="textarea" :rows="4" />
        </el-form-item>
      </el-form>
    </div>
  </template>
  
  <script>
  import ThirdPartyApiLogApi from '@/api/ThirdPartyApiLog_api';
  
  export default {
    props: ['id'],
    data() {
      return {
        log: {}
      };
    },
    created() {
      this.fetchLogDetail(this.id);
    },
    methods: {
      async fetchLogDetail(id) {
        try {
          const response = await ThirdPartyApiLogApi.getLogById(id);
          this.log = response.data;
        } catch (error) {
          console.error('Error fetching log detail:', error);
        }
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