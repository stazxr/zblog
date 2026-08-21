<template>
  <div class="config-container">
    <el-card shadow="never" class="config-card">
      <div slot="header" class="card-header">
        <div class="card-title">
          <i class="el-icon-s-platform" />
          <span>网站基础</span>
        </div>
      </div>

      <el-form ref="configForm" :model="config" :rules="rules" class="config-form" label-width="110px">
        <el-row :gutter="24">
          <el-col :xs="24" :sm="12">
            <el-form-item label="网站名称" prop="websiteName">
              <el-input v-model="config.websiteName" maxlength="100" show-word-limit placeholder="请输入网站名称" />
            </el-form-item>
          </el-col>
          <el-col :xs="24" :sm="12">
            <el-form-item label="网站默认标题" prop="websiteTitle">
              <el-input v-model="config.websiteTitle" maxlength="100" show-word-limit placeholder="请输入网站默认标题" />
            </el-form-item>
          </el-col>
          <el-col :xs="24" :sm="12">
            <el-form-item label="网站作者">
              <el-input v-model="config.websiteAuthor" maxlength="50" show-word-limit placeholder="请输入作者名称" />
            </el-form-item>
          </el-col>
          <el-col :xs="24" :sm="12">
            <el-form-item label="建站日期">
              <el-date-picker v-model="config.websiteCreateTime" type="date" value-format="yyyy-MM-dd" placeholder="请选择建站日期" />
            </el-form-item>
          </el-col>
          <el-col :xs="24" :sm="12">
            <el-form-item label="网站 LOGO">
              <div class="image-setting">
                <muses-image-crop-upload
                  v-model="config.websiteLogo"
                  :aspect-ratio="1"
                  :output-width="128"
                  :output-height="128"
                  :preview-width="80"
                  :preview-height="80"
                  :min-width="128"
                  :min-height="128"
                />
                <div class="image-url">
                  <el-input v-model="config.websiteLogo" maxlength="500" show-word-limit placeholder="上传图片或直接输入图片地址" />
                  <div class="form-tip">
                    建议使用正方形 PNG 图片
                  </div>
                </div>
              </div>
            </el-form-item>
          </el-col>
          <el-col :xs="24" :sm="12">
            <el-form-item label="网站 ICON">
              <div class="image-setting">
                <muses-image-crop-upload
                  v-model="config.websiteFavicon"
                  :aspect-ratio="1"
                  :output-width="128"
                  :output-height="128"
                  :preview-width="80"
                  :preview-height="80"
                />
                <div class="image-url">
                  <el-input v-model="config.websiteFavicon" maxlength="500" show-word-limit placeholder="上传图片或直接输入图片地址" />
                  <div class="form-tip">
                    建议使用正方形 PNG 图片，推荐尺寸 128 × 128
                  </div>
                </div>
              </div>
            </el-form-item>
          </el-col>
          <el-col :xs="24">
            <el-form-item label="网站签名">
              <el-input v-model="config.websiteSignature" type="textarea" :rows="3" maxlength="124" show-word-limit placeholder="请输入网站签名" />
            </el-form-item>
          </el-col>
          <el-col :xs="24">
            <el-form-item label="作者头像">
              <div class="image-setting">
                <muses-image-crop-upload
                  v-model="config.websiteAvatar"
                  :aspect-ratio="1"
                  :output-width="400"
                  :output-height="400"
                  :preview-width="100"
                  :preview-height="100"
                  :min-width="200"
                  :min-height="200"
                  circle
                />
                <div class="image-url">
                  <el-input v-model="config.websiteAvatar" maxlength="500" show-word-limit placeholder="上传图片或直接输入图片地址" />
                  <div class="form-tip">
                    建议使用正方形头像图片
                  </div>
                </div>
              </div>
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
    </el-card>

    <el-card shadow="never" class="config-card">
      <div slot="header" class="card-header">
        <div class="card-title">
          <i class="el-icon-search" />
          <span>SEO 配置</span>
        </div>
      </div>

      <el-form :model="config" class="config-form" label-width="110px">
        <el-form-item label="网站关键词">
          <el-input
            v-model="config.websiteKeywords"
            maxlength="500"
            show-word-limit
            placeholder="多个关键词使用英文逗号分隔，例如：Java,Spring Boot,Vue"
          />
          <div class="form-tip">
            多个关键词建议使用英文逗号分隔
          </div>
        </el-form-item>
        <el-form-item label="网站描述">
          <el-input
            v-model="config.websiteDescription"
            type="textarea"
            :rows="4"
            maxlength="1000"
            show-word-limit
            placeholder="请输入网站描述"
          />
        </el-form-item>
      </el-form>
    </el-card>

    <!-- ================= 网站信息 ================= -->
    <el-card shadow="never" class="config-card">
      <div slot="header" class="card-header">
        <div class="card-title">
          <i class="el-icon-office-building" />
          <span>备案信息</span>
        </div>
      </div>

      <el-form :model="config" class="config-form" label-width="110px">
        <el-row :gutter="24">
          <el-col :xs="24" :sm="12">
            <el-form-item label="ICP备案号">
              <el-input v-model="config.websiteIcpNo" maxlength="100" placeholder="请输入ICP备案号" />
            </el-form-item>
          </el-col>
          <el-col :xs="24" :sm="12">
            <el-form-item label="公安备案号">
              <el-input v-model="config.websitePoliceNo" maxlength="100" placeholder="请输入公安备案号" />
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
    </el-card>

    <el-card shadow="never" class="config-card">
      <div slot="header" class="card-header">
        <div class="card-title">
          <i class="el-icon-picture-outline" />
          <span>页面展示</span>
        </div>
      </div>

      <el-form :model="config" class="config-form" label-width="110px">
        <el-form-item label="页脚背景图">
          <div class="image-setting image-setting-background">
            <div class="footer-background-upload">
              <muses-image-crop-upload
                v-model="config.footerBackground"
                :aspect-ratio="6"
                :output-width="1600"
                :output-height="250"
                :preview-width="600"
                :preview-height="100"
                :min-width="1000"
                :min-height="150"
              />
            </div>
            <div class="image-url">
              <el-input v-model="config.footerBackground" maxlength="500" placeholder="上传图片或直接输入背景图片地址" />
              <div class="form-tip">
                建议使用 16:3 左右的横向图片，推荐输出尺寸 1600 × 300
              </div>
            </div>
          </div>
        </el-form-item>
        <el-form-item label="网站字体">
          <el-input v-model="config.fontUrl" maxlength="500" placeholder="请输入字体文件地址，例如 .woff2" />
          <div class="form-tip">建议使用 WOFF2 格式字体文件</div>
        </el-form-item>
      </el-form>
    </el-card>

    <el-card shadow="never" class="config-card">
      <div slot="header" class="card-header">
        <div class="card-title">
          <i class="el-icon-data-analysis" />
          <span>统计配置</span>
        </div>
      </div>

      <el-alert
        title="统计代码仅建议配置可信的第三方统计服务，例如百度统计。"
        type="warning"
        :closable="false"
        show-icon
        class="statistics-alert"
      />

      <el-form :model="config" class="config-form" label-width="110px">
        <el-form-item label="统计代码">
          <el-input
            v-model="config.statisticsCode"
            type="textarea"
            :rows="8"
            placeholder="请输入第三方网站统计代码"
          />
        </el-form-item>
      </el-form>
    </el-card>

    <div class="submit">
      <el-button :disabled="submitLoading" @click="resetForm">重置</el-button>
      <el-button type="primary" :loading="submitLoading" @click="save">保存配置</el-button>
    </div>
  </div>
</template>

<script>
export default {
  name: 'WebsiteConfig',
  data() {
    return {
      submitLoading: false,
      config: this.createDefaultConfig(),
      originalConfig: null,
      rules: {
        websiteName: [
          { required: true, message: '请输入网站名称', trigger: 'blur' }
        ],
        websiteTitle: [
          { required: true, message: '请输入网站默认标题', trigger: 'blur' }
        ]
      }
    }
  },
  created() {
    this.queryWebsiteConfig()
  },
  methods: {
    createDefaultConfig() {
      return {
        // 网站基础
        websiteName: '',
        websiteTitle: '',
        websiteLogo: '',
        websiteFavicon: '',
        websiteAuthor: '',
        websiteAvatar: '',
        websiteSignature: '',
        // SEO
        websiteKeywords: '',
        websiteDescription: '',
        // 网站信息
        websiteCreateTime: '',
        websiteIcpNo: '',
        websitePoliceNo: '',
        // 页面展示
        footerBackground: '',
        fontUrl: '',
        // 统计
        statisticsCode: '',
        // 版本
        version: null
      }
    },
    queryWebsiteConfig() {
      this.$mapi.websiteConfig.queryWebsiteConfigDetail().then(res => {
        const data = res.data || {}
        this.config = Object.assign(this.createDefaultConfig(), data)
        this.originalConfig = JSON.parse(JSON.stringify(this.config))
      })
    },
    save() {
      this.$refs.configForm.validate(valid => {
        if (!valid) {
          return
        }

        this.submitLoading = true
        this.$mapi.websiteConfig.editWebsiteConfig(this.config).then(res => {
          this.$message.success(res.message)
          this.originalConfig = JSON.parse(JSON.stringify(this.config))
        }).finally(() => {
          this.submitLoading = false
        })
      })
    },
    resetForm() {
      if (!this.originalConfig) {
        return
      }

      this.$confirm('确定要放弃当前修改并恢复为原来的配置吗？', '提示', { type: 'warning' }).then(() => {
        this.config = JSON.parse(JSON.stringify(this.originalConfig))
        this.$nextTick(() => {
          this.$refs.configForm.clearValidate()
        })
      }).catch(() => {})
    }
  }
}
</script>

<style scoped>
.config-container {
  max-width: 1000px;
  margin: 0 auto;
  padding: 12px;
}

.config-card {
  margin-bottom: 16px;
}
.config-card ::v-deep .el-card__header {
  padding: 14px 18px;
}

.card-header {
  display: flex;
  align-items: center;
}

.card-title {
  display: flex;
  align-items: center;
  font-size: 15px;
  font-weight: 600;
}
.card-title i {
  margin-right: 8px;
  font-size: 18px;
}

.config-form {
  max-width: 900px;
}
.config-form ::v-deep .el-input,
.config-form ::v-deep .el-textarea {
  width: 100%;
}
.config-form ::v-deep .el-date-editor {
  width: 100%;
}

.form-tip {
  margin-top: 5px;
  line-height: 18px;
  font-size: 12px;
  color: #909399;
}

.image-setting {
  display: flex;
  flex-direction: column;
  align-items: flex-start;
  gap: 12px;
  width: 100%;
}

.image-url {
  width: 100%;
  min-width: 0;
}

.image-setting-background {
  flex-direction: column;
  align-items: stretch;
  gap: 12px;
}

.footer-background-upload {
  width: 100%;
  max-width: 600px;
}
.footer-background-upload ::v-deep .muses-image-crop-upload {
  width: 100%;
}
.footer-background-upload ::v-deep .image-preview {
  width: 100% !important;
  max-width: 100% !important;
  box-sizing: border-box;
}
.footer-background-upload ::v-deep .image-preview img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.statistics-alert {
  margin-bottom: 20px;
}

.submit {
  display: flex;
  justify-content: center;
  gap: 12px;
  padding: 10px 0 30px;
}
.submit .el-button {
  min-width: 120px;
}

/* =========================
   手机端适配
   ========================= */
@media screen and (max-width: 768px) {
  .config-container {
    padding: 8px;
  }

  .config-card {
    margin-bottom: 10px;
  }
  .config-card ::v-deep .el-card__header {
    padding: 12px 14px;
  }
  .config-card ::v-deep .el-card__body {
    padding: 16px 14px;
  }

  .card-title::before {
    width: 0;
  }

  .config-form {
    width: 100%;
  }
  .config-form ::v-deep .el-form-item {
    margin-bottom: 18px;
  }
  /* 手机端 Label 放到输入框上方 */
  .config-form ::v-deep .el-form-item__label {
    float: none;
    width: 100% !important;
    height: auto;
    line-height: 20px;
    padding: 0 0 7px;
    text-align: left;
  }
  .config-form ::v-deep .el-form-item__content {
    margin-left: 0 !important;
  }
  .config-form ::v-deep .el-date-editor {
    width: 100%;
  }

  .footer-background-upload {
    width: 100%;
    max-width: 100%;
  }

  .image-url {
    width: 100%;
  }

  .statistics-alert {
    margin-bottom: 16px;
  }

  .submit {
    gap: 10px;
    padding: 10px 0 20px;
  }
  .submit .el-button {
    flex: 1;
    min-width: 0;
  }
}

/* 小屏手机 */
@media screen and (max-width: 480px) {
  .config-container {
    padding: 5px;
  }

  .config-card ::v-deep .el-card__body {
    padding: 14px 12px;
  }

  .card-title {
    font-size: 14px;
  }

  .footer-background-upload {
    width: 100%;
    max-width: 100%;
  }

  .image-url {
    width: 100%;
  }
}
</style>
