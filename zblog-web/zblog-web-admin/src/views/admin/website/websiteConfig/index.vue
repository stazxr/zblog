<template>
  <div class="config-container">
    <el-card shadow="never" class="config-card">
      <div slot="header">
        <i class="el-icon-s-platform" />网站信息
      </div>
      <el-form :model="config" class="config-form" label-position="right" label-width="100px">
        <el-form-item label="网站头像">
          <el-upload
            class="avatar-uploader"
            :show-file-list="false"
          >
            <img v-if="config.websiteAvatar" :src="config.websiteAvatar" class="avatar" alt="">
            <i v-else class="el-icon-plus avatar-uploader-icon" />
          </el-upload>
        </el-form-item>
        <el-form-item label="网站名称">
          <el-input v-model="config.websiteName" class="input-item" />
        </el-form-item>
        <el-form-item label="网站作者">
          <el-input v-model="config.websiteAuthor" class="input-item" />
        </el-form-item>
        <el-form-item label="网站介绍">
          <el-input v-model="config.websiteIntro" class="input-item" type="textarea" :rows="3" />
        </el-form-item>
        <el-form-item label="网站地址">
          <el-input v-model="config.websiteLink" class="input-item" />
        </el-form-item>
      </el-form>
    </el-card>
    <el-card shadow="never" class="config-card">
      <div slot="header">
        <i class="el-icon-search" />
        SEO配置
      </div>
      <el-form :model="config" class="config-form" label-width="100px">
        <el-form-item label="页面标题">
          <el-input v-model="config.seoTitle" class="input-item" />
        </el-form-item>
        <el-form-item label="关键词">
          <el-input v-model="config.seoKeywords" class="input-item" />
        </el-form-item>
        <el-form-item label="描述">
          <el-input v-model="config.seoDescription" class="input-item" type="textarea" :rows="3" />
        </el-form-item>
      </el-form>
    </el-card>
    <div class="submit">
      <el-button type="primary" @click="save">保存配置</el-button>
    </div>
  </div>
</template>

<script>
export default {
  data() {
    return {
      submitLoading: false,
      config: {
        // 网站信息
        websiteName: '',
        websiteTitle: '',
        websiteAuthor: '',
        websiteIntro: '',
        websiteLogo: '',
        websiteAvatar: '',
        websiteLink: '',
        // SEO
        seoTitle: '',
        seoKeywords: '',
        seoDescription: '',
        // 首页
        websiteNotice: '',
        websiteCreateTime: '',
        // 主题
        themeName: '',
        primaryColor: '',
        darkMode: false,
        // 字体
        fontEnable: false,
        fontName: '',
        fontUrl: '',
        // 社交
        githubUrl: '',
        giteeUrl: '',
        email: '',
        qq: '',
        // 统计
        statisticCode: ''
      }
    }
  },
  methods: {
    initData() {
      this.$nextTick(() => {
        this.querySocialInfo()
      })
    },
    querySocialInfo() {
      this.$mapi.webSetting.querySocialInfo().then(res => {
        const { data } = res
        Object.keys(this.socialInfo).forEach(key => {
          this.config[key] = data[key]
        })
      })
    },
    updateSocialInfo() {
      this.submitLoading = true
      this.$mapi.webSetting.updateSocialInfo(this.socialInfo).then(res => {
        this.$message.success(res.message)
      }).finally(_ => {
        this.submitLoading = false
      })
    }
  }
}
</script>

<style scoped>
.config-container {
  padding: 10px;
}
.config-card {
  margin-bottom: 15px;
}
.config-card ::v-deep .el-card__header {
  font-weight: 600;
  font-size: 15px;
  padding: 12px 15px;
}
.config-form {
  max-width: 800px;
}
.input-item {
  width: 500px;
}
.submit {
  text-align: center;
  margin: 30px 0;
}
.avatar {
  width: 100px;
  height: 100px;
  border-radius: 50%;
  object-fit: cover;
}
.avatar-uploader-icon {
  width: 100px;
  height: 100px;
  line-height: 100px;
  text-align: center;
  font-size: 28px;
  color: #999;
  border: 1px dashed #ccc;
}
/* 手机端 */
@media screen and (max-width: 768px) {
  .config-container {
    padding: 5px;
  }
  .config-card {
    margin-bottom: 10px;
  }
  .config-card ::v-deep .el-card__body {
    padding: 10px;
  }
  .config-form {
    width: 100%;
  }
  .config-form ::v-deep .el-form-item {
    margin-bottom: 15px;
  }
  .config-form ::v-deep .el-form-item__label {
    float: none;
    width: 100% !important;
    text-align: left;
    padding: 0 0 5px;
  }
  .config-form ::v-deep .el-form-item__content {
    margin-left: 0 !important;
  }
  .input-item {
    width: 100%;
  }
  .submit {
    margin: 20px 0;
  }
}
</style>
