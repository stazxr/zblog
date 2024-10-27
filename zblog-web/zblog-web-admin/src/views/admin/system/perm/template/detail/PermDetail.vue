<template>
  <el-descriptions direction="vertical" :column="4" border>
    <!-- 1 -->
    <el-descriptions-item label="权限序列"> {{ permInfo.id }} </el-descriptions-item>
    <el-descriptions-item v-if="permInfo.permType === '1' || permInfo.permType === '2'" label="权限名称">
      <span>
        <svg-icon :icon-class="permInfo.icon" />
        {{ permInfo.permName }}
      </span>
    </el-descriptions-item>
    <el-descriptions-item v-else label="权限名称">
      <span>
        {{ permInfo.permName }}
      </span>
    </el-descriptions-item>
    <el-descriptions-item label="权限类型">
      <el-tag v-if="permInfo.permType === '1'" size="small">目录</el-tag>
      <el-tag v-else-if="permInfo.permType === '2'" size="small" type="success">菜单</el-tag>
      <el-tag v-else-if="permInfo.permType === '3'" size="small" type="info">按钮</el-tag>
      <span v-else> {{ permInfo.permType }} </span>
    </el-descriptions-item>
    <el-descriptions-item label="访问级别">
      <el-tag v-if="permInfo.permLevel === '1'" size="small" type="warning">公开</el-tag>
      <el-tag v-else-if="permInfo.permLevel === '2'" size="small" type="info">认证</el-tag>
      <el-tag v-else-if="permInfo.permLevel === '3'" size="small" type="success">授权</el-tag>
      <span v-else> {{ permInfo.permLevel }} </span>
    </el-descriptions-item>
    <!-- 2 -->
    <el-descriptions-item label="权限状态">
      <el-tag v-if="permInfo.enabled === 'true'" size="small">启用</el-tag>
      <el-tag v-else-if="permInfo.enabled === 'false'" size="small" type="warning">禁用</el-tag>
      <span v-else> {{ permInfo.enabled }} </span>
    </el-descriptions-item>
    <el-descriptions-item label="是否外链">
      <el-tag v-if="permInfo.iFrame === 'true'" size="small">是</el-tag>
      <el-tag v-else-if="permInfo.iFrame === 'false'" size="small">否</el-tag>
      <span v-else> {{ permInfo.iFrame }} </span>
    </el-descriptions-item>
    <el-descriptions-item label="是否隐藏">
      <el-tag v-if="permInfo.hidden === 'true'" size="small">是</el-tag>
      <el-tag v-else-if="permInfo.hidden === 'false'" size="small">否</el-tag>
      <span v-else> {{ permInfo.hidden }} </span>
    </el-descriptions-item>
    <el-descriptions-item label="是否缓存">
      <el-tag v-if="permInfo.cache === 'true'" size="small">是</el-tag>
      <el-tag v-else-if="permInfo.cache === 'false'" size="small">否</el-tag>
      <span v-else> {{ permInfo.cache }} </span>
    </el-descriptions-item>
    <!-- 3 -->
    <el-descriptions-item label="权限标识" :span="2"> {{ permInfo.permCode }} </el-descriptions-item>
    <el-descriptions-item label="路由地址" :span="2"> {{ permInfo.routerPath }} </el-descriptions-item>
    <!-- 4 -->
    <el-descriptions-item label="组件名称" :span="2"> {{ permInfo.componentName }} </el-descriptions-item>
    <el-descriptions-item label="组件路径" :span="2"> {{ permInfo.componentPath }} </el-descriptions-item>
    <!-- 5 -->
    <el-descriptions-item label="创建用户" :span="2"> {{ permInfo.createUser }} </el-descriptions-item>
    <el-descriptions-item label="创建时间" :span="2"> {{ permInfo.createTime }} </el-descriptions-item>
    <!-- 5 -->
    <el-descriptions-item label="修改用户" :span="2"> {{ permInfo.updateUser }} </el-descriptions-item>
    <el-descriptions-item label="修改时间" :span="2"> {{ permInfo.updateTime }} </el-descriptions-item>
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
        cache: '',
        permCode: '',
        routerPath: '',
        componentName: '',
        componentPath: '',
        createUser: '',
        createTime: '',
        updateUser: '',
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
    },
    isShowIcon() {
      return false
    }
  }
}
</script>

<style scoped>

</style>
