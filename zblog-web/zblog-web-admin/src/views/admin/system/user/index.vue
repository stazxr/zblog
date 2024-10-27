<template>
  <div class="app-container">
    <div class="head-container">
      <!-- 搜索栏 -->
      <div class="search-opts">
        <muses-search-form ref="searchForm" :model="filters" label-position="right" label-width="0" :offset="20" :item-width="120">
          <muses-search-form-item label="" prop="search-username">
            <el-input
              id="search-username"
              v-model="filters.username"
              placeholder="用户名"
              maxlength="20"
              style="width: 120px"
              clearable
              @keyup.enter.native="search"
            />
          </muses-search-form-item>
          <muses-search-form-item label="" prop="search-nickname">
            <el-input
              id="search-nickname"
              v-model="filters.nickname"
              placeholder="用户昵称"
              maxlength="25"
              style="width: 120px"
              clearable
              @keyup.enter.native="search"
            />
          </muses-search-form-item>
          <muses-search-form-item label="" prop="search-enabled">
            <el-select id="search-enabled" v-model="filters.enabled" placeholder="用户状态" style="width: 120px" clearable>
              <el-option label="启用" value="true" />
              <el-option label="禁用" value="false" />
            </el-select>
          </muses-search-form-item>
          <muses-search-form-item btn>
            <el-button type="success" icon="el-icon-search" @click="search()">查询</el-button>
            <el-button type="warning" icon="el-icon-refresh-right" @click="resetSearch()">重置</el-button>
          </muses-search-form-item>
        </muses-search-form>
      </div>
      <!-- 操作栏 -->
      <div class="crud-opts">
        <span class="crud-opts-left">
          <el-button v-perm="['addUser']" type="primary" @click="addUser()">新增</el-button>
        </span>
      </div>
    </div>

    <div class="components-container">
      <el-table
        v-loading="tableLoading"
        element-loading-lock="true"
        element-loading-text="数据加载中"
        element-loading-spinner="el-icon-loading"
        element-loading-background="rgba(255, 255, 255, 0.9)"
        :data="tableData"
        border
        style="width: 100%"
      >
        <el-table-column :show-overflow-tooltip="true" prop="username" label="头像" align="center" width="80">
          <template v-slot="scope">
            <el-image :src="scope.row['headImgUrl']" :preview-src-list="[scope.row['headImgUrl']]" fit="contain" lazy class="el-avatar">
              <div slot="error">
                <i class="el-icon-user-solid" />
              </div>
            </el-image>
          </template>
        </el-table-column>
        <el-table-column :show-overflow-tooltip="true" prop="username" label="用户名" align="center" width="180" />
        <el-table-column :show-overflow-tooltip="true" prop="nickname" label="用户昵称" align="center" width="180" />
        <el-table-column :show-overflow-tooltip="true" prop="email" label="邮箱" align="center" width="180" />
        <el-table-column :show-overflow-tooltip="true" prop="gender" label="性别" align="center" width="80">
          <template v-slot="scope">
            <span v-if="scope.row.gender === 1">男</span>
            <span v-else-if="scope.row.gender === 2">女</span>
            <span v-else>隐藏</span>
          </template>
        </el-table-column>
        <el-table-column :show-overflow-tooltip="true" prop="loginTime" label="登录时间" align="center" />
        <el-table-column :show-overflow-tooltip="true" prop="enabled" label="用户状态" align="center">
          <template v-slot="scope">
            <el-switch
              v-model="scope.row.enabled"
              :disabled="!hasPerm('updateUserStatus') || user.id === scope.row.id"
              active-color="#409EFF"
              inactive-color="#F56C6C"
              @change="changeEnabled(scope.row, scope.row.enabled)"
            />
          </template>
        </el-table-column>
        <el-table-column label="操作" align="center" width="200px" fixed="right">
          <template v-slot="scope">
            <el-button type="text" @click="showUserDetail(scope.row)">详情</el-button>
            <el-button v-perm="['editUser']" type="text" @click="editUser(scope.row)">编辑</el-button>
            <el-button v-perm="['deleteUser']" type="text" @confirm="deleteUser(scope.row)">删除</el-button>
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
    <!-- 查看详情 -->
    <showDetailDialog
      ref="showDetailDialogRef"
      :dialog-visible="showDetailDialogVisible"
      @showDetailDone="showDetailDone"
    />
  </div>
</template>

<script>
import addOrEditDialog from '@/views/admin/system/user/template/addOrEditDialog'
import showDetailDialog from '@/views/admin/system/user/template/showDetailDialog'
import { mapGetters } from 'vuex'
export default {
  name: 'User',
  components: {
    addOrEditDialog,
    showDetailDialog
  },
  data() {
    return {
      filters: {
        username: '',
        nickname: '',
        enabled: ''
      },
      tableData: [],
      tableLoading: false,
      total: 0,
      page: 1,
      pageSize: 10,
      addOrEditDialogTitle: '',
      addOrEditDialogVisible: false,
      showDetailDialogVisible: false,
      enabledStatus: {
        true: '启用',
        false: '禁用'
      }
    }
  },
  computed: {
    ...mapGetters([
      'user'
    ])
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
      this.filters.username = ''
      this.filters.nickname = ''
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
      this.$mapi.user.pageList(param).then(res => {
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
    changeEnabled(data, val) {
      this.$confirm('此操作将' + this.enabledStatus[val] + '[' + data.username + '], 是否继续？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        const param = {
          id: data.id,
          enabled: val
        }
        this.$mapi.user.updateUserStatus(param).then(_ => {
          this.$message.success(this.enabledStatus[val] + '成功')
        }).catch(() => {
          data.enabled = !data.enabled
        })
      }).catch(() => {
        data.enabled = !data.enabled
      })
    },
    addUser() {
      this.addOrEditDialogVisible = true
      this.addOrEditDialogTitle = '新增用户'
      this.$refs.addOrEditDialogRef.initData()
    },
    editUser(row) {
      this.addOrEditDialogVisible = true
      this.addOrEditDialogTitle = '编辑用户'
      this.$refs.addOrEditDialogRef.initData(row.id)
    },
    addOrEditDone(result = false) {
      this.addOrEditDialogVisible = false
      this.addOrEditDialogTitle = ''
      if (result) {
        this.listTableData()
      }
    },
    showUserDetail(row) {
      this.showDetailDialogVisible = true
      this.$refs.showDetailDialogRef.initData(row.id)
    },
    showDetailDone() {
      this.showDetailDialogVisible = false
    },
    deleteUser(row) {
      this.$mapi.user.deleteUser({ userId: row.id }).then(res => {
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
