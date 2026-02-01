import { fileURLToPath, URL } from 'node:url'

import { defineConfig } from 'vite'
import vue from '@vitejs/plugin-vue'
import vueDevTools from 'vite-plugin-vue-devtools'

const TARGET_URL = 'http://localhost:8090';

// https://vite.dev/config/
export default defineConfig({
  plugins: [
    vue(),
    vueDevTools(),
  ],
  resolve: {
    alias: {
      '@': fileURLToPath(new URL('./src', import.meta.url))
    },
  },
  // 生产环境配置
  build: {
    outDir: 'dist', // 输出目录
    assetsDir: 'assets', // 静态资源目录
    minify: 'terser', // 压缩方式
    sourcemap: false, // 生产环境不生成 source map
    terserOptions: {
      compress: {
        drop_console: true, // 生产环境移除 console
      }
    }
  },
  server: {
    host: true,
    port: 3000,
    proxy: {
      '/api': {
        target: TARGET_URL,
        changeOrigin: true
      },
      '/images': {
        target: TARGET_URL,
        changeOrigin: true
      },
      '/products': {
        target: TARGET_URL,
        changeOrigin: true
      }
    }
  }
})
