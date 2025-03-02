<template>
    <div class="edit-emergency-contact-container">
      <el-form ref="emergencyContactForm" :model="emergencyContact" :rules="rules" label-width="150px">
        <!-- 用户ID -->
        <el-form-item label="用户ID" prop="userId">
          <el-input v-model="emergencyContact.userId" />
        </el-form-item>
  
        <!-- 姓名 -->
        <el-form-item label="姓名" prop="name">
          <el-input v-model="emergencyContact.name" />
        </el-form-item>
  
        <!-- 电话 -->
        <el-form-item label="电话" prop="phone">
          <el-input v-model="emergencyContact.phone" />
        </el-form-item>
  
        <!-- 关系 -->
        <el-form-item label="关系" prop="relationship">
          <el-input v-model="emergencyContact.relationship" />
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
  import EmergencyContactApi from '@/api/EmergencyContact_api';
  
  export default {
    data() {
      return {
        emergencyContact: {
          userId: '',
          name: '',
          phone: '',
          relationship: '',
          createdtime: '',
        },
        rules: {
          userId: [{ required: true, message: '请输入用户ID', trigger: 'blur' }],
          name: [{ required: true, message: '请输入姓名', trigger: 'blur' }],
          phone: [{ required: true, message: '请输入电话', trigger: 'blur' }],
          relationship: [{ required: true, message: '请输入关系', trigger: 'blur' }]
        }
      };
    },
    created() {
      this.fetchEmergencyContact();
    },
    methods: {
      async fetchEmergencyContact() {
        const id = this.$route.query.id;
        try {
          const response = await EmergencyContactApi.getEmergencyContactById(id);
          this.emergencyContact = response.data;
        } catch (error) {
          console.error('获取紧急联系人详情失败', error);
        }
      },
      submitForm() {
        this.$refs.emergencyContactForm.validate(async (valid) => {
          if (valid) {
            try {
              await EmergencyContactApi.updateEmergencyContact(this.emergencyContact.id, this.emergencyContact);
              this.$message.success('紧急联系人更新成功');
              this.$router.push('/EmergencyContact/index'); 
            } catch (error) {
              this.$message.error('更新紧急联系人失败');
              console.error(error);
            }
          }
        });
      },
      resetForm() {
        this.fetchEmergencyContact(); // 重新加载当前紧急联系人数据以恢复初始状态
      },
      goBack() {
        this.$router.push('/EmergencyContact/index'); 
      }
    }
  };
  </script>
  
  <style scoped>
  .edit-emergency-contact-container {
    max-width: 600px;
    margin: 40px auto;
    padding: 20px;
    background-color: #fff;
    border-radius: 8px;
    box-shadow: 0 2px 12px rgba(0, 0, 0, 0.1);
  }
  </style>
  