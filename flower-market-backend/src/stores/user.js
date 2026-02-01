import { defineStore } from 'pinia'
import { storage } from '@/utils/storage'
import * as authApi from '@/api/auth'

export const useUserStore = defineStore('user', {
  state: () => ({
    token: storage.get('token') || '',
    userInfo: storage.get('userInfo') || null
  }),
  
  getters: {
    isLoggedIn: (state) => !!state.token,
    userType: (state) => state.userInfo?.userType || '',
    isCustomer: (state) => state.userInfo?.userType === 'CUSTOMER',
    isMerchant: (state) => state.userInfo?.userType === 'MERCHANT',
    isAdmin: (state) => state.userInfo?.userType === 'ADMIN',
    userId: (state) => state.userInfo?.userId || null,
    username: (state) => state.userInfo?.name || state.userInfo?.username || '',
    realName: (state) => state.userInfo?.name || state.userInfo?.realName || '',
    phone: (state) => state.userInfo?.phone || '',
    email: (state) => state.userInfo?.email || '',
    avatar: (state) => state.userInfo?.avatar || state.userInfo?.shopLogo || ''
  },
  
  actions: {
    // Set token
    setToken(token) {
      this.token = token
      storage.set('token', token)
    },
    
    // Set user info
    setUserInfo(userInfo) {
      this.userInfo = userInfo
      storage.set('userInfo', userInfo)
    },
    
    // Customer login
    async customerLogin(loginData) {
      const res = await authApi.customerLogin(loginData)
      if (res.data) {
        // Backend returns the user object directly; append userType
        const userInfo = {
          ...res.data,
          userType: 'CUSTOMER',
          userId: res.data.userId
        }
        // Generate a simple token (should come from backend in real apps)
        const token = 'customer_' + Date.now()
        this.setToken(token)
        this.setUserInfo(userInfo)
      }
      return res
    },
    
    // Merchant login
    async merchantLogin(loginData) {
      const res = await authApi.merchantLogin(loginData)
      if (res.data) {
        const userInfo = {
          ...res.data,
          userType: 'MERCHANT',
          userId: res.data.merchId
        }
        const token = 'merchant_' + Date.now()
        this.setToken(token)
        this.setUserInfo(userInfo)
      }
      return res
    },
    
    // Admin login
    async adminLogin(loginData) {
      const res = await authApi.adminLogin(loginData)
      if (res.data) {
        const userInfo = {
          ...res.data,
          userType: 'ADMIN',
          userId: res.data.adminId
        }
        const token = 'admin_' + Date.now()
        this.setToken(token)
        this.setUserInfo(userInfo)
      }
      return res
    },
    
    // Logout
    logout() {
      // JWT is stateless; just clear client storage
      this.token = ''
      this.userInfo = null
      storage.remove('token')
      storage.remove('userInfo')
    },
    
    // Update user info
    async updateUserInfo(data) {
      const res = await authApi.updateUserInfo(data)
      if (res.data) {
        this.setUserInfo(res.data)
      }
      return res
    },
    
    // Fetch user info
    async getUserInfo() {
      if (!this.userId) return
      const res = await authApi.getUserInfo(this.userId)
      if (res.data) {
        this.setUserInfo(res.data)
      }
      return res
    }
  }
})
