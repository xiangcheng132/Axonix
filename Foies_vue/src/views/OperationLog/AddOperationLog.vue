<template>
    <div class="add-operation-log-container">
        <el-form ref="operationLogForm" :model="operationLog" :rules="rules" label-width="120px">
            <el-form-item label="管理员ID" prop="adminId">
                <el-input v-model="operationLog.adminId" />
            </el-form-item>

            <el-form-item label="操作内容" prop="action">
                <el-input v-model="operationLog.action" />
            </el-form-item>

            <el-form-item label="目标ID" prop="targetId">
                <el-input v-model="operationLog.targetId" />
            </el-form-item>

            <el-form-item label="操作时间" prop="timestamp">
                <el-date-picker v-model="operationLog.timestamp" type="datetime" placeholder="选择日期时间" />
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
import OperationLogApi from '@/api/OperationLog_api';

export default {
    data() {
        return {
            operationLog: {
                adminId: '',
                action: '',
                targetId: '',
                timestamp: new Date() // 自动填充为当前时间
            },
            rules: {
                adminId: [{ required: true, message: '请输入管理员ID', trigger: 'blur' }],
                action: [{ required: true, message: '请输入操作内容', trigger: 'blur' }],
                targetId: [{ required: true, message: '请输入目标ID', trigger: 'blur' }],
                timestamp: [{ required: true, message: '请选择操作时间', trigger: 'change' }]
            }
        };
    },
    methods: {
        submitForm() {
            this.$refs.operationLogForm.validate(async (valid) => {
                if (valid) {
                    try {
                        const formData = { ...this.operationLog };

                        // 格式化时间为 ISO 字符串
                        if (formData.timestamp) {
                            formData.timestamp = new Date(formData.timestamp).toISOString();
                        }

                        await OperationLogApi.addOperationLog(formData);
                        this.$message.success('操作日志添加成功');
                        this.$router.push('/OperationLog/index');
                    } catch (error) {
                        this.$message.error('添加操作日志失败');
                        console.error(error);
                    }
                }
            });
        },
        resetForm() {
            this.$refs.operationLogForm.resetFields();
        },
        goBack() {
            this.$router.push('/OperationLog/index');
        }
    }
};
</script>

<style scoped>
.add-operation-log-container {
    max-width: 600px;
    margin: 0 auto;
    padding: 20px;
    background: #fff;
    border-radius: 8px;
    box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
}
</style>
