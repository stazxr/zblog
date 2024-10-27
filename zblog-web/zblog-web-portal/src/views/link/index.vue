<template>
  <div>
    <div class="banner" :style="cover">
      <h1 class="banner-title">友情链接</h1>
    </div>
    <v-card class="blog-container">
      <div class="link-title mb-1">
        <svg class="iconfont_svg" aria-hidden="true" style="font-size: 20px;margin-right: 5px;">
          <use xlink:href="#icon-lianjie" />
        </svg> 友链列表
      </div>
      <v-row class="link-container">
        <v-col v-for="item of friendLinkList" :key="item.id" class="link-wrapper" md="4" cols="12">
          <a :href="item['linkUrl']" target="_blank">
            <v-avatar size="65" class="link-avatar">
              <img :src="item['headUrl']" alt="">
            </v-avatar>
            <div style="width:100%;z-index:10;">
              <div class="link-name">{{ item['name'] }}</div>
              <div class="link-intro">{{ item['linkRemark'] }}</div>
            </div>
          </a>
        </v-col>
      </v-row>
      <!-- 说明 -->
      <div class="link-title mt-4 mb-4">
        <svg class="iconfont_svg" aria-hidden="true" style="font-size: 20px;margin-right: 5px;">
          <use xlink:href="#icon-xinzeng" />
        </svg> 添加友链
      </div>
      <blockquote>
        <div>名称：{{ websiteConfig['websiteName'] }}</div>
        <div>介绍：{{ websiteConfig['websiteIntro'] }}</div>
        <div>链接：{{ websiteConfig['websiteLink'] }}</div>
        <!-- <div>头像：{{ websiteConfig['websiteAvatar'] }}</div> -->
        <div>头像：https://suntaoblog.oss-cn-beijing.aliyuncs.com/image/siteImg.png</div>
      </blockquote>
      <div class="mt-5 mb-5">
        需要交换友链的可在下方留言💖
      </div>
      <blockquote class="mb-10">
        友链信息展示需要，你的信息格式要包含：名称、介绍、链接、头像
      </blockquote>
      <!-- 评论 -->
      <Comment id="comment" :type="commentType" @getCommentCount="getCommentCount" />
    </v-card>
  </div>
</template>

<script>
import Comment from '../../components/Comment'
export default {
  components: {
    Comment
  },
  data: function() {
    return {
      friendLinkList: [],
      commentType: 2
    }
  },
  computed: {
    websiteConfig() {
      return this.$store.state.websiteConfig
    },
    cover() {
      let cover = ''
      this.$store.state.pageList.forEach(item => {
        if (item['pageLabel'] === 'link') {
          cover = item['pageCover']
        }
      })
      return 'background: url(' + cover + ') center center / cover no-repeat'
    }
  },
  created() {
    this.listFriendLink()
  },
  methods: {
    listFriendLink() {
      this.$loading.show()
      this.$mapi.portal.queryFriendLinkList().then(({ data }) => {
        this.friendLinkList = data
      }).catch(_ => {
        this.friendLinkList = []
      }).finally(_ => {
        this.$loading.hide()
      })
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
    }
  }
}
</script>

<style scoped>
blockquote {
  line-height: 2;
  margin: 0;
  font-size: 15px;
  border-left: 0.2rem solid #49b1f5;
  padding: 10px 1rem !important;
  background-color: #ecf7fe;
  border-radius: 4px;
}
.link-title {
  color: #344c67;
  font-size: 21px;
  font-weight: bold;
  line-height: 2;
}
.link-container {
  margin: 10px 10px 0;
}
.link-wrapper {
  position: relative;
  transition: all 0.3s;
  border-radius: 8px;
}
.link-name {
  overflow: hidden;
  text-overflow: ellipsis;
  display: -webkit-box;
  -webkit-line-clamp: 1;
  -webkit-box-orient: vertical;
}
.theme--dark.v-card .link-wrapper .link-name {
  color: var(--antique-white-color)
}
.link-intro {
  overflow: hidden;
  text-overflow: ellipsis;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
}
.theme--dark.v-card .link-wrapper .link-intro {
  color: var(--white-color)
}
.theme--dark.v-card blockquote {
  color: var(--black-color)
}
.link-avatar {
  margin-top: 5px;
  margin-left: 10px;
  transition: all 0.5s;
}
@media (max-width: 759px) {
  .link-avatar {
    margin-left: 30px;
  }
}
.link-name {
  text-align: center;
  font-size: 1.25rem;
  font-weight: bold;
  z-index: 1000;
}
.link-intro {
  text-align: center;
  padding: 16px 10px;
  height: 50px;
  font-size: 13px;
  color: #1f2d3d;
  width: 100%;
}
.link-wrapper:hover a {
  color: #fff;
}
.link-wrapper:hover .link-intro {
  color: #fff;
}
.link-wrapper:hover .link-avatar {
  transform: rotate(360deg);
}
.link-wrapper a {
  color: #333;
  text-decoration: none;
  display: flex;
  height: 100%;
  width: 100%;
}
.link-wrapper:hover {
  box-shadow: 0 2px 20px #49b1f5;
}
.link-wrapper:hover:before {
  transform: scale(1);
}
.link-wrapper:before {
  position: absolute;
  border-radius: 8px;
  top: 0;
  right: 0;
  bottom: 0;
  left: 0;
  background: #49b1f5 !important;
  content: "";
  transition-timing-function: ease-out;
  transition-duration: 0.3s;
  transition-property: transform;
  transform: scale(0);
}
</style>
