<template>
  <div>
    <el-drawer :visible.sync="dialogVisible" title="内容修改记录（系统每60秒自动保存）" destroy-on-close :before-close="handleClose" size="50%" @closed="doClose">
      <div class="demo-drawer__content">
        <el-table v-loading="tableLoading" :data="tableData" border style="width: 90%;margin: 0 5%">
          <el-table-column :show-overflow-tooltip="true" prop="id" label="记录编码" align="center" />
          <el-table-column :show-overflow-tooltip="true" prop="count" label="字数" align="center" />
          <el-table-column :show-overflow-tooltip="true" prop="saveTime" label="保存时间" align="center" />
          <el-table-column label="操作" align="center" width="150px">
            <template slot-scope="scope">
              <el-button-group>
                <el-button type="info" size="mini" @click="previewContent(scope.row.id)">预览</el-button>
                <el-button type="warning" size="mini" @click="regainContent(scope.row.id)">恢复</el-button>
              </el-button-group>
            </template>
          </el-table-column>
          <div slot="empty">
            <el-empty />
          </div>
        </el-table>

        <div style="width: 90%;margin: 0 5%">
          <el-pagination
            :total="total"
            :current-page="page"
            :hide-on-single-page="true"
            style="margin-top: 8px;"
            layout="total, prev, pager, next, sizes"
            @size-change="sizeChange"
            @current-change="pageChange"
          />
        </div>

        <div class="demo-drawer__footer">
          <el-button @click="cancel">取 消</el-button>
        </div>
      </div>
    </el-drawer>

    <el-dialog :visible.sync="previewArticleDialogVisible" title="预览" width="900px">
      <previewArticle ref="previewArticleRef" />
      <div slot="footer" class="dialog-footer">
        <el-button type="warning" @click="regainContentByPreview">恢 复</el-button>
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
    }
  },
  data() {
    return {
      articleId: '',
      tableData: [],
      tableLoading: false,
      total: 0,
      page: 1,
      pageSize: 10,
      previewRecordId: '',
      previewArticleDialogVisible: false
    }
  },
  methods: {
    initData(articleId) {
      this.articleId = articleId
      this.listTableData(articleId)
    },
    listTableData(articleId) {
      if (articleId != null && articleId !== '') {
        const param = {
          articleId: articleId,
          page: this.page,
          pageSize: this.pageSize
        }
        this.tableLoading = true
        this.$mapi.article.queryAutoSaveArticleContentByPage(param).then(({ data }) => {
          this.tableData = data.list
          this.total = data.total
        }).catch(_ => {
          this.tableData = []
          this.total = 0
        }).finally(() => {
          this.tableLoading = false
        })
      }
    },
    doClose(recordId) {
      this.articleId = ''
      this.tableData = []
      this.tableLoading = false
      this.total = 0
      this.page = 1
      this.pageSize = 10
      this.previewArticleDialogVisible = false
      this.$emit('chooseRecordDone', recordId)
    },
    handleClose() {
      this.doClose()
      // this.$confirm('确认关闭？').then(_ => {
      //   this.doClose()
      // }).catch(_ => {})
    },
    cancel() {
      this.handleClose()
    },
    sizeChange(size) {
      this.page = 1
      this.pageSize = size
      this.listTableData(this.articleId)
    },
    pageChange(page) {
      this.page = page
      this.listTableData(this.articleId)
    },
    previewContent(recordId) {
      this.previewRecordId = recordId
      this.previewArticleDialogVisible = true
      this.$nextTick(() => {
        this.$refs.previewArticleRef.initData(1, recordId)
      })
    },
    closePreviewArticleDialog() {
      this.previewArticleDialogVisible = false
    },
    regainContent(recordId) {
      this.$confirm('确定要恢复这条记录吗？').then(_ => {
        this.doClose(recordId)
      })
    },
    regainContentByPreview() {
      this.$confirm('确定要恢复这条记录吗？').then(_ => {
        this.previewArticleDialogVisible = false
        this.doClose(this.previewRecordId)
      })
    }
  }
}
</script>

<style scoped>
.demo-drawer__content {
  display: flex;
  flex-direction: column;
  height: 100%;
}
.demo-drawer__footer {
  margin-top: 10px;
  display: flex;
}
.demo-drawer__footer button {
  flex: 1;
  margin: 5px 5%;
}
</style>
