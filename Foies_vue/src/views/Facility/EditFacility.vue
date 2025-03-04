<template>
    <div class="edit-facility-container">
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
                id: null,
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
    created() {
        this.fetchFacility();
    },
    methods: {
        async fetchFacility() {
            const id = this.$route.query.id;
            try {
                const response = await FacilityApi.getFacilityById(id);
                this.facility = response.data;
            } catch (error) {
                console.error('获取设施详情失败', error);
            }
        },
        submitForm() {
            this.$refs.facilityForm.validate(async (valid) => {
                if (valid) {
                    this.facility.updatedtime = new Date().toISOString();  // 确保存储 UTC
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
</style>