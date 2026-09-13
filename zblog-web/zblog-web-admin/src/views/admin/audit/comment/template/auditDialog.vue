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
        <el-form-item label="评论内容" prop="content">
          <div class="audit-content" v-html="formData.content || '-'" />
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
        this.queryCommentDetail(dataId)
      })
      this.loadAuditStatusList()
    },
    queryCommentDetail(dataId) {
      this.$mapi.comment.queryCommentDetail({ commentId: dataId }).then(res => {
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
            commentId: this.formData.id,
            status: this.formData.auditStatus,
            reason: this.formData.auditReason
          }
          this.$mapi.comment.auditComment(param).then(res => {
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
.audit-content {
  width: 380px;
  min-height: 80px;
  max-height: 240px;
  padding: 10px 12px;
  overflow-y: auto;
  box-sizing: border-box;
  border: 1px solid #dcdfe6;
  border-radius: 4px;
  background: #f5f7fa;
  color: #606266;
  line-height: 24px;
  word-break: break-word;
}

.audit-content >>> img {
  max-width: 48px;
  max-height: 48px;
  margin: 2px 4px;
  border-radius: 4px;
  vertical-align: middle;
  object-fit: contain;
}

@media screen and (max-width: 768px) {
  .audit-content {
    width: 100%;
  }
}
</style>
