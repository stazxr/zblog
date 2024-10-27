<template>
  <div>
    <!-- 标签或分类名 -->
    <div class="banner" :style="cover">
      <h1 class="banner-title animated fadeInDown">{{ name }}</h1>
    </div>
    <div class="article-list-wrapper">
      <v-row v-if="articleList.length > 0">
        <v-col v-for="item of articleList" :key="item.id" md="4" cols="12">
          <!-- 文章 -->
          <v-card class="animated zoomIn article-item-card">
            <div class="article-item-cover">
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
            <div class="article-item-info">
              <div>
                <router-link :to="'/articles/' + item.id" class="article-title">
                  {{ item['title'] }}
                </router-link>
              </div>
              <div style="margin-top:0.375rem">
                <svg class="iconfont_svg" aria-hidden="true" style="font-size: 15px;">
                  <use xlink:href="#icon-shijianku" />
                </svg> {{ item['createTime'] }}
                <router-link :to="'/categories/' + item['categoryId']" class="float-right">
                  <svg class="iconfont_svg" aria-hidden="true" style="font-size: 15px;">
                    <use xlink:href="#icon-shangjiaduantubiao_bookmark" />
                  </svg> {{ item['categoryName'] }}
                </router-link>
              </div>
            </div>
            <v-divider />
            <div class="tag-wrapper">
              <router-link v-for="tag of item['tagList']" :key="tag.id" :to="'/tags/' + tag.id" class="tag-btn">
                {{ tag['name'] }}
              </router-link>
            </div>
          </v-card>
        </v-col>
      </v-row>
      <div v-if="!pageLoading && articleList.length === 0" class="category-title">空空如也~</div>

      <!-- 无限加载 -->
      <infinite-loading @infinite="infiniteHandler">
        <div slot="no-results" />
        <div slot="no-more" />
      </infinite-loading>
    </div>
  </div>
</template>

<script>
export default {
  data: function() {
    return {
      noArticleCoverImg: 'https://file.suntaoblog.com/image/no-article-cover.png',
      current: 1,
      size: 12,
      articleList: [],
      categoryCover: '',
      name: '',
      pageLoading: false
    }
  },
  computed: {
    cover() {
      const path = this.$route.path
      if (path.indexOf('/categories') !== -1) {
        // 分类
        if (this.categoryCover !== '') {
          return 'background: url(' + this.categoryCover + ') center center / cover no-repeat'
        }
      }

      // 默认封面
      let cover = ''
      this.$store.state.pageList.forEach(item => {
        if (item['pageLabel'] === 'articles') {
          cover = item['pageCover']
        }
      })
      return 'background: url(' + cover + ') center center / cover no-repeat'
    },
    articleDefaultImg() {
      return this.$store.state.articleDefaultImg
    }
  },
  created() {
    const path = this.$route.path
    if (path.indexOf('/categories') !== -1) {
      this.queryCategoryName()
    } else {
      this.queryTagName()
    }
  },
  methods: {
    getArticleCover(article) {
      if (article['coverImageType'] === 5) {
        return this.noArticleCoverImg
      } else if (article['coverImageType'] === 3) {
        return this.articleDefaultImg
      } else {
        return article['articleImgLinkList'] && article['articleImgLinkList'].length > 0 ? article['articleImgLinkList'][0] : ''
      }
    },
    queryCategoryName() {
      const param = { categoryId: this.$route.params.categoryId }
      this.$mapi.portal.queryCategoryById(param).then(({ data }) => {
        this.name = data ? data['name'] : ''
        this.categoryCover = data ? data['imageUrl'] : ''
      }).catch(_ => {
        this.name = ''
        this.categoryCover = ''
      })
    },
    queryTagName() {
      this.categoryCover = ''
      const param = { tagId: this.$route.params.tagId }
      this.$mapi.portal.queryTagById(param).then(({ data }) => {
        this.name = data ? data.name : ''
      }).catch(_ => {
        this.name = ''
      })
    },
    infiniteHandler($state) {
      const param = {
        page: this.current,
        pageSize: this.size,
        categoryId: this.$route.params.categoryId,
        tagId: this.$route.params.tagId
      }

      this.$loading.show()
      this.pageLoading = true
      this.$mapi.portal.queryArticleList(param).then(({ data }) => {
        if (data.list.length === 0) {
          $state.complete()
        } else {
          this.articleList.push(...data.list)
          if (this.articleList.length === data.total) {
            $state.complete()
          } else {
            this.current++
            $state.loaded()
          }
        }
      }).catch(_ => {
        $state.complete()
        this.$toast({ type: 'error', message: '文章列表加载失败' })
      }).finally(_ => {
        this.$loading.hide()
        this.pageLoading = false
      })
    }
  }
}
</script>

<style scoped>
@media (min-width: 760px) {
  .article-list-wrapper {
    max-width: 1106px;
    margin: 370px auto 1rem auto;
  }
  .article-item-card:hover {
    transition: all 0.3s;
    box-shadow: 0 4px 12px 12px rgba(7, 17, 27, 0.15);
  }
  .article-item-card:not(:hover) {
    transition: all 0.3s;
  }
  .article-item-card:hover .on-hover {
    transition: all 0.6s;
    transform: scale(1.1);
  }
  .article-item-card:not(:hover) .on-hover {
    transition: all 0.6s;
  }
  .article-item-info {
    line-height: 1.7;
    padding: 15px 15px 12px 18px;
    font-size: 15px;
  }
}
@media (max-width: 759px) {
  .article-list-wrapper {
    margin-top: 230px;
    padding: 0 12px;
  }
  .article-item-info {
    line-height: 1.7;
    padding: 15px 15px 12px 18px;
  }
}
.article-item-card {
  border-radius: 8px !important;
  box-shadow: 0 4px 8px 6px rgba(7, 17, 27, 0.06);
}
.article-item-card a {
  transition: all 0.3s;
}
.article-item-cover {
  height: 220px;
  overflow: hidden;
}
.article-item-card a:hover {
  color: #8e8cd8;
}
.tag-wrapper {
  padding: 10px 15px 10px 18px;
}
.tag-wrapper a {
  color: #fff !important;
}
.tag-btn {
  display: inline-block;
  font-size: 0.725rem;
  line-height: 22px;
  height: 22px;
  border-radius: 10px;
  padding: 0 12px !important;
  background: linear-gradient(to right, #bf4643 0%, #6c9d8f 100%);
  opacity: 0.6;
  margin-right: 0.5rem;
}
.article-title {
  overflow: hidden;
  text-overflow: ellipsis;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
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
