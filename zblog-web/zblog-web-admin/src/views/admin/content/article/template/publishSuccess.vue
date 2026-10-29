<template>
  <div class="app-container publish-result-page">
    <div class="components-container">
      <el-page-header title="文章管理" content="提交结果" class="page-header" @back="goArticlePage" />

      <div class="result-card">
        <div class="result-icon" :class="resultClass">
          <i :class="resultIcon" />
        </div>

        <div class="result-title">
          {{ resultTitle }}
        </div>

        <div class="result-description">
          {{ resultDescription }}
        </div>

        <div v-if="articleId" class="article-info">
          <span class="article-info-label">文章编号</span>
          <span class="article-info-value">{{ articleId }}</span>
        </div>

        <div v-if="isPendingReview" class="review-tips">
          <i class="el-icon-info" />
          <span>
            文章审核通过后才会正式对外展示，你可以在文章管理中查看审核进度。
          </span>
        </div>

        <div class="result-actions">
          <el-button
            v-if="articleId"
            type="primary"
            icon="el-icon-view"
            @click="openPreviewArticleDialog"
          >
            {{ isPublished ? '查看文章' : '预览文章' }}
          </el-button>

          <el-button v-if="articleId" icon="el-icon-edit" @click="reEdit">
            重新编辑
          </el-button>

          <el-button v-if="articleId" icon="el-icon-document-copy" @click="copyLink">
            {{ isPublished ? '复制文章链接' : '复制预览链接' }}
          </el-button>

          <el-button type="success" plain icon="el-icon-plus" @click="addMore">
            再写一篇
          </el-button>
        </div>

        <div class="back-link">
          <el-link type="primary" :underline="false" @click="goArticlePage">
            返回文章管理
            <i class="el-icon-arrow-right" />
          </el-link>
        </div>
      </div>
    </div>

    <!-- 预览 -->
    <preview-dialog
      ref="previewDialogRef"
      :dialog-visible="previewArticleDialogVisible"
      @previewDone="closePreviewArticleDialog"
    />
  </div>
</template>

<script>
import previewDialog from '@/views/admin/content/article/template/previewDialog'

const ARTICLE_STATUS = {
  PENDING_REVIEW: 2,
  PUBLISHED: 5
}

export default {
  name: 'PublishSuccess',
  components: {
    previewDialog
  },
  data() {
    return {
      articleId: '',
      articleStatus: null,
      previewArticleDialogVisible: false,
      previewInitialized: false
    }
  },

  computed: {
    isPublished() {
      return Number(this.articleStatus) === ARTICLE_STATUS.PUBLISHED
    },

    isPendingReview() {
      return Number(this.articleStatus) === ARTICLE_STATUS.PENDING_REVIEW
    },

    resultClass() {
      if (this.isPublished) {
        return 'result-icon-success'
      }
      if (this.isPendingReview) {
        return 'result-icon-pending'
      }
      return 'result-icon-default'
    },
    resultIcon() {
      if (this.isPublished) {
        return 'el-icon-circle-check'
      }
      if (this.isPendingReview) {
        return 'el-icon-time'
      }
      return 'el-icon-document'
    },
    resultTitle() {
      if (this.isPublished) {
        return '文章发布成功'
      }
      if (this.isPendingReview) {
        return '文章提交成功，等待审核'
      }
      return '文章操作已完成'
    },
    resultDescription() {
      if (this.isPublished) {
        return '你的文章已正式发布，可以查看文章或复制链接分享给其他人。'
      }
      if (this.isPendingReview) {
        return '文章已提交审核，审核通过后将正式对外展示。'
      }
      return '你可以返回文章管理查看文章当前状态。'
    }
  },
  created() {
    this.initPage()
  },
  methods: {
    // 初始化页面
    initPage() {
      const articleId = this.$route.params.articleId
      const status = this.$route.query.status

      if (!articleId) {
        this.$message.warning('文章编号不存在，请返回文章管理')
        this.goArticlePage()
        return
      }

      this.articleId = articleId

      if (status !== undefined && status !== '') {
        this.articleStatus = Number(status)
        return
      }

      // 未传递状态时，建议在此调用项目现有的文章详情接口，
      // 根据接口返回的 articleStatus 设置 this.articleStatus。
      // 不建议仅根据文章权限推断发布状态。
    },

    // 返回文章管理页面
    goArticlePage() {
      this.$router.push({
        name: 'Article',
        query: {
          refresh: 't'
        }
      })
    },

    // 打开文章预览
    openPreviewArticleDialog() {
      if (!this.articleId) {
        this.$message.warning('文章编号不存在')
        return
      }

      this.previewArticleDialogVisible = true
      this.$refs.previewDialogRef.initData(this.articleId)
    },
    // 关闭预览窗口
    closePreviewArticleDialog() {
      this.previewArticleDialogVisible = false
    },

    // 重新编辑文章
    reEdit() {
      if (!this.articleId) {
        this.$message.warning('文章编号不存在')
        return
      }

      this.$router.push({ name: 'AddOrEditArticle', query: { articleId: this.articleId }})
    },

    // 新建文章
    addMore() {
      this.$router.push({ name: 'AddOrEditArticle' })
    },

    // 复制文章链接
    async copyLink() {
      if (!this.articleId) {
        this.$message.warning('文章编号不存在')
        return
      }

      if (!this.isPublished) {
        this.$message.info(
          '文章尚未正式发布，暂不提供公开访问链接。你可以通过文章预览查看内容。'
        )
        return
      }

      // TODO
      const baseUrl = this.$store.state.settings.webApi
      const articleUrl = baseUrl.replace(/\/+$/, '') + '/articles/' + encodeURIComponent(this.articleId)

      try {
        if (navigator.clipboard && window.isSecureContext) {
          await navigator.clipboard.writeText(articleUrl)
        } else {
          this.copyTextFallback(articleUrl)
        }
        this.$message.success('文章链接已复制到剪贴板')
      } catch (error) {
        this.$message.error('复制失败，请手动复制文章链接：' + articleUrl)
      }
    },
    // 兼容不支持 Clipboard API 的浏览器
    copyTextFallback(text) {
      const textArea = document.createElement('textarea')
      textArea.value = text
      textArea.readOnly = true
      textArea.style.position = 'fixed'
      textArea.style.left = '-9999px'
      textArea.style.top = '0'
      document.body.appendChild(textArea)

      try {
        textArea.focus()
        textArea.select()
        if (textArea.setSelectionRange) {
          textArea.setSelectionRange(0, textArea.value.length)
        }

        const copied = document.execCommand('copy')
        if (!copied) {
          throw new Error('浏览器复制操作失败')
        }
      } finally {
        document.body.removeChild(textArea)
      }
    }
  }
}
</script>

<style scoped>
.publish-result-page {
  min-height: calc(100vh - 84px);
}

.page-header {
  padding-bottom: 16px;
}

.result-card {
  box-sizing: border-box;
  width: 100%;
  max-width: 900px;
  min-height: 480px;
  margin: 24px auto;
  padding: 56px 48px 40px;
  background: #fff;
  border: 1px solid #ebeef5;
  border-radius: 12px;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.035);
  text-align: center;
}

.result-icon {
  display: flex;
  align-items: center;
  justify-content: center;
  width: 76px;
  height: 76px;
  margin: 0 auto 24px;
  border-radius: 50%;
  font-size: 42px;
}

.result-icon-success {
  color: #67c23a;
  background: #f0f9eb;
}

.result-icon-pending {
  color: #e6a23c;
  background: #fdf6ec;
}

.result-icon-default {
  color: #409eff;
  background: #ecf5ff;
}

.result-title {
  margin-bottom: 14px;
  color: #303133;
  font-size: 24px;
  font-weight: 600;
  line-height: 1.5;
}

.result-description {
  max-width: 560px;
  margin: 0 auto;
  color: #606266;
  font-size: 14px;
  line-height: 1.9;
}

.article-info {
  display: inline-flex;
  align-items: center;
  margin-top: 24px;
  padding: 8px 16px;
  background: #f5f7fa;
  border-radius: 6px;
  font-size: 13px;
}

.article-info-label {
  margin-right: 12px;
  color: #909399;
}

.article-info-value {
  color: #606266;
  font-weight: 500;
}

.review-tips {
  display: flex;
  align-items: flex-start;
  justify-content: center;
  max-width: 580px;
  margin: 24px auto 0;
  padding: 12px 16px;
  color: #8a6d3b;
  background: #fdf6ec;
  border: 1px solid #faecd8;
  border-radius: 6px;
  font-size: 13px;
  line-height: 1.8;
  text-align: left;
}

.review-tips i {
  flex-shrink: 0;
  margin: 3px 8px 0 0;
  font-size: 16px;
}

.result-actions {
  display: flex;
  flex-wrap: wrap;
  align-items: center;
  justify-content: center;
  gap: 12px;
  margin-top: 36px;
}

.result-actions .el-button + .el-button {
  margin-left: 0;
}

.back-link {
  margin-top: 28px;
}

@media screen and (max-width: 768px) {
  .result-card {
    min-height: auto;
    margin: 16px auto;
    padding: 36px 20px;
  }

  .result-title {
    font-size: 21px;
  }

  .result-actions {
    flex-direction: column;
    align-items: stretch;
  }

  .result-actions .el-button {
    width: 100%;
    margin-left: 0;
  }
}
</style>
