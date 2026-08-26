<template>
  <div>
    <div class="banner" :style="cover">
      <h1 class="banner-title">友人帐</h1>
    </div>
    <v-card class="blog-container friend-link-container" flat>
      <section
        v-for="section in friendSections"
        v-show="section.list && section.list.length"
        :key="section.type"
        class="friend-section"
      >
        <div class="section-header">
          <div class="section-title">
            <v-icon v-if="section.icon" left :color="section.iconColor" size="22">
              {{ section.icon }}
            </v-icon>
            <span>{{ section.title }}</span>
          </div>
          <div class="section-desc">
            {{ section.desc }}
          </div>
        </div>
        <v-row>
          <v-col v-for="item in section.list" :key="item.id" cols="12" sm="6" md="3">
            <v-card class="friend-card" flat @click="openLink(item)">
              <!-- 网站封面 -->
              <div class="friend-cover">
                <v-img :src="item.logo" :alt="item.name" aspect-ratio="1.8" cover class="friend-image" />
                <!-- 图片遮罩 -->
                <div class="friend-cover-mask" />
              </div>

              <!-- 信息 -->
              <div class="friend-content">
                <div class="friend-name">
                  {{ item.name }}
                </div>
                <div class="friend-description">
                  {{ item.description || '这个站点暂时没有留下介绍' }}
                </div>
                <div v-if="item.createTime" class="friend-time">
                  <v-icon size="14">mdi-calendar-outline</v-icon>
                  <span>{{ item.createTime }}</span>
                </div>
              </div>
            </v-card>
          </v-col>
        </v-row>
      </section>

      <!-- 没有友链 -->
      <div v-if="!hasFriendLinks" class="empty-area">
        <v-icon size="50" color="grey lighten-1">
          mdi-link-variant-off
        </v-icon>
        <div class="empty-text">
          暂时还没有友链
        </div>
        <div class="empty-desc">
          期待与你的网站成为伙伴
        </div>
      </div>

      <!-- 申请友链 -->
      <div class="apply-area">
        <div class="apply-text">
          期待与你的网站成为伙伴
        </div>
        <div class="apply-actions">
          <v-btn color="primary" rounded depressed>
            <v-icon left>mdi-link-plus</v-icon> 申请加入
          </v-btn>
          <v-btn color="primary" outlined rounded>
            <v-icon left>mdi-message-text-outline</v-icon> 建议反馈
          </v-btn>
        </div>
      </div>
    </v-card>
  </div>
</template>

<script>
import { getPageRandomCover } from '@/utils/theme'
export default {
  name: 'FriendLink',
  data() {
    return {
      cover: null,
      openLinks: [],
      featuredLinks: [],
      normalLinks: []
    }
  },
  computed: {
    friendSections() {
      return [
        {
          type: 'open',
          title: '开源伙伴',
          desc: '因开源而相遇，共同成长',
          icon: 'mdi-code-tags',
          iconColor: '',
          list: this.openLinks
        },
        {
          type: 'featured',
          title: '特别推荐',
          desc: '一些值得驻足与分享的优秀站点',
          icon: 'mdi-star',
          iconColor: 'orange',
          list: this.featuredLinks
        },
        {
          type: 'normal',
          title: '同行友站',
          desc: '感谢相遇，愿我们在各自的路上共同前行',
          icon: 'mdi-link',
          iconColor: '',
          list: this.normalLinks
        }
      ]
    },
    hasFriendLinks() {
      return (
        this.openLinks.length > 0 ||
        this.featuredLinks.length > 0 ||
        this.normalLinks.length > 0
      )
    }
  },
  created() {
    this.cover = getPageRandomCover(this.$store.state.pages, 'FriendLink')
  },
  mounted() {
    this.$nextTick(() => {
      this.initData()
    })
  },
  methods: {
    /**
     * 加载友链
     */
    initData() {
      this.$mapi.portal.queryFriendLinkList().then(({ data }) => {
        this.openLinks = data['1'] || []
        this.featuredLinks = data['2'] || []
        this.normalLinks = data['3'] || []
      }).catch(e => {
        this.openLinks = []
        this.featuredLinks = []
        this.normalLinks = []
        console.log('load friend link error', e)
        this.$toast({ type: 'error', message: '友链加载失败...' })
      })
    },
    /**
     * 打开友链
     */
    openLink(item) {
      if (!item.url) {
        return
      }

      window.open(item.url, '_blank')
    }
  }
}
</script>

<style scoped>
.friend-section {
  margin-bottom: 50px;
}

.section-header {
  margin-bottom: 22px;
}

.section-title {
  display: flex;
  align-items: center;
  font-size: 21px;
  font-weight: 600;
  line-height: 30px;
  color: #303133;
}

.section-desc {
  margin-top: 4px;
  color: #999;
  font-size: 13px;
  line-height: 22px;
}

/* ==================== 友链卡片 ==================== */

.friend-card {
  height: 280px;
  overflow: hidden;
  border-radius: 12px;
  margin-bottom: 5px;
  background: #fff;
  box-shadow: 0 2px 8px rgba(0, 0, 0, .06);
  cursor: pointer;
}

/* 覆盖全局 v-card hover 阴影效果 */
.friend-card:hover {
  box-shadow: 0 2px 8px rgba(0, 0, 0, .06) !important;
}

/* ==================== 网站封面 ==================== */

.friend-cover {
  position: relative;
  width: 100%;
  height: 175px;
  overflow: hidden;
  background: #f5f5f5;
}

.friend-image {
  width: 100%;
  height: 100%;
  transition: transform .5s ease;
}

/* 只有鼠标悬浮图片区域时才放大图片 */
.friend-cover:hover .friend-image {
  transform: scale(1.06);
}

/* 图片底部渐变遮罩 */
.friend-cover-mask {
  position: absolute;
  left: 0;
  right: 0;
  bottom: 0;
  height: 45px;
  pointer-events: none;
  background: linear-gradient(
    to bottom,
    transparent,
    rgba(0, 0, 0, .08)
  );
}

/* ==================== 网站信息 ==================== */

.friend-content {
  box-sizing: border-box;
  height: 105px;
  padding: 11px 18px 10px;
  display: flex;
  flex-direction: column;
}

.friend-name {
  overflow: hidden;
  color: #222;
  font-size: 16px;
  font-weight: 600;
  line-height: 22px;
  text-overflow: ellipsis;
  white-space: nowrap;
}

/* 网站简介：固定预留两行空间 */
.friend-description {
  min-height: 38px;
  margin-top: 4px;
  padding-right: 5px;
  overflow: hidden;
  color: #666;
  font-size: 13px;
  line-height: 19px;

  display: -webkit-box;
  -webkit-box-orient: vertical;
  -webkit-line-clamp: 2;
}

/* 时间始终位于内容区域底部 */
.friend-time {
  margin-top: auto;
  display: flex;
  align-items: center;
  gap: 4px;
  color: #999;
  font-size: 11px;
  line-height: 16px;
}

/* ==================== 空状态 ==================== */

.empty-area {
  padding: 80px 20px;
  text-align: center;
}

.empty-text {
  margin-top: 15px;
  color: #666;
  font-size: 16px;
}

.empty-desc {
  margin-top: 8px;
  color: #999;
  font-size: 13px;
}

/* ==================== 申请友链 ==================== */

.apply-area {
  margin-top: 10px;
  padding: 35px 0 20px;
  text-align: center;
  border-top: 1px solid #eee;
}

.apply-text {
  margin-bottom: 16px;
  color: #999;
  font-size: 14px;
}

.apply-actions {
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 12px;
}

/* ==================== PC ==================== */

@media (min-width: 760px) {
  .friend-link-container {
    max-width: 1320px !important;
  }
}

/* ==================== 手机端 ==================== */

@media (max-width: 600px) {
  .friend-section {
    margin-bottom: 38px;
  }

  .section-title {
    font-size: 19px;
  }

  .section-desc {
    font-size: 12px;
  }

  .friend-card {
    height: 270px;
  }

  .friend-cover {
    height: 165px;
  }

  .friend-content {
    height: 105px;
    padding: 11px 15px 10px;
  }

  .friend-time {
    font-size: 11px;
  }

  .apply-actions {
    flex-direction: column;
  }

  .apply-actions .v-btn {
    width: 150px;
  }
}
</style>
