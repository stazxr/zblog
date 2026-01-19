<template>
  <el-form ref="userProfileForm" :model="formData" :rules="formRules" label-width="70px">
    <el-form-item label="昵称" prop="nickname">
      <el-input v-model="formData.nickname" :style="isMobile ? '' : 'width: 35%;'" maxlength="25" show-word-limit />
    </el-form-item>
    <el-form-item label="性别" prop="gender">
      <el-radio-group v-model.number="formData.gender" :style="isMobile ? '' : 'width: 35%;'">
        <el-radio :label="1">帅哥</el-radio>
        <el-radio :label="2">美女</el-radio>
        <el-radio :label="3">不显示</el-radio>
      </el-radio-group>
    </el-form-item>
    <el-form-item label="个人网站" prop="website">
      <el-input v-model="formData.website" :style="isMobile ? '' : 'width: 35%;'" />
    </el-form-item>
    <el-form-item label="个性签名" prop="signature">
      <el-input v-model="formData.signature" type="textarea" rows="4" maxlength="100" show-word-limit :style="isMobile ? '' : 'width: 35%;'" />
    </el-form-item>
    <el-form-item>
      <el-button type="primary" :loading="submitLoading" :style="isMobile ? 'width: 100%;' : 'width: 35%;'" @click="submit">保存配置</el-button>
    </el-form-item>
  </el-form>
</template>

<script>
import { mapGetters } from 'vuex'
export default {
  name: 'UserProfileForm',
  data() {
    return {
      submitLoading: false,
      formData: {
        userId: null,
        nickname: null,
        gender: null,
        website: null,
        signature: null
      },
      formRules: {
        nickname: [
          { required: true, message: '请输入用户昵称', trigger: 'blur' },
          { min: 1, max: 25, message: '长度在 1 到 25 个字符', trigger: 'blur' }
        ],
        gender: [
          { required: true, message: '请选择用户性别', trigger: 'change' }
        ]
      }
    }
  },
  computed: {
    ...mapGetters([
      'user'
    ]),
    isMobile() {
      return this.$store.state.app.device === 'mobile'
    }
  },
  watch: {
    user: {
      handler(val) {
        this.formData.userId = val.id
        this.formData.nickname = val.nickname
        this.formData.gender = val.gender
        this.formData.website = val.website
        this.formData.signature = val.signature
      },
      deep: true
    }
  },
  methods: {
    submit() {
      this.$refs.userProfileForm.validate((valid) => {
        if (valid) {
          this.submitLoading = true
          this.$mapi.userCenter.updateUserSelfInfo(this.formData).then(() => {
            this.$message.success('修改成功')
            this.$store.dispatch('RefreshUser').then(() => {})
          }).finally(() => {
            this.submitLoading = false
          })
        }
      })
    }
  }
}
</script>
