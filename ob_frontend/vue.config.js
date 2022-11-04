const { defineConfig } = require('@vue/cli-service')
module.exports = defineConfig({
  transpileDependencies: true,
  configureWebpack: {
    devServer: {
      proxy: {
        '/api': {
          // target: 'http://47.112.184.57:18888/',
          target: 'http://localhost:18888/',
          changeOrigin: true,
          ws: true,
          pathRewrite: {
            '^/api': ''
          }
        }
      }
    }
  },
})
