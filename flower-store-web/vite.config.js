import { defineConfig } from 'vite'
import vue from '@vitejs/plugin-vue'

export default defineConfig({
  plugins: [vue()],
  server: {
    port: 5173,
    // 代理：前端发 /api 请求 → 转发到后端 18080
    proxy: {
      '/api': {
        target: 'http://localhost:18080',
        changeOrigin: true
      },
      '/uploads': {
        target: 'http://localhost:18080',
        changeOrigin: true
      }
    }
  }
})
