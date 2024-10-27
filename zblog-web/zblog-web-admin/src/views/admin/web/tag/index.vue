<template>
  <div class="app-container">
    <div class="head-container">
      <div>
        <el-input v-model="filters.name" clearable placeholder="标签名称" style="width: 180px" class="filter-item" @keyup.enter.native="search" />
        <el-input v-model="filters.createUser" clearable placeholder="创建用户" style="width: 180px" class="filter-item" @keyup.enter.native="search" />
        <el-select v-model="filters.type" clearable placeholder="标签类型" style="width: 120px" class="filter-item">
          <el-option v-for="item in tagTypeEnums" :key="item.value" :value="item.value" :label="item.name" />
        </el-select>
        <el-select v-model="filters.enabled" clearable placeholder="标签状态" style="width: 120px" class="filter-item">
          <el-option label="启用" value="true" />
          <el-option label="禁用" value="false" />
        </el-select>
        <span>
          <el-button class="filter-item" size="small" type="success" icon="el-icon-search" @click="search">查询</el-button>
          <el-button class="filter-item" size="small" type="warning" icon="el-icon-refresh-left" @click="resetSearch">重置</el-button>
        </span>
      </div>
      <div class="crud-opts">
        <span class="crud-opts-left">
          <el-button v-perm="['addTag']" size="small" type="primary" @click="addTag()">
            新增
          </el-button>
        </span>
      </div>
    </div>

    <div class="components-container">
      <el-table v-loading="tableLoading" :data="tableData" border style="width: 100%">
        <el-table-column :show-overflow-tooltip="true" prop="name" label="标签名称" align="center" />
        <el-table-column :show-overflow-tooltip="true" prop="type" label="标签类型" align="center">
          <template slot-scope="scope">
            <span v-if="scope.row.type === 1">公共标签</span>
            <span v-else-if="scope.row.type === 2">定制标签</span>
            <span v-else> {{ scope.row.type }} </span>
          </template>
        </el-table-column>
        <el-table-column :show-overflow-tooltip="true" prop="articleCount" label="文章数" align="center" />
        <el-table-column :show-overflow-tooltip="true" prop="enabled" label="标签状态" align="center">
          <template slot-scope="scope">
            <el-tag v-if="scope.row.enabled" size="small">正常</el-tag>
            <el-tag v-else size="small" type="warning">禁用</el-tag>
          </template>
        </el-table-column>
        <el-table-column :show-overflow-tooltip="true" prop="createUser" label="创建用户" align="center" />
        <el-table-column :show-overflow-tooltip="true" prop="createTime" label="创建时间" align="center" />
        <el-table-column v-if="hasPerm(['queryTagDetail', 'editTag', 'deleteTag'])" label="操作" align="center" width="250px">
          <template slot-scope="scope">
            <el-button-group>
              <el-button v-perm="['queryTagDetail']" type="info" size="mini" @click="showTagDetail(scope.row)">详情</el-button>
              <el-button v-perm="['editTag']" type="success" size="mini" @click="editTag(scope.row)">编辑</el-button>
              <el-popconfirm v-perm="['deleteTag']" title="操作不可撤销，确定删除吗？" @confirm="deleteTag(scope.row)">
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
    <!-- 查看详情 -->
    <showDetailDialog
      ref="showDetailDialogRef"
      :dialog-visible="showDetailDialogVisible"
      @showDetailDone="showDetailDone"
    />
  </div>
</template>

<script>
import addOrEditDialog from '@/views/admin/web/tag/template/addOrEditDialog'
import showDetailDialog from '@/views/admin/web/tag/template/showDetailDialog'
export default {
  name: 'Tag',
  components: {
    addOrEditDialog,
    showDetailDialog
  },
  data() {
    return {
      filters: {
        name: '',
        createUser: '',
        type: '',
        enabled: ''
      },
      tagTypeEnums: [
        { name: '公共标签', value: 1 },
        { name: '定制标签', value: 2 }
      ],
      tableData: [],
      tableLoading: false,
      total: 0,
      page: 1,
      pageSize: 10,
      addOrEditDialogTitle: '',
      addOrEditDialogVisible: false,
      showDetailDialogVisible: false
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
      this.filters.createUser = ''
      this.filters.type = ''
      this.filters.enabled = ''

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
      this.$mapi.tag.pageTagList(param).then(res => {
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
    addTag() {
      this.addOrEditDialogVisible = true
      this.addOrEditDialogTitle = '新增标签'
      this.$refs.addOrEditDialogRef.initData()
    },
    editTag(row) {
      this.addOrEditDialogVisible = true
      this.addOrEditDialogTitle = '编辑标签'
      this.$refs.addOrEditDialogRef.initData(row.id)
    },
    showTagDetail(row) {
      this.showDetailDialogVisible = true
      this.$refs.showDetailDialogRef.initData(row.id)
    },
    deleteTag(row) {
      this.$mapi.tag.deleteTag({ tagId: row.id }).then(res => {
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
