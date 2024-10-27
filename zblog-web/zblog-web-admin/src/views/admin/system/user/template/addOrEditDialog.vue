<template>
  <div>
    <el-dialog append-to-body :close-on-click-modal="false" :before-close="handleClose" :visible.sync="dialogVisible" :title="dialogTitle" width="520px">
      <el-form ref="addForm" :inline="true" :model="addForm" :rules="addRules" size="small" label-width="80px">
        <el-form-item label="用户名" prop="username">
          <el-input v-model="addForm.username" :disabled="addForm.id != null && addForm.id !== ''" style="width: 380px" maxlength="20" show-word-limit />
        </el-form-item>
        <el-form-item label="邮箱" prop="email">
          <el-input v-model="addForm.email" style="width: 380px" maxlength="100" show-word-limit />
        </el-form-item>
        <el-form-item label="用户状态" prop="enabled">
          <el-select v-model="addForm.enabled" placeholder="用户状态" style="width: 380px">
            <el-option v-for="item in userEnabled" :key="item.value" :label="item.name" :value="item.value" />
          </el-select>
        </el-form-item>
        <el-form-item label="用户类型" prop="temp">
          <el-select v-model="addForm.temp" placeholder="用户类型" style="width: 380px">
            <el-option v-for="item in userType" :key="item.value" :label="item.name" :value="item.value" />
          </el-select>
        </el-form-item>
        <el-form-item v-show="addForm.temp === true" prop="expiredTime" label="到期时间">
          <el-date-picker v-model="addForm.expiredTime" style="width: 380px" type="datetime" placeholder="选择日期时间" value-format="yyyy-MM-dd HH:mm:ss" />
        </el-form-item>
        <el-form-item label="角色" prop="roleIds">
          <el-select v-model="addForm.roleIds" multiple filterable placeholder="请选择" style="width: 380px">
            <el-option v-for="item in roleList" :key="item.id" :label="item.roleName" :value="item.id" />
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
import { validUsername, validEmail } from '@/utils/validate'
export default {
  props: {
    dialogVisible: {
      type: Boolean,
      default: false
    },
    dialogTitle: {
      type: String,
      default: ''
    },
    userInfo: {
      type: Object,
      default: null
    }
  },
  data() {
    const validMail = (rule, value, callback) => {
      if (value === '' || value === null) {
        callback(new Error('请填写邮箱'))
      } else if (validEmail(value)) {
        callback()
      } else {
        callback(new Error('邮箱格式错误'))
      }
    }
    const validUserName = (rule, value, callback) => {
      if (value === '' || value === null) {
        callback(new Error('请填写用户名'))
      } else if (validUsername(value)) {
        callback()
      } else {
        callback(new Error('用户名只允许包含数字和字母, 且长度范围要求为[4, 20]'))
      }
    }
    return {
      addForm: {
        id: '',
        username: '',
        email: '',
        enabled: true,
        temp: false,
        expiredTime: '',
        roleIds: []
      },
      addRules: {
        username: [
          { required: true, validator: validUserName, trigger: 'blur' }
        ],
        email: [
          { required: true, validator: validMail, trigger: 'blur' }
        ],
        enabled: [
          { required: true, message: '请选择用户状态', trigger: 'blur' }
        ],
        temp: [
          { required: true, message: '请选择用户类型', trigger: 'blur' }
        ]
      },
      userEnabled: [
        { name: '启用', value: true },
        { name: '禁用', value: false }
      ],
      userType: [
        { name: '临时用户', value: true },
        { name: '正式用户', value: false }
      ],
      roleList: [],
      submitLoading: false
    }
  },
  methods: {
    initData(dataId) {
      this.getRoleList()
      this.getUserDetail(dataId)
    },
    getRoleList() {
      this.$mapi.role.roleList({}).then(res => {
        this.roleList = res.data
      }).catch(_ => {
        this.roleList = []
      })
    },
    getUserDetail(dataId) {
      if (dataId != null && dataId !== '') {
        this.$mapi.user.queryUserDetail({ userId: dataId }).then(res => {
          const { data } = res
          Object.keys(this.addForm).forEach(key => {
            this.addForm[key] = data[key]
          })
        }).catch(_ => {
          this.addForm = {
            id: '',
            username: '',
            email: '',
            enabled: true,
            temp: false,
            expiredTime: '',
            roleIds: []
          }
        })
      }
    },
    doClose(result = false) {
      this.addForm = {
        id: '',
        username: '',
        email: '',
        enabled: true,
        temp: false,
        expiredTime: '',
        roleIds: []
      }
      this.$refs['addForm'].resetFields()
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
    submit() {
      this.$refs['addForm'].validate((valid) => {
        if (valid) {
          this.submitLoading = true
          if (this.addForm.id == null || this.addForm.id === '') {
            // add
            this.$mapi.user.addUser(this.addForm).then(res => {
              this.$message.success(res.message)
              this.doClose(true)
            }).finally(_ => {
              this.submitLoading = false
            })
          } else {
            // edit
            this.$mapi.user.editUser(this.addForm).then(res => {
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
