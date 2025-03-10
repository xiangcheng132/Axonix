<template>
    <div class="app-container">
        <!-- 搜索表单 -->
        <el-form :inline="true" class="demo-form-inline">
            <el-form-item label="用户ID">
                <el-input v-model="filters.userId" placeholder="输入用户ID"></el-input>
            </el-form-item>
            <el-form-item label="姓名">
                <el-input v-model="filters.name" placeholder="输入姓名"></el-input>
            </el-form-item>
            <el-form-item label="电话">
                <el-input v-model="filters.phone" placeholder="输入电话"></el-input>
            </el-form-item>
            <el-form-item>
                <el-button type="primary" @click="fetchEmergencyContacts">查询</el-button>
                <el-button @click="resetSearch">重置</el-button>
            </el-form-item>
        </el-form>

        <!-- 操作按钮 -->
        <el-button type="primary" @click="handleAdd">添加紧急联系人</el-button>
        <el-button type="danger" @click="confirmBatchDelete" :disabled="selectedContacts.length === 0">批量删除</el-button>

        <!-- 紧急联系人表格 -->
        <el-table :data="emergencyContacts" border @selection-change="handleSelectionChange">
            <el-table-column type="selection" width="55" />
            <el-table-column prop="id" label="ID" width="80" />
            <el-table-column prop="userId" label="用户ID" />
            <el-table-column prop="name" label="姓名" />
            <el-table-column prop="phone" label="电话" />
            <el-table-column prop="relationship" label="关系" />
            <el-table-column prop="createdtime" label="创建时间">
                <template #default="scope">
                    {{ formatDate(scope.row.createdtime) }}
                </template>
            </el-table-column>
            <el-table-column label="操作" width="200">
                <template #default="scope">
                    <el-button size="mini" @click="handleView(scope.row)">查看</el-button>
                    <el-button size="mini" type="danger" @click="handleDelete(scope.row.id)">删除</el-button>
                </template>
            </el-table-column>
        </el-table>
    </div>
</template>

<script>
import EmergencyContactApi from '@/api/EmergencyContact_api';

export default {
    data() {
        return {
            emergencyContacts: [],
            selectedContacts: [],
            filters: {
                userId: '',
                name: '',
                phone: ''
            }
        };
    },
    created() {
        this.fetchEmergencyContacts();
    },
    methods: {
        async fetchEmergencyContacts() {
            const query = {
                oredCriteria: [{ criteria: [] }]
            };

            if (this.filters.userId) {
                query.oredCriteria[0].criteria.push({
                    condition: "user_id LIKE",
                    value: `%${this.filters.userId}%`,
                    singleValue: true
                });
            }

            if (this.filters.name) {
                query.oredCriteria[0].criteria.push({
                    condition: "name LIKE",
                    value: `%${this.filters.name}%`,
                    singleValue: true
                });
            }

            if (this.filters.phone) {
                query.oredCriteria[0].criteria.push({
                    condition: "phone LIKE",
                    value: `%${this.filters.phone}%`,
                    singleValue: true
                });
            }

            try {
                const response = await EmergencyContactApi.getEmergencyContacts(query);
                this.emergencyContacts = response.data;
            } catch (error) {
                console.error('获取紧急联系人失败', error);
            }
        },
        resetSearch() {
            this.filters.userId = '';
            this.filters.name = '';
            this.filters.phone = '';
            this.fetchEmergencyContacts();
        },
        handleSelectionChange(selection) {
            this.selectedContacts = selection.map(item => item.id);
        },
        handleAdd() {
            this.$router.push('/add-emergency-contact');
        },
        handleView(contact) {
            this.$router.push({ path: '/edit-emergency-contact', query: { id: contact.id } });
        },
        async handleDelete(id) {
            try {
                await EmergencyContactApi.deleteEmergencyContact(id);
                this.fetchEmergencyContacts();
                this.$message.success('紧急联系人删除成功');
            } catch (error) {
                console.error('删除紧急联系人失败', error);
            }
        },
        confirmBatchDelete() {
            if (this.selectedContacts.length === 0) return;

            this.$confirm('确定要删除选中的紧急联系人吗？', '警告', {
                type: 'warning'
            }).then(async () => {
                try {
                    await Promise.all(this.selectedContacts.map(id => EmergencyContactApi.deleteEmergencyContact(id)));
                    this.fetchEmergencyContacts();
                    this.selectedContacts = [];
                    this.$message.success('批量删除成功');
                } catch (error) {
                    console.error('批量删除失败', error);
                }
            }).catch(() => {});
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
