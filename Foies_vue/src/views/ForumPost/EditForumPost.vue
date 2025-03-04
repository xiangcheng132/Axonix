<template>
    <div class="edit-post-container">
        <el-card>
            <!-- 帖子内容 -->
            <el-form :model="post" ref="postForm" label-width="120px">
                <el-form-item label="标题">
                    <el-input v-model="post.title" />
                </el-form-item>

                <el-form-item label="内容">
                    <el-input type="textarea" v-model="post.content" rows="5" />
                </el-form-item>

                <el-form-item>
                    <el-button type="primary" @click="handleUpdatePost">更新帖子</el-button>
                    <el-button @click="goBack">返回</el-button>
                </el-form-item>
            </el-form>

            <!-- 评论管理 -->
            <el-table :data="comments" border style="margin-top: 20px;">
                <el-table-column prop="id" label="评论ID" width="80" />
                <el-table-column prop="userId" label="用户ID" />
                <el-table-column prop="content" label="评论内容" />
                <el-table-column prop="createdtime" label="创建时间">
                    <template slot-scope="scope">
                        {{ formatDate(scope.row.createdtime) }}
                    </template>
                </el-table-column>

                <el-table-column label="操作" width="200">
                    <template slot-scope="scope">
                        <el-button size="mini" type="danger" @click="handleDeleteComment(scope.row.id)">删除评论</el-button>
                    </template>
                </el-table-column>
            </el-table>
        </el-card>
    </div>
</template>

<script>
import ForumPostApi from '@/api/ForumPost_api';
import ForumCommentApi from '@/api/ForumComment_api';

export default {
    data() {
        return {
            post: {
                id: '',
                title: '',
                content: '',
                createdtime: '',
                updatedtime: ''  // 用于存储最后更新时间
            },
            comments: []
        };
    },
    created() {
        this.fetchPostAndComments();
    },
    methods: {
        async fetchPostAndComments() {
            const postId = this.$route.params.id; // 获取路由传递的帖子ID
            try {
                // 获取帖子信息
                const postResponse = await ForumPostApi.getPostById(postId);
                this.post = postResponse.data;

                // 获取该帖子的评论列表，确保传递 postId
                const commentResponse = await ForumCommentApi.getComments(`?postId=${postId}`);
                this.comments = commentResponse.data;
            } catch (error) {
                console.error('获取帖子和评论失败', error);
            }
        },



        async handleUpdatePost() {
            try {
                const postId = this.post.id;
                this.post.updatedtime = new Date().toISOString();  // 设置为当前时间

                console.log('准备更新的帖子内容:', this.post); // 打印帖子内容，查看是否已更新

                // 更新帖子信息
                await ForumPostApi.updatePost(postId, this.post);
                this.$message.success('帖子更新成功');

                // 跳转到帖子管理页面
                this.$router.push('/Forum/ForumPost');
            } catch (error) {
                console.error('更新帖子失败', error);
                this.$message.error('更新帖子失败');
            }
        },


        async handleDeleteComment(commentId) {
            try {
                await ForumCommentApi.deleteComment(commentId);
                this.comments = this.comments.filter(comment => comment.id !== commentId); // 从列表中移除该评论
                this.$message.success('评论删除成功');
            } catch (error) {
                console.error('删除评论失败', error);
                this.$message.error('删除评论失败');
            }
        },
        goBack() {
            this.$router.push('/Forum/ForumPost');
        },
        formatDate(date) {
            return date ? new Date(date).toLocaleString() : '无';
        }
    }
};
</script>

<style scoped>
.edit-post-container {
    padding: 20px;
}

.el-card {
    margin-bottom: 20px;
}

.el-table {
    margin-top: 20px;
}
</style>