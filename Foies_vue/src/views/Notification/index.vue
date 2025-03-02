<template>
    <div class="app-container">
        <el-button type="primary" @click="handleAdd">添加通知</el-button>
        <el-table :data="notifications" border>
            <el-table-column prop="id" label="ID" width="80" />
            <el-table-column prop="userId" label="用户ID" />
            <el-table-column prop="title" label="标题" />
            <el-table-column prop="type" label="类型" />
            <el-table-column prop="readStatus" label="已读状态">
                <template slot-scope="scope">
                    <el-tag :type="getReadStatusTag(scope.row.readStatus)">
                        {{ scope.row.readStatus === 0 ? '未读' : '已读' }}
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
import NotificationApi from '@/api/Notification_api';

export default {
    data() {
        return {
            notifications: []
        };
    },
    created() {
        this.fetchNotifications();
    },
    methods: {
        async fetchNotifications() {
            try {
                const response = await NotificationApi.getNotifications();
                this.notifications = response.data;
            } catch (error) {
                console.error('获取通知失败', error);
            }
        },
        handleAdd() {
            this.$router.push('/add-notification');
        },
        handleView(notification) {
            this.$router.push({ path: '/edit-notification', query: { id: notification.id } });
        },
        async handleDelete(id) {
            try {
                await NotificationApi.deleteNotification(id);
                this.fetchNotifications();
                this.$message.success('通知删除成功');
            } catch (error) {
                console.error('删除通知失败', error);
            }
        },
        formatDate(date) {
            return date ? new Date(date).toLocaleString() : '无';
        },

        // 根据已读状态返回不同的颜色
        getReadStatusTag(readStatus) {
            return readStatus === 0 ? 'danger' : 'success';
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
