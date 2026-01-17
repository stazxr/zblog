<template>
  <div class="app-container">
    <el-row :gutter="20">
      <!-- 左部分 -->
      <el-col :xs="24" :sm="24" :md="8" :lg="6" :xl="5" style="margin-bottom: 10px">
        <el-card class="box-card">
          <div slot="header" class="clearfix">
            <span>个人信息</span>
          </div>
          <div>
            <div class="avatar-wrapper">
              <div class="el-upload">
                <img :src="avatarUrl" class="avatar" title="点击上传头像" alt="" @click="showAvatarUpload">
                <img-upload
                  ref="imgUploadRef"
                  v-model="showImgUpload"
                  field="file"
                  :headers="headers"
                  :url="$store.state.api.fileUploadApi"
                  :lang-ext="imageUploadLangExt"
                  @crop-upload-success="avatarUploadSuccess"
                />
              </div>
            </div>
            <ul class="user-info">
              <li>用户账号
                <div class="user-right">
                  {{ user.username }}
                </div>
              </li>
              <li>用户昵称
                <div class="user-right">
                  {{ user.nickname }}
                </div>
              </li>
              <li>用户邮箱
                <div class="user-right">
                  {{ user.email }}
                </div>
              </li>
              <li>用户类型
                <div class="user-right">
                  <span v-if="user.userType === 0">系统用户</span>
                  <span v-else-if="user.userType === 1">普通用户</span>
                  <span v-else-if="user.userType === 2">管理员</span>
                  <span v-else-if="user.userType === 3">测试用户</span>
                  <span v-else-if="user.userType === 4">临时用户</span>
                  <span v-else>未知</span>
                </div>
              </li>
              <li>用户状态
                <div class="user-right">
                  <span v-if="user.userStatus === 0">正常</span>
                  <span v-else-if="user.userStatus === 1">禁用</span>
                  <span v-else-if="user.userStatus === 2">锁定</span>
                  <span v-else>未知</span>
                </div>
              </li>
              <li>上次登录
                <div class="user-right">
                  {{ user.lastLoginAddress }} · {{ user.lastLoginTime }}
                </div>
              </li>
              <li>
                安全设置
                <div class="user-right">
                  <el-tag size="mini" effect="plain" class="clickable-tag" @click="$refs.updatePassDialog.dialogVisible = true">
                    修改密码
                  </el-tag>
                  <el-tag size="mini" type="warning" class="clickable-tag" effect="plain" @click="$refs.updateEmailDialog.dialogVisible = true">
                    修改邮箱
                  </el-tag>
                </div>
              </li>
            </ul>
          </div>
        </el-card>
      </el-col>
      <!-- 右部分 -->
      <el-col :xs="24" :sm="24" :md="16" :lg="18" :xl="19">
        <!-- 用户资料 -->
        <el-card class="box-card">
          <el-tabs v-model="activeTabName" @tab-click="handleTabClick">
            <el-tab-pane label="用户资料" name="first">
              <user-profile-form ref="userProfileFormRef" style="margin-top: 10px;" />
            </el-tab-pane>
            <el-tab-pane label="操作日志" name="second">
              <user-operate-log ref="userOperateLogRef" style="margin-top: 10px;" />
            </el-tab-pane>
          </el-tabs>
        </el-card>
      </el-col>
    </el-row>
    <!-- 修改密码 -->
    <update-pass-dialog ref="updatePassDialog" />
    <!-- 修改邮箱 -->
    <update-email-dialog ref="updateEmailDialog" />
  </div>
</template>

<script>
import DefaultAvatar from '@/assets/images/default-avatar.png'
import updatePassDialog from './center/updatePassDialog'
import updateEmailDialog from './center/updateEmailDialog'
import userProfileForm from './center/userProfileForm.vue'
import userOperateLog from './center/userOperateLog.vue'
import ImgUpload from 'vue-image-crop-upload'
import { getToken } from '@/utils/token'
import { mapGetters } from 'vuex'
export default {
  name: 'UserCenter',
  components: { updatePassDialog, updateEmailDialog, userProfileForm, userOperateLog, ImgUpload },
  data() {
    return {
      DefaultAvatar: DefaultAvatar,
      headers: {
        // 图片上传请求头配置
        Authorization: ''
      },
      imageUploadLangExt: {
        success: '上传成功!',
        fail: '图片上传失败!'
      },
      showImgUpload: false,
      activeTabName: 'first'
    }
  },
  computed: {
    ...mapGetters([
      'user'
    ]),
    avatarUrl() {
      return this.user && this.user.headImgUrl ? this.user.headImgUrl : this.DefaultAvatar
    }
  },
  created() {
    this.$store.dispatch('RefreshUser').then(() => {
      this.form = {
        id: this.user.id,
        nickname: this.user.nickname,
        gender: this.user.gender,
        telephone: this.user.telephone,
        signature: this.user.signature,
        website: this.user.website
      }
    })
  },
  methods: {
    // 修改头像
    showAvatarUpload() {
      this.headers.Authorization = getToken()
      this.$refs.imgUploadRef.step = 1
      this.showImgUpload = true
    },
    avatarUploadSuccess(jsonData) {
      const { code, data, message } = jsonData
      if (code === 200) {
        // success
        if (data == null || data.length !== 1) {
          this.$message.error('上传文件列表获取失败')
          this.$refs.imgUploadRef.loading = 3
          this.$refs.imgUploadRef.hasError = true
          this.$refs.imgUploadRef.errorMsg = '上传文件列表获取失败'
          return
        }
        const param = {
          userId: this.user.id,
          fileId: data[0]['fileId']
        }
        this.$mapi.userCenter.updateUserHeadImg(param).then(() => {
          this.$message.success('头像修改成功')
          this.$store.dispatch('RefreshUser')
          this.$refs.imgUploadRef.off()
        }).catch(_ => {
          this.$refs.imgUploadRef.off()
        })
      } else {
        // error
        this.$refs.imgUploadRef.loading = 3
        this.$refs.imgUploadRef.hasError = true
        this.$refs.imgUploadRef.errorMsg = message || '头像上传失败'
      }
    },
    // 标签页切换
    handleTabClick(tab) {
      if (tab.name === 'second') {
        this.$refs.userOperateLogRef.listTableData()
      }
    }
  }
}
</script>

<style rel="stylesheet/scss" lang="scss">
// 操作按钮
.clickable-tag {
  cursor: pointer;
  user-select: none;
}
.clickable-tag:hover {
  opacity: 0.85;
}
// 头像
.avatar-wrapper {
  text-align: center;
}
.avatar {
  width: 120px;
  height: 120px;
  border-radius: 50%;
}
// 用户信息
.user-info {
  padding-left: 0;
  list-style: none;
  li {
    border-bottom: 1px solid #F0F3F4;
    padding: 11px 0;
    font-size: 13px;
  }
  .user-right {
    float: right;
    a {
      color: #317EF3;
    }
  }
}
</style>
