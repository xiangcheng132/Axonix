<template>
    <div class="edit-emergency-request-container">
        <el-form ref="emergencyRequestForm" :model="emergencyRequest" :rules="rules" label-width="150px">
            <!-- 用户ID -->
            <el-form-item label="用户ID" prop="userId">
                <el-input v-model="emergencyRequest.userId" />
            </el-form-item>

            <!-- 联系人ID -->
            <el-form-item label="联系人ID" prop="contactId">
                <el-input v-model="emergencyRequest.contactId" />
            </el-form-item>

            <!-- 状态 -->
            <el-form-item label="状态" prop="status">
                <el-select v-model="emergencyRequest.status">
                    <el-option label="待处理" value="pending" />
                    <el-option label="处理中" value="processing" />
                    <el-option label="已完成" value="completed" />
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
import EmergencyRequestApi from '@/api/EmergencyRequest_api';

export default {
    data() {
        return {
            emergencyRequest: {
                userId: '',
                latitude: '',
                longitude: '',
                contactId: '',
                status: 'pending',
                updatedtime: ''  // 添加 updatedtime 字段
            },
            rules: {
                userId: [{ required: true, message: '请输入用户ID', trigger: 'blur' }],
                latitude: [{ required: true, message: '请输入纬度', trigger: 'blur' }],
                longitude: [{ required: true, message: '请输入经度', trigger: 'blur' }],
                contactId: [{ required: true, message: '请输入联系人ID', trigger: 'blur' }],
                status: [{ required: true, message: '请选择状态', trigger: 'change' }]
            }
        };
    },
    created() {
        this.fetchEmergencyRequest();
    },
    methods: {
        async fetchEmergencyRequest() {
            const id = this.$route.query.id;
            try {
                const response = await EmergencyRequestApi.getEmergencyRequestById(id);
                if (response.data) {
                    this.emergencyRequest = { ...response.data };
                    // 在获取数据后，自动填充更新时间
                    this.emergencyRequest.updatedtime = new Date().toISOString();
                }
            } catch (error) {
                console.error('获取紧急求助失败', error);
            }
        },

        submitForm() {
            this.$refs.emergencyRequestForm.validate(async (valid) => {
                if (valid) {
                    try {
                        // 在提交前自动设置最后更新时间
                        this.emergencyRequest.updatedtime = new Date().toISOString();

                        console.log("提交前的数据：", this.emergencyRequest);
                        const formData = { ...this.emergencyRequest };
                        await EmergencyRequestApi.updateEmergencyRequest(this.emergencyRequest.id, formData);
                        this.$message.success('紧急求助更新成功');
                        this.$router.push('/EmergencyRequest/index');
                    } catch (error) {
                        this.$message.error('更新紧急求助失败');
                        console.error(error);
                    }
                }
            });
        },

        resetForm() {
            this.fetchEmergencyRequest();
        },

        goBack() {
            this.$router.push('/EmergencyRequest/index');
        }
    }
};
</script>

<style scoped>
.edit-emergency-request-container {
    max-width: 600px;
    margin: 40px auto;
    padding: 20px;
    background-color: #fff;
    border-radius: 8px;
    box-shadow: 0 2px 12px rgba(0, 0, 0, 0.1);
}
</style>
