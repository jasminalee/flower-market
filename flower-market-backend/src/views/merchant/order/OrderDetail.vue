<template>
  <div class="order-detail">
    <el-card v-loading="loading">
      <template #header>
        <div class="card-header">
          <span>Order Details</span>
          <el-button @click="$router.back()">Back</el-button>
        </div>
      </template>

      <!-- Order basic info -->
      <div class="section">
        <h3 class="section-title">Order Info</h3>
        <el-descriptions :column="2" border>
          <el-descriptions-item label="Order No">{{ orderData.orderNo }}</el-descriptions-item>
          <el-descriptions-item label="Status">
            <el-tag :type="getStatusType(orderData.status)">{{ orderData.statusText }}</el-tag>
          </el-descriptions-item>
          <el-descriptions-item label="Order Time">{{ orderData.createTime }}</el-descriptions-item>
          <el-descriptions-item label="Paid Time">{{ orderData.payTime || '-' }}</el-descriptions-item>
          <el-descriptions-item label="Customer">{{ orderData.customerName }}</el-descriptions-item>
          <el-descriptions-item label="Phone">{{ orderData.customerPhone }}</el-descriptions-item>
        </el-descriptions>
      </div>

      <!-- Shipping address -->
      <div class="section">
        <h3 class="section-title">Receiver Info</h3>
        <el-descriptions :column="1" border>
          <el-descriptions-item label="Receiver">{{ orderData.receiverName }}</el-descriptions-item>
          <el-descriptions-item label="Phone">{{ orderData.receiverPhone }}</el-descriptions-item>
          <el-descriptions-item label="Address">{{ orderData.receiverAddress }}</el-descriptions-item>
        </el-descriptions>
      </div>

      <!-- Items list -->
      <div class="section">
        <h3 class="section-title">Items</h3>
        <el-table :data="orderData.items" border style="width: 100%">
          <el-table-column label="Product Image" width="100">
            <template #default="{ row }">
              <el-image
                :src="row.productImage"
                fit="cover"
                style="width: 60px; height: 60px; border-radius: 4px"
              />
            </template>
          </el-table-column>
          <el-table-column prop="productName" label="Product Name" min-width="200" />
          <el-table-column prop="price" label="Unit Price" width="120">
            <template #default="{ row }">
              ¥{{ row.price }}
            </template>
          </el-table-column>
          <el-table-column prop="quantity" label="Quantity" width="100" />
          <el-table-column label="Subtotal" width="120">
            <template #default="{ row }">
              ¥{{ (row.price * row.quantity).toFixed(2) }}
            </template>
          </el-table-column>
        </el-table>
      </div>

      <!-- Price details -->
      <div class="section">
        <h3 class="section-title">Price Details</h3>
        <el-descriptions :column="1" border>
          <el-descriptions-item label="Items Total">¥{{ orderData.itemsTotal }}</el-descriptions-item>
          <el-descriptions-item label="Discount">-¥{{ orderData.discountAmount || 0 }}</el-descriptions-item>
          <el-descriptions-item label="Paid Amount">
            <span style="color: #f56c6c; font-size: 18px; font-weight: bold">
              ¥{{ orderData.totalAmount }}
            </span>
          </el-descriptions-item>
        </el-descriptions>
      </div>

      <!-- Shipping info -->
      <div class="section" v-if="orderData.status === 'SHIPPED' || orderData.status === 'COMPLETED'">
        <h3 class="section-title">Shipping Info</h3>
        <el-descriptions :column="2" border>
          <el-descriptions-item label="Courier">{{ orderData.courier || '-' }}</el-descriptions-item>
          <el-descriptions-item label="Tracking No">{{ orderData.trackingNo || '-' }}</el-descriptions-item>
          <el-descriptions-item label="Ship Time" :span="2">{{ orderData.shipTime || '-' }}</el-descriptions-item>
        </el-descriptions>
      </div>

      <!-- Actions -->
      <div class="section" v-if="orderData.status === 'PAID'">
        <el-button type="primary" @click="handleShip">Ship Order</el-button>
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
import { useRoute, useRouter } from 'vue-router'
import { getMerchantOrder, shipOrder } from '@/api/merchant'
import { ElMessage } from 'element-plus'

const route = useRoute()
const router = useRouter()
const loading = ref(false)
const shipping = ref(false)
const shipDialogVisible = ref(false)
const shipFormRef = ref(null)

const orderData = ref({
  orderNo: '',
  status: '',
  statusText: '',
  createTime: '',
  payTime: '',
  customerName: '',
  customerPhone: '',
  receiverName: '',
  receiverPhone: '',
  receiverAddress: '',
  items: [],
  itemsTotal: 0,
  discountAmount: 0,
  totalAmount: 0,
  courier: '',
  trackingNo: '',
  shipTime: ''
})

const shipForm = reactive({
  courier: '',
  trackingNo: ''
})

const shipRules = {
  courier: [{ required: true, message: 'Please enter courier company', trigger: 'blur' }],
  trackingNo: [{ required: true, message: 'Please enter tracking number', trigger: 'blur' }]
}

const statusTextMap = {
  SUBMITTED: 'Pending Payment',
  PAID: 'To Ship',
  SHIPPED: 'To Receive',
  COMPLETED: 'Completed',
  CANCELLED: 'Cancelled'
}

const getStatusType = (status) => {
  const types = {
    SUBMITTED: 'warning',
    PAID: 'info',
    SHIPPED: 'primary',
    COMPLETED: 'success',
    CANCELLED: 'danger'
  }
  return types[status] || 'info'
}

const fetchOrderDetail = async () => {
  loading.value = true
  try {
    const res = await getMerchantOrder(route.params.id)
    const raw = res.data || {}
    const normalizedStatus = (raw.status || '').toString().trim().toUpperCase()
    orderData.value = {
      ...raw,
      status: normalizedStatus,
      statusText: statusTextMap[normalizedStatus] || normalizedStatus
    }
  } catch (error) {
    ElMessage.error('Failed to load data')
  } finally {
    loading.value = false
  }
}

const handleShip = () => {
  shipForm.courier = ''
  shipForm.trackingNo = ''
  shipDialogVisible.value = true
}

const handleConfirmShip = async () => {
  const valid = await shipFormRef.value.validate().catch(() => false)
  if (!valid) return

  shipping.value = true
  try {
    await shipOrder(route.params.id, {
      courier: shipForm.courier,
      trackingNo: shipForm.trackingNo
    })
    ElMessage.success('Shipped successfully')
    shipDialogVisible.value = false
    fetchOrderDetail()
  } catch (error) {
    ElMessage.error(error.message || 'Shipping failed')
  } finally {
    shipping.value = false
  }
}

onMounted(() => {
  fetchOrderDetail()
})
</script>

<style scoped>
.order-detail {
  padding: 20px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.section {
  margin-bottom: 30px;
}

.section:last-child {
  margin-bottom: 0;
}

.section-title {
  margin: 0 0 15px 0;
  font-size: 16px;
  font-weight: 500;
  color: #303133;
  border-left: 3px solid #409eff;
  padding-left: 10px;
}
</style>
