<template>
  <div>
    <!-- banner -->
    <div class="banner" :style="cover">
      <h1 class="banner-title">说说</h1>
    </div>
    <!-- 说说内容 -->
    <v-card v-show="talkInfo.id != null && talkInfo.id !== ''" class="blog-container">
      <div class="talk-wrapper">
        <!-- 用户信息 -->
        <div class="user-info-wrapper">
          <v-avatar size="36" class="user-avatar">
            <img v-if="talkInfo['userAvatar'] !== ''" :src="talkInfo['userAvatar']" alt="">
            <img v-else :src="$store.state.otherConfig['touristAvatar']" alt="">
          </v-avatar>
          <div class="user-detail-wrapper">
            <div class="user-nickname">
              {{ talkInfo['userNickname'] }}
              <span v-if="talkInfo['userId'] === '1'" class="blogger-tag">站长</span>
            </div>
            <!-- 发表时间 -->
            <div class="time">{{ talkInfo['createTime'] }}</div>
            <!-- 说说信息 -->
            <div class="talk-content" v-html="talkInfo['content']" />
            <!-- 图片列表 -->
            <v-row v-if="talkInfo['imagesList']" class="talk-images">
              <v-col v-for="(img, index) of talkInfo['imagesList']" :key="index" :md="4" :cols="6">
                <v-img class="images-items" :src="img" aspect-ratio="1" max-height="200" @click.prevent="previewImg(talkInfo['imagesList'], img)" />
              </v-col>
            </v-row>
            <!-- 说说操作 -->
            <div class="talk-operation">
              <div class="talk-operation-item">
                <v-icon size="16" :color="isLike(talkInfo.id)" class="like-btn" @click.prevent="like(talkInfo)">
                  iconfont icon-dianzan1
                </v-icon>
                <div class="operation-count">
                  {{ talkInfo['likeCount'] == null ? 0 : talkInfo['likeCount'] }}
                </div>
              </div>
              <div class="talk-operation-item">
                <v-icon size="16" color="#999">iconfont icon-comment-v2-full</v-icon>
                <div class="operation-count">
                  {{ talkInfo['commentCount'] == null ? 0 : talkInfo['commentCount'] }}
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>

      <!-- 评论 -->
      <Comment id="comment" :type="commentType" @getCommentCount="getCommentCount" />
    </v-card>
  </div>
</template>

<script>
import Comment from '../../components/TalkComment'
export default {
  components: {
    Comment
  },
  data: function() {
    return {
      commentType: 3,
      talkInfo: {
        id: '',
        userId: '',
        userAvatar: '',
        userNickname: '',
        createTime: '',
        content: '',
        imagesList: [],
        likeCount: 0,
        commentCount: 0
      },
      previewList: [],
      commentLoadFinish: false
    }
  },
  computed: {
    cover() {
      let cover = ''
      this.$store.state.pageList.forEach(item => {
        if (item['pageLabel'] === 'talkDetail') {
          cover = item['pageCover']
        }
      })
      return 'background: url(' + cover + ') center center / cover no-repeat'
    },
    isLike() {
      return function(talkId) {
        const talkLikeSet = this.$store.state.talkLikeSet
        return talkLikeSet.indexOf(talkId) !== -1 ? '#eb5055' : '#999'
      }
    }
  },
  created() {
    this.getTalkById()
  },
  methods: {
    getTalkById() {
      if (this.$route.params.talkId && this.$route.params.talkId !== '') {
        this.$loading.show()
        this.$mapi.portal.queryTalkById({ talkId: this.$route.params.talkId }).then(({ code, message, data }) => {
          if (code === 200) {
            Object.keys(this.talkInfo).forEach(key => {
              this.talkInfo[key] = data[key]
            })
          } else {
            this.$toast({ type: 'error', message: message })
            this.clearTalkInfo()
          }
        }).catch(_ => {
          this.clearTalkInfo()
          this.$router.push('/404')
        }).finally(_ => {
          this.$loading.hide()
        })
      } else {
        this.clearTalkInfo()
      }
    },
    clearTalkInfo() {
      this.talkInfo = {
        id: '',
        userAvatar: '',
        userNickname: '',
        createTime: '',
        content: '',
        imagesList: [],
        likeCount: 0
      }
    },
    getCommentCount(_, firstLoad) {
      if (firstLoad) {
        // 评论首次加载结束
        this.scrollToHash()
      }
    },
    scrollToHash() {
      const hash = location.hash
      if (hash && hash !== '') {
        this.$nextTick(() => {
          setTimeout(() => {
            let targetBox = document.getElementById('comment' + hash.replace('#', ''))
            if (targetBox == null) {
              targetBox = document.getElementById('comment')
            }

            // 跳转
            if (targetBox != null) {
              // 非平滑滚顶
              // targetBox.scrollIntoView()
              // 平滑滚动
              window.scrollTo({
                top: targetBox.getBoundingClientRect().top + window.scrollY - 70,
                behavior: 'smooth'
              })
            }
          }, 500)
        })
      }
    },
    previewImg(imagesList, img) {
      if (imagesList && imagesList.length > 0) {
        this.previewList = imagesList
        this.$imagePreview({
          images: this.previewList,
          index: this.previewList.indexOf(img)
        })
      } else {
        this.previewList = []
      }
    },
    like(talk) {
      // 判断登录
      if (!this.$store.state.user.id) {
        this.$store.state.loginFlag = true
        return false
      }

      // 发送请求
      const param = {
        userId: this.$store.state.user.id,
        talkId: talk.id
      }
      this.$mapi.portal.likeTalk(param).then(({ code, message }) => {
        if (code === 200) {
          // 判断是否点赞
          if (this.$store.state.talkLikeSet.indexOf(talk.id) !== -1) {
            this.$set(talk, 'likeCount', talk.likeCount - 1)
          } else {
            this.$set(talk, 'likeCount', talk.likeCount + 1)
          }

          this.$store.commit('talkLike', talk.id)
        } else {
          this.$toast({ type: 'error', message: message })
        }
      }).catch(_ => {
        this.$toast({ type: 'error', message: '点赞失败' })
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

.col-xl,
.col-xl-auto,
.col-xl-12,
.col-xl-11,
.col-xl-10,
.col-xl-9,
.col-xl-8,
.col-xl-7,
.col-xl-6,
.col-xl-5,
.col-xl-4,
.col-xl-3,
.col-xl-2,
.col-xl-1,
.col-lg,
.col-lg-auto,
.col-lg-12,
.col-lg-11,
.col-lg-10,
.col-lg-9,
.col-lg-8,
.col-lg-7,
.col-lg-6,
.col-lg-5,
.col-lg-4,
.col-lg-3,
.col-lg-2,
.col-lg-1,
.col-md,
.col-md-auto,
.col-md-12,
.col-md-11,
.col-md-10,
.col-md-9,
.col-md-8,
.col-md-7,
.col-md-6,
.col-md-5,
.col-md-4,
.col-md-3,
.col-md-2,
.col-md-1,
.col-sm,
.col-sm-auto,
.col-sm-12,
.col-sm-11,
.col-sm-10,
.col-sm-9,
.col-sm-8,
.col-sm-7,
.col-sm-6,
.col-sm-5,
.col-sm-4,
.col-sm-3,
.col-sm-2,
.col-sm-1,
.col,
.col-auto,
.col-12,
.col-11,
.col-10,
.col-9,
.col-8,
.col-7,
.col-6,
.col-5,
.col-4,
.col-3,
.col-2,
.col-1 {
  width: 100%;
  padding: 2px !important;
}
.talk-wrapper {
  padding: 16px 20px;
  border-radius: 10px;
  background: rgba(255, 255, 255, 0.1);
  box-shadow: 0 3px 8px 6px rgb(7 17 27 / 6%);
  transition: all 0.3s ease 0s;
}
.talk-wrapper:hover {
  box-shadow: 0 5px 10px 8px rgb(7 17 27 / 16%);
  transform: translateY(-3px);
}
.user-info-wrapper {
  width: 100%;
  display: flex;
}
.user-avatar {
  border-radius: 50%;
}
.user-avatar {
  transition: all 0.5s;
}
.user-avatar:hover {
  transform: rotate(360deg);
}
.user-detail-wrapper {
  margin-left: 10px;
  flex: 1;
  width: 0;
}
.user-nickname {
  font-size: 15px;
  font-weight: bold;
  vertical-align: middle;
}
.user-sign {
  margin-left: 4px;
}
.time {
  color: #999;
  margin-top: 2px;
  font-size: 12px;
}
.talk-content {
  margin-top: 8px;
  font-size: 14px;
  white-space: pre-line;
  word-wrap: break-word;
  word-break: break-all;
}
.talk-images {
  padding: 0 10px;
  margin-top: 8px;
}
.images-items {
  cursor: pointer;
  border-radius: 4px;
}
.talk-operation {
  display: flex;
  align-items: center;
  margin-top: 20px;
}
.talk-operation-item {
  display: flex;
  align-items: center;
  margin-right: 40px;
  font-size: 12px;
}
.operation-count {
  margin-left: 4px;
}
.load-wrapper {
  margin-top: 20px;
  display: flex;
  justify-content: center;
  align-items: center;
}
.load-wrapper button {
  background-color: #49b1f5;
  color: #fff;
}
.comment-wrapper {
  margin-top: 20px;
}
.like-btn:hover {
  color: #eb5055 !important;
}
</style>
