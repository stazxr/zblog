<template>
  <div>
    <el-dialog append-to-body :close-on-click-modal="false" title="七牛云存储配置" :before-close="handleClose" :visible.sync="dialogVisible" width="520px">
      <el-form ref="addForm" :inline="true" :model="addForm" size="small" label-width="90px">
        <el-form-item label="域名" prop="domain">
          <el-input v-model="addForm.domain" style="width: 380px" placeholder="外链域名，不以斜杠结尾" />
        </el-form-item>
        <el-form-item label="上传路径" prop="pathPrefix">
          <el-input v-model="addForm.pathPrefix" style="width: 380px" placeholder="文件在 Bucket 中的相对路径，eg: upload" />
        </el-form-item>
        <el-form-item label="AK" prop="ak">
          <el-input v-model="addForm.ak" style="width: 380px" placeholder="accessKey" />
        </el-form-item>
        <el-form-item label="SK" prop="sk">
          <el-input v-model="addForm.sk" style="width: 380px" placeholder="secretKey" />
        </el-form-item>
        <el-form-item label="存储区域" prop="zone">
          <el-select v-model="addForm.zone" placeholder="Region" style="width: 380px">
            <el-option v-for="item in zones" :key="item" :label="item" :value="item" />
          </el-select>
        </el-form-item>
        <el-form-item label="存储空间" prop="bucketName">
          <el-input v-model="addForm.bucketName" style="width: 380px" placeholder="Bucket，访问控制需要是公开级别" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="text" @click="cancel">取 消</el-button>
        <el-button v-perm="['setStorageConfigInfo']" :loading="submitLoading" type="primary" @click="submit">确 认</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { encrypt } from '@/utils/rsaEncrypt'
export default {
  name: 'QiNiuYunStorageConfig',
  props: {
    dialogVisible: {
      type: Boolean,
      default: false
    }
  },
  data() {
    return {
      submitLoading: false,
      zones: ['华东', '华北', '华南', '北美'],
      addForm: {
        storageType: null,
        domain: '',
        pathPrefix: '',
        ak: '',
        sk: '',
        zone: '',
        bucketName: ''
      }
    }
  },
  methods: {
    initData(type) {
      this.$nextTick(() => {
        this.getConfigInfo(type)
      })
    },
    getConfigInfo(type) {
      this.$mapi.file.getConfigInfo({ storageType: type }).then(res => {
        const { data } = res
        if (data != null) {
          Object.keys(this.addForm).forEach(key => {
            this.addForm[key] = data[key]
          })
        }

        // set storageType
        this.addForm.storageType = type
      }).catch(_ => {
        this.doClose()
      })
    },
    doClose() {
      Object.keys(this.addForm).forEach(key => {
        this.addForm[key] = ''
      })
      this.$refs['addForm'].resetFields()
      this.submitLoading = false
      this.$emit('configDone')
    },
    handleClose() {
      if (!this.submitLoading) {
        this.$confirm('确认关闭？').then(_ => {
          this.doClose()
        }).catch(_ => {})
      }
    },
    cancel() {
      this.handleClose()
    },
    submit() {
      this.$refs['addForm'].validate((valid) => {
        if (valid) {
          this.submitLoading = true
          this.addForm.sk = encrypt(this.addForm.sk)
          this.$mapi.file.setConfigInfo(this.addForm).then(res => {
            this.$message.success(res.message)
            this.doClose()
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
