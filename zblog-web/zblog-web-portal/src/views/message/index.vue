<template>
  <div>
    <div class="message-banner">
      <!-- 背景 -->
      <div class="background-blur" :style="backgroundStyle" />
      <!-- 内容 -->
      <div class="message-container">
        <div class="animated fadeInUp message-input-wrapper">
          <input v-model="messageContent" placeholder="说点什么吧" maxlength="200" @click="showBtn = true" @keyup.enter="addToList">
          <button v-show="showBtn" class="ml-3 animated bounceInLeft" @click="addToList">
            发送
          </button>
        </div>
      </div>
      <!-- 弹幕 -->
      <div class="barrage-container">
        <vue-baberrage :barrage-list="barrageList">
          <template v-slot:default="slotProps">
            <span class="barrage-items">
              <img :src="slotProps.item.avatar" width="30" height="30" style="border-radius:50%" alt="">
              <span class="ml-2">{{ slotProps.item.nickname }} :</span>
              <span class="ml-2">{{ slotProps.item.messageContent }}</span>
            </span>
          </template>
        </vue-baberrage>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: 'Message',
  data() {
    return {
      backgroundUrl: null,
      backgroundLoaded: false,
      showBtn: false,
      messageContent: '',
      barrageList: []
    }
  },
  computed: {
    backgroundStyle() {
      if (this.backgroundUrl) {
        return `background: url(${this.backgroundUrl}) center center / cover no-repeat`
      }

      return 'background: linear-gradient(135deg, #89f7fe, #66a6ff)'
    }
  },
  mounted() {
    this.listMessage()
    this.loadBackground()
  },
  methods: {
    loadBackground() {
      const item = this.$store.state.pageList.find(i => i.pageLabel === 'message')
      const url = item?.pageCover
      if (!url) {
        this.backgroundUrl = null
        return
      }

      const backgroundImage = new Image()
      backgroundImage.onload = () => {
        this.backgroundUrl = url
        this.backgroundLoaded = true
      }

      backgroundImage.onerror = () => {
        this.backgroundUrl = null
        this.backgroundLoaded = false
      }

      backgroundImage.src = url
    },
    addToList() {
      if (this.messageContent.trim() === '') {
        this.$toast({ type: 'warning', message: '内容不能为空' })
        return false
      }

      // 访问信息
      const userAvatar = this.$store.state.user ? this.$store.state.user.avatar : this.$store.state.otherConfig['touristAvatar']
      const userNickname = this.$store.state.user ? this.$store.state.user.nickname : '游客'

      const message = {
        avatar: userAvatar,
        nickname: userNickname,
        messageContent: this.messageContent,
        time: Math.floor(Math.random() * (10 - 7)) + 7
      }
      this.messageContent = ''
      this.barrageList.push(message)
      this.$mapi.portal.saveMessage(message).then(_ => {
        this.barrageList.push(message)
        this.$toast({ type: 'success', message: '发送成功' })
      }).catch(_ => {
        this.$toast({ type: 'error', message: '发送失败' })
      })
    },
    listMessage() {
      this.$mapi.portal.queryMessageList().then(({ data }) => {
        this.barrageList = data
      }).catch(_ => {
        this.barrageList = []
      })
    }
  }
}
</script>

<style scoped>
/* =========================
   📌 主容器
========================= */
.message-banner {
  position: absolute;
  top: -60px;
  left: 0;
  right: 0;
  height: 100vh;
  /*animation: header-effect 2s;*/
  overflow: hidden;
}
/* =========================
   🌫 背景
========================= */
.background-blur {
  position: absolute;
  inset: 0;
  background-size: cover;
  background-position: center;
  /* filter: blur(1px); */
  /* transform: scale(1.01); */
  z-index: 2;
}
.background-blur::after {
  content: '';
  position: absolute;
  inset: 0;
  /* background: rgba(0, 0, 0, 0.12); */
}
/* =========================
   🧠 输入区域
========================= */
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
/* =========================
   🎯 弹幕层
========================= */
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
