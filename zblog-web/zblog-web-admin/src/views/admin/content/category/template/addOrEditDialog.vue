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
      width="600px"
    >
      <el-form ref="addOrEditForm" :inline="!isMobile" :model="formData" :rules="formRules" label-width="100px">
        <el-form-item label="分类名称" prop="name">
          <el-input v-model="formData.name" :style="isMobile ? '' : 'width: 168px;'" maxlength="50" show-word-limit />
        </el-form-item>
        <el-form-item label="SLUG" prop="slug">
          <el-input v-model="formData.slug" :style="isMobile ? '' : 'width: 168px;'" maxlength="100" show-word-limit />
        </el-form-item>
        <el-form-item label="上级分类" prop="pid">
          <el-select
            v-model="formData.pid"
            :style="isMobile ? '' : 'width: 450px;'"
            placeholder="上级分类"
            clearable
          >
            <el-option
              v-for="item in firstCategoryList"
              :key="item.id"
              :label="item.name"
              :value="item.id"
              :disabled="formData.id === item.id"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="SEO标题" prop="seoTitle">
          <el-input v-model="formData.seoTitle" :style="isMobile ? '' : 'width: 450px;'" maxlength="255" show-word-limit />
        </el-form-item>
        <el-form-item label="SEO关键词" prop="seoKeywords">
          <el-input v-model="formData.seoKeywords" :style="isMobile ? '' : 'width: 450px;'" maxlength="255" show-word-limit />
        </el-form-item>
        <el-form-item label="SEO描述" prop="seoDescription">
          <el-input v-model="formData.seoDescription" :style="isMobile ? '' : 'width: 450px;'" maxlength="500" show-word-limit />
        </el-form-item>
        <el-form-item label="前台展示" prop="visible">
          <el-select v-model="formData.visible" placeholder="前台展示" :style="isMobile ? '' : 'width: 168px;'">
            <el-option v-for="item in visibleEnums" :key="item.value" :label="item.name" :value="item.value" />
          </el-select>
        </el-form-item>
        <el-form-item label="收录状态" prop="allowIndex">
          <el-select v-model="formData.allowIndex" placeholder="收录状态" :style="isMobile ? '' : 'width: 168px;'">
            <el-option v-for="item in allowIndexEnums" :key="item.value" :label="item.name" :value="item.value" />
          </el-select>
        </el-form-item>
        <el-form-item label="分类状态" prop="enabled">
          <el-select v-model="formData.enabled" placeholder="分类状态" :style="isMobile ? '' : 'width: 168px;'">
            <el-option v-for="item in enabledEnums" :key="item.value" :label="item.name" :value="item.value" />
          </el-select>
        </el-form-item>
        <el-form-item label="分类排序" prop="sort">
          <el-input-number
            v-model.number="formData.sort"
            :min="0"
            :max="99999"
            step-strictly
            controls-position="right"
            :style="isMobile ? '' : 'width: 168px;'"
          />
        </el-form-item>
        <el-form-item label="分类描述" prop="description">
          <el-input
            v-model="formData.description"
            type="textarea"
            rows="4"
            maxlength="1000"
            show-word-limit
            :style="isMobile ? '' : 'width: 450px;'"
          />
        </el-form-item>
        <el-form-item label="分类略缩图" prop="imageUrl">
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
            <i v-if="formData.imageUrl === null" class="el-icon-upload" />
            <div v-if="formData.imageUrl === null" class="el-upload__text">
              将文件拖到此处，或<em>点击上传</em>
            </div>
            <img v-else :src="formData.imageUrl" width="360px" height="180px" alt="">
          </el-upload>
          <el-button v-if="formData.imageUrl !== null" type="danger" @click="removeImg">清除图片</el-button>
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
      visibleEnums: [
        { name: '展示', value: true },
        { name: '隐藏', value: false }
      ],
      allowIndexEnums: [
        { name: '收录', value: true },
        { name: '禁止', value: false }
      ],
      enabledEnums: [
        { name: '启用', value: true },
        { name: '禁用', value: false }
      ],
      showImgUpload: false,
      langExt: {
        success: '上传成功!',
        fail: '上传失败!'
      },
      headers: {
        Authorization: ''
      },
      formData: {
        id: null,
        pid: 0,
        name: null,
        slug: null,
        imageId: null,
        imageUrl: null,
        description: null,
        seoTitle: null,
        seoKeywords: null,
        seoDescription: null,
        visible: false,
        allowIndex: false,
        enabled: true,
        sort: 99999
      },
      formRules: {
        name: [
          { required: true, message: '请输入分类名称', trigger: 'blur' }
        ],
        slug: [
          { required: true, message: '请输入分类唯一标识', trigger: 'blur' }
        ],
        pid: [
          { required: true, message: '请选择上级分类', trigger: 'change' }
        ],
        allowIndex: [
          { required: true, message: '请选择是否允许搜索引擎收录', trigger: 'change' }
        ],
        visible: [
          { required: true, message: '请选择是否前台展示', trigger: 'change' }
        ],
        enabled: [
          { required: true, message: '请选择分类状态', trigger: 'change' }
        ],
        sort: [
          { required: true, message: '请输入分类排序', trigger: 'blur' }
        ]
      }
    }
  },
  computed: {
    isMobile() {
      return this.$store.state.app.device === 'mobile'
    }
  },
  methods: {
    initData(dataId) {
      this.getFirstCategoryList()
      this.$nextTick(() => {
        if (dataId != null && dataId !== '') {
          this.queryDetail(dataId)
        }
      })
    },
    getFirstCategoryList() {
      this.firstCategoryList = []
      this.$mapi.category.queryFirstCategoryList().then(res => {
        this.firstCategoryList = res.data ? res.data : []
        this.firstCategoryList.push({ id: 0, name: '无' })
      }).catch(_ => {
        this.formData.pid = null
        this.firstCategoryList = []
      })
    },
    queryDetail(dataId) {
      this.$mapi.category.queryCategoryDetail({ categoryId: dataId }).then(res => {
        const { data } = res
        Object.keys(this.formData).forEach(key => {
          this.formData[key] = data[key]
        })
      }).catch(_ => {
        setTimeout(() => { this.doClose() }, 500)
      })
    },
    beforeUpload(file) {
      // 支持类型：.jpg,.jpeg,.png
      let imgType = ''
      if (file.name.indexOf('.') !== -1) {
        imgType = file.name.substring(file.name.lastIndexOf('.') + 1).toLowerCase()
      }
      if (imgType === '' || (imgType !== 'jpg' && imgType !== 'jpeg' && imgType !== 'png')) {
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
        console.log('err', err)
        this.$message.error(err.message)
      } catch {
        this.$message.error('系统发生未知错误')
      }
    },
    handleSuccess(response, file) {
      if (response.code === 200) {
        // success
        if (response.data && Array.isArray(response.data) && response.data.length > 0) {
          this.formData.imageId = response.data[0]['fileId']
          this.formData.imageUrl = response.data[0]['fileAccessUrL']
        }

        this.$message.success('上传成功')
      } else {
        // error
        this.$refs.upload.handleError(response, file)
      }
    },
    removeImg() {
      this.formData.imageId = null
      this.formData.imageUrl = null
    },
    doClose(result = false) {
      this.formData = {
        id: null,
        pid: 0,
        name: null,
        slug: null,
        imageId: null,
        imageUrl: null,
        description: null,
        seoTitle: null,
        seoKeywords: null,
        seoDescription: null,
        allowIndex: false,
        visible: false,
        enabled: true,
        sort: 99999
      }
      this.$refs.addOrEditForm.resetFields()
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
      this.$refs.addOrEditForm.validate((valid) => {
        if (valid) {
          this.submitLoading = true
          if (this.formData.id == null || this.formData.id === '') {
            // add
            this.$mapi.category.addCategory(this.formData).then(res => {
              this.$message.success(res.message)
              this.doClose(true)
            }).finally(_ => {
              this.submitLoading = false
            })
          } else {
            // edit
            this.$mapi.category.editCategory(this.formData).then(res => {
              this.$message.success(res.message)
              this.doClose(true)
            }).finally(_ => {
              this.submitLoading = false
            })
          }
        }
      })
    }
  }
}
</script>

<style rel="stylesheet/scss" lang="scss" scoped>
::v-deep .el-input-number .el-input__inner {
  text-align: left;
}
</style>
