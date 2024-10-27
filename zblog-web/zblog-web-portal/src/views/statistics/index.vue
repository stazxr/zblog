<template>
  <div>
    <div class="banner" :style="cover">
      <h1 class="banner-title">网站统计</h1>
    </div>
    <v-card class="blog-container">
      <GithubCalendar />
    </v-card>
  </div>
</template>

<script>
import GithubCalendar from '../../components/git-calerdar/GithubCalendar.vue'
export default {
  name: 'Statistics',
  components: {
    GithubCalendar
  },
  computed: {
    cover() {
      let cover = ''
      this.$store.state.pageList.forEach(item => {
        if (item['pageLabel'] === 'statistics') {
          cover = item['pageCover']
        }
      })
      return 'background: url(' + cover + ') center center / cover no-repeat'
    }
  }
}
</script>

<style scoped>

</style>
