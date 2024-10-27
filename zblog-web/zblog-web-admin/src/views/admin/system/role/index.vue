<template>
  <div class="app-container">
    <div class="head-container">
      <div>
        <el-input v-model="filters.roleName" clearable placeholder="角色名称" style="width: 200px" class="filter-item" @keyup.enter.native="search" />
        <el-input v-model="filters.roleCode" clearable placeholder="角色编码" style="width: 200px" class="filter-item" @keyup.enter.native="search" />
        <el-select v-model="filters.enabled" clearable placeholder="角色状态" style="width: 120px" class="filter-item">
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
          <el-button v-perm="['addRole']" size="small" type="primary" @click="addRole()">
            新增
          </el-button>
        </span>
      </div>
    </div>

    <div class="components-container">
      <el-table v-loading="tableLoading" :data="tableData" border style="width: 100%">
        <el-table-column :show-overflow-tooltip="true" prop="roleName" label="角色名称" align="center" />
        <el-table-column :show-overflow-tooltip="true" prop="roleCode" label="角色编码" align="center" />
        <el-table-column :show-overflow-tooltip="true" prop="desc" label="角色描述" align="center" />
        <el-table-column :show-overflow-tooltip="true" prop="enabled" label="角色状态" align="center">
          <template slot-scope="scope">
            <el-tag v-if="scope.row.enabled" size="small">正常</el-tag>
            <el-tag v-else size="small" type="warning">禁用</el-tag>
          </template>
        </el-table-column>
        <el-table-column :show-overflow-tooltip="true" prop="createTime" label="创建时间" align="center" />
        <el-table-column label="操作" align="center" width="250px">
          <template slot-scope="scope">
            <el-button-group>
              <el-button type="info" size="mini" @click="showRoleDetail(scope.row)">详情</el-button>
              <el-button v-perm="['editRole']" type="success" size="mini" @click="editRole(scope.row)">编辑</el-button>
              <el-button v-perm="['pageUsersByRoleId']" type="primary" size="mini" @click="showRoleUser(scope.row)">用户</el-button>
              <el-button v-perm="['authRole']" type="warning" size="mini" @click="authRole(scope.row)">授权</el-button>
              <el-popconfirm v-perm="['deleteRole']" title="操作不可撤销，确定删除吗？" @confirm="deleteRole(scope.row)">
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
      :data-id="dataId"
      @addOrEditDone="addOrEditDone"
    />
    <!-- 查看详情 -->
    <showDetailDialog
      ref="showDetailDialogRef"
      :dialog-visible="showDetailDialogVisible"
      :data-id="dataId"
      @showDetailDone="showDetailDone"
    />
    <!-- 角色授权 -->
    <authRoleDialog
      ref="authRoleDialogRef"
      :dialog-visible="authRoleDialogVisible"
      :dialog-title="authRoleDialogTitle"
      :data-id="dataId"
      @authRoleDone="authRoleDone"
    />
  </div>
</template>

<script>
import addOrEditDialog from '@/views/admin/system/role/template/addOrEditDialog'
import showDetailDialog from '@/views/admin/system/role/template/showDetailDialog'
import authRoleDialog from '@/views/admin/system/role/template/authRoleDialog'
export default {
  name: 'Role',
  components: {
    addOrEditDialog,
    showDetailDialog,
    authRoleDialog
  },
  data() {
    return {
      filters: {
        roleName: '',
        roleCode: '',
        enabled: ''
      },
      tableData: [],
      tableLoading: false,
      total: 0,
      page: 1,
      pageSize: 10,
      dataId: null,
      addOrEditDialogTitle: '',
      addOrEditDialogVisible: false,
      showDetailDialogVisible: false,
      authRoleDialogTitle: '',
      authRoleDialogVisible: false
    }
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
      this.filters.roleName = ''
      this.filters.roleCode = ''
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
      this.$mapi.role.pageRoleList(param).then(res => {
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
    addRole() {
      this.dataId = null
      this.addOrEditDialogVisible = true
      this.addOrEditDialogTitle = '新增角色'
      this.$refs.addOrEditDialogRef.initData()
    },
    showRoleDetail(row) {
      this.dataId = row.id.toString()
      this.showDetailDialogVisible = true
      this.$refs.showDetailDialogRef.initData()
    },
    editRole(row) {
      this.dataId = row.id.toString()
      this.addOrEditDialogVisible = true
      this.addOrEditDialogTitle = '编辑角色'
      this.$refs.addOrEditDialogRef.initData()
    },
    showRoleUser(row) {
      this.$router.push({
        name: 'UserSearch',
        query: { businessId: row.id, roleName: row.roleName, type: 'role' }
      })
    },
    authRole(row) {
      this.dataId = row.id.toString()
      this.authRoleDialogTitle = '角色授权 - ' + row.roleName
      this.authRoleDialogVisible = true
    },
    deleteRole(row) {
      this.$mapi.role.deleteRole({ roleId: row.id }).then(res => {
        this.$message.success(res.message)
        this.listTableData()
      })
    },
    addOrEditDone(result = false) {
      this.dataId = null
      this.addOrEditDialogVisible = false
      this.addOrEditDialogTitle = ''
      if (result) {
        this.listTableData()
      }
    },
    showDetailDone() {
      this.dataId = null
      this.showDetailDialogVisible = false
    },
    authRoleDone() {
      this.dataId = null
      this.authRoleDialogTitle = ''
      this.authRoleDialogVisible = false
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
