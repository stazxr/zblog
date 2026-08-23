<template>
  <div class="app-container">
    <div class="head-container">
      <div class="search-opts">
        <muses-search-form ref="searchForm" :model="filters" label-position="right" label-width="0" :offset="0" :item-width="140">
          <muses-search-form-item label="" prop="search-linkName">
            <el-input id="search-linkName" v-model="filters.linkName" clearable placeholder="网站链接名称" @keyup.enter.native="search" />
          </muses-search-form-item>
          <muses-search-form-item label="" prop="search-linkType">
            <el-input id="search-linkType" v-model="filters.linkType" clearable placeholder="网站链接类型" @keyup.enter.native="search" />
          </muses-search-form-item>
          <muses-search-form-item label="" prop="search-enabled">
            <el-select id="search-enabled" v-model="filters.enabled" placeholder="网站链接状态" clearable @change="search">
              <el-option v-for="item in enabledList" :key="item.value" :label="item.name" :value="item.value" />
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
          <el-button v-perm="['WEBLA001']" type="success" @click="addWebsiteLink">新增</el-button>
          <el-button v-perm="['WEBLQ002']" :disabled="row === null" type="info" @click="showDetail">详情</el-button>
          <el-button v-perm="['WEBLU001']" :disabled="row === null" type="primary" @click="editWebsiteLink">编辑</el-button>
          <el-button v-perm="['WEBLD001']" :disabled="row === null" type="danger" @click="deleteWebsiteLink">删除</el-button>
        </span>
      </div>
    </div>
    <div class="components-container">
      <el-table
        ref="websiteLinkTable"
        v-loading="tableLoading"
        :data="tableData"
        :header-cell-style="{background:'#FAFAFA'}"
        highlight-current-row
        row-key="id"
        border
        @current-change="handleCurrentChange"
      >
        <el-table-column :show-overflow-tooltip="true" prop="linkName" label="链接名称" align="center" width="140" />
        <el-table-column :show-overflow-tooltip="true" prop="linkType" label="链接类型" align="center" width="140" />
        <el-table-column :show-overflow-tooltip="true" label="链接地址" align="center">
          <template v-slot="scope">
            <el-link
              v-if="scope.row.linkUrl"
              :href="scope.row.linkUrl"
              type="primary"
              target="_blank"
              rel="nofollow noopener noreferrer"
            >
              {{ scope.row.linkUrl }}
            </el-link>
          </template>
        </el-table-column>
        <el-table-column prop="sort" label="链接排序" align="center" width="100" />
        <el-table-column label="链接状态" align="center" width="100">
          <template v-slot="scope">
            <el-tag v-if="scope.row.enabled === true" type="success">正常</el-tag>
            <el-tag v-else type="danger">禁用</el-tag>
          </template>
        </el-table-column>
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
import detailDialog from '@/views/admin/website/websiteLink/template/detailDialog'
import addOrEditDialog from '@/views/admin/website/websiteLink/template/addOrEditDialog'
export default {
  name: 'WebsiteLink',
  components: {
    detailDialog,
    addOrEditDialog
  },
  data() {
    return {
      filters: {
        linkName: null,
        linkType: null,
        enabled: null
      },
      enabledList: [],
      tableData: [],
      tableLoading: false,
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
    this.loadEnabledList()
    this.listTableData()
  },
  methods: {
    handleCurrentChange(row) {
      this.row = row
    },
    loadEnabledList() {
      this.$mapi.communal.queryConfListByDictKey({ dictKey: 'ENABLED_CONFIG' }).then(res => {
        const { data } = res
        this.enabledList = data
      }).catch(_ => {
        this.enabledList = []
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
      this.$mapi.websiteLink.pageWebsiteLinkList(param).then(res => {
        const { data } = res
        this.total = data.total
        this.tableData = data.records
      }).catch(_ => {
        this.total = 0
        this.tableData = []
      }).finally(() => {
        this.tableLoading = false
        this.row = null
        this.$refs.websiteLinkTable.setCurrentRow()
      })
    },
    // 详情
    showDetail() {
      if (this.row === null) {
        this.$message.error('请选择要查看的链接')
        return
      }
      this.detailDialogVisible = true
      this.$refs.detailDialogRef.initData(this.row.id)
    },
    showDetailDone() {
      this.detailDialogVisible = false
    },
    // 新增与编辑
    addWebsiteLink() {
      this.addOrEditDialogVisible = true
      this.addOrEditDialogTitle = '新增网站链接'
      this.$refs.addOrEditDialogRef.initData()
    },
    editWebsiteLink() {
      if (this.row === null) {
        this.$message.error('请选择要编辑的链接')
        return
      }
      this.addOrEditDialogVisible = true
      this.addOrEditDialogTitle = '编辑网站链接'
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
    deleteWebsiteLink() {
      if (this.row === null) {
        this.$message.error('请选择要删除的网站链接')
        return
      }
      this.$confirm('此操作将永久删除链接【' + this.row.linkName + '】, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.$mapi.websiteLink.deleteWebsiteLink({ websiteLinkId: this.row.id }).then(res => {
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
