<template>
  <div class="app-container">
    <div class="head-container">
      <div class="search-opts">
        <muses-search-form ref="searchForm" :model="filters" label-position="right" label-width="0" :offset="0" :item-width="160">
          <muses-search-form-item label="" prop="search-username">
            <el-input id="search-username" v-model="filters.username" clearable placeholder="登录用户" @keyup.enter.native="search" />
          </muses-search-form-item>
          <muses-search-form-item label="" prop="search-loginResult">
            <el-select id="search-execResult" v-model="filters.loginResult" placeholder="登录结果" clearable @change="search">
              <el-option v-for="item in loginResultList" :key="item.value" :label="item.name" :value="item.value" />
            </el-select>
          </muses-search-form-item>
          <muses-search-form-item label="" prop="search-loginType">
            <el-select id="search-loginType" v-model="filters.loginType" placeholder="登录方式" clearable @change="search">
              <el-option v-for="item in loginTypeList" :key="item.value" :label="item.name" :value="item.value" />
            </el-select>
          </muses-search-form-item>
          <muses-search-form-item label="" prop="search-loginAddress">
            <el-input id="search-loginAddress" v-model="filters.loginAddress" clearable placeholder="登录地址" @keyup.enter.native="search" />
          </muses-search-form-item>
          <muses-search-form-item label="" prop="search-loginIp">
            <el-input id="search-loginIp" v-model="filters.loginIp" clearable placeholder="登录IP" @keyup.enter.native="search" />
          </muses-search-form-item>
          <muses-search-form-item label="" prop="search-loginStartTime">
            <el-date-picker id="search-loginStartTime" v-model="filters.loginStartTime" type="datetime" placeholder="开始时间" value-format="yyyy-MM-dd HH:mm:ss" default-time="00:00:00" />
          </muses-search-form-item>
          <muses-search-form-item label="" prop="search-loginEndTime">
            <el-date-picker id="search-loginEndTime" v-model="filters.loginEndTime" type="datetime" placeholder="结束时间" value-format="yyyy-MM-dd HH:mm:ss" default-time="23:59:59" />
          </muses-search-form-item>
          <muses-search-form-item btn btn-open-name="" btn-close-name="">
            <el-button type="success" @click="search()">查 询</el-button>
            <el-button type="warning" @click="resetSearch()">重 置</el-button>
          </muses-search-form-item>
        </muses-search-form>
      </div>
      <div class="crud-opts">
        <span class="crud-opts-left">
          <el-button v-perm="['LOGLE001']" type="primary" :loading="exportLoading" @click="exportData">导出</el-button>
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
        <el-table-column :show-overflow-tooltip="true" prop="username" label="登录用户" width="110" align="center" />
        <el-table-column :show-overflow-tooltip="true" prop="loginTime" label="登录时间" width="140" align="center" />
        <el-table-column :show-overflow-tooltip="true" prop="loginIp" label="登录IP" width="130" align="center" />
        <el-table-column :show-overflow-tooltip="true" prop="loginAddress" label="登录地址" width="130" align="center" />
        <el-table-column :show-overflow-tooltip="true" prop="loginPlatform" label="登录平台" width="110" align="center" />
        <el-table-column :show-overflow-tooltip="true" prop="loginType" label="登录方式" align="center" width="110">
          <template v-slot="scope">
            <span v-if="scope.row['loginType'] === 'LT00'">访客</span>
            <span v-else-if="scope.row['loginType'] === 'LT01'">密码</span>
            <span v-else-if="scope.row['loginType'] === 'LT02'">QQ互信</span>
            <span v-else-if="scope.row['loginType'] === 'LT99'">未知</span>
            <span v-else>{{ scope.row['loginType'] }}</span>
          </template>
        </el-table-column>
        <el-table-column :show-overflow-tooltip="true" prop="loginBrowser" label="浏览器" width="110" align="center" />
        <el-table-column :show-overflow-tooltip="true" prop="loginBrowserVersion" label="浏览器版本" width="110" align="center" />
        <el-table-column :show-overflow-tooltip="true" prop="isSuccess" label="登录结果" width="110" align="center">
          <template v-slot="scope">
            <el-tag v-if="scope.row.isSuccess" type="success">成功</el-tag>
            <el-tag v-else type="danger">失败</el-tag>
          </template>
        </el-table-column>
        <el-table-column :show-overflow-tooltip="true" prop="remark" label="备注" align="center" />
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
  </div>
</template>

<script>
import { downloadFile } from '@/utils'
import nodataImg from '@/assets/images/nodata.png'
export default {
  name: 'LoginLog',
  data() {
    return {
      filters: {
        username: null,
        loginResult: null,
        loginType: null,
        loginAddress: null,
        loginIp: null,
        loginStartTime: null,
        loginEndTime: null
      },
      loginResultList: [],
      loginTypeList: [],
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
    this.loadLoginResultList()
    this.loadLoginTypeList()
    this.listTableData()
  },
  methods: {
    hasPerm(value) {
      return this.checkPerm(value)
    },
    loadLoginResultList() {
      this.$mapi.communal.queryConfListByDictKey({ dictKey: 'LOG_EXEC_RESULT_CONFIG' }).then(res => {
        const { data } = res
        this.loginResultList = data
      }).catch(_ => {
        this.loginResultList = []
      })
    },
    loadLoginTypeList() {
      this.$mapi.communal.queryConfListByDictKey({ dictKey: 'LOGIN_TYPE_CONFIG' }).then(res => {
        const { data } = res
        this.loginTypeList = data
        console.log('this.loginTypeList', this.loginTypeList)
      }).catch(_ => {
        this.loginTypeList = []
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
      this.$mapi.log.pageUserLoginLogList(param).then(res => {
        const { data } = res
        this.total = data.total
        this.tableData = data.records
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
    // 导出
    exportData() {
      this.$confirm(`确认导出登录日志列表吗?`, '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'primary'
      }).then(() => {
        this.exportLoading = true
        this.$mapi.log.exportUserLoginLog({ ... this.filters }).then(res => {
          downloadFile(res, '登录日志列表.xlsx')
        }).finally(_ => {
          this.exportLoading = false
        })
      })
    }
  }
}
</script>

<style scoped>
.head-container .search-opts .el-date-editor {
  width: 170px !important;
}
</style>
