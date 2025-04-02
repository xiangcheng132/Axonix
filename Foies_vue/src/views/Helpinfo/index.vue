<template>
    <div class="app-container">
      <!-- 搜索表单 -->
      <el-form :inline="true" :model="searchForm" class="search-form">
        <el-form-item label="求助者ID">
          <el-input v-model="searchForm.requesterId" placeholder="请输入求助者ID" clearable />
        </el-form-item>
        <el-form-item label="帮助者ID">
          <el-input v-model="searchForm.helperId" placeholder="请输入帮助者ID" clearable />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="fetchHelpRequests">查询</el-button>
          <el-button @click="resetSearch">重置</el-button>
        </el-form-item>
      </el-form>
  
      <!-- 操作按钮和记录数显示 -->
      <el-row class="action-row" type="flex" justify="space-between" align="middle">
        <el-col :span="8">
          <el-button type="primary" @click="handleAdd">添加求助记录</el-button>
          <el-button type="danger" @click="confirmBatchDelete" :disabled="selectedHelpRequests.length === 0">批量删除</el-button>
        </el-col>
        <el-col :span="8" class="record-count" style="text-align: right;">
          <span>当前共有 {{ totalRecords }} 条记录</span>
        </el-col>
      </el-row>
  
      <!-- 求助记录表格 -->
      <el-table :data="helpRequests" border @selection-change="handleSelectionChange">
        <el-table-column type="selection" width="55" />
        <el-table-column prop="id" label="ID" width="80" />
        <el-table-column prop="requesterId" label="求助者ID" width="120" />
        <el-table-column prop="helperId" label="帮助者ID" width="120" />
        <el-table-column prop="requesterLng" label="求助者经度" width="120" />
        <el-table-column prop="requesterLat" label="求助者纬度" width="120" />
        <el-table-column prop="helperLng" label="帮助者经度" width="120" />
        <el-table-column prop="helperLat" label="帮助者纬度" width="120" />
        <el-table-column prop="status" label="状态" width="100">
          <template slot-scope="scope">{{ formatStatus(scope.row.status) }}</template>
        </el-table-column>
        <el-table-column prop="createdAt" label="创建时间" width="180">
          <template slot-scope="scope">{{ formatDate(scope.row.createdAt) }}</template>
        </el-table-column>
        <el-table-column prop="updatedAt" label="更新时间" width="180">
          <template slot-scope="scope">{{ formatDate(scope.row.updatedAt) }}</template>
        </el-table-column>
        <el-table-column label="操作" width="160" fixed="right">
          <template slot-scope="scope">
            <el-button size="mini" @click="handleEdit(scope.row)">编辑</el-button>
            <el-button size="mini" type="danger" @click="handleDelete(scope.row.id)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
  
      <!-- 添加/编辑求助记录弹出框 -->
      <el-dialog :visible.sync="dialogVisible" title="求助记录" width="600px">
        <el-form ref="helpRequestForm" :model="helpRequest" :rules="rules" label-width="100px">
          <el-form-item label="求助者ID" prop="requesterId">
            <el-input v-model="helpRequest.requesterId" />
          </el-form-item>
  
          <el-form-item label="帮助者ID" prop="helperId">
            <el-input v-model="helpRequest.helperId" />
          </el-form-item>
  
          <el-form-item label="求助者经度" prop="requesterLng">
            <el-input v-model="helpRequest.requesterLng" />
          </el-form-item>
  
          <el-form-item label="求助者纬度" prop="requesterLat">
            <el-input v-model="helpRequest.requesterLat" />
          </el-form-item>
  
          <el-form-item label="帮助者经度" prop="helperLng">
            <el-input v-model="helpRequest.helperLng" />
          </el-form-item>
  
          <el-form-item label="帮助者纬度" prop="helperLat">
            <el-input v-model="helpRequest.helperLat" />
          </el-form-item>
  
          <el-form-item label="状态" prop="status">
            <el-select v-model="helpRequest.status">
              <el-option label="待处理" :value="0" />
              <el-option label="处理中" :value="1" />
              <el-option label="已完成" :value="2" />
            </el-select>
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
  import HelpRequestAPI from '@/api/helpinfo_api';
  
  export default {
    data() {
      return {
        helpRequests: [],
        selectedHelpRequests: [],
        totalRecords: 0,
        searchForm: {
          requesterId: '',
          helperId: ''
        },
        dialogVisible: false,
        helpRequest: {
          id: null,
          requesterId: '',
          helperId: '',
          requesterLng: 0,
          requesterLat: 0,
          helperLng: 0,
          helperLat: 0,
          status: 0,
          createdAt: '',
          updatedAt: ''
        },
        rules: {
          requesterId: [{ required: true, message: '请输入求助者ID', trigger: 'blur' }],
          helperId: [{ required: true, message: '请输入帮助者ID', trigger: 'blur' }],
          status: [{ required: true, message: '请选择状态', trigger: 'change' }]
        }
      };
    },
  
    created() {
      this.fetchHelpRequests();
    },
  
    methods: {
      async fetchHelpRequests() {
        const example = {
          oredCriteria: [{ criteria: [] }]
        };
  
        if (this.searchForm.requesterId) {
          example.oredCriteria[0].criteria.push({
            condition: "requester_id =",
            value: this.searchForm.requesterId,
            singleValue: true
          });
        }
  
        if (this.searchForm.helperId) {
          example.oredCriteria[0].criteria.push({
            condition: "helper_id =",
            value: this.searchForm.helperId,
            singleValue: true
          });
        }
  
        try {
          const response = await HelpRequestAPI.getHelpRequests(example);
          this.helpRequests = response.data;
  
          // 获取记录总数并更新
          const countResponse = await HelpRequestAPI.countHelpRequests(example);
          this.totalRecords = countResponse.data;
        } catch (error) {
          console.error('获取求助记录列表失败', error);
        }
      },
  
      resetSearch() {
        this.searchForm = { requesterId: '', helperId: '' };
        this.fetchHelpRequests();
      },
  
      handleSelectionChange(selection) {
        this.selectedHelpRequests = selection.map(item => item.id);
      },
  
      handleAdd() {
        this.helpRequest = { 
          id: null, 
          requesterId: '',
          helperId: '',
          requesterLng: 0,
          requesterLat: 0,
          helperLng: 0,
          helperLat: 0,
          status: 0,
          createdAt: new Date().toISOString(),
          updatedAt: ''
        };
        this.dialogVisible = true;
      },
  
      handleEdit(helpRequest) {
        this.helpRequest = { ...helpRequest };
        this.dialogVisible = true;
      },
  
      async submitForm() {
        this.$refs.helpRequestForm.validate(async (valid) => {
          if (valid) {
            try {
              if (this.helpRequest.id) {
                await HelpRequestAPI.updateHelpRequest(this.helpRequest);
              } else {
                await HelpRequestAPI.addHelpRequest(this.helpRequest);
              }
              this.$message.success('求助记录保存成功');
              this.dialogVisible = false;
              this.fetchHelpRequests();
            } catch (error) {
              this.$message.error('保存求助记录失败');
              console.error(error);
            }
          }
        });
      },
  
      closeDialog() {
        this.dialogVisible = false;
      },
  
      confirmBatchDelete() {
        if (this.selectedHelpRequests.length === 0) return;
        this.$confirm('确定要删除选中的求助记录吗？', '警告', { type: 'warning' })
          .then(() => this.handleBatchDelete())
          .catch(() => { });
      },
  
      async handleBatchDelete() {
        try {
            await Promise.all(this.selectedHelpRequests.map(id => HelpRequestAPI.deleteHelpRequest(id)));
            this.fetchHelpRequests(); 
            this.selectedHelpRequests = []; 
            this.$message.success('批量删除成功'); 
        } catch (error) {
            console.error('批量删除失败', error); 
        }
    },
      async handleDelete(id) {
        this.$confirm('确定要删除该用户吗？', '警告', { type: 'warning' })
        .then(async () =>{
            try {
                await HelpRequestAPI.deleteHelpRequest(id); 
                this.fetchHelpRequests(); // 刷新列表
                this.$message.success('删除成功');
            } catch (error) {
                this.$message.error('删除失败');
                console.error('删除失败', error);
            }
        })
        .catch(() => {
        // 如果用户取消操作，什么都不做
      });
     },
  
      formatStatus(status) {
        switch (status) {
          case 0: return '待处理';
          case 1: return '处理中';
          case 2: return '已完成';
          default: return '未知';
        }
      },
  
      formatDate(date) {
        return date ? new Date(date).toLocaleString() : '无';
      }
      
    }
  };
  </script>
  
  <style scoped>
  .app-container {
    padding: 20px;
  }
  
  .el-table {
    margin-top: 10px;
  }
  
  .el-button {
    margin-right: 5px;
  }
  
  .search-form {
    margin-bottom: 0px;
  }
  
  .record-count {
    margin-top: 20px;
    font-size: 14px;
    color: #606266;
  }
  
  .action-row {
    margin-bottom: 20px;
  }
  </style>
  