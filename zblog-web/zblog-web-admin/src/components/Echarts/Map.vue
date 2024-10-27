<template>
  <div :class="className" :style="{height:height,width:width}" />
</template>

<script>
import echarts from 'echarts'
import china from '@/assets/map/china.json'

require('echarts/theme/macarons') // echarts theme
import { debounce } from '@/utils'

export default {
  name: 'Map',
  props: {
    className: {
      type: String,
      default: 'chart'
    },
    width: {
      type: String,
      default: '100%'
    },
    height: {
      type: String,
      default: '700px'
    }
  },
  data() {
    return {
      mapData: [],
      chart: null
    }
  },
  mounted() {
    this.$nextTick(() => {
      this.initChartData()
    })

    echarts.registerMap('china', china)
    this.__resizeHandler = debounce(() => {
      if (this.chart) {
        this.chart.resize()
      }
    }, 100)
    window.addEventListener('resize', this.__resizeHandler)
  },
  beforeDestroy() {
    if (!this.chart) {
      return
    }
    window.removeEventListener('resize', this.__resizeHandler)
    this.chart.dispose()
    this.chart = null
  },
  methods: {
    initChartData() {
      this.$mapi.home.getHomePanelVisitorAreaCount().then(res => {
        this.initChart(res.data)
      }).catch(e => {
        console.log('init map chart eor', e)
        this.initChart([])
      })
    },
    initChart(data) {
      data = JSON.parse(JSON.stringify(data))
      this.chart = echarts.init(this.$el, 'macarons')
      this.chart.setOption({
        title: {
          text: '访客地域分布'
        },
        backgroundColor: '#ffffff',
        tooltip: {
          show: true,
          trigger: 'item',
          axisPointer: {
            // 默认为直线，可选为：'line' | 'shadow'
            type: 'shadow'
          },
          alwaysShowContent: false,
          backgroundColor: '#000',
          borderColor: 'rgba(0, 0, 0, 0.16)',
          triggerOn: 'mousemove',
          // 鼠标是否可进入提示框浮层中
          enterable: true,
          textStyle: {
            fontSize: '12',
            overflow: 'break'
          },
          formatter: function(params) {
            if (isNaN(params.value)) {
              return `<div> ` + params.name + `：` + '0 人' + `</div>`
            } else {
              return `<div> ` + params.name + `：` + params.value + ' 人' + `</div>`
            }
          }
        },
        visualMap: {
          show: true,
          title: '11',
          type: 'piecewise',
          left: 50,
          bottom: 50,
          showLabel: true,
          itemWidth: 10,
          itemHeight: 10,
          inverse: true,
          // lt:小于; lte:小于等于; gt:大于; gte:大于等于;
          pieces: [
            {
              gte: 1,
              lt: 5,
              label: '1-5 人',
              color: '#bef5cb'
            },
            {
              gte: 5,
              lte: 15,
              label: '5-15 人',
              color: '#85e89d'
            },
            {
              gt: 15,
              lte: 30,
              label: '15-30 人',
              color: '#34d058'
            },
            {
              gt: 30,
              lte: 50,
              label: '30-50 人',
              color: '#28a745'
            },
            {
              gt: 50,
              lte: 100,
              label: '50-100 人',
              color: '#22863a'
            },
            {
              gt: 100,
              lte: 200,
              label: '100-200 人',
              color: '#176f2c'
            },
            {
              gt: 200,
              label: '>200 人',
              color: '#144620'
            }
          ]
        },
        geo: {
          map: 'china',
          // 长宽比,0.75的含义为宽是高的0.75,假如高为100,宽就只有75;0.5的含义就是宽只有高的一半,假如高为100,宽就只有50
          aspectScale: 0.8,
          // 视觉比例大小,1.2即为原有大小的1.2倍
          zoom: 1.2,
          // 是否开启鼠标缩放和平移漫游。默认不开启。可以不用设置,如果只想要开启缩放或者平移，可以设置成 'scale' 或者 'move'。
          roam: false,
          top: '10%',
          label: {
            // 通常状态下的样式
            normal: {
              show: true,
              textStyle: {
                color: '#ffffff'
              }
            },
            // 鼠标放上去的样式
            emphasis: {
              textStyle: {
                color: '#333333'
              }
            }
          },
          // 地图区域的样式设置
          itemStyle: {
            normal: {
              areaColor: 'rgba(0, 0, 0, 0.2)',
              borderColor: 'rgba(0, 0, 0, 0.1)',
              borderWidth: 1
            },
            // 鼠标放上去高亮的样式
            emphasis: {
              areaColor: 'rgba(0, 0, 0, 0.2)',
              borderWidth: 0
            }
          }
        },
        series: [
          {
            selectedMode: false,
            geoIndex: 0,
            type: 'map',
            data: data
          }
        ]
      })
    }
  }
}
</script>
