<template>
    <div class="add-location-share-container">
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
                <div id="map" class="map-container"></div>
                <p>经度: {{ parsedLocation.longitude }}, 纬度: {{ parsedLocation.latitude }}</p>
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
                lastLocation: '' // 存储位置的 JSON 字符串
            },
            map: null,
            marker: null,
            rules: {
                userId: [{ required: true, message: '请输入用户ID', trigger: 'blur' }],
                sharedWith: [{ required: true, message: '请输入共享给的用户ID', trigger: 'blur' }],
                startTime: [{ required: true, message: '请选择开始时间', trigger: 'change' }],
                endTime: [{ required: true, message: '请选择结束时间', trigger: 'change' }],
                lastLocation: [{ required: true, message: '请选择最后位置', trigger: 'blur' }]
            }
        };
    },
    computed: {
        parsedLocation() {
            try {
                // 解析最后位置的 JSON 字符串并返回经纬度对象
                return this.locationShare.lastLocation
                    ? JSON.parse(this.locationShare.lastLocation)
                    : { latitude: '', longitude: '' };
            } catch (e) {
                return { latitude: '', longitude: '' };
            }
        }
    },
    mounted() {
        this.initMap();  // 加载地图
    },
    methods: {
        initMap() {
            this.map = new AMap.Map('map', {
                center: this.parsedLocation.longitude
                    ? [this.parsedLocation.longitude, this.parsedLocation.latitude]
                    : [116.397428, 39.90923], // 默认北京位置
                zoom: 13
            });

            // 如果已有位置，则添加 marker
            if (this.parsedLocation.longitude && this.parsedLocation.latitude) {
                this.marker = new AMap.Marker({
                    position: [this.parsedLocation.longitude, this.parsedLocation.latitude],
                    map: this.map
                });
            }

            // 地图点击事件，更新最后位置
            this.map.on('click', (e) => {
                const newLocation = {
                    latitude: e.lnglat.getLat().toFixed(6),
                    longitude: e.lnglat.getLng().toFixed(6)
                };

                // 使用 $set 确保 Vue 响应式更新
                this.$set(this.locationShare, 'lastLocation', JSON.stringify(newLocation));

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
        async submitForm() {
            this.$refs.locationShareForm.validate(async (valid) => {
                if (valid) {
                    try {
                        const formData = { ...this.locationShare };
                        if (formData.startTime) {
                            formData.startTime = new Date(formData.startTime).toISOString();
                        }
                        if (formData.endTime) {
                            formData.endTime = new Date(formData.endTime).toISOString();
                        }
                        await LocationShareApi.addLocationShare(formData);
                        this.$message.success('位置共享添加成功');
                        this.$router.push('/LocationShare/index');
                    } catch (error) {
                        this.$message.error('添加位置共享失败');
                        console.error(error);
                    }
                }
            });
        },
        resetForm() {
            this.$refs.locationShareForm.resetFields();
        },
        goBack() {
            this.$router.push('/LocationShare/index');
        }
    }
};
</script>

<style scoped>
.add-location-share-container {
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
