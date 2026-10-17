<template>
  <div class="app-container">
    <div class="head-container">
      <div class="head-nav-tabs">
        <span v-show="!isMobile" class="article-status">状态</span>
        <ul class="nav nav-pills">
          <li v-for="tab in statusTabs" :key="tab.key" class="nav-item">
            <span :ref="'tab-' + tab.field" class="nav-link" :class="{ active: filters.tagStatus === tab.key }" @click="statusTabChange(tab.key)">
              {{ tab.label }}({{ articleCountInfo[tab.field] || 0 }})
            </span>
          </li>
        </ul>
      </div>
      <div class="search-opts">
        <muses-search-form ref="searchForm" :model="filters" label-position="right" label-width="0" :offset="0" :item-width="140">
          <muses-search-form-item label="" prop="search-title">
            <el-input id="search-title" v-model="filters.title" clearable placeholder="文章标题" @keyup.enter.native="search" />
          </muses-search-form-item>
          <muses-search-form-item label="" prop="search-slug">
            <el-input id="search-slug" v-model="filters.slug" clearable placeholder="SLUG（精确匹配）" @keyup.enter.native="search" />
          </muses-search-form-item>
          <muses-search-form-item btn btn-open-name="" btn-close-name="">
            <el-button type="success" @click="search()">查 询</el-button>
            <el-button type="warning" @click="resetSearch()">重 置</el-button>
          </muses-search-form-item>
        </muses-search-form>
      </div>
      <div class="crud-opts">
        <span class="crud-opts-left">
          <el-button v-perm="['ARTIA001']" type="success" @click="addArticle">新增</el-button>
          <el-button v-perm="['ARTIU001']" :disabled="row === null" type="primary" @click="editArticle">编辑</el-button>
          <el-button v-perm="['ARTID001']" :disabled="row === null" type="danger" @click="deleteArticle">删除</el-button>
        </span>
      </div>
    </div>
    <div class="components-container">
      <el-table
        ref="articleTable"
        v-loading="tableLoading"
        :data="tableData"
        :header-cell-style="{background:'#FAFAFA'}"
        highlight-current-row
        row-key="id"
        border
        @current-change="handleCurrentChange"
      >
        <el-table-column label="文章封面" align="center" width="180" fixed="left">
          <template v-slot="scope">
            <el-image class="article-cover" :src="getArticleCoverImage(scope.row.coverImageType, scope.row.coverImageList)" :preview-src-list="scope.row.coverImageList">
              <div slot="error" class="image-slot">
                <span v-if="scope.row.coverImageType === 1">默认封面</span>
                <span v-if="scope.row.coverImageType === 5">无封面</span>
                <span v-if="scope.row.coverImageType !== 1 && scope.row.coverImageType !== 5 && scope.row.coverImageList.length === 0">未配置</span>
                <span v-else>加载失败</span>
              </div>
            </el-image>
          </template>
        </el-table-column>
        <el-table-column prop="title" label="文章标题" align="left" width="200" fixed="left" />
        <el-table-column label="文章类型" align="center" width="100">
          <template v-slot="scope">
            <span v-if="scope.row.articleType === 1">原创</span>
            <span v-else-if="scope.row.articleType === 2">转载</span>
            <span v-else-if="scope.row.articleType === 3">翻译</span>
            <span v-else>-</span>
          </template>
        </el-table-column>
        <el-table-column prop="categoryName" label="文章分类" width="100" align="center" />
        <el-table-column label="文章标签" width="250" align="center">
          <template v-slot="scope">
            <el-tag v-for="(item, index) in scope.row.tagNames" :key="index" type="info" style="margin-right:0.2rem;margin-top:0.2rem;">
              {{ item }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="文章权限" width="100" align="center">
          <template v-slot="scope">
            <span v-if="scope.row.articlePerm === 1">公开访问</span>
            <span v-else-if="scope.row.articlePerm === 2">登录可见</span>
            <span v-else-if="scope.row.articlePerm === 3">仅作者可见</span>
            <span v-else-if="scope.row.articlePerm === 4">公众号验证</span>
            <span v-else-if="scope.row.articlePerm === 5">密码访问</span>
            <span v-else-if="scope.row.articlePerm === 6">付费访问</span>
            <span v-else-if="scope.row.articlePerm === 7">指定用户</span>
            <span v-else>-</span>
          </template>
        </el-table-column>
        <el-table-column label="评论状态" width="100" align="center">
          <template v-slot="scope">
            <el-tag v-if="scope.row.commentFlag" effect="dark">开启评论</el-tag>
            <el-tag v-else type="warning" effect="dark">关闭评论</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="wordsCount" label="字数" width="70" align="center" />
        <el-table-column prop="viewCount" label="浏览量" width="70" align="center" />
        <el-table-column prop="commentCount" label="评论数" width="70" align="center" />
        <el-table-column prop="authorName" label="作者" width="100" align="center" />
        <el-table-column prop="updateTime" label="更新时间" width="100" align="center" />

        <!--        <el-table-column :show-overflow-tooltip="true" prop="articleStatus" label="文章状态" width="100" align="center">-->
        <!--          <template v-slot="scope">-->
        <!--            <el-tag v-if="scope.row['articleStatus'] === 1" type="info" effect="dark">草稿</el-tag>-->
        <!--            <el-tag v-else-if="scope.row['articleStatus'] === 2" effect="dark">待审核</el-tag>-->
        <!--            <el-tag v-else-if="scope.row['articleStatus'] === 3" effect="dark">待审核</el-tag>-->
        <!--            <el-tag v-else-if="scope.row['articleStatus'] === 4" type="warning" effect="dark">-->
        <!--              {{ '审核拒绝：' + scope.row['desc'] }}-->
        <!--            </el-tag>-->
        <!--            <el-tag v-else-if="scope.row['articleStatus'] === 5" type="success" effect="dark">-->
        <!--              {{ scope.row['articlePerm'] === 3 ? '私密文章' : '已发布' }}-->
        <!--            </el-tag>-->
        <!--            <el-tag v-else-if="scope.row['articleStatus'] === 6" type="danger" effect="dark">临时下线</el-tag>-->
        <!--            <el-tag v-else-if="scope.row['articleStatus'] === 7" type="danger" effect="dark">待整改</el-tag>-->
        <!--            <el-tag v-else-if="scope.row['articleStatus'] === 8" type="info" effect="dark">回收站</el-tag>-->
        <!--            <el-tag v-else-if="scope.row['articleStatus'] === 9" effect="dark">待发布</el-tag>-->
        <!--            <span v-else>-</span>-->
        <!--          </template>-->
        <!--        </el-table-column>-->
        <!--        <el-table-column :show-overflow-tooltip="true" prop="desc" label="备注" width="150" align="center" />-->
        <div slot="empty">
          <muses-empty />
        </div>
      </el-table>
      <div class="pagination-container">
        <el-pagination
          :total="total"
          :current-page.sync="page"
          :page-size.sync="pageSize"
          :page-sizes="[5, 10, 15, 20, 25]"
          layout="total, prev, pager, next, sizes"
          @size-change="handleSizeChange"
          @current-change="handlePageChange"
        />
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: 'Article',
  data() {
    return {

      statusTabs: [
        { key: 1, label: '全部', field: 'totalCount' },
        { key: 2, label: '公开', field: 'publicCount' },
        { key: 3, label: '私密', field: 'privateCount' },
        { key: 4, label: '待审核', field: 'pendingAuditCount' },
        { key: 5, label: '待发布', field: 'pendingPublishCount' },
        { key: 6, label: '待处理', field: 'pendingDealCount' },
        { key: 7, label: '草稿箱', field: 'draftCount' },
        { key: 8, label: '回收站', field: 'recycleCount' }
      ],
      articleCountInfo: {
        // 全部
        totalCount: 0,
        // 公开
        publicCount: 0,
        // 私密
        privateCount: 0,
        // 待审核
        pendingAuditCount: 0,
        // 待发布
        pendingPublishCount: 0,
        // 待处理（比如审核不通过 / 待整改）
        pendingDealCount: 0,
        // 草稿箱
        draftCount: 0,
        // 回收站
        recycleCount: 0
      },
      filters: {
        tagStatus: 1,
        title: null,
        slug: null
      },
      categoryList: [],
      tableData: [],
      tableLoading: false,
      row: null,
      total: 0,
      page: 1,
      pageSize: 5,

      previewArticleDialogVisible: false
    }
  },
  computed: {
    isMobile() {
      return this.$store.state.app.device === 'mobile'
    }
  },
  mounted() {
    this.loadCategoryTree()
    this.listTableData()
  },
  activated() {
    if (this.$route.query._ts && this.$route.query._ts !== '') {
      this.statusTabChange(this.$route.query._ts)
    } else {
      if (this.$route.query.refresh) {
        this.page = 1
        this.listTableData()
      }
    }
  },
  methods: {
    handleCurrentChange(row) {
      this.row = row
    },
    loadCategoryTree() {
      this.$mapi.category.queryPublicCategoryTree().then(({ data }) => {
        this.categoryList = data
      }).catch(_ => {
        this.categoryList = []
      })
    },
    getArticleCoverImage(coverImageType, coverImageList) {
      if (coverImageType === 1 || coverImageType === 5) {
        return ''
      }
      if (Array.isArray(coverImageList) && coverImageList.length > 0) {
        return coverImageList[0]
      }
      return ''
    },
    // 查询
    statusTabChange(status) {
      this.page = 1
      this.filters.tagStatus = status
      this.listTableData()
    },
    search() {
      this.page = 1
      this.listTableData()
    },
    resetSearch() {
      Object.keys(this.filters).forEach(key => { this.filters[key] = null })
      this.filters.tagStatus = 1
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
      this.$mapi.article.pageMyArticleList(param).then(res => {
        const { data } = res
        this.total = data.total
        this.tableData = data.records
      }).catch(_ => {
        this.total = 0
        this.tableData = []
      }).finally(() => {
        this.tableLoading = false
        this.row = null
        this.$refs.articleTable.setCurrentRow()
        this.loadMyArticleCountInfo()
      })
    },
    loadMyArticleCountInfo() {
      this.$mapi.article.queryMyArticleCountInfo().then(res => {
        const { data } = res
        Object.keys(this.articleCountInfo).forEach(key => {
          this.articleCountInfo[key] = data[key] || 0
        })
      })
    },
    // 详情
    showArticleDetail(row) {
      this.previewArticleDialogVisible = true
      this.$nextTick(() => {
        this.$refs.previewArticleRef.initData(2, row.id)
      })
    },
    closePreviewArticleDialog() {
      this.previewArticleDialogVisible = false
    },
    // 新增与编辑
    addArticle() {
      this.$router.push({ name: 'AddOrEditArticle' })
    },
    editArticle(row) {
      this.$router.push({ name: 'AddOrEditArticle', query: { articleId: row.id }})
    },
    // 删除 TODO
    recycleToDraftBox(row) {
      this.$mapi.article.recycleToDraftBox({ articleId: row.id }).then(res => {
        this.$message.success(res.message)
        this.listTableData()
      })
    },
    moveToRecycleBin(row) {
      this.$mapi.article.moveToRecycleBin({ articleId: row.id }).then(res => {
        this.$message.success(res.message)
        this.listTableData()
      })
    },
    batchMoveToRecycleBin() {
      const articleIds = []
      this.rows.forEach(row => {
        articleIds.push(row.id)
      })

      if (articleIds.length === 0) {
        this.$message.warning('请选择要删除的文章列表')
        return
      }

      this.$mapi.article.batchMoveToRecycleBin(articleIds).then(res => {
        this.$message.success(res.message)
        this.listTableData()
      })
    },
    deleteArticle(row) {
      this.$mapi.article.deleteArticle({ articleId: row.id }).then(res => {
        this.$message.success(res.message)
        this.listTableData()
      })
    },
    clearRecycleBin() {
      this.$mapi.article.clearRecycleBin().then(res => {
        this.$message.success(res.message)
        this.listTableData()
      })
    }
  }
}
</script>

<style scoped>
.head-nav-tabs {
  position: relative;
  display: -webkit-box;
  display: -ms-flexbox;
  display: flex;
  -webkit-box-align: center;
  -ms-flex-align: center;
  align-items: center;
  height: 30px;
  margin-top: -10px;
  margin-bottom: 15px;
}
.head-nav-tabs .article-status {
  font-size: 14px;
  color: #999aaa;
  line-height: 36px;
  height: 30px;
  margin-right: 24px;
  font-weight: bold;
}
.head-nav-tabs .nav {
  display: -webkit-box;
  display: -ms-flexbox;
  display: flex;
  -ms-flex-wrap: wrap;
  flex-wrap: wrap;
  padding-left: 0;
  margin-bottom: 0;
  list-style: none;
  color: #555666;
}
.head-nav-tabs .nav li {
  padding: 0;
  margin: 0;
  list-style: none;
}
.head-nav-tabs .nav-link.active {
  color: #222226;
  background-color: transparent;
  border-radius: 0;
  font-weight: 500;
  position: relative;
}
.head-nav-tabs .nav-link:hover {
  color: #222226;
  background-color: transparent;
  border-radius: 0;
  font-weight: 500;
  position: relative;
}
.head-nav-tabs .nav-link {
  margin-right: 24px;
  font-size: .875rem;
  color: #999;
  padding: 0 0 10px;
  display: block;
  cursor: pointer;
}

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
