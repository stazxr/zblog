<template>
  <div>
    <el-dialog append-to-body :close-on-click-modal="false" :before-close="handleClose" :visible.sync="dialogVisible" :title="dialogTitle" width="540px">
      <el-form ref="addForm" :inline="true" :model="addForm" :rules="addRules" size="small" label-width="80px">
        <el-form-item label="友链名称" prop="name">
          <el-input v-model="addForm.name" style="width: 360px" maxlength="50" show-word-limit />
        </el-form-item>
        <el-form-item label="友链地址" prop="linkUrl">
          <el-input v-model="addForm.linkUrl" style="width: 360px" maxlength="1000" show-word-limit />
        </el-form-item>
        <el-form-item label="头像地址">
          <el-input v-model="addForm.headUrl" style="width: 295px" maxlength="1000" show-word-limit />
          <el-button type="primary" @click="showHeadUrl">查 看</el-button>
        </el-form-item>
        <el-form-item label="友链介绍">
          <el-input v-model="addForm.linkRemark" type="textarea" rows="4" maxlength="200" show-word-limit style="width: 360px" />
        </el-form-item>
        <el-form-item label="友链状态" prop="valid">
          <el-select v-model="addForm.valid" placeholder="友链状态" style="width: 360px">
            <el-option v-for="item in validEnums" :key="item.value" :label="item.name" :value="item.value" />
          </el-select>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="text" @click="cancel">取 消</el-button>
        <el-button :loading="submitLoading" type="primary" @click="submit">确 认</el-button>
      </div>
    </el-dialog>

    <el-dialog :visible.sync="previewDialogVisible" style="padding-bottom: 100px;">
      <img width="100%" :src="addForm.headUrl" alt="图片不存在">
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
      previewDialogVisible: false,
      submitLoading: false,
      validEnums: [
        { name: '有效', value: true },
        { name: '无效', value: false }
      ],
      addForm: {
        id: '',
        name: '',
        headUrl: '',
        linkUrl: '',
        linkRemark: '',
        valid: true
      },
      addRules: {
        name: [
          { required: true, message: '请输入友链名称', trigger: 'blur' }
        ],
        linkUrl: [
          { required: true, message: '请输入友链地址', trigger: 'blur' }
        ],
        valid: [
          { required: true, message: '请选择友链状态', trigger: 'blur' }
        ]
      }
    }
  },
  methods: {
    initData(dataId) {
      this.$nextTick(() => {
        this.getFriendLinkDetail(dataId)
      })
    },
    getFriendLinkDetail(dataId) {
      if (dataId != null && dataId !== '') {
        this.$mapi.friendLink.queryFriendLinkDetail({ friendLinkId: dataId }).then(res => {
          const { data } = res
          Object.keys(this.addForm).forEach(key => {
            this.addForm[key] = data[key]
          })
        }).catch(_ => {
          this.doClose()
        })
      }
    },
    doClose(result = false) {
      this.addForm = {
        id: '',
        name: '',
        headUrl: '',
        linkUrl: '',
        linkRemark: '',
        valid: true
      }
      this.$refs['addForm'].resetFields()
      this.previewDialogVisible = false
      this.submitLoading = false
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
    showHeadUrl() {
      if (this.addForm.headUrl === '') {
        this.$message.warning('请填写头像地址')
        return
      }

      this.previewDialogVisible = true
    },
    submit() {
      this.$refs['addForm'].validate((valid) => {
        if (valid) {
          this.submitLoading = true
          if (this.addForm.id == null || this.addForm.id === '') {
            // add
            this.$mapi.friendLink.addFriendLink(this.addForm).then(res => {
              this.$message.success(res.message)
              this.doClose(true)
            }).finally(_ => {
              this.submitLoading = false
            })
          } else {
            // edit
            this.$mapi.friendLink.editFriendLink(this.addForm).then(res => {
              this.$message.success(res.message)
              this.doClose(true)
            }).finally(_ => {
              this.submitLoading = false
            })
          }
        } else {
          return false
        }
      })
    }
  }
}
</script>

<style rel="stylesheet/scss" lang="scss" scoped>

</style>
