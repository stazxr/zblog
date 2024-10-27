<template>
  <el-form :model="otherInfo" size="small" label-width="120px" label-position="left">
    <el-row style="width:600px">
      <el-col :md="12">
        <el-form-item label="用户头像">
          <el-upload
            ref="userUpload"
            name="file"
            class="avatar-uploader"
            :action="$store.state.api.fileUploadApi"
            :headers="headers"
            :show-file-list="false"
            :before-upload="beforeUpload"
            :on-error="handleError"
            :on-success="handleUserAvatarUploadSuccess"
          >
            <img v-if="otherInfo.userAvatar" :src="otherInfo.userAvatar" class="avatar" alt="">
            <i v-else class="el-icon-plus avatar-uploader-icon" />
          </el-upload>
        </el-form-item>
      </el-col>
      <el-col :md="12">
        <el-form-item label="游客头像">
          <el-upload
            ref="touristUpload"
            name="file"
            class="avatar-uploader"
            :action="$store.state.api.fileUploadApi"
            :headers="headers"
            :show-file-list="false"
            :before-upload="beforeUpload"
            :on-error="handleError"
            :on-success="handleTouristAvatarUploadSuccess"
          >
            <img v-if="otherInfo.touristAvatar" :src="otherInfo.touristAvatar" class="avatar" alt="">
            <i v-else class="el-icon-plus avatar-uploader-icon" />
          </el-upload>
        </el-form-item>
      </el-col>
    </el-row>
    <el-form-item label="默认文章封面">
      <el-upload
        ref="articleCoverUpload"
        class="cover-uploader"
        :action="$store.state.api.fileUploadApi"
        :headers="headers"
        :show-file-list="false"
        :before-upload="beforeUpload"
        :on-error="handleError"
        :on-success="handleArticleCoverAvatarUploadSuccess"
      >
        <img v-if="otherInfo.articleCover" :src="otherInfo.articleCover" class="cover" alt="">
        <i v-else class="el-icon-plus cover-uploader-icon" />
      </el-upload>
    </el-form-item>
    <el-form-item label="文章浏览配置">
      <el-input-number v-model.number="otherInfo.articleViewInterval" :min="-1" :max="1440" placeholder="[-1, 1440]，单位分钟" step-strictly controls-position="right" style="width: 200px" />
      <br><span style="padding-top: 10px;color: gray">同一 IP 访问同一篇文章时，新增文章浏览数的时间间隔，单位分钟，-1 代表浏览数不重复增加，0 代表每次访问都增加</span>
    </el-form-item>
    <el-form-item label="文章搜索策略">
      <el-radio-group v-model="otherInfo.articleSearchStrategy">
        <el-radio label="mysql">MySQL</el-radio>
      </el-radio-group>
    </el-form-item>
    <el-form-item label="评论审核">
      <el-radio-group v-model="otherInfo.isCommentReview">
        <el-radio :label="0">关闭</el-radio>
        <el-radio :label="1">开启</el-radio>
      </el-radio-group>
    </el-form-item>
    <el-form-item label="留言审核">
      <el-radio-group v-model="otherInfo.isMessageReview">
        <el-radio :label="0">关闭</el-radio>
        <el-radio :label="1">开启</el-radio>
      </el-radio-group>
    </el-form-item>
    <el-form-item label="邮箱通知">
      <el-radio-group v-model="otherInfo.isEmailNotice">
        <el-radio :label="0">关闭</el-radio>
        <el-radio :label="1">开启</el-radio>
      </el-radio-group>
    </el-form-item>
    <el-form-item label="打赏状态">
      <el-radio-group v-model="otherInfo.isReward">
        <el-radio :label="0">关闭</el-radio>
        <el-radio :label="1">开启</el-radio>
      </el-radio-group>
    </el-form-item>
    <el-row v-show="otherInfo.isReward === 1" style="width:600px;">
      <el-col :md="12">
        <el-form-item label="微信收款码">
          <el-upload
            ref="weiXinUpload"
            name="file"
            class="avatar-uploader"
            :action="$store.state.api.fileUploadApi"
            :headers="headers"
            :show-file-list="false"
            :before-upload="beforeUpload"
            :on-error="handleError"
            :on-success="handleWeiXinUploadSuccess"
          >
            <img v-if="otherInfo.weiXinQrCode" :src="otherInfo.weiXinQrCode" class="avatar" alt="">
            <i v-else class="el-icon-plus avatar-uploader-icon" />
          </el-upload>
        </el-form-item>
      </el-col>
      <el-col :md="12">
        <el-form-item label="支付宝收款码">
          <el-upload
            ref="alipayUpload"
            name="file"
            class="avatar-uploader"
            :action="$store.state.api.fileUploadApi"
            :headers="headers"
            :show-file-list="false"
            :before-upload="beforeUpload"
            :on-error="handleError"
            :on-success="handleAlipayUploadSuccess"
          >
            <img v-if="otherInfo.alipayQrCode" :src="otherInfo.alipayQrCode" class="avatar" alt="">
            <i v-else class="el-icon-plus avatar-uploader-icon" />
          </el-upload>
        </el-form-item>
      </el-col>
    </el-row>
    <el-form-item label="音乐播放器状态">
      <el-radio-group v-model="otherInfo.isMusicPlayer">
        <el-radio :label="0">关闭</el-radio>
        <el-radio :label="1">开启</el-radio>
      </el-radio-group>
    </el-form-item>
    <el-button v-perm="['updateOtherInfo']" v-loading="submitLoading" type="primary" size="medium" style="margin-left:6.3rem" @click="updateOtherInfo">
      修改
    </el-button>
  </el-form>
</template>

<script>
import { getToken } from '@/utils/token'
export default {
  data() {
    return {
      headers: {
        Authorization: ''
      },
      submitLoading: false,
      otherInfo: {
        userAvatar: '',
        touristAvatar: '',
        articleCover: '',
        isEmailNotice: '',
        articleViewInterval: '',
        articleSearchStrategy: '',
        isCommentReview: '',
        isMessageReview: '',
        isReward: '',
        weiXinQrCode: '',
        alipayQrCode: '',
        isMusicPlayer: ''
      }
    }
  },
  methods: {
    initData() {
      this.$nextTick(() => {
        this.queryOtherInfo()
      })
    },
    queryOtherInfo() {
      this.$mapi.webSetting.queryOtherInfo().then(res => {
        const { data } = res
        Object.keys(this.otherInfo).forEach(key => {
          this.otherInfo[key] = data[key]
        })
      })
    },
    updateOtherInfo() {
      this.submitLoading = true
      this.$mapi.webSetting.updateOtherInfo(this.otherInfo).then(res => {
        this.$message.success(res.message)
      }).finally(_ => {
        this.submitLoading = false
      })
    },
    beforeUpload(file) {
      // 支持类型：.jpg,.jpeg,.png
      if (file.name.indexOf('.') !== -1) {
        const imgType = file.name.substring(file.name.lastIndexOf('.') + 1).toLowerCase()
        if (imgType !== 'jpg' && imgType !== 'jpeg' && imgType !== 'png') {
          this.$message.warning('上传文件只能是 jpg, jpeg, png 格式!')
          return false
        }
      } else {
        this.$message.warning('上传文件只能是 jpg, jpeg, png 格式!')
        return false
      }

      // 最大支持 5M
      const isLt2M = file.size / 1024 / 1024 < 5
      if (!isLt2M) {
        this.$message.warning('上传图片大小不能超过 5MB!')
        return false
      }

      this.headers.Authorization = getToken()
    },
    handleError(err) {
      try {
        this.$message.error(JSON.parse(err.message.toString()).message)
      } catch {
        this.$message.error('系统发生未知错误')
      }
    },
    handleUserAvatarUploadSuccess(response, file) {
      if (response.code === 200) {
        // success
        if (response.data && Array.isArray(response.data) && response.data.length > 0) {
          this.otherInfo.userAvatar = response.data[0]['downloadUrl']
        }

        this.$message.success(response.message || '上传成功')
      } else {
        // error
        this.$refs.userUpload.handleError(response, file)
      }
    },
    handleTouristAvatarUploadSuccess(response, file) {
      if (response.code === 200) {
        // success
        if (response.data && Array.isArray(response.data) && response.data.length > 0) {
          this.otherInfo.touristAvatar = response.data[0]['downloadUrl']
        }

        this.$message.success(response.message || '上传成功')
      } else {
        // error
        this.$refs.touristUpload.handleError(response, file)
      }
    },
    handleArticleCoverAvatarUploadSuccess(response, file) {
      if (response.code === 200) {
        // success
        if (response.data && Array.isArray(response.data) && response.data.length > 0) {
          this.otherInfo.articleCover = response.data[0]['downloadUrl']
        }

        this.$message.success(response.message || '上传成功')
      } else {
        // error
        this.$refs.articleCoverUpload.handleError(response, file)
      }
    },
    handleWeiXinUploadSuccess(response, file) {
      if (response.code === 200) {
        // success
        if (response.data && Array.isArray(response.data) && response.data.length > 0) {
          this.otherInfo.weiXinQrCode = response.data[0]['downloadUrl']
        }

        this.$message.success(response.message || '上传成功')
      } else {
        // error
        this.$refs.weiXinUpload.handleError(response, file)
      }
    },
    handleAlipayUploadSuccess(response, file) {
      if (response.code === 200) {
        // success
        if (response.data && Array.isArray(response.data) && response.data.length > 0) {
          this.otherInfo.alipayQrCode = response.data[0]['downloadUrl']
        }

        this.$message.success(response.message || '上传成功')
      } else {
        // error
        this.$refs.alipayUpload.handleError(response, file)
      }
    }
  }
}
</script>

<style scoped>
::v-deep .avatar-uploader .el-upload {
  border: 1px dashed #d9d9d9;
  border-radius: 6px;
  cursor: pointer;
  position: relative;
  overflow: hidden;
}
::v-deep .avatar-uploader .el-upload:hover {
  border-color: #409eff;
}
::v-deep .avatar-uploader-icon {
  font-size: 28px;
  color: #8c939d;
  width: 120px;
  height: 120px;
  line-height: 120px;
  text-align: center;
}
::v-deep .avatar {
  width: 120px;
  height: 120px;
  display: block;
}
::v-deep .cover-uploader .el-upload {
  border: 1px dashed #d9d9d9;
  border-radius: 6px;
  cursor: pointer;
  position: relative;
  overflow: hidden;
}
::v-deep .cover-uploader .el-upload:hover {
  border-color: #409eff;
}
::v-deep .cover-uploader-icon {
  font-size: 28px;
  color: #8c939d;
  width: 300px;
  height: 160px;
  line-height: 160px;
  text-align: center;
}
::v-deep .cover {
  width: 300px;
  height: 160px;
  display: block;
}
::v-deep .el-input-number .el-input__inner {
  text-align: left;
}
</style>
