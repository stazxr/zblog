<template>
  <div>
    <!-- banner -->
    <div class="banner" :style="cover">
      <h1 class="banner-title">分类</h1>
    </div>
    <!-- 分类列表 -->
    <v-card class="blog-container">
      <div v-if="count > 0" class="category-title">分类 - {{ count }}</div>
      <ul class="category-list">
        <li v-for="item of categoryList" :key="item.id" class="category-list-item">
          <router-link :to="'/categories/' + item.id">
            {{ item['name'] }}
            <span class="category-count">({{ item['articleCount'] }})</span>
          </router-link>
        </li>
      </ul>
      <div v-if="!pageLoading && count === 0" class="category-title">空空如也~</div>
    </v-card>
  </div>
</template>

<script>
export default {
  data: function() {
    return {
      categoryList: [],
      pageLoading: false
    }
  },
  computed: {
    cover() {
      let cover = ''
      this.$store.state.pageList.forEach(item => {
        if (item['pageLabel'] === 'category') {
          cover = item['pageCover']
        }
      })
      return 'background: url(' + cover + ') center center / cover no-repeat'
    },
    count() {
      return this.categoryList.length
    }
  },
  created() {
    this.listCategories()
  },
  methods: {
    listCategories() {
      this.$loading.show()
      this.pageLoading = true
      this.$mapi.portal.queryCategoryList().then(({ data }) => {
        this.categoryList = data
      }).catch(_ => {
        this.categoryList = []
      }).finally(_ => {
        this.$loading.hide()
        this.pageLoading = false
      })
    }
  }
}
</script>

<style scoped>
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
.category-list {
  margin: 0 1.8rem;
  list-style: none;
}
.category-list-item {
  padding: 8px 1.8rem 8px 0;
}
.category-list-item:before {
  display: inline-block;
  position: relative;
  left: -0.75rem;
  width: 12px;
  height: 12px;
  border: 0.2rem solid #49b1f5;
  border-radius: 50%;
  background: #fff;
  content: "";
  transition-duration: 0.3s;
}
.category-list-item:hover:before {
  border: 0.2rem solid #ff7242;
}
.category-list-item a:hover {
  transition: all 0.3s;
  color: #8e8cd8;
}
.category-list-item a:not(:hover) {
  transition: all 0.3s;
}
.category-count {
  margin-left: 0.5rem;
  font-size: 0.75rem;
  color: #858585;
}
</style>
