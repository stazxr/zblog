<template>
  <div ref="reply" class="reply-input-wrapper" style="display: none">
    <textarea v-model="commentContent" class="comment-textarea" :placeholder="'回复 @' + nickname + '：'" auto-grow dense />
    <div class="emoji-container">
      <span :class="chooseEmoji ? 'emoji-btn-active' : 'emoji-btn'" @click="chooseEmoji = !chooseEmoji">
        <i class="iconfont icon-xiaolian" style="font-size: 25px;" />
      </span>
      <div style="margin-left:auto">
        <button class="cancel-btn v-comment-btn" @click="cancelReply">
          取消
        </button>
        <button :disabled="submitDisabled" class="upload-btn v-comment-btn" @click="insertReply">
          提交
        </button>
      </div>
    </div>
    <!-- 表情框 -->
    <emoji :choose-emoji="chooseEmoji" @addEmoji="addEmoji" />
  </div>
</template>

<script>
import Emoji from './Emoji'
import EmojiList from '../assets/emoji/emoji'
export default {
  components: {
    Emoji
  },
  props: {
    type: {
      type: Number,
      default: null
    }
  },
  data: function() {
    return {
      index: 0,
      chooseEmoji: false,
      nickname: '',
      replyUserId: null,
      replyCommentId: null,
      parentId: null,
      commentContent: '',
      submitDisabled: false
    }
  },
  methods: {
    cancelReply() {
      this.$refs.reply.style.display = 'none'
    },
    addEmoji(text) {
      this.commentContent += text
    },
    insertReply() {
      // 判断登录
      if (!this.$store.state.user.id) {
        this.$store.state.loginFlag = true
        return false
      }

      // 判空
      if (this.commentContent.trim() === '') {
        this.$toast({ type: 'error', message: '评论不能为空' })
        return false
      }

      // 解析表情
      const reg = /\[.+?]/g
      const content = this.commentContent.replace(reg, function(str) {
        return (
          "<img src= '" + EmojiList[str] + "' alt='' width='24' height='24' style='margin: 0 1px;vertical-align: text-bottom' />"
        )
      })

      // 封装参数
      const comment = {
        userId: this.$store.state.user.id,
        replyUserId: this.replyUserId,
        replyCommentId: this.replyCommentId,
        parentId: this.parentId,
        content: content,
        type: this.type
      }

      // 解析评论对象
      const path = this.$route.path
      const arr = path.split('/')
      switch (this.type) {
        case 1:
        case 3:
          comment.objectId = arr[2]
          break
        default:
          break
      }

      // 发送请求
      this.submitDisabled = true
      this.$mapi.portal.replyComment(comment).then(({ code, message }) => {
        if (code === 200) {
          this.chooseEmoji = false
          this.commentContent = ''
          const isReview = this.$store.state.otherConfig['isCommentReview']
          if (isReview) {
            this.$toast({ type: 'warning', message: '回复成功，正在审核中' })
          } else {
            this.$toast({ type: 'success', message: '回复成功' })
          }
          this.$emit('reloadReply', this.index)
        } else {
          this.$toast({ type: 'error', message: message })
        }
      }).catch(_ => {
        this.$toast({ type: 'error', message: '回复失败' })
      }).finally(_ => {
        this.submitDisabled = false
      })
    }
  }
}
</script>

<style scoped>
.reply-input-wrapper {
  border: 1px solid #90939950;
  border-radius: 4px;
  padding: 10px;
  margin: 0 0 10px;
}
</style>
