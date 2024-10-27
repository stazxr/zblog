<template>
  <div>
    <el-dialog title="类别详情" append-to-body :close-on-click-modal="true" :before-close="handleClose" :visible.sync="dialogVisible" width="600px">
      <el-descriptions direction="vertical" :column="4" border>
        <!-- 1 -->
        <el-descriptions-item label="类别名称"> {{ dataInfo.name }} </el-descriptions-item>
        <el-descriptions-item label="类别序列"> {{ dataInfo.id }} </el-descriptions-item>
        <el-descriptions-item label="类别排序"> {{ dataInfo.sort }} </el-descriptions-item>
        <el-descriptions-item label="类别状态">
          <span v-if="dataInfo.enabled === 'true'">启用</span>
          <span v-else-if="dataInfo.enabled === 'false'">禁用</span>
          <span v-else> {{ dataInfo.enabled }} </span>
        </el-descriptions-item>
        <!-- 2 -->
        <el-descriptions-item label="类别描述" :span="4">
          <span v-if="dataInfo.desc === ''"> - </span>
          <span v-else>{{ dataInfo.desc }}</span>
        </el-descriptions-item>
        <!-- 3 -->
        <el-descriptions-item label="创建用户" :span="2"> {{ dataInfo.createUser }} </el-descriptions-item>
        <el-descriptions-item label="创建时间" :span="2"> {{ dataInfo.createTime }} </el-descriptions-item>
        <!-- 4 -->
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
        this.getCategoryDetail(dataId)
      })
    },
    getCategoryDetail(dataId) {
      this.$mapi.category.queryCategoryDetail({ categoryId: dataId }).then(res => {
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
