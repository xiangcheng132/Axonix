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
                lastLocation: ''  // Store as JSON (latitude, longitude)
            },
            map: null,
            marker: null,
            rules: {
                userId: [{ required: true, message: '请输入用户ID', trigger: 'blur' }],
                sharedWith: [{ required: true, message: '请输入共享给的用户ID', trigger: 'blur' }],
                startTime: [{ required: true, message: '请选择开始时间', trigger: 'change' }],
                endTime: [{ required: true, message: '请选择结束时间', trigger: 'change' }],
                lastLocation: [{ required: true, message: '请输入最后位置', trigger: 'blur' }]
            }
        };
    },
    computed: {
        parsedLocation() {
            try {
                return this.locationShare.lastLocation
                    ? JSON.parse(this.locationShare.lastLocation)
                    : { latitude: '', longitude: '' };
            } catch (e) {
                return { latitude: '', longitude: '' };
            }
        }
    },
    created() {
        this.fetchLocationShare();
    },
    mounted() {
        this.initMap();
    },
    methods: {
        // Fetch the existing location share details
        async fetchLocationShare() {
            const id = this.$route.query.id;
            try {
                const response = await LocationShareApi.getLocationShareById(id);
                this.locationShare = response.data;
                this.$nextTick(() => {
                    this.initMap();  // Initialize map with the location data
                });
            } catch (error) {
                console.error('获取位置共享详情失败', error);
            }
        },
        
        // Initialize the map
        initMap() {
            this.map = new AMap.Map('map', {
                center: this.parsedLocation.longitude
                    ? [this.parsedLocation.longitude, this.parsedLocation.latitude]
                    : [116.397428, 39.90923], // Default to Beijing
                zoom: 13
            });

            // If the location exists, place a marker
            if (this.parsedLocation.longitude && this.parsedLocation.latitude) {
                this.marker = new AMap.Marker({
                    position: [this.parsedLocation.longitude, this.parsedLocation.latitude],
                    map: this.map
                });
            }

            // Map click event to update the location
            this.map.on('click', (e) => {
                const newLocation = {
                    latitude: e.lnglat.getLat().toFixed(6),
                    longitude: e.lnglat.getLng().toFixed(6)
                };
                this.locationShare.lastLocation = JSON.stringify(newLocation);

                // Update the marker position or add a new one
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

        // Submit the form
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

        // Reset the form to the original data
        resetForm() {
            this.fetchLocationShare();
        },

        // Navigate back
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

.map-container {
    width: 100%;
    height: 300px;
    margin-bottom: 10px;
}
</style>
