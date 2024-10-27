<template>
  <div class="dashboard-container">
    <div class="dashboard-editor-container">
      <!-- Github 跳转链接 -->
      <github-corner class="github-corner" />
      <!-- 访问量，评论量统计 -->
      <panel-group @handleSetLineChartData="handleSetLineChartData" />
      <!-- 访问量，评论量趋势图 -->
      <el-row style="background:#fff;padding:16px 16px 0;margin-bottom:32px;">
        <line-chart :chart-data="lineChartData" />
      </el-row>
      <el-row style="background:#fff;padding:16px 16px 0;margin-bottom:32px;">
        <china-map />
      </el-row>
    </div>
  </div>
</template>

<script>
import GithubCorner from '@/components/GithubCorner'
import PanelGroup from './dashboard/PanelGroup'
import LineChart from './dashboard/LineChart'
import ChinaMap from '@/components/Echarts/Map'
export default {
  name: 'Dashboard',
  components: {
    GithubCorner,
    PanelGroup,
    LineChart,
    ChinaMap
  },
  data() {
    return {
      defaultType: 'pv',
      lineChartData: {
        xAxisData: [],
        actualData: []
      }
    }
  },
  mounted() {
    this.handleSetLineChartData(this.defaultType)
  },
  methods: {
    handleSetLineChartData(type) {
      this.$mapi.home.getHomePanelDetailDataByType({ type: type }).then(res => {
        const { xAxisData, legendData } = res.data
        this.lineChartData.xAxisData = xAxisData
        this.lineChartData.actualData = legendData
      }).catch(() => {
        this.lineChartData.xAxisData = []
        this.lineChartData.actualData = []
      })
    }
  }
}
</script>

<style rel="stylesheet/scss" lang="scss" scoped>
  .dashboard-editor-container {
    padding: 32px;
    background-color: rgb(240, 242, 245);
    position: relative;

    .github-corner {
      position: absolute;
      top: 0;
      border: 0;
      right: 0;
    }

    .chart-wrapper {
      background: #fff;
      padding: 16px 16px 0;
      margin-bottom: 32px;
    }
  }

  @media (max-width:1024px) {
    .chart-wrapper {
      padding: 8px;
    }
  }
</style>
