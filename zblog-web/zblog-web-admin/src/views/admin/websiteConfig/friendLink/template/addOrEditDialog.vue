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
        <el-form-item label="网站名称" prop="name">
          <el-input v-model="formData.name" :style="isMobile ? '' : 'width: 450px;'" maxlength="100" show-word-limit />
        </el-form-item>
        <el-form-item label="网站地址" prop="url">
          <el-input v-model="formData.url" :style="isMobile ? '' : 'width: 450px;'" maxlength="255" show-word-limit />
        </el-form-item>
        <el-form-item label="网站LOGO" prop="logo">
          <el-input v-model="formData.logo" :style="isMobile ? '' : 'width: 450px;'" maxlength="255" show-word-limit />
        </el-form-item>
        <el-form-item label="友链排序" prop="sort">
          <el-input-number
            v-model.number="formData.sort"
            :min="0"
            :max="99999"
            step-strictly
            controls-position="right"
            :style="isMobile ? '' : 'width: 450px;'"
          />
        </el-form-item>
        <el-form-item label="网站介绍" prop="description">
          <el-input v-model="formData.description" :style="isMobile ? '' : 'width: 450px;'" type="textarea" maxlength="255" show-word-limit />
        </el-form-item>
        <el-form-item label="网站邮箱" prop="email">
          <el-input v-model="formData.email" :style="isMobile ? '' : 'width: 168px;'" maxlength="100" show-word-limit />
        </el-form-item>
        <el-form-item label="联系方式" prop="contact">
          <el-input v-model="formData.contact" :style="isMobile ? '' : 'width: 168px;'" maxlength="100" show-word-limit />
        </el-form-item>
        <el-form-item label="是否展示" prop="isVisible">
          <el-select v-model="formData.isVisible" :style="isMobile ? '' : 'width: 168px;'">
            <el-option v-for="item in visibleList" :key="item.value" :label="item.name" :value="item.value" />
          </el-select>
        </el-form-item>
        <el-form-item label="SEO配置" prop="allowFollow">
          <el-select v-model="formData.allowFollow" :style="isMobile ? '' : 'width: 168px;'">
            <el-option v-for="item in allowFollowList" :key="item.value" :label="item.name" :value="item.value" />
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
      visibleList: [
        { name: '展示', value: true },
        { name: '隐藏', value: false }
      ],
      allowFollowList: [
        { name: '允许传递SEO权重', value: true },
        { name: '禁用SEO', value: false }
      ],
      formData: {
        id: null,
        name: null,
        url: null,
        logo: null,
        description: null,
        email: null,
        contact: null,
        isVisible: true,
        allowFollow: false,
        sort: 0
      },
      formRules: {
        name: [
          { required: true, message: '请填写网站名称', trigger: 'blur' }
        ],
        url: [
          { required: true, message: '请填写网站地址', trigger: 'blur' }
        ],
        logo: [
          { required: true, message: '请填写网站LOGO', trigger: 'blur' }
        ],
        isVisible: [
          { required: true, message: '请选择是否前端展示', trigger: 'change' }
        ],
        allowFollow: [
          { required: true, message: '请选择SEO配置', trigger: 'change' }
        ],
        sort: [
          { required: true, message: '请输入分类排序', trigger: 'blur' }
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
      this.$mapi.friendLink.queryFriendLinkDetail({ friendLinkId: dataId }).then(res => {
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
        url: null,
        logo: null,
        description: null,
        email: null,
        contact: null,
        status: 1,
        isVisible: false,
        allowFollow: false,
        sort: 0
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
            this.$mapi.friendLink.addFriendLink(this.formData).then(res => {
              this.$message.success(res.message)
              this.doClose(true)
            }).finally(_ => {
              this.submitLoading = false
            })
          } else {
            // edit
            this.$mapi.friendLink.editFriendLink(this.formData).then(res => {
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
