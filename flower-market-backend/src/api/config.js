import request from '@/utils/request'

/**
 * System configuration APIs
 */

// Config list (pagination + category filter)
export const getConfigList = (params) => {
  return request({
    url: '/api/admin/config',
    method: 'get',
    params
  })
}

// Get config by key
export const getConfigByKey = (configKey) => {
  return request({
    url: `/api/admin/config/key/${configKey}`,
    method: 'get'
  })
}

// Get config list by category
export const getConfigByCategory = (category) => {
  return request({
    url: `/api/admin/config/category/${category}`,
    method: 'get'
  })
}

// Create config
export const createConfig = (data) => {
  return request({
    url: '/api/admin/config',
    method: 'post',
    data
  })
}

// Update config
export const updateConfig = (id, data) => {
  return request({
    url: `/api/admin/config/${id}`,
    method: 'put',
    data
  })
}

// Delete config
export const deleteConfig = (id) => {
  return request({
    url: `/api/admin/config/${id}`,
    method: 'delete'
  })
}

// Batch update configs
export const batchUpdateConfig = (data) => {
  return request({
    url: '/api/admin/config/batch',
    method: 'put',
    data
  })
}
