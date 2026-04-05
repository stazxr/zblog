<template>
  <div class="app-container">
    <el-drawer :title="dialogTitle" :visible.sync="dialogVisible" destroy-on-close :before-close="handleClose" size="60%" @closed="doClose">
      <div class="demo-drawer__content">
        <div class="demo-drawer__operate">
          <el-button size="small" type="primary" @click="addArticle">
            添加文章
          </el-button>
          <el-button size="small" type="danger" @click="deleteArticle">
            批量删除
          </el-button>
          <el-button size="small" type="warning" @click="cancel">
            直接退出
          </el-button>
          <el-button size="small" :loading="submitLoading" type="success" @click="submit">
            保存退出
          </el-button>
        </div>
        <el-table v-loading="tableLoading" :data="tableData" border :cell-class-name="tableCellClassName" @selection-change="handleSelectionChange" @row-dblclick="tableRowClick">
          <el-table-column type="selection" width="55" />
          <el-table-column :show-overflow-tooltip="true" prop="articleTitle" label="文章标题" align="left">
            <template v-slot="scope">
              <el-input
                v-if="scope.row.index + ',' + scope.column.index === currentCell"
                :ref="scope.row.index + ',' + scope.column.index"
                v-model="scope.row.articleTitle"
                size="mini"
                maxlength="100"
                show-word-limit
                @blur="hideInput"
              />
              <span v-else>
                <a style="color: blue;" @click="previewArticle(scope.row.articleId)">
                  【预览】
                </a>
                {{ scope.row.articleTitle }}
              </span>
            </template>
          </el-table-column>
          <el-table-column :show-overflow-tooltip="true" prop="sort" label="文章排序" sortable align="center" width="200">
            <template v-slot="scope">
              <el-input-number
                v-if="scope.row.index + ',' + scope.column.index === currentCell"
                :ref="scope.row.index + ',' + scope.column.index"
                v-model.number="scope.row.sort"
                :min="0"
                :max="99999"
                :controls="false"
                step-strictly
                size="small"
                @blur="hideInput"
              />
              <span v-else>{{ scope.row.sort }}</span>
            </template>
          </el-table-column>
          <el-table-column :show-overflow-tooltip="true" prop="articleStatus" label="文章状态" align="center" width="200" />
        </el-table>
      </div>
    </el-drawer>

    <!-- 新增文章模态框 -->
    <el-dialog :visible.sync="addVisible" width="40%" @open="initArticleData">
      <div slot="title" class="dialog-title-container">
        <i class="el-icon-warning" style=" color:#ff9900" /> 仅允许搜索已发布且公开状态的文章
      </div>
      <div>
        <el-select v-model="chooseArticleList" multiple filterable remote :reserve-keyword="false" placeholder="请输入文章标题进行搜索" :remote-method="remoteMethod" :loading="articleSearchLoading" style="width: 100%">
          <el-option v-for="item in articleList" :key="item.id" :label="item.title" :value="item.id + ',' + item.title" />
        </el-select>
      </div>
      <div slot="footer">
        <el-button @click="addVisible = false">取 消</el-button>
        <el-button :loading="addLoading" type="primary" @click="doAddArticle">确 定</el-button>
      </div>
    </el-dialog>

    <!-- 批量删除模态框 -->
    <el-dialog :visible.sync="deleteVisible" width="30%">
      <div slot="title" class="dialog-title-container">
        <i class="el-icon-warning" style=" color:#ff9900" /> 提示
      </div>
      <div style="font-size:1rem">是否删除选中的列表？</div>
      <div slot="footer">
        <el-button @click="deleteVisible = false">取 消</el-button>
        <el-button :loading="deleteLoading" type="primary" @click="doDeleteArticle">确 定</el-button>
      </div>
    </el-dialog>

    <!-- 文章预览模态框 -->
    <el-dialog :visible.sync="previewArticleDialogVisible" title="预览" width="900px">
      <previewArticle ref="previewArticleRef" />
      <div slot="footer" class="dialog-footer">
        <el-button type="default" @click="closePreviewArticleDialog">关 闭</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import previewArticle from '@/views/admin/web/article/template/previewArticle'
export default {
  components: {
    previewArticle
  },
  props: {
    dialogVisible: {
      type: Boolean,
      default: false
    },
    dialogTitle: {
      type: String,
      default: ''
    }
  },
  data() {
    return {
      columnId: '',
      tableTitle: '',
      tableData: [],
      selectRows: [],
      currentCell: null,
      tableLoading: false,
      submitLoading: false,
      addLoading: false,
      addVisible: false,
      deleteLoading: false,
      deleteVisible: false,
      previewArticleDialogVisible: false,
      articleList: [],
      chooseArticleList: [],
      articleSearchLoading: false
    }
  },
  methods: {
    initData(dataId) {
      this.columnId = dataId
      this.queryColumnDetail()
    },
    queryColumnDetail() {
      this.tableLoading = true
      this.$mapi.column.queryColumnDetail({ columnId: this.columnId }).then(({ data }) => {
        this.tableTitle = data['name']
        this.tableData = data['articles']
      }).catch(_ => {
        this.tableTitle = ''
        this.tableData = []
      }).finally(() => {
        this.tableLoading = false
      })
    },
    handleSelectionChange(val) {
      this.selectRows = val
    },
    tableCellClassName({ row, column, rowIndex, columnIndex }) {
      row.index = rowIndex
      column.index = columnIndex
    },
    hideInput() {
      this.currentCell = null
    },
    tableRowClick(row, column) {
      this.currentCell = row.index + ',' + column.index
      setTimeout(() => {
        if (this.$refs[row.index + ',' + column.index]) {
          this.$refs[row.index + ',' + column.index].focus()
        }
      })
    },
    initArticleData() {
      this.chooseArticleList = []
      this.queryArticleList()
    },
    remoteMethod(query) {
      this.queryArticleList(query)
    },
    queryArticleList(queryString) {
      const articleIds = []
      if (this.tableData.length > 0) {
        for (let i = 0; i < this.tableData.length; i++) {
          articleIds.push(this.tableData[i].articleId)
        }
      }

      this.articleSearchLoading = true
      const param = { 'articleTitle': queryString, 'articleIds': articleIds }
      this.$mapi.column.queryArticleListNotColumn(param).then(({ data }) => {
        this.articleList = data
      }).catch(_ => {
        this.articleList = []
      }).finally(_ => {
        this.articleSearchLoading = false
      })
    },
    addArticle() {
      this.addVisible = true
    },
    doAddArticle() {
      if (this.chooseArticleList.length === 0) {
        this.$message.warning('请选择需要添加的文章')
        return
      }

      for (let i = 0; i < this.chooseArticleList.length; i++) {
        const item = this.chooseArticleList[i]
        if (item.indexOf(',') !== -1) {
          const index = item.indexOf(',')
          const articleId = item.substring(0, index)
          const articleTitle = item.substring(index + 1)
          this.tableData.push({ articleId: articleId, articleTitle: articleTitle, sort: 1, articleStatus: '' })
        }
      }

      this.addVisible = false
    },
    deleteArticle() {
      if (this.selectRows.length === 0) {
        this.$message.warning('请选择需要删除的文章')
        return
      }

      this.deleteVisible = true
    },
    doDeleteArticle() {
      try {
        this.deleteLoading = true
        const selectIds = []
        for (let i = 0; i < this.selectRows.length; i++) {
          selectIds.push(this.selectRows[i].articleId)
        }

        const newData = []
        for (let i = 0; i < this.tableData.length; i++) {
          if (!selectIds.includes(this.tableData[i].articleId)) {
            newData.push(this.tableData[i])
          }
        }

        this.tableData = newData
        this.deleteVisible = false
      } catch (e) {
        console.log('delete article error', e)
        this.$message.error('删除失败')
      } finally {
        this.deleteLoading = false
      }
    },
    previewArticle(articleId) {
      this.previewArticleDialogVisible = true
      this.$nextTick(() => {
        this.$refs.previewArticleRef.initData(2, articleId)
      })
    },
    closePreviewArticleDialog() {
      this.previewArticleDialogVisible = false
    },
    doClose() {
      this.submitLoading = false
      this.$emit('configDone')
    },
    handleClose() {
      if (!this.submitLoading) {
        this.$confirm('没有保存，确认直接退出吗？').then(_ => {
          this.doClose()
        }).catch(_ => {})
      }
    },
    cancel() {
      this.handleClose()
    },
    submit() {
      this.$confirm('确定要提交修改吗？').then(_ => {
        const param = {
          id: this.columnId,
          articleList: this.tableData
        }
        this.submitLoading = true
        this.$mapi.column.configColumn(param).then(res => {
          this.$message.success(res.message)
          this.doClose()
        }).finally(_ => {
          this.submitLoading = false
        })
      })
    }
  }
}
</script>

<style scoped>
.demo-drawer__content {
  margin: 5px 10px;
  flex-direction: column;
  height: 100%;
}
.demo-drawer__operate {
  margin-bottom: 10px;
}
.demo-drawer__footer {
  margin-top: 10px;
  display: flex;
}
.demo-drawer__footer button {
  flex: 2;
  margin: 5px 20px;
}
</style>
