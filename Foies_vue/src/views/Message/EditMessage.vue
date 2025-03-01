<template>
    <div class="edit-message-container">
        <el-form ref="messageForm" :model="message" :rules="rules" label-width="150px">
            <!-- 发送用户ID -->
            <el-form-item label="发送者ID" prop="senderId">
                <el-input v-model="message.senderId" />
            </el-form-item>

            <!-- 接收用户ID -->
            <el-form-item label="接收者ID" prop="receiverId">
                <el-input v-model="message.receiverId" />
            </el-form-item>

            <!-- 消息内容 -->
            <el-form-item label="内容" prop="content">
                <el-input v-model="message.content" type="textarea" />
            </el-form-item>

            <!-- 消息类型 -->
            <el-form-item label="类型" prop="type">
                <el-select v-model="message.type" placeholder="请选择消息类型">
                    <el-option label="文字" value="text" />
                    <el-option label="语音" value="voice" />
                    <el-option label="视频" value="video" />
                </el-select>
            </el-form-item>

            <!-- 按钮组 -->
            <el-form-item>
                <el-button type="primary" @click="submitForm">提交</el-button>
                <el-button @click="resetForm">重置</el-button>
                <el-button @click="goBack">取消</el-button>
            </el-form-item>
        </el-form>
    </div>
</template>

<script>
import MessageApi from '@/api/Message_api';

export default {
    data() {
        return {
            message: {
                userId: '',
                title: '',
                content: '',
                type: '',
                timestamp: ''
            },
            rules: {
                userId: [{ required: true, message: '请输入用户ID', trigger: 'blur' }],
                title: [{ required: true, message: '请输入消息标题', trigger: 'blur' }],
                content: [{ required: true, message: '请输入消息内容', trigger: 'blur' }],
                type: [{ required: true, message: '请选择消息类型', trigger: 'change' }],
                timestamp: [{ required: true, message: '请选择发送时间', trigger: 'change' }]
            }
        };
    },
    created() {
        this.fetchMessage();
    },
    methods: {
        async fetchMessage() {
            const id = this.$route.query.id;
            if (!id) {
                this.$message.error('未提供消息 ID');
                this.$router.push('/Message/index');
                return;
            }
            try {
                const response = await MessageApi.getMessageById(id);
                this.message = response.data;
            } catch (error) {
                this.$message.error('获取消息失败');
                console.error(error);
            }
        },
        submitForm() {
            this.$refs.messageForm.validate(async (valid) => {
                if (valid) {
                    try {
                        await MessageApi.updateMessage(this.message.id, this.message);
                        this.$message.success('消息更新成功');
                        this.$router.push('/Message/index');
                    } catch (error) {
                        this.$message.error('更新消息失败');
                        console.error(error);
                    }
                }
            });
        },
        resetForm() {
            this.fetchMessage(); // 重新加载数据
        },
        goBack() {
            this.$router.push('/Message/index');
        }
    }
};
</script>

<style scoped>
.edit-message-container {
    max-width: 600px;
    margin: 40px auto;
    padding: 20px;
    background-color: #fff;
    border-radius: 8px;
    box-shadow: 0 2px 12px rgba(0, 0, 0, 0.1);
}
</style>
