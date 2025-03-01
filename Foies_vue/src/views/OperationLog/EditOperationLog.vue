<template>
    <div class="edit-operation-log-container">
        <el-form ref="operationLogForm" :model="operationLog" :rules="rules" label-width="150px">
            <!-- 管理员ID -->
            <el-form-item label="管理员ID" prop="adminId">
                <el-input v-model="operationLog.adminId" />
            </el-form-item>

            <!-- 操作内容 -->
            <el-form-item label="操作内容" prop="action">
                <el-input v-model="operationLog.action" />
            </el-form-item>

            <!-- 目标ID -->
            <el-form-item label="目标ID" prop="targetId">
                <el-input v-model="operationLog.targetId" />
            </el-form-item>

            <!-- 操作时间 -->
            <el-form-item label="操作时间" prop="timestamp">
                <el-date-picker v-model="operationLog.timestamp" type="datetime" placeholder="选择日期时间" />
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
import OperationLogApi from '@/api/OperationLog_api';

export default {
    data() {
        return {
            operationLog: {
                adminId: '',
                action: '',
                targetId: '',
                timestamp: ''
            },
            rules: {
                adminId: [{ required: true, message: '请输入管理员ID', trigger: 'blur' }],
                action: [{ required: true, message: '请输入操作内容', trigger: 'blur' }],
                targetId: [{ required: true, message: '请输入目标ID', trigger: 'blur' }],
                timestamp: [{ required: true, message: '请选择操作时间', trigger: 'change' }]
            }
        };
    },
    created() {
        this.fetchOperationLog();
    },
    methods: {
        async fetchOperationLog() {
            const id = this.$route.query.id;
            if (!id) {
                this.$message.error('未提供日志 ID');
                this.$router.push('/OperationLog/index');
                return;
            }
            try {
                const response = await OperationLogApi.getOperationLogById(id);
                this.operationLog = response.data;
            } catch (error) {
                this.$message.error('获取操作日志失败');
                console.error(error);
            }
        },
        submitForm() {
            this.$refs.operationLogForm.validate(async (valid) => {
                if (valid) {
                    try {
                        await OperationLogApi.updateOperationLog(this.operationLog.id, this.operationLog);
                        this.$message.success('操作日志更新成功');
                        this.$router.push('/OperationLog/index');
                    } catch (error) {
                        this.$message.error('更新操作日志失败');
                        console.error(error);
                    }
                }
            });
        },
        resetForm() {
            this.fetchOperationLog(); // 重新加载日志数据以恢复初始状态
        },
        goBack() {
            this.$router.push('/OperationLog/index');
        }
    }
};
</script>

<style scoped>
.edit-operation-log-container {
    max-width: 600px;
    margin: 40px auto;
    padding: 20px;
    background-color: #fff;
    border-radius: 8px;
    box-shadow: 0 2px 12px rgba(0, 0, 0, 0.1);
}
</style>
