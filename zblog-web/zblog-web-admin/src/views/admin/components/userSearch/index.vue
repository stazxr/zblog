<template>
  <div class="app-container">
    <div class="head-container">
      <!-- 页头 -->
      <el-page-header content="用户列表" style="padding-bottom: 10px;" @back="goBack" />
      <!-- 搜索 -->
      <div class="search-opts">
        <muses-search-form ref="searchForm" :model="filters" label-position="right" label-width="0" :offset="0" :item-width="140">
          <muses-search-form-item label="" prop="search-username">
            <el-input id="search-username" v-model="filters.username" clearable placeholder="用户名" @keyup.enter.native="search" />
          </muses-search-form-item>
          <muses-search-form-item label="" prop="search-nickname">
            <el-input id="search-nickname" v-model="filters.nickname" clearable placeholder="用户昵称" @keyup.enter.native="search" />
          </muses-search-form-item>
          <muses-search-form-item label="" prop="search-enabled">
            <el-select id="search-enabled" v-model="filters.enabled" placeholder="用户状态" clearable>
              <el-option label="启用" :value="true" />
              <el-option label="禁用" :value="false" />
            </el-select>
          </muses-search-form-item>
          <muses-search-form-item label="" prop="search-loginStartTime">
            <el-date-picker id="search-loginStartTime" v-model="filters.loginStartTime" clearable placeholder="登录开始时间" value-format="yyyy-MM-dd" />
          </muses-search-form-item>
          <muses-search-form-item label="" prop="search-loginEndTime">
            <el-date-picker id="search-loginEndTime" v-model="filters.loginEndTime" clearable placeholder="登录结束时间" value-format="yyyy-MM-dd" />
          </muses-search-form-item>
          <muses-search-form-item btn btn-open-name="" btn-close-name="">
            <el-button type="success" @click="search()">查 询</el-button>
            <el-button type="warning" @click="resetSearch()">重 置</el-button>
          </muses-search-form-item>
        </muses-search-form>
      </div>
      <div class="clearfix" style="text-align: right">
        <span style="float: left; line-height: 36px; height: 36px; font-size: 14px;">{{ tableTitle }}</span>
        <el-button size="small" type="primary" @click="addUserRelation()">新增</el-button>
        <el-button size="small" type="danger" :loading="deleteLoading" :disabled="selectRows.length === 0" @click="batchDeleteUserRelation()">删除</el-button>
      </div>
    </div>

    <div class="components-container">
      <el-table
        ref="userTable"
        v-loading="tableLoading"
        :data="tableData"
        style="width: 100%"
        border
        @selection-change="handleSelectionChange"
      >
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
import nodataImg from '@/assets/images/nodata.png'
export default {
  name: 'UserSearch',
  components: {
    userChooseDialog,
    showDetailDialog
  },
  data() {
    return {
      type: null,
      dataId: null,
      filters: {
        username: null,
        nickname: null,
        enabled: null,
        loginStartTime: null,
        loginEndTime: null
      },
      nodataImg: nodataImg,
      tableTitle: null,
      tableData: [],
      tableLoading: false,
      selectRows: [],
      total: 0,
      page: 1,
      pageSize: 10,

      deleteLoading: false,
      userChooseDialogVisible: false,
      showDetailDialogVisible: false
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
    handleSelectionChange(val) {
      this.selectRows = val
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
        businessId: this.dataId,
        page: this.page,
        pageSize: this.pageSize
      }

      if (this.type === 'role') {
        this.tableTitle = '当前角色名称为：' + this.$route.query.roleName
        this.tableLoading = true
        this.$mapi.role.pageUsersByRoleId(param).then(res => {
          const { data } = res
          this.total = data.total
          this.tableData = data.list
        }).catch(_ => {
          this.total = 0
          this.tableData = []
        }).finally(() => {
          this.tableLoading = false
          // TODO
          // this.row = null
          // this.$refs.roleTable.setCurrentRow()
        })
      }
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
    }
  }
}
</script>

<style scoped>
.head-container .search-opts .el-date-editor {
  width: 150px !important;
}
</style>
