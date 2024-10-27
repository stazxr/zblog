const plugins = ['@vue/babel-plugin-transform-vue-jsx']

if (process.env.NODE_ENV === 'production') {
  // 禁用生产环境的控制台打印
  plugins.push('transform-remove-console')
}

module.exports = {
  plugins: plugins,
  presets: [
    '@vue/app'
  ]
}
