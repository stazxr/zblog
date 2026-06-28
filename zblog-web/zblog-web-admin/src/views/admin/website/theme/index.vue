<template>
  <div class="app-container">
    <div class="head-container">
      <div class="search-opts">
        <muses-search-form ref="searchForm" :model="filters" label-position="right" label-width="0" :offset="0" :item-width="140">
          <muses-search-form-item label="" prop="search-themeName">
            <el-input id="search-themeName" v-model="filters.themeName" clearable placeholder="主题名称" @keyup.enter.native="search" />
          </muses-search-form-item>
          <muses-search-form-item label="" prop="search-ownerName">
            <el-input id="search-ownerName" v-model="filters.ownerName" clearable placeholder="作者（用户名）" @keyup.enter.native="search" />
          </muses-search-form-item>
          <muses-search-form-item label="" prop="search-themeType">
            <el-select id="search-themeType" v-model="filters.themeType" placeholder="主题类型" clearable @change="search">
              <el-option v-for="item in themeTypeList" :key="item.value" :label="item.name" :value="item.value" />
            </el-select>
          </muses-search-form-item>
          <muses-search-form-item label="" prop="search-ownerType">
            <el-select id="search-ownerType" v-model="filters.ownerType" placeholder="归属类型" clearable @change="search">
              <el-option v-for="item in themeOwnerTypeList" :key="item.value" :label="item.name" :value="item.value" />
            </el-select>
          </muses-search-form-item>
          <muses-search-form-item label="" prop="search-isDefault">
            <el-select id="search-isDefault" v-model="filters.isDefault" placeholder="系统主题状态" clearable @change="search">
              <el-option v-for="item in themeSystemStatusList" :key="item.value" :label="item.name" :value="item.value" />
            </el-select>
          </muses-search-form-item>
          <muses-search-form-item label="" prop="search-isActive">
            <el-select id="search-isActive" v-model="filters.isActive" placeholder="用户主题状态" clearable @change="search">
              <el-option v-for="item in themeUserStatusList" :key="item.value" :label="item.name" :value="item.value" />
            </el-select>
          </muses-search-form-item>
          <muses-search-form-item btn btn-open-name="" btn-close-name="">
            <el-button type="success" @click="search()">查 询</el-button>
            <el-button type="warning" @click="resetSearch()">重 置</el-button>
          </muses-search-form-item>
        </muses-search-form>
      </div>
      <div class="crud-opts">
        <span class="crud-opts-left">
          <el-button v-perm="['THEMA001']" type="success" @click="addPage">新增</el-button>
          <el-button v-perm="['THEMQ002']" :disabled="!row" type="info" @click="showDetail">详情</el-button>
          <el-button v-perm="['THEMU001']" :disabled="!row" type="primary" @click="editPage">编辑</el-button>
          <el-dropdown trigger="click" :disabled="!row" @command="handleEditCommand">
            <el-button type="info" :disabled="!row">更多<i class="el-icon-arrow-down el-icon--right" /></el-button>
            <el-dropdown-menu slot="dropdown">
              <el-dropdown-item v-if="canActiveTheme" command="activeTheme">激活主题</el-dropdown-item>
              <el-dropdown-item v-if="canInactiveTheme" command="inactiveTheme">取消激活</el-dropdown-item>
              <el-dropdown-item v-if="canUpgradeTheme" command="upgradeTheme">转为系统主题</el-dropdown-item>
              <el-dropdown-item v-if="canSetDefault" command="defaultTheme">设为默认</el-dropdown-item>
              <el-dropdown-item v-if="canCancelDefault" command="cancelDefaultTheme">取消默认</el-dropdown-item>
              <el-dropdown-item divided command="showPageConfig">页面预览</el-dropdown-item>
              <el-dropdown-item command="editPageConfig">页面配置</el-dropdown-item>
              <el-dropdown-item divided command="delete">删除</el-dropdown-item>
            </el-dropdown-menu>
          </el-dropdown>
        </span>
      </div>
    </div>
    <div class="components-container">
      <el-table
        ref="themeTable"
        v-loading="tableLoading"
        :data="tableData"
        :header-cell-style="{background:'#FAFAFA'}"
        highlight-current-row
        row-key="id"
        border
        @current-change="handleCurrentChange"
      >
        <el-table-column label="预览图" align="center" width="180">
          <template v-slot="scope">
            <el-image class="theme-cover" :src="scope.row['previewCover']" :preview-src-list="getPreviewList(scope.row)">
              <div slot="error" class="image-slot">
                <span v-if="scope.row['previewCover'] === null">未配置</span>
                <span v-else>加载失败</span>
              </div>
            </el-image>
          </template>
        </el-table-column>
        <el-table-column :show-overflow-tooltip="true" prop="themeName" label="主题名称" align="center" />
        <el-table-column label="主题类型" align="center" width="100px">
          <template v-slot="scope">
            <el-tag v-if="scope.row.themeType === 'MOBILE'" type="primary">移动端</el-tag>
            <el-tag v-else-if="scope.row.themeType === 'PC'" type="primary">电脑端</el-tag>
            <span v-else>-</span>
          </template>
        </el-table-column>
        <el-table-column label="主题归属" align="center" width="100px">
          <template v-slot="scope">
            <el-tag v-if="scope.row.ownerType === 'SYSTEM'" type="primary">系统主题</el-tag>
            <el-tag v-else-if="scope.row.ownerType === 'USER'" type="primary">用户主题</el-tag>
            <span v-else>-</span>
          </template>
        </el-table-column>
        <el-table-column :show-overflow-tooltip="true" prop="ownerName" label="所属用户" align="center" width="120px" />
        <el-table-column :show-overflow-tooltip="true" label="状态" align="center" width="100px">
          <template v-slot="scope">
            <span v-if="scope.row.ownerType === 'SYSTEM'">
              <el-tag v-if="scope.row.isDefault === true" type="success">默认主题</el-tag>
              <el-tag v-else type="info">普通主题</el-tag>
            </span>
            <span v-else-if="scope.row.ownerType === 'USER'">
              <el-tag v-if="scope.row.isActive === true" type="success">已激活</el-tag>
              <el-tag v-else type="info">未激活</el-tag>
            </span>
            <span v-else>-</span>
          </template>
        </el-table-column>
        <el-table-column :show-overflow-tooltip="true" prop="description" label="主题描述" align="center" width="160px" />
        <div slot="empty">
          <el-empty :image="nodataImg" description=" " />
        </div>
      </el-table>
      <div class="pagination-container">
        <el-pagination
          :total="total"
          :current-page.sync="page"
          :page-size.sync="pageSize"
          :page-sizes="[10, 20, 30, 50]"
          layout="total, prev, pager, next, sizes"
          @size-change="handleSizeChange"
          @current-change="handlePageChange"
        />
      </div>
    </div>

    <!-- 详情 -->
    <detailDialog
      ref="detailDialogRef"
      :dialog-visible="detailDialogVisible"
      @showDetailDone="showDetailDone"
    />
    <!-- 新增 / 编辑 -->
    <addOrEditDialog
      ref="addOrEditDialogRef"
      :dialog-title="addOrEditDialogTitle"
      :dialog-visible="addOrEditDialogVisible"
      @addOrEditDone="addOrEditDone"
    />
  </div>
</template>

<script>
import nodataImg from '@/assets/images/nodata.png'
import detailDialog from '@/views/admin/website/theme/template/detailDialog'
import addOrEditDialog from '@/views/admin/website/theme/template/addOrEditDialog'
import { mapGetters } from 'vuex'
export default {
  name: 'Theme',
  components: {
    detailDialog,
    addOrEditDialog
  },
  data() {
    return {
      filters: {
        themeName: null,
        themeType: null,
        ownerType: null,
        ownerName: null,
        isDefault: null,
        isActive: null
      },
      themeTypeList: [],
      themeOwnerTypeList: [],
      themeSystemStatusList: [],
      themeUserStatusList: [],
      tableData: [],
      tableLoading: false,
      nodataImg: nodataImg,
      row: null,
      total: 0,
      page: 1,
      pageSize: 10,
      detailDialogVisible: false,
      addOrEditDialogTitle: null,
      addOrEditDialogVisible: false
    }
  },
  computed: {
    ...mapGetters(['user']),
    canActiveTheme() {
      return this.hasPerm('THEMU002') &&
        this.row &&
        this.row.ownerType === 'USER' &&
        this.row.ownerId === this.user.id &&
        !this.row.isActive
    },
    canInactiveTheme() {
      return this.hasPerm('THEMU002') &&
        this.row &&
        this.row.ownerType === 'USER' &&
        this.row.ownerId === this.user.id &&
        this.row.isActive
    },
    canUpgradeTheme() {
      return this.hasPerm('THEMU004') &&
        this.row &&
        this.row.ownerType === 'USER'
    },
    canSetDefault() {
      return this.hasPerm('THEMU003') &&
        this.row &&
        this.row.ownerType === 'SYSTEM' &&
        !this.row.isDefault
    },
    canCancelDefault() {
      return this.hasPerm('THEMU003') &&
        this.row &&
        this.row.ownerType === 'SYSTEM' &&
        this.row.isDefault
    }
  },
  mounted() {
    this.loadThemeTypeList()
    this.loadThemeOwnerTypeList()
    this.loadThemeSystemStatusList()
    this.loadThemeUserStatusList()
    this.listTableData()
  },
  methods: {
    hasPerm(value) {
      return this.checkPerm(value)
    },
    handleCurrentChange(row) {
      this.row = row
    },
    loadThemeTypeList() {
      this.$mapi.communal.queryConfListByDictKey({ dictKey: 'THEME_TYPE_CONFIG' }).then(res => {
        const { data } = res
        this.themeTypeList = data
      }).catch(_ => {
        this.themeTypeList = []
      })
    },
    loadThemeOwnerTypeList() {
      this.$mapi.communal.queryConfListByDictKey({ dictKey: 'THEME_OWNER_TYPE_CONFIG' }).then(res => {
        const { data } = res
        this.themeOwnerTypeList = data
      }).catch(_ => {
        this.themeOwnerTypeList = []
      })
    },
    loadThemeSystemStatusList() {
      this.$mapi.communal.queryConfListByDictKey({ dictKey: 'THEME_SYSTEM_STATUS_CONFIG' }).then(res => {
        const { data } = res
        this.themeSystemStatusList = data
      }).catch(_ => {
        this.themeSystemStatusList = []
      })
    },
    loadThemeUserStatusList() {
      this.$mapi.communal.queryConfListByDictKey({ dictKey: 'THEME_USER_STATUS_CONFIG' }).then(res => {
        const { data } = res
        this.themeUserStatusList = data
      }).catch(_ => {
        this.themeUserStatusList = []
      })
    },
    handleEditCommand(command) {
      switch (command) {
        case 'activeTheme':
          this.activeTheme()
          break
        case 'inactiveTheme':
          this.inactiveTheme()
          break
        case 'upgradeTheme':
          this.upgradeTheme()
          break
        case 'defaultTheme':
          this.setDefault()
          break
        case 'cancelDefaultTheme':
          this.cancelDefault()
          break
        case 'showPageConfig':
          this.showPageConfig()
          break
        case 'editPageConfig':
          this.editPageConfig()
          break
        case 'delete':
          this.deletePage()
          break
      }
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
      this.$mapi.theme.pageThemeList(param).then(res => {
        const { data } = res
        this.total = data.total
        this.tableData = data.records
      }).catch(_ => {
        this.total = 0
        this.tableData = []
      }).finally(() => {
        this.tableLoading = false
        this.row = null
        this.$refs.themeTable.setCurrentRow()
      })
    },
    getPreviewList(row) {
      if (row['previewCover'] && row['previewCover'] !== '') {
        return [row['previewCover']]
      } else {
        return []
      }
    },
    // 详情
    showDetail() {
      if (this.row === null) {
        this.$message.error('请选择要查看的页面')
        return
      }
      this.detailDialogVisible = true
      this.$refs.detailDialogRef.initData(this.row.id)
    },
    showDetailDone() {
      this.detailDialogVisible = false
    },
    // 新增与编辑
    addPage() {
      this.addOrEditDialogVisible = true
      this.addOrEditDialogTitle = '新增主题'
      this.$refs.addOrEditDialogRef.initData()
    },
    editPage() {
      if (this.row === null) {
        this.$message.error('请选择要编辑的主题')
        return
      }
      this.addOrEditDialogVisible = true
      this.addOrEditDialogTitle = '编辑主题'
      this.$refs.addOrEditDialogRef.initData(this.row.id)
    },
    addOrEditDone(result = false) {
      this.addOrEditDialogTitle = null
      this.addOrEditDialogVisible = false
      if (result) {
        this.listTableData()
      }
    },
    // 设置用户主题状态
    activeTheme() {
      this.doSetActiveTheme(true)
    },
    inactiveTheme() {
      this.doSetActiveTheme(false)
    },
    doSetActiveTheme(isActive) {
      if (this.row === null) {
        this.$message.error('请选择要操作的主题')
        return
      }
      if (this.row.ownerType !== 'USER') {
        this.$message.error('只允许操作用户主题')
        return
      }
      if (this.row.ownerId !== this.user.id) {
        this.$message.error('只允许操作自己创建的主题')
        return
      }
      const action = isActive ? '启用' : '停用'
      const content = isActive ? `启用后将切换为当前主题，是否继续？` : `停用后该主题将无法继续使用，是否继续？`
      this.$confirm(`${content}`, `${action}主题`, {
        confirmButtonText: action,
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        const param = { themeId: this.row.id, status: isActive }
        this.$mapi.theme.editUserThemeStatus(param).then(res => {
          this.$message.success(res.message)
          this.listTableData()
        })
      })
    },
    // 升级用户主题为系统主题
    upgradeTheme() {
      if (this.row === null) {
        this.$message.error('请选择要操作的主题')
        return
      }
      if (this.row.ownerType !== 'USER') {
        this.$message.error('只允许操作用户主题')
        return
      }
      this.$confirm('此操作将升级用户主题为系统主题, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.$mapi.theme.upgradeTheme({ themeId: this.row.id }).then(res => {
          this.$message.success(res.message)
          this.listTableData()
        })
      })
    },
    // 设置系统主题状态
    setDefault() {
      this.doSetDefaultTheme(true)
    },
    cancelDefault() {
      this.doSetDefaultTheme(false)
    },
    doSetDefaultTheme(isDefault) {
      if (this.row === null) {
        this.$message.error('请选择要操作的主题')
        return
      }
      if (this.row.ownerType !== 'SYSTEM') {
        this.$message.error('只允许操作系统主题')
        return
      }
      const content = isDefault ? `是否确认将主题设置为默认主题？` : `是否确认将主题设置为普通主题？`
      this.$confirm(`${content}`, {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        const param = { themeId: this.row.id, status: isDefault }
        this.$mapi.theme.editSystemThemeStatus(param).then(res => {
          this.$message.success(res.message)
          this.listTableData()
        })
      })
    },
    // 查看页面配置
    showPageConfig() {
      this.$router.push({
        path: `/website/theme/config/${this.row.id}`,
        query: {
          mode: 'view'
        }
      })
    },
    // 编辑页面配置
    editPageConfig() {
      this.$router.push({
        path: `/website/theme/config/${this.row.id}`,
        query: {
          mode: 'edit'
        }
      })
    },
    // 删除
    deletePage() {
      if (this.row === null) {
        this.$message.error('请选择要删除的主题')
        return
      }
      this.$confirm('此操作将永久删除主题【' + this.row.themeName + '】, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.$mapi.theme.deleteTheme({ themeId: this.row.id }).then(res => {
          this.$message.success(res.message)
          this.listTableData()
        })
      })
    }
  }
}
</script>

<style scoped>
.theme-cover {
  position: relative;
  width: 100%;
  height: 90px;
  border-radius: 4px;
}
::v-deep .image-slot, .demo-image__placeholder .image-slot {
  display: flex;
  justify-content: center;
  align-items: center;
  width: 100%;
  height: 100%;
  background: #f5f7fa;
  color: #909399;
  font-size: 14px;
}
</style>
