<template>
    <div class="app-container">
        <!-- 操作按钮 -->
        <el-button type="primary" @click="handleAdd">添加支付日志</el-button>
        <el-button type="danger" @click="handleBatchDelete" :disabled="selectedPayments.length === 0">批量删除</el-button>

        <!-- 支付日志列表 -->
        <el-table :data="payments" border @selection-change="handleSelectionChange">
            <el-table-column type="selection" width="55" />
            <el-table-column prop="id" label="ID" width="80" />
            <el-table-column prop="userId" label="用户ID" />
            <el-table-column prop="transactionId" label="交易ID" />
            <el-table-column prop="amount" label="金额" />
            <el-table-column prop="status" label="状态">
                <template slot-scope="scope">
                    <el-tag :type="getStatusTag(scope.row.status)">
                        {{ formatStatus(scope.row.status) }}
                    </el-tag>
                </template>
            </el-table-column>
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
            payments: [],
            selectedPayments: [] // 选中的支付日志
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
            this.$router.push({ name: 'PaymentLogDetail', params: { id: payment.id } });
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

        // 监听选中的日志
        handleSelectionChange(selection) {
            this.selectedPayments = selection.map(payment => payment.id);
        },

        // 批量删除
        async handleBatchDelete() {
            if (this.selectedPayments.length === 0) return;
            try {
                await Promise.all(this.selectedPayments.map(id => PaymentApi.deletePayment(id)));
                this.fetchPayments();
                this.selectedPayments = [];
                this.$message.success('批量删除成功');
            } catch (error) {
                console.error('批量删除失败', error);
            }
        },

        formatDate(date) {
            return date ? new Date(date).toLocaleString() : '无';
        },

        formatStatus(status) {
            const statusMap = {
                pending: '待处理',
                completed: '已完成',
                failed: '失败'
            };
            return statusMap[status] || '未知';
        },

        getStatusTag(status) {
            const tagMap = {
                pending: 'warning',
                completed: 'success',
                failed: 'danger'
            };
            return tagMap[status] || 'info';
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
