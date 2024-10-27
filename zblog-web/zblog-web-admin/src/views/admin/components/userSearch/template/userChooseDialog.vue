<template>
  <div>
    <el-dialog append-to-body :close-on-click-modal="false" :before-close="handleClose" :visible.sync="dialogVisible" title="用户选择" width="1000px">
      <div class="head-container">
        <el-form ref="userSearchForm" :inline="true" size="small">
          <el-input v-model="filters.username" clearable placeholder="用户名" style="width: 200px" class="filter-item" @keyup.enter.native="search" />
          <el-input v-model="filters.nickname" clearable placeholder="用户昵称" style="width: 200px" class="filter-item" @keyup.enter.native="search" />
        </el-form>
      </div>
      <div class="components-container">
        <el-table v-loading="tableLoading" :data="tableData" style="width: 100%" border @selection-change="handleSelectionChange">
          <el-table-column type="selection" :selectable="selectable" width="55" />
          <el-table-column :show-overflow-tooltip="true" prop="username" label="用户名" align="center" />
          <el-table-column :show-overflow-tooltip="true" prop="nickname" label="用户昵称" align="center" />
          <el-table-column :show-overflow-tooltip="true" prop="loginTime" label="登录时间" align="center" />
          <el-table-column :show-overflow-tooltip="true" prop="temp" label="用户类型" align="center">
            <template v-slot="scope">
              <span v-if="scope.row['temp']">临时用户</span>
              <span v-else>正式用户</span>
            </template>
          </el-table-column>
          <el-table-column :show-overflow-tooltip="true" prop="locked" label="锁定状态" align="center">
            <template v-slot="scope">
              <span v-if="scope.row['locked']">锁定</span>
              <span v-else>正常</span>
            </template>
          </el-table-column>
          <el-table-column :show-overflow-tooltip="true" prop="enabled" label="启用状态" align="center">
            <template v-slot="scope">
              <span v-if="scope.row['enabled']">启用</span>
              <span v-else>禁用</span>
            </template>
          </el-table-column>
          <!-- <div slot="empty">
            <el-empty />
          </div> -->
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
        nickname: ''
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
  methods: {
    initData() {
      this.tableData = []
      this.selectRows = []
      this.tableLoading = false
      this.submitLoading = false
      this.listTableData()
    },
    doClose(result = false, data = []) {
      this.filters = {
        username: '',
        nickname: ''
      }
      this.$emit('chooseDown', result, data)
    },
    handleClose() {
      if (!this.submitLoading) {
        this.$confirm('确认关闭？').then(_ => {
          this.doClose()
        }).catch(_ => {})
      }
    },
    cancel() {
      this.handleClose()
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
    },
    search() {
      this.listTableData()
    },
    resetSearch() {
      this.filters.username = ''
      this.filters.nickname = ''
      this.listTableData()
    },
    listTableData() {
      const param = {
        ... this.filters,
        page: this.page,
        pageSize: this.pageSize
      }

      this.tableLoading = true
      this.$mapi.user.pageListOfCommon(param).then(res => {
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
    handleSelectionChange(val) {
      this.selectRows = val
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

<style rel="stylesheet/scss" lang="scss" scoped>

</style>
