<template>
  <el-card class="main-card">
    <el-tabs v-model="activeName" @tab-click="handleClick">
      <el-tab-pane v-if="hasPerm(['queryWebInfo'])" label="网站信息" name="webInfo">
        <WebInfo ref="webInfo" />
      </el-tab-pane>
      <el-tab-pane v-if="hasPerm(['querySocialInfo'])" label="社交信息" name="socialInfo">
        <SocialInfo ref="socialInfo" />
      </el-tab-pane>
      <el-tab-pane v-if="hasPerm(['queryOtherInfo'])" label="其他设置" name="otherInfo">
        <OtherInfo ref="otherInfo" />
      </el-tab-pane>
    </el-tabs>
  </el-card>
</template>

<script>
import WebInfo from '@/views/admin/web/setting/template/webInfo'
import SocialInfo from '@/views/admin/web/setting/template/socialInfo'
import OtherInfo from '@/views/admin/web/setting/template/otherInfo'
export default {
  name: 'WebSetting',
  components: {
    WebInfo, SocialInfo, OtherInfo
  },
  data() {
    return {
      activeName: 'webInfo'
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
        case 'webInfo':
          this.$nextTick(() => {
            this.$refs['webInfo'].initData()
          })
          break
        case 'socialInfo':
          this.$nextTick(() => {
            this.$refs['socialInfo'].initData()
          })
          break
        case 'otherInfo':
          this.$nextTick(() => {
            this.$refs['otherInfo'].initData()
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
.main-card {
  margin: 10px 10px 0 10px;
}
</style>
