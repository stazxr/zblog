<template>
  <div>
    <div>
      <muses-search-form ref="searchForm" :model="filters" label-position="right" label-width="0" :offset="0" :item-width="160">
        <muses-search-form-item label="" prop="search-description">
          <el-input id="search-description" v-model="filters.description" clearable placeholder="操作描述" @keyup.enter.native="search" />
        </muses-search-form-item>
        <muses-search-form-item label="" prop="search-execResult">
          <el-select id="search-execResult" v-model="filters.execResult" placeholder="操作结果" clearable>
            <el-option v-for="item in execResultList" :key="item.value" :label="item.name" :value="item.value" />
          </el-select>
        </muses-search-form-item>
        <muses-search-form-item label="" prop="search-eventStartTime">
          <el-date-picker id="search-eventStartTime" v-model="filters.eventStartTime" type="datetime" placeholder="开始时间" value-format="yyyy-MM-dd HH:mm:ss" default-time="00:00:00" />
        </muses-search-form-item>
        <muses-search-form-item label="" prop="search-eventEndTime">
          <el-date-picker id="search-eventEndTime" v-model="filters.eventEndTime" type="datetime" placeholder="结束时间" value-format="yyyy-MM-dd HH:mm:ss" default-time="23:59:59" />
        </muses-search-form-item>
        <muses-search-form-item btn btn-open-name="" btn-close-name="">
          <el-button type="success" @click="search()">查 询</el-button>
          <el-button type="warning" @click="resetSearch()">重 置</el-button>
        </muses-search-form-item>
      </muses-search-form>
    </div>
    <el-table
      ref="userLogTable"
      v-loading="tableLoading"
      :data="tableData"
      :header-cell-style="{background:'#FAFAFA'}"
      highlight-current-row
      border
    >
      <el-table-column :show-overflow-tooltip="true" prop="id" label="日志ID" width="150" align="center" />
      <el-table-column :show-overflow-tooltip="true" prop="description" label="操作内容" align="center" />
      <el-table-column :show-overflow-tooltip="true" prop="address" label="操作来源" width="120" align="center" />
      <el-table-column :show-overflow-tooltip="true" prop="execResult" label="执行结果" width="120" align="center">
        <template v-slot="scope">
          <el-tag v-if="scope.row.execResult" type="success">成功</el-tag>
          <el-tag v-else type="danger">失败</el-tag>
        </template>
      </el-table-column>
      <el-table-column :show-overflow-tooltip="true" prop="execMessage" label="结果说明" align="center" />
      <el-table-column :show-overflow-tooltip="true" prop="eventTime" width="140" align="right">
        <template slot="header">
          <div style="display:inline-block;float: right;cursor: pointer;white-space: nowrap" @click="listTableData">
            操作时间<i class="el-icon-refresh" style="margin-left: 25px" />
          </div>
        </template>
        <template v-slot="scope">
          <span>{{ scope.row.eventTime }}</span>
        </template>
      </el-table-column>
      <div slot="empty">
        <el-empty :image="nodataImg" description=" " />
      </div>
    </el-table>
    <el-pagination
      :total="total"
      :current-page.sync="page"
      :page-size.sync="pageSize"
      :page-sizes="[10, 20, 30, 50]"
      layout="total, prev, pager, next, sizes"
      style="margin-top: 10px;"
      @size-change="handleSizeChange"
      @current-change="handlePageChange"
    />
  </div>
</template>

<script>
import nodataImg from '@/assets/images/nodata.png'
export default {
  name: 'UserOperateLog',
  data() {
    return {
      filters: {
        description: null,
        execResult: null,
        eventStartTime: null,
        eventEndTime: null
      },
      tableData: [],
      execResultList: [],
      tableLoading: false,
      nodataImg: nodataImg,
      total: 0,
      page: 1,
      pageSize: 10
    }
  },
  created() {
    this.loadExecResultList()
  },
  methods: {
    loadExecResultList() {
      this.$mapi.communal.queryConfListByDictKey({ dictKey: 'LOG_EXEC_RESULT_CONFIG' }).then(res => {
        const { data } = res
        this.execResultList = data
      }).catch(_ => {
        this.execResultList = []
      })
    },
    // 查询
    search() {
      this.page = 1
      this.listTableData()
    },
    resetSearch() {
      Object.keys(this.filters).forEach(key => { this.filters[key] = null })
      this.page = 1
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
      this.$mapi.userCenter.pageUserLogList(param).then(res => {
        const { data } = res
        this.total = data.total
        this.tableData = data.list
      }).catch(_ => {
        this.total = 0
        this.tableData = []
      }).finally(() => {
        this.tableLoading = false
      })
    }
  }
}
</script>

<style scoped>
.el-date-editor {
  width: 166px !important;
}
</style>
