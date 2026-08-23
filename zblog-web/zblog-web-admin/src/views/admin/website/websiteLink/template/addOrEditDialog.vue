<template>
  <div>
    <el-dialog
      :title="dialogTitle"
      :visible.sync="dialogVisible"
      :fullscreen="isMobile"
      :close-on-click-modal="false"
      :close-on-press-escape="true"
      :before-close="handleClose"
      append-to-body
      width="600px"
    >
      <el-form ref="addOrEditForm" :inline="!isMobile" :model="formData" :rules="formRules" label-width="90px">
        <el-form-item label="链接名称" prop="linkName">
          <el-input v-model="formData.linkName" :style="isMobile ? '' : 'width: 170px;'" maxlength="25" show-word-limit />
        </el-form-item>
        <el-form-item label="链接类型" prop="linkType">
          <el-input v-model="formData.linkType" :style="isMobile ? '' : 'width: 170px;'" maxlength="30" show-word-limit />
        </el-form-item>
        <el-form-item label="链接状态" prop="enabled">
          <el-select v-model="formData.enabled" :style="isMobile ? '' : 'width: 170px;'">
            <el-option v-for="item in enabledList" :key="item.value" :label="item.name" :value="item.value" />
          </el-select>
        </el-form-item>
        <el-form-item label="链接排序" prop="sort">
          <el-input-number
            v-model.number="formData.sort"
            :min="0"
            :max="99999"
            step-strictly
            controls-position="right"
            :style="isMobile ? '' : 'width: 170px;'"
          />
        </el-form-item>
        <el-form-item label="链接地址" prop="linkUrl">
          <el-input v-model="formData.linkUrl" :style="isMobile ? '' : 'width: 445px;'" maxlength="1000" show-word-limit />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="text" @click="cancel">取 消</el-button>
        <el-button :loading="submitLoading" type="primary" @click="submit">确 认</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
export default {
  props: {
    dialogVisible: {
      type: Boolean,
      default: false
    },
    dialogTitle: {
      type: String,
      default: ''
    }
  },
  data() {
    return {
      submitLoading: false,
      enabledList: [
        { name: '启用', value: true },
        { name: '禁用', value: false }
      ],
      formData: {
        id: null,
        linkName: null,
        linkType: null,
        linkUrl: null,
        enabled: true,
        sort: 99999
      },
      formRules: {
        linkName: [
          { required: true, message: '请填写链接名称', trigger: 'blur' }
        ],
        linkType: [
          { required: true, message: '请填写链接类型', trigger: 'blur' }
        ],
        enabled: [
          { required: true, message: '请选择链接状态', trigger: 'change' }
        ],
        sort: [
          { required: true, message: '请输入链接排序', trigger: 'blur' }
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
    initData(dataId) {
      this.$nextTick(() => {
        if (dataId != null && dataId !== '') {
          this.queryDetail(dataId)
        }
      })
    },
    queryDetail(dataId) {
      this.$mapi.websiteLink.queryWebsiteLinkDetail({ websiteLinkId: dataId }).then(res => {
        const { data } = res
        Object.keys(this.formData).forEach(key => {
          this.formData[key] = data[key]
        })
      }).catch(_ => {
        setTimeout(() => { this.doClose() }, 500)
      })
    },
    doClose(result = false) {
      this.formData = {
        id: null,
        linkName: null,
        linkType: null,
        linkUrl: null,
        enabled: true,
        sort: 99999
      }
      this.$refs.addOrEditForm.resetFields()
      this.$emit('addOrEditDone', result)
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
      this.$refs.addOrEditForm.validate((valid) => {
        if (valid) {
          this.submitLoading = true
          if (this.formData.id == null || this.formData.id === '') {
            // add
            this.$mapi.websiteLink.addWebsiteLink(this.formData).then(res => {
              this.$message.success(res.message)
              this.doClose(true)
            }).finally(_ => {
              this.submitLoading = false
            })
          } else {
            // edit
            this.$mapi.websiteLink.editWebsiteLink(this.formData).then(res => {
              this.$message.success(res.message)
              this.doClose(true)
            }).finally(_ => {
              this.submitLoading = false
            })
          }
        }
      })
    }
  }
}
</script>

<style rel="stylesheet/scss" lang="scss" scoped>
::v-deep .el-input-number .el-input__inner {
  text-align: left;
}
</style>
