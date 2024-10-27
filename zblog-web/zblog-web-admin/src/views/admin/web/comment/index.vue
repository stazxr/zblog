<template>
  <div class="card-container">
    <el-card class="main-card">
      <div class="card-title">{{ this.$route.meta.title || this.$route.name }}</div>
      <div class="card-item">
        <span>状态</span>
        <span :class="isActive('')" @click="queryListByStatus('')">全部</span>
        <span :class="isActive('1')" @click="queryListByStatus('1')">正常</span>
        <span :class="isActive('2')" @click="queryListByStatus('2')">审核中</span>
      </div>
      <div style="display: flex; align-items: center; margin-bottom: 1rem; margin-top: 1.25rem;">
        <el-button v-perm="['deleteComment']" :disabled="selectRows.length === 0" size="small" type="danger" @click="batchDeleteComment()">
          批量删除
        </el-button>
        <el-button v-perm="['auditComment']" :disabled="selectRows.length === 0" size="small" type="success" @click="auditComment()">
          批量通过
        </el-button>
        <div style="margin-left:auto">
          <el-input v-show="filter.type === 1" v-model="filter.articleTitle" clearable prefix-icon="el-icon-search" size="small" placeholder="请输入文章标题" style="width:200px" @keyup.enter.native="search" />
          <el-input v-show="filter.type === 3" v-model="filter.talkTitle" clearable prefix-icon="el-icon-search" size="small" placeholder="请输入说说标题" style="width:200px" @keyup.enter.native="search" />
          <el-select v-model="filter.type" clearable placeholder="请选择评论来源" size="small" style="margin-right:1rem">
            <el-option v-for="item in commentTypeEnums" :key="item.value" :label="item.label" :value="item.value" />
          </el-select>
          <el-input v-model="filter.nickname" clearable prefix-icon="el-icon-search" size="small" placeholder="请输入用户昵称" style="width:200px" @keyup.enter.native="search" />
          <el-button type="primary" size="small" icon="el-icon-search" style="margin-left:1rem" @click="search">
            搜索
          </el-button>
        </div>
      </div>
      <el-table v-loading="tableLoading" :data="tableData" border style="width: 100%" @selection-change="handleSelectionChange">
        <el-table-column type="selection" width="45" />
        <el-table-column :show-overflow-tooltip="true" prop="avatar" label="头像" align="center" width="80">
          <template v-slot="scope">
            <img :src="scope.row['avatar']" width="40" height="40" alt="">
          </template>
        </el-table-column>
        <el-table-column :show-overflow-tooltip="true" prop="nickname" label="评论人" align="center" width="180" />
        <el-table-column :show-overflow-tooltip="true" prop="replyNickname" label="回复人" align="center" width="180" />
        <el-table-column :show-overflow-tooltip="true" prop="content" label="评论内容" align="center">
          <template v-slot="scope">
            <span class="comment-content" v-html="scope.row.content" />
          </template>
        </el-table-column>
        <el-table-column :show-overflow-tooltip="true" prop="ipSource" label="IP来源" align="center" width="120" />
        <el-table-column :show-overflow-tooltip="true" label="来源" align="center" width="70">
          <template v-slot="scope">
            <el-tag v-if="scope.row.type === 1" :title="scope.row['objectTitle']">文章</el-tag>
            <el-tag v-if="scope.row.type === 2">友链</el-tag>
            <el-tag v-if="scope.row.type === 3" :title="scope.row['objectTitle']">说说</el-tag>
          </template>
        </el-table-column>
        <el-table-column :show-overflow-tooltip="true" prop="isReview" label="状态" align="center" width="80">
          <template v-slot="scope">
            <el-tag v-if="scope.row['isReview']" size="small">正常</el-tag>
            <el-tag v-else size="small" type="warning">审核中</el-tag>
          </template>
        </el-table-column>
        <el-table-column :show-overflow-tooltip="true" prop="createTime" label="评论时间" align="center" width="150" />
        <!-- <el-table-column :show-overflow-tooltip="true" prop="ipAddress" label="IP地址" align="center" width="150" /> -->
        <!-- <el-table-column :show-overflow-tooltip="true" prop="objectTitle" label="文章/说说标题" align="center" width="200" /> -->
        <el-table-column v-if="hasPerm(['deleteComment'])" label="操作" align="center" width="80">
          <template v-slot="scope">
            <el-button-group>
              <el-popconfirm v-perm="['deleteComment']" title="操作不可撤销，确定删除吗？" @confirm="deleteComment(scope.row)">
                <el-button slot="reference" type="danger" size="mini">删除</el-button>
              </el-popconfirm>
            </el-button-group>
          </template>
        </el-table-column>
        <div slot="empty">
          <el-empty />
        </div>
      </el-table>
      <el-pagination
        :hide-on-single-page="false"
        :current-page="page"
        :page-size="pageSize"
        :total="total"
        :page-sizes="[10, 20, 30, 50]"
        background
        layout="total, sizes, prev, pager, next, jumper"
        style="float: right; margin-top: 1rem; margin-bottom: 2.5rem;"
        @size-change="sizeChange"
        @current-change="pageChange"
      />
    </el-card>
  </div>
</template>

<script>
export default {
  name: 'Comment',
  data() {
    return {
      commentTypeEnums: [
        { value: 1, label: '文章' },
        { value: 2, label: '友链' },
        { value: 3, label: '说说' }
      ],
      filter: {
        status: '',
        type: '',
        talkTitle: '',
        articleTitle: '',
        nickname: ''
      },
      selectRows: [],
      tableLoading: false,
      tableData: [],
      total: 0,
      page: 1,
      pageSize: 10
    }
  },
  computed: {
    isActive() {
      return function(status) {
        return this.filter.status === status ? 'card-item-active-status' : 'card-item-status'
      }
    }
  },
  mounted() {
    this.listTableData()
  },
  methods: {
    hasPerm(value) {
      return this.checkPerm(value)
    },
    queryListByStatus(status) {
      this.page = 1
      this.filter.status = status
      this.listTableData()
    },
    search() {
      this.page = 1
      this.listTableData()
    },
    listTableData() {
      const param = {
        ... this.filter,
        page: this.page,
        pageSize: this.pageSize
      }
      this.$mapi.comment.pageList(param).then(res => {
        const { data } = res
        this.tableData = data.list
        this.total = data.total
      }).catch(_ => {
        this.tableData = []
        this.total = 0
      })
    },
    deleteComment(row) {
      const commentIds = []
      commentIds.push(row.id)
      this.doDeleteComment(commentIds)
    },
    batchDeleteComment() {
      if (this.selectRows.length === 0) {
        this.$message.error('请选择需要删除的评论')
        return
      }

      this.$confirm('确认执行删除操作吗？').then(_ => {
        const commentIds = []
        this.selectRows.forEach(row => {
          commentIds.push(row.id)
        })

        this.doDeleteComment(commentIds)
      }).catch(_ => {})
    },
    doDeleteComment(commentIds) {
      this.$mapi.comment.deleteComment(commentIds).then(_ => {
        this.listTableData()
      })
    },
    auditComment() {
      if (this.selectRows.length === 0) {
        this.$message.error('请选择需要审核通过的评论')
        return
      }

      this.$confirm('确认执行审核操作吗？').then(_ => {
        const commentIds = []
        this.selectRows.forEach(row => {
          commentIds.push(row.id)
        })

        this.$mapi.comment.auditComment(commentIds).then(_ => {
          this.listTableData()
        })
      }).catch(_ => {})
    },
    handleSelectionChange(val) {
      this.selectRows = val
    },
    sizeChange(size) {
      this.page = 1
      this.pageSize = size
      this.listTableData()
    },
    pageChange(page) {
      this.page = page
      this.listTableData()
    }
  }
}
</script>

<style scoped>
.comment-content {
  display: inline-block;
}
</style>
