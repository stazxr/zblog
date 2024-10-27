<template>
  <div class="app-container">
    <div class="head-container">
      <div>
        <el-input v-model="filters.description" clearable placeholder="操作描述" style="width: 150px" class="filter-item" @keyup.enter.native="search" />
        <el-input v-model="filters.username" clearable placeholder="操作用户" style="width: 150px" class="filter-item" @keyup.enter.native="search" />
        <el-input v-model="filters.costTime" type="number" clearable placeholder="请求耗时" style="width: 150px;" class="filter-item type-number" />
        <el-select v-model="filters.execResult" clearable placeholder="操作结果" style="width: 150px" class="filter-item">
          <el-option label="成功" value="true" />
          <el-option label="失败" value="false" />
        </el-select>
        <date-range-picker v-model="eventTimeRange" type="datetimerange" start-placeholder="开始时间" end-placeholder="结束时间" />
        <span>
          <el-button class="filter-item" size="small" type="success" icon="el-icon-search" @click="search">查询</el-button>
          <el-button class="filter-item" size="small" type="warning" icon="el-icon-refresh-left" @click="resetSearch">重置</el-button>
        </span>
      </div>
      <div class="crud-opts">
        <span class="crud-opts-left">
          <el-button v-perm="['exportOperateLog']" size="small" type="info" :loading="downloadLoading" @click="exportData">
            导出
          </el-button>
        </span>
      </div>
    </div>

    <div class="components-container">
      <el-table v-loading="tableLoading" :data="tableData" border style="width: 100%">
        <el-table-column type="expand">
          <template slot-scope="props">
            <el-form label-position="left" inline class="demo-table-expand">
              <el-form-item class="el-form-item" label="日志序号:" style="width: 100%">
                <span>{{ props.row['id'] }}</span>
              </el-form-item>
              <el-form-item class="el-form-item" label="日志类型:" style="width: 100%">
                <span v-if="props.row['logType'] === 1">操作日志</span>
                <span v-else-if="props.row['logType'] === 2">接口日志</span>
                <span v-else-if="props.row['logType'] === 3">异常日志</span>
                <span v-else> - </span>
              </el-form-item>
              <el-form-item class="el-form-item" label="请求接口:" style="width: 100%">
                <span>[{{ props.row['requestMethod'] }}] {{ props.row['requestUri'] }}</span>
              </el-form-item>
              <el-form-item class="el-form-item" label="请求参数:" style="width: 100%">
                <span>{{ props.row['requestParam'] }}</span>
              </el-form-item>
              <el-form-item class="el-form-item" label="描述信息:" style="width: 100%">
                <span>{{ props.row['execMessage'] }}</span>
              </el-form-item>
            </el-form>
          </template>
        </el-table-column>
        <el-table-column :show-overflow-tooltip="true" prop="description" label="操作描述" align="center" />
        <el-table-column :show-overflow-tooltip="true" prop="operateUser" label="操作用户" align="center" />
        <el-table-column :show-overflow-tooltip="true" prop="requestIp" label="请求IP" align="center" />
        <el-table-column :show-overflow-tooltip="true" prop="address" label="请求来源" align="center" />
        <el-table-column :show-overflow-tooltip="true" prop="browser" label="浏览器" align="center" />
        <el-table-column :show-overflow-tooltip="true" prop="execResult" label="请求结果" align="center">
          <template slot-scope="scope">
            <el-tag v-if="scope.row['logType'] === 3" size="small" type="danger">系统错误</el-tag>
            <el-tag v-else-if="!scope.row['execResult']" size="small" type="warning">失败</el-tag>
            <el-tag v-else size="small">成功</el-tag>
          </template>
        </el-table-column>
        <el-table-column :show-overflow-tooltip="true" prop="costTime" label="请求耗时" align="center">
          <template slot-scope="scope">
            <span v-if="scope.row['costTime'] === null"> - </span>
            <el-tag v-else-if="scope.row['costTime'] <= 500">{{ scope.row['costTime'] }}ms</el-tag>
            <el-tag v-else-if="scope.row['costTime'] <= 3000" type="warning">{{ scope.row['costTime'] }}ms</el-tag>
            <el-tag v-else type="danger">{{ scope.row['costTime'] }}ms</el-tag>
          </template>
        </el-table-column>
        <el-table-column :show-overflow-tooltip="true" prop="eventTime" label="请求时间" align="center" />
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
import { downloadFile } from '@/utils'
import DateRangePicker from '@/components/DateRangePicker'
export default {
  name: 'OperatorLog',
  components: {
    DateRangePicker
  },
  data() {
    return {
      eventTimeRange: ['', ''],
      filters: {
        description: '',
        username: '',
        execResult: '',
        costTime: '',
        eventStartTime: '',
        eventEndTime: ''
      },
      downloadLoading: false,
      tableData: [],
      tableLoading: false,
      total: 0,
      page: 1,
      pageSize: 10
    }
  },
  watch: {
    eventTimeRange(data) {
      this.filters.eventStartTime = data[0]
      this.filters.eventEndTime = data[1]
    }
  },
  mounted() {
    this.listTableData()
  },
  methods: {
    search() {
      this.page = 1
      this.listTableData()
    },
    resetSearch() {
      this.eventTimeRange = ['', '']
      this.filters.description = ''
      this.filters.username = ''
      this.filters.execResult = ''
      this.filters.costTime = ''
      this.filters.eventStartTime = ''
      this.filters.eventEndTime = ''

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
      this.$mapi.log.queryOperateLogsByPage(param).then(res => {
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
    exportData() {
      this.$confirm(`确认导出操作日志列表吗?`, '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.downloadLoading = true
        this.$mapi.log.exportOperateLog({ ... this.filters }).then(res => {
          downloadFile(res, '操作日志数据导出列表.xlsx')
        }).finally(_ => {
          this.downloadLoading = false
        })
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
.demo-table-expand {
  font-size: 0;
}
.demo-table-expand label {
  width: 100px;
  color: #99a9bf;
}
.demo-table-expand .el-form-item {
  padding-left: 10px;
  margin-right: 0;
  margin-bottom: 0;
  width: 50%;
}
.head-container .el-date-editor {
  width: 350px !important;
}
</style>
