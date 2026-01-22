<template>
  <div class="app-container">
    <div class="head-container">
      <div class="search-opts">
        <muses-search-form ref="searchForm" :model="filters" label-position="right" label-width="0" :offset="0" :item-width="160">
          <muses-search-form-item label="" prop="search-dictName">
            <el-input id="search-dictName" v-model="filters.dictName" clearable placeholder="字典名称（组）" @keyup.enter.native="search" />
          </muses-search-form-item>
          <muses-search-form-item btn btn-open-name="" btn-close-name="">
            <el-button type="success" @click="search()">查 询</el-button>
            <el-button type="warning" @click="resetSearch()">重 置</el-button>
          </muses-search-form-item>
        </muses-search-form>
      </div>
      <div class="crud-opts">
        <span class="crud-opts-left">
          <el-button v-perm="['DICTA001']" type="success" @click="addDictGroup">新增组</el-button>
          <el-button v-perm="['DICTA001']" :disabled="row === null || row.dictType !== 1" type="success" @click="addDictItem">新增项</el-button>
          <el-button v-perm="['DICTQ003']" :disabled="row === null" type="info" @click="showDetail">详情</el-button>
          <el-button v-perm="['DICTU001']" :disabled="row === null" type="primary" @click="editDict">编辑</el-button>
          <el-button v-perm="['DICTD001']" :disabled="row === null" type="danger" @click="deleteDict">删除</el-button>
        </span>
      </div>
    </div>
    <div class="components-container">
      <el-table
        ref="dictTable"
        v-loading="tableLoading"
        :data="tableData"
        :tree-props="tableProps"
        :load="queryChildList"
        :header-cell-style="{background:'#FAFAFA'}"
        highlight-current-row
        row-key="id"
        border
        lazy
        @current-change="handleCurrentChange"
      >
        <el-table-column :show-overflow-tooltip="true" prop="dictName" label="字典名称" align="left" width="200" />
        <el-table-column :show-overflow-tooltip="true" prop="dictKey" label="字典KEY" align="left" />
        <el-table-column :show-overflow-tooltip="true" prop="dictValue" label="字典VALUE" align="left" />
        <el-table-column :show-overflow-tooltip="true" prop="dictDesc" label="字典描述" align="left" />
        <el-table-column :show-overflow-tooltip="true" prop="subCount" label="字典类型" align="center" width="100">
          <template v-slot="scope">
            <el-tag v-if="scope.row.dictType === 1">组</el-tag>
            <el-tag v-else-if="scope.row.dictType === 2" type="success">项</el-tag>
            <span v-else />
          </template>
        </el-table-column>
        <el-table-column :show-overflow-tooltip="true" prop="subCount" label="配置项个数" align="center" width="100">
          <template v-slot="scope">
            <span v-if="scope.row.dictType === 1">{{ scope.row['subCount'] }}</span>
            <span v-else />
          </template>
        </el-table-column>
        <el-table-column :show-overflow-tooltip="false" prop="dictSort" label="字典排序" align="center" width="100" />
        <el-table-column :show-overflow-tooltip="false" prop="enabled" label="字典状态" align="center" width="100">
          <template v-slot="scope">
            <el-tag v-if="scope.row.enabled" type="success">启用</el-tag>
            <el-tag v-else type="danger">禁用</el-tag>
          </template>
        </el-table-column>
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
import detailDialog from '@/views/admin/system/dict/template/detailDialog'
import addOrEditDialog from '@/views/admin/system/dict/template/addOrEditDialog'
import nodataImg from '@/assets/images/nodata.png'
export default {
  name: 'Dict',
  components: {
    detailDialog,
    addOrEditDialog
  },
  data() {
    return {
      filters: {
        dictName: null
      },
      tableData: [],
      tableLoading: false,
      tableProps: {
        children: 'children',
        hasChildren: 'hasChildren'
      },
      nodataImg: nodataImg,
      row: null,
      total: 0,
      page: 1,
      pageSize: 10,
      detailDialogVisible: false,
      addOrEditDialogTitle: null,
      addOrEditDialogVisible: false,
      maps: new Map()
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
      this.$mapi.dict.pageDictList(param).then(res => {
        const { data } = res
        this.total = data.total
        this.tableData = data.records
      }).catch(_ => {
        this.total = 0
        this.tableData = []
      }).finally(() => {
        this.tableLoading = false
        this.row = null
        this.$refs.dictTable.setCurrentRow()
      })
    },
    queryChildList(tree, treeNode, resolve) {
      this.maps.set(tree.id, { tree, treeNode, resolve })
      this.$mapi.dict.queryChildList({ pid: tree.id }).then(res => {
        resolve(res.data)
      }).catch(_ => {
        resolve([])
      })
    },
    // 详情
    showDetail() {
      if (this.row === null) {
        this.$message.error('请选择要查看的字典数据')
        return
      }
      this.detailDialogVisible = true
      this.$refs.detailDialogRef.initData(this.row.id)
    },
    showDetailDone() {
      this.detailDialogVisible = false
    },
    // 新增与编辑
    addDictGroup() {
      this.addOrEditDialogVisible = true
      this.addOrEditDialogTitle = '新增字典组'
      this.$refs.addOrEditDialogRef.initData(1)
    },
    addDictItem() {
      this.addOrEditDialogVisible = true
      this.addOrEditDialogTitle = '新增字典项'
      this.$refs.addOrEditDialogRef.initData(2, this.row.id)
    },
    editDict() {
      if (this.row === null) {
        this.$message.error('请选择要编辑的字典数据')
        return
      }
      this.addOrEditDialogVisible = true
      this.addOrEditDialogTitle = this.row.dictType === 1 ? '编辑字典组' : '编辑字典项'
      this.$refs.addOrEditDialogRef.initData(this.row.dictType, this.row.pid, this.row.id)
    },
    addOrEditDone(result = false, dictType, type) {
      this.addOrEditDialogTitle = null
      this.addOrEditDialogVisible = false
      if (result) {
        if (dictType === 1) {
          // 新增或编辑组
          this.listTableData()
        } else {
          // 新增或编辑项，只有选中组的场景下才可以新增或编辑组
          const curId = this.row.id
          const curPid = this.row.pid
          const realId = type === 'add' ? curId : type === 'edit' ? curPid : ''
          if (realId && this.maps.get(realId)) {
            const { tree, treeNode, resolve } = this.maps.get(realId)
            this.queryChildList(tree, treeNode, resolve)
          } else {
            this.listTableData()
          }
        }
      }
    },
    deleteDict() {
      if (this.row === null) {
        this.$message.error('请选择要删除的字典数据')
        return
      }
      this.$confirm('此操作将永久删除字典【' + this.row.dictName + '】, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.$mapi.dict.deleteDict({ dictId: this.row.id }).then(res => {
          this.$message.success(res.message)
          if (this.row.pid) {
            const { tree, treeNode, resolve } = this.maps.get(this.row.pid)
            this.$set(this.$refs.dictTable.store['states']['lazyTreeNodeMap'], this.row.pid, [])
            this.queryChildList(tree, treeNode, resolve)
            this.row = null
            this.$refs.dictTable.setCurrentRow()
          } else {
            this.listTableData()
          }
        })
      })
    }
  }
}
</script>

<style scoped>

</style>
