<template>
    <div class="app-container">
        <el-button type="primary" @click="handleAdd">添加支付日志</el-button>
        <el-table :data="payments" border>
            <el-table-column prop="id" label="ID" width="80" />
            <el-table-column prop="userId" label="用户ID" />
            <el-table-column prop="transactionId" label="交易ID" />
            <el-table-column prop="amount" label="金额" />
            <el-table-column prop="status" label="状态" />
            <el-table-column prop="createdtime" label="创建时间">
                <template slot-scope="scope">
                    {{ formatDate(scope.row.createdtime) }}
                </template>
            </el-table-column>

            <el-table-column label="操作" width="200">
                <template slot-scope="scope">
                    <el-button size="mini" @click="handleView(scope.row)">查看</el-button>
                    <el-button size="mini" type="danger" @click="handleDelete(scope.row.id)">删除</el-button>
                </template>
            </el-table-column>
        </el-table>
    </div>
</template>

<script>
import PaymentApi from '@/api/Payment_api';

export default {
    data() {
        return {
            payments: []
        };
    },
    created() {
        this.fetchPayments();
    },
    methods: {
        async fetchPayments() {
            try {
                const response = await PaymentApi.getPayments();
                this.payments = response.data;
            } catch (error) {
                console.error('获取支付日志失败', error);
            }
        },

        handleAdd() {
            this.$router.push('/add-payment-log');
        },
        handleView(payment) {
            this.$router.push({ name: 'PaymentLogDetail', params: { id: payment.id } }); // 使用路由参数
        },
        async handleDelete(id) {
            try {
                await PaymentApi.deletePayment(id);
                this.fetchPayments();
                this.$message.success('支付日志删除成功');
            } catch (error) {
                console.error('删除支付日志失败', error);
            }
        },
        formatDate(date) {
            return date ? new Date(date).toLocaleString() : '无';
        }
    }
};
</script>

<style scoped>
.app-container {
    padding: 20px;
}

.el-table {
    margin-top: 20px;
}

.el-button {
    margin-right: 10px;
}
</style>