<template>
  <div class="app-container">
    <div class="head-container">
      <div class="search-opts">
        <muses-search-form ref="searchForm" :model="filters" label-position="right" label-width="0" :offset="0" :item-width="140">
          <muses-search-form-item label="" prop="search-scene">
            <el-select id="search-scene" v-model="filters.scene" placeholder="审核场景" clearable @change="search">
              <el-option v-for="item in auditSceneList" :key="item.value" :label="item.name" :value="item.value" />
            </el-select>
          </muses-search-form-item>
          <muses-search-form-item label="" prop="search-decision">
            <el-select id="search-decision" v-model="filters.decision" placeholder="审核决策结果" clearable @change="search">
              <el-option v-for="item in auditDecisionList" :key="item.value" :label="item.name" :value="item.value" />
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
          <el-button v-perm="['AUDTQ002']" :disabled="row === null" type="info" @click="showDetail">详情</el-button>
        </span>
      </div>
    </div>
    <div class="components-container">
      <el-table
        ref="auditRecordTable"
        v-loading="tableLoading"
        :data="tableData"
        :header-cell-style="{background:'#FAFAFA'}"
        highlight-current-row
        row-key="id"
        border
        @current-change="handleCurrentChange"
      >
        <el-table-column :show-overflow-tooltip="true" prop="id" label="审核id" align="center" width="80px" />
        <el-table-column :show-overflow-tooltip="true" prop="uid" label="用户id" align="center" width="100px" />
        <el-table-column :show-overflow-tooltip="true" prop="oid" label="对象id" align="center" width="100px" />
        <el-table-column label="审核场景" align="center" width="100px">
          <template v-slot="scope">
            <el-tag v-if="scope.row.scene === 'DEFAULT'" type="primary">默认</el-tag>
            <el-tag v-else-if="scope.row.scene === 'BARRAGE'" type="primary">弹幕</el-tag>
            <span v-else>{{ scope.row.scene }}</span>
          </template>
        </el-table-column>
        <el-table-column :show-overflow-tooltip="true" prop="originalContent" label="原始内容" align="center" />
        <el-table-column :show-overflow-tooltip="true" prop="finalContent" label="最终内容" align="center" />
        <el-table-column label="决策结果" align="center" width="100px">
          <template v-slot="scope">
            <el-tag v-if="scope.row.decision === 'PASS'" type="success">通过</el-tag>
            <el-tag v-else-if="scope.row.decision === 'MODIFY'" type="primary">更新</el-tag>
            <el-tag v-else-if="scope.row.decision === 'REJECT'" type="danger">拒绝</el-tag>
            <el-tag v-else-if="scope.row.decision === 'MANUAL'" type="warning">复核</el-tag>
            <span v-else>{{ scope.row.decision }}</span>
          </template>
        </el-table-column>
        <el-table-column :show-overflow-tooltip="true" prop="hitWords" label="敏感词" align="center" />
        <el-table-column :show-overflow-tooltip="true" prop="reason" label="备注" align="center" />
        <el-table-column label="耗时" align="center" width="80px">
          <template v-slot="scope">
            <span>{{ scope.row['costMs'] }} ms</span>
          </template>
        </el-table-column>
        <el-table-column prop="createTime" label="审核时间" align="center" width="160px" />
        <div slot="empty">
          <muses-empty description="暂无审核记录" />
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
import detailDialog from '@/views/admin/contentAudit/autoAudit/template/detailDialog'
export default {
  name: 'Visitor',
  components: {
    detailDialog
  },
  data() {
    return {
      filters: {
        scene: null,
        decision: null
      },
      auditSceneList: [],
      auditDecisionList: [],
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
    this.loadAutoAuditSceneList()
    this.loadAutoAuditDecisionList()
    this.listTableData()
  },
  methods: {
    handleCurrentChange(row) {
      this.row = row
    },
    loadAutoAuditSceneList() {
      this.$mapi.communal.queryConfListByDictKey({ dictKey: 'AUTO_AUDIT_SCENE_CONFIG' }).then(res => {
        const { data } = res
        this.auditSceneList = data
      }).catch(_ => {
        this.auditSceneList = []
      })
    },
    loadAutoAuditDecisionList() {
      this.$mapi.communal.queryConfListByDictKey({ dictKey: 'AUTO_AUDIT_DECISION_CONFIG' }).then(res => {
        const { data } = res
        this.auditDecisionList = data
      }).catch(_ => {
        this.auditDecisionList = []
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
      this.$mapi.autoAudit.pageAutoAuditRecordList(param).then(res => {
        const { data } = res
        this.total = data.total
        this.tableData = data.records
      }).catch(_ => {
        this.total = 0
        this.tableData = []
      }).finally(() => {
        this.tableLoading = false
        this.row = null
        this.$refs.auditRecordTable.setCurrentRow()
      })
    },
    // 详情
    showDetail() {
      if (this.row === null) {
        this.$message.error('请选择要查看的记录')
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

</style>
