<template>
  <div v-loading="pageLoading" element-loading-text="数据加载中..." class="editor-container">
    <div class="editor-content">
      <div class="editor-content-main">
        <div class="editor-content-inner editor-content-core">
          <!-- 最近草稿提示 -->
          <div v-if="showRecentDraftFlag && recentDraft.id" class="recent-draft-box">
            <span class="icon-type">草稿</span>
            <p class="draft-title" :title="recentDraft.title">{{ recentDraft.title }}</p>
            <a class="draft-btn-edit" @click="openDraftPage(recentDraft.id)">继续编辑</a>
            <a class="draft-btn-more" @click="openMoreDraftPage">更多草稿</a>
            <button class="draft-btn-close" @click="closeDraftPage">×</button>
          </div>

          <!-- 标题 -->
          <div class="editor-title">
            <input v-model.trim="formData.title" placeholder="请输入文章标题" maxlength="150">
          </div>

          <!-- Slug -->
          <div class="slug-row">
            <span class="slug-label">Slug：</span>
            <el-input v-model.trim="formData.slug" placeholder="用于生成文章 URL，例如 spring-boot-start" maxlength="150" clearable />
            <el-button type="text" :disabled="!formData.title" @click="generateSlug">自动生成</el-button>
          </div>

          <!-- Markdown 编辑器 -->
          <div class="editor-text-area">
            <muses-markdown-editor
              ref="markdownEditor"
              v-model="formData.contentMd"
              :height="650"
              :upload-image="uploadImage"
              :upload-video="uploadVideo"
              :upload-start="uploadStart"
              :upload-success="uploadSuccess"
              :upload-error="uploadError"
            />
          </div>

          <div class="editor-content-opt">
            <div class="editor-content-opt-left">
              <span v-if="showSaveTimeStatus" class="save-draft-span">
                <i class="el-icon-circle-check" />
                已保存 {{ saveTime }}
              </span>
              <span class="editor-word-count">
                共 {{ totalCount }} 字
              </span>
            </div>
            <div class="editor-content-opt-right">
              <el-dropdown
                split-button
                :loading="draftLoading"
                :disabled="draftLoading || submitLoading"
                @click="saveDraft"
                @command="handleSaveCommand"
              >
                保存
                <el-dropdown-menu slot="dropdown">
                  <el-dropdown-item command="timing">定时发布</el-dropdown-item>
                  <el-dropdown-item command="history">编辑记录</el-dropdown-item>
                </el-dropdown-menu>
              </el-dropdown>
              <el-button
                type="primary"
                :loading="submitLoading"
                :disabled="draftLoading || submitLoading"
                @click="publishArticle"
              >
                保存并发布
              </el-button>
            </div>
          </div>
        </div>

        <!-- 文章配置 -->
        <div class="editor-content-inner editor-content-config">
          <el-form ref="articleForm" :model="formData" :rules="formRules" label-width="110px">
            <!-- 封面 -->
            <el-form-item label="文章封面：" prop="coverImageType">
              <el-radio-group v-model="formData.coverImageType">
                <el-radio v-for="item in coverImageTypeEnums" :key="item.value" :label="item.value" :disabled="item.disabled">
                  {{ item.name }}
                </el-radio>
              </el-radio-group>

              <!-- 默认封面 -->
              <div v-if="formData.coverImageType === 1" class="form-tip">
                使用网站配置的默认封面
              </div>

              <!-- 单封面 -->
              <template v-if="formData.coverImageType === 2">
                <div class="cover-img-box-flex">
                  <div v-if="formData.coverImages.length < maxUploadLimit" class="cover-img-box" @click="uploadCoverImg">
                    <div class="cover-img">
                      <div class="cover-img-hover">
                        <img :src="addArticleImageIcon" alt="" class="cover-img-add-icon">
                      </div>
                    </div>
                  </div>
                  <div v-else class="cover-img-box">
                    <div class="cover-img">
                      <div class="cover-img-over" @mouseenter="showCoverImgIcon(0)" @mouseleave="hideCoverImgIcon(0)">
                        <img :src="formData.coverImages[0]['fileAccessUrl']" alt="" class="cover-img-img-icon">
                        <span class="cover-img-close-icon" @click="removeCoverImg(formData.coverImages[0], 0)">×</span>
                        <span class="cover-img-replace-icon" @click="replaceCoverImg(formData.coverImages[0], 0)">替换</span>
                      </div>
                    </div>
                  </div>
                </div>
                <div class="form-tip">
                  支持 JPG、JPEG、PNG、WebP，单张最大 5MB，建议尺寸 16:9
                </div>
              </template>

              <!-- 多封面 -->
              <template v-if="formData.coverImageType === 3">
                <div class="cover-img-box-flex cover-img-box-more">
                  <div v-if="formData.coverImages.length < maxUploadLimit" class="cover-img-box" @click="uploadCoverImg">
                    <div class="cover-img">
                      <div class="cover-img-hover">
                        <img :src="addArticleImageIcon" alt="" class="cover-img-add-icon">
                      </div>
                    </div>
                  </div>
                  <div v-for="(articleImage, index) in formData.coverImages" :key="articleImage.fileId" class="cover-img-box">
                    <div class="cover-img">
                      <div class="cover-img-over" @mouseenter="showCoverImgIcon(index)" @mouseleave="hideCoverImgIcon(index)">
                        <img :src="articleImage['fileAccessUrl']" alt="" class="cover-img-img-icon">
                        <span class="cover-img-close-icon" @click="removeCoverImg(articleImage, index)">×</span>
                        <span class="cover-img-replace-icon" @click="replaceCoverImg(articleImage, index)">替换</span>
                      </div>
                    </div>
                  </div>
                </div>
                <div v-if="formData.coverImageType === 3" class="form-tip">
                  支持 JPG、JPEG、PNG、WebP，单张最大 5MB，建议尺寸 16:9，最多 {{ maxUploadLimit }} 张
                </div>
              </template>

              <!-- 标题生成 -->
              <div v-if="formData.coverImageType === 4" class="form-tip">
                根据文章标题自动生成封面
              </div>

              <!-- 无封面 -->
              <div v-if="formData.coverImageType === 5" class="form-tip">
                不使用文章封面
              </div>
            </el-form-item>

            <!-- 摘要 -->
            <el-form-item label="文章摘要：" prop="summary">
              <el-input
                v-model="formData.summary"
                type="textarea"
                resize="none"
                :autosize="{ minRows: 5, maxRows: 5 }"
                maxlength="250"
                show-word-limit
                placeholder="请输入文章摘要"
              />
              <el-button round class="set-remark-btn" @click="generateSummary">一键提取</el-button>
            </el-form-item>

            <!-- 评论 -->
            <el-form-item label="评论设置：" prop="commentFlag">
              <el-radio-group v-model="formData.commentFlag">
                <el-radio v-for="item in commentEnums" :key="String(item.value)" :label="item.value">
                  {{ item.name }}
                </el-radio>
              </el-radio-group>
            </el-form-item>

            <!-- 文章类型 -->
            <el-form-item label="文章类型：" prop="articleType">
              <el-radio-group v-model="formData.articleType">
                <el-radio v-for="item in articleTypeEnums" :key="item.value" :label="item.value">
                  {{ item.name }}
                </el-radio>
              </el-radio-group>
            </el-form-item>

            <!-- 转载信息 -->
            <template v-if="formData.articleType !== 1">
              <el-form-item label="来源名称：" prop="sourceName">
                <el-input v-model.trim="formData.sourceName" maxlength="100" placeholder="例如：掘金、公众号、个人博客" />
              </el-form-item>
              <el-form-item label="原作者：" prop="sourceAuthor">
                <el-input v-model.trim="formData.sourceAuthor" maxlength="100" placeholder="请输入原作者" />
              </el-form-item>
              <el-form-item label="原文地址：" prop="sourceUrl">
                <el-input v-model.trim="formData.sourceUrl" maxlength="1000" show-word-limit placeholder="请输入原文地址" />
              </el-form-item>
            </template>

            <!-- 分类 -->
            <el-form-item label="文章分类：" prop="categoryId">
              <el-select v-model="formData.categoryId" placeholder="请选择文章分类" clearable filterable style="width: 400px">
                <el-option-group v-for="group in articleCategoryTree" :key="group.id" :label="group.name">
                  <el-option
                    v-for="item in group.children"
                    :key="item.id"
                    :value="item.id"
                    :label="item.name"
                    :disabled="!group.enabled || !item.enabled"
                  />
                </el-option-group>
              </el-select>
            </el-form-item>

            <!-- 标签 -->
            <el-form-item label="文章标签：">
              <el-select
                v-model="formData.tags"
                placeholder="请选择文章标签"
                multiple
                :multiple-limit="3"
                filterable
                :remote-method="customTagSearchWithoutCase"
                :loading="tagSearchLoading"
                allow-create
                default-first-option
                clearable
                style="width: 400px"
              >
                <el-option v-for="item in tagList" :key="item.id" :value="item.id" :label="item.name" />
              </el-select>

              <span class="form-tip-inline">
                输入文字搜索，按回车可添加自定义标签
              </span>
            </el-form-item>

            <!-- 高级配置：文章访问权限 -->
            <el-card class="article-config-card access-config-card" shadow="never">
              <div slot="header" class="config-card-header">
                <div class="config-card-title">高级配置</div>
                <div class="config-card-description">设置文章的访问范围及访问条件</div>
              </div>

              <el-form-item label="访问权限：" prop="articlePerm">
                <el-radio-group v-model="formData.articlePerm">
                  <el-radio v-for="item in articlePermEnums" :key="item.value" :label="item.value" :disabled="item.disabled">
                    {{ item.name }}
                  </el-radio>
                </el-radio-group>
              </el-form-item>
            </el-card>

            <!-- SEO 配置 -->
            <el-card class="article-config-card seo-config-card" shadow="never">
              <div slot="header" class="config-card-header">
                <div class="config-card-title">SEO 配置</div>
                <div class="config-card-description">设置搜索引擎使用的标题、关键词和描述</div>
              </div>

              <el-form-item label="SEO 标题：">
                <el-input
                  v-model.trim="formData.seoTitle"
                  maxlength="255"
                  show-word-limit
                  placeholder="留空时使用文章标题"
                />
              </el-form-item>
              <el-form-item label="SEO 关键词：">
                <el-input
                  v-model.trim="formData.seoKeywords"
                  maxlength="255"
                  show-word-limit
                  placeholder="多个关键词使用英文逗号分隔，留空时使用文章标签"
                />
              </el-form-item>
              <el-form-item label="SEO 描述：">
                <el-input
                  v-model="formData.seoDescription"
                  type="textarea"
                  resize="none"
                  :autosize="{ minRows: 3, maxRows: 5 }"
                  maxlength="500"
                  show-word-limit
                  placeholder="留空时使用文章摘要"
                />
              </el-form-item>
            </el-card>
          </el-form>
        </div>
      </div>
    </div>

    <!-- 封面上传 -->
    <uploadImgDialog
      ref="uploadImgDialogRef"
      :dialog-visible="uploadImgDialogVisible"
      :limit="maxUploadLimit"
      @uploadImgDone="uploadImgDone"
    />

    <!-- 自动保存记录 -->
    <autoSaveRecordDrawer
      ref="autoSaveRecordDrawerRef"
      :dialog-visible="autoSaveRecordDrawerVisible"
      @chooseRecordDone="regainAutoSaveContent"
    />

    <!-- 定时发布时间选择 -->
    <publish-time-dialog
      :visible.sync="publishTimeChooseDialogVisible"
      :confirm-loading="autoSubmitLoading"
      @confirm="doAutoPublishArticle"
      @cancel="cancelAutoPublish"
    />
  </div>
</template>

<script>
import addArticleImageIcon from '@/assets/images/add-article-image-icon.png'
import uploadImgDialog from '@/views/admin/content/article/template/uploadImgDialog'
import publishTimeDialog from '@/views/admin/content/article/template/publishTimeDialog'
import autoSaveRecordDrawer from '@/views/admin/content/article/template/autoSaveRecordDrawer'

export default {
  name: 'AddOrEditArticle',
  components: {
    uploadImgDialog,
    publishTimeDialog,
    autoSaveRecordDrawer
  },
  data() {
    return {
      addArticleImageIcon: addArticleImageIcon, // 添加封面图标
      coverImageTypeEnums: [ // 封面类型
        { name: '默认封面', value: 1 },
        { name: '单封面', value: 2 },
        { name: '多封面', value: 3 },
        { name: '标题生成', value: 4 },
        { name: '无封面', value: 5 }
      ],
      articleTypeEnums: [ // 文章类型
        { name: '原创', value: 1 },
        { name: '转载', value: 2 },
        { name: '翻译', value: 3 }
      ],
      articlePermEnums: [ // 文章访问权限
        { name: '公开', value: 1 },
        { name: '登录可见', value: 2 },
        { name: '仅作者可见', value: 3 },
        { name: '公众号验证', value: 4, disabled: true },
        { name: '密码验证', value: 5, disabled: true },
        { name: '付费阅读', value: 6, disabled: true },
        { name: '指定用户可见', value: 7, disabled: true }
      ],
      commentEnums: [ // 评论设置
        { name: '开启', value: true },
        { name: '关闭', value: false }
      ],
      articleCategoryTree: [], // 文章分类树
      tagList: [], // 标签列表
      fullTagList: [], // 全量标签列表
      tagSearchLoading: false, // 标签列表查询状态
      uploadImgDialogVisible: false, // 封面上传模态框显示状态

      totalCount: 0, // 当前编辑器字数
      pageLoading: false, // 页面是否正在加载
      draftLoading: false, // 是否正在保存草稿
      submitLoading: false, // 是否正在发布文章
      autoSubmitLoading: false, // 是否正在自动发布文章
      callActivated: false, // 是否已经完成页面初始化（首次进入页面不调用 activated）
      publishTimeChooseDialogVisible: false, // 定时发布事件选择模态框是否显示

      saveTime: '', // 文章保存时间
      showSaveTimeStatus: false, // 是否展示自动保存状态
      autoSaveTimer: null, // 自动保存定时任务
      lastAutoSaveContent: '', // 最近一次正式保存的内容，用于判断自动保存时文章内容是否发生变化
      isAutoSaving: false, // 是否正在保存
      autoSaveRecordDrawerVisible: false, // 是否展示自动保存记录列表
      showRecentDraftFlag: false, // 是否显示最近草稿
      recentDraft: {
        id: '', // 最近草稿ID
        title: '' // 最近草稿标题
      },

      formData: {
        id: '', // 文章ID
        title: '', // 文章标题
        slug: '', // SLUG
        summary: '', // 文章摘要
        contentMd: '', // 文章内容
        coverImageType: 1, // 封面类型
        coverImages: [], // 封面列表
        categoryId: '', // 文章分类
        tags: [], // 文章标签
        commentFlag: true, // 是否开启评论
        articleType: 1, // 文章类型
        sourceName: '', // 文章来源名称
        sourceAuthor: '', // 原作者
        sourceUrl: '', // 原文地址
        articlePerm: 1, // 访问权限
        seoTitle: '', // SEO 标题
        seoKeywords: '', // SEO 关键字
        seoDescription: '', // SEO 描述
        publishTime: '' // 自动发布时间（仅自动发布支持）
      },
      formRules: {
        summary: [
          { required: true, message: '请填写文章摘要', trigger: 'blur' }
        ],
        categoryId: [
          { required: true, message: '请选择文章分类', trigger: 'change' }
        ],
        coverImageType: [
          { required: true, message: '请选择封面类型', trigger: 'change' }
        ],
        articleType: [
          { required: true, message: '请选择文章类型', trigger: 'change' }
        ],
        articlePerm: [
          { required: true, message: '请选择文章访问权限', trigger: 'change' }
        ],
        commentFlag: [
          { required: true, message: '请选择评论设置', trigger: 'change' }
        ],
        sourceUrl: [
          { required: true, message: '请填写原文地址', trigger: 'blur' }
        ]
      }
    }
  },

  computed: {
    // 最大上传封面数
    maxUploadLimit() {
      return this.formData.coverImageType === 3 ? 3 : 1
    }
  },

  // watch: {
  //   /**
  //    * 处理 keep-alive 下的路由变化。
  //    */
  //   $route: {
  //     immediate: true,
  //     handler(to) {
  //       if (
  //         to.path === '/ac/article/publish' &&
  //         to.query._f != null
  //       ) {
  //         const articleId = to.query.articleId
  //
  //         if (articleId) {
  //           this.clearRecentDraft()
  //           this.resetAutoSaveStatus()
  //           this.getArticleDetail(articleId)
  //
  //           this.$router.push({
  //             name: 'AddArticle',
  //             query: {
  //               articleId
  //             }
  //           })
  //         }
  //       }
  //     }
  //   }
  // },

  created() {
    // 查询分类列表
    this.getArticleCategoryTree()
    // 查询标签列表
    this.getArticleTagList()
    // 查询最近的草稿
    this.queryRecentDraft()
    // 重置自动保存状态
    this.resetAutoSaveStatus()
    // 加载文章信息
    const articleId = this.$route.query.articleId
    if (articleId) {
      this.getArticleDetail(articleId)
    } else {
      this.initArticle()
    }
    // 标记 callActivated 为 true
    this.$nextTick(() => {
      this.callActivated = true
    })
  },
  mounted() {
    this.$nextTick(() => {
      // 添加浏览器离开事件
      this.addBeforeunloadEventListener()
      // 开启自动保存事件
      this.startAutoSaveEvent()
    })
  },

  activated() {
    // if (this.callActivated) {
    //   const articleId = this.$route.query.articleId
    //
    //   if (articleId) {
    //     this.clearRecentDraft()
    //
    //     if (articleId !== this.formData.id) {
    //       this.getArticleTagList()
    //       this.resetAutoSaveStatus()
    //       this.getArticleDetail(articleId)
    //     }
    //   } else if (this.formData.action !== 'add') {
    //     this.clearData()
    //     this.getArticleTagList()
    //     this.resetAutoSaveStatus()
    //     this.queryRecentDraft()
    //     this.autoGenerateId()
    //   }
    // }

    this.$nextTick(() => {
      this.startAutoSaveEvent()
    })
  },

  deactivated() {
    this.autoSaveArticleContent()
    this.stopAutoSaveEvent()
  },

  beforeDestroy() {
    this.stopAutoSaveEvent()
    this.autoSaveArticleContent()
    this.removeBeforeunloadEventListener()
  },

  beforeRouteLeave(to, from, next) {
    this.autoSaveArticleContent()
    next()
  },

  methods: {
    // 新增 beforeunload 监听
    addBeforeunloadEventListener() {
      window.addEventListener('beforeunload', this.beforeUnloadHandler, false)
    },
    // 移除 beforeunload 监听
    removeBeforeunloadEventListener() {
      window.removeEventListener('beforeunload', this.beforeUnloadHandler, false)
    },
    // 浏览器关闭处理器
    beforeUnloadHandler(event) {
      this.autoSaveArticleContent()
      event.returnValue = ''
    },

    // 保存按钮更多选择
    handleSaveCommand(command) {
      if (command === 'timing') {
        this.autoPublishArticle()
      } else if (command === 'history') {
        this.showAutoSaveRecord()
      }
    },

    // 获取文章分类列表
    getArticleCategoryTree() {
      this.$mapi.category.queryPublicCategoryTree().then(({ data }) => {
        if (Array.isArray(data) && data.length > 0) {
          this.articleCategoryTree = data
        } else {
          this.articleCategoryTree = []
          this.$message.warning('文章分类列表为空，请先维护文章分类信息')
        }
      }).catch(() => {
        this.articleCategoryTree = []
      })
    },
    // 获取文章标签
    getArticleTagList() {
      this.$mapi.tag.queryPublicTagList().then(({ data }) => {
        this.tagList = data || []
        this.fullTagList = data || []
      }).catch(() => {
        this.tagList = []
        this.fullTagList = []
      })
    },
    // 标签搜索
    customTagSearchWithoutCase(query) {
      if (!query) {
        this.tagList = this.fullTagList
        return
      }

      this.tagSearchLoading = true
      setTimeout(() => {
        this.tagSearchLoading = false
        const keyword = query.toLowerCase()
        this.tagList = this.fullTagList.filter(item => {
          return (
            item.name && item.name.toLowerCase().indexOf(keyword) > -1
          )
        })
      }, 200)
    },

    // 初始化文章信息
    initArticle() {
      this.$mapi.article.initArticle().then(({ data }) => {
        this.formData = this.normalizeArticleData(data)
        this.lastAutoSaveContent = this.formData.contentMd || ''
        this.updateTotalCount()
      }).catch(error => {
        console.log('init article failed', error)
      }).finally(() => {
        this.pageLoading = false
      })
    },
    // 查询文章详情
    getArticleDetail(articleId) {
      this.pageLoading = true
      this.$mapi.article.queryMyArticleDetail({ articleId }).then(({ data }) => {
        if (!data) {
          this.$message.error('文章不存在')
          return
        }

        if (data.deleteFlag != null && data.deleteFlag !== 0) {
          this.$message.error('文章处于回收站中，不允许编辑，请先恢复文章')
          return
        }

        this.formData = this.normalizeArticleData(data)
        this.lastAutoSaveContent = this.formData.contentMd || ''
        this.updateTotalCount()
      }).catch(error => {
        console.log('get article detail failed', error)
        this.$message.error('加载文章信息失败，请刷新浏览器再试！')
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
        publishTime: data.publishTime || ''
      }
    },

    // 开启自动保存
    startAutoSaveEvent() {
      this.stopAutoSaveEvent()
      this.autoSaveTimer = window.setInterval(() => {
        this.autoSaveArticleContent()
      }, 60000)
    },
    // 关闭自动保存
    stopAutoSaveEvent() {
      if (this.autoSaveTimer != null) {
        window.clearInterval(this.autoSaveTimer)
        this.autoSaveTimer = null
      }
    },
    // 重置自动保存状态
    resetAutoSaveStatus() {
      this.saveTime = ''
      this.showSaveTimeStatus = false
    },
    // 自动保存前置检查
    autoSaveArticleContent() {
      if (!this.formData.id) {
        return
      }

      const content = this.formData.contentMd || ''
      if (!content.trim()) {
        return
      }

      if (this.lastAutoSaveContent !== content) {
        this.updateTotalCount()
        this.doAutoSaveArticleContent()
      }
    },
    // 自动保存文章内容
    doAutoSaveArticleContent() {
      if (!this.formData.id) {
        return
      }

      const param = {
        articleId: this.formData.id,
        title: this.formData.title, // 文章标题
        slug: this.formData.slug, // SLUG
        summary: this.formData.summary, // 文章摘要
        content: this.formData.contentMd, // 文章内容
        wordCount: this.totalCount // 文章字数
      }

      this.isAutoSaving = true
      this.$mapi.article.autoSaveArticleContent(param).then(({ data }) => {
        if (data != null) {
          this.lastAutoSaveContent = this.formData.contentMd
          this.saveTime = data
          this.showSaveTimeStatus = true
        }
      }).catch(error => {
        console.log('auto save article content failed', error)
      }).finally(_ => {
        this.isAutoSaving = false
      })
    },
    // 显示自动保存记录
    showAutoSaveRecord() {
      this.autoSaveRecordDrawerVisible = true
      this.$nextTick(() => {
        if (this.$refs.autoSaveRecordDrawerRef) {
          this.$refs.autoSaveRecordDrawerRef.initData(this.formData.id)
        }
      })
    },
    // 恢复自动保存的内容
    regainAutoSaveContent(recordId) {
      this.autoSaveRecordDrawerVisible = false
      if (!recordId) {
        return
      }

      this.doAutoSaveArticleContent()
      this.$mapi.article.queryAutoSaveArticleContentById({ recordId }).then(({ data }) => {
        if (!data) {
          this.$message.error('内容恢复失败')
          return
        }

        const articleId = data.articleId
        if (String(articleId) !== String(this.formData.id)) {
          this.$message.error('内容版本与当前文章不匹配')
          return
        }
        this.formData.title = data.title || this.formData.title
        this.formData.slug = data.slug || this.formData.slug
        this.formData.summary = data.summary || this.formData.summary

        const content = data.content || ''
        this.formData.contentMd = content
        this.lastAutoSaveContent = content
        this.updateTotalCount()
        this.$message.success('内容恢复成功')
      }).catch(() => {
        this.$message.error('内容恢复失败')
      })
    },

    // 查询最近草稿
    queryRecentDraft() {
      this.$mapi.article.queryLastArticleDraft().then(({ data }) => {
        if (!data) {
          this.clearRecentDraft()
          return
        }

        this.recentDraft.id = data.id
        this.recentDraft.title = data.title || '未命名文章'
        this.showRecentDraftFlag = true
      }).catch(() => {
        this.clearRecentDraft()
      })
    },
    // 清除最近草稿提示
    clearRecentDraft() {
      this.showRecentDraftFlag = false
      this.recentDraft.id = ''
      this.recentDraft.title = ''
    },
    // 关闭最近草稿提示
    closeDraftPage() {
      this.showRecentDraftFlag = false
    },
    // 查询最近草稿
    openDraftPage(articleId) {
      if (!articleId) {
        this.$message.warning('草稿 ID 不存在')
        return
      }

      // 清除最近草稿提示及自动保存状态
      this.clearRecentDraft()
      this.resetAutoSaveStatus()

      // 加载目标草稿详情
      this.getArticleDetail(articleId)
    },
    // 打开更多草稿列表
    openMoreDraftPage() {
      this.$router.push({ name: 'Article', query: { _ts: '1' }})
    },
    // 保存草稿
    saveDraft() {
      this.updateTotalCount()
      const param = this.buildCommonSubmitData()
      this.draftLoading = true
      this.$mapi.article.saveArticle(param).then(_ => {
        this.$message.success('草稿保存成功')
        this.lastAutoSaveContent = this.formData.contentMd
        this.saveTime = this.getCurrentTimeText()
        this.showSaveTimeStatus = true
      }).catch(() => {
        this.saveTime = ''
        this.showSaveTimeStatus = false
      }).finally(() => {
        this.draftLoading = false
      })
    },

    // 构造文章提交参数
    buildCommonSubmitData() {
      return {
        id: this.formData.id,
        title: this.formData.title,
        slug: this.formData.slug,
        summary: this.formData.summary,
        contentMd: this.formData.contentMd,
        categoryId: this.formData.categoryId,
        tags: this.formData.tags,
        seoTitle: this.formData.seoTitle,
        seoKeywords: this.formData.seoKeywords,
        seoDescription: this.formData.seoDescription,
        articlePerm: this.formData.articlePerm,
        articleType: this.formData.articleType,
        sourceName: this.formData.sourceName,
        sourceAuthor: this.formData.sourceAuthor,
        sourceUrl: this.formData.sourceUrl,
        commentFlag: this.formData.commentFlag,
        coverImageType: this.formData.coverImageType,
        coverImages: this.formData.coverImages
      }
    },

    // 更新文章字数
    updateTotalCount() {
      const content = this.formData.contentMd || ''

      const text = content
        .replace(/```[\s\S]*?```/g, '')
        .replace(/!\[.*?]\(.*?\)/g, '')
        .replace(/[#>*_~`]/g, '')
        .replace(/\s+/g, '')

      this.totalCount = text.length
    },

    // 显示封面操作按钮
    showCoverImgIcon(index) {
      const closeIcons = document.getElementsByClassName('cover-img-close-icon')
      const replaceIcons = document.getElementsByClassName('cover-img-replace-icon')
      if (closeIcons[index]) {
        closeIcons[index].style.display = 'block'
      }
      if (replaceIcons[index]) {
        replaceIcons[index].style.display = 'block'
      }
    },
    // 隐藏封面操作按钮
    hideCoverImgIcon(index) {
      const closeIcons = document.getElementsByClassName('cover-img-close-icon')
      const replaceIcons = document.getElementsByClassName('cover-img-replace-icon')
      if (closeIcons[index]) {
        closeIcons[index].style.display = 'none'
      }
      if (replaceIcons[index]) {
        replaceIcons[index].style.display = 'none'
      }
    },
    // 删除封面
    removeCoverImg(file, index) {
      this.$confirm(`确定移除该封面吗？`).then(() => {
        const param = { fileId: file.fileId }
        return this.$mapi.file.deleteFile(param)
      }).then(res => {
        this.formData.coverImages.splice(index, 1)
        this.$message.success(res.message || '删除成功')
      }).catch(() => {})
    },
    // 打开封面上传窗口
    uploadCoverImg() {
      this.uploadImgDialogVisible = true
      this.$nextTick(() => {
        this.$refs.uploadImgDialogRef.initData()
      })
    },
    // 替换封面
    replaceCoverImg(file, index) {
      this.uploadImgDialogVisible = true
      this.$nextTick(() => {
        this.$refs.uploadImgDialogRef.initData(true, index)
      })
    },
    // 封面上传完成
    uploadImgDone(fileList, index) {
      this.uploadImgDialogVisible = false
      const list = Array.isArray(fileList) ? fileList : []
      if (index != null && index >= 0 && list.length === 1 && this.formData.coverImages.length > index) {
        this.$set(this.formData.coverImages, index, list[0])
        return
      }
      list.forEach(file => {
        this.formData.coverImages.push(file)
      })
      if (this.formData.coverImages.length > this.maxUploadLimit) {
        this.formData.coverImages = this.formData.coverImages.slice(0, this.maxUploadLimit)
      }
    },

    // 自动生成 SLUG
    generateSlug() {
      if (!this.formData.title) {
        return
      }

      const slug = this.$slug(this.formData.title)
      if (!slug) {
        this.$message.warning('当前标题无法自动生成 Slug，请手动填写')
        return
      }

      this.formData.slug = slug.substring(0, 150)
    },

    // 自动提取摘要
    generateSummary() {
      const content = this.formData.contentMd || ''
      const text = content
        .replace(/```[\s\S]*?```/g, '')
        .replace(/!\[.*?]\(.*?\)/g, '')
        .replace(/\[([^\]]+)]\([^)]+\)/g, '$1')
        .replace(/[#>*_~`]/g, '')
        .replace(/\s+/g, ' ')
        .trim()

      this.formData.summary = text.substring(0, 250)
      this.$refs.articleForm.validateField('summary')
    },

    // 开始上传
    uploadStart(file) {
    },
    // 上传成功
    uploadSuccess(type, file, result) {
    },
    // 上传失败
    uploadError(type, file, result) {
    },
    // 图片上传
    uploadImage(file) {
      return this.uploadEditorFile(file, 'image')
    },
    // 视频上传
    uploadVideo(file) {
      return this.uploadEditorFile(file, 'video')
    },
    // 编辑器文件上传
    uploadEditorFile(file, type) {
      const formData = new FormData()
      formData.append('file', file)
      return this.$mapi.file.uploadFile(formData).then(res => {
        const data = res && res.data
        if (!Array.isArray(data) || data.length === 0) {
          throw new Error(type === 'image' ? '图片上传失败' : '视频上传失败')
        }

        const fileData = data[0]
        return {
          url: fileData['fileAccessUrl'],
          name: fileData['originalFilename'],
          id: fileData.fileId
        }
      })
    },

    // 发布文章
    publishArticle() {
      if (this.draftLoading || this.submitLoading || this.autoSubmitLoading) {
        return
      }

      if (!this.validateBasicInfo()) {
        return
      }

      this.$refs.articleForm.validate(valid => {
        if (!valid) {
          return
        }

        // 构造参数
        const param = this.buildCommonSubmitData()

        // 发送请求
        this.submitLoading = true
        this.$mapi.article.publishArticle(param).then(res => {
          this.$message.success(res.message || '发布成功')
          this.lastAutoSaveContent = this.formData.contentMd
          this.publishSuccess(res.data)
        }).finally(() => {
          this.submitLoading = false
        })
      })
    },

    // 定时发布文章
    autoPublishArticle() {
      if (this.draftLoading || this.submitLoading || this.autoSubmitLoading) {
        return
      }

      if (!this.validateBasicInfo()) {
        return
      }

      if (this.formData.articlePerm === 3) {
        this.$message.warning('仅作者可见的文章暂不支持定时发布')
        return
      }

      this.$refs.articleForm.validate(valid => {
        if (!valid) {
          return
        }

        this.openPublishTimeChooseDialog()
      })
    },
    // 打开自动发布时间选择模态框
    openPublishTimeChooseDialog() {
      this.publishTimeChooseDialogVisible = true
    },
    // 取消定时发布
    cancelAutoPublish() {
      this.publishTimeChooseDialogVisible = false
    },
    // 提交定时发布请求
    doAutoPublishArticle(publishTime) {
      if (!publishTime) {
        this.$message.warning('请选择发布时间')
        return
      }

      if (this.draftLoading || this.submitLoading || this.autoSubmitLoading) {
        return
      }

      // 再次检查权限，避免绕过弹窗流程调用。
      if (this.formData.articlePerm === 3) {
        this.$message.warning('仅作者可见的文章暂不支持定时发布')
        return
      }

      // 记录本次提交的发布时间。
      this.formData.publishTime = publishTime

      // 公共提交参数中原本没有 publishTime，因此在这里显式补充。
      const param = this.buildCommonSubmitData()
      param.publishTime = publishTime

      this.autoSubmitLoading = true
      this.$mapi.article.autoPublishArticle(param).then(() => {
        // 只有接口成功后才关闭弹窗和清理编辑数据。
        this.publishTimeChooseDialogVisible = false
        this.$message.success('定时发布设置成功')

        this.clearData()

        // 定时发布只是创建了定时任务，不应直接提示为已公开发布。
        this.$router.push({
          name: 'Article',
          query: {
            _ts: '3'
          }
        })
      }).catch(error => {
        console.error('schedule article publish failed', error)
        this.$message.error('定时发布设置失败，请稍后重试')
      }).finally(() => {
        this.autoSubmitLoading = false
      })
    },

    // 校验标题和正文
    validateBasicInfo() {
      if (!this.formData.title || !this.formData.title.trim()) {
        this.$message.warning('请填写文章标题')
        return false
      }

      if (!this.formData.contentMd || !this.formData.contentMd.trim()) {
        this.$message.warning('请填写文章内容')
        return false
      }

      if (!this.formData.slug || !this.formData.slug.trim()) {
        this.$message.warning('请填写文章 Slug')
        return false
      }

      return true
    },
    // 发布成功
    publishSuccess(result) {
      const articleId = result.id
      const articleStatus = result.articleStatus
      this.clearData()

      this.$router.push({
        name: 'PublishSuccess',
        params: {
          articleId: String(articleId)
        },
        query: {
          status: String(articleStatus)
        }
      })
    },
    // 清空页面数据
    clearData() {
      this.formData = {
        id: '', // 文章ID
        title: '', // 文章标题
        slug: '', // SLUG
        summary: '', // 文章摘要
        contentMd: '', // 文章内容
        coverImageType: 1, // 封面类型
        coverImages: [], // 封面列表
        categoryId: '', // 文章分类
        tags: [], // 文章标签
        commentFlag: true, // 是否开启评论
        articleType: 1, // 文章类型
        sourceName: '', // 文章来源名称
        sourceAuthor: '', // 原作者
        sourceUrl: '', // 原文地址
        articlePerm: 1, // 访问权限
        seoTitle: '', // SEO 标题
        seoKeywords: '', // SEO 关键字
        seoDescription: '', // SEO 描述
        publishTime: '' // 自动发布时间（仅自动发布支持）
      }

      if (this.$refs.articleForm) {
        this.$refs.articleForm.resetFields()
      }

      // TODO
      this.lastAutoSaveContent = ''
      this.totalCount = 0
      this.publishDate = ''
      this.resetAutoSaveStatus()
    },

    // Markdown 编辑器内容变化
    onContentChange(content) {
      this.formData.contentMd = content || ''
      this.updateTotalCount()
    },

    // 获取当前时间文本
    getCurrentTimeText() {
      const date = new Date()
      return (
        this.padZero(date.getHours()) +
        ':' +
        this.padZero(date.getMinutes()) +
        ':' +
        this.padZero(date.getSeconds())
      )
    },
    // 数字补零
    padZero(value) {
      return value < 10 ? '0' + value : String(value)
    }
  }
}
</script>

<style scoped>
.editor-container {
  width: 100%;
  height: 100%;
  overflow: auto;
  background-color: #f5f5f5;
  color: #333;
}

.editor-content {
  width: 100%;
  min-height: 100%;
}

.editor-content-main {
  width: 100%;
}

.editor-content-inner {
  width: 100%;
  min-width: 1000px;
  max-width: 1600px;
  margin: 0 auto 30px;
  padding: 30px 50px 50px;
  box-sizing: border-box;
  background-color: #fff;
  border: 1px solid #e8e8e8;
  box-shadow: 0 2px 10px rgba(0, 0, 0, .04);
}

.editor-content-core {
  width: 100%;
  margin-bottom: 20px;
}

/* 标题 */
.editor-title {
  padding: 10px 0 20px;
  border-bottom: 1px solid #e8e8e8;
}

.editor-title input {
  width: 100%;
  padding: 0;
  border: 0;
  outline: none;
  font-size: 28px;
  line-height: 42px;
  font-weight: 600;
  color: #303133;
  box-sizing: border-box;
}

.editor-title input::placeholder {
  color: #c0c4cc;
  font-weight: 400;
}

/* Slug */
.slug-row {
  display: flex;
  align-items: center;
  padding: 15px 0;
  border-bottom: 1px solid #f0f0f0;
}

.slug-label {
  width: 55px;
  flex-shrink: 0;
  color: #909399;
  font-size: 14px;
}

.slug-row .el-input {
  flex: 1;
  min-width: 0;
}

.slug-row .el-button {
  flex-shrink: 0;
  margin-left: 10px;
}

.editor-text-area {
  width: 100%;
  margin-top: 20px;
}

/* Markdown 编辑器所在区域 */
.editor-text-area ::v-deep .v-md-editor {
  width: 100%;
  min-width: 0;
}

/*
 * 三栏模式下，让编辑器充分利用可用空间。
 * 避免父元素的最小宽度导致编辑器溢出。
 */
.editor-text-area ::v-deep .v-md-editor__main {
  min-width: 0;
}

.editor-content-opt {
  display: flex;
  align-items: center;
  justify-content: space-between;
  gap: 16px;
  padding: 12px 0;
  border-top: 1px solid #ebeef5;
}

.editor-content-opt-left {
  display: flex;
  align-items: center;
  gap: 16px;
  flex-shrink: 0;
  color: #909399;
  font-size: 13px;
}

.editor-content-opt-right {
  display: flex;
  align-items: center;
  justify-content: flex-end;
  flex-wrap: wrap;
  gap: 8px;
}

.editor-content-opt-right .el-button + .el-button {
  margin-left: 0;
}

.save-draft-span {
  color: #67c23a;
  white-space: nowrap;
}

.editor-word-count {
  white-space: nowrap;
}

/* 配置区域 */
.editor-content-config {
  padding-top: 35px;
}

.form-tip {
  display: block;
  margin-top: 6px;
  color: #909399;
  font-size: 12px;
  line-height: 20px;
}

.form-tip-inline {
  margin-left: 8px;
  color: #909399;
  font-size: 12px;
}

/* 封面 */
.cover-img-box-flex {
  display: flex;
  flex-wrap: wrap;
  width: 100%;
  margin-top: 10px;
}

.cover-img-box {
  margin-right: 24px;
  margin-bottom: 10px;
}

.cover-img {
  position: relative;
  width: 160px;
  height: 90px;
}

.cover-img-hover {
  position: absolute;
  width: 160px;
  height: 90px;
  top: 0;
  left: 0;
  cursor: pointer;
  background: #fff;
  border: 1px dashed #bfbfbf;
  border-radius: 4px;
  box-sizing: border-box;
}

.cover-img-over {
  position: absolute;
  width: 160px;
  height: 90px;
  top: 0;
  left: 0;
  z-index: 1;
  border-radius: 4px;
}

.cover-img-add-icon {
  position: absolute;
  width: 28px;
  height: 28px;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
}

.cover-img-img-icon {
  width: 160px;
  height: 90px;
  object-fit: cover;
  border-radius: 4px;
}

.cover-img-close-icon {
  position: absolute;
  display: none;
  right: -8px;
  top: -8px;
  width: 20px;
  height: 20px;
  line-height: 20px;
  text-align: center;
  border-radius: 50%;
  background: #999;
  color: #fff;
  font-size: 14px;
  cursor: pointer;
}

.cover-img-replace-icon {
  position: absolute;
  display: none;
  left: 50%;
  bottom: 10px;
  width: 56px;
  height: 28px;
  margin-left: -28px;
  line-height: 28px;
  text-align: center;
  border-radius: 4px;
  background-color: rgba(0, 0, 0, .45);
  color: #fff;
  font-size: 13px;
  cursor: pointer;
}

.default-cover-img {
  display: block;
  width: 160px;
  height: 90px;
  margin-top: 10px;
  object-fit: cover;
  border-radius: 4px;
}

.set-remark-btn {
  position: absolute;
  right: 8px;
  bottom: 8px;
  min-height: 30px !important;
  padding: 0 10px !important;
}

/* 最近草稿 */
.recent-draft-box {
  display: flex;
  align-items: center;
  width: 100%;
  margin-bottom: 20px;
  padding: 15px 20px;
  box-sizing: border-box;
  background-color: #f5f6f7;
}

.recent-draft-box .icon-type {
  display: block;
  flex-shrink: 0;
  padding: 3px 7px;
  border: 1px solid #ced0e2;
  background-color: #fff;
  color: #555666;
  font-size: 13px;
  line-height: 1;
}

.draft-title {
  flex: 1;
  min-width: 0;
  margin: 0 0 0 15px;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.draft-btn-edit,
.draft-btn-more {
  flex-shrink: 0;
  margin-left: 20px;
  color: #606266;
  font-size: 13px;
  text-decoration: none;
  cursor: pointer;
}

.draft-btn-edit:hover,
.draft-btn-more:hover {
  color: #409eff;
}

.draft-btn-close {
  flex-shrink: 0;
  width: 24px;
  height: 24px;
  margin-left: 15px;
  padding: 0;
  border: 0;
  background: transparent;
  color: #909399;
  font-size: 20px;
  line-height: 24px;
  cursor: pointer;
}

/* 定时发布 */
.publish-timer-content {
  position: relative;
  padding-top: 20px;
  padding-bottom: 30px;
}

.publish-time-desc {
  margin: 0 0 15px;
  color: #606266;
  font-size: 13px;
}

.publish-ipt-date-box {
  display: flex;
  align-items: center;
}

.publish-time-txt {
  margin: 15px 0 0;
  color: #909399;
  font-size: 13px;
}

.publish-time {
  color: #303133;
  font-weight: 500;
}

/* 访问权限与 SEO 独立配置卡片 */
.article-config-card {
  margin: 28px 0;
  border-radius: 6px;
  border-color: #ebeef5;
}

.article-config-card ::v-deep .el-card__header {
  padding: 16px 20px;
  background-color: #fafafa;
}

.article-config-card ::v-deep .el-card__body {
  padding: 24px 20px 8px;
}

.config-card-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  flex-wrap: wrap;
  gap: 8px 16px;
}

.config-card-title {
  color: #303133;
  font-size: 15px;
  font-weight: 600;
  line-height: 22px;
}

.config-card-description {
  color: #909399;
  font-size: 12px;
  font-weight: 400;
  line-height: 20px;
}

/* Element UI */
/*::v-deep .editor-content-config .el-form-item {*/
/*  margin-bottom: 24px;*/
/*}*/

/*::v-deep .editor-content-config .el-divider {*/
/*  margin-top: 35px;*/
/*  margin-bottom: 25px;*/
/*}*/

/*::v-deep .editor-content-config .el-divider__text {*/
/*  font-size: 14px;*/
/*  font-weight: 600;*/
/*}*/

/*
 * 中等屏幕
 */
@media screen and (max-width: 1200px) {
}

/*
 * 小屏幕
 */
@media screen and (max-width: 768px) {
  .editor-content-opt {
    align-items: flex-start;
    flex-direction: column;
  }

  .editor-content-opt-left {
    flex-wrap: wrap;
  }

  .editor-content-opt-right {
    justify-content: flex-start;
    width: 100%;
  }
}
</style>
