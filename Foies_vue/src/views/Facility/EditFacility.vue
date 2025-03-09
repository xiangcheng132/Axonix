<template>
    <div class="edit-facility-container">
        <el-form ref="facilityForm" :model="facility" :rules="rules" label-width="120px">
            <el-form-item label="设施名称" prop="name">
                <el-input v-model="facility.name" />
            </el-form-item>

            <el-form-item label="设施类型" prop="facilityType">
                <el-input v-model="facility.facilityType" />
            </el-form-item>

            <el-form-item label="位置" prop="location">
                <div id="map" class="map-container"></div>
                <p>经度: {{ parsedLocation.longitude }}, 纬度: {{ parsedLocation.latitude }}</p>
            </el-form-item>

            <el-form-item label="描述" prop="description">
                <el-input type="textarea" :rows="4" v-model="facility.description" />
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
import FacilityApi from '@/api/Facility_api';

export default {
    data() {
        return {
            facility: {
                id: null,
                name: '',
                facilityType: '',
                location: '', // 存储 JSON 格式的经纬度
                description: ''
            },
            map: null,
            marker: null,
            rules: {
                name: [{ required: true, message: '请输入设施名称', trigger: 'blur' }],
                facilityType: [{ required: true, message: '请输入设施类型', trigger: 'blur' }],
                location: [{ required: true, message: '请选择位置', trigger: 'blur' }]
            }
        };
    },
    computed: {
        parsedLocation() {
            try {
                return this.facility.location ? JSON.parse(this.facility.location) : { latitude: '', longitude: '' };
            } catch (e) {
                return { latitude: '', longitude: '' };
            }
        }
    },
    created() {
        this.fetchFacility();
    },
    methods: {
        async fetchFacility() {
            const id = this.$route.query.id;
            try {
                const response = await FacilityApi.getFacilityById(id);
                this.facility = response.data;
                this.$nextTick(() => {
                    this.initMap();
                });
            } catch (error) {
                console.error('获取设施详情失败', error);
            }
        },
        initMap() {
            this.map = new AMap.Map('map', {
                center: this.parsedLocation.longitude
                    ? [this.parsedLocation.longitude, this.parsedLocation.latitude]
                    : [116.397428, 39.90923], // 默认北京
                zoom: 13
            });

            // 如果已有位置，则添加 marker
            if (this.parsedLocation.longitude && this.parsedLocation.latitude) {
                this.marker = new AMap.Marker({
                    position: [this.parsedLocation.longitude, this.parsedLocation.latitude],
                    map: this.map
                });
            }

            // 地图点击事件，更新位置
            this.map.on('click', (e) => {
                const newLocation = {
                    latitude: e.lnglat.getLat().toFixed(6),
                    longitude: e.lnglat.getLng().toFixed(6)
                };
                this.facility.location = JSON.stringify(newLocation);

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
            this.$refs.facilityForm.validate(async (valid) => {
                if (valid) {
                    this.facility.updatedtime = new Date().toISOString();
                    try {
                        await FacilityApi.updateFacilitySelective(this.facility.id, this.facility);
                        this.$message.success('设施更新成功');
                        this.$router.push('/Facility/index');
                    } catch (error) {
                        this.$message.error('更新设施失败');
                        console.error(error);
                    }
                }
            });
        },
        resetForm() {
            this.fetchFacility();
        },
        goBack() {
            this.$router.push('/Facility/index');
        }
    }
};
</script>

<style scoped>
.edit-facility-container {
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
