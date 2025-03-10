<template>
    <div class="app-container">
      <!-- 搜索表单 -->
      <el-form :inline="true" class="demo-form-inline">
        <el-form-item label="设施名称">
          <el-input v-model="filters.name" placeholder="输入设施名称"></el-input>
        </el-form-item>
        <el-form-item label="设施类型">
          <el-input v-model="filters.facilityType" placeholder="输入设施类型"></el-input>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="fetchFacilities">查询</el-button>
          <el-button @click="resetSearch">重置</el-button>
        </el-form-item>
      </el-form>
  
      <!-- 添加设施按钮 -->
      <el-button type="primary" @click="handleAdd">添加设施</el-button>
  
      <!-- 批量删除按钮 -->
      <el-button type="danger" @click="confirmBatchDelete" :disabled="selectedFacilities.length === 0">批量删除</el-button>
  
      <el-table :data="facilities" border @selection-change="handleSelectionChange">
        <el-table-column type="selection" width="55" />
        <el-table-column prop="id" label="ID" width="80" />
        <el-table-column prop="name" label="设施名称" />
        <el-table-column prop="facilityType" label="设施类型" />
        <el-table-column prop="createdtime" label="创建时间">
          <template slot-scope="scope">
            {{ formatDate(scope.row.createdtime) }}
          </template>
        </el-table-column>
        <el-table-column prop="updatedtime" label="更新时间">
          <template slot-scope="scope">
            {{ formatBeijingTime(scope.row.updatedtime) }}
          </template>
        </el-table-column>
  
        <el-table-column label="操作" width="200">
          <template slot-scope="scope">
            <el-button size="mini" @click="handleView(scope.row)">查看</el-button>
            <el-button size="mini" type="danger" @click="confirmDelete(scope.row.id)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
    </div>
  </template>
  
  <script>
  import FacilityApi from '@/api/Facility_api';
  
  export default {
    data() {
      return {
        facilities: [],
        selectedFacilities: [], // 存储选中的设施记录ID
        filters: {
          name: '',
          facilityType: ''
        }
      };
    },
    created() {
      this.fetchFacilities();
    },
    methods: {
        async fetchFacilities() {
  const example = {
    oredCriteria: []
  };


  if (this.filters.name) {
    example.oredCriteria.push({
      criteria: [
        {
          condition: "name LIKE",
          value: `%${this.filters.name}%`,
          singleValue: true
        }
      ]
    });
  }

  if (this.filters.facilityType) {
    example.oredCriteria.push({
      criteria: [
        {
          condition: "facility_type LIKE",
          value: `%${this.filters.facilityType}%`,
          singleValue: true
        }
      ]
    });
  }

  try {
    console.log('查询条件:', example);  // 打印请求的查询条件
const response = await FacilityApi.getFacilities(example);
console.log('返回数据:', response.data);  // 打印后端返回的数据

    // 提取设施数据并忽略查询条件部分
    if (response.data && Array.isArray(response.data)) {
      this.facilities = response.data;
    } else {
      this.$message.error('服务器返回的不是有效的设施数据');
    }
  } catch (error) {
    console.error('获取设施列表失败', error);
    this.$message.error('获取设施列表失败: ' + (error.response ? error.response.data : error.message));
  }
},
  
      handleAdd() {
        this.$router.push('/add-facility');
      },
  
      handleView(facility) {
        this.$router.push({ path: '/edit-facility', query: { id: facility.id } });
      },
  
      // 删除单个设施
      async handleDelete(id) {
        try {
          await FacilityApi.deleteFacility(id);
          this.fetchFacilities();
          this.$message.success('设施删除成功');
        } catch (error) {
          console.error('删除设施失败', error);
        }
      },
  
      // 删除单个设施前的确认操作
      confirmDelete(id) {
        this.$confirm('确定要删除此设施吗？', '警告', {
          type: 'warning'
        }).then(() => {
          this.handleDelete(id);
        }).catch(() => {});
      },
  
      // 批量删除前的确认操作
      async confirmBatchDelete() {
        if (this.selectedFacilities.length === 0) return;
  
        this.$confirm('确定要删除选中的设施吗？', '警告', {
          type: 'warning'
        }).then(async () => {
          try {
            // 执行批量删除
            await Promise.all(this.selectedFacilities.map(id => FacilityApi.deleteFacility(id)));
            this.fetchFacilities();
            this.selectedFacilities = [];
            this.$message.success('批量删除成功');
          } catch (error) {
            console.error('批量删除失败', error);
          }
        }).catch(() => {});
      },
  
      // 监听表格多选
      handleSelectionChange(selection) {
        this.selectedFacilities = selection.map(item => item.id);
      },
  
      formatBeijingTime(utcTime) {
        if (!utcTime) return '无';
        const date = new Date(utcTime);
        return new Intl.DateTimeFormat('zh-CN', { 
          timeZone: 'Asia/Shanghai', 
          year: 'numeric', month: '2-digit', day: '2-digit',
          hour: '2-digit', minute: '2-digit', second: '2-digit' 
        }).format(date);
      },
  
      formatDate(date) {
        return date ? new Date(date).toLocaleString() : '无';
      },
  
      resetSearch() {
        this.filters.name = '';
        this.filters.facilityType = '';
        this.fetchFacilities();
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