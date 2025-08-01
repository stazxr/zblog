<template>
  <el-descriptions direction="vertical" :column="4" border>
    <!-- 1 -->
    <el-descriptions-item label="序号"> {{ permInfo.id }} </el-descriptions-item>
    <el-descriptions-item label="权限名称">
      <span>
        <svg-icon v-if="permInfo.permType === '1' || permInfo.permType === '2'" :icon-class="permInfo.icon" />
        {{ permInfo.permName }}
      </span>
    </el-descriptions-item>
    <el-descriptions-item label="权限类型">
      <el-tag v-if="permInfo.permType === '1'">目录</el-tag>
      <el-tag v-else-if="permInfo.permType === '2'" type="success">菜单</el-tag>
      <el-tag v-else-if="permInfo.permType === '3'" type="info">按钮</el-tag>
      <span v-else> {{ permInfo.permType }} </span>
    </el-descriptions-item>
    <el-descriptions-item label="访问级别">
      <el-tag v-if="permInfo.permLevel === '1'" type="warning">公开</el-tag>
      <el-tag v-else-if="permInfo.permLevel === '2'" type="success">认证</el-tag>
      <el-tag v-else-if="permInfo.permLevel === '4'">授权</el-tag>
      <span v-else> {{ permInfo.permLevel }} </span>
    </el-descriptions-item>
    <!-- 2 -->
    <el-descriptions-item label="权限状态">
      <el-tag v-if="permInfo.enabled === 'true'">启用</el-tag>
      <el-tag v-else-if="permInfo.enabled === 'false'" type="warning">禁用</el-tag>
      <span v-else> - </span>
    </el-descriptions-item>
    <el-descriptions-item label="是否外链">
      <el-tag v-if="permInfo.iFrame === 'true'" type="info">是</el-tag>
      <el-tag v-else-if="permInfo.iFrame === 'false'" type="info">否</el-tag>
      <span v-else> - </span>
    </el-descriptions-item>
    <el-descriptions-item label="是否隐藏">
      <el-tag v-if="permInfo.hidden === 'true'" type="info">是</el-tag>
      <el-tag v-else-if="permInfo.hidden === 'false'" type="info">否</el-tag>
      <span v-else> - </span>
    </el-descriptions-item>
    <el-descriptions-item label="是否缓存">
      <el-tag v-if="permInfo.cacheable === 'true'" type="info">是</el-tag>
      <el-tag v-else-if="permInfo.cacheable === 'false'" type="info">否</el-tag>
      <span v-else> - </span>
    </el-descriptions-item>
    <!-- 3 -->
    <el-descriptions-item label="权限标识"> {{ permInfo.permCode }} </el-descriptions-item>
    <el-descriptions-item label="路由地址"> {{ permInfo.routerPath }} </el-descriptions-item>
    <el-descriptions-item label="组件名称"> {{ permInfo.componentName }} </el-descriptions-item>
    <el-descriptions-item label="组件路径"> {{ permInfo.componentPath }} </el-descriptions-item>
    <!-- 5 -->
    <el-descriptions-item label="创建用户"> {{ permInfo.createUsername }} </el-descriptions-item>
    <el-descriptions-item label="创建时间"> {{ permInfo.createTime }} </el-descriptions-item>
    <el-descriptions-item label="修改用户"> {{ permInfo.updateUsername }} </el-descriptions-item>
    <el-descriptions-item label="修改时间"> {{ permInfo.updateTime }} </el-descriptions-item>
  </el-descriptions>
</template>

<script>
export default {
  name: 'PermDetail',
  data() {
    return {
      permInfo: {
        id: '',
        permName: '',
        icon: '',
        permType: '',
        permLevel: '',
        enabled: '',
        iFrame: '',
        hidden: '',
        cacheable: '',
        permCode: '',
        routerPath: '',
        componentName: '',
        componentPath: '',
        createUsername: '',
        createTime: '',
        updateUsername: '',
        updateTime: ''
      }
    }
  },
  methods: {
    initData(permId) {
      this.$nextTick(() => {
        this.getPermDetail(permId)
      })
    },
    getPermDetail(permId) {
      this.$mapi.perm.queryPermDetail({ permId }).then(res => {
        const { data } = res
        Object.keys(this.permInfo).forEach(key => {
          this.permInfo[key] = data[key] == null || data[key] === '' ? '-' : data[key].toString()
        })
      }).catch(_ => {
        Object.keys(this.permInfo).forEach(key => {
          this.permInfo[key] = '-'
        })
      })
    }
  }
}
</script>

<style scoped>

</style>
