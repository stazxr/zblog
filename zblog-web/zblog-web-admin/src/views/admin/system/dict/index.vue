<template>
  <div class="app-container">
    <div class="head-container">
      <div>
        <el-input v-model="filters.dictName" clearable size="small" placeholder="字典名称（组）" style="width: 200px;" class="filter-item" @keyup.enter.native="search" />
        <span>
          <el-button class="filter-item" size="small" type="success" icon="el-icon-search" @click="search">查询</el-button>
          <el-button class="filter-item" size="small" type="warning" icon="el-icon-refresh-left" @click="resetSearch">重置</el-button>
        </span>
      </div>
      <div class="crud-opts">
        <span class="crud-opts-left">
          <el-button v-perm="['addDict']" size="small" type="primary" @click="addDictGroup()">
            新增组
          </el-button>
        </span>
      </div>
    </div>

    <div class="components-container">
      <el-table ref="table" v-loading="tableLoading" :data="tableData" row-key="id" lazy :load="loadSubList" :tree-props="tableProps" border>
        <el-table-column :show-overflow-tooltip="true" prop="name" label="字典名称" align="left" />
        <el-table-column :show-overflow-tooltip="true" prop="key" label="字典KEY" align="left" />
        <el-table-column :show-overflow-tooltip="true" prop="value" label="字典VALUE" align="left" />
        <el-table-column :show-overflow-tooltip="true" prop="sort" label="字典排序" align="left" width="80" />
        <el-table-column :show-overflow-tooltip="true" prop="locked" label="锁定状态" align="center" width="120">
          <template v-slot="scope">
            <span v-if="scope.row.locked">禁止编辑删除</span>
            <span v-else>允许编辑删除</span>
          </template>
        </el-table-column>
        <el-table-column :show-overflow-tooltip="true" prop="enabled" label="状态" align="center" width="100">
          <template v-slot="scope">
            <el-tag v-if="scope.row.enabled" size="small">启用</el-tag>
            <el-tag v-else size="small" type="warning">禁用</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" align="center" width="250px">
          <template v-slot="scope">
            <el-button-group>
              <el-button type="info" size="mini" @click="showDictDetail(scope.row)">详情</el-button>
              <el-button v-if="hasPerm(['addDict']) && scope.row.type === 1" type="primary" size="mini" @click="addDict(scope.row)">新增子项</el-button>
              <el-button v-if="hasPerm(['editDict']) && !scope.row.locked" type="success" size="mini" @click="editDict(scope.row)">编辑</el-button>
              <el-popconfirm v-if="hasPerm(['deleteDict']) && !scope.row.locked && !scope.row.hasChildren" title="操作不可撤销，确定删除吗？" @confirm="deleteDict(scope.row)">
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
      :data-id="rowId"
      @addOrEditDone="addOrEditDone"
    />
    <!-- 查看详情 -->
    <showDetailDialog
      ref="showDetailDialogRef"
      :dialog-visible="showDetailDialogVisible"
      :data-id="rowId"
      @showDetailDone="showDetailDone"
    />
  </div>
</template>

<script>
import addOrEditDialog from '@/views/admin/system/dict/template/addOrEditDialog'
import showDetailDialog from '@/views/admin/system/dict/template/showDetailDialog'
export default {
  name: 'Dict',
  components: {
    addOrEditDialog,
    showDetailDialog
  },
  data() {
    return {
      filters: {
        dictName: ''
      },
      tableData: [],
      tableProps: {
        children: 'children',
        hasChildren: 'hasChildren'
      },
      maps: new Map(),
      rowId: '',
      rowPid: '',
      tableLoading: false,
      total: 0,
      page: 1,
      pageSize: 10,
      addOrEditDialogTitle: '',
      addOrEditDialogVisible: false,
      showDetailDialogVisible: false
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
      this.filters.dictName = ''

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
      this.$mapi.dict.pageDictList(param).then(res => {
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
    sizeChange(size) {
      this.page = 1
      this.pageSize = size
      this.listTableData()
    },
    pageChange(page) {
      this.page = page
      this.listTableData()
    },
    loadSubList(tree, treeNode, resolve) {
      this.maps.set(tree.id, { tree, treeNode, resolve })
      setTimeout(() => {
        this.$mapi.dict.queryChildList({ pid: tree.id }).then(res => {
          resolve(res.data)
        }).catch(_ => {
          resolve([])
        })
      }, 1)
    },
    addDictGroup() {
      this.rowId = null
      this.rowPid = null
      this.addOrEditDialogVisible = true
      this.addOrEditDialogTitle = '新增字典组'
      this.$refs.addOrEditDialogRef.initData(1)
    },
    addDict(row) {
      this.rowId = null
      this.rowPid = row.id.toString()
      this.addOrEditDialogVisible = true
      this.addOrEditDialogTitle = '新增字典项'
      this.$refs.addOrEditDialogRef.initData(2, row.id.toString())
    },
    editDict(row) {
      this.rowId = row.id.toString()
      this.rowPid = row.pid ? row.pid.toString() : row.pid
      this.addOrEditDialogVisible = true
      this.addOrEditDialogTitle = row.type === 1 ? '编辑字典组' : '编辑字典项'
      this.$refs.addOrEditDialogRef.initData(row.type, this.rowPid)
    },
    deleteDict(row) {
      this.$mapi.dict.deleteDict({ dictId: row.id }).then(res => {
        this.$message.success(res.message)

        if (row.pid) {
          const { tree, treeNode, resolve } = this.maps.get(row.pid)
          this.$set(this.$refs.table.store.states.lazyTreeNodeMap, row.pid, [])
          this.loadSubList(tree, treeNode, resolve)
        } else {
          this.listTableData()
        }
      })
    },
    addOrEditDone(result = false) {
      if (result) {
        const curId = this.rowId
        if (curId && this.maps.get(curId)) {
          const { tree, treeNode, resolve } = this.maps.get(curId)
          this.loadSubList(tree, treeNode, resolve)
        }

        const pid = this.rowPid
        if (pid && this.maps.get(pid)) {
          const { tree, treeNode, resolve } = this.maps.get(pid)
          this.loadSubList(tree, treeNode, resolve)
        }

        if (!this.maps.get(curId) && !this.maps.get(pid)) {
          this.listTableData()
        }
      }

      this.rowId = null
      this.rowPid = null
      this.addOrEditDialogTitle = ''
      this.addOrEditDialogVisible = false
    },
    showDictDetail(row) {
      this.rowId = row.id.toString()
      this.showDetailDialogVisible = true
      this.$refs.showDetailDialogRef.initData()
    },
    showDetailDone() {
      this.rowId = null
      this.showDetailDialogVisible = false
    },
    hasPerm(value) {
      return this.checkPerm(value)
    }
  }
}
</script>

<style scoped>

</style>
