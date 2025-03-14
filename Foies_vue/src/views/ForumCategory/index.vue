<template>
    <div class="app-container">
        <!-- 搜索表单 -->
        <el-form :inline="true" class="demo-form-inline">
            <el-form-item label="分类名称">
                <el-input v-model="filters.name" placeholder="输入分类名称"></el-input>
            </el-form-item>
            <el-form-item>
                <el-button type="primary" @click="fetchForumCategories">查询</el-button>
                <el-button @click="resetSearch">重置</el-button>
            </el-form-item>
        </el-form>

        <!-- 操作按钮 -->
        <el-button type="primary" @click="handleAdd">添加分类</el-button>
        <el-button type="danger" @click="confirmBatchDelete" :disabled="selectedCategories.length === 0">批量删除</el-button>

        <!-- 分类表格 -->
        <el-table :data="forumCategories" border @selection-change="handleSelectionChange">
            <el-table-column type="selection" width="55" />
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
                    <el-button size="mini" @click="handleView(scope.row)">编辑</el-button>
                    <el-button size="mini" type="danger" @click="confirmDelete(scope.row.id)">删除</el-button>
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
            forumCategories: [],
            selectedCategories: [], // 选中的分类ID数组
            filters: {
                name: '' // 搜索的分类名称
            }
        };
    },
    created() {
        this.fetchForumCategories();
    },
    methods: {
        async fetchForumCategories() {
            const example = {
                oredCriteria: [{ criteria: [] }]
            };

            if (this.filters.name) {
                example.oredCriteria[0].criteria.push({
                    condition: "name LIKE",
                    value: `%${this.filters.name}%`,
                    singleValue: true
                });
            }

            try {
                const response = await ForumCategoryApi.getForumCategories(example);
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

        // 删除单个分类
        async handleDelete(id) {
            try {
                await ForumCategoryApi.deleteForumCategory(id);
                this.fetchForumCategories();
                this.$message.success('分类删除成功');
            } catch (error) {
                console.error('删除分类失败', error);
            }
        },

        // 批量删除前的确认操作
        confirmDelete(id) {
            this.$confirm('确定要删除此分类吗？', '警告', {
                type: 'warning'
            }).then(() => {
                this.handleDelete(id);
            }).catch(() => {});
        },

        // 批量删除
        async confirmBatchDelete() {
            if (this.selectedCategories.length === 0) return;

            this.$confirm('确定要删除选中的分类吗？', '警告', {
                type: 'warning'
            }).then(async () => {
                try {
                    // 执行批量删除
                    await Promise.all(this.selectedCategories.map(id => ForumCategoryApi.deleteForumCategory(id)));
                    this.fetchForumCategories();
                    this.selectedCategories = [];
                    this.$message.success('批量删除成功');
                } catch (error) {
                    console.error('批量删除失败', error);
                }
            }).catch(() => {});
        },

        // 监听表格多选
        handleSelectionChange(selection) {
            this.selectedCategories = selection.map(item => item.id);
        },

        // 重置搜索条件
        resetSearch() {
            this.filters.name = '';
            this.fetchForumCategories();
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
