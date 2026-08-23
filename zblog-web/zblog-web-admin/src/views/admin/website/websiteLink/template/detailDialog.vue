<template>
  <div>
    <el-dialog
      title="网站链接详情"
      :visible.sync="dialogVisible"
      :fullscreen="device === 'mobile'"
      :destroy-on-close="true"
      :close-on-click-modal="true"
      :close-on-press-escape="true"
      :before-close="handleClose"
      append-to-body
      width="600px"
    >
      <el-descriptions direction="vertical" :column="4" border>
        <!-- 1 -->
        <el-descriptions-item label="链接名称" :span="2"> {{ dataInfo.linkName }} </el-descriptions-item>
        <el-descriptions-item label="链接类型" :span="2"> {{ dataInfo.linkType }} </el-descriptions-item>
        <!-- 2 -->
        <el-descriptions-item label="链接状态" :span="2">
          <el-tag v-if="dataInfo.enabled === 'true'" type="success">正常</el-tag>
          <el-tag v-else-if="dataInfo.enabled === 'false'" type="danger">禁用</el-tag>
          <span v-else> - </span>
        </el-descriptions-item>
        <el-descriptions-item label="链接排序" :span="2"> {{ dataInfo.sort }} </el-descriptions-item>
        <!-- 3 -->
        <el-descriptions-item label="链接地址" :span="4"> {{ dataInfo.linkUrl }} </el-descriptions-item>
        <!-- 4 -->
        <el-descriptions-item label="创建时间" :span="2"> {{ dataInfo.createTime }} </el-descriptions-item>
        <el-descriptions-item label="修改时间" :span="2"> {{ dataInfo.updateTime }} </el-descriptions-item>
      </el-descriptions>
    </el-dialog>
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
      dataInfo: {
        linkName: null,
        linkType: null,
        linkUrl: null,
        sort: null,
        enabled: null,
        createTime: null,
        updateTime: null
      }
    }
  },
  computed: {
    device() {
      return this.$store.state.app.device
    }
  },
  methods: {
    initData(dataId) {
      this.$nextTick(() => {
        this.queryDetail(dataId)
      })
    },
    queryDetail(dataId) {
      this.$mapi.websiteLink.queryWebsiteLinkDetail({ websiteLinkId: dataId }).then(res => {
        const { data } = res
        Object.keys(this.dataInfo).forEach(key => {
          this.dataInfo[key] = data[key] == null || data[key] === '' ? '-' : data[key].toString()
        })
      }).catch(_ => {
        Object.keys(this.dataInfo).forEach(key => {
          this.dataInfo[key] = '-'
        })
      })
    },
    doClose() {
      Object.keys(this.dataInfo).forEach(key => {
        this.dataInfo[key] = ''
      })
      this.$emit('showDetailDone')
    },
    handleClose() {
      this.$confirm('确认关闭？').then(_ => {
        this.doClose()
      }).catch(_ => {})
    }
  }
}
</script>

<style scoped>

</style>
