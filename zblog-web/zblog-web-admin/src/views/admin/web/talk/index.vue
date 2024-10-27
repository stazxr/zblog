<template>
  <div class="card-container">
    <el-card class="main-card">
      <div class="card-title">{{ this.$route.meta.title || this.$route.name }}</div>
      <div class="card-item">
        <span>状态</span>
        <span :class="isActive('')" @click="queryListByStatus('')">全部</span>
        <span :class="isActive('1')" @click="queryListByStatus('1')">公开</span>
        <span :class="isActive('2')" @click="queryListByStatus('2')">私密</span>
      </div>
      <el-empty v-if="tableData == null || tableData.length === 0" description="暂无说说" />
      <div v-for="item of tableData" :key="item.id" class="talk-item">
        <div class="user-info-wrapper">
          <el-avatar class="user-avatar" :src="item['userAvatar']" :size="36" />
          <div class="user-detail-wrapper">
            <div class="user-nickname">
              <div>{{ item['userNickname'] }}</div>
              <!-- 操作 -->
              <el-dropdown v-if="hasPerm(['addOrEditTalk', 'deleteTalk'])" trigger="click" @command="handleCommand">
                <i class="el-icon-more" style="color:#333;cursor: pointer;" />
                <el-dropdown-menu slot="dropdown">
                  <el-dropdown-item v-perm="['addOrEditTalk']" :command="'1,' + item.id">
                    <i class="el-icon-edit" />编辑
                  </el-dropdown-item>
                  <el-dropdown-item v-perm="['deleteTalk']" :command="'2,' + item.id">
                    <i class="el-icon-delete" />删除
                  </el-dropdown-item>
                </el-dropdown-menu>
              </el-dropdown>
            </div>
            <div class="talk-time">
              {{ item['createTime'] }}
              <span v-if="item['isTop']" class="talk-top">
                <i class="iconfont zhiding" /> 置顶
              </span>
              <span v-if="item.status === 2" class="talk-secret">
                <i class="iconfont mima" /> 私密
              </span>
            </div>
            <div class="talk-content" v-html="item['content']" />
            <el-row v-if="item['imagesList'] && item['imagesList'].length > 0" :gutter="4" class="talk-images">
              <el-col v-for="(img, index) of item['imagesList']" :key="index" :cols="6" :md="8">
                <el-image :src="img" :preview-src-list="item['imagesList']" class="images-items" />
              </el-col>
            </el-row>
          </div>
        </div>
      </div>
      <el-pagination
        :hide-on-single-page="false"
        :current-page="page"
        :page-size="pageSize"
        :total="total"
        layout="prev, pager, next"
        class="card-pagination"
        @size-change="sizeChange"
        @current-change="pageChange"
      />
    </el-card>

    <el-dialog :visible.sync="deleteVisible" width="30%">
      <div slot="title" class="dialog-title-container">
        <i class="el-icon-warning" style="color:#ff9900" />提示
      </div>
      <div style="font-size:1rem">是否删除该说说？</div>
      <div slot="footer">
        <el-button @click="deleteVisible = false">取 消</el-button>
        <el-button :loading="deleteLoading" type="primary" @click="deleteTalk">确 定</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
export default {
  name: 'Talk',
  data() {
    return {
      filter: {
        status: ''
      },
      deleteTalkId: '',
      deleteLoading: false,
      deleteVisible: false,
      tableData: [],
      total: 0,
      page: 1,
      pageSize: 5
    }
  },
  computed: {
    isActive() {
      return function(status) {
        return this.filter.status === status ? 'card-item-active-status' : 'card-item-status'
      }
    }
  },
  mounted() {
    this.listTableData()
  },
  methods: {
    hasPerm(value) {
      return this.checkPerm(value)
    },
    queryListByStatus(status) {
      this.page = 1
      this.filter.status = status
      this.listTableData()
    },
    listTableData() {
      const param = {
        ... this.filter,
        page: this.page,
        pageSize: this.pageSize
      }
      this.$mapi.talk.pageList(param).then(res => {
        const { data } = res
        this.tableData = data.list
        this.total = data.total
      }).catch(_ => {
        this.tableData = []
        this.total = 0
      })
    },
    handleCommand(command) {
      const arr = command.split(',')
      switch (arr[0]) {
        case '1':
          this.$router.push({
            name: 'AddOrEditTalk',
            query: {
              talkId: arr[1]
            }
          })
          break
        case '2':
          this.deleteTalkId = arr[1]
          this.deleteVisible = true
          break
      }
    },
    deleteTalk() {
      this.deleteLoading = true
      this.$mapi.talk.deleteTalk({ talkId: this.deleteTalkId }).then(res => {
        this.$message.success(res.message)
        this.listTableData()
      }).finally(_ => {
        this.deleteVisible = false
        this.deleteLoading = false
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
.talk-item:not(:first-child) {
  margin-top: 20px;
}
.talk-item {
  padding: 16px 20px;
  border-radius: 10px;
  background: rgba(255, 255, 255, 0.1);
  box-shadow: 0 3px 8px 6px rgb(232 232 232);
  transition: all 0.3s ease 0s;
}
.talk-item:hover {
  box-shadow: 0 5px 10px 8px rgb(207 207 207);
  transform: translateY(-3px);
}
.user-info-wrapper {
  width: 100%;
  display: flex;
}
.user-detail-wrapper {
  margin-left: 10px;
  width: 100%;
}
.user-nickname {
  font-size: 15px;
  font-weight: bold;
  display: flex;
  justify-content: space-between;
}
.user-sign {
  margin-left: 4px;
}
.talk-time {
  color: #999;
  margin-top: 2px;
  font-size: 12px;
}
.talk-top {
  color: #ff7242;
  margin-left: 10px;
}
.talk-secret {
  color: #999;
  margin-left: 10px;
}
.talk-content {
  margin-top: 8px;
  font-size: 14px;
  white-space: pre-line;
  word-wrap: break-word;
  word-break: break-all;
}
.talk-images {
  margin-top: 8px;
}
.images-items {
  cursor: pointer;
  object-fit: cover;
  height: 200px;
  width: 100%;
  border-radius: 4px;
}
</style>
