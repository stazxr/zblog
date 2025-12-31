<template>
  <elFrame :src="$store.state.api.swaggerApi" />
</template>

<script>
import elFrame from '@/components/Iframe/index'
export default {
  name: 'Swagger',
  components: { elFrame }
}
</script>

<style scoped>

</style>
