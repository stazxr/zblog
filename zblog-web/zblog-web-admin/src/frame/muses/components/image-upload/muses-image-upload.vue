<template>
  <div class="muses-image-upload">
    <el-upload
      ref="musesImageUpload"
      name="file"
      drag
      :action="uploadUrl"
      :multiple="false"
      :show-file-list="false"
      :with-credentials="withCredentials"
      :before-upload="beforeUpload"
      :on-progress="handleProgress"
      :on-error="handleError"
      :on-success="handleSuccess"
    >
      <template v-if="!value">
        <i class="el-icon-upload" />
        <div class="el-upload__text">
          将文件拖到此处，或<em>点击上传</em>
        </div>
      </template>
      <template v-else>
        <img class="preview" :src="value" :width="previewImgWidth" :height="previewImgHeight" alt="">
      </template>
    </el-upload>
    <el-button v-if="value" type="danger" class="remove-btn" @click="remove">清除图片</el-button>
  </div>
</template>

<script>
import * as imageConversion from 'image-conversion'
export default {
  name: 'MusesImageUpload',
  // 是否继承父组件传递的class/style等属性
  inheritAttrs: false,
  props: {
    // 图片地址
    value: {
      type: String,
      default: null
    },
    // 预览图宽度
    previewImgWidth: {
      type: Number,
      default: 360
    },
    // 预览图高度
    previewImgHeight: {
      type: Number,
      default: 180
    },
    // 文件上传地址
    uploadUrl: {
      type: String,
      default() {
        return this.$store.state.api.fileUploadApi
      }
    },
    // 是否携带 Cookie
    withCredentials: {
      type: Boolean,
      default: true
    },
    // 是否启用图片压缩
    compress: {
      type: Boolean,
      default: true
    },
    // 图片压缩目标大小，单位：KB
    limitSize: {
      type: Number,
      default: 200
    },
    // 原始图片最大大小，单位：MB
    maxFileSize: {
      type: Number,
      default: 20
    },
    // 允许上传的图片MIME类型
    accept: {
      type: Array,
      default() {
        return [
          'image/jpeg',
          'image/png',
          'image/webp'
        ]
      }
    },
    // 是否显示图片上传成功提示
    showSuccessMessage: {
      type: Boolean,
      default: true
    }
  },
  data() {
    return {
      // 上传状态
      uploadLoading: false
    }
  },
  methods: {
    // 上传前处理
    beforeUpload(file) {
      if (!this.accept.includes(file.type)) {
        this.$message.warning('图片格式不支持')
        return false
      }

      if (file.size > this.maxFileSize * 1024 * 1024) {
        this.$message.warning(`图片不能超过${this.maxFileSize}MB`)
        return false
      }

      if (!this.compress || file.size / 1024 <= this.limitSize) {
        return file
      }

      return imageConversion.compressAccurately(file, this.limitSize).then(blob => {
        return new File([blob], file.name, { type: file.type })
      })
    },
    // 上传进度
    handleProgress() {
      this.uploadLoading = true
      this.$emit('on-start')
    },
    // 上传成功回调
    handleSuccess(response) {
      this.uploadLoading = false
      if (response.code === '000000000') {
        const data = response.data && Array.isArray(response.data) && response.data.length > 0 ? response.data[0] : null
        if (!data) {
          this.$message.error('上传返回数据异常')
          return
        }

        // 返回文件信息
        this.$emit('on-success', data)
        if (this.showSuccessMessage) {
          this.$message.success('上传成功')
        }
      } else {
        this.$message.error(response.message || '上传失败')
      }
    },
    handleError(err) {
      this.uploadLoading = false
      this.$emit('on-error', err)
      this.$message.error('上传失败')
    },
    remove() {
      this.$emit('input', null)
      this.$emit('on-remove')
    }
  }
}
</script>

<style scoped lang="scss">
.preview {
  object-fit: cover;
}
.remove-btn {
  margin-top: 10px;
}
</style>
