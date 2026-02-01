/**
 * Formatting helpers
 */

// Format price
export const formatPrice = (price) => {
  if (price === null || price === undefined) return '0.00'
  return Number(price).toFixed(2)
}

// Format date time
export const formatDateTime = (dateTime) => {
  if (!dateTime) return '-'
  const date = new Date(dateTime)
  const year = date.getFullYear()
  const month = String(date.getMonth() + 1).padStart(2, '0')
  const day = String(date.getDate()).padStart(2, '0')
  const hour = String(date.getHours()).padStart(2, '0')
  const minute = String(date.getMinutes()).padStart(2, '0')
  const second = String(date.getSeconds()).padStart(2, '0')
  return `${year}-${month}-${day} ${hour}:${minute}:${second}`
}

// Format date
export const formatDate = (date) => {
  if (!date) return '-'
  const d = new Date(date)
  const year = d.getFullYear()
  const month = String(d.getMonth() + 1).padStart(2, '0')
  const day = String(d.getDate()).padStart(2, '0')
  return `${year}-${month}-${day}`
}

// Format time (HH:mm:ss)
export const formatTime = (time) => {
  if (!time) return '-'
  const date = new Date(time)
  const hour = String(date.getHours()).padStart(2, '0')
  const minute = String(date.getMinutes()).padStart(2, '0')
  const second = String(date.getSeconds()).padStart(2, '0')
  return `${hour}:${minute}:${second}`
}

// Format relative time (just now, minutes ago, hours ago)
export const formatRelativeTime = (time) => {
  if (!time) return '-'
  
  const now = Date.now()
  const past = new Date(time).getTime()
  const diff = now - past
  
  const minute = 60 * 1000
  const hour = 60 * minute
  const day = 24 * hour
  const month = 30 * day
  const year = 365 * day
  
  if (diff < minute) {
    return 'just now'
  } else if (diff < hour) {
    return `${Math.floor(diff / minute)} minutes ago`
  } else if (diff < day) {
    return `${Math.floor(diff / hour)} hours ago`
  } else if (diff < month) {
    return `${Math.floor(diff / day)} days ago`
  } else if (diff < year) {
    return `${Math.floor(diff / month)} months ago`
  } else {
    return `${Math.floor(diff / year)} years ago`
  }
}

// Mask phone (hide middle 4 digits)
export const formatPhone = (phone) => {
  if (!phone) return '-'
  return phone.replace(/(\d{3})\d{4}(\d{4})/, '$1****$2')
}

// Mask ID card (hide middle part)
export const formatIdCard = (idCard) => {
  if (!idCard) return '-'
  return idCard.replace(/(\d{6})\d+(\d{4})/, '$1********$2')
}

// Format file size
export const formatFileSize = (size) => {
  if (!size) return '0 B'
  const units = ['B', 'KB', 'MB', 'GB', 'TB']
  let index = 0
  while (size >= 1024 && index < units.length - 1) {
    size /= 1024
    index++
  }
  return `${size.toFixed(2)} ${units[index]}`
}

// Format number with thousand separators
export const formatNumber = (num) => {
  if (num === null || num === undefined) return '0'
  return num.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ',')
}

// Format percentage
export const formatPercent = (value, total, decimals = 2) => {
  if (!total || total === 0) return '0%'
  return `${((value / total) * 100).toFixed(decimals)}%`
}

// Desensitize string
export const desensitize = (str, start = 3, end = 4) => {
  if (!str) return '-'
  const len = str.length
  if (len <= start + end) {
    return str.replace(/.(?=.{0,2}$)/g, '*')
  }
  return str.substring(0, start) + '*'.repeat(len - start - end) + str.substring(len - end)
}
