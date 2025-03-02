<template>
    <div class="edit-location-share-container">
        <el-form ref="locationShareForm" :model="locationShare" :rules="rules" label-width="120px">
            <el-form-item label="用户ID" prop="userId">
                <el-input v-model="locationShare.userId" />
            </el-form-item>

            <el-form-item label="共享给用户ID" prop="sharedWith">
                <el-input v-model="locationShare.sharedWith" />
            </el-form-item>

            <el-form-item label="开始时间" prop="startTime">
                <el-date-picker v-model="locationShare.startTime" type="datetime" placeholder="选择开始时间" />
            </el-form-item>

            <el-form-item label="结束时间" prop="endTime">
                <el-date-picker v-model="locationShare.endTime" type="datetime" placeholder="选择结束时间" />
            </el-form-item>

            <el-form-item label="最后位置" prop="lastLocation">
                <el-input v-model="locationShare.lastLocation" />
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
import LocationShareApi from '@/api/LocationShare_api';

export default {
    data() {
        return {
            locationShare: {
                userId: '',
                sharedWith: '',
                startTime: '',
                endTime: '',
                lastLocation: ''
            },
            rules: {
                userId: [{ required: true, message: '请输入用户ID', trigger: 'blur' }],
                sharedWith: [{ required: true, message: '请输入共享给的用户ID', trigger: 'blur' }],
                startTime: [{ required: true, message: '请选择开始时间', trigger: 'change' }],
                endTime: [{ required: true, message: '请选择结束时间', trigger: 'change' }],
                lastLocation: [{ required: true, message: '请输入最后位置', trigger: 'blur' }]
            }
        };
    },
    created() {
        this.fetchLocationShare();
    },
    methods: {
        async fetchLocationShare() {
            const id = this.$route.query.id;
            try {
                const response = await LocationShareApi.getLocationShareById(id);
                this.locationShare = response.data;
            } catch (error) {
                console.error('获取位置共享详情失败', error);
            }
        },
        submitForm() {
            this.$refs.locationShareForm.validate(async (valid) => {
                if (valid) {
                    try {
                        await LocationShareApi.updateLocationShareSelective(this.locationShare.id, this.locationShare);
                        this.$message.success('位置共享更新成功');
                        this.$router.push('/LocationShare/index');
                    } catch (error) {
                        this.$message.error('更新位置共享失败');
                        console.error(error);
                    }
                }
            });
        },
        resetForm() {
            this.fetchLocationShare();
        },
        goBack() {
            this.$router.push('/LocationShare/index');
        }
    }
};
</script>

<style scoped>
.edit-location-share-container {
    max-width: 600px;
    margin: 40px auto;
    padding: 20px;
    background-color: #fff;
    border-radius: 8px;
    box-shadow: 0 2px 12px rgba(0, 0, 0, 0.1);
}
</style>
