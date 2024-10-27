<template>
  <el-form :model="socialInfo" size="small" label-width="100px" label-position="left">
    <el-form-item label="QQ">
      <el-input v-model="socialInfo.qq" style="width:400px" placeholder="QQ" />
    </el-form-item>
    <el-form-item label="WeChat">
      <el-input v-model="socialInfo.weChat" style="width:400px" placeholder="WeChat" />
    </el-form-item>
    <el-form-item label="Github">
      <el-input v-model="socialInfo.github" style="width:400px" placeholder="Github" />
    </el-form-item>
    <el-form-item label="Gitee">
      <el-input v-model="socialInfo.gitee" style="width:400px" placeholder="Gitee" />
    </el-form-item>
    <el-form-item label="CSDN">
      <el-input v-model="socialInfo.csdn" style="width:400px" placeholder="CSDN" />
    </el-form-item>
    <el-button v-perm="['updateSocialInfo']" v-loading="submitLoading" type="primary" size="medium" style="margin-left:6.3rem" @click="updateSocialInfo">
      修改
    </el-button>
  </el-form>
</template>

<script>
export default {
  data() {
    return {
      submitLoading: false,
      socialInfo: {
        qq: '',
        weChat: '',
        github: '',
        gitee: '',
        csdn: ''
      }
    }
  },
  methods: {
    initData() {
      this.$nextTick(() => {
        this.querySocialInfo()
      })
    },
    querySocialInfo() {
      this.$mapi.webSetting.querySocialInfo().then(res => {
        const { data } = res
        Object.keys(this.socialInfo).forEach(key => {
          this.socialInfo[key] = data[key]
        })
      })
    },
    updateSocialInfo() {
      this.submitLoading = true
      this.$mapi.webSetting.updateSocialInfo(this.socialInfo).then(res => {
        this.$message.success(res.message)
      }).finally(_ => {
        this.submitLoading = false
      })
    }
  }
}
</script>

<style scoped>

</style>
