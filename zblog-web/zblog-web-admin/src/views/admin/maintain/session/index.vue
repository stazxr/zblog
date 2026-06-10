<template>
  <div class="app-container">
    <div class="head-container">
      <div class="search-opts">
        <muses-search-form ref="searchForm" :model="filters" label-position="right" label-width="0" :offset="0" :item-width="160">
          <muses-search-form-item>
            <el-input v-model="filters.username" placeholder="用户名" clearable @keyup.enter.native="search" />
          </muses-search-form-item>
          <muses-search-form-item btn btn-open-name="" btn-close-name="">
            <el-button v-perm="['SESSD001']" :disabled="row === null" type="danger" @click="kickoutUser">踢出</el-button>
          </muses-search-form-item>
        </muses-search-form>
      </div>
    </div>
    <div class="components-container">
      <el-table
        ref="sessionTable"
        v-loading="tableLoading"
        :data="pageData"
        :header-cell-style="{ background: '#FAFAFA' }"
        highlight-current-row
        row-key="key"
        border
        @current-change="handleCurrentChange"
      >
        <el-table-column :show-overflow-tooltip="true" prop="username" label="用户名" width="120" />
        <el-table-column :show-overflow-tooltip="true" prop="accessTokenId" label="短令牌" />
        <el-table-column :show-overflow-tooltip="true" prop="refreshTokenId" label="长令牌" />
        <el-table-column :show-overflow-tooltip="true" prop="loginTime" label="登录时间" width="150">
          <template v-slot="{ row }">
            {{ formatTime(row.loginTime) }}
          </template>
        </el-table-column>
        <el-table-column :show-overflow-tooltip="true" prop="renewTime" label="续签时间" width="150">
          <template v-slot="{ row }">
            {{ formatTime(row.renewTime) }}
          </template>
        </el-table-column>
        <div slot="empty">
          <el-empty :image="nodataImg" description=" " />
        </div>
      </el-table>
      <div class="pagination-container">
        <el-pagination
          :total="filteredData.length"
          :current-page.sync="page"
          :page-size.sync="pageSize"
          :page-sizes="[10, 20, 30, 50]"
          layout="total, prev, pager, next, sizes"
          @size-change="handleSizeChange"
          @current-change="handlePageChange"
        />
      </div>
    </div>
  </div>
</template>

<script>
import nodataImg from '@/assets/images/nodata.png'
export default {
  name: 'Session',
  data() {
    return {
      filters: {
        username: null
      },
      tableData: [],
      tableLoading: false,
      nodataImg: nodataImg,
      row: null,
      page: 1,
      pageSize: 10
    }
  },
  computed: {
    filteredData() {
      let data = this.tableData || []
      const { username } = this.filters
      if (username) {
        data = data.filter(i =>
          (i.username || '').includes(username)
        )
      }
      return data
    },
    pageData() {
      const start = (this.page - 1) * this.pageSize
      const end = start + this.pageSize
      return this.filteredData.slice(start, end)
    }
  },
  mounted() {
    this.listTableData()
  },
  methods: {
    handleCurrentChange(row) {
      this.row = row
    },
    // 查询
    search() {
      this.page = 1
      this.row = null
      this.$refs.sessionTable.setCurrentRow()
    },
    handleSizeChange(size) {
      this.page = 1
      this.pageSize = size
      this.row = null
      this.$refs.sessionTable.setCurrentRow()
    },
    handlePageChange(page) {
      this.page = page
      this.row = null
      this.$refs.sessionTable.setCurrentRow()
    },
    listTableData() {
      this.tableLoading = true
      this.$mapi.session.list().then(res => {
        const { data } = res
        this.tableData = data
      }).catch(_ => {
        this.tableData = []
      }).finally(() => {
        this.tableLoading = false
        this.row = null
        this.$refs.sessionTable.setCurrentRow()
      })
    },
    // 删除
    kickoutUser() {
      if (this.row === null) {
        this.$message.error('请选择要踢出的用户')
        return
      }
      this.$confirm('操作不可回退, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.$mapi.session.kickout({ userId: this.row.userId }).then(res => {
          this.$message.success(res.message)
          this.listTableData()
        })
      })
    },
    formatTime(timestamp) {
      if (!timestamp) return ''
      const date = new Date(Number(timestamp))
      const y = date.getFullYear()
      const m = String(date.getMonth() + 1).padStart(2, '0')
      const d = String(date.getDate()).padStart(2, '0')
      const h = String(date.getHours()).padStart(2, '0')
      const min = String(date.getMinutes()).padStart(2, '0')
      const s = String(date.getSeconds()).padStart(2, '0')
      return `${y}-${m}-${d} ${h}:${min}:${s}`
    }
  }
}
</script>

<style scoped>

</style>
