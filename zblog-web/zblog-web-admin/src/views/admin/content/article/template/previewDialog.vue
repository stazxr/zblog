
<template>
  <el-dialog
    title="文章预览"
    :visible="dialogVisible"
    :fullscreen="true"
    :append-to-body="true"
    :destroy-on-close="true"
    :close-on-click-modal="true"
    :close-on-press-escape="true"
    :before-close="handleClose"
    custom-class="article-preview-dialog"
  >
    <div v-loading="pageLoading" class="preview-container">
      <div class="preview-paper">
        <!-- 文章头部 -->
        <header class="preview-header">
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
              {{ article.wordsCount }} 字
            </span>
          </div>

          <div v-if="article.summary" class="preview-summary">
            <div class="summary-label">文章摘要</div>
            <div class="summary-content">{{ article.summary }}</div>
          </div>
        </header>

        <!-- 文章正文 -->
        <main class="preview-body">
          <muses-markdown-preview
            v-if="article.contentHtml"
            :html="article.contentHtml"
          />

          <el-empty
            v-else
            description="暂无文章内容"
            :image-size="100"
          />
        </main>
      </div>
    </div>
  </el-dialog>
</template>

<script>
export default {
  name: 'PreviewDialog',
  props: {
    dialogVisible: {
      type: Boolean,
      default: false
    }
  },
  data() {
    return {
      pageLoading: false,
      article: this.createEmptyArticle(),
      requestSequence: 0
    }
  },
  methods: {
    // 创建默认文章模型
    createEmptyArticle() {
      return {
        id: null,
        title: '',
        slug: '',
        summary: '',
        contentMd: '',
        contentHtml: '',
        authorName: '',
        categoryId: null,
        categoryName: '',
        tags: [],
        seoTitle: '',
        seoKeywords: '',
        seoDescription: '',
        articleType: 1,
        articlePerm: 1,
        sourceName: '',
        sourceAuthor: '',
        sourceUrl: '',
        commentFlag: true,
        coverImageType: 1,
        coverImages: [],
        publishTime: '',
        version: 0,
        wordsCount: 0
      }
    },
    // 初始化预览数据，由父组件通过 ref 调用
    initData(articleId) {
      if (articleId === null || articleId === undefined || articleId === '') {
        this.$message.warning('请先选择文章')
        return
      }

      this.queryArticleDetail(articleId)
    },
    // 查询文章详情
    queryArticleDetail(articleId) {
      const sequence = ++this.requestSequence

      this.pageLoading = true
      this.article = this.createEmptyArticle()
      this.$mapi.article.queryMyArticleDetail({ articleId }).then(({ data }) => {
        // 忽略已经过期的请求结果
        if (sequence !== this.requestSequence) {
          return
        }

        if (!data) {
          this.$message.error('文章不存在')
          this.doClose()
          return
        }

        this.article = this.normalizeArticleData(data)
      }).catch(error => {
        if (sequence !== this.requestSequence) {
          return
        }

        // eslint-disable-next-line no-console
        console.error('Get article detail failed:', error)
        this.$message.error('加载文章信息失败，请刷新浏览器再试！')
        this.doClose()
      }).finally(() => {
        if (sequence === this.requestSequence) {
          this.pageLoading = false
        }
      })
    },
    // 将后端返回的数据转换为页面模型
    normalizeArticleData(data) {
      return {
        id: data.id == null ? null : data.id,
        title: data.title || '',
        slug: data.slug || '',
        summary: data.summary || '',
        contentMd: data.contentMd || '',
        contentHtml: data.contentHtml || '',
        authorName: data.authorName || '',
        categoryId: data.categoryId == null ? null : data.categoryId,
        categoryName: data.categoryName || '',
        tags: Array.isArray(data.tags) ? data.tags : [],
        seoTitle: data.seoTitle || '',
        seoKeywords: data.seoKeywords || '',
        seoDescription: data.seoDescription || '',
        articleType: data.articleType == null ? 1 : data.articleType,
        articlePerm: data.articlePerm == null ? 1 : data.articlePerm,
        sourceName: data.sourceName || '',
        sourceAuthor: data.sourceAuthor || '',
        sourceUrl: data.sourceUrl || '',
        commentFlag: data.commentFlag,
        coverImageType: data.coverImageType,
        coverImages: Array.isArray(data.coverImages) ? data.coverImages : [],
        publishTime: data.publishTime || '',
        version: data.version || 0,
        wordsCount: data.wordsCount || 0
      }
    },
    // 弹窗关闭后清理数据
    doClose() {
      this.requestSequence++
      this.pageLoading = false
      this.article = this.createEmptyArticle()
      // 通知父组件关闭预览弹窗
      this.$emit('previewDone')
    },
    // 关闭弹窗
    handleClose() {
      this.doClose()
    }
  }
}
</script>

<style src="@kangc/v-md-editor/lib/style/preview-html.css"></style>
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

.article-preview-dialog .preview-container {
  height: 100%;
  overflow-y: auto;
  padding: 30px 20px;
  box-sizing: border-box;
}

.article-preview-dialog .preview-paper {
  max-width: 900px;
  min-height: 100%;
  margin: 0 auto;
  padding: 45px 65px;
  box-sizing: border-box;
  background: #fff;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.05);
}

.article-preview-dialog .preview-header {
  padding-bottom: 28px;
  border-bottom: 1px solid #ebeef5;
}

.article-preview-dialog .preview-title {
  margin: 24px 0 18px;
  color: #303133;
  font-size: 30px;
  font-weight: 600;
  line-height: 1.5;
  overflow-wrap: anywhere;
}

.article-preview-dialog .preview-meta {
  display: flex;
  flex-wrap: wrap;
  gap: 18px;
  color: #909399;
  font-size: 13px;
}

.article-preview-dialog .preview-meta i {
  margin-right: 4px;
}

.article-preview-dialog .preview-summary {
  margin-top: 25px;
  padding: 16px 20px;
  border-left: 3px solid #409eff;
  background: #f8fafc;
  color: #606266;
  line-height: 1.8;
  overflow-wrap: anywhere;
}

.article-preview-dialog .summary-label {
  margin-bottom: 5px;
  color: #303133;
  font-size: 13px;
  font-weight: 600;
}

.article-preview-dialog .summary-content {
  white-space: pre-wrap;
}

.article-preview-dialog .preview-body {
  color: #303133;
}

/* VuePress 主题的 Markdown 内容样式 */
.article-preview-dialog .vuepress-markdown-body {
  font-size: 16px;
  line-height: 1.9;
  overflow-wrap: anywhere;
}

.article-preview-dialog .vuepress-markdown-body img {
  max-width: 100%;
  height: auto;
}

.article-preview-dialog .vuepress-markdown-body pre {
  max-width: 100%;
  overflow-x: auto;
}

.article-preview-dialog .vuepress-markdown-body table {
  display: block;
  max-width: 100%;
  overflow-x: auto;
  border-collapse: collapse;
}

.article-preview-dialog .vuepress-markdown-body iframe {
  max-width: 100%;
}

@media screen and (max-width: 768px) {
  .article-preview-dialog .preview-container {
    padding: 12px;
  }

  .article-preview-dialog .preview-paper {
    padding: 25px 18px;
  }

  .article-preview-dialog .preview-title {
    font-size: 24px;
  }

  .article-preview-dialog .vuepress-markdown-body {
    font-size: 15px;
  }
}
</style>
