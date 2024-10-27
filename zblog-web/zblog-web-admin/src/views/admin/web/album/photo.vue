<template>
  <div class="card-container">
    <el-card class="main-card">
      <div class="card-title">{{ this.$route.meta.title || this.$route.name }}</div>
      <div class="album-info">
        <el-image fit="cover" class="album-cover" :src="albumData.albumCover" />
        <div class="album-detail">
          <div style="margin-bottom:0.6rem">
            <span class="album-name">{{ albumData.albumName }}</span>
            <span class="photo-count">{{ albumData.photoCount }}张</span>
          </div>
          <div>
            <span v-if="albumData.albumDesc" class="album-desc">
              {{ albumData.albumDesc }}
            </span>
            <el-button v-if="user.id === albumData.userId" v-perm="['saveAlbumPhoto']" icon="el-icon-picture" type="primary" size="small" @click="uploadPhotoDialogVisible = true">
              上传照片
            </el-button>
          </div>
        </div>
        <div class="operation">
          <div class="all-check">
            <el-checkbox v-model="checkAll" :indeterminate="isIndeterminate" @change="handleCheckAllChange">
              全选
            </el-checkbox>
            <div class="check-count">已选择{{ selectedItem.length }}张</div>
          </div>
          <el-button v-if="user.id === albumData.userId" v-perm="['moveAlbumPhoto']" :disabled="selectedItem.length === 0" icon="el-icon-document" size="small" type="success" @click="movePhotoDialogVisible = true">
            移动到
          </el-button>
          <el-button v-if="user.id === albumData.userId" v-perm="['deleteAlbumPhoto']" :disabled="selectedItem.length === 0" icon="el-icon-delete" size="small" type="danger" @click="batchDeletePhotoDialogVisible = true">
            批量删除
          </el-button>
        </div>
      </div>
      <el-row v-loading="tableLoading" class="photo-container" :gutter="10">
        <!-- 空状态 -->
        <el-empty v-if="tableData.length === 0" description="暂无照片" />
        <el-checkbox-group v-model="selectedItem" @change="handleCheckedPhotoChange">
          <el-col v-for="item of tableData" :key="item.id" :md="4" :sm="8" :xs="12">
            <el-checkbox :label="item.id">
              <div class="photo-item">
                <!-- 照片操作 -->
                <div class="photo-operation">
                  <el-dropdown v-if="user.id === albumData.userId && hasPerm(['deleteAlbumPhoto'])" @command="handleCommand">
                    <i class="el-icon-more" style="color:#fff" />
                    <el-dropdown-menu slot="dropdown">
                      <!-- <el-dropdown-item v-perm="['moveAlbumPhoto']" :command="'1,' + item.id">
                        <i class="el-icon-document" />移动
                      </el-dropdown-item> -->
                      <el-dropdown-item v-perm="['deleteAlbumPhoto']" :command="'2,' + item.id">
                        <i class="el-icon-delete" />删除
                      </el-dropdown-item>
                    </el-dropdown-menu>
                  </el-dropdown>
                </div>
                <el-image fit="cover" class="photo-img" :src="item['photoLink']" />
                <div class="photo-name">{{ item['createDate'] }}</div>
              </div>
            </el-checkbox>
          </el-col>
        </el-checkbox-group>
      </el-row>
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
    </el-card>

    <el-dialog :visible.sync="batchDeletePhotoDialogVisible" width="30%">
      <div slot="title" class="dialog-title-container">
        <i class="el-icon-warning" style="color: #ff9900;" />提示
      </div>
      <div style="font-size: 1rem;">是否删除选中照片？</div>
      <div slot="footer">
        <el-button @click="batchDeletePhotoDialogVisible = false">取 消</el-button>
        <el-button type="primary" @click="batchDeletePhoto()">
          确 定
        </el-button>
      </div>
    </el-dialog>

    <el-dialog :visible.sync="movePhotoDialogVisible" width="30%">
      <div slot="title" class="dialog-title-container">
        移动照片
      </div>
      <el-empty v-if="albumList.length < 2" description="暂无其他相册" />
      <el-form v-else label-width="80px" size="medium">
        <el-radio-group v-model="moveAlbumId">
          <div class="album-check-item">
            <template v-for="item of albumList">
              <el-radio v-if="item.id !== albumData.id" :key="item.id" :label="item.id" style="margin-bottom: .5rem;display: block;">
                <div class="album-check">
                  <el-image fit="cover" class="album-check-cover" :src="item.albumCover" />
                  <div style="margin-left:0.5rem">{{ item.albumName }}</div>
                </div>
                <br>
              </el-radio>
            </template>
          </div>
        </el-radio-group>
      </el-form>
      <div slot="footer">
        <el-button @click="movePhotoDialogVisible = false">取 消</el-button>
        <el-button :disabled="albumId == null" type="primary" @click="batchMoveAlbumPhoto">
          确 定
        </el-button>
      </div>
    </el-dialog>

    <!-- 照片上传 -->
    <uploadPhotoDialog
      ref="uploadPhotoDialogRef"
      :dialog-visible="uploadPhotoDialogVisible"
      @uploadPhotoDone="uploadPhotoDone"
    />
  </div>
</template>

<script>
import uploadPhotoDialog from '@/views/admin/web/album/template/uploadPhotoDialog'
import { mapGetters } from 'vuex'
export default {
  name: 'AlbumPhoto',
  components: {
    uploadPhotoDialog
  },
  data() {
    return {
      albumId: '',
      moveAlbumId: '',
      albumData: {
        id: '',
        userId: '',
        albumName: '',
        albumDesc: '',
        albumCover: '',
        photoCount: '',
        status: ''
      },
      albumList: [],
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
      movePhotoDialogVisible: false,
      uploadPhotoDialogVisible: false
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
    this.albumId = this.$route.query.albumId
    this.getAlbumInfo()
    this.listTableData()
    this.listAlbum()
  },
  methods: {
    hasPerm(value) {
      return this.checkPerm(value)
    },
    getAlbumInfo() {
      this.$mapi.album.queryAlbumDetail({ albumId: this.albumId }).then(res => {
        const { data } = res
        Object.keys(this.albumData).forEach(key => {
          this.albumData[key] = data[key]
        })
      })
    },
    listAlbum() {
      this.$mapi.album.queryUserAlbumList({ loginUserId: this.user.id }).then(({ data }) => {
        this.albumList = data
      }).catch(_ => {
        this.albumList = []
      })
    },
    listTableData() {
      const param = {
        albumId: this.albumId,
        page: this.page,
        pageSize: this.pageSize
      }
      this.tableLoading = true
      this.$mapi.album.pagePhotoList(param).then(res => {
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
    handleCommand(command) {
      const arr = command.split(',')
      switch (arr[0]) {
        case '1':
          this.moveAlbumPhoto(arr[1])
          break
        case '2':
          this.deleteAlbumPhoto(arr[1])
          break
      }
    },
    uploadPhotoDone(result) {
      this.uploadPhotoDialogVisible = false
      if (result) {
        this.listTableData()
      }
    },
    deleteAlbumPhoto(photoId) {
      if (photoId == null || photoId === '') {
        this.$message.error('请选择需要删除的照片')
        return
      }

      this.$confirm('是否删除选中照片？').then(_ => {
        const photoIdList = []
        photoIdList.push(photoId)
        this.doDeletePhoto(photoIdList)
      }).catch(_ => {})
    },
    batchDeletePhoto() {
      if (this.selectedItem.length === 0) {
        this.$message.error('请选择需要删除的照片')
        return
      }

      this.doDeletePhoto(this.selectedItem)
    },
    doDeletePhoto(photoIdList) {
      this.$mapi.album.deleteAlbumPhoto({ photoIdList: photoIdList }).then(_ => {
        this.listTableData()
      }).finally(_ => {
        if (this.batchDeletePhotoDialogVisible) {
          this.batchDeletePhotoDialogVisible = false
        }
      })
    },
    moveAlbumPhoto(photoId) {
    },
    batchMoveAlbumPhoto() {
      if (this.selectedItem.length === 0) {
        this.$message.error('请选择需要移动的照片')
        return
      }

      if (this.moveAlbumId == null || this.moveAlbumId === '') {
        this.$message.error('请选择需要移动到的相册')
        return
      }

      this.doMoveAlbumPhoto(this.selectedItem)
    },
    doMoveAlbumPhoto(photoIdList) {
      this.$mapi.album.moveAlbumPhoto({ albumId: this.moveAlbumId, photoIdList: photoIdList }).then(_ => {
        this.listTableData()
      }).finally(_ => {
        if (this.movePhotoDialogVisible) {
          this.movePhotoDialogVisible = false
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

::v-deep .album-check-item .el-radio__input {
  position: absolute !important;
  top: 1.2rem;
  left: 0.2rem;
  z-index: 1000;
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
