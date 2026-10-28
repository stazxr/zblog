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
          <muses-search-form-item label="" prop="search-categoryId">
            <el-select id="search-categoryId" v-model="filters.categoryId" placeholder="文章分类" clearable filterable @change="search">
              <el-option-group v-for="group in categoryList" :key="group.id" :label="group.name">
                <el-option v-for="item in group.children" :key="item.id" :value="item.id" :label="item.name" />
              </el-option-group>
            </el-select>
          </muses-search-form-item>
          <muses-search-form-item label="" prop="search-tagId">
            <el-select id="search-tagId" v-model="filters.tagId" placeholder="文章标签" clearable filterable @change="search">
              <el-option v-for="item in tagList" :key="item.id" :label="item.name" :value="item.id" />
            </el-select>
          </muses-search-form-item>
          <muses-search-form-item label="" prop="search-articleType">
            <el-select id="search-articleType" v-model="filters.articleType" placeholder="文章类型" clearable @change="search">
              <el-option v-for="item in articleTypeList" :key="item.value" :label="item.name" :value="item.value" />
            </el-select>
          </muses-search-form-item>
          <muses-search-form-item label="" prop="search-articleStatus">
            <el-select id="search-articleStatus" v-model="filters.articleStatus" placeholder="文章状态" clearable @change="search">
              <el-option v-for="item in articleStatusList" :key="item.value" :label="item.name" :value="item.value" />
            </el-select>
          </muses-search-form-item>
          <muses-search-form-item label="" prop="search-articlePerm">
            <el-select id="search-articlePerm" v-model="filters.articlePerm" placeholder="文章权限" clearable @change="search">
              <el-option v-for="item in articlePermList" :key="item.value" :label="item.name" :value="item.value" />
            </el-select>
          </muses-search-form-item>
          <muses-search-form-item label="" prop="search-commentFlag">
            <el-select id="search-commentFlag" v-model="filters.commentFlag" placeholder="评论状态" clearable @change="search">
              <el-option v-for="item in commentFlagList" :key="item.value" :label="item.name" :value="item.value" />
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
          <template v-perm="['ARTIA001', 'ARTIU001', 'ARTIU002', 'ARTIU003', 'ARTIQ003']">
            <el-button type="success" @click="addArticle">新增</el-button>
            <el-button v-show="filters.tagStatus !== '8'" :disabled="row === null" type="primary" @click="editArticle">编辑</el-button>
            <el-button type="info" :disabled="row === null" @click="showArticleDetail">详情</el-button>
          </template>
          <el-button v-perm="['ARTIQ007']" type="info" :disabled="row === null" @click="showArticleLog">操作日志</el-button>
          <el-button v-perm="['ARTIQ008']" type="info" :disabled="row === null" @click="showArticleVersion">历史版本</el-button>
          <el-button
            v-show="filters.tagStatus !== '8'"
            v-perm="['ARTIA003']"
            :disabled="row === null || row.deleteFlag !== 0"
            type="primary"
            @click="publishWeChat"
          >
            发布公众号
          </el-button>
          <el-button
            v-show="filters.tagStatus !== '8'"
            v-perm="['ARTID001']"
            :disabled="row === null || row.deleteFlag !== 0"
            type="danger"
            @click="deleteArticle"
          >
            删除
          </el-button>
          <el-button
            v-show="filters.tagStatus === '8'"
            v-perm="['ARTID002']"
            :disabled="row === null || row.deleteFlag !== 1"
            type="danger"
            @click="deleteArticle2"
          >
            删除
          </el-button>
          <el-button
            v-show="filters.tagStatus === '8' && tableData.length > 0"
            v-perm="['ARTID003']"
            type="danger"
            @click="deleteArticle3"
          >
            清空回收站
          </el-button>
          <el-button
            v-show="filters.tagStatus === '8'"
            v-perm="['ARTIU004']"
            :disabled="row === null || row.deleteFlag !== 1"
            type="primary"
            @click="resetArticle"
          >
            还原
          </el-button>
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
            <div class="article-cover-wrapper">
              <el-image
                class="article-cover"
                :src="getArticleCoverImage(scope.row.coverImageType, scope.row.coverImageList)"
                :preview-src-list="scope.row.coverImageList"
                fit="cover"
              >
                <div slot="error" class="image-slot">
                  <span v-if="scope.row.coverImageType === 1">默认封面</span>
                  <span v-else-if="scope.row.coverImageType === 5">无封面</span>
                  <span v-else-if="!scope.row.coverImageList || scope.row.coverImageList.length === 0">未配置</span>
                  <span v-else>加载失败</span>
                </div>
              </el-image>
            </div>
          </template>
        </el-table-column>
        <el-table-column label="文章标题" align="left" width="200" fixed="left">
          <template v-slot="scope">
            <div class="article-text-wrapper">
              <div class="article-title" :title="scope.row.title">
                {{ scope.row.title || '未命名文章' }}
              </div>
            </div>
          </template>
        </el-table-column>
        <el-table-column label="文章摘要" align="left" width="250">
          <template v-slot="scope">
            <div class="article-text-wrapper">
              <div class="article-summary" :title="scope.row.summary">
                {{ scope.row.summary }}
              </div>
            </div>
          </template>
        </el-table-column>
        <el-table-column label="文章类型" align="center" width="100">
          <template v-slot="scope">
            <span v-if="scope.row.articleType === 1">原创</span>
            <span v-else-if="scope.row.articleType === 2">转载</span>
            <span v-else-if="scope.row.articleType === 3">翻译</span>
            <span v-else>-</span>
          </template>
        </el-table-column>
        <el-table-column prop="categoryName" label="文章分类" width="100" align="center" />
        <el-table-column label="文章标签" width="200" align="center">
          <template v-slot="scope">
            <el-tag v-for="(item, index) in scope.row.tagNames" :key="index" type="info" style="margin-right:0.2rem;margin-top:0.2rem;">
              {{ item }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="文章状态" width="100" align="center">
          <template v-slot="scope">
            <el-tag v-if="scope.row.articleStatus === 1" type="info" effect="dark">草稿</el-tag>
            <el-tag v-else-if="scope.row.articleStatus === 2" type="primary" effect="dark">待审核</el-tag>
            <el-tag v-else-if="scope.row.articleStatus === 3" type="primary" effect="dark">待发布</el-tag>
            <el-tag v-else-if="scope.row.articleStatus === 4" type="danger" effect="dark">审核未通过</el-tag>
            <el-tag v-else-if="scope.row.articleStatus === 5" type="success" effect="dark">已发布</el-tag>
            <el-tag v-else-if="scope.row.articleStatus === 91" type="warning" effect="dark">已下线</el-tag>
            <span v-else>-</span>
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
        <el-table-column prop="version" label="版本" width="70" align="center">
          <template v-slot="{ row }">
            <span v-if="row.version !== null && row.version !== undefined && row.version !== 0">
              V{{ row.version }}
            </span>
            <span v-else />
          </template>
        </el-table-column>
        <el-table-column prop="publishTime" label="最新发布时间" width="160" align="center" />
        <el-table-column prop="updateTime" label="更新时间" width="160" align="center" />
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

    <!-- 操作日志 -->
    <operation-log-dialog
      ref="operationLogDialog"
      :dialog-visible="operationLogDialogVisible"
      @showOperationLogDone="showOperationLogDone"
    />
  </div>
</template>

<script>
import operationLogDialog from '@/views/admin/content/article/template/operationLogDialog'
export default {
  name: 'Article',
  components: {
    operationLogDialog
  },
  data() {
    return {
      statusTabs: [
        { key: '1', label: '全部', field: 'totalCount' },
        { key: '2', label: '草稿', field: 'draftCount' },
        { key: '3', label: '私密', field: 'privateCount' },
        { key: '4', label: '已发布', field: 'publicCount' },
        { key: '5', label: '待审核', field: 'pendingAuditCount' },
        { key: '6', label: '待发布', field: 'pendingPublishCount' },
        { key: '7', label: '待处理', field: 'pendingDealCount' },
        { key: '8', label: '回收站', field: 'recycleCount' }
      ],
      articleCountInfo: {
        // 全部
        totalCount: 0,
        // 草稿
        draftCount: 0,
        // 私密
        privateCount: 0,
        // 已发布
        publicCount: 0,
        // 待审核
        pendingAuditCount: 0,
        // 待发布
        pendingPublishCount: 0,
        // 待处理（比如审核不通过 / 待整改）
        pendingDealCount: 0,
        // 回收站
        recycleCount: 0
      },
      filters: {
        tagStatus: '1',
        title: null,
        slug: null,
        categoryId: null,
        tagId: null,
        articleType: null,
        articleStatus: null,
        articlePerm: null,
        commentFlag: null
      },
      categoryList: [],
      tagList: [],
      articleTypeList: [],
      articleStatusList: [],
      articlePermList: [],
      commentFlagList: [],
      tableData: [],
      tableLoading: false,
      row: null,
      total: 0,
      page: 1,
      pageSize: 5,
      operationLogDialogVisible: false,
      versionDialogVisible: false,

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
    this.loadTagList()
    this.loadArticleTypeList()
    this.loadArticleStatusList()
    this.loadArticlePermList()
    this.loadCommentFlagList()
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
    loadTagList() {
      this.$mapi.tag.queryPublicTagList().then(({ data }) => {
        this.tagList = data
      }).catch(_ => {
        this.tagList = []
      })
    },
    loadArticleTypeList() {
      this.$mapi.communal.queryConfListByDictKey({ dictKey: 'ARTICLE_TYPE_CONFIG' }).then(res => {
        const { data } = res
        this.articleTypeList = data
      }).catch(_ => {
        this.articleTypeList = []
      })
    },
    loadArticleStatusList() {
      this.$mapi.communal.queryConfListByDictKey({ dictKey: 'ARTICLE_STATUS_CONFIG' }).then(res => {
        const { data } = res
        this.articleStatusList = data
      }).catch(_ => {
        this.articleStatusList = []
      })
    },
    loadArticlePermList() {
      this.$mapi.communal.queryConfListByDictKey({ dictKey: 'ARTICLE_PERM_CONFIG' }).then(res => {
        const { data } = res
        this.articlePermList = data
      }).catch(_ => {
        this.articlePermList = []
      })
    },
    loadCommentFlagList() {
      this.$mapi.communal.queryConfListByDictKey({ dictKey: 'ARTICLE_COMMENT_FLAG_CONFIG' }).then(res => {
        const { data } = res
        this.commentFlagList = data
      }).catch(_ => {
        this.commentFlagList = []
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
    showArticleDetail() {
      this.previewArticleDialogVisible = true
      this.$nextTick(() => {
        this.$refs.previewArticleRef.initData(2, this.row.id)
      })
    },
    closePreviewArticleDialog() {
      this.previewArticleDialogVisible = false
    },
    // 新增与编辑
    addArticle() {
      this.$router.push({ name: 'AddOrEditArticle' })
    },
    editArticle() {
      if (this.row === null) {
        this.$message.error('请选择要编辑的文章')
        return
      }
      this.$router.push({ name: 'AddOrEditArticle', query: { articleId: this.row.id }})
    },
    // 查看操作日志
    showArticleLog() {
      if (this.row === null) {
        this.$message.error('请选择要查看的文章')
        return
      }

      this.operationLogDialogVisible = true
      this.$refs.operationLogDialog.initData(this.row.id)
    },
    showOperationLogDone() {
      this.operationLogDialogVisible = false
    },
    // 查看发布版本
    showArticleVersion() {
      if (this.row === null) {
        this.$message.error('请选择要查看的文章')
        return
      }

      this.versionDialogVisible = true
      this.$refs.versionDialog.initData(this.row.id)
    },
    showArticleVersionDone() {
      this.versionDialogVisible = false
    },
    publishWeChat() {
      if (this.row === null) {
        this.$message.error('请选择要发布的文章')
        return
      }

      this.$message.warning('暂不支持，功能完善中')
    },
    // 删除
    deleteArticle() {
      if (this.row === null) {
        this.$message.error('请选择要删除的文章')
        return
      }
      this.$confirm('此操作将移动文章【' + this.row.title + '】到回收站, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.$mapi.article.deleteArticle({ articleId: this.row.id }).then(res => {
          this.$message.success(res.message)
          this.listTableData()
        })
      })
    },
    deleteArticle2() {
      if (this.row === null) {
        this.$message.error('请选择要删除的文章')
        return
      }
      this.$confirm('此操作将永久删除文章【' + this.row.title + '】, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.$mapi.article.deleteArticle2({ articleId: this.row.id }).then(res => {
          this.$message.success(res.message)
          this.listTableData()
        })
      })
    },
    deleteArticle3() {
      this.$confirm('此操作将彻底删除回收站的文章, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.$mapi.article.deleteArticle3().then(res => {
          this.$message.success(res.message)
          this.listTableData()
        })
      })
    },
    // 还原文章
    resetArticle() {
      this.$mapi.article.resetArticle({ articleId: this.row.id }).then(res => {
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

/* 封面容器：水平、垂直居中 */
.article-cover-wrapper {
  display: flex;
  align-items: center;
  justify-content: center;
  width: 100%;
  min-height: 90px;
}

/* 文章封面 */
.article-cover {
  display: block;
  width: 100%;
  height: 90px;
  border-radius: 4px;
}

/* 标题和摘要容器：垂直居中 */
.article-text-wrapper {
  display: flex;
  align-items: center;
  width: 100%;
  min-height: 90px;
}

/* 文章标题 */
.article-title {
  display: -webkit-box;
  -webkit-box-orient: vertical;
  -webkit-line-clamp: 4;
  overflow: hidden;
  overflow-wrap: break-word;
  width: 100%;
  line-height: 20px;
  max-height: 80px;
}

/* 文章摘要 */
.article-summary {
  display: -webkit-box;
  -webkit-box-orient: vertical;
  -webkit-line-clamp: 4;
  overflow: hidden;
  overflow-wrap: break-word;
  width: 100%;
  line-height: 20px;
  max-height: 80px;
}

/* 封面加载失败时的占位区域 */
::v-deep .article-cover .image-slot {
  display: flex;
  align-items: center;
  justify-content: center;
  width: 100%;
  height: 100%;
  background: #f5f7fa;
  color: #909399;
  font-size: 14px;
}
</style>
