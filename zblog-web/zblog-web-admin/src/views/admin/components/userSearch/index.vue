<template>
  <div class="app-container">
    <div class="head-container">
      <el-page-header content="用户列表" style="padding-bottom: 10px;" @back="goBack" />

      <el-form ref="userSearchForm" :inline="true" size="small">
        <el-input v-model="filters.username" clearable placeholder="用户名" style="width: 200px" class="filter-item" @keyup.enter.native="search" />
        <el-input v-model="filters.nickname" clearable placeholder="用户昵称" style="width: 200px" class="filter-item" @keyup.enter.native="search" />
        <el-date-picker v-model="filters.loginStartTime" placeholder="登录开始时间" value-format="yyyy-MM-dd" />
        <el-date-picker v-model="filters.loginEndTime" placeholder="登录结束时间" value-format="yyyy-MM-dd" />
        <el-form-item>
          <el-button class="filter-item" size="small" type="success" icon="el-icon-search" @click="search">查询</el-button>
          <el-button class="filter-item" size="small" type="warning" icon="el-icon-refresh-left" @click="resetSearch">重置</el-button>
        </el-form-item>
      </el-form>

      <div class="clearfix" style="text-align: right">
        <span style="float: left; line-height: 36px; height: 36px; font-size: 14px;">{{ tableTitle }}</span>
        <el-button size="small" type="primary" @click="addUserRelation()">新增</el-button>
        <el-button size="small" type="danger" :loading="deleteLoading" :disabled="selectRows.length === 0" @click="batchDeleteUserRelation()">批量删除</el-button>
      </div>
    </div>

    <div class="components-container">
      <el-table v-loading="tableLoading" :data="tableData" style="width: 100%" border @selection-change="handleSelectionChange">
        <el-table-column type="selection" width="55" />
        <el-table-column :show-overflow-tooltip="true" prop="username" label="用户名" align="center" />
        <el-table-column :show-overflow-tooltip="true" prop="nickname" label="用户昵称" align="center" />
        <el-table-column :show-overflow-tooltip="true" prop="loginTime" label="登录时间" align="center" />
        <el-table-column :show-overflow-tooltip="true" prop="authTime" label="授权时间" align="center" />
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
        <el-table-column label="操作" align="center" width="150px">
          <template v-slot="scope">
            <el-button-group>
              <el-button v-if="['queryUserDetail']" type="info" size="mini" @click="showUserDetail(scope.row)">详情</el-button>
              <el-popconfirm title="操作不可撤销，确定删除吗？" @confirm="deleteUserRelation(scope.row)">
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

    <!-- 新增用户 -->
    <userChooseDialog
      ref="userChooseDialogRef"
      :dialog-visible="userChooseDialogVisible"
      :type="type"
      :data-id="dataId"
      @chooseDown="userChooseDown"
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
import userChooseDialog from './template/userChooseDialog'
import showDetailDialog from '../../system/user/template/showDetailDialog'
export default {
  name: 'UserSearch',
  components: {
    userChooseDialog,
    showDetailDialog
  },
  data() {
    return {
      filters: {
        username: '',
        nickname: '',
        loginStartTime: '',
        loginEndTime: ''
      },
      selectRows: [],
      tableData: [],
      tableLoading: false,
      deleteLoading: false,
      total: 0,
      page: 1,
      pageSize: 10,
      tableTitle: '',
      userChooseDialogVisible: false,
      showDetailDialogVisible: false,
      type: '',
      dataId: ''
    }
  },
  mounted() {
    this.type = this.$route.query.type
    this.dataId = this.$route.query.businessId

    this.listTableData()
  },
  methods: {
    goBack() {
      this.$router.go(-1)
    },
    search() {
      this.listTableData()
    },
    resetSearch() {
      this.filters.username = ''
      this.filters.nickname = ''
      this.filters.loginStartTime = ''
      this.filters.loginEndTime = ''
      this.listTableData()
    },
    listTableData() {
      const param = {
        ... this.filters,
        businessId: this.dataId,
        page: this.page,
        pageSize: this.pageSize
      }

      if (this.type === 'role') {
        this.tableTitle = '当前角色名称为：' + this.$route.query.roleName
        this.tableLoading = true
        this.$mapi.role.pageUsersByRoleId(param).then(res => {
          const { data } = res
          this.tableData = data.list
          this.total = data.total
        }).catch(_ => {
          this.tableData = []
          this.total = 0
        }).finally(() => {
          this.tableLoading = false
        })
      }
    },
    handleSelectionChange(val) {
      this.selectRows = val
    },
    showUserDetail(row) {
      this.showDetailDialogVisible = true
      this.$refs.showDetailDialogRef.initData(row.id.toString())
    },
    showDetailDone() {
      this.showDetailDialogVisible = false
    },
    addUserRelation() {
      this.userChooseDialogVisible = true
      this.$refs.userChooseDialogRef.initData()
    },
    userChooseDown(result, data) {
      if (result && (data.length && data.length > 0)) {
        if (this.type === 'role') {
          const param = {
            roleId: this.dataId,
            userIds: data
          }
          this.$mapi.role.batchAddUserRole(param).then(res => {
            this.$message.success(res.message)
            this.listTableData()
          }).finally(_ => {
            this.userChooseDialogVisible = false
          })
        }
      } else {
        this.userChooseDialogVisible = false
      }
    },
    deleteUserRelation(row) {
      const userIds = []
      userIds.push(row.id)
      if (this.type === 'role') {
        const param = {
          roleId: this.dataId,
          userIds: userIds
        }
        this.$mapi.role.batchDeleteUserRole(param).then(res => {
          this.$message.success(res.message)
          this.listTableData()
        })
      }
    },
    batchDeleteUserRelation() {
      if (this.selectRows.length === 0) {
        this.$message.error('请选择需要删除的记录')
      }

      this.$confirm('此操作将永久删除选中记录, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        const userIds = []
        this.selectRows.forEach(row => {
          userIds.push(row.id)
        })
        const param = {
          roleId: this.dataId,
          userIds: userIds
        }

        this.deleteLoading = true
        this.$mapi.role.batchDeleteUserRole(param).then(res => {
          this.$message.success(res.message)
          this.listTableData()
        }).finally(_ => {
          this.deleteLoading = false
        })
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
    }
  }
}
</script>

<style scoped>

</style>
