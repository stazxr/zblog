<template>
  <div class="app-container">
    <div class="head-container">
      <div class="search-opts">
        <muses-search-form ref="searchForm" :model="filters" label-position="right" label-width="0" :offset="0" :item-width="140">
          <muses-search-form-item label="" prop="search-word">
            <el-input id="search-word" v-model="filters.word" clearable placeholder="敏感词" @keyup.enter.native="search" />
          </muses-search-form-item>
          <muses-search-form-item label="" prop="search-status">
            <el-select id="search-status" v-model="filters.status" placeholder="敏感词状态" clearable @change="search">
              <el-option v-for="item in statusList" :key="item.value" :label="item.name" :value="item.value" />
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
          <el-button v-perm="['SEWDA001']" type="success" @click="addSensitiveWord">新增</el-button>
          <el-button v-perm="['SEWDU001']" :disabled="row === null" type="primary" @click="editSensitiveWord">编辑</el-button>
          <el-button v-perm="['SEWDD001']" :disabled="row === null" type="danger" @click="deleteSensitiveWord">删除</el-button>
        </span>
      </div>
    </div>
    <div class="components-container">
      <el-table
        ref="sensitiveWordTable"
        v-loading="tableLoading"
        :data="tableData"
        :header-cell-style="{background:'#FAFAFA'}"
        highlight-current-row
        row-key="id"
        border
        @current-change="handleCurrentChange"
      >
        <el-table-column :show-overflow-tooltip="true" prop="word" label="敏感词" align="center" />
        <el-table-column :show-overflow-tooltip="true" prop="type" label="敏感词类型" align="center" width="180px" />
        <el-table-column label="风险等级" align="center" width="120px">
          <template v-slot="scope">
            <el-tag v-if="scope.row.level === 1">低</el-tag>
            <el-tag v-if="scope.row.level === 2">中</el-tag>
            <el-tag v-if="scope.row.level === 3">高</el-tag>
            <span v-else />
          </template>
        </el-table-column>
        <el-table-column label="敏感词状态" align="center" width="120px">
          <template v-slot="scope">
            <el-tag v-if="scope.row.status === 0">启用</el-tag>
            <el-tag v-if="scope.row.status === 1">禁用</el-tag>
            <span v-else />
          </template>
        </el-table-column>
        <el-table-column :show-overflow-tooltip="true" prop="remark" label="备注" align="center" />
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

    <!-- 新增 / 编辑 -->
    <addOrEditDialog
      ref="addOrEditDialogRef"
      :dialog-title="addOrEditDialogTitle"
      :dialog-visible="addOrEditDialogVisible"
      @addOrEditDone="addOrEditDone"
    />
  </div>
</template>

<script>
import addOrEditDialog from '@/views/admin/contentAudit/sensitiveWord/template/addOrEditDialog'
export default {
  name: 'SensitiveWord',
  components: {
    addOrEditDialog
  },
  data() {
    return {
      filters: {
        word: null,
        status: null
      },
      statusList: [],
      tableData: [],
      tableLoading: false,
      row: null,
      total: 0,
      page: 1,
      pageSize: 10,
      detailDialogVisible: false,
      addOrEditDialogTitle: null,
      addOrEditDialogVisible: false,
      authRoleDialogTitle: null,
      authRoleDialogVisible: false
    }
  },
  mounted() {
    this.loadStatusList()
    this.listTableData()
  },
  methods: {
    handleCurrentChange(row) {
      this.row = row
    },
    loadStatusList() {
      this.$mapi.communal.queryConfListByDictKey({ dictKey: 'SENSITIVE_WORD_STATUS_CONFIG' }).then(res => {
        const { data } = res
        this.statusList = data
      }).catch(_ => {
        this.statusList = []
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
      this.$mapi.sensitiveWord.pageSensitiveWordList(param).then(res => {
        const { data } = res
        this.total = data.total
        this.tableData = data.records
      }).catch(_ => {
        this.total = 0
        this.tableData = []
      }).finally(() => {
        this.tableLoading = false
        this.row = null
        this.$refs.sensitiveWordTable.setCurrentRow()
      })
    },
    // 新增与编辑
    addSensitiveWord() {
      this.addOrEditDialogVisible = true
      this.addOrEditDialogTitle = '新增敏感词'
      this.$refs.addOrEditDialogRef.initData()
    },
    editSensitiveWord() {
      if (this.row === null) {
        this.$message.error('请选择要编辑的敏感词')
        return
      }
      this.addOrEditDialogVisible = true
      this.addOrEditDialogTitle = '编辑敏感词'
      this.$refs.addOrEditDialogRef.initData(this.row)
    },
    addOrEditDone(result = false) {
      this.addOrEditDialogTitle = null
      this.addOrEditDialogVisible = false
      if (result) {
        this.listTableData()
      }
    },
    // 删除
    deleteSensitiveWord() {
      if (this.row === null) {
        this.$message.error('请选择要删除的敏感词')
        return
      }
      this.$confirm('此操作将永久删除敏感词【' + this.row.word + '】, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.$mapi.sensitiveWord.deleteSensitiveWord({ sensitiveWordId: this.row.id }).then(res => {
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
