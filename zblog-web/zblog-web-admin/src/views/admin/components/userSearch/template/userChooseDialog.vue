<template>
  <div>
    <el-dialog
      title="用户选择"
      :visible.sync="dialogVisible"
      :fullscreen="device === 'mobile'"
      :destroy-on-close="true"
      :close-on-click-modal="false"
      :close-on-press-escape="true"
      :before-close="handleClose"
      append-to-body
      width="800px"
    >
      <div class="head-container">
        <muses-search-form ref="searchForm" :model="filters" label-position="right" label-width="0" :offset="0" :item-width="140">
          <muses-search-form-item label="" prop="search-username">
            <el-input id="search-username" v-model="filters.username" clearable placeholder="用户名" @keyup.enter.native="search" />
          </muses-search-form-item>
          <muses-search-form-item label="" prop="search-nickname">
            <el-input id="search-nickname" v-model="filters.nickname" clearable placeholder="用户昵称" @keyup.enter.native="search" />
          </muses-search-form-item>
          <muses-search-form-item label="" prop="search-userType">
            <el-select id="search-userType" v-model="filters.userType" placeholder="用户类型" clearable @change="search">
              <el-option label="系统用户" :value="0" />
              <el-option label="普通用户" :value="1" />
              <el-option label="管理员用户" :value="2" />
              <el-option label="测试用户" :value="3" />
              <el-option label="临时用户" :value="4" />
            </el-select>
          </muses-search-form-item>
          <muses-search-form-item label="" prop="search-userStatus">
            <el-select id="search-userStatus" v-model="filters.userStatus" placeholder="用户状态" clearable @change="search">
              <el-option label="正常" :value="0" />
              <el-option label="禁用" :value="1" />
              <el-option label="锁定" :value="2" />
            </el-select>
          </muses-search-form-item>
        </muses-search-form>
      </div>
      <div class="components-container">
        <el-table v-loading="tableLoading" :data="tableData" style="width: 100%" border @selection-change="handleSelectionChange">
          <el-table-column type="selection" :selectable="selectable" width="55" />
          <el-table-column :show-overflow-tooltip="true" prop="username" label="用户名" align="center" />
          <el-table-column :show-overflow-tooltip="true" prop="nickname" label="用户昵称" align="center" />
          <el-table-column :show-overflow-tooltip="true" prop="lastLoginTime" label="登录时间" align="center" />
          <el-table-column :show-overflow-tooltip="true" prop="userType" label="用户类型" align="center" width="80">
            <template v-slot="scope">
              <span v-if="scope.row.userType === 0">系统用户</span>
              <span v-else-if="scope.row.userType === 1">普通用户</span>
              <span v-else-if="scope.row.userType === 2">管理员</span>
              <span v-else-if="scope.row.userType === 3">测试用户</span>
              <span v-else-if="scope.row.userType === 4">临时用户</span>
              <span v-else />
            </template>
          </el-table-column>
          <el-table-column :show-overflow-tooltip="true" prop="userStatus" label="用户状态" align="center" width="80">
            <template v-slot="scope">
              <el-tag v-if="scope.row.userStatus === 0" type="success">正常</el-tag>
              <el-tag v-else-if="scope.row.userStatus === 1" type="danger">禁用</el-tag>
              <el-tag v-else-if="scope.row.userStatus === 2" type="warning">锁定</el-tag>
              <span v-else />
            </template>
          </el-table-column>
        </el-table>
      </div>
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
      <div slot="footer" class="dialog-footer" style="text-align: center">
        <el-button size="small" type="primary" :loading="submitLoading" @click="submit">确 认</el-button>
        <el-button size="small" type="info" @click="cancel">取 消</el-button>
        <el-button size="small" type="success" @click="search">查 询</el-button>
        <el-button size="small" type="warning" @click="resetSearch">重 置</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
export default {
  props: {
    dialogVisible: {
      type: Boolean,
      default: false
    },
    type: {
      type: String,
      default: ''
    },
    dataId: {
      type: String,
      default: ''
    }
  },
  data() {
    return {
      filters: {
        username: '',
        nickname: '',
        userType: null,
        userStatus: null
      },
      selectRows: [],
      tableData: [],
      tableLoading: false,
      submitLoading: false,
      total: 0,
      page: 1,
      pageSize: 10
    }
  },
  computed: {
    device() {
      return this.$store.state.app.device
    }
  },
  methods: {
    initData() {
      this.tableData = []
      this.selectRows = []
      this.tableLoading = false
      this.submitLoading = false
      this.listTableData()
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
    handleSelectionChange(val) {
      this.selectRows = val
    },
    selectable(row) {
      if (this.type === 'role' && this.dataId !== '') {
        // 角色 - 用户的校验
        if (row['roleIds'] && row['roleIds'].length) {
          return !row['roleIds'].includes(this.dataId)
        }
      }
      // 其他场景都允许选择
      return true
    },
    listTableData() {
      const param = {
        ... this.filters,
        page: this.page,
        pageSize: this.pageSize
      }
      this.tableLoading = true
      this.$mapi.user.pageListOfPublic(param).then(res => {
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
    cancel() {
      this.handleClose()
    },
    handleClose() {
      if (!this.submitLoading) {
        this.$confirm('确认关闭？').then(_ => {
          this.doClose()
        }).catch(_ => {})
      }
    },
    doClose(result = false, data = []) {
      Object.keys(this.filters).forEach(key => { this.filters[key] = null })
      this.page = 1
      this.pageSize = 10
      this.tableData = []
      this.selectRows = []
      this.tableLoading = false
      this.submitLoading = false
      this.$emit('chooseDown', result, data)
    },
    submit() {
      if (this.selectRows.length === 0) {
        this.$message.error('请至少选择一条记录')
        return
      }

      const data = []
      this.selectRows.forEach(row => {
        data.push(row.id)
      })
      this.doClose(true, data)
    }
  }
}
</script>

<style scoped>

</style>
