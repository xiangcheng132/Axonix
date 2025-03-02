<template>
    <div class="app-container">
        <el-button type="primary" @click="handleAdd">添加位置共享</el-button>
        <el-table :data="locationShares" border>
            <el-table-column prop="id" label="ID" width="80" />
            <el-table-column prop="userId" label="用户ID" />
            <el-table-column prop="sharedWith" label="共享给用户ID" />
            <el-table-column prop="startTime" label="开始时间">
                <template slot-scope="scope">
                    {{ formatDate(scope.row.startTime) }}
                </template>
            </el-table-column>
            <el-table-column prop="endTime" label="结束时间">
                <template slot-scope="scope">
                    {{ formatDate(scope.row.endTime) }}
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
import LocationShareApi from '@/api/LocationShare_api';

export default {
    data() {
        return {
            locationShares: []
        };
    },
    created() {
        this.fetchLocationShares();
    },
    methods: {
        async fetchLocationShares() {
            try {
                const response = await LocationShareApi.getLocationShares();
                this.locationShares = response.data;
            } catch (error) {
                console.error('获取位置共享信息失败', error);
            }
        },
        handleAdd() {
            this.$router.push('/add-location-share');
        },
        handleView(locationShare) {
            this.$router.push({ path: '/edit-location-share', query: { id: locationShare.id } });
        },
        async handleDelete(id) {
            try {
                await LocationShareApi.deleteLocationShare(id);
                this.fetchLocationShares();
                this.$message.success('位置共享删除成功');
            } catch (error) {
                console.error('删除位置共享失败', error);
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