<template>
    <div class="edit-friend-relationship-container">
        <el-form ref="friendRelationshipForm" :model="friendRelationship" :rules="rules" label-width="120px">
            <el-form-item label="用户ID" prop="userId">
                <el-input v-model="friendRelationship.userId" />
            </el-form-item>

            <el-form-item label="好友用户ID" prop="friendUserId">
                <el-input v-model="friendRelationship.friendUserId" />
            </el-form-item>

            <el-form-item label="状态" prop="status">
                <el-select v-model="friendRelationship.status" placeholder="friendRelationship.status">
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
                status: []
            },
            rules: {
                userId: [{ required: true, message: '请输入用户ID', trigger: 'blur' }],
                friendUserId: [{ required: true, message: '请输入好友用户ID', trigger: 'blur' }],
                status: [{ required: true, message: '请选择状态', trigger: 'change' }]
            }
        };
    },
    created() {
        this.fetchFriendRelationship();
    },
    methods: {
        async fetchFriendRelationship() {
            const id = this.$route.query.id;
            try {
                const response = await FriendRelationshipApi.getFriendRelationshipById(id);
                this.friendRelationship = response.data;
                if (typeof this.friendRelationship.status === 'string') {
                    this.friendRelationship.status = [this.friendRelationship.status];
                }
            } catch (error) {
                console.error('获取好友关系详情失败', error);
            }
        },
        submitForm() {
            this.$refs.friendRelationshipForm.validate(async (valid) => {
                if (valid) {
                    try {
                        await FriendRelationshipApi.updateFriendRelationshipSelective(this.friendRelationship.id, this.friendRelationship);
                        this.$message.success('好友关系更新成功');
                        this.$router.push('/FriendRelationship/index');
                    } catch (error) {
                        this.$message.error('更新好友关系失败');
                        console.error(error);
                    }
                }
            });
        },
        resetForm() {
            this.fetchFriendRelationship();
        },
        goBack() {
            this.$router.push('/FriendRelationship/index');
        }
    }
};
</script>

<style scoped>
.edit-friend-relationship-container {
    max-width: 600px;
    margin: 40px auto;
    padding: 20px;
    background-color: #fff;
    border-radius: 8px;
    box-shadow: 0 2px 12px rgba(0, 0, 0, 0.1);
}
</style>
