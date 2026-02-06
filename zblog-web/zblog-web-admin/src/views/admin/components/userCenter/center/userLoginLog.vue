<template>
  <div>
    <div>
      <muses-search-form ref="searchForm" :model="filters" label-position="right" label-width="0" :offset="0" :item-width="165">
        <muses-search-form-item label="" prop="search-loginResult">
          <el-select id="search-loginResult" v-model="filters.loginResult" placeholder="登录结果" clearable>
            <el-option v-for="item in loginResultList" :key="item.value" :label="item.name" :value="item.value" />
          </el-select>
        </muses-search-form-item>
        <muses-search-form-item label="" prop="search-loginStartTime">
          <el-date-picker id="search-loginStartTime" v-model="filters.loginStartTime" type="datetime" placeholder="登录开始时间" value-format="yyyy-MM-dd HH:mm:ss" default-time="00:00:00" />
        </muses-search-form-item>
        <muses-search-form-item label="" prop="search-loginEndTime">
          <el-date-picker id="search-loginEndTime" v-model="filters.loginEndTime" type="datetime" placeholder="登录结束时间" value-format="yyyy-MM-dd HH:mm:ss" default-time="23:59:59" />
        </muses-search-form-item>
        <muses-search-form-item btn btn-open-name="" btn-close-name="">
          <el-button type="success" @click="search()">查 询</el-button>
          <el-button type="warning" @click="resetSearch()">重 置</el-button>
        </muses-search-form-item>
      </muses-search-form>
    </div>
    <el-table
      ref="userLoginLogTable"
      v-loading="tableLoading"
      :data="tableData"
      :header-cell-style="{background:'#FAFAFA'}"
      highlight-current-row
      border
    >
      <el-table-column :show-overflow-tooltip="true" prop="username" label="登录用户" width="110" align="center" />
      <el-table-column :show-overflow-tooltip="true" prop="loginTime" label="登录时间" width="140" align="center" />
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
  name: 'UserLoginLog',
  data() {
    return {
      filters: {
        loginResult: null,
        loginStartTime: null,
        loginEndTime: null
      },
      loginResultList: [],
      tableData: [],
      tableLoading: false,
      nodataImg: nodataImg,
      total: 0,
      page: 1,
      pageSize: 10
    }
  },
  created() {
    this.loadLoginResultList()
  },
  methods: {
    loadLoginResultList() {
      this.$mapi.communal.queryConfListByDictKey({ dictKey: 'LOG_EXEC_RESULT_CONFIG' }).then(res => {
        const { data } = res
        this.loginResultList = data
      }).catch(_ => {
        this.loginResultList = []
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
      this.$mapi.userCenter.pageUserLoginLogList(param).then(res => {
        const { data } = res
        this.total = data.total
        this.tableData = data.records
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
  width: 175px !important;
}
</style>
