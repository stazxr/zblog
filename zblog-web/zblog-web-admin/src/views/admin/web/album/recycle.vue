<template>
  <div class="card-container">
    <el-card class="main-card">
      <div class="card-title">{{ this.$route.meta.title || this.$route.name }}</div>

      <div class="album-info">
        <div class="operation">
          <div class="all-check">
            <el-checkbox v-model="checkAll" :indeterminate="isIndeterminate" @change="handleCheckAllChange">
              全选
            </el-checkbox>
            <div class="check-count">已选择{{ selectedItem.length }}张</div>
          </div>
          <el-button v-perm="['recoverAlbumPhoto']" :disabled="selectedItem.length === 0" icon="el-icon-document" size="small" type="success" @click="recoverPhotoDialogVisible = true">
            批量恢复
          </el-button>
          <el-button v-perm="['deleteAlbumPhotoForever']" :disabled="selectedItem.length === 0" icon="el-icon-delete" size="small" type="danger" @click="batchDeletePhotoDialogVisible = true">
            永久删除
          </el-button>
        </div>
      </div>

      <el-row v-loading="tableLoading" class="photo-container" :gutter="10">
        <el-empty v-if="tableData.length === 0" description="暂无照片" />
        <el-checkbox-group v-model="selectedItem" @change="handleCheckedPhotoChange">
          <el-col v-for="item of tableData" :key="item.id" :md="4">
            <el-checkbox :label="item.id">
              <el-image fit="cover" class="photo-img" :src="item['photoLink']" />
              <div class="photo-name">{{ item['createDate'] }}</div>
            </el-checkbox>
          </el-col>
        </el-checkbox-group>
      </el-row>

      <!-- 分页 -->
      <el-pagination
        :hide-on-single-page="true"
        :current-page="page"
        :page-size="pageSize"
        :total="total"
        layout="prev, pager, next"
        class="photo-operation"
        @size-change="sizeChange"
        @current-change="pageChange"
      />

      <el-dialog :visible.sync="recoverPhotoDialogVisible" width="30%">
        <div slot="title" class="dialog-title-container">
          <i class="el-icon-warning" style="color: #ff9900;" />提示
        </div>
        <div style="font-size: 1rem;">是否恢复选中照片？</div>
        <div slot="footer">
          <el-button @click="recoverPhotoDialogVisible = false">取 消</el-button>
          <el-button type="primary" @click="batchRecoverPhoto()">确 定</el-button>
        </div>
      </el-dialog>

      <el-dialog :visible.sync="batchDeletePhotoDialogVisible" width="30%">
        <div slot="title" class="dialog-title-container">
          <i class="el-icon-warning" style="color: #ff9900;" />提示
        </div>
        <div style="font-size: 1rem;">是否永久删除选中照片？</div>
        <div slot="footer">
          <el-button @click="batchDeletePhotoDialogVisible = false">取 消</el-button>
          <el-button type="primary" @click="batchDeletePhoto()">确 定</el-button>
        </div>
      </el-dialog>
    </el-card>
  </div>
</template>

<script>
import { mapGetters } from 'vuex'

export default {
  name: 'AlbumRecycle',
  data() {
    return {
      checkAll: false,
      isIndeterminate: false,
      selectedItem: [],
      tableData: [],
      photoIdList: [],
      tableLoading: false,
      total: 0,
      page: 1,
      pageSize: 18,
      batchDeletePhotoDialogVisible: false,
      recoverPhotoDialogVisible: false
    }
  },
  computed: {
    ...mapGetters([
      'user'
    ])
  },
  watch: {
    tableData() {
      this.photoIdList = []
      this.tableData.forEach(item => {
        this.photoIdList.push(item.id)
      })
    }
  },
  created() {
    this.listTableData()
  },
  methods: {
    hasPerm(value) {
      return this.checkPerm(value)
    },
    listTableData() {
      const param = {
        page: this.page,
        pageSize: this.pageSize
      }
      this.tableLoading = true
      this.$mapi.album.pageDeletePhotoList(param).then(res => {
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
    handleCheckAllChange(value) {
      this.selectedItem = value ? this.photoIdList : []
      this.isIndeterminate = false
    },
    handleCheckedPhotoChange(value) {
      const checkedCount = value.length
      this.checkAll = checkedCount === this.photoIdList.length
      this.isIndeterminate = checkedCount > 0 && checkedCount < this.photoIdList.length
    },
    batchRecoverPhoto() {
      if (this.selectedItem.length === 0) {
        this.$message.error('请选择需要恢复的照片')
        return
      }

      this.doRecoverPhoto(this.selectedItem)
    },
    doRecoverPhoto(photoIdList) {
      this.$mapi.album.recoverAlbumPhoto({ photoIdList: photoIdList }).then(_ => {
        this.listTableData()
      }).finally(_ => {
        if (this.recoverPhotoDialogVisible) {
          this.recoverPhotoDialogVisible = false
        }
      })
    },
    batchDeletePhoto() {
      if (this.selectedItem.length === 0) {
        this.$message.error('请选择需要删除的照片')
        return
      }

      this.doDeletePhoto(this.selectedItem)
    },
    doDeletePhoto(photoIdList) {
      this.$mapi.album.deleteAlbumPhotoForever({ photoIdList: photoIdList }).then(_ => {
        this.listTableData()
      }).finally(_ => {
        if (this.batchDeletePhotoDialogVisible) {
          this.batchDeletePhotoDialogVisible = false
        }
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
::v-deep .photo-container .el-checkbox__input {
  position: absolute !important;
  top: 0.3rem;
  left: 0.8rem;
}

.album-info {
  display: flex;
  margin-top: 2.25rem;
  margin-bottom: 2rem;
}
.album-cover {
  border-radius: 4px;
  width: 5rem;
  height: 5rem;
}
.album-check-cover {
  border-radius: 4px;
  width: 4rem;
  height: 4rem;
}
.album-detail {
  padding-top: 0.4rem;
  margin-left: 0.8rem;
}
.album-desc {
  font-size: 14px;
  margin-right: 0.8rem;
}
.operation {
  padding-top: 1.5rem;
  margin-left: auto;
}
.all-check {
  display: inline-flex;
  align-items: center;
  margin-right: 1rem;
}
.check-count {
  margin-left: 1rem;
  font-size: 12px;
}
.album-name {
  font-size: 1.25rem;
}
.photo-count {
  font-size: 12px;
  margin-left: 0.5rem;
}
.photo-item {
  width: 100%;
  position: relative;
  cursor: pointer;
  margin-bottom: 1rem;
}
.photo-img {
  width: 100%;
  height: 7rem;
  border-radius: 4px;
}
.photo-name {
  font-size: 14px;
  margin-top: 0.3rem;
  text-align: center;
}
.photo-operation {
  position: absolute;
  z-index: 1000;
  top: 0.3rem;
  right: 0.5rem;
}
.album-check {
  display: flex;
  align-items: center;
}
</style>
