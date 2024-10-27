<template>
  <div ref="tagCloudAll" class="tag-cloud-all" :style="{ width: '100%', height: size + 'px' }">
    <a v-for="item in tagList" ref="tagCloudLink" :key="item.id" :style="'color:' + item.color + ';top: 0;left: 0;filter:none;'" @click="gotoTagPage(item.id)">
      {{ item.name }}
    </a>
  </div>
</template>

<script>
export default {
  props: {
    tagList: {
      type: Array,
      required: true
    }
  },
  data() {
    return {
      interval: null,
      stop: false,
      active: true,
      oList: null,
      oA: null,
      mcList: [],
      sa: 0,
      ca: 0,
      sb: 0,
      cb: 0,
      sc: 0,
      cc: 0,
      mouseX: 0,
      mouseY: 0,
      dtr: Math.PI / 180,
      radius: 100,
      tspeed: 5,
      lasta: 1,
      lastb: 1,
      distr: true,
      d: 300,
      size: 250,
      howElliptical: 1
    }
  },
  computed: {
    isMobile() {
      const clientWidth = document.documentElement.clientWidth
      return clientWidth <= 960
    }
  },
  watch: {
    tagList: {
      handler(val) {
        this.init(val)
      }
    }
  },
  destroyed() {
    if (this.interval != null) {
      clearInterval(this.interval)
      this.interval = null
    }
  },
  methods: {
    gotoTagPage(tagId) {
      this.$router.push('/tags/' + tagId)
    },
    init(tagList) {
      tagList.forEach(item => {
        item.color = 'rgb(' + this.getRandomNum() + ',' + this.getRandomNum() + ',' + this.getRandomNum() + ')'
      })
      this.tagList = tagList
      this.onReady()
    },
    // 生成标签云
    onReady() {
      this.$nextTick(() => {
        this.oList = this.$refs.tagCloudAll
        this.oA = this.oList.getElementsByTagName('a')
        let oTag = null
        for (let i = 0; i < this.oA.length; i++) {
          oTag = {}
          oTag.offsetWidth = this.oA[i].offsetWidth
          oTag.offsetHeight = this.oA[i].offsetHeight
          this.mcList.push(oTag)
        }

        this.sineCosine(0, 0, 0)
        this.positionAll()

        // 无效
        // this.$refs.tagCloudLink.onmouseover = () => {
        //   this.stop = true
        // }

        // 无效
        // this.$refs.tagCloudLink.onmouseout = () => {
        //   this.stop = false
        // }

        this.oList.onmouseover = () => {
          // this.active = true
          this.stop = true
        }
        this.oList.onmouseout = () => {
          // this.active = false
          this.stop = false
        }

        this.oList.onmousemove = (event) => {
          const oEvent = window.event || event

          this.mouseX = oEvent.clientX - (this.oList.offsetLeft + this.oList.offsetWidth / 2)
          this.mouseY = oEvent.clientY - (this.oList.offsetTop + this.oList.offsetHeight / 2)
          this.mouseX /= 5
          this.mouseY /= 5
        }

        this.interval = setInterval(() => {
          this.update()
        }, 200)
      })
    },
    // 坐标更新 让标签动起来
    update() {
      this.$nextTick(() => {
        if (this.stop === true) {
          return
        }

        let a, b
        if (!this.active) {
          a = (-Math.min(Math.max(-this.mouseY, -this.size), this.size) / this.radius) * this.tspeed
          b = (Math.min(Math.max(-this.mouseX, -this.size), this.size) / this.radius) * this.tspeed
        } else {
          a = this.lasta * 1.01
          b = this.lastb * 1.01
        }
        this.lasta = a
        this.lastb = b
        if (Math.abs(a) <= 0.01 && Math.abs(b) <= 0.01) {
          return
        }
        const c = 0
        this.sineCosine(a, b, c)
        for (let j = 0; j < this.mcList.length; j++) {
          const rx1 = this.mcList[j].cx
          const ry1 = this.mcList[j].cy * this.ca + this.mcList[j].cz * (-this.sa)
          const rz1 = this.mcList[j].cy * this.sa + this.mcList[j].cz * this.ca
          const rx2 = rx1 * this.cb + rz1 * this.sb
          const ry2 = ry1
          const rz2 = rx1 * (-this.sb) + rz1 * this.cb
          const rx3 = rx2 * this.cc + ry2 * (-this.sc)
          const ry3 = rx2 * this.sc + ry2 * this.cc
          const rz3 = rz2
          this.mcList[j].cx = rx3
          this.mcList[j].cy = ry3
          this.mcList[j].cz = rz3
          const per = this.d / (this.d + rz3)
          this.mcList[j].x = (this.howElliptical * rx3 * per) - (this.howElliptical * 2)
          this.mcList[j].y = ry3 * per
          this.mcList[j].scale = per
          this.mcList[j].alpha = per
          this.mcList[j].alpha = (this.mcList[j].alpha - 0.6) * (10 / 6)
        }

        this.doPosition()
        this.depthSort()
      })
    },
    doPosition() {
      this.$nextTick(() => {
        const l = this.oList.offsetWidth / 2
        const t = this.oList.offsetHeight / 2
        for (let i = 0; i < this.mcList.length; i++) {
          this.oA[i].style.left = this.mcList[i].cx + l - this.mcList[i].offsetWidth / 2 + 'px'
          this.oA[i].style.top = this.mcList[i].cy + t - this.mcList[i].offsetHeight / 2 + 'px'
          this.oA[i].style.fontSize = Math.ceil(12 * this.mcList[i].scale / 2) + 8 + 'px'
          // this.oA[i].style.filter = 'alpha(opacity=' + 100 * this.mcList[i].alpha + ')'
          this.oA[i].style.opacity = this.mcList[i].alpha
        }
      })
    },
    depthSort() {
      this.$nextTick(() => {
        const aTmp = []
        for (let i = 0; i < this.oA.length; i++) {
          aTmp.push(this.oA[i])
        }
        aTmp.sort(function(vItem1, vItem2) {
          if (vItem1.cz > vItem2.cz) {
            return -1
          } else if (vItem1.cz < vItem2.cz) {
            return 1
          } else {
            return 0
          }
        })

        for (let i = 0; i < aTmp.length; i++) {
          aTmp[i].style.zIndex = i
        }
      })
    },
    // 生成随机数
    getRandomNum() {
      return Math.floor(Math.random() * (255 + 1))
    },
    // 三角函数角度计算
    sineCosine(a, b, c) {
      this.sa = Math.sin(a * this.dtr)
      this.ca = Math.cos(a * this.dtr)
      this.sb = Math.sin(b * this.dtr)
      this.cb = Math.cos(b * this.dtr)
      this.sc = Math.sin(c * this.dtr)
      this.cc = Math.cos(c * this.dtr)
    },
    // 设置初始定位
    positionAll() {
      this.$nextTick(() => {
        let phi = 0
        let theta = 0
        const max = this.mcList.length
        const aTmp = []
        const oFragment = document.createDocumentFragment()

        // 随机排序
        for (let i = 0; i < this.tagList.length; i++) {
          aTmp.push(this.oA[i])
        }
        aTmp.sort(() => {
          return Math.random() < 0.5 ? 1 : -1
        })
        for (let i = 0; i < aTmp.length; i++) {
          oFragment.appendChild(aTmp[i])
        }
        this.oList.appendChild(oFragment)
        for (let i = 1; i < max + 1; i++) {
          if (this.distr) {
            phi = Math.acos(-1 + (2 * i - 1) / max)
            theta = Math.sqrt(max * Math.PI) * phi
          } else {
            phi = Math.random() * (Math.PI)
            theta = Math.random() * (2 * Math.PI)
          }

          // 坐标变换
          this.mcList[i - 1].cx = this.radius * Math.cos(theta) * Math.sin(phi)
          this.mcList[i - 1].cy = this.radius * Math.sin(theta) * Math.sin(phi)
          this.mcList[i - 1].cz = this.radius * Math.cos(phi)
          this.oA[i - 1].style.left = this.mcList[i - 1].cx + this.oList.offsetWidth / 2 - this.mcList[i - 1].offsetWidth / 2 + 'px'
          this.oA[i - 1].style.top = this.mcList[i - 1].cy + this.oList.offsetHeight / 2 - this.mcList[i - 1].offsetHeight / 2 + 'px'
        }
      })
    }
  }
}
</script>

<style lang="scss" scoped>
.tag-cloud-all {
  display: block;
  position: relative;
  a {
    position: absolute;
    top: 0;
    left: 0;
    color: #fff;
    font-weight: bold;
    text-decoration: none;
    padding: 3px 6px;
  &:hover {
     color: #FF0000;
     letter-spacing: 2px;
   }
  }
}
</style>
