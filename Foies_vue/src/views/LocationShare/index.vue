<template>
  <div class="app-container">
    <!-- 搜索表单 -->
    <el-form :inline="true" class="demo-form-inline">
      <el-form-item label="用户ID">
        <el-input v-model="filters.userId" placeholder="输入用户ID"></el-input>
      </el-form-item>
      <el-form-item label="共享给用户ID">
        <el-input v-model="filters.sharedWith" placeholder="输入共享给用户ID"></el-input>
      </el-form-item>
      <el-form-item label="开始时间">
        <el-date-picker
          v-model="filters.startTime"
          type="daterange"
          start-placeholder="开始日期"
          end-placeholder="结束日期"
          format="yyyy-MM-dd"
          value-format="yyyy-MM-dd"
        />
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="fetchLocationShares">查询</el-button>
        <el-button @click="resetSearch">重置</el-button>
      </el-form-item>
    </el-form>

    <!-- 操作按钮 -->
    <el-button type="primary" @click="handleAdd">添加位置共享</el-button>
    <el-button type="danger" @click="confirmBatchDelete" :disabled="selectedLocationShares.length === 0">批量删除</el-button>

    <!-- 位置共享列表 -->
    <el-table :data="locationShares" border @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" />
      <el-table-column prop="id" label="ID" width="80" />
      <el-table-column prop="userId" label="用户ID" />
      <el-table-column prop="sharedWith" label="共享给用户ID" />
      <el-table-column prop="startTime" label="开始时间">
        <template slot-scope="scope">
          {{ formatDate(scope.row.startTime) }}
        </template>
      </el-table-column>
      <el-table-column prop="endTime" label="结束时间">
        <template slot-scope="scope">
          {{ formatDate(scope.row.endTime) }}
        </template>
      </el-table-column>
      <el-table-column label="操作" width="200">
        <template slot-scope="scope">
          <el-button size="mini" @click="handleView(scope.row)">查看</el-button>
          <el-button size="mini" type="danger" @click="handleDelete(scope.row.id)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>
  </div>
</template>

<script>
import LocationShareApi from '@/api/LocationShare_api';

export default {
  data() {
    return {
      locationShares: [],
      selectedLocationShares: [], // 存储选中的位置共享记录ID
      filters: {
        userId: '',
        sharedWith: '',
        startTime: '',
        endTime: ''
      }
    };
  },
  created() {
    this.fetchLocationShares();
  },
  methods: {
    async fetchLocationShares() {
      const example = {
        oredCriteria: [{ criteria: [] }]
      };

      // Apply filters if they are set
      if (this.filters.userId) {
        example.oredCriteria[0].criteria.push({
          condition: "user_id LIKE",
          value: `%${this.filters.userId}%`,
          singleValue: true
        });
      }

      if (this.filters.sharedWith) {
        example.oredCriteria[0].criteria.push({
          condition: "shared_with LIKE",
          value: `%${this.filters.sharedWith}%`,
          singleValue: true
        });
      }

      if (this.filters.startTime && this.filters.endTime) {
        example.oredCriteria[0].criteria.push({
          condition: "start_time BETWEEN",
          value: [`${this.filters.startTime} 00:00:00`, `${this.filters.endTime} 23:59:59`],
          singleValue: false
        });
      }

      try {
        const response = await LocationShareApi.getLocationShares(example);
        this.locationShares = response.data;
      } catch (error) {
        console.error('获取位置共享信息失败', error);
      }
    },

    resetSearch() {
      this.filters.userId = '';
      this.filters.sharedWith = '';
      this.filters.startTime = '';
      this.filters.endTime = '';
      this.fetchLocationShares();
    },

    handleAdd() {
      this.$router.push('/add-location-share');
    },

    handleView(locationShare) {
      this.$router.push({ path: '/edit-location-share', query: { id: locationShare.id } });
    },

    async handleDelete(id) {
      try {
        await LocationShareApi.deleteLocationShare(id);
        this.fetchLocationShares();
        this.$message.success('位置共享删除成功');
      } catch (error) {
        console.error('删除位置共享失败', error);
      }
    },

    // 监听表格多选
    handleSelectionChange(selection) {
      this.selectedLocationShares = selection.map(item => item.id);
    },

    // 批量删除前的确认操作
    confirmBatchDelete() {
      if (this.selectedLocationShares.length === 0) return;

      this.$confirm('确定要删除选中的位置共享记录吗？', '警告', {
        type: 'warning'
      }).then(() => {
        this.handleBatchDelete();
      }).catch(() => {});
    },

    // 处理批量删除
    async handleBatchDelete() {
      try {
        await Promise.all(this.selectedLocationShares.map(id => LocationShareApi.deleteLocationShare(id)));
        this.fetchLocationShares();
        this.selectedLocationShares = [];
        this.$message.success('批量删除成功');
      } catch (error) {
        console.error('批量删除失败', error);
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
  margin-top: 20px;
}

.el-button {
  margin-right: 10px;
}
</style>
