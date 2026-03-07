<template>
  <div class="order-list">
    <div class="page-header">
      <h2 class="page-title">Order Management</h2>
    </div>

    <!-- Search -->
    <el-card class="search-card">
      <el-form :inline="true" :model="searchForm">
        <el-form-item label="Keyword">
          <el-input v-model="searchForm.keyword" placeholder="Order No / Customer" clearable style="width: 200px" />
        </el-form-item>
        <el-form-item label="Status">
          <el-select v-model="activeTab" placeholder="All Status" clearable style="width: 180px" @change="handleTabChange">
            <el-option label="All Statuses" name="ALL" value="ALL" />
            <el-option label="Pending Payment" name="SUBMITTED" value="SUBMITTED" />
            <el-option label="Pending Shipment" name="PAID" value="PAID" />
            <el-option label="Pending Receipt" name="SHIPPED" value="SHIPPED" />
            <el-option label="Completed" name="COMPLETED" value="COMPLETED" />
            <el-option label="Refund Applied" name="REFUND_APPLIED" value="REFUND_APPLIED" />
            <el-option label="Refunded" name="REFUNDED" value="REFUNDED" />
            <el-option label="Cancelled" name="CANCELLED" value="CANCELLED" />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleSearch">Search</el-button>
          <el-button @click="handleReset">Reset</el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <!-- Orders table -->
    <el-card class="table-card">
      <el-table :data="orderList" v-loading="loading" style="width: 100%">
        <el-table-column prop="orderNo" label="Order No" width="180" />
        <el-table-column prop="customerName" label="Customer" width="140" />
        <el-table-column prop="merchantName" label="Merchant" width="160" />
        <el-table-column prop="actualPrice" label="Amount" width="120">
          <template #default="{ row }">
            ¥{{ row.actualPrice }}
          </template>
        </el-table-column>
        <el-table-column prop="status" label="Status" width="140">
          <template #default="{ row }">
            <el-tag :type="getStatusType(row.status)">{{ getStatusText(row.status) }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="orderDate" label="Ordered At" width="160">
          <template #default="{ row }">
            {{ formatDate(row.orderDate) }}
          </template>
        </el-table-column>
        <el-table-column label="Actions" min-width="260" fixed="right">
          <template #default="{ row }">
            <div class="operation-buttons">
              <el-button type="primary" plain size="small" icon="View" @click="handleViewDetail(row.id)">Details</el-button>
              <el-button 
                v-if="row.status === 'REFUND_APPLIED'" 
                type="success" 
                plain
                size="small" 
                icon="Check"
                @click="handleAudit(row, true)"
              >
                Approve
              </el-button>
              <el-button 
                v-if="row.status === 'REFUND_APPLIED'" 
                type="danger" 
                plain
                size="small" 
                icon="Close"
                @click="handleAudit(row, false)"
              >
                Reject
              </el-button>
            </div>
          </template>
        </el-table-column>
      </el-table>

      <!-- Pagination -->
      <div class="pagination-container">
        <el-pagination
          v-model:current-page="pagination.page"
          v-model:page-size="pagination.pageSize"
          :total="pagination.total"
          :page-sizes="[10, 20, 50, 100]"
          layout="total, sizes, prev, pager, next, jumper"
          @current-change="fetchOrderList"
          @size-change="fetchOrderList"
        />
      </div>
    </el-card>

    <!-- Order details dialog -->
    <el-dialog v-model="detailDialogVisible" title="Order Details" width="800px">
      <div v-loading="detailLoading">
        <el-descriptions :column="2" border>
          <el-descriptions-item label="Order No" :span="2">{{ orderDetail.orderNo }}</el-descriptions-item>
          <el-descriptions-item label="Customer">{{ orderDetail.customerName }}</el-descriptions-item>
          <el-descriptions-item label="Merchant">{{ orderDetail.merchantName }}</el-descriptions-item>
          <el-descriptions-item label="Order Status">
            <el-tag :type="getStatusType(orderDetail.status)">{{ getStatusText(orderDetail.status) }}</el-tag>
          </el-descriptions-item>
          <el-descriptions-item label="Order Time">{{ formatDate(orderDetail.orderDate) }}</el-descriptions-item>
          <el-descriptions-item label="Shipping Address" :span="2">{{ orderDetail.address }}</el-descriptions-item>
          <el-descriptions-item label="Total">¥{{ orderDetail.totalPrice }}</el-descriptions-item>
          <el-descriptions-item label="Discount">-¥{{ orderDetail.discountAmount || 0 }}</el-descriptions-item>
          <el-descriptions-item label="Payable" :span="2">
            <span style="color: #f56c6c; font-weight: bold; font-size: 18px;">¥{{ orderDetail.actualPrice }}</span>
          </el-descriptions-item>
          <el-descriptions-item label="Note" :span="2">{{ orderDetail.remark || '-' }}</el-descriptions-item>
        </el-descriptions>

        <el-divider />
        <h4>Order Items</h4>
        <el-table :data="orderDetail.items" style="width: 100%">
          <el-table-column prop="name" label="Product" />
          <el-table-column prop="quantity" label="Qty" width="80" />
          <el-table-column prop="unitPrice" label="Unit Price" width="110">
            <template #default="{ row }">
              ¥{{ row.unitPrice }}
            </template>
          </el-table-column>
          <el-table-column prop="totalPrice" label="Subtotal" width="110">
            <template #default="{ row }">
              ¥{{ row.totalPrice }}
            </template>
          </el-table-column>
        </el-table>
      </div>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import request from '@/utils/request'
import { formatDate } from '@/utils/format'
import { auditRefund } from '@/api/order'
import { View, Check, Close } from '@element-plus/icons-vue'

const loading = ref(false)
const detailLoading = ref(false)
const orderList = ref([])
const activeTab = ref('ALL')
const detailDialogVisible = ref(false)
const orderDetail = ref({
  items: []
})

const searchForm = reactive({
  keyword: ''
})

const pagination = reactive({
  page: 1,
  pageSize: 10,
  total: 0
})

// Fetch orders
const fetchOrderList = async () => {
  loading.value = true
  try {
    const params = {
      current: pagination.page,
      size: pagination.pageSize,
      status: activeTab.value === 'ALL' ? undefined : activeTab.value,
      keyword: searchForm.keyword || undefined
    }
    const { data } = await request({
      url: '/api/admin/orders',
      method: 'get',
      params
    })
    // Backend returns IPage structure: records, total, size, current
    orderList.value = data.records || []
    pagination.total = data.total || 0
  } catch (error) {
    ElMessage.error('Failed to load orders')
  } finally {
    loading.value = false
  }
}

// Handle refund audit logic
const handleAudit = async (row, approved) => {
  const action = approved ? 'Approve' : 'Reject'
  const remarkPrompt = approved ? 'Optional audit remark:' : 'Please enter rejection reason (required):'

  try {
    const { value: auditRemark } = await ElMessageBox.prompt(remarkPrompt, `${action} Refund`, {
      confirmButtonText: 'Confirm',
      cancelButtonText: 'Cancel',
      inputPlaceholder: approved ? 'Default: Approved' : 'Please enter why you rejected this refund',
      inputValidator: (val) => {
        if (!approved && (!val || val.trim() === '')) {
          return 'Reason is required for rejection'
        }
        return true
      }
    })

    await auditRefund(row.id, {
      approved,
      remark: auditRemark || (approved ? 'Approved' : '')
    })
    ElMessage.success(`Refund ${action}d successfully`)
    fetchOrderList()
  } catch (error) {
    if (error !== 'cancel') {
      console.error(error)
      ElMessage.error(`Failed to ${action.toLowerCase()} refund`)
    }
  }
}

// Tab changed
const handleTabChange = () => {
  pagination.page = 1
  fetchOrderList()
}

// Search
const handleSearch = () => {
  pagination.page = 1
  fetchOrderList()
}

// Reset
const handleReset = () => {
  searchForm.keyword = ''
  pagination.page = 1
  fetchOrderList()
}

// View details
const handleViewDetail = async (id) => {
  detailDialogVisible.value = true
  detailLoading.value = true
  try {
    const { data } = await request({
      url: `/api/orders/${id}`,
      method: 'get'
    })
    orderDetail.value = data
  } catch (error) {
    ElMessage.error('Failed to load order details')
  } finally {
    detailLoading.value = false
  }
}

// Status tag type
const getStatusType = (status) => {
  const map = {
    SUBMITTED: 'warning',
    PAID: 'primary',
    SHIPPED: 'info',
    COMPLETED: 'success',
    CANCELLED: 'danger',
    REFUND_APPLIED: 'warning',
    REFUNDED: 'info',
    REFUND_REJECTED: 'danger'
  }
  return map[status] || 'info'
}

// Status text
const getStatusText = (status) => {
  const map = {
    SUBMITTED: 'Pending Payment',
    PAID: 'Pending Shipment',
    SHIPPED: 'Pending Receipt',
    COMPLETED: 'Completed',
    CANCELLED: 'Cancelled',
    REFUND_APPLIED: 'Refund Applied',
    REFUNDED: 'Refunded'
  }
  return map[status] || 'Unknown'
}

onMounted(() => {
  fetchOrderList()
})
</script>

<style scoped>
.order-list {
  padding: 20px;
}

.page-title {
  margin: 0 0 20px;
  font-size: 24px;
  font-weight: 500;
  color: #303133;
}

.tabs {
  margin-bottom: 20px;
}

.search-card {
  margin-bottom: 20px;
}

.table-card {
  margin-bottom: 20px;
}

.pagination-container {
  display: flex;
  justify-content: center;
  margin-top: 20px;
}
</style>
