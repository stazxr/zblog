<template>
  <div>
    <!-- 文章信息 -->
    <div class="banner" :style="articleCover">
      <div class="article-info-container">
        <div class="article-title">{{ article.title }}</div>
        <div class="article-info">
          <div class="first-line">
            <span>
              <svg class="iconfont_svg" aria-hidden="true" style="font-size: 15px">
                <use v-if="article.authorGender === 1" xlink:href="#icon-yonghu1" />
                <use v-else-if="article.authorGender === 2" xlink:href="#icon-yonghu2" />
                <use v-else xlink:href="#icon-yonghu" />
              </svg>
              {{ article.authorNickname }}
            </span>
            <span class="separator">|</span>
            <span>
              <svg class="iconfont_svg" aria-hidden="true" style="font-size: 15px">
                <use xlink:href="#icon-calendar" />
              </svg> 发表于 {{ article.createTime }}
            </span>
            <span class="separator">|</span>
            <span v-if="article.updateTime != null && article.updateTime !== ''">
              <svg class="iconfont_svg" aria-hidden="true" style="font-size: 15px">
                <use xlink:href="#icon-dingdanxiangqing-chuangjianshijian" />
              </svg> 更新于
              <template>
                {{ article.updateTime }}
              </template>
              <span class="separator">|</span>
            </span>
            <span class="article-category">
              <router-link :to="'/categories/' + article.categoryId">
                <svg class="iconfont_svg" aria-hidden="true" style="font-size: 15px">
                  <use xlink:href="#icon-fenlei" />
                </svg> {{ article.categoryName }}
              </router-link>
            </span>
          </div>
          <div class="second-line">
            <span>
              字数统计: {{ article.wordCount }}
            </span>
            <span class="separator">|</span>
            <span>
              预计阅读时长: {{ readTime }}
            </span>
          </div>
          <div class="third-line">
            <span class="separator">|</span>
            <!-- 阅读量 -->
            <span>
              阅读量: {{ article.viewCount }}
            </span>
            <span class="separator">|</span>
            <!-- 评论量 -->
            <span>
              评论量: {{ article.commentCount }}
            </span>
          </div>
        </div>
      </div>
    </div>

    <!-- 文章内容 -->
    <v-row class="article-container">
      <!-- 左侧 -->
      <v-col md="9" cols="12">
        <v-card class="article-wrapper">
          <Editor
            ref="article"
            v-model="html"
            :default-config="editorConfig"
            class="article-content"
            @onCreated="onCreated"
          />
          <!-- <article
            id="write"
            ref="article"
            v-highlight
            class="article-content wangEditor-body"
            v-html="article.content"
          /> -->
          <!-- 版权声明 -->
          <div class="article-copyright">
            <div>
              <span>文章作者：</span>
              <router-link to="/">
                {{ article.authorNickname }}
              </router-link>
            </div>
            <div>
              <span>文章链接：</span>
              <a :href="articleLink" target="_blank">{{ articleLink }} </a>
            </div>
            <div>
              <span>版权声明：</span>本文为博主原创文章，遵循&nbsp;
              <a href="https://creativecommons.org/licenses/by-nc-sa/4.0/" target="_blank">
                CC BY-NC-SA 4.0
              </a>
              &nbsp;版权协议，转载请附上原文出处链接和本声明。
            </div>
          </div>
          <!-- 标签 / 转发 -->
          <div class="article-operation">
            <div class="tag-container">
              <router-link v-for="item of article.tagList" :key="item.id" :to="'/tags/' + item.id">
                {{ item.name }}
              </router-link>
            </div>
          </div>
          <!-- 点赞 / 打赏 -->
          <div class="article-reward">
            <a :class="isLike" @click="likeArticle">
              <i class="iconfont icon-dianzan1" /> 点赞
              <span v-show="article.likeCount > 0">{{ article.likeCount }}</span>
            </a>
            <a v-if="otherConfig['isReward'] === 1" class="reward-btn">
              <i class="iconfont icon-ico" /> 打赏
              <div class="animated fadeInDown reward-main">
                <ul class="reward-all">
                  <li class="reward-item">
                    <img :src="otherConfig['weiXinQrCode']" alt="" class="reward-img">
                    <div class="reward-desc">微信</div>
                  </li>
                  <li class="reward-item">
                    <img :src="otherConfig['alipayQrCode']" alt="" class="reward-img">
                    <div class="reward-desc">支付宝</div>
                  </li>
                </ul>
              </div>
            </a>
          </div>
          <!-- 上一篇 / 下一篇 -->
          <div class="pagination-post">
            <div v-if="article.lastArticle" :class="isFull(article.lastArticle)">
              <router-link :to="'/articles/' + article.lastArticle.id">
                <img :src="getArticleCover(article.lastArticle)" alt="" class="post-cover">
                <div class="post-info">
                  <div class="label">上一篇</div>
                  <div class="post-title article-title-more-3">
                    {{ article.lastArticle.title }}
                  </div>
                </div>
              </router-link>
            </div>
            <div v-if="article.nextArticle" :class="isFull(article.nextArticle)">
              <router-link :to="'/articles/' + article.nextArticle.id">
                <img :src="getArticleCover(article.nextArticle)" alt="" class="post-cover">
                <div class="post-info" style="text-align: right">
                  <div class="label">下一篇</div>
                  <div class="post-title article-title-more-3">
                    {{ article.nextArticle.title }}
                  </div>
                </div>
              </router-link>
            </div>
          </div>
          <!-- 推荐文章 -->
          <div v-if="article.recommendList.length > 0" class="recommend-container">
            <div class="recommend-title">
              <svg class="iconfont_svg" aria-hidden="true" style="font-size: 25px;">
                <use xlink:href="#icon-tuijian" />
              </svg> 相关推荐
            </div>
            <div class="recommend-list">
              <div v-for="item of article.recommendList" :key="item.id" class="recommend-item">
                <router-link :to="'/articles/' + item.id">
                  <img :src="getArticleCover(item)" alt="" class="recommend-cover">
                  <div class="recommend-info">
                    <div class="recommend-date">
                      <i class="iconfont icon-riLi" />
                      {{ item.createTime }}
                    </div>
                    <div class="article-title-more-3">{{ item.title }}</div>
                  </div>
                </router-link>
              </div>
            </div>
          </div>
          <hr>
          <comment v-if="article.commentFlag" id="comment" :type="commentType" @getCommentCount="getCommentCount" />
        </v-card>
      </v-col>
      <!-- 右侧 -->
      <v-col md="3" cols="12" class="d-md-block d-none">
        <div style="position: sticky;top: 20px;">
          <!-- 文章目录 -->
          <v-card class="right-container">
            <div class="right-title">
              <i class="iconfont icon-daohang" style="font-size: 16px" />
              <span style="margin-left:10px">目录</span>
            </div>
            <div id="toc" />
          </v-card>
          <!-- 最新文章 -->
          <v-card class="right-container" style="margin-top:20px">
            <div class="right-title">
              <i class="iconfont icon-tubiaozuixin01" style="font-size:16px" />
              <span style="margin-left:10px">最新文章</span>
            </div>
            <div class="article-list">
              <div v-for="item of article.newestList" :key="item.id" class="article-item">
                <router-link :to="'/articles/' + item.id" class="content-cover">
                  <img :src="getArticleCover(item)" alt="">
                </router-link>
                <div class="content">
                  <div class="content-title">
                    <router-link :to="'/articles/' + item.id" class="article-title-more-2">
                      {{ item.title }}
                    </router-link>
                  </div>
                  <div class="content-time">{{ item.createTime }}</div>
                </div>
              </div>
            </div>
          </v-card>
        </div>
      </v-col>
    </v-row>
  </div>
</template>

<script>
import Clipboard from 'clipboard'
import tocbot from 'tocbot'
import Comment from '../../components/Comment'
import { Editor } from '@wangeditor/editor-for-vue'
// import hljs from 'highlight.js'
export default {
  components: {
    Editor, Comment
  },
  data() {
    return {
      editor: null,
      html: '<p><br></p>',
      editorConfig: {
        placeholder: '',
        readOnly: true,
        autoFocus: false,
        scroll: false,
        hoverbarKeys: {
          attachment: {
            menuKeys: ['downloadAttachment']
          },
          formula: {
            menuKeys: ['editFormula']
          }
        }
      },
      // 阅读时长
      readTime: 0,
      // 评论数
      commentCount: 0,
      // 评论类型
      commentType: 1,
      // 文章封面
      noArticleCoverImg: 'https://file.suntaoblog.com/image/no-article-cover.png',
      // 文章链接
      articleLink: window.location.href,
      clipboard: null,
      // 是否禁用文章点赞按钮
      disabledLikeBtn: false,
      article: {
        id: '',
        title: '',
        remark: '',
        content: '',
        coverImageType: '',
        articleImgLinkList: [],
        authorGender: '',
        authorNickname: '',
        categoryId: '',
        categoryName: '',
        createTime: '',
        updateTime: '',
        wordCount: 0,
        viewCount: 0,
        likeCount: 0,
        commentCount: 0,
        tagList: [],
        lastArticle: null,
        nextArticle: null,
        recommendList: [],
        newestList: [],
        commentFlag: ''
      }
    }
  },
  computed: {
    otherConfig() {
      return this.$store.state.otherConfig
    },
    articleCover() {
      let cover
      if (this.article.coverImageType === 3) {
        cover = this.articleDefaultImg
      } else if (this.article.coverImageType === 1 || this.article.coverImageType === 2 || this.article.coverImageType === 4) {
        if (this.article.articleImgLinkList && this.article.articleImgLinkList.length > 0) {
          cover = this.article.articleImgLinkList[0]
        }
      } else {
        cover = this.noArticleCoverImg
      }

      return 'background: url(' + cover + ') center center / cover no-repeat'
    },
    articleDefaultImg() {
      return this.$store.state.articleDefaultImg
    },
    isLike() {
      const articleLikeSet = this.$store.state.articleLikeSet
      return articleLikeSet.indexOf(this.article.id) !== -1 ? 'like-btn-active' : 'like-btn'
    },
    isFull() {
      return function(article) {
        return article ? 'post full' : 'post'
      }
    }
  },
  created() {
    this.getArticle()
  },
  destroyed() {
    tocbot.destroy()
    if (this.clipboard != null) {
      this.clipboard.destroy()
    }
  },
  methods: {
    // Editor
    onCreated(editor) {
      this.editor = Object.seal(editor)
    },
    getArticleCover(article) {
      if (article.coverImageType === 3) {
        return this.articleDefaultImg
      } else if (article.coverImageType === 1 || article.coverImageType === 2 || article.coverImageType === 4) {
        if (article.articleImgLinkList && article.articleImgLinkList.length > 0) {
          return article.articleImgLinkList[0]
        }

        return this.noArticleCoverImg
      } else {
        return this.noArticleCoverImg
      }
    },
    getArticle() {
      this.$loading.show()
      this.$mapi.portal.queryArticleDetail({ articleId: this.$route.params.articleId }).then(({ data }) => {
        if (data == null || data['articleStatus'] !== 5 || data['articlePerm'] !== 1) {
          this.$router.push('/article404')
          return
        }

        this.article.id = data['id'] || ''
        this.article.title = data['title'] || ''
        this.article.remark = data['remark'] || ''
        this.article.content = data['content'] || ''
        this.article.coverImageType = data['coverImageType'] || null
        this.article.articleImgLinkList = data['articleImgLinkList'] || []
        this.article.authorGender = data['authorGender'] || 3
        this.article.authorNickname = data['authorNickname'] || ''
        this.article.categoryId = data['categoryId'] || ''
        this.article.categoryName = data['categoryName'] || ''
        this.article.createTime = data['createTime'] || ''
        this.article.updateTime = data['updateTime'] || ''
        this.article.wordCount = data['wordCount'] || 0
        this.article.viewCount = data['viewCount'] || 0
        this.article.likeCount = data['likeCount'] || 0
        this.article.commentCount = data['commentCount'] || 0
        this.article.tagList = data['tagList'] || []
        this.article.lastArticle = data['lastArticle'] || null
        this.article.nextArticle = data['nextArticle'] || null
        this.article.recommendList = data['recommendList'] || []
        this.article.newestList = data['newestList'] || []
        this.article.commentFlag = data['commentFlag'] || false

        this.$nextTick(() => {
          // 回显文章内容
          this.editor.setHtml(this.article.content)

          // // 自定义代码块
          // const codes = document.querySelectorAll('pre code')
          // console.log('codes', codes)
          // codes.forEach((item) => {
          //   console.log('item', item)
          //   const pre = item.parentElement
          //
          //   // 行号
          //   const lineNumBox = document.createElement('div')
          //   lineNumBox.setAttribute('style', 'height: ' + item.offsetHeight + 'px')
          //   lineNumBox.className = 'line-num-box'
          //   let num = ''
          //   for (let i = 1; i <= Math.ceil(item.offsetHeight / 20); i++) {
          //     // 设行高二十
          //     num += i + '\n'
          //   }
          //   lineNumBox.innerText = num
          //   item.parentElement.insertBefore(lineNumBox, item)
          //
          //   // 代码块
          //   const codeBox = document.createElement('div')
          //   codeBox.className = 'code-box'
          //   codeBox.appendChild(item)
          //
          //   // 插入代码块
          //   pre.appendChild(codeBox)
          //
          //   // 标题
          //   const icon = `<div class="mac-icon">` +
          //     `<span class="mac-icon-red"></span>` +
          //     `<span class="mac-icon-yellow"></span>` +
          //     `<span class="mac-icon-green"></span>` +
          //     // `<span class="mac-icon-lang">${lang.split('-')[1].toUpperCase()}</span>` +
          //     `<button class="copy-button">复制</button>` +
          //     // `<button class="full-screen-button">全屏</button>` +
          //     `</div>`
          //   pre.insertAdjacentHTML('afterbegin', icon)
          //
          //   // 复制
          //   const copyButton = pre.firstElementChild.getElementsByClassName('copy-button')[0]
          //   copyButton.onclick = function() {
          //     // https://developer.mozilla.org/zh-CN/docs/Web/API/Clipboard/writeText
          //     const copyPromise = navigator.clipboard.writeText(pre.lastElementChild.innerText)
          //     copyPromise.then(() => {
          //       alert('复制成功')
          //     }).catch(() => {
          //       alert('复制失败')
          //     })
          //   }
          //
          //   // 代码高亮
          //   hljs.highlightElement(codeBox.firstElementChild)
          // })

          // 添加代码复制功能
          this.clipboard = new Clipboard('.copy-button')
          this.clipboard.on('success', () => {
            this.$toast({ type: 'success', message: '复制成功' })
          })

          // 增加文章阅读量
          this.viewArticle()

          // 计算阅读时间
          this.readTime = Math.round(this.article.wordCount / 400) + '分钟'

          // 添加文章生成目录功能
          tocbot.init({
            tocSelector: '#toc', // 要把目录添加元素位置，支持选择器
            contentSelector: '.w-e-scroll', // 获取html的元素
            headingSelector: 'h1, h2, h3', // 要显示的id的目录
            hasInnerContainers: true,
            onClick: function(e) {
              e.preventDefault()
            }
          })
        })
      }).catch(e => {
        console.log('文章信息反显失败', e)
        this.$toast({ type: 'error', message: '查询文章信息失败' })
        this.clearArticleInfo()
      }).finally(_ => {
        this.$loading.hide()
      })
    },
    clearArticleInfo() {
      this.article = {
        id: '',
        title: '',
        remark: '',
        content: '',
        coverImageType: '',
        articleImgLinkList: [],
        authorNickname: '',
        categoryId: '',
        categoryName: '',
        createTime: '',
        updateTime: '',
        wordCount: 0,
        viewCount: 0,
        likeCount: 0,
        tagList: [],
        lastArticle: null,
        nextArticle: null,
        recommendList: [],
        newestList: [],
        commentFlag: ''
      }
    },
    likeArticle() {
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
        articleId: this.article.id
      }
      this.disabledLikeBtn = true
      this.$mapi.portal.likeArticle(param).then(({ code, message }) => {
        if (code === 200) {
          // 判断是否点赞
          if (this.$store.state.articleLikeSet.indexOf(this.article.id) !== -1) {
            this.$set(this.article, 'likeCount', this.article.likeCount - 1)
            this.$toast({ type: 'success', message: '取消成功' })
          } else {
            this.$set(this.article, 'likeCount', this.article.likeCount + 1)
            this.$toast({ type: 'success', message: '点赞成功' })
          }

          this.$store.commit('articleLike', this.article.id)
        } else {
          this.$toast({ type: 'error', message: message })
        }
      }).catch(_ => {
        this.$toast({ type: 'error', message: '点赞失败' })
      }).finally(_ => {
        this.disabledLikeBtn = false
      })
    },
    viewArticle() {
      const param = {
        articleId: this.article.id
      }

      this.$mapi.portal.viewArticle(param).then(({ code }) => {
        if (code === 200) {
          this.$set(this.article, 'viewCount', this.article.viewCount + 1)
        }
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

<style src='@wangeditor/editor/dist/css/style.css' />
<!-- <style src='highlight.js/styles/xcode.css' /> -->

<style scoped>
/* 头部按钮 */
::v-deep .mac-icon {
  border-bottom: 1px solid silver;
  margin-bottom: 5px;
  color: deeppink;
}
::v-deep .mac-icon > span {
  display: inline-block;
  letter-spacing: 5px;
  word-spacing: 5px;
  width: 16px;
  height: 16px;
  border-radius: 8px;
}
::v-deep .mac-icon-red {
  background-color: red;
}
::v-deep .mac-icon-yellow {
  margin-left: 10px;
  background-color: yellow;
}
::v-deep .mac-icon-green {
  margin-left: 10px;
  background-color: green;
}
::v-deep .mac-icon-lang {
  width: 50px !important;
  padding-left: 10px;
  font-size: 16px;
  vertical-align: top;
}

/* 复制按钮 */
::v-deep .copy-button {
  width: 40px;
  height: 20px;
  background-color: wheat;
  margin-bottom: 3px;
  border-radius: 5px;
  outline: none;
  border: none;
  margin-left: calc(100% - 208px);
}
::v-deep .copy-button:hover {
  background-color: white;
}

/* 行号 */
::v-deep .line-num-box {
  display: inline-block;
  color: #fff;
  border-right: 2px solid white;
  line-height: 20px !important;
  font-size: 16px !important;
  text-align: right;
  padding-left: 10px;
  padding-right: 10px;
}

/* 代码块 */
::v-deep .code-box {
  display: inline-block;
  vertical-align: top;
  width: calc(100% - 50px);
  border-left-style: none;
}
::v-deep code {
  line-height: 20px !important;
  font-size: 16px !important;
  vertical-align: top;
  /* padding-top: 0 !important; */
  /* padding-bottom: 0 !important; */
  padding-left: 10px !important;
}
</style>

<style scoped>
.theme--light .article-content {
  /* textarea - css vars */
  --w-e-textarea-bg-color: #fff;
  --w-e-textarea-color: var(--theme-light-color);
  --w-e-textarea-border-color: #ccc;
  --w-e-textarea-slight-border-color: #e8e8e8;
  --w-e-textarea-slight-color: #d4d4d4;
  --w-e-textarea-slight-bg-color: #f5f2f0;
  --w-e-textarea-selected-border-color: #B4D5FF;
  --w-e-textarea-handler-bg-color: #4290f7;

  /* toolbar - css vars */
  --w-e-toolbar-color: #595959;
  --w-e-toolbar-bg-color: #fff;
  --w-e-toolbar-active-color: #333;
  --w-e-toolbar-active-bg-color: #f1f1f1;
  --w-e-toolbar-disabled-color: #999;
  --w-e-toolbar-border-color: #e8e8e8;

  /* modal - css vars */
  --w-e-modal-button-bg-color: #fafafa;
  --w-e-modal-button-border-color: #d9d9d9;
}
.theme--dark .article-content {
  /* textarea - css vars */
  --w-e-textarea-bg-color: #333;
  --w-e-textarea-color: var(--theme-dark-color);
  --w-e-textarea-slight-bg-color: #111111;
  --w-e-textarea-slight-border-color: #000000;
  --w-e-textarea-selected-border-color: #9b9797;
}

::v-deep pre>code {
  text-shadow: none !important;
}

.banner:before {
  content: "";
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  height: 100%;
  background-color: rgba(0, 0, 0, 0.5);
}
.article-info i {
  font-size: 14px;
}
.article-info {
  font-size: 14px;
  line-height: 1.9;
  display: inline-block;
}
.article-title-more-3 {
  overflow: hidden;
  text-overflow: ellipsis;
  display: -webkit-box;
  -webkit-line-clamp: 3;
  -webkit-box-orient: vertical;
}
.article-title-more-2 {
  overflow: hidden;
  text-overflow: ellipsis;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
}
@media (min-width: 760px) {
  .banner {
    color: #eee !important;
  }
  .article-info span {
    font-size: 95%;
  }
  .article-info-container {
    position: absolute;
    bottom: 6.25rem;
    padding: 0 8%;
    width: 100%;
    text-align: center;
  }
  .second-line,
  .third-line {
    display: inline;
  }
  .article-title {
    font-size: 35px;
    margin: 20px 0 8px;
  }
  .pagination-post {
    display: flex;
  }
  .post {
    width: 50%;
  }
  .recommend-item {
    position: relative;
    display: inline-block;
    overflow: hidden;
    margin: 3px;
    width: calc(33.333% - 6px);
    height: 200px;
    background: #000;
    vertical-align: bottom;
  }
}
@media (max-width: 759px) {
  .banner {
    color: #eee !important;
    height: 360px;
  }
  .article-info span {
    font-size: 90%;
  }
  .separator:first-child {
    display: none;
  }
  .blog-container {
    margin: 322px 5px 0 5px;
  }
  .article-info-container {
    position: absolute;
    bottom: 1.3rem;
    padding: 0 5%;
    width: 100%;
    color: #eee;
    text-align: left;
  }
  .article-title {
    font-size: 1.5rem;
    margin-bottom: 0.4rem;
  }
  .post {
    width: 100%;
  }
  .pagination-post {
    display: block;
  }
  .recommend-item {
    position: relative;
    display: inline-block;
    overflow: hidden;
    width: calc(100% - 4px);
    height: 150px;
    margin: 2px;
    background: #000;
    vertical-align: bottom;
  }
}
.article-operation {
  display: flex;
  align-items: center;
}
.article-category a {
  color: #fff !important;
}
.tag-container a {
  display: inline-block;
  margin: 0.5rem 0.5rem 0.5rem 0;
  padding: 0 0.75rem;
  width: fit-content;
  border: 1px solid #49b1f5;
  border-radius: 1rem;
  color: #49b1f5 !important;
  font-size: 12px;
  line-height: 2;
}
.tag-container a:hover {
  color: #fff !important;
  background: #49b1f5;
  transition: all 0.5s;
}

.article-copyright {
  position: relative;
  margin-top: 40px;
  margin-bottom: 10px;
  font-size: 0.875rem;
  line-height: 2;
  padding: 0.625rem 1rem;
  border: 1px solid #eee;
}
.article-copyright span {
  color: #49b1f5;
  font-weight: bold;
}
.article-copyright a {
  text-decoration: underline !important;
  color: #99a9bf !important;
}
.article-copyright:before {
  position: absolute;
  top: 0.7rem;
  right: 0.7rem;
  width: 1rem;
  height: 1rem;
  border-radius: 1rem;
  background: #49b1f5;
  content: "";
}
.article-copyright:after {
  position: absolute;
  top: 0.95rem;
  right: 0.95rem;
  width: 0.5rem;
  height: 0.5rem;
  border-radius: 0.5em;
  background: #fff;
  content: "";
}

.article-reward {
  margin-top: 5rem;
  display: flex;
  justify-content: center;
  align-items: center;
}
.reward-btn {
  position: relative;
  display: inline-block;
  width: 100px;
  background: #49b1f5;
  margin: 0 1rem;
  color: #fff !important;
  text-align: center;
  line-height: 36px;
  font-size: 0.875rem;
}
.reward-btn:hover .reward-main {
  display: block;
}
.reward-main {
  display: none;
  position: absolute;
  bottom: 40px;
  left: 0;
  margin: 0;
  padding: 0 0 15px;
  width: 100%;
}
.reward-all {
  display: inline-block;
  margin: 0 0 0 -110px;
  padding: 20px 10px 8px !important;
  width: 320px;
  border-radius: 4px;
  background: #f5f5f5;
}
.reward-all:before {
  position: absolute;
  bottom: -10px;
  left: 0;
  width: 100%;
  height: 20px;
  content: "";
}
.reward-all:after {
  content: "";
  position: absolute;
  right: 0;
  bottom: 2px;
  left: 0;
  margin: 0 auto;
  width: 0;
  height: 0;
  border-top: 13px solid #f5f5f5;
  border-right: 13px solid transparent;
  border-left: 13px solid transparent;
}
.reward-item {
  display: inline-block;
  padding: 0 8px;
  list-style-type: none;
}
.reward-img {
  width: 130px;
  height: 130px;
  display: block;
}
.reward-desc {
  margin: -5px 0;
  color: #858585;
  text-align: center;
}
.like-btn {
  display: inline-block;
  width: 100px;
  background: #969696;
  color: #fff !important;
  text-align: center;
  line-height: 36px;
  font-size: 0.875rem;
}
.like-btn-active {
  display: inline-block;
  width: 100px;
  background: #ec7259;
  color: #fff !important;
  text-align: center;
  line-height: 36px;
  font-size: 0.875rem;
}
.pagination-post {
  margin-top: 40px;
  overflow: hidden;
  width: 100%;
  background: #000;
}
.post {
  position: relative;
  height: 150px;
  overflow: hidden;
}
.post-info {
  position: absolute;
  top: 50%;
  padding: 20px 40px;
  width: 100%;
  transform: translate(0, -50%);
  line-height: 2;
  font-size: 14px;
}
.post-cover {
  position: absolute;
  width: 100%;
  height: 100%;
  opacity: 0.6;
  transition: all 0.6s;
  object-fit: cover;
}
.post a {
  position: relative;
  display: block;
  overflow: hidden;
  height: 150px;
}
.post:hover .post-cover {
  opacity: 0.8;
  transform: scale(1.1);
}
.label {
  font-size: 90%;
  color: #eee;
}
.post-title {
  font-weight: 500;
  color: #fff;
}
hr {
  position: relative;
  margin: 40px auto;
  border: 2px dashed #d2ebfd;
  width: calc(100% - 4px);
}
.full {
  width: 100% !important;
}
.right-container {
  padding: 20px 24px;
  font-size: 14px;
}
.right-title {
  display: flex;
  align-items: center;
  line-height: 2;
  font-size: 16px;
  margin-bottom: 6px;
}
.right-title i {
  font-weight: bold;
}
.recommend-container {
  margin-top: 40px;
}
.recommend-title {
  font-size: 20px;
  line-height: 2;
  font-weight: bold;
  margin-bottom: 5px;
}
.recommend-cover {
  width: 100%;
  height: 100%;
  opacity: 0.4;
  transition: all 0.6s;
  object-fit: cover;
}
.recommend-info {
  line-height: 2;
  color: #fff;
  position: absolute;
  top: 50%;
  padding: 0 20px;
  width: 100%;
  transform: translate(0, -50%);
  text-align: center;
  font-size: 14px;
}
.recommend-date {
  font-size: 90%;
}
.recommend-item:hover .recommend-cover {
  opacity: 0.8;
  transform: scale(1.1);
}
.article-item {
  display: flex;
  align-items: center;
  padding: 6px 0;
}
.article-item:first-child {
  padding-top: 0;
}
.article-item:last-child {
  padding-bottom: 0;
}
.article-item:not(:last-child) {
  border-bottom: 1px dashed #f5f5f5;
}
.article-item img {
  width: 100%;
  height: 100%;
  transition: all 0.6s;
  object-fit: cover;
}
.article-item img:hover {
  transform: scale(1.1);
}
.content {
  flex: 1;
  padding-left: 10px;
  word-break: break-all;
  display: -webkit-box;
  overflow: hidden;
  -webkit-box-orient: vertical;
}
.content-cover {
  width: 58px;
  height: 58px;
  overflow: hidden;
}
.content-title a {
  transition: all 0.2s;
  font-size: 95%;
}
.content-title a:hover {
  color: #2ba1d1;
}
.content-time {
  color: #858585;
  font-size: 85%;
  line-height: 2;
}
</style>

<style lang="scss">
.article-content-bak pre {
  padding: 12px 2px 12px 40px !important;
  border-radius: 5px !important;
  position: relative;
  font-size: 14px !important;
  line-height: 22px !important;
  overflow: hidden !important;
  &:hover .copy-btn {
    display: flex;
    justify-content: center;
    align-items: center;
  }
  code {
    display: block !important;
    margin: 0 10px !important;
    overflow-x: auto !important;
    &::-webkit-scrollbar {
      z-index: 11;
      width: 6px;
    }
    &::-webkit-scrollbar:horizontal {
      height: 6px;
    }
    &::-webkit-scrollbar-thumb {
      border-radius: 5px;
      width: 6px;
      background: #666;
    }
    &::-webkit-scrollbar-corner,
    &::-webkit-scrollbar-track {
      background: #1e1e1e;
    }
    &::-webkit-scrollbar-track-piece {
      background: #1e1e1e;
      width: 6px;
    }
  }
  .line-numbers-rows {
    position: absolute;
    pointer-events: none;
    top: 12px;
    bottom: 12px;
    left: 0;
    font-size: 100%;
    width: 40px;
    text-align: center;
    letter-spacing: -1px;
    border-right: 1px solid rgba(0, 0, 0, 0.66);
    user-select: none;
    counter-reset: linenumber;
    span {
      pointer-events: none;
      display: block;
      counter-increment: linenumber;
      &:before {
        content: counter(linenumber);
        color: #999;
        display: block;
        text-align: center;
      }
    }
  }
  b.name {
    position: absolute;
    top: 7px;
    right: 45px;
    z-index: 1;
    color: #999;
    pointer-events: none;
  }
  .copy-btn {
    position: absolute;
    top: 6px;
    right: 6px;
    z-index: 1;
    color: #ccc;
    background-color: #525252;
    border-radius: 6px;
    display: none;
    font-size: 14px;
    width: 32px;
    height: 24px;
    outline: none;
  }
}
</style>
