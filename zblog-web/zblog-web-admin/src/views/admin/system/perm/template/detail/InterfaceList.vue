<template>
  <div>
    <el-table v-loading="tableLoading" :data="tableData" border style="width: 100%;">
      <el-table-column :show-overflow-tooltip="true" prop="uri" label="请求地址" />
      <el-table-column :show-overflow-tooltip="true" prop="method" label="请求方式" />
      <el-table-column :show-overflow-tooltip="true" prop="type" label="接口状态" align="center">
        <template slot-scope="scope">
          <el-tag v-if="scope.row['type'] === 1" size="small">正常</el-tag>
          <el-tag v-else-if="scope.row['type'] === 2" size="small" type="danger">禁止访问</el-tag>
          <span v-else> - </span>
        </template>
      </el-table-column>
    </el-table>
    <!-- 分页组件 -->
    <el-pagination
      :total="total"
      :current-page="page"
      :page-size="pageSize"
      :page-sizes="[5, 10, 20, 50]"
      style="margin-top: 8px;"
      layout="total, prev, pager, next, sizes"
      @size-change="sizeChange"
      @current-change="pageChange"
    />
  </div>
</template>

<script>
export default {
  name: 'PermInterfaceList',
  data() {
    return {
      permId: '',
      tableData: [],
      tableLoading: false,
      total: 0,
      page: 1,
      pageSize: 5
    }
  },
  methods: {
    initData(permId) {
      this.permId = permId
      this.$nextTick(() => {
        this.listTableData()
      })
    },
    listTableData() {
      const param = {
        permId: this.permId,
        page: this.page,
        pageSize: this.pageSize
      }
      this.tableLoading = true
      this.$mapi.perm.queryPermInterfaces(param).then(res => {
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
