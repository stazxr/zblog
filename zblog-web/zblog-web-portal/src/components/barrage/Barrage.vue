<template>
  <div class="barrage-container">
    <div
      v-for="item in showingList"
      :key="item.uuid"
      class="barrage-item"
      :class="{ paused: item.hover }"
      :style="item.style"
      @mouseenter="mouseenter(item)"
      @mouseleave="mouseleave(item)"
    >
      <!-- 头像 -->
      <img class="barrage-avatar" :src="item.avatar || defaultAvatar" alt="">
      <!-- 昵称 -->
      <span class="barrage-nickname">{{ item.nickname || '游客' }}</span>
      <!-- 内容 -->
      <span class="barrage-content">{{ item.content || item.messageContent }}</span>
      <!-- 点赞 -->
      <span v-show="item.hover" class="barrage-like" @click.stop="like(item)">
        👍 {{ item.likeCount || 0 }}
      </span>
    </div>
  </div>
</template>

<script>
export default {
  name: 'Barrage',
  props: {
    // 初始化弹幕
    list: {
      type: Array,
      default() {
        return []
      }
    },
    // 轨道数量
    trackCount: {
      type: Number,
      default: 6
    },
    // 最小动画时间
    minDuration: {
      type: Number,
      default: 10
    },
    // 最大动画时间
    maxDuration: {
      type: Number,
      default: 15
    }
  },
  data() {
    return {
      showingList: [],
      tracks: [],
      defaultAvatar: require('@/assets/avatar/default.png')
    }
  },
  watch: {
    list: {
      immediate: true,
      deep: true,
      handler(val) {
        if (!val || !val.length) {
          return
        }
        val.forEach(item => {
          this.add(item)
        })
      }
    }
  },
  mounted() {
    this.initTracks()
  },
  methods: {
    // 初始化轨道
    initTracks() {
      this.tracks = []
      for (let i = 0; i < this.trackCount; i++) {
        this.tracks.push({
          index: i,
          availableTime: 0
        })
      }
    },
    /**
     * 外部调用新增弹幕
     *
     * this.$refs.barrage.add(data)
     */
    add(item) {
      if (!item) {
        return
      }
      const barrage = {
        ...item,
        uuid: Date.now() + Math.random(),
        hover: false
      }
      const track = this.getTrack()
      const duration = this.random(this.minDuration, this.maxDuration)
      barrage.style = {
        top: track.index * 45 + 'px',
        animationDuration: duration + 's'
      }
      this.showingList.push(barrage)
      // 动画结束移除
      setTimeout(() => {
        this.remove(barrage.uuid)
      }, duration * 1000 + 500)
    },
    // 获取空闲轨道
    getTrack() {
      const now = Date.now()
      let track = this.tracks.find(t => {
        return t.availableTime <= now
      })
      // 没有空闲轨道
      if (!track) {
        track = this.tracks[Math.floor(Math.random() * this.tracks.length)]
      }
      track.availableTime = now + 3000
      return track
    },
    mouseenter(item) {
      item.hover = true
    },
    mouseleave(item) {
      item.hover = false
    },
    remove(uuid) {
      const index = this.showingList.findIndex(item => item.uuid === uuid)
      if (index > -1) {
        this.showingList.splice(index, 1)
      }
    },
    // 点赞
    like(item) {
      this.$emit('like', item)
    },
    random(min, max) {
      return (Math.random() * (max - min) + min).toFixed(1)
    }
  }
}

</script>

<style scoped>
@import "./barrage.css";
</style>
