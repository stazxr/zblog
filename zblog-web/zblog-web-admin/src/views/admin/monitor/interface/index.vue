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
        <span class="crud-opts-left">
          <el-button v-perm="['INTEE001']" type="primary" :loading="exportLoading" @click="exportData">导出</el-button>
        </span>
      </div>
    </div>
    <div class="components-container">
      <el-table
        ref="interfaceTable"
        v-loading="tableLoading"
        :data="tableData"
        :header-cell-style="{background:'#FAFAFA'}"
        highlight-current-row
        row-key="interfaceCode"
        border
        @current-change="handleCurrentChange"
      >
        <el-table-column type="expand" :fixed="!isMobile">
          <template v-slot="props">
            <el-form label-position="left" inline class="my-table-expand">
              <el-form-item class="el-form-item" label="请求地址:" style="width: 100%">
                <span>{{ props.row['interfaceUri'] }}</span>
              </el-form-item>
              <el-form-item class="el-form-item" label="请求方式:" style="width: 100%">
                <span>{{ props.row['interfaceMethod'] }}</span>
              </el-form-item>
            </el-form>
          </template>
        </el-table-column>
        <el-table-column :show-overflow-tooltip="true" prop="interfaceName" :fixed="!isMobile" label="接口名称" align="left" width="200" />
        <el-table-column :show-overflow-tooltip="true" prop="interfaceCode" label="接口编码" align="center" width="120" />
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
        <el-table-column align="center">
          <template slot="header">
            <span>调用统计</span><br>
            <span>Call Statistics Metrics</span>
          </template>
          <el-table-column :show-overflow-tooltip="true" prop="dailyCallCount" label="DCC" align="center" width="90" />
          <el-table-column :show-overflow-tooltip="true" prop="totalCallCount" label="TCC" align="center" width="90" />
          <el-table-column :show-overflow-tooltip="true" prop="totalFailureCount" label="FCC" align="center" width="90">
            <template v-slot="scope">
              <span :class="{ 'text-danger': scope.row['totalFailureCount'] > 0 }">{{ scope.row['totalFailureCount'] }}</span>
            </template>
          </el-table-column>
          <el-table-column :show-overflow-tooltip="true" prop="callSuccessRate" label="接口成功率" align="center" width="90">
            <template v-slot="scope">
              <span v-if="scope.row['callSuccessRate'] == null" />
              <span v-else :class="{ 'text-danger': scope.row['callSuccessRate'] < 99.9999 }">{{ scope.row['callSuccessRate'] }}%</span>
            </template>
          </el-table-column>
        </el-table-column>
        <el-table-column align="center">
          <template slot="header">
            <span>性能响应</span><br>
            <span>Performance / Latency Metrics</span>
          </template>
          <el-table-column :show-overflow-tooltip="true" prop="avgResponseTime" label="ART/ms" align="center" width="90">
            <template v-slot="scope">
              <span :class="{ 'text-danger': scope.row['avgResponseTime'] > 100 }">{{ scope.row['avgResponseTime'] }}</span>
            </template>
          </el-table-column>
          <el-table-column :show-overflow-tooltip="true" prop="maxResponseTime" label="MRT/ms" align="center" width="90" />
          <el-table-column :show-overflow-tooltip="true" prop="p95ResponseTime" label="P95/ms" align="center" width="90" />
          <el-table-column :show-overflow-tooltip="true" prop="p99ResponseTime" label="P99/ms" align="center" width="90" />
        </el-table-column>
        <el-table-column align="center">
          <template slot="header">
            <span>吞吐量</span><br>
            <span>Throughput / Load Metrics</span>
          </template>
          <el-table-column :show-overflow-tooltip="true" prop="maxQps" label="MaxQps" align="center" width="90" />
          <el-table-column :show-overflow-tooltip="true" prop="avgQps" label="AvgQps" align="center" width="90" />
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
import { downloadFile } from '@/utils'
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
      pageSize: 10,
      exportLoading: false
    }
  },
  computed: {
    isMobile() {
      return this.$store.state.app.device === 'mobile'
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
      this.$mapi.communal.queryConfListByDictKey({ dictKey: 'REQUEST_METHOD_CONFIG' }).then(res => {
        const { data } = res
        this.interfaceMethodList = data
      }).catch(_ => {
        this.interfaceMethodList = []
      })
    },
    loadInterfaceLevelList() {
      this.$mapi.communal.queryConfListByDictKey({ dictKey: 'INTERFACE_LEVEL_CONFIG' }).then(res => {
        const { data } = res
        this.interfaceLevelList = data
      }).catch(_ => {
        this.interfaceLevelList = []
      })
    },
    loadInterfaceStatusList() {
      this.$mapi.communal.queryConfListByDictKey({ dictKey: 'INTERFACE_STATUS_CONFIG' }).then(res => {
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
    },
    // 导出
    exportData() {
      this.$confirm(`确认导出接口列表吗?`, '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'primary'
      }).then(() => {
        this.exportLoading = true
        this.$mapi.interfaces.exportInterface({ ... this.filters }).then(res => {
          downloadFile(res, '接口列表.xlsx')
        }).finally(_ => {
          this.exportLoading = false
        })
      })
    }
  }
}
</script>

<style scoped>

</style>
