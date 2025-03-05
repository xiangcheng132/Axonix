<template>
    <div class="add-friend-relationship-container">
        <el-form ref="friendRelationshipForm" :model="friendRelationship" :rules="rules" label-width="120px">
            <el-form-item label="用户ID" prop="userId">
                <el-input v-model="friendRelationship.userId" />
            </el-form-item>

            <el-form-item label="好友用户ID" prop="friendUserId">
                <el-input v-model="friendRelationship.friendUserId" />
            </el-form-item>

            <el-form-item label="状态" prop="status">
                <el-select v-model="friendRelationship.status" placeholder="选择状态">
                    <el-option label="已接受" value="accepted" />
                    <el-option label="待处理" value="pending" />
                    <el-option label="已拒绝" value="blocked" />
                </el-select>
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
import FriendRelationshipApi from '@/api/FriendRelationship_api';

export default {
    data() {
        return {
            friendRelationship: {
                userId: '',
                friendUserId: '',
                status: '',
                createdtime: '',
                updatedtime: ''
            },
            rules: {
                userId: [{ required: true, message: '请输入用户ID', trigger: 'blur' }],
                friendUserId: [{ required: true, message: '请输入好友用户ID', trigger: 'blur' }],
                status: [{ required: true, message: '请选择状态', trigger: 'change' }]
            }
        };
    },
    methods: {
        submitForm() {
            this.$refs.friendRelationshipForm.validate(async (valid) => {
                if (valid) {
                    try {
                        const now = new Date().toISOString(); // 获取当前时间
                        this.friendRelationship.createdtime = now;
                        this.friendRelationship.updatedtime = now;

                        await FriendRelationshipApi.addFriendRelationshipSelective(this.friendRelationship);
                        this.$message.success('好友关系添加成功');
                        this.$router.push('/FriendRelationship/index');
                    } catch (error) {
                        this.$message.error('添加好友关系失败');
                        console.error(error);
                    }
                }
            });
        },
        resetForm() {
            this.$refs.friendRelationshipForm.resetFields();
        },
        goBack() {
            this.$router.push('/FriendRelationship/index');
        }
    }
};
</script>


<style scoped>
.add-friend-relationship-container {
    max-width: 600px;
    margin: 0 auto;
    padding: 20px;
    background: #fff;
    border-radius: 8px;
    box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
}
</style>