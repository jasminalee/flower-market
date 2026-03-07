<template>
  <div class="my-orders-page">
    <el-card>
      <template #header>
        <div class="card-header">
          <h2>My Orders</h2>
        </div>
      </template>
      
      <!-- Status filter -->
      <div class="filter-tabs">
        <el-radio-group v-model="filterStatus" @change="handleFilterChange">
          <el-radio-button label="">All</el-radio-button>
          <el-radio-button label="SUBMITTED">Pending Payment</el-radio-button>
          <el-radio-button label="PAID">Pending Shipment</el-radio-button>
          <el-radio-button label="SHIPPED">Pending Receipt</el-radio-button>
          <el-radio-button label="COMPLETED">Completed</el-radio-button>
          <el-radio-button label="REFUND_APPLIED">Refunding</el-radio-button>
          <el-radio-button label="REFUNDED">Refunded</el-radio-button>
          <el-radio-button label="CANCELLED">Cancelled</el-radio-button>
        </el-radio-group>
      </div>
      
      <!-- Orders -->
      <div class="orders-list" v-loading="loading">
        <div class="order-item" v-for="order in orders" :key="order.id">
          <div class="order-header">
            <div class="order-header__left">
              <span>Order No: {{ order.orderNo }}</span>
              <span class="merchant">Merchant: {{ order.merchantName || 'Unknown' }}</span>
            </div>
            <div class="order-header__right">
              <span>{{ order.orderDate }}</span>
              <el-tag :type="getStatusType(order.status)">{{ getStatusText(order.status) }}</el-tag>
            </div>
          </div>
          
          <div class="order-body">
            <div class="order-products">
              <div 
                class="product-item" 
                v-for="item in order.items" 
                :key="item.id"
              >
                <el-image 
                  :src="item.mainImage || 'https://via.placeholder.com/80x80'" 
                  fit="cover"
                  style="width: 80px; height: 80px; border-radius: 4px;"
                  crossorigin="anonymous"
                />
                <div class="product-info">
                  <div class="product-name">{{ item.name }}</div>
                  <div class="product-spec">
                    <span>¥{{ item.unitPrice }}</span>
                    <span>x{{ item.quantity }}</span>
                  </div>
                </div>
              </div>
            </div>
            
            <div class="order-amount">
              <div class="amount-label">Amount</div>
              <div class="amount-value">¥{{ Math.max(0, order.actualPrice || 0) }}</div>
            </div>
            
            <div class="order-actions">
              <el-button 
                v-if="order.status === 'SUBMITTED'" 
                type="primary" 
                size="small"
                @click="handlePay(order)"
              >
                Pay Now
              </el-button>
              
              <el-button 
                v-if="order.status === 'SUBMITTED'" 
                size="small"
                @click="handleCancel(order)"
              >
                Cancel Order
              </el-button>
              
              <el-button 
                v-if="order.status === 'SHIPPED'" 
                type="primary" 
                size="small"
                @click="handleConfirmReceipt(order)"
              >
                Confirm Receipt
              </el-button>

              <el-button 
                v-if="order.status === 'PAID' || order.status === 'SHIPPED'" 
                type="danger" 
                plain
                size="small"
                @click="handleRefund(order)"
              >
                Refund
              </el-button>
              
              <el-button 
                v-if="order.status === 'COMPLETED' && !order.hasReview" 
                type="primary" 
                size="small"
                @click="handleReview(order)"
              >
                Review
              </el-button>
              
              <el-button 
                size="small"
                @click="handleViewDetail(order)"
              >
                Details
              </el-button>
            </div>
          </div>
        </div>
        
        <el-empty v-if="orders.length === 0" description="No orders" />
      </div>
      
      <!-- Pagination -->
      <div class="pagination-wrapper" v-if="total > 0">
        <el-pagination
          v-model:current-page="pagination.current"
          v-model:page-size="pagination.size"
          :page-sizes="[10, 20, 30, 50]"
          :total="total"
          layout="total, sizes, prev, pager, next, jumper"
          @size-change="loadOrders"
          @current-change="loadOrders"
        />
      </div>
    </el-card>
    
    <!-- Payment dialog -->
    <el-dialog v-model="showPayDialog" title="Pay Order" width="400px">
      <div class="pay-dialog-content">
        <div class="pay-amount">
          <span>Amount:</span>
          <span class="amount">¥{{ Math.max(0, currentOrder?.actualPrice || 0) }}</span>
        </div>
        <el-radio-group v-model="paymentMethod" class="payment-methods">
          <el-radio label="ALIPAY">Alipay</el-radio>
          <el-radio label="WECHAT">WeChat Pay</el-radio>
          <el-radio label="BALANCE">Balance</el-radio>
        </el-radio-group>
      </div>
      
      <template #footer>
        <el-button @click="showPayDialog = false">Cancel</el-button>
        <el-button type="primary" @click="handleConfirmPay" :loading="paying">Confirm</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { getOrderList, cancelOrder, confirmOrder, payOrder, applyRefund } from '@/api/order'
import { useUserStore } from '@/stores/user'
import { ElMessage, ElMessageBox } from 'element-plus'

const router = useRouter()
const userStore = useUserStore()

const loading = ref(false)
const orders = ref([])
const total = ref(0)
const filterStatus = ref('')
const showPayDialog = ref(false)
const currentOrder = ref(null)
const paymentMethod = ref('ALIPAY')
const paying = ref(false)

const pagination = reactive({
  current: 1,
  size: 10
})

onMounted(async () => {
  await loadOrders()
})

const loadOrders = async () => {
  loading.value = true
  try {
    const params = {
      current: pagination.current,
      size: pagination.size,
      userId: userStore.userId
    }
    
    if (filterStatus.value) {
      params.status = filterStatus.value
    }
    
    const res = await getOrderList(params)
    console.log('Order list response:', res.data)
    orders.value = res.data?.records || []
    console.log('Orders:', orders.value)
    if (orders.value.length > 0) {
      console.log('First order:', orders.value[0])
      console.log('Order ID fields:', orders.value[0].id, orders.value[0].orderId)
    }
    total.value = res.data?.total || 0
  } catch (error) {
    console.error('Load orders error:', error)
    ElMessage.error('Failed to load orders')
  } finally {
    loading.value = false
  }
}

const handleFilterChange = () => {
  pagination.current = 1
  loadOrders()
}

const getStatusType = (status) => {
  const typeMap = {
    SUBMITTED: 'warning',
    PAID: 'info',
    SHIPPED: 'primary',
    COMPLETED: 'success',
    CANCELLED: 'danger',
    REFUND_APPLIED: 'warning',
    REFUNDED: 'info',
    REFUND_REJECTED: 'danger'
  }
  return typeMap[status] || 'info'
}

const getStatusText = (status) => {
  const textMap = {
    SUBMITTED: 'Pending Payment',
    PAID: 'Paid',
    SHIPPED: 'Shipped',
    COMPLETED: 'Completed',
    CANCELLED: 'Cancelled',
    REFUND_APPLIED: 'Refunding',
    REFUNDED: 'Refunded',
    REFUND_REJECTED: 'Refund Rejected'
  }
  return textMap[status] || status
}

const handlePay = (order) => {
  currentOrder.value = order
  showPayDialog.value = true
}

const handleConfirmPay = async () => {
  paying.value = true
  try {
    await payOrder(currentOrder.value.id, paymentMethod.value)
    
    ElMessage.success('Payment successful')
    showPayDialog.value = false
    await loadOrders()
  } catch (error) {
    ElMessage.error(error.message || 'Payment failed')
  } finally {
    paying.value = false
  }
}

const handleCancel = async (order) => {
  try {
    await ElMessageBox.confirm('Are you sure you want to cancel this order?', 'Confirmation', {
      confirmButtonText: 'OK',
      cancelButtonText: 'Cancel',
      type: 'warning'
    })
    
    await cancelOrder(order.id)
    ElMessage.success('Order cancelled')
    await loadOrders()
  } catch (error) {
    if (error !== 'cancel') {
      ElMessage.error(error.message || 'Failed to cancel order')
    }
  }
}

const handleConfirmReceipt = async (order) => {
  try {
    await ElMessageBox.confirm('Confirm that you have received the items?', 'Confirmation', {
      confirmButtonText: 'OK',
      cancelButtonText: 'Cancel',
      type: 'info'
    })
    
    await confirmOrder(order.id)
    ElMessage.success('Receipt confirmed')
    await loadOrders()
  } catch (error) {
    if (error !== 'cancel') {
      ElMessage.error(error.message || 'Failed to confirm receipt')
    }
  }
}

const handleRefund = async (order) => {
  try {
    const { value: reason } = await ElMessageBox.prompt('Please enter the reason for refund:', 'Apply for Refund', {
      confirmButtonText: 'Submit',
      cancelButtonText: 'Cancel',
      inputValidator: (value) => {
        if (!value || value.trim().length === 0) {
          return 'Refund reason cannot be empty'
        }
      }
    })
    
    await applyRefund(order.id, reason)
    ElMessage.success('Refund application submitted')
    await loadOrders()
  } catch (error) {
    if (error !== 'cancel') {
      ElMessage.error(error.message || 'Failed to submit refund application')
    }
  }
}

const handleReview = (order) => {
  // Go to review page
  router.push(`/profile/orders/${order.id}/review`)
}

const handleViewDetail = (order) => {
  // Go to order details page
  router.push(`/profile/orders/${order.id}`)
}
</script>

<style scoped>
.my-orders-page {
  padding: var(--spacing-lg);
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.card-header h2 {
  margin: 0;
  font-size: 20px;
  font-weight: 600;
}

.filter-tabs {
  margin-bottom: var(--spacing-lg);
}

.orders-list {
  min-height: 400px;
}

.order-item {
  border: 1px solid var(--color-border);
  border-radius: var(--radius-md);
  margin-bottom: var(--spacing-lg);
  overflow: hidden;
}

.order-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: var(--spacing-md);
  background: var(--color-bg-base);
  border-bottom: 1px solid var(--color-border);
  font-size: 14px;
}

.order-header__left,
.order-header__right {
  display: flex;
  align-items: center;
  gap: var(--spacing-md);
}

.order-header__left .merchant {
  color: var(--color-text-secondary);
}

.order-body {
  display: flex;
  padding: var(--spacing-lg);
  gap: var(--spacing-lg);
}

.order-products {
  flex: 1;
  display: flex;
  flex-direction: column;
  gap: var(--spacing-md);
}

.product-item {
  display: flex;
  gap: var(--spacing-md);
}

.product-info {
  flex: 1;
}

.product-name {
  font-weight: 500;
  margin-bottom: var(--spacing-xs);
}

.product-spec {
  display: flex;
  justify-content: space-between;
  color: var(--color-text-secondary);
  font-size: 14px;
}

.order-amount {
  width: 150px;
  text-align: center;
  display: flex;
  flex-direction: column;
  justify-content: center;
}

.amount-label {
  color: var(--color-text-secondary);
  font-size: 12px;
  margin-bottom: var(--spacing-xs);
}

.amount-value {
  font-size: 20px;
  font-weight: 600;
  color: var(--color-danger);
}

.order-actions {
  width: 150px;
  display: flex;
  flex-direction: column;
  justify-content: center;
  gap: var(--spacing-sm);
}

.pagination-wrapper {
  display: flex;
  justify-content: center;
  margin-top: var(--spacing-xl);
}

/* Payment dialog */
.pay-dialog-content {
  padding: var(--spacing-lg);
}

.pay-amount {
  display: flex;
  justify-content: center;
  align-items: center;
  gap: var(--spacing-sm);
  margin-bottom: var(--spacing-xl);
  font-size: 16px;
}

.pay-amount .amount {
  font-size: 32px;
  font-weight: 600;
  color: var(--color-danger);
}

.payment-methods {
  display: flex;
  flex-direction: column;
  gap: var(--spacing-md);
}
</style>
