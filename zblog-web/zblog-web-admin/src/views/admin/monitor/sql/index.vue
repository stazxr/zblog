<template>
  <div>
    <elFrame :src="$store.state.api.sqlApi" />
  </div>
</template>

<script>
import elFrame from '@/components/Iframe/index'
export default {
  name: 'Sql',
  components: { elFrame }
}
</script>

<style rel="stylesheet/scss" lang="scss" scoped>

</style>
