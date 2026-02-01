// Export all utility functions and constants

export * from './format'
export * from './validate'
export * from './storage'

/**
 * Common enums
 */

// User type
export const UserType = {
  CUSTOMER: 'CUSTOMER',
  MERCHANT: 'MERCHANT',
  ADMIN: 'ADMIN'
}

// User status
export const UserStatus = {
  ACTIVE: 'ACTIVE',
  INACTIVE: 'INACTIVE',
  SUSPENDED: 'SUSPENDED'
}

// Merchant status
export const MerchantStatus = {
  PENDING: 'PENDING',
  ACTIVE: 'ACTIVE',
  REJECTED: 'REJECTED',
  SUSPENDED: 'SUSPENDED'
}

// Product status
export const ProductStatus = {
  DRAFT: 'DRAFT',
  PUBLISHED: 'PUBLISHED',
  UNPUBLISHED: 'UNPUBLISHED'
}

// Stock status
export const StockStatus = {
  IN_STOCK: 'IN_STOCK',
  LOW_STOCK: 'LOW_STOCK',
  OUT_OF_STOCK: 'OUT_OF_STOCK'
}

// Order status
export const OrderStatus = {
  SUBMITTED: 'SUBMITTED',      // submitted / pending payment
  PAID: 'PAID',                // paid / pending shipment
  SHIPPED: 'SHIPPED',          // shipped / pending delivery
  COMPLETED: 'COMPLETED',      // completed (delivered)
  CANCELLED: 'CANCELLED',
  REFUNDED: 'REFUNDED'
}

// Payment method
export const PaymentMethod = {
  ALIPAY: 'ALIPAY',
  WECHAT: 'WECHAT',
  BANK_CARD: 'BANK_CARD'
}

// Coupon type
export const CouponType = {
  DISCOUNT: 'DISCOUNT',
  FULL_REDUCTION: 'FULL_REDUCTION',
  FIXED_AMOUNT: 'FIXED_AMOUNT'
}

// Coupon status
export const CouponStatus = {
  NOT_STARTED: 'NOT_STARTED',
  ONGOING: 'ONGOING',
  EXPIRED: 'EXPIRED'
}

// Customer coupon status
export const CustomerCouponStatus = {
  NOT_USED: 'NOT_USED',
  USED: 'USED',
  EXPIRED: 'EXPIRED'
}

// Review approval status
export const ReviewStatus = {
  PENDING: 'PENDING',
  APPROVED: 'APPROVED',
  REJECTED: 'REJECTED'
}

// Status -> tag type mapping
export const StatusTagType = {
  [UserStatus.ACTIVE]: 'success',
  [UserStatus.INACTIVE]: 'info',
  [UserStatus.SUSPENDED]: 'danger',
  [MerchantStatus.PENDING]: 'warning',
  [MerchantStatus.ACTIVE]: 'success',
  [MerchantStatus.REJECTED]: 'danger',
  [MerchantStatus.SUSPENDED]: 'info',
  [ProductStatus.DRAFT]: 'info',
  [ProductStatus.PUBLISHED]: 'success',
  [ProductStatus.UNPUBLISHED]: 'warning',
  [StockStatus.IN_STOCK]: 'success',
  [StockStatus.LOW_STOCK]: 'warning',
  [StockStatus.OUT_OF_STOCK]: 'danger',
  [OrderStatus.SUBMITTED]: 'warning',
  [OrderStatus.PAID]: 'success',
  [OrderStatus.SHIPPED]: 'primary',
  [OrderStatus.COMPLETED]: 'success',
  [OrderStatus.CANCELLED]: 'info',
  [OrderStatus.REFUNDED]: 'danger',
  [CouponStatus.NOT_STARTED]: 'info',
  [CouponStatus.ONGOING]: 'success',
  [CouponStatus.EXPIRED]: 'danger',
  [CustomerCouponStatus.NOT_USED]: 'success',
  [CustomerCouponStatus.USED]: 'info',
  [CustomerCouponStatus.EXPIRED]: 'danger',
  [ReviewStatus.PENDING]: 'warning',
  [ReviewStatus.APPROVED]: 'success',
  [ReviewStatus.REJECTED]: 'danger'
}

// Status -> display text mapping
export const StatusText = {
  [UserStatus.ACTIVE]: 'Active',
  [UserStatus.INACTIVE]: 'Inactive',
  [UserStatus.SUSPENDED]: 'Suspended',
  [MerchantStatus.PENDING]: 'Pending Review',
  [MerchantStatus.ACTIVE]: 'Approved',
  [MerchantStatus.REJECTED]: 'Rejected',
  [MerchantStatus.SUSPENDED]: 'Suspended',
  [ProductStatus.DRAFT]: 'Draft',
  [ProductStatus.PUBLISHED]: 'Published',
  [ProductStatus.UNPUBLISHED]: 'Unpublished',
  [StockStatus.IN_STOCK]: 'In Stock',
  [StockStatus.LOW_STOCK]: 'Low Stock',
  [StockStatus.OUT_OF_STOCK]: 'Out of Stock',
  [OrderStatus.SUBMITTED]: 'Pending Payment',
  [OrderStatus.PAID]: 'Paid',
  [OrderStatus.SHIPPED]: 'Shipped',
  [OrderStatus.COMPLETED]: 'Completed',
  [OrderStatus.CANCELLED]: 'Cancelled',
  [OrderStatus.REFUNDED]: 'Refunded',
  [CouponStatus.NOT_STARTED]: 'Not Started',
  [CouponStatus.ONGOING]: 'Ongoing',
  [CouponStatus.EXPIRED]: 'Expired',
  [CustomerCouponStatus.NOT_USED]: 'Unused',
  [CustomerCouponStatus.USED]: 'Used',
  [CustomerCouponStatus.EXPIRED]: 'Expired',
  [ReviewStatus.PENDING]: 'Pending',
  [ReviewStatus.APPROVED]: 'Approved',
  [ReviewStatus.REJECTED]: 'Rejected'
}
