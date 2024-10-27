<template>
  <div>
    <el-dialog title="字典详情" append-to-body :close-on-click-modal="true" :before-close="handleClose" :visible.sync="dialogVisible" width="600px">
      <el-descriptions direction="vertical" :column="4" border>
        <!-- 1 -->
        <el-descriptions-item label="字典序列"> {{ dataInfo.id }} </el-descriptions-item>
        <el-descriptions-item label="字典类型">
          <span v-if="dataInfo.type === '1'">组</span>
          <span v-else-if="dataInfo.type === '2'">项</span>
          <span v-else> {{ dataInfo.type }} </span>
        </el-descriptions-item>
        <el-descriptions-item label="锁定状态">
          <span v-if="dataInfo.locked === 'true'">禁止编辑删除</span>
          <span v-else-if="dataInfo.locked === 'false'">允许编辑删除</span>
          <span v-else> {{ dataInfo.locked }} </span>
        </el-descriptions-item>
        <el-descriptions-item label="字典状态">
          <span v-if="dataInfo.enabled === 'true'">启用</span>
          <span v-else-if="dataInfo.enabled === 'false'">禁用</span>
          <span v-else> {{ dataInfo.enabled }} </span>
        </el-descriptions-item>
        <!-- 2 -->
        <el-descriptions-item label="字典名称" :span="2"> {{ dataInfo.name }} </el-descriptions-item>
        <el-descriptions-item label="字典KEY" :span="2"> {{ dataInfo.key }} </el-descriptions-item>
        <!-- 3 -->
        <el-descriptions-item label="创建用户" :span="2"> {{ dataInfo.createUser }} </el-descriptions-item>
        <el-descriptions-item label="创建时间" :span="2"> {{ dataInfo.createTime }} </el-descriptions-item>
        <!-- 4 -->
        <el-descriptions-item label="修改用户" :span="2"> {{ dataInfo.updateUser }} </el-descriptions-item>
        <el-descriptions-item label="修改时间" :span="2"> {{ dataInfo.updateTime }} </el-descriptions-item>
        <!-- 5 -->
        <el-descriptions-item label="字典VALUE" :span="4"> {{ dataInfo.value }} </el-descriptions-item>
        <!-- 6 -->
        <el-descriptions-item label="使用说明" :span="4"> {{ dataInfo.desc }} </el-descriptions-item>
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
        type: '',
        name: '',
        key: '',
        value: '',
        desc: '',
        locked: '',
        enabled: '',
        createUser: '',
        createTime: '',
        updateUser: '',
        updateTime: ''
      }
    }
  },
  methods: {
    initData() {
      this.$nextTick(() => {
        this.getDictDetail()
      })
    },
    getDictDetail() {
      this.$mapi.dict.queryDictDetail({ dictId: this.dataId }).then(res => {
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
