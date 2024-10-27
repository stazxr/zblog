<template>
  <div>
    <!-- banner -->
    <div class="banner" :style="cover">
      <h1 class="banner-title">标签</h1>
    </div>
    <!-- 标签列表 -->
    <v-card class="blog-container">
      <!-- <div v-if="count > 0" class="tag-cloud-title">标签 - {{ count }}</div>
      <div v-else class="tag-cloud-title">暂无内容</div> -->
      <div v-show="tagList.length > 0" class="tag-cloud">
        <router-link v-for="(item, index) of tagList" :key="item.id" :style="{ 'font-size': Math.floor(Math.random() * 10) + 18 + 'px', color: color[index % color.length] }" :to="'/tags/' + item.id">
          {{ item.name }}
        </router-link>
      </div>
      <div v-if="!pageLoading && count === 0" class="category-title">空空如也~</div>
    </v-card>
  </div>
</template>

<script>
export default {
  data: function() {
    return {
      color: ['#86D4FF', '#FF8F6C', '#2CF263', '#9FA8F7', '#1274FF', '#E6613D', '#FFC629', '#FFAB2E', '#F78289', '#FF6C96',
        '#45BFD4', '#4E31FF', '#31FBFB', '#86D4FF', '#BF8AFD', '#FFF500', '#DE58FF', '#72ED7C', '#0BEEB8', '#931CFF',
        '#3D25F2', '#F995C8', '#FBE9B4', '#FF4AB6'
      ],
      tagList: [],
      pageLoading: false
    }
  },
  computed: {
    cover() {
      let cover = ''
      this.$store.state.pageList.forEach(item => {
        if (item['pageLabel'] === 'tag') {
          cover = item['pageCover']
        }
      })
      return 'background: url(' + cover + ') center center / cover no-repeat'
    },
    count() {
      return this.tagList.length
    }
  },
  created() {
    this.listTags()
  },
  methods: {
    listTags() {
      this.$loading.show()
      this.pageLoading = true
      this.$mapi.portal.queryTagList().then(({ data }) => {
        this.tagList = data
      }).catch(_ => {
        this.tagList = []
      }).finally(_ => {
        this.$loading.hide()
        this.pageLoading = false
      })
    }
  }
}
</script>

<style scoped>
.tag-cloud-title {
  line-height: 2;
  font-size: 36px;
  text-align: center;
}
@media (max-width: 759px) {
  .tag-cloud-title {
    font-size: 25px;
  }
}
.tag-cloud {
  text-align: center;
}
.tag-cloud a {
  color: #616161;
  display: inline-block;
  text-decoration: none;
  padding: 0 8px;
  line-height: 2;
  transition: all 0.3s;
}
.tag-cloud a:hover {
  color: #03a9f4 !important;
  transform: scale(1.1);
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
