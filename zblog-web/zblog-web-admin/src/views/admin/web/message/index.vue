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
        <el-button v-perm="['deleteMessage']" :disabled="selectRows.length === 0" size="small" type="danger" @click="batchDeleteMessage()">
          批量删除
        </el-button>
        <el-button v-perm="['auditMessage']" :disabled="selectRows.length === 0" size="small" type="success" @click="auditMessage()">
          批量通过
        </el-button>
        <div style="margin-left:auto">
          <el-input v-model="filter.content" prefix-icon="el-icon-search" size="small" placeholder="请输入留言内容" style="width:200px" @keyup.enter.native="search" />
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
        <el-table-column :show-overflow-tooltip="true" prop="nickname" label="留言人" align="center" width="150" />
        <el-table-column :show-overflow-tooltip="true" prop="messageContent" label="留言内容" align="center" />
        <!-- <el-table-column :show-overflow-tooltip="true" prop="ipAddress" label="IP地址" align="center" width="150" /> -->
        <el-table-column :show-overflow-tooltip="true" prop="ipSource" label="IP来源" align="center" width="150" />
        <el-table-column :show-overflow-tooltip="true" prop="isReview" label="状态" align="center" width="100">
          <template v-slot="scope">
            <el-tag v-if="scope.row['isReview']" size="small">正常</el-tag>
            <el-tag v-else size="small" type="warning">审核中</el-tag>
          </template>
        </el-table-column>
        <el-table-column :show-overflow-tooltip="true" prop="createTime" label="留言时间" align="center" width="150" />
        <el-table-column v-if="hasPerm(['deleteMessage'])" label="操作" align="center" width="150px">
          <template v-slot="scope">
            <el-button-group>
              <el-popconfirm v-perm="['deleteMessage']" title="操作不可撤销，确定删除吗？" @confirm="deleteMessage(scope.row)">
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
  name: 'Talk',
  data() {
    return {
      filter: {
        content: '',
        status: ''
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
      this.$mapi.message.pageList(param).then(res => {
        const { data } = res
        this.tableData = data.list
        this.total = data.total
      }).catch(_ => {
        this.tableData = []
        this.total = 0
      })
    },
    deleteMessage(row) {
      const messageIds = []
      messageIds.push(row.id)
      this.doDeleteMessage(messageIds)
    },
    batchDeleteMessage() {
      if (this.selectRows.length === 0) {
        this.$message.error('请选择需要删除的留言')
        return
      }

      this.$confirm('确认执行删除操作吗？').then(_ => {
        const messageIds = []
        this.selectRows.forEach(row => {
          messageIds.push(row.id)
        })

        this.doDeleteMessage(messageIds)
      }).catch(_ => {})
    },
    doDeleteMessage(messageIds) {
      this.$mapi.message.deleteMessage(messageIds).then(_ => {
        this.listTableData()
      })
    },
    auditMessage() {
      if (this.selectRows.length === 0) {
        this.$message.error('请选择需要审核通过的留言')
        return
      }

      this.$confirm('确认执行审核操作吗？').then(_ => {
        const messageIds = []
        this.selectRows.forEach(row => {
          messageIds.push(row.id)
        })

        this.$mapi.message.auditMessage(messageIds).then(_ => {
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

</style>
