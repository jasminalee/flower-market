import request from '@/utils/request'

/**
 * Order APIs
 */

// Order list (pagination + filters)
export const getOrderList = (params) => {
  return request({
    url: '/api/orders',
    method: 'get',
    params
  })
}

// Order detail
export const getOrderDetail = (id) => {
  return request({
    url: `/api/orders/${id}`,
    method: 'get'
  })
}

// Create order
export const createOrder = (data) => {
  return request({
    url: '/api/orders',
    method: 'post',
    data
  })
}

// Cancel order
export const cancelOrder = (id) => {
  return request({
    url: `/api/orders/${id}/cancel`,
    method: 'put'
  })
}

// Pay order (mock)
export const payOrder = (id, paymentMethod) => {
  return request({
    url: `/api/orders/${id}/pay`,
    method: 'put',
    params: { paymentMethod }
  })
}

// Confirm receipt
export const confirmOrder = (id) => {
  return request({
    url: `/api/orders/${id}/confirm`,
    method: 'put'
  })
}

// Merchant shipment
export const shipOrder = (id, data) => {
  return request({
    url: `/api/orders/${id}/ship`,
    method: 'put',
    data
  })
}

// Order item list
export const getOrderItemList = (params) => {
  return request({
    url: '/api/orders/items',
    method: 'get',
    params
  })
}
