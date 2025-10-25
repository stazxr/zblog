<template>
  <div>
    <el-dialog
      title="用户详情"
      :visible.sync="dialogVisible"
      :fullscreen="device === 'mobile'"
      :destroy-on-close="true"
      :close-on-click-modal="true"
      :close-on-press-escape="true"
      :before-close="handleClose"
      append-to-body
      width="600px"
    >
      <el-descriptions direction="vertical" :column="4" border>
        <!-- 1 -->
        <el-descriptions-item label="序列"> {{ dataInfo.id }} </el-descriptions-item>
        <el-descriptions-item label="用户名"> {{ dataInfo.username }} </el-descriptions-item>
        <el-descriptions-item label="昵称"> {{ dataInfo.nickname }} </el-descriptions-item>
        <el-descriptions-item label="邮箱"> {{ dataInfo.email }} </el-descriptions-item>
        <!-- 2 -->
        <el-descriptions-item label="用户类型">
          <span v-if="dataInfo.userType === '0'">系统用户</span>
          <span v-else-if="dataInfo.userType === '1'">普通用户</span>
          <span v-else-if="dataInfo.userType === '2'">管理员用户</span>
          <span v-else-if="dataInfo.userType === '3'">测试用户</span>
          <span v-else-if="dataInfo.userType === '4'">临时用户</span>
          <span v-else> - </span>
        </el-descriptions-item>
        <el-descriptions-item label="用户状态">
          <span v-if="dataInfo.userStatus === '0'" style="color: green;">正常</span>
          <span v-else-if="dataInfo.userStatus === '1'" style="color: red;">禁用</span>
          <span v-else-if="dataInfo.userStatus === '2'" style="color: yellow;">锁定</span>
          <span v-else> - </span>
        </el-descriptions-item>
        <el-descriptions-item label="用户性别">
          <span v-if="dataInfo.gender === '1'">男</span>
          <span v-else-if="dataInfo.gender === '2'">女</span>
          <span v-else-if="dataInfo.gender === '3'">隐藏</span>
          <span v-else> - </span>
        </el-descriptions-item>
        <el-descriptions-item label="过期时间"> {{ dataInfo.expiredTime }} </el-descriptions-item>
        <!-- 3 -->
        <el-descriptions-item label="登录时间"> {{ dataInfo.lastLoginTime }} </el-descriptions-item>
        <el-descriptions-item label="登录渠道">
          <span v-if="dataInfo.loginChan === '01'">移动端</span>
          <span v-else-if="dataInfo.loginChan === '02'">PC端</span>
          <span v-else> - </span>
        </el-descriptions-item>
        <el-descriptions-item label="登录方式">
          <span v-if="dataInfo.loginType === '00'">访客</span>
          <span v-else-if="dataInfo.loginType === '01'">密码</span>
          <span v-else-if="dataInfo.loginType === '02'">QQ互信</span>
          <span v-else-if="dataInfo.loginType === '99'">未知</span>
          <span v-else> - </span>
        </el-descriptions-item>
        <el-descriptions-item label="登录地址"> {{ dataInfo.loginAddress }} </el-descriptions-item>
        <!-- 4 -->
        <el-descriptions-item label="用户代理" :span="4">
          {{ dataInfo.userAgent }}
        </el-descriptions-item>
        <!-- 5 -->
        <el-descriptions-item label="角色" :span="4">
          {{ dataInfo.roleNames ? dataInfo.roleNames : '-' }}
        </el-descriptions-item>
        <!-- 6 -->
        <el-descriptions-item label="创建用户" :span="2"> {{ dataInfo.createUsername }} </el-descriptions-item>
        <el-descriptions-item label="创建时间" :span="2"> {{ dataInfo.createTime }} </el-descriptions-item>
        <!-- 7 -->
        <el-descriptions-item label="修改用户" :span="2"> {{ dataInfo.updateUsername }} </el-descriptions-item>
        <el-descriptions-item label="修改时间" :span="2"> {{ dataInfo.updateTime }} </el-descriptions-item>
        <!-- 8 -->
        <el-descriptions-item label="个人网站" :span="4"> {{ dataInfo.website }} </el-descriptions-item>
        <el-descriptions-item label="个性签名" :span="4"> {{ dataInfo.signature }} </el-descriptions-item>
      </el-descriptions>
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
      dataInfo: {
        id: '',
        username: '',
        userType: '',
        userStatus: '',
        expiredTime: '',
        changePwdTime: '',
        nickname: '',
        email: '',
        gender: '',
        signature: '',
        website: '',
        headImgUrl: '',
        lastLoginTime: '',
        loginChan: '',
        loginType: '',
        loginAddress: '',
        userAgent: '',
        createUsername: '',
        createTime: '',
        updateUsername: '',
        updateTime: '',
        roleNames: []
      }
    }
  },
  computed: {
    device() {
      return this.$store.state.app.device
    }
  },
  methods: {
    initData(userId) {
      this.$nextTick(() => {
        this.queryDetail(userId)
      })
    },
    queryDetail(dataId) {
      this.$mapi.user.queryUserDetail({ userId: dataId }).then(res => {
        const { data } = res
        Object.keys(this.dataInfo).forEach(key => {
          this.dataInfo[key] = data[key] == null || data[key] === '' ? '-' : data[key].toString()
        })
      }).catch(_ => {
        this.doClose()
      })
    },
    doClose() {
      Object.keys(this.dataInfo).forEach(key => {
        this.dataInfo[key] = ''
      })
      this.$emit('showDetailDone')
    },
    handleClose() {
      this.doClose()
    }
  }
}
</script>

<style scoped>

</style>
