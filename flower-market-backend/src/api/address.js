import request from '@/utils/request'

/**
 * Shipping address APIs
 */

// Address list
export const getAddressList = (userId) => {
  return request({
    url: '/api/customer/addresses',
    method: 'get',
    params: { userId }
  })
}

// Address detail
export const getAddressDetail = (id) => {
  return request({
    url: `/api/customer/addresses/${id}`,
    method: 'get'
  })
}

// Create address
export const createAddress = (data) => {
  return request({
    url: '/api/customer/addresses',
    method: 'post',
    data
  })
}

// Update address
export const updateAddress = (id, data) => {
  return request({
    url: `/api/customer/addresses/${id}`,
    method: 'put',
    data
  })
}

// Delete address
export const deleteAddress = (id) => {
  return request({
    url: `/api/customer/addresses/${id}`,
    method: 'delete'
  })
}

// Set default address
export const setDefaultAddress = (id) => {
  return request({
    url: `/api/customer/addresses/${id}/default`,
    method: 'put'
  })
}
