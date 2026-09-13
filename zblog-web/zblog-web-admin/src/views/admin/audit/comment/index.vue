<template>
  <div class="app-container">
    <div class="head-container">
      <div class="search-opts">
        <muses-search-form ref="searchForm" :model="filters" label-position="right" label-width="0" :offset="0" :item-width="160">
          <muses-search-form-item label="" prop="search-status">
            <el-select id="search-status" v-model="filters.status" placeholder="评论状态" clearable @change="search">
              <el-option v-for="item in commentStatusList" :key="item.value" :label="item.name" :value="item.value" />
            </el-select>
          </muses-search-form-item>
          <muses-search-form-item label="" prop="search-type">
            <el-select id="search-type" v-model="filters.type" placeholder="评论类型" clearable @change="search">
              <el-option v-for="item in commentTypeList" :key="item.value" :label="item.name" :value="item.value" />
            </el-select>
          </muses-search-form-item>
          <muses-search-form-item label="" prop="search-level">
            <el-select id="search-level" v-model="filters.level" placeholder="评论级别" clearable @change="search">
              <el-option v-for="item in commentLevelList" :key="item.value" :label="item.name" :value="item.value" />
            </el-select>
          </muses-search-form-item>
          <muses-search-form-item label="" prop="search-content">
            <el-input id="search-content" v-model="filters.content" clearable placeholder="内容" @keyup.enter.native="search" />
          </muses-search-form-item>
          <muses-search-form-item label="" prop="search-nickname">
            <el-input id="search-nickname" v-model="filters.nickname" clearable placeholder="用户昵称" @keyup.enter.native="search" />
          </muses-search-form-item>
          <muses-search-form-item label="" prop="search-ip">
            <el-input id="search-ip" v-model="filters.ip" clearable placeholder="归属IP" @keyup.enter.native="search" />
          </muses-search-form-item>
          <muses-search-form-item label="" prop="search-eventStartTime">
            <el-date-picker id="search-eventStartTime" v-model="filters.createStartTime" type="datetime" placeholder="开始时间" value-format="yyyy-MM-dd HH:mm:ss" default-time="00:00:00" />
          </muses-search-form-item>
          <muses-search-form-item label="" prop="search-eventEndTime">
            <el-date-picker id="search-eventEndTime" v-model="filters.createEndTime" type="datetime" placeholder="结束时间" value-format="yyyy-MM-dd HH:mm:ss" default-time="23:59:59" />
          </muses-search-form-item>
          <muses-search-form-item btn btn-open-name="" btn-close-name="">
            <el-button type="success" @click="search()">查 询</el-button>
            <el-button type="warning" @click="resetSearch()">重 置</el-button>
          </muses-search-form-item>
        </muses-search-form>
      </div>
      <div class="crud-opts">
        <span class="crud-opts-left">
          <el-button v-perm="['COMNQ002']" :disabled="row === null" type="info" @click="showDetail">详情</el-button>
          <el-button v-perm="['COMNU001']" :disabled="row === null || (row.status !== 0 && row.status !== 4)" type="primary" @click="auditComment">审核</el-button>
          <el-button v-perm="['COMND001']" :disabled="row === null" type="danger" @click="deleteComment">删除</el-button>
        </span>
      </div>
    </div>
    <div class="components-container">
      <el-table
        ref="commentTable"
        v-loading="tableLoading"
        :data="tableData"
        :header-cell-style="{background:'#FAFAFA'}"
        highlight-current-row
        row-key="id"
        border
        @current-change="handleCurrentChange"
      >
        <el-table-column label="评论用户" align="center" width="175">
          <template v-slot="scope">
            <div class="user-info">
              <el-avatar :size="32" :src="scope.row.user.avatar">
                {{ (scope.row.user.nickname || '?').substring(0, 1) }}
              </el-avatar>
              <span class="nickname"> {{ scope.row.user.nickname }} </span>
            </div>
          </template>
        </el-table-column>
        <el-table-column prop="content" label="内容" align="center">
          <template v-slot="scope">
            <div class="comment-content" v-html="scope.row.content" />
          </template>
        </el-table-column>
        <el-table-column label="评论类型" align="center" width="100px">
          <template v-slot="scope">
            {{ commentTypeMap.get(scope.row.type) || scope.row.type }}
          </template>
        </el-table-column>
        <el-table-column label="评论级别" align="center" width="100px">
          <template v-slot="scope">
            {{ commentLevelMap.get(scope.row.level) || scope.row.level }}
          </template>
        </el-table-column>
        <el-table-column label="回复用户" align="center" width="175">
          <template v-slot="scope">
            <div v-if="scope.row.replyUser.id" class="user-info">
              <el-avatar :size="32" :src="scope.row.replyUser.avatar">
                {{ (scope.row.replyUser.nickname || '?').substring(0, 1) }}
              </el-avatar>
              <span class="nickname"> {{ scope.row.replyUser.nickname }} </span>
            </div>
          </template>
        </el-table-column>
        <el-table-column prop="likeCount" label="点赞数" align="center" width="100px" />
        <el-table-column label="IP / 地址" align="center" width="150px">
          <template v-slot="scope">
            <div class="ip-info">
              <div class="ip-address">
                {{ scope.row.ipAddress || '-' }}
              </div>
              <div v-if="scope.row.ipSource" class="ip-source">
                {{ scope.row.ipSource }}
              </div>
            </div>
          </template>
        </el-table-column>
        <el-table-column label="审核状态" align="center" width="100px">
          <template v-slot="scope">
            {{ commentStatusMap.get(scope.row.status) || scope.row.status }}
          </template>
        </el-table-column>
        <el-table-column prop="createTime" label="创建时间" align="center" width="160px" />
        <div slot="empty">
          <muses-empty description="暂无数据" />
        </div>
      </el-table>
      <div class="pagination-container">
        <el-pagination
          :total="total"
          :current-page.sync="page"
          :page-size.sync="pageSize"
          :page-sizes="[10, 20, 30, 50]"
          layout="total, prev, pager, next, sizes"
          @size-change="handleSizeChange"
          @current-change="handlePageChange"
        />
      </div>
    </div>

    <!-- 详情 -->
    <detailDialog
      ref="detailDialogRef"
      :dialog-visible="detailDialogVisible"
      @showDetailDone="showDetailDone"
    />
    <!-- 审核 -->
    <auditDialog
      ref="auditDialogRef"
      :dialog-visible="auditDialogVisible"
      @auditDone="auditDone"
    />
  </div>
</template>

<script>
import detailDialog from '@/views/admin/audit/comment/template/detailDialog'
import auditDialog from '@/views/admin/audit/comment/template/auditDialog'
export default {
  name: 'Comment',
  components: {
    detailDialog,
    auditDialog
  },
  data() {
    return {
      filters: {
        content: null,
        nickname: null,
        ip: null,
        type: null,
        level: null,
        status: null,
        createStartTime: null,
        createEndTime: null
      },
      commentStatusList: [],
      commentTypeList: [],
      commentLevelList: [],
      tableData: [],
      tableLoading: false,
      row: null,
      total: 0,
      page: 1,
      pageSize: 10,
      detailDialogVisible: false,
      auditDialogVisible: false
    }
  },
  computed: {
    commentStatusMap() {
      const map = new Map()
      this.commentStatusList.forEach(item => {
        map.set(Number(item.value), item.name)
      })
      return map
    },
    commentTypeMap() {
      const map = new Map()
      this.commentTypeList.forEach(item => {
        map.set(Number(item.value), item.name)
      })
      return map
    },
    commentLevelMap() {
      const map = new Map()
      this.commentLevelList.forEach(item => {
        map.set(Number(item.value), item.name)
      })
      return map
    }
  },
  mounted() {
    this.loadCommentStatusList()
    this.loadCommentTypeList()
    this.loadCommentLevelList()
    this.listTableData()
  },
  methods: {
    handleCurrentChange(row) {
      this.row = row
    },
    loadCommentStatusList() {
      this.$mapi.communal.queryConfListByDictKey({ dictKey: 'COMMENT_STATUS_CONFIG' }).then(res => {
        const { data } = res
        this.commentStatusList = data
      }).catch(_ => {
        this.commentStatusList = []
      })
    },
    loadCommentTypeList() {
      this.$mapi.communal.queryConfListByDictKey({ dictKey: 'COMMENT_TYPE_CONFIG' }).then(res => {
        const { data } = res
        this.commentTypeList = data
      }).catch(_ => {
        this.commentTypeList = []
      })
    },
    loadCommentLevelList() {
      this.$mapi.communal.queryConfListByDictKey({ dictKey: 'COMMENT_LEVEL_CONFIG' }).then(res => {
        const { data } = res
        this.commentLevelList = data
      }).catch(_ => {
        this.commentLevelList = []
      })
    },
    // 查询
    search() {
      this.page = 1
      this.listTableData()
    },
    resetSearch() {
      Object.keys(this.filters).forEach(key => { this.filters[key] = null })
      this.page = 1
      this.listTableData()
    },
    handleSizeChange(size) {
      this.page = 1
      this.pageSize = size
      this.listTableData()
    },
    handlePageChange(page) {
      this.page = page
      this.listTableData()
    },
    listTableData() {
      const param = {
        ... this.filters,
        page: this.page,
        pageSize: this.pageSize
      }
      this.tableLoading = true
      this.$mapi.comment.pageCommentList(param).then(res => {
        const { data } = res
        this.total = data.total
        this.tableData = data.records
      }).catch(_ => {
        this.total = 0
        this.tableData = []
      }).finally(() => {
        this.tableLoading = false
        this.row = null
        this.$refs.commentTable.setCurrentRow()
      })
    },
    // 详情
    showDetail() {
      if (this.row === null) {
        this.$message.error('请选择要查看的评论')
        return
      }
      this.detailDialogVisible = true
      this.$refs.detailDialogRef.initData(this.row.id)
    },
    showDetailDone() {
      this.detailDialogVisible = false
    },
    // 审核
    auditComment() {
      this.auditDialogVisible = true
      this.$refs.auditDialogRef.initData(this.row.id)
    },
    auditDone(result = false) {
      this.auditDialogVisible = false
      if (result) {
        this.listTableData()
      }
    },
    // 删除
    deleteComment() {
      if (this.row === null) {
        this.$message.error('请选择要删除的评论')
        return
      }
      this.$confirm('此操作将永久删除评论, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.$mapi.comment.deleteComment({ commentId: this.row.id }).then(res => {
          this.$message.success(res.message)
          this.listTableData()
        })
      })
    }
  }
}
</script>

<style scoped>
.head-container .search-opts .el-date-editor {
  width: 170px !important;
}

.user-info {
  display: flex;
  align-items: center;
  justify-content: center;
}
.nickname {
  margin-left: 8px;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.comment-content {
  max-height: 72px;
  overflow: hidden;
  line-height: 24px;
  word-break: break-word;
}
.comment-content >>> img {
  max-width: 48px;
  max-height: 48px;
  margin: 2px 4px;
  border-radius: 4px;
  vertical-align: middle;
  object-fit: contain;
}

.ip-info {
  line-height: 1.5;
}
.ip-address {
  color: #606266;
  font-size: 13px;
}
.ip-source {
  margin-top: 2px;
  color: #909399;
  font-size: 12px;
}
</style>
