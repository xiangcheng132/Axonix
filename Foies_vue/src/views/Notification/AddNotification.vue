<template>
    <div class="add-notification-container">
        <el-form ref="notificationForm" :model="notification" :rules="rules" label-width="120px">
            <el-form-item label="用户ID" prop="userId">
                <el-input v-model="notification.userId" />
            </el-form-item>

            <el-form-item label="标题" prop="title">
                <el-input v-model="notification.title" />
            </el-form-item>

            <el-form-item label="类型" prop="type">
                <el-input v-model="notification.type" />
            </el-form-item>

            <el-form-item label="消息内容" prop="message">
                <el-input v-model="notification.message" type="textarea" />
            </el-form-item>

            <el-form-item label="已读状态" prop="readStatus">
                <el-select v-model="notification.readStatus">
                    <el-option label="未读" :value="false" />
                    <el-option label="已读" :value="true" />
                </el-select>
            </el-form-item>

            <el-form-item label="创建时间" prop="createdtime">
                <el-date-picker v-model="notification.createdtime" type="datetime" placeholder="选择日期时间" />
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
import NotificationApi from '@/api/Notification_api';

export default {
    data() {
        return {
            notification: {
                userId: '',
                title: '',
                type: '',
                message: '',
                readStatus: false,
                createdtime: new Date() // 自动填充为当前时间
            },
            rules: {
                userId: [{ required: true, message: '请输入用户ID', trigger: 'blur' }],
                title: [{ required: true, message: '请输入标题', trigger: 'blur' }],
                type: [{ required: true, message: '请输入类型', trigger: 'blur' }],
                message: [{ required: true, message: '请输入消息内容', trigger: 'blur' }],
                readStatus: [{ required: true, message: '请选择已读状态', trigger: 'change' }],
                createdtime: [{ required: true, message: '请选择创建时间', trigger: 'change' }]
            }
        };
    },
    methods: {
        submitForm() {
            this.$refs.notificationForm.validate(async (valid) => {
                if (valid) {
                    try {
                        const formData = { ...this.notification };

                        // 格式化日期时间为 ISO 字符串
                        if (formData.createdtime) {
                            formData.createdtime = new Date(formData.createdtime).toISOString();
                        }

                        await NotificationApi.addNotification(formData);
                        this.$message.success('通知添加成功');
                        this.$router.push('/Notification/index');
                    } catch (error) {
                        this.$message.error('添加通知失败');
                        console.error(error);
                    }
                }
            });
        },
        resetForm() {
            this.$refs.notificationForm.resetFields();
        },
        goBack() {
            this.$router.push('/Notification/index');
        }
    }
};
</script>

<style scoped>
.add-notification-container {
    max-width: 600px;
    margin: 0 auto;
    padding: 20px;
    background: #fff;
    border-radius: 8px;
    box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
}
</style>
