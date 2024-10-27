<template>
  <el-row :gutter="40" class="panel-group">
    <el-col :xs="12" :sm="12" :lg="6" class="card-panel-col">
      <div class="card-panel" @click="handleSetLineChartData('pv')">
        <div class="card-panel-icon-wrapper icon-pv">
          <svg-icon icon-class="pv" class-name="card-panel-icon" />
        </div>
        <div class="card-panel-description">
          <div class="card-panel-text">
            浏览量
          </div>
          <count-to :start-val="0" :end-val="panelData.pv" :duration="5000" class="card-panel-num" />
        </div>
      </div>
    </el-col>
    <el-col :xs="12" :sm="12" :lg="6" class="card-panel-col">
      <div class="card-panel" @click="handleSetLineChartData('uv')">
        <div class="card-panel-icon-wrapper icon-uv">
          <svg-icon icon-class="uv" class-name="card-panel-icon" />
        </div>
        <div class="card-panel-description">
          <div class="card-panel-text">
            访客数
          </div>
          <count-to :start-val="0" :end-val="panelData.uv" :duration="5000" class="card-panel-num" />
        </div>
      </div>
    </el-col>
    <el-col :xs="12" :sm="12" :lg="6" class="card-panel-col">
      <div class="card-panel" @click="handleSetLineChartData('uu')">
        <div class="card-panel-icon-wrapper icon-uu">
          <svg-icon icon-class="uu" class-name="card-panel-icon" />
        </div>
        <div class="card-panel-description">
          <div class="card-panel-text">
            用户数
          </div>
          <count-to :start-val="0" :end-val="panelData.uu" :duration="5000" class="card-panel-num" />
        </div>
      </div>
    </el-col>
    <el-col :xs="12" :sm="12" :lg="6" class="card-panel-col">
      <div class="card-panel" @click="handleSetLineChartData('av')">
        <div class="card-panel-icon-wrapper icon-av">
          <svg-icon icon-class="av" class-name="card-panel-icon" />
        </div>
        <div class="card-panel-description">
          <div class="card-panel-text">
            阅读量
          </div>
          <count-to :start-val="0" :end-val="panelData.av" :duration="5000" class="card-panel-num" />
        </div>
      </div>
    </el-col>
  </el-row>
</template>

<script>
import CountTo from 'vue-count-to'
export default {
  name: 'PanelGroup',
  components: {
    CountTo
  },
  data() {
    return {
      panelData: {
        pv: 0,
        uv: 0,
        uu: 0,
        av: 0
      }
    }
  },
  mounted() {
    this.$nextTick(() => {
      this.getPanelData()
    })
  },
  methods: {
    getPanelData() {
      this.$mapi.home.getHomePanelDataCount().then(res => {
        const { pv, uv, uu, av } = res.data
        this.panelData.pv = pv || 0
        this.panelData.uv = uv || 0
        this.panelData.uu = uu || 0
        this.panelData.av = av || 0
      }).catch(() => {
        this.panelData.pv = 0
        this.panelData.uv = 0
        this.panelData.uu = 0
        this.panelData.av = 0
      })
    },
    handleSetLineChartData(type) {
      this.$emit('handleSetLineChartData', type)
    }
  }
}
</script>

<style lang="scss" scoped>
.panel-group {
  margin-top: 18px;

  .card-panel-col {
    margin-bottom: 32px;
  }

  .card-panel {
    height: 108px;
    cursor: pointer;
    font-size: 12px;
    position: relative;
    overflow: hidden;
    color: #666;
    background: #fff;
    box-shadow: 4px 4px 40px rgba(0, 0, 0, .05);
    border-color: rgba(0, 0, 0, .05);

    &:hover {
      .card-panel-icon-wrapper {
        color: #fff;
      }

      .icon-pv {
        background: #34bfa3;
      }

      .icon-uv {
        background: #34bfa3;
      }

      .icon-uu {
        background: #34bfa3;
      }

      .icon-av {
        background: #34bfa3;
      }
    }

    .icon-pv {
      color: #34bfa3;
    }

    .icon-uv {
      color: #34bfa3;
    }

    .icon-uu {
      color: #34bfa3;
    }

    .icon-av {
      color: #34bfa3;
    }

    .card-panel-icon-wrapper {
      float: left;
      margin: 14px 0 0 14px;
      padding: 16px;
      transition: all 0.38s ease-out;
      border-radius: 6px;
    }

    .card-panel-icon {
      float: left;
      font-size: 48px;
    }

    .card-panel-description {
      float: right;
      font-weight: bold;
      margin: 26px 26px 26px 0;

      .card-panel-text {
        line-height: 18px;
        color: rgba(0, 0, 0, 0.45);
        font-size: 16px;
        margin-bottom: 12px;
      }

      .card-panel-num {
        font-size: 20px;
      }
    }
  }
}

@media (max-width:550px) {
  .card-panel-description {
    display: none;
  }

  .card-panel-icon-wrapper {
    float: none !important;
    width: 100%;
    height: 100%;
    margin: 0 !important;

    .svg-icon {
      display: block;
      margin: 14px auto !important;
      float: none !important;
    }
  }
}
</style>
