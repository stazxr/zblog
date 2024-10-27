<template>
  <el-tabs v-model="activeName" style="padding-left: 8px;" @tab-click="handleClick">
    <el-tab-pane label="本地存储" name="local">
      <local ref="local" />
    </el-tab-pane>
    <el-tab-pane label="阿里云存储" name="aliYun">
      <ali-yun ref="aliYun" />
    </el-tab-pane>
    <el-tab-pane label="七牛云存储" name="qiNiu">
      <qi-niu-yun ref="qiNiu" />
    </el-tab-pane>
    <el-tab-pane v-if="hasPerm(['getConfigStorageType'])" label="存储配置" name="config">
      <config ref="config" />
    </el-tab-pane>
  </el-tabs>
</template>

<script>
import Local from './local/index'
import AliYun from './aliyun/index'
import QiNiuYun from './qiniuyun/index'
import config from './config/index'
export default {
  name: 'Storage',
  components: { Local, AliYun, QiNiuYun, config },
  data() {
    return {
      activeName: 'local'
    }
  },
  mounted() {
    this.initData()
  },
  methods: {
    handleClick(tab) {
      this.initData(tab.name)
    },
    initData(name) {
      name = name == null || name === '' ? this.activeName : name
      switch (name) {
        case 'local':
          this.$nextTick(() => {
            this.$refs['local'].initData()
          })
          break
        case 'aliYun':
          this.$nextTick(() => {
            this.$refs['aliYun'].initData()
          })
          break
        case 'qiNiu':
          this.$nextTick(() => {
            this.$refs['qiNiu'].initData()
          })
          break
        case 'config':
          this.$nextTick(() => {
            this.$refs['config'].initData()
          })
          break
        default:
          console.log('error tab name', name)
      }
    },
    hasPerm(value) {
      return this.checkPerm(value)
    }
  }
}
</script>

<style scoped>
</style>
