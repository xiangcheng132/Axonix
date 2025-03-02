<template>
    <div class="app-container">
        <el-button type="primary" @click="handleAdd">添加紧急联系人</el-button>
        <el-table :data="emergencyContacts" border>
            <el-table-column prop="id" label="ID" width="80" />
            <el-table-column prop="userId" label="用户ID" />
            <el-table-column prop="name" label="姓名" />
            <el-table-column prop="phone" label="电话" />
            <el-table-column prop="relationship" label="关系" />
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
import EmergencyContactApi from '@/api/EmergencyContact_api';

export default {
    data() {
        return {
            emergencyContacts: []
        };
    },
    created() {
        this.fetchEmergencyContacts();
    },
    methods: {
        async fetchEmergencyContacts() {
            try {
                const response = await EmergencyContactApi.getEmergencyContacts();
                this.emergencyContacts = response.data;
            } catch (error) {
                console.error('获取紧急联系人失败', error);
            }
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
