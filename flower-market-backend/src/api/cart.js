import request from '@/utils/request'

/**
 * Cart APIs
 */

// Cart list
export const getCartList = (userId) => {
  return request({
    url: '/api/cart',
    method: 'get',
    params: { userId }
  })
}

// Add to cart
export const addToCart = (data) => {
  return request({
    url: '/api/cart/items',
    method: 'post',
    data
  })
}

// Update cart item quantity
export const updateCartItem = (id, quantity) => {
  return request({
    url: `/api/cart/items/${id}`,
    method: 'put',
    params: { quantity }
  })
}

// Remove cart item
export const removeCartItem = (id) => {
  return request({
    url: `/api/cart/items/${id}`,
    method: 'delete'
  })
}

// Clear cart
export const clearCart = (userId) => {
  return request({
    url: '/api/cart',
    method: 'delete',
    params: { userId }
  })
}

// Batch delete cart items
export const batchRemoveCartItems = (ids) => {
  return request({
    url: '/api/cart/items/batch',
    method: 'delete',
    data: { ids }
  })
}
