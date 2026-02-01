<template>
  <div class="order-detail-page">
    <el-card v-loading="loading">
      <template #header>
        <div class="card-header">
          <h2>Order Details</h2>
          <el-button @click="goBack" text>
            <el-icon><ArrowLeft /></el-icon>
            Back to Orders
          </el-button>
        </div>
      </template>
      
      <div class="order-detail-content" v-if="order">
        <!-- Order status -->
        <div class="order-status-section">
          <el-steps :active="getStepActive(order.status)" finish-status="success" align-center>
            <el-step title="Order Placed" :description="order.orderDate" />
            <el-step title="Payment Received" :description="order.paymentTime || ''" />
            <el-step title="Shipped" :description="order.deliveryTime || ''" />
            <el-step title="Delivered" :description="order.completionTime || ''" />
            <el-step title="Completed" :description="order.completionTime || ''" />
          </el-steps>
          
          <div class="current-status">
            <el-tag :type="getStatusType(order.status)" size="large">
              {{ getStatusText(order.status) }}
            </el-tag>
          </div>
        </div>
        
        <el-divider />
        
        <!-- Order info -->
        <div class="order-info-section">
          <h3>Order Information</h3>
          <el-descriptions :column="2" border>
            <el-descriptions-item label="Order No">{{ order.orderNo }}</el-descriptions-item>
            <el-descriptions-item label="Order Time">{{ order.orderDate }}</el-descriptions-item>
            <el-descriptions-item label="Merchant">{{ order.merchantName || 'Unknown merchant' }}</el-descriptions-item>
            <el-descriptions-item label="Payment Method">{{ order.paymentMethod || 'Online payment' }}</el-descriptions-item>
            <el-descriptions-item label="Payment Time">{{ order.paymentTime || 'Unpaid' }}</el-descriptions-item>
            <el-descriptions-item label="Status">
              <el-tag :type="getStatusType(order.status)">{{ getStatusText(order.status) }}</el-tag>
            </el-descriptions-item>
            <el-descriptions-item label="Notes">{{ order.remark || 'N/A' }}</el-descriptions-item>
          </el-descriptions>
        </div>
        
        <el-divider />
        
        <!-- Shipping address -->
        <div class="address-section">
          <h3>Shipping Information</h3>
          <el-descriptions :column="1" border v-if="order.address">
            <el-descriptions-item label="Recipient">{{ order.receiverName }}</el-descriptions-item>
            <el-descriptions-item label="Phone">{{ order.receiverPhone }}</el-descriptions-item>
            <el-descriptions-item label="Address">
              {{ order.address }}
            </el-descriptions-item>
          </el-descriptions>
        </div>
        
        <el-divider />
        
        <!-- Items -->
        <div class="products-section">
          <h3>Items</h3>
          <el-table :data="order.items" border style="width: 100%">
            <el-table-column label="Product" min-width="300">
              <template #default="{ row }">
                <div class="product-info">
                  <el-image 
                    :src="row.mainImage || 'https://via.placeholder.com/60x60'" 
                    fit="cover"
                    style="width: 60px; height: 60px; border-radius: 4px;"
                    crossorigin="anonymous"
                  />
                  <div class="product-name">{{ row.name }}</div>
                </div>
              </template>
            </el-table-column>
            <el-table-column prop="unitPrice" label="Unit Price" width="120">
              <template #default="{ row }">
                ¥{{ row.unitPrice }}
              </template>
            </el-table-column>
            <el-table-column prop="quantity" label="Qty" width="100" align="center" />
            <el-table-column label="Subtotal" width="120">
              <template #default="{ row }">
                <span class="subtotal">¥{{ row.totalPrice }}</span>
              </template>
            </el-table-column>
          </el-table>
        </div>
        
        <el-divider />
        
        <!-- Price breakdown -->
        <div class="price-section">
          <h3>Price Breakdown</h3>
          <div class="price-details">
            <div class="price-item">
              <span class="label">Items Total:</span>
              <span class="value">¥{{ order.totalPrice }}</span>
            </div>
            <div class="price-item" v-if="order.discountAmount > 0">
              <span class="label">Discount:</span>
              <span class="value discount">-¥{{ order.discountAmount }}</span>
            </div>
            <div class="price-item total">
              <span class="label">Amount Paid:</span>
              <span class="value">¥{{ order.actualPrice }}</span>
            </div>
          </div>
        </div>
        
        <el-divider />
        
        <!-- Logistics -->
        <div class="logistics-section" v-if="order.status === 'SHIPPED' || order.status === 'COMPLETED'">
          <h3>Shipping Information</h3>
          <el-descriptions :column="2" border v-if="order.logistics">
            <el-descriptions-item label="Carrier">{{ order.logistics?.company || 'N/A' }}</el-descriptions-item>
            <el-descriptions-item label="Tracking No">{{ order.logistics?.trackingNo || 'N/A' }}</el-descriptions-item>
            <el-descriptions-item label="Shipped At" :span="2">{{ order.deliveryTime || 'N/A' }}</el-descriptions-item>
          </el-descriptions>
          <el-alert 
            type="info" 
            :closable="false" 
            style="margin-top: 15px;"
            v-if="order.logistics && order.logistics.trackingNo"
          >
            <template #title>
              You can use the tracking number on the carrier website to check detailed shipping status.
            </template>
          </el-alert>
        </div>
        
        <el-divider />
        
        <!-- Actions -->
        <div class="order-actions">
          <el-button 
            v-if="order.status === 'SUBMITTED'" 
            type="primary" 
            size="large"
            @click="handlePay"
            :loading="actionLoading"
          >
            Pay Now
          </el-button>
          
          <el-button 
            v-if="order.status === 'SUBMITTED'" 
            size="large"
            @click="handleCancel"
            :loading="actionLoading"
          >
            Cancel Order
          </el-button>
          
          <el-button 
            v-if="order.status === 'SHIPPED'" 
            type="primary" 
            size="large"
            @click="handleConfirmReceipt"
            :loading="actionLoading"
          >
            Confirm Receipt
          </el-button>
          
          <el-button 
            v-if="order.status === 'COMPLETED' && !order.hasReview" 
            type="primary" 
            size="large"
            @click="handleReview"
          >
            Review Order
          </el-button>
          
          <el-button 
            size="large"
            @click="goBack"
          >
            Back
          </el-button>
        </div>
      </div>
    </el-card>
    
    <!-- Payment dialog -->
    <el-dialog v-model="payDialogVisible" title="Select Payment Method" width="400px">
      <el-radio-group v-model="payType" class="pay-types">
        <el-radio label="ALIPAY" size="large">
          <div class="pay-option">
            <el-icon size="24"><Wallet /></el-icon>
            <span>Alipay</span>
          </div>
        </el-radio>
        <el-radio label="WECHAT" size="large">
          <div class="pay-option">
            <el-icon size="24"><ChatDotRound /></el-icon>
            <span>WeChat Pay</span>
          </div>
        </el-radio>
        <el-radio label="BALANCE" size="large">
          <div class="pay-option">
            <el-icon size="24"><Money /></el-icon>
            <span>Balance</span>
          </div>
        </el-radio>
      </el-radio-group>
      
      <template #footer>
        <el-button @click="payDialogVisible = false">Cancel</el-button>
        <el-button type="primary" @click="confirmPay" :loading="actionLoading">
          Confirm payment ¥{{ order?.finalAmount }}
        </el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { ArrowLeft, Wallet, ChatDotRound, Money } from '@element-plus/icons-vue'
import { getOrderDetail, payOrder, cancelOrder, confirmOrder } from '@/api/order'
import { ElMessage, ElMessageBox } from 'element-plus'

const router = useRouter()
const route = useRoute()

const loading = ref(false)
const actionLoading = ref(false)
const order = ref(null)
const payDialogVisible = ref(false)
const payType = ref('ALIPAY')

onMounted(() => {
  loadOrderDetail()
})

const loadOrderDetail = async () => {
  loading.value = true
  try {
    const id = route.params.id
    const res = await getOrderDetail(id)
    order.value = res.data
  } catch (error) {
    console.error('Load order detail error:', error)
    ElMessage.error('Failed to load order details')
  } finally {
    loading.value = false
  }
}

const getStepActive = (status) => {
  const statusMap = {
    // Element Plus Steps: steps before `active` use finish-status.
    // Map to show completed orders with all steps green.
    SUBMITTED: 1,
    PAID: 2,
    SHIPPED: 3,
    COMPLETED: 5,
    CANCELLED: 0
  }
  return statusMap[status] || 0
}

const getStatusType = (status) => {
  const typeMap = {
    SUBMITTED: 'warning',
    PAID: 'info',
    SHIPPED: 'primary',
    COMPLETED: 'success',
    CANCELLED: 'danger'
  }
  return typeMap[status] || 'info'
}

const getStatusText = (status) => {
  const textMap = {
    SUBMITTED: 'Pending Payment',
    PAID: 'Paid',
    SHIPPED: 'Shipped',
    COMPLETED: 'Completed',
    CANCELLED: 'Cancelled'
  }
  return textMap[status] || 'Unknown'
}

const handlePay = () => {
  payDialogVisible.value = true
}

const confirmPay = async () => {
  actionLoading.value = true
  try {
    await payOrder(order.value.id, payType.value)
    ElMessage.success('Payment successful')
    payDialogVisible.value = false
    await loadOrderDetail()
  } catch (error) {
    console.error('Pay order error:', error)
    ElMessage.error(error.message || 'Payment failed')
  } finally {
    actionLoading.value = false
  }
}

const handleCancel = async () => {
  try {
    await ElMessageBox.confirm('Are you sure you want to cancel this order?', 'Confirm', {
      confirmButtonText: 'OK',
      cancelButtonText: 'Cancel',
      type: 'warning'
    })
    
    actionLoading.value = true
    await cancelOrder(order.value.id)
    ElMessage.success('Order cancelled')
    await loadOrderDetail()
  } catch (error) {
    if (error !== 'cancel') {
      console.error('Cancel order error:', error)
      ElMessage.error(error.message || 'Failed to cancel order')
    }
  } finally {
    actionLoading.value = false
  }
}

const handleConfirmReceipt = async () => {
  try {
    await ElMessageBox.confirm('Confirm you have received the items?', 'Confirm', {
      confirmButtonText: 'OK',
      cancelButtonText: 'Cancel',
      type: 'info'
    })
    
    actionLoading.value = true
    await confirmOrder(order.value.id)
    ElMessage.success('Receipt confirmed')
    await loadOrderDetail()
  } catch (error) {
    if (error !== 'cancel') {
      console.error('Confirm order error:', error)
      ElMessage.error(error.message || 'Failed to confirm receipt')
    }
  } finally {
    actionLoading.value = false
  }
}

const handleReview = () => {
  router.push(`/profile/orders/${order.value.id}/review`)
}

const goBack = () => {
  router.push('/profile/orders')
}
</script>

<style scoped>
.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.card-header h2 {
  font-size: 20px;
  font-weight: 600;
  color: #333;
  margin: 0;
}

.order-detail-content {
  padding: 20px 0;
}

.order-status-section {
  padding: 30px 0;
}

.current-status {
  text-align: center;
  margin-top: 30px;
}

.order-info-section,
.address-section,
.products-section,
.price-section,
.logistics-section {
  margin: 20px 0;
}

.order-info-section h3,
.address-section h3,
.products-section h3,
.price-section h3,
.logistics-section h3 {
  font-size: 18px;
  font-weight: 600;
  color: #333;
  margin-bottom: 15px;
}

.product-info {
  display: flex;
  align-items: center;
  gap: 15px;
}

.product-name {
  flex: 1;
  font-size: 14px;
  color: #333;
}

.subtotal {
  color: var(--el-color-danger);
  font-weight: 500;
}

.price-details {
  background-color: #f5f7fa;
  padding: 20px;
  border-radius: 8px;
}

.price-item {
  display: flex;
  justify-content: space-between;
  padding: 10px 0;
  font-size: 15px;
}

.price-item .label {
  color: #666;
}

.price-item .value {
  color: #333;
  font-weight: 500;
}

.price-item .value.discount {
  color: var(--el-color-success);
}

.price-item.total {
  border-top: 1px dashed #dcdfe6;
  margin-top: 10px;
  padding-top: 20px;
  font-size: 18px;
}

.price-item.total .value {
  color: var(--el-color-danger);
  font-size: 24px;
  font-weight: 600;
}

.order-actions {
  display: flex;
  gap: 15px;
  justify-content: center;
  padding: 30px 0 10px;
}

.pay-types {
  display: flex;
  flex-direction: column;
  gap: 15px;
}

.pay-types :deep(.el-radio) {
  margin-right: 0;
  padding: 15px;
  border: 1px solid #dcdfe6;
  border-radius: 8px;
  transition: all 0.3s;
}

.pay-types :deep(.el-radio:hover) {
  border-color: var(--el-color-primary);
}

.pay-types :deep(.el-radio.is-checked) {
  border-color: var(--el-color-primary);
  background-color: #ecf5ff;
}

.pay-option {
  display: flex;
  align-items: center;
  gap: 10px;
  font-size: 16px;
}
</style>
