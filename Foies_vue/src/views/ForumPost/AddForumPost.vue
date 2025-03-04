<template>
    <div class="add-post-container">
        <el-form ref="postForm" :model="post" :rules="rules" label-width="120px">
            <el-form-item label="用户ID" prop="userId">
                <el-input v-model="post.userId" />
            </el-form-item>

            <el-form-item label="标题" prop="title">
                <el-input v-model="post.title" />
            </el-form-item>

            <el-form-item label="内容" prop="content">
                <el-input v-model="post.content" type="textarea" :rows="5" />
            </el-form-item>

            <el-form-item label="创建时间" prop="createdtime">
                <el-date-picker v-model="post.createdtime" type="datetime" placeholder="选择日期时间" />
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
import ForumPostApi from '@/api/ForumPost_api';

export default {
    data() {
        return {
            post: {
                userId: '',
                title: '',
                content: '',
                createdtime: new Date() // 默认当前时间
            },
            rules: {
                userId: [{ required: true, message: '请输入用户ID', trigger: 'blur' }],
                title: [{ required: true, message: '请输入标题', trigger: 'blur' }],
                content: [{ required: true, message: '请输入内容', trigger: 'blur' }],
                createdtime: [{ required: true, message: '请选择创建时间', trigger: 'change' }]
            }
        };
    },
    methods: {
        submitForm() {
            this.$refs.postForm.validate(async (valid) => {
                if (valid) {
                    try {
                        const formData = { ...this.post };

                        // 格式化日期时间
                        if (formData.createdtime) {
                            formData.createdtime = new Date(formData.createdtime).toISOString();
                        }

                        await ForumPostApi.addPost(formData);
                        this.$message.success('帖子添加成功');
                        this.$router.push('/Forum/ForumPost');
                    } catch (error) {
                        this.$message.error('添加帖子失败');
                        console.error(error);
                    }
                }
            });
        },
        resetForm() {
            this.$refs.postForm.resetFields();
        },
        goBack() {
            this.$router.push('/Forum/ForumPost');
        }
    }
};
</script>

<style scoped>
.add-post-container {
    max-width: 600px;
    margin: 0 auto;
    padding: 20px;
    background: #fff;
    border-radius: 8px;
    box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
}
</style>
