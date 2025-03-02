<template>
    <div class="add-emergency-request-container">
        <el-form ref="emergencyRequestForm" :model="emergencyRequest" :rules="rules" label-width="120px">
            <el-form-item label="用户ID" prop="userId">
                <el-input v-model="emergencyRequest.userId" />
            </el-form-item>

            <el-form-item label="联系人ID" prop="contactId">
                <el-input v-model="emergencyRequest.contactId" />
            </el-form-item>

            <el-form-item label="状态" prop="status">
                <el-select v-model="emergencyRequest.status">
                    <el-option label="待处理" value="pending" />
                    <el-option label="处理中" value="processing" />
                    <el-option label="已完成" value="completed" />
                </el-select>
            </el-form-item>

            <el-form-item label="纬度" prop="latitude">
                <el-input v-model="emergencyRequest.location.latitude" />
            </el-form-item>

            <el-form-item label="经度" prop="longitude">
                <el-input v-model="emergencyRequest.location.longitude" />
            </el-form-item>

            <el-form-item label="创建时间" prop="createdtime">
                <el-date-picker v-model="emergencyRequest.createdtime" type="datetime" placeholder="选择日期时间" />
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
import EmergencyRequestApi from '@/api/EmergencyRequest_api';

export default {
    data() {
        return {
            emergencyRequest: {
                userId: '',
                contactId: '',
                status: 'pending',
                location: {
                    latitude: '',
                    longitude: ''
                },
                createdtime: new Date() // 自动填充为当前时间
            },
            rules: {
                userId: [{ required: true, message: '请输入用户ID', trigger: 'blur' }],
                contactId: [{ required: true, message: '请输入联系人ID', trigger: 'blur' }],
                status: [{ required: true, message: '请选择状态', trigger: 'change' }],
                createdtime: [{ required: true, message: '请选择创建时间', trigger: 'change' }]
            }
        };
    },
    methods: {
        submitForm() {
            this.$refs.emergencyRequestForm.validate(async (valid) => {
                if (valid) {
                    try {
                        const formData = { ...this.emergencyRequest };
                        // 处理 location 为 JSON 字符串，确保 latitude 和 longitude 使用正确的值
                        formData.location = JSON.stringify({
                            latitude: formData.location.latitude, // 使用 location 对象中的纬度
                            longitude: formData.location.longitude // 使用 location 对象中的经度
                        });

                        // 确保 createdtime 是一个正确的 ISO 格式日期
                        if (formData.createdtime) {
                            formData.createdtime = new Date(formData.createdtime).toISOString();
                        }

                        // 提交数据
                        await EmergencyRequestApi.addEmergencyRequest(formData);
                        this.$message.success("紧急求助添加成功");
                        this.$router.push("/EmergencyRequest/index");
                    } catch (error) {
                        this.$message.error("添加紧急求助失败");
                        console.error(error);
                    }
                }
            });
        },

        resetForm() {
            this.$refs.emergencyRequestForm.resetFields();
        },
        goBack() {
            this.$router.push('/EmergencyRequest/index');
        }
    }
};
</script>

<style scoped>
.add-emergency-request-container {
    max-width: 600px;
    margin: 0 auto;
    padding: 20px;
    background: #fff;
    border-radius: 8px;
    box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
}
</style>
