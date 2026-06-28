<template>
  <div class="app-container">
    <div class="head-container">
      <div class="search-opts">
        <muses-search-form ref="searchForm" :model="filters" label-position="right" label-width="0" :offset="0" :item-width="140">
          <muses-search-form-item label="" prop="search-pageName">
            <el-input id="search-pageName" v-model="filters.pageName" clearable placeholder="页面名称" @keyup.enter.native="search" />
          </muses-search-form-item>
          <muses-search-form-item label="" prop="search-pageLabel">
            <el-input id="search-pageLabel" v-model="filters.pageLabel" clearable placeholder="页面标识" @keyup.enter.native="search" />
          </muses-search-form-item>
          <muses-search-form-item btn btn-open-name="" btn-close-name="">
            <el-button type="success" @click="search()">查 询</el-button>
            <el-button type="warning" @click="resetSearch()">重 置</el-button>
          </muses-search-form-item>
        </muses-search-form>
      </div>
      <div class="crud-opts">
        <span class="crud-opts-left">
          <el-button v-perm="['PAGEA001']" type="success" @click="addPage">新增</el-button>
          <el-button v-perm="['PAGEQ002']" :disabled="row === null" type="info" @click="showDetail">详情</el-button>
          <el-button v-perm="['PAGEU001']" :disabled="row === null" type="primary" @click="editPage">编辑</el-button>
          <el-button v-perm="['PAGED001']" :disabled="row === null" type="danger" @click="deletePage">删除</el-button>
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

    <!-- 详情 -->
    <detailDialog
      ref="detailDialogRef"
      :dialog-visible="detailDialogVisible"
      @showDetailDone="showDetailDone"
    />
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
import nodataImg from '@/assets/images/nodata.png'
import detailDialog from '@/views/admin/website/page/template/detailDialog'
import addOrEditDialog from '@/views/admin/website/page/template/addOrEditDialog'
export default {
  name: 'Page',
  components: {
    detailDialog,
    addOrEditDialog
  },
  data() {
    return {
      filters: {
        pageName: null,
        pageLabel: null
      },
      tableData: [],
      tableLoading: false,
      nodataImg: nodataImg,
      row: null,
      total: 0,
      page: 1,
      pageSize: 10,
      detailDialogVisible: false,
      addOrEditDialogTitle: null,
      addOrEditDialogVisible: false
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
      this.$mapi.page.pagePageList(param).then(res => {
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
        this.$message.error('请选择要查看的页面')
        return
      }
      this.detailDialogVisible = true
      this.$refs.detailDialogRef.initData(this.row.id)
    },
    showDetailDone() {
      this.detailDialogVisible = false
    },
    // 新增与编辑
    addPage() {
      this.addOrEditDialogVisible = true
      this.addOrEditDialogTitle = '新增页面'
      this.$refs.addOrEditDialogRef.initData()
    },
    editPage() {
      if (this.row === null) {
        this.$message.error('请选择要编辑的页面')
        return
      }
      this.addOrEditDialogVisible = true
      this.addOrEditDialogTitle = '编辑页面'
      this.$refs.addOrEditDialogRef.initData(this.row.id)
    },
    addOrEditDone(result = false) {
      this.addOrEditDialogTitle = null
      this.addOrEditDialogVisible = false
      if (result) {
        this.listTableData()
      }
    },
    // 删除
    deletePage() {
      if (this.row === null) {
        this.$message.error('请选择要删除的页面')
        return
      }
      this.$confirm('此操作将永久删除页面【' + this.row.pageName + '】, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.$mapi.page.deletePage({ pageId: this.row.id }).then(res => {
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
