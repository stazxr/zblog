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
        <el-form-item label="用户名" prop="username">
          <el-input v-model="formData.username" :disabled="formData.id != null" :style="isMobile ? '' : 'width: 380px;'" maxlength="20" show-word-limit />
        </el-form-item>
        <el-form-item label="邮箱" prop="email">
          <el-input v-model="formData.email" :style="isMobile ? '' : 'width: 380px;'" maxlength="100" show-word-limit />
        </el-form-item>
        <el-form-item label="用户类型" prop="userType">
          <el-select v-model="formData.userType" :style="isMobile ? '' : 'width: 380px;'" @change="userTypeChangeEvent">
            <el-option v-for="item in userTypeList" :key="item.value" :label="item.name" :value="item.value" />
          </el-select>
        </el-form-item>
        <el-form-item v-show="formData.userType === 4" label="到期时间" prop="expireTime">
          <el-date-picker v-model="formData.expireTime" :style="isMobile ? '' : 'width: 380px;'" type="datetime" placeholder="选择用户到期时间" value-format="yyyy-MM-dd HH:mm:ss" />
        </el-form-item>
        <el-form-item label="用户状态" prop="userStatus">
          <el-select v-model="formData.userStatus" :style="isMobile ? '' : 'width: 380px;'">
            <el-option v-for="item in userStatusList" :key="item.value" :label="item.name" :value="item.value" />
          </el-select>
        </el-form-item>
        <el-form-item label="角色" prop="roleIds">
          <el-select v-model="formData.roleIds" multiple filterable placeholder="请选择角色列表" :style="isMobile ? '' : 'width: 380px;'">
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
import { mapGetters } from 'vuex'
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
      roleList: [],
      submitLoading: false,
      userTypeList: [],
      userStatusList: [
        /* { name: '锁定', value: 2 }, */
        { name: '正常', value: 0 },
        { name: '禁用', value: 1 }
      ],
      formData: {
        id: null,
        username: null,
        email: null,
        userType: null,
        userStatus: 0,
        expireTime: null,
        roleIds: []
      },
      formRules: {
        username: [
          { required: true, validator: validUserName, trigger: 'blur' }
        ],
        email: [
          { required: true, validator: validMail, trigger: 'blur' }
        ],
        userType: [
          { required: true, message: '请选择用户类型', trigger: 'change' }
        ],
        userStatus: [
          { required: true, message: '请选择用户状态', trigger: 'change' }
        ],
        expireTime: [
          { required: false, message: '请选择用户到期时间', trigger: 'blur' }
        ]
      }
    }
  },
  computed: {
    isMobile() {
      return this.$store.state.app.device === 'mobile'
    },
    ...mapGetters([
      'user'
    ])
  },
  methods: {
    initData(dataId) {
      this.getRoleList()
      this.getUserTypeList()
      if (dataId != null && dataId !== '') {
        this.$nextTick(() => {
          this.getUserDetail(dataId)
        })
      }
    },
    getRoleList() {
      this.$mapi.role.roleList().then(res => {
        this.roleList = res.data
      }).catch(_ => {
        this.roleList = []
      })
    },
    getUserTypeList() {
      // 测试用户 > 测试用户
      if (this.user != null) {
        const userType = this.user.userType
        if (userType === 2) {
          // 管理员 > 管理员,普通用户,测试用户,临时用户
          this.userTypeList = [
            { name: '系统用户', value: 0 },
            { name: '普通用户', value: 1 },
            { name: '管理员', value: 2 },
            { name: '测试用户', value: 3 },
            { name: '临时用户', value: 4 }
          ]
        } else if (userType === 1) {
          // 普通用户 > 普通用户,测试用户,临时用户
          this.userTypeList = [
            { name: '普通用户', value: 1 },
            { name: '测试用户', value: 3 },
            { name: '临时用户', value: 4 }
          ]
        } else if (userType === 3) {
          // 测试用户 > 测试用户
          this.userTypeList = [
            { name: '测试用户', value: 3 }
          ]
        } else if (userType === 4) {
          // 临时用户 > 临时用户
          this.userTypeList = [
            { name: '临时用户', value: 4 }
          ]
        }
      } else {
        this.userTypeList = []
      }
    },
    getUserDetail(dataId) {
      this.$mapi.user.queryUserDetail({ userId: dataId }).then(res => {
        const { data } = res
        Object.keys(this.formData).forEach(key => {
          this.formData[key] = data[key]
        })
      }).catch(_ => {
        this.doClose()
      })
    },
    userTypeChangeEvent(userType) {
      if (userType === 4) {
        this.formRules.expireTime[0].required = true
      } else {
        this.formData.expireTime = null
        this.formRules.expireTime[0].required = false
      }
    },
    submit() {
      this.$refs.addOrEditForm.validate((valid) => {
        if (valid) {
          this.submitLoading = true
          if (this.formData.id === null) {
            // add
            this.$mapi.user.addUser(this.formData).then(res => {
              this.$message.success(res.message)
              this.doClose(true)
            }).finally(_ => {
              this.submitLoading = false
            })
          } else {
            // edit
            this.$mapi.user.editUser(this.formData).then(res => {
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
        username: null,
        email: null,
        userType: null,
        userStatus: 0,
        expireTime: null,
        roleIds: []
      }
      this.$refs.addOrEditForm.resetFields()
      this.submitLoading = false
      this.$emit('addOrEditDone', result)
    }
  }
}
</script>

<style scoped>

</style>
