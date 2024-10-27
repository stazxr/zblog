<template>
  <div v-loading="!show" element-loading-text="数据加载中..." :style="!show ? 'height: 500px' : 'height: 100%'" class="app-container">
    <div v-if="show">
      <el-card class="box-card">
        <div style="color: #666;font-size: 13px;">
          <svg-icon icon-class="system" style="margin-right: 5px" />
          <span>
            系统：{{ data.sys.os }}
          </span>
          <span>
            IP：{{ data.sys.serverIp }}
          </span>
          <span>
            项目已不间断运行：{{ data.sys.runtime }}
          </span>
          <i class="el-icon-refresh" style="margin-left: 40px" @click="init" />
        </div>
      </el-card>

      <el-card class="box-card">
        <div slot="header" class="clearfix">
          <span style="font-weight: bold;color: #666;font-size: 15px">状态</span>
        </div>
        <div>
          <el-col :xs="24" :sm="24" :md="6" :lg="6" :xl="6" style="margin-bottom: 10px">
            <div class="title">CPU使用率</div>
            <el-tooltip placement="top-end">
              <div slot="content" style="font-size: 12px;">
                <div style="padding: 3px;">
                  {{ data.cpu.name }}
                </div>
                <div style="padding: 3px">
                  {{ data.cpu.packageNum }}个物理CPU
                </div>
                <div style="padding: 3px">
                  {{ data.cpu.coreNum }}个物理核
                </div>
                <div style="padding: 3px">
                  {{ data.cpu.logicNum }}个逻辑CPU
                </div>
              </div>
              <div class="content">
                <el-progress type="dashboard" :percentage="parseFloat(data.cpu.used)" />
              </div>
            </el-tooltip>
            <div class="footer">{{ data.cpu.coreNum }}核CPU </div>
          </el-col>
          <el-col :xs="24" :sm="24" :md="6" :lg="6" :xl="6" style="margin-bottom: 10px">
            <div class="title">内存使用率</div>
            <el-tooltip placement="top-end">
              <div slot="content" style="font-size: 12px;">
                <div style="padding: 3px;">
                  总量：{{ data.memory.total }}
                </div>
                <div style="padding: 3px">
                  已用：{{ data.memory.used }}
                </div>
                <div style="padding: 3px">
                  空闲：{{ data.memory.available }}
                </div>
              </div>
              <div class="content">
                <el-progress type="dashboard" :percentage="parseFloat(data.memory.usageRate)" />
              </div>
            </el-tooltip>
            <div class="footer">{{ data.memory.used }} / {{ data.memory.total }}</div>
          </el-col>
          <el-col :xs="24" :sm="24" :md="6" :lg="6" :xl="6" style="margin-bottom: 10px">
            <div class="title">交换区使用率</div>
            <el-tooltip placement="top-end">
              <div slot="content" style="font-size: 12px;">
                <div style="padding: 3px;">
                  总量：{{ data.swap.total }}
                </div>
                <div style="padding: 3px">
                  已用：{{ data.swap.used }}
                </div>
                <div style="padding: 3px">
                  空闲：{{ data.swap.available }}
                </div>
              </div>
              <div class="content">
                <el-progress type="dashboard" :percentage="parseFloat(data.swap.usageRate)" />
              </div>
            </el-tooltip>
            <div class="footer">{{ data.swap.used }} / {{ data.swap.total }}</div>
          </el-col>
          <el-col :xs="24" :sm="24" :md="6" :lg="6" :xl="6" style="margin-bottom: 10px">
            <div class="title">JVM使用率</div>
            <el-tooltip placement="top-end">
              <div slot="content" style="font-size: 12px;">
                <div style="padding: 3px;">
                  最大：{{ data.jvm.max }}
                </div>
                <div style="padding: 3px;">
                  总量：{{ data.jvm.total }}
                </div>
                <div style="padding: 3px">
                  已用：{{ data.jvm.used }}
                </div>
                <div style="padding: 3px">
                  空闲：{{ data.jvm.available }}
                </div>
              </div>
              <div class="content">
                <el-progress type="dashboard" :percentage="parseFloat(data.jvm.usageRate)" />
              </div>
            </el-tooltip>
            <div class="footer">{{ data.jvm.used }} / {{ data.jvm.total }}</div>
          </el-col>
        </div>
      </el-card>

      <el-card class="box-card">
        <div slot="header" class="clearfix">
          <span style="font-weight: bold;color: #666;font-size: 15px">文件系统</span>
        </div>
        <div>
          <el-col :xs="24" :sm="24" :md="24" :lg="24" :xl="24" style="margin-bottom: 10px">
            <el-table :data="data.sysFiles" border style="width: 100%">
              <el-table-column :show-overflow-tooltip="true" prop="dirName" label="盘符路径" align="center" />
              <el-table-column :show-overflow-tooltip="true" prop="typeName" label="文件系统" align="center" />
              <el-table-column :show-overflow-tooltip="true" prop="sysTypeName" label="盘符类型" align="center" />
              <el-table-column :show-overflow-tooltip="true" prop="total" label="总大小" align="center" />
              <el-table-column :show-overflow-tooltip="true" prop="free" label="可用大小" align="center" />
              <el-table-column :show-overflow-tooltip="true" prop="used" label="已用大小" align="center" />
              <el-table-column :show-overflow-tooltip="true" prop="usage" label="已用百分比" align="center">
                <template slot-scope="scope">
                  <span>{{ scope.row['usage'] }}%</span>
                </template>
              </el-table-column>
            </el-table>
          </el-col>
        </div>
      </el-card>

      <div>
        <el-row :gutter="6">
          <el-col :xs="24" :sm="24" :md="12" :lg="12" :xl="12" style="margin-bottom: 10px">
            <el-card class="box-card">
              <div slot="header" class="clearfix">
                <span style="font-weight: bold;color: #666;font-size: 15px">CPU使用率监控</span>
              </div>
              <div>
                <v-chart :options="cpuInfo" />
              </div>
            </el-card>
          </el-col>
          <el-col :xs="24" :sm="24" :md="12" :lg="12" :xl="12" style="margin-bottom: 10px">
            <el-card class="box-card">
              <div slot="header" class="clearfix">
                <span style="font-weight: bold;color: #666;font-size: 15px">内存使用率监控</span>
              </div>
              <div>
                <v-chart :options="memoryInfo" />
              </div>
            </el-card>
          </el-col>
        </el-row>
      </div>
    </div>
  </div>
</template>

<script>
import ECharts from 'vue-echarts'
import 'echarts/lib/chart/line'
import 'echarts/lib/component/polar'
export default {
  name: 'Server',
  components: {
    'v-chart': ECharts
  },
  data() {
    return {
      show: false,
      monitor: null,
      data: {
        sys: {
          os: '',
          serverIp: '',
          runtime: ''
        },
        cpu: {
          name: '',
          packageNum: '',
          coreNum: '',
          logicNum: '',
          used: ''
        },
        memory: {
          total: '',
          available: '',
          used: '',
          usageRate: ''
        },
        jvm: {
          max: '',
          total: '',
          available: '',
          used: '',
          usageRate: ''
        },
        swap: {
          total: '',
          available: '',
          used: '',
          usageRate: ''
        },
        sysFiles: []
      },
      cpuInfo: {
        tooltip: {
          trigger: 'axis'
        },
        xAxis: {
          type: 'category',
          boundaryGap: false,
          data: []
        },
        yAxis: {
          type: 'value',
          min: 0,
          max: 100,
          interval: 20
        },
        series: [{
          data: [],
          type: 'line',
          areaStyle: {
            normal: {
              color: 'rgb(32, 160, 255)'
            }
          },
          itemStyle: {
            normal: {
              color: '#6fbae1',
              lineStyle: {
                color: '#6fbae1'
              }
            }
          }
        }]
      },
      memoryInfo: {
        tooltip: {
          trigger: 'axis'
        },
        xAxis: {
          type: 'category',
          boundaryGap: false,
          data: []
        },
        yAxis: {
          type: 'value',
          min: 0,
          max: 100,
          interval: 20
        },
        series: [{
          data: [],
          type: 'line',
          areaStyle: {
            normal: {
              color: 'rgb(32, 160, 255)' // 改变区域颜色
            }
          },
          itemStyle: {
            normal: {
              color: '#6fbae1',
              lineStyle: {
                color: '#6fbae1' // 改变折线颜色
              }
            }
          }
        }]
      }
    }
  },
  created() {
    this.init()
    this.monitor = window.setInterval(() => {
      setTimeout(() => {
        this.init()
      }, 2)
    }, 10000)
  },
  destroyed() {
    clearInterval(this.monitor)
  },
  methods: {
    init() {
      this.$mapi.server.queryServerData().then(res => {
        const { data } = res
        this.data = data
        this.show = true

        // set data
        this.cpuInfo.xAxis.data.push(data.time)
        this.memoryInfo.xAxis.data.push(data.time)
        this.cpuInfo.series[0].data.push(parseFloat(data.cpu.used))
        this.memoryInfo.series[0].data.push(parseFloat(data.memory.usageRate))
        if (this.cpuInfo.xAxis.data.length >= 8) {
          this.cpuInfo.xAxis.data.shift()
          this.memoryInfo.xAxis.data.shift()
          this.cpuInfo.series[0].data.shift()
          this.memoryInfo.series[0].data.shift()
        }
      })
    }
  }
}
</script>

<style rel="stylesheet/scss" lang="scss" scoped>
 ::v-deep .box-card {
    margin-bottom: 5px;
    span {
      margin-right: 28px;
    }
    .el-icon-refresh {
      margin-right: 10px;
      float: right;
      cursor:pointer;
    }
  }
  .cpu, .memory, .swap, .disk  {
    width: 20%;
    float: left;
    padding-bottom: 20px;
    margin-right: 5%;
  }
 .title {
   text-align: center;
   font-size: 15px;
   font-weight: 500;
   color: #999;
   margin-bottom: 16px;
 }
 .footer {
    text-align: center;
    font-size: 15px;
    font-weight: 500;
    color: #999;
    margin-top: -5px;
    margin-bottom: 10px;
  }
  .content {
    text-align: center;
    margin-top: 5px;
    margin-bottom: 5px;
  }
</style>
