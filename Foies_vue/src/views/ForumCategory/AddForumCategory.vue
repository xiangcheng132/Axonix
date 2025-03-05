<template>
    <div class="add-forum-category-container">
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
                description: '',
                createdtime: '',
                updatedtime: ''
            },
            rules: {
                name: [{ required: true, message: '请输入分类名称', trigger: 'blur' }],
                description: [{ required: true, message: '请输入描述', trigger: 'blur' }]
            }
        };
    },
    methods: {
        submitForm() {
            this.$refs.forumCategoryForm.validate(async (valid) => {
                if (valid) {
                    try {
                        const now = new Date().toISOString();
                        this.forumCategory.createdtime = now;
                        this.forumCategory.updatedtime = now;

                        await ForumCategoryApi.addForumCategorySelective(this.forumCategory);
                        this.$message.success('分类添加成功');
                        this.$router.push('/Forum/ForumCategory');
                    } catch (error) {
                        this.$message.error('添加分类失败');
                        console.error(error);
                    }
                }
            });
        },
        resetForm() {
            this.$refs.forumCategoryForm.resetFields();
        },
        goBack() {
            this.$router.push('/Forum/ForumCategory');
        }
    }
};
</script>

<style scoped>
.add-forum-category-container {
    max-width: 600px;
    margin: 0 auto;
    padding: 20px;
    background: #fff;
    border-radius: 8px;
    box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
}
</style>