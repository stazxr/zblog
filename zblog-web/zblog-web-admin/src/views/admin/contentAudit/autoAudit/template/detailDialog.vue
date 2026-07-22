<template>
  <div>
    <el-dialog
      title="自动审核记录详情"
      :visible.sync="dialogVisible"
      :fullscreen="device === 'mobile'"
      :destroy-on-close="true"
      :close-on-click-modal="true"
      :close-on-press-escape="true"
      :before-close="handleClose"
      append-to-body
      width="850px"
    >
      <el-descriptions title="基础信息" :column="2" border>
        <el-descriptions-item label="审核ID">{{ dataInfo.id }}</el-descriptions-item>
        <el-descriptions-item label="审核场景">{{ dataInfo.scene }}</el-descriptions-item>
        <el-descriptions-item label="用户标识">{{ dataInfo.uid }}</el-descriptions-item>
        <el-descriptions-item label="业务对象">{{ dataInfo.oid }}</el-descriptions-item>
        <el-descriptions-item label="执行耗时">{{ dataInfo.costMs }} ms</el-descriptions-item>
        <el-descriptions-item label="创建时间">{{ dataInfo.createTime }}</el-descriptions-item>
      </el-descriptions>
      <el-divider />
      <el-descriptions title="审核结果" :column="2" border>
        <el-descriptions-item label="决策结果">
          <el-tag v-if="dataInfo.decision === 'PASS'" type="success">通过</el-tag>
          <el-tag v-else-if="dataInfo.decision === 'MODIFY'" type="primary">更新</el-tag>
          <el-tag v-else-if="dataInfo.decision === 'REJECT'" type="danger">拒绝</el-tag>
          <el-tag v-else-if="dataInfo.decision === 'MANUAL'" type="warning">复核</el-tag>
          <span v-else>{{ dataInfo.decision }}</span>
        </el-descriptions-item>
        <el-descriptions-item label="备注">{{ dataInfo.reason }}</el-descriptions-item>
      </el-descriptions>
      <el-divider />
      <el-card shadow="never">
        <div slot="header">原始内容</div>
        <div class="content-box">{{ dataInfo.originalContent }}</div>
      </el-card>
      <el-card shadow="never" style="margin-top:15px">
        <div slot="header">最终内容</div>
        <div class="content-box">{{ dataInfo.finalContent }}</div>
      </el-card>
      <el-divider />
      <el-card v-if="dataInfo.hitWords && dataInfo.hitWords.length" shadow="never">
        <div slot="header">命中敏感词</div>
        <el-tag v-for="item in dataInfo.hitWords" :key="item" type="danger" style="margin-right:8px">{{ item }}</el-tag>
      </el-card>
      <el-divider />
      <el-card shadow="never">
        <div slot="header">Processor执行轨迹</div>
        <el-timeline>
          <el-timeline-item v-for="(item, index) in dataInfo.traces" :key="index" :timestamp="item.costMs + ' ms'">
            <div>
              <b>{{ index + 1 }}. {{ item.processor }}</b>
              <el-tag>{{ item.status }}</el-tag>
            </div>
            <div v-if="item.reason" class="trace-reason">
              原因：{{ item.reason }}
            </div>
            <div v-if="item.hitWords && item.hitWords.length" class="trace-word">
              命中：<el-tag v-for="word in item.hitWords" :key="word" type="danger" size="mini">{{ word }}</el-tag>
            </div>
          </el-timeline-item>
        </el-timeline>
      </el-card>
      <span slot="footer">
        <el-button @click="handleClose">关闭</el-button>
      </span>
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
        uid: '',
        oid: '',
        scene: '',
        originalContent: '',
        finalContent: '',
        decision: '',
        hitWords: null,
        reason: '',
        traces: null,
        createTime: '',
        costMs: ''
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
      this.$mapi.autoAudit.queryAutoAuditRecordDetail({
        auditRecordId: dataId
      }).then(res => {
        const { data } = res
        Object.keys(this.dataInfo).forEach(key => {
          if (key === 'traces') {
            this.dataInfo.traces = data.traces || []
          } else if (key === 'hitWords') {
            this.dataInfo.hitWords = data.hitWords || []
          } else {
            this.dataInfo[key] = data[key] == null || data[key] === '' ? '-' : data[key].toString()
          }
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
.content-box {
  min-height: 60px;
  white-space: pre-wrap;
  word-break: break-all;
  line-height: 24px;
}
.trace-reason {
  margin-top: 8px;
  color: #666;
}
.trace-word {
  margin-top: 8px;
}
</style>
