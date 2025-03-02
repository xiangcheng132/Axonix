<template>
    <div class="add-emergency-contact-container">
        <el-form ref="emergencyContactForm" :model="emergencyContact" :rules="rules" label-width="120px">
            <el-form-item label="用户ID" prop="userId">
                <el-input v-model="emergencyContact.userId" />
            </el-form-item>

            <el-form-item label="姓名" prop="name">
                <el-input v-model="emergencyContact.name" />
            </el-form-item>

            <el-form-item label="电话" prop="phone">
                <el-input v-model="emergencyContact.phone" />
            </el-form-item>

            <el-form-item label="关系" prop="relationship">
                <el-input v-model="emergencyContact.relationship" />
            </el-form-item>

            <el-form-item label="创建时间" prop="createdtime">
                <el-date-picker v-model="emergencyContact.createdtime" type="datetime" placeholder="选择日期时间" />
            </el-form-item>

            <el-form-item>
                <el-button type="primary" @click="submitForm">提交</el-button>
                <el-button @click="resetForm">重置</el-button>
                <el-button @click="goBack">返回</el-button>
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
                // 自动填充当前时间为创建时间
                createdtime: new Date().toISOString() // 获取当前时间并格式化为ISO字符串
            },
            rules: {
                userId: [{ required: true, message: '请输入用户ID', trigger: 'blur' }],
                name: [{ required: true, message: '请输入姓名', trigger: 'blur' }],
                phone: [{ required: true, message: '请输入电话', trigger: 'blur' }],
                relationship: [{ required: true, message: '请输入关系', trigger: 'blur' }],
                createdtime: [{ required: true, message: '请选择创建时间', trigger: 'change' }]
            }
        };
    },
    methods: {
        submitForm() {
            this.$refs.emergencyContactForm.validate(async (valid) => {
                if (valid) {
                    try {
                        const formData = { ...this.emergencyContact };
                        // 不需要再手动转换 createdtime 因为已经是 ISO 字符串
                        await EmergencyContactApi.addEmergencyContact(formData);
                        this.$message.success('紧急联系人添加成功');
                        this.$router.push('/EmergencyContact/index');
                    } catch (error) {
                        this.$message.error('添加紧急联系人失败');
                        console.error(error);
                    }
                }
            });
        },
        resetForm() {
            this.$refs.emergencyContactForm.resetFields();
        },
        goBack() {
            this.$router.push('/EmergencyContact/index');
        }
    }
};
</script>

<style scoped>
.add-emergency-contact-container {
    max-width: 600px;
    margin: 0 auto;
    padding: 20px;
    background: #fff;
    border-radius: 8px;
    box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
}
</style>
