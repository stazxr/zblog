<template>
  <!-- 参考文档：https://xtermjs.org/docs/api/terminal/classes/terminal/ -->
  <!-- 参考文档：https://juejin.cn/post/6844903809035010055 -->
  <div class="container">
    <div ref="terminal" />
  </div>
</template>
<script>
import { getToken } from '@/utils/token'
import { Terminal } from 'xterm'
import * as fit from 'xterm/lib/addons/fit/fit'
import * as attach from 'xterm/lib/addons/attach/attach'
import 'xterm/dist/xterm.css'
import { mapGetters } from 'vuex'

export default {
  data() {
    return {
      term: null,
      connection: null,
      content: '',
      rows: 37,
      cols: 150,
      option: {
        operate: 'connect',
        host: '',
        port: '',
        username: '',
        password: ''
      }
    }
  },
  computed: {
    ...mapGetters([
      'webSshApi'
    ])
  },
  mounted() {
    this.getNodeInfo()
  },
  beforeDestroy() {
    this.connection.close()
    this.term.destroy()
  },
  methods: {
    getNodeInfo() {
      this.$mapi.node.queryNodeDetail({ nodeId: this.$route.query.nid }).then(res => {
        const { data } = res
        this.option.operate = 'connect'
        this.option.host = data.ip
        this.option.port = data.port
        this.option.username = data.sshUser
        this.option.password = data.sshPwd

        this.initTerm()
      })
    },
    initTerm() {
      this.applyAddon()
      this.setCols()

      // init Terminal
      this.term = new Terminal({
        cursorBlink: true, // 光标闪烁
        cursorStyle: 'underline', // 光标样式  null | 'block' | 'underline' | 'bar'
        scrollback: 80, // 终端中的回滚量
        tabStopWidth: 8, // 制表宽度
        disableStdin: false, // 是否应禁用输入
        screenKeys: true,
        rows: this.rows,
        cols: this.cols,
        theme: {
          foreground: '#ECECEC', // 字体
          background: '#000000', // 背景色
          lineHeight: 20
        }
      })

      // init WebSocket
      if (window.WebSocket) {
        const usrToken = getToken().split(' ')[1]
        this.connection = new WebSocket(this.webSshApi, usrToken)
      } else {
        this.onError('WebSocket Not Supported')
      }

      // open terminal
      this.term.open(this.$refs['terminal'], true)
      this.term.write('Connecting...' + '\r\n')

      this.term.on('data', (data) => {
        // 键盘输入时的回调函数
        this.connection.send(JSON.stringify({ 'operate': 'command', 'command': data }))
      })

      this.connection.onopen = this.onConnect
      this.connection.onclose = this.onClose
      this.connection.onerror = this.onError
      this.term.attach(this.connection, false, true)
      this.term._initialized = true

      window.addEventListener('resize', this.resize)
    },
    applyAddon() {
      Terminal.applyAddon(attach)
      Terminal.applyAddon(fit)
    },
    setCols() {
      const height = window.innerHeight - 140
      this.rows = parseInt(height / 17 + '', 10)
      const width = window.innerWidth - 230
      this.cols = parseInt(width / 9 + '', 10)

      console.log('[', this.rows, ', ', this.cols, ']')
    },
    resize() {
      const height = window.innerHeight - 140
      this.rows = parseInt(height / 17 + '', 10)
      const width = window.innerWidth - 230
      this.cols = parseInt(width / 9 + '', 10)

      this.term.fit()
      this.term.resize(this.cols, this.rows)
      this.term.scrollToBottom()
    },
    onConnect() {
      // 连接成功回调
      console.log('\rconnection success')
      this.connection.send(JSON.stringify(this.option))
    },
    onClose() {
      // 连接关闭回调
      this.term.write('\rconnection closed')
    },
    onError(error) {
      // 连接失败回调
      this.term.write('Error: ' + error + '\r\n')
    }
  }
}
</script>
