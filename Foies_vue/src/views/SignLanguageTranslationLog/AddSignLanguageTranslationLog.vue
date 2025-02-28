<template>
  <div class="edit-log-container">
    <el-form ref="logForm" :model="log" :rules="rules" label-width="150px">
      <!-- 用户ID -->
      <el-form-item label="用户ID" prop="userId">
        <el-input v-model="log.userId" />
      </el-form-item>

      <!-- 原始视频URL -->
      <el-form-item label="原始视频URL" prop="originalVideoUrl">
        <el-input v-model="log.originalVideoUrl" />
      </el-form-item>

      <!-- 翻译文本 -->
      <el-form-item label="翻译文本" prop="translatedText">
        <el-input v-model="log.translatedText" type="textarea" :rows="4" />
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
import SignLanguageTranslationLogApi from '@/api/SignLanguageTranslationLog_api';

export default {
  data() {
    return {
      log: {
        userId: '',
        originalVideoUrl: '',
        createdtime: new Date().toISOString(), // 默认当前时间
        translatedText: ''
      },
      rules: {
        userId: [{ required: true, message: '请输入用户ID', trigger: 'blur' }],
        originalVideoUrl: [{ required: true, message: '请输入原始视频URL', trigger: 'blur' }],
        translatedText: [{ required: true, message: '请输入翻译文本', trigger: 'blur' }]
      }
    };
  },
  methods: {
    submitForm() {
      this.$refs.logForm.validate(async (valid) => {
        if (valid) {
          try {
            await SignLanguageTranslationLogApi.addLog(this.log);
            this.$message.success('日志添加成功');
            this.$router.push('/signLanguageTranslationLog/index'); 
          } catch (error) {
            this.$message.error('添加日志失败');
            console.error(error);
          }
        }
      });
    },
    resetForm() {
      this.$refs.logForm.resetFields(); // 清空表单内容
    },
    goBack() {
      this.$router.push('/signLanguageTranslationLog/index'); 
    }
  }
};
</script>

<style scoped>
.edit-log-container {
  max-width: 600px;
  margin: 40px auto;
  padding: 20px;
  background-color: #fff;
  border-radius: 8px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.1);
}
</style>