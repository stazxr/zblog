<template>
  <div>
    <el-form title="网站信息" :model="webInfo" :rules="addRules" size="small" label-width="100px" label-position="right">
      <el-form-item label="网站头像">
        <el-upload
          ref="upload"
          name="file"
          class="avatar-uploader"
          :action="$store.state.api.fileUploadApi"
          :headers="headers"
          :show-file-list="false"
          :before-upload="beforeUpload"
          :on-error="handleError"
          :on-success="handleSuccess"
        >
          <img v-if="webInfo.websiteAvatar" :src="webInfo.websiteAvatar" class="avatar" alt="">
          <i v-else class="el-icon-plus avatar-uploader-icon" />
        </el-upload>
      </el-form-item>
      <el-form-item label="网站名称" prop="websiteName">
        <el-input v-model="webInfo.websiteName" style="width:400px" placeholder="网站名称" maxlength="20" show-word-limit />
      </el-form-item>
      <el-form-item label="网站作者" prop="websiteAuthor">
        <el-input v-model="webInfo.websiteAuthor" style="width:400px" placeholder="网站作者" />
      </el-form-item>
      <el-form-item label="网站介绍">
        <el-input v-model="webInfo.websiteIntro" style="width:400px" placeholder="网站介绍" />
      </el-form-item>
      <el-form-item label="网站前台链接">
        <el-input v-model="webInfo.websiteLink" style="width:400px" placeholder="网站前台链接" />
      </el-form-item>
      <el-form-item label="网站后台链接">
        <el-input v-model="webInfo.websiteAdminLink" style="width:400px" placeholder="网站后台链接" />
      </el-form-item>
      <el-form-item label="关于我链接">
        <el-input v-model="webInfo.readMeLink" style="width:400px" placeholder="关于我链接" />
      </el-form-item>
      <el-form-item label="创建日期" prop="websiteCreateTime">
        <el-date-picker v-model="webInfo.websiteCreateTime" type="date" value-format="yyyy-MM-dd" placeholder="创建日期" style="width:400px" />
      </el-form-item>
      <el-form-item label="网站公告">
        <el-input v-model="webInfo.websiteNotice" type="textarea" :rows="4" placeholder="网站公告" style="width:400px" />
      </el-form-item>
      <el-form-item label="网站备案号">
        <el-input v-model="webInfo.websiteRecordNo" style="width:400px" placeholder="网站备案号" />
      </el-form-item>
      <el-form-item label="第三方登录">
        <el-checkbox-group v-model="webInfo.socialLoginList">
          <el-checkbox label="qq">QQ</el-checkbox>
        </el-checkbox-group>
      </el-form-item>
    </el-form>
    <el-form v-show="webInfo.socialLoginList.indexOf('qq') !== -1" title="QQ 第三方登录配置" :model="webInfo" size="small" label-width="100px" label-position="right">
      <el-form-item label="APP ID">
        <el-input v-model="webInfo.qqAppId" style="width:400px" placeholder="QQ APP ID" />
      </el-form-item>
      <el-form-item label="APP Key">
        <el-input v-model="webInfo.qqAppKey" style="width:400px" placeholder="QQ APP Key" />
      </el-form-item>
      <el-form-item label="回调地址">
        <el-input v-model="webInfo.qqCallBackUrl" style="width:400px" placeholder="回调地址" />
      </el-form-item>
    </el-form>
    <el-button v-perm="['updateWebInfo']" v-loading="submitLoading" type="primary" size="medium" style="margin-left:6.3rem" @click="updateWebInfo">
      修改
    </el-button>
  </div>
</template>

<script>
import { getToken } from '@/utils/token'
export default {
  data() {
    return {
      submitLoading: false,
      headers: {
        Authorization: ''
      },
      webInfo: {
        websiteAvatar: '',
        websiteName: '',
        websiteAuthor: '',
        websiteCreateTime: '',
        websiteIntro: '',
        websiteLink: '',
        websiteAdminLink: '',
        readMeLink: '',
        websiteNotice: '',
        websiteRecordNo: '',
        socialLoginList: [],
        qqAppId: '',
        qqAppKey: '',
        qqCallBackUrl: ''
      },
      addRules: {
        websiteName: [
          { required: true, message: '请填写网站名称', trigger: 'blur' }
        ],
        websiteAuthor: [
          { required: true, message: '请填写网站作者', trigger: 'blur' }
        ],
        websiteCreateTime: [
          { required: true, message: '请选择网站创建时间', trigger: 'blur' }
        ]
      }
    }
  },
  methods: {
    initData() {
      this.$nextTick(() => {
        this.queryWebInfo()
      })
    },
    queryWebInfo() {
      this.$mapi.webSetting.queryWebInfo().then(res => {
        const { data } = res
        Object.keys(this.webInfo).forEach(key => {
          this.webInfo[key] = data[key]
        })
      })
    },
    updateWebInfo() {
      this.submitLoading = true
      this.$mapi.webSetting.updateWebInfo(this.webInfo).then(res => {
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
    handleSuccess(response, file) {
      if (response.code === 200) {
        // success
        if (response.data && Array.isArray(response.data) && response.data.length > 0) {
          this.webInfo.websiteAvatar = response.data[0]['downloadUrl']
        }

        this.$message.success(response.message || '上传成功')
      } else {
        // error
        this.$refs.upload.handleError(response, file)
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
</style>
