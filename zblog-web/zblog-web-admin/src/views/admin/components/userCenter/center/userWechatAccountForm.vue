<template>
  <el-form ref="userWechatAccountForm" :model="formData" :rules="formRules" label-width="120px">
    <el-form-item label="公众号名称" prop="accountName">
      <el-input v-model="formData.accountName" :style="isMobile ? '' : 'width: 40%;'" maxlength="100" show-word-limit />
    </el-form-item>
    <el-form-item label="AppId" prop="appId">
      <el-input v-model.trim="formData.appId" :style="isMobile ? '' : 'width: 40%;'" maxlength="64" show-word-limit />
    </el-form-item>
    <el-form-item label="AppSecret" prop="appSecret">
      <el-input v-model.trim="formData.appSecret" type="password" :style="isMobile ? '' : 'width: 40%;'" maxlength="512" show-word-limit />
    </el-form-item>
    <el-form-item label="是否启用" prop="enabled">
      <el-radio-group v-model="formData.enabled" :style="isMobile ? '' : 'width: 35%;'">
        <el-radio :label="true">是</el-radio>
        <el-radio :label="false">否</el-radio>
      </el-radio-group>
    </el-form-item>
    <el-form-item label="验证结果" prop="verifyStatus">
      <el-tag v-if="formData.verifyStatus === 0" type="info">未验证</el-tag>
      <el-tag v-else-if="formData.verifyStatus === 1" type="success">验证成功</el-tag>
      <el-tag v-else-if="formData.verifyStatus === 2" type="danger">验证失败</el-tag>
      <span v-else />
    </el-form-item>
    <el-form-item>
      <el-button type="primary" :loading="submitLoading" :style="isMobile ? 'width: 100%;' : 'width: 35%;'" @click="submit">保存配置</el-button>
    </el-form-item>
  </el-form>
</template>

<script>
export default {
  name: 'UserWechatAccountForm',
  data() {
    return {
      submitLoading: false,
      formData: {
        appId: null,
        appSecret: null,
        accountName: null,
        enabled: true,
        verifyStatus: 0
      },
      formRules: {
        accountName: [
          { required: true, message: '请填写微信公众号名称', trigger: 'blur' }
        ],
        appId: [
          { required: true, message: '请填写微信公众号AppID', trigger: 'blur' }
        ],
        appSecret: [
          { required: true, message: '请填写微信公众号AppSecret密文', trigger: 'blur' }
        ],
        enabled: [
          { required: true, message: '请选择是否启用', trigger: 'change' }
        ]
      }
    }
  },
  computed: {
    isMobile() {
      return this.$store.state.app.device === 'mobile'
    }
  },
  methods: {
    initData() {
      this.$mapi.userCenter.queryUserWechatAccountInfo().then(res => {
        const { data } = res
        this.formData.appId = data.appId
        this.formData.appSecret = data.appSecret
        this.formData.accountName = data.accountName
        this.formData.enabled = data.enabled
        this.formData.verifyStatus = data.verifyStatus
      }).catch(_ => {
        this.formData.appId = null
        this.formData.appSecret = null
        this.formData.accountName = null
        this.formData.enabled = true
        this.formData.verifyStatus = -1
      })
    },
    submit() {
      this.$refs.userWechatAccountForm.validate((valid) => {
        if (valid) {
          this.submitLoading = true
          const param = {
            appId: this.formData.appId,
            appSecret: this.formData.appSecret,
            accountName: this.formData.accountName,
            enabled: this.formData.enabled
          }
          this.$mapi.userCenter.updateUserWechatAccountInfo(param).then(() => {
            this.$message.success('修改成功')
            this.initData()
          }).finally(() => {
            this.submitLoading = false
          })
        }
      })
    }
  }
}
</script>
