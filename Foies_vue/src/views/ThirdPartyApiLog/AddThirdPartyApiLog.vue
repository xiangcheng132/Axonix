<template>
  <div class="add-log-container">
    <el-form ref="logForm" :model="log" :rules="rules" label-width="120px">
      <!-- 服务名称 -->
      <el-form-item label="服务名称" prop="serviceName">
        <el-input v-model="log.serviceName" />
      </el-form-item>

      <!-- 请求时间 -->
      <el-form-item label="请求时间" prop="createdtime">
        <el-date-picker
          v-model="log.createdtime"
          type="datetime"
          placeholder="选择日期时间"
          format="yyyy-MM-ddTHH:mm:ss.sssZ"
          value-format="yyyy-MM-ddTHH:mm:ss.sssZ"
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
        <el-input v-model="log.requestData" />
      </el-form-item>

      <!-- 响应数据 -->
      <el-form-item label="响应数据" prop="responseData">
        <el-input v-model="log.responseData" type="textarea" :rows="4" />
      </el-form-item>

      <!-- 按钮组 -->
      <el-form-item>
        <el-button type="primary" @click="submitForm">提交</el-button>
        <el-button @click="resetForm">重置</el-button>
        <el-button @click="goBack">返回</el-button>
      </el-form-item>
    </el-form>
  </div>
</template>

<script>
import ThirdPartyApiLogApi from '@/api/ThirdPartyApiLog_api'

export default {
  data() {
    return {
      log: {
        serviceName: '',
        createdtime: '',
        status: 'success',
        requestData: '',
        responseData: ''
      },
      rules: {
        serviceName: [{ required: true, message: '请输入服务名称', trigger: 'blur' }],
        createdtime: [{ required: true, message: '请选择请求时间', trigger: 'change' }],
        status: [{ required: true, message: '请选择状态', trigger: 'change' }],
        requestData: [{ required: true, message: '请输入请求数据', trigger: 'blur' }],
        responseData: [{ required: true, message: '请输入响应数据', trigger: 'blur' }]
      }
    }
  },
  methods: {
    submitForm() {
      this.$refs.logForm.validate(async (valid) => {
        if (valid) {
          try {
            // 直接提交整个log对象，不需要额外转换
            await ThirdPartyApiLogApi.addLog(this.log);
            this.$message.success('日志添加成功');
            this.$router.push('@/'); 
          } catch (error) {
            this.$message.error('添加日志失败');
            console.error(error);
          }
        }
      })
    },
    resetForm() {
      this.$refs.logForm.resetFields();
    },
    goBack() {
      this.$router.push('/ThirdPartyApiLog/index'); 
    }
  }
}
</script>

<style scoped>
.add-log-container {
  max-width: 600px;
  margin: 0 auto;
  padding: 20px;
  background: #fff;
  border-radius: 8px;
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
}
</style>