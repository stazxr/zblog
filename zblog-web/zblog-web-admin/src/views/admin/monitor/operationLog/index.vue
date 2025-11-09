<template>
  <div class="app-container">
    <div class="head-container">
      <div class="search-opts">
        <muses-search-form ref="searchForm" :model="filters" label-position="right" label-width="0" :offset="0" :item-width="160">
          <muses-search-form-item label="" prop="search-description">
            <el-input id="search-description" v-model="filters.description" clearable placeholder="操作描述" @keyup.enter.native="search" />
          </muses-search-form-item>
          <muses-search-form-item label="" prop="search-interfaceCode">
            <el-input id="search-interfaceCode" v-model="filters.interfaceCode" clearable placeholder="接口编码" @keyup.enter.native="search" />
          </muses-search-form-item>
          <muses-search-form-item label="" prop="search-username">
            <el-input id="search-username" v-model="filters.username" clearable placeholder="操作用户" @keyup.enter.native="search" />
          </muses-search-form-item>
          <muses-search-form-item label="" prop="search-costTime">
            <el-input id="search-costTime" v-model="filters.costTime" type="number" clearable placeholder="请求耗时" @keyup.enter.native="search" />
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
      <div class="crud-opts">
        <span class="crud-opts-left">
          <el-button v-perm="['LOGID001']" type="danger" :loading="deleteLoading" @click="deleteLog">删除</el-button>
          <el-button v-perm="['LOGIE001']" type="primary" :loading="exportLoading" @click="exportData">导出</el-button>
        </span>
      </div>
    </div>
    <div class="components-container">
      <el-table
        ref="interfaceLogTable"
        v-loading="tableLoading"
        :data="tableData"
        :header-cell-style="{background:'#FAFAFA'}"
        highlight-current-row
        row-key="id"
        border
      >
        <el-table-column type="expand">
          <template v-slot="props">
            <el-form label-position="left" inline class="my-table-expand">
              <el-form-item class="el-form-item" label="日志序号:" style="width: 100%">
                <span>{{ props.row['id'] }}</span>
              </el-form-item>
              <el-form-item class="el-form-item" label="日志类型:" style="width: 100%">
                <span v-if="props.row['logType'] === 1">接口日志</span>
                <span v-else-if="props.row['logType'] === 2">操作日志</span>
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
        <el-table-column :show-overflow-tooltip="true" prop="description" label="操作描述" align="left" width="200" />
        <el-table-column :show-overflow-tooltip="true" prop="interfaceCode" label="接口编码" align="left" width="120" />
        <el-table-column :show-overflow-tooltip="true" prop="operateUser" label="操作用户" align="center" width="120" />
        <el-table-column :show-overflow-tooltip="true" prop="requestIp" label="请求IP" align="center" width="120" />
        <el-table-column :show-overflow-tooltip="true" prop="address" label="请求来源" align="center" />
        <el-table-column :show-overflow-tooltip="true" prop="browser" label="浏览器" align="center" />
        <el-table-column :show-overflow-tooltip="true" prop="execResult" label="请求结果" align="center" width="100">
          <template v-slot="scope">
            <el-tag v-if="scope.row['execResult']" type="success">成功</el-tag>
            <el-link v-else type="danger" :disabled="!hasPerm('LOGOQ002')" @click="showDetail(scope.row.id)">
              失败<i class="el-icon-view el-icon--right" />
            </el-link>
          </template>
        </el-table-column>
        <el-table-column :show-overflow-tooltip="true" prop="costTime" label="请求耗时" align="center" width="100">
          <template v-slot="scope">
            <span v-if="scope.row['costTime'] === null"> - </span>
            <el-tag v-else-if="scope.row['costTime'] <= 100">{{ scope.row['costTime'] }}ms</el-tag>
            <el-tag v-else-if="scope.row['costTime'] <= 1000" type="warning">{{ scope.row['costTime'] }}ms</el-tag>
            <el-tag v-else type="danger">{{ scope.row['costTime'] }}ms</el-tag>
          </template>
        </el-table-column>
        <el-table-column :show-overflow-tooltip="true" prop="eventTime" label="请求时间" align="center" width="160" />
        <div slot="empty">
          <el-empty :image="nodataImg" description=" " />
        </div>
      </el-table>
      <div class="pagination-container">
        <el-pagination
          :total="total"
          :current-page.sync="page"
          :page-size.sync="pageSize"
          :page-sizes="[10, 20, 30, 50]"
          layout="total, prev, pager, next, sizes"
          @size-change="handleSizeChange"
          @current-change="handlePageChange"
        />
      </div>
    </div>
    <!-- 展示异常堆栈信息 -->
    <detailDialog
      ref="detailDialogRef"
      :dialog-visible="detailDialogVisible"
      @showDetailDone="showDetailDone"
    />
  </div>
</template>

<script>
import { downloadFile } from '@/utils'
import detailDialog from '@/views/admin/monitor/operationLog/template/showLogDetail'
import nodataImg from '@/assets/images/nodata.png'
export default {
  name: 'OperationLog',
  components: {
    detailDialog
  },
  data() {
    return {
      filters: {
        description: null,
        interfaceCode: null,
        username: null,
        execResult: null,
        costTime: null,
        eventStartTime: null,
        eventEndTime: null
      },
      execResultList: [],
      tableData: [],
      tableLoading: false,
      nodataImg: nodataImg,
      total: 0,
      page: 1,
      pageSize: 10,
      detailDialogVisible: false,
      deleteLoading: false,
      exportLoading: false
    }
  },
  mounted() {
    this.loadExecResultList()
    this.listTableData()
  },
  methods: {
    hasPerm(value) {
      return this.checkPerm(value)
    },
    loadExecResultList() {
      this.$mapi.dict.queryConfListByDictKey({ dictKey: 'LOG_EXEC_RESULT_CONFIG' }).then(res => {
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
      this.$mapi.log.pageOperationLogList(param).then(res => {
        const { data } = res
        this.total = data.total
        this.tableData = data.list
      }).catch(_ => {
        this.total = 0
        this.tableData = []
      }).finally(() => {
        this.tableLoading = false
        this.row = null
        this.$refs.interfaceLogTable.setCurrentRow()
      })
    },
    // 展示异常堆栈信息
    showDetail(logId) {
      this.detailDialogVisible = true
      this.$refs.detailDialogRef.initData(logId)
    },
    showDetailDone() {
      this.detailDialogVisible = false
    },
    // 删除
    deleteLog() {
      this.$confirm('确认根据查询条件删除操作日志吗, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.deleteLoading = true
        this.$mapi.log.deleteOperationLog({ ... this.filters }).then(res => {
          this.$message.success(res.message)
          this.listTableData()
        }).finally(_ => {
          this.deleteLoading = false
        })
      })
    },
    // 导出
    exportData() {
      this.$confirm(`确认导出操作日志列表吗?`, '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'primary'
      }).then(() => {
        this.exportLoading = true
        this.$mapi.log.exportOperationLog({ ... this.filters }).then(res => {
          downloadFile(res, '操作日志列表.xlsx')
        }).finally(_ => {
          this.exportLoading = false
        })
      })
    }
  }
}
</script>

<style scoped>
.my-table-expand {
  font-size: 0;
}
.my-table-expand label {
  width: 100px;
  color: #99a9bf;
}
.my-table-expand .el-form-item {
  padding-left: 10px;
  margin-right: 0;
  margin-bottom: 0;
  width: 50%;
}
.head-container .search-opts .el-date-editor {
  width: 175px !important;
}
</style>
