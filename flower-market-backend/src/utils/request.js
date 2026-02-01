import axios from 'axios'
import { ElMessage } from 'element-plus'
import router from '@/router'

// Create axios instance
const service = axios.create({
  baseURL: import.meta.env.VITE_API_BASE_URL || '',
  timeout: 15000
})

// Request interceptor
service.interceptors.request.use(
  config => {
    // Get token from localStorage
    const token = localStorage.getItem('token')
    if (token) {
      config.headers['Authorization'] = `Bearer ${token}`
    }
    
    // Get user info from localStorage and append user ID
    const userInfo = localStorage.getItem('userInfo')
    if (userInfo) {
      try {
        const user = JSON.parse(userInfo)
        if (user.userId) {
          config.headers['X-User-Id'] = user.userId
        }
      } catch (e) {
        console.error('Parse userInfo error:', e)
      }
    }
    
    return config
  },
  error => {
    console.error('Request error:', error)
    return Promise.reject(error)
  }
)

// Response interceptor
service.interceptors.response.use(
  response => {
    const res = response.data
    
    // Return blob responses directly
    if (response.config.responseType === 'blob') {
      return response
    }
    
    // Unified handling based on backend status codes
    // Expected format: { code: 200, data: {}, message: '' }
    if (res.code && res.code !== 200) {
      ElMessage.error(res.message || 'Request failed')
      
      // 401: not logged in or token expired
      if (res.code === 401) {
        localStorage.removeItem('token')
        localStorage.removeItem('userInfo')
        router.push('/login')
      }
      
      return Promise.reject(new Error(res.message || 'Error'))
    }
    
    return res
  },
  error => {
    console.error('Response error:', error)
    
    let message = 'System error'
    
    if (error.response) {
      switch (error.response.status) {
        case 400:
          message = error.response.data.message || 'Invalid request parameters'
          break
        case 401:
          message = 'Unauthorized, please login'
          localStorage.removeItem('token')
          localStorage.removeItem('userInfo')
          router.push('/login')
          break
        case 403:
          message = 'Access denied'
          break
        case 404:
          message = 'Endpoint not found'
          break
        case 500:
          message = 'Internal server error'
          break
        case 502:
          message = 'Bad gateway'
          break
        case 503:
          message = 'Service unavailable'
          break
        case 504:
          message = 'Gateway timeout'
          break
        default:
          message = error.response.data.message || 'Request failed'
      }
    } else if (error.message.includes('timeout')) {
      message = 'Request timed out'
    } else if (error.message.includes('Network')) {
      message = 'Network connection failed'
    }
    
    ElMessage.error(message)
    return Promise.reject(error)
  }
)

export default service
