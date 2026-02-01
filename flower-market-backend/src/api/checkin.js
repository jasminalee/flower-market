import request from '@/utils/request'

/**
 * Check-in APIs
 */

// Daily check-in
export const checkin = (userId) => {
  return request({
    url: '/api/customer/checkin',
    method: 'post',
    params: { userId }
  })
}

// Check-in history
export const getCheckinHistory = (userId) => {
  return request({
    url: '/api/customer/checkin/history',
    method: 'get',
    params: { userId }
  })
}

// Check-in stats
export const getCheckinStats = (userId) => {
  return request({
    url: '/api/customer/checkin/stats',
    method: 'get',
    params: { userId }
  })
}
