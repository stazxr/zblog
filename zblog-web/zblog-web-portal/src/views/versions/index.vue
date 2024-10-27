<template>
  <div>
    <div class="banner" :style="cover">
      <h1 class="banner-title">版本</h1>
    </div>
    <v-card class="blog-container">
      <v-row>
        <v-col md="12" cols="12">
          <v-timeline>
            <v-timeline-item v-for="version in versionList" :key="version.id" color="red lighten-2" large>
              <template v-slot:opposite>
                <span>{{ version['createDate'] }}</span>
              </template>
              <v-card class="elevation-2">
                <v-card-title class="text-h5">
                  {{ version['versionName'] }}
                </v-card-title>
                <v-card-text v-text="version['updateContent']" />
              </v-card>
            </v-timeline-item>
          </v-timeline>
        </v-col>
      </v-row>
    </v-card>
  </div>
</template>

<script>
export default {
  name: 'Version',
  data: function() {
    return {
      versionList: []
    }
  },
  computed: {
    cover() {
      let cover = ''
      this.$store.state.pageList.forEach(item => {
        if (item['pageLabel'] === 'version') {
          cover = item['pageCover']
        }
      })
      return 'background: url(' + cover + ') center center / cover no-repeat'
    }
  },
  created() {
    this.listVersions()
  },
  methods: {
    listVersions() {
      this.$loading.show()
      this.$mapi.portal.queryVersionList().then(({ data }) => {
        this.versionList = data
      }).catch(_ => {
        this.versionList = []
      }).finally(_ => {
        this.$loading.hide()
      })
    }
  }
}
</script>

<style scoped>

</style>
