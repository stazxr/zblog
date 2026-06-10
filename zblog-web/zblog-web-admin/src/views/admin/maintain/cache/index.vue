<template>
  <div class="app-container">
    <div class="head-container">
      <div class="search-opts">
        <muses-search-form ref="searchForm" :model="filters" label-position="right" label-width="0" :offset="0" :item-width="160">
          <muses-search-form-item label="" prop="search-pattern">
            <el-select
              id="search-pattern"
              v-model="pattern"
              filterable
              allow-create
              default-first-option
              placeholder="查询关键字"
              @change="listTableData"
            >
              <el-option v-for="item in patternList" :key="item.value" :label="item.name" :value="item.value" />
            </el-select>
          </muses-search-form-item>
          <muses-search-form-item>
            <el-input v-model="filters.key" placeholder="KEY" clearable @keyup.enter.native="search" />
          </muses-search-form-item>
          <muses-search-form-item btn btn-open-name="" btn-close-name="">
            <el-button type="success" @click="search()">查 询</el-button>
            <el-button type="warning" @click="resetSearch()">重 置</el-button>
          </muses-search-form-item>
        </muses-search-form>
      </div>
      <div class="crud-opts">
        <span class="crud-opts-left">
          <el-button v-perm="['CACHQ002']" type="success" :disabled="!row" @click="showCacheContent">查看内容</el-button>
          <el-button v-perm="['CACHD001']" type="danger" :disabled="!row" @click="deleteCache">删除缓存</el-button>
        </span>
      </div>
    </div>

    <div class="components-container">
      <!-- 表格 -->
      <el-table
        ref="cacheTable"
        v-loading="tableLoading"
        :data="pageData"
        :header-cell-style="{ background: '#FAFAFA' }"
        highlight-current-row
        row-key="key"
        border
        @current-change="handleCurrentChange"
        @sort-change="handleSortChange"
      >
        <el-table-column prop="key" label="KEY" show-overflow-tooltip />
        <el-table-column prop="type" label="Type" width="250" show-overflow-tooltip />
        <el-table-column prop="ttl" label="TTL" width="150" sortable="custom">
          <template v-slot="{ row }">
            {{ formatTtl(row.ttl) }}
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

    <!-- 详情 -->
    <detailDialog
      ref="detailDialogRef"
      :dialog-visible="detailDialogVisible"
      @showDetailDone="showDetailDone"
    />
  </div>
</template>

<script>
import nodataImg from '@/assets/images/nodata.png'
import detailDialog from '@/views/admin/maintain/cache/template/detailDialog'
export default {
  name: 'Cache',
  components: {
    detailDialog
  },
  data() {
    return {
      pattern: null,
      filters: {
        key: null
      },
      patternList: [],
      tableData: [],
      tableLoading: false,
      row: null,
      page: 1,
      pageSize: 10,
      sortField: '',
      sortOrder: '', // ascending / descending
      detailDialogVisible: false,
      nodataImg
    }
  },
  computed: {
    filteredData() {
      let data = this.tableData || []
      const { key } = this.filters
      if (key) {
        data = data.filter(i =>
          (i.key || '').includes(key)
        )
      }
      // 排序
      if (this.sortField) {
        const order = this.sortOrder
        data = data.slice().sort((a, b) => {
          const v1 = a[this.sortField]
          const v2 = b[this.sortField]
          if (order === 'ascending') return v1 > v2 ? 1 : -1
          if (order === 'descending') return v1 < v2 ? 1 : -1
          return 0
        })
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
    this.loadPatternList()
    this.listTableData()
  },
  methods: {
    loadPatternList() {
      this.$mapi.communal.queryConfListByDictKey({
        dictKey: 'CACHE_PATTERN_CONFIG'
      }).then(res => {
        this.patternList = res.data || []
      }).catch(() => {
        this.patternList = []
      })
    },
    handleCurrentChange(row) {
      this.row = row
    },
    handleSortChange({ prop, order }) {
      this.row = null
      this.$refs.cacheTable.setCurrentRow()
      this.sortField = prop
      this.sortOrder = order
    },
    // 查询
    search() {
      this.page = 1
      this.row = null
      this.$refs.cacheTable.setCurrentRow()
    },
    resetSearch() {
      this.page = 1
      this.pageSize = 10
      this.pattern = null
      Object.keys(this.filters).forEach(key => { this.filters[key] = null })
      this.listTableData()
    },
    handleSizeChange(size) {
      this.page = 1
      this.pageSize = size
      this.row = null
      this.$refs.cacheTable.setCurrentRow()
    },
    handlePageChange(page) {
      this.page = page
      this.row = null
      this.$refs.cacheTable.setCurrentRow()
    },
    listTableData() {
      this.tableLoading = true
      this.$mapi.cache.scan({ pattern: this.pattern }).then(res => {
        this.tableData = res.data || []
      }).catch(() => {
        this.tableData = []
      }).finally(() => {
        this.tableLoading = false
        this.row = null
        this.$refs.cacheTable.setCurrentRow()
      })
    },
    // 查看内容
    showCacheContent() {
      if (!this.row) {
        this.$message.error('请选择缓存对象')
        return
      }

      this.detailDialogVisible = true
      this.$refs.detailDialogRef.initData(this.row.key)
    },
    showDetailDone() {
      this.detailDialogVisible = false
    },
    deleteCache() {
      if (!this.row) {
        this.$message.error('请选择缓存对象')
        return
      }
      this.$confirm('是否确认删除缓存' + this.row.key + '？', '提示', {
        type: 'warning'
      }).then(() => {
        this.$mapi.cache.delete({ key: this.row.key }).then(res => {
          this.$message.success(res.message)
          this.listTableData()
        })
      })
    },
    formatTtl(ttl) {
      if (ttl == null) {
        return '-'
      }
      if (ttl === -1) {
        return '永久'
      }
      if (ttl <= 0) {
        return '已过期'
      }

      const seconds = Math.floor(ttl / 1000)
      if (seconds < 60) {
        return `${seconds}秒`
      }

      const minutes = Math.floor(seconds / 60)
      if (minutes < 60) {
        return `${minutes}分钟`
      }

      const hours = Math.floor(minutes / 60)
      if (hours < 24) {
        return `${hours}小时`
      }

      const days = Math.floor(hours / 24)
      return `${days}天`
    }
  }
}
</script>

<style scoped>

</style>
