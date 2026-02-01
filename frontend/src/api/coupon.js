import request from '@/utils/request'

/**
 * Coupon APIs
 */

// Coupon list (paginated)
export const getCouponList = (params) => {
  return request({
    url: '/api/coupons',
    method: 'get',
    params
  })
}

// Coupon detail
export const getCouponDetail = (id) => {
  return request({
    url: `/api/coupons/${id}`,
    method: 'get'
  })
}

// Create coupon
export const createCoupon = (data) => {
  return request({
    url: '/api/coupons',
    method: 'post',
    data
  })
}

// Update coupon
export const updateCoupon = (id, data) => {
  return request({
    url: `/api/coupons/${id}`,
    method: 'put',
    data
  })
}

// Delete coupon
export const deleteCoupon = (id) => {
  return request({
    url: `/api/coupons/${id}`,
    method: 'delete'
  })
}

// Coupon code list
export const getCouponCodeList = (params) => {
  return request({
    url: '/api/coupons/codes',
    method: 'get',
    params
  })
}

// Generate coupon code
export const generateCouponCode = (data) => {
  return request({
    url: '/api/coupons/codes',
    method: 'post',
    data
  })
}

// Delete coupon code
export const deleteCouponCode = (id) => {
  return request({
    url: `/api/coupons/codes/${id}`,
    method: 'delete'
  })
}

// Customer claim coupon
export const claimCoupon = (couponId, userId) => {
  return request({
    url: `/api/coupons/${couponId}/receive`,
    method: 'post',
    params: { userId }
  })
}

// Customer coupon list (alias for compatibility)
export const getCustomerCouponList = (params) => {
  return request({
    url: '/api/coupons/my',
    method: 'get',
    params
  })
}

// Use coupon
export const useCoupon = (id) => {
  return request({
    url: `/api/customer/coupons/${id}/use`,
    method: 'put'
  })
}

// Get my coupons
export const getMyCoupons = (params) => {
  return request({
    url: '/api/coupons/my',
    method: 'get',
    params
  })
}
