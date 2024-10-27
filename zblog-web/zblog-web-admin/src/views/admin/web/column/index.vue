<template>
  <div class="app-container">
    <div class="head-container">
      <div>
        <el-input v-model="filters.name" clearable placeholder="专栏名称" style="width: 180px" class="filter-item" @keyup.enter.native="search" />
        <el-select v-model="filters.pageShow" clearable placeholder="首页展示" style="width: 120px" class="filter-item">
          <el-option label="展示" value="true" />
          <el-option label="不展示" value="false" />
        </el-select>
        <el-select v-model="filters.enabled" clearable placeholder="专栏状态" style="width: 120px" class="filter-item">
          <el-option label="启用" value="true" />
          <el-option label="禁用" value="false" />
        </el-select>
        <span>
          <el-button class="filter-item" size="small" type="success" icon="el-icon-search" @click="search">查询</el-button>
          <el-button class="filter-item" size="small" type="warning" icon="el-icon-refresh-left" @click="resetSearch">重置</el-button>
        </span>
      </div>
      <div class="crud-opts">
        <span class="crud-opts-left">
          <el-button v-perm="['addColumn']" size="small" type="primary" @click="addColumn()">
            新增
          </el-button>
        </span>
      </div>
    </div>

    <div class="components-container">
      <el-table v-loading="tableLoading" :data="tableData" border style="width: 100%">
        <el-table-column :show-overflow-tooltip="true" prop="name" label="专栏名称" align="center" />
        <el-table-column :show-overflow-tooltip="true" prop="desc" label="专栏简介" align="center" />
        <el-table-column :show-overflow-tooltip="true" prop="sort" label="排序" align="center" width="100" />
        <el-table-column :show-overflow-tooltip="true" prop="articleCount" label="文章数" align="center" width="100" />
        <el-table-column :show-overflow-tooltip="true" prop="pageShow" label="首页展示" align="center" width="100">
          <template v-slot="scope">
            <el-tag v-if="scope.row.pageShow" size="small" type="success">展示</el-tag>
            <el-tag v-else size="small">不展示</el-tag>
          </template>
        </el-table-column>
        <el-table-column :show-overflow-tooltip="true" prop="enabled" label="专栏状态" align="center" width="100">
          <template v-slot="scope">
            <el-tag v-if="scope.row.enabled" size="small">正常</el-tag>
            <el-tag v-else size="small" type="warning">禁用</el-tag>
          </template>
        </el-table-column>
        <el-table-column :show-overflow-tooltip="true" prop="createUser" label="创建用户" align="center" width="100" />
        <el-table-column :show-overflow-tooltip="true" prop="createTime" label="创建时间" align="center" width="200" />
        <el-table-column label="操作" align="center" width="200">
          <template v-slot="scope">
            <el-button-group>
              <el-button type="info" size="mini" @click="showColumnDetail(scope.row)">详情</el-button>
              <el-button v-perm="['editColumn']" type="success" size="mini" @click="editColumn(scope.row)">编辑</el-button>
              <el-button v-perm="['configColumn']" type="primary" size="mini" @click="configColumn(scope.row)">配置</el-button>
              <el-popconfirm v-show="scope.row['articleCount'] === 0" v-perm="['deleteColumn']" title="操作不可撤销，确定删除吗？" @confirm="deleteColumn(scope.row)">
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
    <!-- 配置 -->
    <configColumnDialog
      ref="configColumnDialogRef"
      :dialog-visible="configDialogVisible"
      :dialog-title="configDialogTitle"
      @configDone="configDone"
    />
    <!-- 查看详情 -->
    <showDetailDialog
      ref="showDetailDialogRef"
      :dialog-visible="showDetailDialogVisible"
      @showDetailDone="showDetailDone"
    />
  </div>
</template>

<script>
import addOrEditDialog from '@/views/admin/web/column/template/addOrEditDialog'
import configColumnDialog from '@/views/admin/web/column/template/configColumnDialog'
import showDetailDialog from '@/views/admin/web/column/template/showDetailDialog'
export default {
  name: 'Column',
  components: {
    addOrEditDialog,
    configColumnDialog,
    showDetailDialog
  },
  data() {
    return {
      filters: {
        name: '',
        pageShow: '',
        enabled: ''
      },
      tableData: [],
      tableLoading: false,
      total: 0,
      page: 1,
      pageSize: 10,
      addOrEditDialogTitle: '',
      addOrEditDialogVisible: false,
      configDialogTitle: '',
      configDialogVisible: false,
      showDetailDialogVisible: false
    }
  },
  mounted() {
    this.listTableData()
  },
  methods: {
    hasPerm(value) {
      return this.checkPerm(value)
    },
    search() {
      this.page = 1
      this.listTableData()
    },
    resetSearch() {
      this.filters.name = ''
      this.filters.pageShow = ''
      this.filters.enabled = ''

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
      this.$mapi.column.pageColumnList(param).then(res => {
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
    addColumn() {
      this.addOrEditDialogVisible = true
      this.addOrEditDialogTitle = '新增专栏'
      this.$refs.addOrEditDialogRef.initData()
    },
    editColumn(row) {
      this.addOrEditDialogVisible = true
      this.addOrEditDialogTitle = '编辑专栏'
      this.$refs.addOrEditDialogRef.initData(row.id)
    },
    configColumn(row) {
      this.configDialogVisible = true
      this.configDialogTitle = '专栏配置 - ' + row.name
      this.$refs.configColumnDialogRef.initData(row.id)
    },
    showColumnDetail(row) {
      this.showDetailDialogVisible = true
      this.$refs.showDetailDialogRef.initData(row.id)
    },
    deleteColumn(row) {
      this.$mapi.column.deleteColumn({ columnId: row.id }).then(res => {
        this.$message.success(res.message)
        this.listTableData()
      })
    },
    addOrEditDone(result = false) {
      this.addOrEditDialogVisible = false
      this.addOrEditDialogTitle = ''
      if (result) {
        this.listTableData()
      }
    },
    configDone() {
      this.configDialogTitle = ''
      this.configDialogVisible = false
    },
    showDetailDone() {
      this.showDetailDialogVisible = false
    },
    sizeChange(size) {
      this.page = 1
      this.pageSize = size
      this.listTableData()
    },
    pageChange(page) {
      this.page = page
      this.listTableData()
    }
  }
}
</script>

<style scoped>

</style>
