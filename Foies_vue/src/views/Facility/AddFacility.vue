<template>
    <div class="add-facility-container">
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

            <!-- 创建时间，默认是当前时间 -->
            <el-form-item label="创建时间" prop="createdtime">
                <el-date-picker v-model="facility.createdtime" type="datetime" placeholder="选择日期时间" :format="beijingTimeFormat" />
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
                name: '',
                facilityType: '',
                location: '', // 存储 JSON 格式的经纬度
                description: '',
                createdtime: new Date(), // 默认创建时间为当前时间
                updatedtime: new Date()
            },
            rules: {
                name: [{ required: true, message: '请输入设施名称', trigger: 'blur' }],
                facilityType: [{ required: true, message: '请输入设施类型', trigger: 'blur' }],
                createdtime: [{ required: true, message: '请选择创建时间', trigger: 'change' }]
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
        },
        beijingTimeFormat() {
            return this.formatBeijingTime(this.facility.createdtime);
        }
    },
    mounted() {
        this.initMap();
    },
    methods: {
        initMap() {
            const map = new AMap.Map('map', {
                center: [116.397428, 39.90923], // 默认中心点（北京）
                zoom: 13
            });

            map.on('click', (e) => {
                const locationData = {
                    latitude: e.lnglat.getLat().toFixed(6),
                    longitude: e.lnglat.getLng().toFixed(6)
                };
                this.facility.location = JSON.stringify(locationData); // 以 JSON 格式存储
            });
        },
        async submitForm() {
            this.$refs.facilityForm.validate(async (valid) => {
                if (valid) {
                    try {
                        const formData = { ...this.facility };

                        // 格式化创建时间为ISO字符串
                        if (formData.createdtime) {
                            formData.createdtime = new Date(formData.createdtime).toISOString();
                        }
                        if (formData.updatedtime) {
                            formData.updatedtime = new Date(formData.updatedtime).toISOString();
                        }

                        await FacilityApi.addFacilitySelective(formData);
                        this.$message.success('设施添加成功');
                        this.$router.push('/Facility/index');
                    } catch (error) {
                        this.$message.error('添加设施失败');
                        console.error(error);
                    }
                }
            });
        },
        resetForm() {
            this.$refs.facilityForm.resetFields();
        },
        goBack() {
            this.$router.push('/Facility/index');
        },
        formatBeijingTime(utcTime) {
            if (!utcTime) return '';
            const date = new Date(utcTime);
            const options = {
                timeZone: 'Asia/Shanghai',
                year: 'numeric',
                month: '2-digit',
                day: '2-digit',
                hour: '2-digit',
                minute: '2-digit',
                second: '2-digit',
            };
            return new Intl.DateTimeFormat('zh-CN', options).format(date);
        }
    }
};
</script>

<style scoped>
.add-facility-container {
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
