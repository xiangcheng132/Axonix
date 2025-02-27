<template>
  <div class="log-detail-container">
    <h2>日志详情</h2>
    <el-form ref="logForm" :model="log" label-width="120px" :disabled="!isEditMode">
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
          value-format="yyyy-MM-dd HH:mm:ss"
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

    </el-form>
    <!-- 按钮组，不绑定到表单的禁用状态 -->
    <div class="button-group">
      <el-button v-if="!isEditMode" type="primary" @click="toggleEditMode">编辑</el-button>
      <el-button v-else type="success" @click="saveChanges">保存</el-button>
      <el-button v-if="isEditMode" @click="cancelEditMode">取消</el-button>
      <el-button @click="goBack">返回</el-button>
    </div>
  </div>
</template>

<script>
import ThirdPartyApiLogApi from '@/api/ThirdPartyApiLog_api';

export default {
  props: ['id'],
  data() {
    return {
      log: {},
      isEditMode: false,
      originalLog: null // 用于存储原始数据以便取消编辑时恢复
    };
  },
  created() {
    this.fetchLogDetail(this.id);
  },
  methods: {
    async fetchLogDetail(id) {
      try {
        const response = await ThirdPartyApiLogApi.getLogById(id);
        this.log = { ...response.data }; // 使用浅拷贝避免直接修改原始数据
        this.originalLog = { ...response.data }; // 存储原始数据
      } catch (error) {
        console.error('Error fetching log detail:', error);
      }
    },
    toggleEditMode() {
      this.isEditMode = true;
    },
    cancelEditMode() {
      this.log = { ...this.originalLog }; // 恢复原始数据
      this.isEditMode = false;
    },
    goBack() {
      this.$router.push('/ThirdPartyApiLog/index'); 
    },
    async saveChanges() {
      try {
        await ThirdPartyApiLogApi.updateLogWithBlobs(this.id, this.log);
        this.$message.success('日志更新成功');
        this.isEditMode = false; // 切换回查看模式
        this.originalLog = { ...this.log }; // 更新原始数据
      } catch (error) {
        this.$message.error('更新日志失败');
        console.error('Error updating log:', error);
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

.button-group {
  text-align: right;
  margin-top: 20px;
}
</style>