<template>
  <el-dialog
    title="发布微信公众号"
    :visible.sync="dialogVisible"
    width="600px"
    :close-on-click-modal="false"
    @closed="handleClosed"
  >
    <el-form
      ref="publishForm"
      :model="form"
      :rules="rules"
      :loading="loading"
      label-width="90px"
    >
      <el-form-item label="文章标题" prop="title">
        <el-input
          v-model="form.title"
          maxlength="32"
          show-word-limit
          placeholder="请输入微信公众号文章标题"
        />
      </el-form-item>

      <el-form-item label="文章摘要" prop="digest">
        <el-input
          v-model="form.digest"
          type="textarea"
          :rows="4"
          maxlength="128"
          show-word-limit
          placeholder="请输入微信公众号文章摘要，不填写时自动提取正文"
        />
      </el-form-item>

      <el-form-item label="封面图片" prop="coverImageId">
        <div class="wechat-cover">
          <div
            v-if="form.coverImageUrl"
            class="wechat-cover-preview"
          >
            <el-image
              :src="form.coverImageUrl"
              fit="cover"
              :preview-src-list="[form.coverImageUrl]"
            />
            <div class="wechat-cover-actions">
              <el-button
                type="text"
                size="mini"
                @click="selectCover"
              >
                更换封面
              </el-button>
            </div>
          </div>

          <el-button
            v-else
            icon="el-icon-plus"
            @click="selectCover"
          >
            选择封面
          </el-button>

          <div class="wechat-cover-tip">
            封面图片将上传到微信公众号素材库。
          </div>
        </div>
      </el-form-item>
    </el-form>

    <div slot="footer">
      <el-button @click="dialogVisible = false">
        取消
      </el-button>
      <el-button
        type="primary"
        :loading="submitLoading"
        @click="handleSubmit"
      >
        创建公众号草稿
      </el-button>
    </div>
  </el-dialog>
</template>

<script>
import { getWeChatPublishConfig, publishWeChatArticle } from '@/api/content/article'

export default {
  name: 'WeChatPublishDialog',
  data() {
    return {
      dialogVisible: false,
      loading: false,
      submitLoading: false,
      articleId: null,
      form: {
        articleId: null,
        title: '',
        digest: '',
        coverImageId: null,
        coverImageUrl: ''
      },
      rules: {
        title: [
          { required: true, message: '请输入微信公众号文章标题', trigger: 'blur' },
          { max: 32, message: '标题不能超过32个字符', trigger: 'blur' }
        ],
        digest: [
          { max: 128, message: '摘要不能超过128个字符', trigger: 'blur' }
        ],
        coverImageId: [
          { required: true, message: '请选择封面图片', trigger: 'change' }
        ]
      }
    }
  },
  methods: {
    initData(articleId) {
      this.articleId = articleId
      this.dialogVisible = true
      this.loadConfig()
    },

    // 获取公众号发布配置
    loadConfig() {
      this.loading = true

      getWeChatPublishConfig(this.articleId).then(res => {
        const data = res.data || {}

        this.form = {
          articleId: data.articleId,
          title: data.title || '',
          digest: data.digest || '',
          coverImageId: data.coverImageId || null,
          coverImageUrl: data.coverImageUrl || ''
        }
      }).finally(() => {
        this.loading = false
      })
    },

    // 选择封面图片
    selectCover() {
      this.$emit('select-cover', {
        articleId: this.articleId,
        currentImageId: this.form.coverImageId
      })
    },

    // 设置封面图片
    setCover(image) {
      this.form.coverImageId = image.id
      this.form.coverImageUrl = image.url

      this.$nextTick(() => {
        this.$refs.publishForm.validateField('coverImageId')
      })
    },

    // 提交公众号草稿
    handleSubmit() {
      this.$refs.publishForm.validate(valid => {
        if (!valid || this.submitLoading) {
          return
        }

        this.submitLoading = true

        publishWeChatArticle({
          articleId: this.form.articleId,
          title: this.form.title.trim(),
          digest: this.form.digest.trim(),
          coverImageId: this.form.coverImageId
        }).then(() => {
          this.$message.success('微信公众号草稿创建成功')
          this.dialogVisible = false
          this.$emit('success')
        }).finally(() => {
          this.submitLoading = false
        })
      })
    },

    // 关闭弹窗
    handleClosed() {
      this.$refs.publishForm && this.$refs.publishForm.resetFields()
      this.articleId = null
    }
  }
}
</script>

<style scoped>
.wechat-cover {
  display: flex;
  flex-direction: column;
  align-items: flex-start;
}

.wechat-cover-preview {
  width: 220px;
}

.wechat-cover-preview >>> .el-image {
  width: 220px;
  height: 110px;
  border-radius: 4px;
}

.wechat-cover-actions {
  text-align: center;
}

.wechat-cover-tip {
  margin-top: 8px;
  color: #909399;
  font-size: 12px;
}
</style>
