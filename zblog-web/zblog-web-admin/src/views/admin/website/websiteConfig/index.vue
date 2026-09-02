<template>
  <div class="config-container">
    <div class="config-panel">
      <div class="config-nav">
        <div
          v-for="item in navList"
          :key="item.id"
          class="config-nav-item"
          :class="{ active: activeSection === item.id }"
          @click="scrollToSection(item.id)"
        >
          <i :class="item.icon" />
          <span>{{ item.name }}</span>
        </div>
      </div>

      <el-form ref="configForm" :model="config" :rules="rules" class="config-form" label-width="110px">
        <section id="basic" class="config-section">
          <div class="section-header">
            <div class="section-title">
              <i class="el-icon-s-platform" />
              <div>
                <div class="section-name">
                  网站基础
                </div>
                <div class="section-desc">
                  配置网站名称、作者、网站图标及封面等基础信息
                </div>
              </div>
            </div>
          </div>
          <div class="section-content">
            <el-row :gutter="28">
              <el-col :xs="24" :sm="12">
                <el-form-item label="网站名称" prop="websiteName">
                  <el-input v-model="config.websiteName" maxlength="100" show-word-limit />
                </el-form-item>
              </el-col>
              <el-col :xs="24" :sm="12">
                <el-form-item label="网站标题" prop="websiteTitle">
                  <el-input v-model="config.websiteTitle" maxlength="100" show-word-limit />
                </el-form-item>
              </el-col>
              <el-col :xs="24" :sm="12">
                <el-form-item label="网站作者">
                  <el-input v-model="config.websiteAuthor" maxlength="50" show-word-limit />
                </el-form-item>
              </el-col>
              <el-col :xs="24" :sm="12">
                <el-form-item label="建站日期">
                  <el-date-picker v-model="config.websiteCreateTime" type="date" value-format="yyyy-MM-dd" />
                </el-form-item>
              </el-col>
              <el-col :xs="24">
                <el-form-item label="网站简介">
                  <el-input v-model="config.websiteIntro" type="textarea" :rows="3" maxlength="500" show-word-limit />
                </el-form-item>
              </el-col>
              <el-col :xs="24">
                <el-form-item label="网站LOGO" class="image-form-item">
                  <div class="image-setting">
                    <muses-image-crop-upload
                      v-model="config.websiteLogo"
                      :accept-image-types="['image/png']"
                      :fixed="false"
                      :fixed-preview="false"
                      :preview-width="180"
                    />
                    <div class="image-url">
                      <el-input v-model="config.websiteLogo" maxlength="500" placeholder="不上传 LOGO 默认显示网站名称" />
                      <div class="form-tip">
                        请上传 PNG 图片，建议比例 4:1 或 2:1
                      </div>
                    </div>
                  </div>
                </el-form-item>
              </el-col>
              <el-col :xs="24">
                <el-form-item label="网站图标(favicon)" class="image-form-item">
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
                      <el-input v-model="config.websiteFavicon" maxlength="500" placeholder="上传图片或直接输入图片地址" />
                      <div class="form-tip">
                        建议使用正方形图片，推荐尺寸 128 × 128
                      </div>
                    </div>
                  </div>
                </el-form-item>
              </el-col>
              <el-col :xs="24">
                <el-form-item label="网站封面" class="image-form-item">
                  <div class="image-setting">
                    <muses-image-crop-upload
                      v-model="config.websiteCover"
                      :aspect-ratio="1.8"
                      :output-width="720"
                      :output-height="400"
                      :preview-width="260"
                      :preview-height="145"
                      :min-width="360"
                      :min-height="200"
                      :max-size="20"
                    />
                    <div class="image-url">
                      <el-input v-model="config.websiteCover" maxlength="500" placeholder="上传图片或直接输入图片地址" />
                      <div class="form-tip">
                        建议使用 16:9 左右的横向图片
                      </div>
                    </div>
                  </div>
                </el-form-item>
              </el-col>
              <el-col :xs="24">
                <el-form-item label="作者头像" class="image-form-item">
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
                      <el-input v-model="config.websiteAvatar" maxlength="500" placeholder="上传图片或直接输入图片地址" />
                      <div class="form-tip">
                        建议使用正方形头像图片
                      </div>
                    </div>
                  </div>
                </el-form-item>
              </el-col>
            </el-row>
          </div>
        </section>

        <section id="notice" class="config-section">
          <div class="section-header">
            <div class="section-title">
              <i class="el-icon-message-solid" />
              <div>
                <div class="section-name">网站公告</div>
                <div class="section-desc">
                  配置网站需要展示给访客的重要通知信息
                </div>
              </div>
            </div>
          </div>
          <div class="section-content">
            <el-form-item label="公告内容">
              <el-input
                v-model="config.websiteNotice"
                type="textarea"
                :rows="4"
                maxlength="1000"
                show-word-limit
                placeholder="请输入网站公告内容"
              />
              <div class="form-tip">
                公告内容可用于网站首页位置展示
              </div>
            </el-form-item>
          </div>
        </section>

        <section id="seo" class="config-section">
          <div class="section-header">
            <div class="section-title">
              <i class="el-icon-search" />
              <div>
                <div class="section-name">
                  SEO 配置
                </div>
                <div class="section-desc">
                  配置搜索引擎展示的网站关键词和描述信息
                </div>
              </div>
            </div>
          </div>
          <div class="section-content">
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
                placeholder="请输入网站描述，建议偏 SEO 语义"
              />
            </el-form-item>
          </div>
        </section>

        <section id="footer" class="config-section">
          <div class="section-header">
            <div class="section-title">
              <i class="el-icon-bottom" />
              <div>
                <div class="section-name">
                  页脚配置
                </div>
                <div class="section-desc">
                  配置网站底部签名、导航及背景展示效果
                </div>
              </div>
            </div>
          </div>
          <div class="section-content">
            <el-form-item label="页脚签名">
              <el-input v-model="config.footerSignature" type="input" maxlength="124" show-word-limit />
            </el-form-item>
            <el-form-item label="页脚导航">
              <div class="switch-setting">
                <el-switch v-model="config.footerNavbarSwitch" :active-value="true" :inactive-value="false" />
                <span class="switch-desc">
                  {{ config.footerNavbarSwitch ? '显示页脚导航栏' : '隐藏页脚导航栏' }}
                </span>
              </div>
            </el-form-item>
            <el-form-item label="页脚背景图" class="image-form-item">
              <div class="image-setting image-setting-background">
                <div class="footer-background-upload">
                  <muses-image-crop-upload
                    v-model="config.footerBackground"
                    :aspect-ratio="6"
                    :output-width="1600"
                    :output-height="267"
                    :preview-width="680"
                    :preview-height="113"
                    :min-width="1000"
                    :min-height="150"
                  />
                </div>
                <div class="image-url">
                  <el-input v-model="config.footerBackground" maxlength="500" placeholder="上传图片或直接输入背景图片地址" />
                  <div class="form-tip">
                    建议使用横向图片，推荐尺寸 1600 × 267，不配置则展示默认背景
                  </div>
                </div>
              </div>
            </el-form-item>
          </div>
        </section>

        <section id="style" class="config-section">
          <div class="section-header">
            <div class="section-title">
              <i class="el-icon-picture-outline" />
              <div>
                <div class="section-name">
                  页面样式
                </div>
                <div class="section-desc">
                  配置网站页面展示相关的样式资源
                </div>
              </div>
            </div>
          </div>
          <div class="section-content">
            <el-form-item label="网站字体">
              <el-input v-model="config.fontUrl" maxlength="500" placeholder="请输入字体文件地址，例如 .woff2" />
              <div class="form-tip">
                建议使用 WOFF2 格式字体文件
              </div>
            </el-form-item>
          </div>
        </section>

        <section id="friend" class="config-section">
          <div class="section-header">
            <div class="section-title">
              <i class="el-icon-connection" />
              <div>
                <div class="section-name">
                  友链配置
                </div>
                <div class="section-desc">
                  配置网站是否开放友链申请等功能
                </div>
              </div>
            </div>
          </div>
          <div class="section-content">
            <el-form-item label="开放申请">
              <div class="switch-setting">
                <el-switch v-model="config.friendLinkApplySwitch" :active-value="true" :inactive-value="false" />
                <span class="switch-desc">
                  {{ config.friendLinkApplySwitch ? '允许访客申请友链' : '暂不开放友链申请' }}
                </span>
              </div>
            </el-form-item>
            <el-form-item label="健康检测配置">
              <el-input-number v-model="config.friendLinkCheckFailedCount" :min="1" :max="9999" controls-position="right" />
              <div class="form-tip">
                友链健康检测失败次数阙值，连续健康检测失败超过阙值后，将不在前台显示
              </div>
            </el-form-item>
          </div>
        </section>

        <section id="comment" class="config-section">
          <div class="section-header">
            <div class="section-title">
              <i class="el-icon-chat-line-round" />
              <div>
                <div class="section-name">
                  评论配置
                </div>
                <div class="section-desc">
                  配置网站评论功能及评论表情包
                </div>
              </div>
            </div>
          </div>

          <div class="section-content">
            <el-form-item label="游客评论">
              <div class="switch-setting">
                <el-switch v-model="config.commentGuestSwitch" :active-value="true" :inactive-value="false" />
                <span class="switch-desc">
                  {{ config.commentGuestSwitch ? '开启' : '关闭' }}
                </span>
              </div>
              <div class="form-tip">
                开启后，未登录用户也可以发表评论
              </div>
            </el-form-item>
            <el-form-item label="表情包">
              <div class="emoji-setting">
                <div v-if="config.commentEmojis.length" class="emoji-list">
                  <div v-for="(emoji, index) in config.commentEmojis" :key="emoji._key" class="emoji-item">
                    <div class="emoji-image">
                      <img :src="emoji.url" alt="">
                    </div>
                    <div class="emoji-content">
                      <el-input v-model="emoji.name" maxlength="5" placeholder="表情名称" />
                    </div>
                    <el-button type="text" class="emoji-delete" @click="removeCommentEmoji(index)">删除</el-button>
                  </div>
                </div>
                <div v-else class="emoji-empty">
                  <i class="el-icon-picture-outline" />
                  <span>暂无表情包</span>
                </div>
                <!-- 新增操作 -->
                <div class="emoji-add">
                  <el-upload
                    class="emoji-upload"
                    :action="$store.state.api.fileUploadApi"
                    :multiple="true"
                    :show-file-list="false"
                    :accept="'.png,.jpg,.jpeg,.gif,.webp'"
                    :with-credentials="true"
                    :before-upload="beforeEmojiUpload"
                    :on-success="handleEmojiUploadSuccess"
                    :on-error="handleEmojiUploadError"
                  >
                    <el-button type="primary" icon="el-icon-upload2">
                      上传
                    </el-button>
                  </el-upload>
                  <el-button icon="el-icon-plus" @click="addCommentEmojiByUrl">
                    新增
                  </el-button>
                </div>
                <div class="form-tip">
                  上传图片或通过图片 URL 新增表情，新增后可直接填写表情名称
                </div>
              </div>
            </el-form-item>
          </div>
        </section>

        <section id="barrage" class="config-section">
          <div class="section-header">
            <div class="section-title">
              <i class="el-icon-chat-dot-round" />
              <div>
                <div class="section-name">
                  弹幕配置
                </div>
                <div class="section-desc">
                  配置弹幕页面相关数据
                </div>
              </div>
            </div>
          </div>
          <div class="section-content">
            <el-form-item label="弹幕加载量">
              <el-input-number v-model="config.barrageMessageLoadSize" :min="1" :max="9999" controls-position="right" />
              <div class="form-tip">
                每次加载弹幕消息的数量，建议设置为 200
              </div>
            </el-form-item>
          </div>
        </section>

        <section id="security" class="config-section">
          <div class="section-header">
            <div class="section-title">
              <i class="el-icon-lock" />
              <div>
                <div class="section-name">
                  安全配置
                </div>
                <div class="section-desc">
                  配置网站安全访问相关内容
                </div>
              </div>
            </div>
          </div>
          <div class="section-content">
            <el-form-item label="Https 升级">
              <div class="switch-setting">
                <el-switch v-model="config.httpsSwitch" :active-value="true" :inactive-value="false" />
                <span class="switch-desc">
                  {{ config.httpsSwitch ? '自动将不安全请求升级为 HTTPS' : '不自动升级不安全请求' }}
                </span>
              </div>
              <div class="form-tip switch-tip">
                开启后会通过 Content-Security-Policy 自动升级 HTTP 请求
              </div>
            </el-form-item>
          </div>
        </section>

        <section id="record" class="config-section">
          <div class="section-header">
            <div class="section-title">
              <i class="el-icon-office-building" />
              <div>
                <div class="section-name">
                  备案信息
                </div>
                <div class="section-desc">
                  配置网站 ICP 备案及公安备案信息
                </div>
              </div>
            </div>
          </div>

          <div class="section-content">
            <el-row :gutter="28">
              <el-col :xs="24" :sm="12">
                <el-form-item label="ICP备案号">
                  <el-input v-model="config.websiteIcpNo" maxlength="100" />
                </el-form-item>
              </el-col>
              <el-col :xs="24" :sm="12">
                <el-form-item label="公安备案号">
                  <el-input v-model="config.websitePoliceNo" maxlength="100" />
                </el-form-item>
              </el-col>
            </el-row>
          </div>
        </section>

        <section id="third-party" class="config-section config-section-last">
          <div class="section-header">
            <div class="section-title">
              <i class="el-icon-data-analysis" />
              <div>
                <div class="section-name">
                  第三方服务
                </div>
                <div class="section-desc">
                  配置第三方网站相关内容
                </div>
              </div>
            </div>
          </div>
          <div class="section-content">
            <el-alert
              title="统计代码仅建议配置可信的第三方统计服务，例如百度统计。"
              type="warning"
              :closable="false"
              show-icon
              class="statistics-alert"
            />
            <el-form-item label="统计代码">
              <el-input v-model="config.statisticsCode" type="textarea" :rows="8" placeholder="请输入第三方网站统计代码" />
            </el-form-item>
          </div>
        </section>
      </el-form>
    </div>

    <div class="submit-bar">
      <div class="submit-inner">
        <div class="submit-tip">
          修改完成后请保存配置
        </div>
        <div class="submit-actions">
          <el-button :disabled="submitLoading" @click="resetForm">
            重置
          </el-button>
          <el-button type="primary" :loading="submitLoading" @click="save">
            保存配置
          </el-button>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: 'WebsiteConfig',
  data() {
    return {
      submitLoading: false,
      activeSection: 'basic',
      navList: [
        { id: 'basic', name: '网站基础', icon: 'el-icon-s-platform' },
        { id: 'notice', name: '网站公告', icon: 'el-icon-message-solid' },
        { id: 'seo', name: 'SEO', icon: 'el-icon-search' },
        { id: 'footer', name: '页脚', icon: 'el-icon-bottom' },
        { id: 'style', name: '样式', icon: 'el-icon-picture-outline' },
        { id: 'friend', name: '友链', icon: 'el-icon-connection' },
        { id: 'comment', name: '评论', icon: 'el-icon-chat-line-round' },
        { id: 'barrage', name: '弹幕', icon: 'el-icon-chat-dot-round' },
        { id: 'security', name: '安全', icon: 'el-icon-lock' },
        { id: 'record', name: '备案', icon: 'el-icon-office-building' },
        { id: 'third-party', name: '服务', icon: 'el-icon-data-analysis' }
      ],
      config: this.createDefaultConfig(),
      originalConfig: null,
      rules: {
        websiteName: [
          { required: true, message: '请输入网站名称', trigger: 'blur' }
        ],
        websiteTitle: [
          { required: true, message: '请输入网站标题', trigger: 'blur' }
        ]
      }
    }
  },
  created() {
    this.queryWebsiteConfig()
  },
  mounted() {
    window.addEventListener('scroll', this.handleScroll)
  },
  beforeDestroy() {
    window.removeEventListener('scroll', this.handleScroll)
  },
  methods: {
    /**
     * 创建默认配置
     */
    createDefaultConfig() {
      return {
        // 网站基础
        websiteName: '',
        websiteTitle: '',
        websiteIntro: '',
        websiteLogo: '',
        websiteFavicon: '',
        websiteCover: '',
        websiteAuthor: '',
        websiteAvatar: '',
        websiteCreateTime: '',
        // 网站通告
        websiteNotice: '',
        // SEO
        websiteKeywords: '',
        websiteDescription: '',
        // 页脚
        footerSignature: '',
        footerNavbarSwitch: true,
        footerBackground: '',
        // 页面样式
        fontUrl: '',
        // 友链
        friendLinkApplySwitch: true,
        friendLinkCheckFailedCount: 3,
        // 评论
        commentGuestSwitch: true,
        commentEmojis: [],
        // 弹幕
        barrageMessageLoadSize: 200,
        // 安全
        httpsSwitch: false,
        // 网站备案
        websiteIcpNo: '',
        websitePoliceNo: '',
        // 第三方服务
        statisticsCode: ''
      }
    },
    // 新增表情（URL）
    addCommentEmojiByUrl() {
      this.$prompt(
        '请输入表情图片地址',
        'URL 新增表情',
        {
          confirmButtonText: '新增',
          cancelButtonText: '取消',
          inputPlaceholder: '请输入图片 URL',
          inputPattern: /^https?:\/\/.+/i,
          inputErrorMessage: '请输入正确的图片 URL'
        }
      ).then(({ value }) => {
        const url = value.trim()
        this.config.commentEmojis.push({
          name: '',
          url,
          _key: this.createEmojiKey()
        })
      }).catch(() => {})
    },
    createEmojiKey() {
      return `${Date.now()}_${Math.random().toString(36).substr(2, 9)}`
    },
    removeCommentEmoji(index) {
      const emoji = this.config.commentEmojis[index]
      if (!emoji) {
        return
      }

      this.$confirm(
        '确定要删除这个表情吗？',
        '提示',
        {
          type: 'warning'
        }
      ).then(() => {
        this.config.commentEmojis.splice(index, 1)
      }).catch(() => {})
    },
    validateCommentEmojis() {
      for (let i = 0; i < this.config.commentEmojis.length; i++) {
        const emoji = this.config.commentEmojis[i]

        if (!emoji.name) {
          this.$message.warning(`第 ${i + 1} 个表情未填写名称`)
          return false
        }

        if (!/^[\u4e00-\u9fa5]{1,5}$/.test(emoji.name)) {
          this.$message.warning(`表情「${emoji.name}」名称需为 1～5 个汉字`)
          return false
        }

        if (!emoji.url) {
          this.$message.warning(`第 ${i + 1} 个表情缺少图片`)
          return false
        }
      }

      return true
    },
    /**
     * 表情上传前校验
     */
    beforeEmojiUpload(file) {
      const allowTypes = [
        'image/png',
        'image/jpeg',
        'image/gif',
        'image/webp'
      ]

      if (!allowTypes.includes(file.type)) {
        this.$message.warning(
          `图片「${file.name}」格式不支持，仅支持 PNG、JPG、GIF、WebP`
        )
        return false
      }

      // 单个文件最大 2MB
      const maxSize = 2 * 1024 * 1024
      if (file.size > maxSize) {
        this.$message.warning(
          `图片「${file.name}」不能超过 2MB`
        )
        return false
      }

      return true
    },
    /**
     * 表情上传成功
     */
    handleEmojiUploadSuccess(response) {
      console.log('response', response)
      const uploadFiles = response && response.data
      if (!Array.isArray(uploadFiles) || !uploadFiles.length) {
        this.$message.error('表情上传成功，但未获取到文件信息')
        return
      }

      uploadFiles.forEach(file => {
        if (!file || !file.fileAccessUrL) {
          return
        }

        this.config.commentEmojis.push({
          name: '',
          url: file.fileAccessUrL,
          _key: this.createEmojiKey()
        })
      })

      this.$message.success(
        `成功上传 ${uploadFiles.length} 个表情`
      )
    },
    /**
     * 表情上传失败
     */
    handleEmojiUploadError() {
      this.$message.error('表情上传失败，请稍后重试')
    },
    /**
     * 查询网站配置
     */
    queryWebsiteConfig() {
      this.$mapi.websiteConfig.queryWebsiteConfigDetail().then(res => {
        const data = res.data || {}
        const config = Object.assign(this.createDefaultConfig(), data)
        config.commentEmojis = (config.commentEmojis || []).map(item => ({
          name: item.name, url: item.url, _key: this.createEmojiKey()
        }))
        this.config = config
        this.originalConfig = JSON.parse(JSON.stringify(this.config))
      })
    },
    /**
     * 滚动到指定区域
     */
    scrollToSection(id) {
      const element = document.getElementById(id)
      if (!element) {
        return
      }

      this.activeSection = id
      element.scrollIntoView({
        behavior: 'smooth',
        block: 'start'
      })
    },
    /**
     * 监听页面滚动
     */
    handleScroll() {
      const offset = 150

      for (let i = this.navList.length - 1; i >= 0; i--) {
        const item = this.navList[i]
        const element = document.getElementById(item.id)
        if (!element) {
          continue
        }

        const rect = element.getBoundingClientRect()
        if (rect.top <= offset) {
          this.activeSection = item.id
          break
        }
      }
    },
    /**
     * 保存配置
     */
    save() {
      this.$refs.configForm.validate(valid => {
        if (!valid) {
          return
        }

        if (!this.validateCommentEmojis()) {
          return
        }

        const param = {
          ...this.config,
          commentEmojis: this.config.commentEmojis.map(item => ({
            name: item.name,
            url: item.url
          }))
        }
        this.submitLoading = true
        this.$mapi.websiteConfig.editWebsiteConfig(param).then(res => {
          this.$message.success(res.message)
          this.originalConfig = JSON.parse(JSON.stringify(this.config))
        }).finally(() => {
          this.submitLoading = false
        })
      })
    },
    /**
     * 重置配置
     */
    resetForm() {
      if (!this.originalConfig) {
        return
      }

      this.$confirm(
        '确定要放弃当前修改并恢复为原来的配置吗？',
        '提示',
        {
          type: 'warning'
        }
      ).then(() => {
        this.config = JSON.parse(
          JSON.stringify(this.originalConfig)
        )
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
  max-width: 1180px;
  margin: 0 auto;
  padding: 20px 20px 100px;
}

.config-panel {
  background: #fff;
  border: 1px solid #ebeef5;
  border-radius: 8px;
}

/* =========================
   快捷导航
   ========================= */

.config-nav {
  display: flex;
  align-items: center;
  gap: 4px;
  padding: 0 28px;
  border-top: 1px solid #f0f2f5;
  border-bottom: 1px solid #ebeef5;
  overflow-x: auto;
}

.config-nav-item {
  flex-shrink: 0;
  display: flex;
  align-items: center;
  height: 52px;
  padding: 0 16px;
  color: #909399;
  font-size: 13px;
  cursor: pointer;
  transition: color .2s ease, background .2s ease;
  border-bottom: 2px solid transparent;
}

.config-nav-item i {
  margin-right: 6px;
  font-size: 15px;
}

.config-nav-item:hover {
  color: #409eff;
  background: #f8fbff;
}

.config-nav-item.active {
  color: #409eff;
  border-bottom-color: #409eff;
}

/* =========================
   表单区域
   ========================= */

.config-form {
  padding: 0 38px;
}

.config-section {
  scroll-margin-top: 20px;
  padding: 42px 0 46px;
  border-bottom: 1px solid #ebeef5;
}

.config-section-last {
  border-bottom: 0;
}

.section-header {
  margin-bottom: 34px;
}

.section-title {
  display: flex;
  align-items: flex-start;
}

.section-title > i {
  display: flex;
  align-items: center;
  justify-content: center;
  flex-shrink: 0;
  width: 34px;
  height: 34px;
  margin-right: 13px;
  color: #409eff;
  font-size: 18px;
  background: #ecf5ff;
  border-radius: 8px;
}

.section-name {
  color: #303133;
  font-size: 17px;
  font-weight: 600;
  line-height: 22px;
}

.section-desc {
  margin-top: 5px;
  color: #909399;
  font-size: 12px;
  line-height: 18px;
}

.section-content {
  max-width: 1000px;
}

/* =========================
   表单
   ========================= */

.config-form ::v-deep .el-form-item {
  margin-bottom: 30px;
}

.config-form ::v-deep .el-form-item__label {
  color: #606266;
  font-size: 13px;
}

.config-form ::v-deep .el-input,
.config-form ::v-deep .el-textarea {
  width: 100%;
}

.config-form ::v-deep .el-date-editor {
  width: 100%;
}

/* =========================
   表单提示
   ========================= */

.form-tip {
  margin-top: 7px;
  color: #909399;
  font-size: 12px;
  line-height: 18px;
}

/* =========================
   图片上传
   ========================= */

.image-setting {
  display: flex;
  flex-direction: column;
  align-items: flex-start;
  gap: 14px;
  width: 100%;
}

.image-url {
  width: 100%;
  min-width: 0;
}

.image-setting-background {
  align-items: stretch;
}

.image-form-item {
  margin-bottom: 38px !important;
}

.footer-background-upload {
  width: 100%;
  max-width: 680px;
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

/* =========================
   Switch
   ========================= */

.switch-setting {
  display: flex;
  align-items: center;
  min-height: 40px;
}

.switch-desc {
  margin-left: 12px;
  color: #606266;
  font-size: 13px;
  line-height: 20px;
}

.switch-tip {
  margin-left: 0;
}

/* =========================
   统计
   ========================= */

.statistics-alert {
  margin-bottom: 28px;
}

/* =========================
   底部操作栏
   ========================= */

.submit-bar {
  position: sticky;
  bottom: 18px;
  z-index: 10;
  margin-top: 28px;
}

.submit-inner {
  display: flex;
  align-items: center;
  justify-content: space-between;
  min-height: 64px;
  padding: 0 22px;
  background: rgba(255, 255, 255, .96);
  border: 1px solid #ebeef5;
  border-radius: 8px;
  box-shadow: 0 4px 16px rgba(0, 0, 0, .06);
}

.submit-tip {
  color: #909399;
  font-size: 13px;
}

.submit-actions {
  display: flex;
  align-items: center;
  gap: 10px;
}

.submit-actions .el-button {
  min-width: 96px;
}

/* =========================
   评论表情包
   ========================= */

.emoji-setting {
  width: 100%;
}

.emoji-list {
  display: grid;
  grid-template-columns: repeat(4, minmax(0, 1fr));
  gap: 12px;
}

.emoji-item {
  display: flex;
  align-items: center;
  min-width: 0;
  padding: 10px;
  border: 1px solid #ebeef5;
  border-radius: 6px;
  box-sizing: border-box;
  transition: border-color .2s ease, box-shadow .2s ease;
}

.emoji-item:hover {
  border-color: #c6e2ff;
  box-shadow: 0 2px 8px rgba(0, 0, 0, .04);
}

.emoji-image {
  display: flex;
  align-items: center;
  justify-content: center;
  flex-shrink: 0;
  width: 42px;
  height: 42px;
  margin-right: 8px;
  background: #f5f7fa;
  border-radius: 6px;
  overflow: hidden;
}

.emoji-image img {
  display: block;
  max-width: 100%;
  max-height: 100%;
  object-fit: contain;
}

.emoji-content {
  flex: 1;
  min-width: 0;
}

.emoji-content ::v-deep .el-input__inner {
  padding: 0 8px;
}

.emoji-delete {
  flex-shrink: 0;
  margin-left: 4px;
  padding: 4px;
  color: #f56c6c !important;
}

.emoji-add {
  display: flex;
  align-items: center;
  margin-top: 14px;
}

.emoji-upload {
  display: inline-block;
  margin-right: 8px;
}

.emoji-empty {
  display: flex;
  align-items: center;
  justify-content: center;
  height: 100px;
  border: 1px dashed #dcdfe6;
  border-radius: 6px;
  color: #909399;
  font-size: 13px;
}
.emoji-empty i {
  margin-right: 8px;
  color: #c0c4cc;
  font-size: 22px;
}

/* =========================
   平板
   ========================= */

@media screen and (max-width: 992px) {
  .config-container {
    padding: 16px 16px 90px;
  }

  .config-form {
    padding: 0 28px;
  }

  .config-nav {
    padding: 0 18px;
  }

  .config-section {
    padding: 38px 0 42px;
  }

  .section-header {
    margin-bottom: 30px;
  }

  .emoji-list {
    grid-template-columns: repeat(3, minmax(0, 1fr));
  }
}

/* =========================
   手机端
   ========================= */

@media screen and (max-width: 768px) {
  .config-container {
    padding: 8px 8px 90px;
  }

  .config-panel {
    border-radius: 6px;
  }

  .config-nav {
    padding: 0 6px;
  }

  .config-nav-item {
    height: 48px;
    padding: 0 12px;
  }

  .config-form {
    padding: 0 18px;
  }

  .config-section {
    padding: 34px 0 38px;
  }

  .section-header {
    margin-bottom: 28px;
  }

  .config-form ::v-deep .el-form-item {
    margin-bottom: 26px;
  }

  .config-form ::v-deep .image-form-item {
    margin-bottom: 32px !important;
  }

  .config-form ::v-deep .el-form-item__label {
    float: none;
    width: 100% !important;
    height: auto;
    line-height: 20px;
    padding: 0 0 8px;
    text-align: left;
  }

  .config-form ::v-deep .el-form-item__content {
    margin-left: 0 !important;
  }

  .config-form ::v-deep .el-date-editor {
    width: 100%;
  }

  .footer-background-upload {
    max-width: 100%;
  }

  .submit-bar {
    bottom: 8px;
    margin-top: 18px;
  }

  .submit-inner {
    min-height: 58px;
    padding: 0 12px;
  }

  .submit-tip {
    display: none;
  }

  .submit-actions {
    width: 100%;
  }

  .submit-actions .el-button {
    flex: 1;
  }

  .emoji-list {
    grid-template-columns: repeat(2, minmax(0, 1fr));
  }
}

/* =========================
   小屏手机
   ========================= */

@media screen and (max-width: 480px) {
  .config-container {
    padding: 4px 4px 82px;
  }

  .config-form {
    padding: 0 14px;
  }

  .config-section {
    padding: 30px 0 34px;
  }

  .section-header {
    margin-bottom: 24px;
  }

  .section-title > i {
    width: 32px;
    height: 32px;
    margin-right: 10px;
  }

  .section-name {
    font-size: 16px;
  }

  .config-nav-item {
    padding: 0 10px;
    font-size: 12px;
  }

  .config-form ::v-deep .el-form-item {
    margin-bottom: 24px;
  }

  .switch-setting {
    align-items: flex-start;
    padding-top: 8px;
  }

  .switch-desc {
    line-height: 20px;
  }

  .emoji-list {
    grid-template-columns: 1fr;
  }
}
</style>
