<template>
  <div class="order-list">
    <!-- Status tabs -->
    <el-tabs v-model="activeTab" @tab-change="handleTabChange">
      <el-tab-pane label="All" name="ALL" />
      <el-tab-pane label="Pending Payment" name="SUBMITTED" />
      <el-tab-pane label="To Ship" name="PAID" />
      <el-tab-pane label="To Receive" name="SHIPPED" />
      <el-tab-pane label="Completed" name="COMPLETED" />
      <el-tab-pane label="Cancelled" name="CANCELLED" />
      <el-tab-pane label="Refunding" name="REFUND_APPLIED" />
      <el-tab-pane label="Refunded" name="REFUNDED" />
    </el-tabs>

    <!-- Search filters -->
    <el-card class="search-card">
      <el-form :inline="true" :model="searchForm">
        <el-form-item label="Order No">
          <el-input v-model="searchForm.orderNo" placeholder="Enter order number" clearable />
        </el-form-item>
        <el-form-item label="Customer">
          <el-input v-model="searchForm.customerName" placeholder="Enter customer name" clearable />
        </el-form-item>
        <el-form-item label="Date Range">
          <el-date-picker
            v-model="searchForm.dateRange"
            type="daterange"
            range-separator="to"
            start-placeholder="Start date"
            end-placeholder="End date"
          />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleSearch">Search</el-button>
          <el-button @click="handleReset">Reset</el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <!-- Orders table -->
    <el-card>
      <el-table :data="tableData" v-loading="loading" style="width: 100%">
        <el-table-column prop="orderNo" label="Order No" width="180" />
        <el-table-column prop="receiverName" label="Receiver" width="120" />
        <el-table-column prop="receiverPhone" label="Phone" width="130" />
        <el-table-column prop="address" label="Address" min-width="200" show-overflow-tooltip />
        <el-table-column label="Paid" width="100">
          <template #default="{ row }">
            ¥{{ row.actualPrice || row.totalPrice || 0 }}
          </template>
        </el-table-column>
        <el-table-column label="Status" width="100">
          <template #default="{ row }">
            <el-tag :type="getStatusType(row.status)">{{ row.statusText }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="Order Time" width="160">
          <template #default="{ row }">
            {{ formatDateTime(row.orderDate) }}
          </template>
        </el-table-column>
        <el-table-column label="Actions" width="260" fixed="right">
          <template #default="{ row }">
            <el-button type="primary" link @click="handleViewDetail(row)">View</el-button>
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
          </template>
        </el-table-column>
      </el-table>

      <!-- Pagination -->
      <div class="pagination">
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
    <el-dialog v-model="shipDialogVisible" title="Ship Order" width="500px">
      <el-form ref="shipFormRef" :model="shipForm" :rules="shipRules" label-width="100px">
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
  REFUNDED: 'Refunded',
  REFUND_REJECTED: 'Refund Rejected'
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

    await auditRefund(row.id, approved, auditRemark || (approved ? 'Approved' : ''))
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
.order-list {
  padding: 20px;
}

.search-card {
  margin-bottom: 20px;
}

.product-info {
  line-height: 1.5;
}

.pagination {
  margin-top: 20px;
  display: flex;
  justify-content: flex-end;
}
</style>
