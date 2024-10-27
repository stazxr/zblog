<template>
  <div>
    <div class="head-container" style="text-align: right;">
      <el-form ref="userSearchForm" :inline="true" size="small" class="clearfix" style="text-align: left">
        <el-input v-model="filters.blurry" clearable placeholder="角色名称/角色编码" style="width: 200px" class="filter-item" @keyup.enter.native="search" />
        <el-form-item style="float: right">
          <el-button
            v-perm="['batchDeleteRolePerm']"
            size="small"
            type="danger"
            :loading="deleteLoading"
            :disabled="selectRows.length === 0"
            @click="batchDeletePermRole()"
          >批量删除</el-button>
        </el-form-item>
      </el-form>
    </div>
    <div class="components-container">
      <el-table v-loading="tableLoading" :data="tableData" border style="width: 100%;" @selection-change="handleSelectionChange">
        <el-table-column type="selection" width="55" />
        <el-table-column :show-overflow-tooltip="true" prop="roleName" label="角色名称" />
        <el-table-column :show-overflow-tooltip="true" prop="roleCode" label="角色编码" />
        <el-table-column :show-overflow-tooltip="true" prop="desc" label="角色描述" />
        <el-table-column prop="enabled" label="角色状态" align="center" width="75px">
          <template slot-scope="scope">
            <el-tag v-if="scope.row.enabled" size="small">启用</el-tag>
            <el-tag v-else size="small" type="warning">禁用</el-tag>
          </template>
        </el-table-column>
        <el-table-column v-if="hasPerm(['batchDeleteRolePerm'])" label="操作" align="center" width="150px">
          <template slot-scope="scope">
            <el-button-group>
              <el-popconfirm v-perm="['batchDeleteRolePerm']" title="操作不可撤销，确定删除吗？" @confirm="deleteRolePerm(scope.row)">
                <el-button slot="reference" type="danger" size="mini">删除</el-button>
              </el-popconfirm>
            </el-button-group>
          </template>
        </el-table-column>
      </el-table>
    </div>

    <div class="pagination-container">
      <el-pagination
        :total="total"
        :current-page="page"
        :page-size="pageSize"
        :page-sizes="[5, 10, 20, 50]"
        layout="total, prev, pager, next, sizes"
        @size-change="sizeChange"
        @current-change="pageChange"
      />
    </div>
  </div>
</template>

<script>
export default {
  name: 'PermRoleList',
  data() {
    return {
      filters: {
        blurry: ''
      },
      permId: '',
      tableData: [],
      selectRows: [],
      tableLoading: false,
      deleteLoading: false,
      total: 0,
      page: 1,
      pageSize: 5
    }
  },
  methods: {
    initData(permId) {
      this.filters.blurry = ''
      this.permId = permId
      this.$nextTick(() => {
        this.listTableData()
      })
    },
    search() {
      this.page = 1
      this.listTableData()
    },
    listTableData() {
      const param = {
        ... this.filters,
        permId: this.permId,
        page: this.page,
        pageSize: this.pageSize
      }
      this.tableLoading = true
      this.$mapi.perm.queryPermRoles(param).then(res => {
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
    handleSelectionChange(val) {
      this.selectRows = val
    },
    batchDeletePermRole() {
      if (this.selectRows.length === 0) {
        this.$message.error('请选择需要删除的记录')
      }

      this.$confirm('此操作将永久删除选中记录, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        const roleIds = []
        this.selectRows.forEach(row => {
          roleIds.push(row.id)
        })
        const param = {
          permId: this.permId,
          roleIds: roleIds
        }

        this.deleteLoading = true
        this.$mapi.perm.batchDeleteRolePerm(param).then(res => {
          this.$message.success(res.message)
          this.listTableData()
        }).finally(_ => {
          this.deleteLoading = false
        })
      })
    },
    deleteRolePerm(row) {
      const roleIds = []
      roleIds.push(row.id)
      const param = {
        permId: this.permId,
        roleIds: roleIds
      }
      this.$mapi.perm.batchDeleteRolePerm(param).then(res => {
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

<style scoped>

</style>
