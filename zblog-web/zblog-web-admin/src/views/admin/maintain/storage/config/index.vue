<template>
  <div class="app-container">
    <el-form ref="addForm" label-position="top" :model="addForm" :rules="addRules" label-width="130px">
      <el-form-item label="文件存储类型" prop="storageType">
        <el-radio v-model="addForm.storageType" label="0" value="0">默认（{{ defaultType }}）</el-radio>
        <el-radio v-model="addForm.storageType" label="1" value="1">本地</el-radio>
        <el-radio v-model="addForm.storageType" label="2" value="2">阿里云</el-radio>
        <el-radio v-model="addForm.storageType" label="3" value="3">七牛云</el-radio>
      </el-form-item>
      <el-form-item style="padding-top: 30px;">
        <el-button type="primary" @click="reset">重 置</el-button>
        <el-button v-perm="['activeStorageConfig']" :loading="submitLoading" type="primary" @click="submit">提 交</el-button>
      </el-form-item>
    </el-form>
  </div>
</template>

<script>
export default {
  name: 'StorageConfig',
  data() {
    return {
      submitLoading: false,
      defaultType: '',
      addForm: {
        storageType: ''
      },
      addRules: {
        storageType: [
          { required: true, message: '请选择存储类型', trigger: 'blur' }
        ]
      }
    }
  },
  methods: {
    initData() {
      this.$nextTick(() => {
        this.getConfigStorageType()
      })
    },
    getConfigStorageType() {
      this.$mapi.file.getConfigStorageType().then(res => {
        const { data } = res
        this.defaultType = data['defaultTypeName']
        this.addForm.storageType = data['activeType'].toString()
      })
    },
    reset() {
      this.getConfigStorageType()
    },
    submit() {
      this.$refs['addForm'].validate((valid) => {
        if (valid) {
          this.submitLoading = true
          this.$mapi.file.activeStorageConfig(this.addForm).then(res => {
            this.$message.success(res.message)
          }).finally(_ => {
            this.submitLoading = false
          })
        } else {
          return false
        }
      })
    }
  }
}
</script>

<style scoped>

</style>
