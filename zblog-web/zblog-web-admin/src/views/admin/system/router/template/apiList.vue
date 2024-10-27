<template>
  <div class="app-container">
    <div class="head-container">
      <div>
        <el-input v-model="filters.name" clearable placeholder="接口名称" style="width: 150px" class="filter-item" @keyup.enter.native="search" />
        <el-input v-model="filters.code" clearable placeholder="接口编码" style="width: 150px" class="filter-item" @keyup.enter.native="search" />
        <el-input v-model="filters.uri" clearable placeholder="接口地址" style="width: 150px" class="filter-item" @keyup.enter.native="search" />
        <el-select v-model="filters.level" clearable placeholder="访问级别" style="width: 120px" class="filter-item">
          <el-option label="公开" value="1" />
          <el-option label="认证" value="2" />
          <el-option label="授权" value="3" />
        </el-select>
        <el-select v-model="filters.status" clearable placeholder="接口状态" style="width: 120px" class="filter-item">
          <el-option label="默认" value="1" />
          <el-option label="启用" value="2" />
          <el-option label="禁用" value="3" />
        </el-select>
        <el-select v-model="filters.logShowed" clearable placeholder="日志展示" style="width: 120px" class="filter-item">
          <el-option label="开启" value="true" />
          <el-option label="关闭" value="false" />
        </el-select>
        <span>
          <el-button class="filter-item" size="small" type="success" icon="el-icon-search" @click="search">查询</el-button>
          <el-button class="filter-item" size="small" type="warning" icon="el-icon-refresh-left" @click="resetSearch">重置</el-button>
        </span>
      </div>
    </div>

    <div class="components-container">
      <el-table v-loading="tableLoading" :data="tableData" border style="width: 100%">
        <el-table-column :show-overflow-tooltip="true" prop="name" label="接口名称" align="left" />
        <el-table-column :show-overflow-tooltip="true" prop="code" label="接口编码" align="left" />
        <el-table-column :show-overflow-tooltip="true" label="接口地址" align="left">
          <template slot-scope="scope">
            <span>[{{ scope.row['method'] }}] {{ scope.row['uri'] }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="permLevel" label="访问级别" align="center" width="75px">
          <template slot-scope="scope">
            <el-tag v-if="scope.row['permLevel'] === 1" size="small" type="warning">公开</el-tag>
            <el-tag v-else-if="scope.row['permLevel'] === 2" size="small" type="info">认证</el-tag>
            <el-tag v-else-if="scope.row['permLevel'] === 3" size="small" type="success">授权</el-tag>
            <span v-else> - </span>
          </template>
        </el-table-column>
        <el-table-column :show-overflow-tooltip="true" label="接口状态" align="center">
          <template slot-scope="scope">
            <el-tag v-if="scope.row['permEnabled'] == null" size="small" type="info">默认</el-tag>
            <el-tag v-if="scope.row['permEnabled'] === true" size="small" type="success">启用</el-tag>
            <el-tag v-if="scope.row['permEnabled'] === false" size="small" type="warning">禁用</el-tag>
          </template>
        </el-table-column>
        <el-table-column :show-overflow-tooltip="true" prop="enabled" label="日志展示" align="center">
          <template slot-scope="scope">
            <el-switch
              v-model="scope.row['logShowed']"
              :disabled="!hasPerm('updateLogShowStatus')"
              active-color="#13ce66"
              inactive-color="#ff4949"
              active-text="开启"
              inactive-text="关闭"
              @change="changeLogShowStatus(scope.row, scope.row['logShowed'])"
            />
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
        :page-size="pageSize"
        style="margin-top: 8px;"
        layout="total, prev, pager, next, sizes"
        @size-change="sizeChange"
        @current-change="pageChange"
      />
    </div>
  </div>
</template>

<script>
export default {
  data() {
    return {
      filters: {
        name: '',
        code: '',
        uri: '',
        level: '',
        status: '',
        logShowed: ''
      },
      showedStatus: {
        true: '开启',
        false: '关闭'
      },
      tableData: [],
      tableLoading: false,
      total: 0,
      page: 1,
      pageSize: 10
    }
  },
  methods: {
    initData() {
      this.$nextTick(() => {
        this.listTableData()
      })
    },
    search() {
      this.page = 1
      this.listTableData()
    },
    resetSearch() {
      this.filters.name = ''
      this.filters.code = ''
      this.filters.uri = ''
      this.filters.level = ''
      this.filters.status = ''
      this.filters.logShowed = ''

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
      this.$mapi.router.pageRouterList(param).then(res => {
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
    changeLogShowStatus(data, val) {
      this.$confirm('此操作将' + this.showedStatus[val] + '[' + data.method + ']' + data.uri + '接口的日志展示, 是否继续？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        const param = {
          uri: data.uri,
          method: data.method,
          logShowed: val
        }
        this.$mapi.router.changeLogShowStatus(param).then(_ => {
          this.$message.success(this.showedStatus[val] + '成功')
        }).catch(() => {
          data['logShowed'] = !data['logShowed']
        })
      }).catch(() => {
        data['logShowed'] = !data['logShowed']
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
    },
    hasPerm(value) {
      return this.checkPerm(value)
    }
  }
}
</script>

<style scoped>

</style>
