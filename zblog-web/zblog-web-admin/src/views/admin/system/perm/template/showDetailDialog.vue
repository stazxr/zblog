<template>
  <div>
    <el-dialog title="权限详情" append-to-body :close-on-click-modal="true" :before-close="handleClose" :visible.sync="dialogVisible" width="800px">
      <el-tabs v-model="activeName" type="border-card" @tab-click="initTabData">
        <el-tab-pane label="详细信息" name="permDetail">
          <perm-detail ref="permDetail" />
        </el-tab-pane>
        <el-tab-pane label="接口列表" name="interfaceList">
          <interface-list ref="interfaceList" />
        </el-tab-pane>
        <el-tab-pane label="角色列表" name="roleList">
          <role-list ref="roleList" />
        </el-tab-pane>
        <el-tab-pane label="操作日志" name="logList">
          <log-list ref="logList" />
        </el-tab-pane>
      </el-tabs>
    </el-dialog>
  </div>
</template>

<script>
import PermDetail from '@/views/admin/system/perm/template/detail/PermDetail'
import InterfaceList from '@/views/admin/system/perm/template/detail/InterfaceList'
import RoleList from '@/views/admin/system/perm/template/detail/RoleList'
import LogList from '@/views/admin/system/perm/template/detail/LogList'
export default {
  components: {
    PermDetail,
    InterfaceList,
    RoleList,
    LogList
  },
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
      activeName: 'permDetail'
    }
  },
  watch: {
    dataId() {
      this.activeName = 'permDetail'
    }
  },
  methods: {
    initTabData(tab) {
      this.initData(tab.name)
    },
    initData(name) {
      name = name == null || name === '' ? this.activeName : name
      switch (name) {
        case 'permDetail':
          this.$nextTick(() => {
            this.$refs['permDetail'].initData(this.dataId)
          })
          break
        case 'interfaceList':
          this.$nextTick(() => {
            this.$refs['interfaceList'].initData(this.dataId)
          })
          break
        case 'roleList':
          this.$nextTick(() => {
            this.$refs['roleList'].initData(this.dataId)
          })
          break
        case 'logList':
          this.$nextTick(() => {
            this.$refs['logList'].initData(this.dataId)
          })
          break
        default:
          console.log('error tab name', name)
      }
    },
    doClose() {
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
