import request from '@/utils/request'

/**
 * Product APIs
 */

// Product list (pagination + filters)
export const getProductList = (params) => {
  return request({
    url: '/api/products',
    method: 'get',
    params
  })
}

// Product detail
export const getProductDetail = (id) => {
  return request({
    url: `/api/products/${id}`,
    method: 'get'
  })
}

// Create product
export const createProduct = (data) => {
  return request({
    url: '/api/products',
    method: 'post',
    data
  })
}

// Update product
export const updateProduct = (id, data) => {
  return request({
    url: `/api/products/${id}`,
    method: 'put',
    data
  })
}

// Delete product
export const deleteProduct = (id) => {
  return request({
    url: `/api/products/${id}`,
    method: 'delete'
  })
}

// Publish product
export const publishProduct = (id) => {
  return request({
    url: `/api/products/${id}/publish`,
    method: 'put'
  })
}

// Unpublish product
export const unpublishProduct = (id) => {
  return request({
    url: `/api/products/${id}/unpublish`,
    method: 'put'
  })
}

// Product category list
export const getCategoryList = (params) => {
  return request({
    url: '/api/products/categories',
    method: 'get',
    params
  })
}

// Get all categories (no pagination)
export const getAllCategories = () => {
  return request({
    url: '/api/products/categories/all',
    method: 'get'
  })
}

// Create product category
export const createCategory = (data) => {
  return request({
    url: '/api/products/categories',
    method: 'post',
    data
  })
}

// Update product category
export const updateCategory = (id, data) => {
  return request({
    url: `/api/products/categories/${id}`,
    method: 'put',
    data
  })
}

// Delete product category
export const deleteCategory = (id) => {
  return request({
    url: `/api/products/categories/${id}`,
    method: 'delete'
  })
}

// Get product trackability
export const getProductTrackability = (prodId) => {
  return request({
    url: `/api/products/trackability/${prodId}`,
    method: 'get'
  })
}

// Save/update product trackability
export const saveProductTrackability = (data) => {
  return request({
    url: '/api/products/trackability',
    method: 'post',
    data
  })
}

// Delete product trackability
export const deleteProductTrackability = (prodId) => {
  return request({
    url: `/api/products/trackability/${prodId}`,
    method: 'delete'
  })
}

// Product favorites
export const getFavoriteList = (params) => {
  return request({
    url: '/api/products/favorites',
    method: 'get',
    params
  })
}

export const addFavorite = (prodId, userId) => {
  return request({
    url: `/api/products/${prodId}/favorite`,
    method: 'post',
    params: { userId }
  })
}

export const removeFavorite = (prodId, userId) => {
  return request({
    url: `/api/products/${prodId}/favorite`,
    method: 'delete',
    params: { userId }
  })
}

export const checkFavorite = (prodId, userId) => {
  return request({
    url: `/api/products/${prodId}/favorite/check`,
    method: 'get',
    params: { userId }
  })
}

// Product reviews
export const getReviewList = (prodId, params) => {
  return request({
    url: `/api/products/${prodId}/reviews`,
    method: 'get',
    params
  })
}

export const createReview = (data) => {
  return request({
    url: '/api/products/reviews',
    method: 'post',
    data
  })
}

export const deleteReview = (id) => {
  return request({
    url: `/api/products/reviews/${id}`,
    method: 'delete'
  })
}

// Review approval (admin)
export const approveReview = (id, status) => {
  return request({
    url: `/api/products/reviews/${id}/approval`,
    method: 'put',
    data: { status }
  })
}
