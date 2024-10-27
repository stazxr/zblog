<template>
  <el-dialog append-to-body :close-on-click-modal="false" :before-close="handleClose" :visible.sync="dialogVisible" :title="dialogTitle" width="540px">
    <el-form ref="addForm" :inline="true" :model="addForm" :rules="addRules" size="small" label-width="80px">
      <el-form-item label="相册名称" prop="albumName">
        <el-input v-model="addForm.albumName" style="width:360px" maxlength="15" show-word-limit />
      </el-form-item>
      <el-form-item label="相册描述">
        <el-input v-model="addForm.albumDesc" style="width:360px" maxlength="50" show-word-limit />
      </el-form-item>
      <el-form-item label="相册封面">
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
          <i v-if="addForm.albumCover === ''" class="el-icon-upload" />
          <div v-if="addForm.albumCover === ''" class="el-upload__text">
            将文件拖到此处，或<em>点击上传</em>
          </div>
          <img v-else :src="addForm.albumCover" width="360px" height="180px" alt="">
        </el-upload>
      </el-form-item>
      <el-form-item label="发布形式" prop="status">
        <el-radio-group v-model="addForm.status">
          <el-radio :label="1">公开</el-radio>
          <el-radio :label="2">私密</el-radio>
        </el-radio-group>
      </el-form-item>
    </el-form>
    <div slot="footer" class="dialog-footer">
      <el-button type="text" @click="cancel">取 消</el-button>
      <el-button :loading="submitLoading" type="primary" @click="submit">确 认</el-button>
    </div>
  </el-dialog>
</template>

<script>
import * as imageConversion from 'image-conversion'
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
        albumName: '',
        albumDesc: '',
        albumCover: '',
        status: 1
      },
      addRules: {
        albumName: [
          { required: true, message: '请填写相册名称', trigger: 'blur' }
        ],
        status: [
          { required: true, message: '请选择发布形式', trigger: 'blur' }
        ]
      }
    }
  },
  methods: {
    initData(dataId) {
      this.$nextTick(() => {
        this.getAlbumDetail(dataId)
      })
    },
    getAlbumDetail(dataId) {
      if (dataId != null && dataId !== '') {
        this.$mapi.album.queryAlbumDetail({ albumId: dataId }).then(res => {
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
        albumName: '',
        albumDesc: '',
        albumCover: '',
        status: 1
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
          this.$mapi.album.addOrEditAlbum(this.addForm).then(res => {
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

      // 压缩图片
      this.headers.Authorization = getToken()
      return new Promise(resolve => {
        if (file.size / 1024 < this.$config.UPLOAD_SIZE) {
          resolve(file)
        }

        imageConversion.compressAccurately(file, this.$config.UPLOAD_SIZE).then(res => {
          resolve(res)
        })
      })
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
          this.addForm.albumCover = response.data[0]['downloadUrl']
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
