<template>
  <div>
    <div class="banner" :style="cover">
      <h1 class="banner-title">专栏</h1>
    </div>
    <v-card class="blog-container">
      <v-row v-if="columnList.length > 0">
        <v-col v-for="item of columnList" :key="item.id" :md="6">
          <div class="album-item">
            <v-img class="album-cover" :src="item['imageUrl']" />
            <router-link :to="'/columns/' + item.id" class="album-wrapper">
              <div class="album-name">{{ item['name'] }}</div>
              <div class="album-desc">{{ item['desc'] }}</div>
            </router-link>
          </div>
        </v-col>
      </v-row>
      <v-row v-if="!pageLoading && columnList.length === 0">
        <h2 style="margin: 0 auto;">空空如也~</h2>
      </v-row>
      <infinite-loading @infinite="infiniteHandler">
        <div slot="no-results" />
        <div slot="no-more" />
      </infinite-loading>
    </v-card>
  </div>
</template>

<script>
export default {
  name: 'Column',
  data: function() {
    return {
      current: 1,
      columnList: [],
      pageLoading: false
    }
  },
  computed: {
    cover() {
      let cover = ''
      this.$store.state.pageList.forEach(item => {
        if (item['pageLabel'] === 'column') {
          cover = item['pageCover']
        }
      })
      return 'background: url(' + cover + ') center center / cover no-repeat'
    },
    count() {
      return this.columnList.length
    }
  },
  methods: {
    infiniteHandler($state) {
      this.$loading.show()
      this.pageLoading = true
      this.$mapi.portal.queryColumnList({ current: this.current }).then(({ data }) => {
        if (data.list.length === 0) {
          $state.complete()
        } else {
          this.columnList.push(...data.list)
          this.current++
          $state.loaded()
        }
      }).catch(_ => {
        $state.complete()
        this.$toast({ type: 'error', message: '专栏列表加载失败' })
      }).finally(_ => {
        this.$loading.hide()
        this.pageLoading = false
      })
    }
  }
}
</script>

<style scoped>
.album-item {
  overflow: hidden;
  position: relative;
  cursor: pointer;
  background: #000;
  border-radius: 0.5rem !important;
}
.album-cover {
  position: relative;
  max-width: none;
  width: calc(100% + 1.25rem);
  height: 250px;
  opacity: 0.8;
  transition: opacity 0.35s, transform 0.35s;
  transform: translate3d(-10px, 0, 0);
  object-fit: cover;
}
.album-wrapper {
  position: absolute;
  top: 0;
  bottom: 0;
  left: 0;
  right: 0;
  padding: 1.8rem 2rem;
  color: #fff !important;
}
.album-item:hover .album-cover {
  transform: translate3d(0, 0, 0);
  opacity: 0.4;
}
.album-item:hover .album-name:after {
  transform: translate3d(0, 0, 0);
}
.album-item:hover .album-desc {
  opacity: 1;
  filter: none;
  transform: translate3d(0, 0, 0);
}
.album-name {
  font-weight: bold;
  font-size: 1.25rem;
  overflow: hidden;
  padding: 0.7rem 0;
  position: relative;
}
.album-name:after {
  position: absolute;
  bottom: 0;
  left: 0;
  width: 100%;
  height: 2px;
  background: #fff;
  content: "";
  transition: transform 0.35s;
  transform: translate3d(-101%, 0, 0);
}
.album-desc {
  margin: 0;
  padding: 0.4rem 0 0;
  line-height: 1.5;
  opacity: 0;
  transition: opacity 0.35s, transform 0.35s;
  transform: translate3d(100%, 0, 0);
  word-break: break-all;
  text-overflow: ellipsis;
  display: -webkit-box;
  -webkit-box-orient: vertical;
  -webkit-line-clamp: 5;
  overflow: hidden;
}
</style>
