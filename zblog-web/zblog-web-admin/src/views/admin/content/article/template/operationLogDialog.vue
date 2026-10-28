<template>
  <el-dialog
    title="文章操作记录"
    :visible.sync="dialogVisible"
    :fullscreen="isMobile"
    :destroy-on-close="true"
    :close-on-click-modal="true"
    :close-on-press-escape="true"
    :before-close="handleClose"
    append-to-body
    width="70%"
    top="8vh"
  >
    <div class="operation-log-container">
      <div class="operation-log-header">
        <div class="operation-log-info">
          <span class="article-label">文章 ID：</span>
          <span>{{ filters.articleId || '-' }}</span>
          <span class="total-text">共 {{ total }} 条记录</span>
        </div>
        <el-button type="primary" icon="el-icon-refresh" :loading="tableLoading" @click="search">刷新</el-button>
      </div>

      <el-table
        ref="articleOperationLogTable"
        v-loading="tableLoading"
        :data="tableData"
        border
        stripe
        row-key="id"
        height="calc(80vh - 260px)"
      >
        <el-table-column prop="createTime" label="操作时间" width="165" align="center">
          <template v-slot="{ row }">
            {{ row.createTime }}
          </template>
        </el-table-column>
        <el-table-column prop="operatorUserNickname" label="操作人" min-width="100" align="center" show-overflow-tooltip />
        <el-table-column prop="operationType" label="操作类型" width="120" align="center">
          <template v-slot="{ row }">
            <el-tag :type="getOperationTagType(row['operationType'])" size="small">
              {{ getOperationTypeName(row['operationType']) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="属性变化" min-width="190" align="center">
          <template v-slot="{ row }">
            <div class="change-item">
              <span class="change-label">状态：</span>
              <span>{{ getStatusName(row['beforeStatus']) }}</span>
              <i class="el-icon-right" />
              <span class="after-value">
                {{ getStatusName(row['afterStatus']) }}
              </span>
            </div>
            <div class="change-item">
              <span class="change-label">权限：</span>
              <span>{{ getPermName(row['beforePerm']) }}</span>
              <i class="el-icon-right" />
              <span class="after-value">
                {{ getPermName(row['afterPerm']) }}
              </span>
            </div>
            <div class="change-item">
              <span class="change-label">类型：</span>
              <span>{{ getArticleTypeName(row['beforeType']) }}</span>
              <i class="el-icon-right" />
              <span class="after-value">
                {{ getArticleTypeName(row['afterType']) }}
              </span>
            </div>
          </template>
        </el-table-column>
        <el-table-column prop="remark" label="操作说明" min-width="200" show-overflow-tooltip />
        <el-table-column prop="version" label="当前发布版本" width="100" align="center">
          <template v-slot="{ row }">
            <span v-if="row.version !== null && row.version !== undefined && row.version !== 0">
              V{{ row.version }}
            </span>
            <span v-else>-</span>
          </template>
        </el-table-column>
      </el-table>

      <div class="operation-log-pagination">
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

    <span slot="footer">
      <el-button @click="dialogVisible = false">关闭</el-button>
    </span>
  </el-dialog>
</template>

<script>
export default {
  props: {
    dialogVisible: {
      type: Boolean,
      default: false
    }
  },
  data() {
    return {
      filters: {
        articleId: null
      },
      tableData: [],
      tableLoading: false,
      total: 0,
      page: 1,
      pageSize: 10
    }
  },
  computed: {
    isMobile() {
      return this.$store.state.app.device === 'mobile'
    }
  },
  methods: {
    // 加载表格数据
    initData(articleId) {
      if (articleId === null || articleId === undefined || articleId === '') {
        this.$message.warning('请先选择文章')
        return
      }

      this.filters.articleId = articleId
      this.search()
    },
    search() {
      this.page = 1
      this.pageSize = 10
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
      this.$mapi.article.pageArticleOperationLogList(param).then(res => {
        const { data } = res
        this.total = data.total
        this.tableData = data.records
      }).catch(_ => {
        this.total = 0
        this.tableData = []
      }).finally(() => {
        this.tableLoading = false
      })
    },
    // 操作类型名称
    getOperationTypeName(type) {
      const map = {
        1: '创建文章',
        2: '保存文章',
        3: '提交审核',
        4: '发布文章',
        5: '定时发布',
        6: '审核拒绝',
        7: '移至回收站',
        8: '删除文章',
        9: '清空回收站',
        10: '恢复文章'
      }
      return map[type] || '其他操作'
    },
    // 操作标签样式
    getOperationTagType(type) {
      const map = {
        1: 'info',
        2: 'primary',
        3: 'info',
        4: 'success',
        5: 'primary',
        6: 'warning',
        7: 'danger',
        8: 'danger',
        9: 'danger',
        10: 'primary'
      }
      return map[type] || 'info'
    },
    // 文章状态名称
    getStatusName(status) {
      if (status === null || status === undefined || status === '') {
        return ''
      }
      const map = {
        1: '草稿',
        2: '待审核',
        3: '待发布',
        4: '审核未通过',
        5: '已发布',
        91: '已下线',
        101: '已删除', // 回收站
        102: '彻底删除' // 回收站的也被删除了
      }
      return map[status] || '未知状态(' + status + ')'
    },
    // 文章权限名称
    getPermName(perm) {
      if (perm === null || perm === undefined || perm === '') {
        return ''
      }
      const map = {
        1: '公开',
        2: '登录可见',
        3: '仅作者可见',
        4: '公众号验证',
        5: '密码验证',
        6: '付费阅读',
        7: '指定用户可见'
      }
      return map[perm] || '未知权限(' + perm + ')'
    },
    // 文章类型名称
    getArticleTypeName(type) {
      if (type === null || type === undefined || type === '') {
        return ''
      }
      const map = {
        1: '原创',
        2: '转载',
        3: '翻译'
      }
      return map[type] || '未知类型(' + type + ')'
    },
    // 关闭模态框
    doClose() {
      this.total = 0
      this.tableData = []
      this.tableLoading = false
      this.page = 1
      this.pageSize = 10
      this.filters.articleId = null
      this.$emit('showOperationLogDone')
    },
    handleClose() {
      this.doClose()
    }
  }
}
</script>

<style scoped>
.operation-log-container {
  width: 100%;
}
.operation-log-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  margin-bottom: 15px;
}
.operation-log-info {
  display: flex;
  align-items: center;
  font-size: 13px;
  color: #606266;
}
.article-label {
  margin-right: 5px;
}
.total-text {
  margin-left: 20px;
  color: #909399;
}
.change-item {
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 5px;
  line-height: 22px;
  white-space: nowrap;
}
.change-label {
  color: #909399;
  margin-right: 3px;
}
.after-value {
  color: #409eff;
  font-weight: 500;
}
.operation-log-pagination {
  display: flex;
  justify-content: flex-end;
  margin-top: 18px;
}
</style>
