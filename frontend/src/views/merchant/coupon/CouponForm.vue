<template>
  <div class="coupon-form">
    <el-card>
      <template #header>
        <div class="card-header">
          <span>{{ isEdit ? 'Edit Coupon' : 'Create Coupon' }}</span>
          <el-button @click="goBack">Back</el-button>
        </div>
      </template>

      <el-form :model="form" :rules="rules" ref="formRef" label-width="120px">
        <el-form-item label="Coupon Name" prop="name">
          <el-input v-model="form.name" placeholder="Enter coupon name"></el-input>
        </el-form-item>

        <el-form-item label="Coupon Type" prop="type">
          <el-radio-group v-model="form.type">
            <el-radio label="DISCOUNT">Discount</el-radio>
            <el-radio label="FULL_REDUCTION">Full Reduction</el-radio>
            <el-radio label="FIXED_AMOUNT">Fixed Amount</el-radio>
          </el-radio-group>
        </el-form-item>

        <el-form-item label="Value" prop="value">
          <el-input-number 
            v-model="form.value" 
            :min="0" 
            :max="form.type === 'DISCOUNT' ? 100 : 9999"
            :precision="2"
            :placeholder="getValuePlaceholder"
          ></el-input-number>
          <span class="input-tip">{{ getValueTip }}</span>
        </el-form-item>

        <el-form-item label="Min Spend" prop="minPrice">
          <el-input-number 
            v-model="form.minPrice" 
            :min="0" 
            :precision="2"
            placeholder="Minimum spend"
          ></el-input-number>
          <span class="input-tip">CNY (0 means no minimum)</span>
        </el-form-item>

        <el-form-item label="Total Quantity" prop="totalQuantity">
          <el-input-number 
            v-model="form.totalQuantity" 
            :min="1" 
            placeholder="Total quantity"
          ></el-input-number>
          <span class="input-tip">pcs</span>
        </el-form-item>

        <el-form-item label="Validity" prop="dateRange">
          <el-date-picker
            v-model="form.dateRange"
            type="datetimerange"
            range-separator="to"
            start-placeholder="Start"
            end-placeholder="End"
            format="YYYY-MM-DD HH:mm:ss"
            value-format="YYYY-MM-DD HH:mm:ss"
          >
          </el-date-picker>
        </el-form-item>

        <el-form-item label="Description" prop="description">
          <el-input 
            v-model="form.description" 
            type="textarea" 
            :rows="4"
            placeholder="Enter usage notes"
          ></el-input>
        </el-form-item>

        <el-form-item label="Status" prop="status">
          <el-radio-group v-model="form.status">
            <el-radio label="ACTIVE">Active</el-radio>
            <el-radio label="INACTIVE">Inactive</el-radio>
          </el-radio-group>
        </el-form-item>

        <el-form-item>
          <el-button type="primary" @click="handleSubmit" :loading="loading">
            {{ isEdit ? 'Save' : 'Create' }}
          </el-button>
          <el-button @click="goBack">Cancel</el-button>
        </el-form-item>
      </el-form>
    </el-card>
  </div>
</template>

<script setup>
import { ref, reactive, computed, onMounted } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { ElMessage } from 'element-plus'
import request from '@/utils/request'

const router = useRouter()
const route = useRoute()
const formRef = ref()
const loading = ref(false)

const isEdit = computed(() => !!route.params.id)

const form = reactive({
  name: '',
  type: 'DISCOUNT',
  value: 0,
  minPrice: 0,
  totalQuantity: 100,
  dateRange: [],
  description: '',
  status: 'ACTIVE'
})

const rules = {
  name: [
    { required: true, message: 'Please enter coupon name', trigger: 'blur' },
    { min: 2, max: 50, message: 'Length must be between 2 and 50 characters', trigger: 'blur' }
  ],
  type: [
    { required: true, message: 'Please select coupon type', trigger: 'change' }
  ],
  value: [
    { required: true, message: 'Please enter value', trigger: 'blur' }
  ],
  totalQuantity: [
    { required: true, message: 'Please enter total quantity', trigger: 'blur' }
  ],
  dateRange: [
    { required: true, message: 'Please select validity period', trigger: 'change' }
  ]
}

const getValuePlaceholder = computed(() => {
  switch (form.type) {
    case 'DISCOUNT':
      return 'Enter discount percent (1-100)'
    case 'FULL_REDUCTION':
      return 'Enter discount amount'
    case 'FIXED_AMOUNT':
      return 'Enter fixed amount'
    default:
      return 'Enter value'
  }
})

const getValueTip = computed(() => {
  switch (form.type) {
    case 'DISCOUNT':
      return 'Example: 85 means 85%'
    case 'FULL_REDUCTION':
      return 'CNY (minPrice triggers value off)'
    case 'FIXED_AMOUNT':
      return 'CNY'
    default:
      return ''
  }
})

const fetchCouponDetail = async () => {
  if (!isEdit.value) return

  try {
    const response = await request.get(`/api/merchant/coupons/${route.params.id}`)
    if (response.code === 200) {
      Object.assign(form, {
        name: response.data.name,
        type: response.data.type,
        value: response.data.value,
        minPrice: response.data.minPrice,
        totalQuantity: response.data.totalQuantity,
        dateRange: [response.data.startDate, response.data.endDate],
        description: response.data.description || '',
        status: response.data.status
      })
    } else {
      ElMessage.error(response.msg || 'Failed to load coupon')
    }
  } catch (error) {
    console.error('Failed to load coupon:', error)
    ElMessage.error('Failed to load coupon')
  }
}

const handleSubmit = async () => {
  if (!formRef.value) return

  await formRef.value.validate(async (valid) => {
    if (valid) {
      loading.value = true
      try {
        const data = {
          name: form.name,
          type: form.type,
          value: form.value,
          minPrice: form.minPrice,
          totalQuantity: form.totalQuantity,
          startDate: form.dateRange[0],
          endDate: form.dateRange[1],
          description: form.description,
          status: form.status
        }

        let response
        if (isEdit.value) {
          response = await request.put(`/api/merchant/coupons/${route.params.id}`, data)
        } else {
          response = await request.post('/api/merchant/coupons', data)
        }

        if (response.code === 200) {
          ElMessage.success(isEdit.value ? 'Updated successfully' : 'Created successfully')
          router.push('/merchant/coupons')
        } else {
          ElMessage.error(response.msg || 'Operation failed')
        }
      } catch (error) {
        console.error('Operation failed:', error)
        ElMessage.error('Operation failed')
      } finally {
        loading.value = false
      }
    }
  })
}

const goBack = () => {
  router.push('/merchant/coupons')
}

onMounted(() => {
  fetchCouponDetail()
})
</script>

<style scoped>
.coupon-form {
  padding: 20px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  font-size: 18px;
  font-weight: bold;
}

.el-form {
  max-width: 800px;
}

.input-tip {
  margin-left: 10px;
  color: #909399;
  font-size: 12px;
}
</style>
