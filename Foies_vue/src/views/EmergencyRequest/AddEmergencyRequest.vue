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

            <el-form-item label="位置" prop="location">
                <div id="map" class="map-container"></div>
                <p>经度: {{ parsedLocation.longitude }}, 纬度: {{ parsedLocation.latitude }}</p>
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
                location: '', // 存储 JSON 格式的经纬度
                createdtime: new Date() // 默认当前时间
            },
            map: null,
            marker: null,
            rules: {
                userId: [{ required: true, message: '请输入用户ID', trigger: 'blur' }],
                contactId: [{ required: true, message: '请输入联系人ID', trigger: 'blur' }],
                status: [{ required: true, message: '请选择状态', trigger: 'change' }],
                createdtime: [{ required: true, message: '请选择创建时间', trigger: 'change' }]
            }
        };
    },
    computed: {
        parsedLocation() {
            try {
                return this.emergencyRequest.location ? JSON.parse(this.emergencyRequest.location) : { latitude: '', longitude: '' };
            } catch (e) {
                return { latitude: '', longitude: '' };
            }
        }
    },
    mounted() {
        this.initMap();
    },
    methods: {
        initMap() {
            this.map = new AMap.Map('map', {
                center: [116.397428, 39.90923], // 默认中心点（北京）
                zoom: 13
            });

            this.map.on('click', (e) => {
                const newLocation = {
                    latitude: e.lnglat.getLat().toFixed(6),
                    longitude: e.lnglat.getLng().toFixed(6)
                };
                this.emergencyRequest.location = JSON.stringify(newLocation);

                // 更新 marker
                if (this.marker) {
                    this.marker.setPosition([newLocation.longitude, newLocation.latitude]);
                } else {
                    this.marker = new AMap.Marker({
                        position: [newLocation.longitude, newLocation.latitude],
                        map: this.map
                    });
                }
            });
        },
        submitForm() {
            this.$refs.emergencyRequestForm.validate(async (valid) => {
                if (valid) {
                    try {
                        const formData = { ...this.emergencyRequest };

                        // 确保 location 存储为 JSON 格式
                        formData.location = JSON.stringify(this.parsedLocation);

                        // 确保 createdtime 格式正确
                        formData.createdtime = new Date(formData.createdtime).toISOString();

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
            this.emergencyRequest.location = ''; // 清空选点
            if (this.marker) {
                this.marker.setMap(null);
                this.marker = null;
            }
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
.map-container {
    width: 100%;
    height: 300px;
    margin-bottom: 10px;
}
</style>
