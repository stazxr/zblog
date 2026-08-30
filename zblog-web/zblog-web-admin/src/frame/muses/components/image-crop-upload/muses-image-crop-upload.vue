<template>
  <div class="muses-image-crop-upload">
    <!-- 图片预览 -->
    <div class="image-preview" :class="{ 'is-empty': !value, 'is-fixed': fixedPreview, 'is-auto': !fixedPreview }" :style="previewStyle" @click="chooseImage">
      <!-- 已上传图片 -->
      <img v-if="value" :src="value" alt="">

      <!-- 空图片 -->
      <div v-else class="image-placeholder" :class="{ 'is-compact': previewHeight < 80 }">
        <i class="el-icon-plus" />
        <span>上传图片</span>
      </div>

      <!-- 更换图片蒙层 -->
      <div v-if="value" class="image-mask" :class="{ 'is-compact': previewHeight < 80 }">
        <i class="el-icon-upload2" />
        <span>更换图片</span>
      </div>

      <!-- 删除按钮 -->
      <div v-if="value && showRemove" class="remove-button" title="删除图片" @click.stop="handleRemove">
        <i class="el-icon-close" />
      </div>
    </div>

    <!-- 隐藏文件选择器 -->
    <input
      ref="fileInput"
      type="file"
      accept="image/*"
      class="file-input"
      @change="handleFileChange"
    >

    <!-- 裁剪弹窗 -->
    <el-dialog
      title="图片裁剪"
      :visible.sync="dialogVisible"
      width="900px"
      append-to-body
      :close-on-click-modal="false"
      @closed="handleDialogClosed"
    >
      <div class="crop-container">
        <!-- 裁剪区域 -->
        <div class="crop-main">
          <vue-cropper
            v-if="cropImage"
            ref="cropperRef"
            :img="cropImage"
            :auto-crop="true"
            :auto-crop-width="autoCropWidth"
            :auto-crop-height="autoCropHeight"
            :fixed="fixed"
            :fixed-number="fixedNumber"
            :fixed-box="fixedBox"
            :can-move="true"
            :can-move-box="true"
            :center-box="true"
            :original="false"
            :info="true"
            :full="true"
            :output-quality="outputQuality"
            :output-type="cropperOutputType"
          />
        </div>

        <!-- 操作区域 -->
        <div class="crop-actions">
          <el-button icon="el-icon-refresh-left" @click="rotateLeft">左旋转</el-button>
          <el-button icon="el-icon-refresh-right" @click="rotateRight">右旋转</el-button>
          <el-button icon="el-icon-zoom-in" @click="changeScale(1)">放大</el-button>
          <el-button icon="el-icon-zoom-out" @click="changeScale(-1)">缩小</el-button>
        </div>
      </div>

      <span slot="footer">
        <el-button :disabled="uploadLoading" @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" :loading="uploadLoading" @click="handleUpload">确认上传</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
import { VueCropper } from 'vue-cropper'
export default {
  name: 'MusesImageCropUpload',
  components: {
    VueCropper
  },
  props: {
    /**
     * v-model
     */
    value: {
      type: String,
      default: ''
    },
    /**
     * 是否显示删除按钮
     */
    showRemove: {
      type: Boolean,
      default: true
    },
    /**
     * 裁剪比例
     *
     * 例如：
     * 1     -> 1:1
     * 16/5  -> 16:5
     */
    aspectRatio: {
      type: Number,
      default: 1
    },
    /**
     * 输出图片宽度
     *
     * 不配置则不固定输出宽度
     */
    outputWidth: {
      type: Number,
      default: null
    },
    /**
     * 输出图片高度
     *
     * 不配置则不固定输出高度
     */
    outputHeight: {
      type: Number,
      default: null
    },
    /**
     * 是否固定预览尺寸
     *
     * true：固定宽高
     * false：宽度固定，高度根据图片比例自适应
     */
    fixedPreview: {
      type: Boolean,
      default: true
    },
    /**
     * 图片预览宽度
     */
    previewWidth: {
      type: Number,
      default: 160
    },
    /**
     * 图片预览高度
     */
    previewHeight: {
      type: Number,
      default: 160
    },
    /**
     * 最大文件大小，单位 MB
     *
     * 0 表示不限制
     */
    maxSize: {
      type: Number,
      default: 10
    },
    /**
     * 最小图片宽度
     *
     * 0 表示不限制
     */
    minWidth: {
      type: Number,
      default: 0
    },
    /**
     * 最小图片高度
     *
     * 0 表示不限制
     */
    minHeight: {
      type: Number,
      default: 0
    },
    /**
     * 是否圆形预览
     */
    circle: {
      type: Boolean,
      default: false
    },
    /**
     * 是否固定裁剪比例
     */
    fixed: {
      type: Boolean,
      default: true
    },
    /**
     * 是否固定裁剪框
     */
    fixedBox: {
      type: Boolean,
      default: false
    },
    /**
     * 输出图片质量
     */
    outputQuality: {
      type: Number,
      default: 1
    },
    /**
     * 限制允许上传的图片类型
     */
    acceptImageTypes: {
      type: Array,
      default: () => ['image/jpeg', 'image/png']
    },
    /**
     * 输出图片类型: jpeg / png
     * null 表示保持原图片类型
     */
    outputType: {
      type: String,
      default: null
    }
  },
  data() {
    return {
      dialogVisible: false,
      cropImage: '',
      uploadLoading: false,
      // 原始图片 MIME 类型
      sourceImageType: 'image/jpeg',
      // 原始图片比例
      imageRatio: 1
    }
  },
  computed: {
    /**
     * 是否固定输出尺寸
     */
    fixedOutputSize() {
      return this.outputWidth > 0 && this.outputHeight > 0
    },
    /**
     * 裁剪比例
     */
    fixedNumber() {
      return [this.aspectRatio, 1]
    },
    /**
     * 默认裁剪框宽度
     */
    autoCropWidth() {
      return 300
    },
    /**
     * 默认裁剪框高度
     */
    autoCropHeight() {
      if (!this.fixed) {
        return 300
      }

      return 300 / this.aspectRatio
    },
    /**
     * 预览样式
     */
    previewStyle() {
      const style = {
        width: this.previewWidth + 'px',
        borderRadius: this.circle ? '50%' : '8px'
      }

      if (this.fixedPreview) {
        style.height = this.previewHeight + 'px'
      } else {
        style.height = this.previewWidth / this.imageRatio + 'px'
      }

      return style
    },
    /**
     * 最终输出 MIME 类型
     */
    outputMimeType() {
      if (this.outputType === 'png') {
        return 'image/png'
      }

      if (this.outputType === 'jpeg') {
        return 'image/jpeg'
      }

      // outputType 为空时，保持原图类型
      return this.sourceImageType
    },
    /**
     * vue-cropper 输出类型
     */
    cropperOutputType() {
      return this.outputMimeType === 'image/png' ? 'png' : 'jpeg'
    },
    /**
     * 输出文件扩展名
     */
    outputFileExtension() {
      return this.outputMimeType === 'image/png' ? 'png' : 'jpg'
    }
  },
  watch: {
    value: {
      immediate: true,
      handler(value) {
        if (!value) {
          this.imageRatio = 1
          return
        }

        const image = new Image()

        image.onload = () => {
          this.imageRatio = image.width / image.height
        }

        image.onerror = () => {
          this.imageRatio = 1
        }

        image.src = value
      }
    }
  },
  methods: {
    /**
     * 选择图片
     */
    chooseImage() {
      if (this.uploadLoading) {
        return
      }

      this.$refs.fileInput.click()
    },
    /**
     * 文件变化
     */
    handleFileChange(event) {
      const file = event.target.files[0]
      if (!file) {
        return
      }

      // 文件类型检查
      if (!file.type.startsWith('image/')) {
        this.$message.warning('请选择图片文件')
        this.resetFileInput()
        return
      }

      // 是否允许上传该图片类型
      if (this.acceptImageTypes && this.acceptImageTypes.length > 0 && !this.acceptImageTypes.includes(file.type)) {
        this.$message.warning('当前图片格式不支持')
        this.resetFileInput()
        return
      }

      // 保存原始图片类型
      this.sourceImageType = file.type

      // 文件大小检查
      if (this.maxSize > 0) {
        const maxSize = this.maxSize * 1024 * 1024
        if (file.size > maxSize) {
          this.$message.warning('图片大小不能超过 ' + this.maxSize + 'MB')
          this.resetFileInput()
          return
        }
      }

      const reader = new FileReader()
      reader.onload = e => {
        const image = new Image()
        image.onload = () => {
          // 最小宽度检查
          if (this.minWidth > 0 && image.width < this.minWidth) {
            this.$message.warning('图片宽度不能小于 ' + this.minWidth + 'px')
            return
          }
          // 最小高度检查
          if (this.minHeight > 0 && image.height < this.minHeight) {
            this.$message.warning('图片高度不能小于 ' + this.minHeight + 'px')
            return
          }

          this.cropImage = e.target.result
          this.dialogVisible = true
        }
        image.onerror = () => {
          this.$message.error('图片读取失败')
        }
        image.src = e.target.result
      }
      reader.onerror = () => {
        this.$message.error('文件读取失败')
      }

      reader.readAsDataURL(file)
      this.resetFileInput()
    },
    /**
     * 左旋转
     */
    rotateLeft() {
      this.$refs.cropperRef.rotateLeft()
    },
    /**
     * 右旋转
     */
    rotateRight() {
      this.$refs.cropperRef.rotateRight()
    },
    /**
     * 缩放
     */
    changeScale(num) {
      this.$refs.cropperRef.changeScale(num)
    },
    /**
     * 上传图片
     */
    handleUpload() {
      const cropper = this.$refs.cropperRef
      if (!cropper) {
        return
      }

      this.uploadLoading = true
      if (this.fixedOutputSize) {
        cropper.getCropData(data => {
          this.createFixedSizeBlob(data).then(blob => {
            this.uploadBlob(blob)
          }).catch(() => {
            this.uploadLoading = false
            this.$message.error('图片裁剪失败')
          })
        })
        return
      }

      cropper.getCropBlob(blob => {
        if (!blob) {
          this.uploadLoading = false
          this.$message.error('图片裁剪失败')
          return
        }

        this.uploadBlob(blob)
      })
    },
    /**
     * 创建固定尺寸图片
     *
     * @param {String} imageData base64 图片
     * @returns {Promise<Blob>}
     */
    createFixedSizeBlob(imageData) {
      return new Promise((resolve, reject) => {
        const image = new Image()
        image.onload = () => {
          const canvas = document.createElement('canvas')
          canvas.width = this.outputWidth
          canvas.height = this.outputHeight
          const context = canvas.getContext('2d')
          context.drawImage(image, 0, 0, this.outputWidth, this.outputHeight)
          canvas.toBlob(blob => {
            if (blob) {
              resolve(blob)
            } else {
              reject(new Error('图片生成失败'))
            }
          }, this.outputMimeType, this.outputQuality)
        }

        image.onerror = () => {
          reject(new Error('图片加载失败'))
        }
        image.src = imageData
      })
    },
    /**
     * 上传 Blob
     */
    uploadBlob(blob) {
      if (!blob) {
        this.uploadLoading = false
        this.$message.error('图片生成失败')
        return
      }

      const fileName = 'image-' + Date.now() + '.' + this.outputFileExtension
      const file = new File(
        [blob],
        fileName,
        {
          type: this.outputMimeType
        }
      )

      const formData = new FormData()
      formData.append('file', file)

      this.$mapi.file.uploadFile(formData).then(res => {
        if (res.code === '000000000') {
          const data = res.data && Array.isArray(res.data) && res.data.length > 0 ? res.data[0] : null
          if (!data) {
            this.$message.error('上传返回数据异常')
            return
          }

          const url = data.fileAccessUrL
          if (!url) {
            this.$message.error('上传文件地址为空')
            return
          }

          this.$emit('input', url)
          this.$emit('success', url)
          this.$message.success('图片上传成功')
          this.dialogVisible = false
        } else {
          this.$message.error(res.message || '图片上传失败')
        }
      }).finally(() => {
        this.uploadLoading = false
      })
    },
    /**
     * 删除图片
     */
    handleRemove() {
      this.$confirm('确定删除当前图片吗？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.$emit('input', '')
        this.$emit('remove')
        this.$message.success('图片已删除')
      }).catch(() => {
        // 取消删除
      })
    },
    /**
     * 清理弹窗
     */
    handleDialogClosed() {
      this.cropImage = ''
    },
    /**
     * 重置文件选择
     */
    resetFileInput() {
      this.$refs.fileInput.value = ''
    }
  }
}
</script>

<style scoped>
.muses-image-crop-upload {
  display: inline-block;
}

/* =========================
   图片预览
   ========================= */

.image-preview {
  position: relative;
  overflow: hidden;
  cursor: pointer;
  background: #f5f7fa;
  border: 1px dashed #dcdfe6;
}
.image-preview:hover {
  border-color: #409eff;
}
.image-preview img {
  display: block;
  width: 100%;
  height: 100%;
  /*object-fit: contain;*/
}
/* 固定尺寸预览 */
.image-preview.is-fixed img {
  object-fit: cover;
}
/* 自适应比例预览 */
.image-preview.is-auto img {
  object-fit: contain;
}

/* =========================
   空图片
   ========================= */

.image-placeholder {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  gap: 6px;
  width: 100%;
  height: 100%;
  padding: 4px;
  color: #909399;
  overflow: hidden;
  box-sizing: border-box;
}

.image-placeholder i {
  flex-shrink: 0;
  font-size: 28px;
}

.image-placeholder span {
  max-width: 100%;
  overflow: hidden;
  font-size: 13px;
  line-height: 1.2;
  white-space: nowrap;
  text-overflow: ellipsis;
}

/* 高度较小时改为横向布局 */
.image-placeholder.is-compact {
  flex-direction: row;
  gap: 5px;
  padding: 4px 8px;
}

.image-placeholder.is-compact i {
  font-size: 20px;
}

.image-placeholder.is-compact span {
  font-size: 12px;
}

/* =========================
   Hover 蒙层
   ========================= */

.image-mask {
  position: absolute;
  top: 0;
  right: 0;
  bottom: 0;
  left: 0;

  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  gap: 6px;

  padding: 4px;
  color: #ffffff;
  background: rgba(0, 0, 0, 0.5);
  overflow: hidden;
  box-sizing: border-box;

  opacity: 0;
  transition: opacity 0.2s;
}

.image-preview:hover .image-mask {
  opacity: 1;
}

.image-mask i {
  flex-shrink: 0;
  font-size: 24px;
}

.image-mask span {
  max-width: 100%;
  overflow: hidden;
  font-size: 13px;
  line-height: 1.2;
  white-space: nowrap;
  text-overflow: ellipsis;
}

/* 高度较小时改为横向布局 */
.image-mask.is-compact {
  flex-direction: row;
  gap: 5px;
  padding: 4px 8px;
}

.image-mask.is-compact i {
  font-size: 18px;
}

.image-mask.is-compact span {
  font-size: 12px;
}

/* =========================
   隐藏文件选择器
   ========================= */

.file-input {
  display: none;
}

/* =========================
   裁剪弹窗
   ========================= */

.crop-container {
  min-height: 500px;
}

.crop-main {
  width: 100%;
  height: 500px;
}

/* =========================
   操作按钮
   ========================= */

.crop-actions {
  display: flex;
  flex-wrap: wrap;
  justify-content: center;
  gap: 10px;
  margin-top: 16px;
}

/* =========================
   删除按钮
   ========================= */

.remove-button {
  position: absolute;
  top: 6px;
  right: 6px;

  display: flex;
  align-items: center;
  justify-content: center;

  width: 24px;
  height: 24px;

  color: #ffffff;
  background: rgba(0, 0, 0, 0.6);
  border-radius: 50%;
  cursor: pointer;

  opacity: 0;
  transition: all 0.2s;
}

.image-preview:hover .remove-button {
  opacity: 1;
}

.remove-button:hover {
  background: #f56c6c;
}

.remove-button i {
  margin: 0;
  font-size: 14px;
}

/* =========================
   手机端
   ========================= */

@media screen and (max-width: 768px) {
  .crop-container {
    min-height: 400px;
  }

  .crop-main {
    height: 400px;
  }

  .crop-actions {
    justify-content: flex-start;
  }

  .crop-actions .el-button {
    margin-left: 0 !important;
  }

  .remove-button {
    opacity: 1;
  }
}

/* =========================
   小屏手机
   ========================= */

@media screen and (max-width: 480px) {
  .crop-main {
    height: 350px;
  }

  .crop-actions {
    display: grid;
    grid-template-columns: 1fr 1fr;
  }

  .crop-actions .el-button {
    width: 100%;
  }
}
</style>
