<template>
  <div class="app-container">
    <div class="head-container">
      <div class="search-opts">
        <muses-search-form ref="searchForm" :model="filters" label-position="right" label-width="0" :offset="0" :item-width="160">
          <muses-search-form-item label="" prop="search-blurry">
            <el-input
              id="search-blurry"
              v-model="filters.blurry"
              placeholder="模糊搜索"
              clearable
              @keyup.enter.native="search"
            />
          </muses-search-form-item>
          <muses-search-form-item label="" prop="search-enabled">
            <el-select id="search-enabled" v-model="filters.enabled" placeholder="权限状态" clearable>
              <el-option label="启用" :value="true" />
              <el-option label="禁用" :value="false" />
            </el-select>
          </muses-search-form-item>
          <muses-search-form-item label="" prop="search-onlyShowMenu">
            <el-select id="search-onlyShowMenu" v-model="filters.onlyShowMenu" placeholder="是否只显示菜单" clearable>
              <el-option label="是" :value="true" />
              <el-option label="否" :value="false" />
            </el-select>
          </muses-search-form-item>
          <muses-search-form-item btn btn-open-name="" btn-close-name="">
            <el-button type="success" icon="el-icon-search" @click="search()">查询</el-button>
            <el-button type="warning" icon="el-icon-refresh-right" @click="resetSearch()">重置</el-button>
          </muses-search-form-item>
        </muses-search-form>
      </div>
      <div class="crud-opts">
        <span class="crud-opts-left">
          <el-button v-perm="['PERMA001']" type="success" @click="addPerm">新增</el-button>
          <el-button v-perm="['PERMQ002']" :disabled="row === null" type="info" @click="showDetail">详情</el-button>
          <el-button v-perm="['PERMU001']" :disabled="row === null" type="primary" @click="editPerm">编辑</el-button>
          <el-button v-perm="['PERMD001']" :disabled="row === null" type="danger" @click="deletePerm">删除</el-button>
        </span>
      </div>
    </div>
    <div class="components-container">
      <el-table
        ref="permissionTable"
        v-loading="tableLoading"
        :data="tableData"
        :header-cell-style="{background:'#FAFAFA'}"
        highlight-current-row
        row-key="id"
        :tree-props="tableProps"
        border
        @current-change="handleCurrentChange"
      >
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
        <el-table-column :show-overflow-tooltip="true" prop="routerPath" label="路由地址" />
        <el-table-column :show-overflow-tooltip="true" prop="componentName" label="组件名称" />
        <el-table-column :show-overflow-tooltip="true" width="250px" prop="componentPath" label="组件路径" />
        <el-table-column prop="cacheable" label="是否缓存" align="center" width="80px">
          <template v-slot="scope">
            <el-tag v-if="scope.row['cacheable'] === true" type="info">是</el-tag>
            <el-tag v-else-if="scope.row['cacheable'] === false" type="info">否</el-tag>
            <span v-else />
          </template>
        </el-table-column>
        <el-table-column prop="permType" label="类型" align="center" width="80px">
          <template v-slot="scope">
            <el-tag v-if="scope.row['permType'] === 1">目录</el-tag>
            <el-tag v-else-if="scope.row['permType'] === 2" type="success">菜单</el-tag>
            <el-tag v-else-if="scope.row['permType'] === 3" type="info">按钮</el-tag>
            <el-tag v-else-if="scope.row['permType'] === 4" type="warning">外链</el-tag>
            <span v-else> - </span>
          </template>
        </el-table-column>
        <el-table-column prop="permLevel" label="访问级别" align="center" width="80px">
          <template v-slot="scope">
            <el-tag v-if="scope.row['permLevel'] === 1" type="warning">公开</el-tag>
            <el-tag v-else-if="scope.row['permLevel'] === 2" type="success">认证</el-tag>
            <el-tag v-else-if="scope.row['permLevel'] === 4">授权</el-tag>
            <span v-else> - </span>
          </template>
        </el-table-column>
        <el-table-column prop="enabled" label="状态" width="80px">
          <template v-slot="scope">
            <el-tag v-if="scope.row.enabled">启用</el-tag>
            <el-tag v-else type="warning">禁用</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="menuSort" label="排序" align="center" width="80px">
          <template v-slot="scope">
            {{ scope.row.sort }}
          </template>
        </el-table-column>
        <div slot="empty">
          <el-empty />
        </div>
      </el-table>
    </div>

    <!-- 查看详情 -->
    <detailDialog
      ref="detailDialogRef"
      :dialog-visible="detailDialogVisible"
      :data-id="row && row.id"
      @showDetailDone="showDetailDone"
    />
    <!-- 新增 / 编辑 -->
    <addOrEditDialog
      ref="addOrEditDialogRef"
      :dialog-visible="addOrEditDialogVisible"
      :dialog-title="addOrEditDialogTitle"
      :data-id="dataId"
      @addOrEditDone="addOrEditDone"
    />
  </div>
</template>

<script>
import detailDialog from '@/views/admin/system/perm/template/detailDialog'
import addOrEditDialog from '@/views/admin/system/perm/template/addOrEditDialog'
export default {
  name: 'Perm',
  components: {
    detailDialog,
    addOrEditDialog
  },
  data() {
    return {
      filters: {
        blurry: '',
        enabled: null,
        onlyShowMenu: null
      },
      tableData: [],
      tableProps: {
        children: 'children',
        hasChildren: 'hasChildren'
      },
      tableLoading: false,
      row: null,
      detailDialogVisible: false,
      addOrEditDialogVisible: false,
      addOrEditDialogTitle: '',
      dataId: null
    }
  },
  mounted() {
    this.listTableData()
  },
  methods: {
    handleCurrentChange(row) {
      this.row = row
    },
    // 查询
    search() {
      this.listTableData()
    },
    resetSearch() {
      this.filters.blurry = ''
      this.filters.enabled = null
      this.filters.onlyShowMenu = null
      this.listTableData()
    },
    listTableData() {
      const param = { ... this.filters }
      this.tableLoading = true
      this.$mapi.perm.queryPermTree(param).then(res => {
        this.tableData = res.data
      }).catch(_ => {
        this.tableData = []
      }).finally(() => {
        this.tableLoading = false
        this.row = null
        this.$refs.permissionTable.setCurrentRow()
      })
    },
    // 查看详情
    showDetail() {
      if (this.row === null) {
        this.$message.error('请选择要查看的权限')
        return
      }
      this.detailDialogVisible = true
      this.$refs.detailDialogRef.initData()
    },
    showDetailDone() {
      this.detailDialogVisible = false
    },
    // 新增与编辑
    addPerm() {
      this.dataId = null
      this.addOrEditDialogVisible = true
      this.addOrEditDialogTitle = '新增权限'
      this.$refs.addOrEditDialogRef.initData()
    },
    editPerm() {
      if (this.row === null) {
        this.$message.error('请选择要编辑的权限')
        return
      }
      this.dataId = this.row.id
      this.addOrEditDialogVisible = true
      this.addOrEditDialogTitle = '编辑权限'
      this.$refs.addOrEditDialogRef.initData()
    },
    addOrEditDone(result = false) {
      this.addOrEditDialogTitle = ''
      this.addOrEditDialogVisible = false
      if (result) {
        this.listTableData()
      }
    },
    // 删除
    deletePerm() {
      if (this.row === null) {
        this.$message.error('请选择要删除的权限')
        return
      }
      this.$confirm('此操作将永久删除选中权限, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.$mapi.perm.deletePerm({ permId: this.row.id }).then(res => {
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
