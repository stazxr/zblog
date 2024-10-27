<template>
  <div class="app-container">
    <div class="head-container">
      <div>
        <el-input v-model="filters.filename" clearable placeholder="文件名" style="width: 150px" class="filter-item" @keyup.enter.native="search" />
        <date-range-picker v-model="createTimeRange" type="daterange" start-placeholder="开始时间" end-placeholder="结束时间" />
        <span>
          <el-button class="filter-item" size="small" type="success" icon="el-icon-search" @click="search">查询</el-button>
          <el-button class="filter-item" size="small" type="warning" icon="el-icon-refresh-left" @click="resetSearch">重置</el-button>
        </span>
      </div>
      <div class="crud-opts">
        <span class="crud-opts-left">
          <el-button v-perm="['testUploadFile']" size="small" icon="el-icon-upload" type="primary" @click="uploadFile">
            上传
          </el-button>
          <el-button v-perm="['getStorageConfigInfo', 'setStorageConfigInfo']" size="small" icon="el-icon-s-tools" type="success" @click="doConfig">
            配置
          </el-button>
          <el-button v-perm="['testDeleteFile']" size="small" icon="el-icon-delete" type="danger" @click="batchDeleteFile">
            删除
          </el-button>
        </span>
      </div>
    </div>

    <div class="components-container">
      <el-table v-loading="tableLoading" :data="tableData" border style="width: 100%" @selection-change="handleSelectionChange">
        <el-table-column type="selection" :selectable="selectable" width="55" />
        <el-table-column :show-overflow-tooltip="true" prop="originalFilename" label="文件名" align="center">
          <template slot-scope="scope">
            <el-popover :content="scope.row['filePath']" placement="top-start" title="路径" width="200" trigger="hover">
              <a slot="reference" :href="scope.row['downloadUrl']" class="filenameLinkC el-link--primary" target="_blank">
                {{ scope.row['originalFilename'] }}
              </a>
            </el-popover>
          </template>
        </el-table-column>
        <el-table-column :show-overflow-tooltip="true" prop="downloadUrl" label="预览" align="center">
          <template slot-scope="scope">
            <el-image :src="scope.row['downloadUrl']" :preview-src-list="[scope.row['downloadUrl']]" fit="contain" lazy class="el-avatar">
              <div slot="error">
                <i class="el-icon-document" />
              </div>
            </el-image>
          </template>
        </el-table-column>
        <el-table-column :show-overflow-tooltip="true" prop="fileType" label="文件类型" align="center" />
        <el-table-column :show-overflow-tooltip="true" prop="size" label="大小" align="center" />
        <el-table-column :show-overflow-tooltip="true" prop="uploadType" label="文件状态" align="center">
          <template slot-scope="scope">
            <span v-if="scope.row['uploadType'] === '1'">普通文件</span>
            <span v-else-if="scope.row['uploadType'] === '2'">临时文件</span>
            <span v-else> - </span>
          </template>
        </el-table-column>
        <el-table-column :show-overflow-tooltip="true" prop="createUser" label="操作人" align="center" />
        <el-table-column :show-overflow-tooltip="true" prop="createTime" label="创建时间" align="center" />
        <el-table-column label="操作" align="center" width="150px">
          <template slot-scope="scope">
            <el-button-group>
              <el-button type="primary" size="mini" @click="download(scope.row)">下载</el-button>
              <el-popconfirm v-if="hasPerm(['testDeleteFile']) && scope.row['uploadType'] !== '1'" title="操作不可撤销，确定删除吗？" @confirm="deleteFile(scope.row)">
                <el-button slot="reference" type="danger" size="mini">删除</el-button>
              </el-popconfirm>
            </el-button-group>
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

    <upload-file-dialog ref="uploadDialogRef" :dialog-visible="uploadDialogVisible" @uploadDone="uploadDone" />
    <config ref="configDialogRef" :dialog-visible="configDialogVisible" @configDone="configDone" />
  </div>
</template>

<script>
import DateRangePicker from '@/components/DateRangePicker'
import uploadFileDialog from '@/views/admin/mnt/storage/compont/uploadFile'
import config from '@/views/admin/mnt/storage/aliyun/template/config'
import { downloadFile } from '@/utils'
export default {
  name: 'AliYunStorage',
  components: {
    DateRangePicker, uploadFileDialog, config
  },
  data() {
    return {
      storageType: 2,
      createTimeRange: ['', ''],
      filters: {
        filename: '',
        createStartTime: '',
        createEndTime: ''
      },
      selectRows: [],
      tableData: [],
      tableLoading: false,
      total: 0,
      page: 1,
      pageSize: 10,
      uploadDialogVisible: false,
      configDialogVisible: false
    }
  },
  watch: {
    createTimeRange(data) {
      this.filters.createStartTime = data[0]
      this.filters.createEndTime = data[1]
    }
  },
  methods: {
    initData() {
      this.$nextTick(() => {
        this.listTableData()
      })
    },
    listTableData() {
      const param = {
        ... this.filters,
        storageType: this.storageType,
        page: this.page,
        pageSize: this.pageSize
      }
      this.tableLoading = true
      this.$mapi.file.pageFileList(param).then(res => {
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
    search() {
      this.page = 1
      this.listTableData()
    },
    resetSearch() {
      this.eventTimeRange = ['', '']
      this.filters.filename = ''
      this.filters.createStartTime = ''
      this.filters.createEndTime = ''

      this.page = 1
      this.listTableData()
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
    selectable(row) {
      return row['uploadType'] !== '1'
    },
    handleSelectionChange(val) {
      this.selectRows = val
    },
    uploadFile() {
      this.uploadDialogVisible = true
      this.$refs.uploadDialogRef.initData(this.storageType)
    },
    uploadDone(result = false) {
      this.uploadDialogVisible = false
      if (result) {
        this.listTableData()
      }
    },
    doConfig() {
      this.configDialogVisible = true
      this.$refs.configDialogRef.initData(this.storageType)
    },
    configDone() {
      this.configDialogVisible = false
    },
    batchDeleteFile() {
      const fileIds = []
      this.selectRows.forEach(row => {
        fileIds.push(row.id)
      })
      this.doDelete(fileIds)
    },
    deleteFile(row) {
      const fileIds = []
      fileIds.push(row.id)
      this.doDelete(fileIds)
    },
    doDelete(fileIds) {
      if (fileIds.length === 0) {
        this.$message.warning('请选择要删除的文件列表')
        return
      }

      this.$mapi.file.testDeleteFile(fileIds).then(res => {
        this.$message.success(res.message)
        this.listTableData()
      })
    },
    download(row) {
      const param = {
        fileId: row.id,
        isDown: false
      }
      this.$mapi.file.downloadFile(param).then(res => {
        downloadFile(res, row['originalFilename'])
      })
    },
    hasPerm(value) {
      return this.checkPerm(value)
    }
  }
}
</script>

<style scoped>
.filenameLinkC {
  word-break: keep-all;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
  color: #1890ff;
  font-size: 13px;
}
.head-container .el-date-editor {
  width: 230px !important;
}
</style>
