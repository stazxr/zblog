<template>
  <div>
    <el-dialog
      title="友链详情"
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
        <el-descriptions-item label="网址名称"> {{ dataInfo.name }} </el-descriptions-item>
        <el-descriptions-item label="网址地址"> {{ dataInfo.url }} </el-descriptions-item>
        <el-descriptions-item label="审核状态">
          <el-tag v-if="dataInfo.status === '0'" type="warning">待审批</el-tag>
          <el-tag v-else-if="dataInfo.status === '1'" type="success">审批通过</el-tag>
          <el-tag v-else-if="dataInfo.status === '2'" type="danger">审批拒绝</el-tag>
          <span v-else> - </span>
        </el-descriptions-item>
        <el-descriptions-item label="网站排序"> {{ dataInfo.sort }} </el-descriptions-item>
        <!-- 2 -->
        <el-descriptions-item label="网站邮箱"> {{ dataInfo.email }} </el-descriptions-item>
        <el-descriptions-item label="联系方式"> {{ dataInfo.contact }} </el-descriptions-item>
        <!-- 4 -->
        <el-descriptions-item label="展示状态">
          <el-tag v-if="dataInfo.isVisible === 'true'" type="success">展示</el-tag>
          <el-tag v-else-if="dataInfo.isVisible === 'false'" type="danger">隐藏</el-tag>
          <span v-else> - </span>
        </el-descriptions-item>
        <el-descriptions-item label="SEO配置">
          <el-tag v-if="dataInfo.allowFollow === 'true'" type="success">允许传递SEO权重</el-tag>
          <el-tag v-else-if="dataInfo.allowFollow === 'false'" type="danger">禁用SEO</el-tag>
          <span v-else> - </span>
        </el-descriptions-item>
        <!-- 3 -->
        <el-descriptions-item label="网站LOGO" :span="2"> {{ dataInfo.logo }} </el-descriptions-item>
        <el-descriptions-item label="网站介绍" :span="2"> {{ dataInfo.description }} </el-descriptions-item>
        <!-- 4 -->
        <el-descriptions-item label="创建用户" :span="2"> {{ dataInfo.createUsername }} </el-descriptions-item>
        <el-descriptions-item label="创建时间" :span="2"> {{ dataInfo.createTime }} </el-descriptions-item>
        <!-- 5 -->
        <el-descriptions-item label="修改用户" :span="2"> {{ dataInfo.updateUsername }} </el-descriptions-item>
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
        id: null,
        name: null,
        url: null,
        logo: null,
        description: null,
        email: null,
        contact: null,
        status: null,
        isVisible: null,
        allowFollow: null,
        sort: null,
        createUsername: null,
        createTime: null,
        updateUsername: null,
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
      this.$mapi.friendLink.queryFriendLinkDetail({ friendLinkId: dataId }).then(res => {
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
