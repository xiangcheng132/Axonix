<template>
  <div class="wrapper">
    <div class="add-user-container">
      <el-form ref="userForm" :model="user" :rules="rules" label-width="100px">
        <el-form-item label="用户名" prop="username">
          <el-input v-model="user.username" />
        </el-form-item>

        <el-form-item label="密码" prop="password">
          <el-input v-model="user.password" type="password" />
        </el-form-item>

        <el-form-item label="邮箱" prop="email">
          <el-input v-model="user.email" />
        </el-form-item>

        <el-form-item label="电话" prop="phone">
          <el-input v-model="user.phone" />
        </el-form-item>

        <el-form-item label="角色" prop="role">
          <el-select v-model="user.role">
            <el-option label="管理员" value="admin" />
            <el-option label="用户" value="user" />
          </el-select>
        </el-form-item>

        <el-form-item label="残疾类型" prop="disabilityType">
          <el-select v-model="user.disabilityType">
            <el-option label="无" value="" />
            <el-option label="视障" value="视障" />
            <el-option label="听障" value="听障" />
          </el-select>
        </el-form-item>


        <el-form-item label="语言偏好" prop="preferences">
          <el-select v-model="user.preferences.language">
            <el-option label="无" value="" />
            <el-option label="中文" value="中文" />
            <el-option label="英语" value="英语" />
            <el-option label="西班牙语" value="西班牙语" />
          </el-select>
        </el-form-item>

        <el-form-item>
          <el-button type="primary" @click="submitForm">提交</el-button>
          <el-button @click="resetForm">重置</el-button>
          <el-button @click="goBack">返回</el-button>
        </el-form-item>
      </el-form>
    </div>
  </div>
</template>
<script>
import UserAPI from '@/api/user_api';

export default {
  data() {
    return {
      user: {
        username: '',
        password: '',
        email: '',
        phone: '',
        role: 'user',
        disabilityType: '',
        preferences: { language: '' },
        createdtime: '', // 添加createdtime字段
      },
      rules: {
        username: [{ required: true, message: '请输入用户名', trigger: 'blur' }],
        password: [{ required: true, message: '请输入密码', trigger: 'blur' }],
        email: [{ required: true, type: 'email', message: '请输入有效邮箱', trigger: 'blur' }]
      }
    };
  },
  methods: {
    submitForm() {
      this.$refs.userForm.validate(async (valid) => {
        if (valid) {
          // 设置创建时间为当前时间的ISO 8601格式
          this.user.createdtime = new Date().toISOString();

          try {
            await UserAPI.addUser({
              ...this.user,
              preferences: JSON.stringify(this.user.preferences)
            });
            this.$message.success('用户添加成功');
            this.$router.push('/User/index');
          } catch (error) {
            this.$message.error('添加用户失败');
            console.error(error);
          }
        }
      });
    },

    goBack() {
      this.$router.push('/User/index');
    },

    resetForm() {
      this.$refs.userForm.resetFields();
    }
  }
};
</script>

<style scoped>
.wrapper {
  display: flex;
  justify-content: center;
  /* 水平居中 */
  align-items: center;
  /* 垂直居中 */
  min-height: 100vh;
  /* 确保有足够的高度让align-items生效 */
  box-sizing: border-box;
}

.add-user-container {
  max-width: 600px;
  margin: 0 auto;
  padding: 20px;
  background: #fff;
  border-radius: 8px;
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
}
</style>