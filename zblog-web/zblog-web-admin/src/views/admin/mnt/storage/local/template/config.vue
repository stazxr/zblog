<template>
  <div>
    <el-dialog append-to-body :close-on-click-modal="false" title="本地存储配置" :before-close="handleClose" :visible.sync="dialogVisible" width="520px">
      <el-form ref="addForm" :inline="true" :model="addForm" size="small" label-width="90px">
        <el-form-item label="域名" prop="domain">
          <el-input v-model="addForm.domain" readonly style="width: 380px" placeholder="外链域名，不以斜杠结尾" />
        </el-form-item>
        <el-form-item label="上传路径" prop="pathPrefix">
          <el-input v-model="addForm.uploadFolder" readonly style="width: 380px" placeholder="文件在 Bucket 中的相对路径，eg: upload" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="text" @click="handleClose">关 闭</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
export default {
  name: 'LocalStorageConfig',
  props: {
    dialogVisible: {
      type: Boolean,
      default: false
    }
  },
  data() {
    return {
      addForm: {
        domain: '',
        uploadFolder: ''
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
      }).catch(_ => {
        this.doClose()
      })
    },
    doClose() {
      Object.keys(this.addForm).forEach(key => {
        this.addForm[key] = ''
      })
      this.$emit('configDone')
    },
    handleClose() {
      this.$confirm('确认关闭？').then(_ => {
        this.doClose()
      }).catch(_ => {})
    }
  }
}
</script>

<style scoped>

</style>
