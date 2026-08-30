<template>
  <div>
    <el-dialog
      title="友链审核"
      :visible.sync="dialogVisible"
      :fullscreen="isMobile"
      :close-on-click-modal="false"
      :close-on-press-escape="true"
      :before-close="handleClose"
      append-to-body
      width="520px"
    >
      <el-form ref="auditForm" :inline="!isMobile" :model="formData" :rules="formRules" label-width="80px">
        <el-form-item label="审核结果" prop="auditStatus">
          <el-select v-model="formData.status" :style="isMobile ? '' : 'width: 380px;'" placeholder="审核结果">
            <el-option v-for="item in statusList" :key="item.value" :label="item.name" :value="item.value" />
          </el-select>
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
      statusList: [],
      formData: {
        id: null,
        status: null
      },
      formRules: {
        status: [
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
        this.queryFriendLinkDetail(dataId)
      })
      this.loadStatusList()
    },
    queryFriendLinkDetail(dataId) {
      this.$mapi.friendLink.queryFriendLinkDetail({ friendLinkId: dataId }).then(res => {
        const { data } = res
        this.formData.id = data.id
      }).catch(_ => {
        this.doClose()
      })
    },
    loadStatusList() {
      this.$mapi.communal.queryConfListByDictKey({ dictKey: 'FRIEND_LINK_STATUS_CONFIG' }).then(res => {
        const { data } = res
        this.statusList = data
      }).catch(_ => {
        this.statusList = []
      })
    },
    submit() {
      this.$refs.auditForm.validate((valid) => {
        if (valid) {
          this.submitLoading = true
          const param = {
            friendLinkId: this.formData.id,
            status: this.formData.status
          }
          this.$mapi.friendLink.auditFriendLink(param).then(res => {
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
        status: null
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
