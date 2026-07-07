<template>
  <div class="app-container">
    <div class="head-container">
      <div class="search-opts">
        <muses-search-form ref="searchForm" :model="filters" label-position="right" label-width="0" :offset="0" :item-width="140">
          <muses-search-form-item label="" prop="search-content">
            <el-input id="search-content" v-model="filters.content" clearable placeholder="弹幕内容" @keyup.enter.native="search" />
          </muses-search-form-item>
          <muses-search-form-item label="" prop="search-nickname">
            <el-input id="search-nickname" v-model="filters.nickname" clearable placeholder="用户昵称" @keyup.enter.native="search" />
          </muses-search-form-item>
          <muses-search-form-item label="" prop="search-ip">
            <el-input id="search-ip" v-model="filters.ip" clearable placeholder="IP" @keyup.enter.native="search" />
          </muses-search-form-item>
          <muses-search-form-item label="" prop="search-auditStatus">
            <el-select id="search-auditStatus" v-model="filters.auditStatus" placeholder="审核状态" clearable @change="search">
              <el-option v-for="item in auditStatusList" :key="item.value" :label="item.name" :value="item.value" />
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
          <el-button v-perm="['BMESQ002']" :disabled="row === null" type="info" @click="showDetail">详情</el-button>
          <el-button v-perm="['BMESU001']" :disabled="row === null" type="primary" @click="auditBarrageMessage">审核</el-button>
          <el-button v-perm="['BMESD001']" :disabled="row === null" type="danger" @click="deleteBarrageMessage">删除</el-button>
        </span>
      </div>
    </div>
    <div class="components-container">
      <el-table
        ref="pageTable"
        v-loading="tableLoading"
        :data="tableData"
        :header-cell-style="{background:'#FAFAFA'}"
        highlight-current-row
        row-key="id"
        border
        @current-change="handleCurrentChange"
      >
        <el-table-column :show-overflow-tooltip="true" prop="pageName" label="页面名称" align="center" />
        <el-table-column :show-overflow-tooltip="true" prop="pageLabel" label="页面标识" align="center" />
        <el-table-column label="展示模式" align="center">
          <template v-slot="scope">
            <el-tag v-if="scope.row.displayMode === 'BANNER'" type="primary">顶部横幅</el-tag>
            <el-tag v-else-if="scope.row.displayMode === 'FULL'" type="primary">全屏背景</el-tag>
            <span v-else>-</span>
          </template>
        </el-table-column>
        <el-table-column :show-overflow-tooltip="true" prop="pageSort" label="页面排序" align="center" width="100px" />
        <el-table-column :show-overflow-tooltip="true" prop="createUsername" label="创建用户" align="center" width="120px" />
        <el-table-column :show-overflow-tooltip="true" prop="createTime" label="创建时间" align="center" width="160px" />
        <el-table-column :show-overflow-tooltip="true" prop="updateUsername" label="更新用户" align="center" width="120px" />
        <el-table-column :show-overflow-tooltip="true" prop="updateTime" label="更新时间" align="center" width="160px" />
        <div slot="empty">
          <muses-empty />
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
    <!-- 审核 -->
    <auditDialog
      ref="auditDialogRef"
      :dialog-visible="auditDialogVisible"
      @showDetailDone="auditDone"
    />
  </div>
</template>

<script>
import detailDialog from '@/views/admin/webfeed/barrageMessage/template/detailDialog'
import auditDialog from '@/views/admin/webfeed/barrageMessage/template/auditDialog'
export default {
  name: 'BarrageMessage',
  components: {
    detailDialog,
    auditDialog
  },
  data() {
    return {
      filters: {
        content: null,
        nickname: null,
        ip: null,
        auditStatus: null
      },
      auditStatusList: [],
      tableData: [],
      tableLoading: false,
      row: null,
      total: 0,
      page: 1,
      pageSize: 10,
      detailDialogVisible: false,
      auditDialogVisible: false
    }
  },
  mounted() {
    this.loadBarrageMessageAuditStatusList()
    this.listTableData()
  },
  methods: {
    handleCurrentChange(row) {
      this.row = row
    },
    loadBarrageMessageAuditStatusList() {
      this.$mapi.communal.queryConfListByDictKey({ dictKey: 'BARRAGE_MESSAGE_AUDIT_STATUS_CONFIG' }).then(res => {
        const { data } = res
        this.auditStatusList = data
      }).catch(_ => {
        this.auditStatusList = []
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
      this.$mapi.barrageMessage.pageBarrageMessageList(param).then(res => {
        const { data } = res
        this.total = data.total
        this.tableData = data.records
      }).catch(_ => {
        this.total = 0
        this.tableData = []
      }).finally(() => {
        this.tableLoading = false
        this.row = null
        this.$refs.pageTable.setCurrentRow()
      })
    },
    // 详情
    showDetail() {
      if (this.row === null) {
        this.$message.error('请选择要查看的弹幕')
        return
      }
      this.detailDialogVisible = true
      this.$refs.detailDialogRef.initData(this.row.id)
    },
    showDetailDone() {
      this.detailDialogVisible = false
    },
    // 审核
    auditBarrageMessage() {
      this.auditDialogVisible = true
      this.$refs.auditDialogRef.initData(this.row.id)
    },
    auditDone(result = false) {
      this.auditDialogVisible = false
      if (result) {
        this.listTableData()
      }
    },
    // 删除
    deleteBarrageMessage() {
      if (this.row === null) {
        this.$message.error('请选择要删除的弹幕')
        return
      }
      this.$confirm('此操作将永久删除弹幕, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.$mapi.barrageMessage.deleteBarrageMessage({ barrageMessageId: this.row.id }).then(res => {
          this.$message.success(res.message)
          this.listTableData()
        })
      })
    }
  }
}
</script>

<style scoped>

</style>
