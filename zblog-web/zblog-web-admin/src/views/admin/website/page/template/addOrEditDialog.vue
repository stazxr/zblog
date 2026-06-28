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
      width="520px"
    >
      <el-form ref="addOrEditForm" :inline="!isMobile" :model="formData" :rules="formRules" label-width="80px">
        <el-form-item label="页面名称" prop="pageName">
          <el-input v-model="formData.pageName" :style="isMobile ? '' : 'width: 380px;'" maxlength="25" show-word-limit />
        </el-form-item>
        <el-form-item label="页面标识" prop="pageLabel">
          <el-input v-model="formData.pageLabel" :style="isMobile ? '' : 'width: 380px;'" maxlength="25" show-word-limit />
        </el-form-item>
        <el-form-item label="展示模式" prop="displayMode">
          <el-select v-model="formData.displayMode" :style="isMobile ? '' : 'width: 380px;'" placeholder="展示模式">
            <el-option v-for="item in displayModeList" :key="item.value" :label="item.name" :value="item.value" />
          </el-select>
        </el-form-item>
        <el-form-item label="页面排序" prop="pageSort">
          <el-input-number v-model.number="formData.pageSort" :min="0" :max="99999" :style="isMobile ? '' : 'width: 380px;'" step-strictly controls-position="right" />
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
      displayModeList: [],
      formData: {
        id: null,
        pageName: null,
        pageLabel: null,
        displayMode: 'BANNER',
        pageSort: 99999
      },
      formRules: {
        pageName: [
          { required: true, message: '请输入页面名称', trigger: 'blur' }
        ],
        pageLabel: [
          { required: true, message: '请输入页面标识', trigger: 'blur' }
        ],
        displayMode: [
          { required: true, message: '请选择页面展示模式', trigger: 'change' }
        ],
        pageSort: [
          { required: true, message: '请选择页面排序', trigger: 'change' }
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
      if (dataId != null && dataId !== '') {
        this.$nextTick(() => {
          this.getPageDetail(dataId)
        })
      }
      this.loadDisplayModeList()
    },
    getPageDetail(dataId) {
      this.$mapi.page.queryPageDetail({ pageId: dataId }).then(res => {
        const { data } = res
        Object.keys(this.formData).forEach(key => {
          this.formData[key] = data[key]
        })
      }).catch(_ => {
        this.doClose()
      })
    },
    loadDisplayModeList() {
      this.$mapi.communal.queryConfListByDictKey({ dictKey: 'PAGE_DISPLAY_MODE_CONFIG' }).then(res => {
        const { data } = res
        this.displayModeList = data
      }).catch(_ => {
        this.displayModeList = []
      })
    },
    submit() {
      this.$refs.addOrEditForm.validate((valid) => {
        if (valid) {
          this.submitLoading = true
          if (this.formData.id === null) {
            // add
            this.$mapi.page.addPage(this.formData).then(res => {
              this.$message.success(res.message)
              this.doClose(true)
            }).finally(_ => {
              this.submitLoading = false
            })
          } else {
            // edit
            this.$mapi.page.editPage(this.formData).then(res => {
              this.$message.success(res.message)
              this.doClose(true)
            }).finally(_ => {
              this.submitLoading = false
            })
          }
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
        pageName: null,
        pageLabel: null,
        displayMode: 'BANNER',
        pageSort: 99999
      }
      this.$refs.addOrEditForm.resetFields()
      this.submitLoading = false
      this.$emit('addOrEditDone', result)
    }
  }
}
</script>

<style scoped>
::v-deep .el-input-number .el-input__inner {
  text-align: left;
}
</style>
