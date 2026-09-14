<template>
  <div v-loading="pageLoading" element-loading-text="数据加载中..." class="editor-container">
    <div class="editor-content">
      <div class="editor-content-main">
        <div class="editor-content-inner editor-content-core">
          <!-- 最近草稿提示 -->
          <div v-if="showRecentDraftFlag && recentDraft.id" class="recent-draft-box">
            <span class="icon-type">草稿</span>

            <p class="draft-title" :title="recentDraft.title">
              <a class="draft-title-link" @click="openDraftPage(recentDraft.id)">
                {{ recentDraft.title }}
              </a>
            </p>

            <a class="draft-btn-edit" @click="openDraftPage(recentDraft.id)">
              继续编辑
            </a>

            <a class="draft-btn-more" @click="openMoreDraftPage">
              更多草稿
            </a>

            <button class="draft-btn-close" @click="closeDraftPage">
              ×
            </button>
          </div>

          <!-- 标题 -->
          <div class="editor-title">
            <input
              v-model.trim="form.title"
              placeholder="请输入文章标题"
              maxlength="150"
            >
          </div>

          <!-- Slug -->
          <div class="slug-row">
            <span class="slug-label">Slug：</span>

            <el-input
              v-model.trim="form.slug"
              placeholder="用于生成文章 URL，例如 spring-boot-start"
              maxlength="150"
              clearable
            />

            <el-button
              type="text"
              :disabled="!form.title"
              @click="generateSlug"
            >
              自动生成
            </el-button>
          </div>

          <!-- Markdown 编辑器 -->
          <div class="editor-text-area">
            <muses-markdown-editor
              ref="markdownEditor"
              v-model="form.contentMd"
              :height="650"
              :upload-image="uploadImage"
              :upload-video="uploadVideo"
            />
          </div>

          <!-- 编辑器底部操作 -->
          <div class="editor-content-opt">
            <div class="editor-content-opt-left">
              <span v-if="saveDraftSpanShow" class="save-draft-span">
                草稿已保存 {{ saveDraftTime }}
              </span>

              <span>
                共 {{ totalCount }} 字
              </span>
            </div>

            <div class="editor-content-opt-right">
              <el-button
                v-if="form.id"
                round
                @click="showContentRecord"
              >
                编辑记录
              </el-button>

              <el-button
                round
                :loading="draftLoading"
                @click="saveDraft"
              >
                {{ draftBtnName }}
              </el-button>

              <el-button
                v-if="autoPublishBtnShow"
                round
                @click="openPublishArticleByTimingDialog"
              >
                定时发布
              </el-button>

              <el-button
                type="danger"
                round
                :loading="submitLoading"
                @click="publishArticle"
              >
                发布文章
              </el-button>
            </div>
          </div>
        </div>

        <!-- 文章配置 -->
        <div class="editor-content-inner editor-content-config">
          <el-form
            ref="articleForm"
            :model="form"
            :rules="rules"
            label-width="100px"
          >
            <!-- 封面 -->
            <el-form-item label="文章封面：" prop="coverImageType">
              <el-radio-group v-model="form.coverImageType">
                <el-radio
                  v-for="item in coverImageTypeEnums"
                  :key="item.value"
                  :label="item.value"
                  :disabled="item.disabled"
                >
                  {{ item.name }}
                </el-radio>
              </el-radio-group>

              <!-- 单封面 -->
              <div
                v-if="form.coverImageType === 1"
                class="cover-img-box-flex"
              >
                <div
                  v-if="form.articleImg.length < maxUploadSize"
                  class="cover-img-box"
                  @click="uploadCoverImg"
                >
                  <div class="cover-img">
                    <div class="cover-img-hover">
                      <img
                        :src="addIcon"
                        alt=""
                        class="cover-img-add-icon"
                      >
                    </div>
                  </div>
                </div>

                <div
                  v-else
                  class="cover-img-box"
                >
                  <div class="cover-img">
                    <div
                      class="cover-img-over"
                      @mouseenter="showCoverImgIcon(0)"
                      @mouseleave="hideCoverImgIcon(0)"
                    >
                      <img
                        :src="form.articleImg[0].downloadUrl"
                        alt=""
                        class="cover-img-img-icon"
                      >

                      <span
                        class="cover-img-close-icon"
                        @click="removeCoverImg(form.articleImg[0], 0)"
                      >
                        ×
                      </span>

                      <span
                        class="cover-img-replace-icon"
                        @click="replaceCoverImg(form.articleImg[0], 0)"
                      >
                        替换
                      </span>
                    </div>
                  </div>
                </div>
              </div>

              <span
                v-if="form.coverImageType === 1"
                class="form-tip"
              >
                支持 jpg、jpeg、png、webp，单张最大 5MB，建议尺寸 16:9
              </span>

              <!-- 多封面 -->
              <div
                v-if="form.coverImageType === 2"
                class="cover-img-box-flex cover-img-box-more"
              >
                <div
                  v-if="form.articleImg.length < maxUploadSize"
                  class="cover-img-box"
                  @click="uploadCoverImg"
                >
                  <div class="cover-img">
                    <div class="cover-img-hover">
                      <img
                        :src="addIcon"
                        alt=""
                        class="cover-img-add-icon"
                      >
                    </div>
                  </div>
                </div>

                <div
                  v-for="(articleImg, index) in form.articleImg"
                  :key="articleImg.id"
                  class="cover-img-box"
                >
                  <div class="cover-img">
                    <div
                      class="cover-img-over"
                      @mouseenter="showCoverImgIcon(index)"
                      @mouseleave="hideCoverImgIcon(index)"
                    >
                      <img
                        :src="articleImg.downloadUrl"
                        alt=""
                        class="cover-img-img-icon"
                      >

                      <span
                        class="cover-img-close-icon"
                        @click="removeCoverImg(articleImg, index)"
                      >
                        ×
                      </span>

                      <span
                        class="cover-img-replace-icon"
                        @click="replaceCoverImg(articleImg, index)"
                      >
                        替换
                      </span>
                    </div>
                  </div>
                </div>
              </div>

              <span
                v-if="form.coverImageType === 2"
                class="form-tip"
              >
                建议尺寸 16:9，最多支持 4 张图片
              </span>

              <!-- 默认封面 -->
              <img
                v-if="form.coverImageType === 3"
                :src="articleDefaultImg || noImg"
                alt=""
                class="default-cover-img"
              >

              <!-- 标题封面 -->
              <span
                v-if="form.coverImageType === 4"
                class="form-tip"
              >
                后台会根据文章标题自动生成文章封面
              </span>

              <!-- 无封面 -->
              <span
                v-if="form.coverImageType === 5"
                class="form-tip"
              >
                本文章不使用封面
              </span>
            </el-form-item>

            <!-- 摘要 -->
            <el-form-item label="文章摘要：" prop="summary">
              <el-input
                v-model="form.summary"
                type="textarea"
                resize="none"
                :autosize="{ minRows: 5, maxRows: 5 }"
                maxlength="250"
                show-word-limit
                placeholder="请输入文章摘要"
              />

              <el-button
                round
                class="set-remark-btn"
                @click="autoSetArticleSummary"
              >
                一键提取
              </el-button>
            </el-form-item>

            <!-- 文章类型 -->
            <el-form-item label="文章类型：" prop="articleType">
              <el-radio-group v-model="form.articleType">
                <el-radio
                  v-for="item in articleTypeEnums"
                  :key="item.value"
                  :label="item.value"
                >
                  {{ item.name }}
                </el-radio>
              </el-radio-group>
            </el-form-item>

            <!-- 转载信息 -->
            <template v-if="form.articleType !== 1">
              <el-form-item
                label="来源名称："
                prop="sourceName"
              >
                <el-input
                  v-model.trim="form.sourceName"
                  maxlength="100"
                  placeholder="例如：掘金、公众号、个人博客"
                />
              </el-form-item>

              <el-form-item
                label="原作者："
                prop="sourceAuthor"
              >
                <el-input
                  v-model.trim="form.sourceAuthor"
                  maxlength="100"
                  placeholder="请输入原作者"
                />
              </el-form-item>

              <el-form-item
                label="原文地址："
                prop="sourceUrl"
              >
                <el-input
                  v-model.trim="form.sourceUrl"
                  maxlength="1000"
                  show-word-limit
                  placeholder="请输入原文地址"
                />
              </el-form-item>
            </template>

            <!-- 分类 -->
            <el-form-item
              label="文章分类："
              prop="categoryId"
            >
              <el-select
                v-model="form.categoryId"
                placeholder="请选择文章分类"
                clearable
                filterable
                style="width: 300px"
              >
                <el-option-group
                  v-for="group in articleCategoryOptions"
                  :key="group.id"
                  :label="group.name"
                >
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
                v-model="form.articleTag"
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
                <el-option
                  v-for="item in articleTagOptions"
                  :key="item.id"
                  :value="item.id"
                  :label="item.name"
                />
              </el-select>

              <span class="form-tip-inline">
                输入文字搜索，按回车可添加自定义标签
              </span>
            </el-form-item>

            <!-- 访问权限 -->
            <el-form-item
              label="访问权限："
              prop="articlePerm"
            >
              <el-radio-group v-model="form.articlePerm">
                <el-radio
                  v-for="item in articlePermEnums"
                  :key="item.value"
                  :label="item.value"
                >
                  {{ item.name }}
                </el-radio>
              </el-radio-group>
            </el-form-item>

            <!-- 密码 -->
            <el-form-item
              v-if="form.articlePerm === 3"
              label="访问密码："
              prop="accessPassword"
            >
              <el-input
                v-model="form.accessPassword"
                type="password"
                maxlength="200"
                show-password
                placeholder="请输入文章访问密码"
                style="width: 300px"
              />
            </el-form-item>

            <!-- 评论 -->
            <el-form-item
              label="评论设置："
              prop="commentFlag"
            >
              <el-radio-group v-model="form.commentFlag">
                <el-radio
                  v-for="item in commentEnums"
                  :key="String(item.value)"
                  :label="item.value"
                >
                  {{ item.name }}
                </el-radio>
              </el-radio-group>
            </el-form-item>

            <!-- 置顶 -->
            <el-form-item label="文章置顶：">
              <el-switch
                v-model="form.topFlag"
                active-text="置顶"
                inactive-text="不置顶"
              />
            </el-form-item>

            <!-- 推荐 -->
            <el-form-item label="文章推荐：">
              <el-switch
                v-model="form.recommendFlag"
                active-text="推荐"
                inactive-text="不推荐"
              />
            </el-form-item>

            <!-- SEO -->
            <el-divider content-position="left">
              SEO 设置
            </el-divider>

            <el-form-item label="SEO 标题：">
              <el-input
                v-model.trim="form.seoTitle"
                maxlength="255"
                show-word-limit
                placeholder="留空时使用文章标题"
              />
            </el-form-item>

            <el-form-item label="SEO 关键词：">
              <el-input
                v-model.trim="form.seoKeywords"
                maxlength="255"
                show-word-limit
                placeholder="多个关键词使用英文逗号分隔"
              />
            </el-form-item>

            <el-form-item label="SEO 描述：">
              <el-input
                v-model="form.seoDescription"
                type="textarea"
                resize="none"
                :autosize="{ minRows: 3, maxRows: 5 }"
                maxlength="500"
                show-word-limit
                placeholder="请输入 SEO 描述"
              />
            </el-form-item>
          </el-form>
        </div>
      </div>
    </div>

    <!-- 定时发布 -->
    <el-dialog
      append-to-body
      title="定时发布"
      :close-on-click-modal="false"
      :visible.sync="choosePublishTimeDialogVisible"
      width="430px"
    >
      <div class="publish-timer-content">
        <p class="publish-time-desc">
          请选择当前时间后 1 小时至 3 天进行定时发布
        </p>

        <div class="publish-ipt-date-box">
          <el-date-picker
            v-model="publishDate"
            :picker-options="publishDateRange"
            value-format="yyyy-MM-dd"
            type="date"
            placeholder="选择日期"
            style="width: 180px"
            @change="changeDatePicker"
          />

          <el-time-select
            v-model="publishTime"
            :picker-options="publishTimeRange"
            :disabled="publishTimeDisabled"
            value-format="HH:mm"
            placeholder="选择时间"
            style="width: 180px; margin-left: 8px"
          />
        </div>

        <p
          v-show="publishDateTimeShow"
          class="publish-time-txt"
        >
          本文将于
          <span class="publish-time">
            {{ publishDateTime }}
          </span>
          发布
        </p>
      </div>

      <div slot="footer" class="dialog-footer">
        <el-button
          round
          @click="cancelPublishByTime"
        >
          取 消
        </el-button>

        <el-button
          round
          type="danger"
          :disabled="!publishDateTimeShow"
          :loading="submitByTimeLoading"
          @click="publishArticleByTiming"
        >
          定时发布
        </el-button>
      </div>
    </el-dialog>

    <!-- 封面上传 -->
    <uploadImgDialog
      ref="uploadImgDialogRef"
      :dialog-visible="uploadImgDialogVisible"
      :max-upload-size="maxUploadSize"
      @uploadImgDone="uploadImgDone"
    />

    <!-- 内容版本 -->
    <contentEditRecordDrawer
      ref="contentEditRecordDrawerRef"
      :dialog-visible="contentRecordDrawerVisible"
      @chooseRecordDone="regainArticleContent"
    />
  </div>
</template>

<script>
import NoImg from '@/assets/images/no-img-4_3.jpg'
import AddIcon from '@/assets/images/add-icon.png'
import uploadImgDialog from '@/views/admin/content/article/template/uploadImgDialog'
import contentEditRecordDrawer from '@/views/admin/content/article/template/contentEditRecordDrawer'

export default {
  name: 'AddArticle',

  components: {
    uploadImgDialog,
    contentEditRecordDrawer
  },

  data() {
    return {
      pageLoading: false,
      submitLoading: false,
      draftLoading: false,
      submitByTimeLoading: false,

      noImg: NoImg,
      addIcon: AddIcon,

      /**
       * 最近一次正式保存的 Markdown 内容。
       *
       * 用于判断自动保存时文章内容是否发生变化。
       */
      oldContent: '',

      /**
       * 当前编辑器字数。
       */
      totalCount: 0,

      /**
       * 自动保存相关状态。
       */
      interval: null,
      saveDraftTime: '',
      saveDraftSpanShow: false,

      /**
       * 是否已经完成页面初始化。
       */
      callActivated: false,

      /**
       * 是否显示最近草稿。
       */
      showRecentDraftFlag: false,

      recentDraft: {
        id: '',
        title: ''
      },

      /**
       * 当前编辑文章的表单。
       *
       * 字段对应新的 article 表结构。
       */
      form: {
        action: '',
        id: '',
        title: '',
        slug: '',
        summary: '',
        contentMd: '',

        categoryId: '',
        authorId: '',

        seoTitle: '',
        seoKeywords: '',
        seoDescription: '',

        articleType: 1,
        articleStatus: 1,

        articlePerm: 1,
        accessPassword: '',

        sourceName: '',
        sourceAuthor: '',
        sourceUrl: '',

        commentFlag: true,
        topFlag: false,
        recommendFlag: false,

        coverImageType: 3,
        articleImg: [],

        articleTag: [],

        publishTime: ''
      },

      /**
       * 表单校验规则。
       */
      rules: {
        title: [
          {
            required: true,
            message: '请输入文章标题',
            trigger: 'blur'
          }
        ],

        slug: [
          {
            required: true,
            message: '请输入文章 Slug',
            trigger: 'blur'
          }
        ],

        coverImageType: [
          {
            required: true,
            message: '请选择封面类型',
            trigger: 'change'
          }
        ],

        articleType: [
          {
            required: true,
            message: '请选择文章类型',
            trigger: 'change'
          }
        ],

        summary: [
          {
            required: true,
            message: '请填写文章摘要',
            trigger: 'blur'
          }
        ],

        categoryId: [
          {
            required: true,
            message: '请选择文章分类',
            trigger: 'change'
          }
        ],

        articlePerm: [
          {
            required: true,
            message: '请选择文章访问权限',
            trigger: 'change'
          }
        ],

        accessPassword: [
          {
            required: true,
            message: '请输入访问密码',
            trigger: 'blur'
          }
        ],

        sourceUrl: [
          {
            required: true,
            message: '请填写原文地址',
            trigger: 'blur'
          }
        ],

        commentFlag: [
          {
            required: true,
            message: '请选择评论设置',
            trigger: 'change'
          }
        ]
      },

      /**
       * 封面类型。
       */
      coverImageTypeEnums: [
        {
          name: '单封面',
          value: 1
        },
        {
          name: '多封面',
          value: 2,
          disabled: false
        },
        {
          name: '系统默认',
          value: 3
        },
        {
          name: '自动生成',
          value: 4
        },
        {
          name: '无封面',
          value: 5
        },
        {
          name: '多封面随机',
          value: 6,
          disabled: false
        }
      ],

      /**
       * 文章类型。
       */
      articleTypeEnums: [
        {
          name: '原创',
          value: 1
        },
        {
          name: '转载',
          value: 2
        },
        {
          name: '翻译',
          value: 3
        }
      ],

      /**
       * 文章访问权限。
       */
      articlePermEnums: [
        {
          name: '全部可见',
          value: 1
        },
        {
          name: '仅我可见',
          value: 2
        },
        {
          name: '密码访问',
          value: 3
        }
      ],

      /**
       * 评论设置。
       */
      commentEnums: [
        {
          name: '开启评论',
          value: true
        },
        {
          name: '关闭评论',
          value: false
        }
      ],

      /**
       * 分类。
       */
      articleCategoryOptions: [],

      /**
       * 标签。
       */
      articleTagOptions: [],
      articleTagFullOptions: [],
      tagSearchLoading: false,

      /**
       * 系统默认封面。
       */
      articleDefaultImg: '',

      /**
       * 封面上传。
       */
      uploadImgDialogVisible: false,

      /**
       * 内容版本抽屉。
       */
      contentRecordDrawerVisible: false,

      /**
       * 定时发布。
       */
      choosePublishTimeDialogVisible: false,
      publishDate: '',
      publishTime: '',
      publishTimeDisabled: true,

      publishDateRange: {
        disabledDate(time) {
          const now = new Date()

          const start = new Date(
            now.getTime() + 60 * 60 * 1000
          )

          const end = new Date(
            now.getTime() + 72 * 60 * 60 * 1000
          )

          const startDate = new Date(
            start.getFullYear(),
            start.getMonth(),
            start.getDate()
          )

          const endDate = new Date(
            end.getFullYear(),
            end.getMonth(),
            end.getDate()
          )

          return (
            time.getTime() < startDate.getTime() ||
            time.getTime() > endDate.getTime()
          )
        }
      },

      publishTimeRange: {
        start: '00:00',
        step: '15:00',
        end: '23:45'
      }
    }
  },

  computed: {
    /**
     * 当前封面最大数量。
     */
    maxUploadSize() {
      if (
        this.form.coverImageType === 2 ||
        this.form.coverImageType === 6
      ) {
        return 4
      }

      return 1
    },

    /**
     * 草稿按钮名称。
     */
    draftBtnName() {
      if (
        this.form.articleStatus == null ||
        this.form.articleStatus === '' ||
        this.form.articleStatus === 1
      ) {
        return '保存草稿'
      }

      return '移至草稿箱'
    },

    /**
     * 是否显示定时发布。
     */
    autoPublishBtnShow() {
      return (
        this.form.articleStatus == null ||
        this.form.articleStatus === '' ||
        this.form.articleStatus === 1 ||
        this.form.articleStatus === 5
      )
    },

    /**
     * 定时发布时间。
     */
    publishDateTime() {
      if (!this.publishDate || !this.publishTime) {
        return ''
      }

      return this.publishDate + ' ' + this.publishTime
    },

    /**
     * 是否已经选择完整的发布时间。
     */
    publishDateTimeShow() {
      return !!(
        this.publishDate &&
        this.publishTime
      )
    }
  },

  watch: {
    /**
     * 处理 keep-alive 下的路由变化。
     */
    $route: {
      immediate: true,

      handler(to) {
        if (
          to.path === '/ac/article/publish' &&
          to.query._f != null
        ) {
          const articleId = to.query.articleId

          if (articleId) {
            this.clearRecentDraft()
            this.resetSaveDraftData()
            this.getArticleDetail(articleId)

            this.$router.push({
              name: 'AddArticle',
              query: {
                articleId
              }
            })
          }
        }
      }
    },

    /**
     * 私密文章不允许定时发布。
     */
    'form.articlePerm'(value) {
      if (value === 2 || value === 3) {
        this.publishDate = ''
        this.publishTime = ''
        this.publishTimeDisabled = true
      }
    }
  },

  created() {
    this.resetSaveDraftData()
    this.getArticleTagList()

    const articleId = this.$route.query.articleId

    if (articleId) {
      this.clearRecentDraft()
      this.getArticleDetail(articleId)
    } else {
      this.queryRecentDraft()
      this.autoGenerateId()
    }

    this.$nextTick(() => {
      this.callActivated = true
    })
  },

  mounted() {
    this.getDefaultArticleImg()
    this.getArticleCategoryTree()

    this.$nextTick(() => {
      this.addBeforeunloadEventListener()
      this.openAutoSaveEvent()
    })
  },

  activated() {
    if (this.callActivated) {
      const articleId = this.$route.query.articleId

      if (articleId) {
        this.clearRecentDraft()

        if (articleId !== this.form.id) {
          this.getArticleTagList()
          this.resetSaveDraftData()
          this.getArticleDetail(articleId)
        }
      } else if (this.form.action !== 'add') {
        this.clearData()
        this.getArticleTagList()
        this.resetSaveDraftData()
        this.queryRecentDraft()
        this.autoGenerateId()
      }
    }

    this.$nextTick(() => {
      this.openAutoSaveEvent()
    })
  },

  deactivated() {
    this.preDoAutoSaveArticleContent()
    this.closeAutoSaveEvent()
  },

  beforeDestroy() {
    this.preDoAutoSaveArticleContent()
    this.closeAutoSaveEvent()
    this.removeBeforeunloadEventListener()
  },

  beforeRouteLeave(to, from, next) {
    this.preDoAutoSaveArticleContent()
    next()
  },

  methods: {
    /**
     * 获取用户 Token。
     *
     * Markdown 编辑器的上传方法由 Muses 注入，
     * 这里保留该方法，方便后续统一处理认证。
     */
    getUserToken() {
      try {
        const token = this.$store.getters.token

        if (token) {
          return token
        }
      } catch (e) {
        // 忽略 getter 不存在的情况
      }

      return ''
    },

    /**
     * 页面离开前保存草稿。
     */
    addBeforeunloadEventListener() {
      window.addEventListener(
        'beforeunload',
        this.beforeUnloadHandler,
        false
      )
    },

    /**
     * 移除页面离开事件。
     */
    removeBeforeunloadEventListener() {
      window.removeEventListener(
        'beforeunload',
        this.beforeUnloadHandler,
        false
      )
    },

    /**
     * 浏览器关闭前保存内容。
     */
    beforeUnloadHandler(event) {
      this.preDoAutoSaveArticleContent()

      event.returnValue = ''
    },

    /**
     * 重置草稿保存状态。
     */
    resetSaveDraftData() {
      this.saveDraftTime = ''
      this.saveDraftSpanShow = false
    },

    /**
     * 清除最近草稿提示。
     */
    clearRecentDraft() {
      this.recentDraft.id = ''
      this.recentDraft.title = ''
      this.showRecentDraftFlag = false
    },

    /**
     * 查询最近草稿。
     */
    queryRecentDraft() {
      this.$mapi.article.queryLastArticleDraft()
        .then(({ data }) => {
          if (!data) {
            this.clearRecentDraft()
            return
          }

          this.showRecentDraftFlag = true
          this.recentDraft.id = data.id
          this.recentDraft.title = data.title || '未命名文章'
        })
        .catch(() => {
          this.clearRecentDraft()
        })
    },

    /**
     * 自动生成文章 ID。
     */
    autoGenerateId() {
      this.$mapi.communal.getId()
        .then(({ data }) => {
          this.form.action = 'add'
          this.form.id = data
          this.pageLoading = false
        })
        .catch(() => {
          this.form.id = ''
          this.pageLoading = false
        })
    },

    /**
     * 查询文章详情。
     *
     * 后端建议直接返回：
     *
     * {
     *   id,
     *   title,
     *   slug,
     *   summary,
     *   contentMd,
     *   categoryId,
     *   ...
     * }
     */
    getArticleDetail(articleId) {
      this.pageLoading = true

      this.$mapi.article.queryArticleDetail({
        articleId
      })
        .then(({ data }) => {
          if (!data) {
            this.$message.error('文章不存在')
            return
          }

          if (
            data.deleteFlag != null &&
            data.deleteFlag !== 0
          ) {
            this.$message.error(
              '文章处于回收站中，不允许编辑，请先恢复文章'
            )
            return
          }

          this.form = this.normalizeArticleData(data)

          this.form.action = 'edit'

          this.oldContent = this.form.contentMd || ''

          this.updateTotalCount()

          this.initPublishTime(this.form.publishTime)
        })
        .catch(error => {
          console.log(
            'getArticleDetail failed',
            error
          )

          this.$message.error(
            '加载文章信息失败，请刷新浏览器再试！'
          )
        })
        .finally(() => {
          this.pageLoading = false
        })
    },

    /**
     * 将后端返回的数据转换成页面模型。
     *
     * 兼容旧接口中的部分字段名称，
     * 方便数据库/后端逐步迁移。
     */
    normalizeArticleData(data) {
      return {
        action: 'edit',

        id: data.id || '',
        title: data.title || '',
        slug: data.slug || '',
        summary: data.summary || data.remark || '',
        contentMd: data.contentMd || data.content || '',

        categoryId: data.categoryId || '',
        authorId: data.authorId || '',

        seoTitle: data.seoTitle || '',
        seoKeywords: data.seoKeywords || '',
        seoDescription: data.seoDescription || '',

        articleType: data.articleType || 1,
        articleStatus:
          data.articleStatus == null
            ? 1
            : data.articleStatus,

        articlePerm:
          data.articlePerm == null
            ? 1
            : data.articlePerm,

        accessPassword: data.accessPassword || '',

        sourceName:
          data.sourceName ||
          '',

        sourceAuthor:
          data.sourceAuthor ||
          '',

        sourceUrl:
          data.sourceUrl ||
          data.reprintLink ||
          '',

        commentFlag:
          data.commentFlag == null
            ? true
            : Boolean(data.commentFlag),

        topFlag:
          data.topFlag == null
            ? false
            : Boolean(data.topFlag),

        recommendFlag:
          data.recommendFlag == null
            ? false
            : Boolean(data.recommendFlag),

        coverImageType:
          data.coverImageType == null
            ? 3
            : data.coverImageType,

        articleImg:
          Array.isArray(data.articleImg)
            ? data.articleImg
            : [],

        articleTag:
          Array.isArray(data.articleTag)
            ? data.articleTag
            : [],

        publishTime:
          data.publishTime || ''
      }
    },

    /**
     * 初始化发布时间。
     */
    initPublishTime(publishTime) {
      if (!publishTime) {
        this.publishDate = ''
        this.publishTime = ''
        this.publishTimeDisabled = true
        return
      }

      const date = new Date(
        String(publishTime).replace(/-/g, '/')
      )

      if (Number.isNaN(date.getTime())) {
        return
      }

      this.publishDate =
        date.getFullYear() +
        '-' +
        this.padZero(date.getMonth() + 1) +
        '-' +
        this.padZero(date.getDate())

      this.publishTime =
        this.padZero(date.getHours()) +
        ':' +
        this.padZero(date.getMinutes())

      this.publishTimeDisabled = false
    },

    /**
     * 数字补零。
     */
    padZero(value) {
      return value < 10 ? '0' + value : String(value)
    },

    /**
     * 获取系统默认文章封面。
     */
    getDefaultArticleImg() {
      this.$mapi.article.queryArticleDefaultImg()
        .then(({ data }) => {
          this.articleDefaultImg = data || ''
        })
        .catch(() => {
          this.articleDefaultImg = ''
        })
    },

    /**
     * 获取文章分类树。
     */
    getArticleCategoryTree() {
      this.$mapi.article.queryCategoryTree()
        .then(({ data }) => {
          if (
            Array.isArray(data) &&
            data.length > 0
          ) {
            this.articleCategoryOptions = data
          } else {
            this.articleCategoryOptions = []

            this.$message.warning(
              '文章分类列表为空，请先维护文章分类信息'
            )
          }
        })
        .catch(() => {
          this.articleCategoryOptions = []
        })
    },

    /**
     * 获取文章标签。
     */
    getArticleTagList() {
      this.$mapi.article.queryTagList()
        .then(({ data }) => {
          this.articleTagOptions = data || []
          this.articleTagFullOptions = data || []
        })
        .catch(() => {
          this.articleTagOptions = []
          this.articleTagFullOptions = []
        })
    },

    /**
     * 标签搜索。
     */
    customTagSearchWithoutCase(query) {
      if (!query) {
        this.articleTagOptions =
          this.articleTagFullOptions

        return
      }

      this.tagSearchLoading = true

      setTimeout(() => {
        this.tagSearchLoading = false

        const keyword =
          query.toLowerCase()

        this.articleTagOptions =
          this.articleTagFullOptions.filter(item => {
            return (
              item.name &&
              item.name
                .toLowerCase()
                .indexOf(keyword) > -1
            )
          })
      }, 200)
    },

    /**
     * 自动生成 Slug。
     *
     * 这里不强制依赖第三方 slug 库，
     * 只做简单中文/英文标题转换。
     *
     * 真正唯一性由后端校验。
     */
    generateSlug() {
      if (!this.form.title) {
        return
      }

      const slug = this.form.title
        .trim()
        .toLowerCase()
        .replace(/[\s]+/g, '-')
        .replace(/[^\w\u4e00-\u9fa5-]/g, '')
        .replace(/-+/g, '-')

      if (!slug) {
        this.$message.warning(
          '当前标题无法自动生成 Slug，请手动填写'
        )
        return
      }

      this.form.slug = slug.substring(0, 150)
    },

    /**
     * 自动提取摘要。
     *
     * Markdown 中去除常见语法后，
     * 截取前 250 个字符。
     */
    autoSetArticleSummary() {
      const content = this.form.contentMd || ''

      const text = content
        .replace(/```[\s\S]*?```/g, '')
        .replace(/!\[.*?]\(.*?\)/g, '')
        .replace(/\[([^\]]+)]\([^)]+\)/g, '$1')
        .replace(/[#>*_~`]/g, '')
        .replace(/\s+/g, ' ')
        .trim()

      this.form.summary = text.substring(0, 250)

      this.$refs.articleForm.validateField(
        'summary'
      )
    },

    /**
     * 更新文章字数。
     */
    updateTotalCount() {
      const content = this.form.contentMd || ''

      const text = content
        .replace(/```[\s\S]*?```/g, '')
        .replace(/!\[.*?]\(.*?\)/g, '')
        .replace(/[#>*_~`]/g, '')
        .replace(/\s+/g, '')

      this.totalCount = text.length
    },

    /**
     * Markdown 编辑器内容变化。
     */
    onContentChange(content) {
      this.form.contentMd = content || ''
      this.updateTotalCount()
    },

    /**
     * 图片上传。
     *
     * Muses Markdown Editor 不直接依赖 zblog，
     * 由当前业务页面注入上传方法。
     *
     * @param file 图片文件
     * @return Promise
     */
    uploadImage(file) {
      return this.uploadEditorFile(
        file,
        'image'
      )
    },

    /**
     * 视频上传。
     *
     * @param file 视频文件
     * @return Promise
     */
    uploadVideo(file) {
      return this.uploadEditorFile(
        file,
        'video'
      )
    },

    /**
     * 编辑器文件上传。
     *
     * 这里兼容你当前 $mapi.file.upload 的返回结构。
     */
    uploadEditorFile(file, type) {
      return this.$mapi.file.upload(file)
        .then(res => {
          const data = res && res.data

          if (
            !Array.isArray(data) ||
            data.length === 0
          ) {
            throw new Error(
              type === 'image'
                ? '图片上传失败'
                : '视频上传失败'
            )
          }

          const fileData = data[0]

          if (
            type === 'image' &&
            fileData.fileType &&
            fileData.fileType !== '图片'
          ) {
            throw new Error('只能上传图片')
          }

          if (
            type === 'video' &&
            fileData.fileType &&
            fileData.fileType !== '视频'
          ) {
            throw new Error('只能上传视频')
          }

          return {
            url: fileData.downloadUrl,
            name: fileData.originalFilename,
            id: fileData.id
          }
        })
        .catch(error => {
          this.$message.error(
            error.message ||
            (type === 'image'
              ? '图片上传失败'
              : '视频上传失败')
          )

          throw error
        })
    },

    /**
     * 查询最近草稿。
     */
    openDraftPage(articleId) {
      this.$router.push({
        name: 'AddArticle',
        query: {
          articleId,
          _f: ''
        }
      })
    },

    /**
     * 打开草稿列表。
     */
    openMoreDraftPage() {
      this.$router.push({
        name: 'Article',
        query: {
          _ts: 7
        }
      })
    },

    /**
     * 关闭最近草稿提示。
     */
    closeDraftPage() {
      this.showRecentDraftFlag = false
    },

    /**
     * 开启自动保存。
     *
     * 默认每 60 秒检查一次。
     */
    openAutoSaveEvent() {
      this.closeAutoSaveEvent()

      this.interval = window.setInterval(() => {
        this.preDoAutoSaveArticleContent()
      }, 60000)
    },

    /**
     * 关闭自动保存。
     */
    closeAutoSaveEvent() {
      if (this.interval != null) {
        window.clearInterval(this.interval)
        this.interval = null
      }
    },

    /**
     * 自动保存前置检查。
     */
    preDoAutoSaveArticleContent() {
      if (!this.form.id) {
        return
      }

      const content =
        this.form.contentMd || ''

      if (!content.trim()) {
        return
      }

      this.updateTotalCount()

      if (this.oldContent === '') {
        this.oldContent = content
        return
      }

      if (this.oldContent !== content) {
        this.doAutoSaveArticleContent()
      }
    },

    /**
     * 自动保存文章内容。
     *
     * 对应：
     * article_content_draft_record
     */
    doAutoSaveArticleContent() {
      if (!this.form.id) {
        return
      }

      const param = {
        id: this.form.id,
        articleId: this.form.id,
        title: this.form.title,
        remark: this.form.summary,
        summary: this.form.summary,
        content: this.form.contentMd,
        contentMd: this.form.contentMd,
        wordCount: this.totalCount,
        saveTime: null
      }

      this.$mapi.article
        .autoSaveArticleContent(param)
        .then(({ data }) => {
          if (data != null) {
            this.oldContent =
              this.form.contentMd

            this.saveDraftTime =
              data.saveTime ||
              data.saveTimeText ||
              data

            this.saveDraftSpanShow = true
          }
        })
        .catch(error => {
          console.log(
            'auto save article content failed',
            error
          )
        })
    },

    /**
     * 保存草稿。
     */
    saveDraft() {
      this.updateTotalCount()

      this.form.articleStatus = 1

      const param =
        this.buildSubmitData()

      param.articleStatus = 1

      this.draftLoading = true

      this.$mapi.article.saveArticleDraft(param)
        .then(({ data, message }) => {
          this.$message.success(
            message || '草稿保存成功'
          )

          this.form.action = 'edit'
          this.form.articleStatus = 1

          this.oldContent =
            this.form.contentMd

          this.saveDraftTime =
            data || this.getCurrentTimeText()

          this.saveDraftSpanShow = true
        })
        .catch(() => {
          this.saveDraftTime = ''
          this.saveDraftSpanShow = false
        })
        .finally(() => {
          this.draftLoading = false
        })
    },

    /**
     * 构造文章提交参数。
     *
     * 页面模型和后端 DTO 解耦，
     * 后续后端字段调整时只修改这里。
     */
    buildSubmitData() {
      this.updateTotalCount()

      return {
        id: this.form.id,
        title: this.form.title,
        slug: this.form.slug,
        summary: this.form.summary,
        contentMd: this.form.contentMd,

        categoryId: this.form.categoryId,
        authorId: this.form.authorId,

        seoTitle: this.form.seoTitle,
        seoKeywords: this.form.seoKeywords,
        seoDescription: this.form.seoDescription,

        articleType: this.form.articleType,
        articleStatus: this.form.articleStatus,

        articlePerm: this.form.articlePerm,
        accessPassword:
          this.form.articlePerm === 3
            ? this.form.accessPassword
            : '',

        sourceName:
          this.form.articleType === 1
            ? ''
            : this.form.sourceName,

        sourceAuthor:
          this.form.articleType === 1
            ? ''
            : this.form.sourceAuthor,

        sourceUrl:
          this.form.articleType === 1
            ? ''
            : this.form.sourceUrl,

        commentFlag: this.form.commentFlag,
        topFlag: this.form.topFlag,
        recommendFlag:
        this.form.recommendFlag,

        coverImageType:
        this.form.coverImageType,

        articleImg:
        this.form.articleImg,

        articleTag:
        this.form.articleTag,

        wordsCount:
        this.totalCount,

        publishTime:
          this.form.publishTime || null
      }
    },

    /**
     * 打开定时发布窗口。
     */
    openPublishArticleByTimingDialog() {
      if (!this.validateBasicInfo()) {
        return
      }

      if (
        this.form.articlePerm === 2 ||
        this.form.articlePerm === 3
      ) {
        this.$message.error(
          '私密或密码文章暂不支持定时发布'
        )

        return
      }

      this.$refs.articleForm.validate(valid => {
        if (!valid) {
          return
        }

        this.openPublishTimeDialog()
      })
    },

    /**
     * 打开时间选择窗口。
     */
    openPublishTimeDialog() {
      this.submitByTimeLoading = false
      this.choosePublishTimeDialogVisible = true
    },

    /**
     * 取消定时发布。
     */
    cancelPublishByTime() {
      this.publishDate = ''
      this.publishTime = ''
      this.publishTimeDisabled = true
      this.choosePublishTimeDialogVisible = false
    },

    /**
     * 日期变化。
     */
    changeDatePicker(date) {
      if (!date) {
        this.publishTime = ''
        this.publishTimeDisabled = true
        return
      }

      const now = new Date()

      const startTime = new Date(
        now.getTime() +
        60 * 60 * 1000
      )

      const endTime = new Date(
        now.getTime() +
        72 * 60 * 60 * 1000
      )

      const startDate =
        this.formatDate(startTime)

      const endDate =
        this.formatDate(endTime)

      if (date === startDate) {
        this.publishTimeRange.start =
          this.calculateTime(
            true,
            startTime.getHours(),
            startTime.getMinutes()
          )

        this.publishTimeRange.end =
          '23:45'
      } else if (date === endDate) {
        this.publishTimeRange.start =
          '00:00'

        this.publishTimeRange.end =
          this.calculateTime(
            false,
            endTime.getHours(),
            endTime.getMinutes()
          )
      } else {
        this.publishTimeRange.start =
          '00:00'

        this.publishTimeRange.end =
          '23:45'
      }

      this.publishTimeDisabled = false

      if (
        this.publishTime &&
        !this.isTimeInRange(
          this.publishTime,
          this.publishTimeRange.start,
          this.publishTimeRange.end
        )
      ) {
        this.publishTime = ''
      }
    },

    /**
     * 计算日期。
     */
    formatDate(date) {
      return (
        date.getFullYear() +
        '-' +
        this.padZero(date.getMonth() + 1) +
        '-' +
        this.padZero(date.getDate())
      )
    },

    /**
     * 计算时间选择器边界。
     */
    calculateTime(isStart, hour, minute) {
      if (isStart) {
        if (minute < 15) {
          return (
            this.padZero(hour) +
            ':15'
          )
        }

        if (minute < 30) {
          return (
            this.padZero(hour) +
            ':30'
          )
        }

        if (minute < 45) {
          return (
            this.padZero(hour) +
            ':45'
          )
        }

        hour++

        if (hour >= 24) {
          return '23:45'
        }

        return (
          this.padZero(hour) +
          ':00'
        )
      }

      if (minute < 15) {
        return (
          this.padZero(hour) +
          ':00'
        )
      }

      if (minute < 30) {
        return (
          this.padZero(hour) +
          ':15'
        )
      }

      if (minute < 45) {
        return (
          this.padZero(hour) +
          ':30'
        )
      }

      return (
        this.padZero(hour) +
        ':45'
      )
    },

    /**
     * 判断时间是否在范围内。
     */
    isTimeInRange(
      time,
      start,
      end
    ) {
      const value =
        this.timeToMinutes(time)

      const startValue =
        this.timeToMinutes(start)

      const endValue =
        this.timeToMinutes(end)

      return (
        value >= startValue &&
        value <= endValue
      )
    },

    /**
     * 时间转分钟。
     */
    timeToMinutes(time) {
      const arr = time.split(':')

      return (
        parseInt(arr[0], 10) * 60 +
        parseInt(arr[1], 10)
      )
    },

    /**
     * 定时发布。
     */
    publishArticleByTiming() {
      if (!this.publishDateTimeShow) {
        this.$message.warning(
          '请选择发布时间'
        )

        return
      }

      if (
        this.form.articlePerm === 2 ||
        this.form.articlePerm === 3
      ) {
        this.$message.error(
          '私密或密码文章暂不支持定时发布'
        )

        return
      }

      this.form.publishTime =
        this.publishDateTime

      const param =
        this.buildSubmitData()

      /*
       * 新表中的定时发布建议由：
       *
       * article_auto_publish_timing
       *
       * 负责维护，而不是长期把状态塞在 article 中。
       *
       * 当前继续复用原接口，
       * 后续后端实现调整即可。
       */
      this.submitByTimeLoading = true

      this.$mapi.article
        .publishArticleByTiming(param)
        .then(() => {
          this.cancelPublishByTime()
          this.publishSuccess()
        })
        .finally(() => {
          this.submitByTimeLoading = false
        })
    },

    /**
     * 发布文章。
     */
    publishArticle() {
      if (!this.validateBasicInfo()) {
        return
      }

      this.$refs.articleForm.validate(valid => {
        if (!valid) {
          return
        }

        const param =
          this.buildSubmitData()

        /*
         * 新增文章：
         * addArticle
         *
         * 编辑文章：
         * editArticle
         */
        this.submitLoading = true

        let request

        if (this.form.action === 'add') {
          request =
            this.$mapi.article.addArticle(
              param
            )
        } else if (
          this.form.action === 'edit'
        ) {
          request =
            this.$mapi.article.editArticle(
              param
            )
        } else {
          this.$message.error(
            '系统异常，请刷新页面重试'
          )

          this.submitLoading = false
          return
        }

        request
          .then(() => {
            this.oldContent =
              this.form.contentMd

            this.publishSuccess()
          })
          .finally(() => {
            this.submitLoading = false
          })
      })
    },

    /**
     * 校验标题和正文。
     */
    validateBasicInfo() {
      if (!this.form.title) {
        this.$message.warning(
          '请填写文章标题'
        )

        return false
      }

      if (
        !this.form.contentMd ||
        !this.form.contentMd.trim()
      ) {
        this.$message.warning(
          '请填写文章内容'
        )

        return false
      }

      return true
    },

    /**
     * 发布成功。
     */
    publishSuccess() {
      this.$router.push(
        '/publish/blog/success/' +
        this.form.id
      )

      this.clearData()
    },

    /**
     * 显示内容版本。
     */
    showContentRecord() {
      this.contentRecordDrawerVisible = true

      this.$nextTick(() => {
        if (
          this.$refs
            .contentEditRecordDrawerRef
        ) {
          this.$refs
            .contentEditRecordDrawerRef
            .initData(this.form.id)
        }
      })
    },

    /**
     * 恢复内容版本。
     *
     * 兼容原来的 contentEditRecordDrawer。
     *
     * 后续切换到：
     * article_content_version
     * 时，只需要调整 API 返回字段。
     */
    regainArticleContent(recordId) {
      this.contentRecordDrawerVisible = false

      if (!recordId) {
        return
      }

      this.doAutoSaveArticleContent()

      this.$mapi.article
        .queryAutoSaveArticleContentById({
          recordId
        })
        .then(({ data }) => {
          if (!data) {
            this.$message.error(
              '内容恢复失败'
            )

            return
          }

          const articleId =
            data.articleId

          if (
            String(articleId) !==
            String(this.form.id)
          ) {
            this.$message.error(
              '内容版本与当前文章不匹配'
            )

            return
          }

          const content =
            data.contentMd ||
            data.content ||
            ''

          this.form.contentMd =
            content

          this.form.summary =
            data.summary ||
            data.remark ||
            this.form.summary

          this.oldContent =
            content

          this.updateTotalCount()

          this.$message.success(
            '内容恢复成功'
          )
        })
        .catch(() => {
          this.$message.error(
            '内容恢复失败'
          )
        })
    },

    /**
     * 显示封面操作按钮。
     */
    showCoverImgIcon(index) {
      const closeIcons =
        document.getElementsByClassName(
          'cover-img-close-icon'
        )

      const replaceIcons =
        document.getElementsByClassName(
          'cover-img-replace-icon'
        )

      if (closeIcons[index]) {
        closeIcons[index].style.display =
          'block'
      }

      if (replaceIcons[index]) {
        replaceIcons[index].style.display =
          'block'
      }
    },

    /**
     * 隐藏封面操作按钮。
     */
    hideCoverImgIcon(index) {
      const closeIcons =
        document.getElementsByClassName(
          'cover-img-close-icon'
        )

      const replaceIcons =
        document.getElementsByClassName(
          'cover-img-replace-icon'
        )

      if (closeIcons[index]) {
        closeIcons[index].style.display =
          'none'
      }

      if (replaceIcons[index]) {
        replaceIcons[index].style.display =
          'none'
      }
    },

    /**
     * 删除封面。
     */
    removeCoverImg(file, index) {
      this.$confirm(
        `确定移除 ${file.originalFilename || '该图片'}？`
      )
        .then(() => {
          const param = {
            fileId: file.id
          }

          return this.$mapi.file.deleteFile(
            param
          )
        })
        .then(res => {
          this.form.articleImg.splice(
            index,
            1
          )

          this.$message.success(
            res.message ||
            '删除成功'
          )
        })
        .catch(() => {})
    },

    /**
     * 打开封面上传窗口。
     */
    uploadCoverImg() {
      this.uploadImgDialogVisible = true

      this.$nextTick(() => {
        this.$refs
          .uploadImgDialogRef
          .initData()
      })
    },

    /**
     * 替换封面。
     */
    replaceCoverImg(file, index) {
      this.uploadImgDialogVisible = true

      this.$nextTick(() => {
        this.$refs
          .uploadImgDialogRef
          .initData(
            true,
            index
          )
      })
    },

    /**
     * 封面上传完成。
     */
    uploadImgDone(fileList, index) {
      this.uploadImgDialogVisible = false

      const list =
        Array.isArray(fileList)
          ? fileList
          : []

      if (
        index != null &&
        index >= 0 &&
        list.length === 1 &&
        this.form.articleImg.length >
        index
      ) {
        this.$set(
          this.form.articleImg,
          index,
          list[0]
        )

        return
      }

      list.forEach(file => {
        this.form.articleImg.push(
          file
        )
      })

      if (
        this.form.articleImg.length >
        this.maxUploadSize
      ) {
        this.form.articleImg =
          this.form.articleImg.slice(
            0,
            this.maxUploadSize
          )
      }
    },

    /**
     * 清空页面数据。
     */
    clearData() {
      this.form = {
        action: '',
        id: '',
        title: '',
        slug: '',
        summary: '',
        contentMd: '',

        categoryId: '',
        authorId: '',

        seoTitle: '',
        seoKeywords: '',
        seoDescription: '',

        articleType: 1,
        articleStatus: 1,

        articlePerm: 1,
        accessPassword: '',

        sourceName: '',
        sourceAuthor: '',
        sourceUrl: '',

        commentFlag: true,
        topFlag: false,
        recommendFlag: false,

        coverImageType: 3,
        articleImg: [],

        articleTag: [],

        publishTime: ''
      }

      this.oldContent = ''
      this.totalCount = 0

      this.publishDate = ''
      this.publishTime = ''
      this.publishTimeDisabled = true

      this.resetSaveDraftData()

      if (this.$refs.articleForm) {
        this.$refs.articleForm.resetFields()
      }
    },

    /**
     * 获取当前时间文本。
     */
    getCurrentTimeText() {
      const date = new Date()

      return (
        this.padZero(date.getHours()) +
        ':' +
        this.padZero(date.getMinutes()) +
        ':' +
        this.padZero(date.getSeconds())
      )
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
  min-height: 64px;
}

.editor-content-opt-left {
  display: flex;
  align-items: center;
  min-width: 0;
  color: #555;
  font-size: 14px;
}

.editor-content-opt-left > span {
  margin-right: 15px;
}

.editor-content-opt-right {
  display: flex;
  align-items: center;
  justify-content: flex-end;
}

.save-draft-span {
  position: relative;
  padding-left: 20px;
}

.save-draft-span::before {
  content: '';
  position: absolute;
  left: 0;
  top: 50%;
  width: 14px;
  height: 14px;
  transform: translateY(-50%);
  background-image: url('../../../../../assets/images/icon-autosave.png');
  background-repeat: no-repeat;
  background-position: center;
  background-size: contain;
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

.draft-title-link {
  color: #303133;
  text-decoration: none;
  cursor: pointer;
}

.draft-title-link:hover {
  color: #409eff;
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
}
</style>
