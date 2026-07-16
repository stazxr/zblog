<template>
  <div>
    <div class="message-banner" :style="cover">
      <div class="message-container">
        <div class="animated fadeInUp message-input-wrapper">
          <input v-model="messageContent" placeholder="说点什么吧" maxlength="200" @click="showSendBtn = true" @keyup.enter="addBarrageMessage">
          <button v-show="showSendBtn" class="ml-3 animated bounceInLeft" @click="addBarrageMessage">发送</button>
        </div>
      </div>
      <!-- 弹幕 -->
      <Barrage ref="barrageRef" @like="likeBarrageMessage" />
    </div>
  </div>
</template>

<script>
import { getPageRandomCover } from '@/utils/theme'
import Barrage from '@/components/barrage/Barrage.vue'
export default {
  name: 'BarrageMessage',
  components: {
    Barrage
  },
  data() {
    return {
      cover: null,
      showSendBtn: false,
      messageContent: null
    }
  },
  created() {
    this.cover = getPageRandomCover(this.$store.state.pages, 'message')
  },
  mounted() {
    this.$nextTick(() => {
      this.initBarrage()
    })
  },
  beforeDestroy() {
    this.$ws.unsubscribe('/topic/barrageMessage')
  },
  methods: {
    /**
     * 初始化弹幕
     */
    initBarrage() {
      /**
       * 加载历史弹幕
       */
      this.queryBarrageMessageList()
      /**
       * 监听实时弹幕
       */
      this.$ws.subscribe('/topic/barrageMessage', this.receiveBarrageMessage)
    },
    /**
     * 接受实时弹幕
     */
    receiveBarrageMessage(barrageMessage) {
      if (barrageMessage && this.$refs.barrageRef) {
        console.log('barrageMessage', barrageMessage)
        this.$refs.barrageRef.add(barrageMessage)
      }
    },
    /**
     * 查询历史弹幕
     */
    queryBarrageMessageList() {
      this.$mapi.portal.queryBarrageMessageList().then(({ data }) => {
        if (Array.isArray(data)) {
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
      if (this.messageContent == null || this.messageContent.trim() === '') {
        return
      }

      const param = { content: this.messageContent.trim() }
      this.$mapi.portal.addBarrageMessage(param).then(_ => {
        this.messageContent = null
        this.$toast({ type: 'success', message: '发送成功' })
      }).catch(_ => {
        this.$toast({ type: 'error', message: '发送失败' })
      })
    },
    /**
     * 点赞
     */
    likeBarrageMessage(item) {
      this.$mapi.portal.likeBarrageMessage({ barrageMessageId: item.id }).then(() => {
        item.likeCount++
      })
    }
  }
}
</script>

<style scoped>
.message-banner {
  position: absolute;
  top: -60px;
  left: 0;
  right: 0;
  height: 100vh;
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
  background: rgba(255,255,255,0.1);
}
.message-input-wrapper input::-webkit-input-placeholder {
  color: #eeee;
}
.message-input-wrapper button {
  outline: none;
  border-radius: 20px;
  height: 100%;
  padding: 0 1.25rem;
  border: #fff 1px solid;
}
</style>
