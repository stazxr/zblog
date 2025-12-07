<template>
  <el-dialog
    title="权限详情"
    :visible.sync="dialogVisible"
    :fullscreen="device === 'mobile'"
    :destroy-on-close="true"
    :close-on-click-modal="true"
    :close-on-press-escape="true"
    :before-close="handleClose"
    append-to-body
    width="600px"
  >
    <el-descriptions direction="vertical" :column="4" border>
      <!-- 1 -->
      <el-descriptions-item label="权限序列" :span="1"> {{ dataInfo.id }} </el-descriptions-item>
      <el-descriptions-item label="权限名称" :span="1">
        <span>
          <svg-icon v-if="dataInfo.permType === '1' || dataInfo.permType === '2'" :icon-class="dataInfo.icon" />
          {{ dataInfo.permName }}
        </span>
      </el-descriptions-item>
      <el-descriptions-item label="权限标识" :span="1"> {{ dataInfo.permCode }} </el-descriptions-item>
      <el-descriptions-item label="权限状态" :span="1">
        <el-tag v-if="dataInfo.enabled === 'true'">启用</el-tag>
        <el-tag v-else-if="dataInfo.enabled === 'false'" type="warning">禁用</el-tag>
        <span v-else> - </span>
      </el-descriptions-item>
      <!-- 2 -->
      <el-descriptions-item label="权限类型" :span="1">
        <el-tag v-if="dataInfo.permType === '1'">目录</el-tag>
        <el-tag v-else-if="dataInfo.permType === '2'" type="success">菜单</el-tag>
        <el-tag v-else-if="dataInfo.permType === '3'" type="info">按钮</el-tag>
        <el-tag v-else-if="dataInfo.permType === '4'" type="warning">外链</el-tag>
        <span v-else> {{ dataInfo.permType }} </span>
      </el-descriptions-item>
      <el-descriptions-item label="访问级别" :span="1">
        <el-tag v-if="dataInfo.permLevel === '1'" type="warning">公开</el-tag>
        <el-tag v-else-if="dataInfo.permLevel === '2'" type="success">认证</el-tag>
        <el-tag v-else-if="dataInfo.permLevel === '4'">授权</el-tag>
        <span v-else> {{ dataInfo.permLevel }} </span>
      </el-descriptions-item>
      <el-descriptions-item label="是否隐藏" :span="1">
        <el-tag v-if="dataInfo.hidden === 'true'" type="info">是</el-tag>
        <el-tag v-else-if="dataInfo.hidden === 'false'" type="info">否</el-tag>
        <span v-else> - </span>
      </el-descriptions-item>
      <el-descriptions-item label="是否缓存" :span="1">
        <el-tag v-if="dataInfo.cacheable === 'true'" type="info">是</el-tag>
        <el-tag v-else-if="dataInfo.cacheable === 'false'" type="info">否</el-tag>
        <span v-else> - </span>
      </el-descriptions-item>
      <!-- 3 -->
      <el-descriptions-item label="路由地址" :span="4"> {{ dataInfo.routerPath }} </el-descriptions-item>
      <!-- 4 -->
      <el-descriptions-item label="组件名称" :span="2"> {{ dataInfo.componentName }} </el-descriptions-item>
      <el-descriptions-item label="组件路径" :span="2"> {{ dataInfo.componentPath }} </el-descriptions-item>
      <!-- 5 -->
      <el-descriptions-item label="角色" :span="4">
        {{ dataInfo.roleCodeList ? dataInfo.roleCodeList : '-' }}
      </el-descriptions-item>
      <!-- 6 -->
      <el-descriptions-item label="创建用户" :span="2"> {{ dataInfo.createUsername }} </el-descriptions-item>
      <el-descriptions-item label="创建时间" :span="2"> {{ dataInfo.createTime }} </el-descriptions-item>
      <!-- 7 -->
      <el-descriptions-item label="修改用户" :span="2"> {{ dataInfo.updateUsername }} </el-descriptions-item>
      <el-descriptions-item label="修改时间" :span="2"> {{ dataInfo.updateTime }} </el-descriptions-item>
    </el-descriptions>
  </el-dialog>
</template>

<script>
export default {
  props: {
    dialogVisible: {
      type: Boolean,
      default: false
    }
  },
  data() {
    return {
      dataInfo: {
        id: null,
        permName: null,
        icon: null,
        permType: null,
        permLevel: null,
        enabled: null,
        hidden: null,
        cacheable: null,
        permCode: null,
        routerPath: null,
        componentName: null,
        componentPath: null,
        createUsername: null,
        createTime: null,
        updateUsername: null,
        updateTime: null,
        roleCodeList: null
      }
    }
  },
  computed: {
    device() {
      return this.$store.state.app.device
    }
  },
  methods: {
    initData(dataId) {
      this.$nextTick(() => {
        this.queryDetail(dataId)
      })
    },
    queryDetail(dataId) {
      this.$mapi.perm.queryPermDetail({ permId: dataId }).then(res => {
        const { data } = res
        Object.keys(this.dataInfo).forEach(key => {
          this.dataInfo[key] = data[key] == null || data[key] === '' ? '-' : data[key].toString()
        })
      }).catch(_ => {
        setTimeout(() => { this.doClose() }, 500)
      })
    },
    handleClose() {
      this.doClose()
    },
    doClose() {
      Object.keys(this.dataInfo).forEach(key => {
        this.dataInfo[key] = null
      })
      this.$emit('showDetailDone')
    }
  }
}
</script>

<style scoped>

</style>
