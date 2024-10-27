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
            <div style="text-align: center">
              <div class="el-upload">
                <img :src="user && user['headImgUrl'] !== '' ? user['headImgUrl'] : Avatar" title="点击上传头像" class="avatar" alt="" @click="toggleShow">
                <img-upload
                  ref="imgUploadRef"
                  v-model="showImgUpload"
                  field="file"
                  :headers="headers"
                  :url="$store.state.api.fileUploadApi"
                  :lang-ext="langExt"
                  @crop-upload-success="cropUploadSuccess"
                />
              </div>
            </div>
            <ul class="user-info">
              <li>
                <div style="height: 100%">
                  <svg-icon icon-class="login" /> 登录账号<div class="user-right">{{ user['username'] }}</div>
                </div>
              </li>
              <li><svg-icon icon-class="nickname" /> 用户昵称 <div class="user-right">{{ user['nickname'] }}</div></li>
              <li><svg-icon icon-class="email" /> 用户邮箱 <div class="user-right">{{ user['email'] }}</div></li>
              <li>
                <svg-icon icon-class="security" /> 安全设置
                <div class="user-right">
                  <a @click="$refs.pass.dialog = true">修改密码</a>
                  <a @click="$refs.email.dialog = true">修改邮箱</a>
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
          <el-tabs v-model="activeName" @tab-click="handleClick">
            <el-tab-pane label="用户资料" name="first">
              <el-form ref="form" :model="form" :rules="rules" style="margin-top: 10px;" size="small" label-width="70px">
                <el-form-item label="昵称" prop="nickname">
                  <el-input v-model="form.nickname" style="width: 35%" maxlength="25" show-word-limit />
                  <span style="color: #C0C0C0;margin-left: 10px;">用户昵称不作为登录使用</span>
                </el-form-item>
                <el-form-item label="手机号" prop="telephone">
                  <el-input v-model="form.telephone" style="width: 35%;" />
                  <span style="color: #C0C0C0;margin-left: 10px;">手机号不能重复</span>
                </el-form-item>
                <el-form-item label="性别">
                  <el-radio-group v-model.number="form.gender" style="width: 35%;">
                    <el-radio :label="1">帅哥</el-radio>
                    <el-radio :label="2">美女</el-radio>
                    <el-radio :label="3">不显示</el-radio>
                  </el-radio-group>
                </el-form-item>
                <el-form-item label="个人网站">
                  <el-input v-model="form.website" style="width: 35%;" />
                </el-form-item>
                <el-form-item label="签名">
                  <el-input v-model="form.signature" type="textarea" style="width: 35%;" rows="4" maxlength="100" show-word-limit />
                </el-form-item>
                <el-form-item>
                  <el-button :loading="saveLoading" size="small" type="primary" @click="doSubmit">保存配置</el-button>
                </el-form-item>
              </el-form>
            </el-tab-pane>
            <!-- 操作日志 -->
            <el-tab-pane label="操作日志" name="second">
              <el-table v-loading="tableLoading" :data="tableData" border style="width: 100%;">
                <el-table-column type="expand">
                  <template v-slot="props">
                    <el-form label-position="left" inline class="demo-table-expand">
                      <el-form-item class="el-form-item" label="日志序号:">
                        <span>{{ props.row['id'] }}</span>
                      </el-form-item>
                      <el-form-item class="el-form-item" label="日志类型:">
                        <span v-if="props.row['logType'] === 1">操作日志</span>
                        <span v-else-if="props.row['logType'] === 2">接口日志</span>
                        <span v-else-if="props.row['logType'] === 3">异常日志</span>
                        <span v-else> - </span>
                      </el-form-item>
                      <el-form-item class="el-form-item" label="请求地址:">
                        <span>{{ props.row['requestIp'] }}</span>
                      </el-form-item>
                      <el-form-item class="el-form-item" label="请求来源:">
                        <span>{{ props.row['address'] }}</span>
                      </el-form-item>
                      <el-form-item class="el-form-item" label="描述信息:" style="width: 100%">
                        <span>{{ props.row['execMessage'] }}</span>
                      </el-form-item>
                    </el-form>
                  </template>
                </el-table-column>
                <el-table-column :show-overflow-tooltip="true" prop="description" label="请求内容" />
                <el-table-column :show-overflow-tooltip="true" prop="browser" label="浏览器" />
                <el-table-column :show-overflow-tooltip="true" prop="execResult" label="请求结果" align="center">
                  <template v-slot="scope">
                    <el-tag v-if="scope.row['logType'] === 3" size="small" type="danger">系统错误</el-tag>
                    <el-tag v-else-if="!scope.row['execResult']" size="small" type="warning">失败</el-tag>
                    <el-tag v-else size="small">成功</el-tag>
                  </template>
                </el-table-column>
                <el-table-column :show-overflow-tooltip="true" prop="costTime" label="请求耗时" align="center">
                  <template v-slot="scope">
                    <span v-if="scope.row['costTime'] === null"> - </span>
                    <el-tag v-else-if="scope.row['costTime'] <= 500">{{ scope.row['costTime'] }}ms</el-tag>
                    <el-tag v-else-if="scope.row['costTime'] <= 3000" type="warning">{{ scope.row['costTime'] }}ms</el-tag>
                    <el-tag v-else type="danger">{{ scope.row['costTime'] }}ms</el-tag>
                  </template>
                </el-table-column>
                <el-table-column :show-overflow-tooltip="true" align="right">
                  <template slot="header">
                    <div style="display:inline-block;float: right;cursor: pointer;white-space: nowrap" @click="listTableData">
                      创建日期<i class="el-icon-refresh" style="margin-left: 40px" />
                    </div>
                  </template>
                  <template v-slot="scope">
                    <i class="el-icon-time" />
                    <span>{{ scope.row['eventTime'] }}</span>
                  </template>
                </el-table-column>
              </el-table>
              <!-- 分页组件 -->
              <el-pagination
                :total="total"
                :current-page="page"
                style="margin-top: 8px;"
                layout="total, prev, pager, next, sizes"
                @size-change="sizeChange"
                @current-change="pageChange"
              />
            </el-tab-pane>
          </el-tabs>
        </el-card>
      </el-col>
    </el-row>
    <updateEmail ref="email" :email="user.email" />
    <updatePass ref="pass" />
  </div>
</template>

<script>
import Avatar from '@/assets/images/avatar.png'
import updatePass from './center/updatePass'
import updateEmail from './center/updateEmail'
import ImgUpload from 'vue-image-crop-upload'
import { isValidPhone } from '@/utils/validate'
import { getToken } from '@/utils/token'
import { mapGetters } from 'vuex'

export default {
  name: 'UserCenter',
  components: { updatePass, updateEmail, ImgUpload },
  data() {
    // 自定义验证
    const validPhone = (rule, value, callback) => {
      if (value && !isValidPhone(value)) {
        callback(new Error('请输入正确的11位手机号码'))
      } else {
        callback()
      }
    }
    return {
      activeName: 'first',
      Avatar: Avatar,
      showImgUpload: false,
      langExt: {
        success: '上传成功!',
        fail: '图片上传失败!'
      },
      headers: {
        Authorization: ''
      },
      form: {},
      rules: {
        nickname: [
          { required: true, message: '请输入用户昵称', trigger: 'blur' },
          { min: 1, max: 25, message: '长度在 2 到 25 个字符', trigger: 'blur' }
        ],
        telephone: [
          { required: false, trigger: 'blur', validator: validPhone }
        ]
      },
      saveLoading: false,
      tableData: [],
      tableLoading: false,
      total: 0,
      page: 1,
      pageSize: 10
    }
  },
  computed: {
    ...mapGetters([
      'user'
    ])
  },
  created() {
    this.$store.dispatch('GetUserInfo').then(() => {
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
    handleClick(tab) {
      if (tab.name === 'second') {
        this.listTableData()
      }
    },
    listTableData() {
      const param = {
        logType: 1,
        username: this.user.username,
        page: this.page,
        pageSize: this.pageSize
      }
      this.tableLoading = true
      this.$mapi.log.queryUserLog(param).then(res => {
        const { data } = res
        this.tableData = data.list
        this.total = data.total
      }).catch(_ => {
        this.tableData = []
        this.total = 0
      }).finally(() => {
        this.tableLoading = false
      })
    },
    sizeChange(size) {
      this.page = 1
      this.pageSize = size
      this.listTableData()
    },
    pageChange(page) {
      this.page = page
      this.listTableData()
    },
    cropUploadSuccess(jsonData) {
      const { code, data, message } = jsonData
      if (code === 200) {
        // success
        const param = {
          id: this.user.id,
          headImg: data[0]['downloadUrl']
        }
        this.$mapi.user.updateUserHeadImg(param).then(() => {
          this.$message.success('头像修改成功')
          this.$store.dispatch('GetUserInfo')
          this.$refs.imgUploadRef.off()
        })
      } else {
        // error
        this.$refs.imgUploadRef.loading = 3
        this.$refs.imgUploadRef.hasError = true
        this.$refs.imgUploadRef.errorMsg = message
      }
    },
    toggleShow() {
      this.headers.Authorization = getToken()

      this.$refs.imgUploadRef.step = 1
      this.showImgUpload = !this.showImgUpload
    },
    doSubmit() {
      if (this.$refs['form']) {
        this.$refs['form'].validate((valid) => {
          if (valid) {
            this.saveLoading = true
            this.$mapi.user.updateUserBaseInfo(this.form).then(() => {
              this.$message.success('修改成功')
              this.$store.dispatch('GetUserInfo').then(() => {})
            }).finally(() => {
              this.saveLoading = false
            })
          }
        })
      }
    }
  }
}
</script>

<style rel="stylesheet/scss" lang="scss">
  .avatar {
    width: 120px;
    height: 120px;
    border-radius: 50%;
  }
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
  .demo-table-expand {
    font-size: 0;
  }
  .demo-table-expand label {
    width: 90px;
    color: #99a9bf;
  }
  .demo-table-expand .el-form-item {
    padding-left: 10px;
    margin-right: 0;
    margin-bottom: 0;
    width: 50%;
  }
</style>
