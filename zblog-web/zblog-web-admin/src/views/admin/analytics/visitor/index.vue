<template>
  <div class="app-container">
    <div class="head-container">
      <div class="search-opts">
        <muses-search-form ref="searchForm" :model="filters" label-position="right" label-width="0" :offset="0" :item-width="140">
          <muses-search-form-item label="" prop="search-username">
            <el-input id="search-username" v-model="filters.username" clearable placeholder="用户名" @keyup.enter.native="search" />
          </muses-search-form-item>
          <muses-search-form-item label="" prop="search-ip">
            <el-input id="search-ip" v-model="filters.ip" clearable placeholder="IP" @keyup.enter.native="search" />
          </muses-search-form-item>
          <muses-search-form-item btn btn-open-name="" btn-close-name="">
            <el-button type="success" @click="search()">查 询</el-button>
            <el-button type="warning" @click="resetSearch()">重 置</el-button>
          </muses-search-form-item>
        </muses-search-form>
      </div>
      <div class="crud-opts">
        <span class="crud-opts-left">
          <el-button v-perm="['VISIQ002']" :disabled="row === null" type="info" @click="showDetail">详情</el-button>
        </span>
      </div>
    </div>
    <div class="components-container">
      <el-table
        ref="visitorTable"
        v-loading="tableLoading"
        :data="tableData"
        :header-cell-style="{background:'#FAFAFA'}"
        highlight-current-row
        row-key="id"
        border
        @current-change="handleCurrentChange"
      >
        <el-table-column label="访客" align="center" width="160px">
          <template v-slot="scope">
            <div class="user-info">
              <el-avatar :size="32" :src="scope.row['visitorAvatar']">
                {{ (scope.row['visitorNickname'] || '?').substring(0, 1) }}
              </el-avatar>
              <span class="nickname"> {{ scope.row['visitorNickname'] }} </span>
            </div>
          </template>
        </el-table-column>
        <el-table-column :show-overflow-tooltip="true" prop="username" label="用户名" align="center" width="120px" />
        <el-table-column :show-overflow-tooltip="true" prop="ip" label="IP地址" align="center" width="110px" />
        <el-table-column :show-overflow-tooltip="true" prop="ipRegion" label="IP归属地" align="center" />
        <el-table-column :show-overflow-tooltip="true" prop="isp" label="运营商" align="center" />
        <el-table-column :show-overflow-tooltip="true" prop="browser" label="浏览器" align="center" />
        <el-table-column :show-overflow-tooltip="true" prop="deviceType" label="设备类型" align="center" />
        <el-table-column prop="firstVisitTime" label="首次访问时间" align="center" width="160px" />
        <el-table-column prop="lastVisitDate" label="最近访问日期" align="center" width="120px" />
        <div slot="empty">
          <muses-empty description="暂无访客数据" />
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

    <!-- 详情 -->
    <detailDialog
      ref="detailDialogRef"
      :dialog-visible="detailDialogVisible"
      @showDetailDone="showDetailDone"
    />
  </div>
</template>

<script>
import detailDialog from '@/views/admin/analytics/visitor/template/detailDialog'
export default {
  name: 'Visitor',
  components: {
    detailDialog
  },
  data() {
    return {
      filters: {
        username: null,
        ip: null
      },
      tableData: [],
      tableLoading: false,
      row: null,
      total: 0,
      page: 1,
      pageSize: 10,
      detailDialogVisible: false
    }
  },
  mounted() {
    this.listTableData()
  },
  methods: {
    handleCurrentChange(row) {
      this.row = row
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
      this.$mapi.visitor.pageVisitorList(param).then(res => {
        const { data } = res
        this.total = data.total
        this.tableData = data.records
      }).catch(_ => {
        this.total = 0
        this.tableData = []
      }).finally(() => {
        this.tableLoading = false
        this.row = null
        this.$refs.visitorTable.setCurrentRow()
      })
    },
    // 详情
    showDetail() {
      if (this.row === null) {
        this.$message.error('请选择要查看的访客')
        return
      }
      this.detailDialogVisible = true
      this.$refs.detailDialogRef.initData(this.row.visitorId)
    },
    showDetailDone() {
      this.detailDialogVisible = false
    }
  }
}
</script>

<style scoped>

</style>
