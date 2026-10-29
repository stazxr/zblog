<template>
  <div>
    <el-drawer :visible.sync="dialogVisible" title="内容修改记录（系统每60秒自动保存）" destroy-on-close :before-close="handleClose" size="50%" @closed="doClose">
      <div class="demo-drawer__content">
        <el-table v-loading="tableLoading" :data="tableData" border style="width: 90%;margin: 0 5%">
          <el-table-column :show-overflow-tooltip="true" prop="id" label="记录编码" align="center" />
          <el-table-column :show-overflow-tooltip="true" prop="wordCount" label="字数" align="center" />
          <el-table-column :show-overflow-tooltip="true" prop="saveTime" label="保存时间" align="center" />
          <el-table-column label="操作" align="center" width="150px">
            <template v-slot="{ row }">
              <el-button-group>
                <el-button type="info" @click="previewContent(row.articleId)">预览</el-button>
                <el-button type="warning" @click="regainContent(row.id)">恢复</el-button>
              </el-button-group>
            </template>
          </el-table-column>
          <div slot="empty">
            <muses-empty />
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
          <el-button @click="cancel">关 闭</el-button>
        </div>
      </div>
    </el-drawer>

    <previewDialog
      ref="previewArticleRef"
      :dialog-visible="previewDialogVisible"
      @previewDone="closePreviewArticleDialog"
    />
  </div>
</template>

<script>
import previewDialog from '@/views/admin/content/article/template/previewDialog'
export default {
  components: {
    previewDialog
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
      previewDialogVisible: false
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
          this.tableData = data.records
          this.total = data.total
        }).catch(_ => {
          this.total = 0
          this.tableData = []
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
      this.previewDialogVisible = false
      this.$emit('chooseRecordDone', recordId)
    },
    handleClose() {
      this.doClose()
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
    previewContent(articleId) {
      this.previewDialogVisible = true
      this.$nextTick(() => {
        this.$refs.previewArticleRef.initData(articleId)
      })
    },
    closePreviewArticleDialog() {
      this.previewDialogVisible = false
    },
    regainContent(recordId) {
      this.$confirm('确定要恢复这条记录吗？').then(_ => {
        this.doClose(recordId)
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
