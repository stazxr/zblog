<template>
  <svg
    class="zblog-icon"
    :class="customClass"
    :style="iconStyle"
    :width="iconSize"
    :height="iconSize"
    :aria-hidden="title ? null : 'true'"
    :aria-label="title || null"
    v-bind="$attrs"
    @click="$emit('click', $event)"
  >
    <title v-if="title">{{ title }}</title>
    <use :href="iconId" />
  </svg>
</template>

<script>
export default {
  name: 'Icon',
  inheritAttrs: false,
  props: {
    /**
     * 图标名称
     */
    name: {
      type: String,
      required: true
    },
    /**
     * 是否激活
     *
     * like + active
     * => like-fill
     */
    active: {
      type: Boolean,
      default: false
    },
    /**
     * 图标大小
     */
    size: {
      type: [Number, String],
      default: 20
    },
    /**
     * 图标颜色
     */
    color: {
      type: String,
      default: 'currentColor'
    },
    /**
     * 自定义 class
     */
    customClass: {
      type: [String, Array, Object],
      default: null
    },
    /**
     * 无障碍标题
     */
    title: {
      type: String,
      default: ''
    }
  },
  computed: {
    /**
     * Symbol ID
     *
     * name = comment
     * ↓
     * #icon-comment
     */
    iconId() {
      const iconName = this.active ? `${this.name}-fill` : this.name
      return `#icon-${iconName}`
    },
    iconSize() {
      if (typeof this.size === 'number') {
        return `${this.size}px`
      }

      return this.size
    },
    iconStyle() {
      return {
        color: this.color
      }
    }
  }
}
</script>

<style scoped>
.zblog-icon {
  display: inline-block;
  vertical-align: middle;
  fill: currentColor;
  overflow: hidden;
}
</style>
