<template>
  <div>
    <div class="message-banner" :style="cover">
      <div class="message-container">
        <!-- <h1 class="message-title">弹幕墙</h1> -->
        <div class="animated fadeInUp message-input-wrapper">
          <input v-model="messageContent" placeholder="说点什么吧" maxlength="200" @click="showBtn = true" @keyup.enter="addToList">
          <button v-show="showBtn" class="ml-3 animated bounceInLeft" @click="addToList">
            发送
          </button>
        </div>
      </div>
      <!-- 弹幕列表 -->
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
      showBtn: false,
      messageContent: '',
      barrageList: []
    }
  },
  computed: {
    cover() {
      let cover = ''
      this.$store.state.pageList.forEach(item => {
        if (item['pageLabel'] === 'message') {
          cover = item['pageCover']
        }
      })
      return 'background: url(' + cover + ') center center / cover no-repeat'
    }
  },
  mounted() {
    this.listMessage()
  },
  methods: {
    addToList() {
      if (this.messageContent.trim() === '') {
        this.$toast({ type: 'warning', message: '内容不能为空' })
        return false
      }

      // 访问信息
      const userAvatar = this.$store.state.user.id ? this.$store.state.user.avatar : this.$store.state.otherConfig['touristAvatar']
      const userNickname = this.$store.state.user.id ? this.$store.state.user.nickname : '游客'

      const message = {
        avatar: userAvatar,
        nickname: userNickname,
        messageContent: this.messageContent,
        time: Math.floor(Math.random() * (10 - 7)) + 7
      }
      this.messageContent = ''
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
.message-banner {
  position: absolute;
  top: -60px;
  left: 0;
  right: 0;
  height: 100vh;
  background-color: #49b1f5;
  animation: header-effect 1s;
}
.message-title {
  color: #eee;
  animation: title-scale 1s;
}
.message-container {
  position: absolute;
  width: 360px;
  top: 35%;
  left: 0;
  right: 0;
  text-align: center;
  z-index: 5;
  margin: 0 auto;
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
}
.barrage-items {
  background: rgb(0, 0, 0, 0.7);
  border-radius: 100px;
  color: #fff;
  padding: 5px 10px 5px 5px;
  align-items: center;
  display: flex;
}
</style>
