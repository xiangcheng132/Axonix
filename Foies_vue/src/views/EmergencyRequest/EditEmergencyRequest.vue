<template>
    <div class="edit-emergency-request-container">
        <el-form ref="emergencyRequestForm" :model="emergencyRequest" :rules="rules" label-width="150px">
            <el-form-item label="用户ID" prop="userId">
                <el-input v-model="emergencyRequest.userId"/>
            </el-form-item>

            <el-form-item label="联系人ID" prop="contactId">
                <el-input v-model="emergencyRequest.contactId"/>
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
                contactId: '',
                status: 'pending',
                location: '', // 存储 JSON 字符串
                updatedtime: ''
            },
            map: null,
            marker: null,
            rules: {
                userId: [{ required: true, message: '请输入用户ID', trigger: 'blur' }],
                contactId: [{ required: true, message: '请输入联系人ID', trigger: 'blur' }],
                status: [{ required: true, message: '请选择状态', trigger: 'change' }]
            }
        };
    },
    computed: {
        parsedLocation() {
            try {
                // 解析 location 字符串，返回经纬度对象
                return this.emergencyRequest.location
                    ? JSON.parse(this.emergencyRequest.location)
                    : { latitude: "", longitude: "" };
            } catch (e) {
                return { latitude: "", longitude: "" };
            }
        }
    },
    created() {
        this.fetchEmergencyRequest();
    },
    mounted() {
        this.$nextTick(() => {
            this.initMap();  // 确保在数据加载完成后初始化地图
        });
    },
    methods: {
        async fetchEmergencyRequest() {
            const id = this.$route.query.id;
            try {
                const response = await EmergencyRequestApi.getEmergencyRequestById(id);
                if (response.data) {
                    this.emergencyRequest = { ...response.data };
                    this.emergencyRequest.updatedtime = new Date().toISOString();

                    // 确保 location 始终是字符串格式的 JSON
                    if (typeof this.emergencyRequest.location !== 'string') {
                        this.emergencyRequest.location = JSON.stringify(this.emergencyRequest.location || { latitude: "", longitude: "" });
                    }
                }
                this.$nextTick(() => {
                    this.initMap();  // 加载完数据后再初始化地图
                });
            } catch (error) {
                console.error('获取紧急求助失败', error);
            }
        },
        initMap() {
            this.map = new AMap.Map('map', {
                center: this.parsedLocation.longitude
                    ? [this.parsedLocation.longitude, this.parsedLocation.latitude]
                    : [116.397428, 39.90923], // 默认中心点（北京）
                zoom: 13
            });

            // 如果已有位置，则添加 marker
            if (this.parsedLocation.longitude && this.parsedLocation.latitude) {
                this.marker = new AMap.Marker({
                    position: [this.parsedLocation.longitude, this.parsedLocation.latitude],
                    map: this.map
                });
            }

            // 禁用地图点击事件，禁止用户修改位置
            this.map.off('click');
        },
        submitForm() {
            this.$refs.emergencyRequestForm.validate(async (valid) => {
                if (valid) {
                    this.emergencyRequest.updatedtime = new Date().toISOString();

                    // 确保提交时 location 是字符串格式的 JSON
                    if (typeof this.emergencyRequest.location !== 'string') {
                        this.emergencyRequest.location = JSON.stringify(this.emergencyRequest.location);
                    }

                    try {
                        await EmergencyRequestApi.updateEmergencyRequest(this.emergencyRequest.id, this.emergencyRequest);
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
.map-container {
    width: 100%;
    height: 300px;
    margin-bottom: 10px;
}
</style>
