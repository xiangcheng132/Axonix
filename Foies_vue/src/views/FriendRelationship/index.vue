<template>
    <div class="app-container">
        <el-button type="primary" @click="handleAdd">添加好友关系</el-button>
        <el-table :data="friendRelationships" border>
            <el-table-column prop="id" label="ID" width="80" />
            <el-table-column prop="userId" label="用户ID" />
            <el-table-column prop="friendUserId" label="好友用户ID" />
            <el-table-column prop="status" label="状态" />
            <el-table-column prop="createdtime" label="创建时间">
                <template slot-scope="scope">
                    {{ formatDate(scope.row.createdtime) }}
                </template>
            </el-table-column>
            <el-table-column prop="updatedtime" label="更新时间">
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
import FriendRelationshipApi from '@/api/FriendRelationship_api';

export default {
    data() {
        return {
            friendRelationships: []
        };
    },
    created() {
        this.fetchFriendRelationships();
    },
    methods: {
        async fetchFriendRelationships() {
            try {
                const response = await FriendRelationshipApi.getFriendRelationships();
                this.friendRelationships = response.data;
            } catch (error) {
                console.error('获取好友关系失败', error);
            }
        },
        handleAdd() {
            this.$router.push('/add-friend-relationship');
        },
        handleView(friendRelationship) {
            this.$router.push({ path: '/edit-friend-relationship', query: { id: friendRelationship.id } });
        },
        async handleDelete(id) {
            try {
                await FriendRelationshipApi.deleteFriendRelationship(id);
                this.fetchFriendRelationships();
                this.$message.success('好友关系删除成功');
            } catch (error) {
                console.error('删除好友关系失败', error);
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
