<template>
  <div class="app-container">
    <div class="head-container">
      <div>
        <el-form ref="searchForm" :inline="true" size="small">
          <el-form-item label="模糊搜索">
            <el-input v-model="filters.blurry" clearable placeholder="模糊搜索" style="width: 200px" class="filter-item" @keyup.enter.native="search" />
          </el-form-item>
          <el-form-item label="状态">
            <el-select v-model="filters.enabled" clearable placeholder="状态" style="width: 90px" class="filter-item">
              <el-option label="启用" value="true" />
              <el-option label="禁用" value="false" />
            </el-select>
          </el-form-item>
          <el-form-item label="仅显示菜单">
            <el-switch v-model="filters.onlyShowMenu" @change="search" />
          </el-form-item>
          <el-form-item>
            <el-button class="filter-item" size="small" type="success" icon="el-icon-search" @click="search">查询</el-button>
            <el-button class="filter-item" size="small" type="warning" icon="el-icon-refresh-left" @click="resetSearch">重置</el-button>
          </el-form-item>
        </el-form>
      </div>
      <div>
        <el-button v-perm="['addPerm']" size="small" type="primary" @click="addPerm()">
          新增
        </el-button>
      </div>
    </div>

    <div class="components-container">
      <el-table v-loading="tableLoading" :data="tableData" row-key="id" :tree-props="tableProps" border>
        <el-table-column :show-overflow-tooltip="true" label="菜单标题" width="200px" prop="permName">
          <template v-slot="scope">
            <span>
              <svg-icon :icon-class="scope.row.icon ? scope.row.icon : ''" />
              {{ scope.row['permName'] }}
            </span>
          </template>
        </el-table-column>
        <el-table-column :show-overflow-tooltip="true" width="100px" prop="routerPath" label="组件路径" />
        <el-table-column :show-overflow-tooltip="true" width="150px" prop="permCode" label="权限编码" />
        <el-table-column :show-overflow-tooltip="true" width="150px" prop="componentName" label="组件名称" />
        <el-table-column :show-overflow-tooltip="true" prop="componentPath" label="组件路径" />
        <el-table-column prop="permType" label="类型" align="center" width="75px">
          <template v-slot="scope">
            <el-tag v-if="scope.row['permType'] === 1" size="small">目录</el-tag>
            <el-tag v-else-if="scope.row['permType'] === 2" size="small" type="success">菜单</el-tag>
            <el-tag v-else-if="scope.row['permType'] === 3" size="small" type="info">按钮</el-tag>
            <span v-else> - </span>
          </template>
        </el-table-column>
        <el-table-column prop="permLevel" label="级别" align="center" width="75px">
          <template v-slot="scope">
            <el-tag v-if="scope.row['permLevel'] === 1" size="small" type="warning">公开</el-tag>
            <el-tag v-else-if="scope.row['permLevel'] === 2" size="small" type="info">认证</el-tag>
            <el-tag v-else-if="scope.row['permLevel'] === 3" size="small" type="success">授权</el-tag>
            <span v-else> - </span>
          </template>
        </el-table-column>
        <el-table-column prop="enabled" label="状态" width="75px">
          <template v-slot="scope">
            <el-tag v-if="scope.row.enabled" size="small">启用</el-tag>
            <el-tag v-else size="small" type="warning">禁用</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="menuSort" label="排序" align="center" width="75px">
          <template v-slot="scope">
            {{ scope.row.sort }}
          </template>
        </el-table-column>
        <el-table-column label="操作" align="center" width="200">
          <template v-slot="scope">
            <el-button-group>
              <el-button type="info" size="mini" @click="showPermDetail(scope.row)">详情</el-button>
              <el-button v-perm="['editPerm']" type="success" size="mini" @click="editPerm(scope.row)">编辑</el-button>
              <el-popconfirm v-perm="['deletePerm']" title="操作不可撤销，确定删除吗？" @confirm="deletePerm(scope.row)">
                <el-button v-show="allowDelete(scope.row)" slot="reference" type="danger" size="mini">删除</el-button>
              </el-popconfirm>
            </el-button-group>
          </template>
        </el-table-column>
        <div slot="empty">
          <el-empty />
        </div>
      </el-table>
    </div>

    <!-- 新增 / 编辑 -->
    <addOrEditDialog
      ref="addOrEditDialogRef"
      :dialog-visible="addOrEditDialogVisible"
      :dialog-title="addOrEditDialogTitle"
      :data-id="editId"
      @addOrEditDone="addOrEditDone"
    />
    <!-- 查看详情 -->
    <showDetailDialog
      ref="showDetailDialogRef"
      :dialog-visible="showDetailDialogVisible"
      :data-id="detailId"
      @showDetailDone="showDetailDone"
    />
  </div>
</template>

<script>
import addOrEditDialog from '@/views/admin/system/perm/template/addOrEditDialog'
import showDetailDialog from '@/views/admin/system/perm/template/showDetailDialog'
export default {
  name: 'Perm',
  components: {
    addOrEditDialog,
    showDetailDialog
  },
  data() {
    return {
      filters: {
        blurry: '',
        enabled: '',
        onlyShowMenu: false
      },
      tableData: [],
      tableProps: {
        children: 'children',
        hasChildren: 'hasChildren'
      },
      tableLoading: false,
      editId: null,
      addOrEditDialogVisible: false,
      addOrEditDialogTitle: '',
      detailId: null,
      showDetailDialogVisible: false
    }
  },
  mounted() {
    this.listTableData()
  },
  methods: {
    search() {
      this.listTableData()
    },
    resetSearch() {
      this.filters.blurry = ''
      this.filters.enabled = ''
      this.filters.onlyShowMenu = false
      this.listTableData()
    },
    listTableData() {
      const param = { ... this.filters }
      this.tableLoading = true
      this.$mapi.perm.queryPermTreeList(param).then(res => {
        this.tableData = res.data
      }).catch(_ => {
        this.tableData = []
      }).finally(() => {
        this.tableLoading = false
      })
    },
    allowDelete(row) {
      return !row.children || !row.children.length || row.children.length === 0
    },
    showPermDetail(row) {
      this.detailId = row.id.toString()
      this.showDetailDialogVisible = true
      this.$refs.showDetailDialogRef.initData()
    },
    addPerm() {
      this.editId = null
      this.addOrEditDialogVisible = true
      this.addOrEditDialogTitle = '新增权限'
      this.$refs.addOrEditDialogRef.initData()
    },
    editPerm(row) {
      this.editId = row.id.toString()
      this.addOrEditDialogVisible = true
      this.addOrEditDialogTitle = '编辑权限'
      this.$refs.addOrEditDialogRef.initData()
    },
    deletePerm(row) {
      this.$mapi.perm.deletePerm({ permId: row.id }).then(res => {
        this.$message.success(res.message)
        this.listTableData()
      })
    },
    addOrEditDone(result = false) {
      this.editId = null
      this.addOrEditDialogVisible = false
      this.addOrEditDialogTitle = ''
      if (result) {
        this.listTableData()
      }
    },
    showDetailDone() {
      this.detailId = null
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
