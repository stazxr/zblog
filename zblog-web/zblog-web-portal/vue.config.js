'use strict'
const defaultSettings = require('./src/settings.js')

module.exports = {
  devServer: {
    host: defaultSettings.devHost,
    port: defaultSettings.devPort,
    open: false, // 启动自动打开浏览器
    overlay: { // 浏览器只显示错误
      warnings: false,
      errors: true
    },
    proxy: {
      '/api': {
        target: defaultSettings.devBaseApiUrl,
        changeOrigin: true,
        secure: false,
        pathRewrite: {
          '^/api': '/api'
        }
      },
      'ws': {
        target: defaultSettings.devBaseApiUrl,
        ws: true,
        changeOrigin: true
      }
    },
    disableHostCheck: true
  },
  transpileDependencies: [
    'vuetify'
  ]
}
