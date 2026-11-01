<template>
  <el-dialog
    title="发布微信公众号"
    :visible.sync="dialogVisible"
    :fullscreen="isMobile"
    :close-on-click-modal="false"
    :close-on-press-escape="true"
    :before-close="handleClose"
    append-to-body
    width="600px"
  >
    <el-form ref="syncWechatForm" :loading="pageLoading" :inline="false" :model="formData" :rules="formRules" label-width="120px">
      <el-form-item label="文章id">
        <el-input v-model="formData.articleId" readonly disabled />
      </el-form-item>
      <el-form-item label="同步版本" prop="articleVersion">
        <el-select v-model="formData.articleVersion" placeholder="请选择微信公众号同步版本">
          <el-option v-for="item in articleVersionList" :key="item" :label="'V' + item" :value="item" />
        </el-select>
      </el-form-item>
      <el-form-item label="文章标题" prop="wechatTitle">
        <el-input v-model="formData.wechatTitle" maxlength="32" show-word-limit placeholder="请输入微信公众号文章标题" />
      </el-form-item>
      <el-form-item label="作者昵称" prop="wechatAuthor">
        <el-input v-model="formData.wechatAuthor" maxlength="16" show-word-limit placeholder="请输入微信公众号文章作者" />
      </el-form-item>
      <el-form-item label="文章摘要" prop="wechatDigest">
        <el-input v-model="formData.wechatDigest" type="textarea" :rows="5" maxlength="120" show-word-limit placeholder="请输入微信公众号文章摘要，不填写时自动提取正文" />
      </el-form-item>
      <el-form-item label="文章封面Id">
        <el-input v-model="formData.thumbMediaId" readonly disabled />
      </el-form-item>
      <el-form-item label="文章封面地址">
        <el-input v-model="formData.thumbMediaUrl" readonly disabled />
      </el-form-item>
      <el-form-item label="封面图片">
        <muses-image-crop-upload
          v-model="formData.wechatCoverImageUrl"
          :aspect-ratio="1.8"
          :output-width="720"
          :output-height="400"
          :preview-width="260"
          :preview-height="145"
          :min-width="360"
          :min-height="200"
          :max-size="20"
          @success="setWechatCoverImageId"
        />
        <div class="image-url">
          <div class="form-tip">
            建议比例 16:9，封面图片将上传到微信公众号素材库
          </div>
        </div>
      </el-form-item>
    </el-form>
    <div slot="footer">
      <el-button @click="doClose">取消</el-button>
      <el-button type="primary" :loading="submitLoading1" @click="handleSubmit1">保存同步配置</el-button>
      <el-button type="primary" :loading="submitLoading2" :disabled="!step1Finish" @click="handleSubmit2">提交同步动作</el-button>
    </div>
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
      submitLoading1: false,
      step1Finish: false,
      submitLoading2: false,
      articleVersionList: [],
      formData: {
        articleId: null,
        articleVersion: null,
        wechatCoverImageId: null,
        wechatCoverImageUrl: null,
        thumbMediaId: null,
        thumbMediaUrl: null,
        wechatTitle: null,
        wechatAuthor: null,
        wechatDigest: null
      },
      formRules: {
        articleVersion: [
          { required: true, message: '请选择同步版本', trigger: 'blur' }
        ],
        wechatTitle: [
          { required: true, message: '请输入微信公众号文章标题', trigger: 'blur' },
          { max: 32, message: '标题不能超过32个字符', trigger: 'blur' }
        ],
        wechatAuthor: [
          { max: 16, message: '标题不能超过16个字符', trigger: 'blur' }
        ],
        digest: [
          { required: true, message: '请输入微信公众号文章摘要', trigger: 'blur' },
          { max: 120, message: '摘要不能超过120个字符', trigger: 'blur' }
        ]
      }
    }
  },
  computed: {
    isMobile() {
      return this.$store.state.app.device === 'mobile'
    }
  },
  methods: {
    initData(articleId) {
      try {
        this.pageLoading = true
        this.queryWeChatSyncConfig(articleId)
        this.queryArticlePublishVersions(articleId)
      } finally {
        this.pageLoading = false
      }
    },
    queryWeChatSyncConfig(articleId) {
      this.$mapi.article.queryWeChatSyncConfig({ articleId }).then(({ data }) => {
        if (data) {
          Object.keys(this.formData).forEach(key => { this.formData[key] = data[key] })
        } else {
          Object.keys(this.formData).forEach(key => { this.formData[key] = null })
          this.formData.articleId = articleId
          this.queryArticleCurrentDetail(articleId)
        }
      }).catch(_ => {
        this.doClose()
      })
    },
    queryArticlePublishVersions(articleId) {
      this.$mapi.article.queryArticleVersionList({ articleId }).then(({ data }) => {
        if (data == null || !Array.isArray(data) || data.length === 0) {
          this.$message.warning('不存在已发布版本，请先发布文章')
          this.doClose()
        } else {
          this.articleVersionList = data
        }
      }).catch(_ => {
        this.doClose()
      })
    },
    queryArticleCurrentDetail(articleId) {
      this.$mapi.article.queryMyArticleDetail({ articleId }).then(({ data }) => {
        if (!data) {
          this.$message.error('文章不存在')
          return
        }

        if (data.deleteFlag != null && data.deleteFlag !== 0) {
          this.$message.error('文章处于回收站中，不允许发布微信公众号')
          return
        }

        this.formData.wechatTitle = data.title
        this.formData.wechatDigest = data.summary
        this.formData.wechatAuthor = data.authorName
      }).catch(_ => {
        this.doClose()
      })
    },
    // 回填文章上传信息
    setWechatCoverImageId(file) {
      this.formData.wechatCoverImageUrl = file['fileAccessUrl']
      this.formData.wechatCoverImageId = file['fileId']
    },
    // 保存公众号同步配置信息
    handleSubmit1() {
      if (this.submitLoading1) {
        return
      }
      this.$refs.syncWechatForm.validate((valid) => {
        if (valid) {
          this.submitLoading1 = true
          const param = {
            articleId: this.formData.articleId,
            articleVersion: this.formData.articleVersion,
            wechatCoverImageId: this.formData.wechatCoverImageId,
            wechatTitle: this.formData.wechatTitle,
            wechatAuthor: this.formData.wechatAuthor,
            wechatDigest: this.formData.wechatDigest
          }
          this.$mapi.article.saveWeChatSyncConfig(param).then(res => {
            this.$message.success(res.message)
            this.queryWeChatSyncConfig(this.formData.articleId)
            this.step1Finish = true
          }).finally(_ => {
            this.submitLoading1 = false
          })
        }
      })
    },
    // 提交公众号同步动作
    handleSubmit2() {
      if (this.submitLoading1) {
        return
      }

      if (!this.step1Finish) {
        this.$message.warning('请先保存公众号同步配置信息')
        return
      }

      this.submitLoading2 = true
      this.$mapi.article.publishWeChat({ articleId: this.formData.articleId }).then(res => {
        this.$message.success(res.message)
        this.doClose()
      }).finally(_ => {
        this.submitLoading2 = false
      })
    },
    // 关闭
    doClose() {
      this.pageLoading = false
      this.submitLoading1 = false
      this.submitLoading2 = false
      this.step1Finish = false
      this.articleVersionList = []
      Object.keys(this.formData).forEach(key => {
        this.formData[key] = null
      })
      this.$refs.syncWechatForm && this.$refs.syncWechatForm.resetFields()
      this.$emit('publishWechatDone')
    },
    handleClose() {
      this.doClose()
    }
  }
}
</script>

<style scoped>
.form-tip {
  margin-top: 7px;
  color: #909399;
  font-size: 12px;
  line-height: 18px;
}
</style>
