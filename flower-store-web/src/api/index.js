import axios from 'axios'
import { ElMessage } from 'element-plus'

const api = axios.create({ baseURL: '/api' })

// 请求拦截器：自动带 Token
api.interceptors.request.use(config => {
  const token = localStorage.getItem('token')
  if (token) {
    config.headers.Authorization = `Bearer ${token}`
  }
  return config
})

// 响应拦截器：统一错误处理
api.interceptors.response.use(
  res => {
    if (res.data.code !== 200) {
      ElMessage.error(res.data.message || '操作失败')
    }
    return res.data
  },
  err => {
    ElMessage.error('网络错误，请检查后端是否启动')
    return Promise.reject(err)
  }
)

export default api
