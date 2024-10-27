<template>
  <div>
    <div v-show="showChatContainer" class="chat-container animated bounceInUp">
      <div class="chat-container-header">
        <img width="32" height="32" :src="logoImg" alt="">
        <div style="margin-left: 12px;">
          <div><strong>小机器人</strong></div>
          <div style="font-size: 10px;">基于 ChatGPT API 的智能 AI</div>
        </div>
        <v-icon class="chat-container-close iconfont icon-guanbi" @click="showChatContainer = false" />
      </div>

      <div id="chat-container-message" class="chat-container-message">
        <div v-for="(item, index) of chatRecordList" :key="index" :class="isMyMessage(item)">
          <!-- 头像 -->
          <img :src="item.avatar" :class="isLeft(item)" alt="">
          <div>
            <div v-if="!isSelf(item)" class="nickname">
              {{ item.nickname }}
              <span style="margin-left:12px">{{ item.createTime | hour }}</span>
            </div>
            <!-- 内容 -->
            <div ref="content" :class="isMyContent(item)">
              <!-- 文字消息 -->
              <div v-html="item.content" />
            </div>
          </div>
        </div>
      </div>

      <div class="chat-container-footer">
        <v-icon style="margin-right: 8px;font-size: 1.2rem;" class="iconfont icon-jianpan" />
        <textarea ref="chatInput" v-model="content" placeholder="请输入内容" @keydown.enter="sendMessage($event)" />
        <!-- 发送按钮 -->
        <i :class="isInput" style="margin-left: 8px;" @click="sendMessage" />
      </div>
    </div>

    <div class="robot-logo" @click="openOrCloseChatContainer">
      <img width="100%" height="100%" :src="logoImg" alt="">
    </div>
  </div>
</template>

<script>
export default {
  data: function() {
    return {
      logoImg: 'https://file.suntaoblog.com/image/logo.png',
      showChatContainer: false,
      content: '',
      chatRecordList: []
    }
  },
  computed: {
    userId() {
      return this.$store.state.user.id
    },
    isInput() {
      return this.content.trim() !== '' ? 'iconfont icon-fasong submit-btn' : 'iconfont icon-fasong'
    },
    isSelf() {
      return function(item) {
        return item.userId === this.userId
      }
    },
    isLeft() {
      return function(item) {
        return this.isSelf(item) ? 'user-avatar right-avatar' : 'user-avatar left-avatar'
      }
    },
    isMyContent() {
      return function(item) {
        return this.isSelf(item) ? 'my-content' : 'user-content'
      }
    },
    isMyMessage() {
      return function(item) {
        return this.isSelf(item) ? 'my-message' : 'user-message'
      }
    }
  },
  updated() {
    const ele = document.getElementById('chat-container-message')
    ele.scrollTop = ele.scrollHeight
  },
  methods: {
    openOrCloseChatContainer() {
      this.showChatContainer = !this.showChatContainer
    },
    sendMessage(e) {
      e.preventDefault()

      // 需要登录才可以使用该功能
      if (!this.$store.state.user.id) {
        this.$toast({ type: 'warning', message: '登录后才可以使用该功能' })
        this.$store.state.loginFlag = true
        return false
      }

      if (this.content.trim() === '') {
        this.$toast({ type: 'error', message: '内容不能为空' })
        return false
      }

      const param = {
        userId: this.userId,
        content: this.content
      }

      // 发送消息
      console.log('param', param)
    }
  }
}
</script>

<style scoped>
.robot-logo {
  background: rgba(0, 0, 0, 0);
  border-radius: 90% !important;
  position: fixed;
  bottom: 15px;
  right: 5px;
  cursor: var(--globalCursor);
  height: 50px !important;
  width: 50px !important;
  z-index: 1000 !important;
  user-select: none;
}

.chat-container {
  box-shadow: 0 5px 40px rgba(0, 0, 0, 0.16) !important;
  font-size: 14px;
  background: #f4f6fb;
  z-index: 1200;
}
.chat-container-header {
  display: flex;
  align-items: center;
  padding: 20px 24px;
  background: #ffffff;
  border-radius: 1rem 1rem 0 0;
  box-shadow: 0 10px 15px -16px rgba(50, 50, 93, 0.08),
  0 4px 6px -8px rgba(50, 50, 93, 0.04);
}
.chat-container-message {
  position: absolute;
  width: 100%;
  padding: 20px 16px 0 16px;
  top: 80px;
  bottom: 50px;
  overflow-y: auto;
  overflow-x: hidden;
}
.chat-container-footer {
  padding: 8px 16px;
  position: absolute;
  width: 100%;
  bottom: 0;
  background: #f7f7f7;
  border-radius: 0 0 1rem 1rem;
  display: flex;
  align-items: center;
}
.chat-container-footer textarea {
  background: #fff;
  padding-left: 10px;
  padding-top: 8px;
  width: 100%;
  height: 32px;
  outline: none;
  resize: none;
  overflow: hidden;
  font-size: 13px;
}

.voice-btn {
  font-size: 13px;
  outline: none;
  height: 32px;
  width: 100%;
  background: #fff;
  border-radius: 2px;
}
.close-voice {
  position: absolute;
  bottom: 60px;
  left: 30px;
  display: inline-block;
  height: 50px;
  width: 50px;
  line-height: 50px;
  border-radius: 50%;
  text-align: center;
  background: #fff;
}

.submit-btn {
  color: rgb(31, 147, 255);
}

@media (min-width: 760px) {
  .chat-container {
    position: fixed;
    color: #4c4948 !important;
    bottom: 84px;
    right: 35px;
    height: calc(85% - 64px - 20px);
    max-height: 590px !important;
    min-height: 250px !important;
    width: 400px !important;
    border-radius: 16px !important;
  }
  .chat-container-close {
    display: none;
  }
}

@media (max-width: 760px) {
  .chat-container {
    position: fixed;
    top: 0;
    bottom: 0;
    right: 0;
    left: 0;
  }
  .chat-container-close {
    display: block;
    margin-left: auto;
  }
}

.text {
  color: #999;
  text-align: center;
  font-size: 12px;
  margin-bottom: 12px;
}
.user-message {
  display: flex;
  margin-bottom: 10px;
}
.my-message {
  display: flex;
  margin-bottom: 10px;
  justify-content: flex-end;
}
.left-avatar {
  margin-right: 10px;
}
.right-avatar {
  order: 1;
  margin-left: 10px;
}
.user-avatar {
  width: 40px;
  height: 40px;
  border-radius: 50%;
}
.nickname {
  display: flex;
  align-items: center;
  font-size: 12px;
  margin-top: 3px;
  margin-bottom: 5px;
}
.user-content {
  position: relative;
  background-color: #fff;
  padding: 10px;
  border-radius: 5px 20px 20px 20px;
  width: fit-content;
  white-space: pre-line;
  word-wrap: break-word;
  word-break: break-all;
}
.my-content {
  position: relative;
  border-radius: 20px 5px 20px 20px;
  padding: 12px;
  background: #12b7f5;
  color: #fff;
  white-space: pre-line;
  word-wrap: break-word;
  word-break: break-all;
}

.back-menu {
  font-size: 13px;
  border-radius: 2px;
  position: absolute;
  background: rgba(255, 255, 255, 0.9);
  color: #000;
  width: 80px;
  height: 35px;
  text-align: center;
  line-height: 35px;
  display: none;
}
.voice {
  position: fixed;
  z-index: 1500;
  bottom: 52px;
  left: 0;
  right: 0;
  top: 80px;
  background: rgba(0, 0, 0, 0.8);
}
</style>
