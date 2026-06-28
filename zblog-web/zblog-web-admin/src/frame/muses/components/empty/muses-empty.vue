<template>
  <el-empty class="muses-empty" :image="image" :description="description" />
</template>

<script>
export default {
  name: 'MusesEmpty',
  inheritAttrs: false,
  props: {
    image: {
      type: String,
      default() {
        return this.$assets.nodata
      }
    },
    description: {
      type: String,
      default: '暂无数据'
    }
  }
}
</script>

<style scoped>
::v-deep .muses-empty .el-empty__description:empty {
  display: none;
}

::v-deep .muses-empty .el-empty__description p:empty {
  display: none;
}
</style>
