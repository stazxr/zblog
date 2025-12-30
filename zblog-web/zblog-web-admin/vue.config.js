'use strict'
const path = require('path')
const defaultSettings = require('./src/settings.js')
const CompressionPlugin = require('compression-webpack-plugin')

function resolve(dir) {
  return path.join(__dirname, dir)
}

const name = defaultSettings.title // 网址标题
const devPort = 31945 // 端口配置

// All configuration item explanations can be find in https://cli.vuejs.org/config/
module.exports = {
  publicPath: process.env.NODE_ENV === 'development' ? '/' : '',
  outputDir: 'dist',
  assetsDir: 'static',
  lintOnSave: process.env.NODE_ENV === 'development', // 只在开发环境 lint
  productionSourceMap: false, // 生产环境 不生成 sourcemap, 防止源码泄露
  devServer: {
    host: '0.0.0.0', // 支持 IP / 局域网访问
    port: devPort,
    open: false, // 启动自动打开浏览器
    // 已弃用 disableHostCheck: true, // 允许非 localhost 访问（有安全风险）
    allowedHosts: [], // 高版本可以使用 'all' 替换
    overlay: { // 浏览器只显示错误
      warnings: false,
      errors: true
    },
    proxy: {
      '/api': {
        target: process.env.VUE_APP_BASE_API,
        changeOrigin: true,
        pathRewrite: {
          '^/api': 'api'
        }
      }
    }
  },
  configureWebpack: {
    // provide the app's title in webpack's name field, so that
    // it can be accessed in index.html to inject the correct title.
    name: name, // 标题
    resolve: {
      alias: { // 别名配置
        '@': resolve('src')
      }
    },
    plugins: [
      // https://www.ydyno.com/archives/1260.html 使用gzip解压缩静态文件
      // 需要Nginx配合 gzip_static on;
      new CompressionPlugin({
        test: /\.(js|css|html)?$/i, // 压缩文件格式
        filename: '[path].gz[query]', // 压缩后的文件名
        algorithm: 'gzip', // 使用gzip压缩
        threshold: 10240, // 10KB 以上才压缩
        minRatio: 0.8 // 压缩率小于1才会压缩
      })
    ]
  },
  chainWebpack(config) {
    // 避免一次加载太多 JS, 避免移动端首屏卡顿
    config.plugins.delete('preload')
    config.plugins.delete('prefetch')

    // set svg-sprite-loader
    config.module
      .rule('svg')
      .exclude.add(resolve('src/assets/icons'))
      .end()
    config.module
      .rule('icons')
      .test(/\.svg$/)
      .include.add(resolve('src/assets/icons'))
      .end()
      .use('svg-sprite-loader')
      .loader('svg-sprite-loader')
      .options({
        symbolId: 'icon-[name]'
      })
      .end()

    // set preserveWhitespace
    config.module
      .rule('vue')
      .use('vue-loader')
      .loader('vue-loader')
      .tap(options => {
        options.compilerOptions.preserveWhitespace = true
        return options
      })
      .end()

    config
      // https://webpack.js.org/configuration/devtool/#development
      .when(process.env.NODE_ENV === 'development',
        config => config.devtool('cheap-source-map')
      )

    config
      .when(process.env.NODE_ENV !== 'development',
        config => {
          config
            .plugin('ScriptExtHtmlWebpackPlugin')
            .after('html')
            .use('script-ext-html-webpack-plugin', [{
            // `runtime` must same as runtimeChunk name. default is `runtime`
              inline: /runtime\..*\.js$/
            }])
            .end()
          config
            .optimization.splitChunks({
              chunks: 'all',
              cacheGroups: {
                libs: {
                  name: 'chunk-libs',
                  test: /[\\/]node_modules[\\/]/,
                  priority: 10,
                  chunks: 'initial' // only package third parties that are initially dependent
                },
                elementUI: {
                  name: 'chunk-elementUI', // split elementUI into a single package
                  priority: 20, // the weight needs to be larger than libs and app or it will be packaged into libs or app
                  test: /[\\/]node_modules[\\/]_?element-ui(.*)/ // in order to adapt to cnpm
                },
                commons: {
                  name: 'chunk-commons',
                  test: resolve('src/components'), // can customize your rules
                  minChunks: 3, //  minimum common number
                  priority: 5,
                  reuseExistingChunk: true
                }
              }
            })
          config.optimization.runtimeChunk('single')
        }
      )
  },
  transpileDependencies: [
    'vue-echarts',
    'resize-detector'
  ]
}
