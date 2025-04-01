<template>
    <div class="app-container">
        <!-- VIP 查询表单 -->
        <el-form :inline="true" :model="searchForm" class="search-form">
            <el-form-item label="用户ID">
                <el-input v-model="searchForm.userId" placeholder="请输入用户ID" clearable />
            </el-form-item>
            <el-form-item>
                <el-button type="primary" @click="fetchVips">查询</el-button>
                <el-button @click="resetSearch">重置</el-button>
            </el-form-item>
        </el-form>

        <!-- 操作按钮 -->
        <el-button type="primary" @click="handleAdd">添加VIP</el-button>
        <el-button type="danger" @click="confirmBatchDelete" :disabled="selectedVips.length === 0">批量删除</el-button>

        <!-- VIP表格 -->
        <el-table :data="vips" border @selection-change="handleSelectionChange">
            <el-table-column type="selection" width="55" />
            <el-table-column prop="id" label="ID" width="100" />
            <el-table-column prop="userId" label="用户ID" width="100" />
            <el-table-column prop="levels" label="VIP等级" width="100" />
            <el-table-column prop="startTime" label="开始时间" width="350" />
            <el-table-column prop="endTime" label="结束时间" width="350" />
            <el-table-column prop="totalPayment" label="总支付金额" width="100" />

            <el-table-column label="操作" width="160" fixed="right">
                <template slot-scope="scope">
                    <el-button size="mini" @click="handleEdit(scope.row)">编辑</el-button>
                    <el-button size="mini" type="danger" @click="handleDelete(scope.row.id)">删除</el-button>
                </template>
            </el-table-column>
        </el-table>

        <!-- 添加/编辑VIP弹出框 -->
        <el-dialog :visible.sync="dialogVisible" title="VIP信息" width="600px">
            <el-form ref="vipForm" :model="vip" :rules="rules" label-width="100px">
                <el-form-item label="用户ID" prop="userId">
                    <el-input v-model="vip.userId" />
                </el-form-item>

                <el-form-item label="VIP等级" prop="levels">
                    <el-input-number v-model="vip.levels" :min="0" :max="10" />
                </el-form-item>

                <el-form-item label="开始时间" prop="startTime">
                    <el-date-picker v-model="vip.startTime" type="datetime" />
                </el-form-item>

                <el-form-item label="结束时间" prop="endTime">
                    <el-date-picker v-model="vip.endTime" type="datetime" />
                </el-form-item>

                <el-form-item label="总支付金额" prop="totalPayment">
                    <el-input-number v-model="vip.totalPayment" :min="0" :max="1000000" />
                </el-form-item>

                <el-form-item>
                    <el-button type="primary" @click="submitForm">保存</el-button>
                    <el-button @click="closeDialog">取消</el-button>
                </el-form-item>
            </el-form>
        </el-dialog>
    </div>
</template>

<script>
import VipAPI from '@/api/uservip_api.js';

export default {
    data() {
        return {
            vips: [],
            selectedVips: [],
            searchForm: {
                userId: ''
            },
            dialogVisible: false,
            vip: {
                id: null,
                userId: 0,
                levels: 0,
                startTime: '',
                endTime: '',
                totalPayment: 0,
            },
            rules: {
                userId: [{ required: true, message: '请输入用户ID', trigger: 'blur' }],
                levels: [{ required: true, message: '请输入VIP等级', trigger: 'blur' }],
                startTime: [{ required: true, message: '请选择开始时间', trigger: 'change' }],
                endTime: [{ required: true, message: '请选择结束时间', trigger: 'change' }],
                totalPayment: [{ required: true, message: '请输入总支付金额', trigger: 'blur' }]
            }
        };
    },
    created() {
        this.fetchVips();
    },
    methods: {
        async fetchVips() {
            const example = {
                oredCriteria: [{ criteria: [] }]
            };

            if (this.searchForm.userId) {
                example.oredCriteria[0].criteria.push({
                    condition: 'user_id =',
                    value: this.searchForm.userId,
                    singleValue: true
                });
            }

            try {
                const response = await VipAPI.getVips(example);
                this.vips = response.data;
            } catch (error) {
                console.error('获取VIP列表失败', error);
            }
        },


        resetSearch() {
            this.searchForm = { userId: '' };
            this.fetchVips();
        },

        handleSelectionChange(selection) {
            this.selectedVips = selection.map(vip => vip.id);
        },

        handleAdd() {
            this.vip = { // 重置VIP数据以便添加新VIP
                id: null,
                userId: 0,
                levels: 0,
                startTime: '',
                endTime: '',
                totalPayment: 0
            };
            this.dialogVisible = true;
        },

        handleEdit(vip) {
            this.vip = { ...vip };  // 填充要编辑的VIP数据
            this.dialogVisible = true;
        },

        async submitForm() {
            this.$refs.vipForm.validate(async (valid) => {
                if (valid) {
                    try {
                        if (this.vip.id) {
                            await VipAPI.updateVip(this.vip);  // 更新现有VIP
                        } else {
                            await VipAPI.addVip(this.vip);  // 添加新VIP
                        }
                        this.$message.success('VIP信息保存成功');
                        this.dialogVisible = false;
                        this.fetchVips();
                    } catch (error) {
                        this.$message.error('保存VIP信息失败');
                        console.error(error);
                    }
                }
            });
        },

        closeDialog() {
            this.dialogVisible = false;
        },

        confirmBatchDelete() {
            if (this.selectedVips.length === 0) return;
            this.$confirm('确定要删除选中的VIP吗？', '警告', { type: 'warning' })
                .then(() => this.handleBatchDelete())
                .catch(() => { });
        },

        async handleBatchDelete() {
            try {
                await Promise.all(this.selectedVips.map(id => VipAPI.deleteVip(id)));
                this.fetchVips();
                this.selectedVips = [];
                this.$message.success('批量删除成功');
            } catch (error) {
                console.error('批量删除失败', error);
            }
        },

        async handleDelete(id) {
            try {
                await VipAPI.deleteVip(id);
                this.fetchVips();
                this.$message.success('删除成功');
            } catch (error) {
                console.error('删除VIP失败', error);
            }
        }
    }
};
</script>

<style scoped>
.app-container {
    padding: 20px;
}

.el-table {
    margin-top: 20px;
}

.el-button {
    margin-right: 5px;
}

.search-form {
    margin-bottom: 0px;
}
</style>