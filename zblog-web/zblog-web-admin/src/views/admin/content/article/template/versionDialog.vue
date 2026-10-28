<template>
  <el-dialog
    title="文章版本记录"
    :visible.sync="dialogVisible"
    :fullscreen="isMobile"
    :destroy-on-close="true"
    :close-on-click-modal="true"
    :close-on-press-escape="true"
    :before-close="handleClose"
    append-to-body
    width="90%"
    top="8vh"
  >
    <div class="version-container">
      <div class="version-header">
        <div class="version-info">
          <span class="article-label">文章 ID：</span>
          <span>{{ filters.articleId || '-' }}</span>
          <span class="total-text">共 {{ total }} 条记录</span>
        </div>
        <el-button type="primary" icon="el-icon-refresh" :loading="tableLoading" @click="search">刷新</el-button>
      </div>

      <el-table
        ref="articleVersionTable"
        v-loading="tableLoading"
        :data="tableData"
        border
        stripe
        row-key="id"
        height="calc(80vh - 260px)"
      >
        <el-table-column label="文章封面" align="center" width="180" fixed="left">
          <template v-slot="scope">
            <div class="article-cover-wrapper">
              <el-image
                class="article-cover"
                :src="getArticleCoverImage(scope.row.coverImageType, scope.row['coverImageList'])"
                :preview-src-list="scope.row['coverImageList']"
                fit="cover"
              >
                <div slot="error" class="image-slot">
                  <span v-if="scope.row.coverImageType === 1">默认封面</span>
                  <span v-else-if="scope.row.coverImageType === 5">无封面</span>
                  <span v-else-if="!scope.row['coverImageList'] || scope.row['coverImageList'].length === 0">未配置</span>
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
                {{ scope.row.title }}
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
            <el-tag v-for="(item, index) in scope.row['tagNames']" :key="index" type="info" style="margin-right:0.2rem;margin-top:0.2rem;">
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
        <el-table-column prop="version" label="当前发布版本" width="100" align="center">
          <template v-slot="{ row }">
            <span v-if="row.version !== null && row.version !== undefined && row.version !== 0">
              V{{ row.version }}
            </span>
            <span v-else>-</span>
          </template>
        </el-table-column>
        <el-table-column prop="publishTime" label="发布时间" width="160" align="center" />
      </el-table>

      <div class="version-pagination">
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

    <span slot="footer">
      <el-button @click="dialogVisible = false">关闭</el-button>
    </span>
  </el-dialog>
</template>

<script>
export default {
  props: {
    dialogVisible: {
      type: Boolean,
      default: false
    }
  },
  data() {
    return {
      filters: {
        articleId: null
      },
      tableData: [],
      tableLoading: false,
      total: 0,
      page: 1,
      pageSize: 10
    }
  },
  computed: {
    isMobile() {
      return this.$store.state.app.device === 'mobile'
    }
  },
  methods: {
    getArticleCoverImage(coverImageType, coverImageList) {
      if (coverImageType === 1 || coverImageType === 5) {
        return ''
      }
      if (Array.isArray(coverImageList) && coverImageList.length > 0) {
        return coverImageList[0]
      }
      return ''
    },
    // 加载表格数据
    initData(articleId) {
      if (articleId === null || articleId === undefined || articleId === '') {
        this.$message.warning('请先选择文章')
        return
      }

      this.filters.articleId = articleId
      this.search()
    },
    search() {
      this.page = 1
      this.pageSize = 10
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
      this.$mapi.article.pageArticleVersionList(param).then(res => {
        const { data } = res
        this.total = data.total
        this.tableData = data.records
      }).catch(_ => {
        this.total = 0
        this.tableData = []
      }).finally(() => {
        this.tableLoading = false
      })
    },
    // 关闭模态框
    doClose() {
      this.total = 0
      this.tableData = []
      this.tableLoading = false
      this.page = 1
      this.pageSize = 10
      this.filters.articleId = null
      this.$emit('showVersionDone')
    },
    handleClose() {
      this.doClose()
    }
  }
}
</script>

<style scoped>
.version-container {
  width: 100%;
}
.version-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  margin-bottom: 15px;
}
.version-info {
  display: flex;
  align-items: center;
  font-size: 13px;
  color: #606266;
}
.article-label {
  margin-right: 5px;
}
.total-text {
  margin-left: 20px;
  color: #909399;
}
.version-pagination {
  display: flex;
  justify-content: flex-end;
  margin-top: 18px;
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
