<template>
    <div class="app-container">
        <el-button type="primary" @click="handleAdd">添加分类</el-button>
        <el-table :data="forumCategories" border>
            <el-table-column prop="id" label="ID" width="80" />
            <el-table-column prop="name" label="名称" />
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
import ForumCategoryApi from '@/api/ForumCategory_api';

export default {
    data() {
        return {
            forumCategories: []
        };
    },
    created() {
        this.fetchForumCategories();
    },
    methods: {
        async fetchForumCategories() {
            try {
                const response = await ForumCategoryApi.getForumCategories();
                this.forumCategories = response.data;
            } catch (error) {
                console.error('获取分类失败', error);
            }
        },
        handleAdd() {
            this.$router.push('/add-forum-category');
        },
        handleView(forumCategory) {
            this.$router.push({ path: '/edit-forum-category', query: { id: forumCategory.id } });
        },
        async handleDelete(id) {
            try {
                await ForumCategoryApi.deleteForumCategory(id);
                this.fetchForumCategories();
                this.$message.success('分类删除成功');
            } catch (error) {
                console.error('删除分类失败', error);
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
