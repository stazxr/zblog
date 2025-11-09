<template>
  <div>
    <el-dialog
      title="字典详情"
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
        <el-descriptions-item label="序列"> {{ dataInfo.id }} </el-descriptions-item>
        <el-descriptions-item label="字典类型">
          <span v-if="dataInfo.dictType === '1'">组</span>
          <span v-else-if="dataInfo.dictType === '2'">项</span>
          <span v-else> - </span>
        </el-descriptions-item>
        <el-descriptions-item label="字典状态" :span="2">
          <span v-if="dataInfo.enabled === 'true'">启用</span>
          <span v-else-if="dataInfo.enabled === 'false'">禁用</span>
          <span v-else> - </span>
        </el-descriptions-item>
        <!-- 2 -->
        <el-descriptions-item label="字典名称" :span="2"> {{ dataInfo.dictName }} </el-descriptions-item>
        <el-descriptions-item label="字典KEY" :span="2"> {{ dataInfo.dictKey }} </el-descriptions-item>
        <!-- 3 -->
        <el-descriptions-item label="字典VALUE" :span="4"> {{ dataInfo.dictValue }} </el-descriptions-item>
        <!-- 4 -->
        <el-descriptions-item label="创建用户" :span="2"> {{ dataInfo.createUsername }} </el-descriptions-item>
        <el-descriptions-item label="创建时间" :span="2"> {{ dataInfo.createTime }} </el-descriptions-item>
        <!-- 5 -->
        <el-descriptions-item label="修改用户" :span="2"> {{ dataInfo.updateUsername }} </el-descriptions-item>
        <el-descriptions-item label="修改时间" :span="2"> {{ dataInfo.updateTime }} </el-descriptions-item>
        <!-- 6 -->
        <el-descriptions-item label="字典描述" :span="4"> {{ dataInfo.dictDesc }} </el-descriptions-item>
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
        id: '',
        dictType: '',
        dictName: '',
        dictKey: '',
        dictValue: '',
        dictDesc: '',
        locked: '',
        enabled: '',
        createUsername: '',
        createTime: '',
        updateUsername: '',
        updateTime: ''
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
      this.$mapi.dict.queryDictDetail({ dictId: dataId }).then(res => {
        const { data } = res
        Object.keys(this.dataInfo).forEach(key => {
          this.dataInfo[key] = data[key] == null || data[key] === '' ? '-' : data[key].toString()
        })
      }).catch(_ => {
        setTimeout(() => { this.doClose() }, 500)
      })
    },
    doClose() {
      Object.keys(this.dataInfo).forEach(key => {
        this.dataInfo[key] = ''
      })
      this.$emit('showDetailDone')
    },
    handleClose() {
      this.doClose()
    }
  }
}
</script>

<style scoped>

</style>
