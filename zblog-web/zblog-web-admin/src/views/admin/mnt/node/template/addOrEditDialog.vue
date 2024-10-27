<template>
  <div>
    <el-dialog append-to-body :close-on-click-modal="false" :before-close="handleClose" :visible.sync="dialogVisible" :title="dialogTitle" width="580px">
      <el-form ref="addForm" :inline="true" :model="addForm" :rules="addRules" size="small" label-width="80px">
        <el-form-item label="节点名称" prop="name">
          <el-input v-model="addForm.name" maxlength="100" show-word-limit style="width: 450px" />
        </el-form-item>
        <el-form-item label="IP地址" prop="ip">
          <el-input v-model="addForm.ip" maxlength="100" show-word-limit style="width: 178px" />
        </el-form-item>
        <el-form-item label="端口" prop="port">
          <el-input-number v-model.number="addForm.port" :min="0" :max="999999" step-strictly controls-position="right" style="width: 178px" />
        </el-form-item>
        <el-form-item label="登录用户" prop="sshUser">
          <el-input v-model="addForm.sshUser" maxlength="30" show-word-limit style="width: 178px" />
        </el-form-item>
        <el-form-item label="登录密码" prop="sshPwd">
          <el-input type="password" style="display: none" />
          <el-input v-model="addForm.sshPwd" type="password" style="width: 178px" autocomplete="new-password" />
        </el-form-item>
        <el-form-item label="备注信息" prop="desc">
          <el-input v-model="addForm.desc" type="textarea" maxlength="100" show-word-limit style="width: 450px" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button :loading="sshTestLoading" type="success" style="float: left" @click="sshTest">连接测试</el-button>
        <el-button @click="cancel">取 消</el-button>
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
      addForm: {
        id: '',
        name: '',
        ip: '',
        port: 22,
        sshUser: '',
        sshPwd: '',
        desc: ''
      },
      addRules: {
        name: [
          { required: true, message: '请填写节点名称', trigger: 'blur' }
        ],
        ip: [
          { required: true, message: '请填写IP地址', trigger: 'blur' }
        ],
        port: [
          { required: true, message: '请填写端口', trigger: 'blur' }
        ],
        sshUser: [
          { required: true, message: '请填写登录用户', trigger: 'blur' }
        ],
        sshPwd: [
          { required: true, message: '请填写登录密码', trigger: 'blur' }
        ]
      },
      sshTestLoading: false,
      submitLoading: false
    }
  },
  methods: {
    initData(dataId) {
      this.addForm.id = dataId
      this.getNodeInfo()
    },
    getNodeInfo() {
      if (this.addForm.id != null && this.addForm.id !== '') {
        this.$mapi.node.queryNodeDetail({ nodeId: this.addForm.id }).then(res => {
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
        ip: '',
        port: 22,
        sshUser: '',
        sshPwd: '',
        desc: ''
      }
      this.$refs['addForm'].resetFields()
      this.sshTestLoading = false
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
    sshTest() {
      this.$refs['addForm'].validate((valid) => {
        if (valid) {
          const param = {
            ip: this.addForm.ip,
            port: this.addForm.port,
            user: this.addForm.sshUser,
            password: this.addForm.sshPwd
          }
          this.sshTestLoading = true
          this.$mapi.node.sshTest(param).then(res => {
            this.$message.success(res.message)
          }).finally(_ => {
            this.sshTestLoading = false
          })
        } else {
          this.$message.warning('请先填写节点信息')
          return false
        }
      })
    },
    submit() {
      this.$refs['addForm'].validate((valid) => {
        if (valid) {
          this.submitLoading = true
          if (this.addForm.id == null || this.addForm.id === '') {
            // add
            this.$mapi.node.addNode(this.addForm).then(res => {
              this.$message.success(res.message)
              this.doClose(true)
            }).finally(_ => {
              this.submitLoading = false
            })
          } else {
            // edit
            this.$mapi.node.editNode(this.addForm).then(res => {
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
::v-deep .el-input-number .el-input__inner {
  text-align: left;
}
</style>
