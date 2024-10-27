<template>
  <div class="app-container">
    <div class="components-container">
      <el-page-header title="文章管理" content="" style="padding-bottom: 10px;" @back="goArticlePage" />
      <div class="success-modal view">
        <div class="success-modal-content">
          <!-- <div class="content-title">发布成功，正在审核中</div> -->
          <img :src="publishSuccessImg" alt="">
        </div>
        <div class="success-modal-footer">
          <a class="success-modal-btn" @click="openPreviewArticleDialog">查看文章</a>
          <a class="success-modal-btn" @click="reEdit">重新编辑</a>
          <div class="success-modal-btn" @click="copyLink">复制链接</div>
          <a class="success-modal-btn" @click="addMore">再写一篇</a>
        </div>
      </div>
    </div>

    <el-dialog :visible.sync="previewArticleDialogVisible" title="预览" width="900px">
      <previewArticle ref="previewArticleRef" />
      <div slot="footer" class="dialog-footer">
        <el-button type="default" @click="closePreviewArticleDialog">关 闭</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import PublishSuccess from '@/assets/images/publish-success.png'
import previewArticle from '@/views/admin/web/article/template/previewArticle'
export default {
  name: 'PublishSuccess',
  components: {
    previewArticle
  },
  data() {
    return {
      articleId: '',
      publishSuccessImg: PublishSuccess,
      previewArticleDialogVisible: false
    }
  },
  mounted() {
    this.articleId = this.$route.params.articleId
  },
  methods: {
    goArticlePage() {
      this.$router.push({
        name: 'Article',
        query: { refresh: 't' }
      })
    },
    openPreviewArticleDialog() {
      this.previewArticleDialogVisible = true
      this.$nextTick(() => {
        this.$refs.previewArticleRef.initData(2, this.articleId)
      })
    },
    closePreviewArticleDialog() {
      this.previewArticleDialogVisible = false
    },
    reEdit() {
      this.$router.push({
        name: 'AddArticle',
        query: {
          articleId: this.articleId
        }
      })
    },
    addMore() {
      this.$router.push({
        name: 'AddArticle'
      })
    },
    copyLink() {
      const textArea = document.createElement('textarea')
      document.body.appendChild(textArea)
      textArea.readOnly = true
      textArea.style.opacity = '0'
      textArea.value = this.$store.state.settings.webApi + '/articles/' + this.articleId
      textArea.select()
      if (textArea.setSelectionRange) {
        textArea.setSelectionRange(0, textArea.value.length)
      } else {
        textArea.select()
      }
      document.execCommand('copy')
      this.$message.success('链接已成功复制到剪贴板')
      document.body.removeChild(textArea)
    }
  }
}
</script>

<style scoped>
.success-modal {
  width: 816px;
  position: fixed;
  top: 50%;
  left: 50%;
  -webkit-transform: translate(-50%, -50%);
  transform: translate(-50%,-50%);
  padding: 24px;
  /* background: #fff;
  -webkit-box-shadow: 0 1px 8px 0 rgb(0 0 0 / 5%);
  box-shadow: 0 1px 8px 0 rgb(0 0 0 / 5%);
  border-radius: 2px;
  border: 1px solid #f0f0f5; */
}
.success-modal .success-modal-content {
  margin-top: 20px;
  text-align: center;
}
.success-modal .success-modal-content .content-title {
  height: 28px;
  font-size: 20px;
  font-weight: 500;
  color: #222226;
  line-height: 28px;
}
.success-modal .success-modal-content img {
  width: 230px;
  height: 275px;
  /* margin-top: 24px; */
  margin-top: 5px;
  margin-bottom: 60px;
}
.success-modal .success-modal-footer {
  padding: 0 48px;
  display: -webkit-box;
  display: -ms-flexbox;
  display: flex;
  -webkit-box-pack: justify;
  -ms-flex-pack: justify;
  justify-content: space-between;
  -webkit-box-align: center;
  -ms-flex-align: center;
  align-items: center;
}
.success-modal .success-modal-footer .success-modal-btn {
  width: 116px;
  height: 32px;
  background: #fff;
  border-radius: 15px;
  border: 1px solid #ccccd8;
  font-size: 14px;
  font-weight: 400;
  color: #222226;
  line-height: 30px;
  text-align: center;
  cursor: pointer;
}
</style>
