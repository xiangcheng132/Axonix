<template>
    <div class="edit-forum-category-container">
        <el-form ref="forumCategoryForm" :model="forumCategory" :rules="rules" label-width="120px">
            <el-form-item label="名称" prop="name">
                <el-input v-model="forumCategory.name" />
            </el-form-item>

            <el-form-item label="描述" prop="description">
                <el-input v-model="forumCategory.description" type="textarea" />
            </el-form-item>

            <el-form-item>
                <el-button type="primary" @click="submitForm">提交</el-button>
                <el-button @click="resetForm">重置</el-button>
                <el-button @click="goBack">返回</el-button>
            </el-form-item>
        </el-form>
    </div>
</template>

<script>
import ForumCategoryApi from '@/api/ForumCategory_api';

export default {
    data() {
        return {
            forumCategory: {
                name: '',
                description: ''
            },
            rules: {
                name: [{ required: true, message: '请输入分类名称', trigger: 'blur' }],
                description: [{ required: true, message: '请输入描述', trigger: 'blur' }]
            }
        };
    },
    created() {
        this.fetchForumCategory();
    },
    methods: {
        async fetchForumCategory() {
            const id = this.$route.query.id;
            try {
                const response = await ForumCategoryApi.getForumCategoryById(id);
                this.forumCategory = response.data;
            } catch (error) {
                console.error('获取分类详情失败', error);
            }
        },
        submitForm() {
            this.$refs.forumCategoryForm.validate(async (valid) => {
                if (valid) {
                    try {
                        // 先获取当前时间并更新 `updatedtime`
                        this.forumCategory.updatedtime = new Date().toISOString();

                        await ForumCategoryApi.updateForumCategorySelective(this.forumCategory.id, this.forumCategory);
                        this.$message.success('分类更新成功');
                        this.$router.push('/Forum/ForumCategory');
                    } catch (error) {
                        this.$message.error('更新分类失败');
                        console.error(error);
                    }
                }
            });
        },
        resetForm() {
            this.fetchForumCategory();
        },
        goBack() {
            this.$router.push('/ForumCategory/index');
        }
    }
};
</script>

<style scoped>
.edit-forum-category-container {
    max-width: 600px;
    margin: 40px auto;
    padding: 20px;
    background-color: #fff;
    border-radius: 8px;
    box-shadow: 0 2px 12px rgba(0, 0, 0, 0.1);
}
</style>
