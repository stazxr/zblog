<template>
  <div>
    <el-dialog
      title="访客日志详情"
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
        <!-- 访问行为头部 -->
        <div class="visit-header">
          <div class="visit-icon">
            <i class="el-icon-view" />
          </div>
          <div class="visit-info">
            <div class="visit-title">{{ dataInfo.title }}</div>
            <div class="visit-path">{{ dataInfo.path }}</div>
          </div>
        </div>
        <!-- 访问信息 -->
        <div class="detail-section">
          <div class="section-title">
            <i class="el-icon-document" />访问信息
          </div>
          <el-descriptions :column="2" border>
            <el-descriptions-item label="访问类型">{{ dataInfo.type }}</el-descriptions-item>
            <el-descriptions-item label="访问时间">{{ dataInfo.visitTime }}</el-descriptions-item>
            <el-descriptions-item label="访问路径" :span="2">{{ dataInfo.path }}</el-descriptions-item>
            <el-descriptions-item label="页面标题" :span="2">{{ dataInfo.title }}</el-descriptions-item>
            <el-descriptions-item label="来源地址" :span="2">{{ dataInfo.referer }}</el-descriptions-item>
          </el-descriptions>
        </div>
        <!-- 访客信息 -->
        <div class="detail-section">
          <div class="section-title">
            <i class="el-icon-user" />访客信息
          </div>
          <el-descriptions column="2" border>
            <el-descriptions-item label="访客ID" :span="2">{{ dataInfo.visitorId }}</el-descriptions-item>
            <el-descriptions-item label="访客昵称">
              <div class="user-info">
                <el-avatar :size="28" :src="dataInfo.visitorAvatar" icon="el-icon-user-solid" />
                <span>{{ dataInfo.visitorNickname }}</span>
              </div>
            </el-descriptions-item>
            <el-descriptions-item label="用户ID">{{ dataInfo.userId }}</el-descriptions-item>
            <el-descriptions-item label="用户名">{{ dataInfo.username }}</el-descriptions-item>
            <el-descriptions-item label="创建时间">{{ dataInfo.createTime }}</el-descriptions-item>
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
            <el-descriptions-item label="浏览器版本">{{ dataInfo.browserVersion }}</el-descriptions-item>
            <el-descriptions-item label="操作系统">{{ dataInfo.os }}</el-descriptions-item>
            <el-descriptions-item label="设备类型">{{ dataInfo.deviceType }}</el-descriptions-item>
          </el-descriptions>
        </div>
        <!-- User-Agent -->
        <div class="detail-section">
          <div class="section-title">
            <i class="el-icon-connection" />User-Agent
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
        id: '',
        visitorId: '',
        visitorNickname: '',
        visitorAvatar: '',
        userId: '',
        username: '',
        path: '',
        title: '',
        referer: '',
        type: '',
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
        visitTime: '',
        createTime: ''
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
      this.$mapi.visitorLog.queryVisitorLogDetail({
        visitorLogId: dataId
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
/**
 * 顶部访问卡片
 */
.visit-header {
  display: flex;
  align-items: center;
  padding: 16px;
  margin-bottom: 18px;
  background: #f7f8fa;
  border-radius: 8px;
}
.visit-icon {
  width: 50px;
  height: 50px;
  display: flex;
  align-items: center;
  justify-content: center;
  border-radius: 50%;
  background: #ecf5ff;
  color: #409EFF;
  font-size: 24px;
}
.visit-info {
  margin-left: 15px;
}
.visit-title {
  font-size: 17px;
  font-weight: 600;
  color: #303133;
}
.visit-path {
  margin-top: 8px;
  font-size: 12px;
  color: #909399;
  word-break: break-all;
}
/**
 * 分组
 */
.detail-section {
  margin-bottom: 20px;
}
.section-title {
  margin-bottom: 10px;
  font-size: 14px;
  font-weight: 600;
  color: #303133;
}
.section-title i {
  margin-right: 6px;
  color: #409EFF;
}
/**
 * 用户
 */
.user-info {
  display: flex;
  align-items: center;
}
.user-info span {
  margin-left: 8px;
}
/**
 * UA
 */
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
