<template>
  <div class="coupon-list">
    <div class="header-actions">
      <el-button type="primary" @click="handleAdd">
        <el-icon><Plus /></el-icon>
        Add Coupon
      </el-button>
    </div>

    <!-- Coupon table -->
    <el-card>
      <el-table :data="tableData" v-loading="loading" style="width: 100%">
        <el-table-column prop="couponId" label="ID" width="80" />
        <el-table-column prop="name" label="Coupon Name" min-width="150" />
        <el-table-column label="Type" width="120">
          <template #default="{ row }">
            <span v-if="row.type === 'DISCOUNT'">Discount</span>
            <span v-else-if="row.type === 'FULL_REDUCTION'">Full Reduction</span>
            <span v-else-if="row.type === 'FIXED_AMOUNT'">Fixed Amount</span>
            <span v-else>{{ row.type }}</span>
          </template>
        </el-table-column>
        <el-table-column label="Value" width="110">
          <template #default="{ row }">
            <span v-if="row.type === 'DISCOUNT'">{{ row.value * 10 }}%</span>
            <span v-else>¥{{ row.value }}</span>
          </template>
        </el-table-column>
        <el-table-column label="Min Spend" width="110">
          <template #default="{ row }">
            ¥{{ row.minPrice || 0 }}
          </template>
        </el-table-column>
        <el-table-column label="Issued/Claimed" width="140">
          <template #default="{ row }">
            {{ row.receivedQuantity || 0 }}/{{ row.totalQuantity || 0 }}
          </template>
        </el-table-column>
        <el-table-column label="Expiration Date" width="160">
          <template #default="{ row }">
            {{ formatDateTime(row.endDate) }}
          </template>
        </el-table-column>
        <el-table-column label="Status" width="100">
          <template #default="{ row }">
            <el-tag :type="getStatusType(row.status)">
              {{ getStatusText(row.status) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="Created At" width="160">
          <template #default="{ row }">
            {{ formatDateTime(row.createDate) }}
          </template>
        </el-table-column>
        <el-table-column label="Actions" width="200" fixed="right">
          <template #default="{ row }">
            <el-button type="primary" link @click="handleEdit(row)">Edit</el-button>
            <el-button
              :type="row.status === 'ACTIVE' ? 'warning' : 'success'"
              link
              @click="handleToggleStatus(row)"
            >
              {{ row.status === 'ACTIVE' ? 'Disable' : 'Enable' }}
            </el-button>
            <el-button type="danger" link @click="handleDelete(row)">Delete</el-button>
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
          @size-change="fetchCoupons"
          @current-change="fetchCoupons"
        />
      </div>
    </el-card>

    <!-- Add/Edit coupon dialog -->
    <el-dialog
      v-model="dialogVisible"
      :title="isEdit ? 'Edit Coupon' : 'Add Coupon'"
      width="600px"
    >
      <el-form
        ref="formRef"
        :model="formData"
        :rules="rules"
        label-width="120px"
      >
        <el-form-item label="Coupon Name" prop="name">
          <el-input v-model="formData.name" placeholder="Enter coupon name" />
        </el-form-item>

        <el-form-item label="Coupon Type" prop="type">
          <el-radio-group v-model="formData.type">
            <el-radio label="FULL_REDUCTION">Full Reduction</el-radio>
            <el-radio label="DISCOUNT">Discount</el-radio>
          </el-radio-group>
        </el-form-item>

        <el-form-item 
          v-if="formData.type === 'FULL_REDUCTION'" 
          label="Discount Amount" 
          prop="value"
        >
          <el-input-number v-model="formData.value" :min="0" :precision="2" :step="1" />
          <span style="margin-left: 10px">CNY</span>
        </el-form-item>

        <el-form-item 
          v-if="formData.type === 'DISCOUNT'" 
          label="Discount" 
          prop="value"
        >
          <el-input-number v-model="formData.value" :min="0.1" :max="9.9" :precision="1" :step="0.1" />
          <span style="margin-left: 10px">(1-9.9, e.g. 8.5 for 15% off)</span>
        </el-form-item>

        <el-form-item label="Min Order Amount" prop="minPrice">
          <el-input-number v-model="formData.minPrice" :min="0" :precision="2" :step="1" />
          <span style="margin-left: 10px">CNY (minimum to use)</span>
        </el-form-item>

        <el-form-item label="Total Quantity" prop="totalQuantity">
          <el-input-number v-model="formData.totalQuantity" :min="1" :step="1" />
        </el-form-item>

        <el-form-item label="Validity" prop="dateRange">
          <el-date-picker
            v-model="formData.dateRange"
            type="datetimerange"
            range-separator="to"
            start-placeholder="Start"
            end-placeholder="End"
            style="width: 100%"
          />
        </el-form-item>

        <el-form-item label="Description" prop="description">
          <el-input
            v-model="formData.description"
            type="textarea"
            :rows="3"
            placeholder="Enter usage notes"
            maxlength="200"
            show-word-limit
          />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">Cancel</el-button>
        <el-button type="primary" @click="handleSubmit" :loading="saving">Save</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { 
  getMerchantCoupons, 
  getMerchantCoupon,
  createCoupon, 
  updateCoupon, 
  deleteCoupon 
} from '@/api/merchant'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Plus } from '@element-plus/icons-vue'
import { useUserStore } from '@/stores/user'

const userStore = useUserStore()

const formRef = ref(null)
const loading = ref(false)
const saving = ref(false)
const dialogVisible = ref(false)
const isEdit = ref(false)
const currentId = ref(null)

const pagination = reactive({
  page: 1,
  size: 10,
  total: 0
})

const tableData = ref([])

const formData = reactive({
  name: '',
  type: 'FULL_REDUCTION',
  value: 0,
  minPrice: 0,
  totalQuantity: 100,
  dateRange: [],
  description: ''
})

const rules = {
  name: [{ required: true, message: 'Please enter coupon name', trigger: 'blur' }],
  type: [{ required: true, message: 'Please select coupon type', trigger: 'change' }],
  value: [{ required: true, message: 'Please enter value', trigger: 'blur' }],
  minPrice: [{ required: true, message: 'Please enter minimum order amount', trigger: 'blur' }],
  totalQuantity: [{ required: true, message: 'Please enter total quantity', trigger: 'blur' }],
  dateRange: [{ required: true, message: 'Please select validity period', trigger: 'change' }]
}

const fetchCoupons = async () => {
  loading.value = true
  try {
    const res = await getMerchantCoupons({
      merchId: userStore.userId,
      current: pagination.page,
      size: pagination.size
    })
    // Backend returns IPage structure: records, total
    tableData.value = res.data.records || []
    pagination.total = res.data.total || 0
  } catch (error) {
    ElMessage.error('Failed to load data')
  } finally {
    loading.value = false
  }
}

const handleAdd = () => {
  isEdit.value = false
  currentId.value = null
  formData.name = ''
  formData.type = 'FULL_REDUCTION'
  formData.value = 0
  formData.minPrice = 0
  formData.totalQuantity = 100
  formData.dateRange = []
  formData.description = ''
  dialogVisible.value = true
}

const handleEdit = async (row) => {
  isEdit.value = true
  currentId.value = row.couponId
  
  try {
    const res = await getMerchantCoupon(row.couponId)
    const data = res.data
    formData.name = data.name
    formData.type = data.type
    formData.value = data.value || 0
    formData.minPrice = data.minPrice
    formData.totalQuantity = data.totalQuantity
    formData.dateRange = [data.startDate, data.endDate]
    formData.description = data.description || ''
    dialogVisible.value = true
  } catch (error) {
    ElMessage.error('Failed to load data')
  }
}

const handleSubmit = async () => {
  const valid = await formRef.value.validate().catch(() => false)
  if (!valid) return

  saving.value = true
  try {
    const data = {
      merchId: userStore.userId,
      name: formData.name,
      type: formData.type,
      value: formData.value,
      minPrice: formData.minPrice,
      totalQuantity: formData.totalQuantity,
      startDate: formData.dateRange[0],
      endDate: formData.dateRange[1],
      description: formData.description
    }

    if (isEdit.value) {
      await updateCoupon(currentId.value, data)
      ElMessage.success('Saved successfully')
    } else {
      await createCoupon(data)
      ElMessage.success('Created successfully')
    }
    
    dialogVisible.value = false
    fetchCoupons()
  } catch (error) {
    ElMessage.error(error.message || 'Operation failed')
  } finally {
    saving.value = false
  }
}

const handleToggleStatus = async (row) => {
  const newStatus = row.status === 'ACTIVE' ? 'INACTIVE' : 'ACTIVE'
  const action = newStatus === 'ACTIVE' ? 'Enable' : 'Disable'
  
  try {
    await ElMessageBox.confirm(`Are you sure you want to ${action.toLowerCase()} this coupon?`, 'Confirmation', {
      type: 'warning'
    })
    
    await updateCoupon(row.couponId, { ...row, status: newStatus })
    ElMessage.success(`${action}d successfully`)
    fetchCoupons()
  } catch (error) {
    if (error !== 'cancel') {
      ElMessage.error(error.message || `${action} failed`)
    }
  }
}

const handleDelete = async (row) => {
  try {
    await ElMessageBox.confirm('Are you sure you want to delete this coupon? This action cannot be undone.', 'Confirmation', {
      type: 'warning'
    })
    
    await deleteCoupon(row.couponId)
    ElMessage.success('Deleted successfully')
    fetchCoupons()
  } catch (error) {
    if (error !== 'cancel') {
      ElMessage.error(error.message || 'Delete failed')
    }
  }
}

const formatDateTime = (dateTime) => {
  if (!dateTime) return '-'
  const date = new Date(dateTime)
  if (isNaN(date.getTime())) return dateTime
  const year = date.getFullYear()
  const month = String(date.getMonth() + 1).padStart(2, '0')
  const day = String(date.getDate()).padStart(2, '0')
  const hours = String(date.getHours()).padStart(2, '0')
  const minutes = String(date.getMinutes()).padStart(2, '0')
  return `${year}-${month}-${day} ${hours}:${minutes}`
}

const getStatusType = (status) => {
  const types = {
    ACTIVE: 'success',
    INACTIVE: 'info',
    EXPIRED: 'warning'
  }
  return types[status] || 'info'
}

const getStatusText = (status) => {
  const texts = {
    ACTIVE: 'Active',
    INACTIVE: 'Inactive',
    EXPIRED: 'Expired'
  }
  return texts[status] || status
}

onMounted(() => {
  fetchCoupons()
})
</script>

<style scoped>
.coupon-list {
  padding: 20px;
}

.header-actions {
  margin-bottom: 20px;
}

.pagination {
  margin-top: 20px;
  display: flex;
  justify-content: flex-end;
}
</style>

