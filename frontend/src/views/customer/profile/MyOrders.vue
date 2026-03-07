<template>
  <div class="my-orders-page">
    <div class="page-header">
      <h2 class="page-title">My Orders</h2>
      <div class="header-actions">
        <el-select
          v-model="filterStatus"
          placeholder="All Status"
          clearable
          style="width: 200px"
          @change="handleFilterChange"
        >
          <el-option label="All Status" value="" />
          <el-option label="Pending Payment" value="SUBMITTED" />
          <el-option label="Pending Shipment" value="PAID" />
          <el-option label="Pending Receipt" value="SHIPPED" />
          <el-option label="Completed" value="COMPLETED" />
          <el-option label="Refunding" value="REFUND_APPLIED" />
          <el-option label="Refunded" value="REFUNDED" />
          <el-option label="Cancelled" value="CANCELLED" />
        </el-select>
      </div>
    </div>

    <!-- Orders Table -->
    <el-card shadow="never" class="table-card">
      <el-table :data="orders" v-loading="loading" style="width: 100%" row-key="id">
        <!-- Order info -->
        <el-table-column label="Order Details" min-width="300">
          <template #default="{ row }">
            <div class="order-info-cell">
              <div class="order-meta">
                <span class="order-no">#{{ row.orderNo }}</span>
                <span class="order-date">{{ formatDate(row.orderDate) || row.orderDate }}</span>
              </div>
              <div class="merchant-info">
                <el-icon><Shop /></el-icon>
                <span>{{ row.merchantName || 'Flower Market' }}</span>
              </div>
              <div class="product-mini-list">
                <div v-for="item in row.items" :key="item.id" class="mini-product-item">
                  <el-image 
                    :src="item.mainImage" 
                    fit="cover"
                    class="mini-thumb"
                  >
                    <template #error>
                      <div class="image-error-slot"><el-icon><Picture /></el-icon></div>
                    </template>
                  </el-image>
                  <div class="mini-info">
                    <div class="mini-name">{{ item.name }}</div>
                    <div class="mini-price">¥{{ item.unitPrice }} x {{ item.quantity }}</div>
                  </div>
                </div>
              </div>
            </div>
          </template>
        </el-table-column>

        <!-- Amount -->
        <el-table-column label="Total Amount" width="150" align="center">
          <template #default="{ row }">
            <div class="total-amount">
              <span class="currency">¥</span>
              <span class="amount-value">{{ Math.max(0, row.actualPrice || 0) }}</span>
            </div>
          </template>
        </el-table-column>

        <!-- Status -->
        <el-table-column label="Status" width="150" align="center">
          <template #default="{ row }">
            <el-tag :type="getStatusType(row.status)" effect="light" round>
              {{ getStatusText(row.status) }}
            </el-tag>
          </template>
        </el-table-column>

        <!-- Actions -->
        <el-table-column label="Actions" width="260" fixed="right" align="left">
          <template #default="{ row }">
            <div class="action-buttons">
              <el-button 
                v-if="row.status === 'SUBMITTED'" 
                type="primary" 
                link
                @click="handlePay(row)"
              >
                Pay Now
              </el-button>
              
              <el-button 
                v-if="row.status === 'SUBMITTED'" 
                type="danger"
                link
                @click="handleCancel(row)"
              >
                Cancel
              </el-button>
              
              <el-button 
                v-if="row.status === 'SHIPPED'" 
                type="success" 
                link
                @click="handleConfirmReceipt(row)"
              >
                Confirm
              </el-button>

              <el-button 
                v-if="row.status === 'PAID' || row.status === 'SHIPPED'" 
                type="warning" 
                link
                @click="handleRefund(row)"
              >
                Refund
              </el-button>
              
              <el-button 
                v-if="row.status === 'COMPLETED' && !row.hasReview" 
                type="primary" 
                link
                @click="handleReview(row)"
              >
                Review
              </el-button>
              
              <el-button 
                type="info"
                link
                @click="handleViewDetail(row)"
              >
                Details
              </el-button>
            </div>
          </template>
        </el-table-column>
      </el-table>

      <!-- Pagination -->
      <div class="pagination-footer">
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
    <el-dialog v-model="showPayDialog" title="Order Payment" width="420px" destroy-on-close class="payment-dialog">
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
import { formatDate } from '@/utils/format'
import { Shop, Picture, Monitor, DocumentCopy, StarFilled, Ticket, SwitchButton, User } from '@element-plus/icons-vue'

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
  padding: 0;
}

.page-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 24px;
}

.page-title {
  margin: 0;
  font-size: 24px;
  font-weight: 600;
  color: #1a1a1a;
}

.table-card {
  border: none;
  border-radius: 12px;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.05);
}

.order-info-cell {
  display: flex;
  flex-direction: column;
  gap: 8px;
  padding: 4px 0;
}

.order-meta {
  display: flex;
  gap: 12px;
  font-size: 13px;
  color: #94a3b8;
}

.order-no {
  color: #64748b;
  font-weight: 500;
}

.merchant-info {
  display: flex;
  align-items: center;
  gap: 6px;
  font-size: 14px;
  font-weight: 500;
  color: #334155;
}

.product-mini-list {
  display: flex;
  flex-direction: column;
  gap: 8px;
  margin-top: 4px;
}

.mini-product-item {
  display: flex;
  align-items: center;
  gap: 10px;
}

.mini-thumb {
  width: 48px;
  height: 48px;
  border-radius: 6px;
  background-color: #f1f5f9;
}

.mini-info {
  flex: 1;
  min-width: 0;
}

.mini-name {
  font-size: 13px;
  color: #475569;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}

.mini-price {
  font-size: 12px;
  color: #94a3b8;
}

.total-amount {
  display: flex;
  align-items: baseline;
  justify-content: center;
  color: #ef4444;
}

.currency {
  font-size: 12px;
  font-weight: 600;
  margin-right: 2px;
}

.amount-value {
  font-size: 18px;
  font-weight: 700;
}

.action-buttons {
  display: flex;
  flex-wrap: wrap;
  justify-content: flex-start;
  gap: 8px;
}

.pagination-footer {
  margin-top: 24px;
  display: flex;
  justify-content: flex-end;
}

.image-error-slot {
  display: flex;
  align-items: center;
  justify-content: center;
  width: 100%;
  height: 100%;
  color: #cbd5e1;
  font-size: 20px;
}

.pay-dialog-content {
  padding: 10px 0;
}

.pay-amount {
  text-align: center;
  margin-bottom: 24px;
  font-size: 16px;
  color: #64748b;
}

.pay-amount .amount {
  display: block;
  font-size: 32px;
  font-weight: 700;
  color: #ef4444;
  margin-top: 8px;
}

.payment-methods {
  display: flex;
  flex-direction: column;
  gap: 16px;
  padding: 0 20px;
}

:deep(.el-radio) {
  width: 100%;
  padding: 12px 16px;
  border: 1px solid #e2e8f0;
  border-radius: 8px;
  margin-right: 0;
  transition: all 0.2s;
}

:deep(.el-radio.is-checked) {
  border-color: #409eff;
  background-color: #f0f7ff;
}

:deep(.el-table__header) {
  th {
    background-color: #f8fafc !important;
    color: #475569;
    font-weight: 600;
  }
}
</style>
