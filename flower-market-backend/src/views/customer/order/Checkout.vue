<template>
  <div class="customer-page">
    <CustomerHeader />
    
    <div class="container">
      <div class="checkout-page">
        <el-card class="mt-lg">
          <template #header>
            <h2>Confirm Order</h2>
          </template>
          
          <div v-loading="loading">
            <!-- Shipping address -->
            <div class="section">
              <div class="section-header">
                <h3>Shipping Info</h3>
                <el-button size="small" @click="router.push('/profile/my-profile')">
                  Change Address
                </el-button>
              </div>
              
              <div v-if="userAddress" class="address-card">
                <div class="address-row">
                  <span class="label">Receiver:</span>
                  <span>{{ userStore.realName || userStore.username }}</span>
                </div>
                <div class="address-row">
                  <span class="label">Phone:</span>
                  <span>{{ userStore.phone || 'Not set' }}</span>
                </div>
                <div class="address-row">
                  <span class="label">Address:</span>
                  <span>{{ userAddress }}</span>
                </div>
              </div>
              <el-alert v-else type="warning" :closable="false">
                Please set a shipping address in your profile first.
              </el-alert>
            </div>
            
            <el-divider />
            
            <!-- Items -->
            <div class="section">
              <h3>Items</h3>
              <el-table :data="orderItems" border>
                <el-table-column label="Product" width="420">
                  <template #default="{ row }">
                    <div class="product-info">
                      <el-image 
                        :src="row.prodImage || 'https://via.placeholder.com/60x60'" 
                        fit="cover"
                        style="width: 60px; height: 60px; border-radius: 4px;"
                      />
                      <div class="product-details">
                        <div class="product-name">{{ row.prodName }}</div>
                        <div class="product-merchant">Merchant: {{ row.merchantName || 'Unknown' }} (ID: {{ row.merchId || '-' }})</div>
                      </div>
                    </div>
                  </template>
                </el-table-column>
                
                <el-table-column label="Unit Price" prop="prodPrice" width="120">
                  <template #default="{ row }">
                    ¥{{ row.prodPrice }}
                  </template>
                </el-table-column>
                
                <el-table-column label="Qty" prop="quantity" width="100" />
                
                <el-table-column label="Subtotal">
                  <template #default="{ row }">
                    <span class="subtotal">¥{{ (row.prodPrice * row.quantity).toFixed(2) }}</span>
                  </template>
                </el-table-column>
              </el-table>
            </div>
            
            <el-divider />
            
            <!-- Coupon -->
            <div class="section">
              <h3>Coupon</h3>
              <el-select 
                v-model="selectedCoupon" 
                placeholder="Select a coupon" 
                clearable
                style="width: 300px"
              >
                <el-option
                  v-for="coupon in availableCoupons"
                  :key="coupon.id"
                  :label="`${coupon.couponName} - Save ¥${coupon.value}`"
                  :value="coupon.id"
                />
              </el-select>
              <span v-if="availableCoupons.length === 0" class="text-secondary">No available coupons</span>
            </div>
            
            <el-divider />
            
            <!-- Amounts -->
            <div class="section">
              <div class="amount-detail">
                <div class="amount-item">
                  <span>Items total:</span>
                  <span>¥{{ totalAmount.toFixed(2) }}</span>
                </div>
                <div class="amount-item" v-if="discountAmount > 0">
                  <span>Discount:</span>
                  <span class="discount">-¥{{ discountAmount.toFixed(2) }}</span>
                </div>
                <div class="amount-item total">
                  <span>Payable:</span>
                  <span class="final-price">¥{{ finalAmount.toFixed(2) }}</span>
                </div>
              </div>
            </div>
            
            <!-- Submit -->
            <div class="submit-section">
              <el-button 
                type="primary" 
                size="large"
                @click="handleSubmitOrder"
                :loading="submitting"
              >
                Place Order
              </el-button>
            </div>
          </div>
        </el-card>
      </div>
    </div>
    
    <CustomerFooter />
  </div>
</template>

<script setup>
import { ref, reactive, computed, onMounted } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import CustomerHeader from '@/components/layouts/CustomerHeader.vue'
import CustomerFooter from '@/components/layouts/CustomerFooter.vue'
import { getCustomerCouponList } from '@/api/coupon'
import { createOrder } from '@/api/order'
import { useCartStore } from '@/stores/cart'
import { useUserStore } from '@/stores/user'
import { ElMessage } from 'element-plus'

const router = useRouter()
const route = useRoute()
const cartStore = useCartStore()
const userStore = useUserStore()

const loading = ref(false)
const submitting = ref(false)
const orderItems = ref([])
const availableCoupons = ref([])
const selectedCoupon = ref(null)
const userAddress = ref('')

// Total amount
const totalAmount = computed(() => {
  return orderItems.value.reduce((sum, item) => sum + item.prodPrice * item.quantity, 0)
})

// Discount amount
const discountAmount = computed(() => {
  if (!selectedCoupon.value) return 0
  const coupon = availableCoupons.value.find(c => c.id === selectedCoupon.value)
  return coupon?.value || 0
})

// Final amount
const finalAmount = computed(() => {
  return Math.max(0, totalAmount.value - discountAmount.value)
})

onMounted(async () => {
  if (!userStore.isLoggedIn) {
    ElMessage.warning('Please log in first')
    router.push('/login')
    return
  }
  
  await loadData()
})

const loadData = async () => {
  loading.value = true
  try {
    // Get user address
    userAddress.value = userStore.userInfo?.address || ''
    
    // Load selected items from cart
    const itemIds = route.query.items?.split(',') || []
    await cartStore.loadCart()
    orderItems.value = cartStore.cartItems.filter(item => 
      itemIds.includes(String(item.id))
    )
    
    if (orderItems.value.length === 0) {
      ElMessage.warning('No items selected')
      router.push('/cart')
      return
    }
    
    // Load available coupons
    const couponRes = await getCustomerCouponList({ 
      userId: userStore.userId,
      status: 'UNUSED'
    })
    availableCoupons.value = couponRes.data || []
  } catch (error) {
    console.error('Load checkout data error:', error)
    ElMessage.error('Failed to load data')
  } finally {
    loading.value = false
  }
}

const handleSubmitOrder = async () => {
  if (!userAddress.value) {
    ElMessage.warning('Please set a shipping address first')
    return
  }
  
  submitting.value = true
  try {
    const orderData = {
      userId: userStore.userId,
      address: userAddress.value,
      receiverName: userStore.realName || userStore.username,
      receiverPhone: userStore.phone,
      customerCouponId: selectedCoupon.value,
      items: orderItems.value.map(item => ({
        prodId: item.prodId,
        quantity: item.quantity,
        price: item.prodPrice
      })),
      totalAmount: totalAmount.value,
      discountAmount: discountAmount.value,
      finalAmount: finalAmount.value
    }
    
    const res = await createOrder(orderData)
    
    ElMessage.success('Order created successfully')
    
    // Go to orders
    router.push('/profile/orders')
  } catch (error) {
    ElMessage.error(error.message || 'Failed to create order')
  } finally {
    submitting.value = false
  }
}
</script>

<style scoped>
.customer-page {
  min-height: 100vh;
  background: var(--color-bg-base);
}

.checkout-page {
  padding-bottom: var(--spacing-xl);
}

.section {
  margin-bottom: var(--spacing-xl);
}

.section-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: var(--spacing-md);
}

.section h3 {
  margin: 0 0 var(--spacing-md) 0;
  font-size: 18px;
  font-weight: 600;
}

/* Address card */
.address-card {
  padding: var(--spacing-lg);
  background: var(--color-bg-light);
  border-radius: var(--radius-md);
  border: 1px solid var(--color-border);
}

.address-row {
  display: flex;
  margin-bottom: var(--spacing-sm);
  font-size: 14px;
}

.address-row:last-child {
  margin-bottom: 0;
}

.address-row .label {
  color: var(--color-text-secondary);
  min-width: 80px;
}

/* Product information */
.product-info {
  display: flex;
  align-items: center;
  gap: var(--spacing-md);
}

.product-details {
  display: flex;
  flex-direction: column;
}

.product-name {
  font-weight: 500;
}

.product-merchant {
  color: var(--color-text-secondary);
  font-size: 12px;
}

.subtotal {
  color: var(--color-danger);
  font-weight: 600;
}

/* Amount details */
.amount-detail {
  max-width: 400px;
  margin-left: auto;
}

.amount-item {
  display: flex;
  justify-content: space-between;
  padding: var(--spacing-sm) 0;
  font-size: 14px;
}

.amount-item.total {
  font-size: 18px;
  font-weight: 600;
  padding-top: var(--spacing-md);
  border-top: 1px solid var(--color-border);
}

.discount {
  color: var(--color-success);
}

.final-price {
  color: var(--color-danger);
  font-size: 24px;
}

/* Submit section */
.submit-section {
  display: flex;
  justify-content: flex-end;
  margin-top: var(--spacing-xl);
  padding-top: var(--spacing-lg);
  border-top: 1px solid var(--color-border);
}

.text-secondary {
  color: var(--color-text-secondary);
  font-size: 14px;
}
</style>
