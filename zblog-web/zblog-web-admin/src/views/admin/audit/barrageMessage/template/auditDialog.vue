<template>
  <div>
    <el-dialog
      title="弹幕审核"
      :visible.sync="dialogVisible"
      :fullscreen="isMobile"
      :close-on-click-modal="false"
      :close-on-press-escape="true"
      :before-close="handleClose"
      append-to-body
      width="520px"
    >
      <el-form ref="auditForm" :inline="!isMobile" :model="formData" :rules="formRules" label-width="80px">
        <el-form-item label="弹幕内容" prop="content">
          <el-input v-model="formData.content" :style="isMobile ? '' : 'width: 380px;'" readonly />
        </el-form-item>
        <el-form-item label="审核结果" prop="auditStatus">
          <el-select v-model="formData.auditStatus" :style="isMobile ? '' : 'width: 380px;'" placeholder="审核结果">
            <el-option v-for="item in auditStatusList" :key="item.value" :label="item.name" :value="item.value" />
          </el-select>
        </el-form-item>
        <el-form-item label="备注" prop="auditReason">
          <el-input v-model="formData.auditReason" :style="isMobile ? '' : 'width: 380px;'" type="textarea" maxlength="200" show-word-limit />
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
    }
  },
  data() {
    return {
      submitLoading: false,
      auditStatusList: [],
      formData: {
        id: null,
        content: null,
        auditStatus: null,
        auditReason: null
      },
      formRules: {
        auditStatus: [
          { required: true, message: '请选择审核结果', trigger: 'blur' }
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
        this.queryBarrageMessageDetail(dataId)
      })
      this.loadAuditStatusList()
    },
    queryBarrageMessageDetail(dataId) {
      this.$mapi.barrageMessage.queryBarrageMessageDetail({ barrageMessageId: dataId }).then(res => {
        const { data } = res
        this.formData.id = data.id
        this.formData.content = data.content
      }).catch(_ => {
        this.doClose()
      })
    },
    loadAuditStatusList() {
      this.$mapi.communal.queryConfListByDictKey({ dictKey: 'AUDIT_STATUS_1_CONFIG' }).then(res => {
        const { data } = res
        this.auditStatusList = data
      }).catch(_ => {
        this.auditStatusList = []
      })
    },
    submit() {
      this.$refs.auditForm.validate((valid) => {
        if (valid) {
          this.submitLoading = true
          const param = {
            barrageMessageId: this.formData.id,
            auditStatus: this.formData.auditStatus,
            auditReason: this.formData.auditReason
          }
          this.$mapi.barrageMessage.auditBarrageMessage(param).then(res => {
            this.$message.success(res.message)
            this.doClose(true)
          }).finally(_ => {
            this.submitLoading = false
          })
        }
      })
    },
    cancel() {
      this.handleClose()
    },
    handleClose() {
      if (!this.submitLoading) {
        this.$confirm('是否确认关闭弹窗？').then(_ => {
          this.doClose()
        }).catch(_ => {})
      }
    },
    doClose(result = false) {
      this.formData = {
        id: null,
        content: null,
        auditStatus: null,
        auditReason: null
      }
      this.$refs.auditForm.resetFields()
      this.submitLoading = false
      this.$emit('auditDone', result)
    }
  }
}
</script>

<style scoped>

</style>
