<template>
  <div class="card-container">
    <el-card class="main-card">
      <div class="card-title">{{ this.$route.meta.title || this.$route.name }}</div>
      <div class="card-operation">
        <el-button v-perm="['addOrEditAlbum']" type="primary" size="small" icon="el-icon-plus" @click="addAlbum()">
          新建相册
        </el-button>
        <div style="margin-left:auto">
          <el-button type="text" size="small" icon="el-icon-delete" style="margin-right:1rem" @click="openAlbumDelete">
            回收站
          </el-button>
          <el-input v-model="filters.albumName" prefix-icon="el-icon-search" size="small" placeholder="请输入相册名" style="width:200px" @keyup.enter.native="searchAlbum" />
          <el-button type="primary" size="small" icon="el-icon-search" style="margin-left:1rem" @click="searchAlbum">
            搜索
          </el-button>
        </div>
      </div>
      <el-row v-loading="tableLoading" class="album-container" :gutter="12">
        <el-empty v-if="tableData == null || tableData.length === 0" description="暂无相册" />
        <el-col v-for="item of tableData" :key="item.id" :md="6">
          <div class="album-item" @click="openAlbumPhoto(item)">
            <div class="album-status" style="color: white;font-size: small;">
              <span v-if="item.status === 1">公开</span>
              <span v-else-if="item.status === 2">私密</span>
              <span v-else>-</span>
              ·
              {{ item['userNickname'] }}
            </div>
            <div class="album-operation">
              <el-dropdown v-if="user.id === '1' || (user.id === item['userId'] && hasPerm(['addOrEditTalk', 'deleteTalk']))" @command="handleCommand">
                <i class="el-icon-more" style="color:#fff" />
                <el-dropdown-menu slot="dropdown">
                  <el-dropdown-item v-perm="['addOrEditAlbum']" :command="'1,' + item.id">
                    <i class="el-icon-edit" />编辑
                  </el-dropdown-item>
                  <el-dropdown-item v-perm="['deleteAlbum']" :command="'2,' + item.id">
                    <i class="el-icon-delete" />删除
                  </el-dropdown-item>
                </el-dropdown-menu>
              </el-dropdown>
            </div>
            <div class="album-photo-count">
              <div>{{ item['photoCount'] }}</div>
              <!-- <i v-if="item.status === 2" class="iconfont mima" /> -->
            </div>
            <el-image fit="cover" class="album-cover" :src="item['albumCover']" />
            <div class="album-name">{{ item['albumName'] }}</div>
          </div>
        </el-col>
      </el-row>
      <el-pagination
        :hide-on-single-page="true"
        :current-page="page"
        :page-size="pageSize"
        :total="total"
        layout="prev, pager, next"
        class="card-pagination"
        @size-change="sizeChange"
        @current-change="pageChange"
      />
    </el-card>

    <!-- 新增 / 编辑 -->
    <addOrEditDialog
      ref="addOrEditDialogRef"
      :dialog-visible="addOrEditDialogVisible"
      :dialog-title="addOrEditDialogTitle"
      @addOrEditDone="addOrEditDone"
    />
  </div>
</template>

<script>
import addOrEditDialog from '@/views/admin/web/album/template/addOrEditDialog'
import { mapGetters } from 'vuex'
export default {
  name: 'Album',
  components: {
    addOrEditDialog
  },
  data() {
    return {
      filters: {
        albumName: ''
      },
      tableData: [],
      tableLoading: false,
      total: 0,
      page: 1,
      pageSize: 8,
      addOrEditDialogTitle: '',
      addOrEditDialogVisible: false
    }
  },
  computed: {
    ...mapGetters([
      'user'
    ])
  },
  mounted() {
    this.listTableData()
  },
  methods: {
    hasPerm(value) {
      return this.checkPerm(value)
    },
    handleCommand(command) {
      const arr = command.split(',')
      switch (arr[0]) {
        case '1':
          this.editAlbum(arr[1])
          break
        case '2':
          this.deleteAlbum(arr[1])
          break
      }
    },
    listTableData() {
      const param = {
        ... this.filters,
        page: this.page,
        pageSize: this.pageSize
      }
      this.tableLoading = true
      this.$mapi.album.pageList(param).then(res => {
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
    searchAlbum() {
      this.page = 1
      this.listTableData()
    },
    openAlbumPhoto(item) {
      this.$router.push({
        name: 'AlbumPhoto',
        query: {
          albumId: item.id
        }
      })
    },
    openAlbumDelete() {
      this.$router.push({ name: 'AlbumRecycle' })
    },
    addAlbum() {
      this.addOrEditDialogVisible = true
      this.addOrEditDialogTitle = '新增相册'
    },
    editAlbum(dataId) {
      this.addOrEditDialogVisible = true
      this.addOrEditDialogTitle = '编辑相册'
      this.$refs.addOrEditDialogRef.initData(dataId)
    },
    addOrEditDone(result = false) {
      this.addOrEditDialogVisible = false
      this.addOrEditDialogTitle = ''
      if (result) {
        this.listTableData()
      }
    },
    deleteAlbum(dataId) {
      this.$confirm('确认执行删除操作吗？').then(_ => {
        this.$mapi.album.deleteAlbum({ albumId: dataId }).then(res => {
          this.$message.success(res.message)
          this.listTableData()
        })
      }).catch(_ => {})
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
.album-cover {
  position: relative;
  border-radius: 4px;
  width: 100%;
  height: 170px;
}
.album-cover::before {
  content: "";
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: rgba(0, 0, 0, 0.3);
}
.album-photo-count {
  display: flex;
  align-items: center;
  justify-content: space-between;
  font-size: 1.5rem;
  z-index: 1000;
  position: absolute;
  left: 0;
  right: 0;
  padding: 0 0.5rem;
  bottom: 2.6rem;
  color: #fff;
}
.album-name {
  text-align: center;
  margin-top: 0.5rem;
}
.album-item {
  position: relative;
  cursor: pointer;
  margin-bottom: 1rem;
}
.album-status {
  position: absolute;
  z-index: 1000;
  top: 0.5rem;
  left: 0.8rem;
}
.album-operation {
  position: absolute;
  z-index: 1000;
  top: 0.5rem;
  right: 0.8rem;
}
</style>
