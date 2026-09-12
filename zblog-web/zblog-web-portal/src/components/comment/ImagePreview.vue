<template>
  <v-dialog
    v-model="visible"
    fullscreen
    hide-overlay
    content-class="image-preview-dialog"
    @keydown.esc="close"
  >
    <div class="image-preview" @click.self="close">
      <button type="button" class="image-preview-close" @click="close">
        <icon name="guanbi" color="#fff" size="20" />
      </button>

      <div class="image-preview-content">
        <img v-if="currentImage" :src="currentImage" class="image-preview-image" alt="" @click.stop>
      </div>

      <!-- 上一张 -->
      <button v-if="images.length > 1" type="button" class="image-preview-prev" @click.stop="prev">
        <icon name="xiangzuo1" color="#fff" size="28" />
      </button>

      <!-- 下一张 -->
      <button v-if="images.length > 1" type="button" class="image-preview-next" @click.stop="next">
        <icon name="xiangyou1" color="#fff" size="28" />
      </button>

      <!-- 图片数量 -->
      <div v-if="images.length > 1" class="image-preview-count">
        {{ currentIndex + 1 }} / {{ images.length }}
      </div>
    </div>
  </v-dialog>
</template>

<script>
export default {
  name: 'ImagePreview',
  props: {
    /**
     * 是否显示预览
     */
    value: {
      type: Boolean,
      default: false
    },
    /**
     * 图片列表
     */
    images: {
      type: Array,
      default: () => []
    },
    /**
     * 当前图片索引
     */
    index: {
      type: Number,
      default: 0
    }
  },
  data() {
    return {
      currentIndex: 0
    }
  },
  computed: {
    visible: {
      get() {
        return this.value
      },
      set(value) {
        this.$emit('input', value)
      }
    },
    currentImage() {
      return this.images[this.currentIndex] || ''
    }
  },
  watch: {
    value(value) {
      if (value) {
        this.setCurrentIndex()
      }
    },
    index() {
      if (this.value) {
        this.setCurrentIndex()
      }
    },
    images() {
      if (this.value) {
        this.setCurrentIndex()
      }
    }
  },
  methods: {
    setCurrentIndex() {
      if (!this.images.length) {
        this.currentIndex = 0
        return
      }

      this.currentIndex = Math.min(
        Math.max(this.index, 0),
        this.images.length - 1
      )
    },
    prev() {
      if (this.images.length <= 1) {
        return
      }

      this.currentIndex = (this.currentIndex - 1 + this.images.length) % this.images.length
    },
    next() {
      if (this.images.length <= 1) {
        return
      }

      this.currentIndex = (this.currentIndex + 1) % this.images.length
    },
    close() {
      this.visible = false
    }
  }
}
</script>
<style scoped>
.image-preview {
  position: relative;
  display: flex;
  align-items: center;
  justify-content: center;
  width: 100vw;
  height: 100vh;
  background: rgba(0, 0, 0, 0.9);
  outline: none;
}

.image-preview-content {
  display: flex;
  align-items: center;
  justify-content: center;
  width: 100%;
  height: 100%;
  padding: 60px 80px;
  box-sizing: border-box;
}

.image-preview-image {
  display: block;
  max-width: 100%;
  max-height: 100%;
  object-fit: contain;
  user-select: none;
}

.image-preview-close,
.image-preview-prev,
.image-preview-next {
  position: absolute;
  z-index: 10;
  display: flex;
  align-items: center;
  justify-content: center;
  width: 40px;
  height: 40px;
  padding: 0;
  border: 0;
  border-radius: 50%;
  outline: none;
  background: rgba(0, 0, 0, 0.4);
  cursor: pointer;
}

.image-preview-close {
  top: 20px;
  right: 20px;
}

.image-preview-prev {
  left: 20px;
  top: 50%;
  transform: translateY(-50%);
}

.image-preview-next {
  right: 20px;
  top: 50%;
  transform: translateY(-50%);
}

.image-preview-count {
  position: absolute;
  bottom: 20px;
  left: 50%;
  z-index: 10;
  transform: translateX(-50%);
  padding: 4px 10px;
  border-radius: 12px;
  background: rgba(0, 0, 0, 0.45);
  color: #fff;
  font-size: 12px;
  line-height: 1.5;
}
</style>
