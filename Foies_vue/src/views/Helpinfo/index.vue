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
      <el-col :span="16"></el-col> <!-- 空列，保持间距 -->
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
        <template slot-scope="scope">
          <el-select v-model="scope.row.status" @change="handleStatusChange(scope.row)">
            <el-option label="待处理" :value="0" />
            <el-option label="处理中" :value="1" />
            <el-option label="已完成" :value="2" />
          </el-select>
        </template>
      </el-table-column>
      <el-table-column prop="createdAt" label="创建时间" width="180">
        <template slot-scope="scope">{{ formatDate(scope.row.createdAt) }}</template>
      </el-table-column>
      <el-table-column prop="updatedAt" label="更新时间" width="180">
        <template slot-scope="scope">{{ formatDate(scope.row.updatedAt) }}</template>
      </el-table-column>
      <el-table-column label="操作" width="160" fixed="right">
        <template slot-scope="scope">
          <el-button size="mini" type="danger" @click="handleDelete(scope.row.id)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>
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

    async handleStatusChange(row) {
      try {
        await HelpRequestAPI.updateHelpRequest(row);
        this.$message.success('状态更新成功');
      } catch (error) {
        this.$message.error('状态更新失败');
        console.error('状态更新失败', error);
      }
    },

    async handleDelete(id) {
      this.$confirm('确定要删除该记录吗？', '警告', { type: 'warning' })
        .then(async () => {
          try {
            await HelpRequestAPI.deleteHelpRequest(id);
            this.fetchHelpRequests(); // 刷新列表
            this.$message.success('删除成功');
          } catch (error) {
            this.$message.error('删除失败');
            console.error('删除失败', error);
          }
        })
        .catch(() => {});
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
