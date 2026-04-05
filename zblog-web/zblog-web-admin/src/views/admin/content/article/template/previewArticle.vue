<template>
  <div v-loading="pageLoading" :element-loading-text="pageLoadingText" class="editor-container">
    <div class="editor-content">
      <div class="editor-content-right">
        <div class="editor-content-inner editor-content-core">
          <div v-show="addForm.title != null && addForm.title !== ''" class="editor-title">
            <input v-model="addForm.title" placeholder="请输入文章标题（最多 100 个字符）" maxlength="100">
          </div>
          <div class="editor-text-area">
            <Editor
              v-model="html"
              :mode="mode"
              :default-config="editorConfig"
              @onCreated="onCreated"
              @onChange="onChange"
            />
          </div>
        </div>
        <div class="editor-content-inner editor-content-config">
          <el-form ref="addForm" :model="addForm" label-width="100px">
            <el-form-item label="文章摘要：" prop="remark">
              <el-input v-model="addForm.remark" type="textarea" resize="none" :autosize="{ minRows: 6, maxRows: 6 }" maxlength="250" />
            </el-form-item>
          </el-form>
          <div class="editor-content-config-opt">
            <div class="editor-content-config-opt-left">
              <span>共 {{ totalCount }} 字</span>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import { Editor } from '@wangeditor/editor-for-vue'
export default {
  components: {
    Editor
  },
  data() {
    return {
      editor: null,
      mode: 'default',
      html: '<p><br></p>',
      totalCount: 0,
      pageLoading: false,
      pageLoadingText: '数据加载中...',
      editorConfig: {
        placeholder: '',
        readOnly: true,
        autoFocus: false,
        scroll: false,
        hoverbarKeys: {
          attachment: {
            menuKeys: ['downloadAttachment']
          },
          formula: {
            menuKeys: ['editFormula']
          }
        }
      },
      addForm: {
        title: '',
        remark: '',
        content: ''
      }
    }
  },
  methods: {
    clearData() {
      this.pageLoading = false
      this.pageLoadingText = '数据加载中...'

      Object.keys(this.addForm).forEach(key => {
        this.addForm[key] = ''
      })

      this.$nextTick(() => {
        if (this.editor != null) {
          this.editor.setHtml('<p><br></p>')
        }
      })
    },
    initData(type, dataId) {
      this.clearData()
      if (type === 1) {
        this.queryArticleTmpContentInfo(dataId)
      }

      if (type === 2) {
        this.queryArticleDetail(dataId)
      }
    },
    queryArticleTmpContentInfo(dataId) {
      this.pageLoading = true
      if (dataId != null && dataId !== '') {
        this.$mapi.article.queryAutoSaveArticleContentById({ recordId: dataId }).then(({ data }) => {
          if (data != null) {
            const { content, remark } = data
            this.addForm.title = ''
            this.addForm.remark = remark
            this.addForm.content = content
            this.editor.setHtml(this.addForm.content)
            this.pageLoading = false
          } else {
            this.pageLoadingText = '数据加载失败...'
          }
        }).catch(_ => {
          this.pageLoadingText = '数据加载失败...'
        })
      }
    },
    queryArticleDetail(dataId) {
      this.pageLoading = true
      if (dataId != null && dataId !== '') {
        this.$mapi.article.queryArticleDetail({ articleId: dataId }).then(res => {
          const { data } = res
          Object.keys(this.addForm).forEach(key => {
            this.addForm[key] = data[key]
          })

          this.editor.setHtml(this.addForm.content)
          this.pageLoading = false
        }).catch(_ => {
          this.pageLoadingText = '数据加载失败...'
        })
      }
    },
    // Editor
    onCreated(editor) {
      this.editor = Object.seal(editor)
    },
    onChange(editor) {
      const contentNoSpace = editor.getText().replace(/[\n\r]/mg, '')
      this.totalCount = contentNoSpace.length
      this.addForm.content = editor.getHtml()
    }
  }
}
</script>

<style src='@wangeditor/editor/dist/css/style.css'></style>

<style scoped>
.editor-container {
  background-color: #fff;
  height: 100%;
  overflow: hidden;
  color: #333;
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
.editor-content-inner {
  width: 850px;
  margin: 30px auto 150px auto;
  background-color: #fff;
  padding: 20px 50px 50px 50px;
  border: 1px solid #e8e8e8;
  box-shadow: 0 2px 10px rgb(0 0 70);
}
.editor-content-core {
  margin-top: 5px;
  margin-bottom: 0;
}
.editor-content-config {
  margin: 15px auto;
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
</style>
