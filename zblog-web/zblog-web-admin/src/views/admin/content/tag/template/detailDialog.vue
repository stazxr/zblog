<template>
  <div>
    <el-dialog
      title="标签详情"
      :visible.sync="dialogVisible"
      :fullscreen="device === 'mobile'"
      :destroy-on-close="true"
      :close-on-click-modal="true"
      :close-on-press-escape="true"
      :before-close="handleClose"
      append-to-body
      width="520px"
    >
      <el-descriptions direction="vertical" :column="4" border>
        <!-- 1 -->
        <el-descriptions-item label="标签序列"> {{ dataInfo.id }} </el-descriptions-item>
        <el-descriptions-item label="标签名称"> {{ dataInfo.name }} </el-descriptions-item>
        <el-descriptions-item label="SLUG"> {{ dataInfo.slug }} </el-descriptions-item>
        <el-descriptions-item label="标签状态">
          <el-tag v-if="dataInfo.enabled === 'true'">启用</el-tag>
          <el-tag v-else-if="dataInfo.enabled === 'false'" type="warning">禁用</el-tag>
          <span v-else> - </span>
        </el-descriptions-item>
        <!-- 2 -->
        <el-descriptions-item label="文章数" :span="2"> {{ dataInfo.articleCount }} </el-descriptions-item>
        <el-descriptions-item label="收录状态" :span="2">
          <el-tag v-if="dataInfo.allowIndex === 'true'">收录</el-tag>
          <el-tag v-else-if="dataInfo.allowIndex === 'false'" type="warning">禁止</el-tag>
          <span v-else> - </span>
        </el-descriptions-item>
        <!-- 3 -->
        <el-descriptions-item label="SEO标题" :span="2"> {{ dataInfo.seoTitle }} </el-descriptions-item>
        <el-descriptions-item label="SEO关键字" :span="2"> {{ dataInfo.seoKeywords }} </el-descriptions-item>
        <!-- 4 -->
        <el-descriptions-item label="SEO描述" :span="4"> {{ dataInfo.seoDescription }} </el-descriptions-item>
        <!-- 5 -->
        <el-descriptions-item label="创建用户" :span="2"> {{ dataInfo.createUsername }} </el-descriptions-item>
        <el-descriptions-item label="创建时间" :span="2"> {{ dataInfo.createTime }} </el-descriptions-item>
        <!-- 6 -->
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
        articleCount: 0,
        slug: null,
        seoTitle: null,
        seoKeywords: null,
        seoDescription: null,
        allowIndex: null,
        enabled: null,
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
      this.$mapi.tag.queryTagDetail({ tagId: dataId }).then(res => {
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
