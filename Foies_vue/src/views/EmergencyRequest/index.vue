<template>
    <div class="app-container">
        <!-- 搜索表单 -->
        <el-form :inline="true" class="demo-form-inline">
            <el-form-item label="用户ID">
                <el-input v-model="filters.userId" placeholder="输入用户ID"></el-input>
            </el-form-item>
            <el-form-item label="联系人ID">
                <el-input v-model="filters.contactId" placeholder="输入联系人ID"></el-input>
            </el-form-item>
            <el-form-item label="状态">
                <el-select v-model="filters.status" placeholder="选择状态">
                    <el-option label="待处理" value="pending"></el-option>
                    <el-option label="处理中" value="processing"></el-option>
                    <el-option label="已完成" value="completed"></el-option>
                </el-select>
            </el-form-item>
            <el-form-item>
                <el-button type="primary" @click="fetchEmergencyRequests">查询</el-button>
                <el-button @click="resetSearch">重置</el-button>
            </el-form-item>
        </el-form>

        <!-- 操作按钮 -->
        <el-button type="primary" @click="handleAdd">添加紧急求助</el-button>
        <el-button type="danger" @click="confirmBatchDelete" :disabled="selectedRequests.length === 0">批量删除</el-button>

        <!-- 紧急求助表格 -->
        <el-table :data="emergencyRequests" border @selection-change="handleSelectionChange">
            <el-table-column type="selection" width="55" />
            <el-table-column prop="id" label="ID" width="80" />
            <el-table-column prop="userId" label="用户ID" />
            <el-table-column prop="contactId" label="联系人ID" />
            <el-table-column prop="status" label="状态">
                <template slot-scope="scope">
                    <el-tag :type="getStatusTag(scope.row.status)">{{ formatStatus(scope.row.status) }}</el-tag>
                </template>
            </el-table-column>
            <el-table-column prop="createdtime" label="创建时间">
                <template slot-scope="scope">{{ formatDate(scope.row.createdtime) }}</template>
            </el-table-column>
            <el-table-column prop="updatedtime" label="最后更新时间">
                <template slot-scope="scope">{{ formatDate(scope.row.updatedtime) }}</template>
            </el-table-column>
            <el-table-column label="操作" width="200">
                <template slot-scope="scope">
                    <el-button size="mini" @click="handleView(scope.row)">编辑</el-button>
                    <el-button size="mini" type="danger" @click="confirmDelete(scope.row.id)">删除</el-button>
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
            emergencyRequests: [],
            selectedRequests: [],
            filters: {
                userId: '',
                contactId: '',
                status: ''
            }
        };
    },
    created() {
        this.fetchEmergencyRequests();
    },
    methods: {
        async fetchEmergencyRequests() {
            const example = {
                oredCriteria: [{ criteria: [] }]
            };

            if (this.filters.userId) {
                example.oredCriteria[0].criteria.push({
                    condition: "user_id LIKE",
                    value: `%${this.filters.userId}%`,
                    singleValue: true
                });
            }

            if (this.filters.contactId) {
                example.oredCriteria[0].criteria.push({
                    condition: "contact_id LIKE",
                    value: `%${this.filters.contactId}%`,
                    singleValue: true
                });
            }

            if (this.filters.status) {
                example.oredCriteria[0].criteria.push({
                    condition: "status =",
                    value: this.filters.status,
                    singleValue: true
                });
            }

            try {
                const response = await EmergencyRequestApi.getEmergencyRequests(example);
                this.emergencyRequests = response.data;
            } catch (error) {
                console.error('获取紧急求助失败', error);
            }
        },

        resetSearch() {
            this.filters.userId = '';
            this.filters.contactId = '';
            this.filters.status = '';
            this.fetchEmergencyRequests();
        },

        handleSelectionChange(selection) {
            this.selectedRequests = selection.map(item => item.id);
        },

        handleAdd() {
            this.$router.push('/add-emergency-request');
        },

        handleView(request) {
            this.$router.push({ path: '/edit-emergency-request', query: { id: request.id } });
        },

        confirmDelete(id) {
            this.$confirm('确定要删除此紧急求助吗？', '警告', { type: 'warning' })
                .then(() => this.handleDelete(id))
                .catch(() => {});
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

        formatStatus(status) {
            const statusMap = {
                pending: '待处理',
                processing: '处理中',
                completed: '已完成'
            };
            return statusMap[status] || '未知';
        },

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
