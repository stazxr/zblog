<template>
  <div class="app-container">
    <div class="head-container">
      <div>
        <el-input v-model="filters.name" clearable placeholder="节点名称" style="width: 150px" class="filter-item" @keyup.enter.native="search" />
        <el-input v-model="filters.ip" clearable placeholder="节点 IP" style="width: 150px" class="filter-item" @keyup.enter.native="search" />
        <span>
          <el-button class="filter-item" size="small" type="success" icon="el-icon-search" @click="search">查询</el-button>
          <el-button class="filter-item" size="small" type="warning" icon="el-icon-refresh-left" @click="resetSearch">重置</el-button>
        </span>
      </div>
      <div class="crud-opts">
        <span class="crud-opts-left">
          <el-button v-perm="['addNode']" size="small" type="primary" @click="addNode()">
            新增
          </el-button>
        </span>
      </div>
    </div>

    <div class="components-container">
      <el-table v-loading="tableLoading" :data="tableData" border style="width: 100%">
        <el-table-column :show-overflow-tooltip="true" prop="name" label="节点名称" width="200" />
        <el-table-column :show-overflow-tooltip="true" prop="name" label="链接信息" width="200">
          <template slot-scope="scope">
            <span>{{ scope.row.sshUser }}@{{ scope.row.ip }}:{{ scope.row.port }}</span>
          </template>
        </el-table-column>
        <el-table-column :show-overflow-tooltip="true" prop="desc" label="描述信息" />
        <el-table-column :show-overflow-tooltip="true" prop="createUser" label="创建用户" width="150" />
        <el-table-column :show-overflow-tooltip="true" prop="createTime" label="创建时间" width="150" />
        <el-table-column v-if="hasPerm(['queryNodeDetail', 'editNode', 'deleteNode'])" label="操作" align="center" width="200px">
          <template slot-scope="scope">
            <el-button-group>
              <el-button v-perm="['queryNodeDetail']" type="info" size="mini" @click="editNode(scope.row)">详情</el-button>
              <el-button v-perm="['editNode']" type="success" size="mini" @click="editNode(scope.row)">编辑</el-button>
              <el-button v-perm="['queryNodeDetail']" type="primary" size="mini" @click="toConsole(scope.row)">控制台</el-button>
              <el-popconfirm v-perm="['deleteNode']" title="操作不可撤销，确定删除吗？" @confirm="deleteNode(scope.row)">
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
  </div>
</template>

<script>
import addOrEditDialog from '@/views/admin/mnt/node/template/addOrEditDialog'
export default {
  name: 'Node',
  components: {
    addOrEditDialog
  },
  data() {
    return {
      filters: {
        name: '',
        ip: ''
      },
      tableData: [],
      tableLoading: false,
      total: 0,
      page: 1,
      pageSize: 10,
      dataId: null,
      addOrEditDialogTitle: '',
      addOrEditDialogVisible: false
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
      this.filters.name = ''
      this.filters.ip = ''

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
      this.$mapi.node.pageNodeList(param).then(res => {
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
    addNode() {
      this.addOrEditDialogVisible = true
      this.addOrEditDialogTitle = '新增节点'
      this.$refs.addOrEditDialogRef.initData()
    },
    editNode(row) {
      this.addOrEditDialogVisible = true
      this.addOrEditDialogTitle = '编辑节点'
      this.$refs.addOrEditDialogRef.initData(row.id)
    },
    addOrEditDone(result = false) {
      this.addOrEditDialogVisible = false
      this.addOrEditDialogTitle = ''
      if (result) {
        this.listTableData()
      }
    },
    toConsole(row) {
      const routeData = this.$router.resolve({
        name: 'XtermPage',
        query: { nid: row.id }
      })
      window.open(routeData.href, '_blank')
    },
    deleteNode(row) {
      this.$mapi.node.deleteNode({ nodeId: row.id }).then(res => {
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
