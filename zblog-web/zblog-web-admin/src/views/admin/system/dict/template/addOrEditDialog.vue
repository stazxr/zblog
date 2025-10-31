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
      width="580px"
    >
      <el-form ref="addOrEditForm" :inline="!isMobile" :model="formData" :rules="formRules" label-width="100px">
        <el-form-item label="字典名称" prop="dictName">
          <el-input v-model="formData.dictName" :style="isMobile ? '' : 'width: 152px;'" maxlength="50" show-word-limit />
        </el-form-item>
        <el-form-item label="字典排序" prop="dictSort">
          <el-input-number
            v-model.number="formData.dictSort"
            :style="isMobile ? '' : 'width: 152px;'"
            :min="0"
            :max="99999"
            step-strictly
            controls-position="right"
          />
        </el-form-item>
        <el-form-item v-if="formData.dictType === 2" label="字典KEY" prop="dictKey">
          <el-input v-model="formData.dictKey" :style="isMobile ? '' : 'width: 152px;'" maxlength="50" show-word-limit />
        </el-form-item>
        <el-form-item v-if="formData.dictType === 2" label="字典状态" prop="enabled">
          <el-select v-model="formData.enabled" :style="isMobile ? '' : 'width: 152px;'" placeholder="是否启用">
            <el-option v-for="item in enabledList" :key="item.value" :label="item.name" :value="item.value" />
          </el-select>
        </el-form-item>
        <el-form-item v-if="formData.dictType === 2" label="字典VALUE" prop="dictValue">
          <el-input
            v-model="formData.dictValue"
            :style="isMobile ? '' : 'width: 420px;'"
            type="textarea"
            :autosize="{ minRows: 4, maxRows: 4 }"
          />
        </el-form-item>
        <el-form-item label="字典描述" prop="dictDesc">
          <el-input
            v-model="formData.dictDesc"
            :style="isMobile ? '' : 'width: 420px;'"
            type="textarea"
            resize="none"
            :autosize="{ minRows: 4, maxRows: 4 }"
            maxlength="100"
            show-word-limit
          />
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
      enabledList: [],
      formData: {
        id: null,
        pid: null,
        dictName: null,
        dictType: null,
        dictKey: null,
        dictValue: null,
        dictSort: 99999,
        dictDesc: null,
        enabled: 'true'
      },
      formRules: {
        dictName: [
          { required: true, message: '请输入字典名称', trigger: 'blur' }
        ],
        dictSort: [
          { required: true, message: '请输入字典排序', trigger: 'blur' }
        ],
        dictKey: [
          { required: true, message: '请输入字典KEY', trigger: 'blur' }
        ],
        enabled: [
          { required: true, message: '请选择字典状态', trigger: 'change' }
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
    initData(type, pid, dataId) {
      if (dataId != null && dataId !== '') {
        this.$nextTick(() => {
          this.getDictDetail(dataId)
        })
      } else {
        this.formData.pid = pid
        this.formData.dictType = type
      }
      this.loadEnabledList()
    },
    getDictDetail(dataId) {
      this.$mapi.dict.queryDictDetail({ dictId: dataId }).then(res => {
        const { data } = res
        Object.keys(this.formData).forEach(key => {
          this.formData[key] = data[key]
        })
      }).catch(_ => {
        setTimeout(() => { this.doClose() }, 500)
      })
    },
    loadEnabledList() {
      this.$mapi.dict.queryConfListByDictKey({ dictKey: 'ENABLED_CONFIG' }).then(res => {
        const { data } = res
        this.enabledList = data
      }).catch(_ => {
        this.enabledList = []
      })
    },
    submit() {
      this.$refs.addOrEditForm.validate((valid) => {
        if (valid) {
          this.submitLoading = true
          if (this.formData.id === null) {
            // add
            this.$mapi.dict.addDict(this.formData).then(res => {
              this.$message.success(res.message)
              this.doClose(true)
            }).finally(_ => {
              this.submitLoading = false
            })
          } else {
            // edit
            this.$mapi.dict.editDict(this.formData).then(res => {
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
      const type = this.formData.dictType
      this.formData = {
        id: null,
        pid: null,
        dictName: null,
        dictType: null,
        dictKey: null,
        dictValue: null,
        dictSort: 99999,
        dictDesc: null,
        enabled: 'true'
      }
      this.$refs.addOrEditForm.resetFields()
      this.submitLoading = false
      this.$emit('addOrEditDone', result, type)
    }
  }
}
</script>

<style rel="stylesheet/scss" lang="scss" scoped>
::v-deep .el-input-number .el-input__inner {
  text-align: left;
}
</style>
