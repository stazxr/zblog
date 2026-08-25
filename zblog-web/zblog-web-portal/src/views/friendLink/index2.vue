<template>
  <div>
    <div class="banner" :style="cover">
      <h1 class="banner-title">友人帐</h1>
    </div>
    <v-card class="blog-container" flat>
      <section class="friend-section">
        <div class="section-title">
          <v-icon left>mdi-code-tags</v-icon>
          开源伙伴
        </div>
        <div class="section-desc">
          因开源而相遇，共同成长
        </div>
        <v-row>
          <v-col v-for="item in sourceLinks" :key="item.id" cols="12" sm="6" md="3">
            <v-card class="friend-card" outlined hover>
              <v-avatar size="70">
                <v-img :src="item.logo" contain />
              </v-avatar>
              <div class="friend-name">{{ item.name }}</div>
              <div class="friend-intro">{{ item.intro }}</div>
              <div class="friend-time">加入时间：{{ item.time }}</div>
            </v-card>
          </v-col>
        </v-row>
      </section>
      <section class="friend-section">
        <div class="section-title">
          <v-icon left color="orange">mdi-star</v-icon>
          特别推荐
        </div>
        <div class="section-desc">
          一些值得驻足与分享的优秀站点
        </div>
        <v-row>
          <v-col v-for="item in excellentLinks" :key="item.id" cols="12" sm="6" md="3">
            <v-card class="friend-card" outlined hover>
              <v-avatar size="70">
                <v-img :src="item.logo" contain />
              </v-avatar>
              <div class="friend-name">{{ item.name }}</div>
              <div class="friend-intro">{{ item.intro }}</div>
              <div class="friend-time">加入时间：{{ item.time }}</div>
            </v-card>
          </v-col>
        </v-row>
      </section>
      <section class="friend-section">
        <div class="section-title">
          <v-icon left>mdi-link</v-icon>
          同行友站
        </div>
        <div class="section-desc">
          感谢相遇，愿我们在各自的路上共同前行
        </div>
        <v-row>
          <v-col v-for="item in normalLinks" :key="item.id" cols="12" sm="6" md="3">
            <v-card class="friend-card" outlined hover>
              <v-avatar size="70">
                <v-img :src="item.logo" contain />
              </v-avatar>
              <div class="friend-name">{{ item.name }}</div>
              <div class="friend-intro">{{ item.intro }}</div>
              <div class="friend-time">加入时间：{{ item.time }}</div>
            </v-card>
          </v-col>
        </v-row>
      </section>
      <div class="apply-area">
        <div class="apply-text">
          期待与你的网站成为伙伴
        </div>
        <v-btn color="primary" rounded>
          <v-icon left>mdi-link-plus</v-icon>
          申请加入
        </v-btn>
        <v-btn color="primary" rounded>
          <v-icon left>mdi-link-plus</v-icon>
          建议反馈
        </v-btn>
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
      sourceLinks: [
        {
          id: 1,
          name: '技术小站',
          logo: 'https://cdn.jsdelivr.net/gh/twitter/twemoji/assets/svg/1f680.svg',
          intro: '基于 SunTao Blog 开源源码搭建',
          time: '2026-08-01'
        },
        {
          id: 2,
          name: '程序员日记',
          logo: 'https://cdn.jsdelivr.net/gh/twitter/twemoji/assets/svg/1f4bb.svg',
          intro: '记录开发中的点点滴滴',
          time: '2026-08-10'
        }
      ],
      excellentLinks: [
        {
          id: 1,
          name: 'Vue技术社区',
          logo: 'https://cdn.jsdelivr.net/gh/twitter/twemoji/assets/svg/1f49a.svg',
          intro: '分享前端技术与实践',
          time: '2026-07-20'
        },
        {
          id: 2,
          name: 'Java成长之路',
          logo: 'https://cdn.jsdelivr.net/gh/twitter/twemoji/assets/svg/2615.svg',
          intro: '专注 Java 后端技术分享',
          time: '2026-07-15'
        },
        {
          id: 3,
          name: '代码人生',
          logo: 'https://cdn.jsdelivr.net/gh/twitter/twemoji/assets/svg/1f4d6.svg',
          intro: '记录技术学习过程',
          time: '2026-06-18'
        },
        {
          id: 4,
          name: '开发者空间',
          logo: 'https://cdn.jsdelivr.net/gh/twitter/twemoji/assets/svg/1f310.svg',
          intro: '探索互联网技术',
          time: '2026-05-22'
        }
      ],
      normalLinks: [
        {
          id: 1,
          name: '小明博客',
          logo: 'https://cdn.jsdelivr.net/gh/twitter/twemoji/assets/svg/1f60a.svg',
          intro: '分享生活与技术',
          time: '2026-04-10'
        },
        {
          id: 2,
          name: '阿强笔记',
          logo: 'https://cdn.jsdelivr.net/gh/twitter/twemoji/assets/svg/1f4dd.svg',
          intro: '个人学习记录',
          time: '2026-03-12'
        },
        {
          id: 3,
          name: '前端小屋',
          logo: 'https://cdn.jsdelivr.net/gh/twitter/twemoji/assets/svg/1f3e0.svg',
          intro: '前端开发分享',
          time: '2026-02-20'
        },
        {
          id: 4,
          name: '云端日志',
          logo: 'https://cdn.jsdelivr.net/gh/twitter/twemoji/assets/svg/2601.svg',
          intro: '云计算技术交流',
          time: '2026-01-15'
        }
      ]
    }
  },
  created() {
    this.cover = getPageRandomCover(this.$store.state.pages, 'FriendLink')
  }
}
</script>
<style scoped>
.blog-container{
  padding:30px;
}
.friend-section{
  margin-bottom:45px;
}
.section-title{
  display:flex;
  align-items:center;
  font-size:22px;
  font-weight:600;
  margin-bottom:10px;
}
.section-desc{
  color:#888;
  font-size:14px;
  margin-bottom:25px;
}
.friend-card{
  height:190px;
  display:flex;
  flex-direction:column;
  align-items:center;
  justify-content:center;
  cursor:pointer;
  transition:.3s;
}
.friend-card:hover{
  transform:translateY(-5px);
}
.friend-name{
  margin-top:15px;
  font-size:17px;
  font-weight:600;
}
.friend-intro{
  margin-top:8px;
  color:#666;
  font-size:14px;
}
.friend-time{
  margin-top:8px;
  color:#999;
  font-size:12px;
}
.apply-area{
  text-align:center;
  padding:30px 0;
}
.apply-text{
  color:#888;
  margin-bottom:15px;
}
@media(max-width:600px){
  .blog-container{
    padding:15px;
  }
}
</style>
