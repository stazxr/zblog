<template>
  <div class="app-container">
    <div class="head-container">
      <div class="search-opts">
        <muses-search-form ref="searchForm" :model="filters" label-position="right" label-width="0" :offset="0" :item-width="160">
          <muses-search-form-item label="" prop="search-pattern">
            <el-select
              id="search-pattern"
              v-model="filters.pattern"
              filterable
              allow-create
              default-first-option
              placeholder="查询关键字"
              @change="search"
            >
              <el-option v-for="item in patternList" :key="item.value" :label="item.name" :value="item.value" />
            </el-select>
          </muses-search-form-item>
          <muses-search-form-item btn btn-open-name="" btn-close-name="">
            <el-button type="success" @click="search()">查 询</el-button>
            <el-button type="warning" @click="resetSearch()">重 置</el-button>
          </muses-search-form-item>
        </muses-search-form>
      </div>
      <div class="crud-opts">
        <span class="crud-opts-left">
          <el-button v-perm="['CACHD001']" :disabled="row === null" type="danger" @click="deleteCache">删除</el-button>
        </span>
      </div>
    </div>
    <div class="components-container">
      <el-table
        ref="cacheTable"
        v-loading="tableLoading"
        :data="tableData"
        :header-cell-style="{background:'#FAFAFA'}"
        highlight-current-row
        row-key="key"
        border
        @current-change="handleCurrentChange"
      >
        <el-table-column :show-overflow-tooltip="true" prop="accessTokenId" label="会话ID" />
        <el-table-column :show-overflow-tooltip="true" prop="username" label="登录用户名" width="200" />
        <el-table-column :show-overflow-tooltip="true" prop="loginTime" label="登录时间" width="200" />
        <el-table-column :show-overflow-tooltip="true" prop="lastAccessTime" label="上次访问时间" width="200" />
        <el-table-column :show-overflow-tooltip="true" prop="renewTime" label="续签时间" width="200" />
        <div slot="empty">
          <el-empty :image="nodataImg" description=" " />
        </div>
      </el-table>
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
        pattern: null
      },
      patternList: [],
      tableData: [],
      tableLoading: false,
      nodataImg: nodataImg,
      row: null
    }
  },
  mounted() {
    this.loadPatternList()
    this.listTableData()
  },
  methods: {
    handleCurrentChange(row) {
      this.row = row
    },
    toggleValue(row) {
      row.showValue = !row.showValue
    },
    loadPatternList() {
      this.$mapi.communal.queryConfListByDictKey({ dictKey: 'CACHE_PATTERN_CONFIG' }).then(res => {
        const { data } = res
        this.patternList = data
      }).catch(_ => {
        this.patternList = []
      })
    },
    // 查询
    search() {
      this.listTableData()
    },
    resetSearch() {
      Object.keys(this.filters).forEach(key => { this.filters[key] = null })
      this.listTableData()
    },
    listTableData() {
      const param = {
        ... this.filters
      }
      this.tableLoading = true
      this.$mapi.session.list(param).then(res => {
        const { data } = res
        this.tableData = data
      }).catch(_ => {
        this.tableData = []
      }).finally(() => {
        this.tableLoading = false
        this.row = null
        this.$refs.cacheTable.setCurrentRow()
      })
    },
    // 删除
    deleteCache() {
      if (this.row === null) {
        this.$message.error('请选择要清理的缓存')
        return
      }
      this.$confirm('操作不可回退, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.$mapi.cache.delete({ key: this.row.key }).then(res => {
          this.$message.success(res.message)
          this.listTableData()
        })
      })
    }
  }
}
</script>

<style scoped>

</style>
