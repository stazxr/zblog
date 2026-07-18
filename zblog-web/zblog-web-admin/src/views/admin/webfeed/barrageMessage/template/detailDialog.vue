<template>
  <div>
    <el-dialog
      title="弹幕详情"
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
        <el-descriptions-item label="弹幕序列" :span="2"> {{ dataInfo.id }} </el-descriptions-item>
        <el-descriptions-item label="审核状态" :span="2">
          <el-tag v-if="dataInfo.auditStatus === '0'" type="primary">待审批</el-tag>
          <el-tag v-else-if="dataInfo.auditStatus === '1'" type="success">审批通过</el-tag>
          <el-tag v-else-if="dataInfo.auditStatus === '2'" type="danger">审批拒绝</el-tag>
          <el-tag v-else-if="dataInfo.auditStatus === '3'" type="warning">待复核</el-tag>
          <span v-else>-</span>
        </el-descriptions-item>
        <!-- 2 -->
        <el-descriptions-item label="来源IP" :span="2"> {{ dataInfo.ip }} </el-descriptions-item>
        <el-descriptions-item label="IP归属" :span="2"> {{ dataInfo.ipRegion }} </el-descriptions-item>
        <!-- 3 -->
        <el-descriptions-item label="创建用户" :span="2">
          <div class="user-info">
            <el-avatar :size="28" :src="dataInfo.avatar" icon="el-icon-user-solid" />
            <span>{{ dataInfo.nickname }}</span>
          </div>
        </el-descriptions-item>
        <el-descriptions-item label="创建时间" :span="2"> {{ dataInfo.createTime }} </el-descriptions-item>
        <!-- 4 -->
        <el-descriptions-item label="弹幕内容" :span="4">
          <span class="barrage-content" :style="{ color: dataInfo.color }">{{ dataInfo.content }}</span>
        </el-descriptions-item>
        <!-- 5 -->
        <el-descriptions-item label="审核结果" :span="4"> {{ dataInfo.auditReason }} </el-descriptions-item>
        <!-- 6 -->
        <el-descriptions-item label="审核用户" :span="2"> {{ dataInfo.auditUsername }} </el-descriptions-item>
        <el-descriptions-item label="审核时间" :span="2"> {{ dataInfo.auditTime }} </el-descriptions-item>
        <!-- 7 -->
        <el-descriptions-item label="设备ID" :span="4"> {{ dataInfo.deviceId }} </el-descriptions-item>
        <!-- 8 -->
        <el-descriptions-item label="访问代理" :span="4"> {{ dataInfo.userAgent }} </el-descriptions-item>
      </el-descriptions>

      <template v-if="dataInfo.auditRecord">
        <el-divider content-position="left">自动审核报告</el-divider>
        <el-descriptions v-if="dataInfo.auditRecord" direction="vertical" :column="4" border>
          <el-descriptions-item label="审核决策" :span="2">
            <el-tag v-if="dataInfo.auditRecord['decision'] === 'MODIFY'" type="primary">MODIFY</el-tag>
            <el-tag v-else-if="dataInfo.auditRecord['decision'] === 'PASS'" type="success">PASS</el-tag>
            <el-tag v-else-if="dataInfo.auditRecord['decision'] === 'REJECT'" type="danger">REJECT</el-tag>
            <el-tag v-else-if="dataInfo.auditRecord['decision'] === 'MANUAL'" type="warning">MANUAL</el-tag>
            <span v-else>-</span>
          </el-descriptions-item>
          <el-descriptions-item label="审核耗时" :span="2">
            {{ dataInfo.auditRecord['costMs'] }} ms
          </el-descriptions-item>
          <!-- 原始内容 -->
          <el-descriptions-item label="原始内容" :span="4">
            <div class="audit-box">{{ dataInfo.auditRecord['originalContent'] }}</div>
          </el-descriptions-item>
          <!-- 最终内容 -->
          <el-descriptions-item label="最终内容" :span="4">
            <div class="audit-box">{{ dataInfo.auditRecord['finalContent'] }}</div>
          </el-descriptions-item>
          <!-- 命中词 -->
          <el-descriptions-item label="命中关键词" :span="4">
            <template v-if="dataInfo.auditRecord['hitWords'] && dataInfo.auditRecord['hitWords'].length">
              <el-tag v-for="(item, index) in dataInfo.auditRecord['hitWords']" :key="index" type="danger" class="hit-word">
                {{ item }}
              </el-tag>
            </template>
            <span v-else> - </span>
          </el-descriptions-item>
          <el-descriptions-item label="备注" :span="4">
            {{ dataInfo.auditRecord.reason }}
          </el-descriptions-item>
        </el-descriptions>

        <!-- Processor执行轨迹 -->
        <el-divider content-position="left">审核执行过程</el-divider>
        <el-collapse>
          <el-collapse-item title="查看 Processor 执行详情" name="trace">
            <el-card v-for="(trace, index) in auditTraces" :key="index" shadow="never" class="trace-card">
              <div class="trace-header">
                <span>{{ index + 1 }}. {{ trace['processor'] }}</span>
                <el-tag>{{ trace['status'] }}</el-tag>
              </div>
              <div class="trace-item">
                耗时：{{ trace['costMs'] }} ms
              </div>
              <div class="trace-item">
                敏感词：{{ trace['hitWords'] }}
              </div>
              <div class="trace-item">
                备注：{{ trace['reason'] }}
              </div>
            </el-card>
          </el-collapse-item>
        </el-collapse>
      </template>
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
        content: '',
        avatar: '',
        nickname: '',
        ip: '',
        ipRegion: '',
        userAgent: '',
        auditStatus: '',
        auditReason: '',
        auditUsername: '',
        auditTime: '',
        deviceId: '',
        color: '',
        createTime: '',
        auditRecord: null
      }
    }
  },
  computed: {
    device() {
      return this.$store.state.app.device
    },
    auditTraces() {
      if (!this.dataInfo.auditRecord) {
        return []
      }
      return this.dataInfo.auditRecord.traces || []
    }
  },
  methods: {
    initData(dataId) {
      this.$nextTick(() => {
        this.queryDetail(dataId)
      })
    },
    queryDetail(dataId) {
      this.$mapi.barrageMessage.queryBarrageMessageDetail({ barrageMessageId: dataId }).then(res => {
        const { data } = res
        Object.keys(this.dataInfo).forEach(key => {
          if (key === 'auditRecord') {
            this.dataInfo.auditRecord = data.auditRecord
          } else {
            this.dataInfo[key] = data[key] == null || data[key] === '' ? '-' : data[key].toString()
          }
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
      this.dataInfo.auditRecord = null
      this.$emit('showDetailDone')
    }
  }
}
</script>

<style scoped>
.barrage-content {
  display: inline-block;
  max-width: 100%;
  padding: 8px 14px;
  background: #303133;
  border-radius: 16px;
  line-height: 22px;
  word-break: break-word;
  white-space: pre-wrap;
}
.audit-box {
  padding: 12px;
  background: #f5f7fa;
  border-radius: 4px;
  word-break: break-word;
  white-space: pre-wrap;
}
.hit-word {
  margin-right: 8px;
}
.user-info {
  display: flex;
  align-items: center;
}
.user-info span {
  margin-left: 8px;
}
.trace-card {
  margin-bottom: 10px;
}
.trace-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 10px;
}
.trace-item {
  margin-top: 6px;
  color: #606266;
}
</style>
