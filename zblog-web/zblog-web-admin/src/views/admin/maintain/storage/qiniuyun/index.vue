<template>
  <div class="app-container" style="padding-top: 0; padding-left: 0">
    <div class="head-container">
      <div class="search-opts">
        <muses-search-form ref="searchForm" :model="filters" label-position="right" label-width="0" :offset="0" :item-width="165">
          <muses-search-form-item label="" prop="search-filename">
            <el-input id="search-filename" v-model="filters.filename" clearable placeholder="文件名称" @keyup.enter.native="search" />
          </muses-search-form-item>
          <muses-search-form-item label="" prop="search-fileMd5">
            <el-input id="search-fileMd5" v-model="filters.fileMd5" clearable placeholder="文件MD5" @keyup.enter.native="search" />
          </muses-search-form-item>
          <muses-search-form-item label="" prop="search-businessType">
            <el-select id="search-businessType" v-model="filters.businessType" placeholder="业务类型" clearable @change="search">
              <el-option label="用户头像" value="0" />
            </el-select>
          </muses-search-form-item>
          <muses-search-form-item label="" prop="search-hasBusiness">
            <el-select id="search-hasBusiness" v-model="filters.hasBusiness" placeholder="业务状态" clearable @change="search">
              <el-option label="有状态" :value="true" />
              <el-option label="无状态" :value="false" />
            </el-select>
          </muses-search-form-item>
          <muses-search-form-item label="" prop="search-createStartTime">
            <el-date-picker id="search-createStartTime" v-model="filters.createStartTime" type="datetime" placeholder="上传开始时间" value-format="yyyy-MM-dd HH:mm:ss" default-time="00:00:00" />
          </muses-search-form-item>
          <muses-search-form-item label="" prop="search-createEndTime">
            <el-date-picker id="search-createEndTime" v-model="filters.createEndTime" type="datetime" placeholder="上传结束时间" value-format="yyyy-MM-dd HH:mm:ss" default-time="23:59:59" />
          </muses-search-form-item>
          <muses-search-form-item btn btn-open-name="" btn-close-name="">
            <el-button type="success" @click="search()">查 询</el-button>
            <el-button type="warning" @click="resetSearch()">重 置</el-button>
          </muses-search-form-item>
        </muses-search-form>
      </div>
      <div class="crud-opts">
        <span class="crud-opts-left">
          <el-button v-perm="['FILEA002']" icon="el-icon-upload" type="primary" @click="uploadFile">上传</el-button>
          <el-button icon="el-icon-download" :disabled="row === null" type="primary" @click="downloadFile">下载</el-button>
          <el-button v-perm="['FILED002']" :disabled="row === null || row.businessId !== null" icon="el-icon-delete" type="danger" @click="deleteFile">删除</el-button>
        </span>
      </div>
    </div>
    <div class="components-container">
      <el-table
        ref="qiNiuYunFileTable"
        v-loading="tableLoading"
        :data="tableData"
        :header-cell-style="{background:'#FAFAFA'}"
        highlight-current-row
        row-key="id"
        border
        @current-change="handleCurrentChange"
      >
        <el-table-column :show-overflow-tooltip="true" prop="originalFilename" label="文件名" align="center">
          <template v-slot="scope">
            <el-popover :content="scope.row['fileAbsolutePath']" placement="top-start" title="路径" width="200" trigger="hover">
              <a slot="reference" :href="scope.row['downloadUrl']" class="filenameLinkC el-link--primary" target="_blank">
                {{ scope.row['originalFilename'] }}
              </a>
            </el-popover>
          </template>
        </el-table-column>
        <el-table-column :show-overflow-tooltip="true" prop="downloadUrl" label="预览" align="center" width="80px">
          <template v-slot="scope">
            <el-image
              v-if="scope.row.fileType.startsWith('image/')"
              :src="scope.row['downloadUrl']"
              :preview-src-list="[scope.row['downloadUrl']]"
              fit="cover"
              lazy
              style="width: 48px; height: 48px; border-radius: 4px;"
            >
              <!-- 加载中 -->
              <div slot="placeholder" class="image-slot">
                <i class="el-icon-loading" />
              </div>
              <!-- 加载失败 -->
              <div slot="error" class="image-slot">
                <i class="el-icon-picture-outline" />
              </div>
            </el-image>
            <span v-else>-</span>
          </template>
        </el-table-column>
        <el-table-column :show-overflow-tooltip="true" prop="fileType" label="文件类型" align="center" width="150px" />
        <el-table-column :show-overflow-tooltip="true" prop="fileSize" label="文件大小" align="center" width="100px" />
        <el-table-column :show-overflow-tooltip="true" prop="fileMd5" label="文件MD5" align="center" width="240px" />
        <el-table-column :show-overflow-tooltip="true" prop="businessType" label="业务类型" align="center" width="120px">
          <template v-slot="scope">
            <span v-if="scope.row['businessType'] === 0">用户头像</span>
            <span v-else-if="scope.row['businessType'] === 1">待定</span>
            <span v-else-if="scope.row['businessType'] === 2">待定</span>
            <span v-else-if="scope.row['businessType'] === 3">待定</span>
            <span v-else-if="scope.row['businessType'] === 4">待定</span>
            <span v-else />
          </template>
        </el-table-column>
        <el-table-column :show-overflow-tooltip="true" prop="createUsername" label="上传用户" align="center" width="100px" />
        <el-table-column :show-overflow-tooltip="true" prop="createTime" label="上传时间" align="center" width="140px" />
        <div slot="empty">
          <el-empty :image="nodataImg" description=" " />
        </div>
      </el-table>
      <div class="pagination-container">
        <el-pagination
          :total="total"
          :current-page.sync="page"
          :page-size.sync="pageSize"
          :page-sizes="[5, 10, 20, 30, 50]"
          layout="total, prev, pager, next, sizes"
          @size-change="handleSizeChange"
          @current-change="handlePageChange"
        />
      </div>
    </div>

    <!-- 图像上传 -->
    <upload-file-test-dialog
      ref="uploadFileDialogRef"
      :dialog-visible="uploadFileDialogVisible"
      @uploadDone="uploadFileDone"
    />
  </div>
</template>

<script>
import { downloadFile } from '@/utils'
import nodataImg from '@/assets/images/nodata.png'
import uploadFileTestDialog from '@/views/admin/maintain/storage/compont/uploadFileTestDialog'
export default {
  name: 'QiNiuStorage',
  components: {
    uploadFileTestDialog
  },
  data() {
    return {
      uploadType: 3,
      filters: {
        filename: null,
        fileMd5: null,
        businessType: null,
        hasBusiness: null,
        createStartTime: null,
        createEndTime: null
      },
      tableData: [],
      tableLoading: false,
      nodataImg: nodataImg,
      row: null,
      total: 0,
      page: 1,
      pageSize: 5,
      uploadFileDialogVisible: false
    }
  },
  methods: {
    initData() {
      this.$nextTick(() => {
        this.listTableData()
      })
    },
    handleCurrentChange(row) {
      this.row = row
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
        uploadType: this.uploadType,
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
        this.row = null
        this.$refs.qiNiuYunFileTable.setCurrentRow()
      })
    },
    // 文件上传
    uploadFile() {
      this.uploadFileDialogVisible = true
      this.$refs.uploadFileDialogRef.initData(this.uploadType)
    },
    uploadFileDone(result = false) {
      this.uploadFileDialogVisible = false
      if (result) {
        this.listTableData()
      }
    },
    // 文件下载
    downloadFile() {
      if (this.row === null) {
        this.$message.error('请选择要下载的文件')
        return
      }
      const param = {
        fileId: this.row.fileId,
        isDown: false
      }
      this.$mapi.file.downloadFile(param).then(res => {
        downloadFile(res, this.row['originalFilename'])
      })
    },
    // 文件删除
    deleteFile() {
      if (this.row === null) {
        this.$message.error('请选择要删除的文件')
        return
      }

      this.$confirm('此操作将删除文件【' + this.row['originalFilename'] + '】, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.$mapi.file.deleteFileTest({ fileId: this.row.fileId }).then(res => {
          this.$message.success(res.message)
          this.listTableData()
        })
      })
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
.head-container .search-opts .el-date-editor {
  width: 175px !important;
}
</style>
