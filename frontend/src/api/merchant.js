import request from '@/utils/request'

/**
 * Merchant-related APIs
 */

// ===== Admin side =====
// Merchant list (pagination + filters)
export const getMerchantList = (params) => {
  return request({
    url: '/api/admin/merchants',
    method: 'get',
    params
  })
}

// Merchant detail
export const getMerchantDetail = (id) => {
  return request({
    url: `/api/admin/merchants/${id}`,
    method: 'get'
  })
}

// Update merchant info
export const updateMerchant = (id, data) => {
  return request({
    url: `/api/admin/merchants/${id}`,
    method: 'put',
    data
  })
}

// Approve / reject merchant
export const approveMerchant = (id, status) => {
  return request({
    url: `/api/admin/merchants/${id}/approval`,
    method: 'put',
    data: { status }
  })
}

// Freeze/unfreeze merchant
export const toggleMerchantStatus = (id) => {
  return request({
    url: `/api/admin/merchants/${id}/toggle`,
    method: 'put'
  })
}

// ===== Merchant side =====
// Merchant dashboard data
export const getMerchantDashboard = (merchId) => {
  return request({
    url: '/api/merchant/dashboard',
    method: 'get',
    params: { merchId }
  })
}

// Get merchant profile
export const getMerchantProfile = (id) => {
  return request({
    url: `/api/merchant/profile/${id}`,
    method: 'get'
  })
}

// Update merchant profile
export const updateMerchantProfile = (data) => {
  const formData = new FormData()
  
  Object.keys(data).forEach(key => {
    const value = data[key]
    if (value !== undefined && value !== null) {
      if (key === 'logo' && typeof value === 'string' && value.startsWith('data:image')) {
        // Convert logo from base64 to Blob
        const logoBlob = base64ToBlob(value)
        formData.append('logoFile', logoBlob, 'merchant_logo.jpg')
      } else if (key !== 'logo') {
        formData.append(key, value)
      }
    }
  })

  return request({
    url: '/api/merchant/profile/update',
    method: 'post',
    data: formData,
    headers: {
      'Content-Type': 'multipart/form-data'
    }
  })
}

// Merchant product list
export const getMerchantProducts = (params) => {
  return request({
    url: '/api/merchant/products',
    method: 'get',
    params
  })
}

// Merchant product detail
export const getMerchantProduct = (id) => {
  return request({
    url: `/api/merchant/products/${id}`,
    method: 'get'
  })
}

// Helper function to convert base64 to Blob
const base64ToBlob = (base64String, mimeType = 'image/jpeg') => {
  // Remove data URL prefix if present
  const base64WithoutPrefix = base64String.replace(/^data:image\/\w+;base64,/, '')
  
  // Decode base64 string
  const binaryString = atob(base64WithoutPrefix)
  
  // Convert to byte array
  const bytes = new Uint8Array(binaryString.length)
  for (let i = 0; i < binaryString.length; i++) {
    bytes[i] = binaryString.charCodeAt(i)
  }
  
  // Create and return Blob
  return new Blob([bytes], { type: mimeType })
}

// Create product
export const createProduct = (data) => {
  // Convert data to FormData for multipart/form-data submission
  const formData = new FormData()
  
  console.log('createProduct data:', data)
  
  // Separate image URLs from file uploads
  const imageUrls = []
  const filesToUpload = []
  
  if (data.images && Array.isArray(data.images)) {
    data.images.forEach((img, index) => {
      if (typeof img === 'string') {
        if (img.startsWith('data:image')) {
          // Base64 image - needs conversion
          const imgBlob = base64ToBlob(img)
          filesToUpload.push(imgBlob)
        } else {
          // URL string - keep as-is
          imageUrls.push(img)
        }
      }
    })
  }
  
  // Add all product fields to FormData
  Object.keys(data).forEach(key => {
    const value = data[key]
    if (value !== undefined && value !== null && key !== 'images' && key !== 'detailImages') {
      if (key === 'image' && typeof value === 'string' && value.startsWith('data:image')) {
        // Convert main image from base64 to Blob
        const imageBlob = base64ToBlob(value)
        formData.append('mainImage', imageBlob, 'main_image.jpg')
      } else if (!['image'].includes(key)) {
        // Add other fields as-is
        if (Array.isArray(value)) {
          // Handle arrays (non-image)
          value.forEach(item => {
            formData.append(key, item)
          })
        } else {
          formData.append(key, value)
        }
      }
    }
  })
  
  // Add existing image URLs as a JSON string
  if (imageUrls.length > 0) {
    formData.append('existingImages', JSON.stringify(imageUrls))
    console.log('Existing image URLs:', imageUrls)
  }
  
  // Add new image files
  filesToUpload.forEach((file, index) => {
    formData.append('images', file, `detail_image_${index}.jpg`)
  })
  console.log('New images to upload:', filesToUpload.length)
  
  console.log('FormData being sent to /api/merchant/products (create)')
  
  return request({
    url: '/api/merchant/products',
    method: 'post',
    data: formData,
    headers: {
      'Content-Type': 'multipart/form-data'
    }
  })
}

// Update product
export const updateProduct = (id, data) => {
  // Convert data to FormData for multipart/form-data submission
  const formData = new FormData()
  
  console.log('updateProduct data:', data)
  
  // Separate image URLs from other data
  const imageUrls = []
  const filesToUpload = []
  
  if (data.images && Array.isArray(data.images)) {
    data.images.forEach((img, index) => {
      if (typeof img === 'string') {
        if (img.startsWith('data:image')) {
          // New image - needs to be uploaded
          const imgBlob = base64ToBlob(img)
          filesToUpload.push(imgBlob)
        } else {
          // Existing URL - just keep the URL
          imageUrls.push(img)
        }
      }
    })
  }
  
  // Add all product fields to FormData
  Object.keys(data).forEach(key => {
    const value = data[key]
    if (value !== undefined && value !== null && key !== 'images' && key !== 'detailImages') {
      if (key === 'image' && typeof value === 'string' && value.startsWith('data:image')) {
        // Convert main image from base64 to Blob
        const imageBlob = base64ToBlob(value)
        formData.append('mainImage', imageBlob, 'main_image.jpg')
      } else if (!['image'].includes(key)) {
        // Add other fields as-is
        if (Array.isArray(value)) {
          // Handle arrays (non-image)
          value.forEach(item => {
            formData.append(key, item)
          })
        } else {
          formData.append(key, value)
        }
      }
    }
  })
  
  // Add existing image URLs as a JSON string
  if (imageUrls.length > 0) {
    formData.append('existingImages', JSON.stringify(imageUrls))
    console.log('Existing image URLs:', imageUrls)
  }
  
  // Add new image files
  filesToUpload.forEach((file, index) => {
    formData.append('images', file, `detail_image_${index}.jpg`)
  })
  console.log('New images to upload:', filesToUpload.length)
  
  console.log('FormData being sent to /api/merchant/products/' + id)
  
  return request({
    url: `/api/merchant/products/${id}`,
    method: 'put',
    data: formData,
    headers: {
      'Content-Type': 'multipart/form-data'
    }
  })
}

// Upload product image directly (for real-time upload)
export const uploadProductImage = (file, type = 'detail') => {
  const formData = new FormData()
  formData.append('file', file)
  formData.append('type', type)
  
  return request({
    url: '/api/merchant/products/upload/image',
    method: 'post',
    data: formData,
    headers: {
      'Content-Type': 'multipart/form-data'
    }
  })
}

// Delete product
export const deleteProduct = (id) => {
  return request({
    url: `/api/merchant/products/${id}`,
    method: 'delete'
  })
}

// Update product status (on/off shelf)
export const updateProductStatus = (id, status) => {
  return request({
    url: `/api/merchant/products/${id}/status`,
    method: 'put',
    data: { status }
  })
}

// Get product trackability
export const getProductTrackability = (id) => {
  return request({
    url: `/api/merchant/products/${id}/trackability`,
    method: 'get'
  })
}

// Create trackability record
export const createTrackability = (id, data) => {
  return request({
    url: `/api/merchant/products/${id}/trackability`,
    method: 'post',
    data
  })
}

// Update trackability record
export const updateTrackability = (id, data) => {
  return request({
    url: `/api/merchant/products/trackability/${id}`,
    method: 'put',
    data
  })
}

// Delete trackability record
export const deleteTrackability = (id) => {
  return request({
    url: `/api/merchant/products/trackability/${id}`,
    method: 'delete'
  })
}

// Merchant order list
export const getMerchantOrders = (params) => {
  return request({
    url: '/api/merchant/orders',
    method: 'get',
    params
  })
}

// Merchant order detail
export const getMerchantOrder = (id) => {
  return request({
    url: `/api/merchant/orders/${id}`,
    method: 'get'
  })
}

// Ship order
export const shipOrder = (id, data) => {
  return request({
    url: `/api/merchant/orders/${id}/ship`,
    method: 'put',
    data
  })
}

// Merchant coupon list
export const getMerchantCoupons = (params) => {
  return request({
    url: '/api/merchant/coupons',
    method: 'get',
    params
  })
}

// Merchant coupon detail
export const getMerchantCoupon = (id) => {
  return request({
    url: `/api/merchant/coupons/${id}`,
    method: 'get'
  })
}

// Create coupon
export const createCoupon = (data) => {
  return request({
    url: '/api/merchant/coupons',
    method: 'post',
    data
  })
}

// Update coupon
export const updateCoupon = (id, data) => {
  return request({
    url: `/api/merchant/coupons/${id}`,
    method: 'put',
    data
  })
}

// Delete coupon
export const deleteCoupon = (id) => {
  return request({
    url: `/api/merchant/coupons/${id}`,
    method: 'delete'
  })
}
