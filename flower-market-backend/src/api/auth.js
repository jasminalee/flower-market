import request from '@/utils/request'

/**
 * User authentication APIs
 */

// Customer registration
export const registerCustomer = (data) => {
  return request({
    url: '/api/customer/register',
    method: 'post',
    data
  })
}

// Merchant registration
export const registerMerchant = (data) => {
  return request({
    url: '/api/merchant/register',
    method: 'post',
    data
  })
}

// Customer login
export const customerLogin = (data) => {
  return request({
    url: '/api/customer/login',
    method: 'post',
    data
  })
}

// Merchant login
export const merchantLogin = (data) => {
  return request({
    url: '/api/merchant/login',
    method: 'post',
    data
  })
}

// Admin login
export const adminLogin = (data) => {
  return request({
    url: '/api/admin/login',
    method: 'post',
    data
  })
}

// Get current user info (customer)
export const getUserInfo = (userId) => {
  return request({
    url: `/api/customer/profile/${userId}`,
    method: 'get'
  })
}

// Update user info (customer)
export const updateUserInfo = (data) => {
  return request({
    url: '/api/customer/profile',
    method: 'put',
    data
  })
}

// Change password
export const changePassword = (data) => {
  return request({
    url: '/api/user/password',
    method: 'put',
    data
  })
}
