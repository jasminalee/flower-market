import request from '@/utils/request'

/**
 * Care knowledge APIs
 */

// Knowledge list (paginated)
export const getKnowledgeList = (params) => {
  return request({
    url: '/api/admin/knowledge',
    method: 'get',
    params
  })
}

// Knowledge detail
export const getKnowledgeDetail = (id) => {
  return request({
    url: `/api/admin/knowledge/${id}`,
    method: 'get'
  })
}

// Create knowledge (admin)
export const createKnowledge = (data) => {
  return request({
    url: '/api/admin/knowledge',
    method: 'post',
    data
  })
}

// Update knowledge (admin)
export const updateKnowledge = (id, data) => {
  return request({
    url: `/api/admin/knowledge/${id}`,
    method: 'put',
    data
  })
}

// Delete knowledge (admin)
export const deleteKnowledge = (id) => {
  return request({
    url: `/api/admin/knowledge/${id}`,
    method: 'delete'
  })
}
