<template>
  <el-dialog
    append-to-body
    title="定时发布"
    :visible="visible"
    :close-on-click-modal="false"
    :close-on-press-escape="!confirmLoading"
    :show-close="!confirmLoading"
    width="460px"
    @close="handleCancel"
  >
    <div class="publish-time-dialog">
      <div class="publish-time-notice">
        <i class="el-icon-time" />
        <span>请选择发布时间，范围为当前时间后 1 小时至 30 天。</span>
      </div>

      <div class="publish-time-fields">
        <el-date-picker
          v-model="publishDate"
          type="date"
          value-format="yyyy-MM-dd"
          placeholder="选择日期"
          :clearable="false"
          :editable="false"
          :picker-options="datePickerOptions"
          @change="handleDateChange"
        />

        <el-time-select
          v-model="publishTime"
          placeholder="选择时间"
          :clearable="false"
          :disabled="!publishDate"
          :picker-options="timePickerOptions"
        />
      </div>

      <div v-if="publishDate && publishTime" class="publish-time-preview">
        <i class="el-icon-circle-check" />
        <span>
          计划发布时间：
          <strong>{{ publishDate }} {{ publishTime }}</strong>
        </span>
      </div>

      <div v-else class="publish-time-tip">
        请选择日期和时间，时间选项以 15 分钟为间隔。
      </div>
    </div>

    <div slot="footer" class="dialog-footer">
      <el-button
        :disabled="confirmLoading"
        @click="handleCancel"
      >
        取消
      </el-button>
      <el-button
        type="primary"
        :loading="confirmLoading"
        :disabled="!canConfirm"
        @click="handleConfirm"
      >
        确认定时发布
      </el-button>
    </div>
  </el-dialog>
</template>

<script>
export default {
  name: 'PublishTimeDialog',
  props: {
    visible: {
      type: Boolean,
      default: false
    },
    confirmLoading: {
      type: Boolean,
      default: false
    }
  },
  data() {
    return {
      publishDate: '',
      publishTime: ''
    }
  },
  computed: {
    // 日期选择范围。
    datePickerOptions() {
      if (!this.visible) {
        return {
          disabledDate: () => false
        }
      }

      return {
        disabledDate: time => {
          const bounds = this.getPublishBounds()
          const date = new Date(
            time.getFullYear(),
            time.getMonth(),
            time.getDate()
          ).getTime()
          const startDate = new Date(
            bounds.min.getFullYear(),
            bounds.min.getMonth(),
            bounds.min.getDate()
          ).getTime()
          const endDate = new Date(
            bounds.max.getFullYear(),
            bounds.max.getMonth(),
            bounds.max.getDate()
          ).getTime()

          return date < startDate || date > endDate
        }
      }
    },

    // 根据所选日期限制时间选项。
    timePickerOptions() {
      if (!this.visible || !this.publishDate) {
        return {
          start: '00:00',
          end: '23:45',
          step: '00:15'
        }
      }

      const range = this.getTimeRange(this.publishDate)

      return {
        start: range.start,
        end: range.end,
        step: '00:15'
      }
    },

    // 是否可以确认。
    canConfirm() {
      return !!(
        this.publishDate && this.publishTime &&
        this.isPublishTimeValid(this.publishDate, this.publishTime)
      )
    }
  },
  watch: {
    visible(value) {
      if (value) {
        this.resetSelection()
      }
    }
  },
  methods: {
    /**
     * 获取允许定时发布的时间边界。
     * 最早时间向上取整到 15 分钟，最晚时间向下取整到 15 分钟。
     */
    getPublishBounds() {
      const now = new Date()
      const min = new Date(now.getTime() + 60 * 60 * 1000)
      const max = new Date(now.getTime() + 30 * 24 * 60 * 60 * 1000)

      min.setSeconds(0, 0)
      const minRemainder = min.getMinutes() % 15
      if (minRemainder !== 0) {
        min.setMinutes(min.getMinutes() + 15 - minRemainder)
      }

      max.setSeconds(0, 0)
      max.setMinutes(max.getMinutes() - (max.getMinutes() % 15))

      return { min, max }
    },

    /**
     * 格式化日期为 yyyy-MM-dd。
     */
    formatDate(date) {
      return [
        date.getFullYear(),
        this.padZero(date.getMonth() + 1),
        this.padZero(date.getDate())
      ].join('-')
    },

    /**
     * 格式化时间为 HH:mm。
     */
    formatTime(date) {
      return this.padZero(date.getHours()) + ':' + this.padZero(date.getMinutes())
    },

    /**
     * 根据选择的日期计算时间选择器的起止时间。
     */
    getTimeRange(dateString) {
      if (!dateString) {
        return { start: '00:00', end: '23:45' }
      }

      const bounds = this.getPublishBounds()
      const startDate = this.formatDate(bounds.min)
      const endDate = this.formatDate(bounds.max)

      let start = '00:00'
      let end = '23:45'

      if (dateString === startDate) {
        start = this.formatTime(bounds.min)
      }

      if (dateString === endDate) {
        end = this.formatTime(bounds.max)
      }

      return { start, end }
    },

    /**
     * 日期变化后清理不再合法的时间。
     */
    handleDateChange() {
      if (!this.publishDate) {
        this.publishTime = ''
        return
      }

      if (!this.isPublishTimeValid(this.publishDate, this.publishTime)) {
        this.publishTime = ''
      }
    },

    /**
     * 校验完整发布时间，避免只依赖日期和时间选择器的选项限制。
     */
    isPublishTimeValid(dateString, timeString) {
      if (!dateString || !timeString) {
        return false
      }

      const selected = new Date(dateString + 'T' + timeString + ':00')
      if (Number.isNaN(selected.getTime())) {
        return false
      }

      const now = new Date()
      const min = new Date(now.getTime() + 60 * 60 * 1000)
      const max = new Date(now.getTime() + 72 * 60 * 60 * 1000)

      return selected.getTime() >= min.getTime() &&
        selected.getTime() <= max.getTime() &&
        selected.getMinutes() % 15 === 0 &&
        selected.getSeconds() === 0
    },

    /**
     * 确认发布时间，向父组件提交 yyyy-MM-dd HH:mm 格式。
     */
    handleConfirm() {
      if (!this.canConfirm) {
        this.$message.warning('请选择当前时间后 1 小时至 3 天内的发布时间')
        return
      }

      this.$emit('confirm', this.publishDate + ' ' + this.publishTime)
    },

    /**
     * 取消选择。
     */
    handleCancel() {
      if (this.confirmLoading) {
        return
      }

      this.$emit('update:visible', false)
      this.$emit('cancel')
    },

    /**
     * 重置本次选择。
     */
    resetSelection() {
      this.publishDate = ''
      this.publishTime = ''
    },

    /**
     * 数字补零。
     */
    padZero(value) {
      return value < 10 ? '0' + value : String(value)
    }
  }
}
</script>

<style scoped>
.publish-time-dialog {
  color: #606266;
}

.publish-time-notice {
  display: flex;
  align-items: flex-start;
  padding: 12px 14px;
  background: #f4f8ff;
  border: 1px solid #d9e8ff;
  border-radius: 4px;
  color: #606f85;
  font-size: 13px;
  line-height: 21px;
}

.publish-time-notice i {
  margin: 3px 8px 0 0;
  color: #409eff;
  font-size: 16px;
}

.publish-time-fields {
  display: flex;
  gap: 10px;
  margin-top: 22px;
}

.publish-time-fields .el-date-editor,
.publish-time-fields .el-input {
  width: 50%;
}

.publish-time-preview {
  display: flex;
  align-items: flex-start;
  margin-top: 20px;
  padding: 12px;
  background: #f0f9eb;
  border-radius: 4px;
  color: #606266;
  font-size: 13px;
  line-height: 21px;
  word-break: break-word;
}

.publish-time-preview i {
  margin: 3px 7px 0 0;
  color: #67c23a;
  font-size: 15px;
}

.publish-time-preview strong {
  color: #303133;
  font-weight: 600;
}

.publish-time-tip {
  margin-top: 20px;
  color: #909399;
  font-size: 12px;
  line-height: 20px;
}

.dialog-footer {
  text-align: right;
}
</style>
