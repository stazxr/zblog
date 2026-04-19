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
      <el-form ref="addOrEditForm" :inline="!isMobile" :model="formData" :rules="formRules" label-width="100px">
        <el-form-item label="标签名称" prop="name">
          <el-input v-model="formData.name" :style="isMobile ? '' : 'width: 168px;'" maxlength="50" show-word-limit />
        </el-form-item>
        <el-form-item label="SLUG" prop="slug">
          <el-input v-model="formData.slug" :style="isMobile ? '' : 'width: 168px;'" maxlength="100" show-word-limit />
        </el-form-item>
        <el-form-item label="SEO标题" prop="seoTitle">
          <el-input v-model="formData.seoTitle" :style="isMobile ? '' : 'width: 450px;'" maxlength="255" show-word-limit />
        </el-form-item>
        <el-form-item label="SEO关键词" prop="seoKeywords">
          <el-input v-model="formData.seoKeywords" :style="isMobile ? '' : 'width: 450px;'" maxlength="255" show-word-limit />
        </el-form-item>
        <el-form-item label="SEO描述" prop="seoDescription">
          <el-input v-model="formData.seoDescription" :style="isMobile ? '' : 'width: 450px;'" maxlength="500" show-word-limit />
        </el-form-item>
        <el-form-item label="收录状态" prop="allowIndex">
          <el-select v-model="formData.allowIndex" placeholder="收录状态" :style="isMobile ? '' : 'width: 168px;'">
            <el-option v-for="item in allowIndexEnums" :key="item.value" :label="item.name" :value="item.value" />
          </el-select>
        </el-form-item>
        <el-form-item label="标签状态" prop="enabled">
          <el-select v-model="formData.enabled" placeholder="标签状态" :style="isMobile ? '' : 'width: 168px;'">
            <el-option v-for="item in enabledEnums" :key="item.value" :label="item.name" :value="item.value" />
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
    },
    dialogTitle: {
      type: String,
      default: ''
    }
  },
  data() {
    return {
      submitLoading: false,
      allowIndexEnums: [
        { name: '收录', value: true },
        { name: '禁止', value: false }
      ],
      enabledEnums: [
        { name: '启用', value: true },
        { name: '禁用', value: false }
      ],
      formData: {
        id: null,
        name: null,
        slug: null,
        seoTitle: null,
        seoKeywords: null,
        seoDescription: null,
        allowIndex: false,
        enabled: true
      },
      formRules: {
        name: [
          { required: true, message: '请输入标签名称', trigger: 'blur' }
        ],
        slug: [
          { required: true, message: '请输入标签唯一标识', trigger: 'blur' }
        ],
        allowIndex: [
          { required: true, message: '请选择是否允许搜索引擎收录', trigger: 'change' }
        ],
        enabled: [
          { required: true, message: '请选择标签状态', trigger: 'change' }
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
      this.$mapi.tag.queryTagDetail({ tagId: dataId }).then(res => {
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
        name: null,
        slug: null,
        seoTitle: null,
        seoKeywords: null,
        seoDescription: null,
        allowIndex: false,
        enabled: true
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
            this.$mapi.tag.addTag(this.formData).then(res => {
              this.$message.success(res.message)
              this.doClose(true)
            }).finally(_ => {
              this.submitLoading = false
            })
          } else {
            // edit
            this.$mapi.tag.editTag(this.formData).then(res => {
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

</style>
