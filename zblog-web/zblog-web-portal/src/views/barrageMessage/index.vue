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
      <div class="barrage-container">
        <Barrage ref="barrageRef" :list="barrageList" @like="likeBarrageMessage" />
      </div>
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
      messageContent: null,
      barrageList: []
    }
  },
  created() {
    this.cover = getPageRandomCover(this.$store.state.pages, 'message')
  },
  mounted() {
    this.$ws.subscribe('/topic/barrageMessage', this.receiveBarrageMessage)
    this.queryBarrageMessageList()
  },
  beforeDestroy() {
    this.$ws.unsubscribe('/topic/barrageMessage')
  },
  methods: {
    receiveBarrageMessage(barrageMessage) {
      if (barrageMessage) {
        this.$refs.barrageRef.add(barrageMessage)
      }
    },
    queryBarrageMessageList() {
      this.$mapi.portal.queryBarrageMessageList().then(({ data }) => {
        this.barrageList = data
      }).catch(_ => {
        this.barrageList = []
      })
    },
    addBarrageMessage() {
      if (this.messageContent == null || this.messageContent.trim() === '') {
        return false
      }

      const param = { content: this.messageContent }
      this.messageContent = null
      this.$mapi.portal.addBarrageMessage(param).then(_ => {
        this.$toast({ type: 'success', message: '发送成功' })
      }).catch(_ => {
        this.$toast({ type: 'error', message: '发送失败' })
      })
    },
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
.barrage-container {
  position: absolute;
  top: 50px;
  left: 0;
  right: 0;
  bottom: 0;
  height: calc(100% - 50px);
  width: 100%;
  z-index: 3;
}
.barrage-items {
  background: rgb(0, 0, 0, 0.7);
  border-radius: 100px;
  color: #fff;
  padding: 5px 10px 5px 5px;
  display: flex;
  align-items: center;
}
</style>
