<template>
  <div>
    <el-dialog title="角色详情" append-to-body :close-on-click-modal="true" :before-close="handleClose" :visible.sync="dialogVisible" width="550px">
      <el-descriptions direction="vertical" :column="4" border>
        <!-- 1 -->
        <el-descriptions-item label="角色序列"> {{ roleInfo.id }} </el-descriptions-item>
        <el-descriptions-item label="角色名称"> {{ roleInfo.roleName }} </el-descriptions-item>
        <el-descriptions-item label="角色编码"> {{ roleInfo.roleCode }} </el-descriptions-item>
        <el-descriptions-item label="角色状态">
          <el-tag v-if="roleInfo.enabled === 'true'" size="small">启用</el-tag>
          <el-tag v-else-if="roleInfo.enabled === 'false'" size="small" type="warning">禁用</el-tag>
          <span v-else> {{ roleInfo.enabled }} </span>
        </el-descriptions-item>
        <!-- 2 -->
        <el-descriptions-item label="创建用户" :span="2"> {{ roleInfo.createUser }} </el-descriptions-item>
        <el-descriptions-item label="创建时间" :span="2"> {{ roleInfo.createTime }} </el-descriptions-item>
        <!-- 3 -->
        <el-descriptions-item label="修改用户" :span="2"> {{ roleInfo.updateUser }} </el-descriptions-item>
        <el-descriptions-item label="修改时间" :span="2"> {{ roleInfo.updateTime }} </el-descriptions-item>
        <!-- 4 -->
        <el-descriptions-item label="角色描述" :span="4"> {{ roleInfo.desc }} </el-descriptions-item>
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
      roleInfo: {
        id: '',
        roleName: '',
        roleCode: '',
        enabled: '',
        desc: '',
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
        this.getRoleDetail()
      })
    },
    getRoleDetail() {
      this.$mapi.role.queryRoleDetail({ roleId: this.dataId }).then(res => {
        const { data } = res
        Object.keys(this.roleInfo).forEach(key => {
          this.roleInfo[key] = data[key] == null || data[key] === '' ? '-' : data[key].toString()
        })
      }).catch(_ => {
        Object.keys(this.roleInfo).forEach(key => {
          this.roleInfo[key] = '-'
        })
      })
    },
    doClose() {
      Object.keys(this.roleInfo).forEach(key => {
        this.roleInfo[key] = ''
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
