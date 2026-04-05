<template>
  <div class="app-container">
    <div class="head-container">
      <div class="search-opts">
        <muses-search-form ref="searchForm" :model="filters" label-position="right" label-width="0" :offset="0" :item-width="160">
          <muses-search-form-item label="" prop="search-name">
            <el-input id="search-name" v-model="filters.name" clearable placeholder="分类名称" @keyup.enter.native="search" />
          </muses-search-form-item>
          <muses-search-form-item label="" prop="search-slug">
            <el-input id="search-slug" v-model="filters.slug" clearable placeholder="SLUG" @keyup.enter.native="search" />
          </muses-search-form-item>
          <muses-search-form-item label="" prop="search-allowIndex">
            <el-select id="search-allowIndex" v-model="filters.allowIndex" placeholder="引擎收录" clearable @change="search">
              <el-option v-for="item in allowIndexList" :key="item.value" :label="item.name" :value="item.value" />
            </el-select>
          </muses-search-form-item>
          <muses-search-form-item label="" prop="search-enabled">
            <el-select id="search-enabled" v-model="filters.visible" placeholder="前台显示" clearable @change="search">
              <el-option v-for="item in visibleList" :key="item.value" :label="item.name" :value="item.value" />
            </el-select>
          </muses-search-form-item>
          <muses-search-form-item label="" prop="search-enabled">
            <el-select id="search-enabled" v-model="filters.enabled" placeholder="分类状态" clearable @change="search">
              <el-option v-for="item in enabledList" :key="item.value" :label="item.name" :value="item.value" />
            </el-select>
          </muses-search-form-item>
          <muses-search-form-item btn btn-open-name="" btn-close-name="">
            <el-button type="success" @click="search()">查询</el-button>
            <el-button type="warning" @click="resetSearch()">重置</el-button>
          </muses-search-form-item>
        </muses-search-form>
      </div>
      <div class="crud-opts">
        <span class="crud-opts-left">
          <el-button v-perm="['CATEA001']" type="success" @click="addCategory">新增</el-button>
          <el-button v-perm="['CATEQ003']" :disabled="row === null" type="info" @click="showDetail">详情</el-button>
          <el-button v-perm="['CATEU001']" :disabled="row === null" type="primary" @click="editCategory">编辑</el-button>
          <el-button v-perm="['CATED001']" :disabled="row === null" type="danger" @click="deleteCategory">删除</el-button>
        </span>
      </div>
    </div>
    <div class="components-container">
      <el-table
        ref="categoryTable"
        v-loading="tableLoading"
        :data="tableData"
        :header-cell-style="{background:'#FAFAFA'}"
        highlight-current-row
        row-key="id"
        :tree-props="tableProps"
        border
        @current-change="handleCurrentChange"
      >
        <el-table-column :show-overflow-tooltip="true" prop="name" label="分类名称" width="200" />
        <el-table-column :show-overflow-tooltip="true" prop="slug" label="SLUG" width="200" />
        <el-table-column prop="articleImgLinkList" label="分类图" align="center" width="180">
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
        <el-table-column :show-overflow-tooltip="false" prop="enabled" label="前台展示" align="center" width="100">
          <template v-slot="scope">
            <el-tag v-if="scope.row.visible" type="success">展示</el-tag>
            <el-tag v-else type="danger">隐藏</el-tag>
          </template>
        </el-table-column>
        <el-table-column :show-overflow-tooltip="false" prop="enabled" label="收录状态" align="center" width="100">
          <template v-slot="scope">
            <el-tag v-if="scope.row.allowIndex" type="success">收录</el-tag>
            <el-tag v-else type="danger">禁止</el-tag>
          </template>
        </el-table-column>
        <el-table-column :show-overflow-tooltip="false" prop="enabled" label="分类状态" align="center" width="100">
          <template v-slot="scope">
            <el-tag v-if="scope.row.enabled" type="success">启用</el-tag>
            <el-tag v-else type="danger">禁用</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="sort" label="排序" align="center" width="100">
          <template v-slot="scope">
            {{ scope.row.sort }}
          </template>
        </el-table-column>
        <div slot="empty">
          <el-empty :image="nodataImg" description=" " />
        </div>
      </el-table>
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
import detailDialog from '@/views/admin/content/category/template/detailDialog'
import addOrEditDialog from '@/views/admin/content/category/template/addOrEditDialog'
import nodataImg from '@/assets/images/nodata.png'
export default {
  name: 'Category',
  components: {
    detailDialog,
    addOrEditDialog
  },
  data() {
    return {
      filters: {
        name: null,
        slug: null,
        allowIndex: null,
        visible: null,
        enabled: null
      },
      allowIndexList: [],
      visibleList: [],
      enabledList: [],
      tableData: [],
      tableLoading: false,
      tableProps: {
        children: 'children',
        hasChildren: 'hasChildren'
      },
      nodataImg: nodataImg,
      row: null,
      detailDialogVisible: false,
      addOrEditDialogTitle: null,
      addOrEditDialogVisible: false,
      maps: new Map()
    }
  },
  mounted() {
    this.listTableData()
    this.loadAllowIndexList()
    this.loadVisibleList()
    this.loadEnabledList()
  },
  methods: {
    handleCurrentChange(row) {
      this.row = row
    },
    loadAllowIndexList() {
      this.$mapi.communal.queryConfListByDictKey({ dictKey: 'CATEGORY_ALLOW_INDEX_CONFIG' }).then(res => {
        const { data } = res
        this.allowIndexList = data
      }).catch(_ => {
        this.allowIndexList = []
      })
    },
    loadVisibleList() {
      this.$mapi.communal.queryConfListByDictKey({ dictKey: 'CATEGORY_VISIBLE_CONFIG' }).then(res => {
        const { data } = res
        this.visibleList = data
      }).catch(_ => {
        this.visibleList = []
      })
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
      this.listTableData()
    },
    resetSearch() {
      Object.keys(this.filters).forEach(key => { this.filters[key] = null })
      this.listTableData()
    },
    listTableData() {
      const param = { ... this.filters }
      this.tableLoading = true
      this.$mapi.category.queryCategoryTree(param).then(res => {
        this.tableData = res.data
      }).catch(_ => {
        this.tableData = []
      }).finally(() => {
        this.tableLoading = false
        this.row = null
        this.$refs.categoryTable.setCurrentRow()
      })
    },
    getPreviewList(row) {
      if (row['imageUrl'] && row['imageUrl'] !== '') {
        return [row['imageUrl']]
      } else {
        return []
      }
    },
    // 详情
    showDetail() {
      if (this.row === null) {
        this.$message.error('请选择要查看的分类')
        return
      }
      this.detailDialogVisible = true
      this.$refs.detailDialogRef.initData(this.row.id)
    },
    showDetailDone() {
      this.detailDialogVisible = false
    },
    // 新增与编辑
    addCategory() {
      this.addOrEditDialogVisible = true
      this.addOrEditDialogTitle = '新增分类'
      this.$refs.addOrEditDialogRef.initData()
    },
    editCategory() {
      if (this.row === null) {
        this.$message.error('请选择要编辑的分类')
        return
      }
      this.addOrEditDialogVisible = true
      this.addOrEditDialogTitle = '编辑分类'
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
    deleteCategory() {
      if (this.row === null) {
        this.$message.error('请选择要删除的分类')
        return
      }
      this.$confirm('此操作将永久删除分类【' + this.row.name + '】, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.$mapi.category.deleteCategory({ categoryId: this.row.id }).then(res => {
          this.$message.success(res.message)
          this.listTableData()
        })
      })
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
