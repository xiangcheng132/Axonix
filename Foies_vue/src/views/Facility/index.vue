<template>
    <div class="app-container">
        <el-button type="primary" @click="handleAdd">添加设施</el-button>
        <el-table :data="facilities" border>
            <el-table-column prop="id" label="ID" width="80" />
            <el-table-column prop="name" label="设施名称" />
            <el-table-column prop="facilityType" label="设施类型" />
            <el-table-column prop="createdtime" label="创建时间">
                <template slot-scope="scope">
                    {{ formatDate(scope.row.createdtime) }}
                </template>
            </el-table-column>
            <el-table-column prop="updatedtime" label="更新时间">
                <template slot-scope="scope">
                    {{ formatBeijingTime(scope.row.updatedtime) }}
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
import FacilityApi from '@/api/Facility_api';

export default {
    data() {
        return {
            facilities: []
        };
    },
    created() {
        this.fetchFacilities();
    },
    methods: {
        async fetchFacilities() {
            try {
                const response = await FacilityApi.getFacilities();
                this.facilities = response.data;
            } catch (error) {
                console.error('获取设施列表失败', error);
            }
        },
        formatBeijingTime(utcTime) {
            if (!utcTime) return '无';
            const date = new Date(utcTime);
            return new Intl.DateTimeFormat('zh-CN', { 
                timeZone: 'Asia/Shanghai', 
                year: 'numeric', month: '2-digit', day: '2-digit',
                hour: '2-digit', minute: '2-digit', second: '2-digit' 
            }).format(date);
        },
        handleAdd() {
            this.$router.push('/add-facility');
        },
        handleView(facility) {
            this.$router.push({ path: '/edit-facility', query: { id: facility.id } });
        },
        async handleDelete(id) {
            try {
                await FacilityApi.deleteFacility(id);
                this.fetchFacilities();
                this.$message.success('设施删除成功');
            } catch (error) {
                console.error('删除设施失败', error);
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
