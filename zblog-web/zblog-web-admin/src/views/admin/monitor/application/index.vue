<template>
  <div>
    <elFrame :src="$store.state.api.sqlApi" />
  </div>
</template>

<script>
import elFrame from '@/components/Iframe/index'
export default {
  name: 'ApplicationMonitoring',
  components: {
    elFrame
  }
}
</script>
