<template>
  <div>
    <el-drawer :with-header="false" :visible.sync="dialogVisible" destroy-on-close :before-close="handleClose" size="70%" @opened="initData" @closed="doClose">
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
    },
    dataId: {
      type: String,
      default: ''
    }
  },
  data() {
    return {
      errorDetail: ''
    }
  },
  methods: {
    initData() {
      this.queryLogErrorDetail()
    },
    queryLogErrorDetail() {
      this.$mapi.log.queryLogErrorDetail({ logId: this.dataId }).then(res => {
        this.errorDetail = res.data
      })
    },
    doClose() {
      this.$emit('closeDetailDone')
    },
    handleClose() {
      if (!this.submitLoading) {
        this.$confirm('确认关闭？').then(_ => {
          this.doClose()
        }).catch(_ => {})
      }
    },
    cancel() {
      this.handleClose()
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
