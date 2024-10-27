<template>
  <div class="app-container">
    <div class="head-container">
      <div>
        <el-input v-model="filters.uri" clearable placeholder="接口地址" style="width: 150px" class="filter-item" @keyup.enter.native="search" />
        <span>
          <el-button class="filter-item" size="small" type="success" icon="el-icon-search" @click="search">查询</el-button>
          <el-button class="filter-item" size="small" type="warning" icon="el-icon-refresh-left" @click="resetSearch">重置</el-button>
          <el-button v-perm="['addBlackOrWhiteRouter']" class="filter-item" size="small" type="primary" icon="el-icon-plus" @click="addBlackOrWhiteUri()">
            新增
          </el-button>
        </span>
      </div>
    </div>

    <div class="components-container">
      <el-table v-loading="tableLoading" :data="tableData" border style="width: 100%">
        <el-table-column :show-overflow-tooltip="true" prop="name" label="接口名称" align="left" />
        <el-table-column :show-overflow-tooltip="true" prop="value" label="接口地址" align="left" />
        <el-table-column :show-overflow-tooltip="true" prop="sort" label="接口排序" align="left" />
        <el-table-column :show-overflow-tooltip="true" prop="desc" label="接口描述" align="left" />
        <el-table-column :show-overflow-tooltip="true" label="状态" align="center">
          <template slot-scope="scope">
            <el-switch
              v-model="scope.row['enabled']"
              :disabled="!hasPerm('changeBlackOrWhiteRouterStatus')"
              active-color="#13ce66"
              inactive-color="#ff4949"
              active-text="启用"
              inactive-text="禁用"
              @change="changeBlackOrWhiteUriStatus(scope.row, scope.row['enabled'])"
            />
          </template>
        </el-table-column>
        <el-table-column v-if="hasPerm(['editBlackOrWhiteRouter', 'deleteBlackOrWhiteRouter'])" label="操作" align="center" width="150">
          <template slot-scope="scope">
            <el-button-group>
              <el-button v-perm="['editBlackOrWhiteRouter']" type="success" size="mini" @click="editBlackOrWhiteUri(scope.row)">编辑</el-button>
              <el-popconfirm v-perm="['deleteBlackOrWhiteRouter']" title="操作不可撤销，确定删除吗？" @confirm="deleteBlackOrWhiteUri(scope.row)">
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
      :dict-key="dictKey"
      :data="editData"
      @addOrEditDone="addOrEditDone"
    />
  </div>
</template>

<script>
import addOrEditDialog from '@/views/admin/system/router/template/addOrEditDialog'
export default {
  components: {
    addOrEditDialog
  },
  props: {
    dictKey: {
      type: String,
      default: ''
    }
  },
  data() {
    return {
      filters: {
        uri: ''
      },
      enabledStatus: {
        true: '启用',
        false: '禁用'
      },
      tableData: [],
      tableLoading: false,
      total: 0,
      page: 1,
      pageSize: 10,
      editData: null,
      addOrEditDialogTitle: '',
      addOrEditDialogVisible: false
    }
  },
  methods: {
    initData() {
      this.$nextTick(() => {
        this.listTableData()
      })
    },
    search() {
      this.page = 1
      this.listTableData()
    },
    resetSearch() {
      this.filters.uri = ''

      this.page = 1
      this.listTableData()
    },
    listTableData() {
      const param = {
        ... this.filters,
        dictKey: this.dictKey,
        page: this.page,
        pageSize: this.pageSize
      }
      this.tableLoading = true
      this.$mapi.router.pageBlackOrWhiteList(param).then(res => {
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
    addBlackOrWhiteUri() {
      this.editData = null
      this.addOrEditDialogVisible = true
      this.addOrEditDialogTitle = this.dictKey === 'routerWhiteList' ? '新增白名单' : '新增黑名单'
    },
    editBlackOrWhiteUri(row) {
      this.editData = row
      this.addOrEditDialogVisible = true
      this.addOrEditDialogTitle = this.dictKey === 'routerWhiteList' ? '编辑白名单' : '编辑黑名单'
    },
    addOrEditDone(result = false) {
      this.editData = null
      this.addOrEditDialogVisible = false
      this.addOrEditDialogTitle = ''
      if (result) {
        this.listTableData()
      }
    },
    deleteBlackOrWhiteUri(row) {
      this.$mapi.router.deleteBlackOrWhiteRouter({ dictId: row.id }).then(res => {
        this.$message.success(res.message)
        this.listTableData()
      })
    },
    changeBlackOrWhiteUriStatus(data, val) {
      this.$confirm('此操作将' + this.enabledStatus[val] + '[' + data.name + '], 是否继续？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        const param = {
          dictId: data.id,
          enabled: val
        }
        this.$mapi.router.changeBlackOrWhiteRouterStatus(param).then(_ => {
          this.$message.success(this.enabledStatus[val] + '成功')
        }).catch(() => {
          data.enabled = !data.enabled
        })
      }).catch(() => {
        data.enabled = !data.enabled
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
