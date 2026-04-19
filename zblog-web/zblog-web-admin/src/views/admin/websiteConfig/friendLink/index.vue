<template>
  <div class="app-container">
    <div class="head-container">
      <div class="search-opts">
        <muses-search-form ref="searchForm" :model="filters" label-position="right" label-width="0" :offset="0" :item-width="140">
          <muses-search-form-item label="" prop="search-name">
            <el-input id="search-name" v-model="filters.name" clearable placeholder="网站名称" @keyup.enter.native="search" />
          </muses-search-form-item>
          <muses-search-form-item label="" prop="search-url">
            <el-input id="search-url" v-model="filters.url" clearable placeholder="网站地址" @keyup.enter.native="search" />
          </muses-search-form-item>
          <muses-search-form-item label="" prop="search-status">
            <el-select id="search-status" v-model="filters.status" placeholder="友链状态" clearable @change="search">
              <el-option v-for="item in statusList" :key="item.value" :label="item.name" :value="item.value" />
            </el-select>
          </muses-search-form-item>
          <muses-search-form-item label="" prop="search-isVisible">
            <el-select id="search-isVisible" v-model="filters.isVisible" placeholder="是否展示" clearable @change="search">
              <el-option v-for="item in visibleList" :key="item.value" :label="item.name" :value="item.value" />
            </el-select>
          </muses-search-form-item>
          <muses-search-form-item label="" prop="search-allowFollow">
            <el-select id="search-allowFollow" v-model="filters.allowFollow" placeholder="是否传递SEO" clearable @change="search">
              <el-option v-for="item in allowFollowList" :key="item.value" :label="item.name" :value="item.value" />
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
          <el-button v-perm="['LINKA001']" type="success" @click="addFriendLink">新增</el-button>
          <el-button v-perm="['LINKQ002']" :disabled="row === null" type="info" @click="showDetail">详情</el-button>
          <el-button v-perm="['LINKU001']" :disabled="row === null" type="primary" @click="editFriendLink">编辑</el-button>
          <el-button v-perm="['LINKD001']" :disabled="row === null" type="danger" @click="deleteFriendLink">删除</el-button>
        </span>
      </div>
    </div>
    <div class="components-container">
      <el-table
        ref="friendLinkTable"
        v-loading="tableLoading"
        :data="tableData"
        :header-cell-style="{background:'#FAFAFA'}"
        highlight-current-row
        row-key="id"
        border
        @current-change="handleCurrentChange"
      >
        <el-table-column prop="name" label="网站名称" align="center" width="250">
          <template v-slot="scope">
            <div class="site-name-cell">
              <img :src="scope.row.logo" class="site-logo" alt="">
              <span class="site-text">{{ scope.row.name }}</span>
            </div>
          </template>
        </el-table-column>
        <el-table-column :show-overflow-tooltip="true" prop="url" label="网站地址" align="center">
          <template v-slot="scope">
            <el-link
              type="primary"
              :href="scope.row.url"
              target="_blank"
              :rel="scope.row.allowFollow ? 'noopener noreferrer' : 'nofollow noopener noreferrer'"
            >
              {{ scope.row.url }}
            </el-link>
          </template>
        </el-table-column>
        <el-table-column :show-overflow-tooltip="true" prop="description" label="网站介绍" align="center" />
        <el-table-column :show-overflow-tooltip="true" prop="status" label="审核状态" align="center" width="100">
          <template v-slot="scope">
            <el-tag v-if="scope.row.status === 0" type="warning">审核中</el-tag>
            <el-tag v-else-if="scope.row.status === 1" type="success">审核通过</el-tag>
            <el-tag v-else-if="scope.row.status === 2" type="danger">审核拒绝</el-tag>
            <span v-else>-</span>
          </template>
        </el-table-column>
        <el-table-column :show-overflow-tooltip="true" prop="isVisible" label="展示状态" align="center" width="100">
          <template v-slot="scope">
            <el-tag v-if="scope.row.isVisible === true" type="success">展示</el-tag>
            <el-tag v-else type="warning">不展示</el-tag>
          </template>
        </el-table-column>
        <el-table-column :show-overflow-tooltip="true" prop="allowFollow" label="SEO配置" align="center" width="100">
          <template v-slot="scope">
            <el-tag v-if="scope.row.allowFollow === true" type="success">启用SEO</el-tag>
            <el-tag v-else type="warning">禁用SEO</el-tag>
          </template>
        </el-table-column>
        <el-table-column :show-overflow-tooltip="true" prop="createTime" label="创建时间" align="center" width="150" />
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
import detailDialog from '@/views/admin/websiteConfig/friendLink/template/detailDialog'
import addOrEditDialog from '@/views/admin/websiteConfig/friendLink/template/addOrEditDialog'
export default {
  name: 'FriendLink',
  components: {
    detailDialog,
    addOrEditDialog
  },
  data() {
    return {
      filters: {
        name: null,
        url: null,
        status: null,
        isVisible: null,
        allowFollow: null
      },
      statusList: [],
      visibleList: [],
      allowFollowList: [],
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
    this.loadStatusList()
    this.loadVisibleList()
    this.loadAllowFollowList()
    this.listTableData()
  },
  methods: {
    handleCurrentChange(row) {
      this.row = row
    },
    loadStatusList() {
      this.$mapi.communal.queryConfListByDictKey({ dictKey: 'APPROVE_STATUS_CONFIG' }).then(res => {
        const { data } = res
        this.statusList = data
      }).catch(_ => {
        this.statusList = []
      })
    },
    loadVisibleList() {
      this.$mapi.communal.queryConfListByDictKey({ dictKey: 'VISIBLE_CONFIG' }).then(res => {
        const { data } = res
        this.visibleList = data
      }).catch(_ => {
        this.visibleList = []
      })
    },
    loadAllowFollowList() {
      this.$mapi.communal.queryConfListByDictKey({ dictKey: 'SEO_ALLOW_FOLLOW_CONFIG' }).then(res => {
        const { data } = res
        this.allowFollowList = data
      }).catch(_ => {
        this.allowFollowList = []
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
      this.$mapi.friendLink.pageFriendLinkList(param).then(res => {
        const { data } = res
        this.total = data.total
        this.tableData = data.records
      }).catch(_ => {
        this.total = 0
        this.tableData = []
      }).finally(() => {
        this.tableLoading = false
        this.row = null
        this.$refs.friendLinkTable.setCurrentRow()
      })
    },
    // 详情
    showDetail() {
      if (this.row === null) {
        this.$message.error('请选择要查看的友链')
        return
      }
      this.detailDialogVisible = true
      this.$refs.detailDialogRef.initData(this.row.id)
    },
    showDetailDone() {
      this.detailDialogVisible = false
    },
    // 新增与编辑
    addFriendLink() {
      this.addOrEditDialogVisible = true
      this.addOrEditDialogTitle = '新增友链'
      this.$refs.addOrEditDialogRef.initData()
    },
    editFriendLink() {
      if (this.row === null) {
        this.$message.error('请选择要编辑的友链')
        return
      }
      this.addOrEditDialogVisible = true
      this.addOrEditDialogTitle = '编辑友链'
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
    deleteFriendLink() {
      if (this.row === null) {
        this.$message.error('请选择要删除的友链')
        return
      }
      this.$confirm('此操作将永久删除友链【' + this.row.name + '】, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.$mapi.friendLink.deleteFriendLink({ friendLinkId: this.row.id }).then(res => {
          this.$message.success(res.message)
          this.listTableData()
        })
      })
    }
  }
}
</script>

<style scoped>
.site-name-cell {
  display: flex;
  align-items: center;   /* 垂直居中核心 */
  justify-content: center; /* 整体水平居中（因为你列是center） */
  gap: 8px; /* 图片和文字间距，比 margin 更优雅 */
}

.site-logo {
  width: 40px;
  height: 40px;
  object-fit: cover; /* 防止图片变形 */
  border-radius: 4px; /* 可选：更好看 */
}

.site-text {
  line-height: 1; /* 避免文本影响对齐 */
}
</style>
