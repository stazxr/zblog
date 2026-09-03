<template>
  <v-card class="image-upload-panel" elevation="8">
    <!-- 标题 -->
    <div class="image-upload-header">
      <span>添加图片</span>
      <v-btn icon small @click="$emit('close')">
        <v-icon small>mdi-close</v-icon>
      </v-btn>
    </div>
    <!-- 未选择图片 -->
    <div v-if="!previewUrl" class="image-upload-select" @click="selectImage">
      <v-icon size="36">
        mdi-image-plus
      </v-icon>
      <div class="image-upload-title">
        选择图片
      </div>
      <div class="image-upload-tip">
        JPG / PNG / GIF / WEBP
      </div>
      <div class="image-upload-tip">
        最大 5MB
      </div>
    </div>
    <!-- 图片预览 -->
    <div v-else class="image-preview-wrapper">
      <img :src="previewUrl" class="image-preview" alt="图片预览">
      <!-- 删除 -->
      <v-btn class="image-preview-remove" icon small @click="removeImage">
        <v-icon small>mdi-close-circle</v-icon>
      </v-btn>
    </div>
    <!-- 底部操作 -->
    <div v-if="previewUrl" class="image-upload-footer">
      <v-btn text small style="margin-right: 5px" @click="selectImage">
        重新选择
      </v-btn>
      <v-btn color="primary" small :loading="uploading" :disabled="uploading" @click="upload">
        上传
      </v-btn>
    </div>
    <input
      ref="fileInput"
      type="file"
      accept="image/jpeg,image/png,image/gif,image/webp"
      style="display: none"
      @change="handleChange"
    >
  </v-card>
</template>
<script>
export default {
  name: 'ImageUploadPanel',
  data() {
    return {
      file: null,
      previewUrl: '',
      uploading: false,
      maxSize: 5 * 1024 * 1024
    }
  },
  beforeDestroy() {
    this.clearPreview()
  },
  methods: {
    selectImage() {
      this.$refs.fileInput.click()
    },
    handleChange(event) {
      const file = event.target.files &&
        event.target.files[0]

      // 清空，保证重复选择同一张图片也能触发 change
      event.target.value = ''

      if (!file) {
        return
      }

      if (!this.validate(file)) {
        return
      }

      this.clearPreview()

      this.file = file

      // 本地预览
      this.previewUrl = URL.createObjectURL(file)
    },
    validate(file) {
      const allowTypes = [
        'image/jpeg',
        'image/png',
        'image/gif',
        'image/webp'
      ]

      if (!allowTypes.includes(file.type)) {
        this.$toast({ type: 'warning', message: '只支持 JPG、PNG、GIF、WEBP 图片' })
        return false
      }

      if (file.size > this.maxSize) {
        this.$toast({ type: 'warning', message: '图片大小不能超过 5MB' })
        return false
      }

      return true
    },
    removeImage() {
      this.file = null
      this.clearPreview()
    },
    clearPreview() {
      if (this.previewUrl) {
        URL.revokeObjectURL(this.previewUrl)
        this.previewUrl = ''
      }
    },
    async upload() {
      if (!this.file || this.uploading) {
        return
      }

      try {
        this.uploading = true
        const url = await this.uploadImage(this.file)

        // 通知父组件
        this.$emit('success', url)

        // 清理
        this.file = null
        this.clearPreview()
      } catch (error) {
        this.$toast({ type: 'error', message: error || '图片上传失败，请稍后重试' })
      } finally {
        this.uploading = false
      }
    },
    async uploadImage(file) {
      const formData = new FormData()
      formData.append('file', file)

      const res = await this.$mapi.other.uploadFile(formData)
      if (res.code === '000000000') {
        const data = res.data && Array.isArray(res.data) && res.data.length > 0 ? res.data[0] : null
        if (!data) {
          this.$toast({ type: 'error', message: '上传返回数据异常' })
          return
        }

        const fileId = data.fileId
        if (!fileId) {
          this.$toast({ type: 'error', message: '上传文件地址为空' })
          return
        }

        this.$toast({ type: 'success', message: '图片上传成功' })
        return fileId
      } else {
        this.$toast({ type: 'success', message: res.message || '图片上传失败' })
      }
    }
  }
}
</script>
<style scoped>
.image-upload-panel {
  overflow: hidden;
  border-radius: 8px;
}

.image-upload-header {
  height: 44px;
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 0 8px 0 16px;
  border-bottom: 1px solid #eee;
  font-size: 14px;
  font-weight: 500;
}

.image-upload-select {
  height: 180px;
  margin: 16px;
  border: 1px dashed #dcdfe6;
  border-radius: 6px;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  cursor: pointer;
  transition: border-color 0.2s;
}

.image-upload-select:hover {
  border-color: #409eff;
}

.image-upload-title {
  margin-top: 10px;
  font-size: 14px;
}

.image-upload-tip {
  margin-top: 4px;
  color: #999;
  font-size: 12px;
}

.image-preview-wrapper {
  position: relative;
  margin: 16px;
  height: 180px;
  display: flex;
  align-items: center;
  justify-content: center;
  background: #f7f7f7;
  border-radius: 6px;
  overflow: hidden;
}

.image-preview {
  max-width: 100%;
  max-height: 100%;
  object-fit: contain;
}

.image-preview-remove {
  position: absolute !important;
  top: 4px;
  right: 4px;
}

.image-upload-footer {
  display: flex;
  align-items: center;
  justify-content: flex-end;
  padding: 8px 12px;
  border-top: 1px solid #eee;
}
</style>
