<template>
  <div>
    <div class="banner" :style="cover">
      <h1 class="banner-title">友人帐</h1>
    </div>
    <v-card class="blog-container friend-link-container" flat>
      <section class="my-site-section">
        <div class="section-header">
          <div class="section-title">
            <v-icon left size="22">
              mdi-card-account-details-outline
            </v-icon>
            <span>关于本站</span>
          </div>
          <div class="section-desc">
            如果你准备与本站交换友链，可以先了解一下本站
          </div>
        </div>
        <v-card class="my-site-card" flat>
          <div class="my-site-main">
            <div class="my-site-cover">
              <v-img :src="websiteConfig.websiteCover" alt="" cover class="my-site-cover-image">
                <div class="my-site-cover-mask" />
              </v-img>
            </div>
            <div class="my-site-content">
              <div class="my-site-profile">
                <v-avatar size="72" class="my-site-avatar">
                  <v-img :src="websiteConfig.websiteAvatar" alt="" />
                </v-avatar>
                <div class="my-site-title">
                  <div class="my-site-name">
                    {{ websiteConfig.websiteName }}
                  </div>
                  <div class="my-site-url">
                    {{ websiteLinks['WEBSITE_PORTAL_URL'] }}
                  </div>
                </div>
              </div>
              <div class="my-site-description">
                {{ websiteConfig.websiteIntro }}
              </div>
            </div>
          </div>
          <div class="my-site-info">
            <!-- 网站名称 -->
            <div class="site-info-item">
              <div class="site-info-label">
                网站名称
              </div>
              <div class="site-info-value">
                {{ websiteConfig.websiteName }}
              </div>
            </div>

            <!-- 网站地址 -->
            <div class="site-info-item">
              <div class="site-info-label">
                网站地址
              </div>
              <div class="site-info-value site-info-url">
                {{ websiteLinks['WEBSITE_PORTAL_URL'] }}
              </div>
            </div>

            <!-- 头像地址 -->
            <div class="site-info-item">
              <div class="site-info-label">
                头像地址
              </div>
              <div class="site-info-value site-info-url">
                {{ websiteConfig.websiteAvatar }}
              </div>
            </div>

            <!-- 网站简介 -->
            <div class="site-info-item site-info-intro-item">
              <div class="site-info-label">
                网站简介
              </div>
              <div class="site-info-value">
                {{ websiteConfig.websiteIntro }}
              </div>
            </div>

            <!-- 封面地址 -->
            <div class="site-info-item">
              <div class="site-info-label">
                封面地址
              </div>
              <div class="site-info-value site-info-url">
                {{ websiteConfig.websiteCover }}
              </div>
            </div>
          </div>
        </v-card>
      </section>

      <section class="apply-section">
        <div class="section-header">
          <div class="section-title">
            <v-icon left size="22">
              mdi-link-plus
            </v-icon>
            <span>申请方式</span>
          </div>
          <div class="section-desc">
            如果你也有一个正在用心经营的网站，欢迎与本站交换友链
          </div>
        </div>
        <v-card class="apply-card" flat>
          <div class="apply-item">
            <div class="apply-item-icon">
              <v-icon size="24">
                mdi-check-circle-outline
              </v-icon>
            </div>
            <div class="apply-item-content">
              <div class="apply-item-title">
                申请条件
              </div>
              <div class="apply-item-desc">
                网站内容健康，能够正常访问，并保持长期维护
              </div>
            </div>
          </div>
          <div class="apply-divider" />
          <div class="apply-item">
            <div class="apply-item-icon">
              <v-icon size="24">
                mdi-email-outline
              </v-icon>
            </div>
            <div class="apply-item-content">
              <div class="apply-item-title">
                联系方式
              </div>
              <div class="apply-item-desc">
                通过邮件，站内留言联系我，提供你的友链信息或点击申请友链按钮
              </div>
            </div>
          </div>
          <div class="apply-divider" />
          <div class="apply-action">
            <v-btn color="primary" rounded depressed @click="applyFriendLink">
              <v-icon left size="18"> mdi-link-plus</v-icon>
              申请友链
            </v-btn>
          </div>
        </v-card>
      </section>
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
    /**
     * 网站配置
     */
    websiteConfig() {
      return this.$store.state.websiteConfig
    },
    /**
     * 网站链接配置
     */
    websiteLinks() {
      return this.$store.state.links
    },
    /**
     * 友链卡片配置
     */
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
      if (!item || !item.url) {
        return
      }

      const link = document.createElement('a')
      link.href = item.url
      link.target = '_blank'
      link.rel = item.allowFollow === false ? 'noopener noreferrer nofollow' : 'noopener noreferrer'
      link.click()
    },
    /**
     * 申请友链
     */
    applyFriendLink() {
      this.$store.state.applyFriendLinkFlag = true
    }
  }
}
</script>

<style scoped> .friend-link-container {
  padding-bottom: 30px;
}

.my-site-section {
  margin-bottom: 45px;
}

.section-header {
  margin-bottom: 20px;
}

.section-title {
  display: flex;
  align-items: center;
  color: #303133;
  font-size: 21px;
  font-weight: 600;
  line-height: 30px;
}

.section-desc {
  margin-top: 4px;
  color: #999;
  font-size: 13px;
  line-height: 22px;
}

.my-site-card {
  overflow: hidden;
  border-radius: 14px;
  background: #fff;
  box-shadow: 0 2px 12px rgba(0, 0, 0, .06);
}

.my-site-main {
  display: flex;
  min-height: 190px;
}

.my-site-cover {
  position: relative;
  width: 34%;
  min-width: 280px;
  overflow: hidden;
}

.my-site-cover-image {
  width: 100%;
  height: 100%;
}

.my-site-cover-mask {
  position: absolute;
  top: 0;
  right: 0;
  bottom: 0;
  left: 0;
  background: linear-gradient(90deg, rgba(0, 0, 0, .02), rgba(0, 0, 0, .18));
}

.my-site-content {
  flex: 1;
  padding: 28px 32px;
}

.my-site-profile {
  display: flex;
  align-items: center;
}

.my-site-avatar {
  flex-shrink: 0;
  box-shadow: 0 2px 8px rgba(0, 0, 0, .12);
}

.my-site-title {
  min-width: 0;
  margin-left: 16px;
}

.my-site-name {
  overflow: hidden;
  color: #222;
  font-size: 20px;
  font-weight: 600;
  line-height: 28px;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.my-site-url {
  margin-top: 3px;
  overflow: hidden;
  color: #999;
  font-size: 13px;
  line-height: 20px;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.my-site-description {
  max-width: 720px;
  margin-top: 22px;
  color: #666;
  font-size: 14px;
  line-height: 24px;
}

.my-site-info {
  padding: 20px 24px;
  border-top: 1px solid rgba(0, 0, 0, 0.06);
}

.site-info-item {
  display: flex;
  align-items: flex-start;
  padding: 10px 0;
  line-height: 1.6;
}

.site-info-label {
  flex-shrink: 0;
  width: 90px;
  color: #999;
}

.site-info-value {
  flex: 1;
  min-width: 0;
  color: #333;
  word-break: break-all;
}

.site-info-url {
  color: #409eff;
  word-break: break-all;
}

.site-info-intro-item .site-info-value {
  white-space: pre-line;
}

.apply-section {
  margin-bottom: 50px;
}

.apply-card {
  display: flex;
  align-items: center;
  padding: 24px 28px;
  border: 1px solid #f0f0f0;
  border-radius: 12px;
  background: #fafafa;
}

.apply-item {
  display: flex;
  align-items: center;
  flex: 1;
  min-width: 0;
}

.apply-item-icon {
  display: flex;
  align-items: center;
  justify-content: center;
  width: 46px;
  height: 46px;
  flex-shrink: 0;
  border-radius: 50%;
  background: rgba(25, 118, 210, .08);
  color: #1976d2;
}

.apply-item-content {
  min-width: 0;
  margin-left: 14px;
}

.apply-item-title {
  color: #333;
  font-size: 15px;
  font-weight: 600;
  line-height: 22px;
}

.apply-item-desc {
  margin-top: 3px;
  color: #888;
  font-size: 12px;
  line-height: 20px;
}

.apply-divider {
  width: 1px;
  height: 42px;
  margin: 0 25px;
  background: #e8e8e8;
}

.apply-action {
  margin-left: 25px;
  flex-shrink: 0;
}

.friend-section {
  margin-bottom: 50px;
}

.friend-card {
  height: 280px;
  overflow: hidden;
  margin-bottom: 5px;
  border-radius: 12px;
  background: #fff;
  box-shadow: 0 2px 8px rgba(0, 0, 0, .06);
  cursor: pointer;
}

.friend-card:hover {
  box-shadow: 0 2px 8px rgba(0, 0, 0, .06) !important;
}

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

.friend-card:hover .friend-image {
  transform: scale(1.06);
}

.friend-cover-mask {
  position: absolute;
  right: 0;
  bottom: 0;
  left: 0;
  height: 45px;
  pointer-events: none;
  background: linear-gradient(to bottom, transparent, rgba(0, 0, 0, .08));
}

.friend-content {
  display: flex;
  flex-direction: column;
  box-sizing: border-box;
  height: 105px;
  padding: 11px 18px 10px;
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

.friend-time {
  display: flex;
  align-items: center;
  gap: 4px;
  margin-top: auto;
  color: #999;
  font-size: 11px;
  line-height: 16px;
}

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

@media (min-width: 760px) {
  .friend-link-container {
    max-width: 1320px !important;
  }
}

@media (max-width: 900px) {
  .my-site-main {
    flex-direction: column;
  }

  .my-site-cover {
    width: 100%;
    height: 180px;
  }

  .my-site-info {
    grid-template-columns: repeat(2, 1fr);
  }

  .site-info-item:nth-child(2) {
    border-right: 0;
  }

  .site-info-item:nth-child(3), .site-info-item:nth-child(4), .site-info-item:nth-child(5) {
    border-top: 1px solid #f2f2f2;
  }

  .site-info-cover-item {
    grid-column: span 2;
  }

  .apply-card {
    flex-wrap: wrap;
  }

  .apply-divider {
    margin: 0 15px;
  }
}

@media (max-width: 600px) {
  .my-site-section {
    margin-bottom: 38px;
  }

  .section-title {
    font-size: 19px;
  }

  .section-desc {
    font-size: 12px;
  }

  .my-site-cover {
    height: 145px;
    min-width: 0;
  }

  .my-site-content {
    padding: 20px;
  }

  .my-site-name {
    font-size: 18px;
  }

  .my-site-description {
    margin-top: 17px;
    font-size: 13px;
  }

  .my-site-info {
    padding: 16px;
  }

  .site-info-item {
    flex-direction: column;
    padding: 8px 0;
  }

  .site-info-label {
    width: auto;
    margin-bottom: 2px;
  }

  .apply-card {
    display: block;
    padding: 20px;
  }

  .apply-item {
    align-items: flex-start;
  }

  .apply-divider {
    width: 100%;
    height: 1px;
    margin: 18px 0;
  }

  .apply-action {
    margin-top: 20px;
    margin-left: 0;
  }

  .apply-action .v-btn {
    width: 100%;
  }

  .friend-section {
    margin-bottom: 38px;
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
}
</style>
