<template>
  <div>
    <el-dialog
      title="访客详情"
      :visible.sync="dialogVisible"
      :fullscreen="device === 'mobile'"
      :destroy-on-close="true"
      :close-on-click-modal="true"
      :close-on-press-escape="true"
      :before-close="handleClose"
      append-to-body
      width="620px"
    >
      <div class="visitor-detail">
        <!-- 访客头部 -->
        <div class="visitor-header">
          <el-avatar :size="64" :src="dataInfo.visitorAvatar" icon="el-icon-user-solid" />
          <div class="visitor-base">
            <div class="nickname">{{ dataInfo.visitorNickname }}</div>
            <div class="visitor-id">{{ dataInfo.visitorId }}</div>
          </div>
        </div>
        <!-- 基础信息 -->
        <div class="detail-section">
          <div class="section-title">
            <i class="el-icon-user" />基础信息
          </div>
          <el-descriptions :column="2" border>
            <el-descriptions-item label="用户ID">{{ dataInfo.userId }}</el-descriptions-item>
            <el-descriptions-item label="用户名">{{ dataInfo.username }}</el-descriptions-item>
            <el-descriptions-item label="首次访问">{{ dataInfo.firstVisitTime }}</el-descriptions-item>
            <el-descriptions-item label="最后访问">{{ dataInfo.lastVisitDate }}</el-descriptions-item>
          </el-descriptions>
        </div>
        <!-- 网络信息 -->
        <div class="detail-section">
          <div class="section-title">
            <i class="el-icon-location" />网络信息
          </div>
          <el-descriptions :column="2" border>
            <el-descriptions-item label="访问IP">{{ dataInfo.ip }}</el-descriptions-item>
            <el-descriptions-item label="运营商">{{ dataInfo.isp }}</el-descriptions-item>
            <el-descriptions-item label="国家">{{ dataInfo.country }}</el-descriptions-item>
            <el-descriptions-item label="省份">{{ dataInfo.province }}</el-descriptions-item>
            <el-descriptions-item label="城市">{{ dataInfo.city }}</el-descriptions-item>
            <el-descriptions-item label="区域">{{ dataInfo.district }}</el-descriptions-item>
            <el-descriptions-item label="IP归属" :span="2">{{ dataInfo.ipRegion }}</el-descriptions-item>
          </el-descriptions>
        </div>
        <!-- 设备信息 -->
        <div class="detail-section">
          <div class="section-title">
            <i class="el-icon-monitor" />设备信息
          </div>
          <el-descriptions :column="2" border>
            <el-descriptions-item label="浏览器">{{ dataInfo.browser }}</el-descriptions-item>
            <el-descriptions-item label="版本">{{ dataInfo.browserVersion }}</el-descriptions-item>
            <el-descriptions-item label="操作系统">{{ dataInfo.os }}</el-descriptions-item>
            <el-descriptions-item label="设备类型">{{ dataInfo.deviceType }}</el-descriptions-item>
          </el-descriptions>
        </div>
        <!-- 时间信息 -->
        <div class="detail-section">
          <div class="section-title">
            <i class="el-icon-time" />时间信息
          </div>
          <el-descriptions :column="2" border>
            <el-descriptions-item label="创建时间">{{ dataInfo.createTime }}</el-descriptions-item>
            <el-descriptions-item label="更新时间">{{ dataInfo.updateTime }}</el-descriptions-item>
          </el-descriptions>
        </div>
        <!-- UA -->
        <div class="detail-section">
          <div class="section-title">
            <i class="el-icon-document" />User-Agent
          </div>
          <div class="user-agent">{{ dataInfo.userAgent }}</div>
        </div>
      </div>
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
        visitorId: '',
        visitorNickname: '',
        visitorAvatar: '',
        userId: '',
        username: '',
        ip: '',
        ipRegion: '',
        country: '',
        province: '',
        city: '',
        district: '',
        isp: '',
        userAgent: '',
        browser: '',
        browserVersion: '',
        os: '',
        deviceType: '',
        firstVisitTime: '',
        lastVisitDate: '',
        createTime: '',
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
      this.$mapi.visitor.queryVisitorDetail({
        visitorId: dataId
      }).then(res => {
        const { data } = res
        Object.keys(this.dataInfo).forEach(key => {
          this.dataInfo[key] = data[key] == null || data[key] === '' ? '-' : data[key].toString()
        })
      }).catch(() => {
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
.visitor-detail {
  padding: 5px;
}

/* 顶部访客卡片 */
.visitor-header {
  display: flex;
  align-items: center;
  padding: 16px;
  margin-bottom: 18px;
  background: #f7f8fa;
  border-radius: 8px;
}
.visitor-base {
  margin-left: 16px;
}
.nickname {
  font-size: 18px;
  font-weight: 600;
  color: #303133;
}
.visitor-id {
  margin-top: 8px;
  font-size: 12px;
  color: #909399;
  word-break: break-all;
}
/* 信息分组 */
.detail-section {
  margin-bottom: 20px;
}
.section-title {
  font-size: 14px;
  font-weight: 600;
  color: #303133;
  margin-bottom: 10px;
}
.section-title i {
  margin-right: 6px;
  color: #409EFF;
}
/* UA */
.user-agent {
  padding: 12px;
  background: #f5f7fa;
  border-radius: 6px;
  line-height: 22px;
  font-size: 12px;
  color: #606266;
  word-break: break-all;
}
</style>
