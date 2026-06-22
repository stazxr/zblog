<template>
  <div>
    <el-dialog
      title="主题详情"
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
        <el-descriptions-item label="主题序列"> {{ dataInfo.id }} </el-descriptions-item>
        <el-descriptions-item label="主题名称"> {{ dataInfo.themeName }} </el-descriptions-item>
        <el-descriptions-item label="主题类型"> {{ dataInfo.themeType }} </el-descriptions-item>
        <el-descriptions-item label="作者"> {{ dataInfo.ownerName }} </el-descriptions-item>
        <!-- 2 -->
        <el-descriptions-item label="主题归属" :span="2"> {{ dataInfo.ownerType }} </el-descriptions-item>
        <el-descriptions-item label="主题状态" :span="2">
          <span v-if="dataInfo.ownerType === 'SYSTEM'">
            <el-tag v-if="dataInfo.isDefault === true" type="success">默认主题</el-tag>
            <el-tag v-else type="info">普通主题</el-tag>
          </span>
          <span v-else-if="dataInfo.ownerType === 'USER'">
            <el-tag v-if="dataInfo.isActive === true" type="success">已激活</el-tag>
            <el-tag v-else type="info">未激活</el-tag>
          </span>
          <span v-else>-</span>
        </el-descriptions-item>
        <!-- 3 -->
        <el-descriptions-item label="创建用户" :span="2"> {{ dataInfo.createUsername }} </el-descriptions-item>
        <el-descriptions-item label="创建时间" :span="2"> {{ dataInfo.createTime }} </el-descriptions-item>
        <!-- 4 -->
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
        id: '',
        themeName: '',
        themeType: '',
        ownerType: '',
        ownerName: '',
        isDefault: '',
        isActive: '',
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
      this.$mapi.theme.queryThemeDetail({ themeId: dataId }).then(res => {
        const { data } = res
        Object.keys(this.dataInfo).forEach(key => {
          this.dataInfo[key] = data[key] == null || data[key] === '' ? '-' : data[key].toString()
        })
      }).catch(_ => {
        this.doClose()
      })
    },
    handleClose() {
      this.doClose()
    },
    doClose() {
      Object.keys(this.dataInfo).forEach(key => {
        this.dataInfo[key] = ''
      })
      this.$emit('showDetailDone')
    }
  }
}
</script>

<style scoped>

</style>
