<template>
  <div>
    <svg-icon icon-class="web" @click="click" />
  </div>
</template>

<script>
import { mapState } from 'vuex'

export default {
  name: 'Web',
  computed: {
    ...mapState({
      webApi: state => state.settings.webApi
    })
  },
  methods: {
    click() {
      window.open(this.webApi, '_blank')
    }
  }
}
</script>
