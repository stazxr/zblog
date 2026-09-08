<template>
  <section class="comment-wrapper">
    <!-- 评论标题 -->
    <div class="comment-header">
      <div class="comment-title">
        <icon name="liuyan-" custom-class="comment-title-icon" />
        <span>{{ title }}</span>
        <span v-if="realTotal > 0" class="comment-total">
          {{ realTotal }}
        </span>
      </div>
    </div>

    <!-- 评论输入框 -->
    <div class="comment-editor">
      <v-avatar size="42" class="comment-editor-avatar">
        <img :src="$getAvatar($store.state.user.avatar)" alt="">
      </v-avatar>
      <div class="comment-editor-main">
        <div class="comment-textarea-wrapper">
          <textarea
            ref="commentContentRef"
            v-model="commentContent"
            class="comment-textarea"
            :placeholder="editorPlaceholder"
            :maxlength="maxLength"
          />
        </div>
        <div class="comment-toolbar-wrapper">
          <div class="comment-editor-toolbar">
            <!-- 表情 -->
            <button type="button" class="toolbar-btn" :class="{ active: showEmojiPicker }" @click="toggleEmojiPicker">
              <icon name="biaoqing" />
            </button>

            <!-- 照片 -->
            <button type="button" class="toolbar-btn" :class="{ active: showImageUpload }" @click="toggleImageUpload">
              <icon name="tianjiatupian_huaban" />
            </button>

            <!-- 评论字数 -->
            <span class="editor-count">
              {{ commentContent.length }}/{{ maxLength }}
            </span>

            <!-- 提交 -->
            <button type="button" class="submit-btn" :disabled="submitDisabled" @click="submitComment">
              <v-progress-circular v-if="submitDisabled" indeterminate size="14" width="2" class="mr-1" />
              {{ submitDisabled ? '提交中' : '发表评论' }}
            </button>
          </div>

          <!-- 表情 -->
          <div v-show="showEmojiPicker" class="emoji-wrapper">
            <Emoji :show-emoji-picker="showEmojiPicker" :emoji-list="emojiList" @selectEmoji="insertEmoji" />
          </div>

          <!-- 图片上传面板：相对于工具栏定位 -->
          <div v-show="showImageUpload" class="image-upload-wrapper">
            <image-upload-panel @success="handleImageUploadSuccess" @close="showImageUpload = false" />
          </div>
        </div>
      </div>
    </div>

    <!-- 回复状态 -->
    <div v-if="replyTarget" class="reply-target">
      <span>
        回复 <strong>{{ replyTarget.nickname }}</strong>
      </span>
      <button type="button" class="cancel-reply-btn" @click="cancelReply">
        取消回复
      </button>
    </div>

    <!-- 评论列表 -->
    <div v-if="commentList.length" class="comment-list">
      <article v-for="(comment) in commentList" :id="'comment-' + comment.id" :key="comment.id" class="comment-item">
        <!-- 一级评论 -->
        <div class="comment-main">
          <v-avatar size="42" class="comment-avatar">
            <img :src="$getAvatar(comment.user.avatar)" alt="">
          </v-avatar>
          <div class="comment-body">
            <div class="comment-user-row">
              <div class="comment-user-info">
                <span class="comment-user-name">
                  {{ comment.user.nickname }}
                </span>
                <span class="blogger-tag">
                  站长
                </span>
                <span class="blogger-tag">
                  LV1
                </span>
                <span class="blogger-tag">
                  筑基
                </span>
              </div>
              <relative-time class="comment-time" :time="comment.createTime" />
            </div>

            <!-- 评论内容 -->
            <div class="comment-content" v-html="comment.content" />

            <!-- 操作 -->
            <div class="comment-actions">
              <button type="button" class="action-btn" :class="{ liked: comment.liked }" @click="toggleLike(comment)">
                <icon name="icon" :active="comment.liked" size="14" />
                <span v-if="comment.likeCount > 0">
                  {{ comment.likeCount }}
                </span>
              </button>
              <button type="button" class="action-btn" @click="startReply(comment)">
                <icon name="huifu" size="14" />
              </button>
              <button v-if="isCommentOwner(comment)" type="button" class="action-btn delete-action" @click="deleteComment(comment)">
                <icon name="xingzhuangjiehe1" size="14" />
              </button>
              <span class="comment-location">
                {{ comment.ipSource || '未知' }}
              </span>
            </div>

            <!-- 回复 -->
            <div v-if="comment.replyList && comment.replyList.length" class="reply-list">
              <div v-for="reply in comment.replyList" :id="'comment-' + reply.id" :key="reply.id" class="reply-item">
                <v-avatar size="34" class="reply-avatar">
                  <img :src="$getAvatar(reply.user.avatar)" alt="">
                </v-avatar>

                <div class="reply-body">
                  <div class="reply-user-row">
                    <div>
                      <span class="reply-user-name">
                        {{ reply.user.nickname }}
                      </span>
                      <span v-if="String(reply.userId) === '1'" class="blogger-tag">
                        站长
                      </span>
                    </div>
                    <relative-time class="reply-time" :time="reply.createTime" />
                  </div>

                  <div class="reply-content">
                    <template v-if="reply.replyUser.id && String(reply.replyUser.id) !== String(reply.user.id)">
                      <span class="reply-label">
                        回复
                      </span>
                      <span class="reply-target-name">
                        {{ reply.replyUser.nickname }}
                      </span>
                      <span class="reply-colon">: </span>
                    </template>
                    <span v-html="reply.content" />
                  </div>

                  <div class="reply-actions">
                    <button type="button" class="action-btn" :class="{ liked: reply.liked }" @click="toggleLike(reply)">
                      <i class="iconfont icon-dianzan1" />
                      <span v-if="reply.likeCount > 0">
                        {{ reply.likeCount }}
                      </span>
                    </button>
                    <button type="button" class="action-btn" @click="startReply(reply, comment)">
                      回复
                    </button>
                    <button v-if="isCommentOwner(reply)" type="button" class="action-btn delete-action" @click="deleteReply(comment, reply)">
                      删除
                    </button>
                  </div>
                </div>
              </div>
            </div>

            <!-- 回复数量 -->
            <button v-if="comment.replyCount > loadedReplyCount(comment)" type="button" class="more-reply-btn" @click="loadReplies(comment)">
              查看全部 {{ comment.replyCount }} 条回复
              <i class="iconfont icon-xiangxia" />
            </button>

            <!-- 回复分页 -->
            <div v-if="comment.replyPage && comment.replyPage.total > 1" class="reply-pagination">
              <button type="button" :disabled="comment.replyPage.current <= 1" @click="changeReplyPage(comment, comment.replyPage.current - 1)">
                上一页
              </button>
              <span>
                {{ comment.replyPage.current }} / {{ comment.replyPage.pages }}
              </span>
              <button type="button" :disabled="comment.replyPage.current >= comment.replyPage.pages" @click="changeReplyPage(comment, comment.replyPage.current + 1)">
                下一页
              </button>
            </div>
          </div>
        </div>
      </article>
    </div>

    <!-- 加载更多 -->
    <div v-if="total > commentList.length" class="comment-load-more">
      <button type="button" :disabled="commentLoading" @click="loadComments">
        <v-progress-circular v-if="commentLoading" indeterminate size="16" width="2" class="mr-2" />
        {{ commentLoading ? '加载中...' : '加载更多评论' }}
      </button>
    </div>

    <!-- 空状态 -->
    <div v-else-if="!commentLoading && !commentList.length" class="comment-empty">
      <div class="empty-icon">
        <i class="iconfont icon-pinglun1" />
      </div>
      <div class="empty-title">
        还没有评论
      </div>
      <div class="empty-text">
        留下你的第一条评论吧~
      </div>
    </div>
  </section>
</template>

<script>
import Emoji from './Emoji'
import ImageUploadPanel from './ImageUploadPanel'
import RelativeTime from './RelativeTime'
export default {
  name: 'Comment',
  components: {
    Emoji,
    ImageUploadPanel,
    RelativeTime
  },
  props: {
    /**
     * 标题
     */
    title: {
      type: String,
      default: '评论'
    },
    /**
     * 评论最大长度
     */
    maxLength: {
      type: Number,
      default: 2000
    },
    /**
     * 评论对象类型（以后台为准）
     */
    type: {
      type: Number,
      required: true
    },
    /**
     * 评论对象ID
     */
    objectId: {
      type: [Number, String],
      required: true
    },
    /**
     * 每页一级评论数量
     */
    pageSize: {
      type: Number,
      default: 10
    },
    /**
     * 每页回复数量
     */
    replyPageSize: {
      type: Number,
      default: 5
    }
  },
  data() {
    return {
      current: 1, // 当前页
      total: 0, // 评论总数
      realTotal: 0, // 评论总数（包含一级和二级评论）
      commentList: [], // 评论列表
      commentLoading: false, // 是否正在加载评论
      commentContent: '', // 评论内容
      showEmojiPicker: false, // 是否显示表情选择框
      showImageUpload: false, // 是否显示图片上传面板
      submitDisabled: false, // 是否正在提交评论
      likeLoading: false, // 是否正在点赞评论
      replyTarget: null, // 当前回复对象
      replyParentId: null // 当前回复所属一级评论
    }
  },
  computed: {
    /* 表情包 */
    emojiList() {
      return this.$store.state.emojiList || []
    },
    // 输入框提示
    editorPlaceholder() {
      if (this.replyTarget) {
        return `回复 ${this.replyTarget.nickname}...`
      } else {
        return '留下点什么吧...'
      }
    }
  },
  created() {
    this.loadComments(true)
  },
  methods: {
    // 加载评论总数
    loadRealCommentCount() {
      const param = {
        objectId: this.objectId,
        type: this.type
      }
      this.$mapi.portal.queryCommentTotal(param).then(res => {
        this.realTotal = res.data
      }).catch(() => {
        this.realTotal = 0
      }).finally(() => {
        this.$emit('getCommentCount', this.realTotal)
      })
    },
    // 加载一级评论列表
    loadComments(reload = false) {
      if (this.commentLoading) {
        return
      }

      if (reload) {
        // 重新加载评论列表
        this.total = 0
        this.realTotal = 0
        this.current = 1
        this.commentList = []
        this.loadRealCommentCount()
      }

      const param = {
        page: this.current,
        pageSize: this.pageSize,
        objectId: this.objectId,
        type: this.type
      }

      this.commentLoading = true
      this.$mapi.portal.queryCommentList(param).then(({ data }) => {
        const list = data.records || []
        if (reload) {
          // 首次加载或重新加载
          this.commentList = list
        } else {
          this.commentList.push(...list)
        }

        this.total = data.total || 0
        this.current++
      }).catch(() => {
        this.$toast({ type: 'error', message: '评论加载失败' })
      }).finally(() => {
        this.commentLoading = false
      })
    },

    // 提交评论
    submitComment() {
      // if (!this.$store.state.websiteConfig.xx) {
      //   this.showImageUpload = false
      //   this.$store.state.loginFlag = true
      //   this.$toast({ type: 'warning', message: `上传照片需要先登录，请登录后操作` })
      //   return
      // }

      // 评论校验
      const content = this.commentContent.trim()
      if (!content) {
        this.$toast({ type: 'error', message: '评论不能为空' })
        return
      }
      if (content.length > this.maxLength) {
        this.$toast({ type: 'error', message: `评论不能超过${this.maxLength}字` })
        return
      }

      // 父评论id
      const parentId = this.replyTarget ? this.replyParentId : 0

      // 评论对象
      const comment = {
        objectId: this.objectId,
        type: this.type,
        content: content,
        parentId: parentId
      }

      console.log('content', content)

      // 提交
      this.submitDisabled = true
      this.$mapi.portal.saveComment(comment).then(({ code, message }) => {
        console.log('code', code)
        console.log('message', message)

        // if (code !== 200) {
        //   this.$toast({ type: 'error', message: message || '评论失败' })
        //   return
        // }
        //
        // this.commentContent = ''
        // this.showEmojiPicker = false
        // this.cancelReply()
        //
        // /**
        //  * 重新加载
        //  */
        // this.current = 1
        // this.commentList = []
        // this.loadComments()
        // const isReview =
        //   this.$store.state.otherConfig &&
        //   this.$store.state.otherConfig.isCommentReview
        // this.$toast({
        //   type: isReview ? 'warning' : 'success',
        //   message: isReview
        //     ? '评论成功，正在审核中'
        //     : '评论成功'
        // })
      }).catch(e => {
        console.log('评论失败', e)
        this.$toast({ type: 'error', message: '评论失败' })
      }).finally(() => {
        this.submitDisabled = false
      })
    },

    // 表情选择
    toggleEmojiPicker() {
      this.showEmojiPicker = !this.showEmojiPicker
      if (this.showEmojiPicker) {
        this.showImageUpload = false
      }
    },
    insertEmoji(name) {
      if (!name) {
        return
      }

      const emojiTag = `[emoji:${name}]`
      this.insertTag(emojiTag)
    },
    // 照片选择
    toggleImageUpload() {
      this.showImageUpload = !this.showImageUpload
      if (this.showImageUpload) {
        this.showEmojiPicker = false

        // 上传照片需要登录
        if (!this.$store.state.authenticated) {
          this.showImageUpload = false
          this.$store.state.loginFlag = true
          this.$toast({ type: 'warning', message: `上传照片需要先登录，请登录后操作` })
        }
      }
    },
    handleImageUploadSuccess(imageId) {
      this.insertImage(imageId)
      this.showImageUpload = false
    },
    insertImage(imageId) {
      if (!imageId) {
        return
      }

      const imageTag = `[image:${imageId}]`
      this.insertTag(imageTag)
    },
    // 新增标签
    insertTag(tag) {
      // 已达到最大长度
      const currentLength = this.commentContent.length
      if (currentLength >= this.maxLength || currentLength + tag.length > this.maxLength) {
        this.$toast({ type: 'warning', message: `评论内容不能超过${this.maxLength}字` })
        return
      }

      // 评论输入框
      const textarea = this.$refs.commentContentRef

      // 没有输入框引用，直接追加
      if (!textarea) {
        this.commentContent += tag
        return
      }

      // 计算表情插入位置
      const start = textarea.selectionStart || 0
      const end = textarea.selectionEnd || 0
      const before = this.commentContent.substring(0, start)
      const after = this.commentContent.substring(end)
      this.commentContent = before + tag + after

      // Vue 更新 DOM 后恢复光标位置
      this.$nextTick(() => {
        const cursorPosition = start + tag.length
        textarea.focus()
        textarea.setSelectionRange(cursorPosition, cursorPosition)
      })
    },

    /**
     * 点赞
     */
    toggleLike(comment) {
      if (this.likeLoading) {
        return
      }

      this.likeLoading = true
      this.$mapi.portal.likeComment({ commentId: comment.id }).then(_ => {
        const liked = comment.liked // TODO
        const likeCount = Number(comment.likeCount || 0)
        this.$set(comment, 'likeCount', liked ? Math.max(likeCount - 1, 0) : likeCount + 1)
        this.$store.commit('commentLike', comment.id)
      }).catch(error => {
        this.$toast({ type: 'error', message: error })
      }).finally(() => {
        this.likeLoading = false
      })
    },

    /**
     * 是否评论本人
     */
    isCommentOwner(comment) {
      if (!this.$store.state.user.id) {
        return false
      }
      return String(this.$store.state.user.id) === String(comment.user.id)
    },

    /**
     * 开始回复
     *
     * comment     一级评论
     * parent      所属一级评论
     */
    startReply(comment, parent = null) {
      if (!this.$store.state.user.id) {
        this.$store.state.loginFlag = true
        return
      }

      const parentComment = parent || comment
      this.replyTarget = {
        id: comment.id,
        userId: comment.user.id,
        nickname: comment.user.nickname
      }

      this.replyParentId = parentComment.id
      this.$nextTick(() => {
        this.$refs.commentContentRef.focus()
      })
    },
    /**
     * 取消回复
     */
    cancelReply() {
      this.replyTarget = null
      this.replyParentId = null
    },

    /**
     * 删除一级评论
     */
    deleteComment(comment) {
      this.$confirm({
        message: '确定删除这条评论吗？'
      }).then(confirm => {
        if (!confirm) {
          return
        }

        this.doDeleteComment(comment)
      })
    },
    /**
     * 删除回复
     */
    deleteReply(parent, reply) {
      this.$confirm({
        message: '确定删除这条回复吗？'
      }).then(confirm => {
        if (!confirm) {
          return
        }

        this.doDeleteComment(reply, parent)
      })
    },
    /**
     * 执行删除
     */
    doDeleteComment(comment, parent = null) {
      this.$mapi.portal.deleteComment({ commentId: comment.id }).then(res => {
        this.$toast({ type: 'success', message: '删除成功' })
        if (parent) {
          // 删除回复
          try {
            const index = parent.replyList.findIndex(item => item.id === comment.id)
            if (index !== -1) {
              parent.replyList.splice(index, 1)
              parent.replyCount = Math.max(Number(parent.replyCount || 0) - 1, 0)
            }
          } catch (e) {
            // 兜底，如果前段删除后的逻辑失败，则后端重新加载缓存
            console.error('删除评论后更新本地状态失败:', e)
            this.loadReplies(parent)
          }
        } else {
          // 删除一级评论
          try {
            const index = this.commentList.findIndex(item => item.id === comment.id)
            if (index !== -1) {
              this.commentList.splice(index, 1)
              this.total = Math.max(this.total - 1, 0)
            }
            throw new Error('评论不存在')
          } catch (e) {
            // 兜底，如果前段删除后的逻辑失败，则后端重新加载缓存
            console.error('删除评论后更新本地状态失败:', e)
            this.loadComments(false)
          }
        }
      }).catch(error => {
        this.$toast({ type: 'error', message: error })
      })
    },

    /**
     * 当前已经加载的回复数量
     */
    loadedReplyCount(comment) {
      return comment.replyList ? comment.replyList.length : 0
    },

    /**
     * 加载回复
     */
    loadReplies(comment) {
      const current = comment.replyPage && comment.replyPage.current ? comment.replyPage.current : 1
      this.loadReplyPage(comment, current)
    },
    /**
     * 回复分页
     */
    changeReplyPage(comment, current) {
      this.loadReplyPage(comment, current)
    },
    /**
     * 加载回复分页
     */
    loadReplyPage(comment, current) {
      const param = {
        parentId: comment.id,
        page: current,
        pageSize: this.replyPageSize
      }

      this.$mapi.portal.queryCommentReplyList(param).then(({ code, data }) => {
        this.$set(comment, 'replyList', data.records || [])
        const total = data.total || 0
        this.$set(comment, 'replyPage', { current, total })
      }).catch(() => {
        this.$toast({ type: 'error', message: '回复加载失败' })
      })
    }
  }
}
</script>

<style scoped>
.comment-wrapper {
  width: 100%;
  margin-top: 30px;
}

/* 标题 */

.comment-header {
  margin-bottom: 18px;
}

.comment-title {
  display: flex;
  align-items: center;
  font-size: 20px;
  font-weight: 600;
  color: #303133;
}

.comment-title-icon {
  width: 25px;
  height: 25px;
  margin-right: 7px;
}

.comment-total {
  margin-left: 8px;
  padding: 1px 7px;
  border-radius: 10px;
  background: #f5f7fa;
  color: #909399;
  font-size: 12px;
  font-weight: 500;
}

/* 评论输入 */

.comment-editor {
  display: flex;
  padding: 16px;
  border: 1px solid #ebeef5;
  border-radius: 10px;
  background: #fff;
  transition: border-color 0.2s, box-shadow 0.2s;
}

.comment-editor:focus-within {
  border-color: #dcdfe6;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.04);
}

.comment-editor-avatar {
  flex-shrink: 0;
}

.comment-editor-main {
  flex: 1;
  min-width: 0;
  margin-left: 12px;
}

.comment-textarea-wrapper {
  width: 100%;
}

.comment-textarea {
  display: block;
  width: 100%;
  min-height: 100px;
  max-height: 200px;
  padding: 8px 0;
  border: 0;
  outline: none;
  resize: vertical;
  background: transparent;
  color: #303133;
  font-size: 14px;
  line-height: 1.7;
  font-family: inherit;
}

.comment-textarea::placeholder {
  color: #c0c4cc;
}

.comment-toolbar-wrapper {
  position: relative;
  margin-top: 5px;
}

.comment-editor-toolbar {
  display: flex;
  align-items: center;
  min-height: 32px;
}

.toolbar-btn {
  display: inline-flex;
  align-items: center;
  border: 0;
  outline: none;
  padding: 4px 7px;
  border-radius: 5px;
  background: transparent;
  color: #909399;
  cursor: pointer;
  font-size: 12px;
  transition: all 0.2s;
}

.toolbar-btn i {
  margin-right: 4px;
  font-size: 18px;
}

.toolbar-btn:hover,
.toolbar-btn.active {
  color: #409eff;
  background: #f0f7ff;
}

.editor-count {
  margin-left: 8px;
  color: #c0c4cc;
  font-size: 11px;
}

.submit-btn {
  display: inline-flex;
  align-items: center;
  justify-content: center;
  min-width: 92px;
  height: 32px;
  margin-left: auto;
  padding: 0 14px;
  border: 0;
  border-radius: 5px;
  outline: none;
  background: #409eff;
  color: #fff;
  font-size: 13px;
  cursor: pointer;
  transition: all 0.2s;
}

.submit-btn:hover {
  opacity: 0.9;
}

.submit-btn:disabled {
  opacity: 0.6;
  cursor: not-allowed;
}

.emoji-wrapper {
  margin-top: 8px;
  border-top: 1px solid #f5f5f5;
  padding-top: 8px;
}

.image-upload-wrapper {
  position: absolute;
  left: 50%;
  bottom: calc(100% + 8px);
  width: 360px;
  max-width: calc(100vw - 40px);
  z-index: 20;
  transform: translateX(-50%);
}

/* 回复目标 */

.reply-target {
  display: flex;
  align-items: center;
  margin: 12px 0;
  padding: 8px 12px;
  border-radius: 6px;
  background: #f5f7fa;
  color: #909399;
  font-size: 12px;
}

.reply-target strong {
  margin: 0 3px;
  color: #409eff;
}

.cancel-reply-btn {
  margin-left: auto;
  border: 0;
  background: transparent;
  color: #909399;
  cursor: pointer;
  font-size: 12px;
}

.cancel-reply-btn:hover {
  color: #409eff;
}

/* 评论列表 */

.comment-list {
  margin-top: 20px;
}

.comment-item {
  padding: 22px 0;
  border-bottom: 1px solid #f2f3f5;
}

.comment-main {
  display: flex;
}

.comment-avatar {
  flex-shrink: 0;
}

.comment-body {
  flex: 1;
  min-width: 0;
  margin-left: 12px;
}

.comment-user-row {
  display: flex;
  align-items: center;
  justify-content: space-between;
}

.comment-user-info {
  display: flex;
  align-items: center;
  min-width: 0;
}

.comment-user-name {
  overflow: hidden;
  color: #4a4a4a;
  font-size: 14px;
  font-weight: 600;
  text-decoration: none;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.blogger-tag {
  display: inline-flex;
  align-items: center;
  height: 18px;
  margin-left: 6px;
  padding: 0 5px;
  border-radius: 3px;
  background: #409eff;
  color: #fff;
  font-size: 10px;
  line-height: 18px;
}

.comment-time {
  flex-shrink: 0;
  margin-left: 10px;
  color: #c0c4cc;
  font-size: 11px;
}

/* 内容 */

.comment-content {
  margin-top: 8px;
  color: #606266;
  font-size: 14px;
  line-height: 1.8;
  word-break: break-word;
}

/* 操作 */

.comment-actions,
.reply-actions {
  display: flex;
  align-items: center;
  margin-top: 7px;
}

.action-btn {
  display: inline-flex;
  align-items: center;
  margin-right: 18px;
  padding: 0;
  border: 0;
  outline: none;
  background: transparent;
  color: #a8abb2;
  cursor: pointer;
  font-size: 11px;
  transition: color 0.2s;
}

.action-btn i {
  margin-right: 4px;
  font-size: 14px;
}

.action-btn:hover {
  color: #409eff;
}

.action-btn.liked {
  color: #f56c6c;
}

.delete-action:hover {
  color: #f56c6c;
}

.comment-location {
  margin-left: auto;
  color: #c0c4cc;
  font-size: 11px;
}

/* 回复 */

.reply-list {
  margin-top: 14px;
  padding: 12px 14px;
  border-radius: 7px;
  background: #f8f9fa;
}

.reply-item {
  display: flex;
  padding: 11px 0;
}

.reply-item + .reply-item {
  border-top: 1px solid #eee;
}

.reply-avatar {
  flex-shrink: 0;
}

.reply-body {
  flex: 1;
  min-width: 0;
  margin-left: 10px;
}

.reply-user-row {
  display: flex;
  align-items: center;
  justify-content: space-between;
}

.reply-user-name {
  color: #606266;
  font-size: 13px;
  font-weight: 500;
  text-decoration: none;
}

.reply-user-name:hover {
  color: #409eff;
}

.reply-time {
  color: #c0c4cc;
  font-size: 11px;
}

.reply-content {
  margin-top: 5px;
  color: #606266;
  font-size: 13px;
  line-height: 1.7;
  word-break: break-word;
}

.reply-label {
  color: #909399;
}

.reply-target-name {
  margin-left: 4px;
  color: #409eff;
}

.reply-colon {
  margin-right: 3px;
  color: #909399;
}

.reply-actions {
  margin-top: 4px;
}

.more-reply-btn {
  margin-top: 5px;
  padding: 0;
  border: 0;
  outline: none;
  background: transparent;
  color: #409eff;
  cursor: pointer;
  font-size: 12px;
}

.more-reply-btn:hover {
  color: #66b1ff;
}

.more-reply-btn i {
  margin-left: 3px;
  font-size: 11px;
}

/* 回复分页 */

.reply-pagination {
  display: flex;
  align-items: center;
  justify-content: center;
  margin-top: 8px;
  padding-top: 10px;
  border-top: 1px solid #eee;
  color: #909399;
  font-size: 11px;
}

.reply-pagination button {
  border: 0;
  outline: none;
  background: transparent;
  color: #409eff;
  cursor: pointer;
  font-size: 11px;
}

.reply-pagination button:disabled {
  color: #c0c4cc;
  cursor: not-allowed;
}

.reply-pagination span {
  margin: 0 12px;
}

/* 加载更多 */

.comment-load-more {
  display: flex;
  justify-content: center;
  padding: 24px 0;
}

.comment-load-more button {
  display: inline-flex;
  align-items: center;
  border: 1px solid #ebeef5;
  border-radius: 5px;
  padding: 7px 18px;
  outline: none;
  background: #fff;
  color: #909399;
  cursor: pointer;
  font-size: 12px;
  transition: all 0.2s;
}

.comment-load-more button:hover {
  border-color: #409eff;
  color: #409eff;
}

.comment-load-more button:disabled {
  cursor: not-allowed;
}

/* 空状态 */

.comment-empty {
  padding: 45px 20px;
  text-align: center;
}

.empty-icon {
  color: #dcdfe6;
  font-size: 35px;
}

.empty-title {
  margin-top: 10px;
  color: #909399;
  font-size: 14px;
}

.empty-text {
  margin-top: 5px;
  color: #c0c4cc;
  font-size: 12px;
}

/* 移动端 */

@media screen and (max-width: 600px) {

  .comment-wrapper {
    margin-top: 20px;
  }

  .comment-editor {
    padding: 12px;
  }

  .comment-editor-avatar {
    display: none;
  }

  .comment-editor-main {
    margin-left: 0;
  }

  .comment-textarea {
    min-height: 65px;
  }

  .comment-item {
    padding: 18px 0;
  }

  .comment-avatar {
    width: 36px !important;
    height: 36px !important;
  }

  .comment-body {
    margin-left: 9px;
  }

  .comment-user-row {
    align-items: flex-start;
  }

  .comment-time {
    font-size: 10px;
  }

  .comment-location {
    display: none;
  }

  .reply-list {
    margin-top: 10px;
    padding: 8px 10px;
  }

  .reply-item {
    padding: 9px 0;
  }

  .editor-count {
    display: none;
  }

  .comment-toolbar-wrapper {
    width: 100%;
  }

  .image-upload-wrapper {
    width: min(360px, calc(100vw - 32px));
    max-width: none;
  }

  .submit-btn {
    min-width: 80px;
  }
}
</style>
