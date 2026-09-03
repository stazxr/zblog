<template>
  <div class="message-page">
    <!-- ==================== 弹幕区域 ==================== -->
    <div class="message-banner" :style="cover">
      <div class="message-container">
        <div class="animated fadeInUp message-input-wrapper">
          <input v-model="messageContent" placeholder="说点什么吧" maxlength="200" @click="showSendBtn = true" @keyup.enter="addBarrageMessage">
          <button v-show="showSendBtn" class="ml-3 animated bounceInLeft" :disabled="messageSending" @click="addBarrageMessage">
            {{ messageSending ? '发送中...' : '发送' }}
          </button>
        </div>
      </div>
      <!-- 弹幕 -->
      <Barrage ref="barrageRef" @like="likeBarrageMessage" />
      <!-- 向下滚动 -->
      <div class="message-scroll-down" @click="scrollToComment">
        <span>向下查看留言</span>
        <v-icon>
          mdi-chevron-double-down
        </v-icon>
      </div>
    </div>

    <!-- ==================== 评论区域 ==================== -->
    <section ref="commentSection" class="message-comment-section">
      <div class="message-comment-container">
        <Comment title="留言" :type="2" :object-id="0" @getCommentCount="getCommentCount" />
      </div>
    </section>
  </div>
</template>

<script>
import { getPageRandomCover } from '@/utils/theme'
import Barrage from '@/components/barrage/Barrage.vue'
import Comment from '@/components/comment/Comment.vue'
export default {
  name: 'BarrageMessage',
  components: {
    Barrage,
    Comment
  },
  data() {
    return {
      cover: null,
      showSendBtn: false,
      messageContent: '',
      messageSending: false,
      topicDestination: '/topic/barrageMessage',
      commentCount: 0
    }
  },
  created() {
    this.cover = getPageRandomCover(this.$store.state.pages, 'BarrageMessage')
  },
  mounted() {
    this.$nextTick(() => {
      this.initData()
    })
  },
  beforeDestroy() {
    this.$ws.unsubscribe(this.topicDestination)
  },
  methods: {
    /**
     * 初始化弹幕
     */
    initData() {
      /**
       * 加载历史弹幕
       */
      this.queryBarrageMessageList()
      /**
       * 监听实时弹幕
       */
      this.$ws.subscribe(this.topicDestination, this.receiveBarrageMessage)
    },
    /**
     * 接收实时弹幕
     */
    receiveBarrageMessage(barrageMessage) {
      if (barrageMessage && this.$refs.barrageRef) {
        this.$refs.barrageRef.add(barrageMessage)
      }
    },
    /**
     * 查询历史弹幕
     */
    queryBarrageMessageList() {
      this.$mapi.portal.queryBarrageMessageList().then(({ data }) => {
        if (Array.isArray(data) && this.$refs.barrageRef) {
          this.$refs.barrageRef.addAll(data)
        }
      }).catch(e => {
        console.log('load barrage message error', e)
        this.$toast({ type: 'error', message: '历史弹幕加载失败...' })
      })
    },
    /**
     * 发布弹幕
     */
    addBarrageMessage() {
      if (this.messageSending) {
        return
      }

      const content = this.messageContent && this.messageContent.trim()
      if (!content) {
        return
      }

      this.messageSending = true
      this.$mapi.portal.addBarrageMessage({ content: content }).then(() => {
        this.messageContent = ''
        this.showSendBtn = false
        this.$toast({ type: 'success', message: '发送成功' })
      }).catch(e => {
        this.$toast({ type: 'error', message: e.message || '发送失败' })
      }).finally(() => {
        this.messageSending = false
      })
    },
    /**
     * 点赞弹幕
     */
    likeBarrageMessage(item) {
      this.$mapi.portal.likeBarrageMessage({ barrageMessageId: item.id }).then(res => {
        if (res.data) {
          this.$set(item, 'likeCount', Number(item.likeCount || 0) + 1)
        }
      }).catch(e => {
        this.$toast({ type: 'error', message: e.message || '点赞失败' })
      })
    },
    /**
     * 滚动到评论
     */
    scrollToComment() {
      const element = this.$refs.commentSection
      if (!element) {
        return
      }

      const top = element.getBoundingClientRect().top + window.pageYOffset - 20
      window.scrollTo({
        top,
        behavior: 'smooth'
      })
    },
    /**
     * 评论数量
     */
    getCommentCount(count) {
      this.commentCount = count
    }
  }
}
</script>

<style scoped>
.message-banner {
  position: relative;
  width: 100%;
  height: 100vh;
  /*height: calc(100vh + 60px);*/
  margin-top: -60px;
  overflow: hidden;
}
.message-container {
  position: absolute;
  width: 360px;
  top: 35%;
  left: 0;
  right: 0;
  text-align: center;
  margin: 0 auto;
  z-index: 5;
  color: #fff;
}
.message-input-wrapper {
  display: flex;
  justify-content: center;
  height: 2.5rem;
  margin-top: 2rem;
}
.message-input-wrapper input {
  outline: none;
  width: 70%;
  border-radius: 20px;
  height: 100%;
  padding: 0 1.25rem;
  color: #eee;
  border: #fff 1px solid;
  background: rgba(255, 255, 255, 0.1);
}
.message-input-wrapper input::-webkit-input-placeholder {
  color: #eeee;
}
.message-input-wrapper button {
  outline: none;
  border-radius: 20px;
  width: 95px;
  min-width: 95px;
  height: 100%;
  white-space: nowrap;
  padding: 0 1.15rem;
  border: #fff 1px solid;
  background: transparent;
  color: #fff;
  cursor: pointer;
}
.message-scroll-down {
  position: absolute;
  z-index: 10;
  bottom: 25px;
  left: 50%;
  display: flex;
  flex-direction: column;
  align-items: center;
  transform: translateX(-50%);
  color: rgba(255, 255, 255, 0.8);
  font-size: 12px;
  cursor: pointer;
  user-select: none;
  animation: message-scroll-down 2s infinite;
}
.message-scroll-down i {
  margin-top: 5px;
  font-size: 18px;
}

@keyframes message-scroll-down {
  0%,
  100% {
    transform: translate(-50%, 0);
    opacity: 0.6;
  }
  50% {
    transform: translate(-50%, 6px);
    opacity: 1;
  }
}
.message-comment-section {
  position: relative;
  padding: 50px 20px 70px;
  background: #fff;
}
.message-comment-container {
  width: 100%;
  max-width: 900px;
  margin: 0 auto;
}

@media screen and (max-width: 600px) {
  .message-banner {
    height: 100vh;
    min-height: 520px;
  }
  .message-container {
    width: 360px;
    max-width: calc(100% - 40px);
  }
  .message-scroll-down {
    bottom: 18px;
  }
  .message-comment-section {
    padding: 35px 15px 50px;
  }
}
</style>
