<template>
  <div style="padding: 5px 0 0 10px">
    <el-tabs v-model="activeName" style="padding-left: 8px;" @tab-click="handleClick">
      <el-tab-pane label="本地存储" name="local">
        <local ref="local" />
      </el-tab-pane>
      <el-tab-pane label="阿里云（OSS）" name="aliYun">
        <ali-yun ref="aliYun" />
      </el-tab-pane>
      <el-tab-pane label="七牛云存储（Kodo）" name="qiNiuYun">
        <qi-niu-yun ref="qiNiuYun" />
      </el-tab-pane>
      <el-tab-pane label="腾讯云存储（COS）" name="tencentYun">
        <tencent-yun ref="tencentYun" />
      </el-tab-pane>
    </el-tabs>
  </div>
</template>

<script>
import Local from './local/index'
import AliYun from './aliyun/index'
import QiNiuYun from './qiniuyun/index'
import TencentYun from './tencentyun/index'
export default {
  name: 'Storage',
  components: { Local, AliYun, QiNiuYun, TencentYun },
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
        case 'qiNiuYun':
          this.$nextTick(() => {
            this.$refs['qiNiuYun'].initData()
          })
          break
        case 'tencentYun':
          this.$nextTick(() => {
            this.$refs['tencentYun'].initData()
          })
          break
        default:
          console.log('error tab name', name)
      }
    }
  }
}
</script>

<style scoped>
</style>
