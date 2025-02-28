<template>
    <div class="edit-notification-container">
      <el-form ref="notificationForm" :model="notification" :rules="rules" label-width="150px">
        <!-- 用户ID -->
        <el-form-item label="用户ID" prop="userId">
          <el-input v-model="notification.userId" />
        </el-form-item>
  
        <!-- 标题 -->
        <el-form-item label="标题" prop="title">
          <el-input v-model="notification.title" />
        </el-form-item>
  
        <!-- 类型 -->
        <el-form-item label="类型" prop="type">
          <el-input v-model="notification.type" />
        </el-form-item>
  
        <!-- 已读状态 -->
        <el-form-item label="已读状态" prop="readStatus">
          <el-switch v-model="notification.readStatus" active-text="已读" inactive-text="未读"></el-switch>
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
  import NotificationApi from '@/api/Notification_api';
  
  export default {
    data() {
      return {
        notification: {
          userId: '',
          title: '',
          type: '',
          readStatus: false,
        },
        rules: {
          userId: [{ required: true, message: '请输入用户ID', trigger: 'blur' }],
          title: [{ required: true, message: '请输入标题', trigger: 'blur' }],
          type: [{ required: true, message: '请输入类型', trigger: 'blur' }]
        }
      };
    },
    created() {
      this.fetchNotification();
    },
    methods: {
      async fetchNotification() {
        const id = this.$route.query.id;
        try {
          const response = await NotificationApi.getNotificationById(id);
          this.notification = response.data;
        } catch (error) {
          console.error('获取通知详情失败', error);
        }
      },
      submitForm() {
        this.$refs.notificationForm.validate(async (valid) => {
          if (valid) {
            try {
              await NotificationApi.updateNotification(this.notification.id, this.notification);
              this.$message.success('通知更新成功');
              this.$router.push('/notification/index'); 
            } catch (error) {
              this.$message.error('更新通知失败,用户不存在！');
              console.error(error);
            }
          }
        });
      },
      resetForm() {
        this.fetchNotification(); // 重新加载当前通知数据以恢复初始状态
      },
      goBack() {
        this.$router.push('/notification/index'); 
      }
    }
  };
  </script>
  
  <style scoped>
  .edit-notification-container {
    max-width: 600px;
    margin: 40px auto;
    padding: 20px;
    background-color: #fff;
    border-radius: 8px;
    box-shadow: 0 2px 12px rgba(0, 0, 0, 0.1);
  }
  </style>