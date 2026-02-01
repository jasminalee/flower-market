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
