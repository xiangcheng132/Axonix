<template>
    <div class="add-facility-container">
        <el-form ref="facilityForm" :model="facility" :rules="rules" label-width="120px">
            <!-- 由于id, createdtime, updatedtime通常由系统生成，这里不显示 -->
            
            <el-form-item label="设施名称" prop="name">
                <el-input v-model="facility.name" />
            </el-form-item>

            <el-form-item label="设施类型" prop="facilityType">
                <el-input v-model="facility.facilityType" />
            </el-form-item>

            <el-form-item label="位置" prop="location">
                <el-input v-model="facility.location" />
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
                name: '',
                facilityType: '',
                location: '',
                description: ''
            },
            rules: {
                name: [{ required: true, message: '请输入设施名称', trigger: 'blur' }],
                facilityType: [{ required: true, message: '请输入设施类型', trigger: 'blur' }],
                location: [{ required: true, message: '请输入位置', trigger: 'blur' }],
                description: [{ required: false }]
            }
        };
    },
    methods: {
        submitForm() {
            this.$refs.facilityForm.validate(async (valid) => {
                if (valid) {
                    const nowUtc = new Date().toISOString();
                    this.facility.createdtime = nowUtc;
                    this.facility.updatedtime = nowUtc;
                    try {
                        await FacilityApi.addFacilitySelective(this.facility);
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
</style>