<template>
  <div>
    <el-dialog append-to-body :close-on-click-modal="false" :before-close="handleClose" :visible.sync="dialogVisible" :title="dialogTitle" width="540px">
      <el-form ref="addForm" :inline="true" :model="addForm" :rules="addRules" size="small" label-width="80px">
        <el-form-item label="页面名称" prop="pageName">
          <el-input v-model="addForm.pageName" style="width:360px" />
        </el-form-item>
        <el-form-item label="页面标签" prop="pageLabel">
          <el-input v-model="addForm.pageLabel" style="width:360px" />
        </el-form-item>
        <el-form-item label="页面排序">
          <el-input-number v-model.number="addForm.pageSort" :min="0" :max="99999" step-strictly controls-position="right" style="width: 360px" />
        </el-form-item>
        <el-form-item label="页面封面">
          <el-upload
            ref="upload"
            name="file"
            class="upload-cover"
            drag
            :action="$store.state.api.fileUploadApi"
            :headers="headers"
            :show-file-list="false"
            :before-upload="beforeUpload"
            :on-error="handleError"
            :on-success="handleSuccess"
          >
            <i v-if="addForm.pageCover === ''" class="el-icon-upload" />
            <div v-if="addForm.pageCover === ''" class="el-upload__text">
              将文件拖到此处，或<em>点击上传</em>
            </div>
            <img v-else :src="addForm.pageCover" width="360px" height="180px" alt="">
          </el-upload>
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
// import * as imageConversion from 'image-conversion'
import { getToken } from '@/utils/token'
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
      submitLoading: false,
      headers: {
        Authorization: ''
      },
      addForm: {
        id: '',
        pageName: '',
        pageLabel: '',
        pageCover: '',
        pageSort: 99999
      },
      addRules: {
        pageName: [
          { required: true, message: '请填写页面名称', trigger: 'blur' }
        ],
        pageLabel: [
          { required: true, message: '请填写页面标签', trigger: 'blur' }
        ]
      }
    }
  },
  methods: {
    initData(dataId) {
      this.$nextTick(() => {
        this.getPageDetail(dataId)
      })
    },
    getPageDetail(dataId) {
      if (dataId != null && dataId !== '') {
        this.$mapi.page.queryPageDetail({ pageId: dataId }).then(res => {
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
        pageName: '',
        pageLabel: '',
        pageCover: '',
        pageSort: 99999
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
          this.$mapi.page.addOrEditPage(this.addForm).then(res => {
            this.$message.success(res.message)
            this.doClose(true)
          }).finally(_ => {
            this.submitLoading = false
          })
        } else {
          return false
        }
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

      this.headers.Authorization = getToken()
      return file
      // 压缩图片
      // return new Promise(resolve => {
      //   if (file.size / 1024 < this.$config.UPLOAD_SIZE) {
      //     resolve(file)
      //   }
      //
      //   imageConversion.compressAccurately(file, this.$config.UPLOAD_SIZE).then(res => {
      //     resolve(res)
      //   })
      // })
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
          this.addForm.pageCover = response.data[0]['downloadUrl']
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

<style rel="stylesheet/scss" lang="scss" scoped>
::v-deep .el-input-number .el-input__inner {
  text-align: left;
}
</style>
