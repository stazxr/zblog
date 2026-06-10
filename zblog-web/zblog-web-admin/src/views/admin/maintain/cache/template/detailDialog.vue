<template>
  <el-dialog
    title="缓存详情"
    :visible.sync="dialogVisible"
    :fullscreen="device === 'mobile'"
    :destroy-on-close="true"
    :close-on-click-modal="true"
    :close-on-press-escape="true"
    :before-close="handleClose"
    append-to-body
    width="520px"
  >
    <div class="content-wrapper">
      <!-- JSON -->
      <json-viewer v-if="isJson" :value="jsonValue" :expand-depth="5" copyable boxed sort />
      <!-- 普通文本 -->
      <pre v-else class="text-content">{{ textValue }}</pre>
    </div>
  </el-dialog>
</template>

<script>
export default {
  props: {
    dialogVisible: {
      type: Boolean,
      default: false
    }
  },
  data() {
    return {
      value: null
    }
  },
  computed: {
    device() {
      return this.$store.state.app.device
    },
    isJson() {
      return this.parseResult.success
    },
    jsonValue() {
      return this.parseResult.value
    },
    textValue() {
      if (this.value == null) {
        return '缓存为空或缓存已失效'
      }
      return String(this.value)
    },
    parseResult() {
      if (this.value == null) {
        return { success: false, value: '缓存为空或缓存已失效' }
      }

      // Object
      if (typeof this.value === 'object') {
        return { success: true, value: this.value }
      }

      // String
      if (typeof this.value === 'string') {
        try {
          return {
            success: true,
            value: JSON.parse(this.value)
          }
        } catch (e) {
          return { success: false, value: this.value }
        }
      }

      return { success: false, value: this.value }
    }
  },
  methods: {
    initData(key) {
      this.$nextTick(() => {
        this.queryDetail(key)
      })
    },
    queryDetail(key) {
      this.$mapi.cache.value({ key: key }).then(res => {
        this.value = res.data
      }).catch(_ => {
        this.value = null
      })
    },
    doClose() {
      this.value = null
      this.$emit('showDetailDone')
    },
    handleClose() {
      this.$confirm('确认关闭？').then(_ => {
        this.doClose()
      }).catch(_ => {})
    }
  }
}
</script>

<style scoped>
.content-wrapper {
  max-height: 600px;
  overflow: auto;
}

.text-content {
  margin: 0;
  padding: 12px;
  background: #f5f7fa;
  border-radius: 4px;
  white-space: pre-wrap;
  word-break: break-word;
}
</style>
