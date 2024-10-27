<template>
  <div class="app-container">
    <div class="head-container">
      <div>
        <el-input v-model="filters.name" clearable size="small" placeholder="分类名称" style="width: 200px;" class="filter-item" @keyup.enter.native="search" />
        <el-select v-model="filters.enabled" clearable placeholder="启用状态" style="width: 120px" class="filter-item">
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
          <el-button v-perm="['addCategory']" size="small" type="primary" @click="addCategory()">
            新增大类
          </el-button>
        </span>
      </div>
    </div>

    <div class="components-container">
      <el-table v-loading="tableLoading" :data="tableData" row-key="id" :tree-props="tableProps" border>
        <el-table-column :show-overflow-tooltip="true" prop="name" label="分类名称" width="200" />
        <el-table-column prop="articleImgLinkList" label="分类图标" align="center" width="180">
          <template v-slot="scope">
            <el-image class="article-cover" :src="scope.row['imageUrl']" :preview-src-list="getPreviewList(scope.row)">
              <div slot="error" class="image-slot">
                <span v-if="scope.row['imageUrl'] === ''">未配置</span>
                <span v-else>加载失败</span>
              </div>
            </el-image>
          </template>
        </el-table-column>
        <el-table-column :show-overflow-tooltip="true" prop="desc" label="分类描述" />
        <el-table-column :show-overflow-tooltip="true" prop="articleCount" label="文章数" align="center" width="100" />
        <el-table-column prop="enabled" label="状态" align="center" width="100">
          <template v-slot="scope">
            <el-tag v-if="scope.row.enabled" size="small">启用</el-tag>
            <el-tag v-else size="small" type="warning">禁用</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="menuSort" label="排序" align="center" width="100">
          <template v-slot="scope">
            {{ scope.row.sort }}
          </template>
        </el-table-column>
        <el-table-column v-if="hasPerm(['queryCategoryDetail', 'editCategory', 'deleteCategory'])" label="操作" align="center" width="200">
          <template v-slot="scope">
            <el-button-group>
              <el-button v-perm="['queryCategoryDetail']" type="info" size="mini" @click="showCategoryDetail(scope.row)">详情</el-button>
              <el-button v-perm="['editCategory']" type="success" size="mini" @click="editCategory(scope.row)">编辑</el-button>
              <el-popconfirm v-perm="['deleteCategory']" title="操作不可撤销，确定删除吗？" @confirm="deleteCategory(scope.row)">
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
      @addOrEditDone="addOrEditDone"
    />
    <!-- 查看详情 -->
    <showDetailDialog
      ref="showDetailDialogRef"
      :dialog-visible="showDetailDialogVisible"
      @showDetailDone="showDetailDone"
    />
  </div>
</template>

<script>
import addOrEditDialog from '@/views/admin/web/category/template/addOrEditDialog'
import showDetailDialog from '@/views/admin/web/category/template/showDetailDialog'
export default {
  name: 'Category',
  components: {
    addOrEditDialog,
    showDetailDialog
  },
  data() {
    return {
      filters: {
        name: '',
        enabled: ''
      },
      tableData: [],
      tableProps: {
        children: 'children',
        hasChildren: 'hasChildren'
      },
      tableLoading: false,
      addOrEditDialogTitle: '',
      addOrEditDialogVisible: false,
      showDetailDialogVisible: false
    }
  },
  mounted() {
    this.listTableData()
  },
  methods: {
    hasPerm(value) {
      return this.checkPerm(value)
    },
    search() {
      this.listTableData()
    },
    resetSearch() {
      this.filters.name = ''
      this.filters.enabled = ''

      this.listTableData()
    },
    listTableData() {
      const param = { ... this.filters }
      this.tableLoading = true
      this.$mapi.category.queryCategoryTreeList(param).then(res => {
        this.tableData = res.data
      }).catch(_ => {
        this.tableData = []
      }).finally(() => {
        this.tableLoading = false
      })
    },
    showCategoryDetail(row) {
      this.showDetailDialogVisible = true
      this.$refs.showDetailDialogRef.initData(row.id)
    },
    addCategory() {
      this.addOrEditDialogVisible = true
      this.addOrEditDialogTitle = '新增类别'
      this.$refs.addOrEditDialogRef.initData()
    },
    editCategory(row) {
      this.addOrEditDialogVisible = true
      this.addOrEditDialogTitle = '编辑类别'
      this.$refs.addOrEditDialogRef.initData(row.id)
    },
    allowDelete(row) {
      return !row.children || !row.children.length || row.children.length === 0
    },
    deleteCategory(row) {
      this.$mapi.category.deleteCategory({ categoryId: row.id }).then(res => {
        this.$message.success(res.message)
        this.listTableData()
      })
    },
    addOrEditDone(result = false) {
      this.addOrEditDialogVisible = false
      this.addOrEditDialogTitle = ''
      if (result) {
        this.listTableData()
      }
    },
    showDetailDone() {
      this.showDetailDialogVisible = false
    },
    getPreviewList(row) {
      if (row['imageUrl'] && row['imageUrl'] !== '') {
        return [row['imageUrl']]
      } else {
        return []
      }
    }
  }
}
</script>

<style scoped>
.article-cover {
  position: relative;
  width: 100%;
  height: 90px;
  border-radius: 4px;
}
::v-deep .image-slot, .demo-image__placeholder .image-slot {
  display: flex;
  justify-content: center;
  align-items: center;
  width: 100%;
  height: 100%;
  background: #f5f7fa;
  color: #909399;
  font-size: 14px;
}
</style>
