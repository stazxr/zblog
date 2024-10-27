<template>
  <div>
    <el-dialog title="标签详情" append-to-body :close-on-click-modal="true" :before-close="handleClose" :visible.sync="dialogVisible" width="550px">
      <el-descriptions direction="vertical" :column="4" border>
        <!-- 1 -->
        <el-descriptions-item label="标签名称"> {{ dataInfo.name }} </el-descriptions-item>
        <el-descriptions-item label="标签序列"> {{ dataInfo.id }} </el-descriptions-item>
        <el-descriptions-item label="标签类型">
          <span v-if="dataInfo.type === '1'">公共标签</span>
          <span v-else-if="dataInfo.type === '2'">定制标签</span>
          <span v-else> {{ dataInfo.type }} </span>
        </el-descriptions-item>
        <el-descriptions-item label="标签状态">
          <el-tag v-if="dataInfo.enabled === 'true'" size="small">启用</el-tag>
          <el-tag v-else-if="dataInfo.enabled === 'false'" size="small" type="warning">禁用</el-tag>
          <span v-else> {{ dataInfo.enabled }} </span>
        </el-descriptions-item>
        <!-- 2 -->
        <el-descriptions-item label="创建用户" :span="2"> {{ dataInfo.createUser }} </el-descriptions-item>
        <el-descriptions-item label="创建时间" :span="2"> {{ dataInfo.createTime }} </el-descriptions-item>
        <!-- 3 -->
        <el-descriptions-item label="修改用户" :span="2"> {{ dataInfo.updateUser }} </el-descriptions-item>
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
    },
    dataId: {
      type: String,
      default: ''
    }
  },
  data() {
    return {
      dataInfo: {
        id: '',
        name: '',
        type: '',
        enabled: '',
        createUser: '',
        createTime: '',
        updateUser: '',
        updateTime: ''
      }
    }
  },
  methods: {
    initData(dataId) {
      this.$nextTick(() => {
        this.getTagDetail(dataId)
      })
    },
    getTagDetail(dataId) {
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
