<template>
    <div class="app-container">
        <el-button type="primary" @click="handleAdd">添加设施</el-button>
        <el-button type="danger" @click="confirmBatchDelete" :disabled="selectedFacilities.length === 0">批量删除</el-button>

        <el-table :data="facilities" border @selection-change="handleSelectionChange">
            <el-table-column type="selection" width="55" />
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
                    <el-button size="mini" type="danger" @click="confirmDelete(scope.row.id)">删除</el-button>
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
            facilities: [],
            selectedFacilities: [] // 选中的设施ID数组
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

        handleAdd() {
            this.$router.push('/add-facility');
        },

        handleView(facility) {
            this.$router.push({ path: '/edit-facility', query: { id: facility.id } });
        },

        // 删除单个设施
        async handleDelete(id) {
            try {
                await FacilityApi.deleteFacility(id);
                this.fetchFacilities();
                this.$message.success('设施删除成功');
            } catch (error) {
                console.error('删除设施失败', error);
            }
        },

        // 删除单个设施前的确认操作
        confirmDelete(id) {
            this.$confirm('确定要删除此设施吗？', '警告', {
                type: 'warning'
            }).then(() => {
                this.handleDelete(id);
            }).catch(() => {});
        },

        // 批量删除前的确认操作
        async confirmBatchDelete() {
            if (this.selectedFacilities.length === 0) return;

            this.$confirm('确定要删除选中的设施吗？', '警告', {
                type: 'warning'
            }).then(async () => {
                try {
                    // 执行批量删除
                    await Promise.all(this.selectedFacilities.map(id => FacilityApi.deleteFacility(id)));
                    this.fetchFacilities();
                    this.selectedFacilities = [];
                    this.$message.success('批量删除成功');
                } catch (error) {
                    console.error('批量删除失败', error);
                }
            }).catch(() => {});
        },

        // 监听表格多选
        handleSelectionChange(selection) {
            this.selectedFacilities = selection.map(item => item.id);
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
