<template>
  <div class="app-container">
    <div class="head-container">
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
              <el-option v-for="item in userTypeList" :key="item.value" :label="item.name" :value="item.value" />
            </el-select>
          </muses-search-form-item>
          <muses-search-form-item label="" prop="search-userStatus">
            <el-select id="search-userStatus" v-model="filters.userStatus" placeholder="用户状态" clearable>
              <el-option v-for="item in userStatusList" :key="item.value" :label="item.name" :value="item.value" />
            </el-select>
          </muses-search-form-item>
          <muses-search-form-item label="" prop="search-loginChan">
            <el-select id="search-loginChan" v-model="filters.loginChan" placeholder="登录渠道" clearable>
              <el-option v-for="item in loginChanList" :key="item.value" :label="item.name" :value="item.value" />
            </el-select>
          </muses-search-form-item>
          <muses-search-form-item label="" prop="search-loginType">
            <el-select id="search-loginType" v-model="filters.loginType" placeholder="登录方式" clearable>
              <el-option v-for="item in loginTypeList" :key="item.value" :label="item.name" :value="item.value" />
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
      <div class="crud-opts">
        <span class="crud-opts-left">
          <el-button v-perm="['USERA001']" type="success" @click="addUser">新增</el-button>
          <el-button v-perm="['USERQ003']" :disabled="row === null" type="info" @click="showDetail">详情</el-button>
          <el-button v-perm="['USERU001']" :disabled="row === null || editOrDeleteDisabled" type="primary" @click="editUser">编辑</el-button>
          <el-button
            v-perm="['USERD001']"
            :disabled="row === null || editOrDeleteDisabled || row.id === user.id"
            type="danger"
            @click="deleteUser"
          >删除</el-button>
        </span>
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
        @current-change="handleCurrentChange"
      >
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
        <el-table-column :show-overflow-tooltip="true" prop="successLoginTime" label="登录时间" align="center" width="140" />
        <el-table-column :show-overflow-tooltip="true" prop="createTime" label="创建时间" align="center" width="140" />
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
import detailDialog from '@/views/admin/system/user/template/detailDialog'
import addOrEditDialog from '@/views/admin/system/user/template/addOrEditDialog'
import nodataImg from '@/assets/images/nodata.png'
import { mapGetters } from 'vuex'
export default {
  name: 'User',
  components: {
    detailDialog,
    addOrEditDialog
  },
  data() {
    return {
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
      userTypeList: [],
      userStatusList: [],
      loginChanList: [],
      loginTypeList: [],
      tableData: [],
      tableLoading: false,
      nodataImg: nodataImg,
      row: null,
      total: 0,
      page: 1,
      pageSize: 10,
      editOrDeleteDisabled: true,
      detailDialogVisible: false,
      addOrEditDialogTitle: null,
      addOrEditDialogVisible: false
    }
  },
  computed: {
    ...mapGetters([
      'user'
    ])
  },
  mounted() {
    this.loadUserTypeList()
    this.loadUserStatusList()
    this.loadLoginChanList()
    this.loadLoginTypeList()
    this.listTableData()
  },
  methods: {
    handleCurrentChange(row) {
      this.row = row
      if (this.row != null && this.user != null) {
        const userType = this.user.userType
        const chooseUserType = this.row.userType
        if (userType === 2) {
          this.editOrDeleteDisabled = false
        } else if (userType === 1) {
          // 普通用户 > 普通用户,测试用户,临时用户
          this.editOrDeleteDisabled = chooseUserType === 0 || chooseUserType === 2
        } else if (userType === 3) {
          // 测试用户 > 测试用户
          this.editOrDeleteDisabled = chooseUserType !== 3
        } else if (userType === 4) {
          // 临时用户 > 临时用户
          this.editOrDeleteDisabled = chooseUserType !== 4
        }
      } else {
        this.editOrDeleteDisabled = true
      }
    },
    loadUserTypeList() {
      this.$mapi.communal.queryConfListByDictKey({ dictKey: 'USER_TYPE_CONFIG' }).then(res => {
        const { data } = res
        this.userTypeList = data
      }).catch(_ => {
        this.userTypeList = []
      })
    },
    loadUserStatusList() {
      this.$mapi.communal.queryConfListByDictKey({ dictKey: 'USER_STATUS_CONFIG' }).then(res => {
        const { data } = res
        this.userStatusList = data
      }).catch(_ => {
        this.userStatusList = []
      })
    },
    loadLoginChanList() {
      this.$mapi.communal.queryConfListByDictKey({ dictKey: 'LOGIN_CHAN_CONFIG' }).then(res => {
        const { data } = res
        this.loginChanList = data
      }).catch(_ => {
        this.loginChanList = []
      })
    },
    loadLoginTypeList() {
      this.$mapi.communal.queryConfListByDictKey({ dictKey: 'LOGIN_TYPE_CONFIG' }).then(res => {
        const { data } = res
        this.loginTypeList = data
      }).catch(_ => {
        this.loginTypeList = []
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
      this.$mapi.user.pageUserList(param).then(res => {
        const { data } = res
        this.total = data.total
        this.tableData = data.list
      }).catch(_ => {
        this.total = 0
        this.tableData = []
      }).finally(() => {
        this.tableLoading = false
        this.row = null
        this.$refs.userTable.setCurrentRow()
      })
    },
    // 详情
    showDetail() {
      if (this.row === null) {
        this.$message.error('请选择要查看的用户')
        return
      }
      this.detailDialogVisible = true
      this.$refs.detailDialogRef.initData(this.row.id)
    },
    showDetailDone() {
      this.detailDialogVisible = false
    },
    // 新增与编辑
    addUser() {
      this.addOrEditDialogVisible = true
      this.addOrEditDialogTitle = '新增用户'
      this.$refs.addOrEditDialogRef.initData()
    },
    editUser() {
      if (this.row === null) {
        this.$message.error('请选择要编辑的用户')
        return
      }
      this.addOrEditDialogVisible = true
      this.addOrEditDialogTitle = '编辑用户'
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
    deleteUser() {
      if (this.row === null) {
        this.$message.error('请选择要删除的用户')
        return
      }
      this.$confirm('此操作将永久删除用户【' + this.row.username + '】, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.$mapi.user.deleteUser({ userId: this.row.id }).then(res => {
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
