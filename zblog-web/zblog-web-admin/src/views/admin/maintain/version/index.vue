<template>
  <div class="app-container">
    <div class="head-container">
      <div class="search-opts">
        <muses-search-form ref="searchForm" :model="filters" label-position="right" label-width="0" :offset="0" :item-width="160">
          <muses-search-form-item label="" prop="search-versionName">
            <el-input id="search-versionName" v-model="filters.versionName" clearable placeholder="版本名称" @keyup.enter.native="search" />
          </muses-search-form-item>
          <muses-search-form-item label="" prop="search-updateContent">
            <el-input id="search-updateContent" v-model="filters.updateContent" clearable placeholder="版本描述" @keyup.enter.native="search" />
          </muses-search-form-item>
          <muses-search-form-item btn btn-open-name="" btn-close-name="">
            <el-button type="success" @click="search()">查 询</el-button>
            <el-button type="warning" @click="resetSearch()">重 置</el-button>
          </muses-search-form-item>
        </muses-search-form>
      </div>
      <div class="crud-opts">
        <span class="crud-opts-left">
          <el-button v-perm="['VERSA001']" type="success" @click="addVersion">新增</el-button>
          <el-button v-perm="['VERSU001']" :disabled="row === null" type="primary" @click="editVersion">编辑</el-button>
          <el-button v-perm="['VERSD001']" :disabled="row === null" type="danger" @click="deleteVersion">删除</el-button>
        </span>
      </div>
    </div>
    <div class="components-container">
      <el-table
        ref="versionTable"
        v-loading="tableLoading"
        :data="tableData"
        :header-cell-style="{background:'#FAFAFA'}"
        highlight-current-row
        row-key="id"
        border
        @current-change="handleCurrentChange"
      >
        <el-table-column :show-overflow-tooltip="true" prop="versionName" label="版本名称" width="130" />
        <el-table-column :show-overflow-tooltip="true" prop="updateContent" label="版本描述" />
        <el-table-column :show-overflow-tooltip="true" prop="sort" label="排序" align="center" width="80px" />
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
import addOrEditDialog from '@/views/admin/maintain/version/template/addOrEditDialog'
import nodataImg from '@/assets/images/nodata.png'
export default {
  name: 'Version',
  components: {
    addOrEditDialog
  },
  data() {
    return {
      filters: {
        versionName: null,
        updateContent: null
      },
      tableData: [],
      tableLoading: false,
      nodataImg: nodataImg,
      row: null,
      total: 0,
      page: 1,
      pageSize: 10,
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
      this.$mapi.version.pageVersionList(param).then(res => {
        const { data } = res
        this.total = data.total
        this.tableData = data.list
      }).catch(_ => {
        this.total = 0
        this.tableData = []
      }).finally(() => {
        this.tableLoading = false
        this.row = null
        this.$refs.versionTable.setCurrentRow()
      })
    },
    // 新增与编辑
    addVersion() {
      this.addOrEditDialogVisible = true
      this.addOrEditDialogTitle = '新增版本'
      this.$refs.addOrEditDialogRef.initData()
    },
    editVersion() {
      if (this.row === null) {
        this.$message.error('请选择要编辑的版本')
        return
      }
      this.addOrEditDialogVisible = true
      this.addOrEditDialogTitle = '编辑版本'
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
    deleteVersion() {
      if (this.row === null) {
        this.$message.error('请选择要删除的版本')
        return
      }
      this.$confirm('此操作将永久删除版本【' + this.row.versionName + '】, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.$mapi.version.deleteVersion({ versionId: this.row.id }).then(res => {
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
