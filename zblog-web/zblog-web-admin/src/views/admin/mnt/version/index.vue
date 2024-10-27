<template>
  <div class="app-container">
    <div class="head-container">
      <div>
        <el-input v-model="filters.versionName" clearable placeholder="版本名称" style="width: 150px" class="filter-item" @keyup.enter.native="search" />
        <el-input v-model="filters.updateContent" clearable placeholder="版本描述" style="width: 150px" class="filter-item" @keyup.enter.native="search" />
        <span>
          <el-button class="filter-item" size="small" type="success" icon="el-icon-search" @click="search">查询</el-button>
          <el-button class="filter-item" size="small" type="warning" icon="el-icon-refresh-left" @click="resetSearch">重置</el-button>
        </span>
      </div>
      <div class="crud-opts">
        <span class="crud-opts-left">
          <el-button v-perm="['addVersion']" size="small" type="primary" @click="addVersion()">
            新增
          </el-button>
        </span>
      </div>
    </div>

    <div class="components-container">
      <el-table v-loading="tableLoading" :data="tableData" border style="width: 100%">
        <el-table-column :show-overflow-tooltip="true" prop="versionName" label="版本名称" width="200" />
        <el-table-column :show-overflow-tooltip="true" prop="createTime" label="创建时间" width="200" />
        <el-table-column :show-overflow-tooltip="true" prop="updateContent" label="版本描述" />
        <el-table-column v-if="hasPerm(['editVersion', 'deleteVersion'])" label="操作" align="center" width="200px">
          <template slot-scope="scope">
            <el-button-group>
              <el-button v-perm="['editVersion']" type="success" size="mini" @click="editVersion(scope.row)">编辑</el-button>
              <el-popconfirm v-perm="['deleteVersion']" title="操作不可撤销，确定删除吗？" @confirm="deleteVersion(scope.row)">
                <el-button slot="reference" type="danger" size="mini">删除</el-button>
              </el-popconfirm>
            </el-button-group>
          </template>
        </el-table-column>
        <div slot="empty">
          <el-empty />
        </div>
      </el-table>
    </div>

    <div class="pagination-container">
      <el-pagination
        :total="total"
        :current-page="page"
        :page-size="pageSize"
        style="margin-top: 8px;"
        layout="total, prev, pager, next, sizes"
        @size-change="sizeChange"
        @current-change="pageChange"
      />
    </div>

    <!-- 新增 / 编辑 -->
    <addOrEditDialog
      ref="addOrEditDialogRef"
      :dialog-visible="addOrEditDialogVisible"
      :dialog-title="addOrEditDialogTitle"
      @addOrEditDone="addOrEditDone"
    />
  </div>
</template>

<script>
import addOrEditDialog from '@/views/admin/mnt/version/template/addOrEditDialog'
export default {
  name: 'Version',
  components: {
    addOrEditDialog
  },
  data() {
    return {
      filters: {
        versionName: '',
        updateContent: ''
      },
      tableData: [],
      tableLoading: false,
      total: 0,
      page: 1,
      pageSize: 10,
      dataId: null,
      addOrEditDialogTitle: '',
      addOrEditDialogVisible: false
    }
  },
  mounted() {
    this.listTableData()
  },
  methods: {
    search() {
      this.page = 1
      this.listTableData()
    },
    resetSearch() {
      this.filters.versionName = ''
      this.filters.updateContent = ''

      this.page = 1
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
        this.tableData = data.list
        this.total = data.total
      }).catch(_ => {
        this.tableData = []
        this.total = 0
      }).finally(() => {
        this.tableLoading = false
      })
    },
    addVersion() {
      this.addOrEditDialogVisible = true
      this.addOrEditDialogTitle = '新增版本'
      this.$refs.addOrEditDialogRef.initData()
    },
    editVersion(row) {
      this.addOrEditDialogVisible = true
      this.addOrEditDialogTitle = '编辑版本'
      this.$refs.addOrEditDialogRef.initData(row.id)
    },
    addOrEditDone(result = false) {
      this.addOrEditDialogVisible = false
      this.addOrEditDialogTitle = ''
      if (result) {
        this.listTableData()
      }
    },
    deleteVersion(row) {
      this.$mapi.version.deleteVersion({ versionId: row.id }).then(res => {
        this.$message.success(res.message)
        this.listTableData()
      })
    },
    sizeChange(size) {
      this.page = 1
      this.pageSize = size
      this.listTableData()
    },
    pageChange(page) {
      this.page = page
      this.listTableData()
    },
    hasPerm(value) {
      return this.checkPerm(value)
    }
  }
}
</script>

<style scoped>

</style>
