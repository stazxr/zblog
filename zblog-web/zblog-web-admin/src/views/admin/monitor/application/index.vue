<template>
  <div>
    <elFrame :src="$store.state.api.druidApi" />
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
