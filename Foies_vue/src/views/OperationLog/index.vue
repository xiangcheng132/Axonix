<template>
    <div class="app-container">
        <el-button type="primary" @click="handleAdd">添加日志</el-button>
        <el-table :data="operationLogs" border>
            <el-table-column prop="id" label="ID" width="80" />
            <el-table-column prop="adminId" label="管理员ID" />
            <el-table-column prop="action" label="操作内容" />
            <el-table-column prop="targetId" label="目标ID" />
            <el-table-column prop="timestamp" label="操作时间">
                <template slot-scope="scope">
                    {{ formatDate(scope.row.timestamp) }}
                </template>
            </el-table-column>
            <el-table-column label="操作" width="200">
                <template slot-scope="scope">
                    <el-button size="mini" @click="handleView(scope.row)">编辑</el-button>
                    <el-button size="mini" type="danger" @click="handleDelete(scope.row.id)">删除</el-button>
                </template>
            </el-table-column>
        </el-table>
    </div>
</template>

<script>
import OperationLogApi from '@/api/OperationLog_api';

export default {
    data() {
        return {
            operationLogs: []
        };
    },
    created() {
        this.fetchOperationLogs();
    },
    methods: {
        async fetchOperationLogs() {
            try {
                const response = await OperationLogApi.getOperationLogs();
                this.operationLogs = response.data;
            } catch (error) {
                console.error('获取操作日志失败', error);
            }
        },
        handleAdd() {
            this.$router.push('/add-operation-log');
        },
        handleView(log) {
            this.$router.push({ path: '/edit-operation-log', query: { id: log.id } });
        },
        async handleDelete(id) {
            try {
                await OperationLogApi.deleteOperationLog(id);
                this.fetchOperationLogs();
                this.$message.success('操作日志删除成功');
            } catch (error) {
                console.error('删除操作日志失败', error);
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
