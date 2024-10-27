<template>
  <div>
    <el-dialog title="用户详情" append-to-body :close-on-click-modal="true" :before-close="doClose" :visible.sync="dialogVisible" width="550px">
      <el-descriptions direction="vertical" :column="4" border>
        <!-- 1 -->
        <el-descriptions-item label="用户序列"> {{ userInfo.id }} </el-descriptions-item>
        <el-descriptions-item label="用户名"> {{ userInfo.username }} </el-descriptions-item>
        <el-descriptions-item label="昵称"> {{ userInfo.nickname }} </el-descriptions-item>
        <el-descriptions-item label="性别">
          <span v-if="userInfo.gender === '1'">男</span>
          <span v-else-if="userInfo.gender === '2'">女</span>
          <span v-else-if="userInfo.gender === '3'">隐藏</span>
          <span v-else> - </span>
        </el-descriptions-item>
        <!-- 2 -->
        <el-descriptions-item label="用户状态">
          <el-tag v-if="userInfo.enabled === 'true'" size="small">启用</el-tag>
          <el-tag v-else-if="userInfo.enabled === 'false'" size="small" type="warning">禁用</el-tag>
          <span v-else> - </span>
        </el-descriptions-item>
        <el-descriptions-item label="锁定状态">
          <el-tag v-if="userInfo.locked === 'true'" size="small" type="warning">锁定</el-tag>
          <el-tag v-else-if="userInfo.locked === 'false'" size="small">正常</el-tag>
          <span v-else> - </span>
        </el-descriptions-item>
        <el-descriptions-item label="用户类型">
          <el-tag v-if="userInfo.temp === 'true'" size="small" type="warning">临时用户</el-tag>
          <el-tag v-else-if="userInfo.temp === 'false'" size="small">正式用户</el-tag>
          <span v-else> - </span>
        </el-descriptions-item>
        <el-descriptions-item label="过期时间"> {{ userInfo.expiredTime }} </el-descriptions-item>
        <!-- 3 -->
        <el-descriptions-item label="角色" :span="4">
          {{ userInfo.roleNames }}
        </el-descriptions-item>
        <!-- 4 -->
        <el-descriptions-item label="创建用户" :span="2"> {{ userInfo.createUser }} </el-descriptions-item>
        <el-descriptions-item label="创建时间" :span="2"> {{ userInfo.createTime }} </el-descriptions-item>
        <!-- 5 -->
        <el-descriptions-item label="修改用户" :span="2"> {{ userInfo.updateUser }} </el-descriptions-item>
        <el-descriptions-item label="修改时间" :span="2"> {{ userInfo.updateTime }} </el-descriptions-item>
        <!-- 6 -->
        <el-descriptions-item label="个人网站" :span="4"> {{ userInfo.website }} </el-descriptions-item>
        <el-descriptions-item label="个性签名" :span="4"> {{ userInfo.signature }} </el-descriptions-item>
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
      userInfo: {
        id: '',
        username: '',
        nickname: '',
        gender: '',
        enabled: '',
        locked: '',
        temp: '',
        expiredTime: '',
        createUser: '',
        createTime: '',
        updateUser: '',
        updateTime: '',
        website: '',
        signature: '',
        roleNames: []
      }
    }
  },
  methods: {
    initData(dataId) {
      this.$nextTick(() => {
        this.getUserDetail(dataId)
      })
    },
    getUserDetail(dataId) {
      this.$mapi.user.queryUserDetail({ userId: dataId }).then(res => {
        const { data } = res
        Object.keys(this.userInfo).forEach(key => {
          this.userInfo[key] = data[key] == null || data[key] === '' ? '-' : data[key].toString()
        })
      }).catch(_ => {
        Object.keys(this.userInfo).forEach(key => {
          this.userInfo[key] = '-'
        })
      })
    },
    doClose() {
      Object.keys(this.userInfo).forEach(key => {
        this.userInfo[key] = ''
      })
      this.$emit('showDetailDone')
    },
    handleClose() {
      this.$confirm('确认关闭？').then(_ => {
        this.doClose()
      }).catch(_ => {})
    }
  }
}
</script>

<style scoped>

</style>
