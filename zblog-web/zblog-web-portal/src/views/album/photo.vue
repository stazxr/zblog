<template>
  <div>
    <!-- banner -->
    <div class="banner" :style="cover">
      <h1 class="banner-title">{{ photoAlbumName }}</h1>
    </div>
    <!-- 相册列表 -->
    <v-card class="blog-container">
      <div class="photo-wrap">
        <img v-for="(item, index) of photoList" :key="item.id" class="photo" :src="item['photoLink']" alt="" @click="preview(index)">
      </div>
      <div v-if="!pageLoading && photoList.length === 0" class="category-title">空空如也~</div>
      <!-- 无限加载 -->
      <infinite-loading @infinite="infiniteHandler">
        <div slot="no-more" />
        <div slot="no-results" />
      </infinite-loading>
    </v-card>
  </div>
</template>

<script>
export default {
  data: function() {
    return {
      photoAlbumName: '',
      photoAlbumCover: '',
      photoList: [],
      photoLinkList: [],
      current: 1,
      size: 10,
      pageLoading: false
    }
  },
  computed: {
    cover() {
      return (
        'background: url(' + this.photoAlbumCover + ') center center / cover no-repeat'
      )
    }
  },
  watch: {
    photoList() {
      this.photoLinkList = []
      this.photoList.forEach(item => {
        this.photoLinkList.push(item['photoLink'])
      })
    }
  },
  methods: {
    preview(index) {
      this.$imagePreview({
        images: this.photoLinkList,
        index: index
      })
    },
    infiniteHandler($state) {
      const param = {
        page: this.current,
        pageSize: this.size,
        albumId: this.$route.params.albumId
      }

      this.$loading.show()
      this.pageLoading = true
      this.$mapi.portal.queryAlbumPhotoList(param).then(({ data }) => {
        this.photoAlbumName = data.photoAlbumName
        this.photoAlbumCover = data.photoAlbumCover
        if (data.list.length === 0) {
          $state.complete()
        } else {
          this.photoList.push(...data.list)
          this.current++
          $state.loaded()
        }
      }).catch(_ => {
        this.$toast({ type: 'error', message: '照片列表查询失败' })
        $state.complete()
      }).finally(_ => {
        this.$loading.hide()
        this.pageLoading = false
      })
    }
  }
}
</script>

<style scoped>
.photo-wrap {
  display: flex;
  flex-wrap: wrap;
}
.photo {
  margin: 3px;
  cursor: pointer;
  flex-grow: 1;
  object-fit: cover;
  height: 200px;
}
.photo-wrap::after {
  content: "";
  display: block;
  flex-grow: 9999;
}
@media (max-width: 759px) {
  .photo {
    width: 100%;
  }
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
