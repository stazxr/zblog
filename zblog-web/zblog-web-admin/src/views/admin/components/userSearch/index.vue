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
          <muses-search-form-item label="" prop="search-email">
            <el-input id="search-email" v-model="filters.email" clearable placeholder="邮箱" @keyup.enter.native="search" />
          </muses-search-form-item>
          <muses-search-form-item label="" prop="search-userType">
            <el-select id="search-userType" v-model="filters.userType" placeholder="用户类型" clearable>
              <el-option label="系统用户" :value="0" />
              <el-option label="普通用户" :value="1" />
              <el-option label="管理员用户" :value="2" />
              <el-option label="测试用户" :value="3" />
              <el-option label="临时用户" :value="4" />
            </el-select>
          </muses-search-form-item>
          <muses-search-form-item label="" prop="search-userStatus">
            <el-select id="search-userStatus" v-model="filters.userStatus" placeholder="用户状态" clearable>
              <el-option label="正常" :value="0" />
              <el-option label="禁用" :value="1" />
              <el-option label="锁定" :value="2" />
            </el-select>
          </muses-search-form-item>
          <muses-search-form-item label="" prop="search-loginChan">
            <el-select id="search-loginChan" v-model="filters.loginChan" placeholder="登录渠道" clearable>
              <el-option label="移动端" value="01" />
              <el-option label="PC端" value="02" />
            </el-select>
          </muses-search-form-item>
          <muses-search-form-item label="" prop="search-loginType">
            <el-select id="search-loginType" v-model="filters.loginType" placeholder="登录方式" clearable>
              <el-option label="访客" value="00" />
              <el-option label="密码" value="01" />
              <el-option label="QQ互信" value="02" />
              <el-option label="未知" value="99" />
            </el-select>
          </muses-search-form-item>
          <muses-search-form-item label="" prop="search-loginAddress">
            <el-input id="search-loginAddress" v-model="filters.loginAddress" clearable placeholder="登录地址" @keyup.enter.native="search" />
          </muses-search-form-item>
          <muses-search-form-item btn btn-open-name="" btn-close-name="">
            <el-button type="success" @click="search()">查 询</el-button>
            <el-button type="warning" @click="resetSearch()">重 置</el-button>
          </muses-search-form-item>
        </muses-search-form>
      </div>
      <div class="clearfix" style="text-align: right">
        <span style="float: left; line-height: 36px; height: 36px; font-size: 14px;">{{ tableTitle }}</span>
        <el-button v-show="showAddBtn" size="small" type="primary" @click="addUserRelation()">新增</el-button>
        <el-button v-show="showDelBtn" size="small" type="danger" :loading="deleteLoading" :disabled="selectRows.length === 0" @click="deleteUserRelation()">删除</el-button>
      </div>
    </div>

    <div class="components-container">
      <el-table
        ref="userTable"
        v-loading="tableLoading"
        :data="tableData"
        :header-cell-style="{background:'#FAFAFA'}"
        highlight-current-row
        row-key="id"
        border
        @selection-change="handleSelectionChange"
      >
        <el-table-column type="selection" width="55" />
        <el-table-column prop="headImgUrl" label="头像" align="center" width="70">
          <template v-slot="scope">
            <el-image :src="scope.row['headImgUrl']" :preview-src-list="[scope.row['headImgUrl']]" fit="contain" lazy class="el-avatar">
              <div slot="error">
                <i class="el-icon-user-solid" />
              </div>
            </el-image>
          </template>
        </el-table-column>
        <el-table-column :show-overflow-tooltip="true" prop="username" label="用户名" align="center" width="120" />
        <el-table-column :show-overflow-tooltip="true" prop="nickname" label="用户昵称" align="center" />
        <el-table-column :show-overflow-tooltip="true" prop="email" label="邮箱" align="center" />
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
        <el-table-column :show-overflow-tooltip="true" prop="gender" label="用户性别" align="center" width="80">
          <template v-slot="scope">
            <span v-if="scope.row.gender === 1">男</span>
            <span v-else-if="scope.row.gender === 2">女</span>
            <span v-else>隐藏</span>
          </template>
        </el-table-column>
        <el-table-column :show-overflow-tooltip="true" prop="loginChan" label="登录渠道" align="center" width="80">
          <template v-slot="scope">
            <span v-if="scope.row['loginChan'] === '01'">移动端</span>
            <span v-else-if="scope.row['loginChan'] === '02'">PC端</span>
            <span v-else>{{ scope.row['loginChan'] }}</span>
          </template>
        </el-table-column>
        <el-table-column :show-overflow-tooltip="true" prop="loginType" label="登录方式" align="center" width="80">
          <template v-slot="scope">
            <span v-if="scope.row['loginType'] === '00'">访客</span>
            <span v-else-if="scope.row['loginType'] === '01'">密码</span>
            <span v-else-if="scope.row['loginType'] === '02'">QQ互信</span>
            <span v-else-if="scope.row['loginType'] === '99'">未知</span>
            <span v-else>{{ scope.row['loginType'] }}</span>
          </template>
        </el-table-column>
        <el-table-column :show-overflow-tooltip="true" prop="loginAddress" label="登录地址" align="center" width="80" />
        <el-table-column :show-overflow-tooltip="true" prop="lastLoginTime" label="登录时间" align="center" width="140" />
        <el-table-column :show-overflow-tooltip="true" prop="createTime" label="创建时间" align="center" />
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
  </div>
</template>

<script>
import userChooseDialog from './template/userChooseDialog'
import nodataImg from '@/assets/images/nodata.png'
export default {
  name: 'UserSearch',
  components: {
    userChooseDialog
  },
  data() {
    return {
      type: null,
      dataId: null,
      showAddBtn: false,
      showDelBtn: false,
      filters: {
        username: null,
        nickname: null,
        email: null,
        userType: null,
        userStatus: null,
        loginChan: null,
        loginType: null,
        loginAddress: null
      },
      tableData: [],
      tableTitle: null,
      tableLoading: false,
      nodataImg: nodataImg,
      selectRows: [],
      total: 0,
      page: 1,
      pageSize: 10,

      deleteLoading: false,
      userChooseDialogVisible: false
    }
  },
  watch: {
    type(value) {
      if (value === 'role') {
        this.showAddBtn = true
        this.showDelBtn = true
      } else {
        this.showAddBtn = true
        this.showDelBtn = true
      }
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
      if (this.type === 'role') {
        const param = {
          ... this.filters,
          roleId: this.dataId,
          page: this.page,
          pageSize: this.pageSize
        }
        this.tableTitle = '当前角色名称为：' + this.$route.query.roleName
        this.tableLoading = true
        this.$mapi.user.pageListOfPublic(param).then(res => {
          const { data } = res
          this.total = data.total
          this.tableData = data.records
        }).catch(_ => {
          this.total = 0
          this.tableData = []
          this.selectRows = []
        }).finally(() => {
          this.tableLoading = false
        })
      } else {
        this.total = 0
        this.selectRows = []
        this.tableData = []
        this.tableTitle = null
      }
    },
    // 新增用户关联关系
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
    // 删除用户关联关系
    deleteUserRelation() {
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

</style>
