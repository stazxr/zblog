<template>
  <div>
    <el-table v-loading="tableLoading" :data="tableData" border style="width: 100%;">
      <el-table-column type="expand">
        <template slot-scope="props">
          <el-form label-position="left" inline class="demo-table-expand">
            <el-form-item class="el-form-item" label="日志序号:">
              <span>{{ props.row['id'] }}</span>
            </el-form-item>
            <el-form-item class="el-form-item" label="日志类型:">
              <span v-if="props.row['logType'] === 1">操作日志</span>
              <span v-else-if="props.row['logType'] === 2">接口日志</span>
              <span v-else-if="props.row['logType'] === 3">异常日志</span>
              <span v-else> - </span>
            </el-form-item>
            <el-form-item class="el-form-item" label="请求地址:">
              <span>{{ props.row['requestIp'] }}</span>
            </el-form-item>
            <el-form-item class="el-form-item" label="请求来源:">
              <span>{{ props.row['address'] }}</span>
            </el-form-item>
            <el-form-item class="el-form-item" label="请求接口:">
              <span>[{{ props.row['requestMethod'] }}] {{ props.row['requestUri'] }}</span>
            </el-form-item>
            <el-form-item class="el-form-item" label="操作环境:">
              <span>{{ props.row['browser'] }}</span>
            </el-form-item>
            <el-form-item class="el-form-item" label="描述信息:" style="width: 100%">
              <span>{{ props.row['execMessage'] }}</span>
            </el-form-item>
          </el-form>
        </template>
      </el-table-column>
      <el-table-column :show-overflow-tooltip="true" prop="operateUser" label="操作人" />
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
      <el-table-column :show-overflow-tooltip="true" align="right">
        <template slot="header">
          <div style="display:inline-block;float: right;cursor: pointer;white-space: nowrap" @click="listTableData">
            创建日期<i class="el-icon-refresh" style="margin-left: 40px" />
          </div>
        </template>
        <template slot-scope="scope">
          <i class="el-icon-time" />
          <span>{{ scope.row['eventTime'] }}</span>
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
  name: 'PermLogList',
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
        logType: 1,
        permId: this.permId,
        page: this.page,
        pageSize: this.pageSize
      }
      this.tableLoading = true
      this.$mapi.perm.queryPermLogs(param).then(res => {
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
</style>
