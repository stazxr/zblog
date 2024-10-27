<template>
  <div>
    <!-- 标题 -->
    <div class="comment-title">
      <svg class="iconfont_svg" aria-hidden="true" style="font-size: 25px;margin-right: 5px;">
        <use xlink:href="#icon-pinglun1" />
      </svg> 评论
    </div>

    <!-- 评论框 -->
    <div class="comment-input-wrapper">
      <div style="display:flex">
        <v-avatar size="40">
          <img v-if="$store.state.user.avatar !== ''" :src="this.$store.state.user.avatar" alt="">
          <img v-else :src="$store.state.otherConfig['touristAvatar']" alt="">
        </v-avatar>
        <div style="width:100%" class="ml-3">
          <div class="comment-input">
            <textarea v-model="commentContent" class="comment-textarea" placeholder="留下点什么吧..." auto-grow dense />
          </div>
          <div class="emoji-container">
            <span :class="chooseEmoji ? 'emoji-btn-active' : 'emoji-btn'" @click="chooseEmoji = !chooseEmoji">
              <i class="iconfont icon-xiaolian" style="font-size: 25px;" />
            </span>
            <button :disabled="submitDisabled" class="upload-btn v-comment-btn" style="margin-left:auto" @click="insertComment">
              提交
            </button>
          </div>
          <!-- 表情框 -->
          <emoji :choose-emoji="chooseEmoji" @addEmoji="addEmoji" />
        </div>
      </div>
    </div>

    <!-- 评论列表 -->
    <div v-if="count > 0 && reFresh">
      <div class="count">{{ count }} 评论</div>
      <div v-for="(item, index) of commentList" :id="'comment' + item.id" :key="item.id" style="display:flex" class="pt-5">
        <v-avatar size="40" class="comment-avatar">
          <img v-if="item['avatar'] !== ''" :src="item['avatar']" alt="">
          <img v-else :src="$store.state.otherConfig['touristAvatar']" alt="">
        </v-avatar>
        <div class="comment-meta">
          <div class="comment-user">
            <span v-if="item['website'] == null || item['website'] === ''">{{ item['nickname'] }}</span>
            <a v-else :href="item['website']" target="_blank">{{ item['nickname'] }}</a>
            <span v-if="item['userId'] === '1'" class="blogger-tag">站长</span>
            <span style="margin-left:10px;font-size: 0.75rem;color: #b3b3b3;">来自{{ item['ipSource'] }}</span>
          </div>
          <div class="comment-info">
            <span style="margin-right:10px">{{ count - index }}楼</span>
            <!-- <span style="margin-right:10px">{{ item['ipSource'] }}</span> -->
            <span style="margin-right:10px">{{ item['createTime'] }}</span>
            <!-- 点赞 -->
            <span :class="isLike(item.id) + ' iconfont icon-dianzan1'" @click="like(item)" />
            <span v-show="item['likeCount'] > 0"> {{ item['likeCount'] }}</span>
            <!-- 删除 -->
            <span v-if="$store.state.user.id === item.userId" class="delete-btn" @click="deleteComment(index, item)">删除</span>
            <!-- 回复 -->
            <span class="reply-btn" @click="replyComment(index, item)">回复</span>
          </div>
          <p class="comment-content" v-html="item.content" />

          <!-- 回复信息 -->
          <div v-for="reply of item.replyList" :id="'comment' + reply.id" :key="reply.id" style="display:flex">
            <v-avatar size="36" class="comment-avatar">
              <img v-if="reply['avatar'] !== ''" :src="reply['avatar']" alt="">
              <img v-else :src="$store.state.otherConfig['touristAvatar']" alt="">
            </v-avatar>
            <div class="reply-meta">
              <div class="comment-user">
                <span v-if="!reply['website']">{{ reply['nickname'] }}</span>
                <a v-else :href="reply['website']" target="_blank">{{ reply['nickname'] }}</a>
                <span v-if="reply['userId'] === '1'" class="blogger-tag">站长</span>
                <span style="margin-left:10px;font-size: 0.75rem;color: #b3b3b3;">来自{{ item['ipSource'] }}</span>
              </div>
              <div class="comment-info">
                <!-- <span style="margin-right:10px">{{ item['ipSource'] }}</span> -->
                <span style="margin-right: 10px;">{{ reply['createTime'] }}</span>
                <!-- 点赞 -->
                <span :class="isLike(reply.id) + ' iconfont icon-dianzan1'" @click="like(reply)" />
                <span v-show="reply['likeCount'] > 0"> {{ reply['likeCount'] }}</span>
                <!-- 删除 -->
                <span v-if="$store.state.user.id === reply.userId" class="delete-btn" @click="deleteComment(index, reply)">删除</span>
                <!-- 回复 -->
                <span class="reply-btn" @click="replyComment(index, reply)">回复</span>
              </div>
              <!-- 回复内容 -->
              <p class="comment-content">
                <!-- 回复用户名 -->
                <!-- <template v-if="reply['userId'] !== reply['replyUserId'] && reply['replyCommentId'] !== item['id']"> -->
                <template v-if="reply['replyCommentId'] !== item['id']">
                  <span>
                    回复
                  </span>
                  <span v-if="!reply['replyWebsite']" class="ml-1" style="color: #b3b3b3;">
                    {{ reply['replyNickname'] }}
                  </span>
                  <a v-else :href="reply['replyWebsite']" target="_blank" class="comment-nickname ml-1">
                    {{ reply['replyNickname'] }}
                  </a>
                  ：
                </template>
                <span v-html="reply['content']" />
              </p>
            </div>
          </div>

          <!-- 回复数量 -->
          <div v-show="item['replyCount'] > 3" ref="check" class="mb-3" style="font-size:0.75rem;color:#6d757a">
            共
            <b>{{ item['replyCount'] }}</b>
            条回复，
            <span style="color:#00a1d6;cursor:pointer" @click="checkReplies(index, item)">
              点击查看
            </span>
          </div>

          <!-- 回复分页 -->
          <div ref="paging" class="mb-3" style="font-size:0.75rem;color:#222;display:none">
            <span style="padding-right:10px">
              共{{ Math.ceil(item['replyCount'] / 5) }}页
            </span>
            <paging ref="page" :total-page="Math.ceil(item['replyCount'] / 5)" :index="index" :comment-id="item.id" @changeReplyCurrent="changeReplyCurrent" />
          </div>

          <!-- 回复框 -->
          <Reply ref="reply" :type="type" @reloadReply="reloadReply" />
        </div>
      </div>

      <!-- 加载更多 -->
      <div class="load-wrapper">
        <v-btn v-if="count > commentList.length" outlined @click="listComments">
          加载更多...
        </v-btn>
      </div>
    </div>

    <!-- 没有评论提示 -->
    <div v-else style="padding:1.25rem;text-align:center">
      来发评论吧~
    </div>
  </div>
</template>

<script>
import Reply from './Reply'
import Paging from './Paging'
import Emoji from './Emoji'
import EmojiList from '../assets/emoji/emoji'
export default {
  components: {
    Emoji,
    Reply,
    Paging
  },
  props: {
    type: {
      type: Number,
      default: null
    }
  },
  data: function() {
    return {
      // 是否禁用评论点赞按钮
      disabledLikeBtn: false,
      submitDisabled: false,
      commentContent: '',
      chooseEmoji: false,
      commentList: [],
      reFresh: true,
      current: 1,
      count: 0
    }
  },
  computed: {
    isLike() {
      return function(commentId) {
        const commentLikeSet = this.$store.state.commentLikeSet
        return commentLikeSet.indexOf(commentId) !== -1 ? 'like-active' : 'like'
      }
    }
  },
  watch: {
    commentList() {
      this.reFresh = false
      this.$nextTick(() => {
        this.reFresh = true
      })
    }
  },
  created() {
    this.listCommentsFirst()
  },
  methods: {
    addEmoji(key) {
      this.commentContent += key
    },
    listCommentsFirst() {
      this.listComments(true)
    },
    listComments(firstLoad = false) {
      // 查看评论
      const param = {
        current: this.current,
        type: this.type
      }

      // 设置评论对象
      const path = this.$route.path
      const arr = path.split('/')
      switch (this.type) {
        case 1:
        case 3:
          param.objectId = arr[2]
          break
        default:
          break
      }

      this.$mapi.portal.queryCommentList(param).then(({ data }) => {
        if (this.current === 1) {
          this.commentList = data.list
        } else {
          this.commentList.push(...data.list)
        }

        this.current++
        this.count = data.total
        this.$nextTick(() => {
          this.$emit('getCommentCount', this.count, firstLoad)
        })
      })
    },
    insertComment() {
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
      const content = this.commentContent.replace(/\[.+?]/g, function(str) {
        return (
          "<img src= '" + EmojiList[str] + "' alt='' width='24' height='24' style='margin: 0 1px;vertical-align: text-bottom' />"
        )
      })

      // 封装参数
      const comment = {
        userId: this.$store.state.user.id,
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
      this.$mapi.portal.saveComment(comment).then(({ code, message }) => {
        if (code === 200) {
          this.current = 1
          this.commentContent = ''
          this.chooseEmoji = false
          this.listComments()
          const isReview = this.$store.state.otherConfig['isCommentReview']
          if (isReview) {
            this.$toast({ type: 'warning', message: '评论成功，正在审核中' })
          } else {
            this.$toast({ type: 'success', message: '评论成功' })
          }
        } else {
          this.$toast({ type: 'error', message: message })
        }
      }).catch(_ => {
        this.$toast({ type: 'error', message: '评论失败' })
      }).finally(_ => {
        this.submitDisabled = false
      })
    },
    like(comment) {
      if (this.disabledLikeBtn) {
        return false
      }

      // 判断登录
      if (!this.$store.state.user.id) {
        this.$store.state.loginFlag = true
        return false
      }

      // 发送请求
      const param = {
        userId: this.$store.state.user.id,
        commentId: comment.id
      }
      this.disabledLikeBtn = true
      this.$mapi.portal.likeComment(param).then(({ code, message }) => {
        if (code === 200) {
          // 判断是否点赞
          if (this.$store.state.commentLikeSet.indexOf(comment.id) !== -1) {
            this.$set(comment, 'likeCount', comment.likeCount - 1)
            this.$toast({ type: 'success', message: '取消成功' })
          } else {
            this.$set(comment, 'likeCount', comment.likeCount + 1)
            this.$toast({ type: 'success', message: '点赞成功' })
          }

          this.$store.commit('commentLike', comment.id)
        } else {
          this.$toast({ type: 'error', message: message })
        }
      }).catch(_ => {
        this.$toast({ type: 'error', message: '点赞失败' })
      }).finally(_ => {
        this.disabledLikeBtn = false
      })
    },
    deleteComment(index, item) {
      this.$confirm({ message: '确定删除吗？' }).then(confirm => {
        if (confirm) {
          const param = {
            userId: this.$store.state.user.id,
            commentId: item.id
          }

          this.$mapi.portal.deleteComment(param).then(({ code, message }) => {
            if (code === 200) {
              this.$toast({ type: 'success', message: '删除成功' })
              this.reloadReplyBeDelete(index)
            } else {
              this.$toast({ type: 'error', message: message })
            }
          }).catch(_ => {
            this.$toast({ type: 'error', message: '删除失败' })
          })
        }
      })
    },
    replyComment(index, item) {
      this.$refs.reply.forEach(item => {
        item.$el.style.display = 'none'
      })

      // 传值给回复框
      this.$refs.reply[index].commentContent = ''
      this.$refs.reply[index].nickname = item.nickname
      this.$refs.reply[index].replyUserId = item.userId
      this.$refs.reply[index].replyCommentId = item.id
      this.$refs.reply[index].parentId = this.commentList[index].id
      this.$refs.reply[index].chooseEmoji = false
      this.$refs.reply[index].index = index
      this.$refs.reply[index].$el.style.display = 'block'
    },
    reloadReply(index) {
      const param = {
        parentId: this.commentList[index].id,
        page: this.$refs.page[index].current,
        pageSize: 5
      }
      this.$mapi.portal.queryCommentReplyList(param).then(({ code, data }) => {
        if (code === 200) {
          this.commentList[index]['replyCount']++
          // 回复大于5条展示分页
          if (this.commentList[index]['replyCount'] > 5) {
            this.$refs.paging[index].style.display = 'flex'
          }
          this.$refs.check[index].style.display = 'none'
          this.$refs.reply[index].$el.style.display = 'none'
          this.commentList[index].replyList = data.list
        }
      })
    },
    reloadReplyBeDelete(index) {
      const param = {
        parentId: this.commentList[index].id,
        page: this.$refs.page[index].current,
        pageSize: 5
      }
      this.$mapi.portal.queryCommentReplyList(param).then(({ code, data }) => {
        if (code === 200) {
          this.commentList[index]['replyCount']--
          // 回复大于5条展示分页
          if (this.commentList[index]['replyCount'] > 5) {
            this.$refs.paging[index].style.display = 'flex'
          }
          this.$refs.check[index].style.display = 'none'
          this.$refs.reply[index].$el.style.display = 'none'
          this.commentList[index].replyList = data.list
        }
      })
    },
    checkReplies(index, item) {
      const param = {
        parentId: item.id,
        page: 1,
        pageSize: 5
      }

      this.$mapi.portal.queryCommentReplyList(param).then(({ data }) => {
        this.$refs.check[index].style.display = 'none'
        item.replyList = data.list

        // 超过1页才显示分页
        if (Math.ceil(item['replyCount'] / 5) > 1) {
          this.$refs.paging[index].style.display = 'flex'
        }
      })
    },
    changeReplyCurrent(current, index, commentId) {
      const param = {
        parentId: commentId,
        page: current,
        pageSize: 5
      }

      this.$mapi.portal.queryCommentReplyList(param).then(({ data }) => {
        this.commentList[index].replyList = data.list
      })
    }
  }
}
</script>

<style scoped>
.blogger-tag {
  background: #ffa51e;
  font-size: 12px;
  display: inline-block;
  border-radius: 2px;
  color: #fff;
  padding: 0 5px;
  margin-left: 6px;
}
.comment-title {
  display: flex;
  align-items: center;
  font-size: 1.25rem;
  font-weight: bold;
  line-height: 40px;
  margin-bottom: 10px;
}
.comment-title i {
  font-size: 1.5rem;
  margin-right: 5px;
}
.comment-input-wrapper {
  border: 1px solid #90939950;
  border-radius: 4px;
  padding: 10px;
  margin: 0 0 10px;
}
.count {
  padding: 5px;
  line-height: 1.75;
  font-size: 1.25rem;
  font-weight: bold;
}
.comment-meta {
  margin-left: 0.8rem;
  width: 100%;
  border-bottom: 1px dashed #f5f5f5;
}
.reply-meta {
  margin-left: 0.8rem;
  width: 100%;
}
.comment-user {
  font-size: 14px;
  line-height: 1.75;
}
.comment-user a {
  color: #1abc9c !important;
  font-weight: 500;
  transition: 0.3s all;
}
.comment-nickname {
  text-decoration: none;
  color: #1abc9c !important;
  font-weight: 500;
}
.comment-info {
  line-height: 1.75;
  font-size: 0.75rem;
  color: #b3b3b3;
}
.reply-btn {
  cursor: pointer;
  float: right;
  color: dodgerblue;
}
.delete-btn {
  cursor: pointer;
  float: right;
  color: #ef2f11;
  margin-left: 10px;
}
.comment-content {
  font-size: 0.875rem;
  line-height: 1.75;
  padding-top: 0.625rem;
  white-space: pre-line;
  word-wrap: break-word;
  word-break: break-all;
}
.comment-avatar {
  transition: all 0.5s;
}
.comment-avatar:hover {
  transform: rotate(360deg);
}
.like {
  cursor: pointer;
  font-size: 0.875rem;
}
.like:hover {
  color: #eb5055;
}
.like-active {
  cursor: pointer;
  font-size: 0.875rem;
  color: #eb5055;
}
.load-wrapper {
  margin-top: 50px;
  display: flex;
  justify-content: center;
  align-items: center;
}
.load-wrapper button {
  background-color: #49b1f5;
  color: #fff;
}
</style>
