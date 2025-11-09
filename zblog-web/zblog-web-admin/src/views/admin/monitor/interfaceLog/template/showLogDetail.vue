<template>
  <div>
    <el-drawer
      :visible.sync="dialogVisible"
      :with-header="false"
      :wrapper-closable="true"
      :close-on-press-escape="true"
      :before-close="handleClose"
      :size="isMobile ? '100%' : '70%'"
      destroy-on-close
    >
      <div class="demo-drawer__content">
        <div id="codeView" v-highlight>
          <pre>
            <code class="language-java" v-html="errorDetail" />
          </pre>
        </div>
        <div class="demo-drawer__footer">
          <el-button @click="cancel">关 闭</el-button>
        </div>
      </div>
    </el-drawer>
  </div>
</template>

<script>
export default {
  props: {
    dialogVisible: {
      type: Boolean,
      default: false
    }
  },
  data() {
    return {
      errorDetail: null
    }
  },
  computed: {
    isMobile() {
      return this.$store.state.app.device === 'mobile'
    }
  },
  methods: {
    initData(dataId) {
      this.queryLogErrorDetail(dataId)
    },
    queryLogErrorDetail(dataId) {
      this.$mapi.log.queryInterfaceLogExpDetail({ logId: dataId }).then(res => {
        this.errorDetail = res.data
      }).catch(_ => {
        setTimeout(() => { this.doClose() }, 500)
      })
    },
    cancel() {
      this.handleClose()
    },
    handleClose() {
      this.doClose()
    },
    doClose() {
      this.errorDetail = null
      this.$emit('showDetailDone')
    }
  }
}
</script>

<style scoped>
.demo-drawer__content {
  display: flex;
  flex-direction: column;
  height: 100%;
}
.demo-drawer__footer {
  margin-top: 10px;
  display: flex;
}
.demo-drawer__footer button {
  flex: 1;
  margin: 5px 20px;
}
</style>
