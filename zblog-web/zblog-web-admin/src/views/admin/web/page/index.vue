<template>
  <div class="card-container">
    <el-card class="main-card">
      <div class="card-title">{{ this.$route.meta.title || this.$route.name }}</div>
      <div class="card-operation">
        <el-button v-perm="['addOrEditPage']" type="primary" size="small" icon="el-icon-plus" @click="addPage()">
          新建页面
        </el-button>
      </div>
      <el-row v-loading="tableLoading" class="page-container" :gutter="12">
        <el-empty v-if="tableData.length === 0" description="暂无页面" />
        <el-col v-for="item of tableData" :key="item.id" :md="6">
          <div class="page-item">
            <!-- 相册操作 -->
            <div v-if="hasPerm(['addOrEditPage', 'deletePage'])" class="page-opreation">
              <el-dropdown @command="handleCommand">
                <i class="el-icon-more" style="color:#fff" />
                <el-dropdown-menu slot="dropdown">
                  <el-dropdown-item v-perm="['addOrEditPage']" :command="'1,' + item.id">
                    <i class="el-icon-edit" />编辑
                  </el-dropdown-item>
                  <el-dropdown-item v-perm="['deletePage']" :command="'2,' + item.id">
                    <i class="el-icon-delete" />删除
                  </el-dropdown-item>
                </el-dropdown-menu>
              </el-dropdown>
            </div>
            <el-image fit="cover" class="page-cover" :src="item['pageCover']" />
            <div class="page-name">{{ item['pageName'] }}</div>
          </div>
        </el-col>
      </el-row>
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
import addOrEditDialog from '@/views/admin/web/page/template/addOrEditDialog'
export default {
  name: 'Page',
  components: {
    addOrEditDialog
  },
  data() {
    return {
      tableData: [],
      tableLoading: false,
      addOrEditDialogTitle: '',
      addOrEditDialogVisible: false
    }
  },
  mounted() {
    this.listTableData()
  },
  methods: {
    hasPerm(value) {
      return this.checkPerm(value)
    },
    listTableData() {
      this.tableLoading = true
      this.$mapi.page.pageList().then(({ data }) => {
        this.tableData = data
      }).catch(_ => {
        this.tableData = []
      }).finally(() => {
        this.tableLoading = false
      })
    },
    handleCommand(command) {
      const arr = command.split(',')
      switch (arr[0]) {
        case '1':
          this.editPage(arr[1])
          break
        case '2':
          this.deletePage(arr[1])
          break
      }
    },
    addPage() {
      this.addOrEditDialogTitle = '新建页面'
      this.addOrEditDialogVisible = true
    },
    editPage(dataId) {
      this.addOrEditDialogVisible = true
      this.addOrEditDialogTitle = '编辑页面'
      this.$refs.addOrEditDialogRef.initData(dataId)
    },
    addOrEditDone(result = false) {
      this.addOrEditDialogVisible = false
      this.addOrEditDialogTitle = ''
      if (result) {
        this.listTableData()
      }
    },
    deletePage(dataId) {
      this.$confirm('确认执行删除操作吗？').then(_ => {
        this.$mapi.page.deletePage({ pageId: dataId }).then(res => {
          this.$message.success(res.message)
          this.listTableData()
        })
      }).catch(_ => {})
    }
  }
}
</script>

<style scoped>
.page-cover {
  position: relative;
  border-radius: 4px;
  width: 100%;
  height: 170px;
}
.page-name {
  text-align: center;
  margin-top: 0.5rem;
}
.page-item {
  position: relative;
  cursor: pointer;
  margin-bottom: 1rem;
}
.page-opreation {
  position: absolute;
  z-index: 1000;
  top: 0.5rem;
  right: 0.8rem;
}
</style>
