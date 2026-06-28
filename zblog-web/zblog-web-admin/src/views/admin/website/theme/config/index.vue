<template>
  <div class="card-container">
    <el-card class="main-card">
      <div class="card-title">{{ pageTitle }}</div>
      <div v-if="isEdit" class="card-operation">
        <el-button type="primary" @click="addThemePage()">新建页面</el-button>
      </div>
      <el-row v-loading="tableLoading" class="page-container" :gutter="12">
        <muses-empty v-if="tableData.length === 0" />
        <el-col v-for="item of tableData" :key="item.id" :md="6">
          <div class="page-item">
            <div v-if="isEdit && hasPerm(['THEMU005', 'THEMD002'])" class="page-opreation">
              <el-dropdown @command="handleCommand">
                <i class="el-icon-more" style="color:#fff" />
                <el-dropdown-menu slot="dropdown">
                  <el-dropdown-item v-perm="['THEMU005']" :command="'1,' + item.id">编辑</el-dropdown-item>
                  <el-dropdown-item v-perm="['THEMD002']" :command="'2,' + item.id">删除</el-dropdown-item>
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
import addOrEditDialog from '@/views/admin/website/theme/config/template/addOrEditDialog'
export default {
  name: 'Page',
  components: {
    addOrEditDialog
  },
  data() {
    return {
      tableData: [],
      tableLoading: false,
      addOrEditDialogTitle: null,
      addOrEditDialogVisible: false
    }
  },
  computed: {
    themeId() {
      return this.$route.params.themeId
    },
    mode() {
      return this.$route.query.mode || 'edit'
    },
    isView() {
      return this.mode === 'view'
    },
    isEdit() {
      return this.mode === 'edit'
    },
    pageTitle() {
      return this.isView ? '主题页面预览' : '主题页面配置'
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
      this.tableLoading = true
      this.$mapi.theme.queryThemePageList({ themeId: this.themeId }).then(({ data }) => {
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
          this.editThemePage(arr[1])
          break
        case '2':
          this.deleteThemePage(arr[1])
          break
      }
    },
    addThemePage() {
      this.addOrEditDialogVisible = true
      this.addOrEditDialogTitle = '新增主题页面'
      this.$refs.addOrEditDialogRef.initData(this.themeId)
    },
    editThemePage(dataId) {
      this.addOrEditDialogVisible = true
      this.addOrEditDialogTitle = '编辑主题页面'
      this.$refs.addOrEditDialogRef.initData(this.themeId, dataId)
    },
    addOrEditDone(result = false) {
      this.addOrEditDialogVisible = false
      this.addOrEditDialogTitle = null
      if (result) {
        this.listTableData()
      }
    },
    deleteThemePage(dataId) {
      this.$confirm('确认执行删除操作吗？').then(_ => {
        this.$mapi.theme.deleteThemePage({ themePageId: dataId }).then(res => {
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
