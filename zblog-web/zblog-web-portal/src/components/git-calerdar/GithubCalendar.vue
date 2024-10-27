<template>
  <div id="github_container" class="github_container">
    <!-- 加载中 -->
    <div id="github_loading" class="github_loading">
      <svg viewBox="0 0 50 50" style="enable-background:new 0 0 50 50;" xml:space="preserve">
        <path :fill="loadColor" d="M25.251,6.461c-10.318,0-18.683,8.365-18.683,18.683h4.068c0-8.071,6.543-14.615,14.615-14.615V6.461z" transform="rotate(275.098 25 25)">
          <animateTransform attributeType="xml" attributeName="transform" type="rotate" from="0 25 25" to="360 25 25" dur="0.6s" repeatCount="indefinite" />
        </path>
      </svg>
    </div>
    <!-- 日历数据 -->
    <div class="github_calendar">
      <div class="border py-2 graph-before-activity-overview">
        <div class="mx-md-2 mx-3 d-flex flex-column flex-items-end flex-xl-items-center overflow-hidden pt-1 is-graph-loading graph-canvas height-full text-center github_calendar_graph">
          <div id="git-calendar-canvas-box">
            <canvas id="git-canvas" style="animation: none;" width="680" height="110" />
          </div>
        </div>
        <div id="git_tooltip_container" />
        <div class="contrib-footer clearfix mt-1 mx-3 px-3 pb-1">
          <div class="float-left text-gray">
            数据来源
            <a :href="githubUrl" target="blank">@{{ githubUser }}</a>
          </div>
          <div class="contrib-legend text-gray">
            Less
            <ul class="legend">
              <li v-for="(item, index) in gitColor" :key="index" :style="{ 'background-color': item }" />
            </ul>More
          </div>
        </div>
      </div>
    </div>
    <!-- 数据汇总 -->
    <div style="display: flex; width: 100%;">
      <div class="contrib-column contrib-column-first table-column">
        <span class="text-muted">过去一年提交</span>
        <span class="contrib-number">{{ lastYearCount }}</span>
        <span class="text-muted">{{ lastYearStart }}&nbsp;-&nbsp;{{ lastYearEnd }}</span>
      </div>
      <div class="contrib-column table-column">
        <span class="text-muted">最近一月提交</span>
        <span class="contrib-number">{{ lastMonthCount }}</span>
        <span class="text-muted">{{ lastMonthStart }}&nbsp;-&nbsp;{{ lastMonthEnd }}</span>
      </div>
      <div class="contrib-column table-column">
        <span class="text-muted">最近一周提交</span>
        <span class="contrib-number">{{ lastWeekCount }}</span>
        <span class="text-muted">{{ lastWeekStart }}&nbsp;-&nbsp;{{ lastWeekEnd }}</span>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: 'GithubCalendar',
  data() {
    return {
      gitColorPurple: [
        '#ebedf0',
        '#fdcdec',
        '#fc9bd9',
        '#fa6ac5',
        '#f838b2',
        '#f5089f',
        '#92055e',
        '#540336',
        '#30021f'
      ],
      gitColorGreen: [
        '#ebedf0',
        '#dcffe4',
        '#bef5cb',
        '#85e89d',
        '#34d058',
        '#28a745',
        '#22863a',
        '#176f2c',
        '#144620'
      ],
      gitColorBlue: [
        '#ebedf0',
        '#c8e1ff',
        '#79b8ff',
        '#2188ff',
        '#0366d6',
        '#005cc5',
        '#044289',
        '#032f62',
        '#05264c'
      ],
      // 数据来源
      githubUrl: '',
      // 数据所属用户
      githubUser: '',
      // 年度贡献量列表
      contributions: [],
      positionPlusData: [],
      // 近一年的数据
      lastYearCount: 0,
      lastYearStart: '',
      lastYearEnd: '',
      // 近一月的数据
      lastMonthCount: 0,
      lastMonthStart: '',
      lastMonthEnd: '',
      // 近一周的数据
      lastWeekCount: 0,
      lastWeekStart: '',
      lastWeekEnd: '',
      // 月份
      months: []
    }
  },
  computed: {
    gitColor() {
      return this.gitColorPurple
    },
    loadColor() {
      return this.gitColor[4]
    }
  },
  created() {
    this.$nextTick(() => {
      this.initGithubData()
    })

    window.onresize = () => {
      this.responsiveChart()
    }
    window.onscroll = () => {
      if (document.querySelector('.github_message')) {
        const git_tooltip_container = document.getElementById('git_tooltip_container')
        git_tooltip_container.innerHTML = ''
      }
    }
  },
  methods: {
    initGithubData() {
      const username = 'stazxr'
      this.$mapi.portal.queryGithubCalendarData({ username: username }).then(({ data, code, message }) => {
        if (code !== 200) {
          this.$toast({ type: 'error', message: message })
        }

        // 解析数据
        this.githubUrl = data['githubUrl']
        this.githubUser = data['githubUser']
        this.contributions = data['contributions']
        this.lastYearCount = data['lastYearCount']
        this.lastYearStart = data['lastYearStart']
        this.lastYearEnd = data['lastYearEnd']
        this.lastMonthCount = data['lastMonthCount']
        this.lastMonthStart = data['lastMonthStart']
        this.lastMonthEnd = data['lastMonthEnd']
        this.lastWeekCount = data['lastWeekCount']
        this.lastWeekStart = data['lastWeekStart']
        this.lastWeekEnd = data['lastWeekEnd']
        this.months = data['months']

        // 回显表格
        document.getElementById('github_loading').innerHTML = ''
        this.responsiveChart()
      }).catch(e => {
        console.error('贡献日历数据显示异常', e)
        this.$toast({ type: 'error', message: '贡献日历数据显示异常' })
      }).finally(_ => {
      })
    },
    responsiveChart() {
      const git_tooltip_container = document.getElementById('git_tooltip_container')
      const canvas = document.getElementById('git-canvas')
      const ctx = canvas.getContext('2d')

      // 设置宽高
      canvas.width = document.getElementById('git-calendar-canvas-box').offsetWidth
      const lineMaxWidth = 0.96 * canvas.width / this.contributions.length
      canvas.height = 9 * lineMaxWidth
      const lineMinWidth = 0.8 * lineMaxWidth

      let git_x = ''; let git_y = ''; let git_span1 = ''; let git_span2 = ''
      const position = { x: 0.02 * canvas.width, y: 0.025 * canvas.width }

      this.positionPlusData = []
      for (const week in this.contributions) {
        const weekData = this.contributions[week]
        for (const day in weekData) {
          const dataItem = { date: '', count: 0, x: 0, y: 0 }
          this.positionPlusData.push(dataItem)
          ctx.fillStyle = this.chooseColor(this.gitColor, weekData[day].count)
          position.y = Math.round(position.y * 100) / 100
          dataItem.date = weekData[day].date
          dataItem.count = weekData[day].count
          dataItem.x = position.x
          dataItem.y = position.y
          ctx.fillRect(position.x, position.y, lineMinWidth, lineMinWidth)
          position.y = position.y + lineMaxWidth
        }

        position.y = 0.025 * canvas.width
        position.x = position.x + lineMaxWidth
      }

      if (document.body.clientWidth > 700) {
        ctx.font = '600  Arial'
        ctx.fillStyle = '#aaa'
        ctx.fillText('日', 0, 1.9 * lineMaxWidth)
        ctx.fillText('二', 0, 3.9 * lineMaxWidth)
        ctx.fillText('四', 0, 5.9 * lineMaxWidth)
        ctx.fillText('六', 0, 7.9 * lineMaxWidth)
        let monthIndexList = canvas.width / 24
        for (const index in this.months) {
          ctx.fillText(this.months[index], monthIndexList, 0.7 * lineMaxWidth)
          monthIndexList = monthIndexList + canvas.width / 12
        }
      }

      canvas.onmousemove = (event) => {
        if (document.querySelector('.github_message')) {
          git_tooltip_container.innerHTML = ''
        }
        getMousePos(canvas, event, this.positionPlusData)
      }

      git_tooltip_container.onmousemove = function(event) {
        if (document.querySelector('.github_message')) {
          git_tooltip_container.innerHTML = ''
        }
      }

      function getMousePos(canvas, event, positionPlusData) {
        const rect = canvas.getBoundingClientRect()
        const x = event.clientX - rect.left * (canvas.width / rect.width)
        const y = event.clientY - rect.top * (canvas.height / rect.height)
        for (const item of positionPlusData) {
          const length_x = x - item.x
          const length_y = y - item.y
          if (length_x > 0 && length_x < lineMinWidth) {
            if (length_y > 0 && length_y < lineMinWidth) {
              git_span1 = item.date
              git_span2 = item.count
              git_x = event.clientX - 100
              git_y = event.clientY - 60
              const html = '<div class="github_message" style="top:' + git_y + 'px;left:' + git_x + 'px;position: fixed;z-index:9999"><div class="angle-wrapper" style="display:block;"><span>' + git_span1 + '&nbsp;</span><span>' + git_span2 + ' 次上传</span></div></div>'

              const temp = document.createElement('div')
              temp.innerHTML = html
              const frag = document.createDocumentFragment()
              while (temp.firstChild) {
                frag.appendChild(temp.firstChild)
              }
              git_tooltip_container.appendChild(frag)
            }
          }
        }
      }
    },
    chooseColor(color, count) {
      if (count === 0) {
        return color[0]
      } else if (count < 2) {
        return color[1]
      } else if (count < 20) {
        const i = parseInt(count / 2)
        return color[i]
      } else {
        return color[9]
      }
    }
  }
}
</script>

<style scoped>
h2.f4.text-normal.mb-3 {
  display: none;
}
.float-left.text-gray {
  float: left;
}
.github_container {
  text-align: center;
  margin: 0 auto;
  width: 100%;
  display: flex;
  display: -webkit-flex;
  justify-content: center;
  align-items: center;
  flex-wrap: wrap;
}
.github_loading {
  width: 10%;
  height: 100%;
  margin: 0 auto;
  display: block;
}
.github_calendar {
  width: 100%;
}
.github_calendar_graph {
  padding: 15px 0 0;
  text-align: center;
}
.contrib-footer {
  font-size: 11px;
  padding: 0 10px 12px;
  text-align: left;
  width: 100%;
  box-sizing: border-box;
  height: 26px;
}
.contrib-footer a {
  color: blue;
}
.contrib-legend {
  text-align: right;
  padding: 0 14px 10px 0;
  display: inline-block;
  float: right;
}
.contrib-legend .legend {
  display: inline-block;
  list-style: none;
  margin: 0 5px;
  position: relative;
  bottom: -1px;
  padding: 0;
}
.contrib-legend .legend li {
  display: inline-block;
  width: 10px;
  height: 10px;
}
.contrib-column {
  text-align: center;
  border-left: 1px solid #ddd;
  border-top: 1px solid #ddd;
  font-size: 11px;
}
.contrib-column-first {
  border-left: 0;
}
.table-column {
  padding: 10px;
  display: table-cell;
  flex: 1;
  vertical-align: top;
}
.contrib-number {
  font-weight: 300;
  line-height: 1.3em;
  font-size: 24px;
  display: block;
}
@media screen and (max-width:650px) {
  .contrib-column {
    display: none;
  }
}
.left.text-muted {
  float: left;
  margin-left: 9px;
  color: #767676;
}
.left.text-muted a {
  color: #4078c0;
  text-decoration: none;
}
.left.text-muted a:hover {
  text-decoration: underline;
}
::v-deep .github_message {
}
::v-deep .angle-wrapper {
  z-index: 9999;
  display: inline;
  width: 200px;
  height: 40px;
  position: relative;
  padding: 5px 0;
  background: rgba(0, 0, 0, 0.8);
  border-radius: 8px;
  text-align: center;
  color: white;
}
::v-deep .angle-wrapper span {
  padding-bottom: 1em;
}
::v-deep .angle-wrapper:before {
  content: "";
  width: 0;
  height: 0;
  border: 10px solid transparent;
  border-top-color: rgba(0, 0, 0, 0.8);
  position: absolute;
  left: 47.5%;
  top: 100%;
}
</style>
