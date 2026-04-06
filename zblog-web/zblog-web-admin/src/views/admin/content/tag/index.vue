<template>
  <div class="app-container">
    <div class="head-container">
      <div class="search-opts">
        <muses-search-form ref="searchForm" :model="filters" label-position="right" label-width="0" :offset="0" :item-width="140">
          <muses-search-form-item label="" prop="search-name">
            <el-input id="search-name" v-model="filters.name" clearable placeholder="标签名称" @keyup.enter.native="search" />
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
            <el-select id="search-enabled" v-model="filters.enabled" placeholder="标签状态" clearable @change="search">
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
          <el-button v-perm="['TAGSA001']" type="success" @click="addTag">新增</el-button>
          <el-button v-perm="['TAGSQ002']" :disabled="row === null" type="info" @click="showDetail">详情</el-button>
          <el-button v-perm="['TAGSU001']" :disabled="row === null" type="primary" @click="editTag">编辑</el-button>
          <el-button v-perm="['TAGSD001']" :disabled="row === null" type="danger" @click="deleteTag">删除</el-button>
        </span>
      </div>
    </div>
    <div class="components-container">
      <el-table
        ref="tagTable"
        v-loading="tableLoading"
        :data="tableData"
        :header-cell-style="{background:'#FAFAFA'}"
        highlight-current-row
        row-key="id"
        border
        @current-change="handleCurrentChange"
      >
        <el-table-column :show-overflow-tooltip="true" prop="name" label="标签名称" />
        <el-table-column :show-overflow-tooltip="true" prop="slug" label="SLUG" />
        <el-table-column :show-overflow-tooltip="true" prop="articleCount" label="文章数" align="center" width="100" />
        <el-table-column :show-overflow-tooltip="false" prop="enabled" label="收录状态" align="center" width="100">
          <template v-slot="scope">
            <el-tag v-if="scope.row.allowIndex" type="success">收录</el-tag>
            <el-tag v-else type="danger">禁止</el-tag>
          </template>
        </el-table-column>
        <el-table-column :show-overflow-tooltip="false" prop="enabled" label="标签状态" align="center" width="100">
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
import nodataImg from '@/assets/images/nodata.png'
import detailDialog from '@/views/admin/content/tag/template/detailDialog'
import addOrEditDialog from '@/views/admin/content/tag/template/addOrEditDialog'
export default {
  name: 'Tag',
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
        enabled: null
      },
      allowIndexList: [],
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
      addOrEditDialogVisible: false
    }
  },
  mounted() {
    this.loadAllowIndexList()
    this.loadEnabledList()
    this.listTableData()
  },
  methods: {
    handleCurrentChange(row) {
      this.row = row
    },
    loadAllowIndexList() {
      this.$mapi.communal.queryConfListByDictKey({ dictKey: 'ALLOW_INDEX_CONFIG' }).then(res => {
        const { data } = res
        this.allowIndexList = data
      }).catch(_ => {
        this.allowIndexList = []
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
      this.$mapi.tag.pageTagList(param).then(res => {
        const { data } = res
        this.total = data.total
        this.tableData = data.records
      }).catch(_ => {
        this.total = 0
        this.tableData = []
      }).finally(() => {
        this.tableLoading = false
        this.row = null
        this.$refs.tagTable.setCurrentRow()
      })
    },
    // 详情
    showDetail() {
      if (this.row === null) {
        this.$message.error('请选择要查看的标签')
        return
      }
      this.detailDialogVisible = true
      this.$refs.detailDialogRef.initData(this.row.id)
    },
    showDetailDone() {
      this.detailDialogVisible = false
    },
    // 新增与编辑
    addTag() {
      this.addOrEditDialogVisible = true
      this.addOrEditDialogTitle = '新增标签'
      this.$refs.addOrEditDialogRef.initData()
    },
    editTag() {
      if (this.row === null) {
        this.$message.error('请选择要编辑的标签')
        return
      }
      this.addOrEditDialogVisible = true
      this.addOrEditDialogTitle = '编辑标签'
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
    deleteTag() {
      if (this.row === null) {
        this.$message.error('请选择要删除的标签')
        return
      }
      this.$confirm('此操作将永久删除标签【' + this.row.name + '】, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.$mapi.tag.deleteTag({ tagId: this.row.id }).then(res => {
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
