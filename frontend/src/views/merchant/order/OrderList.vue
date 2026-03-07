<template>
  <div class="order-list-container">
    <div class="page-header">
      <h2 class="page-title">Order Management</h2>
      <div class="header-actions">
        <el-select
          v-model="activeTab"
          placeholder="All Status"
          clearable
          style="width: 200px"
          @change="handleTabChange"
        >
          <el-option label="All Status" value="ALL" />
          <el-option label="Pending Payment" value="SUBMITTED" />
          <el-option label="To Ship" value="PAID" />
          <el-option label="To Receive" value="SHIPPED" />
          <el-option label="Completed" value="COMPLETED" />
          <el-option label="Cancelled" value="CANCELLED" />
          <el-option label="Refunding" value="REFUND_APPLIED" />
          <el-option label="Refunded" value="REFUNDED" />
        </el-select>
      </div>
    </div>

    <!-- Search filters -->
    <el-card shadow="never" class="search-card">
      <el-form :inline="true" :model="searchForm" class="filter-form">
        <el-form-item label="Order No">
          <el-input v-model="searchForm.orderNo" placeholder="Order #" clearable @keyup.enter="handleSearch" style="width: 200px" />
        </el-form-item>
        <el-form-item label="Customer">
          <el-input v-model="searchForm.customerName" placeholder="Customer name" clearable @keyup.enter="handleSearch" style="width: 180px" />
        </el-form-item>
        <el-form-item label="Date Range">
          <el-date-picker
            v-model="searchForm.dateRange"
            type="daterange"
            range-separator="-"
            start-placeholder="Start"
            end-placeholder="End"
            style="width: 240px"
          />
        </el-form-item>
        <el-form-item class="form-actions">
          <el-button type="primary" @click="handleSearch">Search</el-button>
          <el-button @click="handleReset">Reset</el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <!-- Orders table -->
    <el-card shadow="never" class="table-card">
      <el-table :data="tableData" v-loading="loading" style="width: 100%">
        <el-table-column label="Order No" min-width="180">
          <template #default="{ row }">
            <span class="order-no">#{{ row.orderNo }}</span>
          </template>
        </el-table-column>
        <el-table-column label="Order Date" width="180">
          <template #default="{ row }">
            <span class="order-time">{{ formatDateTime(row.orderDate) }}</span>
          </template>
        </el-table-column>
        <el-table-column label="Customer" min-width="150">
          <template #default="{ row }">
            <div class="customer-info-cell">
              <div class="customer-name">{{ row.receiverName }}</div>
              <div class="customer-phone">{{ row.receiverPhone }}</div>
            </div>
          </template>
        </el-table-column>
        <el-table-column prop="address" label="Shipping Address" min-width="200" show-overflow-tooltip />
        <el-table-column label="Amount" width="120" align="right">
          <template #default="{ row }">
            <span class="price-value">¥{{ row.actualPrice || row.totalPrice || 0 }}</span>
          </template>
        </el-table-column>
        <el-table-column label="Status" width="130" align="center">
          <template #default="{ row }">
            <el-tag :type="getStatusType(row.status)" effect="light" round>{{ row.statusText }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="Actions" width="200" fixed="right" align="left">
          <template #default="{ row }">
            <div class="action-buttons">
              <el-button type="primary" link @click="handleViewDetail(row)">Details</el-button>
              <el-button
                v-if="row.status === 'PAID'"
                type="success"
                link
                @click="handleShip(row)"
              >
                Ship
              </el-button>
              <template v-if="row.status === 'REFUND_APPLIED'">
                <el-button type="success" link @click="handleAudit(row, true)">Approve</el-button>
                <el-button type="danger" link @click="handleAudit(row, false)">Reject</el-button>
              </template>
            </div>
          </template>
        </el-table-column>
      </el-table>

      <!-- Pagination -->
      <div class="pagination-footer">
        <el-pagination
          v-model:current-page="pagination.page"
          v-model:page-size="pagination.size"
          :total="pagination.total"
          :page-sizes="[10, 20, 50, 100]"
          layout="total, sizes, prev, pager, next, jumper"
          @size-change="fetchOrders"
          @current-change="fetchOrders"
        />
      </div>
    </el-card>

    <!-- Ship dialog -->
    <el-dialog v-model="shipDialogVisible" title="Ship Order" width="460px" destroy-on-close>
      <el-form ref="shipFormRef" :model="shipForm" :rules="shipRules" label-position="top">
        <el-form-item label="Courier" prop="courier">
          <el-input v-model="shipForm.courier" placeholder="Enter courier company" />
        </el-form-item>
        <el-form-item label="Tracking No" prop="trackingNo">
          <el-input v-model="shipForm.trackingNo" placeholder="Enter tracking number" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="shipDialogVisible = false">Cancel</el-button>
        <el-button type="primary" @click="handleConfirmShip" :loading="shipping">Confirm Ship</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { getMerchantOrders, shipOrder } from '@/api/merchant'
import { auditRefund } from '@/api/order'
import { ElMessage, ElMessageBox } from 'element-plus'
import { useUserStore } from '@/stores/user'

const router = useRouter()
const userStore = useUserStore()
const loading = ref(false)
const shipping = ref(false)
const activeTab = ref('ALL')
const shipDialogVisible = ref(false)
const shipFormRef = ref(null)
const currentOrder = ref(null)

const searchForm = reactive({
  orderNo: '',
  customerName: '',
  dateRange: []
})

const pagination = reactive({
  page: 1,
  size: 10,
  total: 0
})

const shipForm = reactive({
  courier: '',
  trackingNo: ''
})

const shipRules = {
  courier: [{ required: true, message: 'Please enter courier company', trigger: 'blur' }],
  trackingNo: [{ required: true, message: 'Please enter tracking number', trigger: 'blur' }]
}

const tableData = ref([])

const statusTextMap = {
  SUBMITTED: 'Pending Payment',
  PAID: 'Paid',
  SHIPPED: 'To Receive',
  COMPLETED: 'Completed',
  CANCELLED: 'Cancelled',
  REFUND_APPLIED: 'Refund Applied',
  REFUNDED: 'Refunded'
}

const getStatusType = (status) => {
  const types = {
    SUBMITTED: 'warning',
    PAID: 'info',
    SHIPPED: 'primary',
    COMPLETED: 'success',
    CANCELLED: 'danger',
    REFUND_APPLIED: 'warning',
    REFUNDED: 'info',
    REFUND_REJECTED: 'danger'
  }
  return types[status] || 'info'
}

const formatDateTime = (dateTime) => {
  if (!dateTime) return '-'
  const date = new Date(dateTime)
  const year = date.getFullYear()
  const month = String(date.getMonth() + 1).padStart(2, '0')
  const day = String(date.getDate()).padStart(2, '0')
  const hours = String(date.getHours()).padStart(2, '0')
  const minutes = String(date.getMinutes()).padStart(2, '0')
  const seconds = String(date.getSeconds()).padStart(2, '0')
  return `${year}-${month}-${day} ${hours}:${minutes}:${seconds}`
}

const fetchOrders = async () => {
  loading.value = true
  try {
    const params = {
      merchId: userStore.userId,
      current: pagination.page,
      size: pagination.size,
      status: activeTab.value === 'ALL' ? undefined : activeTab.value,
      orderNo: searchForm.orderNo || undefined,
      customerName: searchForm.customerName || undefined
    }
    
    if (searchForm.dateRange && searchForm.dateRange.length === 2) {
      params.startDate = searchForm.dateRange[0]
      params.endDate = searchForm.dateRange[1]
    }
    
    const res = await getMerchantOrders(params)
    // Backend returns IPage structure: records, total
    tableData.value = (res.data.records || []).map(item => ({
      ...item,
      statusText: statusTextMap[item.status] || item.status
    }))
    pagination.total = res.data.total || 0
  } catch (error) {
    ElMessage.error('Failed to load data')
  } finally {
    loading.value = false
  }
}

const handleTabChange = () => {
  pagination.page = 1
  fetchOrders()
}

const handleSearch = () => {
  pagination.page = 1
  fetchOrders()
}

const handleReset = () => {
  searchForm.orderNo = ''
  searchForm.customerName = ''
  searchForm.dateRange = []
  handleSearch()
}

const handleViewDetail = (row) => {
  router.push(`/merchant/orders/${row.id}`)
}

const handleShip = (row) => {
  currentOrder.value = row
  shipForm.courier = ''
  shipForm.trackingNo = ''
  shipDialogVisible.value = true
}

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
    fetchOrders()
  } catch (error) {
    if (error !== 'cancel') {
      console.error(error)
      ElMessage.error(`Failed to ${action.toLowerCase()} refund`)
    }
  }
}

const handleConfirmShip = async () => {
  const valid = await shipFormRef.value.validate().catch(() => false)
  if (!valid) return

  shipping.value = true
  try {
    await shipOrder(currentOrder.value.id, {
      courier: shipForm.courier,
      trackingNo: shipForm.trackingNo
    })
    ElMessage.success('Shipped successfully')
    shipDialogVisible.value = false
    fetchOrders()
  } catch (error) {
    ElMessage.error(error.message || 'Shipping failed')
  } finally {
    shipping.value = false
  }
}

onMounted(() => {
  fetchOrders()
})
</script>

<style scoped>
.order-list-container {
  padding: 0;
}

.page-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 24px;
}

.page-title {
  font-size: 24px;
  font-weight: 600;
  color: #1a1a1a;
  margin: 0;
}

.search-card {
  border: none;
  border-radius: 12px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.04);
  margin-bottom: 20px;
}

.table-card {
  border: none;
  border-radius: 12px;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.05);
}

.order-id-cell {
  display: flex;
  flex-direction: column;
  gap: 4px;
}

.order-no {
  font-weight: 600;
  color: #334155;
  font-family: monospace;
}

.order-time {
  font-size: 12px;
  color: #94a3b8;
}

.customer-info-cell {
  display: flex;
  flex-direction: column;
  gap: 2px;
}

.customer-name {
  font-weight: 500;
  color: #1e293b;
}

.customer-phone {
  font-size: 13px;
  color: #64748b;
}

.price-value {
  font-weight: 700;
  color: #334155;
  font-size: 15px;
}

.action-buttons {
  display: flex;
  gap: 8px;
  justify-content: flex-start;
}

.pagination-footer {
  margin-top: 24px;
  display: flex;
  justify-content: flex-end;
}

:deep(.el-table__header) {
  th {
    background-color: #f8fafc !important;
    color: #475569;
    font-weight: 600;
  }
}

:deep(.el-form-item__label) {
  font-weight: 500;
}
</style>
