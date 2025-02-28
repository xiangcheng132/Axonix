<template>
    <div class="add-log-container">
        <el-form ref="logForm" :model="log" :rules="rules" label-width="120px">
            <el-form-item label="用户ID" prop="userId">
                <el-input v-model="log.userId" />
            </el-form-item>

            <el-form-item label="交易ID" prop="transactionId">
                <el-input v-model="log.transactionId" />
            </el-form-item>

            <el-form-item label="金额" prop="amount">
                <el-input v-model="log.amount" type="number" />
            </el-form-item>

            <el-form-item label="状态" prop="status">
                <el-select v-model="log.status">
                    <el-option label="待处理" value="pending" />
                    <el-option label="已完成" value="completed" />
                    <el-option label="失败" value="failed" />
                </el-select>
            </el-form-item>

            <el-form-item label="创建时间" prop="createdtime">
                <el-date-picker v-model="log.createdtime" type="datetime" placeholder="选择日期时间" />
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
import PaymentApi from '@/api/Payment_api';

export default {
    data() {
        return {
            log: {
                transactionId: '',
                amount: '',
                status: 'pending',
                createdtime: '' // 统一字段名
            },
            rules: {
                userId: [{ required: true, message: '请输入用户ID', trigger: 'blur' }],
                transactionId: [{ required: true, message: '请输入交易ID', trigger: 'blur' }],
                amount: [{ required: true, message: '请输入金额', trigger: 'blur' }],
                status: [{ required: true, message: '请选择状态', trigger: 'change' }],
                createdtime: [{ required: true, message: '请选择创建时间', trigger: 'change' }]
            }
        };
    },
    methods: {
        submitForm() {
            this.$refs.logForm.validate(async (valid) => {
                if (valid) {
                    try {
                        const formData = { ...this.log };

                        // 格式化日期时间为 ISO 字符串
                        if (formData.createdtime) {
                            formData.createdtime = new Date(formData.createdtime).toISOString();
                        }

                        await PaymentApi.addPayment(formData);
                        this.$message.success('支付日志添加成功');
                        this.$router.push('/Payment/index');
                    } catch (error) {
                        this.$message.error('添加支付日志失败');
                        console.error(error);
                    }
                }
            });
        },
        resetForm() {
            this.$refs.logForm.resetFields();
        },
        goBack() {
            this.$router.push('/Payment/index');
        }
    }
};
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
