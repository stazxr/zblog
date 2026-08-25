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
          <el-input v-model="formData.name" :style="isMobile ? '' : 'width: 168px;'" maxlength="100" show-word-limit />
        </el-form-item>
        <el-form-item label="友链类型" prop="linkType">
          <el-select v-model="formData.linkType" :style="isMobile ? '' : 'width: 168px;'">
            <el-option v-for="item in linkTypeList" :key="item.value" :label="item.name" :value="item.value" />
          </el-select>
        </el-form-item>
        <el-form-item label="网站地址" prop="url">
          <el-input v-model="formData.url" :style="isMobile ? '' : 'width: 450px;'" maxlength="500" show-word-limit />
        </el-form-item>
        <el-form-item label="网站LOGO" prop="logo">
          <el-input v-model="formData.logo" :style="isMobile ? '' : 'width: 450px;'" maxlength="500" show-word-limit />
        </el-form-item>
        <el-form-item label="网站简介" prop="description">
          <el-input
            v-model="formData.description"
            :style="isMobile ? '' : 'width: 450px;'"
            placeholder="一句话总结"
            type="textarea"
            maxlength="255"
            show-word-limit
          />
        </el-form-item>
        <el-form-item label="网站邮箱" prop="email">
          <el-input v-model="formData.email" :style="isMobile ? '' : 'width: 168px;'" maxlength="100" show-word-limit />
        </el-form-item>
        <el-form-item label="联系方式" prop="contact">
          <el-input v-model="formData.contact" :style="isMobile ? '' : 'width: 168px;'" maxlength="100" show-word-limit />
        </el-form-item>
        <el-form-item label="前台展示" prop="isVisible">
          <el-select v-model="formData.isVisible" :style="isMobile ? '' : 'width: 168px;'">
            <el-option v-for="item in visibleList" :key="item.value" :label="item.name" :value="item.value" />
          </el-select>
        </el-form-item>
        <el-form-item label="SEO配置" prop="allowFollow">
          <el-select v-model="formData.allowFollow" :style="isMobile ? '' : 'width: 168px;'">
            <el-option v-for="item in allowFollowList" :key="item.value" :label="item.name" :value="item.value" />
          </el-select>
        </el-form-item>
        <el-form-item label="健康检测" prop="checkEnabled">
          <el-select v-model="formData.checkEnabled" :style="isMobile ? '' : 'width: 168px;'">
            <el-option v-for="item in checkEnabledList" :key="item.value" :label="item.name" :value="item.value" />
          </el-select>
        </el-form-item>
        <el-form-item label="优先级" prop="sort">
          <el-input-number
            v-model.number="formData.sort"
            :min="0"
            :max="99999"
            step-strictly
            controls-position="right"
            :style="isMobile ? '' : 'width: 168px;'"
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
      linkTypeList: [
        // 因开源而相遇，共同成长
        { name: '开源伙伴', value: 1 },
        // 一些值得驻足与分享的优秀站点
        { name: '特别推荐', value: 2 },
        // 感谢相遇，愿我们在各自的路上共同前行
        { name: '同行友站', value: 3 }
      ],
      visibleList: [
        { name: '展示', value: true },
        { name: '隐藏', value: false }
      ],
      allowFollowList: [
        { name: '启用', value: true },
        { name: '禁用', value: false }
      ],
      checkEnabledList: [
        { name: '开启', value: true },
        { name: '关闭', value: false }
      ],
      formData: {
        id: null,
        name: null,
        url: null,
        logo: null,
        linkType: 3,
        description: null,
        email: null,
        contact: null,
        isVisible: true,
        allowFollow: false,
        checkEnabled: false,
        sort: 0
      },
      formRules: {
        name: [
          { required: true, message: '请填写网站名称', trigger: 'blur' }
        ],
        linkType: [
          { required: true, message: '请选择友链类型', trigger: 'change' }
        ],
        url: [
          { required: true, message: '请填写网站地址', trigger: 'blur' }
        ],
        isVisible: [
          { required: true, message: '请选择是否前台展示', trigger: 'change' }
        ],
        allowFollow: [
          { required: true, message: '请选择SEO配置', trigger: 'change' }
        ],
        checkEnabled: [
          { required: true, message: '请选择是否开启健康监测', trigger: 'change' }
        ],
        sort: [
          { required: true, message: '请填写优先级', trigger: 'blur' }
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
        linkType: 3,
        description: null,
        email: null,
        contact: null,
        isVisible: true,
        allowFollow: false,
        checkEnabled: false,
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
