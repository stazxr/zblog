<template>
  <div ref="container" class="barrage-container">
    <div
      v-for="item in showingList"
      :key="item.id"
      class="barrage-item"
      :class="{
        paused:item.hover
      }"
      :style="getStyle(item)"
      @mouseenter="mouseenter(item)"
      @mouseleave="mouseleave(item)"
      @animationend="animationEnd(item)"
    >

      <!-- 用户头像 -->
      <img v-if="showAvatar" class="barrage-avatar" :src="$getAvatar(item.avatar)" alt="">

      <!-- 用户昵称 -->
      <span v-if="showNickname" class="barrage-nickname">{{ item.nickname || '游客' }}</span>

      <!-- 内容 -->
      <span class="barrage-content">{{ item.content }}</span>

      <!-- 点赞 -->
      <span v-if="showLike && (item.hover || item.likeCount > 0)" class="barrage-like" @click.stop="like(item)">👍 {{ item.likeCount || 0 }}</span>
    </div>
  </div>
</template>

<script>
import BarrageEngine from './engine/BarrageEngine'
import Constants from './engine/Constants'

export default {
  name: 'Barrage',
  props: {
    /**
     * 是否显示头像
     */
    showAvatar: {
      type: Boolean,
      default: true
    },
    /**
     * 是否显示昵称
     */
    showNickname: {
      type: Boolean,
      default: true
    },
    /**
     * 是否显示点赞
     */
    showLike: {
      type: Boolean,
      default: true
    }
  },
  data() {
    return {
      engine: null,
      defaultAvatar:
      Constants.DEFAULT_AVATAR
    }
  },
  computed: {
    /**
     * 当前展示弹幕
     */
    showingList() {
      if (!this.engine) {
        return []
      }

      return this.engine.showingList
    }
  },
  mounted() {
    this.initEngine()
    window.addEventListener('resize', this.resize)
  },
  beforeDestroy() {
    window.removeEventListener('resize', this.resize)
    if (this.engine) {
      this.engine.destroy()
    }
  },
  methods: {
    /**
     * 初始化弹幕引擎
     */
    initEngine() {
      this.engine = new BarrageEngine({
        containerWidth: this.$refs.container.clientWidth,
        containerHeight: this.$refs.container.clientHeight,

        /**
         * 新增弹幕回调
         */
        onAdd: (_) => {
          this.$forceUpdate()
        }
      })
    },
    /**
     * 外部调用
     *
     * websocket实时弹幕
     */
    add(item) {
      if (!this.engine) {
        return
      }
      this.engine.add(item)
    },
    /**
     * 外部调用
     *
     * 历史弹幕
     */
    addAll(list) {
      if (!this.engine) {
        return
      }
      this.engine.addAll(list)
    },
    /**
     * css动画参数
     */
    getStyle(item) {
      return {
        top: item.top + 'px',
        '--move-distance': '-' + item.distance + 'px',
        animationDuration: item.duration + 's'
      }
    },
    /**
     * 鼠标进入
     */
    mouseenter(item) {
      this.engine.pause(item)
    },
    /**
     * 鼠标离开
     */
    mouseleave(item) {
      this.engine.resume(item)
    },
    /**
     * 动画结束
     */
    animationEnd(item) {
      this.engine.remove(item.id)
    },
    /**
     * 点赞
     */
    like(item) {
      this.$emit('like', item)
    },
    /**
     * 浏览器大小变化
     */
    resize() {
      if (!this.engine) {
        return
      }
      this.engine.scheduler.resize(this.$refs.container.clientWidth, this.$refs.container.clientHeight)
    }
  }
}

</script>
<style scoped>
@import "./barrage.css";
</style>
