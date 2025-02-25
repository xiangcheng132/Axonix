<template>
    <div class="add-user-container">
        <el-form ref="userForm" :model="user" :rules="rules" label-width="100px">
            <el-form-item label="用户名" prop="username">
                <el-input v-model="user.username" />
            </el-form-item>

            <el-form-item label="密码" prop="password">
                <el-input v-model="user.password" type="password" />
            </el-form-item>

            <el-form-item label="邮箱" prop="email">
                <el-input v-model="user.email" />
            </el-form-item>

            <el-form-item label="电话" prop="phone">
                <el-input v-model="user.phone" />
            </el-form-item>

            <el-form-item label="角色" prop="role">
                <el-select v-model="user.role">
                    <el-option label="管理员" value="admin" />
                    <el-option label="用户" value="user" />
                </el-select>
            </el-form-item>

            <el-form-item label="残疾类型" prop="disability_type">
                <el-select v-model="user.disability_type">
                    <el-option label="无" value="" />
                    <el-option label="视觉" value="visual" />
                    <el-option label="听觉" value="hearing" />
                </el-select>
            </el-form-item>

            <el-form-item label="语言偏好" prop="preferences">
                <el-select v-model="user.preferences.language">
                    <el-option label="无" value="" />
                    <el-option label="英语" value="English" />
                    <el-option label="西班牙语" value="Spanish" />
                </el-select>
            </el-form-item>

            <el-form-item>
                <el-button type="primary" @click="submitForm">提交</el-button>
                <el-button @click="resetForm">重置</el-button>
            </el-form-item>
        </el-form>
    </div>
</template>

<script>
import UserAPI from '@/api/user_api'

export default {
    data() {
        return {
            user: {
                username: '',
                password: '',
                email: '',
                phone: '',
                role: 'user',
                disability_type: '',
                preferences: { language: '' }
            },
            rules: {
                username: [{ required: true, message: '请输入用户名', trigger: 'blur' }],
                password: [{ required: true, message: '请输入密码', trigger: 'blur' }],
                email: [{ required: true, type: 'email', message: '请输入有效邮箱', trigger: 'blur' }]
            }
        }
    },
    methods: {
        submitForm() {
            this.$refs.userForm.validate(async (valid) => {
                if (valid) {
                    try {
                        await UserAPI.addUser({
                            ...this.user,
                            preferences: JSON.stringify(this.user.preferences)
                        })
                        this.$message.success('用户添加成功')
                        this.$router.push('/users')
                    } catch (error) {
                        this.$message.error('添加用户失败')
                        console.error(error)
                    }
                }
            })
        },
        resetForm() {
            this.$refs.userForm.resetFields()
        }
    }
}
</script>

<style scoped>
.add-user-container {
    max-width: 600px;
    margin: 0 auto;
    padding: 20px;
    background: #fff;
    border-radius: 8px;
    box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
}
</style>