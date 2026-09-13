<template>
  <div>
    <el-dialog
      title="评论详情"
      :visible.sync="dialogVisible"
      :fullscreen="device === 'mobile'"
      :destroy-on-close="true"
      :close-on-click-modal="true"
      :close-on-press-escape="true"
      :before-close="handleClose"
      append-to-body
      width="520px"
    >
      <!-- 基础信息 -->
      <el-descriptions
        direction="vertical"
        :column="4"
        border
      >
        <!-- 1 -->
        <el-descriptions-item label="评论序列" :span="2">
          {{ dataInfo.id }}
        </el-descriptions-item>

        <el-descriptions-item label="审核状态" :span="2">
          <el-tag
            v-if="dataInfo.status === '0'"
            type="primary"
          >
            待审核
          </el-tag>
          <el-tag
            v-else-if="dataInfo.status === '1'"
            type="success"
          >
            审核通过
          </el-tag>
          <el-tag
            v-else-if="dataInfo.status === '2'"
            type="danger"
          >
            审核拒绝
          </el-tag>
          <el-tag
            v-else-if="dataInfo.status === '3'"
            type="warning"
          >
            待复核
          </el-tag>
          <span v-else>-</span>
        </el-descriptions-item>

        <!-- 2 -->
        <el-descriptions-item label="评论类型" :span="2">
          {{ dataInfo.typeName }}
        </el-descriptions-item>

        <el-descriptions-item label="评论级别" :span="2">
          <el-tag
            v-if="dataInfo.level === '1'"
            size="small"
          >
            一级评论
          </el-tag>
          <el-tag
            v-else-if="dataInfo.level === '2'"
            size="small"
          >
            回复
          </el-tag>
          <span v-else>-</span>
        </el-descriptions-item>

        <!-- 3 -->
        <el-descriptions-item label="评论对象" :span="4">
          {{ dataInfo.objectTitle }}
        </el-descriptions-item>

        <!-- 4 -->
        <el-descriptions-item label="创建用户" :span="2">
          <div class="user-info">
            <el-avatar
              :size="28"
              :src="dataInfo.userAvatar"
              icon="el-icon-user-solid"
            />
            <span>{{ dataInfo.userNickname }}</span>
          </div>
        </el-descriptions-item>

        <el-descriptions-item label="创建时间" :span="2">
          {{ dataInfo.createTime }}
        </el-descriptions-item>

        <!-- 5 -->
        <el-descriptions-item label="来源 IP" :span="2">
          {{ dataInfo.ipAddress }}
        </el-descriptions-item>

        <el-descriptions-item label="IP 归属" :span="2">
          {{ dataInfo.ipSource }}
        </el-descriptions-item>
      </el-descriptions>

      <!-- 评论内容 -->
      <el-divider content-position="left">
        评论内容
      </el-divider>

      <el-descriptions
        direction="vertical"
        :column="4"
        border
      >
        <el-descriptions-item label="评论内容" :span="4">
          <div
            class="comment-content"
            v-html="dataInfo.content || '-'"
          />
        </el-descriptions-item>

        <el-descriptions-item
          v-if="dataInfo.level === '2'"
          label="回复对象"
          :span="4"
        >
          <div
            v-if="dataInfo.replyUserNickname !== '-'"
            class="user-info"
          >
            <el-avatar
              :size="28"
              :src="dataInfo.replyUserAvatar"
              icon="el-icon-user-solid"
            />
            <span>{{ dataInfo.replyUserNickname }}</span>
          </div>
          <span v-else>-</span>
        </el-descriptions-item>
      </el-descriptions>

      <!-- 审核信息 -->
      <el-divider content-position="left">
        审核信息
      </el-divider>

      <el-descriptions
        direction="vertical"
        :column="4"
        border
      >
        <el-descriptions-item label="审核结果" :span="4">
          <el-tag
            v-if="dataInfo.status === '1'"
            type="success"
          >
            审核通过
          </el-tag>
          <el-tag
            v-else-if="dataInfo.status === '2'"
            type="danger"
          >
            审核拒绝
          </el-tag>
          <el-tag
            v-else-if="dataInfo.status === '3'"
            type="warning"
          >
            待复核
          </el-tag>
          <el-tag
            v-else-if="dataInfo.status === '0'"
            type="primary"
          >
            待审核
          </el-tag>
          <span v-else>-</span>
        </el-descriptions-item>

        <el-descriptions-item label="审核备注" :span="4">
          <div class="audit-box">
            {{ dataInfo.auditReason }}
          </div>
        </el-descriptions-item>

        <el-descriptions-item label="审核用户" :span="2">
          {{ dataInfo.auditUsername }}
        </el-descriptions-item>

        <el-descriptions-item label="审核时间" :span="2">
          {{ dataInfo.auditTime }}
        </el-descriptions-item>
      </el-descriptions>

      <!-- 自动审核报告 -->
      <template v-if="dataInfo.auditRecord">
        <el-divider content-position="left">
          自动审核报告
        </el-divider>

        <el-descriptions
          direction="vertical"
          :column="4"
          border
        >
          <!-- 审核决策 -->
          <el-descriptions-item label="审核决策" :span="2">
            <el-tag
              v-if="dataInfo.auditRecord.decision === 'PASS'"
              type="success"
            >
              PASS
            </el-tag>
            <el-tag
              v-else-if="dataInfo.auditRecord.decision === 'MODIFY'"
              type="primary"
            >
              MODIFY
            </el-tag>
            <el-tag
              v-else-if="dataInfo.auditRecord.decision === 'REJECT'"
              type="danger"
            >
              REJECT
            </el-tag>
            <el-tag
              v-else-if="dataInfo.auditRecord.decision === 'MANUAL'"
              type="warning"
            >
              MANUAL
            </el-tag>
            <span v-else>-</span>
          </el-descriptions-item>

          <!-- 审核耗时 -->
          <el-descriptions-item label="审核耗时" :span="2">
            {{ dataInfo.auditRecord.costMs || 0 }} ms
          </el-descriptions-item>

          <!-- 原始内容 -->
          <el-descriptions-item label="原始内容" :span="4">
            <div class="audit-box">
              {{ dataInfo.auditRecord.originalContent || '-' }}
            </div>
          </el-descriptions-item>

          <!-- 最终内容 -->
          <el-descriptions-item label="最终内容" :span="4">
            <div class="audit-box">
              {{ dataInfo.auditRecord.finalContent || '-' }}
            </div>
          </el-descriptions-item>

          <!-- 命中关键词 -->
          <el-descriptions-item label="命中关键词" :span="4">
            <template
              v-if="
                dataInfo.auditRecord.hitWords &&
                  dataInfo.auditRecord.hitWords.length
              "
            >
              <el-tag
                v-for="(item, index) in dataInfo.auditRecord.hitWords"
                :key="index"
                type="danger"
                class="hit-word"
              >
                {{ item }}
              </el-tag>
            </template>
            <span v-else>-</span>
          </el-descriptions-item>

          <!-- 审核备注 -->
          <el-descriptions-item label="审核备注" :span="4">
            {{ dataInfo.auditRecord.reason || '-' }}
          </el-descriptions-item>
        </el-descriptions>

        <!-- Processor 执行过程 -->
        <el-divider
          v-if="auditTraces.length"
          content-position="left"
        >
          审核执行过程
        </el-divider>

        <el-collapse v-if="auditTraces.length">
          <el-collapse-item name="trace">
            <template slot="title">
              查看 Processor 执行详情（{{ auditTraces.length }}）
            </template>

            <el-card
              v-for="(trace, index) in auditTraces"
              :key="index"
              shadow="never"
              class="trace-card"
            >
              <div class="trace-header">
                <span>
                  {{ index + 1 }}. {{ trace.processor || '-' }}
                </span>

                <el-tag
                  size="small"
                  :type="getTraceStatusType(trace.status)"
                >
                  {{ trace.status || '-' }}
                </el-tag>
              </div>

              <div class="trace-item">
                <span class="trace-label">耗时：</span>
                {{ trace.costMs == null ? '-' : trace.costMs + ' ms' }}
              </div>

              <div class="trace-item">
                <span class="trace-label">敏感词：</span>
                {{ formatHitWords(trace.hitWords) }}
              </div>

              <div class="trace-item">
                <span class="trace-label">备注：</span>
                {{ trace.reason || '-' }}
              </div>
            </el-card>
          </el-collapse-item>
        </el-collapse>
      </template>

      <!-- 访问信息 -->
      <el-divider content-position="left">
        访问信息
      </el-divider>

      <el-collapse>
        <el-collapse-item title="查看访客及设备信息" name="visitor">
          <el-descriptions
            direction="vertical"
            :column="4"
            border
          >
            <el-descriptions-item label="访客ID" :span="4">
              {{ dataInfo.visitorId }}
            </el-descriptions-item>

            <el-descriptions-item label="访问设备" :span="4">
              {{ dataInfo.deviceId }}
            </el-descriptions-item>

            <el-descriptions-item label="访问代理" :span="4">
              <div class="user-agent">
                {{ dataInfo.userAgent }}
              </div>
            </el-descriptions-item>
          </el-descriptions>
        </el-collapse-item>
      </el-collapse>
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
      dataInfo: this.createDefaultData()
    }
  },

  computed: {
    /**
     * 当前设备
     */
    device() {
      return this.$store.state.app.device
    },

    /**
     * Processor 执行轨迹
     */
    auditTraces() {
      if (!this.dataInfo.auditRecord) {
        return []
      }

      return this.dataInfo.auditRecord.traces || []
    }
  },

  methods: {
    /**
     * 创建默认数据
     *
     * @return {Object} 默认详情数据
     */
    createDefaultData() {
      return {
        id: '',
        content: '',
        type: '',
        typeName: '',
        level: '',
        objectId: '',
        objectTitle: '',
        userId: '',
        userNickname: '',
        userAvatar: '',
        userWebsite: '',
        replyUserId: '',
        replyUserNickname: '',
        replyUserAvatar: '',
        replyUserWebsite: '',
        visitorId: '',
        replyVisitorId: '',
        ipAddress: '',
        ipSource: '',
        userAgent: '',
        status: '',
        auditReason: '',
        auditUsername: '',
        auditTime: '',
        createTime: '',
        auditRecord: null
      }
    },

    /**
     * 初始化详情
     *
     * @param {String|Number} dataId 评论ID
     */
    initData(dataId) {
      this.$nextTick(() => {
        this.queryDetail(dataId)
      })
    },

    /**
     * 查询评论详情
     *
     * @param {String|Number} dataId 评论ID
     */
    queryDetail(dataId) {
      this.$mapi.comment.queryCommentDetail({
        commentId: dataId
      }).then(res => {
        const data = res.data || {}

        const defaultData = this.createDefaultData()

        Object.keys(defaultData).forEach(key => {
          if (key === 'auditRecord') {
            this.dataInfo.auditRecord = data.auditRecord || null
            return
          }

          const value = data[key]

          if (value === null || value === undefined || value === '') {
            this.dataInfo[key] = '-'
          } else {
            this.dataInfo[key] = String(value)
          }
        })
      }).catch(() => {
        this.doClose()
      })
    },

    /**
     * 获取 Processor 状态标签类型
     *
     * @param {String} status 状态
     * @return {String} Element 标签类型
     */
    getTraceStatusType(status) {
      if (status === 'PASS') {
        return 'success'
      }

      if (status === 'REJECT') {
        return 'danger'
      }

      if (status === 'MODIFY') {
        return 'primary'
      }

      if (status === 'MANUAL') {
        return 'warning'
      }

      return 'info'
    },

    /**
     * 格式化命中关键词
     *
     * @param {Array|String} hitWords 命中关键词
     * @return {String} 格式化后的关键词
     */
    formatHitWords(hitWords) {
      if (Array.isArray(hitWords)) {
        return hitWords.length ? hitWords.join('、') : '-'
      }

      return hitWords || '-'
    },

    /**
     * 关闭弹窗
     */
    handleClose() {
      this.doClose()
    },

    /**
     * 关闭并清理数据
     */
    doClose() {
      this.dataInfo = this.createDefaultData()
      this.$emit('showDetailDone')
    }
  }
}
</script>

<style scoped>
.comment-content {
  max-height: 240px;
  padding: 10px 12px;
  overflow-y: auto;
  background: #f5f7fa;
  border-radius: 4px;
  color: #606266;
  line-height: 24px;
  word-break: break-word;
  white-space: pre-wrap;
}

/* v-html 内部元素需要使用 deep 选择器 */
.comment-content >>> img {
  max-width: 48px;
  max-height: 48px;
  margin: 2px 4px;
  border-radius: 4px;
  vertical-align: middle;
  object-fit: contain;
}

.audit-box {
  max-height: 180px;
  padding: 12px;
  overflow-y: auto;
  background: #f5f7fa;
  border-radius: 4px;
  color: #606266;
  line-height: 22px;
  word-break: break-word;
  white-space: pre-wrap;
}

.hit-word {
  margin-right: 8px;
  margin-bottom: 6px;
}

.user-info {
  display: flex;
  align-items: center;
  min-height: 28px;
}

.user-info span {
  margin-left: 8px;
}

.trace-card {
  margin-bottom: 10px;
}

.trace-card:last-child {
  margin-bottom: 0;
}

.trace-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  margin-bottom: 10px;
}

.trace-item {
  margin-top: 6px;
  color: #606266;
  line-height: 20px;
  word-break: break-word;
}

.trace-label {
  color: #909399;
}

.user-agent {
  max-height: 120px;
  overflow-y: auto;
  line-height: 20px;
  word-break: break-all;
}
</style>
