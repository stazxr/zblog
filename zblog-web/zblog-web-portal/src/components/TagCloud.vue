<!-- <template>
  <div>
    <div id="tagCloud" :style="{ height: height, width: width }" />
  </div>
</template> -->

<script>
// import * as echarts from 'echarts'
// import 'echarts-wordcloud'
// export default {
//   props: {
//     width: {
//       default: '100%',
//       type: String
//     },
//     height: {
//       default: '100%',
//       type: String
//     },
//     chartData: {
//       type: Array,
//       required: true
//     }
//   },
//   data() {
//     return {
//       chart: null,
//       option: {
//         title: { text: '', x: 'center' },
//         backgroundColor: 'rgba(0, 0, 0, 0)',
//         // tooltip: {
//         //   pointFormat: '{series.name}: <b>{point.percentage:.1f}%</b>'
//         // },
//         series: [
//           {
//             type: 'wordCloud',
//             // shape这个属性虽然可配置，但是在词的数量不太多的时候，效果不明显，它会趋向于画一个椭圆
//             shape: function shapeSquare(theta) {
//               return Math.min(1 / Math.abs(Math.cos(theta)), 1 / Math.abs(Math.sin(theta)))
//             },
//             // 随机生成字体颜色
//             // maskImage: maskImage,
//             left: 'center',
//             top: 'center',
//             right: null,
//             bottom: null,
//             width: '100%',
//             height: '100%',
//             // 用来调整字的大小范围
//             // Text size range which the value in data will be mapped to.
//             // Default to have minimum 12px and maximum 60px size.
//             sizeRange: [12, 48],
//             // 用来调整词的旋转方向，[0,0]--代表着没有角度，也就是词为水平方向，需要设置角度参考注释内容
//             // rotationRange: [-45, 0, 45, 90],
//             // rotationRange: [0, 90],
//             rotationRange: [-90, 90],
//             rotationStep: 45,
//             // 用来调整词之间的距离
//             gridSize: 8,
//             // 允许词太大的时候，超出画布的范围
//             drawOutOfBound: false,
//             // 布局的时候是否有动画
//             layoutAnimation: true,
//             textStyle: {
//               fontFamily: 'sans-serif',
//               fontWeight: 'bold',
//               color: function() {
//                 // Random color
//                 // ['#86D4FF', '#FF8F6C', '#2CF263', '#9FA8F7', '#1274FF', '#E6613D', '#FFC629', '#FFAB2E', '#F78289', '#FF6C96', '#45BFD4', '#4E31FF', '#31FBFB','#86D4FF',  '#BF8AFD', '#FFF500', '#DE58FF',  '#72ED7C', '#0BEEB8','#931CFF',  '#3D25F2', '#F995C8', '#FBE9B4',  '#FF4AB6']
//                 return 'rgb(' + [
//                   Math.round(Math.random() * 160),
//                   Math.round(Math.random() * 160),
//                   Math.round(Math.random() * 160)
//                 ].join(',') + ')'
//               }
//             },
//             emphasis: {
//               focus: 'self',
//               textStyle: {
//                 textShadowBlur: 10,
//                 textShadowColor: '#333'
//               }
//             },
//             data: []
//           }
//         ]
//       }
//     }
//   },
//   watch: {
//     chartData: {
//       handler(val) {
//         this.setOptions(val)
//       }
//     }
//   },
//   created() {
//     this.$nextTick(() => {
//       this.initChart()
//     })
//   },
//   beforeDestroy() {
//     if (!this.chart) {
//       return
//     }
//     this.chart.dispose()
//     this.chart = null
//   },
//   methods: {
//     initChart() {
//       this.chart = echarts.init(document.getElementById('tagCloud'))
//       this.setOptions(this.chartData)
//       this.chart.on('click', function(params) {
//         this.yid = params.data.id
//       })
//     },
//     setOptions(data) {
//       const chartData = []
//       for (let i = 0; i < data.length; i++) {
//         chartData.push({ id: data[i].id, name: data[i].name, value: data[i].value })
//       }
//
//       this.option.series[0].data = chartData
//       this.chart.setOption(this.option)
//     }
//   }
// }
</script>
