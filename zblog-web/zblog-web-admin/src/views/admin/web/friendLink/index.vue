<template>
  <div class="app-container">
    <div class="head-container">
      <div>
        <el-input v-model="filters.name" clearable placeholder="友链名称" style="width: 200px" class="filter-item" @keyup.enter.native="search" />
        <el-select v-model="filters.valid" clearable placeholder="友链状态" style="width: 120px" class="filter-item">
          <el-option label="有效" value="true" />
          <el-option label="失效" value="false" />
        </el-select>
        <span>
          <el-button class="filter-item" size="small" type="success" icon="el-icon-search" @click="search">查询</el-button>
          <el-button class="filter-item" size="small" type="warning" icon="el-icon-refresh-left" @click="resetSearch">重置</el-button>
        </span>
      </div>
      <div class="crud-opts">
        <span class="crud-opts-left">
          <el-button v-perm="['addFriendLink']" size="small" type="primary" @click="addFriendLink()">
            新增
          </el-button>
        </span>
      </div>
    </div>

    <div class="components-container">
      <el-table v-loading="tableLoading" :data="tableData" border style="width: 100%">
        <el-table-column prop="linkAvatar" label="链接头像" align="center" width="150">
          <template slot-scope="scope">
            <img :src="scope.row['headUrl']" width="40" height="40" alt="">
          </template>
        </el-table-column>
        <el-table-column :show-overflow-tooltip="true" prop="name" label="链接名称" align="center" width="200" />
        <el-table-column :show-overflow-tooltip="true" prop="linkUrl" label="链接地址" align="center">
          <template slot-scope="scope">
            <el-link type="primary" :href="scope.row['linkUrl']" target="_blank">{{ scope.row['linkUrl'] }}</el-link>
          </template>
        </el-table-column>
        <el-table-column :show-overflow-tooltip="true" prop="linkRemark" label="链接介绍" align="center" />
        <el-table-column :show-overflow-tooltip="true" prop="valid" label="链接状态" align="center" width="100">
          <template slot-scope="scope">
            <el-tag v-if="scope.row.valid" size="small">有效</el-tag>
            <el-tag v-else size="small" type="warning">失效</el-tag>
          </template>
        </el-table-column>
        <el-table-column :show-overflow-tooltip="true" prop="createTime" label="创建时间" align="center" width="150" />
        <el-table-column v-if="hasPerm(['editFriendLink', 'deleteFriendLink'])" label="操作" align="center" width="150">
          <template slot-scope="scope">
            <el-button-group>
              <el-button v-perm="['editFriendLink']" type="success" size="mini" @click="editFriendLink(scope.row)">编辑</el-button>
              <el-popconfirm v-perm="['deleteFriendLink']" title="操作不可撤销，确定删除吗？" @confirm="deleteFriendLink(scope.row)">
                <el-button slot="reference" type="danger" size="mini">删除</el-button>
              </el-popconfirm>
            </el-button-group>
          </template>
        </el-table-column>
        <div slot="empty">
          <el-empty />
        </div>
      </el-table>
    </div>

    <div class="pagination-container">
      <el-pagination
        :total="total"
        :current-page="page"
        style="margin-top: 8px;"
        layout="total, prev, pager, next, sizes"
        @size-change="sizeChange"
        @current-change="pageChange"
      />
    </div>

    <!-- 新增 / 编辑 -->
    <addOrEditDialog
      ref="addOrEditDialogRef"
      :dialog-visible="addOrEditDialogVisible"
      :dialog-title="addOrEditDialogTitle"
      @addOrEditDone="addOrEditDone"
    />
  </div>
</template>

<script>
import addOrEditDialog from '@/views/admin/web/friendLink/template/addOrEditDialog'
export default {
  name: 'FriendLink',
  components: {
    addOrEditDialog
  },
  data() {
    return {
      filters: {
        name: '',
        valid: ''
      },
      tableData: [],
      tableLoading: false,
      total: 0,
      page: 1,
      pageSize: 10,
      addOrEditDialogTitle: '',
      addOrEditDialogVisible: false
    }
  },
  mounted() {
    this.listTableData()
  },
  methods: {
    hasPerm(value) {
      return this.checkPerm(value)
    },
    search() {
      this.page = 1
      this.listTableData()
    },
    resetSearch() {
      this.filters.name = ''
      this.filters.valid = ''

      this.page = 1
      this.listTableData()
    },
    listTableData() {
      const param = {
        ... this.filters,
        page: this.page,
        pageSize: this.pageSize
      }
      this.tableLoading = true
      this.$mapi.friendLink.pageList(param).then(res => {
        const { data } = res
        this.tableData = data.list
        this.total = data.total
      }).catch(_ => {
        this.tableData = []
        this.total = 0
      }).finally(() => {
        this.tableLoading = false
      })
    },
    addFriendLink() {
      this.addOrEditDialogVisible = true
      this.addOrEditDialogTitle = '新增友链'
      this.$refs.addOrEditDialogRef.initData()
    },
    editFriendLink(row) {
      this.addOrEditDialogVisible = true
      this.addOrEditDialogTitle = '编辑友链'
      this.$refs.addOrEditDialogRef.initData(row.id)
    },
    deleteFriendLink(row) {
      this.$mapi.friendLink.deleteFriendLink({ friendLinkId: row.id }).then(res => {
        this.$message.success(res.message)
        this.listTableData()
      })
    },
    addOrEditDone(result = false) {
      this.addOrEditDialogVisible = false
      this.addOrEditDialogTitle = ''
      if (result) {
        this.listTableData()
      }
    },
    showDetailDone() {
      this.showDetailDialogVisible = false
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
