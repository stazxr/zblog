<template>
  <div>
    <el-dialog title="专栏详情" append-to-body :close-on-click-modal="true" :before-close="handleClose" :visible.sync="dialogVisible" width="600px">
      <el-descriptions direction="vertical" :column="5" border>
        <!-- 1 -->
        <el-descriptions-item label="专栏名称"> {{ dataInfo.name }} </el-descriptions-item>
        <el-descriptions-item label="专栏序列"> {{ dataInfo.id }} </el-descriptions-item>
        <el-descriptions-item label="专栏排序"> {{ dataInfo.sort }} </el-descriptions-item>
        <el-descriptions-item label="专栏状态">
          <span v-if="dataInfo.enabled === 'true'">启用</span>
          <span v-else-if="dataInfo.enabled === 'false'">禁用</span>
          <span v-else> {{ dataInfo.enabled }} </span>
        </el-descriptions-item>
        <el-descriptions-item label="首页展示">
          <span v-if="dataInfo.pageShow === 'true'">展示</span>
          <span v-else-if="dataInfo.pageShow === 'false'">不展示</span>
          <span v-else> {{ dataInfo.pageShow }} </span>
        </el-descriptions-item>
        <!-- 2 -->
        <el-descriptions-item label="专栏简介" :span="5">
          <span v-if="dataInfo.desc === ''"> - </span>
          <span v-else>{{ dataInfo.desc }}</span>
        </el-descriptions-item>
        <!-- 3 -->
        <el-descriptions-item label="创建用户" :span="2"> {{ dataInfo.createUser }} </el-descriptions-item>
        <el-descriptions-item label="创建时间" :span="4"> {{ dataInfo.createTime }} </el-descriptions-item>
        <!-- 4 -->
        <el-descriptions-item label="修改用户" :span="2"> {{ dataInfo.updateUser }} </el-descriptions-item>
        <el-descriptions-item label="修改时间" :span="3"> {{ dataInfo.updateTime }} </el-descriptions-item>
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
        name: '',
        desc: '',
        sort: '',
        enabled: '',
        pageShow: '',
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
        this.getColumnDetail(dataId)
      })
    },
    getColumnDetail(dataId) {
      this.$mapi.column.queryColumnDetail({ columnId: dataId }).then(res => {
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
