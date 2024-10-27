<template>
  <div>
    <el-dialog append-to-body :close-on-click-modal="false" :before-close="handleClose" :visible.sync="dialogVisible" :title="dialogTitle" width="640px">
      <el-form ref="addForm" :inline="true" :model="addForm" :rules="addRules" size="small" label-width="100px">
        <el-form-item label="分类名称" prop="name">
          <el-input v-model="addForm.name" style="width: 178px" maxlength="50" show-word-limit />
        </el-form-item>
        <el-form-item label="上级分类" prop="pid">
          <el-select v-model="addForm.pid" placeholder="上级分类" clearable style="width: 178px">
            <el-option v-for="item in firstCategoryList" :key="item.id" :label="item.name" :value="item.id" />
          </el-select>
        </el-form-item>
        <el-form-item label="分类状态" prop="enabled">
          <el-select v-model="addForm.enabled" placeholder="分类状态" style="width: 178px">
            <el-option v-for="item in enabledEnums" :key="item.value" :label="item.name" :value="item.value" />
          </el-select>
        </el-form-item>
        <el-form-item label="分类排序" prop="sort">
          <el-input-number v-model.number="addForm.sort" :min="0" :max="99999" step-strictly controls-position="right" style="width: 178px" />
        </el-form-item>
        <el-form-item label="分类描述">
          <el-input v-model="addForm.desc" type="textarea" rows="4" maxlength="1000" show-word-limit style="width: 470px" />
        </el-form-item>
        <el-form-item label="分类略缩图">
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
            <i v-if="addForm.imageUrl === ''" class="el-icon-upload" />
            <div v-if="addForm.imageUrl === ''" class="el-upload__text">
              将文件拖到此处，或<em>点击上传</em>
            </div>
            <img v-else :src="addForm.imageUrl" width="360px" height="180px" alt="">
          </el-upload>
          <el-button v-if="addForm.imageUrl !== ''" type="danger" @click="removeImg">清除图片</el-button>
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
      firstCategoryList: [],
      submitLoading: false,
      enabledEnums: [
        { name: '启用', value: true },
        { name: '禁用', value: false }
      ],
      addForm: {
        id: '',
        pid: '',
        name: '',
        imageUrl: '',
        desc: '',
        sort: 99999,
        enabled: true
      },
      addRules: {
        name: [
          { required: true, message: '请输入分类名称', trigger: 'blur' }
        ],
        sort: [
          { required: true, message: '请输入分类排序', trigger: 'blur' }
        ],
        enabled: [
          { required: true, message: '请选择分类状态', trigger: 'blur' }
        ]
      },
      showImgUpload: false,
      langExt: {
        success: '上传成功!',
        fail: '上传失败!'
      },
      headers: {
        Authorization: ''
      }
    }
  },
  methods: {
    initData(dataId) {
      this.addForm.id = dataId
      this.getFirstCategoryList()
      this.$nextTick(() => {
        this.getCategoryDetail()
      })
    },
    getFirstCategoryList() {
      this.firstCategoryList = []
      this.$mapi.category.queryFirstCategoryList().then(res => {
        this.firstCategoryList = res.data ? res.data : []
      })
    },
    getCategoryDetail() {
      if (this.addForm.id != null && this.addForm.id !== '') {
        this.$mapi.category.queryCategoryDetail({ categoryId: this.addForm.id }).then(res => {
          const { data } = res
          Object.keys(this.addForm).forEach(key => {
            this.addForm[key] = data[key]
          })
        }).catch(_ => {
          this.doClose()
        })
      }
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
          this.addForm.imageUrl = response.data[0]['downloadUrl']
        }

        this.$message.success(response.message || '上传成功')
      } else {
        // error
        this.$refs.upload.handleError(response, file)
      }
    },
    removeImg() {
      this.addForm.imageUrl = ''
    },
    doClose(result = false) {
      this.addForm = {
        id: '',
        pid: '',
        name: '',
        imageUrl: '',
        desc: '',
        sort: 99999,
        enabled: true
      }
      this.$refs['addForm'].resetFields()
      this.showImgUpload = false
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
            this.$mapi.category.addCategory(this.addForm).then(res => {
              this.$message.success(res.message)
              this.doClose(true)
            }).finally(_ => {
              this.submitLoading = false
            })
          } else {
            // edit
            this.$mapi.category.editCategory(this.addForm).then(res => {
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
.avatar {
  width: 120px;
  height: 120px;
  // border-radius: 50%;
}
::v-deep .el-input-number .el-input__inner {
  text-align: left;
}
</style>
