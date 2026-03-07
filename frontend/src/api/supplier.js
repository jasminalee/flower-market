import request from '@/utils/request'

/**
 * Admin Supplier APIs
 */

// Get supplier list with pagination
export const getSupplierPage = (params) => {
  return request({
    url: '/api/admin/suppliers/page',
    method: 'get',
    params
  })
}

// Get all active suppliers for dropdowns
export const getActiveSuppliers = () => {
  return request({
    url: '/api/admin/suppliers/list',
    method: 'get'
  })
}

// Get supplier details
export const getSupplierById = (id) => {
  return request({
    url: `/api/admin/suppliers/${id}`,
    method: 'get'
  })
}

// Save or Update supplier
export const saveSupplier = (data) => {
  return request({
    url: '/api/admin/suppliers/save',
    method: 'post',
    data
  })
}

// Delete supplier
export const deleteSupplier = (id) => {
  return request({
    url: `/api/admin/suppliers/${id}`,
    method: 'delete'
  })
}

// Update supplier status
export const updateSupplierStatus = (id, status) => {
  return request({
    url: `/api/admin/suppliers/${id}/status/${status}`,
    method: 'put'
  })
}
