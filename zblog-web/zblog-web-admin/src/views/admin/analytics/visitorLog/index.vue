<template>
  <div class="app-container">
    <div class="head-container">
      <div class="search-opts">
        <muses-search-form ref="searchForm" :model="filters" label-position="right" label-width="0" :offset="0" :item-width="140">
          <muses-search-form-item label="" prop="search-visitorId">
            <el-input id="search-visitorId" v-model="filters.visitorId" clearable placeholder="访客id" @keyup.enter.native="search" />
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
          <el-button v-perm="['VISLQ002']" :disabled="row === null" type="info" @click="showDetail">详情</el-button>
        </span>
      </div>
    </div>
    <div class="components-container">
      <el-table
        ref="visitorLogTable"
        v-loading="tableLoading"
        :data="tableData"
        :header-cell-style="{background:'#FAFAFA'}"
        highlight-current-row
        row-key="id"
        border
        @current-change="handleCurrentChange"
      >
        <el-table-column label="访客" width="160px" align="center">
          <template v-slot="scope">
            <div class="user-info">
              <el-avatar :size="32" :src="scope.row.visitorAvatar" icon="el-icon-user-solid">
                {{ (scope.row.visitorNickname || '?').substring(0,1) }}
              </el-avatar>
              <span class="nickname"> {{ scope.row.visitorNickname }} </span>
            </div>
          </template>
        </el-table-column>
        <el-table-column prop="title" label="访问页面" show-overflow-tooltip align="center" />
        <el-table-column prop="path" label="访问路径" show-overflow-tooltip align="center" />
        <el-table-column prop="type" label="类型" align="center">
          <template v-slot="scope">
            <el-tag size="small">
              {{ scope.row.type || '-' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="referer" label="来源" show-overflow-tooltip align="center" />
        <el-table-column prop="ipRegion" label="地区" show-overflow-tooltip align="center" />
        <el-table-column prop="browser" label="浏览器" align="center" />
        <el-table-column prop="deviceType" label="设备" align="center" />
        <el-table-column prop="visitTime" label="访问时间" width="160px" align="center" />
        <div slot="empty">
          <muses-empty description="暂无访客日志数据" />
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
import detailDialog from '@/views/admin/analytics/visitorLog/template/detailDialog'
export default {
  name: 'VisitorLog',
  components: {
    detailDialog
  },
  data() {
    return {
      filters: {
        visitorId: null,
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
      this.$mapi.visitorLog.pageVisitorLogList(param).then(res => {
        const { data } = res
        this.total = data.total
        this.tableData = data.records
      }).catch(_ => {
        this.total = 0
        this.tableData = []
      }).finally(() => {
        this.tableLoading = false
        this.row = null
        this.$refs.visitorLogTable.setCurrentRow()
      })
    },
    // 详情
    showDetail() {
      if (this.row === null) {
        this.$message.error('请选择要查看的访客日志')
        return
      }
      this.detailDialogVisible = true
      this.$refs.detailDialogRef.initData(this.row.id)
    },
    showDetailDone() {
      this.detailDialogVisible = false
    }
  }
}
</script>

<style scoped>
.user-info {
  display: flex;
  align-items: center;
  justify-content: center;
}

.nickname {
  margin-left: 8px;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.barrage-content {
  display: inline-block;
  padding: 4px 10px;
  background: #303133;
  border-radius: 14px;
  line-height: 20px;
  max-width: 100%;
  word-break: break-all;
}
</style>
