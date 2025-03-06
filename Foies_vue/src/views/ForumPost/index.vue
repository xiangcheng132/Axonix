<template>
    <div class="app-container">
        <el-button type="primary" @click="handleAdd">添加帖子</el-button>
        <el-button type="danger" @click="handleBatchDelete" :disabled="selectedPosts.length === 0">批量删除</el-button>

        <el-table :data="forumPosts" border @selection-change="handleSelectionChange">
            <el-table-column type="selection" width="55" />
            <el-table-column prop="id" label="ID" width="80" />
            <el-table-column prop="userId" label="用户ID" />
            <el-table-column prop="title" label="标题" />
            <el-table-column prop="commentCount" label="评论数" />
            <el-table-column prop="createdtime" label="创建时间">
                <template slot-scope="scope">
                    {{ formatDate(scope.row.createdtime) }}
                </template>
            </el-table-column>
            <el-table-column prop="updatedtime" label="最后一次修改时间">
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
import ForumPostApi from '@/api/ForumPost_api';
import ForumCommentApi from '@/api/ForumComment_api';

export default {
    data() {
        return {
            forumPosts: [],
            selectedPosts: [] // 选中的帖子ID数组
        };
    },
    created() {
        this.fetchForumPosts();
    },
    methods: {
        async fetchForumPosts() {
            try {
                const response = await ForumPostApi.getPosts();
                this.forumPosts = await Promise.all(
                    response.data.map(async (post) => {
                        const commentCount = await ForumCommentApi.getCommentCount({ postId: post.id });
                        return { ...post, commentCount: commentCount.data };
                    })
                );
            } catch (error) {
                console.error('获取帖子失败', error);
            }
        },

        handleAdd() {
            this.$router.push('/add-forum-post');
        },
        handleView(post) {
            this.$router.push({ name: 'ForumPostDetail', params: { id: post.id } });
        },

        async handleDelete(id) {
            try {
                await ForumPostApi.deletePost(id);
                this.fetchForumPosts();
                this.$message.success('帖子删除成功');
            } catch (error) {
                console.error('删除帖子失败', error);
            }
        },

        // 监听表格多选
        handleSelectionChange(selection) {
            this.selectedPosts = selection.map(item => item.id);
        },

        // 批量删除
        async handleBatchDelete() {
            if (this.selectedPosts.length === 0) return;

            try {
                await Promise.all(this.selectedPosts.map(id => ForumPostApi.deletePost(id)));
                this.fetchForumPosts();
                this.selectedPosts = [];
                this.$message.success('批量删除成功');
            } catch (error) {
                console.error('批量删除失败', error);
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
