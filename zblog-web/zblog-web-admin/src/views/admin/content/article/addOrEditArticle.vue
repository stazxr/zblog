<template>
  <div v-loading="pageLoading" element-loading-text="数据加载中..." class="editor-container">
    <div class="editor-toolbar">
      <Toolbar
        v-if="activeEditor"
        :editor="editor"
        :default-config="toolbarConfig"
        :mode="mode"
      />
    </div>
    <div class="editor-content">
      <div class="editor-content-right">
        <div class="editor-content-inner editor-content-core">
          <div v-show="showTipAlert" style="padding-bottom: 15px;">
            <el-alert title="友情提示" description="未防止会话过期、忘记保存等原因导致文章内容丢失，请及时保存草稿！" type="info" show-icon />
          </div>
          <div v-if="showRecentDraftFlag && recentDraft.id !== null && recentDraft.id !== ''" class="recent-draft-box">
            <span class="icon-type">草稿</span>
            <p class="draft-title" :title="recentDraft.title">
              <a class="draft-title-link" @click="openDraftPage(recentDraft.id)">{{ recentDraft.title }}</a>
            </p>
            <a class="draft-btn-edit" target="_self" @click="openDraftPage(recentDraft.id)">继续编辑</a>
            <a class="draft-btn-more" @click="openMoreDraftPage(draftTagFlag)">更多草稿</a>
            <button class="draft-btn-close" @click="closeDraftPage()">
              <svg data-v-c1361018="" width="200" height="200" viewBox="0 0 1024 1024" xmlns="http://www.w3.org/2000/svg" class="icon">
                <!-- eslint-disable-next-line -->
                <path data-v-c1361018="" d="M512 438.379l294.507-294.486a52.032 52.032 0 1173.6 73.622L585.62 512l294.486 294.485a52.075 52.075 0 01-73.6 73.643L512 585.621 217.515 880.128a52.053 52.053 0 11-73.622-73.643L438.38 512 143.893 217.515a52.053 52.053 0 1173.622-73.622L512 438.38z"></path>
              </svg>
            </button>
          </div>
          <div class="editor-title">
            <input v-model="addForm.title" placeholder="请输入文章标题（最多 100 个字符）" maxlength="100">
          </div>
          <div class="editor-text-area">
            <Editor
              v-if="activeEditor"
              v-model="html"
              :default-config="editorConfig"
              :mode="mode"
              @onCreated="onCreated"
              @onChange="onChange"
              @onDestroyed="onDestroyed"
              @onMaxLength="onMaxLength"
              @onFocus="onFocus"
              @onBlur="onBlur"
              @customAlert="customAlert"
            />
          </div>
        </div>
        <div class="editor-content-inner editor-content-config">
          <el-form ref="addForm" :model="addForm" :rules="addRules" label-width="100px">
            <el-form-item label="文章封面：" prop="coverImageType">
              <el-radio-group v-model="addForm.coverImageType">
                <el-radio v-for="item in coverImageTypeEnums" :key="item.value" :label="item.value" :disabled="item.disabled">{{ item.name }}</el-radio>
              </el-radio-group>
              <div v-if="addForm.coverImageType === 1" class="cover-img-box-flex cover-img-box-1">
                <!-- 单封面 -->
                <div v-if="addForm.articleImg.length < maxUploadSize" class="cover-img-box" @click="uploadCoverImg">
                  <div class="cover-img">
                    <div class="cover_img_hover">
                      <img :src="addIcon" alt="" class="cover-img-add-icon">
                    </div>
                  </div>
                </div>
                <div v-else class="cover-img-box">
                  <div class="cover-img">
                    <div class="cover_img_over" @mouseenter="showCoverImgIcon(0)" @mouseleave="hideCoverImgIcon(0)">
                      <img :src="addForm.articleImg[0]['downloadUrl']" alt="" class="cover-img-img-icon">
                      <span class="cover-img-close-icon" @click="removeCoverImg(addForm.articleImg[0], 0)">X</span>
                      <span class="cover-img-replace-icon" @click="replaceCoverImg(addForm.articleImg[0], 0)">替换</span>
                    </div>
                  </div>
                </div>
              </div>
              <span v-if="addForm.coverImageType === 1" style="display: block;">
                <strong>*支持 jpg、jpeg、png、webp 格式，单张图片最大支持 5MB，建议图片尺寸 16:9</strong>
              </span>
              <div v-if="addForm.coverImageType === 2" class="cover-img-box-flex cover-img-box-more">
                <!-- 多封面 -->
                <div v-if="addForm.articleImg.length < maxUploadSize" class="cover-img-box" @click="uploadCoverImg">
                  <div class="cover-img">
                    <div class="cover_img_hover">
                      <img :src="addIcon" alt="" class="cover-img-add-icon">
                    </div>
                  </div>
                </div>
                <div v-for="(articleImg, index) in addForm.articleImg" :key="articleImg.id" class="cover-img-box">
                  <div class="cover-img">
                    <div class="cover_img_over" @mouseenter="showCoverImgIcon(index)" @mouseleave="hideCoverImgIcon(index)">
                      <img :src="articleImg['downloadUrl']" alt="" class="cover-img-img-icon">
                      <span class="cover-img-close-icon" @click="removeCoverImg(articleImg, index)">X</span>
                      <span class="cover-img-replace-icon" @click="replaceCoverImg(articleImg, index)">替换</span>
                    </div>
                  </div>
                </div>
              </div>
              <span v-if="addForm.coverImageType === 2" style="display: block;">
                <strong>*支持 jpg、jpeg、png、webp 格式，单张图片最大支持 5MB，建议图片尺寸 16:9，最多支持 4 张图片</strong>
              </span>
              <img v-if="addForm.coverImageType === 3" :src="articleDefaultImg === '' ? noImg : articleDefaultImg" alt="" style="display: block;width: 160px; height: 90px;margin-top: 5px;">
              <span v-if="addForm.coverImageType === 4" style="display: block;margin-top: 5px;">
                <strong>*后台会根据文章标题自动生成文章封面</strong>
              </span>
            </el-form-item>
            <el-form-item label="文章摘要：" prop="remark">
              <el-input v-model="addForm.remark" type="textarea" resize="none" :autosize="{ minRows: 6, maxRows: 6 }" maxlength="250" />
              <el-button round class="set-remark-btn" @click="autoSetArticleRemark">一件提取</el-button>
            </el-form-item>
            <el-form-item label="文章类型：" prop="articleType">
              <el-radio-group v-model="addForm.articleType">
                <el-radio v-for="item in articleTypeEnums" :key="item.value" :label="item.value">{{ item.name }}</el-radio>
              </el-radio-group>
            </el-form-item>
            <el-form-item v-if="addForm.articleType !== 1" label="原文链接：" prop="reprintLink">
              <el-input v-model="addForm.reprintLink" placeholder="请填写原文链接" maxlength="1000" show-word-limit />
            </el-form-item>
            <el-form-item label="文章分类：" prop="categoryId">
              <el-select v-model="addForm.categoryId" placeholder="请选择文章分类" clearable filterable style="width: 300px">
                <el-option-group v-for="group in articleCategoryOptions" :key="group.id" :label="group.name">
                  <el-option v-for="item in group.children" :key="item.id" :value="item.id" :label="item.name" :disabled="!group.enabled || !item.enabled" />
                </el-option-group>
              </el-select>
            </el-form-item>
            <el-form-item label="文章标签：" prop="articleTag">
              <el-select
                v-model="addForm.articleTag"
                placeholder="请选择文章标签"
                multiple
                :multiple-limit="3"
                filterable
                :remote-method="customTagSearchWithoutCase"
                :loading="tagSearchLoading"
                allow-create
                default-first-option
                clearable
                style="width: 300px"
              >
                <el-option v-for="item in articleTagOptions" :key="item.id" :value="item.id" :label="item.name" />
              </el-select>
              <span style="margin-left: 5px;color: gray;">输入文字可搜索，按回车可添加自定义标签</span>
            </el-form-item>
            <el-form-item label="发布范围：" prop="articlePerm">
              <el-radio-group v-model="addForm.articlePerm">
                <el-radio v-for="item in articlePermEnums" :key="item.value" :label="item.value">{{ item.name }}</el-radio>
              </el-radio-group>
            </el-form-item>
            <el-form-item label="评论设置：" prop="commentFlag">
              <el-radio-group v-model="addForm.commentFlag">
                <el-radio v-for="item in commentEnums" :key="item.value" :label="item.value">{{ item.name }}</el-radio>
              </el-radio-group>
            </el-form-item>
          </el-form>
          <div class="editor-content-config-opt">
            <div class="editor-content-config-opt-left">
              <span v-show="saveDraftSpanShow" class="save-draft-span">草稿已保存 {{ saveDraftTime }} </span>
              <span>共 {{ totalCount }} 字</span>
            </div>
            <div class="editor-content-config-opt-right">
              <el-button v-show="addForm.id != null && addForm.id !== ''" round @click="showContentRecord">编辑记录</el-button>
              <el-button round @click="saveDraft">{{ draftBtnName }}</el-button>
              <el-button v-show="autoPublishBtnShow" round @click="openPublishArticleByTimingDialog">定时发布</el-button>
              <el-button type="danger" round :loading="submitLoading" @click="publishArticle">发布文章</el-button>
            </div>
          </div>
        </div>
      </div>
      <div v-show="false" class="editor-content-left">
        <div class="editor-header-content">
          <h2 style="padding-left: 20px;">目录</h2>
          <hr>
          <ul id="header-container" class="header-container" />
        </div>
      </div>
    </div>

    <el-dialog append-to-body title="定时发布" :close-on-click-modal="false" :visible.sync="choosePublishTimeDialogVisible" width="400px">
      <div class="publish-timer-content">
        <p class="publish-time-desc">请选择当前时间后 1小时 至 3天 进行定时发布</p>
        <div class="publish-ipt-date-box">
          <el-date-picker v-model="publishDate" :picker-options="publishDateRange" value-format="yyyy-MM-dd" type="date" placeholder="选择日期" style="width:155px;" @change="changeDatePicker" />
          <el-time-select v-model="publishTime" :picker-options="publishTimeRange" :disabled="publishTimeDisabled" value-format="HH:mm" placeholder="选择时间" style="width:155px;margin-left: 8px;" />
        </div>
        <p v-show="publishDateTimeShow" class="publish-time-txt"> 本文将于 <span class="publish-time">{{ publishDateTime }}</span> 发布 </p>
      </div>
      <div slot="footer" class="dialog-footer">
        <el-button round @click="cancelPublishByTime">取 消</el-button>
        <el-button round :disabled="!publishDateTimeShow" :loading="submitByTimeLoading" type="danger" @click="publishArticleByTiming">定时发布</el-button>
      </div>
    </el-dialog>

    <!-- 图片上传 -->
    <uploadImgDialog
      ref="uploadImgDialogRef"
      :dialog-visible="uploadImgDialogVisible"
      :max-upload-size="maxUploadSize"
      @uploadImgDone="uploadImgDone"
    />

    <!-- 内容修改记录 -->
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
import { getToken } from '@/utils/token'
import { formatMonth, formatDay } from '@/utils'
import { Editor, Toolbar } from '@wangeditor/editor-for-vue'
import { Message } from 'element-ui'
import uploadImgDialog from '@/views/admin/web/article/template/uploadImgDialog'
import contentEditRecordDrawer from '@/views/admin/web/article/template/contentEditRecordDrawer'
import NProgress from 'nprogress'
export default {
  name: 'AddArticle',
  components: {
    uploadImgDialog, contentEditRecordDrawer, Editor, Toolbar
  },
  data() {
    return {
      editor: null,
      activeEditor: true,
      html: '<p><br></p>',
      // 默认模式
      mode: 'default',
      // 简单模式，取消悬浮菜单，检查工具栏菜单
      // mode: 'simple',
      toolbarConfig: {
        modalAppendToBody: false,
        // 获取 wangeditor 默认配置
        // import { DomEditor } from '@wangeditor/editor'
        // const toolbar = DomEditor.getToolbar(this.editor)
        // const defaultConfig = toolbar.getConfig().toolbarKeys
        // console.log('defaultConfig', defaultConfig)
        toolbarKeys: [
          'undo', 'redo', '|',
          'headerSelect', 'fontSize', 'fontFamily', 'lineHeight', '|',
          'color', 'bgColor',
          {
            'key': 'group-more-style',
            'title': '其他',
            'iconSvg': '<svg viewBox=\"0 0 1024 1024\"><path d=\"M204.8 505.6m-76.8 0a76.8 76.8 0 1 0 153.6 0 76.8 76.8 0 1 0-153.6 0Z\"></path><path d=\"M505.6 505.6m-76.8 0a76.8 76.8 0 1 0 153.6 0 76.8 76.8 0 1 0-153.6 0Z\"></path><path d=\"M806.4 505.6m-76.8 0a76.8 76.8 0 1 0 153.6 0 76.8 76.8 0 1 0-153.6 0Z\"></path></svg>',
            'menuKeys': [
              'bold', 'italic', 'underline', 'through', 'clearStyle'
            ]
          }, '|',
          'bulletedList', 'numberedList', 'todo',
          {
            'key': 'group-justify',
            'title': '对齐',
            'iconSvg': '<svg viewBox=\"0 0 1024 1024\"><path d=\"M768 793.6v102.4H51.2v-102.4h716.8z m204.8-230.4v102.4H51.2v-102.4h921.6z m-204.8-230.4v102.4H51.2v-102.4h716.8zM972.8 102.4v102.4H51.2V102.4h921.6z\"></path></svg>',
            'menuKeys': [
              'justifyLeft',
              'justifyRight',
              'justifyCenter',
              'justifyJustify'
            ]
          },
          {
            'key': 'group-indent',
            'title': '缩进',
            'iconSvg': '<svg viewBox=\"0 0 1024 1024\"><path d=\"M0 64h1024v128H0z m384 192h640v128H384z m0 192h640v128H384z m0 192h640v128H384zM0 832h1024v128H0z m0-128V320l256 192z\"></path></svg>',
            'menuKeys': [
              'indent',
              'delIndent'
            ]
          },
          'divider', 'blockquote', 'codeBlock', 'insertTable', '|',
          {
            'key': 'group-image',
            'title': '图片',
            'iconSvg': '<svg viewBox=\"0 0 1024 1024\"><path d=\"M959.877 128l0.123 0.123v767.775l-0.123 0.122H64.102l-0.122-0.122V128.123l0.122-0.123h895.775zM960 64H64C28.795 64 0 92.795 0 128v768c0 35.205 28.795 64 64 64h896c35.205 0 64-28.795 64-64V128c0-35.205-28.795-64-64-64zM832 288.01c0 53.023-42.988 96.01-96.01 96.01s-96.01-42.987-96.01-96.01S682.967 192 735.99 192 832 234.988 832 288.01zM896 832H128V704l224.01-384 256 320h64l224.01-192z\"></path></svg>',
            'menuKeys': [
              'insertImage',
              'uploadImage'
            ]
          },
          {
            'key': 'group-video',
            'title': '视频',
            'iconSvg': '<svg viewBox=\"0 0 1024 1024\"><path d=\"M981.184 160.096C837.568 139.456 678.848 128 512 128S186.432 139.456 42.816 160.096C15.296 267.808 0 386.848 0 512s15.264 244.16 42.816 351.904C186.464 884.544 345.152 896 512 896s325.568-11.456 469.184-32.096C1008.704 756.192 1024 637.152 1024 512s-15.264-244.16-42.816-351.904zM384 704V320l320 192-320 192z\"></path></svg>',
            'menuKeys': [
              'insertVideo',
              'uploadVideo'
            ]
          },
          'uploadAttachment', 'insertFormula', 'insertLink', 'emotion'
        ]
      },
      editorConfig: {
        placeholder: '请输入文章内容...',
        // editor.enable() or editor.disable()
        readOnly: false,
        autoFocus: false,
        scroll: false,
        hoverbarKeys: {
          attachment: {
            menuKeys: ['downloadAttachment']
          },
          formula: {
            menuKeys: ['editFormula']
          }
        },
        // editor.getAllMenuKeys()：获取所有的菜单默认配置信息
        MENU_CONF: {
          'uploadImage': {
            server: this.$store.state.api.fileUploadApi,
            timeout: 120 * 1000,
            fieldName: 'file',
            headers: {
              Authorization: this.getUserToken()
            },
            maxNumberOfFiles: 10,
            maxFileSize: 10 * 1024 * 1024,
            // async onBeforeUpload(file) {
            //   let key = ''
            //   for (const prop in file) {
            //     key = prop
            //   }
            //
            //   if (file[key].size / 1024 < 200) {
            //     return file
            //   }
            //
            //   await imageConversion.compressAccurately(file[key].data, 200).then(res => {
            //     return res
            //   })
            // },
            // async customUpload(file, insertFn) {
            //   // insertFn(url, alt, href)
            // },
            customInsert(res, insertFn) {
              const { code, data } = res
              if (code === 200) {
                if (data && Array.isArray(data) && data.length > 0) {
                  for (let i = 0; i < data.length; i++) {
                    // 从 res 中找到 url alt href ，然后插入图片
                    const { downloadUrl, originalFilename, fileType } = data[i]
                    if (fileType === '图片') {
                      insertFn(downloadUrl, originalFilename, downloadUrl)
                    } else {
                      Message.error('只能上传图片')
                    }
                  }
                } else {
                  Message.error('图片列表为空')
                }
              } else {
                Message.error(res.message)
              }
            },
            // async customUpload(file, insertFn) {
            // },
            onError(file, err, res) {
              Message.error('图片上传失败')
              console.log('图片上传失败', err.message)
            }
          },
          'uploadVideo': {
            server: this.$store.state.api.fileUploadApi,
            timeout: 120 * 1000,
            maxFileSize: 10 * 1024 * 1024,
            maxNumberOfFiles: 1,
            fieldName: 'file',
            headers: {
              Authorization: this.getUserToken()
            },
            customInsert(res, insertFn) {
              const { code, data } = res
              if (code === 200) {
                if (data && Array.isArray(data) && data.length > 0) {
                  for (let i = 0; i < data.length; i++) {
                    // 从 res 中找到 url poster ，然后插入视频
                    const { downloadUrl, fileType } = data[i]
                    if (fileType === '视频') {
                      insertFn(downloadUrl, downloadUrl)
                    } else {
                      Message.error('只能上传视频')
                    }
                  }
                } else {
                  Message.error('视频列表为空')
                }
              } else {
                Message.error(res.message)
              }
            }
          },
          // <a data-w-e-type="attachment" data-w-e-is-void data-w-e-is-inline href="https://xxx.com/aaa/bbb/xxx.zip" download="xxx.zip">xxx.zip</a>
          'uploadAttachment': {
            server: this.$store.state.api.fileUploadApi,
            timeout: 120 * 1000,
            fieldName: 'file',
            headers: {
              Authorization: this.getUserToken()
            },
            maxFileSize: 10 * 1024 * 1024,
            customInsert(res, file, insertFn) {
              const { code, data } = res
              if (code === 200) {
                if (data && Array.isArray(data) && data.length > 0) {
                  for (let i = 0; i < data.length; i++) {
                    // 从 res 中找到 url ，然后插入附件
                    const { downloadUrl, originalFilename } = data[i]
                    insertFn(originalFilename, downloadUrl)
                  }
                } else {
                  Message.error('附件列表为空')
                }
              } else {
                Message.error(res.message)
              }
            }
          }
        }
      },
      // 编辑器字数
      totalCount: 0,
      saveDraftTime: '',
      saveDraftSpanShow: false,
      contentRecordDrawerVisible: false,
      noImg: NoImg,
      addIcon: AddIcon,
      pageLoading: false,
      // false：提示关闭
      showTipAlert: false,
      articleDefaultImg: '',
      submitLoading: false,
      submitByTimeLoading: false,
      uploadImgDialogVisible: false,
      coverImageTypeEnums: [
        { name: '单封面', value: 1 },
        // 暂不支持多封面
        { name: '多封面', value: 2, disabled: true },
        { name: '系统默认', value: 3 },
        { name: '自动生成', value: 4 },
        { name: '无封面', value: 5 }
      ],
      articleTypeEnums: [
        { name: '原创', value: 1 },
        { name: '转载', value: 2 },
        { name: '翻译', value: 3 }
      ],
      articlePermEnums: [
        { name: '全部可见', value: 1 },
        // { name: '登录可见', value: 2 },
        { name: '仅我可见', value: 3 }
      ],
      commentEnums: [
        { name: '开启评论', value: true },
        { name: '关闭评论', value: false }
      ],
      tagSearchLoading: false,
      articleTagOptions: [],
      articleTagFullOptions: [],
      articleCategoryOptions: [],
      publishDate: '',
      publishTime: '',
      publishTimeDisabled: true,
      publishDateRange: {
        disabledDate(time) {
          let curTime = new Date()
          const startTime = new Date(curTime.setHours(curTime.getHours() + 1))
          const publishDateStartRange = new Date(startTime.getFullYear(), startTime.getMonth(), startTime.getDate())
          curTime = new Date()
          const endTime = new Date(curTime.setHours(curTime.getHours() + 72))
          const publishDateEndRange = new Date(endTime.getFullYear(), endTime.getMonth(), endTime.getDate())
          return time.getTime() < publishDateStartRange.getTime() || time.getTime() > publishDateEndRange.getTime()
        }
      },
      publishTimeRange: {
        start: '00:00',
        step: '00:15',
        end: '23:45'
      },
      choosePublishTimeDialogVisible: false,
      draftTagFlag: 7,
      recentDraft: {
        id: '',
        title: ''
      },
      showRecentDraftFlag: false,
      callActivated: false,
      interval: null,
      oldContent: '',
      addForm: {
        action: '',
        id: '',
        title: '',
        content: '',
        remark: '',
        categoryId: '',
        articleType: 1,
        coverImageType: 3,
        articlePerm: 1,
        reprintLink: '',
        commentFlag: true,
        articleTag: [],
        articleImg: [],
        articleStatus: '',
        extend1: 1,
        extend2: 0,
        autoPublishTime: ''
      },
      addRules: {
        coverImageType: [
          { required: true, message: '请选择封面类型', trigger: 'blur' }
        ],
        articleType: [
          { required: true, message: '请选择文章类型', trigger: 'blur' }
        ],
        remark: [
          { required: true, message: '请填写文章摘要', trigger: 'blur' }
        ],
        articlePerm: [
          { required: true, message: '请选择文章发布范围', trigger: 'blur' }
        ],
        categoryId: [
          { required: true, message: '请选择文章分类', trigger: 'blur' }
        ],
        articleTag: [
          { required: true, message: '请选择文章标签', trigger: 'blur' }
        ],
        reprintLink: [
          { required: true, message: '请填写原文链接', trigger: 'blur' }
        ],
        commentFlag: [
          { required: true, message: '请选择评论设置', trigger: 'blur' }
        ]
      }
    }
  },
  computed: {
    maxUploadSize() {
      if (this.addForm.coverImageType === 2) {
        // 多封面最多支持 4 个封面
        return 4
      }
      return 1
    },
    draftBtnName() {
      if (this.addForm.articleStatus == null || this.addForm.articleStatus === '' || this.addForm.articleStatus === 1) {
        return '保存草稿'
      } else {
        return '移至草稿箱'
      }
    },
    autoPublishBtnShow() {
      return this.addForm.articleStatus == null || this.addForm.articleStatus === '' || this.addForm.articleStatus === 1
    },
    publishDateTime() {
      return this.publishDate + ' ' + this.publishTime
    },
    publishDateTimeShow() {
      return !(this.publishDate === '' || this.publishTime === '')
    }
  },
  watch: {
    $route: {
      handler: function(to) {
        console.log('$route:handler', to.path)
        if (to.path === '/ac/article/publish' && to.query._f != null) {
          const articleId = to.query.articleId
          if (articleId != null && articleId !== '') {
            console.log('$route:编辑草稿', articleId)
            this.clearRecentDraft()
            this.resetSaveDraftData()
            this.getArticleDetail(articleId)
            this.$router.push({ name: 'AddArticle', query: { articleId: articleId }})
          }
        }
      },
      immediate: true
    }
  },
  created() {
    console.log('created')
    this.closeTipAlert()
    this.resetSaveDraftData()
    this.getArticleTagList()
    const dataId = this.$route.query.articleId
    if (dataId != null && dataId !== '') {
      console.log('created:编辑文章', dataId)
      this.clearRecentDraft()
      this.getArticleDetail(dataId)
    } else {
      console.log('created:新增文章')
      this.queryRecentDraft()
      this.autoGenerateId()
    }

    this.$nextTick(() => {
      console.log('created:$nextTick:置位callActivated')
      this.callActivated = true
    })
  },
  mounted() {
    console.log('mounted:初始化基本数据')
    this.getDefaultArticleImg()
    this.getArticleCategoryTree()
    this.$nextTick(() => {
      console.log('mounted:$nextTick:初始化目录')
      this.initTitle()
      this.addBeforeunloadEventListener()
    })
  },
  activated() {
    console.log('activated:callActivated', this.callActivated)
    if (this.callActivated) {
      const dataId = this.$route.query.articleId
      console.log('activated:formId', this.addForm.id)
      console.log('activated:dataId', dataId)
      if (dataId != null && dataId !== '') {
        console.log('activated:编辑文章')
        this.clearRecentDraft()
        if (dataId !== this.addForm.id) {
          console.log('activated:编辑文章序号不同，重新加载文章信息')
          this.getArticleTagList()
          this.resetSaveDraftData()
          this.getArticleDetail(dataId)
        }
      } else {
        console.log('activated:新增文章')
        if (this.addForm.action !== 'add') {
          console.log('activated:原先是编辑文章或是新文章，清除文章内容，重新获取文章ID')
          this.clearData()
          this.getArticleTagList()
          this.resetSaveDraftData()
          this.queryRecentDraft()
          this.autoGenerateId()
        }
      }

      this.$nextTick(() => {
        console.log('activated:$nextTick:inner:formId', this.addForm.id)
      })
    }

    this.$nextTick(() => {
      console.log('activated:$nextTick:outer:启动文章自动保存事件')
      this.activeEditor = true
      this.openAutoSaveEvent()
    })
  },
  deactivated() {
    console.log('deactivated:关闭文章自动保存事件')
    this.activeEditor = false
    this.closeAutoSaveEvent()
    console.log('===========================>>>')
  },
  destroyed() {
    console.log('destroyed')
    this.removeBeforeunloadEventListener()
    console.log('===========================>>>')
  },
  beforeRouteEnter(to, from, next) {
    // 在渲染该组件的对应路由被 confirm 前调用
    // 不能获取组件实例 `this`
    console.log('beforeRouteEnter', from.path, ' -> ', to.path)
    next()
  },
  beforeRouteUpdate(to, from, next) {
    // 在当前路由改变，但是该组件被复用时调用
    // 举例来说，对于一个带有动态参数的路径 /foo/:id，在 /foo/1 和 /foo/2 之间跳转的时候，
    // 由于会渲染同样的 Foo 组件，因此组件实例会被复用。而这个钩子就会在这个情况下被调用。
    // 可以访问组件实例 `this`
    console.log('beforeRouteUpdate', from.path, ' -> ', to.path)
    if (to.path !== from.path || to.query._f != null) {
      next()
    } else {
      NProgress.done()
    }
  },
  beforeRouteLeave(to, from, next) {
    // 导航离开该组件的对应路由时调用
    // 可以访问组件实例 `this`
    console.log('beforeRouteLeave', from.path, ' -> ', to.path)
    this.preDoAutoSaveArticleContent()

    // this.$confirm('确认离开当前页面？').then(_ => {
    //   next()
    // }).catch(_ => {})
    next()
  },
  methods: {
    getUserToken() {
      return getToken()
    },
    addBeforeunloadEventListener() {
      window.addEventListener('beforeunload', this.beforeUnloadHandler, false)
    },
    removeBeforeunloadEventListener() {
      window.removeEventListener('beforeunload', this.beforeUnloadHandler, false)
    },
    beforeUnloadHandler(e) {
      this.preDoAutoSaveArticleContent()
      e.returnValue = ''
    },
    closeTipAlert() {
      setTimeout(() => {
        this.showTipAlert = false
      }, 10000)
    },
    resetSaveDraftData() {
      this.saveDraftTime = ''
      this.saveDraftSpanShow = false
    },
    clearRecentDraft() {
      this.recentDraft.id = ''
      this.recentDraft.title = ''
    },
    queryRecentDraft() {
      this.$mapi.article.queryLastArticleDraft().then(({ data }) => {
        this.showRecentDraftFlag = true
        this.recentDraft.id = data.id
        this.recentDraft.title = data.title
      }).catch(_ => {
        this.clearRecentDraft()
      })
    },
    autoGenerateId() {
      this.$mapi.communal.getId().then(({ data }) => {
        this.pageLoading = false
        this.addForm.action = 'add'
        this.addForm.id = data
      }).catch(_ => {
        this.addForm.id = null
      })
    },
    getArticleDetail(dataId) {
      this.pageLoading = true
      this.$mapi.article.queryArticleDetail({ articleId: dataId }).then(res => {
        const { data } = res
        if (data['articleStatus'] === 8) {
          this.$message.error('文章处于回收站中，不允许编辑，请先把文章移动到草稿箱在进行编辑')
          return
        }

        Object.keys(this.addForm).forEach(key => {
          this.addForm[key] = data[key]
        })

        this.pageLoading = false
        this.addForm.extend1 = '1'
        this.addForm.action = 'edit'
        this.oldContent = this.addForm.content
        this.editor.setHtml(this.addForm.content)
      }).catch(e => {
        this.$message.error('加载文章信息失败，请刷新浏览器再试！')
        console.log('getArticleDetail failed', e)
        Object.keys(this.addForm).forEach(key => {
          this.addForm[key] = ''
        })
      })
    },
    getDefaultArticleImg() {
      this.$mapi.article.queryArticleDefaultImg().then(res => {
        const { data } = res
        this.articleDefaultImg = data || ''
      })
    },
    getArticleCategoryTree() {
      this.$mapi.article.queryCategoryTree().then(res => {
        const { data } = res
        if (data && data instanceof Array && data.length > 0) {
          this.articleCategoryOptions = data
        } else {
          this.$message.warning('文章分类列表为空, 请先维护文章分类信息')
        }
      }).catch(_ => {
        this.articleCategoryOptions = []
      })
    },
    getArticleTagList() {
      this.$mapi.article.queryTagList().then(res => {
        this.articleTagOptions = res.data
        this.articleTagFullOptions = res.data
      }).catch(_ => {
        this.articleTagOptions = []
        this.articleTagFullOptions = []
      })
    },
    initTitle() {
      const headerContainer = document.getElementById('header-container')
      headerContainer.addEventListener('mousedown', event => {
        if (event.target.tagName !== 'LI') return
        event.preventDefault()
        const id = event.target.id

        // 由于目前编辑器设置的不支持滚动，所以点击标题调转不会生效，后续想办法处理，by SunTao AT 2022-12-31
        this.editor.scrollToElem(id)
      }, { passive: false })
    },
    openAutoSaveEvent() {
      // 自动保存一次文章内容（如果内容存在变化）
      this.interval = window.setInterval(() => {
        this.preDoAutoSaveArticleContent()
      }, 60000)
    },
    closeAutoSaveEvent() {
      if (this.interval != null) {
        clearInterval(this.interval)
        this.oldContent = ''
        this.interval = null
      }
    },
    preDoAutoSaveArticleContent() {
      console.log('event:触发文章自动保存事件')
      if (this.oldContent === '' && this.totalCount !== 0) {
        this.oldContent = this.addForm.content
      }

      if (this.oldContent !== this.addForm.content) {
        // 内容存在变化，则保存操作
        this.doAutoSaveArticleContent()
      } else {
        console.log('event:文章内容无变化')
      }
    },
    doAutoSaveArticleContent() {
      const param = {
        id: this.addForm.id,
        content: this.addForm.content,
        remark: this.addForm.remark,
        extend2: this.totalCount
      }
      this.$mapi.article.autoSaveArticleContent(param).then(({ data }) => {
        if (data != null && data !== '') {
          // this.$message.success('文章内容于 ' + data + ' 自动保存到临时库中')
          this.oldContent = param.content
          console.log('event:成功触发文章自动保存事件')
        } else {
          console.log('event:触发文章自动保存事件失败')
        }
      })
    },
    clearData() {
      this.addForm = {
        action: '',
        id: '',
        title: '',
        content: '',
        remark: '',
        categoryId: '',
        articleType: 1,
        coverImageType: 3,
        articlePerm: 1,
        reprintLink: '',
        commentFlag: true,
        articleTag: [],
        articleImg: [],
        articleStatus: '',
        extend1: 1,
        extend2: 0,
        autoPublishTime: ''
      }
      this.html = '<p><br></p>'
      this.$refs['addForm'].resetFields()
    },
    customTagSearchWithoutCase(query) {
      if (query !== '') {
        this.tagSearchLoading = true
        setTimeout(() => {
          this.tagSearchLoading = false
          this.articleTagOptions = this.articleTagFullOptions.filter(item => {
            return item.name.toLowerCase().indexOf(query.toLowerCase()) > -1
          })
        }, 200)
      } else {
        this.articleTagOptions = this.articleTagFullOptions
      }
    },
    autoSetArticleRemark() {
      const content = this.editor.getText()
      const contentNoSpace = content.replace(/\s+/g, '')
      this.addForm.remark = contentNoSpace.substring(0, 250)
      this.$refs.addForm.validateField('remark')
    },
    openDraftPage(articleId) {
      console.log('编辑草稿', articleId)
      this.$router.push({ name: 'AddArticle', query: { articleId: articleId, _f: '' }})
    },
    openMoreDraftPage(draftFlag) {
      this.$router.push({ name: 'Article', query: { _ts: draftFlag }})
    },
    closeDraftPage() {
      this.showRecentDraftFlag = false
    },
    changeDatePicker(date) {
      if (date == null || date === '') {
        this.publishTime = ''
        this.publishTimeDisabled = true
      } else {
        let curTime = new Date()
        const startTime = new Date(curTime.setHours(curTime.getHours() + 1))
        const startDate = startTime.getFullYear() + '-' + formatMonth(startTime) + '-' + formatDay(startTime)
        curTime = new Date()
        const endTime = new Date(curTime.setHours(curTime.getHours() + 72))
        const endDate = endTime.getFullYear() + '-' + formatMonth(endTime) + '-' + formatDay(endTime)

        // 设定时间选择范围
        if (startDate === date) {
          this.publishTimeRange.start = this.calculateTime(true, startTime.getHours(), startTime.getMinutes())
          this.publishTimeRange.end = '23:45'
        } else if (endDate === date) {
          this.publishTimeRange.start = '00:00'
          this.publishTimeRange.end = this.calculateTime(false, endTime.getHours(), endTime.getMinutes())
        } else {
          this.publishTimeRange.start = '00:00'
          this.publishTimeRange.end = '23:45'
        }

        this.publishTimeDisabled = false
      }
    },
    calculateTime(isStart, hour, minute) {
      if (isStart) {
        if (minute >= 0 && minute < 15) {
          return hour + ':' + '15'
        } else if (minute >= 15 && minute < 30) {
          return hour + ':' + '30'
        } else if (minute >= 30 && minute < 45) {
          return hour + ':' + '45'
        } else {
          if (hour !== 23) {
            hour++
          }
          return hour + ':' + '00'
        }
      } else {
        if (minute >= 0 && minute < 15) {
          return hour + ':' + '00'
        } else if (minute >= 15 && minute < 30) {
          return hour + ':' + '15'
        } else if (minute >= 30 && minute < 45) {
          return hour + ':' + '30'
        } else {
          return hour + ':' + '45'
        }
      }
    },
    showContentRecord() {
      this.contentRecordDrawerVisible = true
      this.$refs.contentEditRecordDrawerRef.initData(this.addForm.id)
    },
    regainArticleContent(recordId) {
      this.contentRecordDrawerVisible = false
      if (recordId != null && recordId !== '') {
        this.doAutoSaveArticleContent()
        this.$mapi.article.queryAutoSaveArticleContentById({ recordId: recordId }).then(({ data }) => {
          if (data != null) {
            const { articleId, content, remark } = data
            if (this.addForm.id === articleId) {
              this.$message.success('内容恢复成功')
              this.addForm.remark = remark
              this.addForm.content = content
              this.oldContent = this.addForm.content
              this.editor.setHtml(this.addForm.content)
              return
            }
          }

          this.$message.error('内容恢复失败')
        })
      }
    },
    // 保存草稿
    saveDraft() {
      this.addForm.extend2 = this.totalCount
      this.addForm.action = this.addForm.articleStatus === 1 ? 'edit' : this.addForm.action
      this.$mapi.article.saveArticleDraft(this.addForm).then(({ data, message }) => {
        this.$message.success(message)
        // 状态标志为草稿
        this.addForm.articleStatus = 1
        this.saveDraftTime = data
        this.saveDraftSpanShow = true
      }).catch(_ => {
        this.saveDraftTime = ''
        this.saveDraftSpanShow = false
      })
    },
    // 定时发布
    openPublishArticleByTimingDialog() {
      if (this.addForm.title === '') {
        this.$message.warning('请填写文章标题')
        return
      }

      if (this.editor.isEmpty()) {
        this.$message.warning('请填写文章内容')
        return
      }

      this.addForm.extend2 = this.totalCount
      this.$refs['addForm'].validate((valid) => {
        if (valid) {
          this.openPublishTimeDialog()
        } else {
          return false
        }
      })
    },
    openPublishTimeDialog() {
      // 打开时间模态框
      this.submitByTimeLoading = false
      this.choosePublishTimeDialogVisible = true
    },
    cancelPublishByTime() {
      this.publishDate = ''
      this.publishTime = ''
      this.publishTimeDisabled = true
      this.choosePublishTimeDialogVisible = false
    },
    publishArticleByTiming() {
      console.log('this.publishDate', this.publishDate)
      console.log('this.publishTime', this.publishTime)
      if (this.publishDate === '' || this.publishTime === '') {
        this.$message.warning('请选择发布时间')
        return
      }

      if (this.addForm.articlePerm === 3) {
        this.$message.error('私密文章暂不支持定时发布')
        return
      }

      // 设置自动发布时间
      this.addForm.autoPublishTime = this.publishDate + ' ' + this.publishTime

      // 发布文章
      this.submitByTimeLoading = true
      this.$mapi.article.publishArticleByTiming(this.addForm).then(_ => {
        this.cancelPublishByTime()
        this.publishSuccess()
      }).finally(_ => {
        this.submitByTimeLoading = false
      })
    },
    // 发布文章
    publishArticle() {
      if (this.addForm.title === '') {
        this.$message.warning('请填写文章标题')
        return
      }

      if (this.editor.isEmpty()) {
        this.$message.warning('请填写文章内容')
        return
      }

      this.addForm.extend2 = this.totalCount
      this.addForm.action = this.addForm.articleStatus === 1 ? 'edit' : this.addForm.action
      this.$refs['addForm'].validate((valid) => {
        if (valid) {
          this.submitLoading = true
          if (this.addForm.action === 'add') {
            // add
            this.$mapi.article.addArticle(this.addForm).then(_ => {
              this.publishSuccess()
            }).finally(_ => {
              this.submitLoading = false
            })
          } else if (this.addForm.action === 'edit') {
            // edit
            this.$mapi.article.editArticle(this.addForm).then(_ => {
              this.publishSuccess()
            }).finally(_ => {
              this.submitLoading = false
            })
          } else {
            this.$message.error('系统异常，请刷新页面重试')
            this.submitLoading = false
          }
        } else {
          return false
        }
      })
    },
    publishSuccess() {
      this.$router.push('/publish/blog/success/' + this.addForm.id)
      this.clearData()
    },
    showCoverImgIcon(index) {
      const closeIconCss = document.getElementsByClassName('cover-img-close-icon')
      closeIconCss[index].style.display = 'block'
      const replaceIconCss = document.getElementsByClassName('cover-img-replace-icon')
      replaceIconCss[index].style.display = 'block'
    },
    hideCoverImgIcon(index) {
      const closeIconCss = document.getElementsByClassName('cover-img-close-icon')
      closeIconCss[index].style.display = 'none'
      const replaceIconCss = document.getElementsByClassName('cover-img-replace-icon')
      replaceIconCss[index].style.display = 'none'
    },
    removeCoverImg(file, index) {
      this.$confirm(`确定移除 ${file['originalFilename']} ？`).then(() => {
        const param = {
          fileId: file.id
        }
        this.$mapi.file.deleteFile(param).then(res => {
          this.addForm.articleImg.splice(index, 1)
          this.$message.success(res.message)
        })
      }).catch(() => { })
    },
    uploadCoverImg() {
      this.uploadImgDialogVisible = true
      this.$refs.uploadImgDialogRef.initData()
    },
    replaceCoverImg(file, index) {
      this.uploadImgDialogVisible = true
      this.$refs.uploadImgDialogRef.initData(true, index)
    },
    uploadImgDone(fileList, index) {
      this.uploadImgDialogVisible = false

      // set data
      fileList = fileList || []
      if (index != null && index >= 0 && fileList.length === 1 && this.addForm.articleImg.length >= index + 1) {
        this.addForm.articleImg[index] = fileList[0]
      } else {
        for (let i = 0; i < fileList.length; i++) {
          this.addForm.articleImg.push(fileList[i])
        }
      }

      if (this.addForm.articleImg.length > this.maxUploadSize) {
        this.addForm.articleImg = this.addForm.articleImg.splice(this.addForm.articleImg.length - this.maxUploadSize)
      }
    },
    setArticleHeader(editor) {
      const headerContainer = document.getElementById('header-container')
      const headers = editor.getElemsByTypePrefix('header')
      if (headerContainer != null && headers != null && headers.length > 0) {
        headerContainer.innerHTML = headers.map(header => {
          const { id, type } = header
          const text = header.children && header.children.length > 0 ? header.children[0].text : '【未知】'
          return `<li id="${id}" type="${type}">${text}</li>`
        }).join('')
      }
    },
    // Editor
    onCreated(editor) {
      this.editor = Object.seal(editor)
    },
    onChange(editor) {
      // editor.getHtml() 获取 HTML 内容
      // editor.getText() 获取 TEXT 内容
      // editor.children  获取 JSON 内容
      const contentNoSpace = editor.getText().replace(/[\n\r]/mg, '')
      this.totalCount = contentNoSpace.length
      this.addForm.content = editor.getHtml()

      // 设置摘要
      // this.addForm.remark = contentNoSpace.substring(0, 250)

      // 设置标题
      this.setArticleHeader(editor)
    },
    onDestroyed(editor) {
    },
    onMaxLength(editor) {
    },
    onFocus(editor) {
      // 实施获取最新的Token，防止使用后台弃用的Token进行访问
      const token = this.getUserToken()
      if (token == null || token === '') {
        this.$message.warning('令牌信息不存在，请手动保存文章信息后刷新页面重试')
      }
      this.editorConfig.MENU_CONF.uploadImage.headers.Authorization = token
      this.editorConfig.MENU_CONF.uploadVideo.headers.Authorization = token
      this.editorConfig.MENU_CONF.uploadAttachment.headers.Authorization = token
    },
    onBlur(editor) {
    },
    // customPaste(editor, event, callback) {
    //   console.log('ClipboardEvent 粘贴事件对象', event)
    //   const html = event.clipboardData.getData('text/html') // 获取粘贴的 html
    //   const text = event.clipboardData.getData('text/plain') // 获取粘贴的纯文本
    //   const rtf = event.clipboardData.getData('text/rtf') // 获取 rtf 数据（如从 word wsp 复制粘贴）
    //
    //   // 自定义插入内容
    //   editor.insertText(html)
    //
    //   // 返回 false ，阻止默认粘贴行为
    //   event.preventDefault()
    //   callback(false) // 返回值（注意，vue 事件的返回值，不能用 return）
    //
    //   // 返回 true ，继续默认的粘贴行为
    //   // callback(true)
    // },
    customAlert(message, type) {
      switch (type) {
        case 'success':
          this.$message.success(message)
          break
        case 'info':
          this.$message.info(message)
          break
        case 'warning':
          this.$message.warning(message)
          break
        case 'error':
          this.$message.error(message)
          break
        default:
          this.$message.info(message)
      }
    }
  }
}
</script>

<style src='@wangeditor/editor/dist/css/style.css'></style>

<style scoped>
.cover-img-box-flex {
  width: 100%;
  display: flex;
  box-sizing: border-box;
}
.cover-img-box-1 {
  padding: 0;
  margin: 5px 0 0 0;
}
.cover-img-box-more {
  padding: 0;
  margin: 5px 0 0 0;
  flex-wrap: wrap;
}
.cover-img-box {
  padding: 0;
  margin: 0;
}
.cover-img {
  position: relative;
  width: 160px;
  height: 90px;
  display: inline-block;
  border: none;
  vertical-align: top;
  text-align: center;
  line-height: 105px;
  background-color: #f5f5f5;
  border-radius: 4px;
  margin-right: 24px;
  margin-bottom: 10px;
}
.cover_img_hover {
  width: 160px;
  height: 90px;
  position: absolute;
  left: 0;
  top: 0;
  cursor: pointer;
  background: #fff;
  border: 1px dashed #bfbfbf;
  border-radius: 4px;
}
.cover_img_over {
  position: absolute;
  width: 160px;
  height: 90px;
  z-index: 1;
  left: 0;
  top: 0;
  border-radius: 4px;
}
.cover-img-add-icon {
  color: #999;
  font-size: 24px;
  position: absolute;
  top: 50%;
  left: 50%;
  -webkit-transform: translate(-50%, -50%);
  transform: translate(-50%, -50%);
}
.cover-img-img-icon {
  width: 100%;
  height: 100%;
  border-radius: 4px;
  border-style: none;
}
.cover-img-close-icon {
  position: absolute;
  background: #999;
  color: #fff;
  line-height: 20px;
  right: -8px;
  top: -8px;
  display: none;
  width: 20px;
  height: 20px;
  font-size: 14px;
  text-align: center;
  background-size: contain;
  border-radius: 50%;
  cursor: pointer;
}
.cover-img-replace-icon {
  position: absolute;
  display: none;
  bottom: 12px;
  height: 32px;
  width: 56px;
  background-color: rgba(0,0,0,.4);
  font-size: 14px;
  line-height: 20px;
  padding: 6px 0;
  left: 50%;
  margin-left: -28px;
  border-radius: 4px;
  text-align: center;
  color: #fff;
  cursor: pointer;
}

.editor-container {
  background-color: #fff;
  height: 100%;
  overflow: hidden;
  color: #333;
}
.editor-toolbar {
  width: 1350px;
  min-width: 1350px;
  margin: 0 auto;
  background-color: #FCFCFC;
  border-bottom: 1px solid #e8e8e8;
}
.editor-content {
  display: flex;
  height: calc(100% - 140px);
  background-color: rgb(245, 245, 245);
  overflow-y: auto;
  position: relative;
}
.editor-content-right {
  flex: 1;
  width: calc(100vw - 350px);
}
.editor-content-left {
  width: 350px;
}
.editor-header-content {
  width: 250px;
  margin: 30px auto 150px 0;
  background-color: #fff;
  border: 1px solid #e8e8e8;
  box-shadow: 0 2px 10px rgb(0 0 70);
}
.editor-content-inner {
  width: 850px;
  margin: 30px auto 150px auto;
  background-color: #fff;
  padding: 20px 50px 50px 50px;
  border: 1px solid #e8e8e8;
  box-shadow: 0 2px 10px rgb(0 0 70);
}
.editor-content-core {
  margin-bottom: 0;
}
.editor-content-config-opt {
  width: 100%;
  height: 64px;
  position: relative;
}
.editor-content-config-opt-left {
  width: 35%;
  position: absolute;
  left: 0;
  bottom: 0;
  text-align: left;
}
.editor-content-config-opt-left span {
  color: #555666;
  font-size: 14px;
  padding-left: 5px;
  margin-right: 10px;
  line-height: 24px;
}
.editor-content-config-opt-left .save-draft-span {
  background-image: url(../../../../assets/images/icon-autosave.png);
  background-size: 16px 16px;
  background-repeat: no-repeat;
  background-position: 0;
  padding-left: 20px;
}
.editor-content-config-opt-right {
  width: 65%;
  position: absolute;
  right: 0;
  bottom: 0;
  text-align: right;
}
.editor-title {
  padding: 20px 0;
  border-bottom: 1px solid #e8e8e8;
}
.editor-title input {
  font-size: 23px;
  font-weight: 600;
  color: #606266;
  border: 0;
  outline: none;
  width: 100%;
  line-height: 1;
  /* word-break: break-all;
  white-space: pre-wrap; */
}
.editor-text-area {
  min-height: 700px;
  margin-top: 20px;
}

.publish-timer-content {
  position: relative;
}
.publish-time-desc {
  position: absolute;
  top: -12px;
  -webkit-transform: translateY(-100%);
  transform: translateY(-100%);
  color: #222226;
}
.publish-ipt-date-box {
  padding: 10px 0;
}
.publish-time-txt {
  position: absolute;
  bottom: -12px;
  -webkit-transform: translateY(100%);
  transform: translateY(100%);
  color: #999aaa;
}
.publish-time {
  color: #222226;
}
.set-remark-btn {
  position: absolute;
  padding: 0 10px!important;
  right: 8px;
  bottom: 8px;
  line-height: 18px;
  min-height: unset!important;
  outline: none;
  cursor: pointer;
}
.recent-draft-box {
  margin-bottom: 20px;
  display: -webkit-box;
  /* display: -ms-flexbox; */
  display: flex;
  padding: 16px 24px;
  line-height: 14px;
  background-color: #f5f6f7;
  -webkit-box-align: center;
  /* -ms-flex-align: center; */
  align-items: center;
}
.recent-draft-box .icon-type {
  white-space: nowrap;
  padding: 2px 6px;
  display: block;
  font-size: 14px;
  line-height: 1;
  background-color: #fff;
  color: #555666;
  border: 1px solid #ced0e2;
}
.recent-draft-box .draft-title {
  white-space: nowrap;
  margin-left: 16px;
  width: 100%;
  overflow: hidden;
  text-overflow: ellipsis;
  font-size: 16px;
  font-weight: 500;
  cursor: pointer;
}
.draft-title .draft-title-link {
  white-space: nowrap;
  color: #222226;
  text-decoration: none;
  cursor: pointer;
}
.recent-draft-box .draft-btn-edit {
  margin-left: 24px;
  padding: 2px 6px;
  display: block;
  font-size: 14px;
  line-height: 1;
  background-color: #fff;
  color: #555666;
  border: 1px solid #ced0e2;
  white-space: nowrap;
  text-decoration: none;
  cursor: pointer;
}
.recent-draft-box .draft-btn-more {
  margin-left: 24px;
  display: block;
  color: #555666;
  white-space: nowrap;
  text-decoration: none;
  cursor: pointer;
}
.recent-draft-box .draft-btn-close {
  margin-left: 24px;
  height: 12px;
  width: 12px;
  font-size: 12px;
  border: none;
  cursor: pointer;
  background-color: transparent;
  white-space: nowrap;
  -webkit-appearance: button;
  text-transform: none;
  overflow: visible;
  line-height: 1.15;
}
.recent-draft-box .draft-btn-close svg {
  display: block;
  height: 12px;
  width: 12px;
  fill: #595959;
}

::v-deep .header-container {
  list-style-type: none;
  padding: 0 20px 20px 20px;
  background-color: #fff;
}
::v-deep .header-container li {
  color: #333;
  margin: 10px 0;
  cursor: pointer;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}
::v-deep .header-container li:hover {
  text-decoration: underline;
}
::v-deep .header-container li[type="header1"] {
  font-size: 20px;
  font-weight: bold;
}
::v-deep .header-container li[type="header2"] {
  font-size: 16px;
  padding-left: 15px;
  font-weight: bold;
}
::v-deep .header-container li[type="header3"] {
  font-size: 14px;
  padding-left: 30px;
}
::v-deep .header-container li[type="header4"] {
  font-size: 12px;
  padding-left: 45px;
}
::v-deep .header-container li[type="header5"] {
  font-size: 12px;
  padding-left: 60px;
}
</style>
