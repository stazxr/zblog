<template>
  <div>
    <!-- 封面 -->
    <div class="home-banner" :style="cover">
      <div class="banner-container">
        <h1 class="blog-title animated zoomIn">
          {{ websiteConfig['websiteName'] }}
        </h1>
        <div class="blog-intro">
          <span style="color: #fff">{{ typedConfig.output }}</span>
          <span class="typed-cursor">|</span>
        </div>
      </div>
      <div class="waves-area">
        <svg class="waves-svg" xmlns="http://www.w3.org/2000/svg" xlink="http://www.w3.org/1999/xlink" viewBox="0 24 150 28" preserveAspectRatio="none" shape-rendering="auto">
          <defs>
            <path id="gentle-wave" d="M -160 44 c 30 0 58 -18 88 -18 s 58 18 88 18 s 58 -18 88 -18 s 58 18 88 18 v 44 h -352 Z" />
          </defs>
          <g class="parallax">
            <use href="#gentle-wave" x="48" y="0" />
            <use href="#gentle-wave" x="48" y="3" />
            <use href="#gentle-wave" x="48" y="5" />
            <use href="#gentle-wave" x="48" y="7" />
          </g>
        </svg>
      </div>
      <div class="scroll-down" @click="scrollDown">
        <v-icon class="iconfont icon-collapse scroll-down-effects" />
      </div>
    </div>

    <!-- 主页内容 -->
    <v-row class="home-container">
      <v-col md="9" cols="12">
        <!-- 网站公告 -->
        <v-card v-if="!isMobile" class="animated zoomIn">
          <div class="announcement">
            <v-icon class="iconfont icon-guangbo" />
            <div v-if="websiteConfig['websiteNotice'] && websiteConfig['websiteNotice'] !== ''">
              {{ websiteConfig['websiteNotice'] }}
            </div>
            <div v-else>
              作者很懒，什么都没留下~
            </div>
          </div>
        </v-card>

        <!-- 说说轮播 -->
        <!-- <v-card v-if="talkList.length > 0" class="animated zoomIn">
          <Swiper :list="talkList" />
        </v-card> -->

        <!-- 分界线 -->
        <div class="home-hr">
          📔 文章列表
        </div>

        <!-- 文章列表 -->
        <v-card v-for="(item, index) of articleList" :key="item.id" class="animated zoomIn article-card">
          <!-- 文章封面图 -->
          <div :class="isRight(index)">
            <router-link :to="'/articles/' + item.id">
              <v-img class="on-hover" width="100%" height="100%" :src="getArticleCover(item)">
                <div class="ribbon">
                  <span v-if="item['articleType'] === 1">原创</span>
                  <span v-if="item['articleType'] === 2">转载</span>
                  <span v-if="item['articleType'] === 3">翻译</span>
                </div>
              </v-img>
            </router-link>
          </div>
          <!-- 文章信息 -->
          <div class="article-wrapper">
            <div class="article-top">
              <router-link :to="'/categories/' + item['categoryId']">
                <svg class="iconfont_svg" aria-hidden="true" style="font-size: 15px">
                  <use xlink:href="#icon-fenlei" />
                </svg> {{ item['categoryName'] }}
              </router-link>
              <span> | </span>
              <svg class="iconfont_svg" aria-hidden="true" style="font-size: 15px">
                <use xlink:href="#icon-calendar" />
              </svg> 发布于 {{ item['createTime'] }}
            </div>
            <div class="article-title">
              <router-link :to="'/articles/' + item.id"> {{ item['title'] }} </router-link>
            </div>
            <div class="article-data" style="margin-bottom: 15px">
              <span>
                <svg class="iconfont_svg" aria-hidden="true" style="font-size: 15px">
                  <use v-if="item['authorGender'] === 1" xlink:href="#icon-yonghu1" />
                  <use v-else-if="item['authorGender'] === 2" xlink:href="#icon-yonghu2" />
                  <use v-else xlink:href="#icon-yonghu" />
                </svg>
                {{ item['authorNickname'] }} ·
              </span>
              <span>
                <svg class="iconfont_svg" aria-hidden="true" style="font-size: 15px">
                  <use xlink:href="#icon-caidan_14" />
                </svg> {{ item.viewCount || 0 }} 热度 ·
              </span>
              <span>
                <svg class="iconfont_svg" aria-hidden="true" style="font-size: 15px">
                  <use xlink:href="#icon-liuyan" />
                </svg> {{ item['commentCount'] || 0 }} 条评论 ·
              </span>
              <span>
                <svg class="iconfont_svg" aria-hidden="true" style="font-size: 15px">
                  <use xlink:href="#icon-dianzan" />
                </svg> {{ item['likeCount'] || 0 }} 点赞
              </span>
            </div>
            <div class="article-content">
              {{ item['content'] }}
            </div>
            <div class="article-label">
              <router-link v-for="tag of item['tagList']" :key="tag.id" style="display:inline-block" :to="'/tags/' + tag.id" class="mr-1">
                <svg class="iconfont_svg" aria-hidden="true" style="font-size: 15px">
                  <use xlink:href="#icon-shengdan-biaoqian" />
                </svg> {{ tag['name'] }}
              </router-link>
            </div>
          </div>
        </v-card>

        <!-- 无限加载 -->
        <infinite-loading spinner="bubbles" @infinite="infiniteHandler">
          <div slot="no-results" />
          <div slot="no-more" />
        </infinite-loading>
      </v-col>

      <!-- 博客信息 -->
      <v-col md="3" cols="12" class="d-md-block d-none right-div" style="overflow-y: scroll;">
        <div class="blog-wrapper">
          <!-- 基础介绍 -->
          <v-card class="animated zoomIn blog-card mt-5">
            <div class="author-wrapper">
              <v-avatar size="110">
                <img :src="websiteConfig['websiteAvatar']" class="author-avatar" alt="404">
              </v-avatar>
              <div style="font-size: 1.375rem; margin-top:0.625rem;">
                {{ websiteConfig['websiteAuthor'] || websiteConfig['websiteName'] }}
              </div>
              <div v-if="websiteConfig['websiteIntro'] && websiteConfig['websiteIntro'] !== ''" style="font-size: 0.875rem;">
                {{ websiteConfig['websiteIntro'] }}
              </div>
            </div>
            <div class="blog-info-wrapper">
              <div class="blog-info-data">
                <router-link to="/archives">
                  <div style="font-size: 0.875rem">文章</div>
                  <div style="font-size: 1.25rem">
                    {{ countInfo.articleCount || 0 }}
                  </div>
                </router-link>
              </div>
              <div class="blog-info-data">
                <router-link to="/categories">
                  <div style="font-size: 0.875rem">分类</div>
                  <div style="font-size: 1.25rem">
                    {{ countInfo.categoryCount || 0 }}
                  </div>
                </router-link>
              </div>
              <div class="blog-info-data">
                <router-link to="/columns">
                  <div style="font-size: 0.875rem">专栏</div>
                  <div style="font-size: 1.25rem">
                    {{ countInfo.columnCount || 0 }}
                  </div>
                </router-link>
              </div>
              <div class="blog-info-data">
                <router-link to="/tags">
                  <div style="font-size: 0.875rem">标签</div>
                  <div style="font-size: 1.25rem">
                    {{ countInfo.tagCount || 0 }}
                  </div>
                </router-link>
              </div>
            </div>
            <a v-if="socialConfig['github'] && socialConfig['github'] !== ''" class="collection-btn" :href="socialConfig['github']" target="_blank">
              <svg class="iconfont_svg mr-1" aria-hidden="true" style="font-size: 15px">
                <use xlink:href="#icon-xingxing" />
              </svg> Go to star !
            </a>
            <!-- <v-divider v-else /> -->
            <div class="card-info-social">
              <a v-if="socialConfig['qq'] && socialConfig['qq'] !== ''" target="_blank" :href="socialConfig['qq']">
                <svg class="iconfont_svg" aria-hidden="true" style="font-size: 25px">
                  <use xlink:href="#icon-social-qq" />
                </svg>
              </a>
              <a v-if="socialConfig['weChat'] && socialConfig['weChat'] !== ''" target="_blank" :href="socialConfig['weChat']">
                <svg class="iconfont_svg" aria-hidden="true" style="font-size: 25px">
                  <use xlink:href="#icon-weixin" />
                </svg>
              </a>
              <a v-if="socialConfig['github'] && socialConfig['github'] !== ''" target="_blank" :href="socialConfig['github']">
                <svg class="iconfont_svg" aria-hidden="true" style="font-size: 25px">
                  <use xlink:href="#icon-icon-test" />
                </svg>
              </a>
              <a v-if="socialConfig['gitee'] && socialConfig['gitee'] !== ''" target="_blank" :href="socialConfig['gitee']">
                <svg class="iconfont_svg" aria-hidden="true" style="font-size: 25px">
                  <use xlink:href="#icon-gitee" />
                </svg>
              </a>
              <a v-if="socialConfig['csdn'] && socialConfig['csdn'] !== ''" target="_blank" :href="socialConfig['csdn']">
                <svg class="iconfont_svg" aria-hidden="true" style="font-size: 25px">
                  <use xlink:href="#icon-csdn" />
                </svg>
              </a>
            </div>
          </v-card>
          <v-card class="blog-card animated zoomIn mt-5 big">
            <div class="blog-card-title">
              <svg class="iconfont_svg" aria-hidden="true">
                <use xlink:href="#icon-a-fenleitongji2x" />
              </svg> 文章分类
            </div>
            <div class="blog-card-info categories">
              <ul v-for="item in categoryList" :key="item.id">
                <li>
                  <div v-if="item['pid'] == null" class="categories-item">
                    <div class="special-column-bar" />
                    <img v-if="item['imageUrl'] != null && item['imageUrl'] !== ''" :src="item['imageUrl']" alt="">
                    <span :title="item['name']">{{ item['name'] }}</span>
                  </div>
                  <router-link v-else class="categories-item" :to="'/categories/' + item['id']">
                    <div class="special-column-bar special-column-bar-second" />
                    <img v-if="item['imageUrl'] != null && item['imageUrl'] !== ''" :src="item['imageUrl']" alt="">
                    <span :title="item['name']">{{ item['name'] }}</span>
                  </router-link>
                  <div class="special-column-num">{{ item['articleCount'] || 0 }}篇</div>
                </li>
              </ul>
            </div>
          </v-card>
          <!-- <v-card class="blog-card animated zoomIn mt-5 big">
            <div class="blog-card-title">
              <svg class="iconfont_svg" aria-hidden="true">
                <use xlink:href="#icon-zhuanlan" />
              </svg> 文章专栏
            </div>
          </v-card> -->
          <v-card class="blog-card animated zoomIn mt-5 big">
            <div class="blog-card-title">
              <svg class="iconfont_svg" aria-hidden="true">
                <use xlink:href="#icon-biaoqian" />
              </svg> 标签云
            </div>
            <div class="blog-card-info">
              <!-- <TagCloud ref="tagCloud" :chart-data="tagList" @click.native="gotoTagPage" /> -->
              <TagCloud3D :tag-list="tagList" />
            </div>
          </v-card>
          <v-card class="blog-card animated zoomIn mt-5 big">
            <div class="blog-card-title">
              <svg class="iconfont_svg" aria-hidden="true">
                <use xlink:href="#icon-remen" />
              </svg> 热门文章
            </div>
            <div class="blog-card-info hot-articles">
              <ul v-for="item in hotArticleList" :key="item.id">
                <li>
                  <router-link :to="'/articles/' + item.id">
                    <span>{{ item['title'] }}</span>
                    <img src="https://file.suntaoblog.com/icon/readCountWhite.png" alt="">
                    <span class="read">{{ item['viewCount'] }}</span>
                  </router-link>
                </li>
              </ul>
            </div>
          </v-card>
          <v-card class="blog-card animated zoomIn mt-5 big">
            <div class="blog-card-title">
              <svg class="iconfont_svg" aria-hidden="true">
                <use xlink:href="#icon-pinglun" />
              </svg> 最新评论
            </div>
            <div class="blog-card-info lasted-comments">
              <div v-for="item in lastedCommentList" :key="item.id" class="lasted-comments-list-item">
                <div class="lasted-comments-avatar">
                  <img v-if="item['avatar'] === ''" :src="$store.state.otherConfig['touristAvatar']" class="lasted-comments-avatar-img" alt="">
                  <img v-else :src="item['avatar']" class="lasted-comments-avatar-img" alt="">
                </div>
                <div class="lasted-comments-content">
                  <router-link v-if="item['type'] === 1" :to="'/articles/' + item['objectId'] + '#' + item['id']" class="lasted-comments-content-comment">
                    <div v-html="item['content']" />
                  </router-link>
                  <router-link v-if="item['type'] === 2" :to="'/links#' + item['id']" class="lasted-comments-content-comment">
                    <div v-html="item['content']" />
                  </router-link>
                  <router-link v-if="item['type'] === 3" :to="'/talks/' + item['objectId'] + '#' + item['id']" class="lasted-comments-content-comment">
                    <div v-html="item['content']" />
                  </router-link>
                  <div class="lasted-comments-content-author">
                    <!-- {{ item['type'] === 1 ? '文章' : item['type'] === 2 ? '说说' : '友链' }} · -->
                    <span>{{ item['nickname'] }} · {{ item['ipSource'] }}</span>
                  </div>
                </div>
              </div>
            </div>
          </v-card>
          <v-card class="blog-card animated zoomIn mt-5 big">
            <div class="blog-card-title">
              <svg class="iconfont_svg" aria-hidden="true">
                <use xlink:href="#icon-WEBSITE" />
              </svg> 网站资讯
            </div>
            <div class="blog-card-info">
              <div style="padding:4px 0 0">
                访问数:<span class="float-right"> {{ countInfo.viewsCount || 0 }}</span>
              </div>
              <div style="padding:4px 0 0">
                游客数:<span class="float-right"> {{ countInfo.visitorCount || 0 }}</span>
              </div>
              <div style="padding:4px 0 0">
                用户数:<span class="float-right"> {{ countInfo.userCount || 0 }}</span>
              </div>
              <div style="padding:4px 0 0">
                阅读数:<span class="float-right"> {{ countInfo.articleViewCount || 0 }}</span>
              </div>
              <div style="padding:4px 0 0">
                评论数:<span class="float-right"> {{ countInfo.commentCount || 0 }}</span>
              </div>
              <div style="padding:4px 0 0">
                弹幕数:<span class="float-right"> {{ countInfo.messageCount || 0 }}</span>
              </div>
              <div style="padding:4px 0 0">
                网站版本:<span class="float-right">{{ $store.state.websiteVersion }}</span>
              </div>
              <div style="padding:4px 0 0">
                建站时间:<span class="float-right">{{ websiteConfig['websiteCreateTime'] }}</span>
              </div>
            </div>
          </v-card>
        </div>
      </v-col>
    </v-row>

    <!-- 提示消息 -->
    <!-- <v-snackbar v-model="tip" top color="#49b1f5" :timeout="2000">
      按CTRL+D 键将本页加入书签
    </v-snackbar> -->
  </div>
</template>

<script>
import EasyTyper from 'easy-typer-js'
// import Swiper from '../../components/Swiper.vue'
import TagCloud3D from '../../components/TagCloud3D.vue'
export default {
  name: 'Home',
  components: {
    // Swiper,
    TagCloud3D
  },
  data() {
    return {
      tip: false,
      typedConfig: {
        // 表示要显示的文本内容
        output: '',
        // 表示打字动画的类型
        type: 'rollback',
        // 表示动画是否结束，通常用于判断动画是否完成
        isEnd: true,
        // 表示打字速度，即每个字符打印的延迟时间
        speed: 300,
        // 表示是否启用单个字符回退效果，即是否以逐个字符的方式删除已打印的字符
        singleBack: true,
        // 表示每次打字动画完成后的休眠时间，即动画完成后暂停多长时间才开始下一个动画
        sleep: 1,
        // ss
        backSpeed: 300,
        // ss
        sentencePause: true
      },
      noArticleCoverImg: 'https://file.suntaoblog.com/image/no-article-cover.png',
      articleList: [],
      talkList: [],
      tagList: [],
      hotArticleList: [],
      categoryList: [],
      lastedCommentList: [],
      articlePage: 1,
      articlePageSize: 15
    }
  },
  computed: {
    cover() {
      let cover = ''
      this.$store.state.pageList.forEach(item => {
        if (item['pageLabel'] === 'home') {
          cover = item['pageCover']
          return 'background: url(' + cover + ') center center / cover no-repeat'
        }
      })
      return 'background: url(' + cover + ') center center / cover no-repeat'
    },
    articleDefaultImg() {
      return this.$store.state.articleDefaultImg
    },
    isRight() {
      return function(index) {
        if (index % 2 === 0) {
          return 'article-cover left-radius'
        }
        return 'article-cover right-radius'
      }
    },
    websiteConfig() {
      return this.$store.state.websiteConfig
    },
    socialConfig() {
      return this.$store.state.socialConfig
    },
    countInfo() {
      return this.$store.state.countInfo
    },
    isMobile() {
      return navigator.userAgent.match(
        /(phone|pad|pod|iPhone|iPod|ios|iPad|Android|Mobile|BlackBerry|IEMobile|MQQBrowser|JUC|Fennec|wOSBrowser|BrowserNG|WebOS|Symbian|Windows Phone)/i
      )
    }
  },
  created() {
    this.init()
    this.listArticles()
    // this.listHomeTalks()
    this.listHomeTags()
    this.listLastedComments()
    this.listHotArticles()
    this.listCategories()
  },
  methods: {
    init() {
      document.title = this.websiteConfig['websiteName']
      fetch('https://v1.hitokoto.cn?c=i').then(res => {
        return res.json()
      }).then(({ hitokoto }) => {
        this.initTyped(hitokoto)
      })
    },
    initTyped(input, fn, hooks) {
      const config = this.typedConfig
      // eslint-disable-next-line no-unused-vars
      const typed = new EasyTyper(config, input, fn, hooks)
    },
    scrollDown() {
      window.scrollTo({
        behavior: 'smooth',
        top: document.documentElement.clientHeight
      })
    },
    listArticles() {
      const param = {
        page: this.articlePage,
        pageSize: this.articlePageSize
      }
      this.$loading.show()
      this.$mapi.portal.queryArticleList(param).then(({ data }) => {
        if (data.list.length !== 0) {
          this.articlePage++
          this.articleList.push(...data.list)
        }
      }).finally(_ => {
        this.$loading.hide()
      })
    },
    listHomeTalks() {
      this.$mapi.portal.queryBoardTalkList().then(({ data }) => {
        this.talkList = data
      }).catch(_ => {
        this.talkList = []
      })
    },
    listHomeTags() {
      this.$nextTick(() => {
        this.$mapi.portal.queryBoardTagList().then(({ data }) => {
          this.tagList = data
        }).catch(_ => {
          this.tagList = []
        })
      })
    },
    listHotArticles() {
      this.$mapi.portal.queryBoardHotArticleList().then(({ data }) => {
        this.hotArticleList = data
      }).catch(_ => {
        this.hotArticleList = []
      })
    },
    listCategories() {
      this.$mapi.portal.queryBoardCategoryList().then(({ data }) => {
        this.categoryList = data
      }).catch(_ => {
        this.categoryList = []
      })
    },
    listLastedComments() {
      this.$mapi.portal.queryBoardLastedCommentList().then(({ data }) => {
        this.lastedCommentList = data
      }).catch(_ => {
        this.lastedCommentList = []
      })
    },
    getArticleCover(article) {
      if (article['coverImageType'] === 5) {
        return this.noArticleCoverImg
      } else if (article['coverImageType'] === 3) {
        return this.articleDefaultImg
      } else {
        return article['articleImgLinkList'] && article['articleImgLinkList'].length > 0 ? article['articleImgLinkList'][0] : ''
      }
    },
    gotoTagPage(var1, var2, var3) {
      // 自定义词云
      // if (this.$refs.tagCloud.chart['yid']) {
      //   this.$router.push('/tags/' + this.$refs.tagCloud.chart['yid'])
      // }
    },
    infiniteHandler($state) {
      const param = {
        page: this.articlePage,
        pageSize: this.articlePageSize
      }
      this.$loading.show()
      this.$mapi.portal.queryArticleList(param).then(({ data }) => {
        if (data.list.length === 0) {
          $state.complete()
        } else {
          this.articlePage++
          this.articleList.push(...data.list)
          $state.loaded()
        }
      }).catch(_ => {
        $state.complete()
        this.$toast({ type: 'error', message: '文章列表加载失败' })
      }).finally(_ => {
        this.$loading.hide()
      })
    }
  }
}
</script>

<style lang="stylus">
.typed-cursor
  opacity: 1;
  color: #fff;
  -webkit-animation: blink 0.7s infinite;
  -moz-animation: blink 0.7s infinite;
  animation: blink 0.7s infinite;
  @keyframes blink
    0%
      opacity: 1
    50%
      opacity: 0
    100%
      opacity: 1
  @-webkit-keyframes blink
    0%
      opacity: 1
    50%
      opacity: 0
    100%
      opacity: 1
  @-moz-keyframes blink
    0%
      opacity: 1
    50%
      opacity: 0
    100%
      opacity: 1
</style>

<style scoped>
/* home-banner */
.home-banner {
  position: absolute;
  top: -60px;
  left: 0;
  right: 0;
  height: 100vh;
  background-attachment: fixed;
  text-align: center;
  color: #fff !important;
  animation: header-effect 1s;
}
.home-banner:before {
  position: absolute;
  /* top: -60px; */
  left: 0;
  right: 0;
  height: 100vh;
  background-color: rgba(0,0,0,.25);
  content: "";
}
.banner-container {
  margin-top: 43vh;
  line-height: 2.0;
  user-select: none;
  color: #fff;
}
.blog-intro {
  width: max-content;
  margin: 0 auto;
  cursor: pointer;
  color: #fff;
  background: rgba(0, 0, 0, 0.5);
  border-radius: 10px;
  padding-left: 10px;
  padding-right: 10px;
}

/* waves-area */
.waves-area {
  width: 100%;
  position: absolute;
  left: 0;
  bottom: -10px;
  z-index: 1;
  display: block !important;
}
.waves-area .waves-svg {
  width: 100%;
  height: 4rem;
}
.parallax>use {
  animation: move-forever 25s cubic-bezier(0.55, 0.5, 0.45, 0.5) infinite;
}
.parallax>use:nth-child(1) {
  animation-delay: -2s;
  animation-duration: 7s;
  fill: #5782f038;
}
.parallax>use:nth-child(2) {
  animation-delay: -3s;
  animation-duration: 10s;
  fill: #b2c2eb38;
}
.parallax>use:nth-child(3) {
  animation-delay: -4s;
  animation-duration: 13s;
  fill: #7d9be238;
}
.parallax>use:nth-child(4) {
  animation-delay: -5s;
  animation-duration: 20s;
  fill: #bdc9e738;
}

/* scroll-down */
.scroll-down {
  cursor: var(--globalPointer);
  position: absolute;
  bottom: 0;
  width: 100%;
  z-index: 2;
}
.scroll-down i {
  font-size: 2rem;
}
.scroll-down-effects {
  color: #eee !important;
  text-align: center;
  text-shadow: 0.1rem 0.1rem 0.2rem rgba(0, 0, 0, 0.15);
  line-height: 1.5;
  display: inline-block;
  text-rendering: auto;
  -webkit-font-smoothing: antialiased;
  animation: scroll-down-effect 2s infinite;
}
@keyframes scroll-down-effect {
  0% {
    top: 0;
    opacity: 0.4;
    filter: alpha(opacity=30);
  }
  50% {
    top: -15px;
    opacity: 1;
    filter: none;
  }
  100% {
    top: 0;
    opacity: 0.4;
    filter: alpha(opacity=30);
  }
}

/* announcement */
.announcement {
  margin-top: 20px;
  padding: 22px 22px 22px 30px;
  display: flex;
  /* border: 1px dashed #DDDDDD; */
  /* max-width: 780px; */
}
.announcement i {
  color: red;
  font-size: 22px;
  margin: auto 0;
  animation: scale 0.8s ease-in-out infinite;
}
.announcement div {
  white-space: pre-line;
  margin-left: 20px;
  line-height: 30px;
}

/* home-hr */
.home-hr {
  color: #797979;
  border-bottom: 1px dashed #DDDDDD;
  padding-bottom: 5px;
  margin: 30px 0 50px 0;
}

/* 文章的卡片样式 */
.article-card {
  user-select: none;
  border-radius: 12px 8px 8px 12px;
}
.article-cover {
  overflow: hidden;
}
.left-radius {
  border-radius: 8px 0 0 8px !important;
  order: 0;
}
.right-radius {
  border-radius: 0 8px 8px 0 !important;
  order: 1;
}

/* 文章信息样式 */
.article-wrapper {
  height: 100%;
  padding: 1.25rem 2rem;
}
.article-wrapper a {
  font-size: 14px;
}
.article-wrapper a {
  transition: all 0.3s;
}
.article-wrapper a:hover {
  color: #8e8cd8;
}
.article-wrapper .article-top {
  overflow: hidden;
  text-overflow: ellipsis;
  display: -webkit-box;
  -webkit-line-clamp: 1;
  -webkit-box-orient: vertical;
}
.article-wrapper .article-top, .article-wrapper .article-top a {
  font-size: 12px !important;
  /* color: #797979; */
}
.article-wrapper .article-top a:hover {
  color: #8e8cd8;
}
.article-wrapper .article-title a {
  font-size: 18px;
  font-weight: bold;
  line-height: 1.4;
  overflow: hidden;
  text-overflow: ellipsis;
  display: -webkit-box;
  -webkit-line-clamp: 1;
  -webkit-box-orient: vertical;
  margin: 20px 0;
}
.article-wrapper .article-data {
  font-size: 12px !important;
  /* color: #797979; */
  overflow: hidden;
  text-overflow: ellipsis;
  display: -webkit-box;
  -webkit-line-clamp: 1;
  -webkit-box-orient: vertical;
}
.article-wrapper .article-data span {
  padding-right: 0;
}
.article-wrapper .article-content {
  font-size: 16px;
  line-height: 2;
  overflow: hidden;
  text-overflow: ellipsis;
  display: -webkit-box;
  -webkit-line-clamp: 3;
  -webkit-box-orient: vertical;
}
.article-wrapper .article-label {
  overflow: hidden;
  text-overflow: ellipsis;
  display: -webkit-box;
  -webkit-line-clamp: 1;
  -webkit-box-orient: vertical;
}
.article-wrapper .article-label span {
  padding: 3px 10px;
  background-color: #EEEEEE;
  border-radius: 3px;
  font-size: 12px;
  color: #797979;
  transition: all 0.3s;
  cursor: pointer;
  user-select: none;
}
.article-wrapper .article-label span:hover {
  background-color: orange;
  color: white;
}

/* 博客信息样式 */
.right-div::-webkit-scrollbar {
  width: 0 !important;
}
.right-div {
  -ms-overflow-style: none;
}
.right-div {
  /* overflow: -moz-scrollbars-none; */
}
.blog-wrapper {
  position: sticky;
  top: 10px;
}
.blog-card {
  line-height: 2;
  padding: 1.25rem 1.5rem;
}
.blog-card .blog-card-title {
  font-size: 1em;
  text-align: center;
  color: #9a37c5;
  font-weight: 900;
  padding-bottom: 10px;
}
.blog-card .blog-card-title .v-icon {
  color: #9a37c5;
}
.blog-card .blog-card-info {
  padding: 0.25rem;
  font-size: 0.875rem;
}

/* 基本信息 */
.author-wrapper {
  text-align: center;
}
.author-avatar {
  transition: all 0.5s;
}
.author-avatar:hover {
  transform: rotate(360deg);
}
.blog-info-wrapper {
  display: flex;
  justify-self: center;
  padding: 0.875rem 0;
}
.blog-info-data {
  flex: 1;
  text-align: center;
}
.blog-info-data a {
  text-decoration: none;
}
.blog-info-data a:hover {
  color: #8e8cd8;
}
.collection-btn {
  text-align: center;
  z-index: 1;
  font-size: 14px;
  position: relative;
  display: block;
  background-color: #49b1f5;
  color: #fff !important;
  height: 32px;
  line-height: 32px;
  transition-duration: 1s;
  transition-property: color;
}
.collection-btn:before {
  position: absolute;
  top: 0;
  right: 0;
  bottom: 0;
  left: 0;
  z-index: -1;
  background: #ff7242;
  content: "";
  transition-timing-function: ease-out;
  transition-duration: 0.5s;
  transition-property: transform;
  transform: scaleX(0);
  transform-origin: 0 50%;
}
.collection-btn:hover:before {
  transition-timing-function: cubic-bezier(0.45, 1.64, 0.47, 0.66);
  transform: scaleX(1);
}
.card-info-social {
  line-height: 40px;
  text-align: center;
  margin: 6px 0 -6px;
}
.card-info-social a {
  font-size: 1.5rem;
  padding: 0 8px !important;
}

/* 热门文章 */
.blog-card .blog-card-info.hot-articles {
  padding: 12px 16px 16px 16px;
  overflow: hidden;
}
.blog-card .blog-card-info.hot-articles ul {
  list-style: none;
  margin-top: -8px;
  word-wrap: break-word;
}
.blog-card .blog-card-info.hot-articles ul li {
  margin-bottom: 20px;
  word-wrap: break-word;
}
.blog-card .blog-card-info.hot-articles ul li a {
  display: block;
  word-wrap: break-word;
}
.blog-card .blog-card-info.hot-articles ul li a:hover {
  color: red;
}
.blog-card .blog-card-info.hot-articles ul li a img {
  width: 14px;
  height: 14px;
  border: 0;
  outline: none;
  vertical-align: -2px;
  margin: 0 3px;
  word-wrap: break-word;
}
.blog-card .blog-card-info.hot-articles ul li a .read {
  font-size: 12px;
  color: #999aaa;
  line-height: 24px;
  word-wrap: break-word;
}

/* 分类专栏 */
.blog-card .blog-card-info.categories {
  padding: 12px 16px 16px 16px;
  overflow: hidden;
}
.blog-card .blog-card-info.categories:before {
  display: block;
  position: absolute;
  content: "";
  width: 1px;
  height: 12px;
  background: #fff;
  left: 0;
  top: 0;
}
.blog-card .blog-card-info.categories ul {
  border-left: 1px dashed #ccccd8;
  margin: 0;
  padding: 0;
  list-style: none;
}
.blog-card .blog-card-info.categories ul li {
  margin: 0;
  padding: 5px 0;
  list-style: none;
  display: flex;
  -webkit-box-pack: justify;
  justify-content: space-between;
  -webkit-box-align: center;
  align-items: center;
}
.blog-card .blog-card-info.categories ul li .categories-item {
  text-decoration: none;
  outline: none;
  display: flex;
  -webkit-box-align: center;
  align-items: center;
  font-size: 14px;
  line-height: 24px;
  -webkit-box-flex: 1;
  flex: 1;
  overflow: hidden;
  position: relative;
}
.blog-card .blog-card-info.categories ul li .categories-item .special-column-bar {
  width: 12px;
  height: 1px;
  border-bottom: 1px dashed #ccccd8;
  flex-shrink: 0;
}
.blog-card .blog-card-info.categories ul li .categories-item .special-column-bar-second {
  width: 36px;
}
.blog-card .blog-card-info.categories ul li .special-column-num {
  color: #999aaa;
  font-size: 14px;
  line-height: 16px;
  flex-shrink: 0;
  margin-left: 16px;
}
.blog-card .blog-card-info.categories ul li .categories-item img {
  width: 32px;
  height: 18px;
  border: 1px solid #e8e8ed;
  border-radius: 2px;
  display: block;
  margin-right: 8px;
  margin-left: 4px;
}
.blog-card .blog-card-info.categories ul li .categories-item span {
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

/* 评论 */
.lasted-comments > .lasted-comments-list-item {
  display: flex;
  -webkit-box-align: center;
  -webkit-align-items: center;
  align-items: center;
  padding: 5px;
  border-radius: 10px;
}
.lasted-comments > .lasted-comments-list-item .lasted-comments-avatar {
  overflow: hidden;
  border-radius: 10px;
  width: 4em;
  height: 4em;
}
.lasted-comments > .lasted-comments-list-item .lasted-comments-avatar .lasted-comments-avatar-img {
  overflow: hidden;
  border-radius: 10px;
  width: 4em;
  height: 4em;
}
.lasted-comments > .lasted-comments-list-item .lasted-comments-content {
  -webkit-box-flex: 1;
  flex: 1;
  padding-left: 10px;
  word-break: break-all;
}
.lasted-comments > .lasted-comments-list-item .lasted-comments-content .lasted-comments-content-comment {
  font-size: 95%;
  line-height: 1.5;
  overflow: hidden;
  text-overflow: ellipsis;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
}
.lasted-comments > .lasted-comments-list-item .lasted-comments-content .lasted-comments-content-author {
  display: block;
  color: #858585;
  font-size: 85%;
  -webkit-line-clamp: 1;
  max-height: 20px;
}

/* Pc */
@media (min-width: 760px) {
  .blog-title {
    font-size: 2.5rem;
  }
  .blog-intro {
    font-size: 1.5rem;
  }
  .home-container {
    max-width: 1200px;
    margin: calc(100vh - 48px) auto 28px auto;
    padding: 0 5px;
  }
  .article-card {
    display: flex;
    overflow: hidden;
    align-items: center;
    height: 300px;
    width: 100%;
    margin-top: 20px;
  }
  .article-cover {
    height: 100%;
    width: 50%;
  }
  .article-cover >>> .on-hover {
    transition: all 1s;
  }
  .article-cover >>> .on-hover:hover {
    transform: scale(1.1);
  }
  .article-wrapper {
    width: 55%;
  }
  .article-wrapper .article-label {
    position: absolute;
    bottom: 20px;
  }
}

/* Mobile */
@media (max-width: 759px) {
  .blog-title {
    font-size: 26px;
  }
  .waves-area .waves-svg {
    height: 40px;
    min-height: 40px;
  }
  .home-container {
    width: 100%;
    margin: calc(100vh - 66px) auto 0 auto;
  }
  .home-hr {
    margin: 30px 0 40px 0;
  }
  .article-card {
    margin-top: 1rem;
  }
  .article-cover {
    border-radius: 8px 8px 0 0 !important;
    height: 230px !important;
    width: 100%;
  }
  .article-cover div {
    border-radius: 8px 8px 0 0 !important;
  }
  .article-wrapper {
    width: 100%;
  }
  .article-wrapper .article-label {
    padding-top: 15px;
  }
}
</style>
