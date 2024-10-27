<template>
  <div>
    <!-- banner -->
    <div class="banner" :style="cover">
      <h1 class="banner-title">说说</h1>
    </div>

    <!-- 说说内容 -->
    <v-card class="blog-container">
      <div v-for="item of talkList" :key="item.id" class="talk-item">
        <router-link :to="'/talks/' + item.id">
          <!-- 用户信息 -->
          <div class="user-info-wrapper">
            <v-avatar size="36" class="user-avatar">
              <img v-if="item['userAvatar'] !== ''" :src="item['userAvatar']" alt="">
              <img v-else :src="$store.state.otherConfig['touristAvatar']" alt="">
            </v-avatar>
            <div class="user-detail-wrapper">
              <div class="user-nickname">
                {{ item['userNickname'] }}
                <span v-if="item['userId'] === '1'" class="blogger-tag">站长</span>
              </div>
              <!-- 发表时间 -->
              <div class="time">
                {{ item['createTime'] }}
                <span v-if="item['isTop'] === true" class="top">
                  <svg class="iconfont_svg" aria-hidden="true" style="font-size: 16px;vertical-align: -4px;">
                    <use xlink:href="#icon-zhiding" />
                  </svg>
                </span>
              </div>
              <!-- 说说信息 -->
              <div class="talk-content" v-html="item['content']" />
              <!-- 图片列表 -->
              <v-row v-if="item['imagesList']" class="talk-images">
                <v-col v-for="(img, index) of item['imagesList']" :key="index" :md="4" :cols="6">
                  <v-img class="images-items" :src="img" aspect-ratio="1" max-height="200" @click.prevent="previewImg(item['imagesList'], img)" />
                </v-col>
              </v-row>
              <!-- 说说操作 -->
              <div class="talk-operation">
                <div class="talk-operation-item">
                  <v-icon size="16" :color="isLike(item.id)" class="like-btn" @click.prevent="like(item)">
                    iconfont icon-dianzan1
                  </v-icon>
                  <div class="operation-count">
                    {{ item['likeCount'] == null ? 0 : item['likeCount'] }}
                  </div>
                </div>
                <div class="talk-operation-item">
                  <v-icon size="16" color="#999">iconfont icon-comment-v2-full</v-icon>
                  <div class="operation-count">
                    {{ item['commentCount'] == null ? 0 : item['commentCount'] }}
                  </div>
                </div>
              </div>
            </div>
          </div>
        </router-link>
      </div>
      <div v-if="!pageLoading && talkList.length === 0" class="category-title">空空如也~</div>
      <div v-if="talkList && count > talkList.length" class="load-wrapper" @click="listTalks">
        <v-btn outlined>
          加载更多...
        </v-btn>
      </div>
    </v-card>
  </div>
</template>

<script>
export default {
  data: function() {
    return {
      count: 0,
      current: 1,
      size: 10,
      talkList: [],
      previewList: [],
      pageLoading: false
    }
  },
  computed: {
    cover() {
      let cover = ''
      this.$store.state.pageList.forEach(item => {
        if (item['pageLabel'] === 'talk') {
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
    this.listTalks()
  },
  methods: {
    listTalks() {
      const param = {
        page: this.current,
        pageSize: this.size
      }

      this.$loading.show()
      this.pageLoading = true
      this.$mapi.portal.queryTalkList(param).then(({ data }) => {
        if (this.current === 1) {
          this.talkList = data.list
        } else {
          this.talkList.push(...data.list)
        }

        this.current++
        this.count = data.total
      }).catch(_ => {
        this.$toast({ type: 'error', message: '说说列表加载失败' })
      }).finally(_ => {
        this.$loading.hide()
        this.pageLoading = false
      })
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
.talk-item:not(:first-child) {
  margin-top: 20px;
}
.talk-item {
  padding: 16px 20px;
  border-radius: 10px;
  background: rgba(255, 255, 255, 0.1);
  box-shadow: 0 3px 8px 6px rgb(7 17 27 / 6%);
  transition: all 0.3s ease 0s;
}
.talk-item:hover {
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
  flex: 1;
  margin-left: 10px;
  width: 0;
}
.user-nickname {
  font-size: 15px;
  font-weight: bold;
  vertical-align: middle;
}
.user-sign {
  font-size: 20px;
  vertical-align: -4px;
  margin-left: 5px;
}
.time {
  color: #999;
  margin-top: 2px;
  font-size: 12px;
}
.top {
  color: #ff7242;
  margin-left: 10px;
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
  margin-top: 20px;
  display: flex;
  align-items: center;
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
.like-btn:hover {
  color: #eb5055 !important;
}
.category-title {
  text-align: center;
  font-size: 36px;
  line-height: 2;
}
@media (max-width: 759px) {
  .category-title {
    font-size: 28px;
  }
}
</style>
