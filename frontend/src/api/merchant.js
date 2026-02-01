import request from '@/utils/request'

/**
 * Merchant-related APIs
 */

// ===== Admin side =====
// Merchant list (pagination + filters)
export const getMerchantList = (params) => {
  return request({
    url: '/api/admin/merchants',
    method: 'get',
    params
  })
}

// Merchant detail
export const getMerchantDetail = (id) => {
  return request({
    url: `/api/admin/merchants/${id}`,
    method: 'get'
  })
}

// Update merchant info
export const updateMerchant = (id, data) => {
  return request({
    url: `/api/admin/merchants/${id}`,
    method: 'put',
    data
  })
}

// Approve / reject merchant
export const approveMerchant = (id, status) => {
  return request({
    url: `/api/admin/merchants/${id}/approval`,
    method: 'put',
    data: { status }
  })
}

// Freeze/unfreeze merchant
export const toggleMerchantStatus = (id) => {
  return request({
    url: `/api/admin/merchants/${id}/toggle`,
    method: 'put'
  })
}

// ===== Merchant side =====
// Merchant dashboard data
export const getMerchantDashboard = (merchId) => {
  return request({
    url: '/api/merchant/dashboard',
    method: 'get',
    params: { merchId }
  })
}

// Get merchant profile
export const getMerchantProfile = (id) => {
  return request({
    url: `/api/merchant/profile/${id}`,
    method: 'get'
  })
}

// Update merchant profile
export const updateMerchantProfile = (data) => {
  return request({
    url: '/api/merchant/profile',
    method: 'put',
    data
  })
}

// Merchant product list
export const getMerchantProducts = (params) => {
  return request({
    url: '/api/merchant/products',
    method: 'get',
    params
  })
}

// Merchant product detail
export const getMerchantProduct = (id) => {
  return request({
    url: `/api/merchant/products/${id}`,
    method: 'get'
  })
}

// Create product
export const createProduct = (data) => {
  return request({
    url: '/api/merchant/products',
    method: 'post',
    data
  })
}

// Update product
export const updateProduct = (id, data) => {
  return request({
    url: `/api/merchant/products/${id}`,
    method: 'put',
    data
  })
}

// Delete product
export const deleteProduct = (id) => {
  return request({
    url: `/api/merchant/products/${id}`,
    method: 'delete'
  })
}

// Update product status (on/off shelf)
export const updateProductStatus = (id, status) => {
  return request({
    url: `/api/merchant/products/${id}/status`,
    method: 'put',
    data: { status }
  })
}

// Get product trackability
export const getProductTrackability = (id) => {
  return request({
    url: `/api/merchant/products/${id}/trackability`,
    method: 'get'
  })
}

// Create trackability record
export const createTrackability = (id, data) => {
  return request({
    url: `/api/merchant/products/${id}/trackability`,
    method: 'post',
    data
  })
}

// Update trackability record
export const updateTrackability = (id, data) => {
  return request({
    url: `/api/merchant/products/trackability/${id}`,
    method: 'put',
    data
  })
}

// Delete trackability record
export const deleteTrackability = (id) => {
  return request({
    url: `/api/merchant/products/trackability/${id}`,
    method: 'delete'
  })
}

// Merchant order list
export const getMerchantOrders = (params) => {
  return request({
    url: '/api/merchant/orders',
    method: 'get',
    params
  })
}

// Merchant order detail
export const getMerchantOrder = (id) => {
  return request({
    url: `/api/merchant/orders/${id}`,
    method: 'get'
  })
}

// Ship order
export const shipOrder = (id, data) => {
  return request({
    url: `/api/merchant/orders/${id}/ship`,
    method: 'put',
    data
  })
}

// Merchant coupon list
export const getMerchantCoupons = (params) => {
  return request({
    url: '/api/merchant/coupons',
    method: 'get',
    params
  })
}

// Merchant coupon detail
export const getMerchantCoupon = (id) => {
  return request({
    url: `/api/merchant/coupons/${id}`,
    method: 'get'
  })
}

// Create coupon
export const createCoupon = (data) => {
  return request({
    url: '/api/merchant/coupons',
    method: 'post',
    data
  })
}

// Update coupon
export const updateCoupon = (id, data) => {
  return request({
    url: `/api/merchant/coupons/${id}`,
    method: 'put',
    data
  })
}

// Delete coupon
export const deleteCoupon = (id) => {
  return request({
    url: `/api/merchant/coupons/${id}`,
    method: 'delete'
  })
}
