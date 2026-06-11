<template>
  <div class="swagger-container">
    <el-result
      icon="info"
      title="接口文档已打开"
      sub-title="请在新打开的浏览器标签页中查看接口文档"
    >
      <template slot="extra">
        <el-button
          type="primary"
          @click="openSwagger"
        >
          重新打开
        </el-button>
      </template>
    </el-result>
  </div>
</template>

<script>
export default {
  name: 'Swagger',

  mounted() {
    this.openSwagger()
  },

  methods: {
    openSwagger() {
      const swaggerUrl = this.$store.state.api.swaggerApi
      if (swaggerUrl) {
        window.open(swaggerUrl, '_blank')
      }
    }
  }
}
</script>

<style scoped>
.swagger-container {
  height: calc(100vh - 84px);
  display: flex;
  justify-content: center;
  align-items: center;
}
</style>
