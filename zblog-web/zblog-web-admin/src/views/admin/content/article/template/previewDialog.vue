<template>
  <el-dialog
    title="文章预览"
    :visible.sync="dialogVisible"
    :fullscreen="true"
    :append-to-body="true"
    :close-on-click-modal="false"
    custom-class="article-preview-dialog"
    @closed="handleClose"
  >
    <div v-loading="pageLoading" class="preview-container">
      <div class="preview-paper">
        <header class="preview-header">
          <div class="preview-badge">
            <i class="el-icon-view" />
            <span>预览模式</span>
          </div>

          <h1 class="preview-title">
            {{ article.title || '无标题文章' }}
          </h1>

          <div class="preview-meta">
            <span v-if="article.authorName">
              <i class="el-icon-user" />
              {{ article.authorName }}
            </span>
            <span v-if="article.categoryName">
              <i class="el-icon-folder" />
              {{ article.categoryName }}
            </span>
            <span>
              <i class="el-icon-document" />
              {{ article.totalCount }} 字
            </span>
          </div>

          <div v-if="article.summary" class="preview-summary">
            <div class="summary-label">文章摘要</div>
            <div class="summary-content">{{ article.summary }}</div>
          </div>
        </header>
        <main class="preview-body">
          <div v-if="article.contentHtml" class="markdown-body" v-html="article.contentHtml" />
          <el-empty
            v-else
            description="暂无文章内容"
            :image-size="100"
          />
        </main>

        <footer class="preview-footer">
          <span>文章预览 · 内容尚未正式发布</span>
        </footer>
      </div>
    </div>

    <span slot="footer" class="dialog-footer">
      <el-button @click="dialogVisible = false">
        关闭预览
      </el-button>
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
      pageLoading: false,
      article: {}
    }
  },
  computed: {

  },
  watch: {
    visible(value) {
      this.dialogVisible = value
    },
    dialogVisible(value) {
      this.$emit('update:visible', value)
    }
  },
  methods: {
    // 初始化文章数据
    initData(articleId) {
      if (articleId === null || articleId === undefined || articleId === '') {
        this.$message.warning('请先选择文章')
        return
      }

      this.queryArticleDetail(articleId)
    },
    // 查询文章详情
    queryArticleDetail(articleId) {
      this.pageLoading = true
      this.$mapi.article.queryMyArticleDetail({ articleId }).then(({ data }) => {
        if (!data) {
          this.$message.error('文章不存在')
          return
        }

        this.article = this.normalizeArticleData(data)
      }).catch(error => {
        console.log('get article detail failed', error)
        this.$message.error('加载文章信息失败，请刷新浏览器再试！')
        this.doClose()
      }).finally(() => {
        this.pageLoading = false
      })
    },
    // 将后端返回的数据转换成页面模型
    normalizeArticleData(data) {
      return {
        id: data.id,
        title: data.title,
        slug: data.slug,
        summary: data.summary,
        contentMd: data.contentMd,
        contentHtml: data.contentHtml,
        categoryId: data.categoryId,
        tags: Array.isArray(data.tags) ? data.tags : [],
        seoTitle: data.seoTitle || '',
        seoKeywords: data.seoKeywords || '',
        seoDescription: data.seoDescription || '',
        articleType: data.articleType || 1,
        articlePerm: data.articlePerm == null ? 1 : data.articlePerm,
        sourceName: data.sourceName || '',
        sourceAuthor: data.sourceAuthor || '',
        sourceUrl: data.sourceUrl || '',
        commentFlag: data.commentFlag == null ? true : Boolean(data.commentFlag),
        coverImageType: data.coverImageType == null ? 1 : data.coverImageType,
        coverImages: Array.isArray(data.coverImages) ? data.coverImages : [],
        publishTime: data.publishTime || '',
        version: data.version || 0
      }
    },
    // 关闭模态框
    doClose() {
      this.pageLoading = false
      this.article = {}
      this.$emit('previewDone')
    },
    handleClose() {
      this.doClose()
    }
  }
}
</script>

<style src="@kangc/v-md-editor/lib/theme/style/vuepress.css"></style>

<style>
.article-preview-dialog {
  display: flex;
  flex-direction: column;
  margin: 0 !important;
  width: 100%;
  height: 100%;
  overflow: hidden;
}

.article-preview-dialog .el-dialog__header {
  flex-shrink: 0;
  border-bottom: 1px solid #ebeef5;
}

.article-preview-dialog .el-dialog__body {
  flex: 1;
  min-height: 0;
  padding: 0;
  overflow: hidden;
  background: #f5f6f8;
}

.article-preview-dialog .el-dialog__footer {
  flex-shrink: 0;
  border-top: 1px solid #ebeef5;
}

.preview-container {
  height: 100%;
  overflow-y: auto;
  padding: 30px 20px;
  box-sizing: border-box;
}

.preview-paper {
  max-width: 900px;
  min-height: 100%;
  margin: 0 auto;
  padding: 45px 65px;
  box-sizing: border-box;
  background: #fff;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.05);
}

.preview-header {
  padding-bottom: 28px;
  border-bottom: 1px solid #ebeef5;
}

.preview-badge {
  display: inline-flex;
  align-items: center;
  gap: 6px;
  padding: 5px 10px;
  border-radius: 4px;
  background: #ecf5ff;
  color: #409eff;
  font-size: 12px;
}

.preview-title {
  margin: 24px 0 18px;
  color: #303133;
  font-size: 30px;
  font-weight: 600;
  line-height: 1.5;
  overflow-wrap: anywhere;
}

.preview-meta {
  display: flex;
  flex-wrap: wrap;
  gap: 18px;
  color: #909399;
  font-size: 13px;
}

.preview-meta i {
  margin-right: 4px;
}

.preview-summary {
  margin-top: 25px;
  padding: 16px 20px;
  border-left: 3px solid #409eff;
  background: #f8fafc;
  color: #606266;
  line-height: 1.8;
  overflow-wrap: anywhere;
}

.summary-label {
  margin-bottom: 5px;
  color: #303133;
  font-size: 13px;
  font-weight: 600;
}

.summary-content {
  white-space: pre-wrap;
}

.preview-body {
  padding-top: 30px;
  color: #303133;
}

.markdown-body {
  font-size: 16px;
  line-height: 1.9;
  overflow-wrap: anywhere;
}

.markdown-body img {
  max-width: 100%;
  height: auto;
}

.markdown-body pre {
  max-width: 100%;
  overflow-x: auto;
}

.markdown-body table {
  display: block;
  max-width: 100%;
  overflow-x: auto;
  border-collapse: collapse;
}

.markdown-body iframe {
  max-width: 100%;
}

.preview-footer {
  margin-top: 60px;
  padding-top: 20px;
  border-top: 1px solid #ebeef5;
  color: #c0c4cc;
  font-size: 12px;
  text-align: center;
}

@media screen and (max-width: 768px) {
  .preview-container {
    padding: 12px;
  }

  .preview-paper {
    padding: 25px 18px;
  }

  .preview-title {
    font-size: 24px;
  }

  .markdown-body {
    font-size: 15px;
  }
}
</style>
