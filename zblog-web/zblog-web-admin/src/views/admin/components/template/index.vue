<template>
  <!-- 顶级容器 -->
  <div class="app-container">
    <!-- 头部 -->
    <div class="head-container">
      <div class="search-opts">
        <el-input clearable size="small" placeholder="查询条件1" style="width: 200px;" class="filter-item" />
        <el-input clearable size="small" placeholder="查询条件2" style="width: 200px;" class="filter-item" />
        <el-input clearable size="small" placeholder="查询条件3" style="width: 200px;" class="filter-item" />
        <span>
          <el-button class="filter-item" size="small" type="success" icon="el-icon-search">搜索</el-button>
          <el-button class="filter-item" size="small" type="warning" icon="el-icon-refresh-left">重置</el-button>
        </span>
      </div>
      <div class="crud-opts">
        <span class="crud-opts-left">
          <el-button class="filter-item" size="small" type="success">左侧按钮1</el-button>
          <el-button class="filter-item" size="small" type="warning">左侧按钮2</el-button>
        </span>
        <span class="crud-opts-right">
          <el-button class="filter-item" size="small" type="success">右侧按钮1</el-button>
          <el-button class="filter-item" size="small" type="warning">右侧按钮2</el-button>
        </span>
      </div>
    </div>
    <!-- 内容 -->
    <div class="components-container">
      <el-table border style="width: 100%">
        <el-table-column :show-overflow-tooltip="true" label="列1" align="center" />
        <el-table-column :show-overflow-tooltip="true" label="列2" align="center" />
        <el-table-column :show-overflow-tooltip="true" label="列3" align="center" />
        <el-table-column :show-overflow-tooltip="true" label="列4" align="center" />
        <el-table-column :show-overflow-tooltip="true" label="列5" align="center" />
        <el-table-column :show-overflow-tooltip="true" label="列6" align="center" />
        <div slot="empty">
          <el-empty />
        </div>
      </el-table>
    </div>
    <!-- 脚步 -->
    <div class="pagination-container">
      <el-pagination
        :total="100"
        :current-page="1"
        style="margin-top: 8px;"
        layout="total, prev, pager, next, sizes"
      />
    </div>
  </div>

</template>

<script>
export default {
  name: 'Template'
}
</script>

<style scoped>

</style>
