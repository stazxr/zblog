<template>
  <span class="relative-time">
    {{ displayTime }}
  </span>
</template>

<script>
export default {
  name: 'RelativeTime',
  props: {
    /**
     * 时间
     *
     * 支持：
     * 2026-09-08 20:30:00
     * 2026-09-08T20:30:00
     * 时间戳
     * Date
     */
    time: {
      type: [String, Number, Date],
      required: true
    },

    /**
     * 自动更新时间间隔
     * 默认 30 秒
     */
    refreshInterval: {
      type: Number,
      default: 30000
    }
  },

  data() {
    return {
      now: Date.now(),
      timer: null
    }
  },

  computed: {
    displayTime() {
      return this.formatRelativeTime(this.time, this.now)
    }
  },

  mounted() {
    this.startTimer()
  },

  beforeDestroy() {
    this.stopTimer()
  },

  methods: {
    /**
     * 开始定时刷新
     */
    startTimer() {
      if (this.refreshInterval <= 0) {
        return
      }

      this.timer = setInterval(() => {
        this.now = Date.now()
      }, this.refreshInterval)
    },

    /**
     * 清理定时器
     */
    stopTimer() {
      if (this.timer) {
        clearInterval(this.timer)
        this.timer = null
      }
    },

    /**
     * 格式化相对时间
     */
    formatRelativeTime(time, now) {
      const date = this.parseDate(time)

      // 时间无效
      if (!date || isNaN(date.getTime())) {
        return time || ''
      }

      const diff = now - date.getTime()

      // 未来时间不使用「刚刚」
      if (diff < 0) {
        return this.formatDate(date)
      }

      const seconds = Math.floor(diff / 1000)
      const minutes = Math.floor(seconds / 60)
      const hours = Math.floor(minutes / 60)
      const days = Math.floor(hours / 24)

      // 1分钟以内
      if (seconds < 60) {
        return '刚刚'
      }

      // 1小时以内
      if (minutes < 60) {
        return `${minutes}分钟前`
      }

      // 24小时以内
      if (hours < 24) {
        return `${hours}小时前`
      }

      // 昨天
      if (days === 1) {
        return '昨天'
      }

      // 2～6天
      if (days < 7) {
        return `${days}天前`
      }

      // 7天以后
      return this.formatDate(date)
    },

    /**
     * 解析时间
     */
    parseDate(time) {
      if (time instanceof Date) {
        return time
      }

      if (typeof time === 'number') {
        // 兼容秒级时间戳和毫秒级时间戳
        return new Date(
          time < 10000000000
            ? time * 1000
            : time
        )
      }

      if (typeof time !== 'string') {
        return null
      }

      const value = time.trim()

      if (!value) {
        return null
      }

      // MySQL：
      // 2026-09-08 20:30:00
      if (/^\d{4}-\d{2}-\d{2} \d{2}:\d{2}:\d{2}$/.test(value)) {
        return new Date(value.replace(/-/g, '/'))
      }

      // ISO：
      // 2026-09-08T20:30:00
      return new Date(value)
    },

    /**
     * 格式化日期
     *
     * 2026-09-01
     */
    formatDate(date) {
      const year = date.getFullYear()
      const month = String(date.getMonth() + 1).padStart(2, '0')
      const day = String(date.getDate()).padStart(2, '0')

      return `${year}-${month}-${day}`
    }
  }
}
</script>

<style scoped>
.relative-time {
  color: #b0b3b8;
  font-size: 11px;
  line-height: 20px;
  white-space: nowrap;
}
</style>
