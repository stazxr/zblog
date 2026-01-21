<template>
  <div class="app-container">
    <div class="head-container">
      <div class="search-opts">
        <muses-search-form ref="searchForm" :model="filters" label-position="right" label-width="0" :offset="0" :item-width="140">
          <muses-search-form-item label="" prop="search-roleName">
            <el-input id="search-roleName" v-model="filters.roleName" clearable placeholder="角色名称" @keyup.enter.native="search" />
          </muses-search-form-item>
          <muses-search-form-item label="" prop="search-roleCode">
            <el-input id="search-roleCode" v-model="filters.roleCode" clearable placeholder="角色编码" @keyup.enter.native="search" />
          </muses-search-form-item>
          <muses-search-form-item label="" prop="search-enabled">
            <el-select id="search-enabled" v-model="filters.enabled" placeholder="角色状态" clearable @change="search">
              <el-option v-for="item in enabledList" :key="item.value" :label="item.name" :value="item.value" />
            </el-select>
          </muses-search-form-item>
          <muses-search-form-item btn btn-open-name="" btn-close-name="">
            <el-button type="success" @click="search()">查 询</el-button>
            <el-button type="warning" @click="resetSearch()">重 置</el-button>
          </muses-search-form-item>
        </muses-search-form>
      </div>
      <div class="crud-opts">
        <span class="crud-opts-left">
          <el-button v-perm="['ROLEA001']" type="success" @click="addRole">新增</el-button>
          <el-button v-perm="['ROLEQ003']" :disabled="row === null" type="info" @click="showDetail">详情</el-button>
          <el-button :disabled="row === null" type="primary" @click="showRoleUser">用户</el-button>
          <el-button v-perm="['ROLEU001']" :disabled="row === null" type="primary" @click="editRole">编辑</el-button>
          <el-button v-perm="['ROLEU002']" :disabled="row === null" type="warning" @click="authRole">授权</el-button>
          <el-button v-perm="['ROLED001']" :disabled="row === null" type="danger" @click="deleteRole">删除</el-button>
        </span>
      </div>
    </div>
    <div class="components-container">
      <el-table
        ref="roleTable"
        v-loading="tableLoading"
        :data="tableData"
        :header-cell-style="{background:'#FAFAFA'}"
        highlight-current-row
        row-key="id"
        border
        @current-change="handleCurrentChange"
      >
        <el-table-column :show-overflow-tooltip="true" prop="roleName" label="角色名称" align="center" width="180px" />
        <el-table-column :show-overflow-tooltip="true" prop="roleCode" label="角色编码" align="center" width="180px" />
        <el-table-column :show-overflow-tooltip="true" prop="roleDesc" label="角色描述" align="center" />
        <el-table-column prop="enabled" label="角色状态" align="center" width="120px">
          <template v-slot="scope">
            <el-tag v-if="scope.row.enabled">启用</el-tag>
            <el-tag v-else type="warning">禁用</el-tag>
          </template>
        </el-table-column>
        <el-table-column :show-overflow-tooltip="true" prop="createUsername" label="创建用户" align="center" width="120px" />
        <el-table-column :show-overflow-tooltip="true" prop="createTime" label="创建时间" align="center" width="160px" />
        <el-table-column :show-overflow-tooltip="true" prop="updateUsername" label="更新用户" align="center" width="120px" />
        <el-table-column :show-overflow-tooltip="true" prop="updateTime" label="更新时间" align="center" width="160px" />
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
    <!-- 授权 -->
    <authRoleDialog
      ref="authRoleDialogRef"
      :dialog-visible="authRoleDialogVisible"
      :dialog-title="authRoleDialogTitle"
      @authRoleDone="authRoleDone"
    />
  </div>
</template>

<script>
import detailDialog from '@/views/admin/system/role/template/detailDialog'
import addOrEditDialog from '@/views/admin/system/role/template/addOrEditDialog'
import authRoleDialog from '@/views/admin/system/role/template/authRoleDialog'
import nodataImg from '@/assets/images/nodata.png'
export default {
  name: 'Role',
  components: {
    detailDialog,
    addOrEditDialog,
    authRoleDialog
  },
  data() {
    return {
      filters: {
        roleName: null,
        roleCode: null,
        enabled: null
      },
      enabledList: [],
      tableData: [],
      tableLoading: false,
      nodataImg: nodataImg,
      row: null,
      total: 0,
      page: 1,
      pageSize: 10,
      detailDialogVisible: false,
      addOrEditDialogTitle: null,
      addOrEditDialogVisible: false,
      authRoleDialogTitle: null,
      authRoleDialogVisible: false
    }
  },
  mounted() {
    this.loadEnabledList()
    this.listTableData()
  },
  methods: {
    handleCurrentChange(row) {
      this.row = row
    },
    loadEnabledList() {
      this.$mapi.communal.queryConfListByDictKey({ dictKey: 'ENABLED_CONFIG' }).then(res => {
        const { data } = res
        this.enabledList = data
      }).catch(_ => {
        this.enabledList = []
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
      this.$mapi.role.pageRoleList(param).then(res => {
        const { data } = res
        this.total = data.total
        this.tableData = data.records
      }).catch(_ => {
        this.total = 0
        this.tableData = []
      }).finally(() => {
        this.tableLoading = false
        this.row = null
        this.$refs.roleTable.setCurrentRow()
      })
    },
    // 详情
    showDetail() {
      if (this.row === null) {
        this.$message.error('请选择要查看的角色')
        return
      }
      this.detailDialogVisible = true
      this.$refs.detailDialogRef.initData(this.row.id)
    },
    showDetailDone() {
      this.detailDialogVisible = false
    },
    // 新增与编辑
    addRole() {
      this.addOrEditDialogVisible = true
      this.addOrEditDialogTitle = '新增角色'
      this.$refs.addOrEditDialogRef.initData()
    },
    editRole() {
      if (this.row === null) {
        this.$message.error('请选择要编辑的角色')
        return
      }
      this.addOrEditDialogVisible = true
      this.addOrEditDialogTitle = '编辑角色'
      this.$refs.addOrEditDialogRef.initData(this.row.id)
    },
    addOrEditDone(result = false) {
      this.addOrEditDialogTitle = null
      this.addOrEditDialogVisible = false
      if (result) {
        this.listTableData()
      }
    },
    // 授权
    authRole() {
      if (this.row === null) {
        this.$message.error('请选择要授权的角色')
        return
      }
      this.authRoleDialogVisible = true
      this.authRoleDialogTitle = '角色授权 - ' + this.row.roleName
      this.$refs.authRoleDialogRef.initData(this.row.id)
    },
    authRoleDone() {
      this.authRoleDialogTitle = null
      this.authRoleDialogVisible = false
    },
    // 用户
    showRoleUser() {
      this.$router.push({
        name: 'UserSearch',
        query: { businessId: this.row.id, roleName: this.row.roleName, type: 'role' }
      })
    },
    // 删除
    deleteRole() {
      if (this.row === null) {
        this.$message.error('请选择要删除的角色')
        return
      }
      this.$confirm('此操作将永久删除角色【' + this.row.roleName + '】, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.$mapi.role.deleteRole({ roleId: this.row.id }).then(res => {
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
