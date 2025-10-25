<template>
  <div>
    <el-dialog
      title="角色详情"
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
        <el-descriptions-item label="角色序列"> {{ dataInfo.id }} </el-descriptions-item>
        <el-descriptions-item label="角色名称"> {{ dataInfo.roleName }} </el-descriptions-item>
        <el-descriptions-item label="角色编码"> {{ dataInfo.roleCode }} </el-descriptions-item>
        <el-descriptions-item label="角色状态">
          <el-tag v-if="dataInfo.enabled === 'true'">启用</el-tag>
          <el-tag v-else-if="dataInfo.enabled === 'false'" type="warning">禁用</el-tag>
          <span v-else> - </span>
        </el-descriptions-item>
        <!-- 2 -->
        <el-descriptions-item label="创建用户" :span="2"> {{ dataInfo.createUsername }} </el-descriptions-item>
        <el-descriptions-item label="创建时间" :span="2"> {{ dataInfo.createTime }} </el-descriptions-item>
        <!-- 3 -->
        <el-descriptions-item label="修改用户" :span="2"> {{ dataInfo.updateUsername }} </el-descriptions-item>
        <el-descriptions-item label="修改时间" :span="2"> {{ dataInfo.updateTime }} </el-descriptions-item>
        <!-- 4 -->
        <el-descriptions-item label="角色描述" :span="4"> {{ dataInfo.roleDesc }} </el-descriptions-item>
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
        roleName: '',
        roleCode: '',
        enabled: '',
        roleDesc: '',
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
    initData(roleId) {
      this.$nextTick(() => {
        this.queryDetail(roleId)
      })
    },
    queryDetail(dataId) {
      this.$mapi.role.queryRoleDetail({ roleId: dataId }).then(res => {
        const { data } = res
        Object.keys(this.dataInfo).forEach(key => {
          this.dataInfo[key] = data[key] == null || data[key] === '' ? '-' : data[key].toString()
        })
      }).catch(_ => {
        this.doClose()
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
