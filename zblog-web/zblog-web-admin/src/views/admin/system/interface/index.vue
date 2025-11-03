<template>
  <div class="app-container">
    <div class="head-container">
      <div class="search-opts">
        <muses-search-form ref="searchForm" :model="filters" label-position="right" label-width="0" :offset="0" :item-width="140">
          <muses-search-form-item label="" prop="search-interfaceName">
            <el-input id="search-interfaceName" v-model="filters.interfaceName" clearable placeholder="接口名称" @keyup.enter.native="search" />
          </muses-search-form-item>
          <muses-search-form-item label="" prop="search-interfaceCode">
            <el-input id="search-interfaceCode" v-model="filters.interfaceCode" clearable placeholder="接口编码" @keyup.enter.native="search" />
          </muses-search-form-item>
          <muses-search-form-item label="" prop="search-interfaceUri">
            <el-input id="search-interfaceUri" v-model="filters.interfaceUri" clearable placeholder="请求地址" @keyup.enter.native="search" />
          </muses-search-form-item>
          <muses-search-form-item label="" prop="search-interfaceMethod">
            <el-select id="search-interfaceMethod" v-model="filters.interfaceMethod" placeholder="请求方式" clearable>
              <el-option v-for="item in interfaceMethodList" :key="item.value" :label="item.name" :value="item.value" />
            </el-select>
          </muses-search-form-item>
          <muses-search-form-item label="" prop="search-interfaceLevel">
            <el-select id="search-interfaceLevel" v-model="filters.interfaceLevel" placeholder="接口级别" clearable>
              <el-option v-for="item in interfaceLevelList" :key="item.value" :label="item.name" :value="item.value" />
            </el-select>
          </muses-search-form-item>
          <muses-search-form-item label="" prop="search-interfaceStatus">
            <el-select id="search-interfaceStatus" v-model="filters.interfaceStatus" placeholder="接口状态" clearable>
              <el-option v-for="item in interfaceStatusList" :key="item.value" :label="item.name" :value="item.value" />
            </el-select>
          </muses-search-form-item>
          <muses-search-form-item btn btn-open-name="" btn-close-name="">
            <el-button type="success" @click="search()">查 询</el-button>
            <el-button type="warning" @click="resetSearch()">重 置</el-button>
          </muses-search-form-item>
        </muses-search-form>
      </div>
      <div class="crud-opts">
        <span class="crud-opts-left" />
      </div>
    </div>
    <div class="components-container">
      <el-table
        ref="interfaceTable"
        v-loading="tableLoading"
        :data="tableData"
        :header-cell-style="{background:'#FAFAFA'}"
        highlight-current-row
        row-key="id"
        border
        @current-change="handleCurrentChange"
      >
        <el-table-column :show-overflow-tooltip="true" prop="interfaceName" label="接口名称" align="left" />
        <el-table-column :show-overflow-tooltip="true" prop="interfaceCode" label="接口编码" align="left" />
        <el-table-column :show-overflow-tooltip="true" prop="interfaceUri" label="请求地址" align="left" />
        <el-table-column :show-overflow-tooltip="true" prop="interfaceMethod" label="请求方式" align="center" width="120" />
        <el-table-column :show-overflow-tooltip="false" prop="interfaceLevel" label="接口级别" align="center" width="120">
          <template v-slot="scope">
            <el-tag v-if="scope.row.interfaceLevel === 1" type="warning">公开访问</el-tag>
            <el-tag v-else-if="scope.row.interfaceLevel === 2" type="primary">登录访问</el-tag>
            <el-tag v-else-if="scope.row.interfaceLevel === 4" type="success">授权访问</el-tag>
            <el-tag v-else-if="scope.row.interfaceLevel === 8" type="danger">禁止访问</el-tag>
            <span v-else> - </span>
          </template>
        </el-table-column>
        <el-table-column :show-overflow-tooltip="false" prop="interfaceStatus" label="接口状态" align="center" width="120">
          <template v-slot="scope">
            <el-tag v-if="scope.row.interfaceStatus === 0" type="danger">禁用</el-tag>
            <el-tag v-else-if="scope.row.interfaceStatus === 1" type="success">启用</el-tag>
            <el-tag v-else-if="scope.row.interfaceStatus === 2">默认</el-tag>
            <span v-else> - </span>
          </template>
        </el-table-column>
        <div slot="empty">
          <el-empty :image="nodataImg" description=" " />
        </div>
      </el-table>
      <div class="pagination-container">
        <el-pagination
          :total="total"
          :current-page.sync="page"
          :page-size.sync="pageSize"
          :page-sizes="[10, 20, 30, 50]"
          layout="total, prev, pager, next, sizes"
          @size-change="handleSizeChange"
          @current-change="handlePageChange"
        />
      </div>
    </div>
  </div>
</template>

<script>
import nodataImg from '@/assets/images/nodata.png'
export default {
  name: 'Interface',
  data() {
    return {
      filters: {
        interfaceName: null,
        interfaceCode: null,
        interfaceUri: null,
        interfaceMethod: null,
        interfaceLevel: null,
        interfaceStatus: null
      },
      interfaceMethodList: [],
      interfaceLevelList: [],
      interfaceStatusList: [],
      tableData: [],
      tableLoading: false,
      nodataImg: nodataImg,
      row: null,
      total: 0,
      page: 1,
      pageSize: 10
    }
  },
  mounted() {
    this.loadInterfaceMethodList()
    this.loadInterfaceLevelList()
    this.loadInterfaceStatusList()
    this.listTableData()
  },
  methods: {
    handleCurrentChange(row) {
      this.row = row
    },
    loadInterfaceMethodList() {
      this.$mapi.dict.queryConfListByDictKey({ dictKey: 'REQUEST_METHOD_CONFIG' }).then(res => {
        const { data } = res
        this.interfaceMethodList = data
      }).catch(_ => {
        this.interfaceMethodList = []
      })
    },
    loadInterfaceLevelList() {
      this.$mapi.dict.queryConfListByDictKey({ dictKey: 'INTERFACE_LEVEL_CONFIG' }).then(res => {
        const { data } = res
        this.interfaceLevelList = data
      }).catch(_ => {
        this.interfaceLevelList = []
      })
    },
    loadInterfaceStatusList() {
      this.$mapi.dict.queryConfListByDictKey({ dictKey: 'INTERFACE_STATUS_CONFIG' }).then(res => {
        const { data } = res
        this.interfaceStatusList = data
      }).catch(_ => {
        this.interfaceStatusList = []
      })
    },
    // 查询
    search() {
      this.page = 1
      this.listTableData()
    },
    resetSearch() {
      Object.keys(this.filters).forEach(key => { this.filters[key] = null })
      this.page = 1
      this.listTableData()
    },
    handleSizeChange(size) {
      this.page = 1
      this.pageSize = size
      this.listTableData()
    },
    handlePageChange(page) {
      this.page = page
      this.listTableData()
    },
    listTableData() {
      const param = {
        ... this.filters,
        page: this.page,
        pageSize: this.pageSize
      }
      this.tableLoading = true
      this.$mapi.interfaces.pageInterfaceList(param).then(res => {
        const { data } = res
        this.total = data.total
        this.tableData = data.list
      }).catch(_ => {
        this.total = 0
        this.tableData = []
      }).finally(() => {
        this.tableLoading = false
        this.row = null
        this.$refs.interfaceTable.setCurrentRow()
      })
    }
  }
}
</script>

<style scoped>

</style>
