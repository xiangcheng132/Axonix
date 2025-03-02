<template>
    <div class="app-container">
        <el-button type="primary" @click="handleAdd">添加紧急求助</el-button>
        <el-table :data="emergencyRequests" border>
            <el-table-column prop="id" label="ID" width="80" />
            <el-table-column prop="userId" label="用户ID" />
            <el-table-column prop="contactId" label="联系人ID" />
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
            <el-table-column prop="updatedtime" label="最后更新时间">
                <template slot-scope="scope">
                    {{ formatDate(scope.row.updatedtime) }}
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
import EmergencyRequestApi from '@/api/EmergencyRequest_api';

export default {
    data() {
        return {
            emergencyRequests: []
        };
    },
    created() {
        this.fetchEmergencyRequests();
    },
    methods: {
        async fetchEmergencyRequests() {
            try {
                const response = await EmergencyRequestApi.getEmergencyRequests();
                console.log("获取到的紧急求助数据:", response.data);
                this.emergencyRequests = response.data.map(req => ({
                    ...req,
                }));
            } catch (error) {
                console.error('获取紧急求助失败', error);
            }
        },
        handleAdd() {
            this.$router.push('/add-emergency-request');
        },
        handleView(request) {
            this.$router.push({ path: '/edit-emergency-request', query: { id: request.id } });
        },
        async handleDelete(id) {
            try {
                await EmergencyRequestApi.deleteEmergencyRequest(id);
                this.fetchEmergencyRequests();
                this.$message.success('紧急求助删除成功');
            } catch (error) {
                console.error('删除紧急求助失败', error);
            }
        },
        formatDate(date) {
            return date ? new Date(date).toLocaleString() : '无';
        },
        // 状态映射表
        formatStatus(status) {
            const statusMap = {
                pending: '待处理',
                processing: '处理中',
                completed: '已完成'
            };
            return statusMap[status] || '未知';
        },
        // 颜色映射表
        getStatusTag(status) {
            const tagMap = {
                pending: 'warning',
                processing: 'primary',
                completed: 'success'
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
